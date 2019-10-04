package com.fs.myerp.controller;

import com.fs.diyutils.JsonResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/test")
public class TestSpringSecurityController {

    @Resource
    private StringRedisTemplate redis;

    @RequestMapping("hello")
    public JsonResponse hello(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails){
            username = ((UserDetails) principal).getUsername();
        }else {
            username = principal.toString();
        }
        return JsonResponse.success("hello, spring-security!"+username);
    }

    @RequestMapping("testRedis")
    public JsonResponse testRedis(){
        String aaa = redis.opsForValue().get("aaa");
        return JsonResponse.success(aaa);
    }

}
