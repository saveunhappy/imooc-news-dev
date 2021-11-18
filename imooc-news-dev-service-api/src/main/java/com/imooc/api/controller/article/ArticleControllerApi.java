package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Date;

@Api(value = "文章业务的controller", tags = {"文章业务的controller"})
@RequestMapping("article")
public interface ArticleControllerApi {


    @ApiOperation(value = "用户发文", notes = "用户发文", httpMethod = "POST")
    @PostMapping("/createArticle")
    GraceJSONResult createArticle(@RequestBody @Valid NewArticleBO newArticleBO);

    @ApiOperation(value = "查询用户的所欲文章列表", notes = "admin登录接口", httpMethod = "POST")
    @PostMapping("/queryMyList")
    GraceJSONResult queryMyList(@RequestParam String userId,
                                @RequestParam String keyword,
                                @RequestParam Integer status,
                                @RequestParam Date startDate,
                                @RequestParam Date endDate,
                                @RequestParam Integer page,
                                @RequestParam Integer pageSize);

}