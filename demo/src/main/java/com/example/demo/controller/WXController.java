package com.example.demo.controller;

import com.example.demo.service.WXBusinessService;
import com.example.demo.utils.CommonUtils;
import com.example.demo.utils.FusenJSONResult;
import com.example.demo.utils.HttpClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/WX")
public class WXController {

    public static Logger log = LoggerFactory.getLogger(WXController.class);

    /*@Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;*/

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redis;

    //@Autowired
    //private RedisOperator redis;

    //@Autowired
    //private userService userservice;

    @Autowired
    private WXBusinessService service;

    /*@RequestMapping("callback/wxEntrance")
    public void wxEntrance(HttpServletRequest request, HttpServletResponse response) throws Exception{

        if(request.getParameter("echostr")!=null){

            SignUtil.yanzheng(request, response);

        }else {
            // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
            // request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            // response.setHeader("Content-type", "text/html;charset=UTF-8");
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            Set<Map.Entry<String, String>> set = requestMap.entrySet();
            Iterator<Map.Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String,String> en = it.next();
                log.info("{}-->{}",en.getKey(),en.getValue());
            }
            // 调用核心业务类接收消息、处理消息
            String respMessage = WeiXinCoreService.processRequest(service,requestMap,redis.opsForValue().get("accesstoken"),redis);
            //log.info("respMessage-->"+respMessage);
            // 响应消息
            PrintWriter out = response.getWriter();
            out.print(respMessage);
            out.flush();
            out.close();

        }


    }*/

