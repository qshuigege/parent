package com.fs.eurekaconsumerapp2feign;

import com.fs.diyutils.JsonResult;
import com.fs.eurekaconsumerapp2feign.feign.FeignService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@RestController
public class EurekaConsumerApp2FeignApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumerApp2FeignApplication.class, args);
    }

    @Autowired
    private FeignService service;

    @RequestMapping("/splitUtilFeign")
    public JsonResult splitUtilFeign(String param){
        return service.splitUtil(param);
    }

    @RequestMapping("/sayHello")
    public JsonResult sayHello(){
        return JsonResult.success("", service.sayHelloo());
    }

    @RequestMapping("/sayHello2")
    public JsonResult sayHello2(){
        return JsonResult.success("", service.sayHello2());
    }

    /*@RequestMapping("/testRequest")
    public JsonResult testRequest(HttpServletRequest request){
        return JsonResult.success("", service.testRequest(request));
    }*/

    @RequestMapping("/testRequest2")
    public JsonResult testRequest2(HttpServletRequest request){
        String name = request.getParameter("name");
        String age = request.getParameter("age");
        return JsonResult.success("", service.testRequest2(name, age));
    }

    @RequestMapping("/testMap")
    public JsonResult testMap(@RequestParam Map<String, Object> map){
        return JsonResult.success("", service.testMap(map));
    }

}
