package com.movementinsome.database.vo;


public class AffectedUserVO {
	// 影响用户ID
	private Long auId;
	// 序号
	private Long ord;
	// 水表注册号
	private String registrationNumber;
	// 用户名称
	private String userName;
	// 电话
	private String phone;
	// 所属小区
	private String district;
	// 地址
	private String addr;
	// GID
	private Long gid;
	// 图形
	private GeometryVO shape;

	public Long getAuId() {
		return auId;
	}

	public void setAuId(Long auId) {
		this.auId = auId;
	}

	public Long getOrd() {
		return ord;
	}

	public void setOrd(Long ord) {
		this.ord = ord;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public Long getGid() {
		return gid;
	}

	public void setGid(Long gid) {
		this.gid = gid;
	}

	public GeometryVO getShape() {
		return shape;
	}

	public void setShape(GeometryVO shape) {
		this.shape = shape;
	}

}
