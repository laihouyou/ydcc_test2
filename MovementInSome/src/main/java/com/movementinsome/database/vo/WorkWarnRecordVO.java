package com.movementinsome.database.vo;


public class WorkWarnRecordVO implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2922615617500853347L;
	// 消息ID
	private Long id;
	// 预警时间
	private String warnTimeStr;
	// 预警标题
	private String warnTitle;
	// 预警信息
	private String warnText;
	// 预警模块或分类
	private String warnModules;
	// 预警人ID
	private Long warnPeopleId;
	// 预警人编号
	private String warnPeopleNum;
	// 预警人
	private String warnPeople;
	// 接收时间
	private String acceptTimeStr;
	// 接收人部门ID
	private Long acceptDeptId;
	// 接收人部门编号
	private String acceptDeptNum;
	// 接收人部门
	private String acceptDept;
	// 接收人ID
	private Long acceptPeopleId;
	// 接收人编号
	private String acceptPeopleNum;
	// 接收人
	private String acceptPeople;
	// 关联字段名
	private String linkName;
	// 关联值
	private String linkNum;
	// 预警坐标
	private String coordinte;
	// 预警状态（阅读状态）
	private Long state;
	// 工作完成状态
	private Long workState;
	// 处理工作组
	private String handlerGroup;

	public WorkWarnRecordVO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWarnTitle() {
		return warnTitle;
	}

	public void setWarnTitle(String warnTitle) {
		this.warnTitle = warnTitle;
	}

	public String getWarnText() {
		return warnText;
	}

	public void setWarnText(String warnText) {
		this.warnText = warnText;
	}

	public String getWarnModules() {
		return warnModules;
	}

	public void setWarnModules(String warnModules) {
		this.warnModules = warnModules;
	}

	public Long getWarnPeopleId() {
		return warnPeopleId;
	}

	public void setWarnPeopleId(Long warnPeopleId) {
		this.warnPeopleId = warnPeopleId;
	}

	public String getWarnPeopleNum() {
		return warnPeopleNum;
	}

	public void setWarnPeopleNum(String warnPeopleNum) {
		this.warnPeopleNum = warnPeopleNum;
	}

	public String getWarnPeople() {
		return warnPeople;
	}

	public void setWarnPeople(String warnPeople) {
		this.warnPeople = warnPeople;
	}

	public String getLinkName() {
		return linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	public String getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(String linkNum) {
		this.linkNum = linkNum;
	}

	public String getCoordinte() {
		return coordinte;
	}

	public void setCoordinte(String coordinte) {
		this.coordinte = coordinte;
	}

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public Long getAcceptDeptId() {
		return acceptDeptId;
	}

	public void setAcceptDeptId(Long acceptDeptId) {
		this.acceptDeptId = acceptDeptId;
	}

	public String getAcceptDeptNum() {
		return acceptDeptNum;
	}

	public void setAcceptDeptNum(String acceptDeptNum) {
		this.acceptDeptNum = acceptDeptNum;
	}

	public String getAcceptDept() {
		return acceptDept;
	}

	public void setAcceptDept(String acceptDept) {
		this.acceptDept = acceptDept;
	}

	public Long getAcceptPeopleId() {
		return acceptPeopleId;
	}

	public void setAcceptPeopleId(Long acceptPeopleId) {
		this.acceptPeopleId = acceptPeopleId;
	}

	public String getAcceptPeopleNum() {
		return acceptPeopleNum;
	}

	public void setAcceptPeopleNum(String acceptPeopleNum) {
		this.acceptPeopleNum = acceptPeopleNum;
	}

	public String getAcceptPeople() {
		return acceptPeople;
	}

	public void setAcceptPeople(String acceptPeople) {
		this.acceptPeople = acceptPeople;
	}

	public Long getWorkState() {
		return workState;
	}

	public void setWorkState(Long workState) {
		this.workState = workState;
	}

	public String getHandlerGroup() {
		return handlerGroup;
	}

	public void setHandlerGroup(String handlerGroup) {
		this.handlerGroup = handlerGroup;
	}

	public String getWarnTimeStr() {
		return warnTimeStr;
	}

	public void setWarnTimeStr(String warnTimeStr) {
		this.warnTimeStr = warnTimeStr;
	}

	public String getAcceptTimeStr() {
		return acceptTimeStr;
	}

	public void setAcceptTimeStr(String acceptTimeStr) {
		this.acceptTimeStr = acceptTimeStr;
	}

}