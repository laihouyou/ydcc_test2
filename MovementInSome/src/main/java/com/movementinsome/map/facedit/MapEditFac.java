package com.movementinsome.map.facedit;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.util.WebAccessTools.NET_STATE;
import com.movementinsome.map.facedit.asyctask.FacQueryTask;
import com.movementinsome.map.facedit.dialog.FacEditDialog;
import com.movementinsome.map.facedit.dialog.MapEditFacListView;
import com.movementinsome.map.facedit.vo.FacAttribute;
import com.movementinsome.map.view.MyMapView;

import java.util.ArrayList;
import java.util.List;


public class MapEditFac {
	/**
	 * 设施编辑类
	 */
	private Context context;
	
	
	private List<Point>points= new ArrayList<Point>();
	private ArrayList<Point> mpoints = new ArrayList<Point>();

	private String objectid;
	private int editingmode;
	private static final int POINT = 0;
	private static final int POLYLINE = 1;
	private static final int POLYGON = 2;
	private boolean midpointselected = false;
	private boolean vertexselected = false;
	private int insertingindex;
	private GraphicsLayer graphicsLayerEditing;
	private GraphicsLayer grapLayer;
	private MyMapView map;
	// 编辑类型
	private String type;
	// 设施选择
	private MapEditFacListView mapEditFacListView;
	MyTouchListener myTouchListener;
	
	private FacAttribute facAttribute;
	private List<ArcGISFeatureLayer> arcGISFeatureLayerList; 
	// 配置
	private List<Ftlayer> ftlayerList;
	
