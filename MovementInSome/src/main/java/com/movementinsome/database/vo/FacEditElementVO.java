package com.movementinsome.database.vo;



import java.io.Serializable;
import java.util.List;

public class FacEditElementVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7626099205285806674L;
	// 图层表名
	private String tableName;
	// 图层中文名
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
	// 子表
	private List<RelationshipLayerVO> relationships;

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

	public List<RelationshipLayerVO> getRelationships() {
		return relationships;
	}

	public void setRelationships(List<RelationshipLayerVO> relationships) {
		this.relationships = relationships;
	}
}
