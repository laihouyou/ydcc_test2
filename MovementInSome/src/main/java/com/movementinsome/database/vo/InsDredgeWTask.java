package com.movementinsome.database.vo;


//清疏台账表
public class InsDredgeWTask implements java.io.Serializable {

	private Long dwId;//id
	private String dpNum;//计划清疏编号
	private String serialNumber;//清疏派工编号
	private String workTaskNum;//投诉序号
	private String taskType;//工单类型
	private String sources;//信息来源
	private String reporterNum;//报告人编号
	private String reporterName;//报告人
	private String tel;//电话
	private String addr;//任务地址
	private String roadStart;//路段起点
	private String roadEnd;//路段终点
	private String reason;//事故原因
	private String workType;//清疏类别
	private String otherTask;//其他任务
	private String department;//派工部门
	private String pushTime;//派工时间
	private String leader;//责任人
	private String groupNum;//班组编号
	private String groupName;//班组
	private String carNum;//车牌号
	private Long carSum;//车辆数量
	private String workers;//参加人员
	private String sTime;//作业开始时间段
	private String eTime;//作业结束时间段
	private String result;//处理结果
	private String remarks;//备注
	private String pipeType;//管渠类别
	private Long state;//状态 0未有处理1正在处理2已完成3强制完成
	
	private Long rainWellSum;//雨水检查井数量
	private Long sewageWellSum;//污水检查井数量
	private Long gulliesSum;//雨水口数量
	private String gulliesSize;//雨水管渠尺寸
	private Long gulliesLen;//雨水管渠长
	private Long sewerLen;//污水管渠长
	private String sewerSize;//污水管渠尺寸
	
	private Long rainWellSumRl;//雨水检查井数量
	private Long sewageWellSumRl;//污水检查井数量
	private Long gulliesSumRl;//雨水口数量
	private String gulliesSizeRl;//雨水管渠尺寸
	private Long gulliesLenRl;//雨水管渠长
	private Long sewerLenRl;//污水管渠长
	private String sewerSizeRl;//污水管渠尺寸
	
	private Long sludgeSum;//淤泥量
	private String manageUnit;//区所
	//上报坐标点
	private String reportedCoordinate;
	//创建日期
	private String createDate;
	//创建人ID
	private Long createUId;
	//创建人编号
	private String createUNum;
	//创建人名称
	private String createUName;
	//最后更新时间
	private String lastUpdateDate;
	//最后更新人ID
	private Long lastUpdateUId;
	//最后更新人编号
	private String lastUpdateUNum;
	//最后更新人名称
	private String lastUpdateUName;

    private Long deptId;//部门ID

    private String deptNum;//部门编号
    
    private String deptName;//部门名称
    
    private Long teamId;//班组ID
    
    private String teamNum;//班组编号
    
    private String teamName;//班组名称
    	//移动端唯一编码
	private String moiNum;
	//移动端机器码
	private String phoneImei;
	private String guid;
	
	private String sections;//所在路段
	
	private String sectionsNum;//路段编号
	
	public InsDredgeWTask(){}

	public Long getDwId() {
		return dwId;
	}

	public void setDwId(Long dwId) {
		this.dwId = dwId;
	}

	public String getDpNum() {
		return dpNum;
	}

