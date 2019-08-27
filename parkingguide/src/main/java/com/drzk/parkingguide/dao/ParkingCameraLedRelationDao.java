package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.po.ParkingCameraLedRelationPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParkingCameraLedRelationDao {
    int deleteByPrimaryKey(String id);

    int insert(ParkingCameraLedRelationPo record);

    ParkingCameraLedRelationPo selectByPrimaryKey(String id);

    List<ParkingCameraLedRelationPo> selectAll();

    int updateByPrimaryKey(ParkingCameraLedRelationPo record);

    List<ParkingCameraLedRelationPo> queryAll();

    List<ParkingCameraLedRelationPo> queryByCameraIds(List<ParkingCameraLedRelationPo> list);

    int deleteByCamIdAndLedId(ParkingCameraLedRelationPo po);

    int insertBatch(List<ParkingCameraLedRelationPo> simpleRelations);

    List<ParkingCameraLedRelationPo> queryAllPublishedDevices();
}