package com.movementinsome.kernel.coordinate;

/**
 * 大地坐标值
 * @author gordon
 *
 */
public class Blh {

	private double b;
	private double l;
	private double h;
	
	public Blh(double b,double l,double h){
		this.b = b;
		this.l = l;
		this.h = h;
	}
	
	public double getB() {
		return b;
	}
	public void setB(double b) {
		this.b = b;
	}
	public double getL() {
		return l;
	}
	public void setL(double l) {
		this.l = l;
	}
	public double getH() {
		return h;
	}
	public void setH(double h) {
		this.h = h;
	}
	
	
}
