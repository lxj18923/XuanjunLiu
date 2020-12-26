package com.qigetech.mark.triad.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import com.qigetech.mark.triad.entity.Triad;
import com.qigetech.mark.triad.entity.vo.TriadVO;
import com.qigetech.mark.triad.service.ITriadService;
import com.qigetech.mark.user.auth.utils.AuthUtils;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.service.user.IUserService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-07-21
 */
@RestController
@RequestMapping("/triad")
public class TriadController {

    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    @Autowired
    private ITriadService triadServiceImpl;

    @Autowired
    private IUserService userServiceImpl;


    @GetMapping("/getOrigin")
    public ResultBundle<LabelResultVO> getOrigin(){
        return resultBundleBuilder.bundle("get origin",()->triadServiceImpl.getOrigin());
    }

    @PostMapping
    public ResultBundle<Boolean> save(@RequestBody Triad triad, Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        Long userId = user.getId();
        triad.setUserId(userId.intValue());
        triad.setMarkDate(new Date());
        return resultBundleBuilder.bundle("save triad",()->triadServiceImpl.save(triad));
    }

    @PutMapping
    public ResultBundle<Boolean> update(@RequestBody Triad triad, Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        Long userId = user.getId();
        triad.setUserId(userId.intValue());
        triad.setMarkDate(new Date());
        return resultBundleBuilder.bundle("save triad",()->triadServiceImpl.updateById(triad));
    }

    @GetMapping("/list")
    public ResultBundle<IPage<TriadVO>> getList(@RequestParam(name = "current",defaultValue = "0")long current,
                                                @RequestParam(name = "size",defaultValue = "20")long size){
        Page<Triad> page = new Page<>(current,size);
        return resultBundleBuilder.bundle("get list",()->
                triadServiceImpl.page(page,null)
        );
    }

}
