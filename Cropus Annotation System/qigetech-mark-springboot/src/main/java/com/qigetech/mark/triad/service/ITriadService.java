package com.qigetech.mark.triad.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import com.qigetech.mark.triad.entity.Triad;
import com.qigetech.mark.triad.entity.vo.TriadVO;

import java.util.HashMap;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-21
 */
public interface ITriadService extends IService<Triad> {
    LabelResultVO getOrigin();
    Page<TriadVO> page(Page<Triad> page, Wrapper<Triad> queryWrapper);
    HashMap<String,Integer> count(long userId);
}
