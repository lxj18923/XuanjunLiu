package com.qigetech.mark.result.label.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


//测试批量插入的实体类，对应mapper在ResultMapping.xml
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class t_User {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String delFlag;
}
