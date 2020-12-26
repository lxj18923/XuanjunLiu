package com.qigetech.mark.user.service.permission;


import com.qigetech.mark.user.entity.permission.Role;
import com.qigetech.mark.user.entity.permission.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
public interface ISysRoleService extends IService<SysRole> {
    List<Role> selectRoleByUserId(long userId);
}
