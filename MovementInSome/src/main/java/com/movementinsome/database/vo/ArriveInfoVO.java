package com.movementinsome.database.vo;



import java.util.List;

public class ArriveInfoVO {

	// 任务编号
	private String taskNumber;

	// 时间内容值
	private String timeValue;

	// 处理人
	private String userName;

	// 填表时的经度,Y
	private String longitude;
	
	// 填表时的纬度,X
	private String latitude;
	
	// 用户ID
	private String userId;
	
	// 用户编号
	private String userNum;
	
	// 手机机器码
	private String phoneImei; 		
	
	// 任务别名
	private String taskAlias;

	// 图片组
	private List<PhotoInfoVO> imageBaseCode;

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getTimeValue() {
		return timeValue;
	}

	public void setTimeValue(String timeValue) {
		this.timeValue = timeValue;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<PhotoInfoVO> getImageBaseCode() {
		return imageBaseCode;
	}

	public void setImageBaseCode(List<PhotoInfoVO> imageBaseCode) {
		this.imageBaseCode = imageBaseCode;
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

	public String getPhoneImei() {
		return phoneImei;
	}

	public void setPhoneImei(String phoneImei) {
		this.phoneImei = phoneImei;
	}

	public String getTaskAlias() {
		return taskAlias;
	}

	public void setTaskAlias(String taskAlias) {
		this.taskAlias = taskAlias;
	}

}
