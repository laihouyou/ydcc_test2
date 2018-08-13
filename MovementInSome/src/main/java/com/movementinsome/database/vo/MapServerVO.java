package com.movementinsome.database.vo;



import java.io.Serializable;


public class MapServerVO implements Serializable {
	
	/*
	<mapservice name="gz_ground" label="地图"
		type="tiled" visible="true" alpha="1" format="png24"
		local="http://172.16.0.77:8399/arcgis/rest/services/ora_gz_ground_mxd_cache/MapServer"
		foreign="http://172.16.0.77:8399/arcgis/rest/services/ora_gz_ground_mxd_cache/MapServer">
	</mapservice>
	
	<mapservice label="矢量图" alpha="1" type="tiled" format="png24" checked="true"
		local="http://172.16.0.77:8399/arcgis/rest/services/ora_gz_ground_mxd_cache/MapServer"
		foreign="http://gddst2013.no-ip.org:18399/arcgis/rest/services/ora_gz_ground_mxd_cache/MapServer">
	</mapservice>
	*/
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
	//地图切换使用默认显示底图
	private String checked;

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

	public String getChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		this.checked = checked;
	}
	
}
