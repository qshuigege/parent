package com.fs.teststarter.com.fs.teststarter.controller;

import com.fs.diyutils.JsonResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/teststarter")
public class TestStarterController {

    public JsonResult testSayHello(HttpServletRequest request){
        return JsonResult.success("", "");
    }
}
