package com.qigetech.mark.user.controller;


import com.qigetech.mark.user.auth.utils.AuthUtils;
import com.qigetech.mark.user.entity.user.User;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qigetech.mark.user.service.permission.ISysUserRoleService;
import com.qigetech.mark.user.service.user.IUserService;
import com.qigetech.utils.resultbundle.ResultBundle;
import com.qigetech.utils.resultbundle.ResultBundleBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 用户api
 *
 * @author liuxuanjun
 * @since 2018-11-14
 */
@RestController
@RequestMapping("/user/user")
public class UserController {

    @Autowired
    private IUserService userServiceImpl;

    @Autowired
    private ISysUserRoleService sysUserRoleServiceImpl;

    @Autowired
    private ResultBundleBuilder resultBundleBuilder;

    /**
     * 新建一个用户
     */
    @PostMapping
    public ResultBundle<Boolean> add(@RequestBody User user) {
        return resultBundleBuilder.bundle("", () -> userServiceImpl.save(user));
    }

    /**
     * 更新一个用户
     */
    @PutMapping("/{id}")
    public ResultBundle<Boolean> update(@RequestBody User user, @PathVariable Long id) {
        user.setId(id);
        return resultBundleBuilder.bundle("", () -> userServiceImpl.updateById(user));
    }

    /**
     * 删除一个用户
     */
    @DeleteMapping("/{id}")
    public ResultBundle<Boolean> delete(@PathVariable("id") long id) {
        return resultBundleBuilder.bundle("", () -> userServiceImpl.removeById(id));
    }

    @GetMapping("/{id}")
    public ResultBundle<User> queryById(@PathVariable("id") long id) {
        return resultBundleBuilder.bundle("", () -> userServiceImpl.getById(id));
    }

    @GetMapping("/list")
    public ResultBundle<IPage<User>> list(@RequestParam(name = "pageNum", defaultValue = "0") int pageNum,
                                          @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {// 1 10
        return resultBundleBuilder.bundle("", () ->
                userServiceImpl.page(new Page<>(pageNum, pageSize)));
    }

    /**
     * 查询用户的所有角色
     */
    @GetMapping("/{id}/role")
    public ResultBundle<List<Map<String, Object>>> seletedRolesByUserId(@PathVariable Long id) {
        return resultBundleBuilder.bundle("", () -> userServiceImpl.seletedRolesByUserId(id));
    }

    /**
     * 更新用户的角色
     */
    @PutMapping("/{id}/role")
    public ResultBundle<Boolean> updateUserRole(@RequestBody String roles, @PathVariable Long id) {
        return resultBundleBuilder.bundle("", () -> sysUserRoleServiceImpl.saveBySeleted(id, roles));
    }

    /**
     * 显示用户名
     */
    @GetMapping("/username")
    public ResultBundle<String> getUsername(Authentication authentication) {
        String username = AuthUtils.getUsername(authentication);
        return resultBundleBuilder.bundle("get username", () -> username);
    }


}
