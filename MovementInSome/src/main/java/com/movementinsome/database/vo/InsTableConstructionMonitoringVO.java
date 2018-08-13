package com.movementinsome.database.vo;



import org.codehaus.jackson.annotate.JsonIgnoreProperties;



@JsonIgnoreProperties(ignoreUnknown = true) 
public class InsTableConstructionMonitoringVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2267328653016919081L;
	/**
	 * 施工日常监护表
	 */
	// 施工上报登记表ID
	private Long crId;
	// 施工编号
	private String constructionNum;
	// 日期
	private String cmDateStr;
	// 班组
	private String cmGroup;
	// 反映人
	private String reportedPerson;
	// 是否隐患
	private Long isHazards;
	// 隐患描述
	private String hazardsDesc;
	// 紧急程度
	private String workOrder;
	// 外露管线
	private String showLine;
	// 施工情况
	private String constructionCondition;
	// 备 注
	private String remarks;

	public Long getCrId() {
		return crId;
	}

	public void setCrId(Long crId) {
		this.crId = crId;
	}

	public String getConstructionNum() {
		return constructionNum;
	}

	public void setConstructionNum(String constructionNum) {
		this.constructionNum = constructionNum;
	}

	public String getCmDateStr() {
		return cmDateStr;
	}

	public void setCmDateStr(String cmDateStr) {
		this.cmDateStr = cmDateStr;
	}

	public String getCmGroup() {
		return cmGroup;
	}

	public void setCmGroup(String cmGroup) {
		this.cmGroup = cmGroup;
	}

	public String getReportedPerson() {
		return reportedPerson;
	}

	public void setReportedPerson(String reportedPerson) {
		this.reportedPerson = reportedPerson;
	}

	public Long getIsHazards() {
		return isHazards;
	}

	public void setIsHazards(Long isHazards) {
		this.isHazards = isHazards;
	}

	public String getHazardsDesc() {
		return hazardsDesc;
	}

	public void setHazardsDesc(String hazardsDesc) {
		this.hazardsDesc = hazardsDesc;
	}

	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}

	public String getShowLine() {
		return showLine;
	}

	public void setShowLine(String showLine) {
		this.showLine = showLine;
	}

	public String getConstructionCondition() {
		return constructionCondition;
	}

	public void setConstructionCondition(String constructionCondition) {
		this.constructionCondition = constructionCondition;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
