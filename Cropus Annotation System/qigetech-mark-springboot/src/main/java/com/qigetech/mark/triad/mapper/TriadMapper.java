package com.qigetech.mark.triad.mapper;

import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.triad.entity.Triad;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-21
 */
public interface TriadMapper extends BaseMapper<Triad> {
    @Select("SELECT * FROM triad  WHERE user_id = #{userId} AND to_days(mark_date) = to_days(now())  ")
    List<LabelResult> countDaily(@Param("userId") long userId);

    @Select("SELECT * FROM triad  WHERE user_id = #{userId} AND DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= mark_date ")
    List<LabelResult> countWeek(@Param("userId") long userId);

    @Select("SELECT * FROM triad  WHERE user_id = #{userId}  ")
    List<LabelResult> countTotal(@Param("userId") long userId);
}
