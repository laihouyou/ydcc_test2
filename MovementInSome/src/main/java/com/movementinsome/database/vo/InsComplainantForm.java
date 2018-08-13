package com.movementinsome.database.vo;




/**
 * @author gddst
 *
 */
public class InsComplainantForm implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1287839351398480601L;
	// 投诉ID
	private Long cfId;
	// 任务编号
	private String workTaskNum;
	// 任务类别
	private String taskCategory;
	// 投诉编号
	private String cfNum;
	// 信息来源
	private String cfSource;
	// 投诉类型
	private String cfType;
	// 投诉时间
	private String cfDate;
	// 接待单位
	private String receptionUnit;
	// 接待员
	private String receptionist;
	// 投诉人
	private String cfPeople;
	// 电话
	private String cfPhone;
	// 用户编号
	private String cfUserNum;
	// 地点
	private String cfAddr;
	// 投诉范围
	private String cfRange;
	// 投诉内容
	private String cfContent;
	// 处理意见
	private String treatmentAdvice;
	// 开始处理人
	private String beginHandlePeople;
	// 开始处理时间
	private String beginHandleDate;
	// 结束处理人
	private String endHandlePeople;
	// 结束处理时间
	private String endHandleDate;
	// 处理人
	private String handlePeople;
	// 处理人ID
	private Long handleId;
	// 处理人编号
	private String handleNum;
	// 处理人电话
	private String handlePhone;
	// 处理部门
	private String handleDepartment;
	// 处理完成时间
	private String handleDate;
	// 工作日
	private String workday;
	// 处理结果
	private String results;
	// 用户回访登记人
	private String visitPeople;
	// 回访时间
	private String visitDate;
	// 员工着装
	private String staffDress;
	// 反应意见
	private String reactionComments;
	// 服务态度
	private String serviceAttitude;
	// 服务质量
	private String qos;
	// 满意度
	private String satisfaction;
	// 其它反馈意见
	private String otherFeedback;
	// 责任人
	private String responsible;
	// 考核记分
	private String assessmentScore;
	// 备注
	private String remarks;
	// GUID
	private String guid;
	// 上报坐标点
	private String reportedCoordinate;
	// 流水号
	private String serialNumber;
	// 管理单位
	private String manageUnit;
	// 创建日期
	private String createDate;
	// 创建人ID
	private Long createUId;
	// 创建人编号
	private String createUNum;
	// 创建人名称
	private String createUName;
	// 最后更新时间
	private String lastUpdateDate;
	// 最后更新人ID
	private Long lastUpdateUId;
	// 最后更新人编号
	private String lastUpdateUNum;
	// 最后更新人名称
	private String lastUpdateUName;
	// 移动端唯一编码
	private String moiNum;
	// 移动端机器码
	private String phoneImei;
	// 状态
	private Long state;
	// 接单时间
	private String receiveDate;
	// 出发时间
	private String departureDate;
	// 现场情况
	private String siteSituation;
	// 派遣人ID
	private Long dispatchUId;
	// 派遣人编号
	private String dispatchUNum;
	// 派遣人名称
	private String dispatchUName;
	// 派遣时间
	private String dispatchDate;
	// 派遣备注
	private String dispatchRemark;

	public Long getCfId() {
		return cfId;
	}

	public void setCfId(Long cfId) {
		this.cfId = cfId;
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

	public String getCfNum() {
		return cfNum;
	}

	public void setCfNum(String cfNum) {
		this.cfNum = cfNum;
	}

	public String getCfSource() {
		return cfSource;
	}

	public void setCfSource(String cfSource) {
		this.cfSource = cfSource;
	}

	public String getCfType() {
		return cfType;
	}

	public void setCfType(String cfType) {
		this.cfType = cfType;
	}

	

	public String getReceptionUnit() {
		return receptionUnit;
	}

	public void setReceptionUnit(String receptionUnit) {
		this.receptionUnit = receptionUnit;
	}

	public String getReceptionist() {
		return receptionist;
	}

	public void setReceptionist(String receptionist) {
		this.receptionist = receptionist;
	}

	public String getCfPeople() {
		return cfPeople;
	}

	public void setCfPeople(String cfPeople) {
		this.cfPeople = cfPeople;
	}

	public String getCfPhone() {
		return cfPhone;
	}

	public void setCfPhone(String cfPhone) {
		this.cfPhone = cfPhone;
	}

	public String getCfUserNum() {
		return cfUserNum;
	}

	public void setCfUserNum(String cfUserNum) {
		this.cfUserNum = cfUserNum;
	}

	public String getCfAddr() {
		return cfAddr;
	}

	public void setCfAddr(String cfAddr) {
		this.cfAddr = cfAddr;
	}

	public String getCfRange() {
		return cfRange;
	}

	public void setCfRange(String cfRange) {
		this.cfRange = cfRange;
	}

	public String getCfContent() {
		return cfContent;
	}

	public void setCfContent(String cfContent) {
		this.cfContent = cfContent;
	}

	public String getTreatmentAdvice() {
		return treatmentAdvice;
	}

	public void setTreatmentAdvice(String treatmentAdvice) {
		this.treatmentAdvice = treatmentAdvice;
	}

	public String getBeginHandlePeople() {
		return beginHandlePeople;
	}

	public void setBeginHandlePeople(String beginHandlePeople) {
		this.beginHandlePeople = beginHandlePeople;
	}
	public String getEndHandlePeople() {
		return endHandlePeople;
	}

	public void setEndHandlePeople(String endHandlePeople) {
		this.endHandlePeople = endHandlePeople;
	}
	public String getHandlePeople() {
		return handlePeople;
	}

	public void setHandlePeople(String handlePeople) {
		this.handlePeople = handlePeople;
	}
	public String getWorkday() {
		return workday;
	}

	public void setWorkday(String workday) {
		this.workday = workday;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public String getVisitPeople() {
		return visitPeople;
	}

	public void setVisitPeople(String visitPeople) {
		this.visitPeople = visitPeople;
	}

	public String getStaffDress() {
		return staffDress;
	}

	public void setStaffDress(String staffDress) {
		this.staffDress = staffDress;
	}

	public String getReactionComments() {
		return reactionComments;
	}

	public void setReactionComments(String reactionComments) {
		this.reactionComments = reactionComments;
	}

	public String getServiceAttitude() {
		return serviceAttitude;
	}

	public void setServiceAttitude(String serviceAttitude) {
		this.serviceAttitude = serviceAttitude;
	}

	public String getQos() {
		return qos;
	}

	public void setQos(String qos) {
		this.qos = qos;
	}

	public String getSatisfaction() {
		return satisfaction;
	}

	public void setSatisfaction(String satisfaction) {
		this.satisfaction = satisfaction;
	}

	public String getOtherFeedback() {
		return otherFeedback;
	}

	public void setOtherFeedback(String otherFeedback) {
		this.otherFeedback = otherFeedback;
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	public String getAssessmentScore() {
		return assessmentScore;
	}

	public void setAssessmentScore(String assessmentScore) {
		this.assessmentScore = assessmentScore;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
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

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
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


	public String getSiteSituation() {
		return siteSituation;
	}

	public void setSiteSituation(String siteSituation) {
		this.siteSituation = siteSituation;
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
	public String getDispatchRemark() {
		return dispatchRemark;
	}

	public void setDispatchRemark(String dispatchRemark) {
		this.dispatchRemark = dispatchRemark;
	}

	public String getCfDate() {
		return cfDate;
	}

	public void setCfDate(String cfDate) {
		this.cfDate = cfDate;
	}

	public String getBeginHandleDate() {
		return beginHandleDate;
	}

	public void setBeginHandleDate(String beginHandleDate) {
		this.beginHandleDate = beginHandleDate;
	}

	public String getEndHandleDate() {
		return endHandleDate;
	}

	public void setEndHandleDate(String endHandleDate) {
		this.endHandleDate = endHandleDate;
	}

	public String getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(String handleDate) {
		this.handleDate = handleDate;
	}

	public String getVisitDate() {
		return visitDate;
	}

	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getDispatchDate() {
		return dispatchDate;
	}

	public void setDispatchDate(String dispatchDate) {
		this.dispatchDate = dispatchDate;
	}
}