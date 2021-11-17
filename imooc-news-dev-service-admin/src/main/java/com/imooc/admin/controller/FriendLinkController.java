package com.imooc.admin.controller;

import com.imooc.admin.service.FriendLinkService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.FriendLinkControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.SaveFriendLinkBO;
import com.imooc.pojo.mo.FriendLinkMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

@RestController
public class FriendLinkController extends BaseController implements FriendLinkControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(FriendLinkController.class);
    @Resource
    private FriendLinkService friendLinkService;
    @Override
    public GraceJSONResult saveOrUpdateFriendLink(@Valid SaveFriendLinkBO saveFriendLinkBO) {
        FriendLinkMO friendLinkMO = new FriendLinkMO();
        BeanUtils.copyProperties(saveFriendLinkBO, friendLinkMO);
        friendLinkMO.setCreateTime(new Date());
        friendLinkMO.setUpdateTime(new Date());
        friendLinkService.saveOrUpdateFriendLink(friendLinkMO);
        return GraceJSONResult.ok();
    }
}
