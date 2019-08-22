package com.fs.testemqx.listener;

import com.fs.testemqx.config.MqttConfig;
import com.fs.testemqx.mqtt.MqttBusinessHelper;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class MqttSubscribeListener implements ApplicationListener<ApplicationReadyEvent> {

    @Resource
    private MqttBusinessHelper businessHelper;

    @Resource
    private MqttConfig mqttConfig;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            businessHelper.subscribe(mqttConfig.getTopic());
            log.info("订阅主题{}成功！", mqttConfig.getTopic());
        } catch (MqttException e) {
            log.error("mqtt订阅异常，无法启动！");
            SpringApplication.exit(applicationReadyEvent.getApplicationContext(), ()->1);
        }
    }
}
