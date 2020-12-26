package com.qigetech.mark.user.service.permission;


import com.qigetech.mark.user.entity.permission.SysRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
public interface ISysRolePermissionService extends IService<SysRolePermission> {
    boolean saveBySeleted(long roleId, String json) throws Exception;
}
