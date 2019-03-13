package com.fs.something.qywx;

import com.fs.something.utils.HttpClientUtils;
import com.fs.something.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

public class QYWXUtils {

    public static Map<String, Object> getUserInfo(String userid, String redisAccessTokenKeySuffix, StringRedisTemplate redis) throws Exception{
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+redis.opsForValue().get("accesstoken_"+redisAccessTokenKeySuffix)+"&userid="+userid;
        String json;
        try {
            json = HttpClientUtils.get(url);
        } catch (Exception e) {
            throw new Exception("QYWXUtils.getUserInfo() httpclient exception! -->"+e.getMessage());
        }
        Map<String, Object> map = JsonUtils.jsonToMap(json);
        if (!"0".equals(map.get("errcode").toString())){
            throw new Exception("QYWXUtils.getUserInfo() exception! -->"+json);
        }
        return map;
    }
}


