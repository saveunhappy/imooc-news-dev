package com.imooc.article.controller;

import com.imooc.api.config.RabbitMQConfig;
import com.imooc.api.config.RabbitMQDelayConfig;
import com.imooc.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
@RequestMapping("producer")
public class HelloController{
    @Resource
    private RabbitTemplate template;
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    @GetMapping("/hello")
    public GraceJSONResult hello(){
//        template.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
//                "article.#.do",
//                "这是从生产者发送的消息");
        template.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
                "article.publish.download.do",
                "1002");
        template.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
                "article.success.do",
                "1003");
        template.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
                "article.play",
                "1004");
        return GraceJSONResult.ok();
    }

    @GetMapping("/delay")
    public GraceJSONResult delay(){
        MessagePostProcessor messagePostProcessor = message -> {
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            message.getMessageProperties().setDelay(5000);
            return message;
        };
        template.convertAndSend(RabbitMQDelayConfig.EXCHANGE_DELAY,
                "delay.hello",
                "这是一条延迟消息",
                messagePostProcessor);

        System.out.println("生产者发送的延迟消息"+ new Date());

        return GraceJSONResult.ok();
    }
}
