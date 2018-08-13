package com.movementinsome.database.vo;


/**
 * InsUpCover entity. @author MyEclipse Persistence Tools
 */

public class InsUpCover implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7858106915661837305L;
	// Fields
	//补盖ID
	private Long ucId;
	//补盖时间
	private String ucDateStr;
	//位置
	private String addr;
	//规格
	private String specification;
	//雨水盖（数量个）
	private Long rainCover;
	//污水盖（数量个）
	private Long sewageCover;
	//给水盖（数量个）
	private Long waterCover;
	//雨水箅（数量个）
	private Long rainGrate;
	//原因
	private String reason;
	//备注
	private String remarks;
	//GUID
	private String guid;
	//上报坐标点
	private String reportedCoordinate;
	//流水号
	private String serialNumber;
	//管理单位
	private String manageUnit;
	//创建日期
	private String createDateStr;
	//创建人ID
	private Long createUId;
	//创建人编号
	private String createUNum;
	//创建人名称
	private String createUName;
	//最后更新时间
	private String lastUpdateDateStr;
	//最后更新人ID
	private Long lastUpdateUId;
	//最后更新人编号
	private String lastUpdateUNum;
	//最后更新人名称
	private String lastUpdateUName;
	//移动端唯一编码
	private String moiNum;
	//移动端机器码
	private String phoneImei;

	public Long getUcId() {
		return this.ucId;
	}

	public void setUcId(Long ucId) {
		this.ucId = ucId;
	}

	public String getAddr() {
		return this.addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getSpecification() {
		return this.specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Long getRainCover() {
		return this.rainCover;
	}

	public void setRainCover(Long rainCover) {
		this.rainCover = rainCover;
	}

	public Long getSewageCover() {
		return this.sewageCover;
	}

	public void setSewageCover(Long sewageCover) {
		this.sewageCover = sewageCover;
	}

	public Long getWaterCover() {
		return this.waterCover;
	}

	public void setWaterCover(Long waterCover) {
		this.waterCover = waterCover;
	}

	public Long getRainGrate() {
		return this.rainGrate;
	}

	public void setRainGrate(Long rainGrate) {
		this.rainGrate = rainGrate;
	}

	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getReportedCoordinate() {
		return this.reportedCoordinate;
	}

	public void setReportedCoordinate(String reportedCoordinate) {
		this.reportedCoordinate = reportedCoordinate;
	}

	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getManageUnit() {
		return this.manageUnit;
	}

	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}

	public Long getCreateUId() {
		return this.createUId;
	}

	public void setCreateUId(Long createUId) {
		this.createUId = createUId;
	}

	public String getCreateUNum() {
		return this.createUNum;
	}

	public void setCreateUNum(String createUNum) {
		this.createUNum = createUNum;
	}

	public String getCreateUName() {
		return this.createUName;
	}

	public void setCreateUName(String createUName) {
		this.createUName = createUName;
	}

	public Long getLastUpdateUId() {
		return this.lastUpdateUId;
	}

	public void setLastUpdateUId(Long lastUpdateUId) {
		this.lastUpdateUId = lastUpdateUId;
	}

	public String getLastUpdateUNum() {
		return this.lastUpdateUNum;
	}

	public void setLastUpdateUNum(String lastUpdateUNum) {
		this.lastUpdateUNum = lastUpdateUNum;
	}

	public String getLastUpdateUName() {
		return this.lastUpdateUName;
	}

	public void setLastUpdateUName(String lastUpdateUName) {
		this.lastUpdateUName = lastUpdateUName;
	}

	public String getMoiNum() {
		return this.moiNum;
	}

	public void setMoiNum(String moiNum) {
		this.moiNum = moiNum;
	}
}