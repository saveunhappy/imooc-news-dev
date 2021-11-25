package com.imooc.user.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.UserAccountInfoVO;
import com.imooc.user.service.UserService;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@DefaultProperties(defaultFallback = "defaultFallback")
public class UserController extends BaseController implements UserControllerApi {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserService userService;
    public GraceJSONResult defaultFallback(){
        System.out.println("全局降级");
        return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR_GLOBAL);
    }
    @Override
    public GraceJSONResult getUserInfo(String userId) {
        //0.判断用户参数不能为空
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        //1.根据userId查询用户的信息
        AppUser user = getUser(userId);
        //2.返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user,userVO);
        //2.查询redis中用户的关注数和粉丝数
        userVO.setMyFansCounts(getCountsFromRedis(REDIS_WRITER_FANS_COUNTS + ":" + userId));
        userVO.setMyFollowCounts(getCountsFromRedis(REDIS_MY_FOLLOW_COUNTS +":"+ userId));
        return GraceJSONResult.ok(userVO);

    }

    @Override
    public GraceJSONResult getAccountInfo(String userId) {
        //0.判断用户参数不能为空
        if(StringUtils.isBlank(userId)){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.UN_LOGIN);
        }
        //1.根据userId查询用户的信息
        AppUser user = getUser(userId);
        UserAccountInfoVO accountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user,accountInfoVO);


        return GraceJSONResult.ok(accountInfoVO);
    }

    @Override
    public GraceJSONResult updateUserInfo(UpdateUserInfoBO updateUserInfoBO) {
        //1.执行更新操作

        userService.updateUserInfo(updateUserInfoBO);
        return GraceJSONResult.ok();
    }

    private AppUser getUser(String userId){
        String userJson = redis.get(REDIS_USER_INFO + ":" + userId);
        AppUser user = null;
        if(StringUtils.isNotBlank(userJson)){
            user = JsonUtils.jsonToPojo(userJson,AppUser.class);
        }else {
            user = userService.getUser(userId);
            redis.set(REDIS_USER_INFO+":"+userId,JsonUtils.objectToJson(user));
        }
        return user;
    }
    @Value("${server.port}")
    private String myport;

    @HystrixCommand//(fallbackMethod = "queryByIdsFallBack")
    @Override
    public GraceJSONResult queryByIds(String userIds) {
//        int a = 1 / 0;
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("mypost + " + myport);
        //这里的传过来的是一个Set转换成的String，
        if (StringUtils.isBlank(userIds)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }

        List<AppUserVO> publisherList = new ArrayList<>();
        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);
        //这里又把String给拆成好几个id了。然后返回这几个id去查询对应的作者
        for (String userId : userIdList) {
            // 获得用户基本信息
            AppUserVO userVO = getBasicUserInfo(userId);
            // 添加到publisherList
            publisherList.add(userVO);
        }

        return GraceJSONResult.ok(publisherList);
    }

    public GraceJSONResult queryByIdsFallBack(String userIds) {
        System.out.println("进入降级方法");
        List<AppUserVO> publisherList = new ArrayList<>();
        List<String> userIdList = JsonUtils.jsonToList(userIds, String.class);
        //这里又把String给拆成好几个id了。然后返回这几个id去查询对应的作者
        for (String userId : userIdList) {
            // 获得用户基本信息
            AppUserVO userVO = new AppUserVO();
            // 添加到publisherList
            publisherList.add(userVO);
        }

        return GraceJSONResult.ok(publisherList);
    }
    private AppUserVO getBasicUserInfo(String userId) {
        // 1. 根据userId查询用户的信息
        AppUser user = getUser(userId);

        // 2. 返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user, userVO);

        return userVO;
    }
}
