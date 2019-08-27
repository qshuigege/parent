package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.po.ParkingFloorPo;
import com.drzk.parkingguide.util.JsonResponse;
import com.drzk.parkingguide.vo.ParkingFloorVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParkingFloorDao {

    int deleteByPrimaryKey(String id);

    int insert(ParkingFloorPo record);

    int batchInsert(List<ParkingFloorPo> list);

    ParkingFloorPo selectByPrimaryKey(String id);

    List<ParkingFloorPo> selectAll();

    int updateByPrimaryKey(ParkingFloorPo record);

    List<ParkingFloorVo> queryParkingFloorPage(ParkingFloorVo vo);

    int deleteByIdBatch(List<ParkingFloorPo> list);

    List<ParkingFloorVo> queryByParkName(ParkingFloorVo vo);

    List<ParkingFloorVo> queryByFloorName(ParkingFloorVo vo);

    int updateById(ParkingFloorPo po);

    ParkingFloorVo queryById(ParkingFloorVo vo);

    int updateRemainPlaces(ParkingFloorVo floorVo);

    int updatePublishState(ParkingFloorVo floorVo);

}
