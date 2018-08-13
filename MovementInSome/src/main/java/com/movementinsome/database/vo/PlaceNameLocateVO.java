package com.movementinsome.database.vo;

import java.io.Serializable;
import java.util.List;

public class PlaceNameLocateVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6507453119874069011L;
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

	public String getForeign() {
		return foreign;
	}

	public void setForeign(String foreign) {
		this.foreign = foreign;
	}

	public List<LayerInfo> getLayers() {
		return layers;
	}

	public void setLayers(List<LayerInfo> layers) {
		this.layers = layers;
	}
	
	
}
