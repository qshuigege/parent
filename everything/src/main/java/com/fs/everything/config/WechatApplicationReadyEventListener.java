package com.fs.everything.config;

import com.fs.everything.qywx.MyJob;
import com.fs.everything.qywx.PullTongXunLuJob;
import com.fs.everything.repository.UCML_UserDao;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

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
    private static JdbcTemplate sqlserverJdbcTemplate;
    public static JdbcTemplate getSqlserverJdbcTemplate(){
        return sqlserverJdbcTemplate;
    }
    private static UCML_UserDao ucml_UserDao;
    public static UCML_UserDao getUcml_UserDao(){
        return ucml_UserDao;
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
        JdbcTemplate sqlserverJdbcTpl = (JdbcTemplate)applicationContext.getBean("defaultJdbcTemplate");
        UCML_UserDao ucmluserDao = (UCML_UserDao)applicationContext.getBean("ucml_UserDaoImpl");
        StaticResourceConfiguration staticInfo = (StaticResourceConfiguration) applicationContext.getBean("staticResourceConfiguration");
        redisOperator = op;
        jdbcTemplate = mysqlJdbcTemplate;
        sqlserverJdbcTemplate = sqlserverJdbcTpl;
        ucml_UserDao = ucmluserDao;
        staticResourceConfiguration = staticInfo;*/

        redisOperator= (StringRedisTemplate) applicationContext.getBean("stringRedisTemplate");
        jdbcTemplate = (JdbcTemplate)applicationContext.getBean("mysqlJdbcTemplate");
        sqlserverJdbcTemplate = (JdbcTemplate)applicationContext.getBean("defaultJdbcTemplate");
        ucml_UserDao = (UCML_UserDao)applicationContext.getBean("ucml_UserDaoImpl");
        staticResourceConfiguration = (StaticResourceConfiguration) applicationContext.getBean("staticResourceConfiguration");

        if ("1".equals(staticResourceConfiguration.getInit_data_flag())) {
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

                //启动时先执行一次，以后每天的23点定时执行
                JobDetail onceJob = JobBuilder.newJob(PullTongXunLuJob.class).withIdentity("onceJob").build();
                SimpleScheduleBuilder onceSSBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(1).withRepeatCount(0);
                SimpleTrigger onceSimpleTigger = TriggerBuilder.newTrigger().withIdentity("onceTriger").startNow().withSchedule(onceSSBuilder).build();
                scheduler.scheduleJob(onceJob, onceSimpleTigger);

                JobDetail jobDetai2 = JobBuilder.newJob(PullTongXunLuJob.class).withIdentity("job2").build();
                CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 23 * * ?");//每天23点执行
                CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity("cronTrigger1").withSchedule(cronScheduleBuilder).build();
                scheduler.scheduleJob(jobDetai2, cronTrigger);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
    }

}
