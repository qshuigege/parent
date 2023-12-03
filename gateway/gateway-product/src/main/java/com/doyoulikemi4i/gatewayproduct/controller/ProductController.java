package com.doyoulikemi4i.gatewayproduct.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/product")
@RestController
public class ProductController {

    @RequestMapping("/detail")
    public String detail(){
        return "product controller";
    }

    @PostMapping("/testAxios")
    public String testAxios(HttpServletRequest request){
        String aaa = request.getParameter("aaa");
        String bbb = request.getParameter("bbb");
        return aaa + "-" + bbb;
    }
}
