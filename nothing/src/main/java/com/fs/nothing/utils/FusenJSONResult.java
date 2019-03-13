package com.fs.nothing.utils;

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
    private String msg;

    // 响应中的数据
    private Object result;



    public static FusenJSONResult build( String msg, Object data) {
        return new FusenJSONResult(msg, data);
    }

    public static FusenJSONResult fail(Object data) {
        return new FusenJSONResult(data);
    }
    public static FusenJSONResult ok(Object data) {
        return new FusenJSONResult(data);
    }
    public static FusenJSONResult ok(String sessionid,String role) {
        return new FusenJSONResult(sessionid,role);
    }
    public static FusenJSONResult ok() {
        return new FusenJSONResult("sucess");
    }

    public static FusenJSONResult errorMsg(String msg) {
        return new FusenJSONResult( msg, "");
    }

    public static FusenJSONResult errorMap(Object data) {
        return new FusenJSONResult("error", data);
    }

    public static FusenJSONResult errorTokenMsg(String msg) {
        return new FusenJSONResult(msg, "");
    }

    public static FusenJSONResult errorException(String msg) {
        return new FusenJSONResult(msg, "");
    }

    public FusenJSONResult() {

    }

    public FusenJSONResult(String msg, Object result) {
        this.msg = msg;
        this.result = result;
    }
    public FusenJSONResult(String msg) {
        this.msg = msg;
        this.result = "";
    }
    public FusenJSONResult(Object result) {
        this.msg = "OK";
        this.result = result;
    }
    public FusenJSONResult(String sessionid,String role) {
        this.msg = "OK";
        this.result = "";
    }

    public Boolean isOK() {
        return this.msg == "OK";
    }
    public Boolean isFail() {
        return this.msg == "fail";
    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * @Description: 将json结果集转化为FusenSONResult对象
     * 				需要转换的对象是一个类
     * @param jsonData
     * @param clazz
     * @return
     */
    /*public static FusenJSONResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, FusenJSONResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }*/

    /**
     *
     * @Description: 没有object对象的转化
     * @param json
     * @return
     */
  /*  public static FusenJSONResult format(String json) {
        try {
            return MAPPER.readValue(json, FusenJSONResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }*/

    /**
     *
     * @Description: Object是集合转化
     * 				需要转换的对象是一个list
     * @param jsonData
     * @param clazz
     * @return
     */
   /* public static FusenJSONResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }*/

}
