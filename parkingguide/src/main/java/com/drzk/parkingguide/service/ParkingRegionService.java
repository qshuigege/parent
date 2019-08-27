package com.drzk.parkingguide.service;

import com.drzk.parkingguide.po.ParkingRegionPo;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingRegionVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface ParkingRegionService {

    JsonResponse queryParkingRegionPage(ParkingRegionVo vo);

    JsonResponse permissionAddParkingRegion(ParkingRegionPo po, HttpServletRequest request) throws Exception;

    JsonResponse permissionBatchInsert(List<ParkingRegionPo> list, HttpServletRequest request);

    JsonResponse permissionDeleteById(List<ParkingRegionPo> list, HttpServletRequest request);

    JsonResponse permissionUpdateById(ParkingRegionPo po, HttpServletRequest request) throws Exception;

    JsonResponse queryById(ParkingRegionPo po, HttpServletRequest request);

    JsonResponse queryRegionsByBelongToFloorId(ParkingRegionVo regionVo);

    JsonResponse permissionUploadRegionMap(MultipartFile file, HttpServletRequest request) throws IOException;

    void permissionDownloadRegionMap(String regionId, HttpServletRequest request, HttpServletResponse response) throws Exception;

    JsonResponse permissionUpdateRegionMapByRegionId(ParkingRegionVo regionVo, HttpServletRequest request) throws NoSuchFieldException, IllegalAccessException, Exception;
}
