package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.RegistLoginBO;
import com.imooc.user.service.UserService;
import com.imooc.utils.IPUtil;
import com.imooc.utils.MyInfo;
import com.imooc.utils.SMSUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("passport")
public class PassportController extends BaseController implements PassportControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(PassportController.class);
    @Resource
    private SMSUtils smsUtils;
    @Resource
    private UserService userService;

    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {
        //获取用户ip
        String userIp = IPUtil.getRequestIp(request);
        //根据用户的ip进行限制，限制用户在60s内只能获取一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);
        //生成随机验证码并且发送短信
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        //TODO 注意，这里是写死成自己的了,如果要正常使用，还是要写成前端传过来的。
//        smsUtils.sendSMS(MyInfo.getMobile(), random);
        //把验证码存入redis，用于后序验证
        redis.set(MOBILE_SMSCODE + ":" + mobile, random, 30 * 60);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult doLogin(@Valid RegistLoginBO registLoginBO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> map = getErrors(bindingResult);
            return GraceJSONResult.errorMap(map);
        }
        String mobile = registLoginBO.getMobile();
        String smsCode = registLoginBO.getSmsCode();
        //验证验证码是否正确
        String redisSMSCode = redis.get(MOBILE_SMSCODE + ":" + mobile);
        if (StringUtils.isBlank(redisSMSCode) || !redisSMSCode.equalsIgnoreCase(smsCode)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        //查询数据库,判断该用户注册
        AppUser user = userService.queryMobileIsExist(mobile);
        if (user != null && user.getActiveStatus().equals(UserStatus.FROZEN.type)) {
            //如果用户不为空，并且状态为冻结，则直接抛出异常，禁止登陆
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        }else if(user == null){
            //如果用户没有注册过，则为null，需要注册信息入库
            user = userService.createUser(mobile);
        }
        //最后把根据手机号查出来的用户信息给返回回来。
        return GraceJSONResult.ok(user);
    }

}
