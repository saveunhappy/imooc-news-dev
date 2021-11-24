package com.imooc.article.html.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.CommentControllerApi;
import com.imooc.article.html.service.CommentPortalService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.CommentReplyBO;
import com.imooc.utils.PagedGridResult;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
public class CommentController extends BaseController implements CommentControllerApi {
    @Resource
    private CommentPortalService commentPortalService;
    @Override
    public GraceJSONResult createArticle(@Valid CommentReplyBO commentReplyBO) {
        //1.根据留言用户的id查询他的昵称，用于存到数据表进行字段的冗余处理，从而避免多表查询
        String userId = commentReplyBO.getCommentUserId();
        //2.发起restTemplate调用用户服务，获取用户昵称
        Set<String> idStr = new HashSet<>();
        idStr.add(userId);
        String nickname = getBasicUserList(idStr).get(0).getNickname();
        String face = getBasicUserList(idStr).get(0).getFace();
        //3.保存用户评论的消息到数据库
        commentPortalService.createComment(commentReplyBO.getArticleId(),
                commentReplyBO.getFatherId(),
                commentReplyBO.getContent(),
                userId,
                nickname,face);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult counts(String articleId) {
        Integer counts = getCountsFromRedis(REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId);

        return GraceJSONResult.ok(counts);
    }

    @Override
    public GraceJSONResult list(String articleId,
                                Integer page,
                                Integer pageSize) {
        if(page == null){
            page = COMMON_PAGE_SIZE;
        }
        if(pageSize == 0){
            pageSize = COMMON_PAGE_SIZE;
        }

        return GraceJSONResult.ok(commentPortalService.queryArticleComments(articleId,page,pageSize));
    }

    @Override
    public GraceJSONResult mng(String writerId, Integer page, Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = commentPortalService.queryWriterCommentsMng(writerId, page, pageSize);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult delete(String writerId, String commentId) {
        commentPortalService.deleteComment(writerId, commentId);
        return GraceJSONResult.ok();
    }
}
