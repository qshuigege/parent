package com.example.demo.utils;

import java.util.Map;

public class QueryResult {
	private Map<String, String> tableFieldNameMapping;
	private Map<String, String> tableFieldType;
	public QueryResult(){};
	public QueryResult(Map<String, String> tableFieldNameMapping, Map<String, String> tableFieldType) {
		super();
		this.tableFieldNameMapping = tableFieldNameMapping;
		this.tableFieldType = tableFieldType;
	}
	public Map<String, String> getTableFieldNameMapping() {
		return tableFieldNameMapping;
	}
	public void setTableFieldNameMapping(Map<String, String> tableFieldNameMapping) {
		this.tableFieldNameMapping = tableFieldNameMapping;
	}
	public Map<String, String> getTableFieldType() {
		return tableFieldType;
	}
	public void setTableFieldType(Map<String, String> tableFieldType) {
		this.tableFieldType = tableFieldType;
	}
}
