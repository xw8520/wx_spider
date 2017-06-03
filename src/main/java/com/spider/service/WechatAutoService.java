package com.spider.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.spider.utils.ChromeDriverUtils;
import com.spider.utils.JsonUtils;
import com.spider.utils.UrlUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by wq on 2017/5/27.
 */
@Service
public class WechatAutoService {
    public final static String BIZ = "__biz";
    public final static String FROMMSGID = "frommsgid";
    public final static String UIN = "uin";
    public final static String KEY = "key";
    public final static String PASS_TICKET = "pass_ticket";
    public final static String MID = "mid";
    public final static String IDX = "idx";
    public final static String WXTOKEN = "wxtoken";
    public final static String jsonBaseUrl = "https://mp.weixin.qq.com/mp/profile_ext?action=getmsg&__biz=%s&f=json&frommsgid=%s&count=10&scene=124&is_ok=1&uin=%s&key=%s&pass_ticket=%s&wxtoken=&x5=0&f=json";
    @Resource
    WechatMassMsgService wechatMassMsgService;

    public void autoCrawl() {

        File file = new File("autoUrl.txt");
        try (InputStream is = new FileInputStream(file);
             InputStreamReader isReader = new InputStreamReader(is, Charset.forName("utf-8"))) {
            List<String> lines = IOUtils.readLines(isReader);
            if (lines == null || lines.isEmpty()) return;
            String url = lines.get(0);
            if (StringUtils.isEmpty(url)) return;
            getPage(url);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getPage(String url) {

        try {
            ChromeDriver driver = ChromeDriverUtils.getChromeDriver();
            driver.get(url);
            Thread.sleep(3000);
            String source = driver.getPageSource();
            Document document = Jsoup.parse(source);
            Elements elements = document.getElementsByClass("weui_media_box appmsg");
            if (elements == null || elements.isEmpty()) return;

            getPageContentBatch(elements);

            String fromId = getFrommsgid(elements);
            Map<String, String> param = UrlUtils.parseUrl(url);
            if (!param.containsKey(BIZ)) return;
            String biz = param.get(BIZ) + "==";
            if (!param.containsKey(UIN)) return;
            String uin = param.get(UIN);
            if (!param.containsKey(KEY)) return;
            String key = param.get(KEY);
            if (!param.containsKey(PASS_TICKET)) return;
            String passTicket = param.get(PASS_TICKET);
            String jsonUrl = String.format(jsonBaseUrl, biz, fromId, uin, key, passTicket);
            getJson(jsonUrl);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getFrommsgid(Elements elements) {
        int len = elements.size();
        Element element = elements.get(len - 1);
        return element.attr("msgid");
    }

    public void getPageContentBatch(Elements elements) {

    }

    public void getPageContent(String url) {

    }

    public void getPageContent(String url, String cover) {

    }

    public void getJson(String url) {
        ChromeDriver driver = ChromeDriverUtils.getChromeDriver();
        driver.get(url);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String source = driver.getPageSource();
        Document document = Jsoup.parse(source);
        Elements pre = document.getElementsByTag("pre");
        if (pre == null) return;
        String json = pre.get(0).html();
        JsonNode root = JsonUtils.readToNode(json);
        JsonNode generalMsgList = root.get("general_msg_list");
        if (generalMsgList == null) return;
        JsonNode jsonRoot = JsonUtils.readToNode(generalMsgList.textValue());
        if (jsonRoot == null) return;
        JsonNode list = jsonRoot.get("list");
        if (list == null) return;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            JsonNode f = list.get(i);
            JsonNode appMsgExtInfo = f.get("app_msg_ext_info");

            if (appMsgExtInfo != null) {
                getNewsFromJson(appMsgExtInfo);

                JsonNode multiMsgList = appMsgExtInfo.get("multi_app_msg_item_list");
                if (multiMsgList != null) {
                    for (int j = 0, len = multiMsgList.size(); j < len; j++) {
                        getNewsFromJson(multiMsgList.get(j));
                    }
                }
            }

        }


    }

    public void getNewsFromJson(JsonNode jsonNode) {

    }
}
