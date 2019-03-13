package com.fs.everything.utils;

import com.fs.everything.config.WechatApplicationReadyEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class R_WX {

	private static final Logger log = LoggerFactory.getLogger(R_WX.class);

	public static String DOMAIN;
	public static String APPID;// = "wx2502c42b3e3cd660";//chenb
	public static String APPSECRET;// = "8ce511053b5b11060d7ceef9d54a0e5b";//
	public static String ORIGINAL_ID;// = "gh_7b7815cd56b5";
	public static String APPID_MINIPROG;
	public static String APPSECRET_MINIPROG;
	public static String ORIGINAL_ID_MINIPROG;
	public static String APPID_OPEN_PLATFORM;// = "wx92e45ce9f10eee08";//开放平台APPID
	public static String APPSECRET_OPEN_PLATFORM;// = "a46be8fe9a5ff98018eada332dc1a51c";//开放平台SECRET
	public static String TEMPLATEMSG_FAHUO;
	public static String TEMPLATEMSG_FAHUO_KEY = "PUSH_ORDER_STATUS_INFO_FA";
	public static String TEMPLATEMSG_SHOUHUO;
	public static String TEMPLATEMSG_SHOUHUO_KEY = "PUSH_ORDER_STATUS_INFO_SHOU";
	public static String TEMPLATEMSG_SHCONFIRM;
	public static String TEMPLATEMSG_SHCONFIRM_KEY = "PUSH_ORDER_STATUS_INFO_SHCONFIRM";
	public static int RETRYTIMES;


	static {

		/*APPID = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppid();
		APPSECRET = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppsecret();
		ORIGINAL_ID = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getOriginalid();*/
		APPID_MINIPROG = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppid_miniprog();
		APPSECRET_MINIPROG = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppsecret_miniprog();
		ORIGINAL_ID_MINIPROG = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getOriginalid_miniprog();
		APPID_OPEN_PLATFORM = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppid_openplatform();
		APPSECRET_OPEN_PLATFORM = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppsecret_openplatform();
		/*DOMAIN = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getDomain();
		TEMPLATEMSG_FAHUO = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getTemplatemsg_fahuo();
		TEMPLATEMSG_SHOUHUO = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getTemplatemsg_shouhuo();
		TEMPLATEMSG_SHCONFIRM = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getTemplatemsg_shconfirm();
		RETRYTIMES = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getRetrytimes();*/

	}


}
