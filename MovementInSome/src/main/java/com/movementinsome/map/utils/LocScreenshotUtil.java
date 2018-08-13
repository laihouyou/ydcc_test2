package com.movementinsome.map.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.easyform.util.CameraLogUtil;
import com.movementinsome.easyform.widgets.AddMapScreenshotView;
import com.movementinsome.map.view.MyMapView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LocScreenshotUtil {

	private MyMapView map;
	private GraphicsLayer grapPayer;//点
	private Context context;
	private String storePath;
	private String bizType;
	public static AddMapScreenshotView addMapScreenshotView;
	private Integer qty;
	private List<String> tDataList;
	
	public LocScreenshotUtil(Context context,MyMapView map,String storePath,String bizType,Integer qty,List<String> tDataList){
		this.context= context;
		this.map=map;
		this.storePath=storePath;
		this.bizType=bizType;
		this.qty = qty;
		this.tDataList = tDataList;
		grapPayer = new GraphicsLayer();
		map.addLayer(grapPayer);
		MyTouchListener myTouchListener=new MyTouchListener(context,map.getMapView());
		map.setOnTouchListener(myTouchListener);
	}
	public void ShowLocMap(){
		if (AppContext.getInstance().getCurLocation()!=null){
			Double mapX=AppContext.getInstance().getCurLocation().getMapx();
			Double mapY=AppContext.getInstance().getCurLocation().getMapy();
			Point mpoint = new Point();
			mpoint.setX(mapX);
			mpoint.setY(mapY);
			TextView tvPoint = new TextView(context);
			tvPoint.setText("已定位当前位置\n点击截屏");
			tvPoint.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					map.getCallout().hide();
					Bitmap bitmap=MapUtil.getViewBitmap(map.getMapView());
					Date date=new Date();
			        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMdd_hhmmss");
			        String timeString=dateformat1.format(date);
			      
			        String filename=storePath+timeString+".jpg";
					mapviewshot(bitmap,filename);
					bitmap.recycle();
					tDataList.add(filename);
					//处理拍照记录
			   		 CameraLogUtil.getInstance().writeLog(bizType,storePath,timeString+".jpg"); 
					addMapScreenshotView.updateUI(tDataList);
					((Activity)context).finish();
				}
			});
			map.zoomToScale(mpoint, 300d);//mMapView.getScale()
			grapPayer.addGraphic(new Graphic(mpoint,
					new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.map_inlocation))));
			map.getCallout().show(mpoint, tvPoint);
			
		}
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
			tvPoint.setText("已定位当前位置\n点击截屏");
			tvPoint.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					map.getCallout().hide();
					Bitmap bitmap=MapUtil.getViewBitmap(map.getMapView());
					Date date=new Date();
			        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMdd_hhmmss");
			        String timeString=dateformat1.format(date);
			      
			        String filename=storePath+timeString+".jpg";
					mapviewshot(bitmap,filename);
					bitmap.recycle();
					tDataList.add(filename);
					//处理拍照记录
			   		 CameraLogUtil.getInstance().writeLog(bizType,storePath,timeString+".jpg"); 
					addMapScreenshotView.updateUI(tDataList);
					((Activity)context).finish();
				}
			});
			Graphic graphic = new Graphic(mpoint, new PictureMarkerSymbol(context.getResources().getDrawable(R.drawable.map_inlocation)));
			grapPayer.addGraphic(graphic);
			map.zoomToScale(mpoint, 300d);
			map.getCallout().show(mpoint, tvPoint);
			return super.onSingleTap(point);
		}
	
	}
	//地图截屏
	public void mapviewshot(Bitmap bitmap,String filename) {
        File file_2=new File(storePath);
        if (!file_2.exists()){
            file_2.mkdirs();
        }
        //filename=getfilepath(filename);//判断是否有同一秒内的截图，有就改名字
        File file=new File(filename);    
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
