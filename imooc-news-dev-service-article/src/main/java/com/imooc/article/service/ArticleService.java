package com.imooc.article.service;

import com.imooc.pojo.AppUser;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.pojo.bo.UpdateUserInfoBO;

public interface ArticleService {
    /**
     * 发布文章
     */
    void createArticle(NewArticleBO newArticleBO, Category category);

}
