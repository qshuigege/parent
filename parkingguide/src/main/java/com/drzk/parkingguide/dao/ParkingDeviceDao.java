package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.po.ParkingDevicePo;
import com.drzk.parkingguide.vo.ParkingChannelVo;
import com.drzk.parkingguide.vo.ParkingDeviceVo;
import com.drzk.parkingguide.vo.ParkingPublishVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParkingDeviceDao {
    int addParkingDevice(ParkingDeviceVo deviceVo);

    List<ParkingDeviceVo> queryParkingDevicePage(ParkingDeviceVo vo);

    int deleteByIdBatch(List<ParkingDevicePo> list);

    int updateById(ParkingDeviceVo deviceVo);

    ParkingDeviceVo queryById(ParkingDevicePo po);

    List<ParkingDeviceVo> queryByIds(List<ParkingDevicePo> poList);

    List<ParkingDeviceVo> queryDevicesByBelongToRegionId(ParkingDeviceVo regionId);

    List<ParkingDeviceVo> queryDevicesByBelongToFloorId(ParkingDeviceVo floorId);

    int updateCameraStatusByCameraIdBatch(List<ParkingDevicePo> cameraIdList);

    int updateCameraLocationInfo(ParkingChannelVo vo);

    int updateFloorIdAndLedRownumByLedPartId(ParkingPublishVo publishVo);

    ParkingDeviceVo queryByIdAndOtherCols(ParkingDeviceVo deviceVo);

    int queryByDeviceIp(ParkingDeviceVo deviceVo);

    int updateBelongToIdsAndCameraTypeToNullById(ParkingDeviceVo deviceVo);

    List<ParkingDeviceVo> queryUsableCamerasOfChannel(ParkingDeviceVo deviceVo);

    int updateCameraConnStatusByIp(ParkingDeviceVo deviceVo);

    int updateCameraConnStatusById(ParkingDeviceVo deviceVo);

    int updateCameraById(ParkingDeviceVo deviceVo);

    int updateLedById(ParkingDeviceVo deviceVo);

    int queryByCameraTypeAndId(ParkingDeviceVo deviceVo);
}
