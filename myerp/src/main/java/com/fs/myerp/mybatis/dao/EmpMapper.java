package com.fs.myerp.mybatis.dao;

import com.fs.myerp.mybatis.model.Emp;
import java.util.List;

public interface EmpMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table emp
     *
     * @mbg.generated Mon Jul 08 19:57:49 CST 2019
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table emp
     *
     * @mbg.generated Mon Jul 08 19:57:49 CST 2019
     */
    int insert(Emp record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table emp
     *
     * @mbg.generated Mon Jul 08 19:57:49 CST 2019
     */
    Emp selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table emp
     *
     * @mbg.generated Mon Jul 08 19:57:49 CST 2019
     */
    List<Emp> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table emp
     *
     * @mbg.generated Mon Jul 08 19:57:49 CST 2019
     */
    int updateByPrimaryKey(Emp record);
}