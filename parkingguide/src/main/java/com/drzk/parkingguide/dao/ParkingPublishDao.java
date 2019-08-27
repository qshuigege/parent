package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.vo.ParkingPublishVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Mapper
public interface ParkingPublishDao {

    List<ParkingPublishVo> queryAllWithPage(ParkingPublishVo publishVo);

    int insert(ParkingPublishVo publishVo);

    int isRegionIdExist(ParkingPublishVo publishVo);

    int isFloorIdExist(ParkingPublishVo publishVo);

    int updatePublishStatus(ParkingPublishVo publishVo);

    int updateRegionPublishInfo(ParkingPublishVo publishVo);

    int updateFloorPublishInfo(ParkingPublishVo publishVo);

    int deleteByIdBatch(ArrayList<ParkingPublishVo> list);

    //ParkingPublishVo queryByFloorId(ParkingPublishVo publishVo);

    ParkingPublishVo queryByFloorIdAndPublishType(ParkingPublishVo publishVo);

    List<ParkingPublishVo> queryByLedPartId(ParkingPublishVo publishVo);

    ParkingPublishVo queryByRegionId(ParkingPublishVo publishVo);

    ParkingPublishVo queryByRegionIdAndPublishType(ParkingPublishVo regionPublishVo);
}