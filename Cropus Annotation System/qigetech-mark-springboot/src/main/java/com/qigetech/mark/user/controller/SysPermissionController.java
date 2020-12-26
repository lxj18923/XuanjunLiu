package com.qigetech.mark.user.controller;

import com.qigetech.mark.user.entity.permission.SysPermission;
import com.qigetech.mark.user.service.permission.ISysPermissionService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 权限api控制器
 * @author liuxuanjun
 * @since 2018-11-29
 */
@RestController
@RequestMapping("/user/resource")
public class SysPermissionController{

    @Autowired
    private ISysPermissionService sysPermissionServiceImpl;

    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    /**
     * 查询一个权限资源
     * path: /user/resource/{id}
     */
    @GetMapping("/{id}")
    public ResultBundle<SysPermission> query(@PathVariable("id") long id){
        return resultBundleBuilder.bundle("",()->sysPermissionServiceImpl.getById(id));
    }

    /**
     * 新建一个权限资源
     * path:/user/resource
     * {
     * 	"parentId": 0,
     * 	"resName": xxx,
     * 	"resType": "url",
     * 	"permission": xxx,
     * 	"url": xxx,
     * 	"sort": 10
     * }
     */
    @PostMapping
    public ResultBundle<Boolean> add(@RequestBody SysPermission sysPermission){
        return resultBundleBuilder.bundle("",()->sysPermissionServiceImpl.save(sysPermission));
    }

    /**
     * 更新一个权限资源
     */
    @PutMapping("/{id}")
    public ResultBundle<Boolean> update(@RequestBody SysPermission sysPermission, @PathVariable long id){
        sysPermission.setId(id);
        return resultBundleBuilder.bundle("",()->sysPermissionServiceImpl.updateById(sysPermission));
    }

    /**
     * 删除一个权限资源
     */
    @DeleteMapping("/{id}")
    public ResultBundle<Boolean> delete(@PathVariable("id")long id){
        return resultBundleBuilder.bundle("",()->sysPermissionServiceImpl.removeById(id));

    }

    /**
     * 获取所有的权限信息
     */
    @GetMapping("/list")
    @ResponseBody
    public ResultBundle<Object> getAll(){
        return resultBundleBuilder.bundle("",
                ()->sysPermissionServiceImpl.getPermissionTree()
        );
    }

}
