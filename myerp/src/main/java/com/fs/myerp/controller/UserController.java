package com.fs.myerp.controller;

import com.fs.diyutils.JsonResponse;
import com.fs.myerp.dao.SysuserDao;
import com.fs.myerp.po.SysuserPo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Resource
    private SysuserDao userDao;

    @Resource
    private PasswordEncoder pwdEnc;

    /*@RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse login(@RequestBody SysuserPo user, HttpServletRequest request) {
        try {
            user.setPassword(pwdEnc.encode(user.getPassword()));
            int i = userDao.insert(user);
            return JsonResponse.success(i);
        } catch (Exception e) {
            log.error("系统异常，添加失败！-->{}", e.getMessage());
            return JsonResponse.fail(99999, "系统异常，添加失败！-->"+e.getMessage());
        }
    }*/

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public JsonResponse addUser(@RequestBody SysuserPo user, HttpServletRequest request) {
        try {
            user.setPassword(pwdEnc.encode(user.getPassword()));
            int i = userDao.insert(user);
            return JsonResponse.success(i);
        } catch (Exception e) {
            log.error("系统异常，添加失败！-->{}", e.getMessage());
            return JsonResponse.fail(99999, "系统异常，添加失败！-->"+e.getMessage());
        }
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.POST)
    public JsonResponse getAllUser() {
        return JsonResponse.success(userDao.selectAll());
    }


}
