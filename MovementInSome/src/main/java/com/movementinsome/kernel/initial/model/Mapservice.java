package com.movementinsome.kernel.initial.model;

import java.io.Serializable;

import com.esri.core.map.ImageServiceParameters.IMAGE_FORMAT;
import com.movementinsome.AppContext;

public class Mapservice implements Serializable{

	private int id;
	private String name;
	private String label;
	private LAYERTYPE type;
	private boolean visible;
	private int alpha;
	private IMAGE_FORMAT format;
	private String local;
	private String foreign;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public LAYERTYPE getType() {
		return type;
	}

	public void setType(LAYERTYPE type) {
		this.type = type;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public IMAGE_FORMAT getFormat() {
		return format;
	}

	public void setFormat(IMAGE_FORMAT format) {
		this.format = format;
	}

	public String getLocal() {
		if (this.getType() != null && this.getType() == LAYERTYPE.local) {// 离线模式，本地sdcard
			return "file://" + AppContext.getInstance().getAppStoreMapPath() + this.local;
		}else{
			return local;
		}
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getForeign() {
		if (this.getType() != null && this.getType() == LAYERTYPE.local) {// 离线模式，本地sdcard
			return "file://" + AppContext.getInstance().getAppStoreMapPath() + this.foreign;
		}else{
			return foreign;
		}
	}

	public void setForeign(String foreign) {
		this.foreign = foreign;
	}	
}
