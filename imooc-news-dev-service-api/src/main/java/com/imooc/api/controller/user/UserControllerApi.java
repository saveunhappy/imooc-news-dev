package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.RegistLoginBO;
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

    @ApiOperation(value = "用户信息相关",notes = "用户信息相关",httpMethod = "GET")
    @PostMapping("/getAccountInfo")
    GraceJSONResult getAccountInfo(@RequestParam String userId);

}
