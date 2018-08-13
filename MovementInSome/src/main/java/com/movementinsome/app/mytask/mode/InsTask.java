package com.movementinsome.app.mytask.mode;

import java.io.Serializable;
import java.util.List;

public class InsTask implements Serializable {
	private int id;
	private String tasknum;
	private String areanum;
	private String area;
	private String near;
	private String weather;
	private String freq;
	private List<InsRoad> roads;
	private List<InsBuilder> builders;
	private List<InsKeyPoint> keypoints;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	
	public String getNear() {
		return near;
	}
	public void setNear(String near) {
		this.near = near;
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getFreq() {
		return freq;
	}
	public void setFreq(String freq) {
		this.freq = freq;
	}
	public List<InsRoad> getRoads() {
		return roads;
	}
	public void setRoads(List<InsRoad> roads) {
		this.roads = roads;
	}
	public String getTasknum() {
		return tasknum;
	}
	public void setTasknum(String tasknum) {
		this.tasknum = tasknum;
	}
	public String getAreanum() {
		return areanum;
	}
	public void setAreanum(String areanum) {
		this.areanum = areanum;
	}
	public List<InsBuilder> getBuilders() {
		return builders;
	}
	public void setBuilders(List<InsBuilder> builders) {
		this.builders = builders;
	}
	public List<InsKeyPoint> getKeypoints() {
		return keypoints;
	}
	public void setKeypoints(List<InsKeyPoint> keypoints) {
		this.keypoints = keypoints;
	}

	
	
}
