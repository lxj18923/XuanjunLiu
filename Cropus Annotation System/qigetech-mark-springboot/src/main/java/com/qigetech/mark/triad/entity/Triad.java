package com.qigetech.mark.triad.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Triad implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;

    private Integer originId;

    private String content;

    private Integer userId;

    private Date markDate;


}
