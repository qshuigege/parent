package com.fs.testemqx.controller;

import com.fs.diyutils.JsonResponse;
import com.fs.testemqx.mqtt.MqttBusinessHelper;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/testemqx")
public class TestEmqxController {

    @Resource
    private MqttBusinessHelper businessHelper;

    @RequestMapping("/testSend")
    public JsonResponse testSend(HttpServletRequest request) throws MqttException {
        String topic = request.getParameter("topic");
        String msg = request.getParameter("msg");
        businessHelper.send(topic, msg);
        return JsonResponse.success(null);
    }

}