	public void setDpNum(String dpNum) {
		this.dpNum = dpNum;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getSources() {
		return sources;
	}

	public void setSources(String sources) {
		this.sources = sources;
	}

	public String getReporterNum() {
		return reporterNum;
	}

	public void setReporterNum(String reporterNum) {
		this.reporterNum = reporterNum;
	}

	public String getReporterName() {
		return reporterName;
	}

	public void setReporterName(String reporterName) {
		this.reporterName = reporterName;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getRoadStart() {
		return roadStart;
	}

	public void setRoadStart(String roadStart) {
		this.roadStart = roadStart;
	}

	public String getRoadEnd() {
		return roadEnd;
	}

	public void setRoadEnd(String roadEnd) {
		this.roadEnd = roadEnd;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getOtherTask() {
		return otherTask;
	}

	public void setOtherTask(String otherTask) {
		this.otherTask = otherTask;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPushTime() {
		return pushTime;
	}

	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCarNum() {
		return carNum;
	}

	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}

	public Long getCarSum() {
		return carSum;
	}

	public void setCarSum(Long carSum) {
		this.carSum = carSum;
	}

	public String getWorkers() {
		return workers;
	}

	public void setWorkers(String workers) {
		this.workers = workers;
	}

	public String getsTime() {
		return sTime;
	}

	public void setsTime(String sTime) {
		this.sTime = sTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getPipeType() {
		return pipeType;
	}

	public void setPipeType(String pipeType) {
		this.pipeType = pipeType;
	}

	public Long getRainWellSum() {
		return rainWellSum;
	}

	public void setRainWellSum(Long rainWellSum) {
		this.rainWellSum = rainWellSum;
	}

	public Long getSewageWellSum() {
		return sewageWellSum;
	}

	public void setSewageWellSum(Long sewageWellSum) {
		this.sewageWellSum = sewageWellSum;
	}

	public Long getGulliesSum() {
		return gulliesSum;
	}

	public void setGulliesSum(Long gulliesSum) {
		this.gulliesSum = gulliesSum;
	}

	public String getGulliesSize() {
		return gulliesSize;
	}

	public void setGulliesSize(String gulliesSize) {
		this.gulliesSize = gulliesSize;
	}

	public Long getGulliesLen() {
		return gulliesLen;
	}

	public void setGulliesLen(Long gulliesLen) {
		this.gulliesLen = gulliesLen;
	}

	public Long getSewerLen() {
		return sewerLen;
	}

	public void setSewerLen(Long sewerLen) {
		this.sewerLen = sewerLen;
	}

	public String getSewerSize() {
		return sewerSize;
	}

	public void setSewerSize(String sewerSize) {
		this.sewerSize = sewerSize;
	}

	public Long getRainWellSumRl() {
		return rainWellSumRl;
	}

	public void setRainWellSumRl(Long rainWellSumRl) {
		this.rainWellSumRl = rainWellSumRl;
	}

	public Long getSewageWellSumRl() {
		return sewageWellSumRl;
	}

	public void setSewageWellSumRl(Long sewageWellSumRl) {
		this.sewageWellSumRl = sewageWellSumRl;
	}

	public Long getGulliesSumRl() {
		return gulliesSumRl;
	}

	public void setGulliesSumRl(Long gulliesSumRl) {
		this.gulliesSumRl = gulliesSumRl;
	}

	public String getGulliesSizeRl() {
		return gulliesSizeRl;
	}

	public void setGulliesSizeRl(String gulliesSizeRl) {
		this.gulliesSizeRl = gulliesSizeRl;
	}

	public Long getGulliesLenRl() {
		return gulliesLenRl;
	}

	public void setGulliesLenRl(Long gulliesLenRl) {
		this.gulliesLenRl = gulliesLenRl;
	}

	public Long getSewerLenRl() {
		return sewerLenRl;
	}

	public void setSewerLenRl(Long sewerLenRl) {
		this.sewerLenRl = sewerLenRl;
	}

	public String getSewerSizeRl() {
		return sewerSizeRl;
	}

	public void setSewerSizeRl(String sewerSizeRl) {
		this.sewerSizeRl = sewerSizeRl;
	}

	public Long getSludgeSum() {
		return sludgeSum;
	}

	public void setSludgeSum(Long sludgeSum) {
		this.sludgeSum = sludgeSum;
	}

	public String getManageUnit() {
		return manageUnit;
	}

	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}

	public String getReportedCoordinate() {
		return reportedCoordinate;
	}

	public void setReportedCoordinate(String reportedCoordinate) {
		this.reportedCoordinate = reportedCoordinate;
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

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
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

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptNum() {
		return deptNum;
	}

	public void setDeptNum(String deptNum) {
		this.deptNum = deptNum;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeamNum() {
		return teamNum;
	}

	public void setTeamNum(String teamNum) {
		this.teamNum = teamNum;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
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

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getSections() {
		return sections;
	}

	public void setSections(String sections) {
		this.sections = sections;
	}

	public String getSectionsNum() {
		return sectionsNum;
	}

	public void setSectionsNum(String sectionsNum) {
		this.sectionsNum = sectionsNum;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

}