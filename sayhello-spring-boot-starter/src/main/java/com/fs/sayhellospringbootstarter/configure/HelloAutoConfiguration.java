package com.fs.sayhellospringbootstarter.configure;

import com.fs.sayhellospringbootstarter.properties.HelloProperties;
import com.fs.sayhellospringbootstarter.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(HelloProperties.class)
@ConditionalOnClass(HelloService.class)
@ConditionalOnProperty(prefix = "hello", value = "enabled", matchIfMissing = true)
public class HelloAutoConfiguration {

    @Autowired
    private HelloProperties helloProperties;

    @Bean
    @ConditionalOnMissingBean(HelloService.class)
    public HelloService helloService(){
        HelloService helloService = new HelloService();
        helloService.setName(helloProperties.getMsg());
        return helloService;
    }
}
