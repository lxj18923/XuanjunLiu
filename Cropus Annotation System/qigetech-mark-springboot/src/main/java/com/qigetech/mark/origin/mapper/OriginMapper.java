package com.qigetech.mark.origin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qigetech.mark.origin.entity.Origin;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-06-08
 */
public interface OriginMapper extends BaseMapper<Origin> {

    @Select("SELECT id from origin WHERE status in (5,3,2) and id in (SELECT origin_id FROM `label_result` where user_id = #{userId} GROUP BY origin_id) and `language` = #{language}")
    List<Integer> countCVAll(@Param("userId") long userId, @Param("language") String language);

    @Select("SELECT id from origin WHERE status = 5 and id in (SELECT origin_id FROM `label_result` where user_id = #{userId} GROUP BY origin_id) and `language` = #{language}")
    List<Integer> countCVSuccess(@Param("userId") long userId, @Param("language") String language);

    //有序查找
    List<Origin>  findOriginByIds(@Param("Ids") List<Integer> Ids);

    @Select("WITH origin_TEMP AS(\n" +
            "SELECT origin.*\n" +
            "FROM origin, origin_user\n" +
            "WHERE origin.status = #{status}\n" +
            "AND origin.language = #{language}\n" +
            "AND origin_user.user_id = #{userId} \n" +
            "AND origin_user.sub_status != 1 AND origin_user.sub_status != 2\n" +
            "AND origin.id != origin_user.origin_id)\n" +
            "SELECT * FROM origin_TEMP ORDER BY RAND() LIMIT 1")
    Origin getOriginAfterCheck(@Param("userId") long userId, @Param("language") String language, @Param("status") int status);

    @Select("SELECT *\n" +
            "FROM origin\n" +
            "WHERE status = #{status}\n" +
            "AND language = #{language}\n" +
            "ORDER BY RAND() LIMIT 1")
    Origin getOriginWithoutCheck(@Param("language") String language, @Param("status") int status);

    @Select("SELECT `id` FROM `origin` " +
            "WHERE (`status` = 2 OR `status` = 1 OR `status` = 0) " +
            "AND `language` =  #{language} " +
            "ORDER BY RAND() " +
            "LIMIT #{needNum}")
    List<Integer> getOriginIdList(@Param("needNum") int needNum, @Param("language") String language);

}
