package com.fs.nothing.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PerformanceTimeDaoInterceptor {
    private static Logger log = LoggerFactory.getLogger(PerformanceTimeDaoInterceptor.class);

    @Pointcut("execution(* com.fs.nothing.repository.impl.*.*(..))")
    public void bbb(){}

    @Around("bbb()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable{

        long starttime = System.currentTimeMillis();
        Object o = pjp.proceed(pjp.getArgs());
        long endtime = System.currentTimeMillis();

        log.info("{}()方法执行时间-->{}毫秒", pjp.getSignature().getName(), endtime-starttime);
        return o;
    }
}
