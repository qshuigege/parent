package com.fs.something.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 *
 * 注册拦截器
 * Created by SYSTEM on 2018/5/03.
 */
@Configuration
public class WebAppConfig implements WebMvcConfigurer {


    @Bean
    public InterceptorConfig  interceptorConfig(){
        return  new InterceptorConfig();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册自定义拦截器，添加拦截路径和排除拦截路径
        registry.addInterceptor(interceptorConfig()).addPathPatterns("/**").excludePathPatterns("/api/entwechat/callback/**");

    }


}
