package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.po.CommonQueryConditions;
import com.drzk.parkingguide.po.ParkingRegionPo;
import com.drzk.parkingguide.vo.ParkingRegionVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParkingRegionDao {
    int insert(ParkingRegionPo po);

    int addParkingRegion(ParkingRegionPo po);

    int batchInsert(List<ParkingRegionPo> list);

    List<ParkingRegionVo> queryParkingRegionPage(ParkingRegionVo vo);

    int deleteByIdBatch(List<ParkingRegionPo> list);

    int updateById(ParkingRegionPo po);

    ParkingRegionVo queryById(ParkingRegionPo po);

    List<ParkingRegionVo> queryByRegionName(ParkingRegionVo vo);

    int updateRemainPlaces(ParkingRegionVo regionVo);

    List<ParkingRegionVo> queryRegionsByBelongToFloorId(ParkingRegionVo regionVo);

    int updatePublishState(ParkingRegionVo regionVo);

    List<ParkingRegionVo> queryRegionsByPublishState(ParkingRegionVo vo);

    int updateRegionMapById(ParkingRegionVo regionVo);

    int validateAddRegionTotalPlaces(ParkingRegionPo po);

    ParkingRegionVo queryByIdAndOtherCols(ParkingRegionVo regionVo);

    int getCountByFloorId(ParkingRegionVo regionVo);
}
