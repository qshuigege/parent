package com.fs.myerp.dao;

import com.fs.myerp.po.SysuserPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysuserDao {
    int deleteByPrimaryKey(String id);

    int insert(SysuserPo record);

    SysuserPo selectByPrimaryKey(String id);

    List<SysuserPo> selectAll();

    int updateByPrimaryKey(SysuserPo record);

    SysuserPo selectByUsername(String username);
}