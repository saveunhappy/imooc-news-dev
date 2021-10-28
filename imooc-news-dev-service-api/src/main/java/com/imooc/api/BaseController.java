package com.imooc.api;


import com.imooc.utils.RedisOperator;

import javax.annotation.Resource;

public class BaseController {
    @Resource
    public RedisOperator redis;
    public static final String MOBILE_SMSCODE = "mobile:smscode";
}
