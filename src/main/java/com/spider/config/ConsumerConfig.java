package com.spider.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wq on 2017/5/8.
 */
@Configuration
public class ConsumerConfig {
    public static final String WX_QUEUE_NAME = "wx_queue_name_1";
    public final static String EXCHANGE_NAME = "wx_exchange_1";
    public final static String ROUTING_KEY = "wx_routingkey_1";
    @Value("${rabbitmq.host}")
    String host;
    @Value("${rabbitmq.port}")
    Integer port;
    @Value("${rabbitmq.username}")
    String userName;
    @Value("${rabbitmq.password}")
    String password;

    @Bean
    public Queue queue() {
        return new Queue(WX_QUEUE_NAME, true);
    }

    @Bean
    public TopicExchange defaultExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(defaultExchange()).with(ROUTING_KEY);
    }
}
