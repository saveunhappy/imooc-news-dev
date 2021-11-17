package com.imooc.user.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.imooc.api.BaseController;
import com.imooc.api.controller.user.AdminUserMngControllerApi;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.user.service.AppUserMngService;
import com.imooc.user.service.UserService;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;

@RestController
public class AdminUserMngController extends BaseController implements AdminUserMngControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(AdminUserMngController.class);
    @Resource
    private AppUserMngService appUserMngService;
    @Resource
    private UserService userService;
    @Override
    public GraceJSONResult getUserInfo(String nickname,
                                       Integer status,
                                       Date startDate,
                                       Date endDate,
                                       Integer page,
                                       Integer pageSize) {
        System.out.println(startDate);
        System.out.println(endDate);
        if(page == null){
            page = COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        PagedGridResult result = appUserMngService.queryAllUserList(nickname, status, startDate, endDate, page, pageSize);

        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult userDetail(String userId) {
        return GraceJSONResult.ok(userService.getUser(userId));
    }

    @Override
    public GraceJSONResult freezeUserOrNot(String userId, Integer doStatus) {
        if(!UserStatus.isUserStatusValid(doStatus)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        appUserMngService.freezeUserOrNot(userId,doStatus);
        //刷新用户状态
        //1.删除用户会话，从而保障用户需要重新登录以后再来刷新他的会话状态
        //2.查询最新用户的信息，重新放入redis中，做一次更新
        redis.del(REDIS_USER_INFO + ":" + userId);
        return GraceJSONResult.ok();
    }
}
