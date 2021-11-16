package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Api(value = "首页友情链接维护",tags = {"首页友情链接维护的controller"})
@RequestMapping("friendLinkMng")
public interface FrientLinkControllerApi {


    @ApiOperation(value = "新增或者修改友情链接",notes = "新增或者修改友情链接",httpMethod = "POST")
    @PostMapping("/saveOrUpdateFriendLink")
    GraceJSONResult adminLogin(@RequestBody @Valid AdminLoginBO adminLoginBO,
                            HttpServletRequest request,
                            HttpServletResponse response);

}
