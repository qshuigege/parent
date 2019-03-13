package com.fs.something.controller;

import com.fs.something.pojo.B2B_USER;
import com.fs.something.repository.UserDao;
import com.fs.something.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entwechat")
public class TestController {

    private Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private UserDao dao;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redis;

    @RequestMapping("callback/getTestSessionId")
    public FusenJSONResult getTestSessionId(HttpServletRequest request){
        String qywxuserid = request.getParameter("qywxuserid");
        String pwd = request.getParameter("pwd");

        if (null==qywxuserid||"".equals(qywxuserid)||null==pwd||"".equals(pwd)){
            return FusenJSONResult.failMsg("参数qywxuserid, pwd不能为空！");
        }
        if (!"666cheb".equals(pwd)){
            return FusenJSONResult.failMsg("非法操作！");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("UserId", qywxuserid);
        try {
            List<B2B_USER> userLst = dao.getOrguserByQywxuserid(params);
            B2B_USER user = userLst.get(0);
            String sessionid = ReusableCodes.createSession(redis, user, 604800);
            log.info("获取测试sessionid-->{}, orguser-->{}", sessionid, JsonUtils.objectToJson(user));
            //Map<String, Object> r = new HashMap<>();
            params.clear();
            params.put("sessionId", sessionid);
            params.put("name", user.getQywxnickname());
            params.put("avatar", user.getQywxavatar());
            //r.put("miniprogflag", user.getMiniprogflag());
            //r.put("privilege", user.getF_ROLE());
            return FusenJSONResult.buildSuccess(params);
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("callback/getQywxTongXunLu")
    public FusenJSONResult getQywxTongXunLu(HttpServletRequest request){
        String accesstoken = request.getParameter("accesstoken");
        String departmentid = request.getParameter("departmentid");
        String fetchchild = request.getParameter("fetchchild");
        String url = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token="+accesstoken+"&department_id="+departmentid+"&fetch_child="+fetchchild;
        try {
            String jsonData = HttpClientUtils.get(url);
            Map<String, Object> dataMap = JsonUtils.jsonToMap(jsonData);
            List<Map<String, Object>> mapList = (List<Map<String, Object>>)dataMap.get("userlist");

            for (int i = 0; i < mapList.size(); i++) {
                Map<String, Object> map = mapList.get(i);
                //log.info("userid-->{},name-->{},email-->{},avatar-->{}", map.get("userid"), map.get("name"), map.get("email"), map.get("avatar"));
                String fsid = map.get("userid").toString();
                Map<String, Object> params = new HashMap<>();
                Map<String, Object> erpData;
                params.put("fsid", fsid);
                try {
                    erpData = dao.getUcmluserinfoByFsid(params);
                }catch (Exception e){
                    log.error("TestController.getQywxTongXunLu() getUcmluserinfoByFsid(fsid:{}) exception!-->{}", fsid, e.getMessage());
                    continue;
                }
                String ucml_useroid = erpData.get("ucml_useroid")==null?"":erpData.get("ucml_useroid").toString();
                params.clear();
                params.put("name",erpData.get("personname"));
                params.put("ucml_useroid",erpData.get("ucml_useroid"));
                params.put("orgname",erpData.get("orgname"));
                params.put("zrzx", erpData.get("zrzx"));
                //params.put("varchar6", erpData.get("varchar6"));
                params.put("fsid", fsid);
                params.put("qywxnickname", map.get("name"));
                params.put("email", map.get("email"));
                params.put("avatar", map.get("avatar"));
                //redis.delete(fsid);//先删后存
                redis.opsForHash().putAll(fsid, params);
                redis.opsForHash().putAll(ucml_useroid, params);
                //Map<Object, Object> ttt = redis.opsForHash().entries(map.get("userid").toString());
                //retList.add(ttt);
            }
            log.info("通讯录已保存到redis");
            return FusenJSONResult.buildSuccess(mapList);
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("callback/setEmpInfoToRedis")
    public FusenJSONResult setEmpInfoToRedis(HttpServletRequest request){
        try {
            String fsid = request.getParameter("fsid");
            String name = request.getParameter("name");
            String ucml_useroid = request.getParameter("ucml_useroid");
            String orgname = request.getParameter("orgname");
            String zrzx = request.getParameter("zrzx");
            String qywxnickname = request.getParameter("qywxnickname");
            String email = request.getParameter("email");
            String avatar = request.getParameter("avatar");
            Map<String, Object> params = new HashMap<>();
            params.put("name", name);
            params.put("ucml_useroid", ucml_useroid);
            params.put("orgname", orgname);
            params.put("zrzx", zrzx);
            params.put("fsid", fsid);
            params.put("qywxnickname", qywxnickname);
            params.put("email", email);
            params.put("avatar", avatar);
            redis.delete(fsid);//先删后存
            redis.opsForHash().putAll(fsid, params);
            log.info("fsid({})已保存到redis", fsid);
            return FusenJSONResult.buildSuccess(params);
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("callback/getEmpInfoFromRedis")
    public FusenJSONResult getEmpInfoFromRedis(HttpServletRequest request){
        try {
            String fsid = request.getParameter("fsid");
            Map<Object, Object> ttt = redis.opsForHash().entries(fsid);
            return FusenJSONResult.buildSuccess(ttt);
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("callback/testRedis")
    public FusenJSONResult testRedis(HttpServletRequest request){
        String areyouok = redis.opsForValue().get("areyouok");
        if (null==areyouok){
            return FusenJSONResult.buildSuccess("if the key not exist, the value is null");
        }else if ("".equals(areyouok)){
            return FusenJSONResult.buildSuccess("if the key not exist, the value is \"\"");
        }else {
            return FusenJSONResult.buildSuccess(areyouok);
        }
    }

    @RequestMapping("callback/testRequestOfJson")
    public JsonResult testRequestOfJson(HttpServletRequest request){
        String menuJson = null;
        Map<String, Object> map = null;
        try {
            // 获取输入流
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));

            // 写入数据到Stringbuilder
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = streamReader.readLine()) != null) {
                sb.append(line);
            }
            menuJson = sb.toString();
            log.info("menuJson-->{}",menuJson);
            map = JsonUtils.jsonToMap(menuJson);
        } catch (Exception e) {
            //e.printStackTrace();
            log.error("WXController.wxAddConditionalMenu() exception!-->{}",e.getMessage());
        }
        return JsonResult.success("", map);
    }

    public static void main(String[] args){
        try {
            Map<String, Object> map = HttpClientUtils.getForByteArrayAndContentType("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=3581792254,1787772481&fm=173&app=25&f=JPEG?w=218&h=146&s=DBACB7475B8662D2062E5B6D0300E068");
            System.out.println(map.get("content-name"));
            System.out.println(map.get("content-type"));
        }catch (Exception e){

        }
    }

}
