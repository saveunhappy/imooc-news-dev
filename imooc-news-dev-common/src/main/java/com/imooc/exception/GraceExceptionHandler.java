package com.imooc.exception;

import com.imooc.grace.result.GraceJSONResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GraceExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GraceExceptionHandler.class);

    @ExceptionHandler(MyCustomException.class)
    @ResponseBody
    public GraceJSONResult returnMyException(MyCustomException e) {
        logger.error("业务异常 : " + e.getResponseStatusEnum().msg());
        e.printStackTrace();
        return GraceJSONResult.errorCustom(e.getResponseStatusEnum());
    }

}
