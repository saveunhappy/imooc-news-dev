package com.imooc.article.html;

import org.springframework.stereotype.Component;

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
