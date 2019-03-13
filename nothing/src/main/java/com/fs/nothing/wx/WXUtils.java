package com.fs.nothing.wx;

import com.fs.nothing.utils.HttpClientUtils;
import com.fs.nothing.wx.apimodel.AccessTokenGZPT;
import com.fs.nothing.wx.apimodel.AccessTokenGZPT_WY;
import com.fs.nothing.wx.apimodel.WXUserInfo;
import net.sf.json.JSONObject;


public class WXUtils {
	
	/*public static String getAccessToken(String apiUrl){
		String jsonStr = HttpClientUtils.get(apiUrl);
		JSONObject jo = JSONObject.fromObject(jsonStr);
		String access_token = (String)jo.get("access_token");
		if(access_token!=null&&!"".equals(access_token)){
			return access_token;
		}else{
			return null;
		}
	}
	
	*//**
	 * 创建自定义菜单
	 * @param apiUrl
	 * @param menuJson
	 * @return	返回操作后提示信息(json格式{"errcode":0,"errmsg":"ok"}{"errcode":40018,"errmsg":"invalid button name size"})
	 *//*
	public static String createMenu(String apiUrl, String menuJson){
		
		String jsonStr = HttpClientUtils.postStringEntity(apiUrl, menuJson);
		return jsonStr;
		
	}
	
	*//**
	 * 获得用户基本信息
	 * @param access_token
	 * @param openid
	 * @return
	 * 
	 * 正常情况返回：{   "subscribe": 1, 
   "openid": "o6_bmjrPTlm6_2sgVt7hMZOPfL2M", 
   "nickname": "Band", 
   "sex": 1, 
   "language": "zh_CN", 
   "city": "广州", 
   "province": "广东", 
   "country": "中国", 
   "headimgurl":  "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4
eMsv84eavHiaiceqxibJxCfHe/0",
  "subscribe_time": 1382694957,
  "unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
  "remark": "",
  "groupid": 0,
  "tagid_list":[128,2]
}
	 */
	public static JSONObject getUserInfo_JSONObject(String access_token, String openid) throws Exception{
		String apiUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+access_token+"&lang=zh_CN&openid="+openid;
		String jsonStr = null;
		try {
			jsonStr = HttpClientUtils.get(apiUrl);
		}catch (Exception e){
			throw new Exception("");
		}
		JSONObject jo = JSONObject.fromObject(jsonStr);
		if (jo.get("errcode")==null){
			return jo;
		}else {
			throw new Exception("WXUtils.getUserInfo() get get user info from wechat server failed-->errcode:"+jo.get("errcode")+", errmsg:"+jo.get("errmsg"));
		}
	}
	
