package com.imooc.api.config;

import com.imooc.api.interceptors.AdminTokenInterceptor;
import com.imooc.api.interceptors.PassportInterceptor;
import com.imooc.api.interceptors.UserActiveInterceptor;
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
    @Resource
    private UserActiveInterceptor userActiveInterceptor;
    @Resource
    private AdminTokenInterceptor adminTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor)
                .addPathPatterns("/passport/getSMSCode");
        registry.addInterceptor(userTokenInterceptor)
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo")
                .addPathPatterns("/fs/uploadFace");
//        registry.addInterceptor(userActiveInterceptor)
//                .addPathPatterns("/user/getAccountInfo");
        registry.addInterceptor(adminTokenInterceptor)
                .addPathPatterns("/adminMng/adminIsExist")
                .addPathPatterns("/adminMng/addNewAdmin")
                .addPathPatterns("/adminMng/getAdminList")
                .addPathPatterns("/fs/uploadToGridFS")
                .addPathPatterns("/fs/readInGridFS")

                .addPathPatterns("/friendLinkMng/saveOrUpdateFriendLink")
                .addPathPatterns("/friendLinkMng/getFriendLinkList")
                .addPathPatterns("/friendLinkMng/delete")
        ;
    }
}
