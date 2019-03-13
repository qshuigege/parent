package com.example.demo.controller;

import com.example.demo.service.WXBusinessService;
import com.example.demo.utils.DBResource;
import com.example.demo.vo.DemoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/demo")
public class DemoController {

    /*@Autowired
    private KafkaTemplate kafkaTemplate;*/

    @Autowired
    private WXBusinessService service;

    @Autowired
    private StringRedisTemplate redis;

    @RequestMapping(value = "/testDemo")
    public DemoVO testDemo(@RequestParam(value = "file")MultipartFile mpf, HttpServletRequest request){
        try {
            String path = ResourceUtils.getURL("/upload").getPath();
            System.out.println("aaa-->"+path);
            System.out.println("bbb-->"+request.getServletPath());
            System.out.println("ccc-->"+request.getServletContext().getContextPath());
            System.out.println("ccc22-->"+request.getServletContext().getRealPath("/"));
            System.out.println("ddd-->"+request.getParameter("username"));
            System.out.println("eee-->"+mpf.getOriginalFilename());
            MultipartHttpServletRequest mReq = (MultipartHttpServletRequest)request;
            MultipartFile mpf2 = mReq.getFile("file");
            System.out.println(mpf2.getOriginalFilename());
            if(mpf == null){

                System.out.println("mpf is nullaaa");
            }else {
                System.out.println("oooook");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new DemoVO("aaa",111,new Date(),new BigDecimal(222),333.3,'a');
    }

    @RequestMapping("/testDemo2")
    public DemoVO testDemo2(MultipartFile m, HttpServletRequest request){
        try {
            request.getPart("part");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        }
        return new DemoVO("bbb",1111,new Date(),new BigDecimal(2222),3333.33,'a');
    }

    /*@RequestMapping("/testKafka")
    public Map<String, Object> testKafka(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        String msg = request.getParameter("msg");
        kafkaTemplate.send("template-message", msg);
        map.put("result",msg);
        return map;
    }*/

    @RequestMapping("/testMysql")
    public Map<String, Object> testMysql(HttpServletRequest request){
        String openid = request.getParameter("openid");
        Map<String, Object> unionid = null;
        try {
            unionid = service.getUnionidByOpenid(openid);
        } catch (Exception e) {
            unionid = new HashMap<>();
            unionid.put("fail", e.getMessage());
            //e.printStackTrace();
        }
        return unionid;
    }

    @RequestMapping("/testSqlserver")
    public List<Map<String, Object>> testSqlserver(HttpServletRequest request){
        String docno= request.getParameter("docno");
        Map<String, Object> params = new HashMap<>();
        params.put("docno", docno);
        List<Map<String, Object>> thInfo = null;
        try {
            thInfo = service.getTHInfo(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return thInfo;
    }

    @RequestMapping("/testRedis")
    public Set<String> testRedis(HttpServletRequest request){
        String key = request.getParameter("key");
        String value = request.getParameter("value");
        redis.opsForValue().set(key, value);
        Set<String> keys = redis.keys("*");
        return keys;
    }

    @RequestMapping(path = "/test/callback/testError", value = "/test/callback/testError",produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.GET,RequestMethod.POST})
    public Map<String, Object> testError(HttpServletRequest request, @RequestBody DBResource aaa) {
        Map<String, Object> keys = new HashMap<>();
        keys.put("aaa", "aaa");
        //throw new Exception("haha");
        return keys;
    }
    
    public static void main(String[] args) {
        String str = "47bd2575-509e-485f-aebf-412b5f985b40|符海华||工程建设中心";
        String[] split = str.split("\\|");
        System.out.println(split.length);
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }
    }
}
