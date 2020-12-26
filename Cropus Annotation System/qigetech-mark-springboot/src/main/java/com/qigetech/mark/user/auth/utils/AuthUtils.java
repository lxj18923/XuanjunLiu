package com.qigetech.mark.user.auth.utils;

import com.qigetech.mark.user.auth.config.SelfUserDetails;
import org.springframework.security.core.Authentication;

/**
 * Created by liuxuanjun on 2019-06-09
 * Project : qigetech-mark
 */
public class AuthUtils {

    public static String getUsername(Authentication authentication){
        Object principal = authentication.getPrincipal();
        String user ;
        if(principal==null){
            return null;
        }
        try {
            user = (String) principal;
        }catch (Exception e){
            SelfUserDetails userDetails = (SelfUserDetails) principal;
            user = userDetails.getUsername();
        }
        return user;
    }

}
