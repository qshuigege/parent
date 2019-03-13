package com.fs.nothing.kafka;

import com.fs.nothing.config.StaticResourceConfiguration;
import com.fs.nothing.service.WXBusinessService;
import com.fs.nothing.utils.CommonUtils;
import com.fs.nothing.utils.HttpClientUtils;
import com.fs.nothing.utils.JsonUtils;
import com.fs.nothing.wx.R_WX;
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

/**
 * Created by FS on 2018/7/17.
 */
@Component
public class KafkaReceiver {

    private static Logger log = LoggerFactory.getLogger(KafkaReceiver.class);

    @Autowired
    private WXBusinessService service;

    @Autowired
    private StringRedisTemplate redis;

    @Autowired
    private StaticResourceConfiguration staticRes;

    @KafkaListener(topics = {"template-message"})
    public void listen(ConsumerRecord<?, ?> record) {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {

            String message = (String)kafkaMessage.get();
            Map<String, Object> paramMap = JsonUtils.jsonToMap(message);
            String busicode;
            Map<String, String> allParamsMap = null;
            Map<String, Object> allParamsMapNew = null;
            String allParams = null;
            if(null != paramMap.get("allParams")){
                allParams = paramMap.get("allParams").toString();
                allParamsMap = CommonUtils.parseParamsStr(allParams);
                busicode = allParamsMap.get("BUSICODE");
            }else if(null != paramMap.get("allParamsNew")){
                allParamsMapNew = (Map<String, Object>)paramMap.get("allParamsNew");
                busicode = allParamsMapNew.get("busicode")==null?null:allParamsMapNew.get("busicode").toString();
            }else{
                if (null==paramMap.get("busicode")){
                    busicode = "0";
                }else {
                    busicode = paramMap.get("busicode").toString();
                }
            }

            log.info("allParamsMap-->{}, allParamsMapNew-->{}", allParamsMap, allParamsMapNew);
            String apiUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
            String apiUrlQywx = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";

            if ("0".equals(busicode)){
                Map<String, String> params = CommonUtils.parseParamsStr(paramMap.get("bbb").toString());
                //根据erpoid查到openid
                List<Map<String, Object>> openid = service.getBindedOpenidsByErpoid(params.get("ERPOID"));
                if(openid.size()<1){
                    log.info("该客户的B2B账号没有绑定任何微信号，无需推送消息。");
                }
                try {
                    if (R_WX.TEMPLATEMSG_FAHUO_KEY.equals(paramMap.get("aaa").toString())) {
                        //发货提醒
                        for (int i = 0; i < openid.size(); i++) {
                            String postData = "{    \"touser\": \"" + openid.get(i).get("openid") + "\",    \"template_id\": \"" + R_WX.TEMPLATEMSG_FAHUO + "\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \"" + params.get("1") + "\",            \"color\": \"#2F36BA\"        },        \"keyword1\": {            \"value\": \"" + params.get("2") + "\",            \"color\": \"#2F36BA\"        },        \"keyword2\": {            \"value\": \"" + params.get("3") + "\",            \"color\": \"#2F36BA\"        },        \"keyword3\": {            \"value\": \"" + params.get("4") + "\",            \"color\": \"#2F36BA\"        },\"keyword4\": {            \"value\": \"" + params.get("5") + "\",            \"color\": \"#2F36BA\"        },\"keyword5\": {            \"value\": \"" + params.get("6") + "\",            \"color\": \"#2F36BA\"        },\"remark\": {            \"value\": \"" + params.get("7") + "\",            \"color\": \"#2F36BA\"        }    }}";
                            //String result = HttpRequestUtils.requestStringPost(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                            String result = HttpClientUtils.postStringEntity(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                            log.info("推送模板消息-发货提醒{}",result);
                        }
                        //response.getWriter().print("ok");

                    } else if (R_WX.TEMPLATEMSG_SHOUHUO_KEY.equals(paramMap.get("aaa").toString())) {
                        //收获提醒
                        for (int i = 0; i < openid.size(); i++) {
                            String postData = "{    \"touser\": \"" + openid.get(i).get("openid") + "\",    \"template_id\": \"" + R_WX.TEMPLATEMSG_SHOUHUO + "\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \"" + params.get("1") + "\",            \"color\": \"#2F36BA\"        },        \"keyword1\": {            \"value\": \"" + params.get("2") + "\",            \"color\": \"#2F36BA\"        },        \"keyword2\": {            \"value\": \"" + params.get("3") + "\",            \"color\": \"#2F36BA\"        },        \"keyword3\": {            \"value\": \"" + params.get("4") + "\",            \"color\": \"#2F36BA\"        },\"remark\": {            \"value\": \"" + params.get("5") + "\",            \"color\": \"#2F36BA\"        }    }}";
                            //String result = HttpRequestUtils.requestStringPost(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                            String result = HttpClientUtils.postStringEntity(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                            log.info("推送模板消息-收货提醒{}",result);
                        }
                        //response.getWriter().print("ok");

                    }else if (R_WX.TEMPLATEMSG_SHCONFIRM_KEY.equals(paramMap.get("aaa"))){
                        //送货签收确认
                        for (int i = 0; i < openid.size(); i++) {
                            String postData = "{    \"touser\": \"" + openid.get(i).get("openid") + "\",    \"template_id\": \"" + R_WX.TEMPLATEMSG_SHCONFIRM + "\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \"" + params.get("1") + "\",            \"color\": \"#2F36BA\"        },        \"keyword1\": {            \"value\": \"" + params.get("2") + "\",            \"color\": \"#2F36BA\"        },        \"keyword2\": {            \"value\": \"" + params.get("3") + "\",            \"color\": \"#2F36BA\"        },        \"keyword3\": {            \"value\": \"" + params.get("4") + "\",            \"color\": \"#2F36BA\"        },\"keyword4\": {            \"value\": \"" + params.get("5") + "\",            \"color\": \"#2F36BA\"        },\"keyword5\": {            \"value\": \"" + params.get("6") + "\",            \"color\": \"#2F36BA\"        },\"remark\": {            \"value\": \"" + params.get("7") + "\",            \"color\": \"#2F36BA\"        }    }}";
                            //String result = HttpRequestUtils.requestStringPost(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                            String result = HttpClientUtils.postStringEntity(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                            log.info("推送模板消息-送货签收提醒{}",result);
                        }
                    }
                }catch (Exception e){
                    log.error("推送模板消息异常！busicode:{}-->{}",busicode, e.getMessage());
                }





            }else if("1".equals(busicode)){
                log.info("busicode-->1, {}", paramMap);
                String openids = paramMap.get("openids")==null?"":paramMap.get("openids").toString();
                if ("1".equals(staticRes.getDachuang_templatemsg_extra_flag())) {
                    String miniUrl = paramMap.get("miniUrl") == null ? "" : paramMap.get("miniUrl").toString();
                    if ("".equals(miniUrl)) {
                        openids = openids + staticRes.getDachuang_templatemsg_extra_openids();
                    }
                }
                String msgcontent = paramMap.get("msgcontent").toString();
                Map<String, String> params = CommonUtils.parseParamsStr(msgcontent);
                String[] split = openids.split(",");
                try {
                    for (int i = 0; i < split.length; i++) {
                        String postData = "{\"touser\":\"" + split[i] + "\",\"template_id\":\"" + R_WX.DACHUANG_TEMPLATEMSG_SHENPI + "\",\"url\":\"\",\"miniprogram\":{\"appid\":\"" + R_WX.DACHUANG_MINIPROG_APPID + "\",\"pagepath\":\"" + paramMap.get("miniUrl") + "\"},\"data\":{\"first\":{\"value\":\"" + params.get("1") + "\",\"color\":\"#2F36BA\"},\"keyword1\":{\"value\":\"" + params.get("2") + "\",\"color\":\"#2F36BA\"},\"keyword2\":{\"value\":\"" + params.get("3") + "\",\"color\":\"#2F36BA\"},\"keyword3\":{\"value\":\"" + params.get("4") + "\",\"color\":\"#2F36BA\"},\"keyword4\":{\"value\":\"" + params.get("5") + "\",\"color\":\"#2F36BA\"},\"keyword5\":{\"value\":\"" + params.get("6") + "\",\"color\":\"#2F36BA\"},\"remark\":{\"value\":\"" + params.get("7") + "\",\"color\":\"#2F36BA\"}}}";
                        String result = HttpClientUtils.postStringEntity(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                        log.info("推送模板消息-crm审批提醒(miniprog_appid:{}){}", R_WX.DACHUANG_MINIPROG_APPID, result);
                    }
                }catch (Exception e){
                    log.error("推送模板消息异常！busicode:{}-->{}",busicode, e.getMessage());
                }





            }else if("2".equals(busicode)){
                if (null != allParamsMap) {
                    allParamsMap.put("TOUSER", allParamsMap.get("TOUSER").replace(",", "|"));
                    try {
                        String postData = "{\"touser\":\"" + allParamsMap.get("TOUSER") + "\",\"msgtype\":\"textcard\",\"agentid\":" + allParamsMap.get("AGENTID") + ",\"textcard\":{\"title\":\"" + allParamsMap.get("1") + "\",\"description\":\"" + allParamsMap.get("2") + "\",\"url\":\"" + allParamsMap.get("URL") + "\",\"btntxt\":\"" + allParamsMap.get("3") + "\"}}";
                        String result = HttpClientUtils.postStringEntity(apiUrlQywx + redis.opsForValue().get("accesstoken_" + allParamsMap.get("AGENTID")), postData);
                        log.info("推送企业微信卡片消息(agentid:{})-->{}", allParamsMap.get("AGENTID"), result);
                    } catch (Exception e) {
                        log.error("推送模板消息异常！busicode:{}-->{}", busicode, e.getMessage());
                    }
                }else if (null != allParamsMapNew){
                    try {
                        String postData = JsonUtils.objectToJson(allParamsMapNew);
                        log.info("busicode-->2, postData-->{}", postData);
                        String result = HttpClientUtils.postStringEntity(apiUrlQywx + redis.opsForValue().get("accesstoken_"+allParamsMapNew.get("agentid")), postData);
                        log.info("推送企业微信卡片消息(agentid:{})-->{}", allParamsMapNew.get("agentid"), result);
                    }catch (Exception e){
                        log.error("推送企业微信卡片消息异常！busicode:{}-->{}",busicode, e.getMessage());
                    }
                }





            }else if("3".equals(busicode)){
                //allParamsMap.put("TOUSER", allParamsMap.get("TOUSER").replace(",","|"));
                try {
                    String postData = JsonUtils.objectToJson(allParamsMapNew);
                    log.info("busicode-->3, postData-->{}", postData);
                    String result = HttpClientUtils.postStringEntity(apiUrlQywx + redis.opsForValue().get("accesstoken_"+allParamsMapNew.get("agentid")), postData);
                    log.info("推送企业微信文本消息(agentid:{})-->{}", allParamsMapNew.get("agentid"), result);
                }catch (Exception e){
                    log.error("推送企业微信文本消息异常！busicode:{}-->{}",busicode, e.getMessage());
                }





            }else if("4".equals(busicode)){
                //allParamsMap.put("TOUSER", allParamsMap.get("TOUSER").replace(",","|"));
                try {
                    String postData = JsonUtils.objectToJson(allParamsMapNew);
                    log.info("busicode-->4, postData-->{}", postData);
                    String result = HttpClientUtils.postStringEntity(apiUrlQywx + redis.opsForValue().get("accesstoken_"+allParamsMapNew.get("agentid")), postData);
                    log.info("推送企业微信markdown消息(agentid:{})-->{}", allParamsMapNew.get("agentid"), result);
                }catch (Exception e){
                    log.error("推送企业微信markdown消息异常！busicode:{}-->{}",busicode, e.getMessage());
                }





            }else if ("999".equals(busicode)){
                String postData = "{\"touser\":\"oAZwf1eG35xEUN6CWsz6gJHnXRb0\",\"template_id\":\"" + R_WX.DACHUANG_TEMPLATEMSG_SHENPI + "\",\"url\":\"\",\"miniprogram\":{\"appid\":\"" + R_WX.DACHUANG_MINIPROG_APPID + "\",\"pagepath\":\"" + allParamsMap.get("MINIURL") + "\"},\"data\":{\"first\":{\"value\":\"" + allParamsMap.get("1") + "\",\"color\":\"#2F36BA\"},\"keyword1\":{\"value\":\"" + allParamsMap.get("2") + "\",\"color\":\"#2F36BA\"},\"keyword2\":{\"value\":\"" + allParamsMap.get("3") + "\",\"color\":\"#2F36BA\"},\"keyword3\":{\"value\":\"" + allParamsMap.get("4") + "\",\"color\":\"#2F36BA\"},\"keyword4\":{\"value\":\"" + allParamsMap.get("5") + "\",\"color\":\"#2F36BA\"},\"keyword5\":{\"value\":\"" + allParamsMap.get("6") + "\",\"color\":\"#2F36BA\"},\"remark\":{\"value\":\"" + allParamsMap.get("7") + "\",\"color\":\"#2F36BA\"}}}";
                try {
                    String result = HttpClientUtils.postStringEntity(apiUrl + redis.opsForValue().get("accesstoken"), postData);
                    log.info("推送模板消息-测试allParams方式:{}", result);
                }catch (Exception e){
                    log.error("推送模板消息异常！busicode:{}-->{}",busicode, e.getMessage());
                }
            }else{
                log.info("未推送的卡夫卡消息-->{}/{}", allParams, allParamsMapNew);
            }
        }

    }
}
