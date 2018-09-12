package com.baidu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.model.AMapNaviLocation;

import java.util.ArrayList;
import java.util.List;

public class BaiduAppProxy {

//	public static BMapManager mBMapManager = null;
//	private static BNRoutePlanNode mBNRoutePlanNode = null;
	private static boolean hasRequestComAuth = false;
	public static List<Activity> activityList = new ArrayList<Activity>();
	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	private final static String authComArr[] = { Manifest.permission.READ_PHONE_STATE };
	private static INaviInfoCallback iNaviInfoCallback=new INaviInfoCallback() {
		@Override
		public void onInitNaviFailure() {

		}

		@Override
		public void onGetNavigationText(String s) {

		}

		@Override
		public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

		}

		@Override
		public void onArriveDestination(boolean b) {

		}

		@Override
		public void onStartNavi(int i) {

		}

		@Override
		public void onCalculateRouteSuccess(int[] ints) {

		}

		@Override
		public void onCalculateRouteFailure(int i) {

		}

		@Override
		public void onStopSpeaking() {

		}

		@Override
		public void onReCalculateRoute(int i) {

		}

		@Override
		public void onExitPage(int i) {

		}

		@Override
		public void onStrategyChanged(int i) {

		}

		@Override
		public View getCustomNaviBottomView() {
			return null;
		}

		@Override
		public View getCustomNaviView() {
			return null;
		}

		@Override
		public void onArrivedWayPoint(int i) {

		}
	};

	/**
	 * 新版高德地图导航
	 * @param startLatlng 起点坐标
	 * @param endLatlng 终点坐标
	 */
	public static void CallBaiduNavigationLatLng(LatLng startLatlng, LatLng endLatlng, AmapNaviType amapNaviType,Context context){
		Poi startPoi=new Poi("起点",startLatlng,"");
		Poi endPoi=new Poi("终点",endLatlng,"");
		AmapNaviParams amapNaviParams=new AmapNaviParams(startPoi,null,endPoi,amapNaviType);
		amapNaviParams.setUseInnerVoice(true);
		AmapNaviPage.getInstance().showRouteActivity(context.getApplicationContext(),amapNaviParams,iNaviInfoCallback);
	}


	//	/**
