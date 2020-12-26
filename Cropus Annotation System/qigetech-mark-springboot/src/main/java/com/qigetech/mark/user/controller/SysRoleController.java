package com.qigetech.mark.user.controller;


import com.qigetech.mark.user.entity.permission.SysRole;
import com.qigetech.mark.user.service.permission.ISysPermissionService;
import com.qigetech.mark.user.service.permission.ISysRolePermissionService;
import com.qigetech.mark.user.service.permission.ISysRoleService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  角色api
 * </p>
 *
 * @author liuxuanjun
 * @since 2018-11-29
 */
@RestController
@RequestMapping("/user/role")
public class SysRoleController{

    @Autowired
    private ISysRoleService sysRoleServiceImpl;

    @Autowired
    private ISysRolePermissionService sysRolePermissionServiceImpl;
    
    @Autowired
    private ISysPermissionService sysPermissionServiceImpl;
    
    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    /**
     * 新建一个角色
     * path: /user/role
     * {
     * 	"roleName":xxx
     * }
     */
    @PostMapping
    public ResultBundle<Boolean> add(@RequestBody SysRole sysRole){
        return resultBundleBuilder.bundle("",()->sysRoleServiceImpl.save(sysRole));
    }

    /**
     * 查询一个角色
     * path: /user/role/{id}
     */
    @GetMapping("/{id}")
    public ResultBundle<Map<String,Object>> query(@PathVariable("id") long id){
        Map<String,Object> map = new HashMap<>();
        map.put("role",sysRoleServiceImpl.getById(id));
        map.put("permission",sysPermissionServiceImpl.getPermissionIdByRoleId(id));
        return resultBundleBuilder.bundle("",()->map);
    }

    /**
     * 修改一个角色
     * path: /user/role/{id}
     * {
     * 	"roleName":xxx
     * }
     */
    @PutMapping("/{id}")
    public ResultBundle<Boolean> update(@PathVariable("id") long id, @RequestBody SysRole sysRole){
        sysRole.setId(id);
        return resultBundleBuilder.bundle("",()->sysRoleServiceImpl.updateById(sysRole));
    }
    /**
     * 删除一个角色
     * path: /user/role/{id}
     */
    @DeleteMapping("/{id}")
    public ResultBundle<Boolean> delete(@PathVariable("id")long id){
        return resultBundleBuilder.bundle("",()->sysRoleServiceImpl.removeById(id));
    }

    /**
     * 查询所有角色
     * path: /user/role/list
     */
    @GetMapping("/list")
    public ResultBundle<List<SysRole>> queryAll(){
        return resultBundleBuilder.bundle("",()->sysRoleServiceImpl.list(null));
    }
    
    /**
     * @Author panzejia
     * @Description 通过角色ID获取该角色拥有的权限ID
     * @Date 14:31 2019-02-14
     * @Param 
     * @return 返回该角色已有的权限和没有的权限列表
     **/
    @GetMapping("/{id}/resource")
    public ResultBundle<Map<String,Object>> seletedPermissionByRoleId(@PathVariable(name = "id")long roleId){
        Map<String,Object> map = sysPermissionServiceImpl.getPermissionIdByRoleId(roleId);
        return resultBundleBuilder.bundle("",()-> map );
    }
    /**
     * 绑定角色和权限资源的数据
     */
    @PostMapping("/{id}/resource")
    public ResultBundle<Boolean> permission(@RequestBody String json, @PathVariable Long id){
        return resultBundleBuilder.bundle("",()->sysRolePermissionServiceImpl.saveBySeleted(id,json));
    }
}
