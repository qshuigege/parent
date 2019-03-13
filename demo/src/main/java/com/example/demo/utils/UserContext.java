package com.example.demo.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class UserContext {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


}
