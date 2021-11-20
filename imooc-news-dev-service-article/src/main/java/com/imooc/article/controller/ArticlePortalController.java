package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.ArticlePortalControllerApi;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.article.service.ArticleService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.IndexArticleVO;
import com.imooc.utils.IPUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
public class ArticlePortalController extends BaseController implements ArticlePortalControllerApi {

    final static Logger logger = LoggerFactory.getLogger(ArticlePortalController.class);

    @Autowired
    private ArticlePortalService articlePortalService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GraceJSONResult list(String keyword,
                                Integer category,
                                Integer page,
                                Integer pageSize) {
        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult
                = articlePortalService.queryIndexArticleList(keyword,
                category,
                page,
                pageSize);
// START
// END
        gridResult = rebuildArticleGrid(gridResult);
        return GraceJSONResult.ok(gridResult);
    }

    private PagedGridResult rebuildArticleGrid(PagedGridResult gridResult) {
        // START
        /**
         * 这个就是原始的list，好多个人的文章都在首页显示的那个
         */
        List<Article> list = (List<Article>)gridResult.getRows();

        // 1. 构建发布者id列表
        /**
         * 因为每个人都发布了好多，所以他们的id是重复的，我们需要放到一个Set中去，保证不会重复
         * 只要他们的id就可以
         */
        Set<String> idSet = new HashSet<>();
        List<String> idList = new ArrayList<>();
        for (Article a : list) {
//            System.out.println(a.getPublishUserId());
            // 1.1 构建发布者的set
            idSet.add(a.getPublishUserId());
            // 1.2 构建文章id的list
        }
        System.out.println(idSet.toString());
        // 发起redis的mget批量查询api，获得对应的值

        // 2. 发起远程调用（restTemplate），请求用户服务获得用户（idSet 发布者）的列表
        List<AppUserVO> publisherList = getPublisherList(idSet);

        // 3. 拼接两个list，重组文章列表
        List<IndexArticleVO> indexArticleList = new ArrayList<>();

        for (int i = 0 ; i < list.size() ; i ++) {
            IndexArticleVO indexArticleVO = new IndexArticleVO();
            //这是原始的，所有作者的文章
            Article a = list.get(i);
            //拷贝到这个对象里面，这个对象里面有AppUserVO,这个就是用来拼接对象的
            BeanUtils.copyProperties(a, indexArticleVO);

            // 3.1 从publisherList中获得发布者的基本信息
            AppUserVO publisher  = getUserIfPublisher(a.getPublishUserId(), publisherList);
            //这里就是设置那个多出来的AppUserVo,拼接的那个多出来的
            indexArticleVO.setPublisherVO(publisher);
            indexArticleList.add(indexArticleVO);
        }

        //重新设置一下新的List
        gridResult.setRows(indexArticleList);
// END
        return gridResult;
    }

    private AppUserVO getUserIfPublisher(String publisherId,
                                         List<AppUserVO> publisherList) {
        //传过来的是用户id，然后list是这些用户发布过的文章，去进行一个匹配，查找到对应的人
        for (AppUserVO user : publisherList) {
             if (user.getId().equalsIgnoreCase(publisherId)) {
                return user;
            }
        }
        return null;
    }

    // 发起远程调用，获得用户的基本信息
    private List<AppUserVO> getPublisherList(Set idSet) {
        String userServerUrlExecute
                = "http://user.imoocnews.com:8003/user/queryByIds?userIds=" + JsonUtils.objectToJson(idSet);
        ResponseEntity<GraceJSONResult> responseEntity
                = restTemplate.getForEntity(userServerUrlExecute, GraceJSONResult.class);
        GraceJSONResult bodyResult = responseEntity.getBody();
        List<AppUserVO> publisherList = null;
        if (bodyResult.getStatus() == 200) {
            //这里返回的是一个list对象，然后再转换成一个Json,再转换回来，虽然这俩是一个对象
            // 但是还是都进行这个流程
            String userJson = JsonUtils.objectToJson(bodyResult.getData());
            publisherList = JsonUtils.jsonToList(userJson, AppUserVO.class);
//            publisherList = (List<AppUserVO>) bodyResult.getData();
        }
        return publisherList;
    }

    @Override
    public GraceJSONResult hotList() {
        return GraceJSONResult.ok(articlePortalService.queryHotList());
    }

    @Override
    public GraceJSONResult queryArticleListOfWriter(String writerId, Integer page, Integer pageSize) {

        System.out.println("writerId=" + writerId);

        if (page == null) {
            page = COMMON_START_PAGE;
        }

        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }

        PagedGridResult gridResult = articlePortalService.queryArticleListOfWriter(writerId, page, pageSize);
        gridResult = rebuildArticleGrid(gridResult);
        return GraceJSONResult.ok(gridResult);
    }

    @Override
    public GraceJSONResult queryGoodArticleListOfWriter(String writerId) {
        PagedGridResult gridResult = articlePortalService.queryGoodArticleListOfWriter(writerId);
        return GraceJSONResult.ok(gridResult);
    }
}
