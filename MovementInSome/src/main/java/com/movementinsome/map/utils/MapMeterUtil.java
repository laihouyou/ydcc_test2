package com.movementinsome.map.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.movementinsome.R;
import com.movementinsome.map.view.MyMapView;

import java.util.ArrayList;
import java.util.List;

public class MapMeterUtil {

	private int num;
	private int fillUid;
	private int[] dots = {R.drawable.dot0,R.drawable.dot2};
	private Resources res;
	private PictureMarkerSymbol pointSymbol;
	private GraphicsLayer grapPayer;//点
	private GraphicsLayer grapLayer;//线
	private GraphicsLayer grapFayer;//多边形
	private SimpleFillSymbol sfs;
	public List<List<Double>> listPoint;
	private List<Double> listarea;
	private List<Integer> listUid;	//点图层添加的id
	private List<Integer> listLine;	//线图层添加的id
	public Polygon poly;
	private Point ptPrevious = null;//上一个点
	private String drawType;
	private MyMapView map;
	private Context context;
	public static final String POINT="point";// 线
	public static final String LINE="line";// 线
	public static final String POLY="poly";// 面
	MyTouchListener myTouchListener;
	private boolean isShowCallout=true;

	public MapMeterUtil(MyMapView map,Context context) {
		this.context=context;
		this.map = map;
		res = context.getResources();
		grapPayer = new GraphicsLayer();
		grapLayer = new GraphicsLayer();
		grapFayer = new GraphicsLayer();
		listUid = new ArrayList<Integer>();
		listLine = new ArrayList<Integer>();
		listPoint = new ArrayList<List<Double>>();
		listarea = new ArrayList<Double>();
		fillUid = -9;
		num = 0;
		map.addLayer(grapPayer);
		map.addLayer(grapLayer);
		map.addLayer(grapFayer);
		map.setAllowRotationByPinch(true);
		myTouchListener=new MyTouchListener(context,map.getMapView());
		map.setOnTouchListener(myTouchListener);
		
	}
	class MyTouchListener extends MapOnTouchListener {

