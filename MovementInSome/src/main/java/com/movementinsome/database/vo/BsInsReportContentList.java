package com.movementinsome.database.vo;


/**
 * BsInsReportContentList entity. @author MyEclipse Persistence Tools
 *  巡查上报内容列表
 */

public class BsInsReportContentList implements java.io.Serializable {

//	任务编号
	private String bsTaskNum;
//	巡检内容编号
	private String bsIcsNum;
//	巡检内容
	private String bsIcsName;
//	检查结果
	private Long bsCheck;

	
	public String getBsTaskNum() {
		return this.bsTaskNum;
	}

	public void setBsTaskNum(String bsTaskNum) {
		this.bsTaskNum = bsTaskNum;
	}

	public String getBsIcsNum() {
		return this.bsIcsNum;
	}

	public void setBsIcsNum(String bsIcsNum) {
		this.bsIcsNum = bsIcsNum;
	}

	public String getBsIcsName() {
		return this.bsIcsName;
	}

	public void setBsIcsName(String bsIcsName) {
		this.bsIcsName = bsIcsName;
	}

	public Long getBsCheck() {
		return bsCheck;
	}

	public void setBsCheck(Long bsCheck) {
		this.bsCheck = bsCheck;
	}
	

}