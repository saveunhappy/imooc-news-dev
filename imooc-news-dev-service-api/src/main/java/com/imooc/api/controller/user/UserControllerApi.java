package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.RegistLoginBO;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "用户信息相关",tags = {"用户信息相关的controller"})
@RequestMapping("user")
public interface UserControllerApi {

    @ApiOperation(value = "获得用户账户信息",notes = "获得用户账户信息",httpMethod = "POST")
    @PostMapping("/getAccountInfo")
    GraceJSONResult getAccountInfo(@RequestParam String userId);


    @ApiOperation(value = "修改/完善用户信息",notes = "修改/完善用户信息",httpMethod = "POST")
    @PostMapping("/updateUserInfo")
    GraceJSONResult updateUserInfo(@Valid @RequestBody UpdateUserInfoBO updateUserInfoBO);

}