		public MyTouchListener(Context context, MapView view) {
			super(context, view);
			// TODO Auto-generated constructor stub
			
		}
		@Override
		public boolean onSingleTap(MotionEvent point) {
			// TODO Auto-generated method stub
			meter(point.getX(), point.getY(),drawType);
			return super.onSingleTap(point);
		}
	
	}
	//	画区域（单击地图传屏幕点坐标）
	public void meter(float x, float y,String drawType){
		Point point = map.toMapPoint(x, y);
		List<Double> pointList = new ArrayList<Double>();
		pointList.add(point.getX());
		pointList.add(point.getY());
		listPoint.add(pointList);
		//如过是测量面积或画区域
		if(POLY.equals(drawType)){

			//测量面积或画区域
			if(poly==null){
				poly = new Polygon();
				poly.startPath(point);
				sfs = new SimpleFillSymbol(Color.GREEN);
				sfs.setAlpha(70);
				fillUid = grapFayer.addGraphic(new Graphic(poly, sfs));
			}else{
				poly.lineTo(point);
			}
			//面
			grapFayer.updateGraphic(fillUid, poly);
			
			//测量面积获取值
			if( listPoint.size() >= 3){
				listarea.add((double)Math.abs(Math.round(poly.calculateArea2D())));
			}

		}else if(LINE.equals(drawType)){//测量线长度
			Line line = new Line();
			if(ptPrevious == null){
				ptPrevious = point;
			}
			line.setStart(ptPrevious);
			line.setEnd(point);
			SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.BLUE, 5);
			Polyline polyline = new Polyline();
			polyline.addSegment(line, true);
			Graphic g = new Graphic(polyline, lineSymbol);
			listLine.add(grapLayer.addGraphic(g));
			listarea.add((double)Math.round(line.calculateLength2D()));
			ptPrevious = point;
		}
		if((LINE.equals(drawType)||POLY.equals(drawType))){
			if(isShowCallout){
				// 显示测量结果
				TextView tv=new TextView(context);
				tv.setText(getAreaString());
				map.getCallout().hide();
				map.getCallout().show(point,tv);
			}
			//点
			pointSymbol = new PictureMarkerSymbol(res.getDrawable(dots[num]));
			Graphic grahic = new Graphic(point,pointSymbol);
			int puid = grapPayer.addGraphic(grahic);
			listUid.add(puid);
			num = num<1?++num:0;	
		}else if (POINT.equals(drawType)){
			if(isShowCallout){
				// 显示测量结果
				TextView tv=new TextView(context);
				tv.setText(String.valueOf(point.getX())+"\n"+String.valueOf(point.getY()));
				map.getCallout().hide();
				map.getCallout().show(point,tv);
			}
			grapPayer.removeAll();
			pointSymbol = new PictureMarkerSymbol(res.getDrawable(dots[num]));
			Graphic grahic = new Graphic(point,pointSymbol);
			grapPayer.addGraphic(grahic);
		}

		
	}
	
	/**
	 * 计算面积或长度总值
	 * @param dValue
	 * @return
	 */
	public String getAreaString(){
		String sArea = "";
		double area = 0;//测量长度或面积的总值
		//获取总值
		for(int i = 0;i <listarea.size();i++){
			area += listarea.get(i);
		}
		// 顺时针绘制多边形，面积为正，逆时针绘制，则面积为负
		if(POLY.equals(drawType)){
			if(area >= 1000000){					
				double dArea = area / 1000000.00;
				sArea = Double.toString(dArea) + " 平方公里";
			}else if(area > 0)
				sArea = Double.toString(area) + " 平方米";
			else  if(area <= 0){
				sArea = "…点击地图测量…";
			}
		}else if(LINE.equals(drawType)){
			if(area >= 1000){					
				double dArea = area / 1000.00;
				sArea = Double.toString(dArea) + " 千米";
			}else if(area > 0)
				sArea = Double.toString(area) + " 米";
			else  if(area <= 0){
				sArea = "…点击地图测量…";
			}
		}
		return sArea;
	}

	/**
	 * 清空所有图层
	 */
	public void clearAllLayer(){
		map.getCallout().hide();
		ptPrevious = null;
		num = 0;
		fillUid = -9;
		poly = null;
		sfs = null;
		listPoint.clear();
		listarea.clear();
		listLine.clear();
		listUid.clear();
		grapPayer.removeAll();
		grapLayer.removeAll();
		grapFayer.removeAll();
	}

	public void exit() {
		clearAllLayer();
		drawType=null;
		map.resetTouchListener();
	}

	/**
	 * 撤消一个点
	 */
	public void recallPoint(){
		int size = listPoint.size()-1;
		if(size >=1){
			if(LINE.equals(drawType)){
				//测量线长度
				grapLayer.removeGraphic(listLine.get(size));
				grapPayer.removeGraphic(listUid.get(size));
				listarea.remove(size);	//移除面积值
				listLine.remove(size);	//移除线图层id
				listUid.remove(size);	//移除点图层id
				ptPrevious = new Point(listPoint.get(size-1).get(0),
						listPoint.get(size-1).get(1));
				listPoint.remove(size);	//移除点值	
				
			}else if(POLY.equals(drawType)){
				//画区域、测量面积
				poly.removePoint(0, size);
				grapPayer.removeGraphic(listUid.get(size));
				grapFayer.updateGraphic(fillUid, poly);
				listPoint.remove(size);
				listUid.remove(size);
				if(size>=2){
					listarea.remove(size-2);
				}
			}
			if(LINE.equals(drawType)||POLY.equals(drawType)){
				// 显示测量结果
				map.getCallout().hide();
				Point point =new Point(listPoint.get(size-1).get(0),listPoint.get(size-1).get(1));
				if(isShowCallout){
					TextView tv=new TextView(context);
					tv.setText(getAreaString());
					map.getCallout().show(point,tv);
				}
			}	
		}
	}


	/**
	 * 存储到数据库（目前未使用）
	 * @param strList
	 */
	public void showAddPoly(String strList){
		poly = new Polygon();
		Point point;
		strList = strList.replaceAll("[", "");
		strList = strList.replaceAll("]", "");
		String[] listItme = strList.split(",");
		for(int i = 0 ;i < listItme.length ;i++){
			point = new Point(Double.parseDouble(listItme[i]), Double.parseDouble(listItme[(i+1)]));
			if(i == 0 ){
				poly.startPath(point);
			}else if(i == listItme.length - 1){
				poly.lineTo(poly.getPoint(0));
			}else{
				poly.lineTo(point);
			}
			i++;
		}

		sfs = new SimpleFillSymbol(Color.GREEN);
		sfs.setAlpha(70);
		fillUid = grapFayer.addGraphic(new Graphic(poly, sfs));

	}

	public String getDrawType() {
		return drawType;
	}

	public void setDrawType(String drawType) {
		this.drawType = drawType;
	}

	public boolean isShowCallout() {
		return isShowCallout;
	}

	public void setShowCallout(boolean isShowCallout) {
		this.isShowCallout = isShowCallout;
	}

	public List<List<Double>> getListPoint() {
		return listPoint;
	}

	public void setListPoint(List<List<Double>> listPoint) {
		this.listPoint = listPoint;
	}
	
	public String getGeo4WKT(){
		Geometry shape;
		//listPoint.add(pointList);
		//如过是测量面积或画区域
		if(POLY.equals(drawType)){

			return MapUtil.geometry2WKT(poly);
		}else if(LINE.equals(drawType)){//测量线长度

		}
		else if (POINT.equals(drawType)){

		}
		
		return null;
	}
}
