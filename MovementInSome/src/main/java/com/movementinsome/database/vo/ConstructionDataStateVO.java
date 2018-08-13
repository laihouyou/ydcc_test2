package com.movementinsome.database.vo;

import java.io.Serializable;

//更新施工上报登状态
public class ConstructionDataStateVO implements Serializable{
	// 关键字
	private String tableName;
	// 施工上报参数
	private String params;
	// 状态
	private String state;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

}
