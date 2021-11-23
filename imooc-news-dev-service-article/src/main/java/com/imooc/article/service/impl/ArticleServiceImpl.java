package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.mapper.ArticleMapperCustom;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleReviewLevel;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.org.n3r.idworker.Sid;
import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private ArticleMapperCustom articleMapperCustom;
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
        //TODO 通过阿里智能AI实现对文章文本的自动检测
        String reviewTextResult = ArticleReviewLevel.REVIEW.type;
        if(reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.PASS.type)){
            //修改当前的文章，状态标记为审核通过
            this.updateArticleStatus(articleId,ArticleReviewStatus.SUCCESS.type);
        }else if(reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.REVIEW.type)){
            //修改当前文章，状态标记为需要人工审核
            this.updateArticleStatus(articleId,ArticleReviewStatus.WAITING_MANUAL.type);
        }else if(reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.BLOCK.type)){
            //修改当前文章，状态标记为审核未通过
            this.updateArticleStatus(articleId,ArticleReviewStatus.FAILED.type);

        }
    }
    @Transactional
    @Override
    public void updateAppointToPublish() {
        articleMapperCustom.updateAppointToPublish();
    }

    @Override
    public PagedGridResult queryMyArticleList(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("publishUserId",userId);
        if(StringUtils.isNotBlank(keyword)){
            criteria.andLike("title","%" + keyword + "%");
        }
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus",status);
        }
        if(status != null && status == 12){
            criteria.andEqualTo("articleStatus",ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus",ArticleReviewStatus.WAITING_MANUAL.type);
        }
        criteria.andEqualTo("isDelete",YesOrNo.NO.type);
        if(startDate != null){
            criteria.andGreaterThanOrEqualTo("publishTime",startDate);
        }
        if(endDate != null){
            criteria.andLessThanOrEqualTo("publishTime",endDate);
        }
        PageHelper.startPage(page,pageSize);
        List<Article> articles = articleMapper.selectByExample(example);

        return setterPagedGrid(articles,page);
    }
    @Transactional
    @Override
    public void updateArticleToGridFS(String articleId, String articleMongoId) {
        Article article = new Article();
        article.setId(articleId);
        article.setMongoFileId(articleMongoId);
        articleMapper.updateByPrimaryKeySelective(article);
    }

    @Transactional
    @Override
    public void updateArticleStatus(String articleId, Integer pendingStatus) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",articleId);
        Article pendingArticle = new Article();
        pendingArticle.setArticleStatus(pendingStatus);
        //这里就是把pedingarticle里面的ArticleStatus设置到example中去。
        int result =  articleMapper.updateByExampleSelective(pendingArticle,example);
        if(result != 1){
            GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    @Override
    public PagedGridResult queryAllArticleListAdmin(Integer status, Integer page, Integer pageSize) {
        Example articleExample = new Example(Article.class);
        articleExample.orderBy("createTime").desc();

        Example.Criteria criteria = articleExample.createCriteria();
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }

        // 审核中是机审和人审核的两个状态，所以需要单独判断
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
        }

        //isDelete 必须是0
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);

        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return setterPagedGrid(list, page);
    }

    @Transactional
    @Override
    public void deleteArticle(String userId, String articleId) {
        Example articleExample = makeExampleCriteria(userId, articleId);

        Article pending = new Article();
        pending.setIsDelete(YesOrNo.YES.type);

        int result = articleMapper.updateByExampleSelective(pending, articleExample);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
    }

    @Transactional
    @Override
    public void withdrawArticle(String userId, String articleId) {
        Example articleExample = makeExampleCriteria(userId, articleId);

        Article pending = new Article();
        pending.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);

        int result = articleMapper.updateByExampleSelective(pending, articleExample);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
    }

    private Example makeExampleCriteria(String userId, String articleId) {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = articleExample.createCriteria();
        criteria.andEqualTo("publishUserId", userId);
        criteria.andEqualTo("id", articleId);
        return articleExample;
    }
}
