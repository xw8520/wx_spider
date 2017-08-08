package com.spider.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.spider.utils.ChromeDriverUtils;
import com.spider.utils.JsonUtils;
import com.spider.utils.UrlUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    @Value("${file.path}")
    String basePath;

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
            String fromId = getFrommsgid(document);
            //爬取第一页
            getPageContentBatch(elements);
            //爬取下一页json
            String jsonUrl = getJsonUrl(url, fromId);
            getJson(jsonUrl);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getFrommsgid(Document document) {
        Elements jsCard = document.getElementsByClass("weui_msg_card js_card");
        if (jsCard != null && !jsCard.isEmpty()) {
            Element msgIdEl = jsCard.get(jsCard.size() - 1);
            return msgIdEl.attr("msgid");
        }

        return "";
    }

    public void getPageContentBatch(Elements elements) {
        for (Element element : elements) {
            Elements tmp = element.getElementsByTag("span");
            if (tmp == null) continue;
            String imgUrl = tmp.get(0).attr("style");
            Pattern pattern = Pattern.compile("(?<=\\()[^\\)]+");
            Matcher matcher = pattern.matcher(imgUrl);
            while (matcher.find()) {
                imgUrl = matcher.group();
            }
            getPageContent(tmp.get(0).attr("hrefs"), imgUrl);
        }
    }

    public void getPageContent(String url, String cover) {
        if (StringUtils.isEmpty(url)) return;

        try {
            ChromeDriver driver = ChromeDriverUtils.getChromeDriver();
            driver.get(url);
            Thread.sleep(3000);
            Map<String, String> params = UrlUtils.parseUrl(url);
            if (params.containsKey("sn")) {
                String source = driver.getPageSource();
                wechatMassMsgService.parseAndSave(source, cover, params.get("sn"));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getJsonUrl(String beforeUrl, String fromId) {
        Map<String, String> param = UrlUtils.parseUrl(beforeUrl);
        if (!param.containsKey(BIZ)) return "";
        String biz = param.get(BIZ) + "==";
        if (!param.containsKey(UIN)) return "";
        String uin = param.get(UIN);
        if (!param.containsKey(KEY)) return "";
        String key = param.get(KEY);
        if (!param.containsKey(PASS_TICKET)) return "";
        String passTicket = param.get(PASS_TICKET);
        String jsonUrl = String.format(jsonBaseUrl, biz, fromId, uin, key, passTicket);
        return jsonUrl;
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
        JsonNode canContinue = root.get("can_msg_continue");
        boolean finished = false;
        if (canContinue != null) {
            finished = canContinue.intValue() == 0;
        }
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
            if (finished) return;
            //读取下一页json数据
            if (i == size - 1) {
                Map<String, String> map = UrlUtils.parseUrl(url);
                if (map.containsKey(FROMMSGID)) {
                    String fromId = map.get(FROMMSGID);
                    String newUrl = getJsonUrl(url, String.valueOf(Integer.parseInt(fromId) - 10));
                    getJson(newUrl);
                }
            }
        }
    }

    public void getNewsFromJson(JsonNode jsonNode) {
        String title = "";
        String url = "";
        String cover = "";
        JsonNode urlNode = jsonNode.get("content_url");
        if (urlNode != null) {
            url = urlNode.textValue().replace("amp;", "");
        }
        JsonNode coverNode = jsonNode.get("cover");
        if (coverNode != null) {
            cover = coverNode.textValue().replace("amp;", "");
        }
        getPageContent(url, cover);
    }
}
