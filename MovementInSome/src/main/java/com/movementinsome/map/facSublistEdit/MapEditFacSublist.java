package com.movementinsome.map.facSublistEdit;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Toast;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.core.geometry.Point;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.map.facSublistEdit.asynctask.FacSublistQueryTask;
import com.movementinsome.map.facedit.vo.FacAttribute;
import com.movementinsome.map.view.MyMapView;
import com.movementinsome.map.view.NewMagnifier;

public class MapEditFacSublist {

	private MyMapView map;
	private Context context;
	private MyTouchListener myTouchListener;
	private ArcGISFeatureLayer fl;
	private Ftlayer ftlayer;
	private FacAttribute facAttribute;
	private String queryUrl;
	
	public MapEditFacSublist(Context context, MyMapView map,String url,String queryUrl,Ftlayer ftlayer){
		this.context = context;
		this.map = map;
		this.ftlayer = ftlayer;
		this.queryUrl = queryUrl;
		
		facAttribute = new FacAttribute();
		myTouchListener = new MyTouchListener(context, map.getMapView());
		map.setOnTouchListener(myTouchListener);
		fl= new ArcGISFeatureLayer(url, ArcGISFeatureLayer.MODE.ONDEMAND);
		map.addLayer(fl);
		facAttribute.setFeaturelayer(fl);
		facAttribute.setQueryUrl(queryUrl);
	}
	private boolean showmag = false;
	class MyTouchListener extends MapOnTouchListener {

		private NewMagnifier magnifier;
		public MyTouchListener(Context context, MapView view) {
			super(context, view);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onLongPress(MotionEvent point) {
			// TODO Auto-generated method stub
			super.onLongPress(point);
			if(!map.isFlgmapInitialized()){
				Toast.makeText(context, "地图尚未加载完……", Toast.LENGTH_LONG).show();
			}else{
				magnify(point);
				showmag = true;
			}
		}
		@Override
		public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
			// TODO Auto-generated method stub
			if (showmag) {
				magnify(to);
				return true;
			}
			return super.onDragPointerMove(from, to);
		}
		@Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			// TODO Auto-generated method stub
			if (showmag ) {
				if (magnifier != null) {
					magnifier.hide();
				}
				magnifier.postInvalidate();
				showmag = false;
				float x = to.getX();
				float y = to.getY();
				// 查询点附近的设施
				Point editPoint = map.getMapView().toMapPoint(x, y);
				facAttribute.setEditPoint(editPoint);
				FacSublistQueryTask facSublistQueryTask = new FacSublistQueryTask(context, map, facAttribute, ftlayer);
				facSublistQueryTask.execute("*");
				return true;
			}
			return super.onDragPointerUp(from, to);
		}
		
		private void magnify(MotionEvent to) {

			if (magnifier == null) {
				magnifier = new NewMagnifier(context, map.getMapView());
				map.addView(magnifier);
				magnifier.prepareDrawingCacheAt(to.getX(), to.getY());
			} else {
				magnifier.prepareDrawingCacheAt(to.getX(), to.getY());
			}
		}
		
	}
	
}
