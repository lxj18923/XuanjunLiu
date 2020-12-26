package com.qigetech.mark.user.entity.permission;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * Created by liuxuanjun on 2019-03-12
 * Project : user
 */
@Data
@EqualsAndHashCode
@Accessors(chain = true)
public class Role {

    private static final long serialVersionUID = 1L;
    @TableId
    private Long id;

    private String name;

    private String expireDate;
}
