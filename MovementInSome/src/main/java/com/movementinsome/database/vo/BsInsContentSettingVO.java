package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * BsInsContentSetting entity. @author MyEclipse Persistence Tools
 */
@DatabaseTable(tableName = "BsInsContentSettingVO")
public class BsInsContentSettingVO implements java.io.Serializable {

	private static final long serialVersionUID = -3025287951593349159L;
	@DatabaseField(generatedId = true)
	private Long bsIcsId;
//	巡检类型编号
	@DatabaseField
	private String bsItsNum;
//	巡检内容编号
	@DatabaseField
	private String bsIcsNum;
//	巡检内容
	@DatabaseField
	private String bsIcsName;
//	一级巡检 0,1
	@DatabaseField
	private Long bsIcsLevel1;
//	二级巡检
	@DatabaseField
	private Long bsIcsLevel2;
//	三级巡检
	@DatabaseField
	private Long bsIcsLevel3;
	//序号
	@DatabaseField
	private Long idx;
	//类型
	@DatabaseField
	private String bsIcsType;
	//值
	@DatabaseField
	private String bsIcsValue;

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public String getBsIcsType() {
		return bsIcsType;
	}

	public void setBsIcsType(String bsIcsType) {
		this.bsIcsType = bsIcsType;
	}

	public String getBsIcsValue() {
		return bsIcsValue;
	}

	public void setBsIcsValue(String bsIcsValue) {
		this.bsIcsValue = bsIcsValue;
	}

	public String getBsItsNum() {
		return this.bsItsNum;
	}

	public void setBsItsNum(String bsItsNum) {
		this.bsItsNum = bsItsNum;
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

	public Long getBsIcsLevel1() {
		return this.bsIcsLevel1;
	}

	public void setBsIcsLevel1(Long bsIcsLevel1) {
		this.bsIcsLevel1 = bsIcsLevel1;
	}

	public Long getBsIcsLevel2() {
		return this.bsIcsLevel2;
	}

	public void setBsIcsLevel2(Long bsIcsLevel2) {
		this.bsIcsLevel2 = bsIcsLevel2;
	}

	public Long getBsIcsLevel3() {
		return this.bsIcsLevel3;
	}

	public void setBsIcsLevel3(Long bsIcsLevel3) {
		this.bsIcsLevel3 = bsIcsLevel3;
	}

	public Long getBsIcsId() {
		return bsIcsId;
	}

	public void setBsIcsId(Long bsIcsId) {
		this.bsIcsId = bsIcsId;
	}
	
}