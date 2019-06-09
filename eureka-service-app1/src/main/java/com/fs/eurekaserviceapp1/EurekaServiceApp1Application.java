package com.fs.eurekaserviceapp1;

import com.fs.diyutils.JsonResult;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Scanner;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaServiceApp1Application {

    @Autowired
    private EurekaClient eurekaClient;

    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApp1Application.class, args);

        /*System.out.print("please enter a port number: ");
        Scanner scanner = new Scanner(System.in);
        String port = scanner.nextLine();
        new SpringApplicationBuilder(EurekaServiceApp1Application.class).properties("server.port="+port).run(args);*/
    }

    @RequestMapping("/test")
    public JsonResult test(){
        return JsonResult.success(null, "test");
    }

    @RequestMapping("/testClient")
    public JsonResult testClient(){
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("SERVICEAPP1", false);
        return JsonResult.success(null, instanceInfo.getHomePageUrl());
    }

    @RequestMapping("/splitUtil")
    public JsonResult splitUtil(String param){
        String[] split = param.split("\\|");
        return JsonResult.success("", split);
    }

    @RequestMapping("/sayHello")
    public String sayHello(){
        return "hello";
    }

    @RequestMapping("/sayHello2")
    public String sayHello2(HttpServletRequest request){
        return "hello, url="+request.getRequestURL();
    }

    @RequestMapping("/testRequest")
    public String testRequest(HttpServletRequest request){
        return "name:"+request.getParameter("name")+", age:"+request.getParameter("age");
    }


    @RequestMapping("testMap")
    public String testMap(@RequestParam Map<String, Object> map){
        return "testMap success! map-->"+map;
    }

}
