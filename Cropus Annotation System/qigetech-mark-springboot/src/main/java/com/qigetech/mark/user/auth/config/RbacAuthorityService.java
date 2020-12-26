package com.qigetech.mark.user.auth.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qigetech.mark.user.entity.permission.SysPermission;
import com.qigetech.mark.user.entity.user.User;
import com.qigetech.mark.user.service.permission.ISysPermissionService;
import com.qigetech.mark.user.service.user.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Component("rbacauthorityservice")
public class RbacAuthorityService {

    @Autowired
    private IUserService userServiceImpl;

    @Autowired
    private ISysPermissionService sysPermissionServiceImpl;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    private String[] ignoreUrls = {
            "/auth/code/image","/code/image",
            "/permission/check","/auth/authentication/form",
            "/authentication/form","/auth/permission/check"
    };

    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String user ;
        if(principal==null){
            return false;
        }
        try {
            user = (String) principal;
        }catch (Exception e){
            SelfUserDetails userDetails = (SelfUserDetails) principal;
            user = userDetails.getUsername();
        }
        return hasPermission( user ,request.getRequestURI(), request.getMethod());
    }

    public boolean hasPermission(String user, String url, String method) {
        List<String> ignoreUrlList = Arrays.asList(ignoreUrls);
        if(ignoreUrlList.contains(url)){
            return true;
        }
        boolean hasPermission = false;
        long userId = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",user)).getId();
        //userId = 2733
        List<SysPermission> permissions = sysPermissionServiceImpl.getPermissionByUserId(userId);
        //URL匹配
        for (SysPermission permission : permissions) {
            String requestMethod = permission.getRequestMethod();
            String permissionUrl = permission.getUrl();
            if (antPathMatcher.match(permissionUrl, url)&& StringUtils.equals(requestMethod,method)) {
                hasPermission = true;
                break;
            }
        }
        return hasPermission;
    }
}
