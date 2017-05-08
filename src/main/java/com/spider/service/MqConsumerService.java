package com.spider.service;

import com.spider.config.ConsumerConfig;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created by wq on 2017/5/8.
 */
@Component
@RabbitListener(queues = ConsumerConfig.WX_QUEUE_NAME)
public class MqConsumerService {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("Receiver : " + msg);
    }
}
