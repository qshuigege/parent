package com.fs.everything.controller;

import com.fs.everything.service.WXBusinessService;
import com.fs.everything.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/miniprog/shconfirm")
public class MiniprogTestController {

    private static Logger log = LoggerFactory.getLogger(MiniprogTestController.class);

    @Autowired
    private WXBusinessService service;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redis;

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("defaultJdbcTemplate")
    private JdbcTemplate sqlserverJdbcTemplate;

    private KafkaTemplate kafkaTemplate;

    @RequestMapping("callback/wxMiniprogTestEntrance")
    public FusenJSONResult wxMiniprogEntrance(HttpServletRequest request){
        String code = request.getParameter("code");
        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");
        if (null==code||"".equals(code)){
            log.info("参数code为空！");
            return FusenJSONResult.failMsg("参数code为空！");
        }
        Map<String, Object> loginResult;
        try {
            loginResult = miniprogLogin(code);
            log.info("MiniprogController.wxMiniprogEntrance() loginResult-->{}", JsonUtils.objectToJson(loginResult));
        } catch (Exception e) {
            log.error("MiniprogController.wxMiniprogEntrance() exception! -->{}", e.getMessage());
            return FusenJSONResult.failMsg("MiniprogController.wxMiniprogEntrance() exception! -->"+e.getMessage());
        }

        String openid = loginResult.get("openid").toString();
        //String unionid = loginResult.get("unionid").toString();
        String session_key = loginResult.get("session_key").toString();
        String decryptData = null;
        try {
            decryptData = AESUtils.decrypt(encryptedData, session_key, iv);
            log.info("解密后-->{}", decryptData);
        } catch (Exception e) {
            log.error("MiniprogController.wxMiniprogEntrance() 解密exception! -->{}", e.getMessage());
            return FusenJSONResult.failMsg("MiniprogController.wxMiniprogEntrance() 解密exception! -->" + e.getMessage());
        }
        Map<String, Object> accMap = JsonUtils.jsonToMap(decryptData);


        return FusenJSONResult.buildSuccess(accMap);
    }


    private static Map<String, Object> miniprogLogin(String code) throws Exception{
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=wx5fc118ce1c4c3157&secret=1ca0baffcd702617daeac9700f4af4ae&js_code="+code+"&grant_type=authorization_code";
        String json;
        try {
            json = HttpClientUtils.get(url);
        }catch (Exception e){
            throw new Exception("WXUtils.miniprogLogin() httpclient exception! -->"+e.getMessage());
        }
        Map<String, Object> map = JsonUtils.jsonToMap(json);
        if (null!=map.get("errcode")){
            //log.error("WXUtils.miniprogLogin() exception! -->{}",json);
            throw new Exception("WXUtils.miniprogLogin() exception! -->"+json);
        }
        return map;
    }


}
