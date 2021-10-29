package com.imooc.exception;

import com.imooc.grace.result.ResponseStatusEnum;

public class MyCustomException extends RuntimeException {
    private ResponseStatusEnum responseStatusEnum;

    public ResponseStatusEnum getResponseStatusEnum() {
        return responseStatusEnum;
    }

    public void setResponseStatusEnum(ResponseStatusEnum responseStatusEnum) {
        this.responseStatusEnum = responseStatusEnum;
    }

    public MyCustomException(ResponseStatusEnum responseStatusEnum) {
        super("异常状态码:"+responseStatusEnum.status() +
                ";具体异常信息为:"+responseStatusEnum.msg());
        this.responseStatusEnum = responseStatusEnum;
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
