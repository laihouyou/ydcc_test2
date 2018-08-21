package com.movementinsome.map.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.map.view.MyMapView;

import java.util.HashMap;

public class DrawPointUtil2  {

	private MyMapView map;
	private GraphicsLayer grapPayer;//点
	private Context context;
	private String partition;//分区信息
//	GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private String address;//地图点击点的文字地址
	 //创建lcoationManager对象  
    private LocationManager manager;  
    double lat;
    double lng;
	
	public DrawPointUtil2(MyMapView map,Context context,String partition){
		this.map=map;
		this.context=context;
		this.partition=partition;
		grapPayer = new GraphicsLayer();
		map.addLayer(grapPayer);
		MyTouchListener myTouchListener=new MyTouchListener(context,map.getMapView());
		map.setOnTouchListener(myTouchListener);
		
		// 初始化搜索模块，注册事件监听  
        /** 
         * public static GeoCoder newInstance() 
         * 新建地理编码查询 
         * @return 地理编码查询对象 
         * */  
//        mSearch = GeoCoder.newInstance();
        /** 
         * public void setOnGetGeoCodeResultListener(OnGetGeoCoderResultListener listener) 
         * 设置查询结果监听者 
         * @param listener - 监听者 
         * 
         * 需要实现OnGetGeoCoderResultListener的 
         * onGetGeoCodeResult(GeoCodeResult result)和onGetReverseGeoCodeResult(ReverseGeoCodeResult result) 
         * 方法 
         * */  
//        mSearch.setOnGetGeoCodeResultListener(this);
        
        //获取系统的服务，  
        manager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);  
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  
        
        if (location != null) {    
             lat = location.getLatitude();     
             lng = location.getLongitude();
        }
	}
	
	/**
	 * 通过X,Y坐标wf
	 * @param x
	 * @param y
	 */
	public void drawFlag(Double x,Double y){
		map.getCallout().hide();
		grapPayer.removeAll();
		
		final Point mpoint = new Point();
		mpoint.setX(x);
		mpoint.setY(y);
		TextView tvPoint = new TextView(context);
		tvPoint.setText("最新位置\n点击确认");
		tvPoint.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				map.getCallout().hide();
				if(mpoint!=null){
					AppContext.getInstance().setMapPosition(mpoint.getX()+" "+mpoint.getY());
				}
				AppContext.getInstance().getmHandle().sendEmptyMessage(1);
				exit();
				((Activity)context).finish();
				Intent newFormInfo = new Intent(context, RunForm.class);
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("coordinate", mpoint.getX()+" "+mpoint.getY());
				params.put("partition",partition);
				newFormInfo.putExtra("iParams", params);
				newFormInfo.putExtra("template", "ins_blast_leakage.xml");
				context.startActivity(newFormInfo);
			}
		});
		Graphic graphic = new Graphic(mpoint, new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.dot0)));
		grapPayer.addGraphic(graphic);
		map.getCallout().show(mpoint, tvPoint);
	}
	
	class MyTouchListener extends MapOnTouchListener {

		public MyTouchListener(Context context, MapView view) {
			super(context, view);
			// TODO Auto-generated constructor stub
			
		}
		@Override
		public boolean onSingleTap(MotionEvent point) {
			// TODO Auto-generated method stub
			map.getCallout().hide();
			grapPayer.removeAll();
			
			final Point mpoint = map.toMapPoint(point.getX(),point.getY());
			TextView tvPoint = new TextView(context);
			tvPoint.setText("最新位置\n点击确认");
			
//			String gps=BaiduAppProxy.local2Bd09Position(AppContext.getInstance().getCoordTransform(),mpoint.getY(), mpoint.getX());
//			 String a[] = gps.split(" ");
//			String mGpslat=a[0];
//			String mGpslon=a[1];
//			 LatLng ptCenter = new LatLng((Float.valueOf(mGpslon)),
//                    (Float.valueOf(mGpslat)));
//			 /**
//			  * public boolean reverseGeoCode(ReverseGeoCodeOption option)
//			  * 发起反地理编码请求(经纬度->地址信息)
//			  * @param option - 请求参数
//			  * @return 成功发起检索返回true,失败返回false
//			  *
//			  * public ReverseGeoCodeOption location(LatLng location)
//			  * 设置反地理编码位置坐标
//			  * @param location - 位置坐标
//			  * @return 该反地理编码请求参数对象
//			  * */
//			 mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));
			
			tvPoint.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					map.getCallout().hide();
					if(mpoint!=null){
						AppContext.getInstance().setMapPosition(mpoint.getX()+""+mpoint.getY());
					}
					AppContext.getInstance().getmHandle().sendEmptyMessage(1);
					exit();
					((Activity)context).finish();
					Intent newFormInfo = new Intent(context, RunForm.class);
					HashMap<String, String> params = new HashMap<String, String>();
					
         
					params.put("gpscoordinates", lng+" "+lat);
         			params.put("address", address);//地图点击点的文字地址
					params.put("coordinate", "Point("+mpoint.getX()+" "+mpoint.getY()+")");
					params.put("partition",partition);//分区信息
					newFormInfo.putExtra("iParams", params);
					newFormInfo.putExtra("template", "ins_blast_leakage.xml");
					context.startActivity(newFormInfo);
				}
			});
			Graphic graphic = new Graphic(mpoint, new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.dot0)));
			grapPayer.addGraphic(graphic);
			map.getCallout().show(mpoint, tvPoint);
			return super.onSingleTap(point);
		}
	
	}
	public void exit() {
		map.removeLayer(grapPayer);
		map.resetTouchListener();
	}
	 //////////////////OnGetGeoCoderResultListener//////////////////  
//    /**
//     * void onGetGeoCodeResult(GeoCodeResult result)
//     * 地理编码查询结果回调函数
//     * @param result - 地理编码查询结果
//     * */
//	@Override
//	public void onGetGeoCodeResult(GeoCodeResult arg0) {
//		// TODO 自动生成的方法存根
//
//	}
//	 /**
//     * void onGetReverseGeoCodeResult(ReverseGeoCodeResult result)
//     * 反地理编码查询结果回调函数
//     * @param result - 反地理编码查询结果
//     * */
//	@Override
//	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
//		// TODO 自动生成的方法存根
//		  if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
////	            Toast.makeText(GeoCoderDemo.this, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
//	            return;
//	        }
//	        /**
//	         * ReverseGeoCodeResult:反 Geo Code 结果
//	         * */
//		  if (result != null && result.error == SearchResult.ERRORNO.NO_ERROR) {
//
//              //得到位置
//              address = result.getAddress();
//          }
//	}
}
