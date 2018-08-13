package com.movementinsome.database.vo;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsTablePushTaskVo")
public class InsTablePushTaskVo implements Serializable{

	// ID
	@DatabaseField(id = true)
	private String guid;
	// 用户名称
	@DatabaseField
	private String username;
	// 后台标示
	@DatabaseField
	private String title;
	// 任务类型
	@DatabaseField
	private String taskCategory;
	// 任务编号
	@DatabaseField
	private String taskNum;
	// 任务类型(数组：a1,a2,a3...)
	@DatabaseField
	private String names;
	// 任务名称(数组：a1,a2,a3...)
	@DatabaseField
	private String values;
	// 下载标示（1表示已下载）
	@DatabaseField
	private String isDownload;
	// 表单状态：1（操作中）2（提交）3(超期)
	@DatabaseField
	private String tbType;
	
	public String getTbType() {
		return tbType;
	}
	public void setTbType(String tbType) {
		this.tbType = tbType;
	}
	public String getIsDownload() {
		return isDownload;
	}
	public void setIsDownload(String isDownload) {
		this.isDownload = isDownload;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}
	public String getNames() {
		return names;
	}
	public void setNames(String names) {
		this.names = names;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	
	
	 
}
