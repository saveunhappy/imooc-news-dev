package com.imooc.article.html.controller;

import com.imooc.api.config.RabbitMQConfig;
import com.imooc.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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
}
