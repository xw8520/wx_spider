package com.spider.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.*;
import com.spider.model.Discuss;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wq on 2017/4/3.
 */
@Service("wechatMassMsgService")
public class WechatMassMsgService {
    Logger logger = LoggerFactory.getLogger(WechatMassMsgService.class);

    //获取文章内容
    public void getNewsFromUrl(String url) {
        try (WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage(url);
            Thread.sleep(3000);

            Document document = Jsoup.parse(page.asXml());
            Elements elements = document.getElementsByClass("discuss_item");
            List<Discuss> discusses = new ArrayList<>();
            for (Element el : elements) {
                //文章内容
                Discuss discuss = getDiscussByElement(el);
                if (discuss != null) {
                    Elements tmp = el.getElementsByClass("reply_result");
                    if (tmp != null && tmp.size() > 0) {
                        Discuss reply = getReplyByElement(tmp.first());
                        discuss.setReply_result(reply);
                    }
                    discusses.add(discuss);
                }
            }

            ObjectMapper mapper = new ObjectMapper();
            String content = mapper.writeValueAsString(discusses);
            writeToFile(content);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //获取评价
    private Discuss getDiscussByElement(Element el) {
        Discuss discuss = new Discuss();
        Elements tmp = el.getElementsByClass("praise_num");
        if (tmp != null && tmp.size() > 0) {
            discuss.setPraise_num(tmp.first().html());
        }
        tmp = el.getElementsByClass("nickname");
        if (tmp != null && tmp.size() > 0) {
            discuss.setNickname(tmp.first().html());
        }
        tmp = el.getElementsByClass("avatar");
        if (tmp != null && tmp.size() > 0) {
            discuss.setAvatar(tmp.first().html());
        }
        tmp = el.getElementsByClass("discuss_message_content");
        if (tmp != null && tmp.size() > 0) {
            discuss.setDiscuss_message_content(tmp.first().html());
        }
        tmp = el.getElementsByClass("discuss_extra_info");
        if (tmp != null && tmp.size() > 0) {
            discuss.setDiscuss_extra_info(tmp.first().html());
        }
        return discuss;
    }

    //获取作者回复
    private Discuss getReplyByElement(Element el) {
        Discuss reply = new Discuss();
        Elements tmp = el.getElementsByClass("praise_num");
        if (tmp != null && tmp.size() > 0) {
            reply.setPraise_num(tmp.first().html());
        }
        tmp = el.getElementsByClass("nickname");
        if (tmp != null && tmp.size() > 0) {
            reply.setNickname(tmp.first().html());
        }

        tmp = el.getElementsByClass("discuss_message_content");
        if (tmp != null && tmp.size() > 0) {
            reply.setDiscuss_message_content(tmp.first().html());
        }
        tmp = el.getElementsByClass("discuss_extra_info");
        if (tmp != null && tmp.size() > 0) {
            reply.setDiscuss_extra_info(tmp.first().html());
        }
        return reply;
    }

    //写入文件
    private void writeToFile(String content) {
        try {
            File newFile = new File(UUID.randomUUID() + ".json");
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            FileUtils.write(newFile, content, "utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getByHtmlUnitV2(String url) {
        try (WebClient webClient = new WebClient()) {
            WebRequest request = new WebRequest(new URL(url));

//            request.setAdditionalHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            request.setAdditionalHeader("Accept-Encoding", "zh-CN,zh;q=0.8");
//            request.setAdditionalHeader("Cache-Control","max-age=0");
//            request.setAdditionalHeader("Connection","keep-alive");
//            request.setAdditionalHeader("Accept-Language", "zh-CN,zh;q=0.8");
            request.setHttpMethod(HttpMethod.GET);
            request.setAdditionalHeader("Cookie", "wxtokenkey=a19f40a0b77ad106c03b696b323398c6dda6c83a6ec4d14b967d1092db809609; wxticket=4163818000; wxticketkey=7719cd3a84afc31743bc5dd2c3b7df57dda6c83a6ec4d14b967d1092db809609; wap_sid=CN2K1tYCEkBRUzZTdzZiODZ0X2l5a25VRWpPVzl4ekdFM3NTMHB5aUczOUZfeXdwckVOY0Flb1JTVmtQTHlWSmU0VGVnUWo4GAQg/BEo/KHMuAsw8oKIyAU=; wap_sid2=CN2K1tYCElxCMWg4TUtKeUE2YlZIU2hqQXFLM1NKUk02VEhzbVljNzRIOHBaMFB6VWRKNlZVSTgxWDBCUGhvQ0VJczM3SXlHMGMxR1RKd2ZySWRqSnRlY2dtdGpkWVlEQUFBfjDygojIBQ==");
            request.setAdditionalHeader("Host","mp.weixin.qq.com");
            request.setAdditionalHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36 MicroMessenger/6.5.2.501 NetType/WIFI WindowsWechat QBCore/3.43.373.400 QQBrowser/9.0.2524.400");
            HtmlPage page = webClient.getPage(request);
            Thread.sleep(5000);

            String body = page.asXml();
            File newFile = new File(UUID.randomUUID() + ".html");
            FileUtils.write(newFile, body, "utf-8");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
