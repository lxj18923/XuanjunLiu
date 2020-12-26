package com.qigetech.mark.user.entity.permission;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
@TableName("sys_user_role")
public class SysUserRole{

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long roleId;

}
