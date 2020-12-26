package com.qigetech.mark.result.label.entity.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by liuxuanjun on 2019-06-11
 * Project : qigetech-mark
 */
@Data
public class LabelResultVO {

    private Long id;

    private String markContent;

    private Integer originId;

    private Integer userId;

    private String username;

    private Date markDate;

    private List<String> diffString;
}
