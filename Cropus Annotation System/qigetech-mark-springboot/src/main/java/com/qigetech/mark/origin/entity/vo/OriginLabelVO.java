package com.qigetech.mark.origin.entity.vo;

import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import lombok.Data;

import java.util.List;

/**
 * Created by liuxuanjun on 2019-06-17
 * Project : qigetech-mark
 */
@Data
public class OriginLabelVO {


    /**
     * id : 4
     * sentence : 这是一个测试1
     * systemLabel : 这是/n 一个/n 测试/n 1/num
     * language : 中文简体
     * source : 测试
     * status : 3
     * labelResults : [{"markContent":"这是/n 一个/n 测试/a 1/n ","originId":4,"userId":2641,"username":"renhonglin","markDate":"Jun 11, 2019 11:39:34 AM"},{"markContent":"这是/n 一个/n 测试/n 1/n ","originId":4,"userId":1,"username":"panzejia","markDate":"Jun 11, 2019 11:38:43 AM"}]
     */

    private Integer id;
    private String sentence;
    private String systemLabel;
    private String language;
    private String source;
    private Integer articleId;
    private Integer status;
    private List<LabelResultVO> labelResults;

}
