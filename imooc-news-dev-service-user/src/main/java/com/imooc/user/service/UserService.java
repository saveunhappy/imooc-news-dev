package com.imooc.user.service;

import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;

public interface UserService {
    /**
     *
     * @param mobile 根据手机号查询用户是否存在
     */
    AppUser queryMobileIsExist(String mobile);

    /**
     *
     * @param mobile 如果用户不存在则创建用户
     */
    AppUser createUser(String mobile);

    /**
     *根据用户主键去查询用户信息
     */
    AppUser getUser(String userId);

    /**
     *  用户修改信息，完善资料，并且激活
     */
    void updateUserInfo(UpdateUserInfoBO updateUserInfoBO);
}
