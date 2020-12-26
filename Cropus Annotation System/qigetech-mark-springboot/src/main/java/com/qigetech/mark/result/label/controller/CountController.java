package com.qigetech.mark.result.label.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qigetech.mark.result.label.entity.count.CountAll;
import com.qigetech.mark.result.label.entity.count.CountStu;
import com.qigetech.mark.result.label.mapper.count.CountAllMapper;
import com.qigetech.mark.result.label.service.count.ICountStuService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/count")
public class CountController {
    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    @Resource
    private CountAllMapper countAllMapper;

    @Resource
    private ICountStuService countStuServiceImpl;

    @GetMapping("/all")
    public ResultBundle<List<CountAll>> getMarkCount(){
        return resultBundleBuilder.bundle("get Mark count",()->
                countAllMapper.countAllWeek()
        );
    }

    @GetMapping("/stu")
    public ResultBundle<IPage<CountStu>> getStuCount(@RequestParam(name = "current",defaultValue = "0")long current,
                                                     @RequestParam(name = "size",defaultValue = "20")long size){
        return resultBundleBuilder.bundle("get stu count",()->
                countStuServiceImpl.page(new Page<>(current,size))
        );
    }
}
