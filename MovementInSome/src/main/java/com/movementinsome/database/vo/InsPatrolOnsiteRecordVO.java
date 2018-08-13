package com.movementinsome.database.vo;

import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


//路面巡查记录类
@DatabaseTable(tableName = "INS_PATROL_ONSITE_RECORD")
public class InsPatrolOnsiteRecordVO implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2023275039142557209L;
	//记录ID
	@DatabaseField(id = true)
	private String id;
	// 巡检现场记录ID
	//private Long pdsId;
	// 巡检数据表ID
	@DatabaseField
	private Long wpdId;
	// 任务编号
	@DatabaseField
	private String workTaskNum;
	
	// 路段编号
	@DatabaseField
	private String roadNum;
	// 路名
	@DatabaseField
	private String roadName;
	// 巡查开始时间
	@DatabaseField
	private String patrolStartDate;
	// 开始巡查坐标
	@DatabaseField
	private String patrolStartCoordinate;
	// 巡查结束时间
	@DatabaseField
	private String patrolEndDate;
	// 结束巡查坐标
	@DatabaseField
	private String patrolEndCoordinate;
	
	// 上一个路段编号
	@DatabaseField
	private String preRoadNum;
	// 上一个路段名称
	@DatabaseField
	private String preRoadName;
	// 上一个路段巡查开始时间
	@DatabaseField
	private String prePsDate;
	// 上一个路段开始巡查坐标
	@DatabaseField
	private String prePsCoordinate;
	// 上一个路段巡查结束时间
	@DatabaseField
	private String prePeDate;
	// 上一个路段结束巡查坐标
	@DatabaseField
	private String prePeCoordinate;

	// 巡查人
	@DatabaseField
	private String patrolPeople;
	// 流水号
	@DatabaseField
	private String serialNumber;
	// 移动端机器码
	@DatabaseField
	private String phoneImei;
	// 移动端唯一编码
	@DatabaseField
	private String moiNum;
	// 管理单位
	@DatabaseField
	private String manageUnit;
	// 创建日期
	@DatabaseField
	private String createDate;
	// 创建人ID
	@DatabaseField
	private Long createUId;
	// 创建人编号
	@DatabaseField
	private String createUNum;
	// 创建人名称
	@DatabaseField
	private String createUName;
	// 最后更新时间
	@DatabaseField
	private String lastUpdateDate;
	// 最后更新人ID
	@DatabaseField
	private Long lastUpdateUId;
	// 最后更新人编号
	@DatabaseField
	private String lastUpdateUNum;
	// 最后更新人名称
	@DatabaseField
	private String lastUpdateUName;
	// 上报坐标点
	@DatabaseField
	private String reportedCoordinate;
	// GUID
	@DatabaseField
	private String guid;
	// 任务类型
	@DatabaseField
	private String taskCategory;
	//巡查次数
	@DatabaseField
	private Long insCount;
	
	
	public Long getInsCount() {
		return insCount;
	}
	public void setInsCount(Long insCount) {
		this.insCount = insCount;
	}
	public String getTaskCategory() {
		return taskCategory;
	}
	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
