package com.drzk.parkingguide.aop;

import com.drzk.parkingguide.bo.UserContext;
import com.drzk.parkingguide.util.JsonResponse;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Aspect
public class PermissionCheckInterceptor {

    private Logger log = LoggerFactory.getLogger(PermissionCheckInterceptor.class);

    @Pointcut("execution(* com.drzk.parkingguide.service.impl.*.permission*(..))")
    public void permissionPoint(){}

    @Around("permissionPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (null != args) {
            for (Object arg : args) {
                if (arg instanceof HttpServletRequest) {
                    HttpServletRequest request = (HttpServletRequest) arg;
                    UserContext uc = (UserContext) request.getSession(false).getAttribute("userContext");
                    log.debug("当前登录用户信息-->{}", uc);
                    if (uc.isIsmanager()) {
                        return pjp.proceed(pjp.getArgs());
                    } else {
                        return JsonResponse.fail("", "没有权限！请以管理员身份登录！");
                    }
                }
            }
            return pjp.proceed(pjp.getArgs());
        }else {
            return pjp.proceed(pjp.getArgs());
        }

    }

}
