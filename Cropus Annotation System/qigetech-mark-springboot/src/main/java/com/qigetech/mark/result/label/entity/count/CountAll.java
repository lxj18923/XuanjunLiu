package com.qigetech.mark.result.label.entity.count;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class CountAll {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer amount;

    private Date countDate;
}
