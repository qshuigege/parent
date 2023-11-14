package com.doyoulikemi4i.boot3demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public class InterceptorConfig implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        /*HttpSession session = (HttpSession) request.getSession(false);
        if (null != session){
            log.debug("sessionid-->{}", session.getId());
            return true;
        }else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print("{\"result\":0,\"errCode\":\"998\",\"data\":\"无效的sessionid！请重新登录！\"}");
            return false;
        }*/
        String sessionid = request.getParameter("sessionid");
        System.out.println("进入权限拦截器："+sessionid);
        return true;
    }
}
