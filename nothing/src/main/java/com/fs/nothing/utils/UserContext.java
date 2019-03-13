package com.fs.nothing.utils;

import com.fs.nothing.pojo.LoginCache;
import org.springframework.data.redis.core.StringRedisTemplate;

public class UserContext {

    //@Autowired
    //private StringRedisTemplate stringRedisTemplate;

    private String erpuseroid;

    private String unionid;

    private String orgid;

    private String sessionId;


    private UserContext(StringRedisTemplate stringRedisTemplate, String sessionid){
        String contextJson = stringRedisTemplate.opsForValue().get("json:"+sessionid);
        LoginCache lc = JsonUtils.jsonToPojo(contextJson, LoginCache.class);
        this.erpuseroid = lc.getClientID();
        this.unionid = lc.getUnionid();
        this.orgid = lc.getUserId();
        this.sessionId = lc.getSessionID();
    }

    public static UserContext createUserContext(StringRedisTemplate stringRedisTemplate, String sessionid){
        return new UserContext(stringRedisTemplate,sessionid);
    }


    public String getErpuseroid() {
        return erpuseroid;
    }

    public void setErpuseroid(String erpuseroid) {
        this.erpuseroid = erpuseroid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
