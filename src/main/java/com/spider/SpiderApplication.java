package com.spider;

import com.spider.service.WechatMassMsgService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class, args);
        String url = "http://weixin.sogou.com/weixin?type=2&ie=utf8&query=%E6%80%A7%E6%83%85%E5%90%9B&tsn=0&ft=null&et=null&interation=null&wxid=oIWsFt8zDNZe0mLskR1rKVHYsNUk&usip=nzjk39&from=tool";
        WechatMassMsgService wechatMassMsgService = SpringContext.getBean("wechatMassMsgService");

//        wechatMassMsgService.getBySelenuim(url);
        wechatMassMsgService.getByHtmlUnitV2(url);
    }

}
