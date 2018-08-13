package com.movementinsome.database.vo;

import java.util.List;

public class MapparentmenuVO {
	private String id;// 标示
	private String name;// 显示名称
	private boolean isdisplay;// 是否显示
	private List<MapmenuVO> mapmenuList;
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
	public boolean isIsdisplay() {
		return isdisplay;
	}
	public void setIsdisplay(boolean isdisplay) {
		this.isdisplay = isdisplay;
	}
	public List<MapmenuVO> getMapmenuList() {
		return mapmenuList;
	}
	public void setMapmenuList(List<MapmenuVO> mapmenuList) {
		this.mapmenuList = mapmenuList;
	}
	
}
