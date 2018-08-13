package com.movementinsome.kernel.initial.model;

public class AutoStart {

	private boolean boot;
	
	private String timing;

	public boolean isBoot() {
		return boot;
	}

	public void setBoot(boolean boot) {
		this.boot = boot;
	}

	public String getTiming() {
		return timing;
	}

	public void setTiming(String timing) {
		this.timing = timing;
	}
	
}
