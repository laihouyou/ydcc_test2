package com.movementinsome.map.nearby;

public class FacTypeObj {

	private String label;
	private String foreign;
	private String local;
	private String [] layerIds;
	
	public String[] getLayerIds() {
		return layerIds;
	}
	public void setLayerIds(String[] layerIds) {
		this.layerIds = layerIds;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getForeign() {
		return foreign;
	}
	public void setForeign(String foreign) {
		this.foreign = foreign;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	
	
}
