package com.drzk.parkingguide.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class InterceptorConfig implements HandlerInterceptor {

    private Logger log = LoggerFactory.getLogger(InterceptorConfig.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        HttpSession session = request.getSession(false);
        if (null != session){
            log.debug("sessionid-->{}", session.getId());
            return true;
        }else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print("{\"result\":0,\"errCode\":\"998\",\"data\":\"无效的sessionid！请重新登录！\"}");
            return false;
        }
    }
}
