package com.fs.myerp.po;

import javax.validation.constraints.NotBlank;

public class LoginPo {

    @NotBlank
    private String loginId;

    @NotBlank
    private String loginPwd;

    @NotBlank
    private String loginName;

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
