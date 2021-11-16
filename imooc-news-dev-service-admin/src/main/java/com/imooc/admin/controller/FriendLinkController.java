package com.imooc.admin.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.FriendLinkControllerApi;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.SaveFriendLinkBO;
import com.imooc.pojo.mo.SaveFriendLinkMO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

@RestController
public class FriendLinkController extends BaseController implements FriendLinkControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(FriendLinkController.class);

    @Override
    public GraceJSONResult adminLogin(@Valid SaveFriendLinkBO saveFriendLinkBO) {
        SaveFriendLinkMO saveFriendLinkMO = new SaveFriendLinkMO();
        BeanUtils.copyProperties(saveFriendLinkBO,saveFriendLinkMO);
        saveFriendLinkMO.setCreateTime(new Date());
        saveFriendLinkMO.setUpdateTime(new Date());
        return GraceJSONResult.ok();
    }
}
