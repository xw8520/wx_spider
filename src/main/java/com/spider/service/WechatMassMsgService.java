package com.spider.service;

import com.spider.domain.NewsMessage;
import com.spider.repo.NewsMessageRepo;
import com.spider.utils.EncryptUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * Created by wq on 2017/4/3.
 */
@Service
public class WechatMassMsgService {
    @Resource
    private NewsMessageRepo newsMessageRepo;
    @Value("${file.path}")
    String basePath;

    public void parseAndSave(String source) {
        parseAndSave(source, "", "");
    }


    public void parseAndSave(String source, String cover, String sn) {
        NewsMessage news = new NewsMessage();
        Document document = Jsoup.parse(source);
        //保存图片到本地
        document = saveImage(document);
        //保存语音
        document = getVoice(document);
        Element postDate = document.getElementById("post-date");
        Element postUser = document.getElementById("post-user");
        Elements metaValue = document.getElementsByClass("profile_meta_value");
        Element title = document.getElementById("activity-name");
        if (postDate != null) {
            LocalDate date = LocalDate.parse(postDate.html());
            news.setPostdate(date.toDate());
        }
        if (postUser != null) {
            news.setNamecn(postUser.html());
        }
        if (metaValue != null && metaValue.size() > 0) {
            news.setNameen(metaValue.get(0).html());
        }
        if (title != null) {
            String titleStr = title.html();
            news.setTitle(titleStr);
            //使用title+nameen作为文章标识
            news.setSum(EncryptUtils.md5(titleStr + news.getNameen()));
        }
        news.setSn("");

        String html = document.html();
        html = Base64Utils.encodeToString(html.getBytes(Charset.forName("utf-8")));
        news.setMainbody(html);
        news.setCreatedate(new Date());
        news.setCover(cover);
        news.setSn(sn);
        newsMessageRepo.save(news);
    }

    public Document saveImage(Document document) {
        List<Element> innerImg = document.getElementsByTag("img");
        if (innerImg != null) {
            for (Element img : innerImg) {
                try {
                    String imgUrl = img.attr("data-src");
                    if (StringUtils.isEmpty(imgUrl)) {
                        imgUrl = img.attr("src");
                        if (StringUtils.isEmpty(imgUrl)) continue;
                        if (imgUrl.contains("javascript")) continue;
                    }
                    if (!imgUrl.contains("http")) {
                        imgUrl = "https:" + imgUrl;
                    }
                    if (imgUrl.contains("icon_loading_white2805ea.gif")) {
                        continue;
                    }
                    System.out.println(imgUrl);
                    InputStream in = new URL(imgUrl).openStream();
                    String suffix = imgUrl.contains("wx_fmt=gif") ? ".gif" : ".png";
                    byte[] buffer = IOUtils.toByteArray(in);
                    File pathFile = new File(basePath);
                    if (!pathFile.exists()) {
                        pathFile.mkdir();
                    }
                    String newImgName = UUID.randomUUID() + suffix;
                    FileUtils.writeByteArrayToFile(new File(basePath + File.separator + newImgName), buffer);
                    IOUtils.closeQuietly(in);
                    img.removeAttr("data-src");
                    img.attr("src", newImgName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

        return document;
    }

    private Document getVoice(Document document) {
        Elements el = document.getElementsByTag("mpvoice");
        if (!el.isEmpty()) {
            Element voice = el.get(0);
            String mediaId = voice.attr("voice_encode_fileid");
            String url = "https://res.wx.qq.com/voice/getvoice?mediaid=" + mediaId;
            HttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            try {
                String fileName = UUID.randomUUID() + ".mp3";

                HttpResponse resp = client.execute(get);
                byte[] buffer = EntityUtils.toByteArray(resp.getEntity());
                File newFile = new File(basePath + fileName);
                FileUtils.writeByteArrayToFile(newFile, buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return document;
    }

    @Transactional
    public void updateComment(String sn, String body) {
        NewsMessage news = newsMessageRepo.findBySn(sn);
        if (news != null) {
            news.setComment(body);
            newsMessageRepo.save(news);
        }
    }

    @Transactional
    public void updateViews(String sn, String body) {
        NewsMessage news = newsMessageRepo.findBySn(sn);
        if (news != null) {
            news.setViews(body);
            newsMessageRepo.save(news);
        }
    }
}
