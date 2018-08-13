package com.movementinsome.map.nearby;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.esri.android.map.GraphicsLayer;
import com.movementinsome.AppContext;
import com.movementinsome.map.view.MyMapView;

public class FacOnClickListener implements OnClickListener{

	private FacTypeObj factype;
	private Context context;
	private MyMapView mapView;
	private GraphicsLayer grapLayer;
	private NearByTools nearByTools;
	
	public FacOnClickListener(Context context,MyMapView mapView,GraphicsLayer grapLayer,FacTypeObj factype,NearByTools nearByTools){
		this.factype=factype;
		this.context=context;
		this.mapView = mapView;
		this.grapLayer = grapLayer;
		this.nearByTools=nearByTools;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String[] queryParams = { factype.getForeign(), "1=1" };
		float longitude=0;
		float latitude=0;
		if (AppContext.getInstance().getCurLocation()!=null){
			longitude=(float) AppContext.getInstance().getCurLocation().getLongitude();
			latitude=(float) AppContext.getInstance().getCurLocation().getLatitude();
		}
		AsyncQueryTask ayncQuery = new AsyncQueryTask(context,mapView,grapLayer,longitude,latitude,factype.getLayerIds(),nearByTools);
		ayncQuery.execute(queryParams);
		//Toast.makeText(context, factype.getForeign(), 4).show();
	}

}
