package com.spider;

import com.spider.service.WechatAutoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class SpiderApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(SpiderApplication.class)
                .web(false)
                .run(args);
        WechatAutoService wechatAutoService = SpringContext.getBean("wechatAutoService");
        wechatAutoService.autoCrawl();
    }

}
