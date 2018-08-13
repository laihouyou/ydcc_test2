package com.movementinsome.kernel.initial.model;

import java.io.Serializable;
import java.util.List;

public class Field implements Serializable {

	private String name;
	private String alias;//设备编号
	private boolean visible;
	private String editType;
	private String synopsis;
	private List<String> dropList;
	

	public String getSynopsis() {
		return synopsis;
	}

	public void setSynopsis(String synopsis) {
		this.synopsis = synopsis;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public String getEditType() {
		return editType;
	}

	public void setEditType(String editType) {
		this.editType = editType;
	}

	public List<String> getDropList() {
		return dropList;
	}

	public void setDropList(List<String> dropList) {
		this.dropList = dropList;
	}

}
