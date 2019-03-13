/*
package com.example.demo.controller;

import com.fs.vehicleservice.config.StaticResourceConfiguration;
import com.fs.vehicleservice.pojo.B2B_USER;
import com.fs.vehicleservice.utils.JsonResult;
import com.fs.vehicleservice.utils.ReusableCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/intelligence/vehicle")
public class LoginController {

    private static Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    @Qualifier("stringRedisTemplate")
    private StringRedisTemplate redis;

    @Autowired
    private StaticResourceConfiguration staticRes;

    @RequestMapping("login")
    public JsonResult login(HttpServletRequest request){
        String acc = request.getParameter("acc");
        String pwd = request.getParameter("pwd");
        if (null==acc||"".equals(acc)||null==pwd||"".equals(pwd)){
            return JsonResult.fail("账号和密码不能为空！", "");
        }
        if (staticRes.getSys_acc().equals(acc)&&staticRes.getSys_pwd().equals(pwd)){
            B2B_USER user = new B2B_USER();
            String sessionid = ReusableCodes.createSession(redis, user, Long.parseLong(staticRes.getSys_session_duration()));
            Map<String, Object> map = new HashMap<>();
            map.put("sessionid", sessionid);
            return JsonResult.success("", map);
        }else {
            return JsonResult.fail("用户名或密码错误！", "");
        }
    }

    @RequestMapping("logout")
    public JsonResult logout(HttpServletRequest request){
        String sessionid = request.getParameter("sessionid");
        boolean b = ReusableCodes.destroySession(redis, sessionid);
        if (b){
            return JsonResult.success("退出登录成功！", "");
        }else {
            return JsonResult.fail("销毁sessionid异常！", "");
        }
    }

}
*/
