package com.movementinsome.database.vo;



import java.io.Serializable;
import java.util.List;



public class ConstructionVO implements Serializable {
	
	/*<mapservice name="gz_ground" label="地图"
		type="tiled" visible="true" alpha="1" format="png24"
		local="http://172.16.0.77:8399/arcgis/rest/services/ora_gz_ground_mxd_cache/MapServer"
		foreign="http://172.16.0.77:8399/arcgis/rest/services/ora_gz_ground_mxd_cache/MapServer">
	</mapservice>*/
	/**
	 * 
	 */
	private static final long serialVersionUID = -5799245785877978200L;
	
	private String name;
	
	private String label;
	
	private String type;
	
	private String visible;
	
	private String alpha;
	
	private String format;
	//内网
	private String local;
	//外网
	private String foreign;
	
	private String[] viewLayers;
	
	private List<LayerInfo> layers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVisible() {
		return visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}

	public String getAlpha() {
		return alpha;
	}

	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
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

	public List<LayerInfo> getLayers() {
		return layers;
	}

	public void setLayers(List<LayerInfo> layers) {
		this.layers = layers;
	}

	public String[] getViewLayers() {
		return viewLayers;
	}

	public void setViewLayers(String[] viewLayers) {
		this.viewLayers = viewLayers;
	}
	
}