    /*@RequestMapping("callback/wxWebEntrance")
    @ResponseBody
    public FusenJSONResult wxWebEntrance(HttpServletRequest request, HttpServletResponse response){
        String code = request.getParameter("code");
        Map<String, String> resultMap = new HashMap<>();
        AccessTokenGZPT_WY accessToken_wy = null;
        try {
            accessToken_wy = WXUtils.getAccessToken_WY(code);
            log.info("WXController.wxWebEntrance() openid-->"+accessToken_wy.getOpenid());
        } catch (Exception e) {
            log.error("WXController.wxWebEntrance() exception.-->"+e.getMessage());
            resultMap.put("result","fail");
            resultMap.put("cause","WXController.wxWebEntrance() exception.-->"+e.getMessage());
            return FusenJSONResult.errorMap(resultMap);
        }
        Map<String, Object> unionidMap = null;
        try {
            unionidMap = service.getUnionidByOpenid(accessToken_wy.getOpenid());
        }catch (Exception e){
            log.error("WXController.wxWebEntrance() exception. 根据openid查不到unionid-->"+e.getMessage());
            resultMap.put("result", "fail");
            resultMap.put("cause", "WXController.wxWebEntrance() exception. 根据openid查不到unionid-->"+e.getMessage());
            return FusenJSONResult.errorMap(resultMap);
        }
        String unionid = unionidMap.get("unionid").toString();
        log.info("WXController.wxWebEntrance() unionid-->{}",unionid);
        List<B2B_USER> orguserList = null;
        B2B_USER user = null;
        try {
            //orguserMap = service.getErpuseroidByOpenid(accessToken_wy.getOpenid());
            orguserList = service.getOrguserByUnionid(unionid);
            user = orguserList.get(0);
        }catch (Exception e){
            log.error("WXController.wxWebEntrance() exception. 该微信号未与b2b账号绑定-->"+e.getMessage());
            resultMap.put("result", "fail");
            resultMap.put("cause", "WXController.wxWebEntrance() exception. 该微信号未与b2b账号绑定-->"+e.getMessage());
            return FusenJSONResult.errorMap(resultMap);
        }
        String sessionId = ReusableCodes.createSession(redis, unionid, user);
        resultMap.put("sessionId", sessionId);
        return FusenJSONResult.build("success", resultMap);
        *//*if(redis.hasKey(unionid)){
            //resultMap.put("erpuseroid", user.getF_KH_FK());
            resultMap.put("sessionId", redis.opsForValue().get(unionid).substring(5));
            return FusenJSONResult.build("success", resultMap);
        }else{
            String sessionId = UUID.randomUUID().toString();
            LoginCache lc = new LoginCache(user.getF_ID(),sessionId,user.getF_KH_FK(),user.getF_YHLX(),user.getF_GSMC(),unionid,user.getF_FSKHBM());
            redis.opsForValue().set(unionid,"json:"+sessionId);
            redis.expire(unionid, 1800,TimeUnit.SECONDS);
            redis.opsForValue().set("json:"+sessionId, JsonUtils.objectToJson(lc));
            redis.expire("json:"+sessionId,1800, TimeUnit.SECONDS);//设置30分钟过期
            //resultMap.put("erpuseroid", user.getF_KH_FK());
            resultMap.put("sessionId", sessionId);
            return FusenJSONResult.build("success", resultMap);
        }*//*



    }

    @RequestMapping("callback/wxLoginVerify")
    @ResponseBody
    public FusenJSONResult wxLoginVerify(HttpServletRequest request){
        Map<String, String > br = new HashMap<>();
        String code = request.getParameter("code");
        //String state = request.getParameter("state");
        String access_token_json = "";

        String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+ R_WX.APPID_OPEN_PLATFORM+"&secret="+R_WX.APPSECRET_OPEN_PLATFORM+"&code="
                + code + "&grant_type=authorization_code";
        try {
            access_token_json = HttpRequestUtils.requestStringGet(path);
        }catch (Exception e){
            br.put("result", "fail");
            br.put("cause", "WechatLoginController.wxLoginVerify() HttpURLConnection Exception");
            return FusenJSONResult.errorMap(br);
        }
        log.info("access_token_json-->"+access_token_json);

        //解析json
        JSONObject jo = JSONObject.fromObject(access_token_json);
        if (null != jo.get("errcode")){
            br.put("result", "fail");
            br.put("cause", "errcode:"+jo.get("errcode")+", errmsg:"+jo.get("errmsg"));
            return FusenJSONResult.errorMap(br);
        }
        //String access_token = (String)jo.get("access_token");
        //String expires_in = jo.getString("expires_in");
        //String refresh_token = jo.getString("refresh_token");
        //String openid = jo.getString("openid");
        //String scope = jo.getString("scope");
        String unionid = (String)jo.get("unionid");
        if(unionid==null||"".equals(unionid)){
            //out.print("unionid为空");
            br.put("result", "fail");
            br.put("cause", "unionid is null");
            return FusenJSONResult.errorMap(br);
        }


        boolean isBinded = service.isBinded(unionid);
        if (!isBinded){
            br.put("result", "fail");
            br.put("cause", "not binded");
            return FusenJSONResult.errorMap(br);
        }


        List<B2B_USER> orguserList = null;
        B2B_USER user = null;
        try {
            //orguserMap = service.getErpuseroidByOpenid(accessToken_wy.getOpenid());
            orguserList = service.getOrguserByUnionid(unionid);
            user = orguserList.get(0);
        }catch (Exception e){
            log.error("WXController.wxWebEntrance() exception. 该微信号未与b2b账号绑定-->"+e.getMessage());
            br.put("result", "fail");
            br.put("cause", "WXController.wxWebEntrance() exception. 该微信号未与b2b账号绑定-->"+e.getMessage());
            return FusenJSONResult.errorMap(br);
        }

        String sessionId = ReusableCodes.createSession(redis, unionid, user);
        Map<String, String> m = new HashMap<>();
        m.put("sessionId", sessionId);
        return FusenJSONResult.build("success", m);

    }


    @RequestMapping("callback/wxPushTemplateMsg")
    @ResponseBody
    public FusenJSONResult wxPushTemplateMsg(HttpServletRequest request)throws Exception{
        String aaa = request.getParameter("aaa");
        String bbb = request.getParameter("bbb");
        Map<String, String> params = CommonUtils.parseParamsStr(bbb);
        //根据erpoid查到openid
        List<Map<String, Object>> openid = service.getBindedOpenidsByErpoid(params.get("ERPOID"));
        if(openid.size()<1){
            return FusenJSONResult.errorMsg("该客户的B2B账号没有绑定任何微信号，无需推送消息。");
        }
        String apiUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
        if("PUSH_ORDER_STATUS_INFO_FA".equals(aaa)){
            //发货提醒
            for (int i = 0; i < openid.size(); i++) {
                String postData = "{    \"touser\": \""+openid.get(i).get("openid")+"\",    \"template_id\": \"HaFDkfGlQw5rNQ_vIK96JnsmAZy4tpRZkGeXjpOTLTk\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \""+params.get("1")+"\",            \"color\": \"#173177\"        },        \"keyword1\": {            \"value\": \""+params.get("2")+"\",            \"color\": \"#173177\"        },        \"keyword2\": {            \"value\": \""+params.get("3")+"\",            \"color\": \"#173177\"        },        \"keyword3\": {            \"value\": \""+params.get("4")+"\",            \"color\": \"#173177\"        },\"keyword4\": {            \"value\": \""+params.get("5")+"\",            \"color\": \"#173177\"        },\"keyword5\": {            \"value\": \""+params.get("6")+"\",            \"color\": \"#173177\"        },\"remark\": {            \"value\": \""+params.get("7")+"\",            \"color\": \"#173177\"        }    }}";
                String result = HttpRequestUtils.requestStringPost(apiUrl+redis.opsForValue().get("accesstoken"), postData);
                log.info(result);
            }
            //response.getWriter().print("ok");

        }else if("PUSH_ORDER_STATUS_INFO_SHOU".equals(aaa)){
            //收获提醒
            for (int i = 0; i < openid.size(); i++) {
                String postData = "{    \"touser\": \""+openid.get(i).get("openid")+"\",    \"template_id\": \"AAf5e8u62Tr6yfrh8MhgjNvLMQnk9iuoQtz7JOHx2LI\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \""+params.get("1")+"\",            \"color\": \"#173177\"        },        \"keyword1\": {            \"value\": \""+params.get("2")+"\",            \"color\": \"#173177\"        },        \"keyword2\": {            \"value\": \""+params.get("3")+"\",            \"color\": \"#173177\"        },        \"keyword3\": {            \"value\": \""+params.get("4")+"\",            \"color\": \"#173177\"        },\"remark\": {            \"value\": \""+params.get("5")+"\",            \"color\": \"#173177\"        }    }}";
                HttpRequestUtils.requestStringPost(apiUrl+redis.opsForValue().get("accesstoken"), postData);
            }
            //response.getWriter().print("ok");

        }

        return FusenJSONResult.ok();
    }


    *//**
     *生成二维码，用于扫码登录
     *//*
    @RequestMapping("callback/wxLoginQrcode")
    @ResponseBody
    public Map<String, String> wxLoginQrcode(HttpServletRequest request){
        String eventkey = request.getParameter("eventkey");
        Map<String, String> resultMap = new HashMap<String, String>();
        String accesstoken;
        try {
            accesstoken = redis.opsForValue().get("accesstoken");
            if (null==accesstoken||"".equals(accesstoken)){
                resultMap.put("result", "fail");
                resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxBindQrcode() get accesstoken from redis server failure");
                resultMap.put("qrcodeUrl", "");
                return resultMap;
            }
        }catch (Exception e){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxBindQrcode() get accesstoken from redis server failure-->"+e.getMessage());
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }

        String erweimaurl = "https://www.baidu.com/img/2016_6_1logo_8d0030f576acdcf660d470e225368007.gif";
        String postData = "{\"expire_seconds\": 30, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+eventkey+"\"}}}";
        String ticket_qr_code_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accesstoken;
        String ticketJson = null;
        try {
            ticketJson = HttpRequestUtils.requestStringPost(ticket_qr_code_url, postData);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("cause", e.getMessage());
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }
        JSONObject jo = JSONObject.fromObject(ticketJson);
        if(jo.size()==0){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxBindQrcode() jo.size()==0");
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }

        if(null==jo.get("ticket")){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxBindQrcode() qrcode ticket is null");
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }
        String ticket = jo.get("ticket").toString();
        //String expire_seconds = jo.getString("expire_seconds");
        //String imgUrl = jo.getString("url");
        erweimaurl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
        resultMap.put("result", "success");
        resultMap.put("cause", "");
        resultMap.put("qrcodeUrl", erweimaurl);
        return resultMap;
    }


    *//*@RequestMapping("callback/wxGetAccessToken")
    @ResponseBody
    public Map<String, String> wxGetAccessToken(){
        Map<String,String> map = new HashMap<>();
        map.put("accesstoken", redis.get("accesstoken"));
        return map;
    }*//*


    @RequestMapping("callback/wxGetLoginResult")
    @ResponseBody
    public FusenJSONResult wxGetLoginResult(HttpServletRequest request){
        String eventkey = request.getParameter("eventkey");
        Map<String,Object> map = new HashMap<>();
        if (!redis.hasKey(eventkey)){
            return FusenJSONResult.errorMsg("please wait");
        }else if ("0".equals(redis.opsForValue().get(eventkey))){
            return FusenJSONResult.errorMsg("fail");
        }else {
            //return FusenJSONResult.build("success", JsonUtils.jsonToPojo(redis.get(eventkey), QrcodeLoginResult.class));
            return FusenJSONResult.build("success", JsonUtils.jsonToMap(redis.opsForValue().get(eventkey)));
        }
    }

    public static void main(String[] args) {
        String str = "已下单|a";
        String[] arr = str.split("\\|");
        for (int i = 0; i < arr.length; i++) {
            log.info("arr["+i+"]"+arr[i]);
        }
    }


    *//**
     *生成二维码，用于B2B账号绑定微信
     *//*
    @RequestMapping("wxBindQrcode")
    @ResponseBody
    public Map<String, String> wxBindQrcode(HttpServletRequest request){
        Map<String, String> resultMap = new HashMap<String, String>();
        String sessionId = request.getParameter("sessionId");
        String sessionId2 = request.getHeader("sessionId");
        if (null!=sessionId2&&!"".equals(sessionId2)){
            sessionId = sessionId2;
        }
        //LoginCache lc = JsonUtils.jsonToPojo(redis.opsForValue().get("json:"+sessionId), LoginCache.class);
        UserContext uc = (UserContext)request.getAttribute("userContext");

        String accesstoken;
        try {
            accesstoken = redis.opsForValue().get("accesstoken");
            if (null==accesstoken||"".equals(accesstoken)){
                resultMap.put("result", "fail");
                resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxBindQrcode() get accesstoken from redis server failure");
                resultMap.put("qrcodeUrl", "");
                return resultMap;
            }
        }catch (Exception e){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxBindQrcode() get accesstoken from redis server failure-->"+e.getMessage());
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }

        String erweimaurl = "https://www.baidu.com/img/2016_6_1logo_8d0030f576acdcf660d470e225368007.gif";
        //String postData = "{\"expire_seconds\": 15, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+lc.getUserId()+"\"}}}";
        String postData = "{\"expire_seconds\": 15, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \""+uc.getOrgid()+"\"}}}";
        String ticket_qr_code_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accesstoken;
        String ticketJson = null;
        try {
            ticketJson = HttpRequestUtils.requestStringPost(ticket_qr_code_url, postData);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("cause", e.getMessage());
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }
        JSONObject jo = JSONObject.fromObject(ticketJson);
        if(jo.size()==0){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxBindQrcode() jo.size()==0");
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }

        if(null==jo.get("ticket")){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxBindQrcode() qrcode ticket is null");
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }
        String ticket = jo.get("ticket").toString();
        //String expire_seconds = jo.getString("expire_seconds");
        //String imgUrl = jo.getString("url");
        erweimaurl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
        resultMap.put("result", "success");
        resultMap.put("cause", "");
        resultMap.put("qrcodeUrl", erweimaurl);
        return resultMap;
    }

    *//**
     *生成二维码，用于解绑微信
     *//*
    @RequestMapping("wxUnbindQrcode")
    @ResponseBody
    public Map<String, String> wxUnbindQrcode(HttpServletRequest request){
        Map<String, String> resultMap = new HashMap<String, String>();

        String accesstoken;
        try {
            accesstoken = redis.opsForValue().get("accesstoken");
            if (null==accesstoken||"".equals(accesstoken)){
                resultMap.put("result", "fail");
                resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxUnbindQrcode() get accesstoken from redis server failure");
                resultMap.put("qrcodeUrl", "");
                return resultMap;
            }
        }catch (Exception e){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxUnbindQrcode() get accesstoken from redis server failure-->"+e.getMessage());
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }

        String erweimaurl = "https://www.baidu.com/img/2016_6_1logo_8d0030f576acdcf660d470e225368007.gif";
        String postData = "{\"expire_seconds\": 15, \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"0\"}}}";
        String ticket_qr_code_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+accesstoken;
        String ticketJson = null;
        try {
            ticketJson = HttpRequestUtils.requestStringPost(ticket_qr_code_url, postData);
        } catch (Exception e) {
            resultMap.put("result", "fail");
            resultMap.put("cause", e.getMessage());
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }
        JSONObject jo = JSONObject.fromObject(ticketJson);
        if(jo.size()==0){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxUnbindQrcode() jo.size()==0");
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }

        if(null==jo.get("ticket")){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxUnbindQrcode() qrcode ticket is null");
            resultMap.put("qrcodeUrl", "");
            return resultMap;
        }
        String ticket = jo.get("ticket").toString();
        //String expire_seconds = jo.getString("expire_seconds");
        //String imgUrl = jo.getString("url");
        erweimaurl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
        resultMap.put("result", "success");
        resultMap.put("cause", "");
        resultMap.put("qrcodeUrl", erweimaurl);
        return resultMap;
    }

    *//**
     *查询某个b2b账号下绑定的所有微信号
     *//*
    @RequestMapping("wxQueryBindedAccs")
    @ResponseBody
    public Map<String, Object> wxQueryBindedAccs(HttpServletRequest request){
        Map<String, Object> resultMap = new HashMap<String, Object>();
        *//*String sessionId = request.getParameter("sessionId");
        String sessionId2 = request.getHeader("sessionId");
        if (null!=sessionId2&&!"".equals(sessionId2)){
            sessionId = sessionId2;
        }
        LoginCache lc = JsonUtils.jsonToPojo(redis.opsForValue().get("json:"+sessionId), LoginCache.class);*//*
        UserContext uc = (UserContext)request.getAttribute("userContext");
        //String sql = "select a.openid, a.unionid, a.headimgurl, a.nickname, t.createdate from wxaccount a join (select r.unionid, r.createdate from wxacc_orguser_relation r where r.orgid = ?)t on a.unionid = t.unionid order by t.createdate desc";
        try {
            //List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, new Object[]{lc.getUserId()});
            //List<Map<String, Object>> data = jdbcTemplate.queryForList(sql, new Object[]{uc.getOrgid()});
            List<Map<String, Object>> data = service.getBindedWXAccs(uc.getOrgid());
            resultMap.put("result", "success");
            resultMap.put("cause","");
            resultMap.put("data",data);
            return resultMap;
        }catch (Exception e){
            resultMap.put("result", "fail");
            resultMap.put("cause", "com.b2b.member.controller.WechatLoginController.wxQueryBindedAccs()-->"+e.getMessage());
            return resultMap;
        }
    }

    *//**
     *批量解绑微信
     *//*
    @RequestMapping("wxBatchUnbind")
    @ResponseBody
    public Map<String, String> wxBatchUnbind(HttpServletRequest request){
        Map<String, String> resultMap = new HashMap<String, String>();
        //String[] unionids = request.getParameterValues("unionids");
        String unionidsTmp = request.getParameter("unionids");
        String[] unionids = null;
        if(null!=unionidsTmp) {
            unionids = unionidsTmp.split("\\|");
        }
        if (unionids==null||unionids.length<1){
            resultMap.put("result", "fail");
            resultMap.put("cause", "WXController.wxBatchUnbind() 参数unionids为空！");
            log.error("WXController.wxBatchUnbind() 参数unionids为空！");
            return resultMap;
        }
        try{
            //int rows = jdbcTemplate.update(sql.toString());
            int rows = service.unbindWXAccsBatch(unionids);
            if (rows<1){
                resultMap.put("result", "fail");
                resultMap.put("cause", "WXController.wxBatchUnbind() 没有更新任何数据！");
                log.error("WXController.wxBatchUnbind() 没有更新任何数据！");
                return resultMap;
            }
        }catch (Exception e){
            resultMap.put("result", "fail");
            resultMap.put("cause", "WXController.wxBatchUnbind() 批量解绑异常！-->"+e.getMessage());
            log.error("WXController.wxBatchUnbind() 批量解绑异常！-->{}",e.getMessage());
            return resultMap;
        }
        resultMap.put("result", "success");
        resultMap.put("cause", "");
        return resultMap;
    }*/



