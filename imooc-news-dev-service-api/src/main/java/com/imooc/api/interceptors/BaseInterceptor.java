package com.imooc.api.interceptors;

import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

public class BaseInterceptor {
    @Resource
    private RedisOperator redis;
    public static final String REDIS_USER_TOKEN = "redis_user_token";

    public boolean verifyUserIdToken(String id,
                                     String token,
                                     String redisKeyPrefix){
        if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(token)){
            String redisToken = redis.get(redisKeyPrefix + ":" + id);
            if(StringUtils.isBlank(redisToken)){
                GraceException.display(ResponseStatusEnum.UN_LOGIN);
                return false;
            }else{
                if(!redisToken.equalsIgnoreCase(token)){
                    GraceException.display(ResponseStatusEnum.TICKET_INVALID);
                    return false;
                }
            }
        }else{
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        return true;
    }

}
