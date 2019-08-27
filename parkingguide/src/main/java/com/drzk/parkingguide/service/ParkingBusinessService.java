package com.drzk.parkingguide.service;

import com.drzk.parkingguide.bo.RemainPlaces;
import com.drzk.parkingguide.camera.fAnalyzerDataCallBackImpl;
import com.drzk.parkingguide.po.*;
import com.drzk.parkingguide.vo.*;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ParkingBusinessService {

    List<ParkingCameraLedRelationPo> queryPublishedDevices();

    /*RemainPlaces updateRemainPlacesIncrease(fAnalyzerDataCallBackImpl.Camera camera);

    RemainPlaces updateRemainPlacesDecrease(fAnalyzerDataCallBackImpl.Camera camera) throws Exception;*/

    RemainPlaces updateRemainPlaces(fAnalyzerDataCallBackImpl.Camera camera, int plusOrSubtract) throws Exception;

    int saveVehicleSnapInfo(ParkingSnapInfoPo snapPo);

    //void permissionPublish(List<ParkingCameraLedRelationPo> list, HttpServletRequest request);

    @Transactional(rollbackFor = Exception.class)
    String publishRegionInternal(ParkingDeviceVo vo);

    @Transactional(rollbackFor = Exception.class)
    String permissionPublishRegion(ParkingDeviceVo vo, HttpServletRequest request);

    @Transactional(rollbackFor = Exception.class)
    List<String> permissionPublishFloor(ParkingRegionVo regionVo, HttpServletRequest request);

    void sendRemainPlacesToLED(Map<ParkingDeviceVo, List<ParkingDeviceVo>> detailRelations);

    Map<ParkingDeviceVo, List<ParkingDeviceVo>> injectDetailInfo(List<ParkingCameraLedRelationPo> simpleRelations);

    List<fAnalyzerDataCallBackImpl> buildCameraCallBackObjects(Map<ParkingDeviceVo, List<ParkingDeviceVo>> detailRelations);

    void permissionLogoutFromCamera(List<ParkingDeviceVo> camList, HttpServletRequest request);

    List<ParkingRegionVo> queryAllPublishedRegions();

    List<ParkingFloorVo> getFloors(ParkingFloorVo floorVo);

    List<ParkingRegionVo> getRegionsByBelongToFloorId(ParkingRegionVo regionVo);

    List<ParkingRegionVo> getRegions(ParkingRegionVo regionVo);

    String permissionStopPublishRegion(ParkingDeviceVo regionId, HttpServletRequest request);

    String permissionStopPublishFloor(ParkingRegionVo floorId, HttpServletRequest request);

    void correctRemainPlaces(ParkingDeviceVo businessVo);

    List<ParkingDeviceVo> queryAllDevices();

    List<ParkingPublishVo> getAllPublishInfo();
}
