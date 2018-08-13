package com.movementinsome.app.remind.nav;

import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.movementinsome.AppContext;

public class NearObject {

	private String objId = null;

	private String objNum = null;

	private String objName = null;

	private double minDistance = 0.00;

	private String mapXY;

	private String dateTime;

	private String wktGeom;
	// 路段状态
	private String state;

	private Point coordinate; // 最近点坐标
	
	private Polyline preRunTrace;

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	public String getObjNum() {
		return objNum;
	}

	public void setObjNum(String objNum) {
		this.objNum = objNum;
	}

	public String getObjName() {
		return objName;
	}

	public void setObjName(String objName) {
		this.objName = objName;
	}

	public double getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}

	public String getMapXY() {
		return mapXY;
	}

	public void setMapXY(String mapXY) {
		this.mapXY = mapXY;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public String getWktGeom() {
		return wktGeom;
	}

	public void setWktGeom(String wktGeom) {
		this.wktGeom = wktGeom;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Point getCoordinate() {
		return coordinate;
	}

	public void setCoordinate(Point coordinate) {
		this.coordinate = coordinate;
	}

	
	public Polyline getPreRunTrace() {
		return preRunTrace;
	}

	public void setPreRunTrace(Polyline preRunTrace) {
		this.preRunTrace = preRunTrace;
	}

	// 进入判断
	public boolean isCanPost() {
		return (this.getMinDistance() <= AppContext.getInstance()
				.getNavigation().getArrive())
				&& (null != this.getObjName());
	}

	// 偏离警告
	public boolean isOutWarn() {
		return (this.getMinDistance() > AppContext.getInstance()
				.getNavigation().getArrive())
				&& (this.getMinDistance() < AppContext.getInstance()
						.getNavigation().getRoadBuf())
				&& (null != this.getObjName());
	}

	// 偏离警告
	public boolean isOutWarn2(String targetCoord) {
		String[] tCoord = targetCoord.split("\\ ");
		String[] sCoord = this.getMapXY().split("\\ ");

		double x1 = Double.valueOf(tCoord[0]), y1 = Double.valueOf(tCoord[1]), x2 = Double
				.valueOf(sCoord[0]), y2 = Double.valueOf(sCoord[1]);
		double temp_A, temp_B;
		double c; // 用来储存算出来的斜边距离
		temp_A = x1 > x2 ? (x1 - x2) : (x2 - x1); // 横向距离 (取正数，因为边长不能是负数)
		temp_B = y1 > y2 ? (y1 - y2) : (y2 - y1); // 竖向距离 (取正数，因为边长不能是负数)
		c = java.lang.Math.sqrt(temp_A * temp_A + temp_B * temp_B); // 计算

		return (c > AppContext.getInstance().getNavigation().getArrive())
				&& (this.getMinDistance() < AppContext.getInstance()
						.getNavigation().getRoadBuf())
				&& (null != this.getObjName());
	}

	// 偏离警告
	public boolean isOutWarn3(String wktGeom) {
		String pointXY = this.getMapXY();
		double c = NavLocalGeoData.cacDistance(pointXY, wktGeom);
		return (c > AppContext.getInstance().getNavigation().getArrive())
				&& (this.getMinDistance() < AppContext.getInstance()
						.getNavigation().getRoadBuf())
				&& (null != this.getObjName());
	}

	// 偏离
	public boolean isDeviate() {
		return (this.getMinDistance() > AppContext.getInstance()
				.getNavigation().getRoadBuf());
	}

	// 根据目标值起点坐标计算是否偏离
	public boolean isDeviate2(String targetCoord) {
		String[] tCoord = targetCoord.split("\\ ");
		String[] sCoord = this.getMapXY().split("\\ ");

		double x1 = Double.valueOf(tCoord[0]), y1 = Double.valueOf(tCoord[1]), x2 = Double
				.valueOf(sCoord[0]), y2 = Double.valueOf(sCoord[1]);
		double temp_A, temp_B;
		double c; // 用来储存算出来的斜边距离
		temp_A = x1 > x2 ? (x1 - x2) : (x2 - x1); // 横向距离 (取正数，因为边长不能是负数)
		temp_B = y1 > y2 ? (y1 - y2) : (y2 - y1); // 竖向距离 (取正数，因为边长不能是负数)
		c = java.lang.Math.sqrt(temp_A * temp_A + temp_B * temp_B); // 计算

		return (c > AppContext.getInstance().getNavigation().getRoadBuf());
	}

	// 根据目标值起点坐标计算是否偏离
	public boolean isDeviate3(String wktGeom) {
		String pointXY = this.getMapXY();
		double c = NavLocalGeoData.cacDistance(pointXY, wktGeom);
		return (c > AppContext.getInstance().getNavigation().getRoadBuf());
	}
}
