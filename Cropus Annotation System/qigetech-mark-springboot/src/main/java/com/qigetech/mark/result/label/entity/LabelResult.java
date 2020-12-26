package com.qigetech.mark.result.label.entity;

import com.baomidou.mybatisplus.annotation.TableName;
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
 * @since 2019-06-08
 */

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("label_result")
public class LabelResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String word;

    private String partOfSpeech;

    private Integer location;

    private Integer originId;

    private Integer userId;

    private Date markDate;


}
