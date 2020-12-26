package com.qigetech.mark.user.service.permission.impl;

import com.qigetech.mark.user.entity.permission.Role;
import com.qigetech.mark.user.entity.permission.SysRole;
import com.qigetech.mark.user.mapper.permission.SysRoleMapper;
import com.qigetech.mark.user.service.permission.ISysRoleService;
import com.qigetech.mark.user.service.permission.ISysUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private ISysUserRoleService sysUserRoleServiceImpl;

    /**
     * 根据用户ID获取角色信息
     */
    @Override
    public List<Role> selectRoleByUserId(long userId){
        //selectRoleByUserId(userId)：自定义Mapper
        return this.baseMapper.selectRoleByUserId(userId); //userId = 2733
    }

}
