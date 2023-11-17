package com.doyoulikemi4i.nacosconsumer.service;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "provider8001")
//@LoadBalancerClient
public interface TestFeignService {

    /*@PostMapping(value = "/testProvider", consumes = "application/json")
    String test1(@RequestBody TestPo po);

    @PostMapping(value = "/testProvider")
    String test2(@SpringQueryMap TestPo po);*/

    @LoadBalanced
    @RequestMapping("/testProvider?name=zhangsan&age=22")
    String testVisitProvider();

}
