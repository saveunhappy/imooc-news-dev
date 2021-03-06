package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.config.RabbitMQConfig;
import com.imooc.api.controller.article.ArticleControllerApi;
import com.imooc.api.controller.article.ArticleHTMLControllerApi;
import com.imooc.api.controller.article.ArticlePortalControllerApi;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleCoverType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import com.mongodb.client.gridfs.GridFSBucket;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
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
    @Resource
    private GridFSBucket gridFSBucket;

    @Override
    public GraceJSONResult createArticle(@Valid NewArticleBO newArticleBO) {
        //??????????????????????????????????????????????????????????????????
        if (newArticleBO.getArticleType() == ArticleCoverType.ONE_IMAGE.type) {
            if (StringUtils.isBlank(newArticleBO.getArticleCover())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_COVER_NOT_EXIST_ERROR);
            }
        } else if (newArticleBO.getArticleType() == ArticleCoverType.WORDS.type) {
            newArticleBO.setArticleCover("");
        }
        //????????????id????????????
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
            //????????????
            pedingStatus = ArticleReviewStatus.SUCCESS.type;
        } else if (passOrNot == YesOrNo.NO.type) {
            //????????????
            pedingStatus = ArticleReviewStatus.FAILED.type;
        } else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
        //?????????????????????????????????????????????????????????????????????
        articleService.updateArticleStatus(articleId, pedingStatus);
        if (pedingStatus == ArticleReviewStatus.SUCCESS.type) {
            try {
//                createArticleHTML(articleId);
                String articleMongoId = createArticleHTMLToGridFS(articleId);
                //??????????????????????????????????????????
                articleService.updateArticleToGridFS(articleId, articleMongoId);
                //???????????????????????????html
                doDownloadArticleHTML(articleId,articleMongoId);
                //???????????????mq?????????????????????????????????????????????html
//                doDownloadArticleHTMLByMQ(articleId,articleMongoId);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("??????????????????");
            }
        }

        return GraceJSONResult.ok();
    }
    @Resource
    private RabbitTemplate rabbitTemplate;
    private void doDownloadArticleHTMLByMQ(String articleId, String articleMongoId) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_ARTICLE,
                "article.download.do",
                articleId +"," + articleMongoId);
    }
    @Resource
    private ArticleHTMLControllerApi articleHTMLControllerApi;
    private void doDownloadArticleHTML(String articleId, String articleMongoId) {
//        String url = "http://html.imoocnews.com:8002/article/html/download?" +
//                "articleId=" + articleId + "&articleMongoId=" + articleMongoId;
        Integer status = articleHTMLControllerApi.download(articleId,articleMongoId);
//        Integer status = restTemplate.getForObject(url, Integer.class);
        if (HttpStatus.OK.value() == status) {
            return;
        }
        GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
    }


    private String makePathAndSubstring(String path) {
        return path.substring(1).replace("/", File.separator);
    }

    //????????????html
    public void createArticleHTML(String articleId) throws Exception {
        //0.??????freemarker????????????
        Configuration cfg = new Configuration(Configuration.getVersion());
        String classPath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(makePathAndSubstring(classPath + "templates")));
        //1.?????????????????????ftl??????
        Template template = cfg.getTemplate("detail.ftl", StandardCharsets.UTF_8.name());
        //???????????????????????????
        ArticleDetailVO detailVO = getArticleDetail(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("articleDetail", detailVO);
        //3.??????????????????ftl,??????HTML
        File tempDic = new File(articlePath);
        if (!tempDic.exists()) {
            tempDic.mkdirs();
        }
        articlePath = articlePath + File.separator + detailVO.getId() + ".html";
        Writer out = new FileWriter(articlePath);
        template.process(map, out);
        out.close();
    }

    //????????????html
    public String createArticleHTMLToGridFS(String articleId) throws Exception {
        //0.??????freemarker????????????
        Configuration cfg = new Configuration(Configuration.getVersion());
        String classPath = this.getClass().getResource("/").getPath();
        cfg.setDirectoryForTemplateLoading(new File(makePathAndSubstring(classPath + "templates")));
        //1.?????????????????????ftl??????
        Template template = cfg.getTemplate("detail.ftl", StandardCharsets.UTF_8.name());
        //???????????????????????????
        ArticleDetailVO detailVO = getArticleDetail(articleId);
        Map<String, Object> map = new HashMap<>();
        map.put("articleDetail", detailVO);
        //??????content???????????????html?????????
        String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream inputStream = IOUtils.toInputStream(content);
        //?????????????????????mongoId
        ObjectId objectId = gridFSBucket.uploadFromStream(detailVO.getId() + ".html", inputStream);
        return objectId.toString();
    }
    @Resource
    private ArticlePortalControllerApi articlePortalControllerApi;
    //??????????????????rest,????????????????????????
    public ArticleDetailVO getArticleDetail(String articleId) {
//        String url
//                = "http://www.imoocnews.com:8001/portal/article/detail?articleId=" + articleId;
//        ResponseEntity<GraceJSONResult> responseEntity
//                = restTemplate.getForEntity(url, GraceJSONResult.class);
        GraceJSONResult bodyResult = articlePortalControllerApi.detail(articleId);
//        GraceJSONResult bodyResult = responseEntity.getBody();
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
