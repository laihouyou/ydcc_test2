package com.movementinsome.database.vo;


import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;



@JsonIgnoreProperties(ignoreUnknown = true) 
public class GeometryVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5144921723408844125L;

	/**
	 * 获取上报设施的信息（类型和坐标点集合）
	 */

	private String type;
	//point List<String> points; points.get(0)=x;points.get(1)=y;
	//polyline/polygan List<List<String>> points; points.get(0).get(0)=x;points.get(0).get(1)=y;
	private List<Object> points;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Object> getPoints() {
		return points;
	}

	public void setPoints(List<Object> points) {
		this.points = points;
	}

}
