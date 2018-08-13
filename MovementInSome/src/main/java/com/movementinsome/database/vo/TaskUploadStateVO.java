package com.movementinsome.database.vo;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author gddst
 *
 */
@DatabaseTable(tableName = "TaskUploadStateVO")
public class TaskUploadStateVO implements Serializable{
	// ID(gui)
	@DatabaseField(id = true)
	private String guid;
	@DatabaseField
	private String moiNum;// 终端上传数据的唯一编码
	@DatabaseField
	private String imei;// 手机唯一编码
	@DatabaseField
	private String taskCategory;// 表单类型
	@DatabaseField
	private String tableName;// 表单在数据库中表名
	@DatabaseField
	private String usId;// 用户ID
	@DatabaseField
	private String usUsercode;// 用户编号
	@DatabaseField
	private String usNameZh;// 用户名称
	@DatabaseField
	private String updateDate;// 修改日期
	@DatabaseField
	private String gpsCoord;// 坐标值,"经度，纬度"
	@DatabaseField
	private String mapCoord;// 地图相应坐标值,"x，y"
	@DatabaseField
	private String taskNum;// 任务编号
	@DatabaseField
	private String status;// 最新状态
	@DatabaseField
	private String order;// 状态队列序号
	
	
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public String getMoiNum() {
		return moiNum;
	}
	public void setMoiNum(String moiNum) {
		this.moiNum = moiNum;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getTaskCategory() {
		return taskCategory;
	}
	public void setTaskCategory(String taskCategory) {
		this.taskCategory = taskCategory;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getUsId() {
		return usId;
	}
	public void setUsId(String usId) {
		this.usId = usId;
	}
	public String getUsUsercode() {
		return usUsercode;
	}
	public void setUsUsercode(String usUsercode) {
		this.usUsercode = usUsercode;
	}
	public String getUsNameZh() {
		return usNameZh;
	}
	public void setUsNameZh(String usNameZh) {
		this.usNameZh = usNameZh;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getGpsCoord() {
		return gpsCoord;
	}
	public void setGpsCoord(String gpsCoord) {
		this.gpsCoord = gpsCoord;
	}
	public String getMapCoord() {
		return mapCoord;
	}
	public void setMapCoord(String mapCoord) {
		this.mapCoord = mapCoord;
	}
	public String getTaskNum() {
		return taskNum;
	}
	public void setTaskNum(String taskNum) {
		this.taskNum = taskNum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
