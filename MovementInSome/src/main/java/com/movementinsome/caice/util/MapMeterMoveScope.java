package com.movementinsome.caice.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
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
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.bizcenter.MainMineActivity;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.map.utils.MapUtil;
import com.movementinsome.map.view.MyMapView;

import java.util.ArrayList;
import java.util.List;

public class MapMeterMoveScope implements OnClickListener,OnGetGeoCoderResultListener {

	public static final String DANXIAN = "danxian";
	public static final String DUOXIAN = "duoxian";
	private int num;
	private int fillUid;
	private int[] dots = {R.drawable.dot0,R.drawable.dot3};
	private Resources res;
	private PictureMarkerSymbol pointSymbol;
	public GraphicsLayer grapPayer;//点
	public GraphicsLayer grapPayerW;//点(完成)
    public GraphicsLayer grapLayer;//线
	public GraphicsLayer grapLayerW;//线(完成)
	private GraphicsLayer grapFayer;//多边形
	private GraphicsLayer editorPayer;//编辑图层
	private SimpleFillSymbol sfs;
	public List<List<Double>> listPoint;
	private List<Double> listarea;
	private List<Integer> listUid;	//点图层添加的id
	private List<Integer> listLine;	//线图层添加的id
	public Polygon poly;
	private Point ptPrevious = null;//上一个点
	private String drawType;
	private MyMapView map;
	public String wpiId;
	private Context context;
	public static final String POINT="point";// 线
	public static final String VALUE="value";
	public static final String POINTS="points";// 线
	public static final String LINE="line";// 线
	public static final String ISEDIT="edit";// 跳转runform时判断是不是编辑数据
	public static final String GATHER="gather";// 跳转runform时判断是不是采集数据
	public static final String LINEPOINT="linePoint";// 线上的设施点
	public static final String POLY="poly";// 面
	public static final String SELECT="select";// 查属性
	public static final String SELECT2="select2";// 查属性
	public static final String MODIFYLIE="modifyLie";// 修改线属性
	public static final String CHECK="check";// 查看
	public static final String MOVE="move";// 采集模式
	public static final String DELETELINE="deleteLine";// 删除线
	MyTouchListener myTouchListener;
	private boolean isShowCallout=true;
//	private TaskListMainReceiver taskListMainReceiver;
	private TextView moveTvxs;

	private String isCompleteSubmit;
    public Point mPoint;
    public boolean isComplete=false;
	private boolean isDrawType;
	private MainMineActivity activity;
	private Graphic editGraphic;
	private GeoCoder mGeoCoder = null;
	private String pointAddress;
	private List<List<String>> pointIdSegment;
	private LatLng cenpt;
	private Handler handler;
	private OkHttpRequest okHttpRequest;

	private String facNum;

	public MapMeterMoveScope(Context context, MyMapView map, boolean isDrawType,
                             List<List<String>> pointIdSegment, LatLng cenpt, Handler handler, OkHttpRequest okHttpRequest) {
		this.context=context;
		this.activity=(MainMineActivity)context;
		this.map = map;
		this.isDrawType=isDrawType;
		this.pointIdSegment=pointIdSegment;
		res = context.getResources();
		grapPayer = new GraphicsLayer();
		grapPayerW = new GraphicsLayer();
		grapLayer = new GraphicsLayer();
		grapLayerW = new GraphicsLayer();
		grapFayer = new GraphicsLayer();
		editorPayer = new GraphicsLayer();
		listUid = new ArrayList<Integer>();
		listLine = new ArrayList<Integer>();
		listPoint = new ArrayList<List<Double>>();
		listarea = new ArrayList<Double>();
		pointAddress=new String();
		fillUid = -9;
		num = 0;
		map.addLayer(grapPayer);
		map.addLayer(grapPayerW);
		map.addLayer(grapLayer);
		map.addLayer(grapLayerW);
		map.addLayer(grapFayer);
		map.addLayer(editorPayer);
		map.setAllowRotationByPinch(true);
		myTouchListener=new MyTouchListener(context,map.getMapView());
		map.setOnTouchListener(myTouchListener);
//		moveTvxs=map.getMoveTvxs();
//		moveTvxs.setOnClickListener(this);

//		taskListMainReceiver = new TaskListMainReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(Constant.TASK_LIST_UPDATE_MAPRED);
//		filter.addAction(Constant.TASK_LIST_UPDATE_MAPYELLOW);
//		filter.addAction(Constant.TASK_LIST_UPDATE_MAPPOINT);
//		context.registerReceiver(taskListMainReceiver, filter);// 更新任务广播

		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(this);

	}

