package com.qigetech.mark.origin.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.entity.vo.OriginLabelVO;
import com.qigetech.mark.origin.mapper.OriginMapper;
import com.qigetech.mark.origin.service.IOriginService;
import com.qigetech.mark.result.label.entity.dto.LabelResultDTO;
import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.service.user.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuxuanjun on 2019-08-26
 * Project : qigetech-mark
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OriginServiceImplTest {

    @Autowired
    private IOriginService originServiceImpl;

    @Autowired
    private ILabelResultService labelResultServiceImpl;

    @Autowired
    private IUserService userServiceImpl;

    @Resource
    private OriginMapper originMapper;

    @Test
    public void test(){
        LabelResultDTO labelResultDTO = new LabelResultDTO();
        labelResultDTO.setOriginId(30326);
        labelResultDTO.setWords(new ArrayList<>());
        labelResultServiceImpl.save(labelResultDTO, Long.valueOf(1));
    }

    @Test
    public void getStata(){
        List<Integer> list = new ArrayList<>();
        for(int i=0; i<1000; i++){
            Origin origin = originServiceImpl.getOriginByCheck(Long.valueOf(2837), "简体");
            list.add(origin.getStatus());
        }
        for (Integer integer : list) {
            if (integer == 3 || integer == 5){
                System.out.println(integer+"垃圾数据");
            }
        }
    }

    @Test
    public void testgetOriginByCheck(){
        long start = System.currentTimeMillis();

        Origin origin = originServiceImpl.getOriginByCheck(Long.valueOf(2733),"简体");
        System.out.println(origin);

        long end = System.currentTimeMillis();
        System.out.println("---------------" + (start - end) + "---------------");
    }

    @Test
    public void testgetOriginLabelPage(){
        User user = userServiceImpl.getById(2733);

        Page<OriginLabelVO> originLabelVOPage = new Page<>(0,20);
        List<OriginLabelVO> originLabelVOS = new ArrayList<>();
        Map<String,Object> map = labelResultServiceImpl.getOriginListByRole(0,20,user);

        List<Integer> originIds = (List<Integer>) map.get("originIds");
        long total = (long) map.get("total");
        List<Origin> origins = originMapper.selectBatchIds(originIds);
        //根据当前页获取分词内容
        for(Origin origin:origins){
            long start = System.currentTimeMillis();
            List<LabelResultVO> labelResultVOS = labelResultServiceImpl.getLabelResultByOriginId(origin.getId());
            OriginLabelVO originLabelVO = new OriginLabelVO();
            BeanUtils.copyProperties(origin,originLabelVO);
            originLabelVO.setLabelResults(labelResultVOS);
            originLabelVOS.add(originLabelVO);
            long end = System.currentTimeMillis();
            System.out.println("---------------" + (start - end) + "---------------");
        }

        originLabelVOPage.setTotal(total);
        originLabelVOPage.setRecords(originLabelVOS);
    }

}