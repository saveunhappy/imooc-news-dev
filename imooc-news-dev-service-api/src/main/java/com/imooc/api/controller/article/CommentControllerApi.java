package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.CommentReplyBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "评论相关业务的controller", tags = {"评论相关业务的controller"})
@RequestMapping("comment")
public interface CommentControllerApi {


    @ApiOperation(value = "用户评论", notes = "用户评论", httpMethod = "POST")
    @PostMapping("/createComment")
    GraceJSONResult createArticle(@RequestBody @Valid CommentReplyBO commentReplyBO);

    @ApiOperation(value = "用户评论数查询", notes = "用户评论数查询", httpMethod = "GET")
    @GetMapping("/counts")
    GraceJSONResult counts(@RequestParam String articleId);

    @ApiOperation(value = "查询文章的所有评论列表", notes = "查询文章的所有评论列表", httpMethod = "GET")
    @GetMapping("/list")
    GraceJSONResult list(@RequestParam String articleId,@RequestParam Integer page,@RequestParam Integer pageSize);
}
