package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.bo.RegistLoginBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "查询admin用户是否存在",notes = "查询admin用户是否存在",httpMethod = "POST")
    @PostMapping("/adminIsExist")
    GraceJSONResult adminIsExist(@RequestParam String username);

    @ApiOperation(value = "创建admin",notes = "创建admin",httpMethod = "POST")
    @PostMapping("/addNewAdmin")
    GraceJSONResult addNewAdmin(@RequestBody NewAdminBO newAdminBO,
                               HttpServletRequest request,
                               HttpServletResponse response);

    @ApiOperation(value = "查询admin列表",notes = "查询admin列表",httpMethod = "POST")
    @PostMapping("/getAdminList")
    GraceJSONResult getAdminList(@ApiParam(name = "page",value = "第几页")@RequestParam Integer page,
    @ApiParam(name = "pageSize",value = "每页显示多少条")@RequestParam Integer pageSize);


    @ApiOperation(value = "admin退出登录",notes = "admin退出登录",httpMethod = "POST")
    @PostMapping("/adminLogout")
    GraceJSONResult adminLogout(@RequestParam String adminId,
                                HttpServletRequest request,
                                HttpServletResponse response );

    @ApiOperation(value = "admin管理员的人脸登录",notes = "admin管理员的人脸登录",httpMethod = "POST")
    @PostMapping("/adminFaceLogin")
    GraceJSONResult adminFaceLogin(@RequestBody AdminLoginBO adminLoginBO,
                                HttpServletRequest request,
                                HttpServletResponse response );
}
