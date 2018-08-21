package com.movementinsome.map.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.LocationDisplayManager.AutoPanMode;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.kernel.location.LocationInfo;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.map.coordinate.TranslateCoordinateFactory;
import com.movementinsome.map.utils.MapUtil;

public final class LocationManage {

	

	//private int serviceMode = LocationMode.BAIDU;
	private MapView map;
	private GraphicsLayer locationOverlay;
	private PictureMarkerSymbol locationSymbol;
	private LocationInfo locationInfo = null;
	private int locationMode = LocationMode.NORMAL;
	//private TranslateParam translateParam;
	private boolean first = true;
	private boolean change = false;
	private boolean isStart = false;
	private boolean isPause = false;
	private Point mapPoint = null;
	private Context context;

	//private ILocation location;//定位实现接口
	//private LocationService arcLocationService;
	private LocationDisplayManager arcLocationService;
/*	private boolean threadDisable = false;

	private Gps gps = null;
	private Bgps bgps = null;
	private Thread gpsrun;
	private Handler handler=new Handler();*/
	
	private float mTargetDirection = 0.0f;
	private Sensor mOrientationSensor;
	private SensorManager mSensorManager;
	private TraceReceiver receiver = null;

	// 获取广播数据
	private class TraceReceiver extends BroadcastReceiver {
		
/*			String userId;
			String phoneIMEI;
			String curUserName;
			//SpringUtil traceUpload;
*/			
		public TraceReceiver(){
/*				userId=AppContext.getInstance().getCurUser()==null?null:AppContext.getInstance().getCurUser().getUserId();
				phoneIMEI=AppContext.getInstance().getPhoneIMEI();
				curUserName=AppContext.getInstance().getCurUser().getUserName();*/
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			locationInfo = (LocationInfoExt) intent
					.getSerializableExtra(Constant.TRACE_INFO);
			doLocation();
/*				if (location != null) {

					location = (LocationInfoExt) TranslateCoordinateFactory.gpsTranslate(AppContext
							.getInstance().getCoordTransform(), AppContext
							.getInstance().getSrid(), location);

					location.setImei(phoneIMEI);
					location.setUsnum(curUserName);
					
					if(userId!=null&&!"null".equals(userId)){
						location.setUsid(Long.valueOf(userId));
					}
					// 作为全局位置信息获取来源
					AppContext.getInstance().setCurLocation(location);

				}*/
		}
	}

/*	public Thread iniThead(){
		if (gpsrun != null){
			gpsrun = null;
		}
		gpsrun = new Thread(new Runnable() {
			@Override
			public void run() {
				while (!threadDisable) {
					try {
						Thread.sleep(1000);//1s采集一次
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					if (gps != null) { // 当结束服务时gps为空
						// 获取经纬度
						locationInfo = gps.getLocation();
						// 如果gps无法获取经纬度，改用基站定位获取
						if (locationInfo == null) {
							// Log.v(TAG, "gps location null");
							// 2.根据基站信息获取经纬度
							try {
								locationInfo = bgps.getLocation();
							} catch (Exception e) {
								locationInfo = null;
								e.printStackTrace();
							}
							if (locationInfo == null) {
								// Log.v(TAG, "cell location null");
							}
						}
					}
					if (locationInfo != null) {
						doLocation();
					}
				}
			}
		});
		return gpsrun;
	}*/
	
	public LocationManage(Context context, MapView map) {
		this.context = context;
		this.map = map;
		//this.translateParam = param;
		//serviceMode = mode;
		locationOverlay = new GraphicsLayer();
		
		locationSymbol = new PictureMarkerSymbol(context.getResources()
				.getDrawable(R.drawable.gps_point));
		
		arcLocationService = map.getLocationDisplayManager();///.getLocationService();
		arcLocationService.setOpacity(0);
		arcLocationService.setAutoPanMode(AutoPanMode.OFF);
		
/*		arcLocationService.setAutoPan(false);
		arcLocationService.setBearing(true);*/
	}

	public boolean start() {
		this.map.addLayer(locationOverlay);
		if (receiver == null){
			receiver = new TraceReceiver();
			
		}
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.SERVICE_NAME);
		this.context.registerReceiver(receiver, filter);
		
/*		if (gps == null)
			gps = new Gps(context);
		//百度定位
		if (bgps == null)
			bgps = new Bgps(context);
		threadDisable = false;
		
		iniThead().start();
		
		*/
		
		if (mOrientationSensor == null){
			mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
			mOrientationSensor = mSensorManager
					.getDefaultSensor(Sensor.TYPE_ORIENTATION);//方向传感器
			mSensorManager.registerListener(mOrientationSensorEventListener,
					mOrientationSensor, SensorManager.SENSOR_DELAY_FASTEST);
			
			//SENSOR_DELAY_FASTEST(0毫秒延迟);  
            // SENSOR_DELAY_GAME(20,000毫秒延迟)、SENSOR_DELAY_UI(60,000毫秒延迟))  
		}
		
