package com.example.demo.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBResource {
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ResultSet resultSet;

	public DBResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
		this.connection = connection;
		this.preparedStatement = preparedStatement;
		this.resultSet = resultSet;
	}

	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public PreparedStatement getPreparedStatement() {
		return preparedStatement;
	}
	public void setPreparedStatement(PreparedStatement preparedStatement) {
		this.preparedStatement = preparedStatement;
	}
	public ResultSet getResultSet() {
		return resultSet;
	}
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
	
}
