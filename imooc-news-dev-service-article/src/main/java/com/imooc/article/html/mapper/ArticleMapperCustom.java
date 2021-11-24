package com.imooc.article.html.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Article;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapperCustom extends MyMapper<Article> {
    void updateAppointToPublish();
}