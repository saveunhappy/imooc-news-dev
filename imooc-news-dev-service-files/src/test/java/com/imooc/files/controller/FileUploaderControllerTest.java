package com.imooc.files.controller;

import com.imooc.files.resource.FileResource;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
class FileUploaderControllerTest {
    @Resource
    FileResource fileResource;
    @Test
    public void test() throws Exception{
        System.out.println(fileResource.getHost());
    }

}