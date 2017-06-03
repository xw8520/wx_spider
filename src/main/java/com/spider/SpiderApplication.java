package com.spider;

import com.spider.service.WechatAutoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class, args);

        WechatAutoService wechatAutoService = SpringContext.getBean("wechatAutoService");
        wechatAutoService.autoCrawl();
    }

}
