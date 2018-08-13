package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MyReceiveMsgVO")
public class MyReceiveMsgVO {
	
	// id
	@DatabaseField(id = true)
	private String id;
	// 接收时间
	@DatabaseField
	private String receiveTime;
	// 消息内容
	@DatabaseField
	private String receiveMsg;
	// 定位坐标
	@DatabaseField
	private String coordinate;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReceiveTime() {
		return receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}
	public String getReceiveMsg() {
		return receiveMsg;
	}
	public void setReceiveMsg(String receiveMsg) {
		this.receiveMsg = receiveMsg;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
	
	
}