    @RequestMapping("getOrderMobileWeb")
    @ResponseBody
    public List<Map<String, Object>> getOrderMobileWeb(HttpServletRequest request){
        /*Map<String, String> req = CommonUtils.getAllParamsFromRequest(request.getParameterMap());
        UserContext uc = (UserContext)request.getAttribute("userContext");
        log.info(uc.getErpuseroid()+req.get("minono")+req.get("maxono"));*/

        String erpuseroid = request.getParameter("erpuseroid");
        if(null==erpuseroid||"".equals(erpuseroid)){
            //erpuseroid = "8FC979F8-35B7-4DFD-9509-F79090273A67";
            erpuseroid = "0003F4EE-0000-0000-0000-000009D2B6A6";
        }
        String minono = request.getParameter("minono");
        String maxono = request.getParameter("maxono");
        String pageSize = request.getParameter("pageSize");
        String pageNumber = request.getParameter("pageNumber");
        String rowIndex = ((Integer.parseInt(pageNumber)-1)*Integer.parseInt(pageSize))+"";
        List<Map<String, Object>> r = null;
        try {
            r = service.getOrderMobileWeb(erpuseroid, minono, maxono, pageSize, rowIndex);
        }catch (Exception e){
            log.error("WXController.getOrderMobileWeb() exception.-->"+e.getMessage());
            r = new ArrayList<>();
            Map<String, Object> m = new HashMap<>();
            m.put("result", "fail");
            m.put("cause",e.getMessage());
            r.add(m);
            return r;
        }
        return r;

    }

