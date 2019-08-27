package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.camera.Res;
import com.drzk.parkingguide.util.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/testDaHuaRes")
    public JsonResponse testDaHuaRes(){
        return JsonResponse.success(Res.string().getYear());
    }
}
