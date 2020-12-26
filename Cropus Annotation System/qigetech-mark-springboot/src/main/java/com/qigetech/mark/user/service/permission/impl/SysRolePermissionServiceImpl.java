package com.qigetech.mark.user.service.permission.impl;

import com.qigetech.mark.user.entity.permission.SysRolePermission;
import com.qigetech.mark.user.mapper.permission.SysRolePermissionMapper;
import com.qigetech.mark.user.service.permission.ISysRolePermissionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Resource
    private SysRolePermissionMapper sysRolePermissionMapper;

    /**
     * 保存角色和权限的对应关系
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveBySeleted(long roleId, String json) throws Exception {
        Map<String, Object> map = new Gson().fromJson(json, Map.class);
        List<String> permissions = (List<String>) map.get("permissions");
        //获取原资源列表
        List<SysRolePermission> oldRolePermissions = sysRolePermissionMapper.selectList(
                new QueryWrapper<SysRolePermission>().eq("role_id", roleId)
        );
        List<String> oldPermissions = new ArrayList<>();
        List<String> oldPermissionsTemp = new ArrayList<>();
        for (SysRolePermission permission : oldRolePermissions) {
            String id = String.valueOf( permission.getPermissionId());
            oldPermissions.add(id);
            oldPermissionsTemp.add(id);
        }
        //删除已经被剔除的数据
        if (oldPermissionsTemp.size() != 0) {
            //在原数组内删除新数据，在数据库中删除被剔除的数据
            oldPermissionsTemp.removeAll(permissions);
            //依次删除被剔除的数据
            for (String permissionId : oldPermissionsTemp) {
                int delStatus = sysRolePermissionMapper.delete(
                        new QueryWrapper<SysRolePermission>()
                                .eq("permission_id",Long.valueOf(permissionId))
                                .eq("role_id",roleId)
                );
                if (delStatus != 1) {
                    throw new Exception("删除失败");
                }
            }
        }
        //在新的数组中过滤掉原数组的数据
        permissions.removeAll(oldPermissions);
        SysRolePermission sysRolePermission = new SysRolePermission();
        for(int i = 0 ;i<permissions.size();i++){
            if(permissions.get(i)==null){
                continue;
            }
            String id = String.valueOf(permissions.get(i));
            sysRolePermission.setRoleId(roleId);
            sysRolePermission.setPermissionId(Double.valueOf(id).longValue());
            sysRolePermissionMapper.insert(sysRolePermission);
        }
        return true;
    }

}
