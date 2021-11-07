package com.imooc.exception;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public GraceJSONResult validExceptionHandler(MethodArgumentNotValidException e) {
        Map<String, Object> map = getErrors(e.getBindingResult());
        return GraceJSONResult.errorMap(map);
    }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseBody
    public GraceJSONResult returnMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return GraceJSONResult.errorCustom(ResponseStatusEnum.FILE_MAX_SIZE_ERROR);
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public GraceJSONResult validExceptionHandler(BindException e) {
        logger.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return GraceJSONResult.errorMsg(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
    public Map<String, Object> getErrors(BindingResult result) {
        Map<String, Object> map = new HashMap<>();
        List<FieldError> errorList = result.getFieldErrors();
        for (FieldError error : errorList) {
            //发送验证码错误的时候对应的某个属性
            String field = error.getField();
            //验证的错误消息
            String message = error.getDefaultMessage();
            map.put(field, message);
        }
        return map;
    }

}
