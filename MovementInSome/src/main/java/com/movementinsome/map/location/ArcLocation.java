package com.movementinsome.map.location;

import java.util.Iterator;

import android.content.Context;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.kernel.location.LocationInfo;
import com.movementinsome.kernel.util.MyDateTools;

public class ArcLocation implements ILocation {

	private Context context;
	private MapView map;
	// private TranslateParam translateParam;
	//private LocationService arcLocationService;
	private LocationDisplayManager arcLocationService;
	private LocationManager lm;
	// private GraphicsLayer locationOverlay;
	// private PictureMarkerSymbol locationSymbol;
	// private Point mapPoint = null;
	private Handler mhandler;

	public ArcLocation(Context context, MapView map, Handler mhandler) {
		this.context = context;
		this.map = map;
		// this.translateParam = param;
		this.mhandler = mhandler;

		lm = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		String bestProvider = lm.getBestProvider(getCriteria(), true);
		lm.requestLocationUpdates(bestProvider, 1000, 0, locationlistener);
		// 绑定监听状态
		lm.addGpsStatusListener(gpsListener);

		arcLocationService = map.getLocationDisplayManager();//.getLocationService();
		arcLocationService.setOpacity(0);
		//arcLocationService.setAutoPan(false);
		//arcLocationService.setBearing(true);
		// arcLocationService.setSymbol(locationSymbol);
		//arcLocationService.setLocationListener(locationlistener);
	}

	// 为获取地理位置信息时设置查询条件
	private Criteria getCriteria() {

		Criteria criteria = new Criteria();
		// 设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 获取精准位置
		// 设置是否允许运营商收费
		criteria.setCostAllowed(true);// 允许产生开销
		criteria.setPowerRequirement(Criteria.POWER_HIGH);// 消耗大的话，获取的频率高
		// 设置是否要求速度
		criteria.setSpeedRequired(true);// 手机位置移动
		// 设置是否需要海拔信息
		criteria.setAltitudeRequired(true);// 海拔
		// 设置是否需要方位信息
		criteria.setBearingRequired(true);
		// 设置对电源的需求
		// criteria.setPowerRequirement(Criteria.POWER_LOW);
		return criteria;
	}

