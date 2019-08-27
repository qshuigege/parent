package com.drzk.parkingguide.bo;

import java.util.Date;

public class UserContext {
    private String id;

    private Date createDate;

    private Date updateDate;

    private String loginId;

    private String loginName;

    private boolean ismanager;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public boolean isIsmanager() {
        return ismanager;
    }

    public void setIsmanager(boolean ismanager) {
        this.ismanager = ismanager;
    }

    @Override
    public String toString() {
        return "UserContext{" +
                "id='" + id + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", loginId='" + loginId + '\'' +
                ", loginName='" + loginName + '\'' +
                ", ismanager=" + ismanager +
                '}';
    }
}
