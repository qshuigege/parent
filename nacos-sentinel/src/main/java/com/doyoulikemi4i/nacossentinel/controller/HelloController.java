package com.doyoulikemi4i.nacossentinel.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.doyoulikemi4i.nacossentinel.pojo.User;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class HelloController {

    private static final String RESOURCE_NAME = "hello";
    private static final String USER_RESOURCE_NAME = "user";
    private static final String DEGRADE_RESOURCE_NAME = "degrade";

    @RequestMapping("/hello")
    public String hello(){
        Entry entry = null;
        try {
            entry = SphU.entry(RESOURCE_NAME);
            //被保护的业务逻辑
            return "hello world";
        } catch (BlockException e) {
            //log.info("block!");
            return "被流控了！";
        } catch (Exception e){
            //若需要配置降级规则，需要通过这种方式记录业务异常
            Tracer.traceEntry(e, entry);
        } finally {
            if(entry!=null){
                entry.exit();
            }
        }
        return null;
    }

    /**
     * value 定义资源
     * blockHandler 设置流控降级后的处理方法
     * fallback 当出现了异常后的处理方法
     * blockHandler和fallback可以定义在其它类中，但必须是static的
     * 两者如果同时指定了，则blockHandler优先级更高
     * exceptionsToIgnore 哪些异常不处理
     * @param id
     * @return
     */
    @RequestMapping("/user")
    @SentinelResource(value = USER_RESOURCE_NAME, blockHandler = "blockHandlerForGetUser", fallback = "fallbackHandlerForGetUser"/*, exceptionsToIgnore = {ArithmeticException.class}*/)
    public User getUser(int id){
        int a = 1/id;
        return new User("zhangsan");
    }

    /**
     * 注意：
     * 1. 一定要public
     * 2. 返回值一定要和原方法保持一致，包含原方法的参数
     * 3. 可以在参数最后添加BlockException，可以区分是什么规则的处理方法
     * @param id
     * @param e
     * @return
     */
    public User blockHandlerForGetUser(int id, BlockException e){
        e.printStackTrace();
        return new User("流控！");
    }

    public User fallbackHandlerForGetUser(int id, Throwable e){
        e.printStackTrace();
        return new User("异常处理！");
    }















    @PostConstruct
    private static void initFlowRules(){

        //流控规则
        List<FlowRule> list = new ArrayList<>();

        FlowRule rule = new FlowRule();

        //为哪个资源进行流控
        rule.setResource(RESOURCE_NAME);

        //设置流控规则QPS
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);

        //设置受保护资源的阈值
        //set limit QPS to 20
        rule.setCount(1);//每秒访问次数超过1就会被流控
        list.add(rule);

        FlowRule rule2 = new FlowRule();
        rule2.setResource(USER_RESOURCE_NAME);
        rule2.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule2.setCount(1);
        list.add(rule2);

        //加载配置好的规则
        FlowRuleManager.loadRules(list);

    }

}
