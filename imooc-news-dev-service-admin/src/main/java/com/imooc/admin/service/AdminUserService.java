package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;

public interface AdminUserService {
    /**
     * 获得管理员的用户信息
     */
    AdminUser queryAdminUserByUsername(String username);

    void addNewAdminUser(NewAdminBO newAdminBO);

    void queryAdminList(Integer page, Integer pageSize);
}
