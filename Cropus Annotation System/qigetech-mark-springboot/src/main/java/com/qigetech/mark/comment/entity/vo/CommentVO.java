package com.qigetech.mark.comment.entity.vo;

import lombok.Data;

/**
 * Created by liuxuanjun on 2019-07-07
 * Project : qigetech-mark
 */
@Data
public class CommentVO {


    /**
     * id : 7
     * content : 测试1ddddddddddddddd顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶
     * username : 用户1
     * postDate : 2019-7-6 17:43
     */

    private Integer id;
    private String content;
    private String username;
    private String postDate;
    private Integer originId;
}
