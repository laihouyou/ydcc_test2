package com.movementinsome.database.vo;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
@DatabaseTable(tableName = "InsDredgePTask")
public class InsDredgePTask implements Serializable {

	// ID
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private Long dpId;//DP_ID
	@DatabaseField
	private String workTaskNum;//任务编号
	@DatabaseField
	private String serialNumber;//清疏编号
	@DatabaseField
	private String sTime;//作业开始时间段
	@DatabaseField
	private String eTime;//作业结束时间段
	@DatabaseField
	private String planSTime;//计划作业开始时间段
	@DatabaseField
	private String planETime;//计划作业结束时间段
	@DatabaseField
	private Long state;//状态 0未有处理1正在处理2已完成3强制完成
	@DatabaseField
	private String roadType;//路段等级
	@DatabaseField
	private String road;//路段
	@DatabaseField
	private String remarks;//备注
	@DatabaseField
	private String manageUnit;//区所
	@DatabaseField
	private Long createUserNum;//创建人编号
	@DatabaseField
	private Long sewerLen;//污水管渠长
	@DatabaseField
	private String sewerSize;//污水管渠尺寸
	@DatabaseField
	private Long gulliesLen;//雨水管渠长
	@DatabaseField
	private String gulliesSize;//雨水管渠尺寸
	@DatabaseField
	private Long gulliesSum;//雨水口数量
	@DatabaseField
	private Long sewageWellSum;//污水检查井数量
	@DatabaseField
	private Long rainWellSum;//雨水检查井数量
	@DatabaseField
	private Long sewerLenRl;//实际污水管渠长
	@DatabaseField
	private String sewerSizeRl;//实际污水管渠尺寸
	@DatabaseField
	private Long gulliesLenRl;//实际雨水管渠长
	@DatabaseField
	private String gulliesSizeRl;//实际雨水管渠尺寸
	@DatabaseField
	private Long gulliesSumRl;//实际雨水口数量
	@DatabaseField
	private Long sewageWellSumRl;//实际污水检查井数量
	@DatabaseField
	private Long rainWellSumRl;//实际雨水检查井数量
	
	@DatabaseField
	private Long sewerLenNow;//实时污水管渠长
	@DatabaseField
	private Long gulliesLenNow;//实时雨水管渠长
	@DatabaseField
	private Long gulliesSumNow;//实时雨水口数量
	@DatabaseField
	private Long sewageWellSumNow;//实时污水检查井数量
	@DatabaseField
	private Long rainWellSumNow;//实时雨水检查井数量
	@DatabaseField
	private Long sludgeSum;//淤泥量
	@DatabaseField
	private String workers;//参加人员
	
