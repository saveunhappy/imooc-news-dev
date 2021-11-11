package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.RegistLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "管理员admin维护",tags = {"管理员admin维护的controller"})
@RequestMapping("adminMng")
public interface AdminMngControllerApi {


    @ApiOperation(value = "admin登录接口",notes = "admin登录接口",httpMethod = "POST")
    @PostMapping("/adminLogin")
    GraceJSONResult adminLogin(@RequestBody @Valid AdminLoginBO adminLoginBO,
                            HttpServletRequest request,
                            HttpServletResponse response);

}
