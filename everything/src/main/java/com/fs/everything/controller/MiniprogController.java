package com.fs.everything.controller;

import com.fs.everything.pojo.B2B_USER;
import com.fs.everything.pojo.UserContext;
import com.fs.everything.pojo.WXAccount;
import com.fs.everything.qywx.JsApiTicketSign;
import com.fs.everything.qywx.QYWXUtils;
import com.fs.everything.qywx.R_QYWX;
import com.fs.everything.service.WXBusinessService;
import com.fs.everything.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/miniprog/shconfirm")
public class MiniprogController {

    private static Logger log = LoggerFactory.getLogger(MiniprogController.class);

    @Autowired
    private WXBusinessService service;

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redis;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @RequestMapping("callback/wxMiniprogEntrance")
    public FusenJSONResult wxMiniprogEntrance(HttpServletRequest request){
        String code = request.getParameter("code");
        String encryptedData = request.getParameter("encryptedData");
        String iv = request.getParameter("iv");
        if (null==code||"".equals(code)){
            log.info("参数code为空！");
            return FusenJSONResult.failMsg("参数code为空！");
        }
        Map<String, Object> loginResult;
        try {
             loginResult = WXUtils.miniprogLogin(code);
             log.info("MiniprogController.wxMiniprogEntrance() loginResult-->{}", JsonUtils.objectToJson(loginResult));
        } catch (Exception e) {
            log.error("MiniprogController.wxMiniprogEntrance() exception! -->{}", e.getMessage());
            return FusenJSONResult.failMsg("MiniprogController.wxMiniprogEntrance() exception! -->"+e.getMessage());
        }

        String openid = loginResult.get("openid").toString();
        //String unionid = loginResult.get("unionid").toString();
        String session_key = loginResult.get("session_key").toString();
        String decryptData = null;
        try {
            decryptData = AESUtils.decrypt(encryptedData, session_key, iv);
            log.info("解密后-->{}", decryptData);
        } catch (Exception e) {
            log.error("MiniprogController.wxMiniprogEntrance() exception! -->{}", e.getMessage());
            return FusenJSONResult.failMsg("MiniprogController.wxMiniprogEntrance() exception! -->{}" + e.getMessage());
        }
        Map<String, Object> accMap = JsonUtils.jsonToMap(decryptData);

        if(!service.isAccExist(openid)) {
            WXAccount acc = new WXAccount();
            acc.setAppid(R_WX.ORIGINAL_ID_MINIPROG);
            acc.setOpenid(openid);
            acc.setUnionid(accMap.get("unionId").toString());
            acc.setCity(accMap.get("city").toString());
            acc.setCountry(accMap.get("country").toString());
            acc.setHeadimgurl(accMap.get("avatarUrl").toString());
            acc.setNickname(accMap.get("nickName").toString());
            acc.setProvince(accMap.get("province").toString());
            acc.setSex(accMap.get("gender").toString());
            try {
                service.addMiniprogAcc(acc);
            } catch (Exception e) {
                log.error("MiniprogController.wxMiniprogEntrance() exception! -->{}", e.getMessage());
                return FusenJSONResult.failMsg("MiniprogController.wxMiniprogEntrance() exception! -->" + e.getMessage());
            }
            log.info("用户(openid:{})第一次访问小程序，已保存用户信息。",openid);
            /*Map<String, String> t = new HashMap<>();
            t.put("cause", "用户第一次访问小程序，已保存用户信息。");
            return FusenJSONResult.buildFail(t);*/
        }

        boolean bl = service.isBinded(accMap.get("unionId").toString(), openid);
        if (!bl){
            log.info("该微信号(openid:{})没有与b2b账号绑定。",openid);
            return FusenJSONResult.failMsg("该微信号没有与b2b账号绑定。");
        }

        List<B2B_USER> userList = null;
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("unionid", accMap.get("unionId"));
            userList = service.getOrguserByUnionid(params);
        } catch (Exception e) {
            log.error("MiniprogController.wxMiniprogEntrance()-->",e.getMessage());
            return FusenJSONResult.failMsg("MiniprogController.wxMiniprogEntrance()-->"+e.getMessage());
        }
        B2B_USER user = userList.get(0);
        /*String f_role = user.getF_ROLE();
        String[] split = f_role.split(",");
        boolean privilege = false;
        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith("7")){
                privilege = true;
                break;
            }
        }
        if (!privilege){
            log.error("没有权限访问小程序！");
            return FusenJSONResult.failMsg("没有权限访问小程序！");
        }*/
        if ('0'==user.getMiniprogflag()){
            try {
                service.changeMiniprogflag("1", user.getF_ID());
            }catch (Exception e){
                log.error("MiniprogController.wxMiniprogEntrance()设置miniprogflag异常！-->", e.getMessage());
                return FusenJSONResult.failMsg("MiniprogController.wxMiniprogEntrance()设置miniprogflag异常！-->"+e.getMessage());
            }
        }
        Map<String, Object> session_key_map = new HashMap<>();
        session_key_map.put("session_key", session_key);
        user.setExtra(session_key_map);
        String sessionid = ReusableCodes.createSession(redis, user);
        log.info("登录成功，sessionid-->{}", sessionid);
        Map<String, Object> r = new HashMap<>();
        r.put("sessionId", sessionid);
        r.put("miniprogflag", user.getMiniprogflag());
        r.put("privilege", user.getF_ROLE());
        return FusenJSONResult.buildSuccess(r);
    }

    @RequestMapping("callback/qywxMiniprogEntrance")
    public FusenJSONResult qywxMiniprogEntrance(HttpServletRequest request){
        String code = request.getParameter("code");
        if (null==code||"".equals(code)){
            return FusenJSONResult.failMsg("参数code不能为空！");
        }
        log.info("code-->{}", code);
        Map<String, Object> userInfo = null;
        Map<String, Object> userInfoDetail = null;
        try {
            userInfo = QYWXUtils.qywxMiniprogLogin(code, R_QYWX.SECRET_LIST.get(0));
            log.info("企业微信userid-->{}", userInfo);
            userInfoDetail = QYWXUtils.getUserInfo(userInfo.get("userid").toString(), R_QYWX.SECRET_LIST.get(0));
            //userInfoDetail = QYWXUtils.getUserInfo("qy015c0502d353b801287b6b60fe", R_QYWX.SECRET_LIST.get(0));
            log.info("userInfoDetail-->{}", userInfoDetail);
        } catch (Exception e) {
            log.error("MiniprogController.qywxMiniprogEntrance() exception！-->{}", e.getMessage());
            return FusenJSONResult.failMsg("MiniprogController.qywxMiniprogEntrance() exception！-->"+e.getMessage());
        }

        Map<String, Object> params = new HashMap<>();
        params.put("userid", userInfoDetail.get("email"));
        List<B2B_USER> userList = null;
        String orgid = null;
        try {
            userList = service.getOrguserByUserid(params);
        } catch (Exception e) {
            try {
                params.put("role", R_QYWX.MINIPROG_ROLE);
                params.put("rolename", R_QYWX.MINIPROG_ROLENAME);
                params.put("name", userInfoDetail.get("name"));
                params.put("qywxuserid", userInfoDetail.get("userid"));
                orgid = service.addOrguserAccQywx(params);
                params.put("orgid", orgid);
                userList = service.getOrguserByOrgid(params);
            } catch (Exception ee) {
                log.error("MiniprogController.qywxMiniprogEntrance() exception！-->{}", ee.getMessage());
                FusenJSONResult.failMsg("MiniprogController.qywxMiniprogEntrance() exception！-->"+ee.getMessage());
            }
        }

        B2B_USER user = userList.get(0);
        /*String f_role = user.getF_ROLE();
        String[] split = f_role.split(",");
        boolean privilege = false;
        for (int i = 0; i < split.length; i++) {
            if (split[i].startsWith("7")){
                privilege = true;
                break;
            }
        }
        if (!privilege){
            log.error("没有权限访问小程序！");
            return FusenJSONResult.failMsg("没有权限访问小程序！");
        }*/
        if ('0'==user.getMiniprogflag()){
            try {
                service.changeMiniprogflag("1", user.getF_ID());
            }catch (Exception e){
                log.error("MiniprogController.wxMiniprogEntrance()设置miniprogflag异常！-->", e.getMessage());
                return FusenJSONResult.failMsg("MiniprogController.wxMiniprogEntrance()设置miniprogflag异常！-->"+e.getMessage());
            }
        }
        Map<String, Object> session_key_map = new HashMap<>();
        session_key_map.put("session_key", userInfo.get("session_key"));
        user.setExtra(session_key_map);
        String sessionid = ReusableCodes.createSession(redis, user);
        log.info("登录成功，sessionid-->{}", sessionid);
        Map<String, Object> r = new HashMap<>();
        r.put("sessionId", sessionid);
        r.put("miniprogflag", user.getMiniprogflag());
        r.put("privilege", user.getF_ROLE());
        return FusenJSONResult.buildSuccess(r);
    }

    @RequestMapping("callback/jsApiTicketSignMiniprog001")
    public FusenJSONResult jsApiTicketSignMiniprog001(HttpServletRequest request){
        String url = request.getParameter("url");
        if (null==url||"".equals(url)){
            return FusenJSONResult.failMsg("参数url不能为空！");
        }
        Map<String, String> sign = JsApiTicketSign.sign(redis.opsForValue().get("jsapiticket_" + R_QYWX.SECRET_LIST.get(0)), url);
        log.info("MiniprogController.jsApiTicketSignMiniprog001() sign-->{}", sign);
        return FusenJSONResult.buildSuccess(sign);
    }

    @RequestMapping("callback/jsApiTicketSignMiniprog002")
    public FusenJSONResult jsApiTicketSignMiniprog002(HttpServletRequest request){
        String url = request.getParameter("url");
        if (null==url||"".equals(url)){
            return FusenJSONResult.failMsg("参数url不能为空！");
        }
        Map<String, String> sign = JsApiTicketSign.sign(redis.opsForValue().get("jsapiticket_" + R_QYWX.SECRET_LIST.get(1)), url);
        log.info("MiniprogController.jsApiTicketSignMiniprog002() sign-->{}", sign);
        return FusenJSONResult.buildSuccess(sign);
    }

    @RequestMapping("callback/jsApiTicketSignMiniprog003")
    public FusenJSONResult jsApiTicketSignMiniprog003(HttpServletRequest request){
        String url = request.getParameter("url");
        if (null==url||"".equals(url)){
            return FusenJSONResult.failMsg("参数url不能为空！");
        }
        Map<String, String> sign = JsApiTicketSign.sign(redis.opsForValue().get("jsapiticket_" + R_QYWX.SECRET_LIST.get(2)), url);
        log.info("MiniprogController.jsApiTicketSignMiniprog003() sign-->{}", sign);
        return FusenJSONResult.buildSuccess(sign);
    }

    @RequestMapping("callback/jsApiTicketSignMiniprog004")
    public FusenJSONResult jsApiTicketSignMiniprog004(HttpServletRequest request){
        String url = request.getParameter("url");
        if (null==url||"".equals(url)){
            return FusenJSONResult.failMsg("参数url不能为空！");
        }
        Map<String, String> sign = JsApiTicketSign.sign(redis.opsForValue().get("jsapiticket_" + R_QYWX.SECRET_LIST.get(3)), url);
        log.info("MiniprogController.jsApiTicketSignMiniprog003() sign-->{}", sign);
        return FusenJSONResult.buildSuccess(sign);
    }

    @RequestMapping("getPSAddr")
    public FusenJSONResult getPSAddr(HttpServletRequest request){
        String psno = request.getParameter("psno");
        if (null==psno||"".equals(psno)){
            return FusenJSONResult.failMsg("参数psno为空！");
        }
        try {
            Map<String, Object> addr = service.getPSAddr(psno);
            return FusenJSONResult.buildSuccess(addr);
        }catch (Exception e){
            return FusenJSONResult.failMsg("MiniprogController.getPSAddr() exception!-->"+e.getMessage());
        }
    }

    @RequestMapping("doshconfirm")
    public FusenJSONResult doshconfirm(HttpServletRequest request){
        String shpc002oid = request.getParameter("shpc002oid");
        String psno = request.getParameter("psno");
        String confirmplace = request.getParameter("confirmplace");
        String pics = request.getParameter("pics");
        String remark = request.getParameter("remark");
        String sumcartons = request.getParameter("sumcartons");
        String fsno = request.getParameter("fsno");
        String basb001oid = request.getParameter("basb001oid");
        String partnername = request.getParameter("partnername");
        log.info("shpc002oid-->{}, psno-->{}, confirmplace-->{}, pics-->{}, remark-->{}, sumcartons-->{}, fsno-->{}, basb001oid-->{}, partnername-->{}",shpc002oid, psno, confirmplace, pics, remark, sumcartons, fsno, basb001oid, partnername);
        if (null==psno||"".equals(psno)||null==confirmplace||"".equals(confirmplace)||null==sumcartons||"".equals(sumcartons)||null==fsno||"".equals(fsno)||null==basb001oid||"".equals(basb001oid)||null==partnername||"".equals(partnername)||null==shpc002oid||"".equals(shpc002oid)){
            log.info("psno,confirmplace,sumcartons,fsno,basb001oid,partnername,shpc002oid不能为空！");
            return FusenJSONResult.failMsg("psno,confirmplace,sumcartons,fsno,basb001oid,partnername,shpc002oid不能为空！");
        }
        UserContext uc = (UserContext) request.getAttribute("userContext");
        try {
            service.doshconfirm(shpc002oid, psno, confirmplace, pics, remark, uc.getOrgid());
            log.info("MiniprogController.doshconfirm() 送货回签成功！");
            boolean b = service.isNeedShconfirmMsg(basb001oid);
            if(b) {
                Map<String, String> kafkaMap = new HashMap<>();
                kafkaMap.put("busicode","0");
                kafkaMap.put("aaa", R_WX.TEMPLATEMSG_SHCONFIRM_KEY);
                StringBuffer sb = new StringBuffer();
                sb.append("1:尊敬的客户，您的货物已经签收|2:")
                        .append(partnername)
                        .append("|3:")
                        .append(fsno)
                        .append("|4:")
                        .append(sumcartons)
                        .append("件|5:送货|6:")
                        .append(DateUtils.format_m(new Date()))
                        .append("|7:感谢您的使用|ERPOID:")
                        .append(basb001oid);
                kafkaMap.put("bbb", sb.toString());
                try {
                    kafkaTemplate.send("template-message", JsonUtils.objectToJson(kafkaMap));
                    log.info("MiniprogController.doshconfirm()推送微信模板消息成功。{}", sb.toString());
                } catch (Exception e) {
                    log.info("MiniprogController.doshconfirm()推送微信模板消息异常。{}-->{}", sb.toString(), e.getMessage());
                }
            }
            return FusenJSONResult.success();
        }catch (Exception e){
            log.error("MiniprogController.doshconfirm() exception! -->"+e.getMessage());
            return FusenJSONResult.failMsg("MiniprogController.doshconfirm() exception! -->"+e.getMessage());
        }
    }


    @RequestMapping("getTHInfo")
    public FusenJSONResult getTHInfo(HttpServletRequest request){
        String thno = request.getParameter("thno");
        if (null==thno||"".equals(thno)){
            return FusenJSONResult.failMsg("参数thno为空！");
        }
        try {
            List<Map<String, Object>> data = service.getTHInfo(thno);
            //CommonUtils.replaceNullValueOfMap(data);
            return FusenJSONResult.buildSuccess(data);
        }catch (Exception e){
            return FusenJSONResult.failMsg("MiniprogController.getTHInfo() exception!-->"+e.getMessage());
        }
    }


    @RequestMapping("saveStepData")
    public FusenJSONResult saveStepData(HttpServletRequest request){
        String shpc002oid = request.getParameter("shpc002oid");
        String docno = request.getParameter("docno");
        String sth_cur_step = request.getParameter("sth_cur_step");
        String step_place = request.getParameter("step_place");
        String pics = request.getParameter("pics");
        String remark = request.getParameter("remark");
        log.info("shpc002oid-->{}, docno-->{}, sth_cur_step-->{}, step_place-->{}, pics-->{}, remark-->{}", shpc002oid, docno, sth_cur_step, step_place, pics, remark);
        if (null==docno||"".equals(docno)||null==sth_cur_step||"".equals(sth_cur_step)||null==step_place||"".equals(step_place)||null==shpc002oid||"".equals(shpc002oid)){
            log.error("参数docno,sth_cur_step,step_place,shpc002oid不能为空！");
            return FusenJSONResult.failMsg("参数docno,sth_cur_step,step_place,shpc002oid不能为空！");
        }
        UserContext uc = (UserContext)request.getAttribute("userContext");
        try {
            service.saveStepData(shpc002oid, sth_cur_step, docno, step_place, pics, remark,uc.getQywxuserid()+","+uc.getQywxnickname());
            log.info("MiniprogController.saveStepData() success. (sth_cur_step:{}, docno:{})", sth_cur_step, docno);
            return FusenJSONResult.success();
        }catch (Exception e){
            log.error("MiniprogController.saveStepData() exception! -->{}", e.getMessage());
            return FusenJSONResult.failMsg("MiniprogController.saveStepData() exception! -->"+e.getMessage());
        }
    }


}
