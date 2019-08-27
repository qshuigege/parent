package com.drzk.parkingguide.service;

import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingPublishVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ParkingPublishService {
    List<ParkingPublishVo> getAllPublishedInfo(ParkingPublishVo publishVo);

    JsonResponse permissionAddParkingPublish(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception;

    JsonResponse permissionDoPublish(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception;

    JsonResponse permissionCorrectRemainPlaces(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception;

    JsonResponse permissionUpdateParkingPublish(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception;

    JsonResponse permissionDelParkingPublish(ParkingPublishVo publishVo, HttpServletRequest request) throws Exception;
}
