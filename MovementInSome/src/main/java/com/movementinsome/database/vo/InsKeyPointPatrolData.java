package com.movementinsome.database.vo;


import com.esri.core.geometry.Geometry;
import com.movementinsome.map.utils.MapUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsKeyPointPatrolData")
public class InsKeyPointPatrolData implements java.io.Serializable {// 关键点巡查

	// ID
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private Long kppdId;//关键点巡查数据ID
	@DatabaseField
	private String frequencyType;//频率类型
	@DatabaseField
	private String frequency;//频率
	@DatabaseField
	private Long frequencyNumber;//次数
	@DatabaseField
	private String frequencyDesc;//频率描述
	@DatabaseField
	private String serialNumber;//流水号
	@DatabaseField
	private String workTaskNum;//任务编号
	@DatabaseField
	private String lastInsDate;//最后巡查时间
	@DatabaseField
	private Long insCount;//巡查次数
	@DatabaseField
	private String manageUnit;//管理单位
	@DatabaseField
	private String archiveNum;//档案号
	@DatabaseField
	private String kpaName;//关键点名称
	@DatabaseField
	private String tableName;//图层表名（英文）
	@DatabaseField
	private String facName;//设施名称
	@DatabaseField
	private String facNum;//设施编号
	@DatabaseField
	private String gid;//GIS内部编号
	@DatabaseField
	private Long objectid;//OBJECTID
	@DatabaseField
	private String coordinate;//坐标
	@DatabaseField
	private String kpaPosition;//关键点位置
	@DatabaseField
	private String sections;//所在路段
	@DatabaseField
	private String kpaAddr;//详细位置描述
	@DatabaseField
	private String kpaType;//关键点类型
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getKppdId() {
		return kppdId;
	}

	public void setKppdId(Long kppdId) {
		this.kppdId = kppdId;
	}

	public String getFrequencyType() {
		return frequencyType;
	}

	public void setFrequencyType(String frequencyType) {
		this.frequencyType = frequencyType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Long getFrequencyNumber() {
		return frequencyNumber;
	}

	public void setFrequencyNumber(Long frequencyNumber) {
		this.frequencyNumber = frequencyNumber;
	}

	public String getFrequencyDesc() {
		return frequencyDesc;
	}

	public void setFrequencyDesc(String frequencyDesc) {
		this.frequencyDesc = frequencyDesc;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public String getLastInsDate() {
		return lastInsDate;
	}

	public void setLastInsDate(String lastInsDate) {
		this.lastInsDate = lastInsDate;
	}

	public Long getInsCount() {
		return insCount;
	}

	public void setInsCount(Long insCount) {
		this.insCount = insCount;
	}

	public String getManageUnit() {
		return manageUnit;
	}

	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}

	public String getArchiveNum() {
		return archiveNum;
	}

	public void setArchiveNum(String archiveNum) {
		this.archiveNum = archiveNum;
	}

	public String getKpaName() {
		return kpaName;
	}

	public void setKpaName(String kpaName) {
		this.kpaName = kpaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFacName() {
		return facName;
	}

	public void setFacName(String facName) {
		this.facName = facName;
	}

	public String getFacNum() {
		return facNum;
	}

	public void setFacNum(String facNum) {
		this.facNum = facNum;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public Long getObjectid() {
		return objectid;
	}

	public void setObjectid(Long objectid) {
		this.objectid = objectid;
	}

	public String getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getKpaPosition() {
		return kpaPosition;
	}

	public void setKpaPosition(String kpaPosition) {
		this.kpaPosition = kpaPosition;
	}

	public String getSections() {
		return sections;
	}

	public void setSections(String sections) {
		this.sections = sections;
	}

	public String getKpaAddr() {
		return kpaAddr;
	}

	public void setKpaAddr(String kpaAddr) {
		this.kpaAddr = kpaAddr;
	}

	public String getKpaType() {
		return kpaType;
	}

	public void setKpaType(String kpaType) {
		this.kpaType = kpaType;
	}
	
	public Geometry getShape(){
		return MapUtil.wkt2Geometry(kpaPosition);
	}
	
}