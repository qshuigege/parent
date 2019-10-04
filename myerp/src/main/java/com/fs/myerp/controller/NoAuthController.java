package com.fs.myerp.controller;

import com.fs.diyutils.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/noauth")
public class NoAuthController {

    @RequestMapping("session/invalid")
    public JsonResponse sessionInvalid(){
        return JsonResponse.fail("sessionid无效！");
    }
}
