package com.fs.everything.utils;

/**
 * @Description 自定义响应数据结构
 * 				这个类是提供给门户，ios，安卓，微信商城用的
 * 				门户接受此类数据后需要使用本类的方法转换成对于的数据类型格式（类，或者list）
 * 				其他自行处理

 *@author chenb
 *@date  2018-04-17
 */
public class FusenJSONResult {

    // 定义jackson对象
    // private static final ObjectMapper MAPPER = new ObjectMapper();


    // 响应消息
    private String result;

    // 响应中的数据
    private Object data;


    private FusenJSONResult(String result, Object data) {
        this.result = result;
        this.data = data;
    }

    public static FusenJSONResult build(String result, Object data){
        return new FusenJSONResult(result, data);
    }

    public static FusenJSONResult buildSuccess(Object data) {
        return new FusenJSONResult("success", data);
    }

    public static FusenJSONResult buildFail(Object data) {
        return new FusenJSONResult("fail", data);
    }

    public static FusenJSONResult success() {
        return new FusenJSONResult("success", "");
    }

    public static FusenJSONResult successMsg(String msg) {
        return new FusenJSONResult("success", msg);
    }

    public static FusenJSONResult fail() {
        return new FusenJSONResult("fail", "");
    }

    public static FusenJSONResult failMsg(String msg) {
        return new FusenJSONResult("fail", msg);
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
