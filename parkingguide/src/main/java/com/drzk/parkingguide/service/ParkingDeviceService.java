package com.drzk.parkingguide.service;

import com.drzk.parkingguide.po.ParkingDevicePo;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingDeviceVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ParkingDeviceService {
    JsonResponse queryParkingDevicePage(ParkingDeviceVo vo);

    JsonResponse permissionAddParkingDevice(ParkingDeviceVo deviceVo, HttpServletRequest request) throws Exception;

    JsonResponse queryById(ParkingDevicePo po, HttpServletRequest request);

    JsonResponse permissionDeleteById(List<ParkingDevicePo> list, HttpServletRequest request);

    JsonResponse permissionUpdateById(ParkingDeviceVo deviceVo, HttpServletRequest request) throws Exception;

    List<ParkingDeviceVo> queryUsableCamerasOfChannel(ParkingDeviceVo deviceVo);

    JsonResponse changeCameraLoginState(ParkingDeviceVo deviceVo) throws Exception;
}
