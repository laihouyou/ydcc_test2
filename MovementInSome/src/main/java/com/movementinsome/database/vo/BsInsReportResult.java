package com.movementinsome.database.vo;


/**
 * BsInsReportResult entity. @author MyEclipse Persistence Tools
 */

public class BsInsReportResult implements java.io.Serializable {

//	任务编号
	private String bsTaskNum;
//	路段编号
	private String bsIrsNum;
//	设施编号
	private String bsFacNum;
//	设施名称
	private String bsFacName;
//	设施类型
	private String bsFacType;
//	设施所在位置
	private String bsFacAddr;
//	设施口径
	private Long bsFacCaliber;
//	设施材质
	private String bsFacMaterial;
//	设施埋深
	private Double bsFacDepth;
//	设施坐标
	private String bsFacMap;
//	设施所在路段
	private String bsFacRiName;
//	设施GID
	private String gid;
//	备注
	private String bsRemarks;
//	上报时间
	private String bsReportDateStr;

//	上报人ID
	private Long bsReportId;
//	上报人编号
	private String bsReportNum;
//	上报人名称
	private String bsReportName;
//	上报人部门ID
	private Long bsReportDeptId;
//	上报人部门编号
	private String bsReportDeptNum;
//	上报人部门名称
	private String bsReportDeptName;
//	上报人班组ID
	private Long bsReportTeamId;
//	上报人班组编号
	private String bsReportTeamNum;
//	上报人班组名称
	private String bsReportTeamName;
//	上报人手机机器码
	private String imei;
//	GUID
	private String guid;
	// 结果选项
	private String bsInsReportContentLists;

	public String getBsTaskNum() {
		return this.bsTaskNum;
	}

	public void setBsTaskNum(String bsTaskNum) {
		this.bsTaskNum = bsTaskNum;
	}

	public String getBsIrsNum() {
		return this.bsIrsNum;
	}

	public void setBsIrsNum(String bsIrsNum) {
		this.bsIrsNum = bsIrsNum;
	}

	public String getBsFacNum() {
		return this.bsFacNum;
	}

	public void setBsFacNum(String bsFacNum) {
		this.bsFacNum = bsFacNum;
	}

	public String getBsFacName() {
		return this.bsFacName;
	}

	public void setBsFacName(String bsFacName) {
		this.bsFacName = bsFacName;
	}

	public String getBsFacType() {
		return this.bsFacType;
	}

	public void setBsFacType(String bsFacType) {
		this.bsFacType = bsFacType;
	}

	public String getBsFacAddr() {
		return this.bsFacAddr;
	}

	public void setBsFacAddr(String bsFacAddr) {
		this.bsFacAddr = bsFacAddr;
	}

	public String getBsFacMaterial() {
		return this.bsFacMaterial;
	}

	public void setBsFacMaterial(String bsFacMaterial) {
		this.bsFacMaterial = bsFacMaterial;
	}

	public Double getBsFacDepth() {
		return this.bsFacDepth;
	}

	public void setBsFacDepth(Double bsFacDepth) {
		this.bsFacDepth = bsFacDepth;
	}

	public String getBsFacMap() {
		return this.bsFacMap;
	}

	public void setBsFacMap(String bsFacMap) {
		this.bsFacMap = bsFacMap;
	}

	public String getBsFacRiName() {
		return this.bsFacRiName;
	}

	public void setBsFacRiName(String bsFacRiName) {
		this.bsFacRiName = bsFacRiName;
	}

	public String getGid() {
		return this.gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getBsRemarks() {
		return this.bsRemarks;
	}

	public void setBsRemarks(String bsRemarks) {
		this.bsRemarks = bsRemarks;
	}

	public String getBsReportNum() {
		return this.bsReportNum;
	}

	public void setBsReportNum(String bsReportNum) {
		this.bsReportNum = bsReportNum;
	}

	public String getBsReportName() {
		return this.bsReportName;
	}

	public void setBsReportName(String bsReportName) {
		this.bsReportName = bsReportName;
	}

	public String getBsReportDeptNum() {
		return this.bsReportDeptNum;
	}

	public void setBsReportDeptNum(String bsReportDeptNum) {
		this.bsReportDeptNum = bsReportDeptNum;
	}

	public String getBsReportDeptName() {
		return this.bsReportDeptName;
	}

	public void setBsReportDeptName(String bsReportDeptName) {
		this.bsReportDeptName = bsReportDeptName;
	}

	public String getBsReportTeamNum() {
		return this.bsReportTeamNum;
	}

	public void setBsReportTeamNum(String bsReportTeamNum) {
		this.bsReportTeamNum = bsReportTeamNum;
	}

	public String getBsReportTeamName() {
		return this.bsReportTeamName;
	}

	public void setBsReportTeamName(String bsReportTeamName) {
		this.bsReportTeamName = bsReportTeamName;
	}

	public String getImei() {
		return this.imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Long getBsFacCaliber() {
		return bsFacCaliber;
	}

	public void setBsFacCaliber(Long bsFacCaliber) {
		this.bsFacCaliber = bsFacCaliber;
	}

	public String getBsReportDateStr() {
		return bsReportDateStr;
	}

	public void setBsReportDateStr(String bsReportDateStr) {
		this.bsReportDateStr = bsReportDateStr;
	}

	public Long getBsReportId() {
		return bsReportId;
	}

	public void setBsReportId(Long bsReportId) {
		this.bsReportId = bsReportId;
	}

	public Long getBsReportDeptId() {
		return bsReportDeptId;
	}

	public void setBsReportDeptId(Long bsReportDeptId) {
		this.bsReportDeptId = bsReportDeptId;
	}

	public Long getBsReportTeamId() {
		return bsReportTeamId;
	}

	public void setBsReportTeamId(Long bsReportTeamId) {
		this.bsReportTeamId = bsReportTeamId;
	}
	
}