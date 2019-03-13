package com.example.demo.authentication;

import com.example.demo.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

public class UserContext {

    private Map<String, Object> session;

    private UserContext(StringRedisTemplate stringRedisTemplate, String sessionKeyOfRedis) {
        String contextJson = stringRedisTemplate.opsForValue().get(sessionKeyOfRedis);
        this.session = JsonUtils.jsonToMap(contextJson);
    }

    public static UserContext createUserContext(StringRedisTemplate stringRedisTemplate, String sessionKeyOfRedis){
        return new UserContext(stringRedisTemplate, sessionKeyOfRedis);
    }
}
