package com.drzk.parkingguide.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticResourceConfiguration {

    @Value("${camera.login.waitTime}")
    private String waitTime;

    @Value("${camera.login.tryTimes}")
    private String tryTimes;

    @Value("${camera.login.nConnectTime}")
    private String nConnectTime;

    @Value("${camera.login.nGetConnInfoTime}")
    private String nGetConnInfoTime;

    @Value("${led.conn.timeout}")
    private String timeout;

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(String tryTimes) {
        this.tryTimes = tryTimes;
    }

    public String getnConnectTime() {
        return nConnectTime;
    }

    public void setnConnectTime(String nConnectTime) {
        this.nConnectTime = nConnectTime;
    }

    public String getnGetConnInfoTime() {
        return nGetConnInfoTime;
    }

    public void setnGetConnInfoTime(String nGetConnInfoTime) {
        this.nGetConnInfoTime = nGetConnInfoTime;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }
}
