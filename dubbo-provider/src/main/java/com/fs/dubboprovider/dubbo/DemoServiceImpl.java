package com.fs.dubboprovider.dubbo;

import com.fs.dubboapi.DemoService;
import org.apache.dubbo.config.annotation.Service;

@Service
public class DemoServiceImpl implements DemoService {
    @Override
    public String sayHello(String name) {
        return "hello, "+name;
    }
}
