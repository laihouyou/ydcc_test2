package com.movementinsome.easyform.formengineer;

public class XmlGuiFormAteps {

	private String groupid ;
	private String name;
	private String state;// 0空闲中，1操作中，2提交
	private String startState;// 开始状态
	private String finishState;// 完成状态
	
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public String getStartState() {
		return startState;
	}
	public void setStartState(String startState) {
		this.startState = startState;
	}
	public String getFinishState() {
		return finishState;
	}
	public void setFinishState(String finishState) {
		this.finishState = finishState;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getJsonResult() {
		return '"'+this.groupid +'"'+":"+ '"'+state+ '"';
	}
	
}
