package com.imooc.article.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

    public GraceJSONResult hello(){
        return GraceJSONResult.ok();
    }
}
