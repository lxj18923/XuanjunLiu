package com.qigetech.mark.user.mapper.permission;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qigetech.mark.user.entity.permission.SysPermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
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
public interface SysPermissionMapper extends BaseMapper<SysPermission> {
    /**
     * 根据用户id获取其拥有的权限
     */
    @Select("SELECT * FROM sys_permission permission INNER JOIN sys_role_permission rolePermission " +
            "on permission.id = rolePermission.permission_id AND rolePermission.role_id = " +
            "(SELECT role_id FROM sys_user_role WHERE user_id = #{userId})")
    @Results({
            @Result(property = "parentId",column = "parent_id"),
            @Result(property = "resName",column = "res_name"),
            @Result(property = "resType",column = "res_type"),
    })
    List<SysPermission> selectPermissionByUserId(@Param("userId") long userId);

    /**
     *  获取权限内容
     * @param userId 用户id
     * @param resId 资源id
     * @param type 资源类型
     * @return 资源列表
     */
    @Select("SELECT * FROM sys_permission permission INNER JOIN sys_role_permission rolePermission " +
            "on permission.id = rolePermission.permission_id AND rolePermission.role_id= " +
            "(SELECT role_id FROM sys_user_role WHERE user_id = #{userId}) " +
            "WHERE parent_id = #{resId} AND res_type = #{resType}")
    @Results({
            @Result(property = "parentId",column = "parent_id"),
            @Result(property = "resName",column = "res_name"),
            @Result(property = "resType",column = "res_type"),
    })
    List<SysPermission> selectPermissionByType(
            @Param("userId") long userId,
            @Param("resId") long resId,
            @Param("resType") String type
    );

    /**
     * 根据角色获取权限内容
     * @param roleId 角色id
     * @return 资源列表
     */
    @Select("SELECT * FROM sys_permission permission INNER JOIN sys_role_permission rolePermission " +
            "on permission.id = rolePermission.permission_id AND rolePermission.role_id= #{roleId}")
    @Results({
            @Result(property = "parentId",column = "parent_id"),
            @Result(property = "resName",column = "res_name"),
            @Result(property = "resType",column = "res_type"),
    })
    List<SysPermission> selectPermissionByRoleId(@Param("roleId") long roleId);
}
