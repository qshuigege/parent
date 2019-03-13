package com.fs.everything.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceTimeInterceptor {
    private static Logger log = LoggerFactory.getLogger(PerformanceTimeInterceptor.class);

    @Pointcut("execution(* com.fs.everything.controller.*.*(..))")
    public void aaa(){}

    @Around("aaa()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        long starttime = System.currentTimeMillis();
        Object o = pjp.proceed(pjp.getArgs());
        long endtime = System.currentTimeMillis();
        log.info("{}()方法执行时间-->{}毫秒", pjp.getSignature().getName(), endtime-starttime);
        return o;
    }
}
