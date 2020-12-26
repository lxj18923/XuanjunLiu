package com.qigetech.mark.origin.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qigetech.mark.origin.entity.Origin;
import com.qigetech.mark.origin.entity.vo.OriginLabelVO;
import com.qigetech.mark.origin.service.IOriginService;
import com.qigetech.mark.result.label.entity.vo.LabelResultVO;
import com.qigetech.mark.result.label.service.labelresult.ILabelResultService;
import com.qigetech.mark.user.auth.utils.AuthUtils;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.service.user.IUserService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author liuxuanjun
 * @since 2019-06-08
 */
@RestController
@RequestMapping("/origin")
public class OriginController {

    @Autowired
    private IOriginService originServiceImpl;

    @Autowired
    private ResultBundleBuilder resultBundleBuilder;  //实现数据序列化？

    @Autowired
    private IUserService userServiceImpl;

    @Autowired
    private ILabelResultService labelResultServiceImpl;

    @PostMapping  //这个方法有用到？？？
    public ResultBundle<Boolean> add(@RequestBody Origin origin){
        return resultBundleBuilder.bundle("Add origin",()->
                originServiceImpl.save(origin)
        );
    }

    @DeleteMapping("/{id}")   //根据id在origin表进行删除，这个有用到？
    public ResultBundle<Boolean> deleteById(@PathVariable String id){
        return resultBundleBuilder.bundle(id,()->
                originServiceImpl.removeById(id)
        );
    }

    @PutMapping  //修改origin表的数据，有用到？？
    public ResultBundle<Boolean> update(@RequestBody Origin origin){
        return resultBundleBuilder.bundle("Update origin",()->
                originServiceImpl.updateById(origin)
        );
    }

    @GetMapping("/{id}") //通过id获取origin表数据
    public ResultBundle<Origin> getById(@PathVariable String id){
        return resultBundleBuilder.bundle(id,()->
                originServiceImpl.getById(id)
        );
    }

    @GetMapping("/list")  //标注结果列表里的翻页查询，可看到json数据，？
    public ResultBundle<IPage<Origin>> getList(@RequestParam(name = "current",defaultValue = "0")long current,
                                               @RequestParam(name = "size",defaultValue = "20")long size){
        Page<Origin> page = new Page<>(current,size);
        return resultBundleBuilder.bundle("get list",()->
                originServiceImpl.page(page,null)
        );
    }

    @GetMapping("/random")
    public ResultBundle<Origin> random(Authentication authentication){
        String username = AuthUtils.getUsername(authentication); //spring security？？
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        Long userId = user.getId();
        String language = user.getLanguage();
        return resultBundleBuilder.bundle("get one by random",()->
                originServiceImpl.getOrigin(userId,language)
        );
    }

    /**author:LXJ date:2020/1/30 */
    @GetMapping("/mark")
    public ResultBundle<List<LabelResultVO>> random1(Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        Long userId = user.getId();
        String language = user.getLanguage();
        return resultBundleBuilder.bundle("get one by random1",()->
                labelResultServiceImpl.getLabelResultVO(userId,language)
        );
    }

    /**author：LXJ date：2020.2.8  */
    //由于选取数据算法的改变，这里的跳过操作，此接口需要包含一次getoriginbycheck的操作，否则无法完成跳过功能
    @GetMapping("/skip")
    public ResultBundle<Boolean> skip(Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        Long userId = user.getId();
        String language = user.getLanguage();
        return resultBundleBuilder.bundle("skip",()->
                originServiceImpl.skip(userId,language)
        );
    }

    @GetMapping("/page") //？？？
    public ResultBundle<IPage<OriginLabelVO>> getPage(@RequestParam(name = "current",defaultValue = "0")long current,
                                                      @RequestParam(name = "size",defaultValue = "20")long size,
                                                      Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        return resultBundleBuilder.bundle("get list",()->
                originServiceImpl.getOriginLabelPage(current,size,user)
        );
    }

    @PutMapping("/delete")
    public ResultBundle<Boolean>  deleteOrigin(Authentication authentication){
        String username = AuthUtils.getUsername(authentication);
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        Long userId = user.getId();
        String language = user.getLanguage();
        return resultBundleBuilder.bundle("update origin status",()->
                originServiceImpl.deleteOrigin(userId,language)
        );
    }

    @GetMapping("/searchPage")
    public ResultBundle<IPage<OriginLabelVO>> getOriginSearchPage(@RequestParam(name = "current",defaultValue = "1")long current,
                                                                  @RequestParam(name = "size",defaultValue = "20")long size,
                                                                  @RequestParam(name = "status",defaultValue ="5") int status,
                                                                  @RequestParam(name = "source",defaultValue ="") String source,
                                                                  @RequestParam(name = "word" ,defaultValue ="")String word){
        //默认显示所有没被标注过的句子
        return resultBundleBuilder.bundle("get searchPage list",()->
                originServiceImpl.getOriginSearchPage(current,size,status,source,word)
        );
    }

}
