package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.api.controller.user.MyFansControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.user.mapper.FansMapper;
import com.imooc.user.service.MyFansService;
import com.imooc.utils.RedisOperator;
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
}
