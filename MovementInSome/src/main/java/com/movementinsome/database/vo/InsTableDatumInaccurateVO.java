package com.movementinsome.database.vo;



import java.util.List;


public class InsTableDatumInaccurateVO {

	/**
	 * 图与现实不符
	 */
	// 移动端唯一编码
	private String moiNum;
	// 处理工作组
	private String handlerGroup;
	// 问题类型
	private String problemType;
	// 问题描述
	private String problemDesc;
	// 反映时间
	private String reportedDateStr;
	// 巡线员
	private String patrolPerson;
	// 反映人
	private String reportedPerson;
	// 联系电话
	private String phone;
	// 图形
	private GeometryVO geometry;
	// 新图形
	private GeometryVO newGeometry;
	// 设备类型
	private String deviceType;
	// 设施编号
	private String facilitiesNum;
	// 设施口径
	private Long facilitiesCaliber;
	// 设施材质
	private String facilitiesMaterial;
	// GID
	private String gid;
	// 管理单位
	private String managedept;
	// 备注
	private String remarks;
	// 状态
	private String state;
	// 图片组
	private List<PhotoInfoVO> imageBaseCode;

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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getProblemType() {
		return problemType;
	}

	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getProblemDesc() {
		return problemDesc;
	}

	public void setProblemDesc(String problemDesc) {
		this.problemDesc = problemDesc;
	}

	public String getReportedDateStr() {
		return reportedDateStr;
	}

	public void setReportedDateStr(String reportedDateStr) {
		this.reportedDateStr = reportedDateStr;
	}

	public String getPatrolPerson() {
		return patrolPerson;
	}

	public void setPatrolPerson(String patrolPerson) {
		this.patrolPerson = patrolPerson;
	}

	public String getReportedPerson() {
		return reportedPerson;
	}

	public void setReportedPerson(String reportedPerson) {
		this.reportedPerson = reportedPerson;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public GeometryVO getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryVO geometry) {
		this.geometry = geometry;
	}

	public GeometryVO getNewGeometry() {
		return newGeometry;
	}

	public void setNewGeometry(GeometryVO newGeometry) {
		this.newGeometry = newGeometry;
	}

	public List<PhotoInfoVO> getImageBaseCode() {
		return imageBaseCode;
	}

	public void setImageBaseCode(List<PhotoInfoVO> imageBaseCode) {
		this.imageBaseCode = imageBaseCode;
	}

	public String getManagedept() {
		return managedept;
	}

	public void setManagedept(String managedept) {
		this.managedept = managedept;
	}

	public Long getFacilitiesCaliber() {
		return facilitiesCaliber;
	}

	public void setFacilitiesCaliber(Long facilitiesCaliber) {
		this.facilitiesCaliber = facilitiesCaliber;
	}

	public String getFacilitiesMaterial() {
		return facilitiesMaterial;
	}

	public void setFacilitiesMaterial(String facilitiesMaterial) {
		this.facilitiesMaterial = facilitiesMaterial;
	}

}
