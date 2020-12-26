package com.qigetech.mark.user.service.user.impl;


import com.qigetech.mark.user.entity.permission.Role;
import com.qigetech.mark.user.entity.permission.SysRole;
import com.qigetech.mark.user.entity.permission.SysUserRole;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.mapper.permission.SysRoleMapper;
import com.qigetech.mark.user.mapper.user.UserMapper;
import com.qigetech.mark.user.service.user.IUserService;
import com.qigetech.mark.user.utils.PasswordUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;

import com.qigetech.mark.user.service.permission.ISysUserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-14
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private ISysUserRoleService sysUserRoleServiceImpl;

    private static final String LOG_TYPE = "用户";
    /**
     * 由用户发起的注册信息
     */
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean saveFromUser(User user) {
        if (this.baseMapper.selectOne(new QueryWrapper<User>().eq("name", user.getName())) != null) {
            return false;
        }
        user.setSalt(user.getName());
        user.setPassword(PasswordUtils.getSlatPwMd5(user.getPassword()));
        user.setCreateUser("system");
        user.setCreateTime(new Date());
        user.setUpdateUser("system");
        user.setUpdateTime(new Date());
        if (this.baseMapper.insert(user) == 0) {
            return false;
        }
        //设置用户角色为学生
        if (sysUserRoleServiceImpl.save(user.getId(), Long.valueOf(3))) {
            return false;
        }
        return true;
    }

    /**
     * 保存一个用户
     */
    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean save(User user) {

        if (this.baseMapper.selectOne(new QueryWrapper<User>().eq("name", user.getName())) != null) {
            return false;
        }

        BCryptPasswordEncoder md5PasswordEncoder = new BCryptPasswordEncoder();
        user.setPassword(md5PasswordEncoder.encode(user.getPassword()));

        user.setStuId(user.getStuId());
        user.setStuName(user.getStuName());
        user.setMajor(user.getMajor());

        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());




        if (this.baseMapper.insert(user) == 0) {
            return false;
        }
        //设置用户角色为学生
        if (sysUserRoleServiceImpl.save(user.getId(), Long.valueOf(3))) {
            return false;
        }

        return true;
    }

    /**
     * 更新一个用户
     */
    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean updateById(User user) {
        user.setUpdateTime(new Date());
        if(StringUtils.isNotEmpty(user.getPassword())){
            user.setPassword(PasswordUtils.getSlatPwMd5(user.getPassword()));
        }
        user.setStuId(user.getStuId());
        user.setStuName(user.getStuName());
        user.setMajor(user.getMajor());
        return this.retBool(this.baseMapper.updateById(user));
    }



    /**
     * 根据用户ID获取角色
     */
    public List<Map<String, Object>> seletedRolesByUserId(long userId) {
        List<Map<String, Object>> roles = new ArrayList<>();
        //判断当前用户是否具备更改用户权限
        Map<Long, Long> userRole = sysRoleMapper.selectRoleByUserId(userId)
                .stream()
                .collect(Collectors.toMap(Role::getId, Role::getId));
        List<SysRole> roleList = sysRoleMapper.selectList(null);
        for (SysRole s : roleList) {
            Map<String, Object> map = new HashMap<>();
            if (userRole.containsKey(s.getId())) {
                map.put("selected", true);
            } else {
                map.put("selected", false);
            }
            map.put("role", s);
            roles.add(map);
        }
        return roles;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    @Override
    public boolean removeById(Serializable id) {
        //删除相对应的用户类型表数据
        User baseUser = this.baseMapper.selectById(id);
        //当删除用户时也从用户角色表中删除相对应的内容
        sysUserRoleServiceImpl.remove(new QueryWrapper<SysUserRole>().eq("user_id", String.valueOf(id)));
        return SqlHelper.delBool(this.baseMapper.deleteById(id));
    }
}
