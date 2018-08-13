package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsTableSaveDataVo")
public class InsTableSaveDataVo {
	
	//id
	@DatabaseField(id = true)
	private String guid;
	// 编号
	@DatabaseField
	private String mainNumber;
	// 子编号
	@DatabaseField
	private String childNumber;
	// 类型
	@DatabaseField
	private String tbType;
	// 状态(0:编辑过,1：还没编辑,2：提交)
	@DatabaseField
	private String tbState;
	// 数据
	@DatabaseField
	private String tbData;
	// 后台标示
	@DatabaseField
	private String title;
	// 任务类型
	@DatabaseField
	private String taskCategory;
	
	
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTaskCategory() {
		return taskCategory;
	}
	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getMainNumber() {
		return mainNumber;
	}
	public void setMainNumber(String mainNumber) {
		this.mainNumber = mainNumber;
	}
	public String getChildNumber() {
		return childNumber;
	}
	public void setChildNumber(String childNumber) {
		this.childNumber = childNumber;
	}
	public String getTbType() {
		return tbType;
	}
	public void setTbType(String tbType) {
		this.tbType = tbType;
	}
	public String getTbState() {
		return tbState;
	}
	public void setTbState(String tbState) {
		this.tbState = tbState;
	}
	public String getTbData() {
		return tbData;
	}
	public void setTbData(String tbData) {
		this.tbData = tbData;
	}
	
	
}
