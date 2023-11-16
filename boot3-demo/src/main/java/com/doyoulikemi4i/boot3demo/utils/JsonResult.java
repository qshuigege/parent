package com.doyoulikemi4i.boot3demo.utils;

public class JsonResult {

    private String result;

    private String cause;

    private Object data;


    private JsonResult(String result, String cause, Object data){
        this.result = result;
        this.cause = cause;
        this.data = data;
    }

    public static JsonResult success(String cause, Object data) {
        return new JsonResult("success", cause, data);
    }

    public static JsonResult fail(String cause, Object data) {
        return new JsonResult("fail", cause, data);
    }

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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