    @RequestMapping("getLogisticMobileWeb")
    @ResponseBody
    public List<Map<String, Object>> getLogisticMobileWeb(HttpServletRequest request){
        List<Map<String, Object>> r = null;
        try {
            r = service.getLogisticMobileWeb(request.getParameter("orda001oid"));
        }catch (Exception e){
            log.error("WXController.getLogisticMobileWeb() exception.-->"+e.getMessage());
            r = new ArrayList<>();
            Map<String, Object> m = new HashMap<>();
            m.put("result", "fail");
            m.put("cause",e.getMessage());
            r.add(m);
            return r;
        }
        return r;
    }

    @RequestMapping("getOrderAndLogistics")
    @ResponseBody
    public List<Map<String, Object>> getOrderAndLogistics(HttpServletRequest request){
        //UserContext uc = (UserContext) request.getAttribute("userContext");
        String erpuseroid = request.getParameter("erpuseroid");
        if(null==erpuseroid||"".equals(erpuseroid)){
            //erpuseroid = "8FC979F8-35B7-4DFD-9509-F79090273A67";
            erpuseroid = "0003F4EE-0000-0000-0000-000009D2B6A6";
        }
        List<Map<String, Object>> r = null;
        try {
            r = service.getOrderAndLogistics(erpuseroid);
        }catch (Exception e){
            log.error("WXController.getOrderAndLogistics() exception.-->"+e.getMessage());
            r = new ArrayList<>();
            Map<String, Object> m = new HashMap<>();
            m.put("result", "fail");
            m.put("cause",e.getMessage());
            r.add(m);
            return r;
        }
        return r;
    }

