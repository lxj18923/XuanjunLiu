package com.qigetech.mark.result.label.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.entity.t_User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-06-08
 */
@Repository
public interface LabelResultMapper extends BaseMapper<LabelResult> {

    @Select("SELECT id FROM label_result  WHERE user_id = #{userId} AND to_days(mark_date) = to_days(now()) GROUP BY origin_id ")
    List<LabelResult> countDaily(@Param("userId") long userId);

    @Select("SELECT id FROM label_result  WHERE user_id = #{userId} AND YEARWEEK(date_format(mark_date,'%Y-%m-%d'),1) = YEARWEEK(now(),1) GROUP BY origin_id")
    List<LabelResult> countWeek(@Param("userId") long userId);

    @Select("SELECT id FROM label_result WHERE user_id = #{userId} GROUP BY origin_id ")
    List<LabelResult> countTotal(@Param("userId") long userId);

    @Select("SELECT id FROM `label_result` WHERE DATEDIFF(mark_date,NOW())=-1 GROUP BY origin_id,user_id")
    List<Integer> countAllDaily();

    //测试批量插入
    void insertBatch(List<t_User> list);
}
