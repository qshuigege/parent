package com.doyoulikemi4i.boot3demo.kafka;

import com.doyoulikemi4i.boot3demo.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * Created by FS on 2018/7/17.
 */
@Component
public class KafkaSender {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    //private Gson gson = new GsonBuilder().create();

    //发送消息方法
    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());

        System.out.println("+++++++++++++++++++++  message = {"+ JsonUtils.objectToJson(message)+"}");
        kafkaTemplate.send("quickstart-events", JsonUtils.objectToJson(message));
    }
}
