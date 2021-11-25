package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.article.service.CommentPortalService;
import com.imooc.article.mapper.CommentsMapper;
import com.imooc.article.mapper.CommentsMapperCustom;
import org.n3r.idworker.Sid;
import com.imooc.pojo.Comments;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.pojo.vo.CommentsVO;
import com.imooc.utils.PagedGridResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentPortalServiceImpl extends BaseService implements CommentPortalService {
    @Resource
    private Sid sid;
    @Resource
    private ArticlePortalService articlePortalService;
    @Resource
    private CommentsMapper commentsMapper;
    @Resource
    private CommentsMapperCustom commentsMapperCustom;

    @Override
    public void createComment( String articleId, String fatherCommentId, String content, String userId, String nickname,String face) {
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
        comments.setCommentUserFace(face);
        comments.setCreateTime(new Date());
        commentsMapper.insert(comments);
        redis.increment(REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId,1);
    }

    @Override
    public PagedGridResult queryArticleComments(String articleId, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>();
        map.put("articleId",articleId);
        PageHelper.startPage(page,pageSize);
        List<CommentsVO> list = commentsMapperCustom.queryArticleCommentList(map);
        return setterPagedGrid(list,page);
    }
    @Override
    public PagedGridResult queryWriterCommentsMng(String writerId, Integer page, Integer pageSize) {

        Comments comment = new Comments();
        comment.setWriterId(writerId);

        PageHelper.startPage(page, pageSize);
        List<Comments> list = commentsMapper.select(comment);
        return setterPagedGrid(list, page);
    }

    @Override
    public void deleteComment(String writerId, String commentId) {
        Comments comment = new Comments();
        comment.setId(commentId);
        comment.setWriterId(writerId);

        commentsMapper.delete(comment);
    }
}
