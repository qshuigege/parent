package com.fs.testemqx.config;

import com.fs.testemqx.mqtt.MqttBusinessCallBack;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class MqttConfig {

    @Resource
    private MqttBusinessCallBack businessCallBack;

    @Value("${mqtt.config.mqttServer}")
	private String mqttServer;

    @Value("${mqtt.config.mqttUserName}")
	private String mqttUserName;

    @Value("${mqtt.config.mqttPwd}")
	private String mqttPwd;

    @Value("${mqtt.config.clientId}")
	private String clientId;

    @Value("${mqtt.config.topic}")
    private String topic;

    @Value("${mqtt.config.cleanSession}")
    private boolean cleanSession;

    public String getMqttServer() {
        return mqttServer;
    }

    public void setMqttServer(String mqttServer) {
        this.mqttServer = mqttServer;
    }

    public String getMqttUserName() {
        return mqttUserName;
    }

    public void setMqttUserName(String mqttUserName) {
        this.mqttUserName = mqttUserName;
    }

    public String getMqttPwd() {
        return mqttPwd;
    }

    public void setMqttPwd(String mqttPwd) {
        this.mqttPwd = mqttPwd;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Bean
    public MqttClient getMqttClient() throws MqttException {
        MqttClient mqttClient = new MqttClient(mqttServer, clientId, new MemoryPersistence());
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(cleanSession);
        options.setUserName(mqttUserName);
        options.setPassword(mqttPwd.toCharArray());
        options.setConnectionTimeout(3000);
        //options.setKeepAliveInterval();
        mqttClient.setCallback(businessCallBack);
        mqttClient.connect(options);
        return mqttClient;
    }
}
