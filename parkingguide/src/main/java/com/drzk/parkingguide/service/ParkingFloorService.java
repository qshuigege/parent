package com.drzk.parkingguide.service;

import com.drzk.parkingguide.po.ParkingFloorPo;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingFloorVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ParkingFloorService {

    JsonResponse permissionBatchInsert(List<ParkingFloorPo> list, HttpServletRequest request);

    JsonResponse queryParkingFloorPage(ParkingFloorVo vo);

    JsonResponse queryByParkName(ParkingFloorVo vo);

    JsonResponse queryByFloorName(ParkingFloorVo vo);

    JsonResponse permissionDeleteById(List<ParkingFloorPo> list, HttpServletRequest request);

    JsonResponse permissionInsert(ParkingFloorPo po, HttpServletRequest request) throws Exception;

    JsonResponse permissionUpdateById(ParkingFloorPo po, HttpServletRequest request) throws Exception;

    JsonResponse queryById(ParkingFloorVo vo);
}
