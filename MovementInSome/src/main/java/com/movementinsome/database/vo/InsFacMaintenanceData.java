package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "InsFacMaintenanceData")
public class InsFacMaintenanceData implements java.io.Serializable {

	// ID
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private Long fmdId;//设施维修数据ID
	@DatabaseField
	private String serialNumber;//流水号
	@DatabaseField
	private String workTaskNum;//任务编号
	@DatabaseField
	private String manageUnit;//管理单位
	@DatabaseField
	private String facName;//设施名称
	@DatabaseField
	private String facNum;//设施编号
	@DatabaseField
	private String gid;//GIS内部编号
	@DatabaseField
	private Long objectid;//OBJECTID
	@DatabaseField
	private String fmdAddr;//详细位置描述
	@DatabaseField
	private String reportedContent;//故障内容（多选）
	@DatabaseField
	private String coordinate;//坐标
	@DatabaseField
	private String remarks;//备注
	@DatabaseField
	private Long state;//状态
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getFmdId() {
		return fmdId;
	}

	public void setFmdId(Long fmdId) {
		this.fmdId = fmdId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public String getManageUnit() {
		return manageUnit;
	}

	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}

	public String getFacName() {
		return facName;
	}

	public void setFacName(String facName) {
		this.facName = facName;
	}

	public String getFacNum() {
		return facNum;
	}

	public void setFacNum(String facNum) {
		this.facNum = facNum;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Long getObjectid() {
		return objectid;
	}

	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}

	public String getFmdAddr() {
		return fmdAddr;
	}

	public void setFmdAddr(String fmdAddr) {
		this.fmdAddr = fmdAddr;
	}

	public String getReportedContent() {
		return reportedContent;
	}

	public void setReportedContent(String reportedContent) {
		this.reportedContent = reportedContent;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}
	
}