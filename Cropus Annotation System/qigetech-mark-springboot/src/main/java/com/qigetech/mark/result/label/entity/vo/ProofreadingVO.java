package com.qigetech.mark.result.label.entity.vo;

import com.qigetech.mark.origin.entity.Origin;
import lombok.Data;

import java.util.List;

/**
 * Created by liuxuanjun on 2019-06-11
 * Project : qigetech-mark
 */
@Data
public class ProofreadingVO {

    private Origin origin;

    private List<LabelResultVO> labelResults;

}
