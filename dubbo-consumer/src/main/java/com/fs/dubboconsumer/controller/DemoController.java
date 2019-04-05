package com.fs.dubboconsumer.controller;

import com.fs.dubboapi.DemoService;
import com.fs.diyutils.JsonResult;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Reference
    private DemoService demoService;

    @RequestMapping("sayHello")
    public JsonResult sayHello(HttpServletRequest request){
        String name = request.getParameter("name");
        String ret = demoService.sayHello(name);
        return JsonResult.success("", ret);
    }
}
