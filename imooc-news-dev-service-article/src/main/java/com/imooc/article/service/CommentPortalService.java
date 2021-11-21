package com.imooc.article.service;

import com.imooc.pojo.Article;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface CommentPortalService {


    /**
     *    发表评论
     */
    void createComment(String articleId,
                                          String fatherCommentId,
                                          String content,
                                          String userId,
                                          String nickname
                                      );


}
