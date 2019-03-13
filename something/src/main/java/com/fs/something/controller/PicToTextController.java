package com.fs.something.controller;

import com.fs.something.config.StaticResourceConfiguration;
import com.fs.something.utils.FusenJSONResult;
import com.fs.something.utils.HttpClientUtils;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/entwechat")
public class PicToTextController {

    private Logger log = LoggerFactory.getLogger(PicToTextController.class);

    @Autowired
    private StaticResourceConfiguration staticRes;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redis;

    @RequestMapping("pic2text")
    @ResponseBody
    public FusenJSONResult pic2text(HttpServletRequest request){
        String mediaid = request.getParameter("mediaid");
        String state = request.getParameter("state");
        String aiType = request.getParameter("aiType");
        if (null==mediaid||"".equals(mediaid.trim())||null==aiType||"".equals(aiType.trim())){
            return FusenJSONResult.failMsg("参数mediaid, aiType不能为空！");
        }
        log.info("mediaid-->{}, aiType-->{}, state-->{}", mediaid, aiType, state);
        String accesstoken = redis.opsForValue().get("accesstoken_"+state);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token="+accesstoken+"&media_id="+mediaid;
        log.info("qywx pic api-->{}", url);
        byte[] bytes;
        Map<String, Object> map;
        try {
            map = HttpClientUtils.getForByteArrayAndContentType(url);
            bytes = (byte[]) map.get("byte-array");
        } catch (Exception e) {
            log.error("MainController.pic2text() getForByteArray exception! -->{}", e.getMessage());
            return FusenJSONResult.failMsg("MainController.pic2text() getForByteArray exception! -->"+e.getMessage());
        }
        String url2 = staticRes.getShibie_pic_url();
        log.info("mongodb pic recognition api-->{}", url2);
        try {
            Map<String, Object> params = new HashMap<>();
            //params.put("customId", UUID.randomUUID().toString());
            //params.put("orderId", UUID.randomUUID().toString());
            String suffix = map.get("content-type").toString().split("\\/")[1];
            //String filename = UUID.randomUUID().toString();
            String filename = "图片识别";
            params.put("filename", filename);
            params.put("filenameWithSuffix", filename+"."+suffix);
            params.put("aiType", aiType);
            String result = HttpClientUtils.postStreamForString(url2, bytes, ContentType.APPLICATION_OCTET_STREAM, params);
            log.info("识别结果-->"+result);
            return FusenJSONResult.buildSuccess(result);
        } catch (Exception e) {
            log.error("PicToTextController.pic2text() exception! -->{}",e);
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }
}
