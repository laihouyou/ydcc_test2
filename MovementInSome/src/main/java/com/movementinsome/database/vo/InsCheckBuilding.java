package com.movementinsome.database.vo;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsCheckBuilding")
public class InsCheckBuilding implements Serializable {

	//任务编号
	@DatabaseField
	private String workTaskNum;
	
	//片区id
	@DatabaseField
	private Long areaId;
	
	@DatabaseField
	private String roadName;
	
	@DatabaseField
	private String buildingType;
	
	@DatabaseField
	private String checkItemType;
	
	@DatabaseField
	private String checkItemContent;
	
	@DatabaseField
	private String remarks;
	
	@DatabaseField
	private String guid;
	
	@DatabaseField
	private String pipeId;
	
	@DatabaseField
	private String buildingCoordinate;
	

	/*public String getWorkTaskNum() {
		return workTaskNum;
	}
	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	public String getBuildingType() {
		return buildingType;
	}
	public void setBuildingType(String buildingType) {
		this.buildingType = buildingType;
	}
	public String getCheckItemType() {
		return checkItemType;
	}
	public void setCheckItemType(String checkItemType) {
		this.checkItemType = checkItemType;
	}
	public String getCheckItemContent() {
		return checkItemContent;
	}
	public void setCheckItemContent(String checkItemContent) {
		this.checkItemContent = checkItemContent;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getPipeId() {
		return pipeId;
	}
	public void setPipeId(String pipeId) {
		this.pipeId = pipeId;
	}
	public String getBuildingCoordinate() {
		return buildingCoordinate;
	}
	public void setBuildingCoordinate(String buildingCoordinate) {
		this.buildingCoordinate = buildingCoordinate;
	}*/
	
}
