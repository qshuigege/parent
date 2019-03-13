package com.fs.everything.utils;

import java.util.Map;

public class WXUtils {

	/**
	 * //满足UnionID返回条件时，返回的JSON数据包
	 * {
	 *     "openid": "OPENID",
	 *     "session_key": "SESSIONKEY",
	 *     "unionid": "UNIONID"
	 * }
	 * 小程序登录
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static Map<String, Object> miniprogLogin(String code) throws Exception{
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+R_WX.APPID_MINIPROG+"&secret="+R_WX.APPSECRET_MINIPROG+"&js_code="+code+"&grant_type=authorization_code";
		String json;
		try {
			json = HttpClientUtils.get(url);
		}catch (Exception e){
			throw new Exception("WXUtils.miniprogLogin() httpclient exception! -->"+e.getMessage());
		}
		Map<String, Object> map = JsonUtils.jsonToMap(json);
		if (null!=map.get("errcode")){
			//log.error("WXUtils.miniprogLogin() exception! -->{}",json);
			throw new Exception("WXUtils.miniprogLogin() exception! -->"+json);
		}
		return map;
	}
}
