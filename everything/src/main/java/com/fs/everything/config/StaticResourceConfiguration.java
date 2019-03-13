package com.fs.everything.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticResourceConfiguration {

    @Value("${project.init_data_flag}")
    private String init_data_flag;

    @Value("${wechat.appid_miniprog}")
    private String appid_miniprog;

    @Value("${wechat.appsecret_miniprog}")
    private String appsecret_miniprog;

    @Value("${wechat.originalid_miniprog}")
    private String originalid_miniprog;

    @Value("${wechat.appid_openplatform}")
    private String appid_openplatform;

    @Value("${wechat.appsecret_openplatform}")
    private String appsecret_openplatform;

    @Value("${qywx.corpid}")
    private String corpid;

    @Value("${qywx.miniprog_secret}")
    private String miniprog_secret;

    @Value("${qywx.miniprog_agentid}")
    private String miniprog_agentid;

    @Value("${qywx.miniprog_role}")
    private String miniprog_role;

    @Value("${qywx.miniprog_rolename}")
    private String miniprog_rolename;

    @Value("${qywx.tongxunlu_departmentid}")
    private String tongxunlu_departmentid;

    @Value("${qywx.tongxunlu_fetchchild}")
    private String tongxunlu_fetchchild;

    public String getInit_data_flag() {
        return init_data_flag;
    }

    public void setInit_data_flag(String init_data_flag) {
        this.init_data_flag = init_data_flag;
    }

    public String getAppid_miniprog() {
        return appid_miniprog;
    }

    public void setAppid_miniprog(String appid_miniprog) {
        this.appid_miniprog = appid_miniprog;
    }

    public String getAppsecret_miniprog() {
        return appsecret_miniprog;
    }

    public void setAppsecret_miniprog(String appsecret_miniprog) {
        this.appsecret_miniprog = appsecret_miniprog;
    }

    public String getOriginalid_miniprog() {
        return originalid_miniprog;
    }

    public void setOriginalid_miniprog(String originalid_miniprog) {
        this.originalid_miniprog = originalid_miniprog;
    }

    public String getAppid_openplatform() {
        return appid_openplatform;
    }

    public void setAppid_openplatform(String appid_openplatform) {
        this.appid_openplatform = appid_openplatform;
    }

    public String getAppsecret_openplatform() {
        return appsecret_openplatform;
    }

    public void setAppsecret_openplatform(String appsecret_openplatform) {
        this.appsecret_openplatform = appsecret_openplatform;
    }

    public String getCorpid() {
        return corpid;
    }

    public void setCorpid(String corpid) {
        this.corpid = corpid;
    }

    public String getMiniprog_secret() {
        return miniprog_secret;
    }

    public void setMiniprog_secret(String miniprog_secret) {
        this.miniprog_secret = miniprog_secret;
    }

    public String getMiniprog_agentid() {
        return miniprog_agentid;
    }

    public void setMiniprog_agentid(String miniprog_agentid) {
        this.miniprog_agentid = miniprog_agentid;
    }

    public String getMiniprog_role() {
        return miniprog_role;
    }

    public void setMiniprog_role(String miniprog_role) {
        this.miniprog_role = miniprog_role;
    }

    public String getMiniprog_rolename() {
        return miniprog_rolename;
    }

    public void setMiniprog_rolename(String miniprog_rolename) {
        this.miniprog_rolename = miniprog_rolename;
    }

    public String getTongxunlu_departmentid() {
        return tongxunlu_departmentid;
    }

    public void setTongxunlu_departmentid(String tongxunlu_departmentid) {
        this.tongxunlu_departmentid = tongxunlu_departmentid;
    }

    public String getTongxunlu_fetchchild() {
        return tongxunlu_fetchchild;
    }

    public void setTongxunlu_fetchchild(String tongxunlu_fetchchild) {
        this.tongxunlu_fetchchild = tongxunlu_fetchchild;
    }
}
