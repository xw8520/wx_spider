package com.spider.service;

import com.spider.model.NewsMessageInfo;
import com.spider.utils.JsonUtils;
import com.spider.utils.UrlUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by wq on 2017/5/8.
 */
@Service
public class MqConsumerService {
    private Logger logger = LoggerFactory.getLogger(MqConsumerService.class);
    @Resource
    WechatMassMsgService wechatMassMsgService;

    public void process(String msg) {
        try {
            NewsMessageInfo newsInfo = JsonUtils.from(msg, NewsMessageInfo.class);
            if (newsInfo == null) {
                System.out.println("newsInfo is empty");
                return;
            }
            if (StringUtils.isBlank(newsInfo.getUrl())) {
                System.out.println("url is empty");
                return;
            }
            String sn = UrlUtils.parseUrl(newsInfo.getUrl()).get("sn");
            if (newsInfo.getType() == 0) {
                wechatMassMsgService.parseAndSave(newsInfo.getBody(),"",sn);
            }
            if (newsInfo.getType() == 1) {
                wechatMassMsgService.updateViews(sn, newsInfo.getBody());
            }
            if (newsInfo.getType() == 2) {
                wechatMassMsgService.updateComment(sn, newsInfo.getBody());
            }
        } catch (Exception ex) {
            logger.error("MqConsumerService.process",ex);
        }
    }
}
