package com.movementinsome.database.vo;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BasTBaseMechanical")
public class BasTBaseMechanicalVO {

	@DatabaseField(id = true)
	private Long bmId;
	
	@DatabaseField
	private String bisMechanicalNum;
	
	@DatabaseField
	private String bisMechanicalName;
	
	@DatabaseField
	private Date bisBuyDate;

	public BasTBaseMechanicalVO() {
		;
	}

	public Long getBmId() {
		return bmId;
	}

	public void setBmId(Long bmId) {
		this.bmId = bmId;
	}

	public String getBisMechanicalNum() {
		return bisMechanicalNum;
	}

	public void setBisMechanicalNum(String bisMechanicalNum) {
		this.bisMechanicalNum = bisMechanicalNum;
	}

	public String getBisMechanicalName() {
		return bisMechanicalName;
	}

	public void setBisMechanicalName(String bisMechanicalName) {
		this.bisMechanicalName = bisMechanicalName;
	}

	public Date getBisBuyDate() {
		return bisBuyDate;
	}

	public void setBisBuyDate(Date bisBuyDate) {
		this.bisBuyDate = bisBuyDate;
	}

}
