package com.movementinsome.database.vo;


import com.esri.core.geometry.Geometry;
import com.movementinsome.map.utils.MapUtil;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsSiteManage")
public class InsSiteManage implements java.io.Serializable {// 工地

	// ID
	@DatabaseField(id = true)
	private String id;
	@DatabaseField
	private Long smId;//工地管理ID
	@DatabaseField
	private String workTaskNum;//任务编号
	@DatabaseField
	private String constructionNum;//施工编号
	@DatabaseField
	private String projectNum;//工程档案编号
	@DatabaseField
	private String projectName;//项目名称
	@DatabaseField
	private String constructionAddr;//详细位置
	@DatabaseField
	private String sections;//所在路段
	@DatabaseField
	private String projectLeader;//项目负责人
	@DatabaseField
	private String constructionUnit;//施工单位
	@DatabaseField
	private String constructionPhone;//施工方电话
	@DatabaseField
	private String prjStartDate;//施工工期开始日期
	@DatabaseField
	private String prjEndDate;//施工工期结束日期
	@DatabaseField
	private String prjBuildUnit;//建设单位
	@DatabaseField
	private String prjBuildLeader;//建设单位负责人
	@DatabaseField
	private String prjBuildPhone;//建设单位负责人电话
	@DatabaseField
	private String supervisionUnit;//监理单位
	@DatabaseField
	private String supervisionLeader;//监理单位负责人
	@DatabaseField
	private String supervisionPhone;//监理单位负责人电话
	@DatabaseField
	private String itLeader;//管网所巡查队负责人
	@DatabaseField
	private String itLeaderPhone;//管网所巡查队负责人电话
	@DatabaseField
	private String irLeader;//管网所综合室负责人
	@DatabaseField
	private String irLeaderPhone;//管网所综合室负责人电话
	@DatabaseField
	private String areaEngineer;//管网部片区工程师
	@DatabaseField
	private String areaEngineerPhone;//管网部片区工程师电话
	@DatabaseField
	private String odEngineer;//管网运营部工程师
	@DatabaseField
	private String odEngineerPhone;//管网运营部工程师电话
	@DatabaseField
	private String prjProgress;//工程进度
	@DatabaseField
	private String outfallLocation;//排放口位置
	@DatabaseField
	private String rnDateAndNum;//开具整改通知书日期及编号
	@DatabaseField
	private String rectificationSituation;//整改情况
	@DatabaseField
	private String createDate;//创建日期
	@DatabaseField
	private Long createUId;//创建人ID
	@DatabaseField
	private String createUNum;//创建人编号
	@DatabaseField
	private String createUName;//创建人名称
	@DatabaseField
	private String lastUpdateDate;//最后更新时间
	@DatabaseField
	private Long lastUpdateUId;//最后更新人ID
	@DatabaseField
	private String lastUpdateUNum;//最后更新人编号
	@DatabaseField
	private String lastUpdateUName;//最后更新人名称
	@DatabaseField
	private String guid;//GUID
	@DatabaseField
	private String frequencyType;//频率类型
	@DatabaseField
	private String frequency;//频率
	@DatabaseField
	private Long frequencyNumber;//次数
	@DatabaseField
	private String frequencyDesc;//频率描述
	@DatabaseField
	private Long handlerId;//处理人ID
	@DatabaseField
	private String handlerNum;//处理人编号
	@DatabaseField
	private String handlerName;//处理人名称
	@DatabaseField
	private String handlerPhone;//处理人电话
	@DatabaseField
	private String handlerDepartment;//处理部门
	@DatabaseField
	private Long state;//状态
	@DatabaseField
	private String serialNumber;//流水号
	@DatabaseField
	private String manageUnit;//管理单位
	@DatabaseField
	private String sectionsNum;//路段编号
	@DatabaseField
	private String prjBound;//工地范围
	@DatabaseField
	private String isFirst;// 未巡:0第一次巡:1
	//最后巡查日期:yyyy-MM-dd
	@DatabaseField
	private String lastInsDateStr;
	//巡查次数
	@DatabaseField
	private Long insCount;
	// 模板
	@DatabaseField
	private String androidForm;
	// 模板
	@DatabaseField
	private String androidForm2;
	// 工地顺序
	@DatabaseField
	private Long siteOrder;
	
