package com.fs.nothing.pojo;

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

    public LoginCache(){}

    public LoginCache(String userId, String sessionID, String clientID, int clientTYPE, String companyNAME, String unionid, String partnerId) {
        this.userId = userId;
        this.sessionID = sessionID;
        this.clientID = clientID;
        this.clientTYPE = clientTYPE;
        this.companyNAME = companyNAME;
        this.unionid = unionid;
        this.partnerId = partnerId;
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
}