    public MapMeterMoveScope(Context context, MyMapView map, String wpiId, String isCompleteSubmit) {
		this.context=context;
		this.map = map;
		this.wpiId = wpiId;
		this.isCompleteSubmit=isCompleteSubmit;
		res = context.getResources();
		grapPayer = new GraphicsLayer();
		grapPayerW = new GraphicsLayer();
		grapLayer = new GraphicsLayer();
		grapLayerW = new GraphicsLayer();
		grapFayer = new GraphicsLayer();
		editorPayer = new GraphicsLayer();
		listUid = new ArrayList<Integer>();
		listLine = new ArrayList<Integer>();
		listPoint = new ArrayList<List<Double>>();
		listarea = new ArrayList<Double>();
		pointAddress=new String();
		fillUid = -9;
		num = 0;
		map.addLayer(grapPayer);
		map.addLayer(grapPayerW);
		map.addLayer(grapLayer);
		map.addLayer(grapLayerW);
		map.addLayer(grapFayer);
		map.addLayer(editorPayer);
		map.setAllowRotationByPinch(true);
		myTouchListener=new MyTouchListener(context,map.getMapView());
		map.setOnTouchListener(myTouchListener);
//		moveTvxs=map.getMoveTvxs();
//		moveTvxs.setOnClickListener(this);
		
//		taskListMainReceiver = new TaskListMainReceiver();
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(Constant.TASK_LIST_UPDATE_MAPRED);
//		filter.addAction(Constant.TASK_LIST_UPDATE_MAPYELLOW);
//		filter.addAction(Constant.TASK_LIST_UPDATE_MAPPOINT);
//		context.registerReceiver(taskListMainReceiver, filter);// 更新任务广播

		mGeoCoder = GeoCoder.newInstance();
		mGeoCoder.setOnGetGeoCodeResultListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}

	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

	}

	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
		if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(activity, "定位失败，请移动到空旷地带", Toast.LENGTH_LONG)
					.show();
			return;
		}
		pointAddress = reverseGeoCodeResult.getAddress();

	}

	class MyTouchListener extends MapOnTouchListener {

		public MyTouchListener(Context context, MapView view) {
			super(context, view);
			// TODO Auto-generated constructor stub
			
		}
		@Override
		public boolean onSingleTap(final MotionEvent point) {
			// TODO Auto-generated method stub
//            if (isDrawType){
//				meter(point.getX(), point.getY(),drawType,isCompleteSubmit);
//				mPoint=map.toMapPoint(point.getX(),point.getY());
//			}else {
//				if (AppContext.getInstance().getGrapPayerl()!=null) {
//					editorPayer.removeAll();
//					//
//					int[] num = AppContext.getInstance().getGrapPayerl().getGraphicIDs(point.getX(), point.getY(), 10);
//					if (num.length > 0) {
//						final Graphic graphic = AppContext.getInstance().getGrapPayerl().getGraphic(num[0]);
//						Geometry gememtry = graphic.getGeometry();
//						if (isShowCallout) {
//							// 显示测量结果
//							View view=null;
//							facNum=graphic.getAttributes().get("facNum").toString();
//							String facId=graphic.getAttributes().get("facId").toString();
//							String draw_type=graphic.getAttributes().get("draw_type").toString();
//							final Point arcPoint = map.toMapPoint(point.getX(), point.getY());
//							String baiduPointStr=ArcgisToBd09.toBd09Position(AppContext.getInstance().getCoordParam(),map.getSpatialReference(),arcPoint.getX(),arcPoint.getY());
//							String[] baiduPoint=baiduPointStr.split(" ");
//							String baiduX=baiduPoint[0];
//							String baiduY=baiduPoint[1];
//
//							if (draw_type.equals(POINT)){
//								view=okHttpRequest.getCompileView(null,false,
//										null,cenpt,null,true,facNum,facId);
//							}else if (draw_type.equals(LINE)){
//								//编辑数据
//								boolean isPointOrLine = false;
//								List<String> idList=null;
//								for (int i = 0; i < pointIdSegment.size(); i++) {
//									for (int j = 0; j < pointIdSegment.get(i).size(); j++) {
//										if (facId.equals(pointIdSegment.get(i).get(j))) {
//											isPointOrLine = true;
//											idList = pointIdSegment.get(i);
//											view=okHttpRequest.getCompileView(null,isPointOrLine,
//													idList,cenpt,null,true,facNum,facId);
//											break;
//										}
//									}
//								}
//
//							}
//							map.getCallout().hide();
//							map.getCallout().show((Point) gememtry, view);
//						}
//					}
//				}
//			}
			return super.onSingleTap(point);
		}

//		@Override
//		public boolean onDragPointerMove(MotionEvent from, MotionEvent point) {
//			if (AppContext.getInstance().getGrapPayerl()!=null) {
//				int[] num = AppContext.getInstance().getGrapPayerl().getGraphicIDs(point.getX(), point.getY(), 10);
//				if (num.length > 0) {
//					editGraphic = AppContext.getInstance().getGrapPayerl().getGraphic(num[0]);
//				}
//			}
//			return super.onDragPointerMove(from, point);
//		}
//
//		@Override
//		public boolean onDragPointerUp(MotionEvent from, MotionEvent point) {
//			PictureMarkerSymbol pointSymbols = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot2));
//			if (AppContext.getInstance().getGrapPayerl()!=null) {
//				int[] num = AppContext.getInstance().getGrapPayerl().getGraphicIDs(point.getX(), point.getY(), 10);
//				if(num.length>0) {
//					Graphic graphic = AppContext.getInstance().getGrapPayerl().getGraphic(num[0]);
//					Geometry gememtry = graphic.getGeometry();
//					AppContext.getInstance().getGrapPayerl().removeGraphic(num[0]);
//					Graphic grahic = new Graphic(gememtry, pointSymbols);
//					editorPayer.addGraphic(grahic);
//				}
//			}
//
//			return super.onDragPointerMove(from, point);
//		}
	}
	//	画区域（单击地图传屏幕点坐标）
	public  void meter(float x, float y, final String drawType, String isCompleteSubmit){
		final Point point = map.toMapPoint(x, y);
		List<Double> pointList = new ArrayList<Double>();
		pointList.add(point.getX());
		pointList.add(point.getY());
		//如过是测量面积或画区域
		if(POLY.equals(drawType)){
			listPoint.add(pointList);
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
				listarea.add((double) Math.abs(Math.round(poly.calculateArea2D())));
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
			listarea.add((double) Math.round(line.calculateLength2D()));
			ptPrevious = point;
		}
		if((LINE.equals(drawType)||POLY.equals(drawType))){
			if(isShowCallout){
				// 显示测量结果
				listPoint.add(pointList);
				if(listPoint.size()>1){
					String bdPoint= ArcgisToBd09.toBd09Position(
							AppContext.getInstance().getCoordTransform(),
							map.getSpatialReference(),
							point.getX(),
							point.getY());
					String[] bd09Point=bdPoint.split(" ");
					final LatLng latLng=new LatLng(Double.parseDouble(bd09Point[1])
							, Double.parseDouble(bd09Point[0]));
					mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
							.location(latLng));
					popupLayer(point);
			}
			//点
			pointSymbol = new PictureMarkerSymbol(res.getDrawable(dots[num]));
			Graphic grahic = new Graphic(point,pointSymbol);
			int puid = grapPayer.addGraphic(grahic);
			listUid.add(puid);
			num = num<1?++num:0;	
			}
		}else if (POINT.equals(drawType)){
			if(isShowCallout){
				// 显示测量结果
				listPoint.remove(listPoint);
				listPoint.add(pointList);

				TextView tv=new TextView(context);
				final String x2= String.valueOf(point.getX());
				final String y2= String.valueOf(point.getY());
				tv.setText("点击填写位置点信息");

				//转换成bd9,提交上百度云
				String bdPoint= ArcgisToBd09.toBd09Position(
						AppContext.getInstance().getCoordTransform(),
						map.getSpatialReference(),
						Double.parseDouble(x2),
						Double.parseDouble(y2));
				String[] bd09Point=bdPoint.split(" ");
				final LatLng latLng=new LatLng(Double.parseDouble(bd09Point[1])
						, Double.parseDouble(bd09Point[0]));
				mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
						.location(latLng));

				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
//						activity.setData(latLng,"录入数据",null,"",false,pointAddress,true,null,
//                                "","","","","","","","");
					}
				});
				map.getCallout().hide();
				map.getCallout().show(point,tv);
			}
			grapPayer.removeAll();
			pointSymbol = new PictureMarkerSymbol(res.getDrawable(dots[num]));
			Graphic grahic = new Graphic(point,pointSymbol);
			grapPayer.addGraphic(grahic);
		}else if (VALUE.equals(drawType)){
			if(isShowCallout){
				// 显示测量结果
				TextView tv=new TextView(context);
				final String x2= String.valueOf(point.getX());
				final String y2= String.valueOf(point.getY());
				tv.setText("点击查询阀门");
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String PolygonStr = "POINT  ("+x2+" "+y2+")";
					}
				});
				map.getCallout().hide();
				map.getCallout().show(point,tv);
			}
			grapPayer.removeAll();
			pointSymbol = new PictureMarkerSymbol(res.getDrawable(dots[num]));
			Graphic grahic = new Graphic(point,pointSymbol);
			grapPayer.addGraphic(grahic);
		}else if (SELECT.equals(drawType)){
			if(isShowCallout){
				// 显示测量结果
				TextView tv=new TextView(context);
				final String x2= String.valueOf(point.getX());
				final String y2= String.valueOf(point.getY());
				tv.setText("点击查询设施信息");
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String PolygonStr = "POINT  ("+x2+" "+y2+")";
					}
				});
				map.getCallout().hide();
				map.getCallout().show(point,tv);
			}
			grapPayer.removeAll();
			pointSymbol = new PictureMarkerSymbol(res.getDrawable(dots[num]));
			Graphic grahic = new Graphic(point,pointSymbol);
			grapPayer.addGraphic(grahic);
		}else if (SELECT2.equals(drawType)){
			if(isShowCallout){
				// 显示测量结果
				TextView tv=new TextView(context);
				final String x2= String.valueOf(point.getX());
				final String y2= String.valueOf(point.getY());
				tv.setText("点击查询设施信息");
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						String PolygonStr = "POINT  ("+x2+" "+y2+")";
					}
				});
				map.getCallout().hide();
				map.getCallout().show(point,tv);
			}
			grapPayer.removeAll();
			pointSymbol = new PictureMarkerSymbol(res.getDrawable(dots[num]));
			Graphic grahic = new Graphic(point,pointSymbol);
			grapPayer.addGraphic(grahic);
		}else if (POINTS.equals(drawType)){
			if(isShowCallout){
				// 显示测量结果
				TextView tv=new TextView(context);
				final String x2= String.valueOf(point.getX());
				final String y2= String.valueOf(point.getY());
				tv.setText("获取标识坐标");
//				AppContext.getInstance().setmMapPoint(point);
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
	 * 画点后弹窗
	 */
	public void popupLayer(Point point){
		if(listPoint.size()>1){
			View view = LayoutInflater.from(context).inflate(R.layout.infowindor_view2, null);
			RelativeLayout infoRe= (RelativeLayout) view.findViewById(R.id.infoRe);
			infoRe.setBackground(null);
			TextView infowindor_tvRevocation = (TextView) view.findViewById(R.id.infowindor_tvRevocation);//撤回
			infowindor_tvRevocation.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					recallPoint();
				}
			});

			TextView infowindor_tvTd = (TextView) view.findViewById(R.id.infowindor_tvTd);//填单
			infowindor_tvTd.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					List<LatLng> pointList=new ArrayList<LatLng>();
					for(int i=0;i<listPoint.size();i++){
						String bdPoint= ArcgisToBd09.toBd09Position(
								AppContext.getInstance().getCoordTransform(),
								map.getSpatialReference(),
								listPoint.get(i).get(0),
								listPoint.get(i).get(1));
						String[] bd09Point=bdPoint.split(" ");
						final LatLng latLng=new LatLng(Double.parseDouble(bd09Point[1])
								, Double.parseDouble(bd09Point[0]));
						pointList.add(latLng);
					}
//					activity.setDataLine(pointList,pointAddress,false,null,"","","","","","",true,"","");
				}
			});

			TextView infowindor_tvClose= (TextView) view.findViewById(R.id.infowindor_tvClose);//清除
			infowindor_tvClose.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					clearAllLayer();
				}
			});
			
