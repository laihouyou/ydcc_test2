package com.movementinsome.kernel.initial.model;

import java.util.ArrayList;
import java.util.List;

public class Module {

	private String id;
	private String name;
	private ArrayList<Ftlayer> layers;
	private String formtype;
	private String template;
	private String icon;
	private String ftlayer;
	private boolean theme;
	private boolean canwrite;
	private String linemodule;  //相关联的module id值，用“，”隔
	private String history;	// 是否在历史列表中显示（true false）
	private String isShowDeleteBnt; // 是否显示保存按钮
	private String url;
	private List<Property> properties = new ArrayList();
	
	private MenuClassify menu;

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getIsShowDeleteBnt() {
		return isShowDeleteBnt;
	}
	public void setIsShowDeleteBnt(String isShowDeleteBnt) {
		this.isShowDeleteBnt = isShowDeleteBnt;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Ftlayer> getLayers() {
		return layers;
	}

	public void setLayers(ArrayList<Ftlayer> layers) {
		this.layers = layers;
	}
	
	public String getFormtype() {
		return formtype;
	}

	public void setFormtype(String formtype) {
		this.formtype = formtype;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getFtlayer() {
		return ftlayer;
	}

	public void setFtlayer(String ftlayer) {
		this.ftlayer = ftlayer;
	}

	public boolean isTheme() {
		return theme;
	}

	public void setTheme(boolean theme) {
		this.theme = theme;
	}

	public boolean isCanwrite() {
		return canwrite;
	}

	public void setCanwrite(boolean canwrite) {
		this.canwrite = canwrite;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public MenuClassify getMenu() {
		return menu;
	}

	public void setMenu(MenuClassify menu) {
		this.menu = menu;
	}

	public String getLinemodule() {
		return linemodule;
	}

	public void setLinemodule(String linemodule) {
		this.linemodule = linemodule;
	}

	
}
