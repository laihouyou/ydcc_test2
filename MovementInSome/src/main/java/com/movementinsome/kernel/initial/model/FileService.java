package com.movementinsome.kernel.initial.model;

public class FileService {
	private String local;
	
	private String foreign;

	private boolean compress;
	
	private String type;
	
	private String config;
	
	private String camreaMessage;
	
	private String labelLocation;
	
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

	public boolean isCompress() {
		return compress;
	}

	public void setCompress(boolean compress) {
		this.compress = compress;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getCamreaMessage() {
		return camreaMessage;
	}

	public void setCamreaMessage(String camreaMessage) {
		this.camreaMessage = camreaMessage;
	}

	public String getLabelLocation() {
		return labelLocation;
	}

	public void setLabelLocation(String labelLocation) {
		this.labelLocation = labelLocation;
	}
	
	
}
