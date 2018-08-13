package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BasTBaseMaterial")
public class BasTBaseMaterialVO {
	/**
	 * 
	 */
	// 材料id
	@DatabaseField(id = true)
	private Long maId;
	// 材料类型
	@DatabaseField
	private String materialType;
	// 材料编号
	@DatabaseField
	private String materialNum;
	// 材料名称
	@DatabaseField
	private String materialName;

	public BasTBaseMaterialVO(){
		;
	}
	

	public Long getMaId() {
		return maId;
	}


	public void setMaId(Long maId) {
		this.maId = maId;
	}


	public String getMaterialType() {
		return materialType;
	}

	public void setMaterialType(String materialType) {
		this.materialType = materialType;
	}

	public String getMaterialNum() {
		return materialNum;
	}

	public void setMaterialNum(String materialNum) {
		this.materialNum = materialNum;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

}
