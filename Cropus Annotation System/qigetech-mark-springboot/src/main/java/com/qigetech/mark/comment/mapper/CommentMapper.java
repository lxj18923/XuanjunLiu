package com.qigetech.mark.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qigetech.mark.comment.entity.Comment;
import com.qigetech.mark.comment.entity.vo.CommentVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-06
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {

    @Select("SELECT qige_user.`name`, `comment`.id, `comment`.content, `comment`.user_id, `comment`.post_date," +
            "`comment`.origin_id, `comment`.parent_id FROM `comment`\n" +
            "INNER JOIN qige_user ON `comment`.user_id = qige_user.id WHERE parent_id = #{parentId} ORDER BY post_date ASC")
    @Results({
            @Result(column = "id",property = "id"),
            @Result(column = "name",property = "username"),
            @Result(column = "origin_id",property = "originId"),
            @Result(column = "post_date",property = "postDate")
    })
    List<CommentVO> selectCommentsByParentId(@Param("parentId") long parentId);

}
