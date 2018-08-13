package com.movementinsome.map.facSublistEdit.vo;

import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Point;

public class FacAttribute {
	private ArcGISFeatureLayer featurelayer;// 编辑图层
	private String queryUrl;
	private Point editPoint;// 位置
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
	public String getQueryUrl() {
		return queryUrl;
	}
	public void setQueryUrl(String queryUrl) {
		this.queryUrl = queryUrl;
	}
}
