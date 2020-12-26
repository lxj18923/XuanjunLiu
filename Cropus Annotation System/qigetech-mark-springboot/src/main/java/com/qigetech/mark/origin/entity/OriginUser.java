package com.qigetech.mark.origin.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by liuxuanjun on 2019-07-08
 * Project : qigetech-mark
 */
@Data
public class OriginUser {
    private int id;
    private Integer userId;
    private Integer originId;
    private Integer sub_status;
    private Date sub_date;
}
