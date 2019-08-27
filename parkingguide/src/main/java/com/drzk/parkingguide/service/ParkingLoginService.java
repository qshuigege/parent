package com.drzk.parkingguide.service;

import com.drzk.parkingguide.po.ParkingLoginPo;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingLoginVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ParkingLoginService {

    JsonResponse login(ParkingLoginPo parkingLogin, HttpServletRequest request);

    JsonResponse permissionAddUser(ParkingLoginPo user, HttpServletRequest request) throws Exception;

    JsonResponse permissionAddUserBatch(List<ParkingLoginPo> userList, HttpServletRequest request);

    JsonResponse permissionDeleteUser(ParkingLoginPo user, HttpServletRequest request);

    JsonResponse permissionGetAllUser(ParkingLoginVo userVo, HttpServletRequest request);

    JsonResponse permissionIsLoginIdExist(ParkingLoginPo userPo, HttpServletRequest request);

    JsonResponse permissionDeleteUserBatch(List<ParkingLoginPo> userList, HttpServletRequest request);

    JsonResponse permissionResetPwd(ParkingLoginPo userPo, HttpServletRequest request);

    JsonResponse updatePwd(ParkingLoginVo userVo);
}
