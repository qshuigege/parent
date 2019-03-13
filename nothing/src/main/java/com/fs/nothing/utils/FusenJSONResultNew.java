package com.fs.nothing.utils;

/**
 * @Description 自定义响应数据结构
 * 				这个类是提供给门户，ios，安卓，微信商城用的
 * 				门户接受此类数据后需要使用本类的方法转换成对于的数据类型格式（类，或者list）
 * 				其他自行处理

 *@author chenb
 *@date  2018-04-17
 */
public class FusenJSONResultNew {

    // 定义jackson对象
    // private static final ObjectMapper MAPPER = new ObjectMapper();


    // 响应消息
    private String result;

    // 响应中的数据
    private Object data;


    private FusenJSONResultNew(String result, Object data) {
        this.result = result;
        this.data = data;
    }

    public static FusenJSONResultNew build(String result, Object data){
        return new FusenJSONResultNew(result, data);
    }

    public static FusenJSONResultNew buildSuccess(Object data) {
        return new FusenJSONResultNew("success", data);
    }

    public static FusenJSONResultNew buildFail(Object data) {
        return new FusenJSONResultNew("fail", data);
    }

    public static FusenJSONResultNew success() {
        return new FusenJSONResultNew("success", "");
    }

    public static FusenJSONResultNew successMsg(String msg) {
        return new FusenJSONResultNew("success", msg);
    }

    public static FusenJSONResultNew fail() {
        return new FusenJSONResultNew("fail", "");
    }

    public static FusenJSONResultNew failMsg(String msg) {
        return new FusenJSONResultNew("fail", msg);
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
