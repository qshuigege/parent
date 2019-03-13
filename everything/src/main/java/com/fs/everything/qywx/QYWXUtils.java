package com.fs.everything.qywx;

import com.fs.everything.config.WechatApplicationReadyEventListener;
import com.fs.everything.utils.HttpClientUtils;
import com.fs.everything.utils.JsonUtils;

import java.util.List;
import java.util.Map;

public class QYWXUtils {

    public static String getAccessToken(String secret) throws Exception{
        String apiUrl = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid="+R_QYWX.CORPID+"&corpsecret="+secret;
        String jsonStr;
        try {
            jsonStr = HttpClientUtils.get(apiUrl);
        }catch (Exception e){
            throw new Exception("QYWXUtils.getAccessToken() httpconn exception-->"+e.getMessage());
        }
        //JSONObject jo = JSONObject.fromObject(jsonStr);
        Map<String, Object> map = JsonUtils.jsonToMap(jsonStr);
        if (!"0".equals(map.get("errcode").toString())) {
            throw new Exception("QYWXUtils.getAccessToken() get accesstoken failed-->secret:"+secret+", errcode:" + map.get("errcode") + ", errmsg:" + map.get("errmsg"));
        }
        return map.get("access_token").toString();
    }

    public static String getJsApiTicket(String access_token) throws Exception{
        String url = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token="+access_token;
        String jsonStr;
        try {
            jsonStr = HttpClientUtils.get(url);
        }catch (Exception e){
            throw new Exception("QYWXUtils.getJsApiTicket() httpconn exception-->"+e.getMessage());
        }
        Map<String, Object> map = JsonUtils.jsonToMap(jsonStr);
        if (!"0".equals(map.get("errcode").toString())) {
            throw new Exception("QYWXUtils.getJsApiTicket() exception!-->errcode:" + map.get("errcode") + ", errmsg:" + map.get("errmsg"));
        }
        return map.get("ticket").toString();
    }

    public static Map<String, Object> qywxMiniprogLogin(String code, String redisAccessTokenKeySuffix) throws Exception{
        String url = "https://qyapi.weixin.qq.com/cgi-bin/miniprogram/jscode2session?access_token="+WechatApplicationReadyEventListener.getRedisOperator().opsForValue().get("accesstoken_"+redisAccessTokenKeySuffix) +"&js_code="+code+"&grant_type=authorization_code";
        String json;
        try {
            json = HttpClientUtils.get(url);
        }catch (Exception e){
            throw new Exception("QYWXUtils.qywxMiniprogLogin() httpclient exception! -->"+e.getMessage());
        }
        Map<String, Object> map = JsonUtils.jsonToMap(json);
        if (!"0".equals(map.get("errcode").toString())){
            throw new Exception("QYWXUtils.qywxMiniprogLogin() exception! -->"+json);
        }
        return map;
    }

    public static Map<String, Object> getUserInfo(String userid, String redisAccessTokenKeySuffix) throws Exception{
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token="+WechatApplicationReadyEventListener.getRedisOperator().opsForValue().get("accesstoken_"+redisAccessTokenKeySuffix)+"&userid="+userid;
        String json;
        try {
            json = HttpClientUtils.get(url);
        } catch (Exception e) {
            throw new Exception("QYWXUtils.getUserInfo() httpclient exception! -->"+e.getMessage());
        }
        Map<String, Object> map = JsonUtils.jsonToMap(json);
        if (!"0".equals(map.get("errcode").toString())){
            throw new Exception("QYWXUtils.getUserInfo() exception! -->"+json);
        }
        return map;
    }

    public static List<Map<String, Object>> pullTongXunLu(String accesstoken, String departmentid, String fetchchild) throws Exception{
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token="+accesstoken+"&department_id="+departmentid+"&fetch_child="+fetchchild;
        String jsonData;
        try {
            jsonData = HttpClientUtils.get(url);
        }catch (Exception e){
            throw new Exception("QYWXUtils.pullTongXunLu() httpclient exception! -->"+e.getMessage());
        }
        Map<String, Object> dataMap = JsonUtils.jsonToMap(jsonData);
        if (!"0".equals(dataMap.get("errcode").toString())){
            throw new Exception("QYWXUtils.pullTongXunLu() exception! -->"+jsonData);
        }
        List<Map<String, Object>> mapList = (List<Map<String, Object>>)dataMap.get("userlist");
        return mapList;
    }
}


