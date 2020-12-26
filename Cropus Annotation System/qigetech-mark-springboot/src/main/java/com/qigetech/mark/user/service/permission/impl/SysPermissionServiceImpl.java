package com.qigetech.mark.user.service.permission.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qigetech.mark.user.entity.permission.Role;
import com.qigetech.mark.user.entity.permission.SysPermission;
import com.qigetech.mark.user.entity.permission.SysPermissionTree;
import com.qigetech.mark.user.mapper.permission.SysPermissionMapper;
import com.qigetech.mark.user.service.permission.ISysPermissionService;
import com.qigetech.mark.user.service.permission.ISysRoleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    @Resource
    private SysPermissionMapper sysPermissionMapper;

    @Autowired
    private ISysRoleService sysRoleServiceImpl;

    /**
     * 通过用户ID获取权限
     *
     * @param userId 用户ID
     */
    @Override
    public List<SysPermission> getPermissionByUserId(long userId) {
        //获取用户已有的角色
        List<Role> roles = sysRoleServiceImpl.selectRoleByUserId(userId); //userId = 2733

        List<SysPermission> permissions = new ArrayList<>();
        roles.stream().forEach((item) -> {
            permissions.addAll(getResourceByRoleId(item.getId()));
        });


        return permissions.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 通过角色获取权限
     */
    @Override
    public List<SysPermission> getResourceByRoleId(long roleId) {
        return sysPermissionMapper.selectPermissionByRoleId(roleId);
    }

    /**
     * 获取权限内容
     *
     * @param userId 用户id
     * @param resId  资源id
     * @param type   资源类型
     * @return 资源列表
     */
    @Override
    public List<SysPermission> selectPermissionByType(long userId, long resId, String type) {
        return sysPermissionMapper.selectPermissionByType(userId, resId, type);
    }

    /**
     * @return
     * @Author panzejia
     * @Description 通过权限类型获取资源树
     * @Date 14:42 2019-02-14
     * @Param
     **/
    @Override
    public Map<String, Object> getPermissionTree() {
        Map<String, Object> map = new HashMap();
        Map<Long, SysPermissionTree> parentTemp = new HashMap<>();
        Map<Long, SysPermissionTree> parentDataTemp = new HashMap<>();
        List<SysPermission> permissions = sysPermissionMapper.selectList(
                new QueryWrapper<SysPermission>().orderByAsc("parent_id")
        );
        for (SysPermission permission : permissions) {
            SysPermissionTree permissionTree = new SysPermissionTree();
            permissionTree.setId(permission.getId());
            permissionTree.setTitle(permission.getResName());
            permissionTree.setKey(permission.getPermission());
            permissionTree.setValue(String.valueOf(permission.getId()));
            BeanUtils.copyProperties(permission, permissionTree);
            if (permission.getParentId() != 0) {
                if (permission.getResType().equals("data")) {
                    permissionTree.setTitle(permission.getUrl());
                    permissionTree.setKey(String.valueOf(permission.getId()));
                    SysPermissionTree parent = parentDataTemp.get(permission.getParentId());
                    if(parent.getChildren()==null){
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(permissionTree);
                    parentDataTemp.put(parent.getId(), parent);
                } else {
                    SysPermissionTree parent = parentTemp.get(permission.getParentId());
                    if(parent.getChildren()==null){
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(permissionTree);
                    parentTemp.put(parent.getId(), parent);
                }
            } else {
                if (permission.getResType().equals("data")) {
                    permissionTree.setTitle(permission.getResName());
                    parentDataTemp.put(permissionTree.getId(), permissionTree);
                } else {
                    parentTemp.put(permissionTree.getId(), permissionTree);
                }
            }
        }
        map.put("data", parentDataTemp.values());
        map.put("url", parentTemp.values());
        return map;
    }

    /**
     * 根据角色ID获取权限信息
     */
    @Override
    public Map<String, Object> getPermissionIdByRoleId(long roleId) {
        Map<String, Object> resources = new HashMap<>();
        Map<Long, Long> rolePermission = sysPermissionMapper.selectPermissionByRoleId(roleId)
                .stream()
                .collect(Collectors.toMap(SysPermission::getId, SysPermission::getId));
        List<SysPermission> permissions = sysPermissionMapper.selectList(null);
        List<Long> select = new ArrayList<>();
        List<Long> noSelect = new ArrayList();
        Map<String, Long> data = new HashMap<>();
        for (SysPermission s : permissions) {
            if (rolePermission.containsKey(s.getId())) {
                if (s.getResType().equals("data")) {
                    data.put(s.getPermission(), s.getId());
                } else {
                    select.add(s.getId());
                }
            } else {
                noSelect.add(s.getId());
            }
        }
        resources.put("select", select);
        resources.put("noSelect", noSelect);
        resources.put("data", data);
        return resources;
    }
}
