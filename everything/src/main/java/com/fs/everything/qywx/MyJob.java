package com.fs.everything.qywx;

import com.fs.everything.config.WechatApplicationReadyEventListener;
import com.fs.everything.utils.DateUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;


public class MyJob implements Job {
	public static Logger log = LoggerFactory.getLogger(MyJob.class);
	public void execute(JobExecutionContext context) {

		for (int i = 0; i < R_QYWX.SECRET_LIST.size(); i++) {
			try {
				String accessToken = QYWXUtils.getAccessToken(R_QYWX.SECRET_LIST.get(i));
				WechatApplicationReadyEventListener.getRedisOperator().opsForValue().set("accesstoken_"+R_QYWX.SECRET_LIST.get(i), accessToken);
				WechatApplicationReadyEventListener.getRedisOperator().opsForValue().set("accesstoken_"+R_QYWX.AGENTID_LIST.get(i), accessToken);
				String jsApiTicket = QYWXUtils.getJsApiTicket(accessToken);
				WechatApplicationReadyEventListener.getRedisOperator().opsForValue().set("jsapiticket_"+R_QYWX.SECRET_LIST.get(i), jsApiTicket);
				WechatApplicationReadyEventListener.getRedisOperator().opsForValue().set("jsapiticket_"+R_QYWX.AGENTID_LIST.get(i), jsApiTicket);
				log.info("accesstoken,jsapiticket刷新成功！{}(agentid:{})-->token:{}, ticket:{}",DateUtils.format_no_space(new Date()), R_QYWX.AGENTID_LIST.get(i), accessToken, jsApiTicket);
			} catch (Exception e) {
				log.error("accesstoken,jsapiticket刷新失败！(agentid:{})-->{}",R_QYWX.AGENTID_LIST.get(i), e.getMessage());
			}
		}

	}
}
