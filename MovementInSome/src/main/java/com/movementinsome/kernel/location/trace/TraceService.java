package com.movementinsome.kernel.location.trace;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.movementinsome.app.pub.Constant;
import com.movementinsome.kernel.location.LocationInfoExt;

import java.io.Serializable;

/**
 * 位置跟踪服务
 * 
 * @author gordon
 * 
 */
public class TraceService extends Service {

	//ArrayList<CellInfo> cellIds = null;
	private Gps gps = null;
//	private Bgps bgps = null;
	private Agps agps = null;
	private boolean threadDisable = false;
	private final static String TAG = TraceService.class.getSimpleName();
	
	private static long minTime;
	private static float minDistance;

	private ZhdGps zhdGps=null;
	private  static Context context;
	
	public static void init(long minTime, float minDistance, Context context){
		TraceService.minTime = minTime;
		TraceService.minDistance = minDistance;
		TraceService.context=context;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		//GPS定位 
		gps = new Gps(TraceService.this,minTime,minDistance);
		
		//cellIds = TraceUtil.init(TraceService.this);
//		//百度定位
//		bgps = new Bgps(TraceService.this,minTime,minDistance);

		//高德
		agps = new Agps(TraceService.this,minTime,minDistance);

		//中海达定位
		zhdGps=new ZhdGps(context);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (!threadDisable) {
					try {
						Thread.sleep(1000);//1s采集一次
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//先用中海达获取坐标
					if (zhdGps!=null){
						// 获取经纬度
						LocationInfoExt location = null;
						try{
							location = zhdGps.getLocation();
							if (location.getQuality()==0){
								location=null;

							}
						}catch(NullPointerException ex){
							location = null;
						}

						if (null != location) {
							// 发送广播
							Intent intent = new Intent();
							intent.putExtra(Constant.TRACE_INFO, (Serializable) location);
							intent.setAction(Constant.SERVICE_NAME);
							sendBroadcast(intent);
						} else if (location == null) { // 当结束服务时gps为空
							// 获取经纬度
//							LocationInfoExt location = null;
							try {
								location = gps.getLocation();
							} catch (NullPointerException ex) {
								location = null;
							}

							// 如果gps无法获取经纬度，改用高德定位获取
							if (location == null) {
								// Log.v(TAG, "gps location null");
								// 2.根据基站信息获取经纬度
								try {
									location = agps.getLocation();
								} catch (Exception e) {
									location = null;
									e.printStackTrace();
								}
							} else if (null == location.getAddr() || "中国".equals(location.getAddr())) {
								try {
									location.setAddr(agps.getLocation().getAddr());
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								agps.closeLocation();  //减少流量消耗，如果GPS定位正常则关闭百度定位
							}

							if (null != location) {
								location.setQuality(0);
								// 发送广播
								Intent intent = new Intent();
								intent.putExtra(Constant.TRACE_INFO, (Serializable) location);
								intent.setAction(Constant.SERVICE_NAME);
								sendBroadcast(intent);
							}
						}


					}
				}
			}
		}).start();

	}

	@Override
	public void onDestroy() {
		threadDisable = true;
/*		if (cellIds != null && cellIds.size() > 0) {
			cellIds = null;
		}*/
		if (gps != null) {
			gps.closeLocation();
			gps = null;
		}
		if (agps != null) {
			agps.closeLocation();
			agps = null;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

}
