package com.imooc.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.enums.Sex;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import org.n3r.idworker.Sid;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.Fans;
import com.imooc.pojo.vo.RegionRatioVO;
import com.imooc.user.mapper.FansMapper;
import com.imooc.user.service.MyFansService;
import com.imooc.user.service.UserService;
import com.imooc.utils.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
    @Transactional
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
    @Transactional
    @Override
    public void unfollow(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setFanId(fanId);
        fans.setWriterId(writerId);
        int res = fansMapper.delete(fans);
        if(res != 1){
            GraceException.display(ResponseStatusEnum.SYSTEM_OPERATION_ERROR);
        }
        // redis作家粉丝数累减
        redis.decrement(REDIS_WRITER_FANS_COUNTS + ":" + writerId,1);
        //当前用户(我的)关注数累减
        redis.decrement(REDIS_MY_FOLLOW_COUNTS + ":" + fanId,1);
    }

    @Override
    public PagedGridResult queryMyFansList(String writerId,Integer page,Integer pageSize) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        PageHelper.startPage(page,pageSize);
        List<Fans> list = fansMapper.select(fans);
        return setterPagedGrid(list,page);
    }

    @Override
    public Integer queryFansCounts(String writerId, Sex sex) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setSex(sex.type);
        int count = fansMapper.selectCount(fans);
        return count;

    }
    public static final String[] regions = {"北京", "天津", "上海", "重庆",
            "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
            "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
            "内蒙古", "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门"};
    @Override
    public List<RegionRatioVO> queryRegionRatioCounts(String writerId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        List<RegionRatioVO> list = new ArrayList<>();
        for(String province : regions){
            fans.setProvince(province);
            Integer count = fansMapper.selectCount(fans);
            RegionRatioVO regionRatioVO = new RegionRatioVO();
            regionRatioVO.setName(province);
            regionRatioVO.setValue(count);
            list.add(regionRatioVO);
        }
        return list;
    }
}
