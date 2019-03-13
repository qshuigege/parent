package com.example.demo.config;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class InterceptorConfig implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        String  sessionId = request.getHeader("sessionId");
        String  sessionId2 = request.getParameter("sessionId");
        String sessionid3 = request.getHeader("sessionid");
        String sessionid4 = request.getParameter("sessionid");
        if(sessionId2!=null){
            sessionId = sessionId2;
        }
        if(sessionid3!=null){
            sessionId = sessionid3;
        }
        if(sessionid4!=null){
            sessionId = sessionid4;
        }
        if ("22854798-79df-431b-a3b6-336260aad6fd".equals(sessionId)){
            return true;
        }else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().print("{\"result\":\"fail\",\"msg\":\"illegal sessionid!\"}");
            return false;
        }
    }
}