	/**
	 * 微信公众平台开发获取AccessToken接口
	 * @return JavaBean,包装了AccessToken
	 */
	public static AccessTokenGZPT getAccessToken() throws Exception{
		String apiUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
			+ R_WX.APPID + "&secret=" + R_WX.APPSECRET;
		String jsonStr = null;
		try {
			jsonStr = HttpClientUtils.get(apiUrl);
		}catch (Exception e){
			throw new Exception("WXUtils.getAccessTokenGZPT() httpconn exception-->"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(jsonStr);
		if(jo.get("errcode")==null){
			AccessTokenGZPT obj = (AccessTokenGZPT)JSONObject.toBean(jo, AccessTokenGZPT.class);
			return obj;
		}else{
			throw new Exception("WXUtils.getAccessTokenGZPT() get get AccessToken from wechat server failed-->errcode:"+jo.get("errcode")+", errmsg:"+jo.get("errmsg"));
		}
	}
	
	/**
	 * 微信网页开发获取AccessToken接口
	 * @return JavaBean,包装了AccessToken
	 */
	public static AccessTokenGZPT_WY getAccessToken_WY(String code) throws Exception{
		String apiUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid="+R_WX.APPID +"&secret="+R_WX.APPSECRET +"&code="+code+"&grant_type=authorization_code";
		String jsonStr = null;
		try {
			jsonStr = HttpClientUtils.get(apiUrl);
		}catch (Exception e){
			throw new Exception("WXUtils.AccessTokenGZPT_WY() httpconn exception-->"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(jsonStr);
		if(jo.get("errcode")==null){
			AccessTokenGZPT_WY obj = (AccessTokenGZPT_WY)JSONObject.toBean(jo, AccessTokenGZPT_WY.class);
			return obj;
		}else{
			throw new Exception("WXUtils.getAccessToken_WY() get get AccessToken_WY from wechat server failed-->errcode:"+jo.get("errcode")+", errmsg:"+jo.get("errmsg"));
		}
	}
	
	/**
	 * 微信网页开发获取用户基本信息接口
	 * @return JavaBean,包装了AccessToken
	 */
	public static JSONObject getUserinfo_WY(String access_token,String openid) throws Exception{
		String apiUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		String jsonStr = null;
		try {
			jsonStr = HttpClientUtils.get(apiUrl);
		}catch (Exception e){
			throw new Exception("WXUtils.getUserinfo_WY() httpconn exception-->"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(jsonStr);
		if (jo.get("errcode")==null) {
			jo.remove("privilege");//待以后业务需要再改写，先不要。
			return jo;
		}else{
			throw new Exception("WXUtils.getUserinfo_WY() get user info_WY from wechat server failed-->errcode:"+jo.get("errcode")+", errmsg:"+jo.get("errmsg"));
		}
	}
	
	/**
	 * 微信网页开发获取用户基本信息接口
	 * @return JavaBean,包装了AccessToken
	 */
	public static WXUserInfo getUserinfo_WY2(String access_token,String openid)throws Exception{
		String apiUrl = "https://api.weixin.qq.com/sns/userinfo?access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
		String jsonStr = null;
		try {
			jsonStr = HttpClientUtils.get(apiUrl);
		}catch (Exception e){
			throw new Exception("WXUtils.getUserinfo_WY2() httpconn exception-->"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(jsonStr);
		if(jo.get("errcode")==null){
			jo.remove("privilege");//待以后业务需要再改写，先不要。
			return (WXUserInfo)JSONObject.toBean(jo, WXUserInfo.class);
		}else{
			throw new Exception("WXUtils.getUserinfo_WY2() get user info_WY2 from wechat server failed-->errcode:"+jo.get("errcode")+", errmsg:"+jo.get("errmsg"));
		}
	}
	
	/**
	 * 获取微信用户基本信息
	 * @param accessToken
	 * @param openid
	 * @return javaBean,包装了用户基本信息
	 */
	public static WXUserInfo getUserInfo(String accessToken, String openid) throws Exception {
		String apiUrl = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+accessToken+"&lang=zh_CN&openid="+openid;
		String jsonStr = null;
		try {
			jsonStr = HttpClientUtils.get(apiUrl);
		}catch (Exception e){
			throw new Exception("WXUtils.getUserInfo() httpconn exception-->"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(jsonStr);
		if(jo.get("errcode")==null){
			return (WXUserInfo)JSONObject.toBean(jo, WXUserInfo.class);
		}else{
			throw new Exception("WXUtils.getUserInfo() get user info from wechat server failed-->errcode:"+jo.get("errcode")+", errmsg:"+jo.get("errmsg"));
		}
		
	}
	
	/**
	 * 创建微信公众号菜单
	 * @param accessToken
	 * @param menuJson
	 * @return 创建成功返回true,否则返回false
	 */
	public static boolean createMenu(String accessToken, String menuJson) throws Exception {
		String apiUrl = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+accessToken;
		String jsonStr = null;
		try {
			jsonStr = HttpClientUtils.postStringEntity(apiUrl, menuJson);
		}catch (Exception e){
			throw new Exception("WXUtils.createMenu() httpconn exception-->"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(jsonStr);
		if(jo.get("errcode")==null){
			return true;
		}else{
			return false;
		}
		
	}


	/**
	 * 创建个性化菜单
	 * @param accesstoken
	 * @param menuJson
	 * @return menuid
	 * @throws Exception
	 */
	public static String addConditionalMenu(String accesstoken, String menuJson) throws Exception {
		String url="https://api.weixin.qq.com/cgi-bin/menu/addconditional?access_token="+accesstoken;
		String resultJson = null;
		try {
			resultJson = HttpClientUtils.postStringEntity(url, menuJson);
		}catch (Exception e){
			throw new Exception("WXUtils.addConditionalMenu() httpconn exception-->"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(resultJson);
		if (jo.has("menuid")){
			return jo.getString("menuid");
		}else{
			throw new Exception("WXUtils.addConditionalMenu() 创建个性化菜单失败！-->"+jo.toString());
		}
	}


    /**
     * 发送客服消息
     * @param accesstoken
     * @param openid
     * @param content
     * @return
     * @throws Exception
     */
	public static String sendKeFuMsg(String accesstoken, String openid, String content) throws Exception {
		String url="https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+accesstoken;
		String postData ="{\"touser\":\""+openid+"\",				\"msgtype\":\"text\",				\"text\":			{				\"content\":\""+content+"\"			}		}";
		String resultJson = null;
		try {
			resultJson = HttpClientUtils.postStringEntity(url, postData);
		}catch (Exception e){
			throw new Exception("WXUtils.sendKeFuMsg() httpconn exception-->"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(resultJson);
		/*if (jo.has("menuid")){
			return jo.getString("menuid");
		}else{
			throw new Exception("WXUtils.addConditionalMenu() 创建个性化菜单失败！-->"+jo.toString());
		}*/
		return resultJson;
	}

	public static String generateQrcode(String accesstoken, String eventkey, long duration)throws Exception{
		//43200,二维码失效时间12小时
		String postData = "{\"expire_seconds\": "+duration+", \"action_name\": \"QR_STR_SCENE\", \"action_info\": {\"scene\": {\"scene_str\": \"" + eventkey + "\"}}}";
		String ticket_qr_code_url = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=" + accesstoken;
		String ticketJson = null;
		String ticket = null;
		try {
			ticketJson = HttpClientUtils.postStringEntity(ticket_qr_code_url, postData);
		} catch (Exception e) {
			throw new Exception("WXUtils.generateQrcode() -->"+e.getMessage());
		}
		JSONObject jo = JSONObject.fromObject(ticketJson);
		if (jo.size() > 0 && null == jo.get("ticket")) {
			throw new Exception("WXUtils.generateQrcode() errcode-->"+ jo.getString("errcode") +", errmsg-->"+ jo.getString("errmsg"));
		}
		ticket = jo.getString("ticket");
		return ticket;
		//String erweimaurl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket="+ticket;
	}
}
