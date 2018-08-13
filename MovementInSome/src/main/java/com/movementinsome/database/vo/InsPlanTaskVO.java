package com.movementinsome.database.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsPlanTaskVO")
public class InsPlanTaskVO implements java.io.Serializable{
	
	// ID
	@DatabaseField(id = true)
	private String guid;
	@DatabaseField
	private Long workTaskId;//任务ID
	@DatabaseField
	private String workTaskNum;//任务编号
	@DatabaseField
	private String workTaskType;//任务类型:PLAN_PATROL_CYCLE(周期计划),PLAN_PATROL_TEMPORARY(临时计划)
	@DatabaseField
	private String taskSource;//任务来源
	@DatabaseField
	private String workTaskName;//任务名称
	@DatabaseField
	private String workTaskAddr;//任务地点
	@DatabaseField
	private String workTaskWork;//工作内容
	@DatabaseField
	private String workTaskPEDate;//计划完成时间
	@DatabaseField
	private String workTaskRemarks;//备注
	@DatabaseField
	private Long createUId;//创建人ID
	@DatabaseField
	private String createUNum;//创建人编号
	@DatabaseField
	private String createUName;//创建人名称
	@DatabaseField
	private Long lastUpdateUId;//最后更新人ID
	@DatabaseField
	private String lastUpdateUNum;//最后更新人编号
	@DatabaseField
	private String lastUpdateUName;//最后更新人名称
	@DatabaseField
	private String manageUnit;//管理单位
	@DatabaseField
	private Long state;//任务状态
	@DatabaseField
	private Long handlerId;//处理人ID
	@DatabaseField
	private String handlerNum;//处理人编号
	@DatabaseField
	private String handlerName;//处理人名称
	@DatabaseField
	private String handlerPhone;//处理人电话
	@DatabaseField
	private String handlerDepartment;//处理部门
	@DatabaseField
	private String areaBound;//区界(GeometryVO)
	@DatabaseField
	private String workTaskPSDate;//计划开始时间
	@DatabaseField
	private String receiveDate;//接单时间
	@DatabaseField
	private String workTaskEDate;//完成时间
	@DatabaseField
	private String createDate;//创建日期
	@DatabaseField
	private String lastUpdateDate;//最后更新时间
	@DatabaseField
	private Long workOrder;// 当前工序
	
	
	public Long getWorkOrder() {
		return workOrder;
	}
	public void setWorkOrder(Long workOrder) {
		this.workOrder = workOrder;
	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getAreaBound() {
		return areaBound;
	}
	public void setAreaBound(String areaBound) {
		this.areaBound = areaBound;
	}
	public Long getWorkTaskId() {
		return workTaskId;
	}
	public void setWorkTaskId(Long workTaskId) {
		this.workTaskId = workTaskId;
	}
	public String getWorkTaskNum() {
		return workTaskNum;
	}
	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}
	public String getWorkTaskType() {
		return workTaskType;
	}
	public void setWorkTaskType(String workTaskType) {
		this.workTaskType = workTaskType;
	}
	public String getTaskSource() {
		return taskSource;
	}
	public void setTaskSource(String taskSource) {
		this.taskSource = taskSource;
	}
	public String getWorkTaskName() {
		return workTaskName;
	}
	public void setWorkTaskName(String workTaskName) {
		this.workTaskName = workTaskName;
	}
	public String getWorkTaskAddr() {
		return workTaskAddr;
	}
	public void setWorkTaskAddr(String workTaskAddr) {
		this.workTaskAddr = workTaskAddr;
	}
	public String getWorkTaskWork() {
		return workTaskWork;
	}
	public void setWorkTaskWork(String workTaskWork) {
		this.workTaskWork = workTaskWork;
	}
	
	public String getWorkTaskPEDate() {
		return workTaskPEDate;
	}
	public void setWorkTaskPEDate(String workTaskPEDate) {
		this.workTaskPEDate = workTaskPEDate;
	}
	
	public String getWorkTaskRemarks() {
		return workTaskRemarks;
	}
	public void setWorkTaskRemarks(String workTaskRemarks) {
		this.workTaskRemarks = workTaskRemarks;
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
	public String getManageUnit() {
		return manageUnit;
	}
	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}
	public Long getState() {
		return state;
	}
	public void setState(Long state) {
		this.state = state;
	}
	public Long getHandlerId() {
		return handlerId;
	}
	public void setHandlerId(Long handlerId) {
		this.handlerId = handlerId;
	}
	public String getHandlerNum() {
		return handlerNum;
	}
	public void setHandlerNum(String handlerNum) {
		this.handlerNum = handlerNum;
	}
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public String getHandlerPhone() {
		return handlerPhone;
	}
	public void setHandlerPhone(String handlerPhone) {
		this.handlerPhone = handlerPhone;
	}
	public String getHandlerDepartment() {
		return handlerDepartment;
	}
	public void setHandlerDepartment(String handlerDepartment) {
		this.handlerDepartment = handlerDepartment;
	}
	public String getWorkTaskPSDate() {
		return workTaskPSDate;
	}
	public void setWorkTaskPSDate(String workTaskPSDate) {
		this.workTaskPSDate = workTaskPSDate;
	}
	public String getReceiveDate() {
		return receiveDate;
	}
	public void setReceiveDate(String receiveDate) {
		this.receiveDate = receiveDate;
	}
	public String getWorkTaskEDate() {
		return workTaskEDate;
	}
	public void setWorkTaskEDate(String workTaskEDate) {
		this.workTaskEDate = workTaskEDate;
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
	
}
