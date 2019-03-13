package com.fs.nothing.controller;

import com.fs.nothing.utils.*;
import com.fs.nothing.wx.*;
import com.fs.nothing.wx.testacc.WXTestAccHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Controller
@RequestMapping("/WX/callback/testacc")
public class WXTestAccController {

    public static Logger log = LoggerFactory.getLogger(WXTestAccController.class);

    /*@Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;*/

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redis;

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //@Autowired
    //private RedisOperator redis;

    //@Autowired
    //private userService userservice;


    @RequestMapping("wxEntrance")
    public void wxEntrance(HttpServletRequest request, HttpServletResponse response) throws Exception{

        if(request.getParameter("echostr")!=null){

            SignUtil.yanzheng(request, response);

        }else {
            // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
            // request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            // response.setHeader("Content-type", "text/html;charset=UTF-8");
            // xml请求解析
            Map<String, String> requestMap = MessageUtil.parseXml(request);
            Set<Map.Entry<String, String>> set = requestMap.entrySet();
            Iterator<Map.Entry<String, String>> it = set.iterator();
            while (it.hasNext()) {
                Map.Entry<String,String> en = it.next();
                log.info("{}-->{}",en.getKey(),en.getValue());
            }
            // 调用核心业务类接收消息、处理消息
            String respMessage = WXTestAccHandler.processRequest(requestMap);
            //log.info("respMessage-->"+respMessage);
            // 响应消息
            PrintWriter out = response.getWriter();
            out.print(respMessage);
            out.flush();
            out.close();

        }

    }
}
