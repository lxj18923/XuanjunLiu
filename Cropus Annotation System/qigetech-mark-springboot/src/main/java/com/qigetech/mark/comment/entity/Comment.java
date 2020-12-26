package com.qigetech.mark.comment.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String content;

    private Integer userId;

    private LocalDateTime postDate;

    private Integer originId;

    private Integer parentId;


}
