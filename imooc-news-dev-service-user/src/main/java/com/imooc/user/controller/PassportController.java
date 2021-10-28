package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.IPUtil;
import com.imooc.utils.MyInfo;
import com.imooc.utils.SMSUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("passport")
public class PassportController extends BaseController implements PassportControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);
    @Resource
    private SMSUtils smsUtils;

    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {
        //获取用户ip
        String userIp = IPUtil.getRequestIp(request);
        //根据用户的ip进行限制，限制用户在60s内只能获取一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);
        //生成随机验证码并且发送短信
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        //TODO 注意，这里是写死成自己的了,如果要正常使用，还是要写成前端传过来的。
        smsUtils.sendSMS(MyInfo.getMobile(), random);
        //把验证码存入redis，用于后序验证
        redis.set(MOBILE_SMSCODE + ":" + mobile,random,30 * 60);
        return GraceJSONResult.ok();
    }
}
