package com.fs.something.controller;

import com.fs.something.config.StaticResourceConfiguration;
import com.fs.something.pojo.B2B_USER;
import com.fs.something.repository.UserDao;
import com.fs.something.service.SHConfirmService;
import com.fs.something.utils.FusenJSONResult;
import com.fs.something.utils.HttpClientUtils;
import com.fs.something.utils.JsonUtils;
import com.fs.something.utils.ReusableCodes;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/entwechat")
public class SHConfirmController {

    private Logger log = LoggerFactory.getLogger(SHConfirmController.class);

    @Autowired
    private StaticResourceConfiguration staticRes;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redis;

    @Autowired
    private SHConfirmService service;

    @Autowired
    private UserDao dao;

    /*@RequestMapping("callback/qywxGetUserIdByCode")
    public FusenJSONResult qywxGetUserIdByCode(HttpServletRequest request){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        log.info("code-->{}, state-->{}", code, state);
        if (redis.hasKey("qywxcode_" + code)){
            String session = redis.opsForValue().get("qywxcode_" + code);
            String[] split = session.split("\\|");
            Map<String, String> map = new HashMap<>();
            map.put("sessionId", split[0]);
            map.put("name", split[1]);
            map.put("avatar", split[2]);
            log.info("登录成功(已被缓存的code:{})，sessionid-->{}",code, JsonUtils.objectToJson(FusenJSONResult.buildSuccess(map)));
            return FusenJSONResult.buildSuccess(map);
        }
        //String accesstoken003 = redis.opsForValue().get("accesstoken_"+staticRes.getMiniprog_secret003());
        String accesstoken = redis.opsForValue().get("accesstoken_"+state);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+accesstoken+"&code="+code;
        String useridJson = null;
        try {
            useridJson = HttpClientUtils.get(url);
        } catch (Exception e) {
            log.error("SHConfirmController.qywxGetUserIdByCode() exception!-->{}", e.getMessage());
            return FusenJSONResult.failMsg("SHConfirmController.qywxGetUserIdByCode() exception!-->"+e.getMessage());
        }
        Map<String, Object> useridMap = JsonUtils.jsonToMap(useridJson);
        log.info("useridMap-->{}", useridMap);
        if (!"0".equals(useridMap.get("errcode").toString())){
            log.error("useridMap-->{}", useridMap);
            return FusenJSONResult.failMsg(useridMap.toString());
        }
        List<B2B_USER> userList;
        Map<String, Object> userInfo;
        //String orgid = null;
        try {
            userList = service.getOrguserByQywxuserid(useridMap);
        }catch (Exception e){
            try {
                //userInfo = QYWXUtils.getUserInfo(useridMap.get("UserId").toString(), staticRes.getMiniprog_secret003(), redis);
                userInfo = QYWXUtils.getUserInfo(useridMap.get("UserId").toString(), state, redis);
            }catch (Exception ee){
                log.error("SHConfirmController.qywxGetUserIdByCode() getUserInfo exception!-->{}", ee.getMessage());
                return FusenJSONResult.failMsg("SHConfirmController.qywxGetUserIdByCode() getUserInfo exception!-->"+ee.getMessage());
            }
            Map<String, Object> params = new HashMap<>();
            params.put("role", "0");
            params.put("rolename", "0");
            params.put("name", userInfo.get("name"));
            params.put("qywxuserid", userInfo.get("userid"));
            params.put("userid", userInfo.get("email"));
            params.put("qywxnickname", userInfo.get("name"));
            params.put("qywxavatar", userInfo.get("avatar"));
            params.put("UserId", userInfo.get("userid"));
            try {
                service.addOrguserAccQywx(params);
                userList = service.getOrguserByQywxuserid(params);
            }catch (Exception ee){
                log.error("SHConfirmController.qywxGetUserIdByCode() addOrguserAccQywx exception!-->{}", ee.getMessage());
                return FusenJSONResult.failMsg("SHConfirmController.qywxGetUserIdByCode() addOrguserAccQywx exception!-->"+ee.getMessage());
            }

        }
        B2B_USER user = userList.get(0);
        *//*useridMap.put("empnum", user.getEmpnum());
        Map<String, Object> userZrzx = null;
        try {
            userZrzx = service.getUserZrzx(useridMap);
        }catch (Exception e){
            log.error("SHConfirmController.qywxGetUserIdByCode() getUserZrzx exception!-->{}", e.getMessage());
            return FusenJSONResult.failMsg("SHConfirmController.qywxGetUserIdByCode() getUserZrzx exception!-->{}"+e.getMessage());
        }
        user.setUcml_zrzx(userZrzx.get("orgname").toString());*//*
        *//*List<Map<String, Object>> leaderAuditInfo = dao.getLeaderAuditInfo(useridMap);
        useridMap.clear();
        if (leaderAuditInfo.size()>0){
            String temp =JsonUtils.objectToJson(leaderAuditInfo);
            user.setUcml_audit(temp);
            useridMap.put("auditpermission", temp);
        }*//*
        String sessionid = ReusableCodes.createSession(redis, user, 604800);//sessionid有效时长为604800秒(7天)
        //Map<String, Object> r = new HashMap<>();
        useridMap.clear();
        useridMap.put("sessionId", sessionid);
        useridMap.put("name", user.getQywxnickname());
        useridMap.put("avatar", user.getQywxavatar());
        //r.put("miniprogflag", user.getMiniprogflag());
        //r.put("privilege", user.getF_ROLE());
        log.info("登录成功，sessionid-->{}", JsonUtils.objectToJson(FusenJSONResult.buildSuccess(useridMap)));
        redis.opsForValue().set("qywxcode_"+code, sessionid+"|"+user.getQywxnickname()+"|"+user.getQywxavatar());
        redis.expire("qywxcode_"+code, 30, TimeUnit.DAYS);
        return FusenJSONResult.buildSuccess(useridMap);
    }*/

