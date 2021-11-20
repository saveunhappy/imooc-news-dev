package com.imooc.user.service.impl;

import com.imooc.api.service.BaseService;
import com.imooc.enums.Sex;
import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.org.n3r.idworker.Sid;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.Fans;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.mapper.FansMapper;
import com.imooc.user.service.MyFansService;
import com.imooc.user.service.UserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class MyFansServiceImpl extends BaseService implements MyFansService {
    @Resource
    private FansMapper fansMapper;
    @Resource
    private UserService userService;
    @Resource
    private Sid sid;
    @Override
    public boolean isMeFollowThisWriter(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setFanId(fanId);
        fans.setWriterId(writerId);
        int count = fansMapper.selectCount(fans);
        return count > 0;
    }

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        AppUser fanInfo = userService.getUser(fanId);
        String sid = this.sid.nextShort();
        Fans fans = new Fans();
        fans.setId(sid);
        fans.setFanId(fanId);
        fans.setWriterId(writerId);
        fans.setFace(fanInfo.getFace());
        fans.setSex(fanInfo.getSex());
        fans.setFanNickname(fanInfo.getNickname());
        fans.setProvince(fanInfo.getProvince());
        int res = fansMapper.insert(fans);
        if(res != 1){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        // redis作家粉丝数累加
        redis.increment(REDIS_WRITER_FANS_COUNTS + ":" + writerId,1);
        //当前用户(我的)关注数累加
        redis.increment(REDIS_MY_FOLLOW_COUNTS + ":" + fanId,1);
        return GraceJSONResult.ok();
    }
}
