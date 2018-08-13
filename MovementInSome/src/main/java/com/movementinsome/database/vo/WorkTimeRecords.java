package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


/**
 * InsUpCover entity. @author MyEclipse Persistence Tools
 */
@DatabaseTable(tableName = "WorkTimeRecords")
public class WorkTimeRecords implements java.io.Serializable {

	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private String moiNum;
	//打卡时间
	@DatabaseField
	private String workTime;
	// 打卡类型
	@DatabaseField
	private int type;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMoiNum() {
		return moiNum;
	}
	public void setMoiNum(String moiNum) {
		this.moiNum = moiNum;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}