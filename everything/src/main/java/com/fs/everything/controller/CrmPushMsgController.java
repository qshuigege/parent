package com.fs.everything.controller;

import com.fs.everything.service.WXBusinessService;
import com.fs.everything.utils.FusenJSONResult;
import com.fs.everything.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/miniprog/shconfirm")
public class CrmPushMsgController {

    private static Logger log = LoggerFactory.getLogger(CrmPushMsgController.class);

    @Autowired
    private WXBusinessService service;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redis;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping("callback/pushTemplateMsg")
    public FusenJSONResult pushTemplateMsg(HttpServletRequest request){
        String busicode = request.getParameter("busicode");
        String msgcontent = request.getParameter("msgcontent");
        String openids = request.getParameter("openids");
        String miniUrl = request.getParameter("miniUrl");
        if (null==busicode||"".equals(busicode)||null==msgcontent||"".equals(msgcontent)||null==openids||"".equals(openids)||null==miniUrl){
            return FusenJSONResult.failMsg("参数busicode, msgcontent, openids, miniUrl不能为空！");
        }
        log.info("busicode:{}, openids:{}, miniUrl:{}", busicode, openids, miniUrl);
        Map<String, String> kafkaMap = new HashMap<>();
        kafkaMap.put("busicode", busicode);
        kafkaMap.put("msgcontent", msgcontent);
        kafkaMap.put("openids", openids);
        kafkaMap.put("miniUrl", miniUrl);
        try {
            kafkaTemplate.send("template-message", JsonUtils.objectToJson(kafkaMap));
            log.info("CrmPushMsgController.pushTemplateMsg()推送微信模板消息成功。{}", msgcontent);
        } catch (Exception e) {
            log.info("CrmPushMsgController.pushTemplateMsg()推送微信模板消息异常。{}-->{}", msgcontent, e.getMessage());
        }
        return FusenJSONResult.success();
    }
}
