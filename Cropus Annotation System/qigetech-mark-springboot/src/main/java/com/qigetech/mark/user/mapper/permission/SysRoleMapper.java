package com.qigetech.mark.user.mapper.permission;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qigetech.mark.user.entity.permission.Role;
import com.qigetech.mark.user.entity.permission.SysRole;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
    /**
     * 根据用户id获取角色
     * @param userId 用户id
     * @return
     */
    @Select("SELECT * FROM sys_role role INNER JOIN sys_user_role userRole on role.id = userRole.role_id AND userRole.user_id = #{userId}")
    List<Role> selectRoleByUserId(@Param("userId") long userId);
}
