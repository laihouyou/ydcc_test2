package com.movementinsome.database.vo;

import java.util.List;

public class InsGpsRouterInfoVO {

	private String igiRouterNum;//轨迹段值

	private String igiStarttime;//开始时间

	private String igiEndtime;//结束时间

	private String workTaskNum;//任务编号

	private List<InsGpsRouterDataVO> dateList;

	public String getIgiRouterNum() {
		return igiRouterNum;
	}

	public void setIgiRouterNum(String igiRouterNum) {
		this.igiRouterNum = igiRouterNum;
	}

	public String getIgiStarttime() {
		return igiStarttime;
	}

	public void setIgiStarttime(String igiStarttime) {
		this.igiStarttime = igiStarttime;
	}

	public String getIgiEndtime() {
		return igiEndtime;
	}

	public void setIgiEndtime(String igiEndtime) {
		this.igiEndtime = igiEndtime;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public List<InsGpsRouterDataVO> getDateList() {
		return dateList;
	}

	public void setDateList(List<InsGpsRouterDataVO> dateList) {
		this.dateList = dateList;
	}

}
