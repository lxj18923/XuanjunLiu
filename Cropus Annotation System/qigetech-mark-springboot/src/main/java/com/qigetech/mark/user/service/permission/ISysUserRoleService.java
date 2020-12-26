package com.qigetech.mark.user.service.permission;


import com.qigetech.mark.user.entity.permission.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
public interface ISysUserRoleService extends IService<SysUserRole> {
    boolean save(Long userId);
    boolean save(Long userId, Long roleId);
    boolean saveBySeleted(Long userId, String json);
}
