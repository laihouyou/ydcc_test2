package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "Tpconfig")
public class TpconfigVO  {

	private static final long serialVersionUID = -7943851186771393203L;
	
	@DatabaseField(generatedId = true)
	private Long id;
	
	/**
	 * 下拉项类型名称
	 */
	@DatabaseField
	private String name;
	/**
	 * 显示的下拉项值
	 */
	@DatabaseField
	private String pvalue;
	/**
	 * 下拉默认项
	 */
	@DatabaseField
	private Long isdef;
	/**
	 * 下拉项保存到数据库的下拉值
	 */
	@DatabaseField
	private String pvaluenum;
	/**
	 * 序号
	 */
	@DatabaseField
	private Long ordnum;
	/**
	 * 上级的下拉项保存到数据库的下拉值
	 */
	@DatabaseField
	private String frontOption;
	/**
	 * 上级的下拉项类型名称
	 */
	@DatabaseField
	private String frontName;
	
	/**
	 * 下拉选项域名
	 */
	@DatabaseField
	private String tableName;
	/**
	 * 说明
	 */
	@DatabaseField
	private String memo;

	
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public TpconfigVO(){
		;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPvalue() {
		return pvalue;
	}

	public void setPvalue(String pvalue) {
		this.pvalue = pvalue;
	}

	public Long getIsdef() {
		return isdef;
	}

	public void setIsdef(Long isdef) {
		this.isdef = isdef;
	}

	public String getPvaluenum() {
		return pvaluenum;
	}

	public void setPvaluenum(String pvaluenum) {
		this.pvaluenum = pvaluenum;
	}

	public Long getOrdnum() {
		return ordnum;
	}

	public void setOrdnum(Long ordnum) {
		this.ordnum = ordnum;
	}

	public String getFrontOption() {
		return frontOption;
	}

	public void setFrontOption(String frontOption) {
		this.frontOption = frontOption;
	}

	public String getFrontName() {
		return frontName;
	}

	public void setFrontName(String frontName) {
		this.frontName = frontName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

}
