package com.imooc.api.controller.user;

import com.imooc.api.config.MyServiceList;
import com.imooc.api.controller.user.fallback.UserControllerFactoryFallback;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(value = "用户信息相关",tags = {"用户信息相关的controller"})
@RequestMapping("user")
@FeignClient(value = MyServiceList.SERVICE_USER, fallbackFactory = UserControllerFactoryFallback.class)
public interface UserControllerApi {

    @ApiOperation(value = "获得用户基本信息",notes = "获得用户基本信息",httpMethod = "POST")
    @PostMapping("/getUserInfo")
    GraceJSONResult getUserInfo(@RequestParam String userId);

    @ApiOperation(value = "获得用户账户信息",notes = "获得用户账户信息",httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    GraceJSONResult getAccountInfo(@RequestParam String userId);

    @ApiOperation(value = "修改/完善用户信息",notes = "修改/完善用户信息",httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    GraceJSONResult updateUserInfo(@RequestBody @Valid UpdateUserInfoBO updateUserInfoBO);

    @ApiOperation(value = "根据用户的ids查询用户列表", notes = "根据用户的ids查询用户列表", httpMethod = "GET")
    @GetMapping("/queryByIds")
    GraceJSONResult queryByIds(@RequestParam String userIds);
}
