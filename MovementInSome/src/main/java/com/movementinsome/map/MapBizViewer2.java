package com.movementinsome.map;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.movementinsome.AppContext;
import com.movementinsome.R.drawable;
import com.movementinsome.kernel.initial.model.MenuClassify;
import com.movementinsome.map.facedit.MapEditFac;
import com.movementinsome.map.utils.BufferQueryTools;
import com.movementinsome.map.utils.DrawGraphLoctionUtil;
import com.movementinsome.map.utils.DrawPointUtil;
import com.movementinsome.map.utils.IdentifyUtil;
import com.movementinsome.map.utils.LocScreenshotUtil;
import com.movementinsome.map.utils.MapMeterUtil;
import com.movementinsome.map.view.MyMapView;
import com.pop.android.common.util.ToastUtils;

import java.util.ArrayList;

public class MapBizViewer2 extends MapViewer {

	public final static int BIZ_MAP_OPERATE_FAC_EDIT=10000;

	private String queryTaskUrl = "http://gddst2013.no-ip.org:28399/arcgis/rest/services/szsw_new/MapServer/29";
	// private GraphicsLayer grapLayer;
	private MyMapView mMapView;
	private ProgressDialog progress;
	private BufferQueryTools bqTools;
	private int bizMapState;
	private int mapid;
	private String pipeBroAnalysisValue;
	private String bizPosition;
	private DrawPointUtil drowPointUtil;
	private MapMeterUtil mapMeterUtil;
	private IdentifyUtil identifyUtil;
	private DrawGraphLoctionUtil drawGraphLoctionUtil;
	private LocScreenshotUtil locScreenshotUtil;
	private String strGraph;
	// 截屏后存放的位置
	private String storePath;
	private String bizType;
	private Integer qty;
	private ArrayList<String> dataList;
	private LinearLayout mapOperateBro;
	private MapEditFac mapEditFac;

	private String MB_CONTAIN_CLZ="mapContain";
	private int WindowWidths;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		mMapView = getMapView();
		/*
		 * grapLayer = new GraphicsLayer(); SimpleRenderer sr = new
		 * SimpleRenderer( new SimpleFillSymbol(Color.RED));
		 * grapLayer.setRenderer(sr); mMapView.addLayer(grapLayer);
		 */

		bqTools = new BufferQueryTools(this, mMapView);

		bizMapState = getIntent().getIntExtra("type", 0);
		bizPosition = getIntent().getStringExtra("position");
		mapid = getIntent().getIntExtra("mapid", 0);
		
		strGraph= getIntent().getStringExtra("strGraph");
		storePath= getIntent().getStringExtra("storePath");
		bizType = getIntent().getStringExtra("bizType");
		pipeBroAnalysisValue = getIntent().getStringExtra("pipeBroAnalysisValue");
		qty = getIntent().getIntExtra("qty", 8);
		dataList = getIntent().getStringArrayListExtra("dataList");
		
		mMapView.resetTouchListener();
		
		mMapView.getMapView().setOnStatusChangedListener(new OnStatusChangedListener() {

			private static final long serialVersionUID = 1L;

			public void onStatusChanged(Object source, STATUS status) {
				if (source == mMapView.getMapView() && status == STATUS.INITIALIZED) {
					
					mMapView.setFlgmapInitialized(true);
					
					Point centerPt = null;

					if (AppContext.getInstance().getCurLocation() != null){
						centerPt = new Point(AppContext.getInstance().getCurLocation().getMapx(),AppContext.getInstance().getCurLocation().getMapy());
					}
					
					switch (bizMapState) {
					case 10000: 
						break;
					}
				}
			}
		});
		
