package com.movementinsome.kernel.location.trace;

import android.content.Context;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.model.LatLng;
import com.movementinsome.caice.util.Bd09toArcgis;
import com.movementinsome.kernel.location.LocationInfoExt;

import java.util.Map;

public class Bgps {
	private LocationInfoExt locationInfo = null;
	
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="bd09ll";
	
	/**百度定位*/
	private static LocationClient mLocationClient;
	public MyLocationListener mMyLocationListener;
	private long minTime = 10;
	private float minDistance = 0;
	
	public Bgps(Context context){		
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//定位模式:高精度、低功耗、仅设备
		option.setCoorType(tempcoor);//坐标系:gcj02、bd0911,bd09
		int span=1000;
		option.setScanSpan(span);//定位时间间隔Ϊ0000ms
		option.setIsNeedAddress(true);
		//可选，设置是否需要地址信息，默认不需要

		option.setOpenGps(true);
		//可选，默认false,设置是否使用gps

		option.setLocationNotify(true);
		//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

		option.setIsNeedLocationDescribe(true);
		//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

		option.setIsNeedLocationPoiList(true);
		//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

		option.setIsNeedAltitude(true);
		//可选，获取高度信息，目前只有是GPS定位结果时或者设置LocationClientOption.setIsNeedAltitude(true)时才有效，单位米

		mLocationClient.setLocOption(option);
	
		//mLocationClient.start();
	}
	
	public Bgps(Context context, long minTime, float minDistance){		
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//定位模式:高精度、低功耗、仅设备
		option.setCoorType(tempcoor);//坐标系:gcj02、bd0911,bd09
		int span= (int) minTime;
		option.setScanSpan(span);//定位时间间隔Ϊ0000ms

		option.setIsNeedAddress(true);
		//可选，设置是否需要地址信息，默认不需要

		option.setOpenGps(true);
		//可选，默认false,设置是否使用gps

		option.setLocationNotify(true);
		//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

		option.setIsNeedLocationDescribe(true);
		//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

		option.setIsNeedLocationPoiList(true);
		//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

		option.setIgnoreKillProcess(false);
		//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

		option.setIsNeedAltitude(true);
		//可选，获取高度信息，目前只有是GPS定位结果时或者设置LocationClientOption.setIsNeedAltitude(true)时才有效，单位米

		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}
	
	public LocationInfoExt getLocation() {
		if (!mLocationClient.isStarted()){
			mLocationClient.start();
		}
		return locationInfo;

	}
	
	public void closeLocation() {
		if (mLocationClient.isStarted()){
			mLocationClient.unRegisterLocationListener(mMyLocationListener);
			mLocationClient.stop();
		}
	}

	public static void startlocation(boolean isLocation){
		if (mLocationClient!=null&&isLocation==true){
			mLocationClient.start();
		}
	}
	
	private class MyLocationListener extends BDAbstractLocationListener {

		@Override
		public void onReceiveLocation(BDLocation loc) {
			String addr = "";
			String zoning = "";
			if (loc.getLocType() != BDLocation.TypeOffLineLocationNetworkFail ){
				zoning = loc.getDistrict()==null?"":loc.getDistrict();
				addr = (loc.getDistrict()==null?"":loc.getDistrict())+loc.getStreet()+(loc.getStreetNumber()==null?"":loc.getStreetNumber());//loc.getAddrStr()
                LatLng latLng_bd09=new LatLng(loc.getLatitude(),loc.getLongitude());
                Map<String,Double>  map_wgs84= Bd09toArcgis.bd09ToWg84(latLng_bd09);
                locationInfo = new LocationInfoExt(map_wgs84.get("lat"),
						map_wgs84.get("lon"), loc.getAltitude(), loc.getRadius(),
						loc.getSpeed(), loc.getDirection(), loc.getTime(), 0, 0,"百度",addr,loc.getSatelliteNumber(),zoning);
				locationInfo.setSolutionUsedSats(loc.getSatelliteNumber());
                locationInfo.setLatitude_bd09(loc.getLatitude());
                locationInfo.setLongitude_bd09(loc.getLongitude());
			}
		}
	}
}
