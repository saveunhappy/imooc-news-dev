package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.CommentReplyBO;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Api(value = "评论相关业务的controller", tags = {"评论相关业务的controller"})
@RequestMapping("comment")
public interface CommentControllerApi {


    @ApiOperation(value = "用户评论", notes = "用户评论", httpMethod = "POST")
    @PostMapping("/createComment")
    GraceJSONResult createArticle(@RequestBody @Valid CommentReplyBO commentReplyBO);

    @ApiOperation(value = "用户评论数查询", notes = "用户评论数查询", httpMethod = "GET")
    @GetMapping("/counts")
    GraceJSONResult count(@RequestParam String articleId);
}