    @RequestMapping("callback/qywxGetUserIdByCode")
    public FusenJSONResult qywxGetUserIdByCode(HttpServletRequest request){
        String code = request.getParameter("code");
        String state = request.getParameter("state");
        log.info("code-->{}, state-->{}", code, state);
        if (redis.hasKey("qywxcode_" + code)){
            String session = redis.opsForValue().get("qywxcode_" + code);
            String[] split = session.split("\\|");
            Map<String, String> map = new HashMap<>();
            map.put("sessionId", split[0]);
            map.put("name", split[1]);
            map.put("avatar", split[2]);
            map.put("orgname", split[3]);
            log.info("登录成功(已被缓存的code:{})，sessionid-->{}",code, JsonUtils.objectToJson(FusenJSONResult.buildSuccess(map)));
            return FusenJSONResult.buildSuccess(map);
        }
        //String accesstoken003 = redis.opsForValue().get("accesstoken_"+staticRes.getMiniprog_secret003());
        String accesstoken = redis.opsForValue().get("accesstoken_"+state);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token="+accesstoken+"&code="+code;
        String useridJson = null;
        try {
            long begin = System.currentTimeMillis();
            useridJson = HttpClientUtils.get(url);
            long end = System.currentTimeMillis();
            log.info("企业微信通过code调用登录接口获取用户信息花费时间-->{}ms", end-begin);
        } catch (Exception e) {
            log.error("SHConfirmController.qywxGetUserIdByCode() exception!-->{}", e.getMessage());
            return FusenJSONResult.failMsg("SHConfirmController.qywxGetUserIdByCode() exception!-->"+e.getMessage());
        }
        Map<String, Object> useridMap = JsonUtils.jsonToMap(useridJson);
        log.info("useridMap-->{}", useridMap);
        if (!"0".equals(useridMap.get("errcode").toString())){
            log.error("useridMap-->{}", useridMap);
            return FusenJSONResult.failMsg(useridMap.toString());
        }
        String qywxuserid = useridMap.get("UserId").toString();
        Map<Object, Object> entries = redis.opsForHash().entries(qywxuserid);
        B2B_USER user = new B2B_USER();
        user.setEmpnum(qywxuserid);
        user.setLoginid(entries.get("email")==null?"":entries.get("email").toString());
        user.setQywxavatar(entries.get("avatar")==null?"":entries.get("avatar").toString());
        user.setQywxnickname(entries.get("qywxnickname")==null?"":entries.get("qywxnickname").toString());
        user.setQywxuserid(qywxuserid);
        user.setUcml_department_name(entries.get("orgname")==null?"":entries.get("orgname").toString());
        user.setUcml_useroid(entries.get("ucml_useroid")==null?"":entries.get("ucml_useroid").toString());
        user.setF_YHMC(entries.get("name")==null?"":entries.get("name").toString());
        user.setUcml_zrzx(entries.get("zrzx")==null?"":entries.get("zrzx").toString());
        /*useridMap.put("empnum", user.getEmpnum());
        Map<String, Object> userZrzx = null;
        try {
            userZrzx = service.getUserZrzx(useridMap);
        }catch (Exception e){
            log.error("SHConfirmController.qywxGetUserIdByCode() getUserZrzx exception!-->{}", e.getMessage());
            return FusenJSONResult.failMsg("SHConfirmController.qywxGetUserIdByCode() getUserZrzx exception!-->{}"+e.getMessage());
        }
        user.setUcml_zrzx(userZrzx.get("orgname").toString());*/
        /*List<Map<String, Object>> leaderAuditInfo = dao.getLeaderAuditInfo(useridMap);
        useridMap.clear();
        if (leaderAuditInfo.size()>0){
            String temp =JsonUtils.objectToJson(leaderAuditInfo);
            user.setUcml_audit(temp);
            useridMap.put("auditpermission", temp);
        }*/
        String sessionid = ReusableCodes.createSession(redis, user, 604800);//sessionid有效时长为604800秒(7天)
        //Map<String, Object> r = new HashMap<>();
        useridMap.clear();
        useridMap.put("sessionId", sessionid);
        useridMap.put("name", user.getQywxnickname());
        useridMap.put("avatar", user.getQywxavatar());
        useridMap.put("orgname", user.getUcml_department_name());
        //r.put("miniprogflag", user.getMiniprogflag());
        //r.put("privilege", user.getF_ROLE());
        log.info("登录成功，sessionid-->{}", JsonUtils.objectToJson(FusenJSONResult.buildSuccess(useridMap)));
        redis.opsForValue().set("qywxcode_"+code, sessionid+"|"+user.getQywxnickname()+"|"+user.getQywxavatar()+"|"+user.getUcml_department_name());
        redis.expire("qywxcode_"+code, 30, TimeUnit.DAYS);
        return FusenJSONResult.buildSuccess(useridMap);
    }

