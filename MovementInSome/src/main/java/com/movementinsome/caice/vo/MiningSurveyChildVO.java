package com.movementinsome.caice.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MiningSurveyChildVO")
public class MiningSurveyChildVO  {

	// 子工程ID
	@DatabaseField(id = true)
	private String childProjectId;
	// 关联id
	@DatabaseField
	private String wpId;
	// 子工程编号
	@DatabaseField
	private String childProjectNum;
	// 子工程名称
	@DatabaseField
	private String childProjectName;
	// 子工程类型
	@DatabaseField
	private String childProjectType;
	// 子工程创建时间
	@DatabaseField
	private String childProjectSDateStr;
	// 子工程结束时间
	@DatabaseField
	private String childProjectEDateStr;
	// 子工程保存表单
	@DatabaseField
	private String childProjectSave;
	// 子工程坐标
	@DatabaseField
	private String Point;
	// 子工程内容
	@DatabaseField
	private String projectContext;
	// 是否提交
	@DatabaseField
	private String projectSubmit;
	
	public String getChildProjectId() {
		return childProjectId;
	}
	public void setChildProjectId(String childProjectId) {
		this.childProjectId = childProjectId;
	}
	public String getWpId() {
		return wpId;
	}
	public void setWpId(String wpId) {
		this.wpId = wpId;
	}
	public String getChildProjectNum() {
		return childProjectNum;
	}
	public void setChildProjectNum(String childProjectNum) {
		this.childProjectNum = childProjectNum;
	}
	public String getChildProjectName() {
		return childProjectName;
	}
	public void setChildProjectName(String childProjectName) {
		this.childProjectName = childProjectName;
	}
	public String getChildProjectType() {
		return childProjectType;
	}
	public void setChildProjectType(String childProjectType) {
		this.childProjectType = childProjectType;
	}
	public String getChildProjectSDateStr() {
		return childProjectSDateStr;
	}
	public void setChildProjectSDateStr(String childProjectSDateStr) {
		this.childProjectSDateStr = childProjectSDateStr;
	}
	public String getChildProjectEDateStr() {
		return childProjectEDateStr;
	}
	public void setChildProjectEDateStr(String childProjectEDateStr) {
		this.childProjectEDateStr = childProjectEDateStr;
	}
	public String getChildProjectSave() {
		return childProjectSave;
	}
	public void setChildProjectSave(String childProjectSave) {
		this.childProjectSave = childProjectSave;
	}
	public String getPoint() {
		return Point;
	}
	public void setPoint(String point) {
		Point = point;
	}
	public String getProjectContext() {
		return projectContext;
	}
	public void setProjectContext(String projectContext) {
		this.projectContext = projectContext;
	}
	public String getProjectSubmit() {
		return projectSubmit;
	}
	public void setProjectSubmit(String projectSubmit) {
		this.projectSubmit = projectSubmit;
	}
	
	
}
