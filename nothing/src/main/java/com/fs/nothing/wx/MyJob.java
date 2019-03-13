package com.fs.nothing.wx;

import com.fs.nothing.config.WechatApplicationReadyEventListener;
import com.fs.nothing.utils.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class MyJob implements Job {
	public static Logger log = LoggerFactory.getLogger(MyJob.class);
	//private static boolean flag = false;
	public void execute(JobExecutionContext context) {
		//System.out.println(DateUtils.format(new Date(System.currentTimeMillis())));
		//String jsonStr = HttpRequestUtils.requestStringGet(R_WX.ACCESS_TOKEN_URL);
		String access_token = null;
		for (int i = 0; i < R_WX.RETRYTIMES&&access_token==null; i++) {
			log.info("第{}次获取accesstoken", i+1);
			try {
				access_token = R_WX.getAccessToken();
			}catch (Exception e){
				log.error("access_token刷新失败！-->{}",e.getMessage());
			}
			//ajc_sign
			//String access_token_ajc = R_WX.getAccessToken_ajc();
			if (access_token!=null) {
				//JSONObject jo = JSONObject.fromObject(jsonStr);
				//R_WX.ACCESS_TOKEN = access_token;

				//WXUtils.pushAccessToken();

				WechatApplicationReadyEventListener.getRedisOperator().opsForValue().set("accesstoken", access_token);
				log.info("accesstoken from wechat server({})-->{}",DateUtils.format_no_space(new Date()),access_token);
				/*if (flag==false){
					flag=true;
					log.info("开启获取登录二维码线程");

					new Thread(new Runnable() {
						@Override
						public void run() {
							while (true){
								try {
									String uuid = UUID.randomUUID().toString();
									long expirytime = System.currentTimeMillis()+R_WX.DURATION;
									String ticket = WXUtils.generateQrcode(WechatApplicationReadyEventListener.getRedisOperator().opsForValue().get("accesstoken"), "forlogin_" + uuid, R_WX.DURATION);
									log.info("获取登录二维码成功(ticket:{})。", ticket);
									//Map<String, String> m = new HashMap<>();
									//m.put("forlogin_"+uuid, ticket);
									String[] arr = new String[]{"forlogin_"+uuid, ticket, expirytime+""};
									try{
										log.info("开始往队列中添加二维码,size->"+R_WX.LOGIN_QRCODE_QUEUE.size());
										R_WX.LOGIN_QRCODE_QUEUE.put(arr);
										log.info("将二维码(ticket:{})添加到登录二维码队列。", ticket);
									}catch (Exception e){
										log.error("登录二维码队列添加元素异常！-->"+e.getMessage());
										throw e;
									}
								}catch (Exception e){
									log.error("获取登录二维码失败。-->{}", e.getMessage());
									try {
										Thread.sleep(5000);
									}catch (Exception ee){
										log.warn(ee.getMessage());
									}
								}

							}
						}
					}).start();

				}*/



			}else{
				log.error("access_token刷新失败! access_token from wechat server is null");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.error(e.getMessage());
			}
		}

		//System.out.println(new Date());
		//ajc_sign
		/*if (access_token_ajc!=null) {
			//JSONObject jo = JSONObject.fromObject(jsonStr);
			R_WX.ACCESS_TOKEN_ajc = access_token_ajc;
			
			WXUtils.pushAccessToken_ajc();
		}else{
			System.out.println(DateUtils.format(new Date(System.currentTimeMillis()))+"：access_token_ajc刷新失败！");
		}*/
	}
}
