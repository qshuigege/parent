package com.fs.nothing.config;

import com.fs.nothing.wx.MyJob;
import com.fs.nothing.wx.R_WX;
import com.fs.nothing.wx.WXUtils;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class WechatApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

    private static Logger log = LoggerFactory.getLogger(WechatApplicationReadyEventListener.class);

    private static StringRedisTemplate redisOperator;
    public static StringRedisTemplate getRedisOperator(){
        return redisOperator;
    }
    private static JdbcTemplate jdbcTemplate;
    public static JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }
    private static StaticResourceConfiguration staticResourceConfiguration;
    public static  StaticResourceConfiguration getStaticResourceConfiguration(){
        return staticResourceConfiguration;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        /*StringRedisTemplate op= (StringRedisTemplate) applicationContext.getBean("stringRedisTemplate");
        JdbcTemplate mysqlJdbcTemplate = (JdbcTemplate)applicationContext.getBean("mysqlJdbcTemplate");
        StaticResourceConfiguration staticInfo = (StaticResourceConfiguration) applicationContext.getBean("staticResourceConfiguration");
        redisOperator = op;
        jdbcTemplate = mysqlJdbcTemplate;
        staticResourceConfiguration = staticInfo;*/
        redisOperator = (StringRedisTemplate) applicationContext.getBean("stringRedisTemplate");
        jdbcTemplate = (JdbcTemplate)applicationContext.getBean("mysqlJdbcTemplate");
        staticResourceConfiguration = (StaticResourceConfiguration) applicationContext.getBean("staticResourceConfiguration");

        /*String accesstoken = null;
        try{
            accesstoken = R_WX.getAccessToken();
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        op.set("accesstoken", accesstoken);*/

        if("1".equals(staticResourceConfiguration.getInit_data_flag())) {//redis中的共享数据，只需集群中的一台机器负责初始化
            log.info("项目启动，初始化数据！");
            SchedulerFactory sf = new StdSchedulerFactory();
            try {
                Scheduler scheduler = sf.getScheduler();
                JobDetail jobDetail = JobBuilder.newJob(MyJob.class).withIdentity("job1").build();//new JobDetail("job1", "job_group_1", MyJob.class);
                SimpleScheduleBuilder ssbuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(1).repeatForever();
                SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity("trigger1").startNow().withSchedule(ssbuilder).build();//new CronTrigger("trigger1", "trigger_group_1", "0 0 0/1 * * ?");
                //CronTrigger cronTrigger = ;
                //TriggerBuilder.newTrigger().withIdentity("crontrigger1").startNow().withSchedule(SimpleScheduleBuilder.).build();
                scheduler.start();
                scheduler.scheduleJob(jobDetail, simpleTrigger);

            } catch (SchedulerException e) {
                e.printStackTrace();
            }

        }

        log.info("开启获取登录二维码线程");
        while (redisOperator.opsForValue().get("accesstoken") == null){
            log.info("redis中尚未保存accesstoken，等待从微信服务器获取到accesstoken。。。");
            try {
                Thread.sleep(500);
            }catch (Exception e){
                e.printStackTrace();
                log.error("WechatApplicationReadyEventListener.onApplicationEvent() exception! -->", e.getMessage());
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try {
                        String uuid = UUID.randomUUID().toString();
                        long expirytime = System.currentTimeMillis()+R_WX.DURATION;
                        String ticket = WXUtils.generateQrcode(redisOperator.opsForValue().get("accesstoken"), "forlogin_" + uuid, R_WX.DURATION);
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

    }
}