	public Long getSiteOrder() {
		return siteOrder;
	}

	public void setSiteOrder(Long siteOrder) {
		this.siteOrder = siteOrder;
	}

	public String getAndroidForm2() {
		return androidForm2;
	}

	public void setAndroidForm2(String androidForm2) {
		this.androidForm2 = androidForm2;
	}

	public String getSectionsNum() {
		return sectionsNum;
	}

	public void setSectionsNum(String sectionsNum) {
		this.sectionsNum = sectionsNum;
	}

	public String getAndroidForm() {
		return androidForm;
	}

	public void setAndroidForm(String androidForm) {
		this.androidForm = androidForm;
	}

	public String getLastInsDateStr() {
		return lastInsDateStr;
	}

	public void setLastInsDateStr(String lastInsDateStr) {
		this.lastInsDateStr = lastInsDateStr;
	}

	public Long getInsCount() {
		return insCount;
	}

	public void setInsCount(Long insCount) {
		this.insCount = insCount;
	}

	public String getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(String isFirst) {
		this.isFirst = isFirst;
	}

	public String getPrjBound() {
		return prjBound;
	}

	public void setPrjBound(String prjBound) {
		this.prjBound = prjBound;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getSmId() {
		return smId;
	}

	public void setSmId(Long smId) {
		this.smId = smId;
	}

	public String getWorkTaskNum() {
		return workTaskNum;
	}

	public void setWorkTaskNum(String workTaskNum) {
		this.workTaskNum = workTaskNum;
	}

	public String getConstructionNum() {
		return constructionNum;
	}

	public void setConstructionNum(String constructionNum) {
		this.constructionNum = constructionNum;
	}

	public String getProjectNum() {
		return projectNum;
	}

	public void setProjectNum(String projectNum) {
		this.projectNum = projectNum;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getConstructionAddr() {
		return constructionAddr;
	}

	public void setConstructionAddr(String constructionAddr) {
		this.constructionAddr = constructionAddr;
	}

	public String getSections() {
		return sections;
	}

	public void setSections(String sections) {
		this.sections = sections;
	}

	public String getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}

	public String getConstructionUnit() {
		return constructionUnit;
	}

	public void setConstructionUnit(String constructionUnit) {
		this.constructionUnit = constructionUnit;
	}

	public String getConstructionPhone() {
		return constructionPhone;
	}

	public void setConstructionPhone(String constructionPhone) {
		this.constructionPhone = constructionPhone;
	}

	public String getPrjStartDate() {
		return prjStartDate;
	}

	public void setPrjStartDate(String prjStartDate) {
		this.prjStartDate = prjStartDate;
	}

	public String getPrjEndDate() {
		return prjEndDate;
	}

	public void setPrjEndDate(String prjEndDate) {
		this.prjEndDate = prjEndDate;
	}

	public String getPrjBuildUnit() {
		return prjBuildUnit;
	}

	public void setPrjBuildUnit(String prjBuildUnit) {
		this.prjBuildUnit = prjBuildUnit;
	}

	public String getPrjBuildLeader() {
		return prjBuildLeader;
	}

	public void setPrjBuildLeader(String prjBuildLeader) {
		this.prjBuildLeader = prjBuildLeader;
	}

	public String getPrjBuildPhone() {
		return prjBuildPhone;
	}

	public void setPrjBuildPhone(String prjBuildPhone) {
		this.prjBuildPhone = prjBuildPhone;
	}

	public String getSupervisionUnit() {
		return supervisionUnit;
	}

	public void setSupervisionUnit(String supervisionUnit) {
		this.supervisionUnit = supervisionUnit;
	}

	public String getSupervisionLeader() {
		return supervisionLeader;
	}

	public void setSupervisionLeader(String supervisionLeader) {
		this.supervisionLeader = supervisionLeader;
	}

	public String getSupervisionPhone() {
		return supervisionPhone;
	}

	public void setSupervisionPhone(String supervisionPhone) {
		this.supervisionPhone = supervisionPhone;
	}

	public String getItLeader() {
		return itLeader;
	}

	public void setItLeader(String itLeader) {
		this.itLeader = itLeader;
	}

	public String getItLeaderPhone() {
		return itLeaderPhone;
	}

	public void setItLeaderPhone(String itLeaderPhone) {
		this.itLeaderPhone = itLeaderPhone;
	}

	public String getIrLeader() {
		return irLeader;
	}

	public void setIrLeader(String irLeader) {
		this.irLeader = irLeader;
	}

	public String getIrLeaderPhone() {
		return irLeaderPhone;
	}

	public void setIrLeaderPhone(String irLeaderPhone) {
		this.irLeaderPhone = irLeaderPhone;
	}

	public String getAreaEngineer() {
		return areaEngineer;
	}

	public void setAreaEngineer(String areaEngineer) {
		this.areaEngineer = areaEngineer;
	}

	public String getAreaEngineerPhone() {
		return areaEngineerPhone;
	}

	public void setAreaEngineerPhone(String areaEngineerPhone) {
		this.areaEngineerPhone = areaEngineerPhone;
	}

	public String getOdEngineer() {
		return odEngineer;
	}

	public void setOdEngineer(String odEngineer) {
		this.odEngineer = odEngineer;
	}

	public String getOdEngineerPhone() {
		return odEngineerPhone;
	}

	public void setOdEngineerPhone(String odEngineerPhone) {
		this.odEngineerPhone = odEngineerPhone;
	}

	public String getPrjProgress() {
		return prjProgress;
	}

	public void setPrjProgress(String prjProgress) {
		this.prjProgress = prjProgress;
	}

	public String getOutfallLocation() {
		return outfallLocation;
	}

	public void setOutfallLocation(String outfallLocation) {
		this.outfallLocation = outfallLocation;
	}

	public String getRnDateAndNum() {
		return rnDateAndNum;
	}

	public void setRnDateAndNum(String rnDateAndNum) {
		this.rnDateAndNum = rnDateAndNum;
	}

	public String getRectificationSituation() {
		return rectificationSituation;
	}

	public void setRectificationSituation(String rectificationSituation) {
		this.rectificationSituation = rectificationSituation;
	}
	
	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Long getCreateUId() {
		return createUId;
	}

	public void setCreateUId(Long createUId) {
		this.createUId = createUId;
	}

	public String getCreateUNum() {
		return createUNum;
	}

	public void setCreateUNum(String createUNum) {
		this.createUNum = createUNum;
	}

	public String getCreateUName() {
		return createUName;
	}

	public void setCreateUName(String createUName) {
		this.createUName = createUName;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Long getLastUpdateUId() {
		return lastUpdateUId;
	}

	public void setLastUpdateUId(Long lastUpdateUId) {
		this.lastUpdateUId = lastUpdateUId;
	}

	public String getLastUpdateUNum() {
		return lastUpdateUNum;
	}

	public void setLastUpdateUNum(String lastUpdateUNum) {
		this.lastUpdateUNum = lastUpdateUNum;
	}

	public String getLastUpdateUName() {
		return lastUpdateUName;
	}

	public void setLastUpdateUName(String lastUpdateUName) {
		this.lastUpdateUName = lastUpdateUName;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
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

	public Long getState() {
		return state;
	}

	public void setState(Long state) {
		this.state = state;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getManageUnit() {
		return manageUnit;
	}

	public void setManageUnit(String manageUnit) {
		this.manageUnit = manageUnit;
	}
	
	public Geometry getShape(){
		return MapUtil.wkt2Geometry(prjBound);
	}
}