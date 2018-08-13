package com.movementinsome.kernel.gpswidget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.location.LocationInfoExt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GpsViewActivity extends FullActivity {
	private int minTime = 1000;
	private int minDistance = 0;
	private static final String TAG = "GpsView";

	private LocationManager locationManager;
	private SatellitesView satellitesView;
	private TextView tv_sd;
	private TextView tv_gc;
	private TextView tv_pos_jd;
	private TextView tv_pos_zt;
	private TextView tv_jd;
	private TextView tv_wd;
	private TextView tv_kjwx;
	private TextView tv_jswx;
	
	private Button nearby_Back;

	private TraceReceiver receiver;
	private LocationInfoExt locationInfoExt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gps_view_activity);

		tv_sd = (TextView) findViewById(R.id.tv_sd);
		tv_gc = (TextView) findViewById(R.id.tv_gc);
		tv_pos_jd = (TextView) findViewById(R.id.tv_pos_jd);
		tv_pos_zt = (TextView) findViewById(R.id.tv_pos_zt);
		tv_jd = (TextView) findViewById(R.id.tv_jd);
		tv_wd = (TextView) findViewById(R.id.tv_wd);
		tv_kjwx = (TextView) findViewById(R.id.tv_kjwx);
		tv_jswx = (TextView) findViewById(R.id.tv_jswx);
		
		satellitesView = (SatellitesView) findViewById(R.id.satellitesView);
		
		nearby_Back = (Button) findViewById(R.id.nearby_Back);
		nearby_Back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 注册广播
		receiver = new TraceReceiver();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(Constant.SERVICE_NAME);
		registerReceiver(receiver, filter1);

		registerListener();

	}
    /**
     * 注册监听
     */
	private void registerListener() {
		if (locationManager == null) {
			locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		}
		//侦听位置信息(经纬度变化)
//		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//				minTime, minDistance, locationListener);
		// 侦听GPS状态，主要是捕获到的各个卫星的状态
		locationManager.addGpsStatusListener(gpsStatusListener);
		//TODO:考虑增加监听传感器中的方位数据，以使罗盘的北能自动指向真实的北向
	}
    /**
     * 移除监听
     */
	private void unregisterListener() {
		if (locationManager != null) {
			locationManager.removeGpsStatusListener(gpsStatusListener);
//			locationManager.removeUpdates(locationListener);
		}
	}
    /**
     * 坐标位置监听
     */
//	private LocationListener locationListener = new LocationListener() {
//
//		@Override
//		public void onLocationChanged(Location location) {
//			int fmt = Location.FORMAT_DEGREES;
//			tv_jd.setText("经度："+Location.convert(location==null?0:location.getLongitude(), fmt)+" E");
//			tv_wd.setText("纬度："+Location.convert(location==null?0:location.getLatitude(), fmt)+" N");
//			tv_gc.setText("高程："+(location==null?0:getBigDecimal(location.getAltitude(), 2))+" 米");
//			tv_sd.setText("速度："+(location==null?0:getBigDecimal(location.getSpeed(), 2))+" km/h");
//			tv_pos_jd.setText("精度："+(location==null?0:location.getAccuracy())+" 米");
//		}
//
//		@Override
//		public void onStatusChanged(String provider, int status, Bundle extras) {
//			//gpsStatusText.setText("onStatusChanged");
//			tv_pos_zt.setText("状态：未锁定");
//		}
//
//		@Override
//		public void onProviderEnabled(String provider) {
//			//gpsStatusText.setText("onProviderEnabled");
//			tv_pos_zt.setText("状态：信号开启");
//		}
//
//		@Override
//		public void onProviderDisabled(String provider) {
//			//gpsStatusText.setText("onProviderDisabled");
//			tv_pos_zt.setText("状态：信号关闭");
//
//		}
//
//	};
	
    /**
     * Gps状态监听
     */
	private GpsStatus.Listener gpsStatusListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			GpsStatus gpsStatus = locationManager.getGpsStatus(null);
			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX: {
				tv_pos_zt.setText("状态：锁定");
				//gpsStatusText.setText("GPS_EVENT_FIRST_FIX");
				// 第一次定位时间UTC gps可用
				// Log.v(TAG,"GPS is usable");
				int i = gpsStatus.getTimeToFirstFix();
				break;
			}

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {// 周期的报告卫星状态
				// 得到所有收到的卫星的信息，包括 卫星的高度角、方位角、信噪比、和伪随机号（及卫星编号）
				Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();

				List<GpsSatellite> satelliteList = new ArrayList<GpsSatellite>();

				int x=0,y=0;
				
				for (GpsSatellite satellite : satellites) {
					// 包括 卫星的高度角、方位角、信噪比、和伪随机号（及卫星编号）
					/*
					 * satellite.getElevation(); //卫星仰角
					 * satellite.getAzimuth();   //卫星方位角 
					 * satellite.getSnr();       //信噪比
					 * satellite.getPrn();       //伪随机数，可以认为他就是卫星的编号
					 * satellite.hasAlmanac();   //卫星历书 
					 * satellite.hasEphemeris();
					 * satellite.usedInFix();
					 */
					if (satellite.usedInFix()){
						x++;
					}
					y++;
					satelliteList.add(satellite);
				}

				tv_kjwx.setText("可见卫星："+y+" 颗");
				tv_jswx.setText("解算卫星："+x+" 颗");
				satellitesView.repaintSatellites(satelliteList);
				/*gpsStatusText.setText("GPS_EVENT_SATELLITE_STATUS:"
						+ satelliteList.size());*/
				break;
			}

			case GpsStatus.GPS_EVENT_STARTED: {
				//gpsStatusText.setText("GPS_EVENT_STARTED");
				break;
			}

			case GpsStatus.GPS_EVENT_STOPPED: {
				//gpsStatusText.setText("GPS_EVENT_STOPPED");
				break;
			}

			default:
				//gpsStatusText.setText("GPS_EVENT:" + event);
				break;
			}
		}
	};

/*	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			unregisterListener();
			finish();
			//System.exit(0);
			return false;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}*/
	
	@Override
	protected void onResume() {
		super.onResume();
		registerListener();
	}
	
	@Override
	protected void onDestroy() {
		unregisterListener();
		unregisterReceiver(receiver);
		super.onDestroy();
	}
	private String getBigDecimal(double value,int scale){
		int   roundingMode   =   4;//表示四舍五入，可以选择其他舍值方式，例如去尾，等等. 
		BigDecimal   bd   =   new   BigDecimal(value); 
		bd   =   bd.setScale(scale,roundingMode); 
		return bd.toString();
	}

	// 获取实时坐标广播数据
	private class TraceReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Constant.SERVICE_NAME.equals(action)) {    //获取坐标
				locationInfoExt = (LocationInfoExt) intent
						.getSerializableExtra(Constant.TRACE_INFO);
				tv_jd.setText("经度："+locationInfoExt.getLongitude());
				tv_wd.setText("纬度："+locationInfoExt.getLatitude());
				tv_gc.setText("高程："+locationInfoExt.getAltitude()+" 米");
				tv_sd.setText("速度："+locationInfoExt.getSpeed()+" km/h");
				tv_pos_jd.setText("精度："+locationInfoExt.getAccuracy()+" 米");
			}
		}
	}

}
