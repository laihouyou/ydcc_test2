package com.movementinsome.database.vo;



import java.util.List;

/**
 * 现场施工反馈
 * 
 * @author gddst
 * 
 */
public class InsTableSiteOperationFeedBackVO {

	// 任务编号(流水号)
	private String taskNumber;
	// 处理人
	private String userName;
	// 处理人电话
	private String userPhone;
	// 处理部门（所属班组）
	private String userClass;
	// 处理内容
	private String content;
	// 备注
	private String remarks;
	// 图片组
	private List<PhotoInfoVO> imageBaseCode;
	// 图形
	private GeometryVO geometry;
	// 管理单位
	private String managedept;
	// 处理结果
	private String result;
	// gid
	private String gid;
	// 设施编号(GIS编号)
	private String facilitiesNum;
	// 设施名称
	private String facilitiesName;
	// 所在道路(具体位置)
	private String facilitiesAddr;
	// 设施口径
	private String facilitiesCaliber;
	// 设施材质
	private String facilitiesMaterial;
	// 移动端唯一编码
	private String moiNum;
	// 处理时间
	private String repairingDateStr;
	// 未完成原因
	private String unfinishReason;

	public String getTaskNumber() {
		return taskNumber;
	}

	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserClass() {
		return userClass;
	}

	public void setUserClass(String userClass) {
		this.userClass = userClass;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public List<PhotoInfoVO> getImageBaseCode() {
		return imageBaseCode;
	}

	public void setImageBaseCode(List<PhotoInfoVO> imageBaseCode) {
		this.imageBaseCode = imageBaseCode;
	}

	public GeometryVO getGeometry() {
		return geometry;
	}

	public void setGeometry(GeometryVO geometry) {
		this.geometry = geometry;
	}

	public String getManagedept() {
		return managedept;
	}

	public void setManagedept(String managedept) {
		this.managedept = managedept;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getFacilitiesNum() {
		return facilitiesNum;
	}

	public void setFacilitiesNum(String facilitiesNum) {
		this.facilitiesNum = facilitiesNum;
	}

	public String getFacilitiesName() {
		return facilitiesName;
	}

	public void setFacilitiesName(String facilitiesName) {
		this.facilitiesName = facilitiesName;
	}

	public String getFacilitiesAddr() {
		return facilitiesAddr;
	}

	public void setFacilitiesAddr(String facilitiesAddr) {
		this.facilitiesAddr = facilitiesAddr;
	}

	public String getFacilitiesCaliber() {
		return facilitiesCaliber;
	}

	public void setFacilitiesCaliber(String facilitiesCaliber) {
		this.facilitiesCaliber = facilitiesCaliber;
	}

	public String getFacilitiesMaterial() {
		return facilitiesMaterial;
	}

	public void setFacilitiesMaterial(String facilitiesMaterial) {
		this.facilitiesMaterial = facilitiesMaterial;
	}

	public String getMoiNum() {
		return moiNum;
	}

	public void setMoiNum(String moiNum) {
		this.moiNum = moiNum;
	}

	public String getRepairingDateStr() {
		return repairingDateStr;
	}

	public void setRepairingDateStr(String repairingDateStr) {
		this.repairingDateStr = repairingDateStr;
	}

	public String getUnfinishReason() {
		return unfinishReason;
	}

	public void setUnfinishReason(String unfinishReason) {
		this.unfinishReason = unfinishReason;
	}

}
