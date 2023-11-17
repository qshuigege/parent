package com.fs.eurekaconsumerapp2feign.feign;

import com.fs.diyutils.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@FeignClient(name = "serviceApp1")
public interface FeignService {
    @RequestMapping("/test")
    JsonResult test();

    @RequestMapping("/splitUtil")
    JsonResult splitUtil(String param);

    @RequestMapping("/sayHello")
    String sayHelloo();

    @RequestMapping("/sayHello2")
    String sayHello2();

    /*@RequestMapping("/testRequest")
    String testRequest(HttpServletRequest request);*/

    @RequestMapping("/testRequest")
    String testRequest2(@RequestParam("name") String name, @RequestParam("age") String age);

    @RequestMapping("/testMap")
    String testMap(@RequestParam Map<String, Object> map);
}
