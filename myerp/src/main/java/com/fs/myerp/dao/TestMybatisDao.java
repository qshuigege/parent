package com.fs.myerp.dao;

import com.fs.myerp.model.Emp;
import com.fs.myerp.model.Test;
import com.fs.myerp.vo.TestVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface TestMybatisDao {

    List<Map<String, Object>> getList(Map<String, Object> params);

    int getCount(Map<String, Object> params);

    List<Map<String, Object>> testIf(Map<String, Object> params);

    List<Map<String, Object>> testPagehelper(Map<String, Object> params);

    List<Test> testMybatisDateParam(TestVo testVo);

    List<Emp> testConcat(List<Emp> list);

}
