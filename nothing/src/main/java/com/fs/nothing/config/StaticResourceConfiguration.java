package com.fs.nothing.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StaticResourceConfiguration {

    @Value("${project.init_data_flag}")
    private String init_data_flag;

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.appsecret}")
    private String appsecret;

    @Value("${wechat.originalid}")
    private String originalid;

    @Value("${wechat.appid_openplatform}")
    private String appid_openplatform;

    @Value("${wechat.appsecret_openplatform}")
    private String appsecret_openplatform;

    @Value("${wechat.domain}")
    private String domain;

    @Value("${wechat.templatemsg_fahuo}")
    private String templatemsg_fahuo;

    @Value("${wechat.templatemsg_shouhuo}")
    private String templatemsg_shouhuo;

    @Value("${wechat.templatemsg_shconfirm}")
    private String templatemsg_shconfirm;

    @Value("${wechat.retrytimes}")
    private int retrytimes;

    @Value("${wechat.capacity}")
    private int capacity;

    @Value("${wechat.duration}")
    private long duration;

    @Value("${wechat.dachuang_bind_qrcode_callback_url}")
    private String dachuang_bind_qrcode_callback_url;

    @Value("${wechat.dachuang_templatemsg_shenpi}")
    private String dachuang_templatemsg_shenpi;

    @Value("${wechat.dachuang_miniprog_appid}")
    private String dachuang_miniprog_appid;

    @Value("${wechat.dachuang_templatemsg_extra_openids}")
    private String dachuang_templatemsg_extra_openids;

    @Value("${wechat.dachuang_templatemsg_extra_flag}")
    private String dachuang_templatemsg_extra_flag;


    public String getInit_data_flag() {
        return init_data_flag;
    }

    public void setInit_data_flag(String init_data_flag) {
        this.init_data_flag = init_data_flag;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getOriginalid() {
        return originalid;
    }

    public void setOriginalid(String originalid) {
        this.originalid = originalid;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTemplatemsg_fahuo() {
        return templatemsg_fahuo;
    }

    public void setTemplatemsg_fahuo(String templatemsg_fahuo) {
        this.templatemsg_fahuo = templatemsg_fahuo;
    }

    public String getTemplatemsg_shouhuo() {
        return templatemsg_shouhuo;
    }

    public void setTemplatemsg_shouhuo(String templatemsg_shouhuo) {
        this.templatemsg_shouhuo = templatemsg_shouhuo;
    }

    public String getTemplatemsg_shconfirm() {
        return templatemsg_shconfirm;
    }

    public void setTemplatemsg_shconfirm(String templatemsg_shconfirm) {
        this.templatemsg_shconfirm = templatemsg_shconfirm;
    }

    public int getRetrytimes() {
        return retrytimes;
    }

    public void setRetrytimes(int retrytimes) {
        this.retrytimes = retrytimes;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDachuang_bind_qrcode_callback_url() {
        return dachuang_bind_qrcode_callback_url;
    }

    public void setDachuang_bind_qrcode_callback_url(String dachuang_bind_qrcode_callback_url) {
        this.dachuang_bind_qrcode_callback_url = dachuang_bind_qrcode_callback_url;
    }

    public String getDachuang_templatemsg_shenpi() {
        return dachuang_templatemsg_shenpi;
    }

    public void setDachuang_templatemsg_shenpi(String dachuang_templatemsg_shenpi) {
        this.dachuang_templatemsg_shenpi = dachuang_templatemsg_shenpi;
    }

    public String getDachuang_miniprog_appid() {
        return dachuang_miniprog_appid;
    }

    public void setDachuang_miniprog_appid(String dachuang_miniprog_appid) {
        this.dachuang_miniprog_appid = dachuang_miniprog_appid;
    }

    public String getDachuang_templatemsg_extra_openids() {
        return dachuang_templatemsg_extra_openids;
    }

    public void setDachuang_templatemsg_extra_openids(String dachuang_templatemsg_extra_openids) {
        this.dachuang_templatemsg_extra_openids = dachuang_templatemsg_extra_openids;
    }

    public String getDachuang_templatemsg_extra_flag() {
        return dachuang_templatemsg_extra_flag;
    }

    public void setDachuang_templatemsg_extra_flag(String dachuang_templatemsg_extra_flag) {
        this.dachuang_templatemsg_extra_flag = dachuang_templatemsg_extra_flag;
    }
}
