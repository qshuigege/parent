package com.doyoulikemi4i.boot3demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class RedissonController {

    @Resource
    private RedissonClient redissonClient;

    @RequestMapping(value = "/testRedisson", method = RequestMethod.POST)
    public String testRedisson(HttpServletRequest request, HttpServletResponse response) {
            RLock lock = redissonClient.getLock("LOCK_KEY");
            try {
                if (!lock.tryLock(10, TimeUnit.SECONDS)) {
                    System.out.println("锁失败");
                }
                //模拟
                Thread.sleep(1000);
                System.out.println("操作业务逻辑");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (lock.isLocked() && lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }
        return null;
    }
}
