package com.movementinsome.database.vo;

public class SiteSituationVO {

	// 任务编号
	private String taskNumber;

	// 现场情况
	private String siteSituation;

	// 影响情况
	private String affectSituation;
	
	// 处理备注
	private String remarks;
	
	// 处理结果
	private String result;
	
	// 完工时间
	private String commitTime;

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getSiteSituation() {
		return siteSituation;
	}

	public void setSiteSituation(String siteSituation) {
		this.siteSituation = siteSituation;
	}

	public String getAffectSituation() {
		return affectSituation;
	}

	public void setAffectSituation(String affectSituation) {
		this.affectSituation = affectSituation;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCommitTime() {
		return commitTime;
	}

	public void setCommitTime(String commitTime) {
		this.commitTime = commitTime;
	}

}
