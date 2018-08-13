package com.movementinsome.database.vo;


import java.util.List;


/**
 * WorkPlanTask entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class WorkPlanTaskVO implements java.io.Serializable {

	//任务编号
	private String workTaskNum;
	//任务类型
	private Long workTaskType;
	//任务名称
	private String workTaskName;
	//任务地点
	private String workTaskAddr;
	//主要工作
	private String workTaskWork;
	//计划开始时间
	private String workTaskPSDateStr; 
	//计划完成时间
	private String workTaskPEDate;
	//接单时间
	private String receiveDateStr;
	//完成时间
	private String workTaskEDateStr; 
	//填单日期
	private String workTaskFDateStr; 
	//备注
	private String workTaskRemarks;
	//填单人编号
	private String workTaskUserNum;
	//填单人
	private String workTaskUserName;
	//管理单位
	private String manageUnit;
	//任务状态
	private Long state;
	//处理人ID
	private Long handlerId;
	//处理人编号
	private String handlerNum;
	//处理人名称
	private String handlerName;
	//处理人电话
	private String handlerPhone;
	//处理部门
	private String handlerDepartment;
	//巡检数据
	private List<WorkPatrolDataVO> pdList; 
	// 图形
	private GeometryVO geometry;

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public Long getWorkTaskType() {
		return workTaskType;
	}

	public void setWorkTaskType(Long workTaskType) {
		this.workTaskType = workTaskType;
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

	public String getWorkTaskPSDateStr() {
		return workTaskPSDateStr;
	}

	public void setWorkTaskPSDateStr(String workTaskPSDateStr) {
		this.workTaskPSDateStr = workTaskPSDateStr;
	}

	public String getWorkTaskPEDate() {
		return workTaskPEDate;
	}

	public void setWorkTaskPEDate(String workTaskPEDate) {
		this.workTaskPEDate = workTaskPEDate;
	}

	public String getReceiveDateStr() {
		return receiveDateStr;
	}

	public void setReceiveDateStr(String receiveDateStr) {
		this.receiveDateStr = receiveDateStr;
	}

	public String getWorkTaskEDateStr() {
		return workTaskEDateStr;
	}

	public void setWorkTaskEDateStr(String workTaskEDateStr) {
		this.workTaskEDateStr = workTaskEDateStr;
	}

	public String getWorkTaskFDateStr() {
		return workTaskFDateStr;
	}

	public void setWorkTaskFDateStr(String workTaskFDateStr) {
		this.workTaskFDateStr = workTaskFDateStr;
	}

	public String getWorkTaskRemarks() {
		return workTaskRemarks;
	}

	public void setWorkTaskRemarks(String workTaskRemarks) {
		this.workTaskRemarks = workTaskRemarks;
	}

	public String getWorkTaskUserNum() {
		return workTaskUserNum;
	}

	public void setWorkTaskUserNum(String workTaskUserNum) {
		this.workTaskUserNum = workTaskUserNum;
	}

	public String getWorkTaskUserName() {
		return workTaskUserName;
	}

	public void setWorkTaskUserName(String workTaskUserName) {
		this.workTaskUserName = workTaskUserName;
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

	public GeometryVO getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryVO geometry) {
		this.geometry = geometry;
	}

	public List<WorkPatrolDataVO> getPdList() {
		return pdList;
	}

	public void setPdList(List<WorkPatrolDataVO> pdList) {
		this.pdList = pdList;
	}

}