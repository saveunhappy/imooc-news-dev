package com.imooc.search.controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.search.pojo.Stu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {
    @Resource
    private ElasticsearchTemplate esTemplate;
    private static final Logger logger = LoggerFactory.getLogger(HelloController.class);
    @RequestMapping("hello")
    public Object hello(){
        return GraceJSONResult.ok("hello world!!!");
    }
    @GetMapping("createIndex")
    public Object createIndex(){
        esTemplate.createIndex(Stu.class);
        return GraceJSONResult.ok("hello world!!!");
    }
    @GetMapping("deleteIndex")
    public Object deleteIndex(){
        esTemplate.deleteIndex(Stu.class);
        return GraceJSONResult.ok("hello world!!!");
    }
}
