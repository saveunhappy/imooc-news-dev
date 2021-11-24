package com.imooc.api.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQDelayConfig {
    //定义交换机的名字
    public static final String EXCHANGE_DELAY = "exchange_delay";

    //定义队列的名字
    public static final String QUEUE_DELAY = "queue_delay";

    @Bean(EXCHANGE_DELAY)
    public Exchange exchange(){
        return ExchangeBuilder
                .topicExchange(EXCHANGE_DELAY)
                .delayed()
                .durable(true)
                .build();
    }

    @Bean(QUEUE_DELAY)
    public Queue queue(){
        return new Queue(QUEUE_DELAY);
    }
    @Bean
    public Binding delayBinding(
           @Qualifier(QUEUE_DELAY) Queue queue,
           @Qualifier(EXCHANGE_DELAY) Exchange exchange){
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with("publish.delay.#")
                .noargs();//执行绑定
    }
}
