package com.movementinsome.kernel.coordinate;

/**
 * 平面直角坐标值
 * @author gordon
 *
 */
public class Xyh {

	private double x;
	private double y;
	private double h;
	
	public Xyh(double x,double y,double h){
		this.x = x;
		this.y = y;
		this.h = h;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getH() {
		return h;
	}
	public void setH(double h) {
		this.h = h;
	}
	
	
}
