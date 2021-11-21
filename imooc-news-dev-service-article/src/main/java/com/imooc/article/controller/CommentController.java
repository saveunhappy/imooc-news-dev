package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.CommentControllerApi;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.article.service.CommentPortalService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.CommentReplyBO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
public class CommentController extends BaseController implements CommentControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
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
        //3.保存用户评论的消息到数据库
        commentPortalService.createComment(commentReplyBO.getArticleId(),
                commentReplyBO.getFatherId(),
                commentReplyBO.getContent(),
                userId,
                nickname);
        return GraceJSONResult.ok();
    }
}
