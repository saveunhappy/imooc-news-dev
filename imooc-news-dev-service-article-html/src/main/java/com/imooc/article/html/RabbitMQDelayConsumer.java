package com.imooc.article.html;

import com.imooc.api.config.RabbitMQConfig;
import com.imooc.api.config.RabbitMQDelayConfig;
import com.imooc.article.controller.ArticleHTMLComponent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class RabbitMQDelayConsumer {

//    @RabbitListener(queues = {RabbitMQDelayConfig.QUEUE_DELAY})
//    public void watch(String payload, Message message){
//        System.out.println(payload);
//        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
//        System.out.println(receivedRoutingKey);
//        System.out.println("消费者接收的延迟消息"+ new Date());
//    }
}