		OperateFacDdit(MapBizViewer2.this, mMapView);
	}
	
	private void OperateFacDdit(MapBizViewer2 mapBizViewer2, MyMapView mMapView2) {
		// TODO Auto-generated method stub
		mapEditFac=new MapEditFac(mapBizViewer2, MapBizViewer2.this.mMapView);
		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        int WindowWidth = wm.getDefaultDisplay().getWidth();
        WindowWidths =WindowWidth/4;
		mMapView.getSetBtn().setVisibility(View.GONE);
		mapOperateBro = mMapView.getFooterView();
		mapOperateBro.setVisibility(View.VISIBLE);
	        Button btn1=new Button(mapBizViewer2);
	        Button btn2=new Button(mapBizViewer2);  
	        Button btn3=new Button(mapBizViewer2);
	        Button btn4=new Button(mapBizViewer2);
	        btn1.setText("增加");
	        btn1.setWidth(WindowWidths);
	        btn1.setBackgroundResource(drawable.value_compile_k);
	        btn2.setText("修改");
	        btn2.setWidth(WindowWidths);
	        btn2.setBackgroundResource(drawable.value_compile_k);
	        btn3.setText("删除");
	        btn3.setWidth(WindowWidths);
	        btn3.setBackgroundResource(drawable.value_compile_k);
	        btn4.setText("返回");
	        btn4.setWidth(WindowWidths);
	        btn4.setBackgroundResource(drawable.value_compile_k);
	        mapOperateBro.addView(btn1);  
	        mapOperateBro.addView(btn2);  
	        mapOperateBro.addView(btn3);
	        mapOperateBro.addView(btn4);
	        
		        btn1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mapEditFac.setType("add");
						mapEditFac.getMapEditFacListView().showFacListNameDialog("add");
						mapEditFac.clear();
					}
				});
	         
	        	btn2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mapEditFac.setType("edit");
						mapEditFac.getMapEditFacListView().showFacListNameDialog("edit");
						mapEditFac.clear();
					}
				});
	         
		        btn3.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mapEditFac.setType("delect");
						mapEditFac.getMapEditFacListView().showFacListNameDialog("delect");
						mapEditFac.clear();
					}
				});
		        
		        btn4.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		        
	
	}
	
	private void drawBound(){
		mMapView.getMap_search_layout().setVisibility(View.GONE);
		mMapView.getMapLinearDraw().setVisibility(View.VISIBLE);
		mapMeterUtil=new MapMeterUtil(mMapView, MapBizViewer2.this);
		mapMeterUtil.setDrawType(MapMeterUtil.POLY);
		mapMeterUtil.setShowCallout(false);
		mMapView.getDrawMapbtnAgainDraw().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapMeterUtil.clearAllLayer();
			}
		});
		mMapView.getDrawMapbtnRecall().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mapMeterUtil.recallPoint();
			}
		});
		mMapView.getDrawMapbtnConfirm().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mapMeterUtil.getListPoint().size()<3){
					ToastUtils.showToast(MapBizViewer2.this,"至少要绘制3个点");
				}else{
					AppContext.getInstance().setMapBound(mapMeterUtil.getGeo4WKT());
					AppContext.getInstance().getmHandle().sendEmptyMessage(1);
					finish();
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

	/**
	 * 
	 * Query Task executes asynchronously.
	 * 
	 */
	private class AsyncQueryTask extends AsyncTask<String, Void, FeatureSet> {

		protected void onPreExecute() {
			progress = ProgressDialog.show(MapBizViewer2.this, "", "正在获取图形位置");

		}

		/**
		 * First member in parameter array is the query URL; second member is
		 * the where clause.
		 */
		protected FeatureSet doInBackground(String... queryParams) {
			if (queryParams == null || queryParams.length <= 1)
				return null;

			String url = queryParams[0];
			Query query = new Query();
			String whereClause = queryParams[1];

			if (mMapView.getSpatialReference().getID() != 0)
				query.setOutSpatialReference(mMapView.getSpatialReference());
			query.setReturnGeometry(true);
			query.setWhere(whereClause);

			QueryTask qTask = new QueryTask(url);
			FeatureSet fs = null;

			try {
				fs = qTask.execute(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return fs;
			}
			return fs;

		}

		protected void onPostExecute(FeatureSet result) {
			if (result != null) {
				Graphic[] grs = result.getGraphics();

				if (grs.length > 0) {
					mMapView.setExtent(bqTools.buffer(grs[0].getGeometry(), 2,
							Color.RED));
				}

			}
			progress.dismiss();
		}
	}
	
	@Override
	public MenuClassify getMenu() {
		return AppContext.getInstance().getMenu(MB_CONTAIN_CLZ);
	}
}
