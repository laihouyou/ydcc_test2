package com.movementinsome.map;

import android.os.Bundle;

import com.movementinsome.R;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.map.view.MyMapView;

public class MoveMapViewer extends ContainActivity {
	String flag = this.getClass().getName();
	
	private MyMapView mMapView;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_map_activity);
		mMapView = (MyMapView)findViewById(R.id.myMapView);// 
		mMapView.setMapPopupWindow(this);
		//加载上一次地图范围
		mMapView.loadLastMapExtent();
	}

	public MyMapView getMapView(){
		return mMapView;
	}
	
	@Override
	protected void onDestroy() {
		//退出前保存当前地图的位置
		mMapView.saveCurMapExtent();
		mMapView.destroy();
		super.onDestroy();

	}

	@Override
	protected void onPause() {
		mMapView.pause();
		super.onPause();

	}

	@Override
	protected void onResume() {
		mMapView.unpause();
		super.onResume();
	}
}

