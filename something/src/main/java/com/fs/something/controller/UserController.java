package com.fs.something.controller;

import com.fs.something.repository.UserDao;
import com.fs.something.utils.FusenJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/entwechat")
public class UserController {

    @Autowired
    private UserDao dao;

    @RequestMapping("callback/getOrguserByUserid")
    public FusenJSONResult getOrguserByUserid(HttpServletRequest request){
        String userid = request.getParameter("userid");
        if (null==userid||"".equals(userid)){
            return FusenJSONResult.failMsg("参数userid不能为空！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("userid", userid);
        try {
            //List<B2B_USER> lst = dao.getOrguserByUserid(params);
            //B2B_USER user = lst.get(0);
            return FusenJSONResult.buildSuccess(dao.getOrguserByUserid(params).get(0));
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("callback/getOrguserByQywxuserid")
    public FusenJSONResult getOrguserByQywxuserid(HttpServletRequest request){
        String qywxuserid = request.getParameter("qywxuserid");
        if (null==qywxuserid||"".equals(qywxuserid)){
            return FusenJSONResult.failMsg("参数qywxuserid不能为空！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("UserId", qywxuserid);
        try {
            //List<B2B_USER> lst = dao.getOrguserByUserid(params);
            //B2B_USER user = lst.get(0);
            return FusenJSONResult.buildSuccess(dao.getOrguserByQywxuserid(params).get(0));
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("callback/getOrguserByUcmluseroid")
    public FusenJSONResult getOrguserByUcmluseroid(HttpServletRequest request){
        String ucml_useroid = request.getParameter("ucml_useroid");
        if (null==ucml_useroid||"".equals(ucml_useroid)){
            return FusenJSONResult.failMsg("参数ucml_useroid不能为空！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ucml_useroid", ucml_useroid);
        try {
            //List<B2B_USER> lst = dao.getOrguserByUserid(params);
            //B2B_USER user = lst.get(0);
            return FusenJSONResult.buildSuccess(dao.getOrguserByUcmluseroid(params).get(0));
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("callback/getOrguserByFsid")
    public FusenJSONResult getOrguserByFsid(HttpServletRequest request){
        String ucml_useroid = request.getParameter("empnum");
        if (null==ucml_useroid||"".equals(ucml_useroid)){
            return FusenJSONResult.failMsg("参数empnum不能为空！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("empnum", ucml_useroid);
        try {
            //List<B2B_USER> lst = dao.getOrguserByUserid(params);
            //B2B_USER user = lst.get(0);
            return FusenJSONResult.buildSuccess(dao.getOrguserByFsid(params).get(0));
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

    @RequestMapping("callback/getUcmluserinfoByUcmluseroid")
    public FusenJSONResult getUcmluserinfoByUcmluseroid(HttpServletRequest request){
        String ucml_useroid = request.getParameter("ucml_useroid");
        if (null==ucml_useroid||"".equals(ucml_useroid)){
            return FusenJSONResult.failMsg("参数ucml_useroid不能为空！");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("ucml_useroid", ucml_useroid);
        try {
            //List<B2B_USER> lst = dao.getOrguserByUserid(params);
            //B2B_USER user = lst.get(0);
            return FusenJSONResult.buildSuccess(dao.getUcmluserinfoByUcmluseroid(params));
        }catch (Exception e){
            return FusenJSONResult.failMsg(e.getMessage());
        }
    }

}
