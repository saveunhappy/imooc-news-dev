package com.imooc.api;


import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {
    @Resource
    public RedisOperator redis;
    public static final String MOBILE_SMSCODE = "mobile:smscode";
    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final Integer COOKIE_MONTH = 30 * 24 *60 *60;
    @Value("${website.domain-name}")
    public String DOMAIN_NAME;
    public Map<String, Object> getErrors(BindingResult result) {
        Map<String, Object> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            //发送验证码错误的时候对应的某个属性
            String field = error.getField();
            //验证的错误消息
            String message = error.getDefaultMessage();
            map.put(field, message);
        }
        return map;
    }

    public void setCookie(HttpServletRequest request,
                          HttpServletResponse response,
                          String cookieName,
                          String cookieValue,
                          Integer maxAge) {
        try {
            cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            setCookieValue(request,response,cookieName,cookieValue,maxAge);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void setCookieValue(HttpServletRequest request,
                               HttpServletResponse response,
                               String cookieName,
                               String cookieValue,
                               Integer maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(maxAge);
        cookie.setDomain(DOMAIN_NAME);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
