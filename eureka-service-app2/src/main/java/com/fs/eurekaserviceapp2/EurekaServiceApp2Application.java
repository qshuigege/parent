package com.fs.eurekaserviceapp2;

import com.fs.diyutils.JsonResult;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class EurekaServiceApp2Application {

    @Autowired
    private EurekaClient eurekaClient;

    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApp2Application.class, args);
    }

    @RequestMapping("/testapp2")
    public JsonResult test(){
        return JsonResult.success(null, "test service app 2");
    }

    @RequestMapping("/testEurekaApp2")
    public JsonResult testEureka(){
        InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka("SERVICEAPP2", false);
        return JsonResult.success(null, instanceInfo.getHomePageUrl());
    }

}
