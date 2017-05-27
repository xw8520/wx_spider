package com.spider.service;

import com.spider.utils.ChromeDriverUtils;
import com.spider.utils.UrlUtils;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
            Thread.sleep(5000);
            String source = driver.getPageSource();
            Document document = Jsoup.parse(source);
            Elements elements = document.getElementsByClass("weui_msg_card");
            if (elements == null || elements.isEmpty()) return;

            String fromId = "";

            Map<String, String> param = UrlUtils.parseUrl(url);
            if (!param.containsKey(BIZ)) return;
            String biz = param.get(BIZ);
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

    public void getJson(String url) {

    }
}