    /*@RequestMapping("callback/loginByQywxuserid")
    public FusenJSONResult loginByQywxuserid(HttpServletRequest request, Map<String, Object> insideParams){
        String qywxuserid = null;
        String fssecret = null;
        if (null == insideParams) {
            qywxuserid = request.getParameter("qywxuserid");
            fssecret = request.getParameter("fssecret");
        }else{
            qywxuserid = (String)insideParams.get("qywxuserid");
            fssecret = "cheb666";
        }

        if (null == qywxuserid || "".equals(qywxuserid) || null == fssecret || "".equals(fssecret)) {
            return FusenJSONResult.failMsg("参数qywxuserid, fssecret不能为空！");
        }
        if (!"cheb666".equals(fssecret)) {
            return FusenJSONResult.failMsg("非法操作！");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("UserId", qywxuserid);
        try {
            List<B2B_USER> userList = dao.getOrguserByQywxuserid(params);
        } catch (Exception e) {
            return
        }

        return null;
    }*/

    /*public FusenJSONResult loginByUidAndPwd(HttpServletRequest request){
        Map<String, Object> params = new HashMap<>();
        String uid = request.getParameter("uid");
        String pwd = request.getParameter("pwd");
        String fssecret = request.getParameter("fssecret");
        if (null==uid||"".equals(uid)||null==pwd||"".equals(pwd)||null==fssecret||"".equals(fssecret)){
            return FusenJSONResult.failMsg("参数uid, pwd, fssecret不能为空！");
        }
        if ("cheb666".equals(fssecret)){
            return FusenJSONResult.failMsg("非法操作！");
        }
        try {
            List<B2B_USER> userList = dao.getOrguserByUidAndPwd(params);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/


    //上传图片
    @RequestMapping("shconfirm/uploadPics")
    public FusenJSONResult uploadPics(HttpServletRequest request){
        String mediaid = request.getParameter("mediaid");
        String state = request.getParameter("state");
        if (null==mediaid||"".equals(mediaid.trim())){
            return FusenJSONResult.failMsg("参数mediaid不能为空！");
        }
        log.info("mediaid-->{}, state-->{}", mediaid, state);
        String accesstoken = redis.opsForValue().get("accesstoken_"+state);
        String url = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token="+accesstoken +"&media_id="+mediaid;
        log.info("SHConfirmController.uploadPics pic url-->{}", url);
        byte[] bytes;
        Map<String, Object> map;
        try {
            map = HttpClientUtils.getForByteArrayAndContentType(url);
            bytes = (byte[]) map.get("byte-array");
        } catch (Exception e) {
            log.error("SHConfirmController.uploadPics() getForByteArray exception! -->{}", e.getMessage());
            return FusenJSONResult.failMsg("SHConfirmController.uploadPics() getForByteArray exception! -->"+e.getMessage());
        }
        //String url2 = "https://test.fusen.net.cn/file/upload";
        String url2 = staticRes.getUpload_pic_url();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("customId", UUID.randomUUID().toString());
            params.put("orderId", UUID.randomUUID().toString());
            String suffix = map.get("content-type").toString().split("\\/")[1];
            //String filename = UUID.randomUUID().toString();
            String filename = "附件";
            params.put("filename", filename);
            params.put("filenameWithSuffix", filename+"."+suffix);
            String result = HttpClientUtils.postStreamForString(url2, bytes, ContentType.APPLICATION_OCTET_STREAM, params);
            log.info("SHConfirmController.uploadPics()上传结果-->"+result);
            return FusenJSONResult.buildSuccess(result);
        } catch (Exception e) {
            log.error("SHConfirmController.uploadPics() postStreamForString exception!-->{}", e.getMessage());
            return FusenJSONResult.failMsg("SHConfirmController.uploadPics() postStreamForString exception!-->"+e.getMessage());
        }
    }

}
