package com.doyoulikemi4i.boot3demo.kafka;

import com.doyoulikemi4i.boot3demo.utils.JsonResult;
import com.doyoulikemi4i.boot3demo.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/demo")
public class KafkaSenderController {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    //private Gson gson = new GsonBuilder().create();

    //发送消息方法
    @RequestMapping("testKafka")
    public JsonResult testKafka() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());

        System.out.println("+++++++++++++++++++++  message = {"+ JsonUtils.objectToJson(message)+"}");
        ListenableFuture send = (ListenableFuture) kafkaTemplate.send("quickstart-events", JsonUtils.objectToJson(message));
        return JsonResult.success("", send);
    }
}
