package com.fs.teststarter.com.fs.teststarter.controller;

import com.fs.diyutils.JsonResult;
import com.fs.sayhellospringbootstarter.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/teststarter")
public class TestStarterController {

    @Autowired
    private HelloService helloService;

    @RequestMapping("testSayHello")
    public JsonResult testSayHello(HttpServletRequest request){
        String s = helloService.sayHello();
        return JsonResult.success("", s);
    }
}
