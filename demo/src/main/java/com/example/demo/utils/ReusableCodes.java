package com.example.demo.utils;

public class ReusableCodes {

    /**
     *
     * @param redis
     * @param unionid
     * @param user
     * @return sessionId
     */
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

}
