package com.qigetech.mark.comment.entity.dto;

import lombok.Data;

/**
 * Created by liuxuanjun on 2019-07-08
 * Project : qigetech-mark
 */
@Data
public class CommentDTO {

    private String content;

    private Integer originId;

    private Integer parentId;
}
