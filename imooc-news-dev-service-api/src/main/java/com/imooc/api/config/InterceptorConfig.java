package com.imooc.api.config;

import com.imooc.api.interceptors.PassportInterceptor;
import com.imooc.api.interceptors.UserTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Resource
    private PassportInterceptor passportInterceptor;
    @Resource
    private UserTokenInterceptor userTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor)
                .addPathPatterns("/passport/getSMSCode");
        registry.addInterceptor(userTokenInterceptor)
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo");
    }
}
