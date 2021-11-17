package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Date;

@Api(value = "用户管理相关的接口定义",tags = {"用户管理相关的接口controller"})
@RequestMapping("appUser")
public interface AdminUserMngControllerApi {

    @ApiOperation(value = "查询所有网站用户",notes = "查询所有网站用户",httpMethod = "POST")
    @PostMapping("/queryAll")
    GraceJSONResult getUserInfo(@RequestParam("nickname") String nickname,
                                @RequestParam("status") Integer status,
                                @RequestParam("startDate") Date startDate,
                                @RequestParam("endDate") Date endDate,
                                @RequestParam("page") Integer page,
                                @RequestParam("pageSize") Integer pageSize);

    @ApiOperation(value = "查看用户详情",notes = "查看用户详情",httpMethod = "POST")
    @PostMapping("/userDetail")
    GraceJSONResult userDetail(@RequestParam String userId);

    @ApiOperation(value = "冻结用户或者解冻用户",notes = "冻结用户或者解冻用户",httpMethod = "POST")
    @PostMapping("/freezeUserOrNot")
    GraceJSONResult freezeUserOrNot(@RequestParam String userId,
                               @RequestParam Integer doStatus);
}
