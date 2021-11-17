package com.imooc.admin.service.impl;

import com.imooc.admin.repository.FriendLinkRepository;
import com.imooc.admin.service.FriendLinkService;
import com.imooc.pojo.mo.FriendLinkMO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FriendLinkServiceImpl implements FriendLinkService {
    @Resource
    private FriendLinkRepository friendLinkRepository;

    @Override
    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO) {
        friendLinkRepository.save(friendLinkMO);
    }
}
