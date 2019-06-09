package com.fs.eurekaconsumerapp2feign.config.hystrix;

import com.fs.diyutils.JsonResult;
import com.fs.eurekaconsumerapp2feign.feign.FeignService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class ProducerServiceFeignClientHystrix implements FeignService {

    @Override
    public JsonResult splitUtil(String param) {
        return JsonResult.success("", "服务[serviceApp1]无法访问");
    }

    @Override
    public String sayHelloo() {
        return "no hello";
    }

    @Override
    public String sayHello2() {
        return "no hello2";
    }

    /*@Override
    public String testRequest(HttpServletRequest request) {
        return "testRequest error!";
    }*/

    @Override
    public String testRequest2(String name, String age) {
        return "testRequest2 error!-->name="+name+", age="+age;
    }

    @Override
    public String testMap(Map<String, Object> map) {
        return "testMap error!-->map:"+map;
    }
}