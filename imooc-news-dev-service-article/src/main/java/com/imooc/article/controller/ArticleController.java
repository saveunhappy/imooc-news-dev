package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleCoverType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
public class ArticleController extends BaseController implements ArticleControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);
    @Resource
    private ArticleService articleService;
    @Value("${freemarker.html.article}")
    private String articlePath;
    @Override
    public GraceJSONResult createArticle(@Valid NewArticleBO newArticleBO) {
        //判断文章封面类型，单图必填，纯文字则设置为空
        if (newArticleBO.getArticleType() == ArticleCoverType.ONE_IMAGE.type) {
            if (StringUtils.isBlank(newArticleBO.getArticleCover())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        } else if (newArticleBO.getArticleType() == ArticleCoverType.WORDS.type) {
            newArticleBO.setArticleCover("");
        }
        //判断分类id是否存在
        String allCatJson = redis.get(REDIS_ALL_CATEGORY);
        Category temp = null;
        if (StringUtils.isBlank(allCatJson)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        } else {
            List<Category> categories = JsonUtils.jsonToList(allCatJson, Category.class);
            for (Category category : categories) {
                if (category.getId() == newArticleBO.getCategoryId()) {
                    temp = category;
                    break;
                }
            }
            if (temp == null) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_CATEGORY_NOT_EXIST_ERROR);
            }
        }
        articleService.createArticle(newArticleBO, temp);
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
        if (page == null) {
            page = COMMON_START_PAGE;
        }
        if (pageSize == null) {
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
        if (passOrNot == YesOrNo.YES.type) {
            //审核通过
            pedingStatus = ArticleReviewStatus.SUCCESS.type;
        } else if (passOrNot == YesOrNo.NO.type) {
            //审核失败
            pedingStatus = ArticleReviewStatus.FAILED.type;
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        //保存到数据库，更改文章的状态为审核成功或者失败
        articleService.updateArticleStatus(articleId, pedingStatus);
        if (pedingStatus == ArticleReviewStatus.SUCCESS.type) {
            try {
                createArticleHTML(articleId);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("创建文章出错");
            }
        }

        return GraceJSONResult.ok();
    }
    private String makePathAndSubstring(String path){
        return path.substring(1).replace("/",File.separator);
    }
    //文章生成html
    public void createArticleHTML(String articleId) throws Exception {
        //0.配置freemarker基本环境
        Configuration cfg = new Configuration(Configuration.getVersion());
        String classPath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(makePathAndSubstring(classPath + "templates")));
        //1.获得现有的模板ftl文件
        Template template = cfg.getTemplate("detail.ftl", StandardCharsets.UTF_8.name());
        //获得文章的详情数据
        ArticleDetailVO detailVO = getArticleDetail(articleId);
        Map<String,Object> map = new HashMap<>();
        map.put("articleDetail",detailVO);
        //3.融合动态数据ftl,生成HTML
        File tempDic = new File(articlePath);
        if(!tempDic.exists()){
            tempDic.mkdirs();
        }
        articlePath = articlePath + File.separator + detailVO.getId() + ".html";
        Writer out = new FileWriter(articlePath);
        template.process(map,out);
        out.close();
    }
    //发起远程调用rest,获得文章详情数据
    public ArticleDetailVO getArticleDetail(String articleId) {
        String url
                = "http://www.imoocnews.com:8001/portal/article/detail?articleId=" + articleId;
        ResponseEntity<GraceJSONResult> responseEntity
                = restTemplate.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult bodyResult = responseEntity.getBody();
        ArticleDetailVO detailVO = null;
        if (bodyResult.getStatus() == 200) {
            String detailJson = JsonUtils.objectToJson(bodyResult.getData());
            detailVO = JsonUtils.jsonToPojo(detailJson, ArticleDetailVO.class);
        }
        return detailVO;
    }

    @Override
    public GraceJSONResult delete(String userId, String articleId) {
        articleService.deleteArticle(userId, articleId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult withdraw(String userId, String articleId) {
        articleService.withdrawArticle(userId, articleId);
        return GraceJSONResult.ok();
    }
}
