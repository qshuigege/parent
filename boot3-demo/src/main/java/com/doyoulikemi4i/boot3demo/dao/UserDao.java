package com.doyoulikemi4i.boot3demo.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.doyoulikemi4i.boot3demo.domain.Sys_user;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<Sys_user> {
}
