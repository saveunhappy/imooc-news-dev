package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.bo.SaveFriendLinkBO;
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
public interface FriendLinkControllerApi {


    @ApiOperation(value = "新增或者修改友情链接",notes = "新增或者修改友情链接",httpMethod = "POST")
    @PostMapping("/saveOrUpdateFriendLink")
    GraceJSONResult saveOrUpdateFriendLink(@RequestBody @Valid SaveFriendLinkBO saveFriendLinkBO);
    @ApiOperation(value = "查看友情链接列表",notes = "查看友情链接列表",httpMethod = "POST")
    @PostMapping("/getFriendLinkList")
    GraceJSONResult getFriendLinkList();
    @ApiOperation(value = "查看友情链接列表",notes = "查看友情链接列表",httpMethod = "POST")
    @PostMapping("/delete")
    GraceJSONResult delete(@RequestParam String linkId);
}
