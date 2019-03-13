package com.fs.dubboprovider.dubbo;

import com.alibaba.boot.dubbo.autoconfigure.DubboAutoConfiguration;
import com.alibaba.dubbo.config.annotation.Service;
import com.fs.dubboapi.DemoService;

@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        DubboAutoConfiguration a;
        return "hello, "+name;
    }
}
