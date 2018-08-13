package com.movementinsome.database.vo;



/**
 * 任务反馈对象
 * 
 * @author gddst
 * 
 */
public class InsTaskFeedBackVO {

	/**
	 * taskKey: 接单(receive),退单(return),延期(delay),撤销延期(undelay),销单(finish)
	 * 出发时间(go-off),到场时间(arriveTime),开工时间(startTime),停水时间(stopTime)
	 * 复水时间(resumeTime),完工时间(commitTime)
	 * 
	 * taskValue： 退单,延期说明 到场情况说明
	 */

	// 任务编号
	private String taskNumber;
	// 任务别名
	private String taskAlias;
	// 标示键
	private String taskKey;
	// 时间内容值
	private String timeValue;
	// 其他内容值
	private String taskValue;
	//到场：影响情况
	private String taskAffect;
	// 填表时的经度,Y
	private String longitude; 	
	// 填表时的纬度,X
	private String latitude;  	
	// 用户ID
	private String userId; 
	// 用户编号
	private String userNum; 
	// 用户名称
	private String userName; 	
	// 手机机器码
	private String phoneImei; 								

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getTaskKey() {
		return taskKey;
	}

	public void setTaskKey(String taskKey) {
		this.taskKey = taskKey;
	}

	public String getTimeValue() {
		return timeValue;
	}

	public void setTimeValue(String timeValue) {
		this.timeValue = timeValue;
	}

	public String getTaskValue() {
		return taskValue;
	}

	public void setTaskValue(String taskValue) {
		this.taskValue = taskValue;
	}

	public String getTaskAlias() {
		return taskAlias;
	}

	public void setTaskAlias(String taskAlias) {
		this.taskAlias = taskAlias;
	}

	public String getTaskAffect() {
		return taskAffect;
	}

	public void setTaskAffect(String taskAffect) {
		this.taskAffect = taskAffect;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNum() {
		return userNum;
	}

	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoneImei() {
		return phoneImei;
	}

	public void setPhoneImei(String phoneImei) {
		this.phoneImei = phoneImei;
	}

}
