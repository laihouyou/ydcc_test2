package com.movementinsome.database.vo;


import com.esri.core.geometry.Geometry;
import com.movementinsome.map.utils.MapUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BsEmphasisInsArea")
public class BsEmphasisInsArea  {

	// 重点区域ID
	@DatabaseField(id = true)
	private Long eiaId;
	// 巡查任务ID
	@DatabaseField
	private Long pnitId;
	// 区域编号
	@DatabaseField
	private String areaNum;
	// 区域名称
	@DatabaseField
	private String areaName;
	// 详细位置
	@DatabaseField
	private String addr;
	// 施工单位
	@DatabaseField
	private String cunit;
	// 工地负责人
	@DatabaseField
	private String sitePerson;
	// 施工方电话
	@DatabaseField
	private String tel;
	// 重要级别
	@DatabaseField
	private String areaLevel;
	// OBJECTID
	@DatabaseField
	private Long objectid;
	// 重点区域坐标
	@DatabaseField
	private String eiaCoordinate;
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
	
	public Long getEiaId() {
		return eiaId;
	}
	public void setEiaId(Long eiaId) {
		this.eiaId = eiaId;
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
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getCunit() {
		return cunit;
	}
	public void setCunit(String cunit) {
		this.cunit = cunit;
	}
	public String getSitePerson() {
		return sitePerson;
	}
	public void setSitePerson(String sitePerson) {
		this.sitePerson = sitePerson;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAreaLevel() {
		return areaLevel;
	}
	public void setAreaLevel(String areaLevel) {
		this.areaLevel = areaLevel;
	}
	public Long getObjectid() {
		return objectid;
	}
	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}
	public String getEiaCoordinate() {
		return eiaCoordinate;
	}
	public void setEiaCoordinate(String eiaCoordinate) {
		this.eiaCoordinate = eiaCoordinate;
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
		return MapUtil.wkt2Geometry(eiaCoordinate);
	}
}
