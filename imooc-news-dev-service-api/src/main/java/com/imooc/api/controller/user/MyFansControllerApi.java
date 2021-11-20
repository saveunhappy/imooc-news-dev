package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api(value = "粉丝管理",tags = {"粉丝管理的controller"})
@RequestMapping("fans")
public interface MyFansControllerApi {
    @ApiOperation(value = "查询当前用户是否关注作家",notes = "查询当前用户是否关注作家",httpMethod = "POST")
    @PostMapping("/isMeFollowThisWriter")
    GraceJSONResult isMeFollowThisWriter(@RequestParam String writerId,
                                         @RequestParam String fanId);

    @ApiOperation(value = "用户关注作家,成为粉丝",notes = "用户关注作家,成为粉丝",httpMethod = "POST")
    @PostMapping("/follow")
    GraceJSONResult follow(@RequestParam String writerId,
                                         @RequestParam String fanId);
    @ApiOperation(value = "取消关注，作家损失粉丝",notes = "取消关注，作家损失粉丝",httpMethod = "POST")
    @PostMapping("/unfollow")
    GraceJSONResult unfollow(@RequestParam String writerId,
                           @RequestParam String fanId);

    @PostMapping("/queryAll")
    @ApiOperation(value = "查询我的所有粉丝列表", notes = "查询我的所有粉丝列表", httpMethod = "POST")
    GraceJSONResult queryAll(@RequestParam String writerId,
                                 @ApiParam(name = "page", value = "查询下一页的第几页")
                                 @RequestParam Integer page,
                                 @ApiParam(name = "pageSize", value = "分页的每一页显示的条数")
                                 @RequestParam Integer pageSize);
}
