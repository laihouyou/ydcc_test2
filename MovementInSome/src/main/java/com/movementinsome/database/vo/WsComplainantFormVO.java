package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "WsComplainantFormVO")
public class WsComplainantFormVO {
	/*
	 * 武水派工单下载对象
	*/
	// id
	@DatabaseField(id = true)
	private Long twoId;
	//紧急程度
	@DatabaseField
	private String urgencyLevel;
	//客服关联字段
	@DatabaseField
	private String workNum;
	//接报时间
	@DatabaseField
	private String receivedDate;
	//客户资料号
	@DatabaseField
	private String userNum;
	//任务编号
	@DatabaseField
	private String workTaskNum;
	//反映人
	@DatabaseField
	private String reportedPerson;
	//反映单位
	@DatabaseField
	private String units;
	//联系电话
	@DatabaseField
	private String tel;
	//呼入电话
	@DatabaseField
	private String callPhone;
	//反映内容
	@DatabaseField
	private String reportedContent;
	//发生地址
	@DatabaseField
	private String happenAddr;
	//反映形式
	@DatabaseField
	private String reportedPatterned;
	//反映来源
	@DatabaseField
	private String sources;
	//反映类别
	@DatabaseField
	private String reportedCategory;
	//发生时间
	@DatabaseField
	private String happenDate;
	//预约日期
	@DatabaseField
	private String advanceDate;
	//处理级别
	@DatabaseField
	private String transactionLevels;
	@DatabaseField
	private String backReason;
	@DatabaseField
	private String backDate;
	//任务状态0：未派工(新单),1：消息未送达,2：消息已送达,3:已接收,4：处理中,5：已完成,6：开始,7：暂停,8：退单
	@DatabaseField
	private Long status;
	//受理人
	@DatabaseField
	private String receptionist;
	//反应意见（受理备注）
	@DatabaseField
	private String reactionComments;
	//处理意见
	@DatabaseField
	private String treatmentAdvice;
	//处理结果
	@DatabaseField
	private String results;
	//受理备注
	@DatabaseField
	private String remarks;
	//设施类型
	@DatabaseField
	private String deviceType;
	//设施编码
	@DatabaseField
	private String deviceNum;
	//记录来源 (1、热线,2、问题上报)
	@DatabaseField
	private Long recordType;
	// GUID
	@DatabaseField
	private String guid;
  	//手机需要填写的工单模板，中间层下载时使用
	@DatabaseField
  	private String androidForm;
	@DatabaseField
	private String reportedCoordinate;
	
	
	public String getReportedCoordinate() {
		return reportedCoordinate;
	}
	public void setReportedCoordinate(String reportedCoordinate) {
		this.reportedCoordinate = reportedCoordinate;
	}
	public Long getTwoId() {
		return twoId;
	}
	public void setTwoId(Long twoId) {
		this.twoId = twoId;
	}
	public String getWorkTaskNum() {
		return workTaskNum;
	}
	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}
	public String getUrgencyLevel() {
		return urgencyLevel;
	}
	public void setUrgencyLevel(String urgencyLevel) {
		this.urgencyLevel = urgencyLevel;
	}
	public String getWorkNum() {
		return workNum;
	}
	public void setWorkNum(String workNum) {
		this.workNum = workNum;
	}
	public String getReceivedDate() {
		return receivedDate;
	}
	public void setReceivedDate(String receivedDate) {
		this.receivedDate = receivedDate;
	}
	public String getUserNum() {
		return userNum;
	}
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}
	public String getReportedPerson() {
		return reportedPerson;
	}
	public void setReportedPerson(String reportedPerson) {
		this.reportedPerson = reportedPerson;
	}
	public String getUnits() {
		return units;
	}
	public void setUnits(String units) {
		this.units = units;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getCallPhone() {
		return callPhone;
	}
	public void setCallPhone(String callPhone) {
		this.callPhone = callPhone;
	}
	public String getReportedContent() {
		return reportedContent;
	}
	public void setReportedContent(String reportedContent) {
		this.reportedContent = reportedContent;
	}
	public String getHappenAddr() {
		return happenAddr;
	}
	public void setHappenAddr(String happenAddr) {
		this.happenAddr = happenAddr;
	}
	public String getReportedPatterned() {
		return reportedPatterned;
	}
	public void setReportedPatterned(String reportedPatterned) {
		this.reportedPatterned = reportedPatterned;
	}
	public String getSources() {
		return sources;
	}
	public void setSources(String sources) {
		this.sources = sources;
	}
	public String getReportedCategory() {
		return reportedCategory;
	}
	public void setReportedCategory(String reportedCategory) {
		this.reportedCategory = reportedCategory;
	}
	public String getHappenDate() {
		return happenDate;
	}
	public void setHappenDate(String happenDate) {
		this.happenDate = happenDate;
	}
	public String getAdvanceDate() {
		return advanceDate;
	}
	public void setAdvanceDate(String advanceDate) {
		this.advanceDate = advanceDate;
	}
	public String getTransactionLevels() {
		return transactionLevels;
	}
	public void setTransactionLevels(String transactionLevels) {
		this.transactionLevels = transactionLevels;
	}
	public String getBackReason() {
		return backReason;
	}
	public void setBackReason(String backReason) {
		this.backReason = backReason;
	}
	public String getBackDate() {
		return backDate;
	}
	public void setBackDate(String backDate) {
		this.backDate = backDate;
	}
	public Long getStatus() {
		return status;
	}
	public void setStatus(Long status) {
		this.status = status;
	}
	public String getReceptionist() {
		return receptionist;
	}
	public void setReceptionist(String receptionist) {
		this.receptionist = receptionist;
	}
	public String getReactionComments() {
		return reactionComments;
	}
	public void setReactionComments(String reactionComments) {
		this.reactionComments = reactionComments;
	}
	public String getTreatmentAdvice() {
		return treatmentAdvice;
	}
	public void setTreatmentAdvice(String treatmentAdvice) {
		this.treatmentAdvice = treatmentAdvice;
	}
	public String getResults() {
		return results;
	}
	public void setResults(String results) {
		this.results = results;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getDeviceNum() {
		return deviceNum;
	}
	public void setDeviceNum(String deviceNum) {
		this.deviceNum = deviceNum;
	}
	public Long getRecordType() {
		return recordType;
	}
	public void setRecordType(Long recordType) {
		this.recordType = recordType;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getAndroidForm() {
		return androidForm;
	}
	public void setAndroidForm(String androidForm) {
		this.androidForm = androidForm;
	}
	
}
