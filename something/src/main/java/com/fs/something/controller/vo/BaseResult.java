package com.fs.something.controller.vo;

/**
 * Created by FS on 2018/5/10.
 */
public class BaseResult {
    private String result = "fail";
    private String cause = "exception";

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}
