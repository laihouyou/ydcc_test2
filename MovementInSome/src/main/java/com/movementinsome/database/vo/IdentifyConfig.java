package com.movementinsome.database.vo;


import java.util.List;

public class IdentifyConfig  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3800851182618580727L;

	// 内网
	private String local;
	// 外网
	private String foreign;

	private String layerOption;

	private List<LayerInfo> layers;

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getLayerOption() {
		return layerOption;
	}

	public void setLayerOption(String layerOption) {
		this.layerOption = layerOption;
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
