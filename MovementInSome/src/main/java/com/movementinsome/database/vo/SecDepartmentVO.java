package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "SecDepartment")
public class SecDepartmentVO {

	// 部门ID
	@DatabaseField(id = true)
	private Long dtId;
	// 上级部门ID
	@DatabaseField
	private Long dtParentid;
	// 部门名称
	@DatabaseField
	private String dtName;
	// 部门编号
	@DatabaseField
	private String dtNum;
	// 是否有下级部门 0=没有， 1以上就是下级部门数量
	@DatabaseField
	private Long isChildren;
	// 平级部门序号
	@DatabaseField
	private Long dtOrder;
	
	public SecDepartmentVO(){
		;
	}

	public Long getDtId() {
		return dtId;
	}

	public void setDtId(Long dtId) {
		this.dtId = dtId;
	}

	public Long getDtParentid() {
		return dtParentid;
	}

	public void setDtParentid(Long dtParentid) {
		this.dtParentid = dtParentid;
	}

	public String getDtName() {
		return dtName;
	}

	public void setDtName(String dtName) {
		this.dtName = dtName;
	}

	public String getDtNum() {
		return dtNum;
	}

	public void setDtNum(String dtNum) {
		this.dtNum = dtNum;
	}

	public Long getIsChildren() {
		return isChildren;
	}

	public void setIsChildren(Long isChildren) {
		this.isChildren = isChildren;
	}

	public Long getDtOrder() {
		return dtOrder;
	}

	public void setDtOrder(Long dtOrder) {
		this.dtOrder = dtOrder;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return dtId == null ? "<Null>" : dtId.toString();
	}
}
