package com.movementinsome.caice.vo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "MiningSurveyDataVO")
public class  MiningSurveyDataVO {

	//录入时间
	@DatabaseField(id = true)
	private Long entryTime;

	//设备编号
	@DatabaseField
	private String facNum;

	//设备名称
	@DatabaseField
	private String facName;

	//设备类型  (区分是point还是line)
	@DatabaseField
	private String facType;

	//设备口径
	@DatabaseField
	private String caliber;

	//提交时间
	@DatabaseField
	private String commitDate;

	//设备属性（设备类型）
	@DatabaseField
	private String facattribute;

	//埋深
	@DatabaseField
	private String buriedDepth;

	//坐标（百度）
	@DatabaseField
	private String coordinatesBaidu;

	//坐标（Arcgis）
	@DatabaseField
	private String coordinatesArcgis;

	//地面高程
	@DatabaseField
	private String groundHigh;

	//地址
	@DatabaseField
	private String addr;

	//行政区
	@DatabaseField
	private String administrativeArea;

	//敷设类型
	@DatabaseField
	private String layingType;

	//特征点
	@DatabaseField
	private String featurePoint;

	//坐标集合（百度）
	@DatabaseField
	private String pointListBaidu;

	//坐标集合（arcgis）
	@DatabaseField
	private String pointListArcgis;

	//管材
	@DatabaseField
	private String tubeStock;

	//管线类型
	@DatabaseField
	private String lineType;

	//父工程id
	@DatabaseField
	private String parentProjectId;

	//每一条线段的长度
	@DatabaseField
	private double  lineLength;

	//保存activity退出时的工程ID
	@DatabaseField
	private String lastProjectId;

	//预留8
	@DatabaseField
	private String obligate8;

	//预留9
	@DatabaseField
	private String obligate9;

	//预留10
	@DatabaseField
	private String obligate10;

	public Long getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Long entryTime) {
		this.entryTime = entryTime;
	}

	public String getFacNum() {
		return facNum;
	}

	public void setFacNum(String facNum) {
		this.facNum = facNum;
	}

	public String getFacName() {
		return facName;
	}

	public void setFacName(String facName) {
		this.facName = facName;
	}

	public String getFacType() {
		return facType;
	}

	public void setFacType(String facType) {
		this.facType = facType;
	}

	public String getCaliber() {
		return caliber;
	}

	public void setCaliber(String caliber) {
		this.caliber = caliber;
	}

	public String getCommitDate() {
		return commitDate;
	}

	public void setCommitDate(String commitDate) {
		this.commitDate = commitDate;
	}

	public String getFacattribute() {
		return facattribute;
	}

	public void setFacattribute(String facattribute) {
		this.facattribute = facattribute;
	}

	public String getBuriedDepth() {
		return buriedDepth;
	}

	public void setBuriedDepth(String buriedDepth) {
		this.buriedDepth = buriedDepth;
	}

	public String getCoordinatesBaidu() {
		return coordinatesBaidu;
	}

	public void setCoordinatesBaidu(String coordinatesBaidu) {
		this.coordinatesBaidu = coordinatesBaidu;
	}

	public String getCoordinatesArcgis() {
		return coordinatesArcgis;
	}

	public void setCoordinatesArcgis(String coordinatesArcgis) {
		this.coordinatesArcgis = coordinatesArcgis;
	}

	public String getGroundHigh() {
		return groundHigh;
	}

	public void setGroundHigh(String groundHigh) {
		this.groundHigh = groundHigh;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getAdministrativeArea() {
		return administrativeArea;
	}

	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	public String getLayingType() {
		return layingType;
	}

	public void setLayingType(String layingType) {
		this.layingType = layingType;
	}

	public String getFeaturePoint() {
		return featurePoint;
	}

	public void setFeaturePoint(String featurePoint) {
		this.featurePoint = featurePoint;
	}

	public String getPointListBaidu() {
		return pointListBaidu;
	}

	public void setPointListBaidu(String pointListBaidu) {
		this.pointListBaidu = pointListBaidu;
	}

	public String getPointListArcgis() {
		return pointListArcgis;
	}

	public void setPointListArcgis(String pointListArcgis) {
		this.pointListArcgis = pointListArcgis;
	}

	public String getTubeStock() {
		return tubeStock;
	}

	public void setTubeStock(String tubeStock) {
		this.tubeStock = tubeStock;
	}

	public String getLineType() {
		return lineType;
	}

	public void setLineType(String lineType) {
		this.lineType = lineType;
	}

	public String getParentProjectId() {
		return parentProjectId;
	}

	public void setParentProjectId(String parentProjectId) {
		this.parentProjectId = parentProjectId;
	}

	public double getLineLength() {
		return lineLength;
	}

	public void setLineLength(double lineLength) {
		this.lineLength = lineLength;
	}

	public String getLastProjectId() {
		return lastProjectId;
	}

	public void setLastProjectId(String lastProjectId) {
		this.lastProjectId = lastProjectId;
	}

	public String getObligate8() {
		return obligate8;
	}

	public void setObligate8(String obligate8) {
		this.obligate8 = obligate8;
	}

	public String getObligate9() {
		return obligate9;
	}

	public void setObligate9(String obligate9) {
		this.obligate9 = obligate9;
	}

	public String getObligate10() {
		return obligate10;
	}

	public void setObligate10(String obligate10) {
		this.obligate10 = obligate10;
	}

	@Override
	public String toString() {
		return "MiningSurveyDataVO{" +
				"entryTime=" + entryTime +
				", facNum='" + facNum + '\'' +
				", facName='" + facName + '\'' +
				", facType='" + facType + '\'' +
				", caliber='" + caliber + '\'' +
				", commitDate='" + commitDate + '\'' +
				", facattribute='" + facattribute + '\'' +
				", buriedDepth='" + buriedDepth + '\'' +
				", coordinatesBaidu='" + coordinatesBaidu + '\'' +
				", coordinatesArcgis='" + coordinatesArcgis + '\'' +
				", groundHigh='" + groundHigh + '\'' +
				", addr='" + addr + '\'' +
				", administrativeArea='" + administrativeArea + '\'' +
				", layingType='" + layingType + '\'' +
				", featurePoint='" + featurePoint + '\'' +
				", pointListBaidu='" + pointListBaidu + '\'' +
				", pointListArcgis='" + pointListArcgis + '\'' +
				", tubeStock='" + tubeStock + '\'' +
				", lineType='" + lineType + '\'' +
				", parentProjectId='" + parentProjectId + '\'' +
				", lineLength=" + lineLength +
				", lastProjectId='" + lastProjectId + '\'' +
				", obligate8='" + obligate8 + '\'' +
				", obligate9='" + obligate9 + '\'' +
				", obligate10='" + obligate10 + '\'' +
				'}';
	}
}
