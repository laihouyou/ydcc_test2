package com.movementinsome.database.vo;



import java.util.List;


public class InsTableFacilityProblemVO {

	/**
	 * 上传管网设施问题上报单
	 */
	// 移动端唯一编码
	private String moiNum;
	// 流水号
	private String serialNumber;
	// 处理工作组
	private String handlerGroup;
	// 设施类型
	private String facilitiesType;
	// 反映类别
	private String reportedType;
	// 发生地址
	private String addrss;
	// 紧急程度
	private String workOrder;
	// 巡线员
	private String patrolPerson;
	// 联系方式
	private String phone;
	// 反映内容
	private String reportedContent;
	// 反映时间
	private String reportedDateStr;
	// 水表编号
	private String waterMeterNum;
	// 水表位置
	private GeometryVO waterMeterPoint;
	// 反映人
	private String reportedPerson;
	// 设施编号
	private String facilitiesNum;
	// 设施口径
	private Long facilitiesCaliber;
	// 设施材质
	private String facilitiesMaterial;
	// 备注
	private String remarks;
	// GID
	private String gid;
	// 状态
	private String state;
	// 管理单位
	private String managedept;
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

	public String getAddrss() {
		return addrss;
	}

	public void setAddrss(String addrss) {
		this.addrss = addrss;
	}

	public String getWorkOrder() {
		return workOrder;
	}

	public void setWorkOrder(String workOrder) {
		this.workOrder = workOrder;
	}

	public String getPatrolPerson() {
		return patrolPerson;
	}

	public void setPatrolPerson(String patrolPerson) {
		this.patrolPerson = patrolPerson;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFacilitiesType() {
		return facilitiesType;
	}

	public void setFacilitiesType(String facilitiesType) {
		this.facilitiesType = facilitiesType;
	}

	public String getReportedDateStr() {
		return reportedDateStr;
	}

	public void setReportedDateStr(String reportedDateStr) {
		this.reportedDateStr = reportedDateStr;
	}

	public String getReportedType() {
		return reportedType;
	}

	public void setReportedType(String reportedType) {
		this.reportedType = reportedType;
	}

	public String getReportedContent() {
		return reportedContent;
	}

	public void setReportedContent(String reportedContent) {
		this.reportedContent = reportedContent;
	}

	public String getFacilitiesNum() {
		return facilitiesNum;
	}

	public void setFacilitiesNum(String facilitiesNum) {
		this.facilitiesNum = facilitiesNum;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public GeometryVO getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryVO geometry) {
		this.geometry = geometry;
	}

	public List<PhotoInfoVO> getImageBaseCode() {
		return imageBaseCode;
	}

	public void setImageBaseCode(List<PhotoInfoVO> imageBaseCode) {
		this.imageBaseCode = imageBaseCode;
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

	public String getHandlerGroup() {
		return handlerGroup;
	}

	public void setHandlerGroup(String handlerGroup) {
		this.handlerGroup = handlerGroup;
	}

	public String getWaterMeterNum() {
		return waterMeterNum;
	}

	public void setWaterMeterNum(String waterMeterNum) {
		this.waterMeterNum = waterMeterNum;
	}

	public GeometryVO getWaterMeterPoint() {
		return waterMeterPoint;
	}

	public void setWaterMeterPoint(GeometryVO waterMeterPoint) {
		this.waterMeterPoint = waterMeterPoint;
	}

	public String getReportedPerson() {
		return reportedPerson;
	}

	public void setReportedPerson(String reportedPerson) {
		this.reportedPerson = reportedPerson;
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
