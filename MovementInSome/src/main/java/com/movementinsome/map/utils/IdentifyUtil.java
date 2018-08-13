package com.movementinsome.map.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.MapParam;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.map.MapLoad;
import com.movementinsome.map.view.IdentifyData;
import com.movementinsome.map.view.IdentifyDialog;
import com.movementinsome.map.view.MyMapView;
import com.movementinsome.map.view.NewMagnifier;

import java.util.ArrayList;

public class IdentifyUtil {

	private int IDEN_TOLERANCE = 5;
	// 地图设置参数
	private MapParam mapParam;
	private Context context;
	private MyMapView mapView;
	private Point identifyMagnifierPoint = null; // 查询设施的点
	private MyTouchListener myTouchListener;
	private ProgressDialog progress;
	private IdentifyParameters params;
	private int queryCount = 0;
	// 查询结果
	private ArrayList<IdentifyData> identifyDatas = new ArrayList<IdentifyData>();

	public IdentifyUtil(MyMapView mapView, Context context) {
		this.mapView = mapView;
		this.context = context;
		mapParam = MapLoad.mapParam;
		myTouchListener = new MyTouchListener(this.context, mapView.getMapView());
		mapView.setOnTouchListener(myTouchListener);
	}

	private Handler mhandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Constant.MSG_IDEN_RETURN) {
				queryCount--;
				if (queryCount == 0) {
					progress.dismiss();
					if (identifyDatas.size() > 0)
						ShowResult();
				}

			}
		}

	};

	private void ShowResult() {
		com.movementinsome.map.view.IdentifyDialog identifyDialog = new IdentifyDialog(
				context, mapView, identifyDatas, this.mapParam, true);
		identifyDialog.show();
	}

	protected class MyTouchListener extends MapOnTouchListener {

		private NewMagnifier magnifier;

		private String type;
		private Context context;
		private MapView view;
		private boolean showmag = false;

		public MyTouchListener(Context context, MapView view) {
			super(context, view);
			this.context = context;
			this.view = view;
		}

		public void setType(String geometryType) {
			this.type = geometryType;
		}

		public String getType() {
			return this.type;
		}

		public boolean onSingleTap(MotionEvent e) {
			return true;
		}

		public void onLongPress(MotionEvent point) {
			magnify(point);
			showmag = true;

			// super.onLongPress(point);

		}

		public boolean onDragPointerMove(MotionEvent from, final MotionEvent to) {
			if (showmag) {
				magnify(to);
				return true;
			}
			return super.onDragPointerMove(from, to);
		}

		@Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			if (showmag) {
				if (magnifier != null) {
					magnifier.hide();
				}
				magnifier.postInvalidate();
				showmag = false;
				// redrawCache = true;
				float x = to.getX();
				float y = to.getY();
				// 查询点附近的设施
				identifyMagnifierPoint = view.toMapPoint(x, y);
				identifyData(x, y);

				return true;
			}
			return super.onDragPointerUp(from, to);
		}

		private void magnify(MotionEvent to) {

			if (magnifier == null) {
				magnifier = new NewMagnifier(context, view);
				view.addView(magnifier);
				magnifier.prepareDrawingCacheAt(to.getX(), to.getY());
			} else {
				magnifier.prepareDrawingCacheAt(to.getX(), to.getY());
			}
			// redrawCache = false;
		}

		/**
		 * 设施查询
		 */
		@SuppressWarnings({ "static-access" })
		private void identifyData(float x, float y) {
			// Identify查询设施参数设置
			identifyMagnifierPoint = view.toMapPoint(x, y);

			if (mapParam == null){
				return ;
			}
			//初始化计算器及存储变量
			queryCount = 0;
			
			identifyDatas = new ArrayList<IdentifyData>();
			//循环查询
			for (Mapservice mapservice : mapParam.getBizMap()) {
				//mapservice.getType() == LAYERTYPE.dynamic &&
				if (mapParam.getLayerIds(mapservice).length>0) {
					// set Identify Parameters
					params = new IdentifyParameters();
					params.setTolerance(IDEN_TOLERANCE);
					params.setDPI(96);
					params.setLayers(mapParam.getLayerIds(mapservice));
					params.setLayerMode(IdentifyParameters.ALL_LAYERS);
					Point identifyPoint = view.toMapPoint(x, y);
					params.setGeometry(identifyPoint);
					if (view.getSpatialReference().getID()!=0)
					  params.setSpatialReference(view.getSpatialReference());
					params.setMapHeight(view.getHeight());
					params.setMapWidth(view.getWidth());
					Envelope env = new Envelope();
					view.getExtent().queryEnvelope(env);
					params.setMapExtent(env);
					params.setReturnGeometry(true);
					

					MyIdentifyTask mTask = new MyIdentifyTask(identifyPoint,
							view, mapservice.getForeign());
					mTask.execute(params);
					queryCount++;
					
				}
			}
			if (queryCount>0){
				progress = ProgressDialog.show(this.context, "",
						"正在查询当中...");
			}
		}

		private class MyIdentifyTask extends
				AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {

			IdentifyTask mIdentifyTask;
			// Point mAnchor;
			MapView view;

			String identifyTaskUrl;

			private int serviceId;
			private Mapservice mapService;

			MyIdentifyTask(Point anchorPoint, MapView view,
					String identifyTaskUrl) {
				// mAnchor = anchorPoint;
				this.view = view;
				this.identifyTaskUrl = identifyTaskUrl;
				mapService = mapParam.findMapservice(identifyTaskUrl);
				if (mapService != null) {
					serviceId = mapService.getId();
				} else {
					serviceId = -1;
				}
			}

			@Override
			protected IdentifyResult[] doInBackground(
					IdentifyParameters... params) {
				IdentifyResult[] mResult = null;
				if (params != null && params.length > 0) {
					IdentifyParameters mParams = params[0];
					try {
						mResult = mIdentifyTask.execute(mParams);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						// 如果查询异常计算器减1
						// queryCount--;
						mhandler.sendEmptyMessage(Constant.MSG_IDEN_RETURN);
						e.printStackTrace();
					}

				}
				return mResult;
			}

			@Override
			protected void onPostExecute(IdentifyResult[] results) {
				// TODO Auto-generated method stub

				if (results != null && results.length > 0) {
					analysisIdentify(results, serviceId);
				} else {
					// 查询没有结果计算器减1
					// queryCount--;
					mhandler.sendEmptyMessage(Constant.MSG_IDEN_RETURN);
				}
			}

			@Override
			protected void onPreExecute() {
				try {
					mIdentifyTask = new IdentifyTask(identifyTaskUrl);
				} catch (Exception ex) {
					mhandler.sendEmptyMessage(Constant.MSG_IDEN_RETURN);
				}
			}

			// 查询结果初步处理
			private synchronized void analysisIdentify(
					IdentifyResult[] identifyAll, int serviceId) {

				//
				try {
					/*
					 * for(int r=0;r<identifyAll.length;r++){ if (identifyAll[r]
					 * == null){ continue; } IdentifyResult idenResult =
					 * identifyAll[r];
					 */
					for (IdentifyResult idenResult : identifyAll) {
						//
						if (identifyDatas.size() == 0) {
							Ftlayer ftLyr = mapParam.findFtlayer(serviceId,
									idenResult.getLayerId());

							IdentifyData identifyData = new IdentifyData();
							identifyData.setServiceId(serviceId);
							identifyData.setLayerId(idenResult.getLayerId());
							identifyData
									.setLayerName(idenResult.getLayerName());
							identifyData.setFtLayer(ftLyr);
							identifyData.getData().add(idenResult);
							identifyDatas.add(identifyData);
						} else {
							int i = 0;
							for (IdentifyData identifyData : identifyDatas) {
								i++;
								Ftlayer ftLyr = mapParam.findFtlayer(serviceId,
										idenResult.getLayerId());
								/*
								 * if (ftLyr == null){ break; }
								 */
								// if (identifyData.getLayerId() ==
								// idenResult.getLayerId()){
								if (identifyData.getFtLayer().getId() == ftLyr
										.getId()) {
									boolean isExist = false;
									for (IdentifyResult rlt : identifyData
											.getData()) {
										if (rlt.getAttributes()
												.get("OBJECTID")
												.toString()
												.equals(idenResult
														.getAttributes()
														.get("OBJECTID")
														.toString())) {
											// if
											// (!GeometryEngine.equals(rlt.getGeometry(),
											// idenResult.getGeometry(),
											// MyMapView.this.getSpatialReference())){
											// identifyData.getData().add(idenResult);
											isExist = true;
											break;
										}
									}
									if (!isExist) {
										identifyData.getData().add(idenResult);
										break;
									}
									/*
									 * if(!identifyData.getData().contains(
									 * idenResult))
									 * identifyData.getData().add(idenResult);
									 * break;
									 */
								} else {
									if (i == identifyDatas.size()) {
										IdentifyData identifyData1 = new IdentifyData();
										identifyData1.setServiceId(serviceId);
										identifyData1.setLayerId(idenResult
												.getLayerId());
										identifyData1.setLayerName(idenResult
												.getLayerName());
										identifyData1.getData().add(idenResult);
										identifyData1.setFtLayer(ftLyr);
										identifyDatas.add(identifyData1);
										break;
									}
								}
							}
						}
					}
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
				// 有结果计算器减1
				// queryCount--;
				mhandler.sendEmptyMessage(Constant.MSG_IDEN_RETURN);
			}
		}
	}
}
