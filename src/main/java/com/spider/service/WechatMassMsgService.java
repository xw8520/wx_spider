package com.spider.service;

import com.spider.domain.NewsMessage;
import com.spider.repo.NewsMessageRepo;
import com.spider.utils.EncryptUtils;
import org.joda.time.LocalDate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;


/**
 * Created by wq on 2017/4/3.
 */
@Service
public class WechatMassMsgService {
    @Resource
    private NewsMessageRepo newsMessageRepo;

    public void parseAndSave(String sn, String source) {
        NewsMessage news = new NewsMessage();
        Document document = Jsoup.parse(source);
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
            news.setSum(EncryptUtils.md5(titleStr));
        }
        news.setSn(sn);
        news.setCreatedate(new Date());
        newsMessageRepo.save(news);
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
