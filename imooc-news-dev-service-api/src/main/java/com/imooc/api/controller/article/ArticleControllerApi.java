package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "文章业务的controller",tags = {"文章业务的controller"})
@RequestMapping("article")
public interface ArticleControllerApi {


    @ApiOperation(value = "admin登录接口",notes = "admin登录接口",httpMethod = "POST")
    @PostMapping("/createArticle")
    GraceJSONResult adminLogin(@RequestBody @Valid NewArticleBO newArticleBO);

}
