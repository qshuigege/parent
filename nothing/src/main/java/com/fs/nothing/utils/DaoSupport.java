package com.fs.nothing.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import java.util.Date;

public abstract class DaoSupport<T> {
	@SuppressWarnings("unchecked")
	protected Class<T> clazz = GenericsUtils.getSuperClassGenricType(this.getClass());
//	protected Gson gson = new Gson();
//	protected BO bo = new BO();
//	protected BOAPI boapi = SDK.getBOAPI();

	public static Connection getConn(String url, String driverClassName, String user, String password){
		Connection conn = null;
			try {
				Class.forName(driverClassName);
				conn = DriverManager.getConnection(url, user, password);
			} catch (SQLException e) {
			} catch (ClassNotFoundException e) {
			}
		
		
		return conn;
	}

	public static Connection getConn(){
		Connection conn = null;
			try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection("jdbc:oracle:thin:@10.1.1.42:1521:fusen42", "aws6", "aws6");
			} catch (SQLException e) {
			} catch (ClassNotFoundException e) {
			}
		
		
		return conn;
	}
	
	/**
	 * 查询数据库记录并组装成实体对象
	 * @param sql sql语句
	 * @param params sql语句中占位符('?')对应的参数
	 * @return 返回bean集合
	 */
	public List<T> getRecords(String sql, Object[] params) {
		return getRecords(clazz, sql, params);
	}

	/**
	 * 查询数据库记录并组装成实体对象(支持只查询部分字段)
	 * @param clazz
	 * @param sql
	 * @param params sql语句中的占位符相对应的参数,如果sql语句中没有点位符,该参数可以传null
	 * @return
	 */
	public static <E> List<E> getRecords(Class<E> clazz, String sql, Object[] params){

		List<E> list = new ArrayList<E>();
		Connection conn = null;
		PreparedStatement pt = null;
		ResultSet rs = null;
		try {
			conn = getConn();
			pt = conn.prepareStatement(sql);
			
			if (params!=null) {
				for (int i = 0; i < params.length; i++) {
					pt.setObject(i+1, params[i]);
				}
			}
			rs = pt.executeQuery();
			
			Field[] fields = clazz.getDeclaredFields();//通过反射取得实体所有字段
			Map<String, Field> fieldMap = new HashMap<String, Field>();//保存所有field
			Map<String, String> fieldNameMap = new HashMap<String, String>();//保存实体的所有字段名
			
			String fieldName = null;
			int fieldsCount = fields.length;//实体类字段个数
			for (int i = 0; i < fieldsCount; i++) {
				fieldName = fields[i].getName();
				fieldMap.put(fieldName, fields[i]);
				fieldNameMap.put(fieldName.toUpperCase(), fieldName);
			}
			
			
			ResultSetMetaData metaData = rs.getMetaData();
			int rsColumnCount = metaData.getColumnCount();//查询结果的列数
			
			// 组装Bean
			E t = null;
			Object value = null;
			String fieldClassName = null;
			while (rs.next()) {
				t = clazz.newInstance();
				for (int i = 0; i < rsColumnCount; i++) {
					fieldName = fieldNameMap.get((metaData.getColumnName(i+1)).toUpperCase());
//					System.out.println("fieldName-->"+ fieldName);
					fieldClassName = fieldMap.get(fieldName).getType().getName();
//					System.out.println("fieldClassName-->"+fieldClassName);
					try{
						value = rs.getObject(fieldName);
						if (value instanceof BigDecimal&&"java.lang.Integer".equals(fieldClassName)) {
							value = ((BigDecimal)(value)).intValue();
						}
					}catch(Exception e){//如果数据库中的数据有异常(比如数据库中整形字段的值为null,日期类型的字段为null等等)
						if ("java.util.Date".equals(fieldClassName)) {//如果字段是日期类型
							value = new Date(0);
						}else if ("java.lang.Integer".equals(fieldClassName)) {//如果字段是整型
							value = new Integer(-1);
						}else if ("java.math.BigDecimal".equals(fieldClassName)) {//如果字段是BigDecimal类型
							value = new BigDecimal(-1);
						}
						System.out.println(e.toString());
					}
					
					fieldMap.get(fieldName).setAccessible(true);
					fieldMap.get(fieldName).set(t, value);

				}
				
				
				list.add(t);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.toString());
		} finally{
			closeDBRes(conn, pt, rs);//释放数据库资源
		}
		
		
		return list;
	}
	
	/**
	 * 查询总记录数
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public static int getTotalRecords(String sql, Object[] params){
		int totalRecords = 0;//总记录数
		Connection conn = getConn();
		PreparedStatement pt = null;
		ResultSet rs = null;
		try {
			pt = conn.prepareStatement(sql);

			if (params!=null) {
				for (int i = 0; i < params.length; i++) {
					pt.setObject(i+1, params[i]);
				}
			}
			rs = pt.executeQuery();
			rs.next();
			totalRecords = Integer.parseInt(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}finally{
			closeDBRes(conn, pt, rs);
		}
		return totalRecords;
	}
	
	/**
	 * 分页功能，得到总页数
	 * @param totalRecords
	 * @param pageSize
	 * @return
	 */
	public static int getTotalPages(int totalRecords, int pageSize){
//		int totalRecords = getTotalRecords(sql, params);// 总记录数
		int totalPages;//总页数
		if (totalRecords % pageSize == 0) {
			totalPages = totalRecords / pageSize;
		} else {
			totalPages = (totalRecords / pageSize) + 1;
		}
		return totalPages;
	}
	/**
	 * 往数据库插入一条记录(通过JDBC)
	 * 
	 * @param sql 
	 * @param params
	 */
	public static boolean save(String sql, Object[] params){
		Connection conn = getConn();
		PreparedStatement pt = null;
		boolean flag = false;
		try {
			pt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				if (params[i] instanceof Date) {
					Timestamp t = new Timestamp(((Date)(params[i])).getTime());
					params[i] = t;
				}
				pt.setObject(i+1, params[i]);
			}
			
			flag = pt.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}finally{
			closeDBRes(conn, pt, null);
		}
		
		return flag;
	}

	/**
	 * 更新一条记录(通过JDBC)
	 * @param sql
	 * @param params
	 * @return
	 */
	/*public static int update(String sql, Object[] params){
		Connection conn = getConn();
		PreparedStatement pt = null;
		int rows = -1;//更新的行数，如果出现异常则返回-1
		try {
			pt = conn.prepareStatement(sql);
			for (int i = 0; i < params.length; i++) {
				pt.setObject(i+1, params[i]);
			}
			
			rows = pt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}finally{
			closeDBRes(conn, pt, null);
		}
		
		return rows;
	}*/
	
	/**
	 * 从b2b_interface、b2b_interface_note表中查出ERP表字段与B2B表字段的对应信息
	 * @param tableName
	 * @return
	 */
	public static QueryResult getTableFieldInfo(String tableName){
		
		String sql = "select t2.inter_b2bfield, t2.inter_erpfield, t2.inter_fieldtype from b2b_interface t1, b2b_interface_note t2 where t1.inter_b2b_tname = '"+tableName+"' and t1.inter_id = t2.inter_id";
				
		Connection conn = getConn();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		//erp表字段与b2b表字段对应信息。key为b2b表字段名，value为erp表字段名
		Map<String, String> tableColumnNameMapping = new HashMap<String, String>();
		Map<String, String> tableFieldType = new HashMap<String, String>();
		
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tableColumnNameMapping.put(rs.getString("inter_b2bfield").toUpperCase(), rs.getString("inter_erpfield").toUpperCase());
				tableFieldType.put(rs.getString("inter_b2bfield").toUpperCase(), rs.getString("inter_fieldtype"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("DaoSupport-getTableFieldInfo()"+e);
		}finally{
			closeDBRes(conn, pstmt, rs);
		}
		return new QueryResult(tableColumnNameMapping, tableFieldType);
	}

	
	/**
	 * 插入、删除操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static boolean executeInsertOrDelete(String sql, Object[] params) {
		boolean flag = false;
		Connection conn = null;
		PreparedStatement pt = null;
		ResultSet rs = null;
		conn = getConn();
		try {
			pt = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					if (params[i] instanceof Date) {
						Timestamp t = new Timestamp(((Date)(params[i])).getTime());
						params[i] = t;
					}
					pt.setObject(i + 1, params[i]);
				}
			}
			flag = pt.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeDBRes(conn, pt, rs);
		}

		return flag;
	}

	
	/**
	 * 插入、删除操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static boolean executeProcedure(String sql, Object[] params) {
		boolean flag = false;
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		conn = getConn();
		try {
			cs = conn.prepareCall(sql);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					if (params[i] instanceof Date) {
						Timestamp t = new Timestamp(((Date)(params[i])).getTime());
						params[i] = t;
					}
					cs.setObject(i + 1, params[i]);
				}
			}
			flag = cs.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			closeDBRes(conn, cs, rs);
		}

		return flag;
	}

	
	/**
	 * 插入、删除操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static DBResource executeProcedureWithOutParam(String sql, Object[] params, int[] outParams) {
		Connection conn = null;
		CallableStatement cs = null;
		ResultSet rs = null;
		conn = getConn();
		try {
			cs = conn.prepareCall(sql);
			int paramIndex = 0;
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					if (params[i] instanceof Date) {
						Timestamp t = new Timestamp(((Date)(params[i])).getTime());
						params[i] = t;
					}
					cs.setObject(i + 1, params[i]);
					paramIndex = i;
				}
			}
			if(outParams!=null){
				for (int i = 0; i < outParams.length; i++) {
					cs.registerOutParameter(paramIndex+2+i, outParams[i]);
				}
			}
			cs.execute();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return new DBResource(conn, cs, rs);
	}

	/**
	 * 更新操作
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public static int executeUpdate(String sql, Object[] params) {
		int rows = -1;
		Connection conn = null;
		PreparedStatement pt = null;
		ResultSet rs = null;
		conn = getConn();
		try {
			pt = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					if (params[i] instanceof Date) {
						Timestamp t = new Timestamp(((Date)(params[i])).getTime());
						params[i] = t;
					}
					pt.setObject(i + 1, params[i]);
				}
			}
			rows = pt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		} finally {
			closeDBRes(conn, pt, rs);
		}

		return rows;
	}

	/**
	 * 查询操作
	 * 
	 * @param sql
	 * @param params
	 * @return  
	 */
	public static DBResource executeQuery(String sql, Object[] params) {
		Connection conn = null;
		PreparedStatement pt = null;
		ResultSet rs = null;
		conn = getConn();
		try {
			pt = conn.prepareStatement(sql);
			if(params!=null){
				for (int i = 0; i < params.length; i++) {
					if (params[i] instanceof Date) {
						Timestamp t = new Timestamp(((Date)(params[i])).getTime());
						params[i] = t;
					}
					pt.setObject(i + 1, params[i]);
				}
			}
			rs = pt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
//		Map<String, Object> map = new HashMap<String, Object>();
//		map.put(R.ERP_CONN, conn);
//		map.put(R.ERP_PT, pt);
//		map.put(R.ERP_RS, rs);
		DBResource DBRes = new DBResource(conn, pt, rs);
		return DBRes;
	}

	/**
	 * 查询指定的一个或多个字段
	 * @param tableName 表名
	 * @param whereStatement sql语句的where部分(不带"where"关键词)
	 * @param fieldNames 需要查询的字段
	 * @return 返回一个Map集合，集合中的每个map代表一条记录，map的key为字段名的大写，value为字段值
	 * @throws SQLException 
	 */
	public static List<Map<String, Object>> executeQuerySpecifiedFields(String tableName, String whereStatement, String...fieldNames){
		Connection conn = null;
		PreparedStatement pt = null;
		ResultSet rs = null;
		StringBuffer sql = new StringBuffer("select ");
		for (int i = 0; i < fieldNames.length; i++) {
			sql.append(fieldNames[i]).append(",");
		}
		sql.setLength(sql.length()-1);
		sql.append(" from ").append(tableName);
		if(whereStatement!=null&&!"".equals(whereStatement)){
			sql.append(" where ").append(whereStatement);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try{
			conn = getConn();
			pt = conn.prepareStatement(sql.toString());
			System.out.println(sql.toString());
			rs = pt.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<String, Object>();
				for (int i = 0; i < fieldNames.length; i++) {
					map.put(fieldNames[i].toUpperCase(), rs.getObject(fieldNames[i]));
				}
				list.add(map);
			}
		}catch(SQLException e){
			System.out.println("DaoSupport.executeQuerySpecifiedFields()-->error:"+e);
		}finally{
			closeDBRes(conn, pt, rs);
		}
		
		return list;
	}

	/**
	 * 释放数据库资源
	 * 
	 */
	public static void closeDBRes(DBResource DBRes) {
		//closeDBRes(DBRes.getConnection(), DBRes.getPreparedStatement(), DBRes.getResultSet());
		
		try {
			if (DBRes.getResultSet() != null) {
				DBRes.getResultSet().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (DBRes.getPreparedStatement() != null) {
					DBRes.getPreparedStatement().close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (DBRes.getConnection() != null) {
						DBRes.getConnection().close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void closeDBRes(Connection conn, PreparedStatement pt, ResultSet rs) {
		
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pt != null) {
					pt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (conn != null) {
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
