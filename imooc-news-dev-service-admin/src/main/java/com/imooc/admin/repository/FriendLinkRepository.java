package com.imooc.admin.repository;

import com.imooc.pojo.mo.FriendLinkMO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FriendLinkRepository extends MongoRepository<FriendLinkMO,String> {
}