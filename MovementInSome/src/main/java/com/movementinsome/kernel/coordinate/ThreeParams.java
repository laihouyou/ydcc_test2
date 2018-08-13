package com.movementinsome.kernel.coordinate;

public class ThreeParams {
	double dx;// X、Y、Z轴的平移参数
	double dy;
	double dz;

	public ThreeParams() {
		dx = 0;
		dy = 0;
		dz = 0;
	}

	public ThreeParams(double dx,double dy,double dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
	}
	public double getDx() {
		return dx;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public double getDz() {
		return dz;
	}

	public void setDz(double dz) {
		this.dz = dz;
	}

}
