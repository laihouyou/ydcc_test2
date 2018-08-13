package com.movementinsome.map.utils;

import java.util.ArrayList;
import java.util.List;

import com.movementinsome.database.vo.GeometryVO;


public class GeometryUtility {
	
	/**
	 * @author sitanwo
	 * @param coord
	 * @return
	 * 
	 * 
	 * 坐标转换几何图形对象
	 */
	public static GeometryVO coordToGeometry(String coord){
		GeometryVO vo = new GeometryVO();
		try {
			if (coord != null && !coord.equals("")) {
				String str1 = coord.substring(coord.indexOf("(") + 1,
						coord.lastIndexOf(")"));
				if (coord.toUpperCase().indexOf("POINT") != -1) {
					String[] pointTemp = str1.trim().split(" ");
					List<Object> points = new ArrayList<Object>();
					for (String xy : pointTemp) {
						points.add(xy);
					}
					vo.setPoints(points);
					vo.setType("POINT");
				} else if(coord.toUpperCase().indexOf("MULTILINESTRING") != -1){
					List<Object> polyline = new ArrayList<Object>();
					int sIdx = coord.indexOf("((");
					int eidx = coord.indexOf("))");
					coord = coord.substring(sIdx+2, eidx);
					String[] linesArr = coord.split("\\),\\(");
					String[] points = null;
					String[] pointXY = null;
					List<String> linePoint = new ArrayList<String>();
					for(String lineStr: linesArr){
						points = lineStr.split(",");
						linePoint = new ArrayList<String>();
						for(String point: points){
							pointXY = point.trim().split(" ");
							for (int i = 0; i < pointXY.length; i++) {
								linePoint.add(pointXY[i]);
							}
						}
						polyline.add(linePoint);
					}
					vo.setPoints(polyline);
					vo.setType("POLYLINE");

				}else if (coord.toUpperCase().indexOf("LINESTRING") != -1) {
					String[] polylineTemp = str1.trim().split(",");
					List<Object> polyline = new ArrayList<Object>();
					for (int i = 0; i < polylineTemp.length; i++) {
						List<String> point = new ArrayList<String>();
						String[] strs = polylineTemp[i].trim().split(" ");
						for (int j = 0; j < strs.length; j++) {
							point.add(strs[j]);
						}
						polyline.add(point);
					}
					vo.setPoints(polyline);
					vo.setType("POLYLINE");
				} else if (coord.toUpperCase().indexOf("POLYGON") != -1) {
					List<Object> polygon = new ArrayList<Object>();
					String str4 = str1.substring(str1.indexOf("(") + 1,
							str1.lastIndexOf(")"));
					if (!str4.trim().equals("")) {
						String[] polygonTemp = str4.trim().split(",");
						for (int i = 0; i < polygonTemp.length; i++) {
							List<String> point = new ArrayList<String>();
							String[] strs2 = polygonTemp[i].trim().split(" ");
							for (int j = 0; j < strs2.length; j++) {
								point.add(strs2[j]);
							}
							polygon.add(point);
						}
					}
					vo.setPoints(polygon);
					vo.setType("POLYGON");
				}
			}
			return vo;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
	}
	
	/**
	 * @author sitanwo
	 * @param vo
	 * @return
	 * 
	 * 几何图形对象转换坐标
	 */
	public static String geometryToCoord(GeometryVO vo){
		StringBuffer coord = new StringBuffer();
		if(vo !=null ){
			if(vo.getType() != null && !vo.getType().equals("")){
				if(vo.getType().toUpperCase().equals("POINT")){
					if(vo.getPoints() != null && vo.getPoints().size() == 2){
						//point (1 2)
						coord.append("point (");
						coord.append(vo.getPoints().get(0));
						coord.append(" ");
						coord.append(vo.getPoints().get(1));
						coord.append(")");
					}
				}else if(vo.getType().toUpperCase().equals("POLYLINE")){
					if(vo.getPoints() != null){
						//linestring (33 2, 34 3, 35 6)
						coord.append("linestring (");
						for(int i=0;i<vo.getPoints().size();i++){
							List<String> point = (List<String>)vo.getPoints().get(i);
							if(point != null && point.size() == 2){
								coord.append(point.get(0));
								coord.append(" ");
								coord.append(point.get(1));
								if(i != vo.getPoints().size() - 1){
									coord.append(",");
								}
							}
						}
						coord.append(")");
					}
				}else if(vo.getType().toUpperCase().equals("POLYGON")){
					if(vo.getPoints() != null){
						//polygon ((3 3, 4 6, 5 3, 3 3))
						coord.append("polygon ((");
						for(int i=0;i<vo.getPoints().size();i++){
							List<String> point = (List<String>)vo.getPoints().get(i);
							if(point != null && point.size() == 2){
								coord.append(point.get(0));
								coord.append(" ");
								coord.append(point.get(1));
								if(i != vo.getPoints().size() - 1){
									coord.append(",");
								}
							}
						}
						coord.append("))");
					}
				}
			}
		}
		return coord.toString();
	}
}