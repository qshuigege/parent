package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.po.ParkingSettingsPo;
import com.drzk.parkingguide.vo.ParkingSettingsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParkingSettingsDao {
    int deleteByPrimaryKey(String id);

    int insert(ParkingSettingsPo record);

    ParkingSettingsPo selectByPrimaryKey(String id);

    List<ParkingSettingsPo> selectAll();

    int updateByPrimaryKey(ParkingSettingsPo record);

    List<ParkingSettingsVo> queryAllWithPage(ParkingSettingsVo vo);

    int deleteByIdBatch(List<ParkingSettingsPo> list);

    int updateById(ParkingSettingsPo po);

    Object queryById(ParkingSettingsPo po);

    int updateByOptionKey(ParkingSettingsVo vo);
}