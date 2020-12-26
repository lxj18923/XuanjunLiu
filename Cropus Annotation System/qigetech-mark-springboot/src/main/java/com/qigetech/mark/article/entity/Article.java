package com.qigetech.mark.article.entity;

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
 * @since 2019-06-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Document(indexName = "article_backup", type = "doc")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private Date postDate;

    private String content;

    private String url;

    private String source;


}