	@Override
	public boolean start() {
		// TODO Auto-generated method stub
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			String bestProvider = lm.getBestProvider(getCriteria(), true);
			lm.requestLocationUpdates(bestProvider, 1000, 0, locationlistener);
			Location location = lm.getLastKnownLocation(bestProvider);
/*			if (location == null) {
				// List<String> list=lm.getAllProviders();
				lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						1000, 0, locationlistener);
				location = lm
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				if (location != null) {
					// latitude = location.getLatitude(); //经度
					// longitude = location.getLongitude(); //纬度
					// lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
					// 1000, 0, locationlistener);
					lm.removeUpdates(locationlistener);
				}
			}*/
			return true;
		}else{
			Message msg = new Message();
			msg.what = Constant.MSG_LOCATION_FAIL;
			mhandler.sendMessage(msg);
			return true;
		}
		//arcLocationService.start();
		
	}

	@Override
	public boolean stop() {
		// TODO Auto-generated method stub
		//arcLocationService.stop();
		lm.removeUpdates(locationlistener);
		return true;
	}

	@Override
	public boolean pause() {
		// TODO Auto-generated method stub
		//arcLocationService.stop();
		lm.removeUpdates(locationlistener);
		return true;
	}

	@Override
	public boolean unpause() {
		// TODO Auto-generated method stub
		//arcLocationService.start();
		String bestProvider = lm.getBestProvider(getCriteria(), true);
		lm.requestLocationUpdates(bestProvider, 1000, 0, locationlistener);
		return true;
	}

	private LocationListener locationlistener = new LocationListener() {
		@Override
		public void onLocationChanged(Location loc) {
			if (loc == null) {
				return;
			}

			/*
			 * mapPoint =
			 * TranslateCoordinateFactory.gpsTranslate(translateParam,
			 * map.getSpatialReference(), loc.getLatitude(), loc.getLongitude(),
			 * loc.getAltitude());
			 */
			LocationInfo locationInfo = new LocationInfo(loc.getLatitude(),
					loc.getLongitude(), loc.getAltitude(), loc.getAccuracy(),
					loc.getSpeed(), loc.getBearing(), MyDateTools.long2String(
							loc.getTime(), MyDateTools.S_DATE_FORMAT), 0, 0,null,null,0);

			Bundle bundle = new Bundle();
			bundle.putSerializable("location", locationInfo);
			Message msg = new Message();
			msg.setData(bundle);
			msg.what = Constant.MSG_LOCATION_CHANGE;
			mhandler.sendMessage(msg);
			// doLocation();
		}

		public void onProviderDisabled(String provider) {
			Message msg = new Message();
			msg.what = Constant.MSG_LOCATION_FAIL;
			mhandler.sendMessage(msg);
/*			if (!lm.isProviderEnabled(provider)) {
				System.out.println("ProviderDisabled:" + provider);
				Message msg = new Message();
				msg.what = Constant.MSG_LOCATION_FAIL;
				mhandler.sendMessage(msg);
			} else {
				Location loc = lm
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (loc != null) {
					LocationInfo locationInfo = new LocationInfo(
							loc.getLatitude(), loc.getLongitude(),
							loc.getAltitude(), loc.getAccuracy(),
							loc.getSpeed(), loc.getBearing(), MyDateTools.long2String(
									loc.getTime(), MyDateTools.S_DATE_FORMAT), 0,
							0);
					Bundle bundle = new Bundle();
					bundle.putSerializable("location", locationInfo);
					Message msg = new Message();
					msg.setData(bundle);
					msg.what = Constant.MSG_LOCATION_CHANGE;
					mhandler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = Constant.MSG_LOCATION_FAIL;
					mhandler.sendMessage(msg);
				}
			}*/
		}

		public void onProviderEnabled(String provider) {
			System.out.println("ProviderEnabled:" + provider);
			Location location = lm.getLastKnownLocation(provider);
		}

		/**
		 * GPS状态变化时触发
		 */
		public void onStatusChanged(String provider, int status, Bundle extras) {
			System.out.println("StatusChanged:" + provider);
			GpsStatus gpsStatus = lm.getGpsStatus(null);

			switch (status) {
			// GPS状态为可见时
			case LocationProvider.AVAILABLE:
				// Log.i(TAG, "当前GPS状态为可见状态");
				break;
			// GPS状态为服务区外时
			case LocationProvider.OUT_OF_SERVICE:
				// Log.i(TAG, "当前GPS状态为服务区外状态");
				break;
			// GPS状态为暂停服务时
			case LocationProvider.TEMPORARILY_UNAVAILABLE:
				// Log.i(TAG, "当前GPS状态为暂停服务状态");
				break;
			}
		}

	};

	// 状态监听
	GpsStatus.Listener gpsListener = new GpsStatus.Listener() {
		public void onGpsStatusChanged(int event) {
			GpsStatus gpsStatus = lm.getGpsStatus(null);

			switch (event) {
			case GpsStatus.GPS_EVENT_FIRST_FIX: {
				// 第一次定位时间UTC gps可用
				// Log.v(TAG,"GPS is usable");
				int i = gpsStatus.getTimeToFirstFix();
				// Utils.DisplayToastShort(GPSService.this, "GPS 第一次可用  "+i);
				// Utils.setGPSStatus(Utils.GPS_STATUS.START);
				break;
			}

			case GpsStatus.GPS_EVENT_SATELLITE_STATUS: {// 周期的报告卫星状态
				// 得到所有收到的卫星的信息，包括 卫星的高度角、方位角、信噪比、和伪随机号（及卫星编号）
				Iterable<GpsSatellite> allSatellites;
				allSatellites = gpsStatus.getSatellites();
				Iterator<GpsSatellite> iterator = allSatellites.iterator();
				int numOfSatellites = 0;
				int maxSatellites = gpsStatus.getMaxSatellites();
				while (iterator.hasNext() && numOfSatellites < maxSatellites) {
					numOfSatellites++;
					iterator.next();
				}
				// Log.v(TAG,"GPS is **unusable** "+ numOfSatellites +"      "+
				// maxSatellites);
				if (numOfSatellites < 3) {

					// Utils.DisplayToastShort(GPSService.this, "***卫星少于3颗***");
					// Utils.setGPSStatus(Utils.GPS_STATUS.STOP);
					Message msg = new Message();
					msg.what = Constant.MSG_LOCATION_FAIL;
					mhandler.sendMessage(msg);
				} else if (numOfSatellites > 7) {
					// Utils.setGPSStatus(Utils.GPS_STATUS.START);
				}
				break;
			}

			case GpsStatus.GPS_EVENT_STARTED: {
				// Utils.DisplayToastShort(GPSService.this, "GPS start Event");
				break;
			}

			case GpsStatus.GPS_EVENT_STOPPED: {
				// Utils.DisplayToastShort(GPSService.this,
				// "GPS **stop*** Event");
				// Utils.setGPSStatus(Utils.GPS_STATUS.STOP);
				Message msg = new Message();
				msg.what = Constant.MSG_LOCATION_FAIL;
				mhandler.sendMessage(msg);
				break;
			}

			default:
				break;
			}
		};
	};
}
