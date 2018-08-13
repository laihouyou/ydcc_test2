package com.movementinsome.database.vo;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * BsInsTypeSetting entity. @author MyEclipse Persistence Tools
 */
@DatabaseTable(tableName = "BsInsTypeSettingVO")
public class BsInsTypeSettingVO implements java.io.Serializable {

	private static final long serialVersionUID = -8631735980682635649L;
	
	@DatabaseField(generatedId = true)
	private Long bsItsId;
//	巡检类型编号
	@DatabaseField
	private String bsItsNum;
//	巡检类型名称
	@DatabaseField
	private String bsItsName;
//	巡检项目类型
	@DatabaseField
	private String bsItsItemName;
//  巡检内容
	private List<BsInsContentSettingVO> bsInsContentSettings;

	// Property accessors
	public String getBsItsNum() {
		return this.bsItsNum;
	}

	public void setBsItsNum(String bsItsNum) {
		this.bsItsNum = bsItsNum;
	}

	public String getBsItsName() {
		return this.bsItsName;
	}

	public void setBsItsName(String bsItsName) {
		this.bsItsName = bsItsName;
	}

	public String getBsItsItemName() {
		return this.bsItsItemName;
	}

	public void setBsItsItemName(String bsItsItemName) {
		this.bsItsItemName = bsItsItemName;
	}

	public List<BsInsContentSettingVO> getBsInsContentSettings() {
		return bsInsContentSettings;
	}

	public void setBsInsContentSettings(
			List<BsInsContentSettingVO> bsInsContentSettings) {
		this.bsInsContentSettings = bsInsContentSettings;
	}

	public Long getBsItsId() {
		return bsItsId;
	}

	public void setBsItsId(Long bsItsId) {
		this.bsItsId = bsItsId;
	}
}