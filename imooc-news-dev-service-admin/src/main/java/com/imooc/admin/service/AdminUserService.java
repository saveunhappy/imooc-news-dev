package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;

public interface AdminUserService {
    /**
     * 获得管理员的用户信息
     */
    AdminUser queryAdminUserByUsername(String username);

    void addNewAdminUser(NewAdminBO newAdminBO);

    PagedGridResult queryAdminList(Integer page, Integer pageSize);
}
