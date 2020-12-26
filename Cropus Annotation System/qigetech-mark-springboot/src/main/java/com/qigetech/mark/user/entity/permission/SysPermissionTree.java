package com.qigetech.mark.user.entity.permission;

import lombok.Data;

import java.util.List;

/**
 * Created by liuxuanjun on 2019-02-14
 * Project : user
 */
@Data
public class SysPermissionTree {

    private Long id;

    private String title;

    private String key;

    private String value;

    private Long parentId;

    private String resName;

    private String resType;

    private String permission;

    private String url;

    private String requestMethod;

    private int sort;

    private List<SysPermissionTree> children ;

}
