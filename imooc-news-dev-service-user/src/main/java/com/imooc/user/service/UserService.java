package com.imooc.user.service;

import com.imooc.pojo.AppUser;

public interface UserService {
    /**
     *
     * @param mobile 根据手机号查询用户是否存在
     */
    AppUser queryMobileIsExist(String mobile);

    /**
     *
     * @param mobile 如果用户不存在则创建用户
     * @return
     */
    AppUser createUser(String mobile);
}
