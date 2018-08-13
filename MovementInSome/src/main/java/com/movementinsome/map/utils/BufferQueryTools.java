package com.movementinsome.map.utils;

import android.content.Context;
import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.tasks.identify.IdentifyResult;
import com.movementinsome.R;
import com.movementinsome.map.view.MyMapView;

import java.util.List;

public final class BufferQueryTools {

	private Context context;
	private MyMapView map;
	private Geometry queryGeo;
	private GraphicsLayer queryGpLayer;
	int uid = -1;
	private String icon_mark = "icon_mark";
	private String def_icon_mark = "icon_mark_pt.png";

	private PictureMarkerSymbol pmSymbol = null;
	private Graphic gp = null;

	public BufferQueryTools(Context context, MyMapView map) {
		this.context = context;
		this.map = map;

		queryGpLayer = new GraphicsLayer();
		map.addLayer(queryGpLayer);

	}

	public void clear(){
		queryGpLayer.removeAll();
	}
	public void queryByPoint(Geometry point, int buffer, String layers) {

		queryGeo = buffer(point, buffer);
	}

	public void queryByPolyLine(Geometry polyLine, int buffer, String layers) {
		queryGeo = buffer(polyLine, buffer);
	}

	public void queryByPolyGon(Geometry polyGon, int buffer, String layers) {
		queryGeo = buffer(polyGon, buffer);
	}

	public void drawResult(IdentifyResult idenResult) {

		pmSymbol = new PictureMarkerSymbol(context.getResources().getDrawable(
				R.drawable.icon_mark_pt));
		Geometry geo=idenResult.getGeometry();
		gp = new Graphic(geo, pmSymbol);
		queryGpLayer.addGraphic(gp);

	}
	public void drawResult3(Geometry geo) {
		if (geo instanceof Point) {
			pmSymbol = new PictureMarkerSymbol(context.getResources().getDrawable(
					R.drawable.icon_mark_pt));
			gp = new Graphic(geo, pmSymbol);
			queryGpLayer.addGraphic(gp);
		} else if (geo instanceof Polyline) {
			gp = new Graphic(geo, new SimpleLineSymbol(Color.GREEN, 4));
			queryGpLayer.addGraphic(gp);
		}
	}
	public void drawResult2(Graphic graphic) {

		Geometry geo=graphic.getGeometry();
		pmSymbol = new PictureMarkerSymbol(context.getResources().getDrawable(
				R.drawable.map_inlocation));
		;
		gp = new Graphic(geo, pmSymbol);

		queryGpLayer.addGraphic(gp);
		Point point=null;
		if (geo instanceof Point) {
			point = (Point) geo;
		} else if (geo instanceof Polyline) {
			Polyline pline = (Polyline) geo;
			point=pline.getPoint(0);
		} else if (geo instanceof Polygon) {
			// Polygon pgon = (Polygon)
			// mMapIden.getGeometry();
			Envelope env = new Envelope();
			geo.queryEnvelope(env);
			point=env.getCenter();
		}
		if(point!=null){
			map.centerAt(point, true);
		}
	}

	public void clearResult(){
		queryGpLayer.removeAll();
	}
	
	public void quit(){
		queryGpLayer.removeAll();
		try{
			map.removeLayer(queryGpLayer);
		}catch(Exception e){
			
		}
	}
	
	public void drawResult(List<IdentifyResult> idenResults) {
		int i = 0;
		for (IdentifyResult iden : idenResults) {
			i++;

			if (i <= 10) {
				int resID = context.getResources().getIdentifier(
						"icon_mark" + String.valueOf(i), "drawable",
						context.getPackageName());
				pmSymbol = new PictureMarkerSymbol(context.getResources()
						.getDrawable(resID));
				gp = new Graphic(iden.getGeometry(), pmSymbol);
			} else {
				pmSymbol = new PictureMarkerSymbol(context.getResources()
						.getDrawable(R.drawable.icon_mark_pt));
				;
				gp = new Graphic(iden.getGeometry(), pmSymbol);
			}
			queryGpLayer.addGraphic(gp);
		}
	}

	// 缓冲分析，并绘制范围
	public Geometry buffer(Geometry geo, int buffer) {
		Polygon polygon = GeometryEngine.buffer(geo, map.getMapSr(),
				buffer, map.getMapUnit());

		Envelope env = new Envelope();
		map.getExtent().queryEnvelope(env);
		// 用当前地图范围进行裁剪
		Geometry clipGeo = GeometryEngine.clip(polygon, env,
				map.getMapSr());

		Graphic g = new Graphic(clipGeo, new SimpleLineSymbol(Color.BLUE, 5));

		uid = queryGpLayer.addGraphic(g);

		return clipGeo;
	}
	
	// 缓冲分析，并绘制范围
	public Geometry buffer(Geometry geo, int buffer,int color) {
		Polygon polygon = GeometryEngine.buffer(geo, map.getMapSr(),
				buffer, map.getMapUnit());

		Envelope env = new Envelope();
		map.getExtent().queryEnvelope(env);
		// 用当前地图范围进行裁剪
		Geometry clipGeo = GeometryEngine.clip(polygon, env,
				map.getMapSr());

		Graphic g = new Graphic(clipGeo, new SimpleLineSymbol(color, 5));

		uid = queryGpLayer.addGraphic(g);

		return clipGeo;
	}	
}
