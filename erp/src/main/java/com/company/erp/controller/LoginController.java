package com.company.erp.controller;

import com.fs.diyutils.JsonResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public JsonResponse login(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if (!StringUtils.isEmpty(username)&&!StringUtils.isEmpty(password)){
            HttpSession session = request.getSession(true);
            session.setAttribute("aaa", "111");
            return JsonResponse.success(session.getId());
        }else {
            return JsonResponse.fail("用户名或密码错误！");
        }
    }

}
