package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BsPnInsTask")
public class BsPnInsTask  {

	// 巡查任务ID
	@DatabaseField(id = true)
	private Long pnitId;
	// 巡查任务编号
	@DatabaseField
	private String pnitNum;
	// 巡查任务名称
	@DatabaseField
	private String pnitName;
	// 巡查任务类型
	@DatabaseField
	private String pnitType;
	// 工作内容描述
	@DatabaseField
	private String workDesc;
	// 计划开始时间
	@DatabaseField
	private String planSDateStr;
	// 计划结束时间
	@DatabaseField
	private String planEDateStr;
	// 实际开始时间
	@DatabaseField
	private String actualSDateStr;
	// 实际结束时间
	@DatabaseField
	private String actualEDateStr;
	// 备注
	@DatabaseField
	private String remark;
	// 管理单位
	@DatabaseField
	private String manageUnit;
	// 任务状态
	@DatabaseField
	private Long pnitStatus;
	// 创建日期
	@DatabaseField
	private String createDateStr;
	// 创建人ID
	@DatabaseField
	private Long createUId;
	// 创建人编号
	@DatabaseField
	private String createUNum;
	// 创建人名称
	@DatabaseField
	private String createUName;
	// 最后更新时间
	@DatabaseField
	private String lastUpdateDateStr;
	// 最后更新人ID
	@DatabaseField
	private Long lastUpdateUId;
	// 最后更新人编号
	@DatabaseField
	private String lastUpdateUNum;
	// 最后更新人名称
	@DatabaseField
	private String lastUpdateUName;
	// 周期
	@DatabaseField
	private String frequencyType;
	// 频次
	@DatabaseField
	private String frequency;
	// 频率描述
	@DatabaseField
	private String frequencyDesc;
	// 次数
	@DatabaseField
	private long FrequencyNumber;
	
	
	public long getFrequencyNumber() {
		return FrequencyNumber;
	}
	public void setFrequencyNumber(long frequencyNumber) {
		FrequencyNumber = frequencyNumber;
	}
	public Long getPnitId() {
		return pnitId;
	}
	public void setPnitId(Long pnitId) {
		this.pnitId = pnitId;
	}
	public String getPnitNum() {
		return pnitNum;
	}
	public void setPnitNum(String pnitNum) {
		this.pnitNum = pnitNum;
	}
	public String getPnitName() {
		return pnitName;
	}
	public void setPnitName(String pnitName) {
		this.pnitName = pnitName;
	}
	public String getPnitType() {
		return pnitType;
	}
	public void setPnitType(String pnitType) {
		this.pnitType = pnitType;
	}
	public String getWorkDesc() {
		return workDesc;
	}
	public void setWorkDesc(String workDesc) {
		this.workDesc = workDesc;
	}
	public String getPlanSDateStr() {
		return planSDateStr;
	}
	public void setPlanSDateStr(String planSDateStr) {
		this.planSDateStr = planSDateStr;
	}
	public String getPlanEDateStr() {
		return planEDateStr;
	}
	public void setPlanEDateStr(String planEDateStr) {
		this.planEDateStr = planEDateStr;
	}
	public String getActualSDateStr() {
		return actualSDateStr;
	}
	public void setActualSDateStr(String actualSDateStr) {
		this.actualSDateStr = actualSDateStr;
	}
	public String getActualEDateStr() {
		return actualEDateStr;
	}
	public void setActualEDateStr(String actualEDateStr) {
		this.actualEDateStr = actualEDateStr;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getManageUnit() {
		return manageUnit;
	}
	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}
	public Long getPnitStatus() {
		return pnitStatus;
	}
	public void setPnitStatus(Long pnitStatus) {
		this.pnitStatus = pnitStatus;
	}
	public String getCreateDateStr() {
		return createDateStr;
	}
	public void setCreateDateStr(String createDateStr) {
		this.createDateStr = createDateStr;
	}
	public Long getCreateUId() {
		return createUId;
	}
	public void setCreateUId(Long createUId) {
		this.createUId = createUId;
	}
	public String getCreateUNum() {
		return createUNum;
	}
	public void setCreateUNum(String createUNum) {
		this.createUNum = createUNum;
	}
	public String getCreateUName() {
		return createUName;
	}
	public void setCreateUName(String createUName) {
		this.createUName = createUName;
	}
	public String getLastUpdateDateStr() {
		return lastUpdateDateStr;
	}
	public void setLastUpdateDateStr(String lastUpdateDateStr) {
		this.lastUpdateDateStr = lastUpdateDateStr;
	}
	public Long getLastUpdateUId() {
		return lastUpdateUId;
	}
	public void setLastUpdateUId(Long lastUpdateUId) {
		this.lastUpdateUId = lastUpdateUId;
	}
	public String getLastUpdateUNum() {
		return lastUpdateUNum;
	}
	public void setLastUpdateUNum(String lastUpdateUNum) {
		this.lastUpdateUNum = lastUpdateUNum;
	}
	public String getLastUpdateUName() {
		return lastUpdateUName;
	}
	public void setLastUpdateUName(String lastUpdateUName) {
		this.lastUpdateUName = lastUpdateUName;
	}
	public String getFrequencyType() {
		return frequencyType;
	}
	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getFrequencyDesc() {
		return frequencyDesc;
	}
	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

}
