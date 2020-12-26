package com.qigetech.mark.user.auth.config;


import com.google.gson.Gson;
import com.qigetech.mark.user.auth.bean.AjaxResponseBody;
import com.qigetech.mark.user.auth.utils.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();

        responseBody.setStatus("200");
        responseBody.setMsg("Login Success!");

        SelfUserDetails userDetails = (SelfUserDetails) authentication.getPrincipal();

        String jwtToken = JwtTokenUtil.generateToken(userDetails.getUsername(), 31536000, "_secret");
        responseBody.setJwtToken(jwtToken);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.getWriter().write(new Gson().toJson(responseBody));
    }
}

