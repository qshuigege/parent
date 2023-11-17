package com.doyoulikemi4i.nacosconsumer.service;

import com.doyoulikemi4i.nacosconsumer.po.Payment;
import com.doyoulikemi4i.nacosconsumer.utils.CommonResult;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "provider8001")
@LoadBalancerClient
public interface TestFeignService {

    /*@PostMapping(value = "/testProvider", consumes = "application/json")
    String test1(@RequestBody TestPo po);

    @PostMapping(value = "/testProvider")
    String test2(@SpringQueryMap TestPo po);*/

    //@LoadBalanced
    @RequestMapping("/testProvider?name=zhangsan&age=22")
    String testVisitProvider();

    @PostMapping(value = "/selectPaymentListByQuery")
    String selectPaymentListByQuery(@RequestBody Payment payment);

    //Get方式以对象传参，实体类必须有setter和getter方法
    @GetMapping(value = "/selectPaymentListByQuery2")
    String selectPaymentListByQuery2(@SpringQueryMap Payment payment);

}
