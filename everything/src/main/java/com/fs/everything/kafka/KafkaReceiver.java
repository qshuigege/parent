package com.fs.everything.kafka;/*
package com.fs.everything.kafka;

import com.fs.everything.service.WXBusinessService;
import com.fs.everything.utils.CommonUtils;
import com.fs.everything.utils.HttpClientUtils;
import com.fs.everything.utils.JsonUtils;
import com.fs.everything.utils.R_WX;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

*/
/**
 * Created by FS on 2018/7/17.
 *//*

@Component
public class KafkaReceiver {

    private static Logger log = LoggerFactory.getLogger(KafkaReceiver.class);

    @Autowired
    private WXBusinessService service;

    @Autowired
    private StringRedisTemplate redis;

    @KafkaListener(topics = {"template-message"})
    public void listen(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {

            String message = (String)kafkaMessage.get();
            Map<String, Object> paramMap = JsonUtils.jsonToMap(message);
            Map<String, String> params = CommonUtils.parseParamsStr(paramMap.get("bbb").toString());
            //根据erpoid查到openid
            List<Map<String, Object>> openid = service.getBindedOpenidsByErpoid(params.get("ERPOID"));
            if(openid.size()<1){
                log.info("该客户的B2B账号没有绑定任何微信号，无需推送消息。");
            }
            String apiUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
            try {
                if (R_WX.TEMPLATEMSG_FAHUO_KEY.equals(paramMap.get("aaa").toString())) {
                    //发货提醒
                    for (int i = 0; i < openid.size(); i++) {
                        String postData = "{    \"touser\": \"" + openid.get(i).get("openid") + "\",    \"template_id\": \"" + R_WX.TEMPLATEMSG_FAHUO + "\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \"" + params.get("1") + "\",            \"color\": \"#173177\"        },        \"keyword1\": {            \"value\": \"" + params.get("2") + "\",            \"color\": \"#173177\"        },        \"keyword2\": {            \"value\": \"" + params.get("3") + "\",            \"color\": \"#173177\"        },        \"keyword3\": {            \"value\": \"" + params.get("4") + "\",            \"color\": \"#173177\"        },\"keyword4\": {            \"value\": \"" + params.get("5") + "\",            \"color\": \"#173177\"        },\"keyword5\": {            \"value\": \"" + params.get("6") + "\",            \"color\": \"#173177\"        },\"remark\": {            \"value\": \"" + params.get("7") + "\",            \"color\": \"#173177\"        }    }}";
                        //String result = HttpRequestUtils.requestStringPost(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                        String result = HttpClientUtils.postStringEntity(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                        log.info("发货提醒{}",result);
                    }
                    //response.getWriter().print("ok");

                } else if (R_WX.TEMPLATEMSG_SHOUHUO_KEY.equals(paramMap.get("aaa").toString())) {
                    //收获提醒
                    for (int i = 0; i < openid.size(); i++) {
                        String postData = "{    \"touser\": \"" + openid.get(i).get("openid") + "\",    \"template_id\": \"" + R_WX.TEMPLATEMSG_SHOUHUO + "\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \"" + params.get("1") + "\",            \"color\": \"#173177\"        },        \"keyword1\": {            \"value\": \"" + params.get("2") + "\",            \"color\": \"#173177\"        },        \"keyword2\": {            \"value\": \"" + params.get("3") + "\",            \"color\": \"#173177\"        },        \"keyword3\": {            \"value\": \"" + params.get("4") + "\",            \"color\": \"#173177\"        },\"remark\": {            \"value\": \"" + params.get("5") + "\",            \"color\": \"#173177\"        }    }}";
                        //String result = HttpRequestUtils.requestStringPost(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                        String result = HttpClientUtils.postStringEntity(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                        log.info("收货提醒{}",result);
                    }
                    //response.getWriter().print("ok");

                }else if (R_WX.TEMPLATEMSG_SHCONFIRM_KEY.equals(paramMap.get("aaa"))){
                    //送货签收确认
                    for (int i = 0; i < openid.size(); i++) {
                        String postData = "{    \"touser\": \"" + openid.get(i).get("openid") + "\",    \"template_id\": \"" + R_WX.TEMPLATEMSG_SHCONFIRM + "\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \"" + params.get("1") + "\",            \"color\": \"#173177\"        },        \"keyword1\": {            \"value\": \"" + params.get("2") + "\",            \"color\": \"#173177\"        },        \"keyword2\": {            \"value\": \"" + params.get("3") + "\",            \"color\": \"#173177\"        },        \"keyword3\": {            \"value\": \"" + params.get("4") + "\",            \"color\": \"#173177\"        },\"keyword4\": {            \"value\": \"" + params.get("5") + "\",            \"color\": \"#173177\"        },\"keyword5\": {            \"value\": \"" + params.get("6") + "\",            \"color\": \"#173177\"        },\"remark\": {            \"value\": \"" + params.get("7") + "\",            \"color\": \"#173177\"        }    }}";
                        //String result = HttpRequestUtils.requestStringPost(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                        String result = HttpClientUtils.postStringEntity(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                        log.info("送货签收提醒{}",result);
                    }
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
        }

    }
}
*/
