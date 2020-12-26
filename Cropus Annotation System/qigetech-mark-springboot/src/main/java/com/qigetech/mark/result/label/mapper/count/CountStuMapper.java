package com.qigetech.mark.result.label.mapper.count;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qigetech.mark.result.label.entity.count.CountStu;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface CountStuMapper extends BaseMapper<CountStu> {

    @Update("truncate table count_stu")
    void reCreateCountStu();

    @Select("SELECT `name` FROM `qige_user` WHERE id = #{userId}")
    String selectNameByUserId(int userId);
}
