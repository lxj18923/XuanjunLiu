package com.qigetech.mark.user.service.permission;

import com.qigetech.mark.user.entity.permission.SysPermission;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
public interface ISysPermissionService extends IService<SysPermission> {
    List<SysPermission> getPermissionByUserId(long userId);
    List<SysPermission> selectPermissionByType(long userId, long resId, String type);
    List<SysPermission> getResourceByRoleId(long roleId);
    Map<String,Object> getPermissionIdByRoleId(long roleId);
    Map<String,Object>  getPermissionTree();
}
