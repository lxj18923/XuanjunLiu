package com.qigetech.mark.result.label.entity.count;


import lombok.Data;

@Data
public class CountStu {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer userId;
    private String username;
    private String lastMarkdate;
    private Integer countDaily;
    private Integer countWeek;
    private Integer countTotal;
}
