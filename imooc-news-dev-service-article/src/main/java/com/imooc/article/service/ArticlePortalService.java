package com.imooc.article.service;

import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.PagedGridResult;

import java.util.Date;
import java.util.List;

public interface ArticlePortalService {


    /**
     *    用户中心，查询我的文章列表
     */
    PagedGridResult queryIndexArticleList(String keyword,
                                       Integer category,
                                       Integer page,
                                       Integer pageSize);

    /**
     * 首页查询热闻列表
     */
    public List<Article> queryHotList();
}
