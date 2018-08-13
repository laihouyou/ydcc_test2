package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

// 配合工作
@DatabaseTable(tableName = "CoordinateVO")
public class CoordinateVO {
	// 表id
	@DatabaseField(id = true)
	private String id;
	// 
	@DatabaseField
	private String title;
	// 任务类型
	@DatabaseField
	private String taskCategory;
	// 任务编号
	@DatabaseField
	private String workTaskNum;
	// 地址
	@DatabaseField
	private String workTaskAddr;
	// 工作内容
	@DatabaseField
	private String workTaskWork;
	// 接单时间
	@DatabaseField
	private String receiveTime;
	
	
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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
	public String getWorkTaskNum() {
		return workTaskNum;
	}
	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}
	public String getWorkTaskAddr() {
		return workTaskAddr;
	}
	public void setWorkTaskAddr(String workTaskAddr) {
		this.workTaskAddr = workTaskAddr;
	}
	public String getWorkTaskWork() {
		return workTaskWork;
	}
	public void setWorkTaskWork(String workTaskWork) {
		this.workTaskWork = workTaskWork;
	}
	
	
}
