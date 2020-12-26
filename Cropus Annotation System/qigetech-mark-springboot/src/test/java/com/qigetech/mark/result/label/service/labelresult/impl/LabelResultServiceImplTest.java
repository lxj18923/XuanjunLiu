package com.qigetech.mark.result.label.service.labelresult.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qigetech.mark.dictionary.service.IDictionaryService;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.mapper.LabelResultMapper;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.service.user.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by liuxuanjun on 2019-07-21
 * Project : qigetech-mark
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LabelResultServiceImplTest {

    @Autowired
    private ILabelResultService labelResultServiceImpl;

    @Autowired
    private IDictionaryService dictionaryServiceImpl;

    @Autowired
    private IUserService userServiceImpl;

    @Autowired
    private LabelResultMapper labelResultMapper;

    @Test
    public void getOriginListByRole() {
        Page<LabelResult> page = new Page<>(0,20);
        QueryWrapper<LabelResult> queryWrapper = new QueryWrapper<LabelResult>().orderByDesc("mark_date").groupBy("origin_id").eq("user_id",1);
        IPage<LabelResult> labelResultIPage = labelResultServiceImpl.page(page,queryWrapper);
        List<LabelResult> labelResults = labelResultIPage.getRecords();
        for(LabelResult result : labelResults){
            System.out.println(result.getOriginId());
        }
        List<Integer> originIds = labelResults.stream().map(LabelResult::getOriginId).collect(Collectors.toList());
        System.out.println(originIds);
    }

    @Test
    public void getPartOfSpeech() {
        System.out.println(dictionaryServiceImpl.getPartOfSpeech("猪猪"));
    }

    @Test
    public void getCount(){
        long start = System.currentTimeMillis();

        List<LabelResult> labelResults = labelResultMapper.countTotal(2733);
        System.out.println(labelResults);

        long end = System.currentTimeMillis();
        System.out.println("---------------" + (start - end) + "---------------");
    }

    @Test
    public void testgetOriginListByRole(){
        long start = System.currentTimeMillis();

        User user = userServiceImpl.getById(1);
        Map<String,Object> map  = labelResultServiceImpl.getOriginListByRole(0,20, user);
        System.out.println(map);

        long end = System.currentTimeMillis();
        System.out.println("---------------" + (start - end) + "---------------");
    }

    @Test
    public void test(){
        long start = System.currentTimeMillis();

        List<LabelResult> labelResults = labelResultMapper.selectList(
                new QueryWrapper<LabelResult>().select("user_id").eq("origin_id",74526));

        for (LabelResult labelResult : labelResults) {
            //一旦查出有该用户，boolean改为true，退出for循环
            if(labelResult.getUserId() == 2733){
                //System.out.println("在labelresult中该用户已经标注过了");
                break;
            }
        }

        long end = System.currentTimeMillis();
        System.out.println("---------------" + (start - end) + "---------------");
    }



}