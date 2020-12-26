package com.qigetech.mark.origin.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.elasticsearch.annotations.Document;

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
@Document(indexName = "origin_backup", type = "doc")
public class Origin implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String sentence;

    private String systemLabel;

    private String language;

    private String source;

    private Integer status;

    private Integer articleId;

    private Integer articleLocation;

    private String remark;

    private Date updateDate;

}
