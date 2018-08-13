package com.movementinsome.database.vo;



import java.util.Date;


/**
 * InsTerminalInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class InsTerminalInfoVO implements java.io.Serializable {

	// Fields

	private Long itiId;
	private String itiMdn;
	private String itiName;
	private String versionName;
	private String itiIcon;
	private Long itiState;
	private Long itiType;
	private Date createDate;
	
	// Property accessors

	public Long getItiId() {
		return this.itiId;
	}

	public void setItiId(Long itiId) {
		this.itiId = itiId;
	}

	public String getItiMdn() {
		return this.itiMdn;
	}

	public void setItiMdn(String itiMdn) {
		this.itiMdn = itiMdn;
	}

	public String getItiName() {
		return this.itiName;
	}

	public void setItiName(String itiName) {
		this.itiName = itiName;
	}

	public String getItiIcon() {
		return this.itiIcon;
	}

	public void setItiIcon(String itiIcon) {
		this.itiIcon = itiIcon;
	}

	public Long getItiState() {
		return this.itiState;
	}

	public void setItiState(Long itiState) {
		this.itiState = itiState;
	}

	public Long getItiType() {
		return this.itiType;
	}

	public void setItiType(Long itiType) {
		this.itiType = itiType;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}