package com.example.demo.config;

import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by 01367594 on 2017/11/2.
 */
@Configuration
public class RibbonConfig {
//    private HttpConnectionManager connManager = new HttpConnectionManager();
//    private ClientHttpRequestFactory crf=new HttpComponentsClientHttpRequestFactory(connManager.getHttpClient());
    @Bean
    RestTemplate restTemplate() {
//        return new RestTemplate(crf);
        return new RestTemplate(createClientHttpRequestFactory());
    }


//    @Bean
//    public IRule ribbonRule() {
//        return new RandomRule();//这里配置策略，和配置文件对应
//    }

    @Bean(name = "pollingConnectionManager")
    public PoolingHttpClientConnectionManager createPollingConnectionManager()
    {
        PoolingHttpClientConnectionManager phcm=new PoolingHttpClientConnectionManager();
        phcm.setMaxTotal(500);
        phcm.setDefaultMaxPerRoute(200);
      return phcm;
    }

    @Bean
    public HttpClientBuilder createHttpClientBuilder()
    {
        HttpClientBuilder hcb=HttpClientBuilder.create();
        hcb.setConnectionManager(createPollingConnectionManager());
        return hcb;
    }

    @Bean
    public ClientHttpRequestFactory createClientHttpRequestFactory()
    {
        HttpComponentsClientHttpRequestFactory chrf=new HttpComponentsClientHttpRequestFactory(createHttpClientBuilder().build());
        chrf.setConnectTimeout(3000);
        chrf.setReadTimeout(1000);
        chrf.setConnectionRequestTimeout(1000);
        return  chrf;
    }

}
