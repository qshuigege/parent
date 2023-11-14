package com.doyoulikemi4i.boot3demo.redis;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author fangkun
 * @create 2023-05-10-13:32
 */
@Component
public class RedisCache {
    @Resource
    RedisTemplate redisTemplate;

    public <T> void setCacheObject(final String key, final T value, Integer timeout, final TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
    }

    public <T> T getCacheObject(final String key) {
        ValueOperations<String,T> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }
}