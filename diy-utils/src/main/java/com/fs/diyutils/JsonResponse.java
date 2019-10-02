package com.fs.diyutils;

public class JsonResponse {

    private String result;

    private int errCode;

    private Object data;


    private JsonResponse(String result, int errCode, Object data){
        this.result = result;
        this.errCode = errCode;
        this.data = data;
    }

    public static JsonResponse success(Object data) {
        return new JsonResponse("success", 0, data);
    }

    public static JsonResponse success(int errCode, Object data) {
        return new JsonResponse("success", errCode, data);
    }

    public static JsonResponse fail(Object data) {
        return new JsonResponse("fail", 0, data);
    }

    public static JsonResponse fail(int errCode, Object data) {
        return new JsonResponse("fail", errCode, data);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
