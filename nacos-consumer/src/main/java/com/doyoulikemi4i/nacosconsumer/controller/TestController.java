package com.doyoulikemi4i.nacosconsumer.controller;

import com.doyoulikemi4i.nacosconsumer.service.TestFeignService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Resource
    private TestFeignService service;

    @RequestMapping("/testFeign")
    public String testFeign(){
        //TestPo po = new TestPo("zhangsan", 22);
        //String ret1 = service.test1(po);
        //po = new TestPo("lisi", 23);
        //String ret2 = service.test2(po);
        //return ret1+ret2;
        return service.testVisitProvider();
    }

}
