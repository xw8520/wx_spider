package com.spider.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.spider.model.Discuss;
import com.spider.model.WxNews;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by wq on 2017/4/3.
 */
@Service("wechatMassMsgService")
public class WechatMassMsgService {
    Logger logger = LoggerFactory.getLogger(WechatMassMsgService.class);

    //搜狗入口
    public void getByHtmlUnit(String url, String domain) {
        if (StringUtils.isEmpty(url)) return;
        if (domain == null) {
            //获取域名
        }
        if (!url.contains("http")) {
            url = domain + url;
        }
        try (WebClient webClient = new WebClient()) {
            HtmlPage page = webClient.getPage(url);
            Thread.sleep(3000);
            List<HtmlElement> elements = (List<HtmlElement>) page.getByXPath("//ul[@class='news-list']//li//div[@class='txt-box']//h3//a");
            if (elements != null) {
                for (HtmlElement item : elements) {
                    String newsUrl = item.getAttribute("href");
                    System.out.println(newsUrl);
                    getNewsFromUrl(newsUrl);
                }
            }
            //翻页
            HtmlElement next_page = page.getFirstByXPath("//a[@id='sogou_next']");
            if (next_page != null) {
                String href = next_page.getAttribute("href");
//                System.out.println(href);
                getByHtmlUnit(href, domain);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

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
                WxNews wxNews = getWxNewsByElement(el);
//                评论
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

    //获取文章信息
    private WxNews getWxNewsByElement(Element el) {
        WxNews wxNews = new WxNews();
        Element tmp = el.getElementById("post-user");
        if (tmp != null) {
            wxNews.setPost_user(tmp.html());
        }
        Elements elements = el.getElementsByClass("profile_meta_value");
        if (elements != null) {
            wxNews.setPost_value(elements.first().html());
        }
        tmp = el.getElementById("js_content");
        if (tmp != null) {
            wxNews.setContent(tmp.html());
        }
        return wxNews;
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
            HtmlPage page = webClient.getPage(url);
            Thread.sleep(3000);
            List<HtmlElement> elements = (List<HtmlElement>) page.getByXPath("//ul[@class='news-list']/li");
            for (HtmlElement el : elements) {
                HtmlElement a = el.getFirstByXPath("./div[@class='txt-box']/h3/a");
                if (a != null) {
                    System.out.println(a.getAttribute("href"));
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
