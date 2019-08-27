package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.po.ParkingSnapInfoPo;
import com.drzk.parkingguide.vo.ParkingSnapInfoVo;
import com.drzk.parkingguide.vo.StatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParkingSnapInfoDao {

    int insert(ParkingSnapInfoPo record);

    ParkingSnapInfoPo queryById(ParkingSnapInfoPo po);

    List<ParkingSnapInfoVo> queryAllWithPage(ParkingSnapInfoVo vo);

    int updateById(ParkingSnapInfoPo po);

    int deleteByIdBatch(List<ParkingSnapInfoPo> list);

    List<StatisticsVo> regionVehicleFlowStatistics(StatisticsVo regionId);
}