package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleCoverType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
public class Articlecontroller extends BaseController implements ArticleControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(Articlecontroller.class);
    @Resource
    private ArticleService articleService;
    @Override
    public GraceJSONResult createArticle(@Valid NewArticleBO newArticleBO) {
        //判断文章封面类型，单图必填，纯文字则设置为空
        if(newArticleBO.getArticleType() == ArticleCoverType.ONE_IMAGE.type){
            if(StringUtils.isBlank(newArticleBO.getArticleCover())){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        }else if(newArticleBO.getArticleType() == ArticleCoverType.WORDS.type){
            newArticleBO.setArticleCover("");
        }
        //判断分类id是否存在
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);
        Category temp = null;
        if(StringUtils.isBlank(allCatJson)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }else {
            List<Category> categories = JsonUtils.jsonToList(allCatJson, Category.class);
            for (Category category : categories) {
                if(category.getId() == newArticleBO.getCategoryId()){
                    temp = category;
                    break;
                }
            }
            if(temp == null){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
            }
        }
        articleService.createArticle(newArticleBO,temp);
        logger.info(newArticleBO.toString());
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryMyList(String userId,
                                       String keyword,
                                       Integer status,
                                       Date startDate,
                                       Date endDate,
                                       Integer page,
                                       Integer pageSize) {
        if(page == null){
            page = COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult result = articleService.queryMyArticleList(userId, keyword, status, startDate, endDate, page, pageSize);

        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult queryAllList(Integer status, Integer page, Integer pageSize) {

        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = articleService.queryAllArticleListAdmin(status, page, pageSize);

        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult doReview(String articleId, Integer passOrNot) {
        Integer pedingStatus;
        if(passOrNot == YesOrNo.YES.type){
            //审核通过
            pedingStatus = ArticleReviewStatus.SUCCESS.type;
        }else if(passOrNot == YesOrNo.NO.type){
            //审核失败
            pedingStatus = ArticleReviewStatus.FAILED.type;
        }else{
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        //保存到数据库，更改文章的状态为审核成功或者失败
        articleService.updateArticleStatus(articleId,pedingStatus);
        return GraceJSONResult.ok();
    }
}
