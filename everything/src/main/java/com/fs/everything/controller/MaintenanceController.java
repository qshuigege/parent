package com.fs.everything.controller;

import com.fs.everything.pojo.UserContext;
import com.fs.everything.repository.impl.MaintenanceDaoImpl;
import com.fs.everything.service.WXBusinessService;
import com.fs.everything.utils.FusenJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Autowired
    private WXBusinessService service;

    @Autowired
    private MaintenanceDaoImpl dao;

    @RequestMapping("/changeMiniprogflag")
    public FusenJSONResult changeMiniprogflag(HttpServletRequest request){
        String miniprogflag = request.getParameter("miniprogflag");
        UserContext uc = (UserContext) request.getAttribute("userContext");
        try {
            service.changeMiniprogflag(miniprogflag, uc.getOrgid());
            return FusenJSONResult.success();
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("/clearStepData")
    public FusenJSONResult clearStepData(HttpServletRequest request){
        String docno = request.getParameter("docno");
        if (null==docno||"".equals(docno)){
            return FusenJSONResult.failMsg("docno不能为空！");
        }
        try {
            dao.clearStepData(docno,null);
            return FusenJSONResult.success();
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("/test001")
    public FusenJSONResult test001(HttpServletRequest request){
        String unionid = request.getParameter("unionid");
        Map<String, Object> params = new HashMap<>();
        params.put("unionid", unionid);
        try {
            int rows = dao.test001(params);
            return FusenJSONResult.successMsg(rows+"");
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("/bind")
    public FusenJSONResult bind(HttpServletRequest request){
        String unionid = request.getParameter("unionid");
        String orgid = request.getParameter("orgid");
        String openid = request.getParameter("openid");
        String originalid = request.getParameter("originalid");
        Map<String, Object> params = new HashMap<>();
        params.put("unionid", unionid);
        params.put("orgid", orgid);
        params.put("openid", openid);
        params.put("originalid", originalid);
        boolean b = dao.binding(params);
        if (b){
            return FusenJSONResult.success();
        }else {
            return FusenJSONResult.fail();
        }
    }

    @RequestMapping("/unbind")
    public FusenJSONResult unbind(HttpServletRequest request){
        String unionid = request.getParameter("unionid");
        String orgid = request.getParameter("orgid");
        String openid = request.getParameter("openid");
        String originalid = request.getParameter("originalid");
        Map<String, Object> params = new HashMap<>();
        params.put("unionid", unionid);
        params.put("orgid", orgid);
        params.put("openid", openid);
        params.put("originalid", originalid);
        boolean b = dao.unbinding(params);
        if (b){
            return FusenJSONResult.success();
        }else {
            return FusenJSONResult.fail();
        }
    }




}
