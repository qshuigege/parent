package com.fs.nothing.wx;

import com.fs.nothing.config.WechatApplicationReadyEventListener;
import com.fs.nothing.utils.HttpClientUtils;
import com.fs.nothing.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

public class R_WX {
	//public static String APPID_GZPT = "";
	//public static String APPSECRET_GZPT = "";

	//public static String APPID = "wx633dc6d72b14c174";// "wxf48eadf1ba6a4e64";
	//public static String APPSECRET = "f40df3e84e9ad02567d3ece30df0ee26";// "2351893b2755f98c823a0bf527746ad7";
	//public static String ORIGINAL_ID = "gh_7c3b6f6158d7";//"gh_efc52d49a695"

	private static Logger log = LoggerFactory.getLogger(R_WX.class);

	private static JdbcTemplate jdbcTemplate = WechatApplicationReadyEventListener.getJdbcTemplate();

    //public static ArrayBlockingQueue<Map<String, String>> LOGIN_QRCODE_QUEUE;
    public static ArrayBlockingQueue<String[]> LOGIN_QRCODE_QUEUE;
	public static String DOMAIN;
	public static String APPID;// = "wx2502c42b3e3cd660";//chenb
	public static String APPSECRET;// = "8ce511053b5b11060d7ceef9d54a0e5b";//
	public static String ORIGINAL_ID;// = "gh_7b7815cd56b5";
	public static String APPID_OPEN_PLATFORM;// = "wx92e45ce9f10eee08";//开放平台APPID
	public static String APPSECRET_OPEN_PLATFORM;// = "a46be8fe9a5ff98018eada332dc1a51c";//开放平台SECRET
	public static String TEMPLATEMSG_FAHUO;
	public static String TEMPLATEMSG_FAHUO_KEY = "PUSH_ORDER_STATUS_INFO_FA";
	public static String TEMPLATEMSG_SHOUHUO;
	public static String TEMPLATEMSG_SHOUHUO_KEY = "PUSH_ORDER_STATUS_INFO_SHOU";
	public static String TEMPLATEMSG_SHCONFIRM;
	public static String TEMPLATEMSG_SHCONFIRM_KEY = "PUSH_ORDER_STATUS_INFO_SHCONFIRM";
	public static int RETRYTIMES;
	public static int CAPACITY;
	public static long DURATION;
	public static String DACHUANG_BIND_QRCODE_CALLBACK_URL;
	public static String DACHUANG_TEMPLATEMSG_SHENPI;
	public static String DACHUANG_MINIPROG_APPID;


	static {

		APPID = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppid();
		APPSECRET = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppsecret();
		ORIGINAL_ID = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getOriginalid();
		APPID_OPEN_PLATFORM = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppid_openplatform();
		APPSECRET_OPEN_PLATFORM = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getAppsecret_openplatform();
		DOMAIN = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getDomain();
		TEMPLATEMSG_FAHUO = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getTemplatemsg_fahuo();
		TEMPLATEMSG_SHOUHUO = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getTemplatemsg_shouhuo();
		TEMPLATEMSG_SHCONFIRM = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getTemplatemsg_shconfirm();
		RETRYTIMES = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getRetrytimes();
		CAPACITY = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getCapacity();
		DURATION = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getDuration();
		DACHUANG_BIND_QRCODE_CALLBACK_URL = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getDachuang_bind_qrcode_callback_url();
		DACHUANG_TEMPLATEMSG_SHENPI = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getDachuang_templatemsg_shenpi();
		DACHUANG_MINIPROG_APPID = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getDachuang_miniprog_appid();
		LOGIN_QRCODE_QUEUE = new ArrayBlockingQueue<>(CAPACITY);

		/*if(null == APPID||"".equals(APPID)){
			String sql = "select * from staticresource";
			Map<String, String> map = new HashMap<>();
			List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql);
			Map<String, Object> temp = null;
			for (int i = 0; i < mapList.size(); i++) {
				temp = mapList.get(i);
				map.put(temp.get("name").toString(),temp.get("content").toString());
				log.info("{}-->{}", temp.get("name"), temp.get("content"));
			}

			if("dev".equals(map.get("env_mode"))){
				log.info("env_mode-->dev");
				APPID = map.get("appid_chenb");
				APPSECRET = map.get("appsecret_chenb");
				ORIGINAL_ID = map.get("originalid_chenb");
			}else{
				log.info("env_mode-->prod");
				APPID = map.get("appid");
				APPSECRET = map.get("appsecret");
				ORIGINAL_ID = map.get("originalid");
			}
			APPID_OPEN_PLATFORM = map.get("appid_openplatform");
			APPSECRET_OPEN_PLATFORM = map.get("appsecret_openplatform");
		}*/
	}

