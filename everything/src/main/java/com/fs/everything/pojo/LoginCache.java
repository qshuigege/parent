package com.fs.everything.pojo;

/**
 *
 * @Description 登录平台是需要记录下的缓存内容,需要设置缓存到期时间为30分钟
 * @author chenb
 * @date 2018-4-18
 */
public  class LoginCache {

    /**
     * 用户ID
     */
    private String userId;
    /**
     * 会话ID
     */
    private String  sessionID;
    /**
     * 客户ID
     */
    private String  clientID;
    /**
     * 客户类型
     */
    private int  clientTYPE;
    /**
     * 公司名称
     */
    private String  companyNAME;

    private String unionid;

    private String partnerId;

    private String session_key;

    private String qywxuserid;

    private String qywxnickname;

    private String loginid;

    private String empnum;

    private String ucml_useroid;

    private String ucml_department_name;

    private String ucml_zrzx;

    private String ucml_audit;

    public LoginCache(){}

    public LoginCache(String userId, String sessionID, String clientID, int clientTYPE, String companyNAME, String unionid, String partnerId, String qywxuserid, String qywxnickname, String loginid, String empnum, String ucml_useroid, String ucml_department_name, String ucml_zrzx, String ucml_audit) {
        this.userId = userId;
        this.sessionID = sessionID;
        this.clientID = clientID;
        this.clientTYPE = clientTYPE;
        this.companyNAME = companyNAME;
        this.unionid = unionid;
        this.partnerId = partnerId;
        this.qywxuserid = qywxuserid;
        this.qywxnickname = qywxnickname;
        this.loginid = loginid;
        this.empnum = empnum;
        this.ucml_useroid = ucml_useroid;
        this.ucml_department_name = ucml_department_name;
        this.ucml_zrzx = ucml_zrzx;
        this.ucml_audit = ucml_audit;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public int getClientTYPE() {
        return clientTYPE;
    }

    public void setClientTYPE(int clientTYPE) {
        this.clientTYPE = clientTYPE;
    }

    public String getCompanyNAME() {
        return companyNAME;
    }

    public void setCompanyNAME(String companyNAME) {
        this.companyNAME = companyNAME;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
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
