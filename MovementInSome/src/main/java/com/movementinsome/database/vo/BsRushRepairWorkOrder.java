package com.movementinsome.database.vo;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "BsRushRepairWorkOrder")
public class BsRushRepairWorkOrder implements java.io.Serializable {
	// Fields
	private static final long serialVersionUID = -1898093607618250858L;
	// 抢维工单ID
	@DatabaseField(id = true)
	private Long rrwoId;
	// 通知单编号
	@DatabaseField
	private String woNum;
	// 工单编号
	@DatabaseField
	private String rrwoNum;
	// 工程计划编号
	@DatabaseField
	private String planPrjNum;
	// 工程类型
	@DatabaseField
	private String prjType;
	// 现场工作负责人id
	@DatabaseField
	private Long siteHeadUId;
	// 现场工作负责人编号
	@DatabaseField
	private String siteHeadUNum;
	// 现场工作负责人名称
	@DatabaseField
	private String siteHeadUName;
	// 工作班成员
	@DatabaseField
	private String workMember;
	// 设施编号
	@DatabaseField
	private String facNum;
	// 设施名称
	@DatabaseField
	private String facType;
	// 口径
	@DatabaseField
	private Long facDs;
	// 材质
	@DatabaseField
	private String facMaterial;
	// 埋深
	@DatabaseField
	private Double facDepth;
	// GID
	@DatabaseField
	private String gid;
	// 处理地点
	@DatabaseField
	private String handleLocation;
	// 是否抢修
	@DatabaseField
	private Long isRushRepair;
	// 施工方信息（外单位）
	@DatabaseField
	private String constructionInfo;
	// 施工方联系人（外单位）
	@DatabaseField
	private String constructionContactPerson;
	// 施工方联系电话（外单位）
	@DatabaseField
	private String constructionTel;
	// 维修类别
	@DatabaseField
	private String rrType;
	// 维修内容
	@DatabaseField
	private String rrContent;
	// 处理结果
	@DatabaseField
	private String handleResult;
	// 派遣人部门名称
	@DatabaseField
	private String sendDeptName;
	// 派遣人名称
	@DatabaseField
	private String sendPersonName;
	// 派遣时间
	@DatabaseField
	private String sendDateStr;
	// 接单时间
	@DatabaseField
	private String receiveDateStr;
	// 到场时间
	@DatabaseField
	private String sceneTimeStr;
	// 完成时间
	@DatabaseField
	private String completeDateStr;
	// 处理人部门名称
	@DatabaseField
	private String handleDeptName;
	// 处理人班组名称
	@DatabaseField
	private String handleTeamName;
	// 处理人名称
	@DatabaseField
	private String handlePersonName;
	// 处理情况
	@DatabaseField
	private String handleSituation;
	// 是否开挖
	@DatabaseField
	private Long isExcavation;
	// 备注
	@DatabaseField
	private String remark;
	// 工单状态
	@DatabaseField
	private Long status;
	// 是否延期
	@DatabaseField
	private Long isExtension;
	// GUID
	@DatabaseField
	private String guid;
	// 维修坐标
	@DatabaseField
	private String coordinate;
	// 终端上报坐标
	@DatabaseField
	private String reportCoordinate;
	// 移动端机器码
	@DatabaseField
	private String imeiCode;
	// 移动端唯一编码
	@DatabaseField
	private String uniqueCode;
	

	public Long getRrwoId() {
		return this.rrwoId;
	}

	public void setRrwoId(Long rrwoId) {
		this.rrwoId = rrwoId;
	}

	public String getWoNum() {
		return this.woNum;
	}

	public void setWoNum(String woNum) {
		this.woNum = woNum;
	}

	public String getRrwoNum() {
		return this.rrwoNum;
	}

	public void setRrwoNum(String rrwoNum) {
		this.rrwoNum = rrwoNum;
	}

	public String getPlanPrjNum() {
		return this.planPrjNum;
	}

	public void setPlanPrjNum(String planPrjNum) {
		this.planPrjNum = planPrjNum;
	}

	public String getPrjType() {
		return this.prjType;
	}

	public void setPrjType(String prjType) {
		this.prjType = prjType;
	}

	public Long getSiteHeadUId() {
		return this.siteHeadUId;
	}

	public void setSiteHeadUId(Long siteHeadUId) {
		this.siteHeadUId = siteHeadUId;
	}

	public String getSiteHeadUNum() {
		return this.siteHeadUNum;
	}

	public void setSiteHeadUNum(String siteHeadUNum) {
		this.siteHeadUNum = siteHeadUNum;
	}

	public String getSiteHeadUName() {
		return this.siteHeadUName;
	}

	public void setSiteHeadUName(String siteHeadUName) {
		this.siteHeadUName = siteHeadUName;
	}

