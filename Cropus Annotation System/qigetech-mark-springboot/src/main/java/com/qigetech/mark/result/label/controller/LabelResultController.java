package com.qigetech.mark.result.label.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qigetech.mark.result.label.entity.LabelResult;
import com.qigetech.mark.result.label.entity.dto.LabelResultDTO;
import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.triad.service.ITriadService;
import com.qigetech.mark.user.auth.utils.AuthUtils;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.service.user.IUserService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-06-08
 */
@RestController
@RequestMapping("/label/result")
public class LabelResultController {

    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    @Autowired
    private ILabelResultService labelResultServiceImpl;

    @Autowired
    private IUserService userServiceImpl;

    @Autowired
    private ITriadService triadServiceImpl;


    @PostMapping
    public ResultBundle<Boolean> add(@RequestBody LabelResultDTO labelResultVO, Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        Long userId = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username)).getId();
        return resultBundleBuilder.bundle("Add label result",()->
                labelResultServiceImpl.save(labelResultVO,userId)
        );
    }

    @PutMapping
    public ResultBundle<Boolean> update(@RequestBody LabelResultDTO labelResultVO, Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        Long userId = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username)).getId();
        return resultBundleBuilder.bundle("update label result",()->
                labelResultServiceImpl.update(labelResultVO,userId)
        );
    }

    //未使用
//    @GetMapping("/proofreading")
//    public ResultBundle<ProofreadingVO> getProofreading(){
//        return resultBundleBuilder.bundle("",()->labelResultServiceImpl.getProofreading());
//    }

    //未使用
//    @PostMapping("/proofreading")
//    public ResultBundle<Boolean> saveProofreading(@RequestBody LabelResultDTO labelResultVO, Authentication authentication){
//        String username = AuthUtils.getUsername(authentication);
//        Long userId = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username)).getId();
//        return resultBundleBuilder.bundle("Add label result",()->
//                labelResultServiceImpl.saveProofreading(labelResultVO,userId)
//        );
//    }

    @GetMapping("/list")
    public ResultBundle<IPage<LabelResult>> getList(@RequestParam(name = "current",defaultValue = "0")long current,
                                                    @RequestParam(name = "size",defaultValue = "20")long size){
        Page<LabelResult> page = new Page<>(current,size);
        return resultBundleBuilder.bundle("get list",()->
                labelResultServiceImpl.page(page,null)
        );
    }

    @GetMapping("/count")
    public ResultBundle<Map<String,Integer>> getCount(Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        Long userId = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username)).getId();
        Map<String,Integer> count = labelResultServiceImpl.count(userId);
        count.putAll(triadServiceImpl.count(userId));
        return resultBundleBuilder.bundle("get count",()->
                count
        );
    }

    /**author:LXJ date:2020/2/10 */
    @GetMapping("/random")
    public ResultBundle<List<LabelResultVO>> random1(Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        Long userId = user.getId();
        String language = user.getLanguage();
        return resultBundleBuilder.bundle("get labelresultvo by random",()->
                labelResultServiceImpl.getLabelResultVO(userId,language)
        );
    }

}
