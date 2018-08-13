package com.movementinsome.map.utils;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.movementinsome.R;
import com.movementinsome.database.vo.GeometryVO;
import com.movementinsome.database.vo.HistoryTrajectoryVO;
import com.movementinsome.map.view.MyMapView;

import java.util.List;

public class DrawGraphLoctionUtil {

	private MyMapView map;
	private GraphicsLayer grapLayer;
	private GraphicsLayer grapLayer2;
	private GraphicsLayer grapLayer3;
	private GraphicsLayer runTraceGlayer;
	private Polyline runTrace;
	private Point runPrePoint = null;
	
	private Context context;
	
	public DrawGraphLoctionUtil(MyMapView map,Context context){
		this.map=map;
		this.context=context;
		grapLayer = new GraphicsLayer();
		grapLayer2 = new GraphicsLayer();
		grapLayer3 = new GraphicsLayer();
		runTraceGlayer = new GraphicsLayer();
		runTrace = new Polyline();
		map.addLayer(grapLayer);
		map.addLayer(grapLayer2);
		map.addLayer(grapLayer3);
		map.addLayer(runTraceGlayer);
	}
	
	public void clearGraphy(){
		grapLayer.removeAll();
		grapLayer2.removeAll();
		grapLayer3.removeAll();
	}
	

	public void drawIconLoction(Point point,int r){
		grapLayer2.removeAll();
		grapLayer2.addGraphic(new Graphic(point,
				new PictureMarkerSymbol(context.getResources().getDrawable(r))));
	}
	public void drawLine(Point startPoint,Point endPoint,int color){
		Line line = new Line();
		line.setStart(startPoint);
		line.setEnd(endPoint);
		Polyline polyline = new Polyline();
		polyline.addSegment(line, true);
		grapLayer2.addGraphic(new Graphic(polyline,new SimpleLineSymbol(color, 2, SimpleLineSymbol.STYLE.DOT)));
	}
	
