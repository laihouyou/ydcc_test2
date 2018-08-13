package com.movementinsome.database.vo;


/**
 * BsFacInfo entity. @author MyEclipse Persistence Tools
 * 设施信息表(搜索出来的设施)
 */

public class BsFacInfo implements java.io.Serializable {

	// Fields

//	设施信息ID
	private Long bsFacId;
//	设施编号
	private String bsFacNum;
//	设施名称
	private String bsFacName;
//	设施类型
	private String bsFacType;
//	设施所在位置
	private String bsFacAddr;
//	设施口径
	private Long bsFacCaliber;
//	设施材质
	private String bsFacMaterial;
//	设施埋深
	private Double bsFacDepth;
//	设施坐标
	private String bsFacMap;
//	设施所在路段
	private String bsFacRiName;
//	设施GID
	private String gid;
//	设施GUID
	private String guid;
	// 阀门井编号
	private String bsIpsNum;

	//管线材料
	private String bsPipeMaterial;
	//特征
	private String bsFeatures;
	//附属物
	private String bsAppendages;
	//X
	private String bsPointX;
	//Y
	private String bsPointY;
	//地面
	private Float bsGround;
	//管顶
	private Float bsPipeTop;
	//管径或断面尺寸
	private Long bsDiameter;
	//埋深
	private Float bsBurDepth;
	//连接方向
	private String bsConnDirection;
	// 里程
	private String bsMileage;
	// 型号
	private String bsModel;
	// 厂家
	private String bsFactory;
	// 巡检周期
	private Long bsInsCycle;
	// 规格
	private String bsSpecification;
	// 转数
	private Long bsRevolutions;
	
	// 有无延长杆
	private Long bsExtensionRod;
	// 是否打卡点
	private Long bsPunchPoint;
	//排气阀编号
	private String rtuValveNum;
	//端站名称
	private String rtuNetName;
	
		
	public String getBsIpsNum() {
		return bsIpsNum;
	}

	public void setBsIpsNum(String bsIpsNum) {
		this.bsIpsNum = bsIpsNum;
	}

	public Long getBsExtensionRod() {
		return bsExtensionRod;
	}

	public void setBsExtensionRod(Long bsExtensionRod) {
		this.bsExtensionRod = bsExtensionRod;
	}

	public Long getBsPunchPoint() {
		return bsPunchPoint;
	}

	public void setBsPunchPoint(Long bsPunchPoint) {
		this.bsPunchPoint = bsPunchPoint;
	}

	public String getRtuValveNum() {
		return rtuValveNum;
	}

	public void setRtuValveNum(String rtuValveNum) {
		this.rtuValveNum = rtuValveNum;
	}

	public String getRtuNetName() {
		return rtuNetName;
	}

	public void setRtuNetName(String rtuNetName) {
		this.rtuNetName = rtuNetName;
	}

	public String getBsMileage() {
		return bsMileage;
	}

	public void setBsMileage(String bsMileage) {
		this.bsMileage = bsMileage;
	}

	public String getBsModel() {
		return bsModel;
	}

	public void setBsModel(String bsModel) {
		this.bsModel = bsModel;
	}

	public String getBsFactory() {
		return bsFactory;
	}

	public void setBsFactory(String bsFactory) {
		this.bsFactory = bsFactory;
	}

	public Long getBsInsCycle() {
		return bsInsCycle;
	}

	public void setBsInsCycle(Long bsInsCycle) {
		this.bsInsCycle = bsInsCycle;
	}

	public String getBsSpecification() {
		return bsSpecification;
	}

	public void setBsSpecification(String bsSpecification) {
		this.bsSpecification = bsSpecification;
	}

	public Long getBsRevolutions() {
		return bsRevolutions;
	}

	public void setBsRevolutions(Long bsRevolutions) {
		this.bsRevolutions = bsRevolutions;
	}

	public String getBsPipeMaterial() {
		return bsPipeMaterial;
	}

	public void setBsPipeMaterial(String bsPipeMaterial) {
		this.bsPipeMaterial = bsPipeMaterial;
	}

	public String getBsFeatures() {
		return bsFeatures;
	}

	public void setBsFeatures(String bsFeatures) {
		this.bsFeatures = bsFeatures;
	}

	public String getBsAppendages() {
		return bsAppendages;
	}

	public void setBsAppendages(String bsAppendages) {
		this.bsAppendages = bsAppendages;
	}

	public String getBsPointX() {
		return bsPointX;
	}

	public void setBsPointX(String bsPointX) {
		this.bsPointX = bsPointX;
	}

	public String getBsPointY() {
		return bsPointY;
	}

	public void setBsPointY(String bsPointY) {
		this.bsPointY = bsPointY;
	}

	public Float getBsGround() {
		return bsGround;
	}

	public void setBsGround(Float bsGround) {
		this.bsGround = bsGround;
	}

	public Float getBsPipeTop() {
		return bsPipeTop;
	}

	public void setBsPipeTop(Float bsPipeTop) {
		this.bsPipeTop = bsPipeTop;
	}

	public Long getBsDiameter() {
		return bsDiameter;
	}

	public void setBsDiameter(Long bsDiameter) {
		this.bsDiameter = bsDiameter;
	}

	public Float getBsBurDepth() {
		return bsBurDepth;
	}

	public void setBsBurDepth(Float bsBurDepth) {
		this.bsBurDepth = bsBurDepth;
	}

	public String getBsConnDirection() {
		return bsConnDirection;
	}

	public void setBsConnDirection(String bsConnDirection) {
		this.bsConnDirection = bsConnDirection;
	}

	public String getBsFacNum() {
		return this.bsFacNum;
	}

	public void setBsFacNum(String bsFacNum) {
		this.bsFacNum = bsFacNum;
	}

	public String getBsFacName() {
		return this.bsFacName;
	}

	public void setBsFacName(String bsFacName) {
		this.bsFacName = bsFacName;
	}

	public String getBsFacType() {
		return this.bsFacType;
	}

	public void setBsFacType(String bsFacType) {
		this.bsFacType = bsFacType;
	}

	public String getBsFacAddr() {
		return this.bsFacAddr;
	}

	public void setBsFacAddr(String bsFacAddr) {
		this.bsFacAddr = bsFacAddr;
	}

	public String getBsFacMaterial() {
		return this.bsFacMaterial;
	}

	public void setBsFacMaterial(String bsFacMaterial) {
		this.bsFacMaterial = bsFacMaterial;
	}

	public Double getBsFacDepth() {
		return this.bsFacDepth;
	}

	public void setBsFacDepth(Double bsFacDepth) {
		this.bsFacDepth = bsFacDepth;
	}

	public String getBsFacMap() {
		return this.bsFacMap;
	}

	public void setBsFacMap(String bsFacMap) {
		this.bsFacMap = bsFacMap;
	}

	public String getBsFacRiName() {
		return this.bsFacRiName;
	}

	public void setBsFacRiName(String bsFacRiName) {
		this.bsFacRiName = bsFacRiName;
	}

	public String getGid() {
		return this.gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Long getBsFacId() {
		return bsFacId;
	}

	public void setBsFacId(Long bsFacId) {
		this.bsFacId = bsFacId;
	}

	public Long getBsFacCaliber() {
		return bsFacCaliber;
	}

	public void setBsFacCaliber(Long bsFacCaliber) {
		this.bsFacCaliber = bsFacCaliber;
	}
	
}