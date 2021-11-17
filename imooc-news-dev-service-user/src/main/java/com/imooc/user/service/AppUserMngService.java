package com.imooc.user.service;

import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.utils.PagedGridResult;

import java.util.Date;

public interface AppUserMngService {
    PagedGridResult queryAllUserList(String nickname,
                                     Integer status,
                                     Date startDate,
                                     Date endDate,
                                     Integer page,
                                     Integer pageSize);
}
