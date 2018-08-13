package com.movementinsome.kernel.initial.model;

import java.util.List;

public class Task {

	private String id;
	private String name;
	private String label;
	private int tolerance;
	private String local;
	private String foreign;
	private List<Ftlayer> layers;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public int getTolerance() {
		return tolerance;
	}

	public void setTolerance(int tolerance) {
		this.tolerance = tolerance;
	}

	public List<Ftlayer> getLayers() {
		return layers;
	}

	public void setLayers(List<Ftlayer> layers) {
		this.layers = layers;
	}

}
