package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.api.controller.user.MyFansControllerApi;
import com.imooc.enums.Sex;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.vo.FansCountsVO;
import com.imooc.user.mapper.FansMapper;
import com.imooc.user.service.MyFansService;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class MyFansController extends BaseController implements MyFansControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(MyFansController.class);
    @Resource
    private MyFansService myFansService;
    @Override
    public GraceJSONResult isMeFollowThisWriter(String writerId, String fanId) {
        boolean res = myFansService.isMeFollowThisWriter(writerId, fanId);
        return GraceJSONResult.ok(res);
    }

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        myFansService.follow(writerId,fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult unfollow(String writerId, String fanId) {
        myFansService.unfollow(writerId,fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryAll(String writerId, Integer page, Integer pageSize) {
        if(StringUtils.isBlank(writerId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        if(page == null){
            page = COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult result = myFansService.queryMyFansList(writerId, page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult queryRatio(String writerId) {
        int manCounts = myFansService.queryFansCounts(writerId, Sex.man);
        int womanCounts = myFansService.queryFansCounts(writerId, Sex.woman);
        FansCountsVO fansCountsVO = new FansCountsVO();
        fansCountsVO.setManCounts(manCounts);
        fansCountsVO.setWomanCounts(womanCounts);
        return GraceJSONResult.ok(fansCountsVO);
    }
}
