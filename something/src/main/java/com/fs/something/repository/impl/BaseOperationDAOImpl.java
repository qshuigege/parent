package com.fs.something.repository.impl;


import com.fs.something.repository.BaseOperationDAO;
import com.fs.something.repository.pojo.BusinessType;
import com.fs.something.repository.pojo.SupplierInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * Created by FS on 2018/5/10.
 */
@Repository
public class BaseOperationDAOImpl implements BaseOperationDAO {
    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate jdbcTemplate;

    @Autowired
    @Qualifier("defaultJdbcTemplate")
    private JdbcTemplate jdbctemplate2;

    @Override
    public List<SupplierInfo> findCustomSuppliers(String customId, BusinessType businessType) {
        List<SupplierInfo> list = jdbcTemplate.query("SELECT B1.BASB001OID AS customId,O1.BusiType AS businessCode,D14.VendorName AS name,COUNT(O1.VendorName) AS counts FROM dbo.BASB001 AS B1\n" +
                "INNER JOIN dbo.BASB002D14 AS D14 ON B1.BASB001OID = D14.BASB001_FK\n" +
                "LEFT JOIN dbo.ORDA001 AS O1 ON B1.BASB001OID=O1.BASB001_FK AND D14.VendorName = O1.VendorName\n" +
                "WHERE D14.VStatus='4' AND O1.BusiType=? AND B1.BASB001OID=? \n" +
                "GROUP BY B1.BASB001OID,O1.BusiType,D14.VendorName order by COUNT(O1.VendorName) desc", new Object[]{businessType.getCode(), customId}, new BeanPropertyRowMapper(SupplierInfo.class));
        if (list != null && list.size() > 0) {
            return list;
        } else {
            return null;
        }
    }

    @Override
    public String execProcTest(String param) {
        String param2Value = (String) jdbctemplate2.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = "{call test_Proc(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        cs.setString(1, param);// 设置输入参数的值
                        cs.registerOutParameter(2, Types.VARCHAR);// 注册输出参数的类型
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        cs.execute();
                        return cs.getString(2);// 获取输出参数的值
                    }
                });
        return param2Value;
    }
}
