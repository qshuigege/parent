package com.fs.something.controller;

import com.fs.something.config.StaticResourceConfiguration;
import com.fs.something.utils.FusenJSONResult;
import com.fs.something.utils.JsonResult;
import com.fs.something.utils.JsonUtils;
import com.fs.something.utils.ReusableCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/entwechat")
public class KafkaController {

    private static Logger log = LoggerFactory.getLogger(KafkaController.class);

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Autowired
    private StaticResourceConfiguration staticRes;

    @RequestMapping("callback/pushMsg")
    public FusenJSONResult pushMsg(HttpServletRequest request){
        /*
            0:物流消息提醒
            1:crm审批消息
         */
        String allParams = request.getParameter("allParams");
        if (null==allParams||"".equals(allParams)){
            return FusenJSONResult.failMsg("参数allParams不能为空！");
        }
        Map<String, Object> kafkaMap = new HashMap<>();
        kafkaMap.put("allParams", allParams);
        try {
            kafkaTemplate.send(staticRes.getKafka_topic(), JsonUtils.objectToJson(kafkaMap));
            log.info("KafkaController.pushMsg()推送消息成功。{}", allParams);
            return FusenJSONResult.successMsg(allParams);
        } catch (Exception e) {
            log.info("KafkaController.pushMsg()推送消息异常。{}-->{}", allParams, e.getMessage());
            return FusenJSONResult.failMsg(allParams);
        }
    }

    @RequestMapping("callback/pushMsgNew")
    public JsonResult pushMsgNew(HttpServletRequest request){
        /*
            0:物流消息提醒
            1:crm审批消息
         */
        Map<String, Object> requestJsonData;
        try {
            requestJsonData = ReusableCodes.getRequestJsonData(request);
        }catch (Exception e){
            log.error("KafkaController.pushMsgNew() exception!-->{}", e.getMessage());
            return JsonResult.fail(e.getMessage(), "");
        }
        String busicode = requestJsonData.get("busicode")==null?null:requestJsonData.get("busicode").toString();
        if (null==busicode||"".equals(busicode)){
            return JsonResult.fail("参数内容格式错误！", "");
        }
        Map<String, Object> kafkaMap = new HashMap<>();
        kafkaMap.put("allParamsNew", requestJsonData);
        try {
            kafkaTemplate.send(staticRes.getKafka_topic(), JsonUtils.objectToJson(kafkaMap));
            log.info("KafkaController.pushMsgNew()推送消息成功。{}", requestJsonData);
            return JsonResult.success(requestJsonData.toString(), "");
        } catch (Exception e) {
            log.info("KafkaController.pushMsgNew()推送消息异常。{}-->{}", requestJsonData, e.getMessage());
            return JsonResult.fail(requestJsonData.toString(), "");
        }
    }
}
