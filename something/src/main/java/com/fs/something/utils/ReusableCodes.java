package com.fs.something.utils;

import com.fs.something.pojo.B2B_USER;
import com.fs.something.pojo.LoginCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ReusableCodes {

    public static Logger log = LoggerFactory.getLogger(ReusableCodes.class);

    /**
     *
     * @param redis
     * @param user
     * @return sessionId
     */
    public static String createSession(StringRedisTemplate redis, B2B_USER user){
        String sessionId = UUID.randomUUID().toString();
        LoginCache lc = new LoginCache(user.getF_ID(),sessionId,user.getF_KH_FK(),user.getF_YHLX(),user.getF_GSMC(),null,user.getF_FSKHBM(),user.getQywxuserid(),user.getQywxnickname(),user.getLoginid(),user.getEmpnum(),user.getUcml_useroid(),user.getUcml_department_name(),user.getUcml_zrzx(),user.getUcml_audit());
        redis.opsForValue().set("json:"+sessionId, JsonUtils.objectToJson(lc));
        redis.expire("json:"+sessionId,1800, TimeUnit.SECONDS);//设置30分钟过期
        //resultMap.put("erpuseroid", user.getF_KH_FK());
        //resultMap.put("sessionId", sessionId);
        //return FusenJSONResult.build("success", resultMap);
        return sessionId;
    }

    /**
     *
     * @param redis
     * @param user
     * @param duration sessionid有效时长，以秒为单位
     * @return sessionid
     */
    public static String createSession(StringRedisTemplate redis, B2B_USER user, long duration){
        String sessionId = UUID.randomUUID().toString();
        LoginCache lc = new LoginCache(user.getF_ID(),sessionId,user.getF_KH_FK(),user.getF_YHLX(),user.getF_GSMC(),null,user.getF_FSKHBM(),user.getQywxuserid(),user.getQywxnickname(),user.getLoginid(),user.getEmpnum(),user.getUcml_useroid(),user.getUcml_department_name(),user.getUcml_zrzx(),user.getUcml_audit());
        redis.opsForValue().set("json:"+sessionId, JsonUtils.objectToJson(lc));
        redis.expire("json:"+sessionId,duration, TimeUnit.SECONDS);//设置过期
        //resultMap.put("erpuseroid", user.getF_KH_FK());
        //resultMap.put("sessionId", sessionId);
        //return FusenJSONResult.build("success", resultMap);
        return sessionId;
    }

    public static boolean destroySession(StringRedisTemplate redis, String sessionId){
        if (redis.hasKey("json:"+sessionId)) {
            boolean f = redis.delete("json:"+sessionId);
            if(f){
                log.info("销毁sessionId({})成功！", sessionId);
            }else {
                log.error("销毁sessionId({})失败！", sessionId);
            }
            return f;
        }else {
            log.warn("reidis中不存在此sessionId({}),无法销毁！", sessionId);
            return false;
        }
    }
    /*public static String createSession(StringRedisTemplate redis, String unionid, B2B_USER user){
        if(redis.hasKey(unionid)){
            //resultMap.put("erpuseroid", user.getF_KH_FK());
            //resultMap.put("sessionId", redis.opsForValue().get(unionid).substring(5));
            //return FusenJSONResult.build("success", resultMap);
            redis.expire(unionid, 1800, TimeUnit.SECONDS);
            String sessionIdKey = redis.opsForValue().get(unionid);
            redis.expire(sessionIdKey, 1800, TimeUnit.SECONDS);
            return sessionIdKey.substring(5);
        }else{
            String sessionId = UUID.randomUUID().toString();
            LoginCache lc = new LoginCache(user.getF_ID(),sessionId,user.getF_KH_FK(),user.getF_YHLX(),user.getF_GSMC(),unionid,user.getF_FSKHBM());
            redis.opsForValue().set(unionid,"json:"+sessionId);
            redis.expire(unionid, 1800,TimeUnit.SECONDS);
            redis.opsForValue().set("json:"+sessionId, JsonUtils.objectToJson(lc));
            redis.expire("json:"+sessionId,1800, TimeUnit.SECONDS);//设置30分钟过期
            //resultMap.put("erpuseroid", user.getF_KH_FK());
            //resultMap.put("sessionId", sessionId);
            //return FusenJSONResult.build("success", resultMap);
            return sessionId;
        }
    }*/

    public static Map<String, Object> getRequestJsonData(HttpServletRequest request) throws Exception {
        Map<String, Object> map;
        // 获取输入流
        BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

        // 写入数据到Stringbuilder
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = streamReader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString();
        json = json.replaceAll("\t", "");
        map = JsonUtils.jsonToMap(json);
        if (null==map) {
            throw new Exception("解析request请求的json数据异常！-->"+json);
        }
        return map;
    }
}
