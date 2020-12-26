package com.lxj.config.filter;

import com.lxj.common.LoginPublicUri;
import com.lxj.pojo.User;
import com.lxj.util.JsonUtil;
import com.lxj.util.RedisShardedPoolUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: 登录拦截过滤器（不用，用LoginInterceptor替换）
 * @version 1.0
 */
//@Aspect
//@Component
public class LoginFilter {

	@Pointcut("execution(* com.lxj.controller..*.*(..))")
	public void log(){	}

	/**
	 * 请求服务器之前的操作
	 */
	@Before("log()")
	public void logBeforeRequest(JoinPoint joinPoint) throws Exception{
		
		//获取请求
		ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = attributes.getRequest();
		
		//用于判断是否需要做登录校验
		boolean isNeedLogin = true;
		
		//请求存在于公开地址，则不需要做登录校验
		for(String uri : LoginPublicUri.LIST){
		    if(request.getRequestURI().startsWith(uri)){
				isNeedLogin = false;
				break;
		    }
		}
		
		//登录校验
		if(isNeedLogin){
		    
//		    HttpSession session = request.getSession();
//		    User user = (User) session.getAttribute(Const.CURRENT_USER);
			String loginToken = request.getHeader("login_token");
			String userJsonStr = RedisShardedPoolUtil.get(loginToken);
			User user = JsonUtil.string2Obj(userJsonStr,User.class);

            if(null == user){
                //需要在exceptionHandle中处理这个自定义异常
                throw new RuntimeException("1000");
            }
		}
	}
}
