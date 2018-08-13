package com.movementinsome.database.vo;

import java.io.Serializable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class PicFileInfoVO implements Serializable {

	private static final long serialVersionUID = 5244729016698119952L;
	// 唯一编码(GUID)
	private String guid;
	// 业务类型
	private String businessType;
	// 本地空间位置
	private String position;
	// GPS空间位置
	private String gpsPosition;
	// 拍照时间
	//private Date photographedDate;
	private String photographedDateStr;
	// 文件名
	private String pfiName;
	// 文件大小
	private Double pfiSize;
	// 文件类型（PNG、GIF、JPG）
	private String pfiType;
	// 操作人名称
	private String operUName;
	// 操作类型（上传、下载、查看）
	// private String operType;
	// 操作时间
	//private Date operDate;
	private String operDateStr;
	// 关键字（类型）
	private Map<String, Object> keywords = null;
	// 是否已上传
	private String isUpload;

	
	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getGpsPosition() {
		return gpsPosition;
	}

	public void setGpsPosition(String gpsPosition) {
		this.gpsPosition = gpsPosition;
	}

	public String getPfiName() {
		return pfiName;
	}

	public void setPfiName(String pfiName) {
		this.pfiName = pfiName;
	}

	public Double getPfiSize() {
		return pfiSize;
	}

	public void setPfiSize(Double pfiSize) {
		this.pfiSize = pfiSize;
	}

	public String getPfiType() {
		return pfiType;
	}

	public void setPfiType(String pfiType) {
		this.pfiType = pfiType;
	}

	public String getOperUName() {
		return operUName;
	}

	public void setOperUName(String operUName) {
		this.operUName = operUName;
	}

	/*
	 * public String getOperType() { return operType; } public void
	 * setOperType(String operType) { this.operType = operType; }
	 */

	public Map<String, Object> getKeywords() {
		return keywords;
	}

	public String getPhotographedDateStr() {
		return photographedDateStr;
	}

	public void setPhotographedDateStr(String photographedDateStr) {
		this.photographedDateStr = photographedDateStr;
	}

	public String getOperDateStr() {
		return operDateStr;
	}

	public void setOperDateStr(String operDateStr) {
		this.operDateStr = operDateStr;
	}

	public void setKeywords(Map<String, Object> keywords) {
		this.keywords = keywords;
	}

	public String toJson() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("guid", this.getGuid());
			ob.put("businessType", this.getBusinessType());
			ob.put("position", this.getPosition());
			ob.put("gpsPosition", this.getGpsPosition());
			ob.put("photographedDate", this.getPhotographedDateStr());
			ob.put("pfiName", this.getPfiName());
			ob.put("pfiSize", this.getPfiSize());
			ob.put("pfiType", this.getPfiType());
			ob.put("operUName", this.getOperUName());
			ob.put("operDate", this.getOperDateStr());
			ob.put("keywords", this.getKeywords());

		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return ob.toString();
	}
}
