package com.movementinsome.database.vo;

import java.util.List;

public class PlanOffWaterVO {
	// 计划停水申请ID
	private String owsaId;
	// 方案ID
	private String brpId;
	// 任务编号
	private String workTaskNum;
	// 经办人
	private String handler;
	// 施工地点
	private String constructionAddr;
	// 施工队长
	private String constructionPerson;
	// 工程名称
	private String projectName;
	// 联系电话
	private String phone;
	// 停水原因
	private String stoppageCauses;
	// 申请单位
	private String applyingUnit;
	// 申报日期
	private String reportingDate;
	// 计划停水时间
	private String planStagnantWaterDate;
	// 计划复水时间
	private String planRecoveryWaterDate;
	// 影响用户数
	private String affectUsers;
	// 爆漏点个数
	private String pointCount;
	// 影响区域
	private GeometryVO brpArea;
	// 备注
	private String remarks;
	// 重点单位
	private String keyUnits;
	// 开关阀门
	private List<OffValveVO> offValveList;
	// 漏水点
	private List<BurstPipesPointVO> bpPointList;
	// 影响用户
	private List<AffectedUserVO> affUserList;
	// 管网管线信息
	private List<PipelineVO> pipelineList;
	// 照片URL
	private List<String> photoURLList;
	//阀门总数
	private Long valveSum;

	
	public Long getValveSum() {
		return valveSum;
	}

	public void setValveSum(Long valveSum) {
		this.valveSum = valveSum;
	}

	public String getOwsaId() {
		return owsaId;
	}

	public void setOwsaId(String owsaId) {
		this.owsaId = owsaId;
	}

	public String getBrpId() {
		return brpId;
	}

	public void setBrpId(String brpId) {
		this.brpId = brpId;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public String getConstructionAddr() {
		return constructionAddr;
	}

	public void setConstructionAddr(String constructionAddr) {
		this.constructionAddr = constructionAddr;
	}

	public String getConstructionPerson() {
		return constructionPerson;
	}

	public void setConstructionPerson(String constructionPerson) {
		this.constructionPerson = constructionPerson;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStoppageCauses() {
		return stoppageCauses;
	}

	public void setStoppageCauses(String stoppageCauses) {
		this.stoppageCauses = stoppageCauses;
	}

	public String getApplyingUnit() {
		return applyingUnit;
	}

	public void setApplyingUnit(String applyingUnit) {
		this.applyingUnit = applyingUnit;
	}

	public String getReportingDate() {
		return reportingDate;
	}

	public void setReportingDate(String reportingDate) {
		this.reportingDate = reportingDate;
	}

	public String getPlanStagnantWaterDate() {
		return planStagnantWaterDate;
	}

	public void setPlanStagnantWaterDate(String planStagnantWaterDate) {
		this.planStagnantWaterDate = planStagnantWaterDate;
	}

	public String getPlanRecoveryWaterDate() {
		return planRecoveryWaterDate;
	}

	public void setPlanRecoveryWaterDate(String planRecoveryWaterDate) {
		this.planRecoveryWaterDate = planRecoveryWaterDate;
	}

	public String getAffectUsers() {
		return affectUsers;
	}

	public void setAffectUsers(String affectUsers) {
		this.affectUsers = affectUsers;
	}

	public String getPointCount() {
		return pointCount;
	}

	public void setPointCount(String pointCount) {
		this.pointCount = pointCount;
	}

	public GeometryVO getBrpArea() {
		return brpArea;
	}

	public void setBrpArea(GeometryVO brpArea) {
		this.brpArea = brpArea;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getKeyUnits() {
		return keyUnits;
	}

	public void setKeyUnits(String keyUnits) {
		this.keyUnits = keyUnits;
	}

	public List<OffValveVO> getOffValveList() {
		return offValveList;
	}

	public void setOffValveList(List<OffValveVO> offValveList) {
		this.offValveList = offValveList;
	}

	public List<BurstPipesPointVO> getBpPointList() {
		return bpPointList;
	}

	public void setBpPointList(List<BurstPipesPointVO> bpPointList) {
		this.bpPointList = bpPointList;
	}

	public List<AffectedUserVO> getAffUserList() {
		return affUserList;
	}

	public void setAffUserList(List<AffectedUserVO> affUserList) {
		this.affUserList = affUserList;
	}

	public List<PipelineVO> getPipelineList() {
		return pipelineList;
	}

	public void setPipelineList(List<PipelineVO> pipelineList) {
		this.pipelineList = pipelineList;
	}

	public List<String> getPhotoURLList() {
		return photoURLList;
	}

	public void setPhotoURLList(List<String> photoURLList) {
		this.photoURLList = photoURLList;
	}


}
