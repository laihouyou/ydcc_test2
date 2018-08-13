package com.movementinsome.database.vo;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;



@JsonIgnoreProperties(ignoreUnknown = true) 
public class InsTableConstructionRegisteredVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8208772209282818795L;
	/**
	 * 施工上报登记表
	 */
	// 施工编号
	private String constructionNum;
	// 施工单位
	private String constructionUnit;
	// 施工方电话
	private String constructionPhone;
	// 详细位置
	private String constructionAddr;
	// 所在道路
	private String constructionWay;
	// 施工类别
	private String constructionCategory;
	// 管径大小
	private String caliber;
	// 反映人
	private String reportPerson;
	// 联系电话
	private String phone;
	// 上报日期
	private String reportToDateStr;
	// 图形
	private GeometryVO geometry;

	public String getConstructionNum() {
		return constructionNum;
	}

	public void setConstructionNum(String constructionNum) {
		this.constructionNum = constructionNum;
	}

	public String getConstructionUnit() {
		return constructionUnit;
	}

	public void setConstructionUnit(String constructionUnit) {
		this.constructionUnit = constructionUnit;
	}

	public String getConstructionPhone() {
		return constructionPhone;
	}

	public void setConstructionPhone(String constructionPhone) {
		this.constructionPhone = constructionPhone;
	}

	public String getConstructionAddr() {
		return constructionAddr;
	}

	public void setConstructionAddr(String constructionAddr) {
		this.constructionAddr = constructionAddr;
	}

	public String getConstructionWay() {
		return constructionWay;
	}

	public void setConstructionWay(String constructionWay) {
		this.constructionWay = constructionWay;
	}

	public String getConstructionCategory() {
		return constructionCategory;
	}

	public void setConstructionCategory(String constructionCategory) {
		this.constructionCategory = constructionCategory;
	}

	public String getCaliber() {
		return caliber;
	}

	public void setCaliber(String caliber) {
		this.caliber = caliber;
	}

	public String getReportPerson() {
		return reportPerson;
	}

	public void setReportPerson(String reportPerson) {
		this.reportPerson = reportPerson;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getReportToDateStr() {
		return reportToDateStr;
	}

	public void setReportToDateStr(String reportToDateStr) {
		this.reportToDateStr = reportToDateStr;
	}

	public GeometryVO getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryVO geometry) {
		this.geometry = geometry;
	}
}
