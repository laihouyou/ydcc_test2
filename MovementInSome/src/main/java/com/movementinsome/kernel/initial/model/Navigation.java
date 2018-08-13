package com.movementinsome.kernel.initial.model;

public class Navigation {
	private Integer gpsBuf = 50; // gps缓冲计算半径
	private Integer arrive = 40; // 到达目标缓冲半径
	private Integer roadBuf = 40; // 噵路宽模拟半径，用于将道路线形成面
	private Integer accuracy = 20; //设置采信的GPS精度值,20表示误差值在20以内
	private boolean isTwoWay = false; //是否双向巡查为一次
	private double tolerance;// 容差值，未巡长度容忍值  ,值为0表示没有容差，1表示不受限

	public Integer getGpsBuf() {
		return gpsBuf;
	}

	public void setGpsBuf(Integer gpsBuf) {
		this.gpsBuf = gpsBuf;
	}

	public Integer getArrive() {
		return arrive;
	}

	public void setArrive(Integer arrive) {
		this.arrive = arrive;
	}

	public Integer getRoadBuf() {
		return roadBuf;
	}

	public void setRoadBuf(Integer roadBuf) {
		this.roadBuf = roadBuf;
	}

	public Integer getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(Integer accuracy) {
		this.accuracy = accuracy;
	}

	public boolean isTwoWay() {
		return isTwoWay;
	}

	public void setTwoWay(boolean isTwoWay) {
		this.isTwoWay = isTwoWay;
	}

	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}
}
