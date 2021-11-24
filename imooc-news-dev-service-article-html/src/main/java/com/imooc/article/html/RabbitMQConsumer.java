package com.imooc.article.html;

import com.imooc.api.config.RabbitMQConfig;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {
    @RabbitListener(queues = {RabbitMQConfig.QUEUE_DOWNLOAD_HTML})
    public void watch(String payload, Message message){
        System.out.println(payload);
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        if(receivedRoutingKey.equalsIgnoreCase("article.publish.download.do")){
            System.out.println("1002");
        }else if (receivedRoutingKey.equalsIgnoreCase("article.success.do")){
            System.out.println("1003");
        }else{
            System.out.println("不符合规则");
        }
    }
}
