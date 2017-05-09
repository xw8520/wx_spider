package com.spider.service;

import org.springframework.stereotype.Service;

/**
 * Created by wq on 2017/5/8.
 */
@Service
public class MqConsumerService {

    public void process(String msg) {
        System.out.println(msg);
    }
}