	public void drawGraphLoction(String geomeryStr,int color){
		GeometryVO geomery = GeometryUtility.coordToGeometry(geomeryStr);
		if(geomery!=null){
			String geomeryType=geomery.getType();
			List<Object> list=geomery.getPoints();
			if(list!=null&&list.size()>0){
				Point point = null;
				if("POINT".equals(geomeryType)){
					point = new Point(Double.parseDouble(list.get(0).toString()),
							Double.parseDouble(list.get(1).toString()));
					map.zoomToScale(point, 300d);//mMapView.getScale()
					grapLayer.removeAll();
					grapLayer.addGraphic(new Graphic(point,
							new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.map_inlocation))));
				}else if("POLYLINE".equals(geomeryType)){
					Line line = new Line();
					Point startPoint = null;
					for(int i = 0; i <list.size();i++){
						List<String> listPoint = (List<String>) list.get(i);
						point = new Point(Double.parseDouble(listPoint.get(0)), 
								Double.parseDouble(listPoint.get(1)));
						if(startPoint == null){
							startPoint = point;
						}else{
							line.setStart(startPoint);
							line.setEnd(point);
							startPoint = point;
						}
					}
					Polyline polyline = new Polyline();
					polyline.addSegment(line, true);
					grapLayer.removeAll();
					grapLayer.addGraphic(new Graphic(polyline,new SimpleLineSymbol(color, 5)));
					Envelope enve = new Envelope();
					polyline.queryEnvelope(enve);
					point = new Point(enve.getCenterX(), enve.getCenterY());
					map.zoomToScale(point, 1600d);
				}else if("POLYGON".equals(geomeryType)){
					Polygon poly = new Polygon();
					Point startPoint = null;
					for(int i = 0; i <list.size();i++){
						List<String> listPoint = (List<String>) list.get(i);
						point = new Point(Double.parseDouble(listPoint.get(0)), 
								Double.parseDouble(listPoint.get(1)));
						if(startPoint == null){
							startPoint = point;
							poly.startPath(startPoint);
						}else{
							poly.lineTo(point);
						}
					}
					SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
							Color.YELLOW);
					simpleFillSymbol.setAlpha(100);
					simpleFillSymbol.setOutline(new SimpleLineSymbol(color, 4));
					Graphic graphic = new Graphic(poly,simpleFillSymbol);
					grapLayer.removeAll();
					grapLayer.addGraphic(graphic);
					Envelope enve = new Envelope();
					poly.queryEnvelope(enve);
					point = new Point(enve.getCenterX(), enve.getCenterY());
					map.zoomToScale(point, 2400d);
				}
			}
		}
	}
	public void drawGraphLoctionMeger(String geomeryStr,int color){
		GeometryVO geomery = GeometryUtility.coordToGeometry(geomeryStr);
		if(geomery!=null){
			String geomeryType=geomery.getType();
			List<Object> list=geomery.getPoints();
			if(list!=null&&list.size()>0){
				Point point = null;
				if("POINT".equals(geomeryType)){
					point = new Point(Double.parseDouble(list.get(0).toString()),
							Double.parseDouble(list.get(1).toString()));
					//map.zoomToScale(point, 300d);//mMapView.getScale()
					//grapLayer.removeAll();
					grapLayer.addGraphic(new Graphic(point,
							new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.icon_mark_pt))));
					/*TextSymbol textSymbol =new TextSymbol("serif", "签到点", Color.BLACK);
					grapLayer.addGraphic(new Graphic(point,textSymbol.setSize(18)));*/
				}else if("POLYLINE".equals(geomeryType)){
					Polyline polyline = new Polyline();
					Line line = new Line();
					Point startPoint = null;
					for(int i = 0; i <list.size();i++){
						List<String> listPoint = (List<String>) list.get(i);
						point = new Point(Double.parseDouble(listPoint.get(0)), 
								Double.parseDouble(listPoint.get(1)));
						if(startPoint == null){
							startPoint = point;
						}else{
							line.setStart(startPoint);
							line.setEnd(point);
							startPoint = point;
							polyline.addSegment(line, true);
						}
					}					
					//grapLayer.removeAll();
					grapLayer.addGraphic(new Graphic(polyline,new SimpleLineSymbol(color, 5)));
					/*Envelope enve = new Envelope();
					polyline.queryEnvelope(enve);
					point = new Point(enve.getCenterX(), enve.getCenterY());*/
					//map.zoomToScale(point, 1600d);
				}else if("POLYGON".equals(geomeryType)){
					Polygon poly = new Polygon();
					Point startPoint = null;
					for(int i = 0; i <list.size();i++){
						List<String> listPoint = (List<String>) list.get(i);
						point = new Point(Double.parseDouble(listPoint.get(0)), 
								Double.parseDouble(listPoint.get(1)));
						if(startPoint == null){
							startPoint = point;
							poly.startPath(startPoint);
						}else{
							poly.lineTo(point);
						}
					}
					SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
							Color.YELLOW);
					simpleFillSymbol.setAlpha(100);
					simpleFillSymbol.setOutline(new SimpleLineSymbol(color, 4));
					Graphic graphic = new Graphic(poly,simpleFillSymbol);
					//grapLayer.removeAll();
					grapLayer.addGraphic(graphic);
					Envelope enve = new Envelope();
					poly.queryEnvelope(enve);
					point = new Point(enve.getCenterX(), enve.getCenterY());
					//map.zoomToScale(point, 2400d);
				}
			}
		}
	}
	
	public void drawTrajectory(List<HistoryTrajectoryVO> historyTrajectoryVOlist, int color){
			Polyline polyline = new Polyline();
			Line line = new Line();
			grapLayer3.removeAll();
			Point startPoint = null;
			Point point = null;
			for(int i = 0; i <historyTrajectoryVOlist.size();i++){
//				List<String> listPoint = (List<String>) list.get(i);
				Double X =historyTrajectoryVOlist.get(i).getCoordinatesX();
				Double Y =historyTrajectoryVOlist.get(i).getCoordinatesY();
				Long id = historyTrajectoryVOlist.get(i).getId();
				point = new Point(X,Y);
				if(startPoint == null){
					startPoint = point;
				}else{
					line.setStart(startPoint);
					line.setEnd(point);
					startPoint = point;
					polyline.addSegment(line, true);
				}
			}					
			//grapLayer.removeAll();
			grapLayer3.addGraphic(new Graphic(polyline,new SimpleLineSymbol(color, 2, SimpleLineSymbol.STYLE.DASH)));
//			grapLayer.addGraphic(new Graphic(polyline,new SimpleLineSymbol(color, 2)));
			/*Envelope enve = new Envelope();
			polyline.queryEnvelope(enve);
			point = new Point(enve.getCenterX(), enve.getCenterY());*/
			//map.zoomToScale(point, 1600d);
		}
	
	public Polyline runTrace(Point nowPoint){
		if (runPrePoint == null){
			runPrePoint = nowPoint;
		}else{
			Line line = new Line();
			line.setStart(runPrePoint);
			line.setEnd(nowPoint);
			runTrace.addSegment(line, false);
		}
		return runTrace;
	}
	
	public void drawRunTrace(Geometry runTrace,int color){
		Graphic graphic = new Graphic(runTrace,new SimpleLineSymbol(color, 5));
		runTraceGlayer.addGraphic(graphic);
	}
	
	public void drawRunTraceBound(Polyline runTrace,int buffer){
		//runTraceGlayer.removeAll();
		Polygon pg = GeometryEngine.buffer(runTrace, map.getSpatialReference(), buffer, map.getSpatialReference().getUnit());
		SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
				Color.YELLOW);
		simpleFillSymbol.setAlpha(800);
		simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.GREEN, 1));
		Graphic graphic = new Graphic(pg,simpleFillSymbol);
		//grapLayer.removeAll();
		runTraceGlayer.addGraphic(graphic);
	}
	/**
	 * 绘制图形
	 * @param geomeryType 类型：point，polyline
	 * @param list 点集合
	 */

	public void drawGraphLoction(String geomeryStr){
		GeometryVO geomery = GeometryUtility.coordToGeometry(geomeryStr);
		if(geomery!=null){
			String geomeryType=geomery.getType();
			List<Object> list=geomery.getPoints();
			if(list!=null&&list.size()>0){
				Point point = null;
				if("POINT".equals(geomeryType)){
					point = new Point(Double.parseDouble(list.get(0).toString()),
							Double.parseDouble(list.get(1).toString()));
					map.zoomToScale(point, 300d);//mMapView.getScale()
					grapLayer.removeAll();
					grapLayer.addGraphic(new Graphic(point,
							new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.map_inlocation))));
				}else if("POLYLINE".equals(geomeryType)){
					
					Point startPoint = null;
					Polyline polyline = new Polyline();;
					grapLayer.removeAll();
					for(int i = 0; i <list.size();i++){
						
						List<String> listPoint = (List<String>) list.get(i);
						point = new Point(Double.parseDouble(listPoint.get(0)), 
								Double.parseDouble(listPoint.get(1)));
						if(startPoint == null){
							startPoint = point;
						}else{
							Line line = new Line();
							line.setStart(startPoint);
							line.setEnd(point);
							startPoint = point;
							polyline.addSegment(line, true);
						}
					}
					grapLayer.addGraphic(new Graphic(polyline,new SimpleLineSymbol(Color.RED, 2)));
					Envelope enve = new Envelope();
					polyline.queryEnvelope(enve);
					point = new Point(enve.getCenterX(), enve.getCenterY());
					map.zoomToScale(point, 1600d);
				}else if("POLYGON".equals(geomeryType)){
					Polygon poly = new Polygon();
					Point startPoint = null;
					for(int i = 0; i <list.size();i++){
						List<String> listPoint = (List<String>) list.get(i);
						point = new Point(Double.parseDouble(listPoint.get(0)), 
								Double.parseDouble(listPoint.get(1)));
						if(startPoint == null){
							startPoint = point;
							poly.startPath(startPoint);
						}else{
							poly.lineTo(point);
						}
					}
					SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
							Color.YELLOW);
					simpleFillSymbol.setAlpha(100);
					simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK, 4));
					Graphic graphic = new Graphic(poly,simpleFillSymbol);
					grapLayer.removeAll();
					grapLayer.addGraphic(graphic);
					Envelope enve = new Envelope();
					poly.queryEnvelope(enve);
					point = new Point(enve.getCenterX(), enve.getCenterY());
					map.zoomToScale(point, 2400d);
				}
			}
		}
	}
	/**
	 * 绘制图形
	 * @param geomeryType 类型：point，polyline
	 * @param list 点集合
	 */
	
	public void drawGraphLoctionBurst(String geomeryStr){
		GeometryVO geomery = GeometryUtility.coordToGeometry(geomeryStr);
		if(geomery!=null){
			String geomeryType=geomery.getType();
			List<Object> list=geomery.getPoints();
			if(list!=null&&list.size()>0){
				Point point = null;
				if("POINT".equals(geomeryType)){
					point = new Point(Double.parseDouble(list.get(0).toString()),
							Double.parseDouble(list.get(1).toString()));
					map.zoomToScale(point, 300d);//mMapView.getScale()
					grapLayer.removeAll();
					grapLayer.addGraphic(new Graphic(point,
							new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.yema1_1))));
					TextView tvPoint = new TextView(context);
					tvPoint.setText("当前爆漏位置\n");
					map.getCallout().show(point, tvPoint);
				}else if("POLYLINE".equals(geomeryType)){
					
					Point startPoint = null;
					Polyline polyline = new Polyline();;
					grapLayer.removeAll();
					for(int i = 0; i <list.size();i++){
						
						List<String> listPoint = (List<String>) list.get(i);
						point = new Point(Double.parseDouble(listPoint.get(0)), 
								Double.parseDouble(listPoint.get(1)));
						if(startPoint == null){
							startPoint = point;
						}else{
							Line line = new Line();
							line.setStart(startPoint);
							line.setEnd(point);
							startPoint = point;
							polyline.addSegment(line, true);
						}
					}
					grapLayer.addGraphic(new Graphic(polyline,new SimpleLineSymbol(Color.RED, 2)));
					Envelope enve = new Envelope();
					polyline.queryEnvelope(enve);
					point = new Point(enve.getCenterX(), enve.getCenterY());
					map.zoomToScale(point, 1600d);
				}else if("POLYGON".equals(geomeryType)){
					Polygon poly = new Polygon();
					Point startPoint = null;
					for(int i = 0; i <list.size();i++){
						List<String> listPoint = (List<String>) list.get(i);
						point = new Point(Double.parseDouble(listPoint.get(0)), 
								Double.parseDouble(listPoint.get(1)));
						if(startPoint == null){
							startPoint = point;
							poly.startPath(startPoint);
						}else{
							poly.lineTo(point);
						}
					}
					SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
							Color.YELLOW);
					simpleFillSymbol.setAlpha(100);
					simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK, 4));
					Graphic graphic = new Graphic(poly,simpleFillSymbol);
					grapLayer.removeAll();
					grapLayer.addGraphic(graphic);
					Envelope enve = new Envelope();
					poly.queryEnvelope(enve);
					point = new Point(enve.getCenterX(), enve.getCenterY());
					map.zoomToScale(point, 2400d);
				}
			}
		}
	}
}
