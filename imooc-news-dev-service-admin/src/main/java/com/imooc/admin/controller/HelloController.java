package com.imooc.admin.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.RedisOperator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController implements HelloControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    public Object hello(){
        return GraceJSONResult.ok();
    }
}
