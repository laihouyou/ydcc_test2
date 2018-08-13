package com.movementinsome.database.vo;


import java.util.List;

public class LayerInfo  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1896721954064987852L;

	private String layerKey;

	private String layerId;

	private String layerName;

	private String tableName;

	private String serverName;

	/*
	 * alias:"要素编码" name:"OBJECTID" type:"esriFieldTypeOID" state:0不显示/1显示
	 * layerId:数据库存储用 serverName:数据库存储用
	 */
	private List<FieldInfo> fields;
	
	/*
	 * alias:"要素编码" name:"OBJECTID" type:"esriFieldTypeOID" state:null
	 * layerId:数据库存储用 serverName:数据库存储用
	 */
	private List<FieldInfo> queryFields;

	// 内网
	private String local;
	// 外网
	private String foreign;

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<FieldInfo> getFields() {
		return fields;
	}

	public void setFields(List<FieldInfo> fields) {
		this.fields = fields;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getLayerKey() {
		return layerKey;
	}

	public void setLayerKey(String layerKey) {
		this.layerKey = layerKey;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getForeign() {
		return foreign;
	}

	public void setForeign(String foreign) {
		this.foreign = foreign;
	}

	public List<FieldInfo> getQueryFields() {
		return queryFields;
	}

	public void setQueryFields(List<FieldInfo> queryFields) {
		this.queryFields = queryFields;
	}
}