    @RequestMapping("getConditionOrder")
    @ResponseBody
    public List<Map<String, Object>> getConditionOrder(HttpServletRequest request){
        //UserContext uc = (UserContext) request.getAttribute("userContext");
        String erpuseroid = request.getParameter("erpuseroid");
        if(null==erpuseroid||"".equals(erpuseroid)){
            //erpuseroid = "8FC979F8-35B7-4DFD-9509-F79090273A67";
            erpuseroid = "0003F4EE-0000-0000-0000-000009D2B6A6";
        }
        List<Map<String, Object>> r = null;
        //String erpuseroid = uc.getErpuseroid();
        String fsno = request.getParameter("fsno");
        String custOrderNo = request.getParameter("custorderno");
        String checks = request.getParameter("orderstatus");
        String[] orderStatus = null;
        if(null!=checks) {
            orderStatus = checks.split("\\|");
        }
        String pageSize = request.getParameter("pageSize");
        String pageNumber = request.getParameter("pageNumber");
        //String rowIndex = request.getParameter("rowIndex");
        String rowIndex = ((Integer.parseInt(pageNumber)-1)*Integer.parseInt(pageSize))+"";
        try {
            r = service.getConditionOrder(erpuseroid,orderStatus,fsno,custOrderNo,pageSize,rowIndex);
        }catch (Exception e){
            log.error("WXController.getConditionOrder() exception.-->"+e.getMessage());
            r = new ArrayList<>();
            Map<String, Object> m = new HashMap<>();
            m.put("result", "fail");
            m.put("cause",e.getMessage());
            r.add(m);
            return r;
        }
        return r;
    }


