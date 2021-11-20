package com.imooc.user.service;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;

public interface MyFansService {
    /**
     * 查询当前用户是否关注作家
     * @param writerId
     * @param fanId
     * @return
     */
    boolean isMeFollowThisWriter(String writerId, String fanId);

    GraceJSONResult follow(String writerId, String fanId);

    void unfollow(String writerId, String fanId);
}
