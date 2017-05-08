package com.spider;

import com.spider.config.ConsumerConfig;
import com.spider.repo.NewsMessageRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderApplicationTests {
    @Resource
    NewsMessageRepo newsMessageRepo;

    @Test
    public void contextLoads() {
    }

    @Resource
    RabbitTemplate rabbitTemplate;

//    @Test
//    public void testMq() {
//        rabbitTemplate.convertAndSend(ConsumerConfig.EXCHANGE_NAME,
//                ConsumerConfig.ROUTING_KEY, "this is a rabbitmq message");
//    }

//    @Test
//    public void testRepoInsert() {
//        NewsMessage news=new NewsMessage();
//        news.setComment("common");
//        news.setNamecn("中文名");
//        news.setNameen("nameen");
//        news.setCreatedate(new Date());
//        newsMessageRepo.save(news);
//    }
//
//    @Test
//    public void testRepoFindAll() {
//        Iterable<NewsMessage> list = newsMessageRepo.findAll();
//        Assert.assertNotNull(list);
//    }
}