    @RequestMapping("callback/wxPushTemplateMsg")
    @ResponseBody
    public FusenJSONResult wxPushTemplateMsg(HttpServletRequest request)throws Exception{
        String accesstoken = redis.opsForValue().get("accesstoken");

        String aaa = request.getParameter("aaa");
        String bbb = request.getParameter("bbb");
        Map<String, String> params = CommonUtils.parseParamsStr(bbb);
        //根据erpoid查到openid
        //List<Map<String, Object>> openid = service.getBindedOpenidsByErpoid(params.get("ERPOID"));
        List<Map<String, Object>> openid = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("openid", "oAZwf1eG35xEUN6CWsz6gJHnXRb0");
        openid.add(map);
        if(openid.size()<1){
            return FusenJSONResult.buildFail("该客户的B2B账号没有绑定任何微信号，无需推送消息。");
        }
        String apiUrl = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=";
        if("PUSH_ORDER_STATUS_INFO_FA".equals(aaa)){
            //发货提醒
            for (int i = 0; i < openid.size(); i++) {
                String postData = "{    \"touser\": \""+openid.get(i).get("openid")+"\",    \"template_id\": \"3nZykSsxtTlCgxRBaVP9f5w0Tp-lM7MjxDeafREC73Y\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \""+params.get("1")+"\",            \"color\": \"#173177\"        },        \"keyword1\": {            \"value\": \""+params.get("2")+"\",            \"color\": \"#173177\"        },        \"keyword2\": {            \"value\": \""+params.get("3")+"\",            \"color\": \"#173177\"        },        \"keyword3\": {            \"value\": \""+params.get("4")+"\",            \"color\": \"#173177\"        },\"keyword4\": {            \"value\": \""+params.get("5")+"\",            \"color\": \"#173177\"        },\"keyword5\": {            \"value\": \""+params.get("6")+"\",            \"color\": \"#173177\"        },\"remark\": {            \"value\": \""+params.get("7")+"\",            \"color\": \"#173177\"        }    }}";
                //String result = HttpRequestUtils.requestStringPost(apiUrl+accesstoken, postData);
                //Map<String, Object> tmpMap = new HashMap<>();
                //tmpMap.put("aaa", postData);
                String result = HttpClientUtil.postStringEntity(apiUrl+accesstoken, postData);
                log.info(result);
            }
            //response.getWriter().print("ok");

        }else if("PUSH_ORDER_STATUS_INFO_SHOU".equals(aaa)){
            //收获提醒
            for (int i = 0; i < openid.size(); i++) {
                String postData = "{    \"touser\": \""+openid.get(i).get("openid")+"\",    \"template_id\": \"TLc69dWM7MUSPdVAEqgOn-7RV6LgVMcBMMxfDjm5tTs\",    \"url\": \"\",    \"topcolor\": \"#FF0000\",    \"data\": {        \"first\": {            \"value\": \""+params.get("1")+"\",            \"color\": \"#173177\"        },        \"keyword1\": {            \"value\": \""+params.get("2")+"\",            \"color\": \"#173177\"        },        \"keyword2\": {            \"value\": \""+params.get("3")+"\",            \"color\": \"#173177\"        },        \"keyword3\": {            \"value\": \""+params.get("4")+"\",            \"color\": \"#173177\"        },\"remark\": {            \"value\": \""+params.get("5")+"\",            \"color\": \"#173177\"        }    }}";
                //HttpRequestUtils.requestStringPost(apiUrl+redis.opsForValue().get("accesstoken"), postData);
                //Map<String, Object> tmpMap = new HashMap<>();
                //tmpMap.put("aaa", postData);
                String result = HttpClientUtil.postStringEntity(apiUrl+accesstoken, postData);
                log.info(result);
            }
            //response.getWriter().print("ok");

        }

        return FusenJSONResult.success();
    }




    /*public static void main(String[] args) {

        //MongoClient mongoClient = new MongoClient("192.168.128.128", 27017);
        //MongoDatabase testDB = mongoClient.getDatabase("test");

        ServerAddress serverAddress = new ServerAddress("192.168.128.128", 27017);
        //List<ServerAddress> addrs = new ArrayList<>();
        //addrs.add(serverAddress);

        MongoCredential scramSha1Credential = MongoCredential.createScramSha1Credential("scott", "test", "tiger".toCharArray());
        //List<MongoCredential> credentials = new ArrayList<>();
        //credentials.add(scramSha1Credential);


        MongoClient mongoClient = new MongoClient(serverAddress, scramSha1Credential, MongoClientOptions.builder().build());
        MongoDatabase testdb = mongoClient.getDatabase("test");
        MongoCollection<Document> collection = testdb.getCollection("test");
        FindIterable<Document> iterable = collection.find();
        MongoCursor<Document> iterator = iterable.iterator();
        while (iterator.hasNext()){
            Document doc = iterator.next();
            System.out.println(doc.toJson());
        }


        FindIterable<Document> documents = collection.find(new Document("name", "zhangsan"));
        documents.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                System.out.println(document.toJson());
            }
        });


    }*/
}