	public MapEditFac(Context context, MyMapView map) {
		this.context = context;
		this.map = map;
		mapEditFacListView = new MapEditFacListView(context, map.getMapView());
		myTouchListener = new MyTouchListener(context, map.getMapView());
		grapLayer = new GraphicsLayer();
		map.addLayer(grapLayer);
		map.setOnTouchListener(myTouchListener);
		arcGISFeatureLayerList = new ArrayList<ArcGISFeatureLayer>();
		ftlayerList = new ArrayList<Ftlayer>();
		List<Ftlayer> ftlayers = AppContext.getInstance().getMapParam().getFtlayers();
		// 循环查询加载ArcGISFeatureLayer
		for(int i=0;i<ftlayers.size();++i){
			Ftlayer ftlayer = ftlayers.get(i);
			Mapservice mapservice = ftlayer.getMapservice();
			if(mapservice!=null){
				String url = "";
				if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.NULL){
					url = (mapservice.getForeign()+"/"+ftlayer.getFeatureServerId()).replace("MapServer", "FeatureServer");
				}else if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.FOEIGN ){
					url = (mapservice.getForeign()+"/"+ftlayer.getFeatureServerId()).replace("MapServer", "FeatureServer");
				}else{
					url = (mapservice.getLocal()+"/"+ftlayer.getFeatureServerId()).replace("MapServer", "FeatureServer");
				}
				ArcGISFeatureLayer fl= new ArcGISFeatureLayer(url, ArcGISFeatureLayer.MODE.ONDEMAND);
				arcGISFeatureLayerList.add(fl);
				map.addLayer(fl);
				ftlayerList.add(ftlayer);
			}
		}
	}
	class MyTouchListener extends MapOnTouchListener {

		public MyTouchListener(Context context, MapView view) {
			super(context, view);
			// TODO Auto-generated constructor stub
			
		}
		@Override
		public boolean onSingleTap(MotionEvent point) {
			// TODO Auto-generated method stub
			Point identifyPoint = map.toMapPoint(point.getX(), point.getY());
			if(mapEditFacListView==null||mapEditFacListView.getFacAttribute()==null
					||mapEditFacListView.getFacAttribute().getLegend()==null){
				Toast.makeText(context, "请选择设施", Toast.LENGTH_LONG).show();
			}else{
				facAttribute = mapEditFacListView.getFacAttribute();
				facAttribute.setEditPoint(identifyPoint);
				if(mapEditFacListView.getEditingmode()==MapEditFacListView.POLYLINE){
					List<Point>points=getPoints();
					points.add(identifyPoint);
					setPoints(points);
					refresh();
				}else if(mapEditFacListView.getEditingmode()==MapEditFacListView.POINT){
					List<Point>points=new ArrayList<Point>();
					setPoints(points);
					refresh();
				}
				
				showEditDialog();
			}
			
			return super.onSingleTap(point);
		}
	
	}
	
	public List<Point> getPoints() {
		return points;
	}
	public void setPoints(List<Point> points) {
		this.points = points;
	}
	public void showEditDialog(){
		LinearLayout editDialogLayout=(LinearLayout) View.inflate(context, R.layout.map_edit_dialog, null);
		Button addFac=(Button) editDialogLayout.findViewById(R.id.addFac);
		addFac.setVisibility(View.GONE);
		/*addFac.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new FacEditDialog(context, facAttribute, null, "facAdd").show();
				grapLayer.removeAll();
				clear();
			}
		});*/
		Button editFac=(Button) editDialogLayout.findViewById(R.id.editFac);
		if("add".equals(type)){// 添加设施
			editFac.setText("添加");
		}else if("delect".equals(type)){// 删除设施
			editFac.setText("删除");
		}else if("edit".equals(type)){// 编辑属性
			editFac.setText("编辑");
		}
		editFac.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String layerName = facAttribute.getLegend().getName();
				String url = facAttribute.getFeaturelayer().getUrl();
				String[] urls=url.split("FeatureServer/");
				for(Ftlayer ftlayer :ftlayerList){
					if(urls[1]!=null&&urls[1].equals(ftlayer.getFeatureServerId())){
						String queryUrl = "";
						if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.NULL){
							queryUrl = ftlayer.getMapservice().getForeign()+"/"+ftlayer.getLayerIds();
						}else if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.FOEIGN ){
							queryUrl = ftlayer.getMapservice().getForeign()+"/"+ftlayer.getLayerIds();
						}else{
							queryUrl = ftlayer.getMapservice().getLocal()+"/"+ftlayer.getLayerIds();
						}
						facAttribute.setQueryUrl(queryUrl);
						break;
					}
				}
				Ftlayer ftlayer = null;
				List<Ftlayer> ftlayers = AppContext.getInstance().getMapParam().getFtlayers();
				
				for(int i=0;i<ftlayers.size();++i){
					if(layerName.equals(ftlayers.get(i).getName())){
						ftlayer = ftlayers.get(i);
					}
				}
				if("add".equals(type)){// 添加设施
					facAttribute.setGeom(getGeometry(points));
					new FacEditDialog(context,"add",ftlayer,facAttribute,MapEditFac.this).showAtLocation(map, Gravity.CENTER, 0, 0);
				}else if("delect".equals(type)){// 删除设施
					FacQueryTask facQueryTask=new FacQueryTask(context, map, facAttribute,"delect",MapEditFac.this,ftlayer);
					facQueryTask.execute("*");
				}else if("edit".equals(type)){// 编辑属性
					FacQueryTask facQueryTask=new FacQueryTask(context, map, facAttribute,"edit",MapEditFac.this,ftlayer);
					facQueryTask.execute("*");
				}
			}
		});
		PictureMarkerSymbol symbol = new PictureMarkerSymbol(
				context.getResources().getDrawable(R.drawable.dot3));
		map.getCallout().show(facAttribute.getEditPoint(),editDialogLayout);
		grapLayer.removeAll();

		Graphic graphic = new Graphic(facAttribute.getEditPoint(), symbol);
		grapLayer.addGraphic(graphic);
	}
	private Geometry getGeometry(List<Point> points){
		if(points.size()>0){
			MultiPath multipath;
			multipath = new Polyline();
			multipath.startPath(points.get(0));
			for (int i = 1; i < points.size(); i++) {
				multipath.lineTo(points.get(i));
			}
			Geometry geom = GeometryEngine.simplify(multipath, map
					.getSpatialReference());
			return geom;
		}
		return null;
	}
	public void drawPolyline() {
		if(graphicsLayerEditing == null){
			graphicsLayerEditing = new GraphicsLayer();
			map.addLayer(graphicsLayerEditing);
		}
		
		if (points.size() <= 1)
			return;
		Graphic graphic;		
		MultiPath multipath;
	//	if (editingmode == POLYLINE)
		multipath = new Polyline();
		/*else
			multipath = new Polygon();
		*/
		multipath.startPath(points.get(0));
		for (int i = 1; i < points.size(); i++) {
			multipath.lineTo(points.get(i));
		}
		graphic = new Graphic(multipath,new SimpleLineSymbol(Color.BLACK, 4));
		/*if (editingmode == POLYLINE)
			graphic = new Graphic(multipath,new SimpleLineSymbol(Color.BLACK, 4));
		else {
			SimpleFillSymbol simpleFillSymbol = new SimpleFillSymbol(
					Color.YELLOW);
			simpleFillSymbol.setAlpha(100);
			simpleFillSymbol.setOutline(new SimpleLineSymbol(Color.BLACK, 4));
			graphic = new Graphic(multipath,(simpleFillSymbol));
		}*/
		graphicsLayerEditing.addGraphic(graphic);
	}
	public void drawMidPoints() {
		if(graphicsLayerEditing == null){
			graphicsLayerEditing = new GraphicsLayer();
			map.addLayer(graphicsLayerEditing);
		}
		int index;
		Graphic graphic;
		//GraphicsLayer gll = null;
		// draw mid-point
		if (points.size() > 1) {
			mpoints.clear();
			for (int i = 1; i < points.size(); i++) {
				Point p1 = points.get(i - 1);
				Point p2 = points.get(i);
				mpoints.add(new Point((p1.getX() + p2.getX()) / 2,
						(p1.getY() + p2.getY()) / 2));
			}
			if (editingmode == POLYGON) { // complete the circle
				Point p1 = points.get(0);
				Point p2 = points.get(points.size() - 1);
				mpoints.add(new Point((p1.getX() + p2.getX()) / 2,
						(p1.getY() + p2.getY()) / 2));
			}
			index = 0;
			for (Point pt : mpoints) {
				
				if (midpointselected && insertingindex == index)
					graphic = new Graphic(pt,new SimpleMarkerSymbol(Color.RED, 20,
							SimpleMarkerSymbol.STYLE.CIRCLE));
				else
					graphic = new Graphic(pt,new SimpleMarkerSymbol(Color.GREEN, 15,
							SimpleMarkerSymbol.STYLE.CIRCLE));
				graphicsLayerEditing.addGraphic(graphic);
				index++;
			}
		}
	}

	public void drawVertices() {
		if(graphicsLayerEditing == null){
			graphicsLayerEditing = new GraphicsLayer();
			map.addLayer(graphicsLayerEditing);
		}
		int index;
		// draw vertices
		index = 0;
		for (Point pt : points) {
			/*if (vertexselected && index == insertingindex){
				Graphic graphic = new Graphic(pt,new SimpleMarkerSymbol(Color.RED, 20,
						SimpleMarkerSymbol.STYLE.CIRCLE));
				mapActivity.grapLayer.addGraphic(graphic);
			}
			else if (index == points.size() - 1 && !midpointselected && !vertexselected)
			{
				Graphic graphic = new Graphic(pt,new SimpleMarkerSymbol(Color.RED, 20,
						SimpleMarkerSymbol.STYLE.CIRCLE));			
				
				//int id = graphicsLayer.addGraphic(graphic);
			}
			else{
				Graphic graphic = new Graphic(pt,new SimpleMarkerSymbol(Color.BLACK, 20,
						SimpleMarkerSymbol.STYLE.CIRCLE));
				graphicsLayerEditing.addGraphic(graphic);
			}
	
			index++;*/
			Graphic graphic = new Graphic(pt,new SimpleMarkerSymbol(Color.BLACK, 20,
					SimpleMarkerSymbol.STYLE.CIRCLE));
			graphicsLayerEditing.addGraphic(graphic);
		}
	}
	public void refresh() {
		
		if(graphicsLayerEditing!=null)
			graphicsLayerEditing.removeAll();
		drawPolyline();
	//	drawMidPoints();
		drawVertices();
	}
	public void clear() {
		points.clear();
		mpoints.clear();
		midpointselected = false;
		vertexselected = false;
		insertingindex = 0;
		if(graphicsLayerEditing!=null){
			graphicsLayerEditing.removeAll();
		}
		grapLayer.removeAll();
		map.getCallout().hide();
	}
	public void exit(){
		points.clear();
		mpoints.clear();
		midpointselected = false;
		vertexselected = false;
		insertingindex = 0;
		if(graphicsLayerEditing!=null){
			graphicsLayerEditing.removeAll();
			map.removeLayer(graphicsLayerEditing);
			graphicsLayerEditing=null;
		}
		grapLayer.removeAll();
		map.removeLayer(grapLayer);
		grapLayer = null;
		for(int i=0;i<arcGISFeatureLayerList.size();++i){
			map.removeLayer(arcGISFeatureLayerList.get(i));
		}
		map.getCallout().hide();
		map.resetTouchListener();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public MapEditFacListView getMapEditFacListView() {
		return mapEditFacListView;
	}
	public void setMapEditFacListView(MapEditFacListView mapEditFacListView) {
		this.mapEditFacListView = mapEditFacListView;
	}
	public GraphicsLayer getGrapLayer() {
		return grapLayer;
	}
	public void setGrapLayer(GraphicsLayer grapLayer) {
		this.grapLayer = grapLayer;
	}
	public MyMapView getMap() {
		return map;
	}
	public void setMap(MyMapView map) {
		this.map = map;
	}
}
