package com.fs.eurekaconsumerapp1;

import com.fs.diyutils.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@RestController
public class EurekaConsumerApp1Application {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumerApp1Application.class, args);
    }

    @RequestMapping("/splitUtil")
    public JsonResult splitUtil(@RequestParam String param){
        String url = "http://serviceApp1/splitUtil?param="+param;
        return JsonResult.success(null,restTemplate.getForObject(url, String.class));
    }

}
