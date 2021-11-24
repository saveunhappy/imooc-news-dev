package com.imooc.article.task;

import com.imooc.article.service.ArticleService;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

//@Configuration
//@EnableScheduling
public class TaskPublishArticles {
    @Resource
    private ArticleService articleService;
    @Scheduled(cron = "0/3 * * * * ?")
    public void publishArticles(){
        articleService.updateAppointToPublish();
    }
}
