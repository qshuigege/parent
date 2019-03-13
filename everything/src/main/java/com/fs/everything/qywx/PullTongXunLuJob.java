package com.fs.everything.qywx;

import com.fs.everything.config.WechatApplicationReadyEventListener;
import com.fs.everything.repository.UCML_UserDao;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PullTongXunLuJob implements Job {

	private static Logger log = LoggerFactory.getLogger(PullTongXunLuJob.class);

	public void execute(JobExecutionContext context) {
		log.info("开始拉取企业微信通讯录信息");
		//System.out.println("asdfasdfasdfasdfasdf-->{}"+context.getJobDetail().getKey());
		StringRedisTemplate redis = WechatApplicationReadyEventListener.getRedisOperator();
		UCML_UserDao ucml_UserDao = WechatApplicationReadyEventListener.getUcml_UserDao();
		String accesstoken = redis.opsForValue().get("accesstoken_tongxunlu");
		String departmentid = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getTongxunlu_departmentid();
		String fetchchild = WechatApplicationReadyEventListener.getStaticResourceConfiguration().getTongxunlu_fetchchild();
		List<Map<String, Object>> mapList;
		try {
			mapList = QYWXUtils.pullTongXunLu(accesstoken, departmentid, fetchchild);
		}catch (Exception e){
			log.error("PullTongXunLuJob.execute() exception!-->{}", e.getMessage());
			return;
		}
		for (int i = 0; i < mapList.size(); i++) {
			Map<String, Object> map = mapList.get(i);
			//log.info("userid-->{},name-->{},email-->{},avatar-->{}", map.get("userid"), map.get("name"), map.get("email"), map.get("avatar"));
			String fsid = map.get("userid")==null?"":map.get("userid").toString();
			Map<String, Object> params = new HashMap<>();
			Map<String, Object> erpData;
			params.put("fsid", fsid);
			try {
				erpData = ucml_UserDao.getUcmluserinfoByFsid(params);
			}catch (Exception e){
				log.error("PullTongXunLuJob.execute() getUcmluserinfoByFsid(fsid:{}) exception!-->{}", fsid, e.getMessage());
				continue;
			}
			String ucml_useroid = erpData.get("ucml_useroid")==null?"":erpData.get("ucml_useroid").toString();
			params.clear();
			params.put("name",erpData.get("personname"));
			params.put("ucml_useroid", ucml_useroid);
			params.put("orgname",erpData.get("orgname"));
			params.put("zrzx", erpData.get("zrzx"));
			params.put("con_email_addr", erpData.get("con_email_addr"));
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
		log.info("PullTongXunLuJob.execute() 通讯录已保存到redis");
	}
}
