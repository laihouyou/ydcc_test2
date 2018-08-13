package com.movementinsome.map.facedit.vo;

import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;

public class FacAttribute {
	private String queryUrl;
	private Legend legend;// 图例
	private ArcGISFeatureLayer featurelayer;// 编辑图层
	private Point editPoint;// 位置
	private Geometry geom;// 图形
	
	public FacAttribute(Legend legend,ArcGISFeatureLayer featurelayer,Point editPoint,Geometry geom){
		this.legend=legend;
		this.featurelayer=featurelayer;
		this.editPoint=editPoint;
		this.geom=geom;
	}
	public FacAttribute(){
		
	}	
	public Legend getLegend() {
		return legend;
	}
	public void setLegend(Legend legend) {
		this.legend = legend;
	}
	public ArcGISFeatureLayer getFeaturelayer() {
		return featurelayer;
	}
	public void setFeaturelayer(ArcGISFeatureLayer featurelayer) {
		this.featurelayer = featurelayer;
	}
	public Point getEditPoint() {
		return editPoint;
	}
	public void setEditPoint(Point editPoint) {
		this.editPoint = editPoint;
	}
	public Geometry getGeom() {
		return geom;
	}
	public void setGeom(Geometry geom) {
		this.geom = geom;
	}
	public String getQueryUrl() {
		return queryUrl;
	}
	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}
	
}
