package com.fs.myerp.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMybatisDao {

    List<Object> getList(Map<String, String> params);

}
