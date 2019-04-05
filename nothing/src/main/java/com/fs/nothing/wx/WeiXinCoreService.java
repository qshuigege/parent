package com.fs.nothing.wx;

import com.fs.nothing.pojo.B2B_USER;
import com.fs.nothing.service.WXBusinessService;
import com.fs.nothing.utils.HttpClientUtils;
import com.fs.nothing.utils.JsonUtils;
import com.fs.nothing.utils.ReusableCodes;
import com.fs.nothing.wx.apimodel.WXAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 核心服务类
 * @author 简爱微萌
 * @Email zyw205@gmial.com
 *Administrator
 */
public class WeiXinCoreService {

    public static Logger log = LoggerFactory.getLogger(WeiXinCoreService.class);
    public static String keyWord;


    public static final String MENU2="技术支持群:347245650 345531810 欢迎关注：Javen_Crazy";

    public static final String MENU="谢谢关注！！！";
    public static final String SERVER_EXCEPTION = "服务器异常！";
    /**
     * 处理微信发来的请求
     *
     * @return
     */
    public static String processRequest(WXBusinessService service, Map<String, String> requestMap, String accesstoken, StringRedisTemplate redis) {

        // 发送方帐号（open_id）
        String fromUserName = requestMap.get("FromUserName");
        // 公众帐号
        String toUserName = requestMap.get("ToUserName");
        // 消息类型
        String msgType = requestMap.get("MsgType");
        // 回复文本消息
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(fromUserName);
        textMessage.setFromUserName(toUserName);
        textMessage.setCreateTime(new Date().getTime());
        textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
        textMessage.setFuncFlag(0);
        // 默认返回的文本消息内容
        String respContent = "您输入的内容无效！";


        // 文本消息
        if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)) {

            String msgCont = requestMap.get("Content");

            log.info("您输入的内容-->" + msgCont);
            respContent = "你输入的是文本消息，内容为：\n" + msgCont;

		/*if (msgCont.startsWith("接入")) {
			keyWord = msgCont.replaceAll("^接入[\\+ ~!@#%^-_=]?", "");
			if (keyWord.startsWith("gh_")) {
				respContent="URL:http://java205.sinaapp.com/Javen?id="+ keyWord+"\n Token:Javen";
			}else {
				respContent="请输入正确的原始id \n 请登录微信公众平台>设置>账号信息>查看原始ID";
			}
			//respContent="因为服务器原因此功能暂时不开放 /::P/::P";
		}*/
        }
        // 图片消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {
            String picUrl = requestMap.get("PicUrl");
            respContent = FaceService.detect(picUrl);
        }
        // 地理位置消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {
            respContent = "您发送的是地理位置消息！";
            String Location_X = requestMap.get("Location_X");
            String Location_Y = requestMap.get("Location_Y");
            log.info("Location_X:" + Location_X + " Location_Y:" + Location_Y);
        }
        // 链接消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)) {
            respContent = "您发送的是链接消息！";
        }
        // 音频消息
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)) {

            String recognition = requestMap.get("Recognition");
            if (recognition == null || recognition.trim().equals("")) {
                respContent = "您发送的是音频消息！";
            } else {
                respContent = "语音识别结果:" + recognition;
            }

        }
        // 事件推送
        else if (msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)) {

            // 事件类型
            String eventType = requestMap.get("Event");
            // 订阅(用户关注公众号)
            if (eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)) {
                String eventKey = requestMap.get("EventKey");
                if (eventKey == null || "".equals(eventKey)) {//用户扫描公众号二维码并点击关注
                    Map<String, Object> jo = null;
                    try {
                        jo = WXUtils.getUserInfo_JSONObject(accesstoken, requestMap.get("FromUserName"));
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        respContent = SERVER_EXCEPTION;
                        textMessage.setContent(respContent);
                        return MessageUtil.textMessageToXml(textMessage);
                    }
                    service.addAcc(createWXAccountFromJSONObject(jo));//用户关注公众号，将该用户的信息保存到账号表（如果是用户取消关注后再次关注，一样可以直接往wxaccount表插入数据，但是会插入失败，因为openid是主键）
                    respContent = MENU;
                } else {//用户扫描带参数的二维码并点击关注
                    eventKey = eventKey.replace("qrscene_", "");//可能是微信的Bug，如果用户未关注公众号，扫描带参数的二维码，参数前会加上"qrscene_"
                    log.info("replace后二维码带的参数----------->" + eventKey);
                    Map<String, Object> jo = null;
                    try {
                        jo = WXUtils.getUserInfo_JSONObject(accesstoken, requestMap.get("FromUserName"));
                    }catch (Exception e){
                        log.error(e.getMessage());
                        respContent = SERVER_EXCEPTION;
                        textMessage.setContent(respContent);
                        return MessageUtil.textMessageToXml(textMessage);
                    }
                    String unionid = jo.get("unionid").toString();
                    service.addAcc(createWXAccountFromJSONObject(jo));//用户关注公众号，将该用户的信息保存到账号表（如果是用户取消关注后再次关注，一样可以直接往wxaccount表插入数据，但是会插入失败，因为openid是主键）

                    if(eventKey.startsWith("crm_bind_")){
                        //调用大创接口
                        log.info("调用大创接口");
                        Map<String, Object> params = new HashMap<>();
                        eventKey = eventKey.replace("crm_bind_", "");
                        params.put("userId",eventKey);
                        params.put("openId",requestMap.get("FromUserName"));
                        params.put("unionId", unionid);
                        try {
                            String r = HttpClientUtils.post(R_WX.DACHUANG_BIND_QRCODE_CALLBACK_URL, params);
                            log.info("调用大创接口result-->{}", r);
                            Map<String, Object> m = JsonUtils.jsonToMap(r);
                            if (null!=m.get("status")&&"success".equals(m.get("status"))) {
                                respContent = m.get("msg").toString();
                            }
                        }catch (Exception e){
                            log.error("调用大创接口result-->{}",e.getMessage());
                            respContent = SERVER_EXCEPTION;
                            textMessage.setContent(respContent);
                            return MessageUtil.textMessageToXml(textMessage);
                        }

                    }else if (eventKey.startsWith("forlogin_")){
                        if(service.isBinded(jo.get("unionid").toString(), jo.get("openid").toString())){
                            respContent = "登录成功！";
                            //String unionid = jo.getString("unionid");
                            List<B2B_USER> orguserList = null;
                            B2B_USER user = null;
                            try {
                                Map<String, Object> params = new HashMap<>();
                                params.put("unionid", unionid);
                                orguserList = service.getOrguserByUnionid(params);
                                user = orguserList.get(0);
                            }catch (Exception e){
                                log.error("service.getOrguserByUnionid()-->"+e.getMessage());
                            }
                            String sessionId = ReusableCodes.createSession(redis, user);
                            Map<String, String> m = new HashMap<>();
                            m.put("sessionId",sessionId);
                            m.put("role",user.getF_ROLE());
                            m.put("name",user.getF_YHMC());
                            redis.opsForValue().set(eventKey, JsonUtils.objectToJson(m));
                            redis.expire(eventKey, 60, TimeUnit.SECONDS);

                        }else {
                            respContent = "您的微信尚未与B2B账号绑定，无法使用扫码登录功能！";
                            redis.opsForValue().set(eventKey,"0");
                            redis.expire(eventKey, 60, TimeUnit.SECONDS);//设置1分钟过期
                        }
                    }else if ("0".equals(eventKey)) {
                        //用户解绑
                        if (service.isBinded(jo.get("unionid").toString(), jo.get("openid").toString())) {

                            Map<String, Object> params = new HashMap<>();
                            params.put("unionid", jo.get("unionid"));
                            if (service.unbinding(params)) {
                                respContent = "感谢关注！\n解绑成功！";
                            } else {
                                respContent = "感谢关注！\n服务器异常，解绑失败！";
                            }
                        } else {
                            respContent = "感谢关注！\n您的微信尚未与B2B账号绑定，无需解绑！";
                        }

                    } else {
                        //用户绑定
                        if (eventKey.startsWith("qrscene_")) {//可能是微信的Bug，如果用户未关注公众号，扫描带参数的二维码，参数前会加上"qrscene_"
                            eventKey = eventKey.replace("qrscene_", "");
                            requestMap.put("EventKey", eventKey);
                        }
                        service.addAcc(createWXAccountFromJSONObject(jo));//用户关注公众号，将该用户的信息保存到账号表

                        boolean isBinded = service.isBinded(jo.get("unionid").toString(), jo.get("openid").toString());
                        if (isBinded) {//如果已经绑定
                            respContent = "感谢关注！\n您的B2B账号已经与该微信绑定过，无需再次绑定！";
                        } else {
							/*String orgid = null;
							try {
								orgid = service.getOrgidByUniqueid(requestMap.get("EventKey"));
							}catch (Exception e){
								log.error("用户扫描带参数的二维码并点击关注-->"+e.getMessage());
								respContent = "感谢关注！\n服务器异常，绑定失败！";
								textMessage.setContent(respContent);
								return MessageUtil.textMessageToXml(textMessage);
							}
							boolean b = service.binding(jo.getString("unionid"), orgid);*/
                            boolean b = service.binding(jo.get("unionid").toString(), eventKey, jo.get("openid").toString());
                            if (b) {
                                respContent = "感谢关注！\n您的微信已与B2B账号绑定，您现在可以通过微信扫描的方式来登录富森B2B系统！";
                            } else {
                                respContent = "感谢关注！\n服务器异常，绑定失败！";
                            }
                        }

                    }
                }
            }
            //用户已关注时的事件推送
            //扫带参数的二维码
            else if (eventType.equals(MessageUtil.EVENT_TYPE_SCAN)) {
                String eventKey = requestMap.get("EventKey");
                eventKey = eventKey.replace("qrscene_", "");
                log.info("二维码带的参数----------->" + eventKey);
                Map<String, Object> jo = null;
                try {
                    jo = WXUtils.getUserInfo_JSONObject(accesstoken, requestMap.get("FromUserName"));
                }catch (Exception e){
                    log.error(e.getMessage());
                    respContent = SERVER_EXCEPTION;
                    textMessage.setContent(respContent);
                    return MessageUtil.textMessageToXml(textMessage);
                }
                String unionid = jo.get("unionid").toString();
                if(eventKey.startsWith("crm_bind_")){
                    //调用大创接口
                    log.info("调用大创接口");
                    Map<String, Object> params = new HashMap<>();
                    eventKey = eventKey.replace("crm_bind_", "");
                    params.put("userId",eventKey);
                    params.put("openId",requestMap.get("FromUserName"));
                    params.put("unionId", unionid);
                    try {
                        String r = HttpClientUtils.post(R_WX.DACHUANG_BIND_QRCODE_CALLBACK_URL, params);
                        log.info("调用大创接口result-->{}", r);
                        Map<String, Object> m = JsonUtils.jsonToMap(r);
                        if (null!=m.get("status")&&"success".equals(m.get("status"))) {
                            respContent = m.get("msg").toString();
                        }
                    }catch (Exception e){
                        log.error("调用大创接口result-->{}",e.getMessage());
                        respContent = SERVER_EXCEPTION;
                        textMessage.setContent(respContent);
                        return MessageUtil.textMessageToXml(textMessage);
                    }

                }else if(eventKey.startsWith("forlogin_")){
                    log.info("forlogin_ unionid-->"+unionid);
                    if(service.isBinded(unionid, jo.get("openid").toString())){
                        respContent = "登录成功！";
                        List<B2B_USER> orguserList = null;
                        B2B_USER user = null;
                        try {
                            Map<String, Object> params = new HashMap<>();
                            params.put("unionid", unionid);
                            orguserList = service.getOrguserByUnionid(params);
                            user = orguserList.get(0);
                        }catch (Exception e){
                            log.error("WeiXinCoreService.processRequest() service.getOrguserByUnionid()-->"+e.getMessage());
                        }
                        String sessionId = ReusableCodes.createSession(redis, user);
                        Map<String, String> m = new HashMap<>();
                        m.put("sessionId",sessionId);
                        m.put("role",user.getF_ROLE());
                        m.put("name",user.getF_YHMC());
                        redis.opsForValue().set(eventKey, JsonUtils.objectToJson(m));
                        redis.expire(eventKey, 60, TimeUnit.SECONDS);
                        /*if(!redis.hasKey(unionid)){
                            String sessionId = UUID.randomUUID().toString();
                            LoginCache lc = new LoginCache(user.getF_ID(),sessionId,user.getF_KH_FK(),user.getF_YHLX(),user.getF_GSMC(),unionid,user.getF_FSKHBM());
                            redis.opsForValue().set(unionid,"json:"+sessionId);
                            redis.expire(unionid, 1800,TimeUnit.SECONDS);
                            redis.opsForValue().set("json:"+sessionId, JsonUtils.objectToJson(lc));
                            redis.expire("json:"+sessionId,1800, TimeUnit.SECONDS);//设置30分钟过期
                            Map<String, String> m = new HashMap<>();
                            m.put("role",user.getF_ROLE());
                            m.put("name",user.getF_YHMC());
                            m.put("sessionId",sessionId);
                            redis.opsForValue().set(eventKey, JsonUtils.objectToJson(m));
                            redis.expire(eventKey, 60, TimeUnit.SECONDS);//设置1分钟过期
                        }else{
                            redis.expire(unionid, 1800, TimeUnit.SECONDS);
                            redis.expire(redis.opsForValue().get(unionid), 1800, TimeUnit.SECONDS);
                            Map<String, String> m = new HashMap<>();
                            m.put("role",user.getF_ROLE());
                            m.put("name",user.getF_YHMC());
                            m.put("sessionId",redis.opsForValue().get(unionid).substring(5));
                            redis.opsForValue().set(eventKey, JsonUtils.objectToJson(m));
                            redis.expire(eventKey, 60, TimeUnit.SECONDS);
                        }*/
                        /*String jsonStr = redis.opsForValue().get(redis.opsForValue().get(unionid));
                        LoginCache loginCache = JsonUtils.jsonToPojo(jsonStr, LoginCache.class);
                        log.info("sessionId-->"+loginCache.getSessionID());*/

                    }else {
                        respContent = "您的微信尚未与B2B账号绑定，无法使用扫码登录功能！";
                        redis.opsForValue().set(eventKey,"0");
                        redis.expire(eventKey, 60, TimeUnit.SECONDS);//设置1分钟过期
                    }
                }else if ("0".equals(eventKey) || "qrscene_0".equals(eventKey)) {
                    //用户解绑
                    if (service.isBinded(unionid, jo.get("openid").toString())) {

                        Map<String, Object> params = new HashMap<>();
                        params.put("unionid", unionid);
                        if (service.unbinding(params)) {
                            respContent = "解绑成功！";
                        } else {
                            respContent = "服务器异常，解绑失败！";
                        }
                    } else {
                        respContent = "您的微信尚未与B2B账号绑定，无需解绑！";
                    }

                } else {
                    //用户绑定
                    service.addAcc(createWXAccountFromJSONObject(jo));//用户关注公众号，将该用户的信息保存到账号表

                    boolean isBinded = service.isBinded(jo.get("unionid").toString(), jo.get("openid").toString());
                    if (isBinded) {//如果已经绑定
                        respContent = "您的B2B账号已经与该微信绑定，无需再次绑定！";
                    } else {
						/*String orgid = null;
						try {
							orgid = service.getOrgidByUniqueid(requestMap.get("EventKey"));
						}catch (Exception e){
							log.error("用户扫描带参数的二维码并点击关注-->"+e.getMessage());
							respContent = "服务器异常，绑定失败！";
							textMessage.setContent(respContent);
							return MessageUtil.textMessageToXml(textMessage);
						}
						boolean b = service.binding(jo.getString("unionid"), orgid);*/
                        boolean b = service.binding(jo.get("unionid").toString(), eventKey, jo.get("openid").toString());
                        if (b) {
                            respContent = "绑定成功！\n您的微信已与B2B账号绑定，您现在可以通过微信扫描的方式来登录富森B2B系统！";
                        } else {
                            respContent = "服务器异常，绑定失败！";
                        }
                    }

                }
            }
            // 取消订阅
            else if (eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {

                //用户取消关注，将wxaccount表subscribe字段设为0
                service.unsubscribe(requestMap.get("FromUserName"));

            }else if(eventType.equals("VIEW")){
                //log.info("viewwwwwwwwwwwwwwwwwwwwwwwwwwwww");
                String wodedingdan = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+R_WX.APPID+"&redirect_uri=http%3A%2F%2F"+R_WX.DOMAIN+"%2Fgongzhonghao&response_type=code&scope=snsapi_base&state=1234567#wechat_redirect";
                if(requestMap.get("EventKey").equals(wodedingdan)){
                    log.info("用户点击菜单“我的订单”");
                    Map<String, Object> jo = null;
                    try {
                        jo = WXUtils.getUserInfo_JSONObject(accesstoken, requestMap.get("FromUserName"));
                    }catch (Exception e){
                        log.error("用户点击我的订单-->{}",e.getMessage());
                    }
                    String unionid = jo.get("unionid").toString();
                    if(!service.isBinded(unionid, jo.get("openid").toString())){
                        try {
                            String resultMsg = WXUtils.sendKeFuMsg(accesstoken, requestMap.get("FromUserName"), "该微信号未与富森B2B账号绑定，无法查看订单信息！");
                            log.info("发送客服消息成功-->{}",resultMsg);
                        }catch (Exception e){
                            log.error("用户点击我的订单发送客服信息失败！-->{}", e.getMessage());
                        }
                    }/*else{
						try {
							WXUtils.sendKeFuMsg(accesstoken, requestMap.get("FromUserName"), "欢迎查看订单信息！");
						}catch (Exception e){
							log.error("用户点击我的订单发送客服信息失败！-->{}", e.getMessage());
						}
					}*/
                }
            }
            // 自定义菜单点击事件
            else if (eventType.equals(MessageUtil.EVENT_TYPE_CLICK)) {
                //自定义菜单权没有开放，暂不处理该类消息
                if (requestMap.get("EventKey").equals("wuliugenzong")) {
                    String erpoid = "";
                    String sql = "select o.erp_useroid from orguser o where o.id = (select r.orgid from wxaccount a join wxacc_orguser_relation r on a.unionid = r.unionid where a.openid =?)";
                    Map<String, Object> map = null;//jdbcTemplate.queryForMap(sql, new Object[]{requestMap.get("FromUserName")});
                    erpoid = (String) map.get("erp_useroid");
                    //DBResource dbres = DaoSupport.executeQuery(sql, new Object[]{requestMap.get("FromUserName")});
                    //ResultSet rs = dbres.getResultSet();
                    //while (rs.next()) {
                    //	erpoid = rs.getString("erp_useroid");
                    //}
                    //DaoSupport.closeDBRes(dbres);
                    respContent = "<a href='http://b2b.fusen.net.cn/r/w?sid=sid&cmd=com.actionsoft.apps.fs_b2b.ckwl.CheckLogistcs.GetOrderMobileWeb&ERPUSERID=" + erpoid + "'>物流跟踪</a>";
                } else if (requestMap.get("EventKey").equals("test_click")) {
                    //回复图文消息
				/*NewsMessage newsMessage = new NewsMessage();
				newsMessage.setToUserName(fromUserName);
				newsMessage.setFromUserName(toUserName);
				newsMessage.setCreateTime(new Date().getTime());
				newsMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_NEWS);
				newsMessage.setFuncFlag(0);
				newsMessage.setArticleCount(1);
				List<Article> articles = new ArrayList<Article>();
				Article article = new Article();
				article.setDescription("富森十五周年征文投票开始啦！每人有 25 次点赞机会，每篇文章只能点赞 1 次，选出你心目中最优的25 篇作品吧！");
				article.setPicUrl("http://b2b.fusen.net.cn/wei/images/dianzanFengmian.jpg");
				article.setTitle("富森十五周年征文投票25强入围赛");
				//article.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf48eadf1ba6a4e64&redirect_uri=http%3A%2F%2Fb2b.fusen.net.cn%2Fwei%2Fcontroller%2FQuestionnaireController%2FgetPage.action&response_type=code&scope=snsapi_userinfo&state=1234567#wechat_redirect");
				article.setUrl("https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf48eadf1ba6a4e64&redirect_uri=http%3A%2F%2Fb2b.fusen.net.cn%2Fwei%2Fcontroller%2FDianzanController%2FgetPage.action&response_type=code&scope=snsapi_userinfo&state=1234567#wechat_redirect");
				articles.add(article);
				newsMessage.setArticles(articles);
				respMessage = MessageUtil.newsMessageToXml(newsMessage);
				return respMessage;*/
                }
            }

        }

        textMessage.setContent(respContent);
        return MessageUtil.textMessageToXml(textMessage);

    }


    private static WXAccount createWXAccountFromJSONObject(Map<String, Object> jo){
        WXAccount acc = new WXAccount();
        if (jo.get("subscribe") == null) {//未关注用户与公众号地一次交互信息(微信api规定，获取未关注用户的基本信息中,没有subscribe)
            acc.setSubscribe("0");
        } else {//用户主动关注公众号，往wxaccount表中添加或更新信息
            acc.setSubscribe("1");
        }
        acc.setAppid(R_WX.ORIGINAL_ID);
        acc.setCreatedate(new Date());
        acc.setUnionid(jo.containsKey("unionid") ? jo.get("unionid").toString() : "");
        acc.setCity(jo.get("city").toString());
        acc.setCountry(jo.get("country").toString());
        acc.setGroupid(jo.get("groupid").toString());
        acc.setHeadimgurl(jo.get("headimgurl").toString());
        acc.setLanguage(jo.get("language").toString());
        acc.setNickname(jo.get("nickname").toString());
        acc.setOpenid(jo.get("openid").toString());
        acc.setProvince(jo.get("province").toString());
        acc.setRemark(jo.get("remark").toString());
        acc.setSex(jo.get("sex").toString());
        acc.setSubscribe_time(jo.get("subscribe_time").toString());
        acc.setTagid_list(jo.containsKey("tagid_list")?jo.get("tagid_list").toString():"tagid_list");
        acc.setSubscribe_scene(jo.get("subscribe_scene").toString());
        acc.setQr_scene(jo.get("qr_scene").toString());
        acc.setQr_scene_str(jo.get("qr_scene_str").toString());
        return acc;
    }

}
