package com.fs.myerp.controller;

import com.fs.diyutils.JsonResponse;
import com.fs.myerp.bo.UserContext;
import com.fs.myerp.po.LoginPo;
import com.fs.myerp.vo.LoginVo;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse login(HttpServletRequest request, @Validated @RequestBody LoginPo po){
        if (("admin".equals(po.getLoginId())&&"adminn".equals(po.getLoginPwd()))||("scott".equals(po.getLoginId())&&"tiger".equals(po.getLoginPwd()))){
            HttpSession session = request.getSession(true);
            UserContext userContext = new UserContext();
            userContext.setLoginId(po.getLoginId());
            userContext.setLoginName(po.getLoginName());
            LoginVo vo = new LoginVo();
            vo.setSessionid(session.getId());
            session.setAttribute("userContext", userContext);
            return JsonResponse.success(vo);
        }else {
            return JsonResponse.fail("登录失败！用户名或密码错误！");
        }
    }

}
