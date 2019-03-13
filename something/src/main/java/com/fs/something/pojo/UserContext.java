package com.fs.something.pojo;

import com.fs.something.utils.JsonUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

public class UserContext {

    //@Autowired
    //private StringRedisTemplate stringRedisTemplate;

    private String erpuseroid;

    private String unionid;

    private String orgid;

    private String sessionId;

    private String session_key;

    private String qywxuserid;

    private String qywxnickname;

    private String loginid;

    private String empnum;

    private String ucml_useroid;

    private String ucml_department_name;

    private String ucml_zrzx;

    private String ucml_audit;

    private UserContext(StringRedisTemplate stringRedisTemplate, String sessionid){
        String contextJson = stringRedisTemplate.opsForValue().get("json:"+sessionid);
        LoginCache lc = JsonUtils.jsonToPojo(contextJson, LoginCache.class);
        this.erpuseroid = lc.getClientID();
        this.unionid = lc.getUnionid();
        this.orgid = lc.getUserId();
        this.sessionId = lc.getSessionID();
        this.session_key = lc.getSession_key();
        this.qywxuserid = lc.getQywxuserid();
        this.qywxnickname = lc.getQywxnickname();
        this.loginid = lc.getLoginid();
        this.empnum = lc.getEmpnum();
        this.ucml_useroid = lc.getUcml_useroid();
        this.ucml_department_name = lc.getUcml_department_name();
        this.ucml_zrzx = lc.getUcml_zrzx();
        this.ucml_audit = lc.getUcml_audit();
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

    public String getSession_key() {
        return session_key;
    }

    public void setSession_key(String session_key) {
        this.session_key = session_key;
    }

    public String getQywxuserid() {
        return qywxuserid;
    }

    public void setQywxuserid(String qywxuserid) {
        this.qywxuserid = qywxuserid;
    }

    public String getQywxnickname() {
        return qywxnickname;
    }

    public void setQywxnickname(String qywxnickname) {
        this.qywxnickname = qywxnickname;
    }

    public String getLoginid() {
        return loginid;
    }

    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }

    public String getEmpnum() {
        return empnum;
    }

    public void setEmpnum(String empnum) {
        this.empnum = empnum;
    }

    public String getUcml_useroid() {
        return ucml_useroid;
    }

    public void setUcml_useroid(String ucml_useroid) {
        this.ucml_useroid = ucml_useroid;
    }

    public String getUcml_department_name() {
        return ucml_department_name;
    }

    public void setUcml_department_name(String ucml_department_name) {
        this.ucml_department_name = ucml_department_name;
    }

    public String getUcml_zrzx() {
        return ucml_zrzx;
    }

    public void setUcml_zrzx(String ucml_zrzx) {
        this.ucml_zrzx = ucml_zrzx;
    }

    public String getUcml_audit() {
        return ucml_audit;
    }

    public void setUcml_audit(String ucml_audit) {
        this.ucml_audit = ucml_audit;
    }
}
