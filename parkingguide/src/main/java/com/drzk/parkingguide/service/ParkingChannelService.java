package com.drzk.parkingguide.service;

import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingChannelVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ParkingChannelService {
    JsonResponse queryParkingChannelPage(ParkingChannelVo vo);

    JsonResponse permissionAddParkingChannel(ParkingChannelVo vo, HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException, Exception;

    JsonResponse queryById(ParkingChannelVo vo, HttpServletRequest request);

    JsonResponse permissionDeleteById(List<ParkingChannelVo> list, HttpServletRequest request);

    JsonResponse permissionUpdateById(ParkingChannelVo vo, HttpServletRequest request) throws Exception;
}
