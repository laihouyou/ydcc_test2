package com.movementinsome.map.location;

public class BaiduLocation  {
	
//	private Handler mhandler;
//	private LocationMode tempMode = LocationMode.Hight_Accuracy;
//	private String tempcoor="gcj02";
//
//	/**百度定位*/
//	private LocationClient mLocationClient;
//	public MyLocationListener mMyLocationListener;
//
//	public BaiduLocation(Context context, Handler mhandler){
//		//this.context = context;
//		//this.map = map;
//		//this.translateParam = param;
//		this.mhandler = mhandler;
//
//		mLocationClient = new LocationClient(context);
//		mMyLocationListener = new MyLocationListener();
//		mLocationClient.registerLocationListener(mMyLocationListener);
//	}
//	@Override
//	public boolean start() {
//		// TODO Auto-generated method stub
//		LocationClientOption option = new LocationClientOption();
//		option.setLocationMode(tempMode);//定位模式:高精度、低功耗、仅设备
//		option.setCoorType(tempcoor);//坐标系:gcj02、bd0911,bd09
//		int span=1000;
//		option.setScanSpan(span);//定位时间间隔Ϊ0000ms
//		option.setIsNeedAddress(true);
//		mLocationClient.setLocOption(option);
//
//		mLocationClient.start();
//		return true;
//	}
//
//	@Override
//	public boolean stop() {
//		// TODO Auto-generated method stub
//		mLocationClient.stop();
//		return true;
//	}
//
//	@Override
//	public boolean pause() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean unpause() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	private class MyLocationListener implements BDLocationListener {
//
//		@Override
//		public void onReceiveLocation(BDLocation loc) {
//			/*mapPoint = TranslateCoordinateFactory.gpsTranslate(translateParam,
//					map.getSpatialReference(), loc.getLatitude(),
//					loc.getLongitude(), loc.getAltitude());*/
//
//			if (loc.getLocType() != BDLocation.TypeOffLineLocationNetworkFail ){
//				Map<String, Double> localHashMap = Gcj022Gps.gcj2wgs(loc.getLongitude(), loc.getLatitude());
//				LocationInfo locationInfo = new LocationInfo(localHashMap.get("lat"),
//						localHashMap.get("lon"), loc.getAltitude(), loc.getDirection(),
//						loc.getSpeed(), loc.getRadius(),loc.getTime(),0, 0,null,null,0);
//
//				if (loc.getLocType() == BDLocation.TypeGpsLocation){
//					System.out.println("gps定位");
//					Message msg = new Message();
//					msg.what = Constant.MSG_LOCATION_GPS;
//					mhandler.sendMessage(msg);
//					return;
//				}else if (loc.getLocType() == BDLocation.TypeNetWorkLocation){
//					System.out.println("网络定位:"+loc.getAddrStr());
//				}
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("location", locationInfo);
//				Message msg = new Message();
//				msg.setData(bundle);
//				msg.what = Constant.MSG_LOCATION_CHANGE;
//				mhandler.sendMessage(msg);
//			}
//			//doLocation();
//
///*			//Receive Location
//			StringBuffer sb = new StringBuffer(256);
//			sb.append("time : ");
//			sb.append(location.getTime());
//			sb.append("\nerror code : ");
//			sb.append(location.getLocType());
//			sb.append("\nlatitude : ");
//			sb.append(location.getLatitude());
//			sb.append("\nlontitude : ");
//			sb.append(location.getLongitude());
//			sb.append("\nradius : ");
//			sb.append(location.getRadius());
//			if (location.getLocType() == BDLocation.TypeGpsLocation){
//				sb.append("\nspeed : ");
//				sb.append(location.getSpeed());
//				sb.append("\nsatellite : ");
//				sb.append(location.getSatelliteNumber());
//				sb.append("\ndirection : ");
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//				sb.append(location.getDirection());
//			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
//				sb.append("\naddr : ");
//				sb.append(location.getAddrStr());
//				//��Ӫ����Ϣ
//				sb.append("\noperationers : ");
//				sb.append(location.getOperators());
//			}
//			//logMsg(sb.toString());
//			Log.i("BaiduLocationApiDem", sb.toString());*/
//		}
//
////		@Override
////		public void onConnectHotSpotMessage(String s, int i) {
////
////		}
//	}
}
