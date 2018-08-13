package com.movementinsome.database.vo;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "InsDatumInaccurate")
public class InsDatumInaccurate implements Serializable{

	// ID
	@DatabaseField(id = true)
	private Long diId;//图与现场不符ID
	
	@DatabaseField
	private String workTaskNum;//任务编号
	
	@DatabaseField
	private String sources;//信息来源
	
	@DatabaseField
	private String deviceType;//设备类型
	
	@DatabaseField
	private String problemType;//问题类型
	
	@DatabaseField
	private String problemDesc;//问题描述
	
	@DatabaseField
	private String reportedDate;//反映时间
	
	@DatabaseField
	private String reportedPerson;//反映人
	
	@DatabaseField
	private String phone;//联系电话
	
	@DatabaseField
	private String coordinate;//坐标
	
	@DatabaseField
	private String newCoordinate;//新坐标
	
	@DatabaseField
	private String facilitiesNum;//设施编号
	
	@DatabaseField
	private Long facilitiesCaliber;//设施口径
	
	@DatabaseField
	private String facilitiesMaterial;//设施材质
	
	@DatabaseField
	private String gid;//GID
	
	@DatabaseField
	private Long taskCompletion;//任务完成
	
	@DatabaseField
	private String remarks;//备注
	
	@DatabaseField
	private String guid;//GUID
	
	@DatabaseField
	private String reportedCoordinate;//上报坐标点
	
	@DatabaseField
	private String serialNumber;//流水号
	
	@DatabaseField
	private String manageUnit;//管理单位
	
	@DatabaseField
	private String createDate;//创建日期
	
	@DatabaseField
	private String createDateStart;//创建日期
	
	@DatabaseField
	private String createDateEnd;//创建日期
	
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
    private Long deptId;//部门ID

	@DatabaseField
    private String deptNum;//部门编号
    
	@DatabaseField
    private String deptName;//部门名称
    
	@DatabaseField
    private Long teamId;//班组ID
    
	@DatabaseField
    private String teamNum;//班组编号
    
	@DatabaseField
    private String teamName;//班组名称
    
	@DatabaseField
	private String moiNum;//移动端唯一编码
	
	@DatabaseField
	private String phoneImei;//移动端机器码
	
	@DatabaseField
	private String sections;//所在路段
	
	@DatabaseField
	private String sectionsNum;//路段编号
	
	//处理结果
	@DatabaseField
	private String handleResult;
	//处理备注
	@DatabaseField
	private String handleRemarks;
	//处理时间
	@DatabaseField
	private String handleDate;
	
	
	public InsDatumInaccurate(){}
	
//	public Long getDiId() {
//		return diId;
//	}
//	public void setDiId(Long diId) {
//		this.diId = diId;
//	}
	public String getSources() {
		return sources;
	}
//	public void setSources(String sources) {
//		this.sources = sources;
//	}
	public String getDeviceType() {
		return deviceType;
	}
//	public void setDeviceType(String deviceType) {
//		this.deviceType = deviceType;
//	}
	public String getProblemType() {
		return problemType;
	}
	public void setProblemType(String problemType) {
		this.problemType = problemType;
	}
	public String getProblemDesc() {
		return problemDesc;
	}
	public void setProblemDesc(String problemDesc) {
		this.problemDesc = problemDesc;
	}
	
