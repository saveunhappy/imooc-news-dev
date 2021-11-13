package com.imooc.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.admin.service.AdminUserService;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.org.n3r.idworker.Sid;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Resource
    private AdminUserMapper adminUserMapper;
    @Resource
    private Sid sid;
    @Override
    public AdminUser queryAdminUserByUsername(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("username",username);
        AdminUser adminUser = adminUserMapper.selectOneByExample(adminExample);
        return adminUser;
    }
    @Transactional
    @Override
    public void addNewAdminUser(NewAdminBO newAdminBO) {
        String adminId = sid.nextShort();
        AdminUser adminUser = new AdminUser();
        adminUser.setId(adminId);
        adminUser.setUsername(newAdminBO.getUsername());
        adminUser.setAdminName(newAdminBO.getAdminName());
        //如果密码不为空则需要加密密码存入数据库
        if(StringUtils.isNotBlank(newAdminBO.getPassword())){
            adminUser.setPassword(BCrypt.hashpw(newAdminBO.getPassword(),BCrypt.gensalt()));
        }
        if(StringUtils.isNotBlank(newAdminBO.getFaceId())){
            adminUser.setFaceId(newAdminBO.getFaceId());
        }
        adminUser.setCreatedTime(new Date());
        adminUser.setUpdatedTime(new Date());
        int result = adminUserMapper.insert(adminUser);
        if(result != 1){
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }
    }

    @Override
    public void queryAdminList(Integer page, Integer pageSize) {
        Example adminExample = new Example(AdminUser.class);
        adminExample.orderBy("createdTime").desc();
        PageHelper.startPage(page,pageSize);
        List<AdminUser> adminUsers = adminUserMapper.selectByExample(adminExample);
        System.out.println(adminUsers);
    }
}
