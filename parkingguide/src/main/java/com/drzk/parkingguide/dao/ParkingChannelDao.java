package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.po.ParkingChannelPo;
import com.drzk.parkingguide.vo.ParkingChannelVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParkingChannelDao {
    int insert(ParkingChannelPo po);

    int addParkingChannel(ParkingChannelVo po);

    int batchInsert(List<ParkingChannelPo> list);

    List<ParkingChannelVo> queryParkingChannelPage(ParkingChannelVo vo);

    int deleteByIdBatch(List<ParkingChannelVo> list);

    int updateById(ParkingChannelVo vo);

    ParkingChannelVo queryById(ParkingChannelVo vo);

    List<ParkingChannelVo> queryByChannelName(ParkingChannelVo vo);

    int getCountByRegionId(ParkingChannelVo channelVo);

    int updateInRecogCamId(ParkingChannelVo channelVo);

    int updateOutRecogCamId(ParkingChannelVo channelVo);

}
