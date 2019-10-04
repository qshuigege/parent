package com.fs.testsecurity.mybatis.dao;

import com.fs.testsecurity.mybatis.model.CrmSysUser;
import java.util.List;

public interface CrmSysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CrmSysUser record);

    CrmSysUser selectByPrimaryKey(Integer id);

    List<CrmSysUser> selectAll();

    int updateByPrimaryKey(CrmSysUser record);
}