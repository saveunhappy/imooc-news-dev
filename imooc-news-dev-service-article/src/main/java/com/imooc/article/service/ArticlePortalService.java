package com.imooc.article.service;

import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.pojo.vo.ArticleDetailVO;
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
    List<Article> queryHotList();

    /**
     * 查询作家发布的所有文章列表
     */
    PagedGridResult queryArticleListOfWriter(String writerId,
                                             Integer page,
                                             Integer pageSize);

    /**
     * 作家页面查询近期佳文
     */
    PagedGridResult queryGoodArticleListOfWriter(String writerId);

    /**
     * 查询文章详情
     */
    ArticleDetailVO queryDetail(String articleId);
}
