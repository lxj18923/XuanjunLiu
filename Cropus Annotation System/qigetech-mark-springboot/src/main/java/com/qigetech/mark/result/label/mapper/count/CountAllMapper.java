package com.qigetech.mark.result.label.mapper.count;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qigetech.mark.result.label.entity.count.CountAll;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CountAllMapper extends BaseMapper<CountAll> {
    @Select("select * from (select id,amount,count_date from count_all order by id desc limit 7) a order by id")
    List<CountAll> countAllWeek();

}
