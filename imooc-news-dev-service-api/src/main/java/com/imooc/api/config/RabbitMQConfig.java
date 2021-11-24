package com.imooc.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    //定义交换机的名字
    public static final String EXCHANGE_ARTICLE = "exchange_article";

    //定义队列的名字
    public static final String QUEUE_DOWNLOAD_HTML = "queue_download_html";

    @Bean(EXCHANGE_ARTICLE)
    public Exchange exchange(){
        return ExchangeBuilder
                .topicExchange(EXCHANGE_ARTICLE)
                .durable(true)
                .build();
    }

    @Bean(QUEUE_DOWNLOAD_HTML)
    public Queue queue(){
        return new Queue(QUEUE_DOWNLOAD_HTML);
    }
    @Bean
    public Binding binding(
           @Qualifier(QUEUE_DOWNLOAD_HTML) Queue queue,
           @Qualifier(EXCHANGE_ARTICLE) Exchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("article.#.do")
                .noargs();//执行绑定
    }
}
