package com.movementinsome.kernel.initial.model;

import java.io.Serializable;
import java.util.List;

public class Ftlayer implements Serializable {

	private int id;
	private int layerId = -1;//图层在地图服务中的ID
	private String layerIds = "";//分组图层在地图服务中的ID串,以“，”进行分隔
	private String featureServerId;// 设施编辑id
	private int relationshipId;// 子表关联id
	private List<Field> relationshipFields;// 子表字段
	private String name;
	private Mapservice mapservice;
	private String tablename;
	private String keyfield;	//远程配置文件字段
	private boolean isWrite;   //是否允许编辑
	/**featurelayer内部URL
	 * featurelayer外部URL  这两字段在navftlayers使用,有了这两个配制mapservice不需要
	 */
	private String local;        
	private String foreign;      
	private List<Field> fields;

	public List<Field> getRelationshipFields() {
		return relationshipFields;
	}
	public void setRelationshipFields(List<Field> relationshipFields) {
		this.relationshipFields = relationshipFields;
	}
	public int getRelationshipId() {
		return relationshipId;
	}
	public void setRelationshipId(int relationshipId) {
		this.relationshipId = relationshipId;
	}
	public String getFeatureServerId() {
		return featureServerId;
	}
	public void setFeatureServerId(String featureServerId) {
		this.featureServerId = featureServerId;
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	public int getLayerId() {
		return layerId;
	}

	public void setLayerId(int layerId) {
		this.layerId = layerId;
	}

	public String getLayerIds() {
		return layerIds;
	}

	public void setLayerIds(String layerIds) {
		this.layerIds = layerIds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Mapservice getMapservice() {
		return mapservice;
	}

	public void setMapservice(Mapservice mapservice) {
		this.mapservice = mapservice;
	}

	public String getTablename() {
		return tablename;
	}

	public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	public String getKeyfield() {
		return keyfield;
	}

	public void setKeyfield(String keyfield) {
		this.keyfield = keyfield;
	}
	
	public boolean isWrite() {
		return isWrite;
	}

	public void setWrite(boolean isWrite) {
		this.isWrite = isWrite;
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

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public String getArrayFields() {
		String flds = "";
		for(Field filed: fields){
			if ("".equals(flds)){
				flds = filed.getName();
			}else{
				flds += ","+filed.getName();
			}
		}
		return flds;
	}
}
