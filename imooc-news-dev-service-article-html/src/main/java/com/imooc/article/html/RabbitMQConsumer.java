package com.imooc.article.html;

import com.imooc.api.config.RabbitMQConfig;
import com.imooc.article.controller.ArticleHTMLComponent;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class RabbitMQConsumer {
    @Resource
    private ArticleHTMLComponent articleHTMLComponent;

    @RabbitListener(queues = {RabbitMQConfig.QUEUE_DOWNLOAD_HTML})
    public void watch(String payload, Message message){
        System.out.println(payload);
        String receivedRoutingKey = message.getMessageProperties().getReceivedRoutingKey();
        if(receivedRoutingKey.equalsIgnoreCase("article.publish.download.do")){
            System.out.println("1002");
        }else if (receivedRoutingKey.equalsIgnoreCase("article.success.do")){
            System.out.println("1003");
        }else if (receivedRoutingKey.equalsIgnoreCase("article.download.do")){
            String articleId = payload.split(",")[0];
            String articleMongoId = payload.split(",")[1];
            articleHTMLComponent.download(articleId,articleMongoId);


        }else{
            System.out.println("不符合规则");
        }
    }
}
