package com.imooc.api.interceptors;
import com.imooc.utils.RedisOperator;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserTokenInterceptor extends BaseInterceptor implements HandlerInterceptor {
    @Resource
    public RedisOperator redis;
    public static final String MOBILE_SMSCODE = "mobile:smscode";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");
        boolean run = verifyUserIdToken(userId, userToken, REDIS_USER_TOKEN);
        System.out.println("verifyUserIdToken : " + run);
        return run;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
