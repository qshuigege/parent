package com.company.erp.controller;

import com.fs.diyutils.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/getAllUser")
    public JsonResponse getAllUser(){
        return JsonResponse.success("test getAllUser");
    }

}
