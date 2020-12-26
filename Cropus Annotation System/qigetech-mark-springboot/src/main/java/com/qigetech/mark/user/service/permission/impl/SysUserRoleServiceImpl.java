package com.qigetech.mark.user.service.permission.impl;

import com.qigetech.mark.user.entity.permission.SysUserRole;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qigetech.mark.user.entity.permission.Role;
import com.qigetech.mark.user.mapper.permission.SysUserRoleMapper;
import com.qigetech.mark.user.service.permission.ISysUserRoleService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;


    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean save(Long userId){
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        sysUserRoleMapper.insert(sysUserRole);
        return true;
    }
    @Override
    public boolean save(Long userId,Long roleId){
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        sysUserRole.setRoleId(roleId);
        sysUserRoleMapper.insert(sysUserRole);
        return true;
    }
    /**
     * 保存角色和用户的对应关系
     */
    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean saveBySeleted(Long userId,String json){
        Type type = new TypeToken<List<Role>>(){}.getType();
        List<Role> roles = new Gson().fromJson(json, type);
        int delStatus = sysUserRoleMapper.delete(
                new QueryWrapper<SysUserRole>().eq("user_id",userId)
        );
        if(delStatus == 0){
            return false;
        }
        for(Role role : roles){
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setRoleId(role.getId());
            sysUserRole.setUserId(userId);
            sysUserRoleMapper.insert(sysUserRole);
        }

        return true;
    }
}
