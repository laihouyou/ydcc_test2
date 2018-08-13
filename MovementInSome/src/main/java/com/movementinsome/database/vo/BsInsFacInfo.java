package com.movementinsome.database.vo;


import com.esri.core.geometry.Geometry;
import com.movementinsome.map.utils.MapUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BsInsFacInfo")
public class BsInsFacInfo {

	// 巡查设施信息ID
	@DatabaseField(id = true)
	private Long ifiId;
	// 巡查任务ID
	@DatabaseField
	private Long pnitId;
	// 巡查任务编号
	@DatabaseField
	private String pnitNum;
	// 设施名称
	@DatabaseField
	private String ifiFacType;
	// 设施编号
	@DatabaseField
	private String ifiFacNum;
	// 所在道路
	@DatabaseField
	private String ifiFacLaneWay;
	// 安装地址
	@DatabaseField
	private String ifiFacAddr;
	// 口径
	@DatabaseField
	private Long ifiFacDs;
	// 管理单位
	@DatabaseField
	private String manageUnit;
	// GID
	@DatabaseField
	private String gid;
	// OBJECTID
	@DatabaseField
	private Long objectid;
	// 设施坐标
	@DatabaseField
	private String ifiFacCoordinate;
	// 周期
	@DatabaseField
	private String frequencyType;
	// 频次
	@DatabaseField
	private String frequency;
	// 频率描述
	@DatabaseField
	private String frequencyDesc;
	// 最后巡检时间
	@DatabaseField
	private String lastInsDateStr;
	
	// 巡检次数
	@DatabaseField
	private Long insCount;
	// 上报表单名
	@DatabaseField
	private String reportTableNames;
	// 次数
	@DatabaseField
	private Long frequencyNumber;
	// 任务编号
	@DatabaseField
	private String workTaskNum;
	
	
	
	public Long getFrequencyNumber() {
		return frequencyNumber;
	}
	public void setFrequencyNumber(Long frequencyNumber) {
		this.frequencyNumber = frequencyNumber;
	}
	public String getWorkTaskNum() {
		return workTaskNum;
	}
	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}
	public String getReportTableNames() {
		return reportTableNames;
	}
	public void setReportTableNames(String reportTableNames) {
		this.reportTableNames = reportTableNames;
	}
	public Long getInsCount() {
		return insCount;
	}
	public void setInsCount(Long insCount) {
		this.insCount = insCount;
	}
	
	public String getLastInsDateStr() {
		return lastInsDateStr;
	}
	public void setLastInsDateStr(String lastInsDateStr) {
		this.lastInsDateStr = lastInsDateStr;
	}
	public Long getIfiId() {
		return ifiId;
	}
	public void setIfiId(Long ifiId) {
		this.ifiId = ifiId;
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
	public String getIfiFacType() {
		return ifiFacType;
	}
	public void setIfiFacType(String ifiFacType) {
		this.ifiFacType = ifiFacType;
	}
	public String getIfiFacNum() {
		return ifiFacNum;
	}
	public void setIfiFacNum(String ifiFacNum) {
		this.ifiFacNum = ifiFacNum;
	}
	public String getIfiFacLaneWay() {
		return ifiFacLaneWay;
	}
	public void setIfiFacLaneWay(String ifiFacLaneWay) {
		this.ifiFacLaneWay = ifiFacLaneWay;
	}
	public String getIfiFacAddr() {
		return ifiFacAddr;
	}
	public void setIfiFacAddr(String ifiFacAddr) {
		this.ifiFacAddr = ifiFacAddr;
	}
	public Long getIfiFacDs() {
		return ifiFacDs;
	}
	public void setIfiFacDs(Long ifiFacDs) {
		this.ifiFacDs = ifiFacDs;
	}
	public String getManageUnit() {
		return manageUnit;
	}
	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
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
	public String getIfiFacCoordinate() {
		return ifiFacCoordinate;
	}
	public void setIfiFacCoordinate(String ifiFacCoordinate) {
		this.ifiFacCoordinate = ifiFacCoordinate;
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
	public Geometry getShape(){
		return MapUtil.wkt2Geometry(ifiFacCoordinate);
	}
}
