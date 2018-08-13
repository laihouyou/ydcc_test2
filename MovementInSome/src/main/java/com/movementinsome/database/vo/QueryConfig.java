package com.movementinsome.database.vo;


import java.util.List;

public class QueryConfig  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -647440414238159660L;

	// 内网
	private String local;
	// 外网
	private String foreign;

	private List<LayerInfo> layers;

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public List<LayerInfo> getLayers() {
		return layers;
	}

	public void setLayers(List<LayerInfo> layers) {
		this.layers = layers;
	}

	public String getForeign() {
		return foreign;
	}

	public void setForeign(String foreign) {
		this.foreign = foreign;
	}

}
