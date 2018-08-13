package com.movementinsome.database.vo;



public class MngUsertableVO {

	private Long id;
	// 表名
	private String tablename;
	// 中文名
	private String tablecn;
	// 字段名
	private String fieldname;
	// 字段中文名
	private String fieldcn;
	// 字段类型 I(数字)/C(字符串)/T(时间)/F(浮点)
	private String fieldtyp;
	// 字段长度
	private Double fieldlen;
	// 可以查询字段 1(可以)/0(不可以)
	private Long queryyn;
	// 可以编辑字段 1(可以)/0(不可以)
	private Long edityn;
	private Long lookup;
	private String fmemo;
	private Long idx;
	private Long statusyn;
	private Long groupyn;
	// 查询结果显示字段 1(显示)/0(不显示)
	private Long querydis;
	private String isseq;

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

	public String getTablecn() {
		return tablecn;
	}

	public void setTablecn(String tablecn) {
		this.tablecn = tablecn;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getFieldcn() {
		return fieldcn;
	}

	public void setFieldcn(String fieldcn) {
		this.fieldcn = fieldcn;
	}

	public String getFieldtyp() {
		return fieldtyp;
	}

	public void setFieldtyp(String fieldtyp) {
		this.fieldtyp = fieldtyp;
	}

	public Double getFieldlen() {
		return fieldlen;
	}

	public void setFieldlen(Double fieldlen) {
		this.fieldlen = fieldlen;
	}

	public Long getQueryyn() {
		return queryyn;
	}

	public void setQueryyn(Long queryyn) {
		this.queryyn = queryyn;
	}

	public Long getEdityn() {
		return edityn;
	}

	public void setEdityn(Long edityn) {
		this.edityn = edityn;
	}

	public Long getLookup() {
		return lookup;
	}

	public void setLookup(Long lookup) {
		this.lookup = lookup;
	}

	public String getFmemo() {
		return fmemo;
	}

	public void setFmemo(String fmemo) {
		this.fmemo = fmemo;
	}

	public Long getIdx() {
		return idx;
	}

	public void setIdx(Long idx) {
		this.idx = idx;
	}

	public Long getStatusyn() {
		return statusyn;
	}

	public void setStatusyn(Long statusyn) {
		this.statusyn = statusyn;
	}

	public Long getGroupyn() {
		return groupyn;
	}

	public void setGroupyn(Long groupyn) {
		this.groupyn = groupyn;
	}

	public Long getQuerydis() {
		return querydis;
	}

	public void setQuerydis(Long querydis) {
		this.querydis = querydis;
	}

	public String getIsseq() {
		return isseq;
	}

	public void setIsseq(String isseq) {
		this.isseq = isseq;
	}

}
