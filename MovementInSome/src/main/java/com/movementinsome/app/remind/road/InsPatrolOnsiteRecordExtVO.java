package com.movementinsome.app.remind.road;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Polyline;
import com.movementinsome.app.remind.nav.NavLocalGeoData;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.database.vo.InsPatrolOnsiteRecordVO;
import com.movementinsome.map.utils.MapUtil;

public class InsPatrolOnsiteRecordExtVO extends InsPatrolOnsiteRecordVO {
	private double minDistance = 0.00;
	private int preEnterState=0; // 前一路段进入路段状态0:未进入,1:已进入, 2:偏离 ,3:暂停
	private int enterState=0;   //进入路段状态0:未进入,1:已进入, 2:偏离 ,3:暂停
	private String warnState= String.valueOf("00");   //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
	private String memo;
	private NearObject curNearObj;
	private Polyline runTrace;
	private Polyline preRunTrace;
	
	
	private String wktGeom;

	public double getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(double minDistance) {
		this.minDistance = minDistance;
	}

	public int getPreEnterState() {
		return preEnterState;
	}

	public void setPreEnterState(int preEnterState) {
		this.preEnterState = preEnterState;
	}

	public int getEnterState() {
		return enterState;
	}

	public void setEnterState(int enterState) {
		this.enterState = enterState;
	}

	
	public String getWarnState() {
		return warnState;
	}

	public void setWarnState(String warnState) {
		this.warnState = warnState;
	}

	public String getWktGeom() {
		return wktGeom;
	}

	public void setWktGeom(String wktGeom) {
		this.wktGeom = wktGeom;
	}
	
	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public NearObject getCurNearObj() {
		return curNearObj;
	}

	public void setCurNearObj(NearObject curNearObj) {
		this.curNearObj = curNearObj;
	}
	
	public Polyline getRunTrace() {
		return runTrace;
	}

	public void setRunTrace(Polyline runTrace) {
		this.runTrace = runTrace;
	}
	
	public Polyline getPreRunTrace() {
		return preRunTrace;
	}

	public void setPreRunTrace(Polyline preRunTrace) {
		this.preRunTrace = preRunTrace;
	}

	public Geometry getRoadShape() {
		return MapUtil.wkt2Geometry(wktGeom);
	}
	
	public Geometry getNowRunRoadShape() {
		return NavLocalGeoData.hadRunRoad(this.runTrace, MapUtil.wkt2Geometry(wktGeom));
	}
	
	public Polyline getHadRunTrace(){
		return NavLocalGeoData.hadRunTrace(this.preRunTrace,this.runTrace);
	}
	
	public Geometry getHadRunRoadShape() {
		return NavLocalGeoData.hadRunRoad(getHadRunTrace(), MapUtil.wkt2Geometry(wktGeom));
	}

	public String getHadRunRoadShapeStr() {
		Geometry geo = NavLocalGeoData.hadRunRoad(getHadRunTrace(), MapUtil.wkt2Geometry(wktGeom));
		return MapUtil.geometry2WKT(geo);
	}
	
	public String getHashCode(){
		return this.getRoadName()+String.valueOf((int)this.getMinDistance());
	}
}
