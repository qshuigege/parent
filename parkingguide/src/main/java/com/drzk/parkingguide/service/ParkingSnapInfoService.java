package com.drzk.parkingguide.service;

import com.drzk.parkingguide.po.ParkingSnapInfoPo;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingSnapInfoVo;
import com.drzk.parkingguide.vo.StatisticsVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ParkingSnapInfoService {
    JsonResponse queryParkingSnapInfoPage(ParkingSnapInfoVo vo);

    JsonResponse permissionAddParkingSnapInfo(ParkingSnapInfoPo po, HttpServletRequest request);

    JsonResponse queryById(ParkingSnapInfoPo po);

    JsonResponse permissionDeleteById(List<ParkingSnapInfoPo> list, HttpServletRequest request);

    JsonResponse permissionUpdateById(ParkingSnapInfoPo po, HttpServletRequest request);

    List<StatisticsVo> regionVehicleFlowStatistics(StatisticsVo regionId);
}
