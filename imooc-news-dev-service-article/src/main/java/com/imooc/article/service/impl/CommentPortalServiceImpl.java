package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.mapper.CommentsMapper;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.article.service.CommentPortalService;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.org.n3r.idworker.Sid;
import com.imooc.pojo.Article;
import com.imooc.pojo.Comments;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CommentPortalServiceImpl extends BaseService implements CommentPortalService {
    @Resource
    private Sid sid;
    @Resource
    private ArticlePortalService articlePortalService;
    @Resource
    private CommentsMapper commentsMapper;
    @Override
    public void createComment(String articleId, String fatherCommentId, String content, String userId, String nickname) {
        String commentId = sid.nextShort();
        ArticleDetailVO articleDetailVO = articlePortalService.queryDetail(articleId);
        Comments comments = new Comments();
        comments.setId(commentId);
        comments.setWriterId(articleDetailVO.getPublishUserId());
        comments.setArticleTitle(articleDetailVO.getTitle());
        comments.setArticleCover(articleDetailVO.getCover());
        comments.setArticleId(articleId);
        comments.setFatherId(fatherCommentId);
        comments.setCommentUserId(userId);
        comments.setCommentUserNickname(nickname);
        comments.setContent(content);
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
        redis.increment(REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId,1);
    }
}
