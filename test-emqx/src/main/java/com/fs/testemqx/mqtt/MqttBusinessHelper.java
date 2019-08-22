package com.fs.testemqx.mqtt;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MqttBusinessHelper {

    @Resource
    private MqttClient mqttClient;

    public void send(String topic, String msg) throws MqttException {
        send(0, false, topic, msg);
    }

    public void send(int qos,boolean retained,String topic,String pushMessage) throws MqttException {
        MqttMessage message = new MqttMessage();
        message.setQos(qos);
        message.setRetained(retained);
        message.setPayload(pushMessage.getBytes());
        MqttTopic mTopic = mqttClient.getTopic(topic);
        if(null == mTopic){
            log.error("topic not exist");
            throw new NullPointerException("topic not exist");
        }
        MqttDeliveryToken token;
        token = mTopic.publish(message);
        token.waitForCompletion();
    }

    public void subscribe(String topic) throws MqttException {
        subscribe(topic, 0);
    }

    public void subscribe(String topic, int qos) throws MqttException {
        mqttClient.subscribe(topic);
    }

}
