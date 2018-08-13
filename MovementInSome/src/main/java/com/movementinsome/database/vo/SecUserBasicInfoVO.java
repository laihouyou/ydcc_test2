package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "SecUserBasicInfo")
public class SecUserBasicInfoVO {
	// 用户ID
	@DatabaseField(id = true)
	private Long usId;
	// 用户编号
	@DatabaseField
	private String usUsercode;
	// 用户名称
	@DatabaseField
	private String usNameZh;
	// 电话号码
	@DatabaseField
	private String usPhone;
	// EMAIL
	@DatabaseField
	private String usEmail;
	// 部门ID
	@DatabaseField
	private Long deptId;
	// 部门名称
	@DatabaseField
	private String deptName;
	// 部门编号
	@DatabaseField
	private String deptNum;
	// 班组ID
	@DatabaseField
	private Long teamId;
	// 班组编号
	@DatabaseField
	private String teamNum;
	// 班组名称
	@DatabaseField
	private String teamName;
	
	public SecUserBasicInfoVO(){
		;
	}
	
	public Long getTeamId() {
		return teamId;
	}
	public String getDeptNum() {
		return deptNum;
	}
	public void setDeptNum(String deptNum) {
		this.deptNum = deptNum;
	}


	public String getTeamNum() {
		return teamNum;
	}


	public void setTeamNum(String teamNum) {
		this.teamNum = teamNum;
	}


	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public Long getUsId() {
		return usId;
	}

	public void setUsId(Long usId) {
		this.usId = usId;
	}

	public String getUsUsercode() {
		return usUsercode;
	}

	public void setUsUsercode(String usUsercode) {
		this.usUsercode = usUsercode;
	}

	public String getUsNameZh() {
		return usNameZh;
	}

	public void setUsNameZh(String usNameZh) {
		this.usNameZh = usNameZh;
	}

	public String getUsPhone() {
		return usPhone;
	}

	public void setUsPhone(String usPhone) {
		this.usPhone = usPhone;
	}

	public String getUsEmail() {
		return usEmail;
	}

	public void setUsEmail(String usEmail) {
		this.usEmail = usEmail;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public String toString(){
		return usId == null ? "<Null>" : usId.toString();
	}
}
