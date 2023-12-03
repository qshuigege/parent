package com.doyoulikemi4i.gatewayorder.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @RequestMapping("/detail")
    public String detail(){
        return "order controller";
    }
}
