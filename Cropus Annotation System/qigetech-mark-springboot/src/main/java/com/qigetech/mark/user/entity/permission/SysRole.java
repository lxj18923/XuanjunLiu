package com.qigetech.mark.user.entity.permission;

import com.baomidou.mybatisplus.annotation.TableId;
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
public class SysRole {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    private String name;


}
