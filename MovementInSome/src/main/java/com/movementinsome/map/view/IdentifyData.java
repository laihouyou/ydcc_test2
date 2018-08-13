package com.movementinsome.map.view;

import java.util.ArrayList;

import com.esri.core.tasks.identify.IdentifyResult;
import com.movementinsome.kernel.initial.model.Ftlayer;

public class IdentifyData {

	private int serviceId;
	private int layerId;
	private String layerName;
	private Ftlayer ftLayer;  //图层对象
	private ArrayList<IdentifyResult> data = new ArrayList();
	
	public int getServiceId() {
		return serviceId;
	}
	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}
	public int getLayerId() {
		return layerId;
	}
	public void setLayerId(int layerId) {
		this.layerId = layerId;
	}
	public String getLayerName() {
		return layerName;
	}
	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}
	
	public Ftlayer getFtLayer() {
		return ftLayer;
	}
	public void setFtLayer(Ftlayer ftLayer) {
		this.ftLayer = ftLayer;
	}
	public ArrayList<IdentifyResult> getData() {
		return data;
	}
	public void setData(ArrayList<IdentifyResult> data) {
		this.data = data;
	}
	
	
}
