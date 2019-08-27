package com.drzk.parkingguide.service.impl;

import com.drzk.parkingguide.bo.UserContext;
import com.drzk.parkingguide.dao.ParkingLoginDao;
import com.drzk.parkingguide.po.ParkingLoginPo;
import com.drzk.parkingguide.service.ParkingLoginService;
import com.drzk.parkingguide.util.AESUtils;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.util.ParameterValidationUtils;
import com.drzk.parkingguide.vo.ParkingLoginVo;
import com.github.dozermapper.core.Mapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParkingLoginServiceImpl implements ParkingLoginService {

    private Logger log = LoggerFactory.getLogger(ParkingLoginServiceImpl.class);

    @Autowired
    private ParkingLoginDao parkingLoginDao;

    @Autowired
    private Mapper dozer;

    @Override
    public JsonResponse login(ParkingLoginPo parkingLogin, HttpServletRequest request) {
        try {
            parkingLogin.setLoginPwd(AESUtils.encrypt(parkingLogin.getLoginPwd()));
            List<ParkingLoginPo> loginList = parkingLoginDao.login(parkingLogin);
            if (loginList.size() > 0) {
                ParkingLoginPo login = loginList.get(0);
                UserContext uc = dozer.map(login, UserContext.class);
                HttpSession session = request.getSession(true);
                session.setAttribute("userContext", uc);
                ParkingLoginVo vo = dozer.map(login, ParkingLoginVo.class);
                vo.setSessionid(session.getId());
                log.debug("{}登录成功, sessionid-->{}", parkingLogin.getLoginId(), session.getId());
                return JsonResponse.success(vo);
            } else {
                return JsonResponse.fail("002", "登录失败！用户名或密码错误！");
            }
        }catch (Exception e){
            return JsonResponse.fail(JsonResponse.SYS_ERR, e.getMessage());
        }
    }

    @Override
    public JsonResponse permissionAddUser(ParkingLoginPo user, HttpServletRequest request) throws Exception{
        String[] validateFields = new String[]{"loginId", "loginPwd", "loginName", "ismanager"};
        ParameterValidationUtils.ValidationResult validate = ParameterValidationUtils.validate(validateFields, user);
        if (!validate.isSuccess()){
            return JsonResponse.fail("001", "参数中"+ParameterValidationUtils.concatFailureFields(validate)+"字段内容不能为空！");
        }
        user.setLoginPwd(AESUtils.encrypt(user.getLoginPwd()));
        int rows = parkingLoginDao.addUser(user);
        return JsonResponse.success(rows);
    }

    @Override
    public JsonResponse permissionAddUserBatch(List<ParkingLoginPo> userList, HttpServletRequest request) {
        try {
            /*for (int i = 0; i < userList.size(); i++) {
                ParkingLoginPo po = userList.get(i);
                po.setLoginPwd(AESUtils.encrypt(po.getLoginPwd()));
            }*/
            for (ParkingLoginPo po : userList) {
                po.setLoginPwd(AESUtils.encrypt(po.getLoginPwd()));
            }
            int rows = parkingLoginDao.addUserBatch(userList);
            return JsonResponse.success(rows);
        }catch (Exception e){
            return JsonResponse.fail(JsonResponse.SYS_ERR, e.getMessage());
        }
    }

    @Override
    public JsonResponse permissionDeleteUser(ParkingLoginPo user, HttpServletRequest request) {
        user = parkingLoginDao.queryById(user);
        if ("admin".equals(user.getLoginId())){
            return JsonResponse.fail("003", "不能删除管理员！");
        }
        List<ParkingLoginPo> userList = new ArrayList<>();
        userList.add(user);
        int rows = parkingLoginDao.deleteByIdBatch(userList);
        return JsonResponse.success(rows+"条数据被删除！");
    }

    @Override
    public JsonResponse permissionDeleteUserBatch(List<ParkingLoginPo> userList, HttpServletRequest request) {
        userList = parkingLoginDao.queryByIds(userList);
        for (ParkingLoginPo user : userList) {
            if ("admin".equals(user.getLoginId())){
                return JsonResponse.fail("003", "不能删除管理员！");
            }
        }
        int rows = parkingLoginDao.deleteByIdBatch(userList);
        return JsonResponse.success(rows+"条数据被删除！");
    }

    @Override
    public JsonResponse permissionGetAllUser(ParkingLoginVo userVo, HttpServletRequest request) {
        try {
            PageHelper.startPage(userVo.getPageNum(), userVo.getPageSize());
            List<ParkingLoginVo> list = parkingLoginDao.getAllUser();
            for (ParkingLoginVo vo : list) {
                vo.setLoginPwd(AESUtils.decrypt(vo.getLoginPwd()));
            }
            PageInfo<ParkingLoginVo> pageInfo = new PageInfo<>(list);
            return JsonResponse.success(pageInfo);
        }catch (Exception e){
            return JsonResponse.fail(JsonResponse.SYS_ERR, e.getMessage());
        }
    }

    @Override
    public JsonResponse permissionIsLoginIdExist(ParkingLoginPo userPo, HttpServletRequest request) {
        int count = parkingLoginDao.isLoginIdExist(userPo);
        Map<String, Object> resp = new HashMap<>();
        if (count > 0){
            resp.put("isExist", true);
            resp.put("msg", "用户名已存在！");
            return JsonResponse.success(resp);
        }else {
            resp.put("isExist", false);
            resp.put("msg", "用户名可以使用！");
            return JsonResponse.success(resp);
        }
    }

    @Override
    public JsonResponse permissionResetPwd(ParkingLoginPo userPo, HttpServletRequest request) {
        try {
            if ("admin".equals(userPo.getLoginId())) {
                return JsonResponse.fail("004", "管理员密码无法重置！");
            }
            userPo.setLoginPwd(AESUtils.encrypt("123456"));
            int rows = parkingLoginDao.resetPwd(userPo);
            return JsonResponse.success(rows);
        }catch (Exception e){
            log.error("", e);
            return JsonResponse.fail(JsonResponse.SYS_ERR, e.getMessage());
        }
    }

    @Override
    public JsonResponse updatePwd(ParkingLoginVo userVo) {
        try {
            ParkingLoginPo po = new ParkingLoginPo();
            po.setLoginId(userVo.getLoginId());
            po.setLoginPwd(AESUtils.encrypt(userVo.getLoginPwd()));
            //修改密码前先验证
            List<ParkingLoginPo> login = parkingLoginDao.login(po);
            if (login.size()>0){//验证通过
                userVo.setNewPwd(AESUtils.encrypt(userVo.getNewPwd()));
                int rows = parkingLoginDao.updatePwd(userVo);
                if (rows > 0) {
                    return JsonResponse.success("密码已更新！");
                }else {
                    return JsonResponse.fail(JsonResponse.SYS_ERR, "修改密码失败，原因未知。请稍后重试！");
                }
            }else {
                return JsonResponse.fail("005", "原密码错误，修改失败！");
            }
        }catch (Exception e){
            log.error("", e);
            return JsonResponse.fail(JsonResponse.SYS_ERR, e.getMessage());
        }
    }
}
