package com.movementinsome.database.vo;


import java.util.List;

/**
 * 漏点上报
 * 
 * @author gddst
 * 
 */
public class InsTableLDReportVO {

	// 移动端唯一编码
	private String moiNum;
	// 流水号
	private String serialNumber;
	// 处理工作组
	private String handlerGroup;
	// 巡线员
	private String patrolPerson;
	// 反映人
	private String ldTvUser;
	// 反映单位
	private String ldTvUserClass;
	// 反映时间
	private String ldTvReporttimeStr;
	// 问题描述
	private String probleDesc;
	// 具体漏水地点
	private String ldTvAddress;
	// 备 注
	private String ldTvRemark;
	// 是否定位漏点
	private String strIsLd;
	// 紧急程度
	private String strUrgencyDegree;
	// 设施类型
	private String facilitiesType;
	// 设施编号
	private String facilitiesNum;
	// 管线口径
	private String ldTvCaliber;
	// 管线材质
	private String workFnortnlnMaterial;
	// GID
	private String gid;
	// 状态
	private String state;
	// 管理单位
	private String managedept;
	// 联系电话
	private String phone;
	// 图形
	private GeometryVO geometry;
	// 图片组
	private List<PhotoInfoVO> imageBaseCode;
	// 照片URL
	private List<String> photoURLList;

	public String getMoiNum() {
		return moiNum;
	}

	public void setMoiNum(String moiNum) {
		this.moiNum = moiNum;
	}

	public String getHandlerGroup() {
		return handlerGroup;
	}

	public void setHandlerGroup(String handlerGroup) {
		this.handlerGroup = handlerGroup;
	}

	public String getProbleDesc() {
		return probleDesc;
	}

	public void setProbleDesc(String probleDesc) {
		this.probleDesc = probleDesc;
	}

	public String getLdTvReporttimeStr() {
		return ldTvReporttimeStr;
	}

	public void setLdTvReporttimeStr(String ldTvReporttimeStr) {
		this.ldTvReporttimeStr = ldTvReporttimeStr;
	}

	public String getLdTvCaliber() {
		return ldTvCaliber;
	}

	public void setLdTvCaliber(String ldTvCaliber) {
		this.ldTvCaliber = ldTvCaliber;
	}

	public String getLdTvAddress() {
		return ldTvAddress;
	}

	public void setLdTvAddress(String ldTvAddress) {
		this.ldTvAddress = ldTvAddress;
	}

	public String getPatrolPerson() {
		return patrolPerson;
	}

	public void setPatrolPerson(String patrolPerson) {
		this.patrolPerson = patrolPerson;
	}

	public String getLdTvUser() {
		return ldTvUser;
	}

	public void setLdTvUser(String ldTvUser) {
		this.ldTvUser = ldTvUser;
	}

	public GeometryVO getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryVO geometry) {
		this.geometry = geometry;
	}

	public String getLdTvRemark() {
		return ldTvRemark;
	}

	public void setLdTvRemark(String ldTvRemark) {
		this.ldTvRemark = ldTvRemark;
	}

	public String getStrIsLd() {
		return strIsLd;
	}

	public void setStrIsLd(String strIsLd) {
		this.strIsLd = strIsLd;
	}

	public String getStrUrgencyDegree() {
		return strUrgencyDegree;
	}

	public void setStrUrgencyDegree(String strUrgencyDegree) {
		this.strUrgencyDegree = strUrgencyDegree;
	}

	public List<PhotoInfoVO> getImageBaseCode() {
		return imageBaseCode;
	}

	public void setImageBaseCode(List<PhotoInfoVO> imageBaseCode) {
		this.imageBaseCode = imageBaseCode;
	}

	public String getFacilitiesNum() {
		return facilitiesNum;
	}

	public void setFacilitiesNum(String facilitiesNum) {
		this.facilitiesNum = facilitiesNum;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getFacilitiesType() {
		return facilitiesType;
	}

	public void setFacilitiesType(String facilitiesType) {
		this.facilitiesType = facilitiesType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getManagedept() {
		return managedept;
	}

	public void setManagedept(String managedept) {
		this.managedept = managedept;
	}

	public String getWorkFnortnlnMaterial() {
		return workFnortnlnMaterial;
	}

	public void setWorkFnortnlnMaterial(String workFnortnlnMaterial) {
		this.workFnortnlnMaterial = workFnortnlnMaterial;
	}

	public String getLdTvUserClass() {
		return ldTvUserClass;
	}

	public void setLdTvUserClass(String ldTvUserClass) {
		this.ldTvUserClass = ldTvUserClass;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<String> getPhotoURLList() {
		return photoURLList;
	}

	public void setPhotoURLList(List<String> photoURLList) {
		this.photoURLList = photoURLList;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

}
