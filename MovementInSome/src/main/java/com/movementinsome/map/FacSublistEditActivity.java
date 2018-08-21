package com.movementinsome.map;

import android.os.Bundle;
import android.view.KeyEvent;

import com.esri.android.map.event.OnStatusChangedListener;
import com.movementinsome.AppContext;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.util.WebAccessTools.NET_STATE;
import com.movementinsome.map.facSublistEdit.MapEditFacSublist;
import com.movementinsome.map.view.MyMapView;

import java.util.List;

public class FacSublistEditActivity extends MapViewer {

	private MyMapView mMapView;
	private MapEditFacSublist mapEditFacSublist;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		mMapView = getMapView();
		mMapView.resetTouchListener();
		mMapView.getMapView().setOnStatusChangedListener(new OnStatusChangedListener() {

			@Override
			public void onStatusChanged(Object source, STATUS status) {
				// TODO Auto-generated method stub
				if (source == mMapView.getMapView() && status == STATUS.INITIALIZED) {
					String url = "";
					String queryUrl = "";
					Ftlayer ftlayer = null;
					List<Ftlayer> ftlayers = AppContext.getInstance().getMapParam().getFtlayers();
					
					for(int i=0;i<ftlayers.size();++i){
						if("水表组".equals(ftlayers.get(i).getName())){
							ftlayer = ftlayers.get(i);
							Mapservice mapservice = ftlayer.getMapservice();
							if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.NULL){
								queryUrl = mapservice.getForeign()+"/"+ftlayer.getLayerIds();
								url = (mapservice.getForeign()+"/"+ftlayer.getFeatureServerId()).replace("MapServer", "FeatureServer");
							}else if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.FOEIGN ){
								queryUrl = mapservice.getForeign()+"/"+ftlayer.getLayerIds();
								url = (mapservice.getForeign()+"/"+ftlayer.getFeatureServerId()).replace("MapServer", "FeatureServer");
							}else{
								queryUrl = mapservice.getLocal()+"/"+ftlayer.getLayerIds();
								url = (mapservice.getLocal()+"/"+ftlayer.getFeatureServerId()).replace("MapServer", "FeatureServer");
							}
						}
					}
					if(ftlayer!=null){
						mapEditFacSublist = new MapEditFacSublist(FacSublistEditActivity.this, mMapView, url,queryUrl, ftlayer);
					}
				}
				if(status == STATUS.INITIALIZED){
					
					mMapView.setFlgmapInitialized(true);
				}
			}
			
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
