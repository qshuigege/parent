package com.company.erp.controller;

import com.fs.diyutils.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("/hello")
    public JsonResponse hello(){
        return JsonResponse.success("hello, world!");
    }

}
