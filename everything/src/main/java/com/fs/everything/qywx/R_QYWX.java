package com.fs.everything.qywx;

import com.fs.everything.config.WechatApplicationReadyEventListener;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;

public class R_QYWX {
    public static String CORPID;//"ww6850c9512db95bfe"企业ID
    public static List<String> SECRET_LIST = new ArrayList<>();//"QjXub2_nKoLAj8k3anxYvY0DQjd4pil2j9jkjQTeZrg"富森仓储管理系统小程序
    public static List<String> AGENTID_LIST = new ArrayList<>();
    public static String MINIPROG_ROLE;
    public static String MINIPROG_ROLENAME;

    static {
        CORPID = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getCorpid();
        String miniprog_secret = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getMiniprog_secret();
        String miniprog_agentid = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getMiniprog_agentid();
        String[] split = miniprog_secret.split(",");
        String[] splitAgen = miniprog_agentid.split(",");
        StringRedisTemplate redis = WechatApplicationReadyEventListener.getRedisOperator();
        for (int i = 0; i < split.length; i++) {
            SECRET_LIST.add(split[i]);
            AGENTID_LIST.add(splitAgen[i]);
            redis.opsForValue().set("agentid_"+splitAgen[i], split[i]);
        }
        MINIPROG_ROLE = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getMiniprog_role();
        MINIPROG_ROLENAME = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getMiniprog_rolename();
    }
}
