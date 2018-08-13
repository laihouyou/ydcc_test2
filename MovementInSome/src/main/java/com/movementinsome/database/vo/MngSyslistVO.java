package com.movementinsome.database.vo;



import java.io.Serializable;

public class MngSyslistVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -422696741442714845L;
	private Long id;
	// 表名
	private String tablename;
	// 字段名
	private String fieldname;
	// 显示的下拉值
	private String disvalue;
	private Long idx;
	// 存入数据库的下拉值
	private String listvalue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getDisvalue() {
		return disvalue;
	}

	public void setDisvalue(String disvalue) {
		this.disvalue = disvalue;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public String getListvalue() {
		return listvalue;
	}

	public void setListvalue(String listvalue) {
		this.listvalue = listvalue;
	}

}
