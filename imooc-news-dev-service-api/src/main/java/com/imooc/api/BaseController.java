package com.imooc.api;


import com.imooc.utils.RedisOperator;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BaseController {
    @Resource
    public RedisOperator redis;
    public static final String MOBILE_SMSCODE = "mobile:smscode";
    public Map<String,Object> getErrors(BindingResult result){
        Map<String,Object> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            //发送验证码错误的时候对应的某个属性
            String field = error.getField();
            //验证的错误消息
            String message = error.getDefaultMessage();
            map.put(field,message);
        }
        return map;
    }
}
