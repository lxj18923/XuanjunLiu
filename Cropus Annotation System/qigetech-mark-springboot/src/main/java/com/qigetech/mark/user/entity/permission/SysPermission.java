package com.qigetech.mark.user.entity.permission;

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
public class SysPermission{

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long parentId;

    private String resName;

    private String resType;

    private String permission;

    private String url;

    private String requestMethod;

}
