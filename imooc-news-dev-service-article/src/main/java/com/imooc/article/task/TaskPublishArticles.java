package com.imooc.article.task;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class TaskPublishArticles {
    @Scheduled(cron = "0/3 * * * * ?")
    public void publishArticles(){
        System.out.println("执行定时任务"+ LocalDateTime.now());
    }
}
