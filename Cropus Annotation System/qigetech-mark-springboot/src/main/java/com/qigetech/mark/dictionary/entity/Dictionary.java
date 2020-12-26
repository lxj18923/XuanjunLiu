package com.qigetech.mark.dictionary.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author liuxuanjun
 * @since 2020-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    private String word;

    private String partOfSpeech;

    private Integer count;


}