//			Button move_ok = (Button) view.findViewById(R.id.move_ok);//完成
//			move_ok.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Toast.makeText(context, "测试完成按钮", Toast.LENGTH_SHORT).show();
//				}
//			});
			
			map.getCallout().hide();
			map.getCallout().show(point,view);
		}
	}


	/**
	 * 提交未完成的line
	 * @param isCompleteSubmit  提交完成后是否完成工程
	 *   0为完成  1为不完成
	 */

	public void submitLine(String isCompleteSubmit) {
		String PointList = "";
		for (int i = 0; i < listPoint.size(); i++) {
            if(i==listPoint.size()-1){
                String x = String.valueOf(listPoint.get(i).get(0));
                String y = String.valueOf(listPoint.get(i).get(1));
                PointList +=x+","+y;
            }else{
                String x = String.valueOf(listPoint.get(i).get(0));
                String y = String.valueOf(listPoint.get(i).get(1));
                PointList +=x+","+y+":";
            }
        }
//		HashMap<String, String> params = new HashMap<String, String>();
//		Intent newFormInfo = new Intent(
//                context, RunForm.class);
//		newFormInfo.putExtra("template",
//                "ins_hidden_trouble_form_zs.xml");
//		newFormInfo.putExtra("moveTable",
//                true);
//		params.put("wpId", wpiId);
//		if(drawType.equals("line")){
//            params.put("graph", "line");
//        }
//		params.put("PointList", PointList);
//		if(listPoint.size()>1&&listPoint.size()==2){
//            params.put("moveType", DANXIAN);
//        }else if(listPoint.size()>2){
//            params.put("moveType", DUOXIAN);
//        }else if(listPoint.size()==1){
//            params.put("moveType", POINT);
//        }
//        params.put("isCompleteSubmit",isCompleteSubmit);
//		newFormInfo.putExtra("moveparams", params);
//		context.startActivity(newFormInfo);
	}

	/**
	 * 把点串成图形
	 * @param taskType 
	 */
	public void catherCheck(String point2, String taskType){
		if(taskType.equals("point")){
			Point point = new Point();
			String[] pointStr=point2.split(",");
			point.setXY(Double.valueOf(pointStr[0]), Double.valueOf(pointStr[1]));
			grapPayer.removeAll();
			map.getCallout().hide();
			pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot1));
			Graphic grahic = new Graphic(point,pointSymbol);
			grapPayer.addGraphic(grahic);
			
		}else if(taskType.equals("line")){
			String[] pointStrList=point2.split(":");
			Graphic graphic = null;
			Polyline polylines = new Polyline();
			for (int i = 0; i < pointStrList.length; i++) {
				Point point = new Point();
				if(i==0){
					String[] pointStr=pointStrList[i].split(",");
					point.setXY(Double.valueOf(pointStr[0]), Double.valueOf(pointStr[1]));
					polylines.startPath(point);
					pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot0));
					Graphic grahicd = new Graphic(point,pointSymbol);
					graphic = new Graphic(point, new SimpleLineSymbol(
							Color.RED, 5));
				}else{
					String[] pointStr=pointStrList[i].split(",");
					point.setXY(Double.valueOf(pointStr[0]), Double.valueOf(pointStr[1]));
					polylines.lineTo(point);
				}
				SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.YELLOW, 5);
				Graphic g = new Graphic(polylines, lineSymbol);
				grapLayer.addGraphic(g);
			}
		}
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
		int size2 = listLine.size()-1;
		int size3 = listUid.size()-1;
		if(size >=1){
			if(LINE.equals(drawType)){
				//测量线长度
				grapLayer.removeGraphic(listLine.get(size2));
				grapPayer.removeGraphic(listUid.get(size2));
				listarea.remove(size);	//移除面积值
				listLine.remove(size2);	//移除线图层id
				listUid.remove(size3);	//移除点图层id
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
//				if(isShowCallout){
//					TextView tv=new TextView(context);
//					tv.setText(getAreaString());
//					map.getCallout().show(point,tv);
//				}
				popupLayer(point);
			}	
		}
	}



	public String getDrawType() {
		return drawType;
	}

	public void setDrawType(String drawType) {
		this.drawType = drawType;
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
	
//	public class TaskListMainReceiver extends BroadcastReceiver {
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (Constant.TASK_LIST_UPDATE_MAPRED.equals(action)) {
//				ptPrevious=null;
//				Line line = new Line();
//				Point pointStatt= new Point();
//				Point pointEnd= new Point();
//				List<Double> pointList2=listPoint.get(0);
//				List<Double> pointList3=listPoint.get(1);
//				pointStatt.setXY(pointList2.get(0),pointList2.get(1));
//				line.setStart(pointStatt);
//				pointEnd.setXY(pointList3.get(0),pointList3.get(1));
//				line.setEnd(pointEnd);
//				SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.RED, 5);
//				Polyline polyline = new Polyline();
//				polyline.addSegment(line, true);
//				Graphic g = new Graphic(polyline, lineSymbol);
//				grapLayerW.addGraphic(g);
//				listPoint.removeAll(listPoint);
////				listPoint.add(pointList3);
//				grapPayer.removeAll();
//				map.getCallout().hide();
//
//				String isCompleteSubmit=intent.getStringExtra("isCompleteSubmit");
//				if (isCompleteSubmit.equals("0")){
//					//完成工程
//					Dao<ProjectVo, Long> miningSurveyVOdao = null;
//					try {
//						miningSurveyVOdao = AppContext
//                                .getInstance().getAppDbHelper()
//                                .getDao(ProjectVo.class);
//						List<ProjectVo> miningSurveyVOList=miningSurveyVOdao.queryForEq("projectId",wpiId);
//						if (miningSurveyVOList.size()==1&&miningSurveyVOList!=null) {
//							final ProjectVo miningSurveyVO = miningSurveyVOList.get(0);
//							miningSurveyVO.setIsSubmit("0");
//							int s=miningSurveyVOdao.update(miningSurveyVO);
//							if (s==1){
//								Toast.makeText(context,"保存成功", Toast.LENGTH_LONG).show();
////                                map.getMoveDrawingLine().setVisibility(View.GONE);
////                                map.getMoveDrawing().setVisibility(View.GONE);
//								isComplete=true;
//							}
//						}
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//			}else if(Constant.TASK_LIST_UPDATE_MAPYELLOW.equals(action)){
//				ptPrevious=null;
//				Line line = new Line();
//				Graphic graphic = null;
//				Polyline polylines = new Polyline();
//				for (int i = 0; i < listPoint.size(); i++) {
//					Point point = new Point();
//					List<Double> listDouble = null;
//					if(i==0){
//						listDouble=listPoint.get(i);
//						point.setXY(listDouble.get(0), listDouble.get(1));
//						polylines.startPath(point);
//						pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot0));
//						Graphic grahicd = new Graphic(point,pointSymbol);
//						graphic = new Graphic(point, new SimpleLineSymbol(
//								Color.RED, 5));
//					}else{
//						listDouble=listPoint.get(i);
//						point.setXY(listDouble.get(0), listDouble.get(1));
//						polylines.lineTo(point);
//					}
//					SimpleLineSymbol lineSymbol = new SimpleLineSymbol(Color.YELLOW, 5);
//					Graphic g = new Graphic(polylines, lineSymbol);
//					grapLayerW.addGraphic(g);
//				}
//				int s = listPoint.size();
//				List<Double> endPoint=listPoint.get(s-1);
//				listPoint.removeAll(listPoint);
////				listPoint.add(endPoint);
//				grapPayer.removeAll();
//				map.getCallout().hide();
//
//				String isCompleteSubmit=intent.getStringExtra("isCompleteSubmit");
//				if (isCompleteSubmit.equals("0")){
//					//完成工程
//					Dao<ProjectVo, Long> miningSurveyVOdao = null;
//					try {
//						miningSurveyVOdao = AppContext
//								.getInstance().getAppDbHelper()
//								.getDao(ProjectVo.class);
//						List<ProjectVo> miningSurveyVOList=miningSurveyVOdao.queryForEq("projectId",wpiId);
//						if (miningSurveyVOList.size()==1&&miningSurveyVOList!=null) {
//							final ProjectVo miningSurveyVO = miningSurveyVOList.get(0);
//							miningSurveyVO.setIsSubmit("0");
//							int a=miningSurveyVOdao.update(miningSurveyVO);
//							if (a==1){
//								Toast.makeText(context,"保存成功", Toast.LENGTH_LONG).show();
////                                map.getMoveDrawingLine().setVisibility(View.GONE);
////                                map.getMoveDrawing().setVisibility(View.GONE);
//								isComplete=true;
//							}
//						}
//					} catch (SQLException e) {
//						e.printStackTrace();
//					}
//				}
//
//			}else if(Constant.TASK_LIST_UPDATE_MAPPOINT.equals(action)){
////				grapPayer.removeAll();
////				map.getCallout().hide();
////				List<Double> listDouble = listPoint.get(0);
////				Point point = new Point();
////				point.setXY(listDouble.get(0), listDouble.get(1));
////				pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot1));
////				Graphic grahic = new Graphic(point,pointSymbol);
////				grapPayerW.addGraphic(grahic);
//
//                grapPayer.removeAll();
//                map.getCallout().hide();
//				listPoint.clear();
////                List<Double> listDouble = listPoint.get(0);
//                double pointX=intent.getDoubleExtra("pointX",0);
//                double pointY=intent.getDoubleExtra("pointY",0);
//                Point point = new Point(pointX,pointY);
////                point.setXY(points[0],points[1]);
//                pointSymbol = new PictureMarkerSymbol(res.getDrawable(R.drawable.dot1));
//                Graphic grahic = new Graphic(point,pointSymbol);
//                grapPayerW.addGraphic(grahic);
//
//                String isCompleteSubmit=intent.getStringExtra("isCompleteSubmit");
//                if (isCompleteSubmit.equals("0")){
//                    //完成工程
//                    Dao<ProjectVo, Long> miningSurveyVOdao = null;
//                    try {
//                        miningSurveyVOdao = AppContext
//                                .getInstance().getAppDbHelper()
//                                .getDao(ProjectVo.class);
//                        List<ProjectVo> miningSurveyVOList=miningSurveyVOdao.queryForEq("projectId",wpiId);
//                        if (miningSurveyVOList.size()==1&&miningSurveyVOList!=null) {
//                            final ProjectVo miningSurveyVO = miningSurveyVOList.get(0);
//                            miningSurveyVO.setIsSubmit("0");
//                            int a=miningSurveyVOdao.update(miningSurveyVO);
//                            if (a==1){
//                                Toast.makeText(context,"保存成功", Toast.LENGTH_LONG).show();
////                                map.getMoveDrawingLine().setVisibility(View.GONE);
////                                map.getMoveDrawing().setVisibility(View.GONE);
//								isComplete=true;
//                            }
//                        }
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//			}
//		}
//	}

    public List<List<String>> getPointIdSegment() {
        return pointIdSegment;
    }

    public void setPointIdSegment(List<List<String>> pointIdSegment) {
        this.pointIdSegment = pointIdSegment;
    }

	public OkHttpRequest getOkHttpRequest() {
		return okHttpRequest;
	}

	public void setOkHttpRequest(OkHttpRequest okHttpRequest) {
		this.okHttpRequest = okHttpRequest;
	}

	public boolean isDrawType() {
		return isDrawType;
	}

	public void setDrawType(boolean drawType) {
		isDrawType = drawType;
	}

	public LatLng getCenpt() {
		return cenpt;
	}

	public void setCenpt(LatLng cenpt) {
		this.cenpt = cenpt;
	}

	public String getFacNum() {
		return facNum;
	}

	public void setFacNum(String facNum) {
		this.facNum = facNum;
	}
}
