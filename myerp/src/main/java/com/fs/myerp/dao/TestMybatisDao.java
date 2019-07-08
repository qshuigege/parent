package com.fs.myerp.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface TestMybatisDao {

    List<Map<String, Object>> getList(Map<String, Object> params);

    int getCount(Map<String, Object> params);

    List<Map<String, Object>> testIf(Map<String, Object> params);

}
