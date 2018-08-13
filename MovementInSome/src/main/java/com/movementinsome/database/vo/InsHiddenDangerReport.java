package com.movementinsome.database.vo;


import java.util.Date;


public class InsHiddenDangerReport implements java.io.Serializable{

	
	private Long frId;//设施问题ID
	
	private String workTaskNum;//任务编号
	
	private String taskCategory;//任务类别
	
	private String sources;//信息来源
	
	private Long facilitiesCaliber;//设施口径
	
	private String facilitiesMaterial;//设施材质
	
	private String addr;//发生地址
	
	private String workOrder;//紧急程度
	
	private String patrolPerson;//巡线员
	
	private String phone;//联系方式
	
	private String facilitiesType;//设施类型
	
	private String reportedDate;//反映时间
	
	private String reportedType;//反映类别
	
	private String reportedContent;//反映内容（多选）
	
	private String facilitiesNum;//设施编号
	
	private String coordinate;//坐标
	
	private String remarks;//备注
	
	private String gid;//GID
	
	private Long status;//状态
	
	private String guid;//GUID
	
	private String reportedCoordinate;//上报坐标点
	
	private String serialNumber;//流水号
	
	private String manageUnit;//管理单位
	
	private String createDate;//创建日期
	
	private Long createUId;//创建人ID
	
	private String createUNum;//创建人编号
	
	private String createUName;//创建人名称
	
	private Date lastUpdateDate;//最后更新时间
	
	private Long lastUpdateUId;//最后更新人ID
	
	private String lastUpdateUNum;//最后更新人编号
	
	private String lastUpdateUName;//最后更新人名称
	
	private String moiNum;//移动端唯一编码
	
	private String phoneImei;//移动端机器码
	
	private String handlePeople;//处理人
	
	private Long handleId;//处理人ID
	
	private String handleNum;//处理人编号
	
	private String handlePhone;//处理人电话
	
	private String handleDepartment;//处理部门
	
	private String handleDate;//处理完成时间
	
	private String results;//处理结果
	
	private Long dispatchUId;//派遣人ID
	
	private String dispatchUNum;//派遣人编号
	
	private String dispatchUName;//派遣人名称
	
	private String dispatchDate;//派遣时间
	
	private String dispatchRemark;//派遣备注

	public Long getFrId() {
		return frId;
	}

	public void setFrId(Long frId) {
		this.frId = frId;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public String getTaskCategory() {
		return taskCategory;
	}

	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}

	public String getSources() {
		return sources;
	}

	public void setSources(String sources) {
		this.sources = sources;
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

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
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

	public String getReportedDate() {
		return reportedDate;
	}

	public void setReportedDate(String reportedDate) {
		this.reportedDate = reportedDate;
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

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
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

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getReportedCoordinate() {
		return reportedCoordinate;
	}

	public void setReportedCoordinate(String reportedCoordinate) {
		this.reportedCoordinate = reportedCoordinate;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getManageUnit() {
		return manageUnit;
	}

	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUId() {
		return createUId;
	}

	public void setCreateUId(Long createUId) {
		this.createUId = createUId;
	}

	public String getCreateUNum() {
		return createUNum;
	}

	public void setCreateUNum(String createUNum) {
		this.createUNum = createUNum;
	}

	public String getCreateUName() {
		return createUName;
	}

	public void setCreateUName(String createUName) {
		this.createUName = createUName;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Long getLastUpdateUId() {
		return lastUpdateUId;
	}

	public void setLastUpdateUId(Long lastUpdateUId) {
		this.lastUpdateUId = lastUpdateUId;
	}

	public String getLastUpdateUNum() {
		return lastUpdateUNum;
	}

	public void setLastUpdateUNum(String lastUpdateUNum) {
		this.lastUpdateUNum = lastUpdateUNum;
	}

	public String getLastUpdateUName() {
		return lastUpdateUName;
	}

	public void setLastUpdateUName(String lastUpdateUName) {
		this.lastUpdateUName = lastUpdateUName;
	}

	public String getMoiNum() {
		return moiNum;
	}

	public void setMoiNum(String moiNum) {
		this.moiNum = moiNum;
	}

	public String getPhoneImei() {
		return phoneImei;
	}

	public void setPhoneImei(String phoneImei) {
		this.phoneImei = phoneImei;
	}

	public String getHandlePeople() {
		return handlePeople;
	}

	public void setHandlePeople(String handlePeople) {
		this.handlePeople = handlePeople;
	}

	public Long getHandleId() {
		return handleId;
	}

	public void setHandleId(Long handleId) {
		this.handleId = handleId;
	}

	public String getHandleNum() {
		return handleNum;
	}

	public void setHandleNum(String handleNum) {
		this.handleNum = handleNum;
	}

	public String getHandlePhone() {
		return handlePhone;
	}

	public void setHandlePhone(String handlePhone) {
		this.handlePhone = handlePhone;
	}

	public String getHandleDepartment() {
		return handleDepartment;
	}

	public void setHandleDepartment(String handleDepartment) {
		this.handleDepartment = handleDepartment;
	}

	public String getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public Long getDispatchUId() {
		return dispatchUId;
	}

	public void setDispatchUId(Long dispatchUId) {
		this.dispatchUId = dispatchUId;
	}

	public String getDispatchUNum() {
		return dispatchUNum;
	}

	public void setDispatchUNum(String dispatchUNum) {
		this.dispatchUNum = dispatchUNum;
	}

	public String getDispatchUName() {
		return dispatchUName;
	}

	public void setDispatchUName(String dispatchUName) {
		this.dispatchUName = dispatchUName;
	}

	public String getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(String dispatchDate) {
		this.dispatchDate = dispatchDate;
	}

	public String getDispatchRemark() {
		return dispatchRemark;
	}

	public void setDispatchRemark(String dispatchRemark) {
		this.dispatchRemark = dispatchRemark;
	}
	
}