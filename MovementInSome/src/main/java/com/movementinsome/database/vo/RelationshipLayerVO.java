package com.movementinsome.database.vo;



import java.io.Serializable;
import java.util.List;

public class RelationshipLayerVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8171563146773232710L;
	// 子表ID
	private String relationshipId;
	// 表名
	private String tableName;
	// 中文名
	private String layerName;
	// update/all/none 图层可以编辑的类型
	private String editType;
	// 内网查询路径
	private String localQueryUrl;
	// 内网编辑路径
	private String localEditUrl;
	// 外网查询路径
	private String foreignQueryUrl;
	// 外网编辑路径
	private String foreignEditUrl;
	// 表字段描述
	private List<MngUsertableVO> mngUsertables;
	// 表字段的下拉项
	private List<MngSyslistVO> mngSyslist;

	public String getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public String getLocalQueryUrl() {
		return localQueryUrl;
	}

	public void setLocalQueryUrl(String localQueryUrl) {
		this.localQueryUrl = localQueryUrl;
	}

	public String getLocalEditUrl() {
		return localEditUrl;
	}

	public void setLocalEditUrl(String localEditUrl) {
		this.localEditUrl = localEditUrl;
	}

	public String getForeignQueryUrl() {
		return foreignQueryUrl;
	}

	public void setForeignQueryUrl(String foreignQueryUrl) {
		this.foreignQueryUrl = foreignQueryUrl;
	}

	public String getForeignEditUrl() {
		return foreignEditUrl;
	}

	public void setForeignEditUrl(String foreignEditUrl) {
		this.foreignEditUrl = foreignEditUrl;
	}

	public List<MngUsertableVO> getMngUsertables() {
		return mngUsertables;
	}

	public void setMngUsertables(List<MngUsertableVO> mngUsertables) {
		this.mngUsertables = mngUsertables;
	}

	public List<MngSyslistVO> getMngSyslist() {
		return mngSyslist;
	}

	public void setMngSyslist(List<MngSyslistVO> mngSyslist) {
		this.mngSyslist = mngSyslist;
	}

}
