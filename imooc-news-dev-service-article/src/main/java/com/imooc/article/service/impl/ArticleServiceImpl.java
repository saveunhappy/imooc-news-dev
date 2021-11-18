package com.imooc.article.service.impl;

import com.imooc.api.service.BaseService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.org.n3r.idworker.Sid;
import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private Sid sid;
    @Transactional
    @Override
    public void createArticle(NewArticleBO newArticleBO, Category category) {
        String articleId = sid.nextShort();
        Article article = new Article();
        BeanUtils.copyProperties(newArticleBO,article);
        article.setId(articleId);
        article.setCategoryId(category.getId());
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setCommentCounts(0);
        article.setReadCounts(0);

        article.setIsDelete(YesOrNo.NO.type);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        if(article.getIsAppoint() == ArticleAppointType.TIMING.type){
            article.setPublishTime(article.getPublishTime());
        }else if(article.getIsAppoint() == ArticleAppointType.IMMEDIATELY.type){
            article.setPublishTime(new Date());
        }
        int result = articleMapper.insert(article);
        if(result != 1){
            GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
    }
}
