package com.imooc.user.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.MyInfo;
import com.imooc.utils.SMSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class PassportController implements PassportControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);
    @Resource
    private SMSUtils smsUtils;

    @Override
    public GraceJSONResult getSMSCode() {
        String random = "123789";
        smsUtils.sendSMS(MyInfo.getMobile(),random);
        return GraceJSONResult.ok();
    }
}