//	 * 新版百度导航
//	 * @param context
//	 * @param coType 坐标类型
//	 * @param startLongitude 起点经度
//	 * @param startLatitude 起点纬度
//	 * @param endLongitude 终点经度
//	 * @param endLatitude 终点纬度
//	 */
//	public static void CallBaiduNavigationDouble(final Activity context,BNRoutePlanNode.CoordinateType coType,
//												 double startLongitude,double startLatitude,double endLongitude,double endLatitude){
//
//		// 权限申请
//		if (android.os.Build.VERSION.SDK_INT >= 23) {
//			// 保证导航功能完备
//			if (!hasCompletePhoneAuth(context)) {
//				if (!hasRequestComAuth) {
//					hasRequestComAuth = true;
////                    this.requestPermissions(authComArr, authComRequestCode);
//					return;
//				} else {
//					Toast.makeText(context, "没有完备的权限!", Toast.LENGTH_SHORT).show();
//				}
//			}
//
//		}
//
//		BNRoutePlanNode sNode = null;
//		BNRoutePlanNode eNode = null;
//		switch (coType) {
//			case GCJ02: {
//				sNode=new BNRoutePlanNode(startLongitude, startLatitude,"当前定位位置", null, coType);
//				eNode=new BNRoutePlanNode(endLongitude,endLatitude,"地图选取位置", null, coType);
//				break;
//			}
//			case WGS84: {
//				sNode=new BNRoutePlanNode(startLongitude, startLatitude,"当前定位位置", null, coType);
//				eNode=new BNRoutePlanNode(endLongitude,endLatitude,"地图选取位置", null, coType);
//				break;
//			}
//			case BD09_MC: {
//				sNode=new BNRoutePlanNode(startLongitude,startLatitude,"当前定位位置", null, coType);
//				eNode=new BNRoutePlanNode(endLongitude,endLatitude,"地图选取位置", null, coType);
//				break;
//			}
//			case BD09LL: {
//				sNode=new BNRoutePlanNode(startLongitude, startLatitude,"当前定位位置", null, coType);
//				eNode=new BNRoutePlanNode(endLongitude,endLatitude,"地图选取位置", null, coType);
//				break;
//			}
//			default:
//				break ;
//		}
//		if (sNode != null && eNode != null) {
//			List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
//			list.add(sNode);
//			list.add(eNode);
//			mBNRoutePlanNode=sNode;
//			BaiduNaviManager.getInstance().launchNavigator(context, list, 1, true, new BaiduNaviManager.RoutePlanListener() {
//
//				@Override
//				public void onRoutePlanFailed() {
//					// TODO 自动生成的方法存根
//					Toast.makeText(context, "算路失败", Toast.LENGTH_SHORT).show();
//				}
//
//				@Override
//				public void onJumpToNavigator() {
//					// TODO 自动生成的方法存根
//    				/*
//    				 * 设置途径点以及resetEndNode会回调该接口
//    				 */
//
//					for (Activity ac : activityList) {
//
//						if (ac.getClass().getName().endsWith("BNDemoGuideActivity")) {
//
//							return;
//						}
//					}
//					Intent intent = new Intent(context, BNDemoGuideActivity.class);
//					Bundle bundle = new Bundle();
//					bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
//					intent.putExtras(bundle);
//					context.startActivity(intent);
//				}
//			});
//		}
//	}
//
//	/**
//	 * 百度骑行导航
//	 * @param context
//	 * @param startLatlng
//	 * @param endLatlng
//	 */
//	public static void CyclingNavigation(final Context context, LatLng startLatlng, LatLng endLatlng) {
//		final BikeNavigateHelper mNaviHelper = BikeNavigateHelper.getInstance();
//		final BikeNaviLaunchParam param;
//		try {
//			param = new BikeNaviLaunchParam().stPt(startLatlng).endPt(endLatlng);
//
//			mNaviHelper.initNaviEngine((Activity) context, new IBEngineInitListener() {
//				@Override
//				public void engineInitSuccess() {
//					Log.d("View", "engineInitSuccess");
//					mNaviHelper.routePlanWithParams(param, new IBRoutePlanListener() {
//						@Override
//						public void onRoutePlanStart() {
//							Log.d("View", "onRoutePlanStart");
//						}
//
//						@Override
//						public void onRoutePlanSuccess() {
//							Log.d("View", "onRoutePlanSuccess");
//							Intent intent = new Intent();
//							intent.setClass(context, BNaviGuideActivity.class);
//							context.startActivity(intent);
//						}
//
//						@Override
//						public void onRoutePlanFail(BikeRoutePlanError error) {
//							Log.d("View", "onRoutePlanFail");
//						}
//
//					});
//				}
//
//				@Override
//				public void engineInitFail() {
//					Log.d("View", "engineInitFail");
//				}
//			});
//
//			// 设置诱导信息回调监听
//			mNaviHelper.setTTsPlayer(new IBTTSPlayer() {
//
//				/**
//				 * 诱导文本回调
//				 * @param s 诱导文本
//				 * @param b 是否抢先播报
//				 * @return
//				 */
//				@Override
//				public int playTTSText(String s, boolean b) {
//					return 0;
//				}
//			});
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	/**
//	 * 百度步行导航
//	 * @param context
//	 * @param startLatlng
//	 * @param endLatlng
//	 */
//	public static void PedestrianNavigation(final Context context, LatLng startLatlng, LatLng endLatlng) {
//		final WalkNavigateHelper mWNaviHelper = WalkNavigateHelper.getInstance();
//		final WalkNaviLaunchParam walkParam;
//
//		try {
//			walkParam = new WalkNaviLaunchParam().stPt(startLatlng).endPt(endLatlng);
//
//			mWNaviHelper.initNaviEngine((Activity) context, new IWEngineInitListener() {
//				@Override
//				public void engineInitSuccess() {
//					Log.d("View", "engineInitSuccess");
//					mWNaviHelper.routePlanWithParams(walkParam, new IWRoutePlanListener() {
//						@Override
//						public void onRoutePlanStart() {
//							Log.d("View", "onRoutePlanStart");
//						}
//
//						@Override
//						public void onRoutePlanSuccess() {
//							Log.d("View", "onRoutePlanSuccess");
//							Intent intent = new Intent();
//							intent.setClass(context, WNaviGuideActivity.class);
//							context.startActivity(intent);
//						}
//
//						@Override
//						public void onRoutePlanFail(WalkRoutePlanError error) {
//							Log.d("View", "onRoutePlanFail");
//						}
//
//					});
//				}
//
//				@Override
//				public void engineInitFail() {
//					Log.d("View", "engineInitFail");
//				}
//			});
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//
//	private static boolean hasCompletePhoneAuth(Activity context) {
//		// TODO Auto-generated method stub
//
//		PackageManager pm = context.getPackageManager();
//		for (String auth : authComArr) {
//			if (pm.checkPermission(auth, context.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	public static void callStreetScape(Context context,String position){
//		Intent intent = null;
//		intent = new Intent(context, demos[0].demoClass);
//		intent.putExtra("type", demos[0].type);
//		intent.putExtra("position", position);   //坐标位置
//		context.startActivity(intent);
//	}
//
//	public static void callStreetScapeNull(Context context,String position){
//		Intent intent = null;
//		intent = new Intent(context, demos[0].demoClass);
//		intent.putExtra("type", demos[0].type);
//		intent.putExtra("position", position);   //坐标位置
//		intent.putExtra("autoClose", true);
//		context.startActivity(intent);
//	}
//
//	public static String webM2Bd09Position(double x,double y){
//		double wgslonlat[] = Gps2Mct.mercator2lonLat(x, y);
//		double bdlonlat[] = Gcj022Bd09.bd09Encrypt(wgslonlat[1], wgslonlat[0]);
//		return bdlonlat[0]+" "+bdlonlat[1];
//	}
//
//	public static String local2Bd09Position(CoordParam param, double x, double y){
//		double wgslonlat[] = Gps2Locale.RevTranslate(x, y, 0, param);
//		Map<String, Double> lonlat = Gcj022Gps.wgs2gcj(wgslonlat[0], wgslonlat[1]);
//		double bdlonlat[] = Gcj022Bd09.bd09Encrypt(lonlat.get("lat"), lonlat.get("lon"));
//		return	 bdlonlat[0]+" "+bdlonlat[1];
//	}
//
//	public static String wgs2Bd09Position(double longitude,double latitude){
//		Map<String, Double> lonlat = Gcj022Gps.wgs2gcj(longitude, latitude);
//		double bdlonlat[] = Gcj022Bd09.bd09Encrypt(lonlat.get("lat"), lonlat.get("lon"));
//		return	 bdlonlat[0]+" "+bdlonlat[1];
//	}
//
//	public static String toBd09Position(CoordParam param, SpatialReference spRefence, double x, double y){
//		if ((spRefence == null)
//				&& (param == null ||param.getCoordinate().equals("") || param.getCoordinate().equals("0"))) {
//			return webM2Bd09Position(x,y);
//		} else {
//			if (spRefence.isAnyWebMercator()) {
//				return webM2Bd09Position(x,y);
//			} else if (spRefence.isWGS84()) {
//				return wgs2Bd09Position(x,y);
//			} else {
//				return local2Bd09Position(param,y,x);
//			}
//		}
//	}
//
//	public static final DemoInfo[] demos = {
//
//      /*  new DemoInfo(PanoramaDemoActivityMain.PID, R.string.demo_title_panorama,
//                     R.string.demo_desc_panorama1, PanoramaDemoActivityMain.class),*/
////        new DemoInfo(PanoramaDemoActivityMain.GEO, R.string.demo_title_panorama,
////        		R.string.demo_desc_panorama2, PanoramaDemoActivityMain.class),
//        /*  new DemoInfo(PanoramaDemoActivityMain.MERCATOR, R.string.demo_title_panorama,
//        		R.string.demo_desc_panorama3, PanoramaDemoActivityMain.class),
//        new DemoInfo(PanoramaDemoActivityMain.MARKER, R.string.demo_title_panorama,
//        		R.string.demo_desc_panorama4, PanoramaDemoActivityMain.class),
//        new DemoInfo(PanoramaDemoActivityMain.INDOOR, R.string.demo_title_panorama,
//        		R.string.demo_desc_panorama5, PanoramaDemoActivityMain.class)      */
//	};
//
//
//	public static class DemoInfo{
//		private final int title;
//		private final int desc;
//		public final int type;
//		public final Class<? extends Activity> demoClass;
//
//		public DemoInfo(int type, int title , int desc,Class<? extends Activity> demoClass) {
//			this.title = title;
//			this.desc  = desc;
//			this.type = type;
//			this.demoClass = demoClass;
//		}
//	}
//
//	public static void bdInit(Context context){
//
//		//百度授权初始化
//		if (mBMapManager == null) {
//	            mBMapManager = new BMapManager(context);
//        }
//
//        if (!mBMapManager.init(new MyGeneralListener())) {
//            Toast.makeText(context, "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
//        }
//	}
//
//
//	/**
//	 * 内部TTS播报状态回调接口
//	 */
//	private static BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {
//
//		@Override
//		public void playEnd() {
//			Log.i("TTS","TTS导航语音结束");
//		}
//
//		@Override
//		public void playStart() {
//			Log.i("TTS","TTS导航语音开始");
//		}
//	};
//
//	private static String getSdcardDir() {
//		if (Environment.getExternalStorageState().equalsIgnoreCase(
//				Environment.MEDIA_MOUNTED)) {
//			return Environment.getExternalStorageDirectory().toString();
//		}
//		return null;
//	}
//
////	private static NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
////		public void engineInitSuccess() {
////			boolean mIsEngineInitSuccess = true;
////		}
////
////		public void engineInitStart() {
////		}
////
////		public void engineInitFail() {
////		}
////	};
////
////    private BNKeyVerifyListener mKeyVerifyListener = new BNKeyVerifyListener() {
////
////		@Override
////		public void onVerifySucc() {
////			// TODO Auto-generated method stub
////			//Toast.makeText(DemoMainActivity.this, "key校验成功", Toast.LENGTH_LONG).show();
////		}
////
////		@Override
////		public void onVerifyFailed(int arg0, String arg1) {
////			// TODO Auto-generated method stub
////			//Toast.makeText(DemoMainActivity.this, "key校验失败", Toast.LENGTH_LONG).show();
////		}
////	};
//	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
//    public static class MyGeneralListener implements MKGeneralListener {
//
//        @Override
//        public void onGetPermissionState(int iError) {
//        	//非零值表示key验证未通过
//            if (iError != 0) {
//                //授权Key错误：
//               // Toast.makeText(AppContext.getInstance().getApplicationContext(),
//              //          "请在AndoridManifest.xml中输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
//            }
//            else{
//            	//Toast.makeText(AppContext.getInstance().getApplicationContext(),
//                //        "key认证成功", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
