package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "DrainageVO")
public class DrainageVO {

	// ID
	@DatabaseField(id = true)
	private String id;
	// 
	@DatabaseField
	private String title;
	// 信息发送人id
	@DatabaseField
	private String senderId;
	// 信息发送人编号
	@DatabaseField
	private String senderNum;
	// 信息发送人名称
	@DatabaseField
	private String senderName;
	// 描述内容
	@DatabaseField
	private String content;
	//方案编号
	@DatabaseField
	private String workTaskNum;
	// 接收信息时间
	@DatabaseField
	private String receiveTime;
	
	public String getWorkTaskNum() {
		return workTaskNum;
	}
	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}
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
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderNum() {
		return senderNum;
	}
	public void setSenderNum(String senderNum) {
		this.senderNum = senderNum;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
