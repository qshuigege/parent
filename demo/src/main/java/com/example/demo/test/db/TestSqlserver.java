package com.example.demo.test.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestSqlserver {
    public static void main(String[] args) throws Exception{
        //String driverName = "com.mysql.jdbc.Driver";
        //String driverNameOracle = "oracle.jdbc.driver.OracleDriver";
        String driverNameSqlserver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        //String url = "jdbc:mysql://localhost:3306/myerp?useSSL=true";//
        //String urlOracle = "jdbc:oracle:thin:@localhost:1521:orcl";
        String urlSqlServer = "jdbc:sqlserver://192.168.128.129:1433;DatabaseName=Test";
        String user = "sa";
        String password = "admin";
        Class.forName(driverNameSqlserver);
        Connection conn = DriverManager.getConnection(urlSqlServer, user, password);
        String sql = "select * from studentscore";
        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            System.out.println(resultSet.getObject(1)+", "+resultSet.getObject(2)+", "+resultSet.getObject(3)+", "+resultSet.getObject(4));
        }
        resultSet.close();
        preparedStatement.close();
        conn.close();

    }
}
