package com.fs.myerp.aop;

import com.fs.diyutils.JsonResponse;
import com.fs.diyutils.JsonResult;
import com.fs.myerp.bo.UserContext;
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
public class AopTest {

    private Logger log = LoggerFactory.getLogger(AopTest.class);

    @Pointcut("execution(* com.fs.myerp.service.impl.*.test*(..))")
    public void testPoint(){}

    @Around("testPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] args = pjp.getArgs();
        if (null != args) {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof HttpServletRequest){
                    HttpServletRequest request = (HttpServletRequest) args[i];
                    UserContext uc = (UserContext) request.getSession(false).getAttribute("userContext");
                    if ("admin".equals(uc.getLoginId())){
                        return pjp.proceed(pjp.getArgs());
                    }else {
                        return JsonResponse.fail("没有权限！请以管理员身份登录！");
                    }
                    /*String loginId = request.getParameter("loginId");
                    if ("admin".equals(loginId)){
                        long starttime = System.currentTimeMillis();
                        log.debug("被拦截的方法({}())开始执行，timestamp-->{}", pjp.getSignature().getName() ,starttime);
                        Object o = pjp.proceed(pjp.getArgs());
                        long endtime = System.currentTimeMillis();
                        log.info("{}()方法执行时间-->{}毫秒", pjp.getSignature().getName(), endtime-starttime);
                        return o;
                    }else {
                        return JsonResult.fail("没有权限", "loginId-->"+loginId);
                    }*/
                }
            }
            return pjp.proceed(pjp.getArgs());
        }else {
            return pjp.proceed(pjp.getArgs());
        }

    }

}
