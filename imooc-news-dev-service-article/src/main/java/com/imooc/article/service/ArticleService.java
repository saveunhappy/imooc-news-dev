package com.imooc.article.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.PagedGridResult;

import java.util.Date;

public interface ArticleService {
    /**
     * 发布文章
     */
    void createArticle(NewArticleBO newArticleBO, Category category);

    /**
     * 更新定时发布为即时发布
     */
    void updateAppointToPublish();

    /**
     *    用户中心，查询我的文章列表
     */
    PagedGridResult queryMyArticleList(String userId,
                                       String keyword,
                                       Integer status,
                                       Date startDate,
                                       Date endDate,
                                       Integer page,
                                       Integer pageSize);

    /**
     *
     * 更改文字的状态
     * @param articleId
     * @param pendingStatus
     */
    void updateArticleStatus(String articleId, Integer pendingStatus);

    /**
     *
     * 关联文章和GridFS的html文件id
     */
    void updateArticleToGridFS(String articleId, String articleMongoId);

    /**
     * 管理员查询文章列表
     */
    PagedGridResult queryAllArticleListAdmin(Integer status, Integer page, Integer pageSize);
    /**
     * 删除文章
     */
    void deleteArticle(String userId, String articleId);

    /**
     * 撤回文章
     */
    void withdrawArticle(String userId, String articleId);
}
