package com.spider;

import com.spider.repo.NewsMessageRepo;
import com.spider.utils.UrlUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpiderApplicationTests {
    @Resource
    NewsMessageRepo newsMessageRepo;
    @Resource
    RabbitTemplate rabbitTemplate;

    @Test
    public void contextLoads() {
    }

//    @Test
//    public void testParseUrl() {
//        String url = "http://mp.weixin.qq.com/s?__biz=MzA5MDE5OTA5NQ==&mid=2663120339&idx=2&sn=31592bcf68e9c15ef92c59e466ad9ccd&chksm=8b40188bbc37919d12912315ebc52e14824fb56bef69883a7e5eb9c1690e785c1753b71a5ba6&scene=0&ascene=7&devicetype=android-22&version=26050733&nettype=WIFI&abtest_cookie=AAACAA%3D%3D&pass_ticket=4%2F5qxNq2T3MtyXl52%2BriLNas8Oj3SKShwijb%2FfNNg4CowAnWZ%2BH4gCQtRo%2F52IT%2F&wx_header=1";
//        Map<String, String> map = UrlUtils.parseUrl(url);
//        Assert.assertFalse(map.isEmpty());
//        Set<String> keys = map.keySet();
//        for (String key : keys) {
//            System.out.println(key+"="+map.get(key));
//        }
//    }

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
