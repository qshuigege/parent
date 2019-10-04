package com.company.erp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfigurerImpl implements WebMvcConfigurer {

    @Bean
    public InterceptorConfig interceptorConfig(){
        return new InterceptorConfig();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptorConfig())
                .addPathPatterns(
                        "/**"
                )//
                .excludePathPatterns(
                        "/static/**",//
                        "/test/**",//
                        "/error",//
                        "/login"
                );
    }
}
