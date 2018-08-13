package com.movementinsome.database.vo;

import com.esri.core.geometry.Geometry;
import com.movementinsome.map.utils.MapUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BsSupervisionPoint")
public class BsSupervisionPoint {

	// 区域编号
	private String areaNum;
	// 是否显示
	private boolean noShow;
	// 监督点ID
	@DatabaseField(id = true)
	private Long spId;
	// 巡查任务ID
	@DatabaseField
	private Long pnitId;
	// 监督点编号
	@DatabaseField
	private String spNum;
	// 监督点名称
	@DatabaseField
	private String spName;
	// 监督点类型
	@DatabaseField
	private String spType;
	// 重要级别
	@DatabaseField
	private String pointLevel;
	// OBJECTID
	@DatabaseField
	private Long objectid;
	// 常规巡查区域坐标
	@DatabaseField
	private String spCoordinate;

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
	
	
	public boolean isNoShow() {
		return noShow;
	}
	public void setNoShow(boolean noShow) {
		this.noShow = noShow;
	}
	public String getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
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
	public Long getSpId() {
		return spId;
	}
	public void setSpId(Long spId) {
		this.spId = spId;
	}
	public Long getPnitId() {
		return pnitId;
	}
	public void setPnitId(Long pnitId) {
		this.pnitId = pnitId;
	}
	public String getSpNum() {
		return spNum;
	}
	public void setSpNum(String spNum) {
		this.spNum = spNum;
	}
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	public String getSpType() {
		return spType;
	}
	public void setSpType(String spType) {
		this.spType = spType;
	}
	public String getPointLevel() {
		return pointLevel;
	}
	public void setPointLevel(String pointLevel) {
		this.pointLevel = pointLevel;
	}
	public Long getObjectid() {
		return objectid;
	}
	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}
	public String getSpCoordinate() {
		return spCoordinate;
	}
	public void setSpCoordinate(String spCoordinate) {
		this.spCoordinate = spCoordinate;
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
		return MapUtil.wkt2Geometry(spCoordinate);
	}
}
