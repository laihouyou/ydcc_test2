package com.movementinsome.database.vo;


//签到点签到记录类
public class InsReachRecord implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7474453943212909492L;
	// Fields
	// 到达ID
	private Long irrId;
	// 签到时间
	private String irrDate;
	// 签到人名称
	private String irrName;
	// 签到位置
	private String irrPosition;
	// GUID
	private String guid;
	// 上报坐标点
	private String reportedCoordinate;
	// 流水号
	private String serialNumber;
	// 管理单位
	private String manageUnit;
	// 移动端唯一编码
	private String moiNum;
	// 移动端机器码
	private String phoneImei;
	// OBJECTID
	private Long objectid;
	// 任务编号
	private String workTaskNum;
	// 创建日期
	private String createDate;
	// 创建人ID
	private Long createUId;
	// 创建人编号
	private String createUNum;
	// 创建人名称
	private String createUName;
	// 最后更新时间
	private String lastUpdateDate;
	// 最后更新人ID
	private Long lastUpdateUId;
	// 最后更新人编号
	private String lastUpdateUNum;
	// 最后更新人名称
	private String lastUpdateUName;

	// Constructors

	/** default constructor */
	public InsReachRecord() {
	}

	public Long getIrrId() {
		return irrId;
	}

	public void setIrrId(Long irrId) {
		this.irrId = irrId;
	}

	public String getIrrName() {
		return irrName;
	}

	public void setIrrName(String irrName) {
		this.irrName = irrName;
	}

	public String getIrrPosition() {
		return irrPosition;
	}

	public void setIrrPosition(String irrPosition) {
		this.irrPosition = irrPosition;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getReportedCoordinate() {
		return reportedCoordinate;
	}

	public void setReportedCoordinate(String reportedCoordinate) {
		this.reportedCoordinate = reportedCoordinate;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getManageUnit() {
		return manageUnit;
	}

	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}

	public String getMoiNum() {
		return moiNum;
	}

	public void setMoiNum(String moiNum) {
		this.moiNum = moiNum;
	}

	public String getPhoneImei() {
		return phoneImei;
	}

	public void setPhoneImei(String phoneImei) {
		this.phoneImei = phoneImei;
	}

	public Long getObjectid() {
		return objectid;
	}

	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
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


}