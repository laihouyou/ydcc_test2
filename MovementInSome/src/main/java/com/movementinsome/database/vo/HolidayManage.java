package com.movementinsome.database.vo;

import java.io.Serializable;
import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "HolidayManage")
public class HolidayManage implements Serializable {

	@DatabaseField(id = true)
	private Long hmId;
	// 年份yyyy
	@DatabaseField
	private String hmYear;
	// 月份mm
	@DatabaseField
	private String hmMonth;
	// 日期dd
	@DatabaseField
	private String hmDay;
	// 工作日：1/非工作:0
	@DatabaseField
	private Long hmWorkState;
	// 说明
	@DatabaseField
	private String hmRead;

	private Date createDate;
	private Long createUId;
	private String createUNum;
	private String createUName;
	private Date lastUpdateDate;
	private Long lastUpdateUId;
	private String lastUpdateUNum;
	private String lastUpdateUName;

	public Long getHmId() {
		return hmId;
	}

	public void setHmId(Long hmId) {
		this.hmId = hmId;
	}

	public String getHmYear() {
		return hmYear;
	}

	public void setHmYear(String hmYear) {
		this.hmYear = hmYear;
	}

	public String getHmMonth() {
		return hmMonth;
	}

	public void setHmMonth(String hmMonth) {
		this.hmMonth = hmMonth;
	}

	public String getHmDay() {
		return hmDay;
	}

	public void setHmDay(String hmDay) {
		this.hmDay = hmDay;
	}

	public Long getHmWorkState() {
		return hmWorkState;
	}

	public void setHmWorkState(Long hmWorkState) {
		this.hmWorkState = hmWorkState;
	}

	public String getHmRead() {
		return hmRead;
	}

	public void setHmRead(String hmRead) {
		this.hmRead = hmRead;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUId() {
		return createUId;
	}

	public void setCreateUId(Long createUId) {
		this.createUId = createUId;
	}

	public String getCreateUNum() {
		return createUNum;
	}

	public void setCreateUNum(String createUNum) {
		this.createUNum = createUNum;
	}

	public String getCreateUName() {
		return createUName;
	}

	public void setCreateUName(String createUName) {
		this.createUName = createUName;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Long getLastUpdateUId() {
		return lastUpdateUId;
	}

	public void setLastUpdateUId(Long lastUpdateUId) {
		this.lastUpdateUId = lastUpdateUId;
	}

	public String getLastUpdateUNum() {
		return lastUpdateUNum;
	}

	public void setLastUpdateUNum(String lastUpdateUNum) {
		this.lastUpdateUNum = lastUpdateUNum;
	}

	public String getLastUpdateUName() {
		return lastUpdateUName;
	}

	public void setLastUpdateUName(String lastUpdateUName) {
		this.lastUpdateUName = lastUpdateUName;
	}
}
