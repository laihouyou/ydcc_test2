package com.movementinsome.app.mytask.mode;

import java.io.Serializable;

public class DreRecord implements Serializable {
	private int rid;
	private String workdate;
    private String worktype;//截污点,检查井、雨水口、管渠、明沟
	private String stime;
	private String etime;
	private String sewagepipe;
	private String rainpipe;
	private String sewagewell;
	private String rainwell;
	private String raininlet;
	private String qty;
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public String getWorkdate() {
		return workdate;
	}
	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}
	public String getWorktype() {
		return worktype;
	}
	public void setWorktype(String worktype) {
		this.worktype = worktype;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
	public String getEtime() {
		return etime;
	}
	public void setEtime(String etime) {
		this.etime = etime;
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
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	

}
