package com.fs.something.config;

import com.fs.something.pojo.UserContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class InterceptorConfig implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(InterceptorConfig.class);
    //@Autowired
    //private RedisOperator redis;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 进入controller层之前拦截请求
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        /*"---------------------验证用户会话时间是否到期----------------------------"*/
        // 所有请求第一个进入的方法
        String  sessionId = request.getHeader("sessionId");
        String  sessionId2 = request.getParameter("sessionId");
        String sessionid3 = request.getHeader("sessionid");
        String sessionid4 = request.getParameter("sessionid");
        log.info("sessionId:{}, sessionId2:{}, sessionid3:{}, sessionid4:{}", sessionId, sessionId2, sessionid3, sessionid4);
        if(sessionId2!=null){
            sessionId = sessionId2;
        }
        if(sessionid3!=null){
            sessionId = sessionid3;
        }
        if(sessionid4!=null){
            sessionId = sessionid4;
        }
        if (null != sessionId){
            if(stringRedisTemplate.hasKey("json:"+sessionId)){
                log.info("----刷新sessionid有效时间-->json:{}----",sessionId);
                stringRedisTemplate.expire("json:"+sessionId,604800,TimeUnit.SECONDS);//重新设定 redis 有效时间
                UserContext uc = UserContext.createUserContext(stringRedisTemplate, sessionId);
                //LoginCache lc = JsonUtils.jsonToPojo(redis.get("json:" + sessionId), LoginCache.class);
                /*if (null!=uc.getUnionid()&&!"".equals(uc.getUnionid())) {
                    redis.expire(uc.getUnionid(), 1800);
                }*/
                request.setAttribute("userContext", uc);
                return true;
            }else{
                log.info("----invalid sessionid({})----{}",sessionId,request.getServletPath());
                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json; charset=utf-8");
                response.getWriter().print("{\"result\":\"fail\",\"msg\":\"invalid sessionid!("+sessionId+")\",\"cause\":\"invalid sessionid!("+sessionId+")\",\"data\":\"\"}");
                return false;
            }

        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().print("{\"result\":\"fail\",\"msg\":\"no sessionid!\",\"cause\":\"no sessionid!\",\"data\":\"\"}");
        log.info("----no sessionid!----{}",request.getServletPath());
        return false;
    }
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

        //log.info("---------------------处理请求完成后视图渲染之前的处理操作----------------------------");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //log.info("---------------视图渲染之后的操作-------------------------0");
    }

}
