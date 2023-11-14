package com.doyoulikemi4i.boot3demo.controller;

import com.doyoulikemi4i.boot3demo.annotation.RepeatSubmit;
import com.doyoulikemi4i.boot3demo.aop.RepeatSubmitChecker;
import com.doyoulikemi4i.boot3demo.dao.UserDao;
import com.doyoulikemi4i.boot3demo.domain.Sys_user;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @Resource
    private UserDao userDao;

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    @RepeatSubmit(interval = 10000)
    public String test(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        List<Sys_user> userList = userDao.selectList(null);
        System.out.println(userList);
        String params = null;
        if("application/json".equals(request.getHeader("Content-Type"))){
            params = RepeatSubmitChecker.threadLocal.get();
            RepeatSubmitChecker.threadLocal.remove();
        }else {
            params = new ObjectMapper().writeValueAsString(request.getParameterMap());
        }
        return params;
    }
}
