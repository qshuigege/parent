package com.drzk.parkingguide.controller;

import com.drzk.parkingguide.po.ParkingLoginPo;
import com.drzk.parkingguide.service.ParkingLoginService;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingLoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ParkingLoginController {

    private Logger log = LoggerFactory.getLogger(ParkingLoginController.class);

    @Autowired
    private ParkingLoginService service;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public JsonResponse login(@RequestBody ParkingLoginPo parkingLogin, HttpServletRequest request){
        return service.login(parkingLogin, request);
    }

    @RequestMapping(value = "/addUserBatch", method = RequestMethod.POST)
    public JsonResponse addUserBatch(@RequestBody List<ParkingLoginPo> userList, HttpServletRequest request){
        return service.permissionAddUserBatch(userList, request);
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.POST)
    public JsonResponse addUser(@RequestBody ParkingLoginPo user, HttpServletRequest request) {
        try {
            return service.permissionAddUser(user, request);
        } catch (Exception e) {
            log.error("系统异常，添加失败！-->{}", e.getMessage());
            return JsonResponse.fail(JsonResponse.SYS_ERR, "系统异常，添加失败！-->"+e.getMessage());
        }
    }

    /*@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public JsonResponse deleteUser(@RequestBody ParkingLoginPo user, HttpServletRequest request){
        return service.permissionDeleteUser(user, request);
    }*/

    @RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
    public JsonResponse deleteUserBatch(@RequestBody List<ParkingLoginPo> userList, HttpServletRequest request){
        return service.permissionDeleteUserBatch(userList, request);
    }

    @RequestMapping(value = "/getAllUser", method = RequestMethod.POST)
    public JsonResponse getAllUser(@RequestBody ParkingLoginVo userVo, HttpServletRequest request){
        return service.permissionGetAllUser(userVo, request);
    }

    @RequestMapping(value = "/isLoginIdExist", method = RequestMethod.POST)
    public JsonResponse isLoginIdExist(@RequestBody ParkingLoginPo userPo, HttpServletRequest request){
        return service.permissionIsLoginIdExist(userPo, request);
    }

    @RequestMapping(value = "/resetPwd", method = RequestMethod.POST)
    public JsonResponse resetPwd(@RequestBody ParkingLoginPo userPo, HttpServletRequest request){
        return service.permissionResetPwd(userPo, request);
    }

    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public JsonResponse updatePwd(@RequestBody ParkingLoginVo userVo){
        return service.updatePwd(userVo);
    }


}
