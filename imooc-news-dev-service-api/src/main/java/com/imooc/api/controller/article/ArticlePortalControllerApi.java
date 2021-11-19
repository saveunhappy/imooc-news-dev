package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Api(value = "门户端文章业务的controller", tags = {"门户端文章业务的controller"})
@RequestMapping("/portal/article")
public interface ArticlePortalControllerApi {


    @GetMapping("/list")
    @ApiOperation(value = "首页查询文章列表", notes = "首页查询文章列表", httpMethod = "GET")
    GraceJSONResult list(@RequestParam String keyword,
                         @RequestParam Integer category,
                         @ApiParam(name = "page", value = "查询下一页的第几页")
                         @RequestParam Integer page,
                         @ApiParam(name = "pageSize", value = "分页的每一页显示的条数")
                         @RequestParam Integer pageSize);
    @GetMapping("hotList")
    @ApiOperation(value = "首页查询热闻列表", notes = "首页查询热闻列表", httpMethod = "GET")
    GraceJSONResult hotList();
}
