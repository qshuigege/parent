package com.example.demo.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

public class WechatApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {
    /*private static StringRedisTemplate redisOperator;
    public static StringRedisTemplate getRedisOperator(){
        return redisOperator;
    }
    private static JdbcTemplate jdbcTemplate;
    public static JdbcTemplate getJdbcTemplate(){
        return jdbcTemplate;
    }*/

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        StaticResourceConfiguration staticResourceConfiguration = (StaticResourceConfiguration) applicationContext.getBean("staticResourceConfiguration");
        /*System.out.println(staticResourceConfiguration.getAppid());
        System.out.println(staticResourceConfiguration.getAppsecret());
        System.out.println(staticResourceConfiguration.getOriginalid());*/
        /*StringRedisTemplate op= (StringRedisTemplate) applicationContext.getBean("stringRedisTemplate");
        JdbcTemplate mysqlJdbcTemplate = (JdbcTemplate)applicationContext.getBean("mysqlJdbcTemplate");
        redisOperator = op;
        jdbcTemplate = mysqlJdbcTemplate;*/
        /*String accesstoken = null;
        try{
            accesstoken = R_WX.getAccessToken();
        }catch (Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        op.set("accesstoken", accesstoken);*/

        /*SchedulerFactory sf = new StdSchedulerFactory();
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
        }*/




    }
}
