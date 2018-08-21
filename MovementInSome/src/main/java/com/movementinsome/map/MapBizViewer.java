package com.movementinsome.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.map.offvalve.PipeBroAnalysisTools2;
import com.movementinsome.map.utils.BufferQueryTools;
import com.movementinsome.map.utils.DrawGraphLoctionUtil;
import com.movementinsome.map.utils.DrawPointUtil;
import com.movementinsome.map.utils.DrawPointUtil2;
import com.movementinsome.map.utils.IdentifyUtil;
import com.movementinsome.map.utils.LocScreenshotUtil;
import com.movementinsome.map.utils.MapAreaUtil;
import com.movementinsome.map.utils.MapMeterUtil;
import com.movementinsome.map.utils.MapUtil;
import com.movementinsome.map.view.MyMapView;
import com.pop.android.common.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class MapBizViewer extends MapViewer {

	public final static int BIZ_MAP_OPERATE_INS_LOCATE = 10000; // 设施定位
	public final static int BIZ_MAP_OPERATE_INS_ATTRIBUTE = 10001; // 获取设施属性信息
	public final static int BIZ_MAP_OPERATE_POSITION = 10002; // 获取地图位置
	public final static int BIZ_MAP_OPERATE_BOUND = 10003; // 获取范围
	public final static int BIZ_MAP_OPERATE_GPS_LOCATE = 10004; // gps定位
	public final static int BIZ_MAP_OPERATE_POINT_LOCATE = 10005; // 坐标定位
	public final static int BIZ_MAP_OPERATE_GRAPH_LOCATE = 10006; // 图形定位
	public final static int BIZ_MAP_OPERATE_SCREENSHOT_LOCATE = 10007; // 定位后截屏
	public final static int BIZ_MAP_OPERATE_PIPEBRO_ANALYSIS = 10008;// 关阀分析
	public final static int BIZ_MAP_OPERATE_AREA = 10009;// 测量面积

	private String queryTaskUrl = "http://gddst2013.no-ip.org:28399/arcgis/rest/services/szsw_new/MapServer/29";
	// private GraphicsLayer grapLayer;
	private MyMapView mMapView;
	private  List<InsCheckFacRoad> insCheckFacRoadList;
	private ProgressDialog progress;
	private BufferQueryTools bqTools;
	private int bizMapState=0;
	private int mapid;
	private String pipeBroAnalysisValue;
	private String bizPosition;
	private String partition;//存放分区信息
	private String variable;//存放页面跳转变量信息
	private DrawPointUtil drowPointUtil;
	private DrawPointUtil2 drowPointUtil2;
	private MapMeterUtil mapMeterUtil;
	private MapAreaUtil mapAreaUtil;
	private IdentifyUtil identifyUtil;
	private DrawGraphLoctionUtil drawGraphLoctionUtil;
	private LocScreenshotUtil locScreenshotUtil;
	private String strGraph;
	// 截屏后存放的位置
	private String storePath;
	private String bizType;
	private String tableNum;
	private Integer qty;
	private ArrayList<String> dataList;

	private String MB_CONTAIN_CLZ="mapContain";
	private int tube_analysis_PT;
	
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
		
		partition = getIntent().getStringExtra("partition");//获取分区信息
		tube_analysis_PT = getIntent().getIntExtra("tube_analysis_PT", 0);//获取爆管分析信息  （莆田专用）
		if (bizMapState==0&&tube_analysis_PT!=0) {
			bizMapState=tube_analysis_PT;
		}
		
		variable = getIntent().getStringExtra("variable");//获取页面跳转信息
		
		strGraph= getIntent().getStringExtra("strGraph");
		storePath= getIntent().getStringExtra("storePath");
		bizType = getIntent().getStringExtra("bizType");
		tableNum = getIntent().getStringExtra("tableNum");
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
					case 10000: // 定位
						String[] queryParams = { queryTaskUrl,
								"objectid=" + mapid };
						AsyncQueryTask ayncQuery = new AsyncQueryTask();
						ayncQuery.execute(queryParams);
						break;
					case 10001:// 获取设施属性信息
						if (centerPt != null)
							//mMapView.centerAt(centerPt, true);
							// mMapView.zoomToResolution(centerPt, 1);
						identifyUtil=new IdentifyUtil(mMapView, MapBizViewer.this);
						break;
					case 10002:// 获取地图位置

						drowPointUtil=new DrawPointUtil(mMapView, MapBizViewer.this);
						if (bizPosition!=null&&!"".equals(bizPosition)){
							String[] xy = bizPosition.split("\\ ");
							try{
								MapUtil.locateByPoint(mMapView,Double.valueOf(xy[0]),Double.valueOf(xy[1]));
								drowPointUtil.drawFlag(Double.valueOf(xy[0]),Double.valueOf(xy[1]));
							}catch (NumberFormatException e) {
								// TODO: handle exception
							}
						}
						break;
					case 10003:// 获取范围
						drawBound();
						break;
					case 10004:
						break;
					case 10005:
						drowPointUtil2=new DrawPointUtil2(mMapView, MapBizViewer.this,partition);
							if (bizPosition!=null&&!"".equals(bizPosition)){
								String[] xy = bizPosition.split("\\ ");
								try{
									MapUtil.locateByPoint(mMapView,Double.valueOf(xy[0]),Double.valueOf(xy[1]));
									drowPointUtil2.drawFlag(Double.valueOf(xy[0]),Double.valueOf(xy[1]));
								}catch (NumberFormatException e) {
									// TODO: handle exception
								}
							}
						
						break;
					case 10006:
						drawGraphLoctionUtil=new DrawGraphLoctionUtil(mMapView,  MapBizViewer.this);
						drawGraphLoctionUtil.drawGraphLoction(strGraph);
						mMapView.getMap_search_layout().setVisibility(View.GONE);
						
						break;
					case 10007:
						locScreenshotUtil = new LocScreenshotUtil(MapBizViewer.this, mMapView, storePath,bizType,qty,dataList);
						locScreenshotUtil.ShowLocMap();
						break;
					case 10008:
						new PipeBroAnalysisTools2(MapBizViewer.this, mMapView , pipeBroAnalysisValue);
						break;
					case 10009:
						if(mapAreaUtil==null){
							mapAreaUtil=new MapAreaUtil(mMapView, MapBizViewer.this ,MapBizViewer.this);
						}else{
							mapAreaUtil.clearAllLayer();
						}
						mapAreaUtil.setDrawType(MapAreaUtil.POLY);
						break;
					case 10010:
						drawGraphLoctionUtil=new DrawGraphLoctionUtil(mMapView,  MapBizViewer.this);
						mMapView.setOnClickListener(null);
						drawGraphLoctionUtil.drawGraphLoctionBurst(strGraph);
						mMapView.getMap_search_layout().setVisibility(View.GONE);
					case 10011://莆田专用爆管分析
						new PipeBroAnalysisTools2(MapBizViewer.this, mMapView , "");
					}
				}
			}
		});
	}
	private void drawBound(){
		mMapView.getMap_search_layout().setVisibility(View.GONE);
		mMapView.getMapLinearDraw().setVisibility(View.VISIBLE);
		mapMeterUtil=new MapMeterUtil(mMapView, MapBizViewer.this);
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
					ToastUtils.showToast(MapBizViewer.this,"至少要绘制3个点");
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
			progress = ProgressDialog.show(MapBizViewer.this, "", "正在获取图形位置");

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
	public void showList(){
		LinearLayout v = (LinearLayout) View.inflate(MapBizViewer.this,
				R.layout.pipe_bro_analysis_list, null);
		RelativeLayout pipe_bro_list_ll = (RelativeLayout) v.findViewById(R.id.pipe_bro_list_ll);
		ListView pipe_bro_list =(ListView) v.findViewById(R.id.pipe_bro_list);
		Button pipe_bro_list_pack_up = (Button) v.findViewById(R.id.pipe_bro_list_pack_up);
		Button pipe_bro_list_project = (Button) v.findViewById(R.id.pipe_bro_list_project);
		TextView pipe_bro_enlarge_count = (TextView) v.findViewById(R.id.pipe_bro_enlarge_count);
		Button pipe_bro_enlarge_btn =(Button) v.findViewById(R.id.pipe_bro_enlarge_btn);
		pipe_bro_list_project.setVisibility(View.GONE);
		pipe_bro_enlarge_btn.setText("提交设施");
		
		pipe_bro_enlarge_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				List<String > targetList = new ArrayList<String>();
		        for (int i = 0; i < insCheckFacRoadList.size(); i++) {
		            if (insCheckFacRoadList.get(i).isSelected()) {
		                targetList.add(String.valueOf(insCheckFacRoadList.get(i).getId()));
		            }
		        }
		        String ids = targetList.toString();
		        String ids1 = ids.replace("[", "");
		        String ids2 = ids1.replace("]", "");
//		        RegionAnalyzeTask2 regionAnalyzeTask2 = new RegionAnalyzeTask2(MapBizViewer.this,MapBizViewer.this,ids2);
//		        regionAnalyzeTask2.execute("");
			}
		});
		pipe_bro_list_project.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		
		pipe_bro_list.setAdapter(new BgfxCursorAdapter(MapBizViewer.this));
		
		Button pipe_bro_list_close = (Button) v
				.findViewById(R.id.pipe_bro_list_close);
		pipe_bro_list_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mMapView.getHeaderContainer().removeAllViews();
			}
		});
		
		pipe_bro_list_pack_up.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
			}
		});
		mMapView.getHeaderContainer().addView(v);
	}
	
	private class BgfxCursorAdapter extends BaseAdapter{
		private Context context;

		public BgfxCursorAdapter(Context context){
			this.context=context;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return insCheckFacRoadList.size();
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return insCheckFacRoadList.get(arg0);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 final Viewholder viewholder;
		        if (convertView == null) {
		            convertView = getLayoutInflater().inflate(R.layout.regionanalyze_item, parent, false);
		            viewholder = new Viewholder();
		            viewholder.regionChecked = (CheckedTextView) convertView.findViewById(R.id.regionChecked);

		            convertView.setTag(viewholder);
		        } else {
		            viewholder = (Viewholder) convertView.getTag();

		        }
		        final InsCheckFacRoad entity = insCheckFacRoadList.get(position);
		        //显示内容
		        viewholder.regionChecked.setTextColor(Color.BLUE);
		        viewholder.regionChecked.setText("设施编号:"+insCheckFacRoadList.get(position).getFacNum()
		        		+"\n设施类型:"+insCheckFacRoadList.get(position).getFacType()
		        		+"\n路段名称:"+insCheckFacRoadList.get(position).getRoadName());

		        // 根据isSelected来设置checkbox的选中状况
		        viewholder.regionChecked.setOnClickListener(new OnClickListener() {

		            @Override
		            public void onClick(View arg0) {
		                //如果是选择了，就反选
		                if (entity.isSelected()) {
		                    viewholder.regionChecked.setSelected(false);
		                    viewholder.regionChecked.setChecked(false);

		                    //同时改变list里面的数据
		                    insCheckFacRoadList.get(position).setSelected(false);
		                } else {
		                    viewholder.regionChecked.setSelected(true);
		                    viewholder.regionChecked.setChecked(true);
		                    //同时改变list里面的数据
		                    insCheckFacRoadList.get(position).setSelected(true);
		                }
		                
		                //更新
		                notifyDataSetChanged();
		            }
		        });

		        //这个地方一定要这样写，if  else  不然就上下拉的时候，会错误显示
		        if (entity.isSelected()) {
		            //如果是勾，显示勾
		            //这里的两个方法，我不知道 那个可以，忘记了。
		            viewholder.regionChecked.setSelected(true);
		            viewholder.regionChecked.setChecked(true);
		        } else {
		            viewholder.regionChecked.setSelected(false);
		            viewholder.regionChecked.setChecked(false);

		        }

		        return convertView;

		}
	}
	class Viewholder {
	        CheckedTextView regionChecked;
	    }
	 
	public List<InsCheckFacRoad> getAllItems() {
	        return this.insCheckFacRoadList;
	    }
	
//	@Override
//	public MenuClassify getMenu() {
//		return AppContext.getInstance().getMenu(MB_CONTAIN_CLZ);
//	}
	public List<InsCheckFacRoad> getInsCheckFacRoadList() {
		return insCheckFacRoadList;
	}
	public void setInsCheckFacRoadList(List<InsCheckFacRoad> insCheckFacRoadList) {
		this.insCheckFacRoadList = insCheckFacRoadList;
	}
	
	
}
