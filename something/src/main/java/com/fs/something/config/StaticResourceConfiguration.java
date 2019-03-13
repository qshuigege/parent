package com.fs.something.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticResourceConfiguration {

    /*@Value("${qywx.miniprog_secret001}")
    private String miniprog_secret001;

    @Value("${qywx.miniprog_secret002}")
    private String miniprog_secret002;

    @Value("${qywx.miniprog_secret003}")
    private String miniprog_secret003;*/

    @Value("${qywx.kafka_topic}")
    private String kafka_topic;

    @Value("${qywx.upload_pic_url}")
    private String upload_pic_url;

    @Value("${qywx.shibie_pic_url}")
    private String shibie_pic_url;

    /*public String getMiniprog_secret001() {
        return miniprog_secret001;
    }

    public void setMiniprog_secret001(String miniprog_secret001) {
        this.miniprog_secret001 = miniprog_secret001;
    }

    public String getMiniprog_secret002() {
        return miniprog_secret002;
    }

    public void setMiniprog_secret002(String miniprog_secret002) {
        this.miniprog_secret002 = miniprog_secret002;
    }

    public String getMiniprog_secret003() {
        return miniprog_secret003;
    }

    public void setMiniprog_secret003(String miniprog_secret003) {
        this.miniprog_secret003 = miniprog_secret003;
    }*/

    public String getKafka_topic() {
        return kafka_topic;
    }

    public void setKafka_topic(String kafka_topic) {
        this.kafka_topic = kafka_topic;
    }

    public String getUpload_pic_url() {
        return upload_pic_url;
    }

    public void setUpload_pic_url(String upload_pic_url) {
        this.upload_pic_url = upload_pic_url;
    }

    public String getShibie_pic_url() {
        return shibie_pic_url;
    }

    public void setShibie_pic_url(String shibie_pic_url) {
        this.shibie_pic_url = shibie_pic_url;
    }
}
