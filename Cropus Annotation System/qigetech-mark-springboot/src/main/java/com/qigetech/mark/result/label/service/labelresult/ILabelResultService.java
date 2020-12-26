package com.qigetech.mark.result.label.service.labelresult;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.entity.dto.LabelResultDTO;
import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import com.qigetech.mark.user.entity.user.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-06-08
 */
public interface ILabelResultService extends IService<LabelResult> {
    /**
     * @Author panzejia
     * @Description 保存前端传来的标注结果
     * @Date 11:26 2019-06-11
     * @Param [labelResultVO, userId]
     * @return java.lang.Boolean
     **/
    Boolean save(LabelResultDTO labelResultVO, Long userId);
    Boolean update(LabelResultDTO labelResultDTO, Long userId);
    //Boolean saveProofreading(LabelResultDTO labelResultVO, Long userId);未使用
    Map<String,Object> getOriginListByRole(long current, long size, User user);
    //ProofreadingVO getProofreading();
    List<LabelResultVO> getLabelResultByOriginId (Integer originId);
    List<LabelResultVO> getSearchPageLabelResultByOriginId (Integer originId);
    HashMap<String,Integer> count(long userId);
    Boolean proofreading(int x,List<LabelResult> newResults);
    /** author：LXJ date：2020.2.1 */
    List<LabelResultVO> getLabelResultVO(Long userId,String language);
    //Boolean save2(LabelResultDTO labelResultDTO, Long userId);
}