	//创建日期
	@DatabaseField
	private String createDate;
	//创建人ID
	@DatabaseField
	private Long createUId;
	//创建人编号
	@DatabaseField
	private String createUNum;
	//创建人名称
	@DatabaseField
	private String createUName;
	//最后更新时间
	@DatabaseField
	private String lastUpdateDate;
	//最后更新人ID
	@DatabaseField
	private Long lastUpdateUId;
	//最后更新人编号
	@DatabaseField
	private String lastUpdateUNum;
	//最后更新人名称
	@DatabaseField
	private String lastUpdateUName;
	@DatabaseField
    private Long deptId;//部门ID
	@DatabaseField
    private String deptNum;//部门编号
	@DatabaseField
    private String deptName;//部门名称
	@DatabaseField
    private Long teamId;//班组ID
	@DatabaseField
    private String teamNum;//班组编号
	@DatabaseField
    private String teamName;//班组名称
    //移动端唯一编码
	@DatabaseField
	private String moiNum;
	//移动端机器码
	@DatabaseField
	private String phoneImei;
	@DatabaseField
	private String geometryStr;
	@DatabaseField
	private String objectid;//objectId
	@DatabaseField
	private String gid;//gid
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getDpId() {
		return dpId;
	}
	public void setDpId(Long dpId) {
		this.dpId = dpId;
	}
	public String getWorkTaskNum() {
		return workTaskNum;
	}
	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
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
	public String getPlanSTime() {
		return planSTime;
	}
	public void setPlanSTime(String planSTime) {
		this.planSTime = planSTime;
	}
	public String getPlanETime() {
		return planETime;
	}
	public void setPlanETime(String planETime) {
		this.planETime = planETime;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
	public String getRoadType() {
		return roadType;
	}
	public void setRoadType(String roadType) {
		this.roadType = roadType;
	}
	public String getRoad() {
		return road;
	}
	public void setRoad(String road) {
		this.road = road;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getManageUnit() {
		return manageUnit;
	}
	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}
	public Long getCreateUserNum() {
		return createUserNum;
	}
	public void setCreateUserNum(Long createUserNum) {
		this.createUserNum = createUserNum;
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
	public Long getGulliesLen() {
		return gulliesLen;
	}
	public void setGulliesLen(Long gulliesLen) {
		this.gulliesLen = gulliesLen;
	}
	public String getGulliesSize() {
		return gulliesSize;
	}
	public void setGulliesSize(String gulliesSize) {
		this.gulliesSize = gulliesSize;
	}
	public Long getGulliesSum() {
		return gulliesSum;
	}
	public void setGulliesSum(Long gulliesSum) {
		this.gulliesSum = gulliesSum;
	}
	public Long getSewageWellSum() {
		return sewageWellSum;
	}
	public void setSewageWellSum(Long sewageWellSum) {
		this.sewageWellSum = sewageWellSum;
	}
	public Long getRainWellSum() {
		return rainWellSum;
	}
	public void setRainWellSum(Long rainWellSum) {
		this.rainWellSum = rainWellSum;
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
	public Long getGulliesLenRl() {
		return gulliesLenRl;
	}
	public void setGulliesLenRl(Long gulliesLenRl) {
		this.gulliesLenRl = gulliesLenRl;
	}
	public String getGulliesSizeRl() {
		return gulliesSizeRl;
	}
	public void setGulliesSizeRl(String gulliesSizeRl) {
		this.gulliesSizeRl = gulliesSizeRl;
	}
	public Long getGulliesSumRl() {
		return gulliesSumRl;
	}
	public void setGulliesSumRl(Long gulliesSumRl) {
		this.gulliesSumRl = gulliesSumRl;
	}
	public Long getSewageWellSumRl() {
		return sewageWellSumRl;
	}
	public void setSewageWellSumRl(Long sewageWellSumRl) {
		this.sewageWellSumRl = sewageWellSumRl;
	}
	public Long getRainWellSumRl() {
		return rainWellSumRl;
	}
	public void setRainWellSumRl(Long rainWellSumRl) {
		this.rainWellSumRl = rainWellSumRl;
	}
	public Long getSewerLenNow() {
		return sewerLenNow;
	}
	public void setSewerLenNow(Long sewerLenNow) {
		this.sewerLenNow = sewerLenNow;
	}
	
	public Long getGulliesLenNow() {
		return gulliesLenNow;
	}
	public void setGulliesLenNow(Long gulliesLenNow) {
		this.gulliesLenNow = gulliesLenNow;
	}
	public Long getGulliesSumNow() {
		return gulliesSumNow;
	}
	public void setGulliesSumNow(Long gulliesSumNow) {
		this.gulliesSumNow = gulliesSumNow;
	}
	public Long getSewageWellSumNow() {
		return sewageWellSumNow;
	}
	public void setSewageWellSumNow(Long sewageWellSumNow) {
		this.sewageWellSumNow = sewageWellSumNow;
	}
	public Long getRainWellSumNow() {
		return rainWellSumNow;
	}
	public void setRainWellSumNow(Long rainWellSumNow) {
		this.rainWellSumNow = rainWellSumNow;
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
	public String getGeometryStr() {
		return geometryStr;
	}
	public void setGeometryStr(String geometryStr) {
		this.geometryStr = geometryStr;
	}
	public String getObjectid() {
		return objectid;
	}
	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}
	public String getGid() {
		return gid;
	}
	public void setGid(String gid) {
		this.gid = gid;
	}
	public Long getSludgeSum() {
		return sludgeSum;
	}
	public void setSludgeSum(Long sludgeSum) {
		this.sludgeSum = sludgeSum;
	}
	public String getWorkers() {
		return workers;
	}
	public void setWorkers(String workers) {
		this.workers = workers;
	}
	
	
}