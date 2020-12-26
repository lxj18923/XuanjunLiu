package com.qigetech.mark.triad.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.service.IOriginService;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.triad.entity.Triad;
import com.qigetech.mark.triad.entity.vo.TriadVO;
import com.qigetech.mark.triad.mapper.TriadMapper;
import com.qigetech.mark.triad.service.ITriadService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-21
 */
@Service
public class TriadServiceImpl extends ServiceImpl<TriadMapper, Triad> implements ITriadService {

    @Autowired
    private IOriginService originServiceImpl;

    @Autowired
    private ILabelResultService labelResultServiceImpl;

    @Override
    public LabelResultVO getOrigin(){
        List<Origin> originList = originServiceImpl.list(new QueryWrapper<Origin>().eq("status",1).select("id"));
        List<Triad> triads = this.baseMapper.selectList(new QueryWrapper<Triad>().select("origin_id","id"));
        Map<Integer,List<Triad>> triadMap = triads.stream().collect(Collectors.groupingBy(Triad::getOriginId));
        Collections.shuffle(originList);
        for(Origin origin : originList){
            if(triadMap.get(origin.getId())==null){

                return labelResultServiceImpl.getSearchPageLabelResultByOriginId(origin.getId()).get(0);
            }
        }
        return null;
    }

    @Override
    public Page<TriadVO> page(Page<Triad> page, Wrapper<Triad> queryWrapper){
        Page<TriadVO> triadVOPage = new Page<>(page.getCurrent(),page.getSize());
        IPage<Triad> triadIPage = this.baseMapper.selectPage(page,queryWrapper);
        List<TriadVO> triadVOS = new ArrayList<>();
        for(Triad triad : triadIPage.getRecords()){
            TriadVO triadVO = new TriadVO();
            BeanUtils.copyProperties(triad,triadVO);
            triadVO.setOriginMarkContent(labelResultServiceImpl.getSearchPageLabelResultByOriginId(triad.getOriginId()).get(0).getMarkContent());
            triadVOS.add(triadVO);
        }
        triadVOPage.setRecords(triadVOS);
        triadVOPage.setTotal(triadIPage.getTotal());
        triadVOPage.setPages(triadIPage.getPages());
        return triadVOPage;
    }
    @Override
    public HashMap<String,Integer> count(long userId){
        HashMap<String,Integer> counts = new HashMap<>(3);
        List<LabelResult> totalResults = this.baseMapper.countTotal(userId);
        List<LabelResult> weekResults = this.baseMapper.countWeek(userId);
        List<LabelResult> dailyResults = this.baseMapper.countDaily(userId);
        counts.put("triadTotal",totalResults.size());
        counts.put("triadWeek",weekResults.size());
        counts.put("triadDaily",dailyResults.size());
        return counts;
    }
}
