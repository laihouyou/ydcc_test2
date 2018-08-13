package com.movementinsome.database.vo;





import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;



/**
 * 工作任务提交对象
 * 
 * @author gddst
 * 
 */
@JsonIgnoreProperties(ignoreUnknown = true) 
public class InsTableUpLoadVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4903984830205989905L;
	private String userName; 								// 用户名称
	private String phoneImei; 								// 手机机器码
	private String longitude; 								// 填表时的经度
	private String latitude;  								// 填表时的纬度
	private String upTimeStr;  								// 填表时的时间
	private String tableName;  								// 表单名称，识别表单类型
	private InsTableSiteOperationFeedBackVO siteFeedBack;			//现场反馈
	private List<InsTableFacilityProblemVO> facProblem;			//问题上报
	private List<InsTableLDReportVO> lDReport;						//漏点上报
	private List<InsTableDatumInaccurateVO> datumInaccurate;		//图与显示不符
	private List<InsTableTaskFeedLeakVO> taskFeedLeakList;				//现场定漏单
	private List<InsTableConstructionRegisteredVO> consRegistered;	//施工上报
	private List<InsTableConstructionMonitoringVO> consMonitoring;	//日常监控
	
	
	public List<InsTableConstructionMonitoringVO> getConsMonitoring() {
		return consMonitoring;
	}
	public void setConsMonitoring(
			List<InsTableConstructionMonitoringVO> consMonitoring) {
		this.consMonitoring = consMonitoring;
	}
	public List<InsTableConstructionRegisteredVO> getConsRegistered() {
		return consRegistered;
	}
	public void setConsRegistered(
			List<InsTableConstructionRegisteredVO> consRegistered) {
		this.consRegistered = consRegistered;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhoneImei() {
		return phoneImei;
	}
	public void setPhoneImei(String phoneImei) {
		this.phoneImei = phoneImei;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getUpTimeStr() {
		return upTimeStr;
	}
	public void setUpTimeStr(String upTimeStr) {
		this.upTimeStr = upTimeStr;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public List<InsTableFacilityProblemVO> getFacProblem() {
		return facProblem;
	}
	public void setFacProblem(List<InsTableFacilityProblemVO> facProblem) {
		this.facProblem = facProblem;
	}
	public List<InsTableLDReportVO> getlDReport() {
		return lDReport;
	}
	public void setlDReport(List<InsTableLDReportVO> lDReport) {
		this.lDReport = lDReport;
	}
	public List<InsTableDatumInaccurateVO> getDatumInaccurate() {
		return datumInaccurate;
	}
	public void setDatumInaccurate(List<InsTableDatumInaccurateVO> datumInaccurate) {
		this.datumInaccurate = datumInaccurate;
	}
	public List<InsTableTaskFeedLeakVO> getTaskFeedLeakList() {
		return taskFeedLeakList;
	}
	public void setTaskFeedLeakList(List<InsTableTaskFeedLeakVO> taskFeedLeakList) {
		this.taskFeedLeakList = taskFeedLeakList;
	}
	public InsTableSiteOperationFeedBackVO getSiteFeedBack() {
		return siteFeedBack;
	}
	public void setSiteFeedBack(InsTableSiteOperationFeedBackVO siteFeedBack) {
		this.siteFeedBack = siteFeedBack;
	}
	
}
