package com.movementinsome.database.vo;



import com.esri.core.geometry.Geometry;
import com.movementinsome.map.utils.MapUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BsLeakInsArea")
public class BsLeakInsArea {

	// 查漏区域ID
	@DatabaseField(id = true)
	private Long liaId;
	// 巡查任务ID
	@DatabaseField
	private Long pnitId;
	// 区域编号
	@DatabaseField
	private String areaNum;
	// 区域名称
	@DatabaseField
	private String areaName;
	// 区域描述
	@DatabaseField
	private String areaDesc;
	@DatabaseField
	private String areaLevel;
	// 管理单位
	@DatabaseField
	private String manageUnit;
	// OBJECTID
	@DatabaseField
	private Long objectid;
	// 查漏区域坐标
	@DatabaseField
	private String liaCoordinate;
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
	
	public Long getLiaId() {
		return liaId;
	}
	public void setLiaId(Long liaId) {
		this.liaId = liaId;
	}
	public Long getPnitId() {
		return pnitId;
	}
	public void setPnitId(Long pnitId) {
		this.pnitId = pnitId;
	}
	public String getAreaNum() {
		return areaNum;
	}
	public void setAreaNum(String areaNum) {
		this.areaNum = areaNum;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getAreaDesc() {
		return areaDesc;
	}
	public void setAreaDesc(String areaDesc) {
		this.areaDesc = areaDesc;
	}
	public String getAreaLevel() {
		return areaLevel;
	}
	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}
	public String getManageUnit() {
		return manageUnit;
	}
	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}
	public Long getObjectid() {
		return objectid;
	}
	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}
	public String getLiaCoordinate() {
		return liaCoordinate;
	}
	public void setLiaCoordinate(String liaCoordinate) {
		this.liaCoordinate = liaCoordinate;
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
		return MapUtil.wkt2Geometry(liaCoordinate);
	}
}