		isStart = true;
		return isStart;
	}

	public void stop() {
		//threadDisable = true;
		
		this.map.removeLayer(locationOverlay);
		if (mOrientationSensor != null) {
			mSensorManager.unregisterListener(mOrientationSensorEventListener);
			mOrientationSensor = null;
			mSensorManager = null;
		}
	//	location.stop();
		// 注销位置追踪信息接收
		
		if (receiver != null){
			if (!isPause)
				this.context.unregisterReceiver(receiver);
			receiver = null;
		}

		isPause = false;
		isStart = false;
	}

	public void pause() {
		isPause = true;
		if (isStart){
			//stop();
		//	threadDisable = true;
			//handler.removeCallbacks(gpsrun);
			//gpsrun.suspend();
			
			if (mOrientationSensor != null) {
				mSensorManager.unregisterListener(mOrientationSensorEventListener);
			}
			if (receiver != null)
				this.context.unregisterReceiver(receiver);
		}	
	}

	public void upPause() {
		if (isPause){
			//start();
		//	threadDisable = false;
			isPause = false;
			//gpsrun.run();
			//handler.postDelayed(gpsrun, 1000);
		//	iniThead().start();
			
			if (mOrientationSensor != null) {
				mSensorManager.registerListener(mOrientationSensorEventListener,
						mOrientationSensor, SensorManager.SENSOR_DELAY_FASTEST);
			}
			
			if (isStart){
				if (receiver == null){
					receiver = new TraceReceiver();
					
				}
				
				IntentFilter filter = new IntentFilter();
				filter.addAction(Constant.SERVICE_NAME);
				this.context.registerReceiver(receiver, filter);
			}
		}
	}

	public LocationInfo getLocation() {
		return locationInfo;
	}

	public void setLocationMode(int mode) {
		this.locationMode = mode;
		change = true;
		if (locationInfo != null) {
			doLocation();
		}
	}

	public boolean isStart() {
		return isStart;
	}

	private void doLocation() {
		if (this.map.getLayerByID(locationOverlay.getID())==null){
			this.map.addLayer(locationOverlay);
		}
		if (locationInfo==null){
			return;
		}
		if (!isPause) {

			mapPoint = TranslateCoordinateFactory.gpsTranslate(AppContext.getInstance().getCoordTransform(),
					map.getSpatialReference(), locationInfo.getLatitude(),
					locationInfo.getLongitude(), locationInfo.getAltitude());
			try{
				if (Double.isNaN(mapPoint.getX())||Double.isNaN(mapPoint.getY())){
					return;
				}
			}catch(Exception ex){
				return;
				
			}
			locationInfo.setMapx(mapPoint.getX());
			locationInfo.setMapy(mapPoint.getY());
			
			locationSymbol = new PictureMarkerSymbol(context.getResources()
					.getDrawable(R.drawable.gps_locationgrey));
			Graphic graphicPoint = new Graphic(mapPoint, locationSymbol);
			Graphic circle = MapUtil.createCircle(mapPoint,
					locationInfo.getAccuracy(), 20, Color.BLUE);
			if (first) {
				// locationOverlay.removeAll();
				// 图层的创建
				// locationOverlay.addGraphic(graphicPoint);
/*				Unit mapUnit = map.getMapUnit();// map.getSpatialReference().getUnit();
				double zoomWidth = Unit.convertUnits(map.SEARCH_RADIUS,
						Unit.create(LinearUnit.Code.MILE_US), mapUnit);
				Envelope zoomExtent = new Envelope(mapPoint, zoomWidth,
						zoomWidth);			
				map.setExtent(zoomExtent);*/
				
				map.setRotationAngle(0);
				map.centerAt(mapPoint, false);
				first = false;
			}
			switch (locationMode) {
			case LocationMode.NORMAL:
				locationOverlay.removeAll();
				
				locationOverlay.addGraphic(circle);
				
				locationSymbol = new PictureMarkerSymbol(context.getResources()
						.getDrawable(R.drawable.gps_point));
				locationSymbol.setAngle(0);
				graphicPoint = new Graphic(mapPoint, locationSymbol);

				locationOverlay.addGraphic(graphicPoint);
				if (change) {
					//map.centerAt(mapPoint, false);
					map.setRotationAngle(0, mapPoint, false);
					change = false;
				}
				break;
			case LocationMode.FOLLOWING:
				locationOverlay.removeAll();
				locationOverlay.addGraphic(circle);
				
				// 图层的创建
				locationSymbol = new PictureMarkerSymbol(context.getResources()
						.getDrawable(R.drawable.gps_navigation));
				locationSymbol.setAngle(mTargetDirection);//locationInfo.getBearing());
				graphicPoint = new Graphic(mapPoint, locationSymbol);
				locationOverlay.addGraphic(graphicPoint);
				
				map.setRotationAngle(0, mapPoint, false);
				break;
			case LocationMode.COMPASS:		
				locationOverlay.removeAll();
				// 图层的创建
				locationOverlay.addGraphic(circle);
				//locationSymbol.setAngle(locationInfo.getBearing());
				locationSymbol = new PictureMarkerSymbol(context.getResources()
						.getDrawable(R.drawable.gps_compass));
				locationSymbol.setAngle(0);//locationInfo.getBearing());
				graphicPoint = new Graphic(mapPoint, locationSymbol);
				locationOverlay.addGraphic(graphicPoint);
				
				map.setRotationAngle(mTargetDirection, mapPoint, false);

				break;
			default:
				locationOverlay.removeAll();
				// 图层的创建
				locationOverlay.addGraphic(graphicPoint);
				map.centerAt(mapPoint, false);
				break;
			}

		}
	}
	
	private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {

		@Override
		public void onSensorChanged(SensorEvent event) {
			float tmpDir = 0;
			// 方向传感器
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            	// x表示手机指向的方位，0表示北,90表示东，180表示南，270表示西
				// float direction = event.values[0] * -1.0f;
				tmpDir= event.values[0];// 360.0f - direction;//
													// normalizeDegree(direction);
				
				if (Math.abs(tmpDir-mTargetDirection)>1){
					mTargetDirection = tmpDir;
					doLocation();
				}else{
					mTargetDirection = tmpDir;
				}
            }
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
	};
}
