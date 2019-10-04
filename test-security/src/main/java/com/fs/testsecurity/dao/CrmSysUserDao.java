package com.fs.testsecurity.dao;

import com.fs.testsecurity.po.CrmSysUserPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CrmSysUserDao {
    int deleteByPrimaryKey(Integer id);

    int insert(CrmSysUserPo record);

    CrmSysUserPo selectByPrimaryKey(Integer id);

    List<CrmSysUserPo> selectAll();

    int updateByPrimaryKey(CrmSysUserPo record);

    CrmSysUserPo selectByUsername(String username);
}
