package com.movementinsome.kernel.location.trace;

import android.content.Context;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.movementinsome.AppContext;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.kernel.location.coordinate.Gcj022Gps;

import java.util.Map;

public class Agps {
	private LocationInfoExt locationInfo = null;

	private AMapLocationClient locationClient = null;
	private AMapLocationClientOption locationOption = null;
	private long minTime = 10;
	private float minDistance = 0;

	public Agps(Context context){
		//初始化client
		locationClient = new AMapLocationClient(AppContext.getInstance().getApplicationContext());
		locationOption = getDefaultOption(1000);
		//设置定位参数
		locationClient.setLocationOption(locationOption);
		// 设置定位监听
		locationClient.setLocationListener(locationListener);
	}

	public Agps(Context context, long minTime, float minDistance){
		//初始化client
		locationClient = new AMapLocationClient(AppContext.getInstance().getApplicationContext());
		locationOption = getDefaultOption((int) minTime);
		//设置定位参数
		locationClient.setLocationOption(locationOption);
		// 设置定位监听
		locationClient.setLocationListener(locationListener);
		locationClient.startLocation();
	}

	/**
	 * 默认的定位参数
	 * @since 2.8.0
	 * @author hongming.wang
	 *
	 */
	private AMapLocationClientOption getDefaultOption(int minTime){
		AMapLocationClientOption mOption = new AMapLocationClientOption();
		mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
		mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
		mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
		mOption.setInterval(minTime);//可选，设置定位间隔。默认为2秒
		mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
		mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
		mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
		AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
		mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
		mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
		mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
		mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
		return mOption;
	}
	
	public LocationInfoExt getLocation() {
		if (!locationClient.isStarted()){
			locationClient.startLocation();
		}
		return locationInfo;

	}
	
	public void closeLocation() {
		// 停止定位
		if (locationClient.isStarted()){
			locationClient.unRegisterLocationListener(locationListener);
			locationClient.stopLocation();
		}
	}


	/**
	 * 定位监听
	 */
	AMapLocationListener locationListener = new AMapLocationListener() {

		@Override
		public void onLocationChanged(AMapLocation aMapLocation) {
			String addr = "";
			String zoning = "";
			if (aMapLocation!=null){
				addr=aMapLocation.getAddress();
				zoning=aMapLocation.getDistrict();
				Map<String, Double> wgs84Map=Gcj022Gps.gcj2wgs(aMapLocation.getLongitude(),aMapLocation.getLatitude());
				locationInfo = new LocationInfoExt(
						wgs84Map.get("lat"),
						wgs84Map.get("lon"),
						aMapLocation.getAltitude(),
						aMapLocation.getAccuracy(),
						aMapLocation.getSpeed(),
						aMapLocation.getBearing(),
						aMapLocation.getTime()+"",
						0,
						0,
						"高德定位",
						addr,
						aMapLocation.getSatellites(),
						zoning
				);
				locationInfo.setSolutionUsedSats(aMapLocation.getSatellites());
				locationInfo.setLatitude_gcj02(aMapLocation.getLatitude());
				locationInfo.setLongitude_gcj02(aMapLocation.getLongitude());
			}
		}
	};
}
