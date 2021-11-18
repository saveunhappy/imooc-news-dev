package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.mo.FriendLinkMO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface FriendLinkService {

    /**
     * 新增或者更新友情链接
     */
    void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

    List<FriendLinkMO> queryAllFriendLinks();

    void delete(String linkId);

    List<FriendLinkMO> queryPortalAllFriendLinks();

}
