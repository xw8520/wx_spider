package com.spider.config;

import com.spider.service.MqConsumerService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by wq on 2017/5/8.
 */
@Configuration
public class ConsumerConfig {
    public static final String WX_QUEUE_NAME = "wx_news_spider_queue";
    @Value("${rabbitmq.host}")
    String host;
    @Value("${rabbitmq.port}")
    Integer port;
    @Value("${rabbitmq.username}")
    String userName;
    @Value("${rabbitmq.password}")
    String password;

    /**
     * 配置消息队列2
     * 针对消费者配置
     *
     * @return
     */
    @Bean
    public Queue queue() {
        return new Queue(WX_QUEUE_NAME, false);

    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory,
                                                           MqConsumerService mqConsumerService) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue());
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL); //设置确认模式手工确认
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            byte[] body = message.getBody();
            mqConsumerService.process(new String(body));

            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false); //确认消息成功消费
        });
        return container;
    }

    @Bean
    MessageListenerAdapter messageListenerAdapter() {
        return new MessageListenerAdapter();
    }
}
