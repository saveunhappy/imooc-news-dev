package com.imooc.article;

import com.imooc.api.config.RabbitMQDelayConfig;
import com.imooc.article.service.ArticleService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class RabbitMQDelayConsumer {
    @Resource
    private ArticleService articleService;
    @RabbitListener(queues = {RabbitMQDelayConfig.QUEUE_DELAY})
    public void watch(String payload, Message message){
        System.out.println(payload);
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        System.out.println(receivedRoutingKey);
        System.out.println("消费者接收的延迟消息"+ new Date());
        String articleId = payload;
        articleService.updateArticleToPublish(articleId);
    }
}
