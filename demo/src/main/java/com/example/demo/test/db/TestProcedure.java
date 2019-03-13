package com.example.demo.test.db;

import com.example.demo.utils.FusenJSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.sql.*;

@RestController
public class TestProcedure {

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate mysqlJdbcTemplate;

    @Autowired
    @Qualifier("defaultJdbcTemplate")
    private JdbcTemplate sqlserverJdbcTemplate;

    /**
     * 调用有返回值的存储过程(非结果集)
     * @param request
     * @return
     */
    @RequestMapping("/test/callback/testProcedure")
    public FusenJSONResult testProcedure(HttpServletRequest request){
        String fyfk = sqlserverJdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                CallableStatement cstmt = conn.prepareCall("{call PR_GetDocno(?,?)}");
                cstmt.setString(1, "FYFK");
                cstmt.registerOutParameter(2, Types.VARCHAR);
                return cstmt;
            }
        }, new CallableStatementCallback<String>() {
            @Override
            public String doInCallableStatement(CallableStatement cstmt) throws SQLException, DataAccessException {
                cstmt.execute();
                return cstmt.getString(2);
            }
        });
        return FusenJSONResult.buildSuccess(fyfk);
    }

    /**
     * 调用存储过程，返回结果集
     * @param request
     * @return
     */
    @RequestMapping("/test/callback/testProcedureWithResultSet")
    public FusenJSONResult testProcedureWithResultSet(HttpServletRequest request){
        String fyfk = sqlserverJdbcTemplate.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection conn) throws SQLException {
                CallableStatement cstmt = conn.prepareCall("{call prtest_getUserInfo(?)}");
                cstmt.setString(1, "FS563");
                return cstmt;
            }
        }, new CallableStatementCallback<String>() {
            @Override
            public String doInCallableStatement(CallableStatement cstmt) throws SQLException, DataAccessException {
                ResultSet rs = cstmt.executeQuery();
                while (rs.next()){
                    System.out.println(rs.getString("CON_EMP_NUM"));
                    System.out.println(rs.getString("personname"));
                }
                return "ayok";
            }
        });
        return FusenJSONResult.buildSuccess(fyfk);
    }
    
}
