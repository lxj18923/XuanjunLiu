package com.qigetech.mark.triad.entity.vo;

import lombok.Data;

import java.util.Date;

/**
 * Created by liuxuanjun on 2019-07-21
 * Project : qigetech-mark
 */
@Data
public class TriadVO {

    private Integer id;

    private Integer originId;

    private String content;

    private Integer userId;

    private String originMarkContent ;

    private Date markDate;

}