	//private static String APPID_ajc = "wx94bb21425d376e62";
	//private static String APPSECRET_ajc = "e9d7d3450e243e7678762be879657530";
	//public static String ACCESS_TOKEN = getAccessToken();
	//public static String ACCESS_TOKEN_ajc = getAccessToken_ajc();
	
	//获取AccessToken接口(GET方式)
	/*public static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
			+ APPID + "&secret=" + APPSECRET;
	
	
	//获取带参数的二维码ticket接口
	public static String TICKET_QR_CODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token="+ACCESS_TOKEN;

	
	//二维码ticket接口
	public static String QR_CODE_URL = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=";

	//获取用户基本信息（包括UnionID机制）
	public static String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token="+ACCESS_TOKEN+"&lang=zh_CN&openid=";
	*/
	
	
	/**
	 * 初始化AccessToken，并且设置定时任务，每隔一个小时刷新AccessToken
	 * @return
	 */
	/*static{
		//ACCESS_TOKEN = getAccessToken();
			//System.out.println("\"com.actionsoft.apps.fs_b2b.weixin.R_WX\"类的\"getAccessToken()\"方法运行完毕，初始化access_token完成-->"+ACCESS_TOKEN);
			//向用到该AccesssToken的服务器推送新的AccesssToken
			//WXUtils.pushAccessToken();

			//if(ACCESS_TOKEN!=null){
				SchedulerFactory sf = new StdSchedulerFactory();
				try {
					Scheduler scheduler = sf.getScheduler();
					JobDetail jobDetail = new JobDetail("job1", "job_group_1", MyJob.class);
					CronTrigger cronTrigger = new CronTrigger("trigger1", "trigger_group_1", "0 0 0/1 * * ?");
					scheduler.scheduleJob(jobDetail, cronTrigger);
					scheduler.start();				
					
				} catch (SchedulerException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
			//}
	
		
	}*/

	/**
	 * 获取access_token。获取不到返回null.
	 * @return
	 */
	public static String getAccessToken() throws Exception{
		String apiUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+ APPID + "&secret=" + APPSECRET;
		String jsonStr = null;
		try {
			//jsonStr = HttpRequestUtils.requestStringGet(apiUrl);
			jsonStr = HttpClientUtils.get(apiUrl);
		}catch (Exception e){
			throw new Exception("R_WX.getAccessToken() httpconn exception-->"+e.getMessage());
		}
		Map<String, Object> jo = JsonUtils.jsonToMap(jsonStr);
		if (jo.get("errcode")!=null&&"50002".equals(jo.get("errcode").toString())) {
			throw new Exception("R_WX.getAccessToken() get accesstoken failed-->errcode:" + jo.get("errcode") + ", errmsg:" + jo.get("errmsg"));
		}
		return jo.get("access_token")==null?null:jo.get("access_token").toString();
	}
	
	/*public static String getAccessToken_ajc() throws Exception{
		String apiUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
				+ APPID_ajc + "&secret=" + APPSECRET_ajc;
			String jsonStr = HttpRequestUtils.requestStringGet(apiUrl);
			JSONObject jo = JSONObject.fromObject(jsonStr);
			String access_token = (String)jo.get("access_token");
			if(access_token!=null&&!"".equals(access_token)){
				return access_token;
			}else{
				return null;
			}
	}*/

}
