package com.movementinsome.database.vo;


public class OffValveVO {
	// 开关阀门ID
	private String svId;
	// 序号
	private String ord;
	// 设施编码
	private String valveNum;
	// 类型
	private String valveType;
	// 口径
	private String valveCaliber;
	// 必须关闭
	private String needsToClose;
	// 是否异常
	private String isAbnormal;
	// 所在道路
	private String valveRoad;
	// GID
	private String gid;
	// 图形
	private GeometryVO shape;
	//操作前圈数
	private String turns;
	
	

	public String getTurns() {
		return turns;
	}

	public void setTurns(String turns) {
		this.turns = turns;
	}
	public String getSvId() {
		return svId;
	}

	public void setSvId(String svId) {
		this.svId = svId;
	}

	public String getOrd() {
		return ord;
	}

	public void setOrd(String ord) {
		this.ord = ord;
	}

	public String getValveNum() {
		return valveNum;
	}

	public void setValveNum(String valveNum) {
		this.valveNum = valveNum;
	}

	public String getValveType() {
		return valveType;
	}

	public void setValveType(String valveType) {
		this.valveType = valveType;
	}

	public String getValveCaliber() {
		return valveCaliber;
	}

	public void setValveCaliber(String valveCaliber) {
		this.valveCaliber = valveCaliber;
	}

	public String getNeedsToClose() {
		return needsToClose;
	}

	public void setNeedsToClose(String needsToClose) {
		this.needsToClose = needsToClose;
	}

	public String getIsAbnormal() {
		return isAbnormal;
	}

	public void setIsAbnormal(String isAbnormal) {
		this.isAbnormal = isAbnormal;
	}

	public String getValveRoad() {
		return valveRoad;
	}

	public void setValveRoad(String valveRoad) {
		this.valveRoad = valveRoad;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public GeometryVO getShape() {
		return shape;
	}

	public void setShape(GeometryVO shape) {
		this.shape = shape;
	}

}
