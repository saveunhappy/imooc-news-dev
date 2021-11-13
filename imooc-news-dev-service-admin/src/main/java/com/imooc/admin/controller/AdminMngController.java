package com.imooc.admin.controller;

import com.imooc.admin.service.AdminUserService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.AdminMngControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;
@RestController
public class AdminMngController extends BaseController implements AdminMngControllerApi {
    @Resource
    private AdminUserService adminUserService;
    @Resource
    private RedisOperator redis;


    @Override
    public GraceJSONResult adminLogin(@Valid AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {

        //1.查询用户admin用户的信息
        AdminUser admin = adminUserService.queryAdminUserByUsername(adminLoginBO.getUsername());
        //2.判断admin不为空，如果为空则登录失败
        if(admin == null){
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        boolean checkpw = BCrypt.checkpw(adminLoginBO.getPassword(), admin.getPassword());
        if(checkpw){
            doLoginSettings(admin,request,response);
            return GraceJSONResult.ok();
        }else {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
    }

    @Override
    public GraceJSONResult adminIsExist(String username) {
        checkAdminExist(username);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult addNewAdmin(NewAdminBO newAdminBO, HttpServletRequest request, HttpServletResponse response) {
        //0. TODO 验证BO中的用户名和密码不为空
        //1.base64不为空，则代表需要人脸入库，否则需要用户输入密码和确认密码
        if(StringUtils.isBlank(newAdminBO.getImg64())){
            if (StringUtils.isBlank(newAdminBO.getPassword())
                    || StringUtils.isBlank(newAdminBO.getConfirmPassword())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }
        //2.密码不为空则两次密码必须一致
        if(StringUtils.isNotBlank(newAdminBO.getPassword())){
            if(!newAdminBO.getPassword().equalsIgnoreCase(newAdminBO.getConfirmPassword())){
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        //校验用户名唯一
        checkAdminExist(newAdminBO.getUsername());

        adminUserService.addNewAdminUser(newAdminBO);
        return GraceJSONResult.ok();
    }

    private void checkAdminExist(String username){
        AdminUser admin = adminUserService.queryAdminUserByUsername(username);
        if(admin != null){
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }


    private void doLoginSettings(AdminUser admin,
                                 HttpServletRequest request,
                                 HttpServletResponse response){
        //保存token放到redis
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_TOKEN + ":" + admin.getId(),token);
        //保存admin登录基本token信息到cookie中
        setCookie(request,response,"atoken",token,COOKIE_MONTH);
        setCookie(request,response,"aid",admin.getId(),COOKIE_MONTH);
        setCookie(request,response,"aname",admin.getAdminName(),COOKIE_MONTH);
    }

    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {
        if(page == null){
            page = COMMON_START_PAGE;
        }
        if(pageSize == null){
            pageSize = COMMON_PAGE_SIZE;
        }
        adminUserService.queryAdminList(page,pageSize);
        return GraceJSONResult.ok();
    }
}
