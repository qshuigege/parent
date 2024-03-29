package com.doyoulikemi4i.boot3demo.kafka;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;



@Component
public class KafkaReceiver {

    private static Logger log = LoggerFactory.getLogger(KafkaReceiver.class);

    @Autowired
    private StringRedisTemplate redis;

    @KafkaListener(topics = {"quickstart-events"}, groupId = "g1")
    public void listen(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {

            String message = (String)kafkaMessage.get();
            log.info("消费消息-->{}", message);
        }

    }

    @KafkaListener(topics = {"quickstart-events"},groupId = "g1")
    public void listen2(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {

            String message = (String)kafkaMessage.get();
            log.info("消费消息2-->{}", message);
        }

    }
}