	public String getReportedPerson() {
		if(reportedPerson==null){
			reportedPerson = createUName;
		}
		return reportedPerson;
	}
	public void setReportedPerson(String reportedPerson) {
		this.reportedPerson = reportedPerson;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(String coordinate) {
		this.coordinate = coordinate;
	}
//	public String getNewCoordinate() {
//		return newCoordinate;
//	}
//	public void setNewCoordinate(String newCoordinate) {
//		this.newCoordinate = newCoordinate;
//	}
	public String getFacilitiesNum() {
		return facilitiesNum;
	}
//	public void setFacilitiesNum(String facilitiesNum) {
//		this.facilitiesNum = facilitiesNum;
//	}
	public Long getFacilitiesCaliber() {
		return facilitiesCaliber;
	}
//	public void setFacilitiesCaliber(Long facilitiesCaliber) {
//		this.facilitiesCaliber = facilitiesCaliber;
//	}
	public String getFacilitiesMaterial() {
		return facilitiesMaterial;
	}
//	public void setFacilitiesMaterial(String facilitiesMaterial) {
//		this.facilitiesMaterial = facilitiesMaterial;
//	}
//	public String getGid() {
//		return gid;
//	}
//	public void setGid(String gid) {
//		this.gid = gid;
//	}
//	public Long getTaskCompletion() {
//		return taskCompletion==null?0L:1;
//	}
//	public void setTaskCompletion(Long taskCompletion) {
//		this.taskCompletion = taskCompletion;
//	}
	public String getRemarks() {
		return remarks;
	}
//	public void setRemarks(String remarks) {
//		this.remarks = remarks;
//	}
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
//	public String getReportedCoordinate() {
//		return reportedCoordinate;
//	}
//	public void setReportedCoordinate(String reportedCoordinate) {
//		this.reportedCoordinate = reportedCoordinate;
//	}
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

//	public Long getCreateUId() {
//		return createUId;
//	}
//	public void setCreateUId(Long createUId) {
//		this.createUId = createUId;
//	}
//	public String getCreateUNum() {
//		return createUNum;
//	}
//	public void setCreateUNum(String createUNum) {
//		this.createUNum = createUNum;
//	}
//	public String getCreateUName() {
//		return createUName;
//	}
//	public void setCreateUName(String createUName) {
//		this.createUName = createUName;
//	}

//	public Long getLastUpdateUId() {
//		return lastUpdateUId;
//	}
//	public void setLastUpdateUId(Long lastUpdateUId) {
//		this.lastUpdateUId = lastUpdateUId;
//	}
//	public String getLastUpdateUNum() {
//		return lastUpdateUNum;
//	}
//	public void setLastUpdateUNum(String lastUpdateUNum) {
//		this.lastUpdateUNum = lastUpdateUNum;
//	}
//	public String getLastUpdateUName() {
//		return lastUpdateUName;
//	}
//	public void setLastUpdateUName(String lastUpdateUName) {
//		this.lastUpdateUName = lastUpdateUName;
//	}
//    public Long getDeptId() {
//        return deptId;
//    }

//    public void setDeptId(Long deptId) {
//        this.deptId = deptId;
//    }

//    public String getDeptNum() {
//        return deptNum;
//    }

//    public void setDeptNum(String deptNum) {
//        this.deptNum = deptNum;
//    }

//    public String getDeptName() {
//        return deptName;
//    }

//    public void setDeptName(String deptName) {
//        this.deptName = deptName;
//    }

//    public Long getTeamId() {
//        return teamId;
//    }

//    public void setTeamId(Long teamId) {
//        this.teamId = teamId;
//    }

//    public String getTeamNum() {
//        return teamNum;
//    }

//    public void setTeamNum(String teamNum) {
//        this.teamNum = teamNum;
//    }

//    public String getTeamName() {
//        return teamName;
//    }

//    public void setTeamName(String teamName) {
//        this.teamName = teamName;
//    }

//	public String getMoiNum() {
//		return moiNum;
//	}
//	public void setMoiNum(String moiNum) {
//		this.moiNum = moiNum;
//	}
//	public String getPhoneImei() {
//		return phoneImei;
//	}
//	public void setPhoneImei(String phoneImei) {
//		this.phoneImei = phoneImei;
//	}

//	public String getWorkTaskNum() {
//		return workTaskNum;
//	}

//	public void setWorkTaskNum(String workTaskNum) {
//		this.workTaskNum = workTaskNum;
//	}

	public String getSections() {
		return sections;
	}

	public void setSections(String sections) {
		this.sections = sections;
	}

	public String getSectionsNum() {
		return sectionsNum;
	}

//	public void setSectionsNum(String sectionsNum) {
//		this.sectionsNum = sectionsNum;
//	}

//	public void setLinkedNum(String linkedNum) {
//	}

//	public String getCreateDateStart() {
//		return createDateStart;
//	}

//	public void setCreateDateStart(String createDateStart) {
//		this.createDateStart = createDateStart;
//	}

//	public String getCreateDateEnd() {
//		return createDateEnd;
//	}

//	public void setCreateDateEnd(String createDateEnd) {
//		this.createDateEnd = createDateEnd;
//	}

	public String getHandleResult() {
		return handleResult;
	}

//	public void setHandleResult(String handleResult) {
//		this.handleResult = handleResult;
//	}

//	public String getHandleRemarks() {
//		return handleRemarks;
//	}

//	public void setHandleRemarks(String handleRemarks) {
//		this.handleRemarks = handleRemarks;
//	}

	public String getReportedDate() {
		return reportedDate;
	}

	public void setReportedDate(String reportedDate) {
		this.reportedDate = reportedDate;
	}

//	public String getCreateDate() {
//		return createDate;
//	}

//	public void setCreateDate(String createDate) {
//		this.createDate = createDate;
//	}

//	public String getLastUpdateDate() {
//		return lastUpdateDate;
//	}

//	public void setLastUpdateDate(String lastUpdateDate) {
//		this.lastUpdateDate = lastUpdateDate;
//	}

	public String getHandleDate() {
		return handleDate;
	}

//	public void setHandleDate(String handleDate) {
//		this.handleDate = handleDate;
//	}
	
	

}
