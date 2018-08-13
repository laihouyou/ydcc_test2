package com.movementinsome.app.mytask.mode;

import java.io.Serializable;
import java.util.List;

public class DrePlan implements Serializable {
	private int id;
	private String month;
	private String team;
	private String monitor;
	private String sewagepipe;
	private String rainpipe;
	private String sewagewell;
	private String rainwell;
	private String raininlet;

	private List<DreRoad> dreroads;
	public int getId() {
		return id;
	}
	public String getMonth() {
		return month;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getMonitor() {
		return monitor;
	}
	public void setMonitor(String monitor) {
		this.monitor = monitor;
	}
	public String getSewagepipe() {
		return sewagepipe;
	}
	public void setSewagepipe(String sewagepipe) {
		this.sewagepipe = sewagepipe;
	}
	public String getRainpipe() {
		return rainpipe;
	}
	public void setRainpipe(String rainpipe) {
		this.rainpipe = rainpipe;
	}
	public String getSewagewell() {
		return sewagewell;
	}
	public void setSewagewell(String sewagewell) {
		this.sewagewell = sewagewell;
	}
	public String getRainwell() {
		return rainwell;
	}
	public void setRainwell(String rainwell) {
		this.rainwell = rainwell;
	}
	public String getRaininlet() {
		return raininlet;
	}
	public void setRaininlet(String raininlet) {
		this.raininlet = raininlet;
	}
	public List<DreRoad> getDreroads() {
		return dreroads;
	}
	public void setDreroads(List<DreRoad> dreroads) {
		this.dreroads = dreroads;
	}
	
}
	
