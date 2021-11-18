package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleCoverType;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class Articlecontroller extends BaseController implements ArticleControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(Articlecontroller.class);

    @Override
    public GraceJSONResult adminLogin(@Valid NewArticleBO newArticleBO) {
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
        if(StringUtils.isBlank(allCatJson)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }else {
            List<Category> categories = JsonUtils.jsonToList(allCatJson, Category.class);
            Category temp = null;
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

        logger.info(newArticleBO.toString());
        return GraceJSONResult.ok();
    }
}
