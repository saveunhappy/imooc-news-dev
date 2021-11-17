package com.imooc.user.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.imooc.api.BaseController;
import com.imooc.api.controller.user.AdminUserMngControllerApi;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.user.service.AppUserMngService;
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
}
