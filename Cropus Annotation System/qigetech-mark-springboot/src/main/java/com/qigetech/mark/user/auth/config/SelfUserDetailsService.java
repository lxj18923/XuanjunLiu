package com.qigetech.mark.user.auth.config;

import com.qigetech.mark.user.entity.user.User;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qigetech.mark.user.service.user.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class SelfUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userServiceImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //构建用户信息的逻辑(取数据库/LDAP等用户信息)

        SelfUserDetails userInfo = new SelfUserDetails();
        User user = userServiceImpl.getOne(new QueryWrapper<User>().eq("name",username));
        userInfo.setUsername(username);
        userInfo.setPassword(user.getPassword());

        Set authoritiesSet = new HashSet();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ADMIN");
        authoritiesSet.add(authority);
        userInfo.setAuthorities(authoritiesSet);

        return userInfo;
    }
}
