package com.qigetech.mark.user.auth.config;

import com.google.gson.Gson;
import com.qigetech.mark.user.auth.bean.AjaxResponseBody;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AjaxAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        AjaxResponseBody responseBody = new AjaxResponseBody();

        responseBody.setStatus("403");
        responseBody.setMsg("没有权限");
        httpServletResponse.setStatus(403);
        httpServletResponse.getWriter().write(new Gson().toJson(responseBody));
    }
}
