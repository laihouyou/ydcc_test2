package com.movementinsome.database.vo;

import com.esri.core.geometry.Geometry;
import com.movementinsome.map.utils.MapUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsPatrolAreaData")
public class InsPatrolAreaData implements java.io.Serializable {
	//ID
	@DatabaseField(id = true)
	private Long id;
	//编号
	@DatabaseField
	private String areaNum;
	// objectid
	@DatabaseField
	private Long areaId;
	//名称
	@DatabaseField
	private String areaName;
	//频率类型
	@DatabaseField
	private String frequencyType;
	//频率
	@DatabaseField
	private String frequency;
	//次数
	@DatabaseField
	private Long frequencyNumber;
	//频率描述
	@DatabaseField
	private String frequencyDesc;
	// 任务编号
	@DatabaseField
	private String workTaskNum;
	@DatabaseField
	private String geometryStr;

	//最后巡查日期:yyyy-MM-dd
	@DatabaseField
	private String lastInsDateStr;
	//巡查次数
	@DatabaseField
	private Long insCount;
	//巡查状态（start,finish,pause）
	@DatabaseField
	private String state;
	@DatabaseField
	private String title;
	// 本机任务分类
	@DatabaseField
	private String taskCategory;
	// 模板
	@DatabaseField
	private String androidForm;
	//区域顺序
	@DatabaseField
  	private Long areaOrder;

	public Long getAreaOrder() {
		return areaOrder;
	}

	public void setAreaOrder(Long areaOrder) {
		this.areaOrder = areaOrder;
	}

	public String getLastInsDateStr() {
		return lastInsDateStr;
	}

	public void setLastInsDateStr(String lastInsDateStr) {
		this.lastInsDateStr = lastInsDateStr;
	}

	public Long getInsCount() {
		return insCount;
	}

	public void setInsCount(Long insCount) {
		this.insCount = insCount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTaskCategory() {
		return taskCategory;
	}

	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}

	public String getAndroidForm() {
		return androidForm;
	}

	public void setAndroidForm(String androidForm) {
		this.androidForm = androidForm;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getFrequencyNumber() {
		return frequencyNumber;
	}

	public void setFrequencyNumber(Long frequencyNumber) {
		this.frequencyNumber = frequencyNumber;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public String getGeometryStr() {
		return geometryStr;
	}

	public void setGeometryStr(String geometryStr) {
		this.geometryStr = geometryStr;
	}


	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}
	public Geometry getShape(){
		return MapUtil.wkt2Geometry(geometryStr);
	}
	
}
