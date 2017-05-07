package com.spider;

import com.spider.service.WechatMassMsgService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class SpiderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpiderApplication.class, args);
        String url = "https://mp.weixin.qq.com/s?__biz=MzA3MTQ4MDA2MA==&mid=2653054808&idx=1&sn=b80882cc68557dc8eace396e6166d06e&chksm=84fa20cbb38da9dd7bcdc352cb6d04763317737cd995cbd5b9bbfaf200593bd11126bcfb8c72&scene=42&key=27923f08b6373c165064862a771b8c2701aeb8d75b2fb0ff38c6fa47e3f75db49fb306706ce4836d2b143cec63290d175d436e2558debff6cdc6ad374e082fbd0fdad8150d4b0766eea6381417d03732&ascene=7&uin=NzE4NjM2Mzgx&devicetype=Windows+10&version=6204014f&pass_ticket=mcnwpKv4OBV5XU4iK45PWscTASXXjVh%2B06Z%2FJsyoJSXqAM0A%2Bw%2B6AzlKTvZFcYXV&winzoom=1";
        WechatMassMsgService wechatMassMsgService = SpringContext.getBean("wechatMassMsgService");

//        wechatMassMsgService.getBySelenuim(url);
        wechatMassMsgService.getByHtmlUnitV2(url);
    }

}
