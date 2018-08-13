package com.movementinsome.database.vo;



import java.io.Serializable;

public class GroupWaterMeterVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -550688510261778144L;
	// 内网
	private String local;
	// 外网
	private String foreign;
	
	private String relationshipId;
	
	private String outFields;

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

	public String getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(String relationshipId) {
		this.relationshipId = relationshipId;
	}

	public String getOutFields() {
		return outFields;
	}

	public void setOutFields(String outFields) {
		this.outFields = outFields;
	}
	
}
