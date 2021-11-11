package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;

public interface AdminUserService {
    /**
     * 获得管理员的用户信息
     */
    AdminUser queryAdminUserByUsername(String username);
}
