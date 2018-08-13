package com.movementinsome.app.mytask.mode;

import java.io.Serializable;
import java.util.List;

public class DreRoad  implements Serializable{
	private int rid;
	private String name;
	private String sewagepipe;
	private String rainpipe;
	private String sewagewell;
	private String rainwell;
	private String raininlet;
	private String lvl;
	private String state;
	private List<DreRecord> drerecords;
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getLvl() {
		return lvl;
	}
	public void setLvl(String lvl) {
		this.lvl = lvl;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}	
	public List<DreRecord> getDrerecords() {
		return drerecords;
	}
	public void setDrerecords(List<DreRecord> drerecords) {
		this.drerecords = drerecords;
	}
	
}

