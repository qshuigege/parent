package com.doyoulikemi4i.boot3demo.aop;

import com.doyoulikemi4i.boot3demo.annotation.RepeatSubmit;
import com.doyoulikemi4i.boot3demo.redis.RedisCache;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class RepeatSubmitChecker {

    private Logger log = LoggerFactory.getLogger(RepeatSubmitChecker.class);

    /*@Pointcut("execution(* com.drzk.parkingguide.service.impl.*.permission*(..))")
    public void permissionPoint(){}*/
    public static final String REPEAT_PARAMS = "repeat_params";
    public static final String REPEAT_TIME = "repeat_time";
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit_key";
    public static final String HEADER = "Authorization";

    @Resource
    RedisCache redisCache;

    public static final ThreadLocal<String> threadLocal = new ThreadLocal<>();

    @Pointcut("@annotation(com.doyoulikemi4i.boot3demo.annotation.RepeatSubmit)")
    public void permissionPoint(){}

    @Around("permissionPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (null != args) {
            HttpServletRequest request = null;
            HttpServletResponse response = null;
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    request = (HttpServletRequest) arg;
                }else if (arg instanceof HttpServletResponse){
                    response = (HttpServletResponse) arg;
                }
            }
            if (request != null && response != null){
                MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
                Method method = methodSignature.getMethod();
                RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);
                if (repeatSubmit != null){
                    if (isRepeatSubmit(request,repeatSubmit)){
                        //拦截返回错误信息
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("status",500);
                        map.put("message",repeatSubmit.message());
                        //response.setContentType("application/json;charset=utf-8");
                        //response.getWriter().write(new ObjectMapper().writeValueAsString(map));
                        return new ObjectMapper().writeValueAsString(map);
                    }else {
                        return pjp.proceed(pjp.getArgs());
                    }
                }else {
                    throw new Exception("DIY Common Exception.");
                }
            }else {
                throw new Exception("DIY Common Exception.");
            }

        }else {
            throw new Exception("DIY Common Exception.");
        }

    }

    private boolean isRepeatSubmit(HttpServletRequest request, RepeatSubmit repeatSubmit) throws IOException {
        //获取请求参数字符串
        String nowParams = "";
        String contentType = request.getHeader("Content-Type");
        //System.out.println(contentType);
        if("application/json".equals(contentType)){
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            nowParams = sb.toString();
            threadLocal.set(nowParams);
        }
        //否则说明参数是key-value格式
        if (StringUtils.isEmpty(nowParams)){
            nowParams = new ObjectMapper().writeValueAsString(request.getParameterMap());
        }

        //包装参数和当前时间
        HashMap<String, Object> nowDataMap = new HashMap<>();
        nowDataMap.put(REPEAT_PARAMS,nowParams);
        nowDataMap.put(REPEAT_TIME,System.currentTimeMillis());

        //获取请求信息，组装key
        String requestURI = request.getRequestURI();
        String header = request.getHeader(HEADER);
        String cacheKey = REPEAT_SUBMIT_KEY + requestURI + header.replace("Bearer ","");

        //根据key查找redis
        Object cacheObject = redisCache.getCacheObject(cacheKey);

        if (cacheObject != null){
            //这里说明不是第一次，判断是否为重复请求（参数、时间）
            Map<String, Object> cacheMap = (Map<String, Object>) cacheObject;
            if (compareParams(cacheMap, nowDataMap) && compareTime(cacheMap, nowDataMap, repeatSubmit.interval())){
                return true;
            }
        }

        //到这里说明是第一次访问
        redisCache.setCacheObject(cacheKey,nowDataMap,repeatSubmit.interval(), TimeUnit.MILLISECONDS);
        return false;
    }

    private boolean compareTime(Map<String, Object> cacheMap, HashMap<String, Object> nowDataMap, int interval) {
        Long nowTime = (Long) nowDataMap.get(REPEAT_TIME);
        Long cacheTime = (Long) cacheMap.get(REPEAT_TIME);
        if (nowTime - cacheTime < interval) {
            return true;
        }
        return false;
    }

    private boolean compareParams(Map<String, Object> cacheMap, HashMap<String, Object> nowDataMap) {
        String cacheParams = (String) cacheMap.get(REPEAT_PARAMS);
        String nowParams = (String) nowDataMap.get(REPEAT_PARAMS);
        return nowParams.equals(cacheParams);
    }

}
