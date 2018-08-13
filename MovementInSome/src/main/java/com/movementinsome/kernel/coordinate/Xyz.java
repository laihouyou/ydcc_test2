package com.movementinsome.kernel.coordinate;

/**
 * 空间直角坐标值
 * @author gordon
 *
 */
public class Xyz {

	private double x;
	private double y;
	private double z;
	
	public Xyz(double x,double y,double z){
		this.x = x;
		this.y = y;
		this.z = z;
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
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
	
	
}
