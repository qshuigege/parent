package com.fs.everything.repository.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class MaintenanceDaoImpl {

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("defaultJdbcTemplate")
    private JdbcTemplate sqlserverJdbcTemplate;

    public int clearStepData(String docno, String step){
        String sql = "delete from shpc002pics where docno=?";
        return sqlserverJdbcTemplate.update(sql, new Object[]{docno});
    }

    public int test001(Map<String, Object> params){
        String sql = "select count(0) num from wxaccount where unionid=?";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, new Object[]{params.get("unionid")});
        return Integer.parseInt(map.get("num").toString());
    }

    public boolean binding(Map<String, Object> params) {
        String sql = "insert into wxacc_orguser_relation (uuid,unionid,orgid,createdate,openid,appid) values(UUID(),?,?,sysdate(),?,?)";
        int rows = jdbcTemplate.update(sql, new Object[]{params.get("unionid"), params.get("orgid"), params.get("openid"),params.get("originalid")});
        if (rows>0){
            return true;
        }else {
            return false;
        }
    }

    public boolean unbinding(Map<String, Object> params) {
        //String sql = "delete from wxacc_orguser_relation where unionid = ? and openid = ?";
        String sql = "delete from wxacc_orguser_relation where unionid = ?";
        int rows = jdbcTemplate.update(sql, new Object[]{params.get("unionid")});
        if (rows>0){
            return true;
        }else {
            return false;
        }
    }

}