	public String getWorkMember() {
		return this.workMember;
	}

	public void setWorkMember(String workMember) {
		this.workMember = workMember;
	}

	public String getFacNum() {
		return this.facNum;
	}

	public void setFacNum(String facNum) {
		this.facNum = facNum;
	}

	public String getFacType() {
		return this.facType;
	}

	public void setFacType(String facType) {
		this.facType = facType;
	}

	public Long getFacDs() {
		return this.facDs;
	}

	public void setFacDs(Long facDs) {
		this.facDs = facDs;
	}

	public String getFacMaterial() {
		return this.facMaterial;
	}

	public void setFacMaterial(String facMaterial) {
		this.facMaterial = facMaterial;
	}

	public Double getFacDepth() {
		return this.facDepth;
	}

	public void setFacDepth(Double facDepth) {
		this.facDepth = facDepth;
	}

	public String getGid() {
		return this.gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getHandleLocation() {
		return this.handleLocation;
	}

	public void setHandleLocation(String handleLocation) {
		this.handleLocation = handleLocation;
	}

	public Long getIsRushRepair() {
		return this.isRushRepair;
	}

	public void setIsRushRepair(Long isRushRepair) {
		this.isRushRepair = isRushRepair;
	}

	public String getConstructionInfo() {
		return this.constructionInfo;
	}

	public void setConstructionInfo(String constructionInfo) {
		this.constructionInfo = constructionInfo;
	}

	public String getConstructionContactPerson() {
		return this.constructionContactPerson;
	}

	public void setConstructionContactPerson(String constructionContactPerson) {
		this.constructionContactPerson = constructionContactPerson;
	}

	public String getConstructionTel() {
		return this.constructionTel;
	}

	public void setConstructionTel(String constructionTel) {
		this.constructionTel = constructionTel;
	}

	public String getRrType() {
		return this.rrType;
	}

	public void setRrType(String rrType) {
		this.rrType = rrType;
	}

	public String getRrContent() {
		return this.rrContent;
	}

	public void setRrContent(String rrContent) {
		this.rrContent = rrContent;
	}

	public String getHandleResult() {
		return this.handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	public String getSendDeptName() {
		return this.sendDeptName;
	}

	public void setSendDeptName(String sendDeptName) {
		this.sendDeptName = sendDeptName;
	}
	public String getSendPersonName() {
		return this.sendPersonName;
	}

	public void setSendPersonName(String sendPersonName) {
		this.sendPersonName = sendPersonName;
	}

	public String getHandleDeptName() {
		return this.handleDeptName;
	}

	public void setHandleDeptName(String handleDeptName) {
		this.handleDeptName = handleDeptName;
	}

	public String getHandleTeamName() {
		return this.handleTeamName;
	}

	public void setHandleTeamName(String handleTeamName) {
		this.handleTeamName = handleTeamName;
	}
	public String getHandlePersonName() {
		return this.handlePersonName;
	}

	public void setHandlePersonName(String handlePersonName) {
		this.handlePersonName = handlePersonName;
	}

	public String getHandleSituation() {
		return this.handleSituation;
	}

	public void setHandleSituation(String handleSituation) {
		this.handleSituation = handleSituation;
	}

	public Long getIsExcavation() {
		return this.isExcavation;
	}

	public void setIsExcavation(Long isExcavation) {
		this.isExcavation = isExcavation;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getStatus() {
		return this.status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public Long getIsExtension() {
		return this.isExtension;
	}

	public void setIsExtension(Long isExtension) {
		this.isExtension = isExtension;
	}

	public String getGuid() {
		return this.guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getCoordinate() {
		return this.coordinate;
	}

	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}

	public String getReportCoordinate() {
		return this.reportCoordinate;
	}

	public void setReportCoordinate(String reportCoordinate) {
		this.reportCoordinate = reportCoordinate;
	}

	public String getImeiCode() {
		return this.imeiCode;
	}

	public void setImeiCode(String imeiCode) {
		this.imeiCode = imeiCode;
	}

	public String getUniqueCode() {
		return this.uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}


	public String getSendDateStr() {
		return sendDateStr;
	}

	public void setSendDateStr(String sendDateStr) {
		
		this.sendDateStr = sendDateStr;
	}

	public String getReceiveDateStr() {
		return receiveDateStr;
	}

	public void setReceiveDateStr(String receiveDateStr) {
		this.receiveDateStr = receiveDateStr;
	}

	public String getSceneTimeStr() {
		
		return sceneTimeStr;
	}

	public void setSceneTimeStr(String sceneTimeStr) {
		this.sceneTimeStr = sceneTimeStr;
	}

	public String getCompleteDateStr() {
		return completeDateStr;
	}

	public void setCompleteDateStr(String completeDateStr) {
		
		this.completeDateStr = completeDateStr;
	}

}