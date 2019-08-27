package com.drzk.parkingguide.util;

public class JsonResponse {

    private int result;

    private String errCode;

    private Object data;

    public static final String SUCCESS = "000";
    public static final String SYS_ERR = "999";


    private JsonResponse(int result, String errCode, Object data){
        this.result = result;
        this.errCode = errCode;
        this.data = data;
    }

    public static JsonResponse success(Object data) {
        return new JsonResponse(1, SUCCESS, data);
    }

    public static JsonResponse fail(String errCode, Object data) {
        return new JsonResponse(0, errCode, data);
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errorCode) {
        this.errCode = errorCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
