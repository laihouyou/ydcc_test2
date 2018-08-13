package com.movementinsome.kernel.coordinate;

public class SevenParams {

    double dx ;//X、Y、Z轴的平移参数
    double dy ;
    double dz ;
    double rx ;// X、Y、Z轴旋转参数
    double ry ;
    double rz ;
    double m ;// 尺度因子
    
	double getDx() {
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
	public double getRx() {
		return rx;
	}
	public void setRx(double rx) {
		this.rx = rx;
	}
	public double getRy() {
		return ry;
	}
	public void setRy(double ry) {
		this.ry = ry;
	}
	public double getRz() {
		return rz;
	}
	public void setRz(double rz) {
		this.rz = rz;
	}
	public double getM() {
		return m;
	}
	public void setM(double m) {
		this.m = m;
	}
    
    
}
