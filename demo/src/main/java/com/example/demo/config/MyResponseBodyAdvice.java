package com.example.demo.config;

import com.example.demo.utils.FusenJSONResult;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class MyResponseBodyAdvice implements ResponseBodyAdvice {
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return false;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        if(o instanceof FusenJSONResult){
            o = FusenJSONResult.failMsg("are you ok?");
        }else if (o instanceof List){
            Map<String, String> map = new HashMap<>();
            map.put("111","aaa");
            map.put("222","bbb");
            List<Map<String, String>> list = new ArrayList<>();
            list.add(map);
            o = list;
        }

        Map<String, String> map = new HashMap<>();
        map.put("111","aaa");
        map.put("222","bbb");
        List<Map<String, String>> list = new ArrayList<>();
        list.add(map);
        o = list;
        return o;
    }
}
