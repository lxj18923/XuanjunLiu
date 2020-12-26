package com.qigetech.mark.origin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qigetech.mark.origin.entity.OriginUser;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-06-08
 */
@Repository
public interface OriginUserMapper extends BaseMapper<OriginUser> {

    @Select("INSERT INTO origin_user ( origin_id, user_id, sub_status, sub_date) " +
            "VALUES ( #{originId}, #{userId}, 0, CURRENT_TIMESTAMP)")
    void insertNewOrigin(int originId, int userId);

    @Select("UPDATE origin_user SET sub_status = 1, sub_date = CURRENT_TIMESTAMP WHERE id = #{id}")
    void updateSubStatusAfterSkip(int id);

    @Select("UPDATE origin_user SET sub_status = 2, sub_date = CURRENT_TIMESTAMP WHERE id = #{id}")
    void updateSubStatusAfterSave(int id);

}
