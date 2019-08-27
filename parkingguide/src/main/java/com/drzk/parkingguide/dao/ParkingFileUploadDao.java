package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.po.ParkingFileUploadPo;
import com.drzk.parkingguide.vo.ParkingFileUploadVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParkingFileUploadDao {
    int deleteByPrimaryKey(String id);

    int insert(ParkingFileUploadPo record);

    ParkingFileUploadPo selectByPrimaryKey(String id);

    List<ParkingFileUploadPo> selectAll();

    int updateByPrimaryKey(ParkingFileUploadPo record);

    ParkingFileUploadVo getUploadFileByRegionId(ParkingFileUploadVo vo);

    ParkingFileUploadVo queryById(ParkingFileUploadVo fileUploadVo);
}