/*	public Long getPdsId() {
		return pdsId;
	}
	public void setPdsId(Long pdsId) {
		this.pdsId = pdsId;
	}*/
	public Long getWpdId() {
		return wpdId;
	}
	public void setWpdId(Long wpdId) {
		this.wpdId = wpdId;
	}
	public String getWorkTaskNum() {
		return workTaskNum;
	}
	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}
	public String getRoadNum() {
		return roadNum;
	}
	public void setRoadNum(String roadNum) {
		this.roadNum = roadNum;
	}
	public String getRoadName() {
		return roadName;
	}
	public void setRoadName(String roadName) {
		this.roadName = roadName;
	}
	public String getPatrolStartDate() {
		return patrolStartDate;
	}
	public void setPatrolStartDate(String patrolStartDate) {
		this.patrolStartDate = patrolStartDate;
	}
	public String getPatrolEndDate() {
		return patrolEndDate;
	}
	public void setPatrolEndDate(String patrolEndDate) {
		this.patrolEndDate = patrolEndDate;
	}
	public String getPreRoadNum() {
		return preRoadNum;
	}
	public void setPreRoadNum(String preRoadNum) {
		this.preRoadNum = preRoadNum;
	}
	public String getPreRoadName() {
		return preRoadName;
	}
	public void setPreRoadName(String preRoadName) {
		this.preRoadName = preRoadName;
	}
	public String getPrePsDate() {
		return prePsDate;
	}
	public void setPrePsDate(String prePsDate) {
		this.prePsDate = prePsDate;
	}
	public String getPrePsCoordinate() {
		return prePsCoordinate;
	}
	public void setPrePsCoordinate(String prePsCoordinate) {
		this.prePsCoordinate = prePsCoordinate;
	}
	public String getPrePeDate() {
		return prePeDate;
	}
	public void setPrePeDate(String prePeDate) {
		this.prePeDate = prePeDate;
	}
	public String getPrePeCoordinate() {
		return prePeCoordinate;
	}
	public void setPrePeCoordinate(String prePeCoordinate) {
		this.prePeCoordinate = prePeCoordinate;
	}
	public String getPatrolPeople() {
		return patrolPeople;
	}
	public void setPatrolPeople(String patrolPeople) {
		this.patrolPeople = patrolPeople;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getPhoneImei() {
		return phoneImei;
	}
	public void setPhoneImei(String phoneImei) {
		this.phoneImei = phoneImei;
	}
	public String getMoiNum() {
		return moiNum;
	}
	public void setMoiNum(String moiNum) {
		this.moiNum = moiNum;
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
	public String getReportedCoordinate() {
		return reportedCoordinate;
	}
	public void setReportedCoordinate(String reportedCoordinate) {
		this.reportedCoordinate = reportedCoordinate;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	
	public String getPatrolStartCoordinate() {
		return patrolStartCoordinate;
	}
	public void setPatrolStartCoordinate(String patrolStartCoordinate) {
		this.patrolStartCoordinate = patrolStartCoordinate;
	}
	public String getPatrolEndCoordinate() {
		return patrolEndCoordinate;
	}
	public void setPatrolEndCoordinate(String patrolEndCoordinate) {
		this.patrolEndCoordinate = patrolEndCoordinate;
	}
	public String toJson(){

		JSONObject ob=new JSONObject();
		try {
			ob.put("moiNum", this.getId());
			ob.put("wpdId", this.getWpdId());
			ob.put("guid", this.getGuid());
			
			ob.put("workTaskNum", this.getWorkTaskNum());
			ob.put("tableName", "INS_PATROL_ONSITE_RECORD");
			ob.put("roadNum", this.getRoadNum());
			ob.put("roadName", this.getRoadName());
			
			ob.put("patrolStartDate", this.getPatrolStartDate());
			ob.put("patorlStartCoordinate", this.getPatrolStartCoordinate());
			ob.put("patrolEndDate", this.getPatrolEndDate());
			ob.put("patorlEndCoordinate", this.getPatrolEndCoordinate());
			ob.put("preRoadNum", this.getPreRoadNum());
			ob.put("preRoadName", this.getPreRoadName());
			ob.put("prePsDate",this.getPrePsDate());
			ob.put("prePsCoordinate",this.getPrePsCoordinate());
			ob.put("prePeDate",this.getPrePeDate());
			ob.put("prePeCoordinate",this.getPrePeCoordinate());
			ob.put("patrolPeople",this.getPatrolPeople());
			ob.put("phoneImei",this.getPhoneImei());
			ob.put("createDate",this.getCreateDate());
			ob.put("createUId",this.getCreateUId());	
			ob.put("createUNum",this.getCreateUNum());
			ob.put("createUName",this.getCreateUName());
			ob.put("reportedCoordinate",this.getReportedCoordinate());
			ob.put("reportedCoordinate",this.getReportedCoordinate());
			ob.put("guid",this.getGuid());		
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return ob.toString();
	}
	// status 上传状态 1:正常 2：暂停 3：完成,4:偏离,5:终止
	//新扩展状态：1.进入巡查,2.暂停,3：完成(由系统对传入为1的进行计算判断),4:偏离,5:偏离警告,6.续巡,7.结束,8。终止        29/9
	public String toJson(Long status){

		JSONObject ob=new JSONObject();
		try {
			ob.put("moiNum", this.getId());
			ob.put("wpdId", this.getWpdId());
			ob.put("guid", this.getGuid());
			
			ob.put("workTaskNum", this.getWorkTaskNum());
			ob.put("tableName", "INS_PATROL_ONSITE_RECORD");
			ob.put("roadNum", this.getRoadNum());
			ob.put("roadName", this.getRoadName());
			
			ob.put("patrolStartDate", this.getPatrolStartDate());
			ob.put("patorlStartCoordinate", this.getPatrolStartCoordinate());
			ob.put("patrolEndDate", this.getPatrolEndDate());
			ob.put("patorlEndCoordinate", this.getPatrolEndCoordinate());
			ob.put("preRoadNum", this.getPreRoadNum());
			ob.put("preRoadName", this.getPreRoadName());
			ob.put("prePsDate",this.getPrePsDate());
			ob.put("prePsCoordinate",this.getPrePsCoordinate());
			ob.put("prePeDate",this.getPrePeDate());
			ob.put("prePeCoordinate",this.getPrePeCoordinate());
			ob.put("patrolPeople",this.getPatrolPeople());
			ob.put("phoneImei",this.getPhoneImei());
			ob.put("createDate",this.getCreateDate());
			ob.put("createUId",this.getCreateUId());	
			ob.put("createUNum",this.getCreateUNum());
			ob.put("createUName",this.getCreateUName());
			ob.put("reportedCoordinate",this.getReportedCoordinate());
			ob.put("reportedCoordinate",this.getReportedCoordinate());
			ob.put("guid",this.getGuid());	
			ob.put("status",status);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        return ob.toString();
	}
}
