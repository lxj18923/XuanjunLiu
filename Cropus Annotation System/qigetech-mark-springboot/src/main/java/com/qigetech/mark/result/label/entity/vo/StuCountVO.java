package com.qigetech.mark.result.label.entity.vo;

import lombok.Data;

/**
 * Created by liuxuanjun on 2019-06-11
 * Project : qigetech-mark
 */

//未排行的个人标注量分页功能，暂时弃用
@Data
public class StuCountVO {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer userId;
    private String username;
    private String lastMarkDate;
    private Integer countDaily;
    private Integer countWeek;
    private Integer countTotal;
}
