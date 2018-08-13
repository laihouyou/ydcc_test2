package com.movementinsome.kernel.updateApk;
/**
 * 获取最新版本信息
 * @author gddst
 *
 */
public class UpdateApkInfo {
	
	private String versionCode;			//版本号 (312)
	private String apkType;				//mainSystem,plugInSystem
	private String versionTime;			//版本时间(更新的时间)
	private String versionName;			//版本名称(gddst3.1.2)
	private String akpName;				//安装apk名称
	private String versionExplain;		//版本说明(更新说明文档，String类型，以 ';' 换行)
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionTime() {
		return versionTime;
	}
	public void setVersionTime(String versionTime) {
		this.versionTime = versionTime;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getAkpName() {
		return akpName;
	}
	public void setAkpName(String akpName) {
		this.akpName = akpName;
	}
	public String getVersionExplain() {
		return versionExplain;
	}
	public void setVersionExplain(String versionExplain) {
		this.versionExplain = versionExplain;
	}
	public String getApkType() {
		return apkType;
	}
	public void setApkType(String apkType) {
		this.apkType = apkType;
	}
	

}