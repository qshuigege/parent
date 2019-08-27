package com.drzk.parkingguide.dao;

import com.drzk.parkingguide.po.ParkingLoginPo;
import com.drzk.parkingguide.vo.ParkingLoginVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface ParkingLoginDao {

    int deleteByPrimaryKey(String id);

    int insert(ParkingLoginPo record);

    ParkingLoginPo selectByPrimaryKey(String id);

    List<ParkingLoginPo> selectAll();

    int updateByPrimaryKey(ParkingLoginPo record);

    List<ParkingLoginPo> login(ParkingLoginPo parkingLogin);

    int addUser(ParkingLoginPo user);

    int addUserBatch(List<ParkingLoginPo> userList);

    List<ParkingLoginVo> getAllUser();

    int isLoginIdExist(ParkingLoginPo userPo);

    int deleteByIdBatch(List<ParkingLoginPo> userList);

    int resetPwd(ParkingLoginPo userPo);

    int updatePwd(ParkingLoginVo userVo);

    ParkingLoginPo queryById(ParkingLoginPo user);

    List<ParkingLoginPo> queryByIds(List<ParkingLoginPo> userList);

}