package com.movementinsome.map.utils;

import android.app.Activity;
import android.content.Context;
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
import com.movementinsome.map.view.MyMapView;

public class DrawPointUtil {

	private MyMapView map;
	private GraphicsLayer grapPayer;//点
	private Context context;
	
	public DrawPointUtil(MyMapView map,Context context){
		this.map=map;
		this.context=context;
		grapPayer = new GraphicsLayer();
		map.addLayer(grapPayer);
		MyTouchListener myTouchListener=new MyTouchListener(context,map.getMapView());
		map.setOnTouchListener(myTouchListener);
	}
	
	/**
	 * 通过X,Y坐标wf
	 * @param type
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
}
