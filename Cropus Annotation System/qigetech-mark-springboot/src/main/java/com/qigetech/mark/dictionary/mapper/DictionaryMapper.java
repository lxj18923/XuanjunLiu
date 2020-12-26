package com.qigetech.mark.dictionary.mapper;

import com.qigetech.mark.dictionary.entity.Dictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxuanjun
 * @since 2020-03-22
 */
@Repository
public interface DictionaryMapper extends BaseMapper<Dictionary> {

    //"SELECT * FROM `dictionary` WHERE (word = #{word}) ORDER BY count DESC LIMIT 1"
    @Select("SELECT * FROM `dictionary` WHERE word = #{word}")
    Dictionary findPart(@Param("word") String word);

}
