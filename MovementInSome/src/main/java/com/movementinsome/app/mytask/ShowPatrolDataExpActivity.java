package com.movementinsome.app.mytask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.dataop.DataOperaterFactory;
import com.movementinsome.app.dataop.IDataOperater;
import com.movementinsome.app.dataop.IDataOperater.DataType;
import com.movementinsome.app.mytask.adapter.PlanWorkAdapter;
import com.movementinsome.app.pub.asynctask.NotPatrolTask;
import com.movementinsome.app.pub.asynctask.RegionAnalyzeTask2;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.HistoryTrajectoryVO;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.initial.model.UserRight;
import com.movementinsome.kernel.util.MyDateTools;
import com.movementinsome.map.utils.DrawGraphLoctionUtil;
import com.movementinsome.map.utils.MapMeterScope;
import com.movementinsome.map.utils.MapMeterUtil;
import com.movementinsome.map.view.MyMapView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ShowPatrolDataExpActivity extends FullActivity implements
		OnClickListener, OnInitListener {

	private ImageButton show_patrol_data_back;// 返回
	private MapMeterScope mapMeterScope;// 测量工具
	private MapMeterUtil mapMeterUtil;
	private TextView show_yx_title;// 已巡
	private TextView show_wx_title;// 未巡
	private TextView show_dx_title;// 待巡
	private TextView show_qdd_title;// 簽到點
	private TextView show_search_title;// 搜索
	private TextView show_nearby_title;// 附近列表
	
	private LinearLayout show_search_ll;// 搜索容器
	private EditText show_search_num;// 搜索输入
	private Button show_search_btn;// 搜索按钮
	private Button show_search_code;// 二维码

	private final String TYPE_YX = "type_yx";// 已巡
	private final String TYPE_WX = "type_wx";// 未巡
	private final String TYPE_DX = "type_dx";// 待巡
	private final String TYPE_QDD = "type_qdd";// 簽到點
	private final String TYPE_SEARCH = "TYPE_SEARCH";// 搜索
	private final String TYPE_NEARBY = "TYPE_NEARBY";// 附近
	private String type = TYPE_WX;
	private int numer;

	private ListView show_patrol_data_wx;// 巡查列表

	private String guid;
	private Button show_patrol_data_write;// 填单
	private int lastVisibleIndex;

	private List<Module> lstModule;// 所有表单配置
	private List<Module> taskModule;// 当前任务表单配置
	private List<InsCheckFacRoad> insCheckFacRoadList;
	private List<InsCheckFacRoad> selectRoadList;
	private InsTablePushTaskVo insTablePushTaskVo;  //当前巡查任务
	private PlanWorkAdapter planWorkAdapter; //详细内容显示适配器
	private BgfxCursorAdapter bgfxCursorAdapter;
	
	private InsPatrolOnsiteRecordExtVO insPatrolOnsiteRecordVO = null; // 当前路段
	//private InsPatrolOnsiteRecordExtVO preInsPatrolOnsiteRecordVO = null;// 上一巡查路段

	private LinearLayout show_work_message_ly;// 当前巡查容器
	private TextView show_work_content;// 当前巡查
	private TextView show_work_message;// 当前巡查
	private boolean isShowMessage;

	private RelativeLayout show_qdd_msg_rl;// 当前签到点容器
	private TextView show_qdd_msg;// 签到提示
	private Button show_work_arrive;// 签到'
	private Button show_work_im;// 工地填单
	
	private LinearLayout show_work_op_ll;// 暂停巡检容器
	private LinearLayout show_work_pause_ll_lyx;
	private Button show_work_op;// 暂停巡检按钮
	private TextView show_tv_pause;
	private TextView show_work_op_tv;// 
	private TextView show_task_ex_title;// 标题
	private ProgressBar pg;
	
	private LinearLayout patrolListModel;//列表模式容器
	private LinearLayout patrolMapModel; //地图模式容器
	private View pgview;
	private int MaxDateNum;
	private Button data_model;
	private boolean isShowMap;// 是否显示地图
	private boolean isShowSearch;
	private boolean isShowNearby;
	private RelativeLayout troubleshoot_btn_ll;
	private Button troubleshoot_shouqi_btn;
	private List<InsCheckFacRoad> insCheckFacRoadLoad;

	private MyMapView insMapView;

	private TTsMemo roadTTsMemo = new TTsMemo();
	private TTsMemo chkPntTTsMemo = new TTsMemo();
	
	private IDataOperater dataOp,chkDataOp;
	
	private int dspModel = 0;//0列表模式,1地图模式
	
	private boolean isFirst = false;//第一次绘制所有路段
	private DrawGraphLoctionUtil drawGraphLoctionUtil;
	
	private int CODE_RESULT_CODE = 100027;
	
	private Timer timer;// 定时器
	private boolean timer_start= false; //巡检示意是否已经启动，测试有可能启动失败，需要再次启协。

	private Handler sHandler;
	private Handler tHandler;
	private boolean isShowReport;
	private boolean isShowHadDoDataList;
	private String title;
	private String hadDoDataListName;
	private String text;
	
	private boolean isPause;
	private boolean isWriteTable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_patrol_data_map_activity);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		timer = new Timer();
		
		init();
		
		lstModule = AppContext.getInstance().getModuleData();
		taskModule = new ArrayList<Module>();
		for(Module module : lstModule){
			
			if(Constant.HID_DENRPT.equals(module.getId())
					||Constant.BUIDERRPT_ID.equals(module.getId())
					||Constant.GISCORRECT_ID.equals(module.getId())
					||Constant.BIZ_DEMOLITIONRPT.equals(module.getId())
					||"biz.giscorrect2".equals(module.getId())
					||"biz.hidden_trouble_form_zs".equals(module.getId())
					||"biz.ins_nearby_building_gs".equals(module.getId())){
				taskModule.add(module);
			}
		}
		
		guid = UUID.randomUUID().toString();
		insTablePushTaskVo = (InsTablePushTaskVo) getIntent()
				.getSerializableExtra("insTablePushTaskVo");
		dspModel = getIntent().getIntExtra("dspModel", 0);
		
		//初始化本次待巡检路段信息
		dataOp = DataOperaterFactory.createDataOp(this, insTablePushTaskVo, guid,lstModule);
		if (dataOp.initial(timeHamdler)==0){
			show_work_message.setText("温情提示：本次无需要巡查的区域！");
		}
		
		chkDataOp = DataOperaterFactory.createChkPntDataOp(this, insTablePushTaskVo, guid,lstModule);
		chkDataOp.initial(timeHamdler);

        //签到点与其它点类型的操作有点冲突
		if (dataOp.dataType() == DataType.Pl){
			show_work_arrive.setVisibility(View.VISIBLE);
			show_work_im.setVisibility(View.GONE);
			show_qdd_title.setVisibility(View.VISIBLE);
		}
		
		if(Constant.PLAN_PAICHA_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
			data_model.setText("列表");
			dspModel=1;
			patrolListModel.setVisibility(View.GONE);
			patrolMapModel.setVisibility(View.VISIBLE);
//			if (isFirst){
				List<InsPatrolAreaData> insPatrolAreaDataList = new ArrayList<InsPatrolAreaData>();
				List<InsCheckFacRoad> result = new ArrayList<InsCheckFacRoad>();
				try {
					Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(InsPatrolAreaData.class);
					insPatrolAreaDataList = insPatrolAreaDataDao.queryForEq(
							"workTaskNum", insTablePushTaskVo.getTaskNum());
					Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(InsCheckFacRoad.class);
					
					List<InsCheckFacRoad> insPatrolAreaDataList2 = insCheckFacRoadDao.queryForEq("workTaskNum", insTablePushTaskVo.getTaskNum());
					for (int i = 0; i < insPatrolAreaDataList2.size(); i++) {
						if(insPatrolAreaDataList2.get(i).getState()==null||insPatrolAreaDataList2.get(i).getState()==0||insPatrolAreaDataList2.get(i).getState()==1){
							result.add(insPatrolAreaDataList2.get(i));
						}
					}
					if(result.size()>0){
						showList();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
//			}
			
			show_yx_title.setVisibility(View.VISIBLE);
			show_qdd_title.setVisibility(View.GONE);
			show_nearby_title.setVisibility(View.GONE);
			show_dx_title.setVisibility(View.VISIBLE);
			type=TYPE_DX;
		}
		
		//初始巡查列表内容
		updateList(type);
		
		tHandler = new Handler();
		
		sHandler= new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					final List<Map<String, Object>> dataList;
					dataList = new ArrayList<Map<String, Object>>();
					if(insCheckFacRoadList!=null){
						for(InsCheckFacRoad insCheckFacRoad : insCheckFacRoadList){
							Map<String, Object> d = new HashMap<String, Object>();
							d.put("InsCheckFacRoad", insCheckFacRoad);
							dataList.add(d);
						}
					}
					show_search_ll.setVisibility(View.GONE);
					show_wx_title.setBackgroundResource(R.drawable.tv_green_bg);
					show_yx_title.setBackgroundResource(0);
					show_qdd_title.setBackgroundResource(0);
					show_search_title.setBackgroundResource(0);
					show_nearby_title.setBackgroundResource(0);
					show_dx_title.setBackgroundResource(0);
					
					show_dx_title.setTextColor(Color.parseColor("#737373"));
					show_wx_title.setTextColor(Color.parseColor("#3a5fcd"));
					show_yx_title.setTextColor(Color.parseColor("#737373"));
					show_qdd_title.setTextColor(Color.parseColor("#737373"));
					show_search_title.setTextColor(Color.parseColor("#737373"));
					show_nearby_title.setTextColor(Color.parseColor("#737373"));
					
					type = TYPE_WX;
					show_patrol_data_wx.addFooterView(pgview);
					
					planWorkAdapter = new PlanWorkAdapter(ShowPatrolDataExpActivity.this, dataList, sHandler, insTablePushTaskVo);
					show_patrol_data_wx.setAdapter(planWorkAdapter);
					show_patrol_data_wx.setOnScrollListener(new OnScrollListener() {

						@Override
						public void onScroll(AbsListView view, int firstVisibleItem,
								int visibleItemCount, int totalItemCount) {
							// TODO Auto-generated method stub
							lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

							// 所有的条目已经和最大条数相等，则移除底部的View
							if (totalItemCount == MaxDateNum + 1) {
								show_patrol_data_wx.removeFooterView(ShowPatrolDataExpActivity.this.pgview);
								Toast.makeText(ShowPatrolDataExpActivity.this, "数据全部加载完成，没有更多数据！",
										Toast.LENGTH_LONG).show();
							}
						}

						@Override
						public void onScrollStateChanged(AbsListView view,
								int scrollState) {
							// TODO Auto-generated method stub
							if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
									&& (lastVisibleIndex ) == planWorkAdapter.getCount()) {
								// 当滑到底部时自动加载
								if(type == TYPE_WX){
									pg.setVisibility(View.VISIBLE);
									int tiaoshu= insCheckFacRoadList.size()+1;
									NotPatrolTask notPatrolTask = new NotPatrolTask(ShowPatrolDataExpActivity.this, insTablePushTaskVo, sHandler, insCheckFacRoadList);
									notPatrolTask.execute(""+tiaoshu+"");
								}

							}
						}
						
					});
					break;
				
				case 2:
					final List<Map<String, Object>> dataList2;
					dataList2 = new ArrayList<Map<String, Object>>();
					if(insCheckFacRoadList!=null){
						for(InsCheckFacRoad insCheckFacRoad : insCheckFacRoadList){
							Map<String, Object> d = new HashMap<String, Object>();
							d.put("InsCheckFacRoad", insCheckFacRoad);
							dataList2.add(d);
						}
					}
					planWorkAdapter.setHandler(sHandler);
					planWorkAdapter.setPdList(dataList2);
					planWorkAdapter.notifyDataSetChanged();
					break;
					
				case 3:
					data_model.setText("列表");
					dspModel=1;
					//isFirst = false;
					patrolListModel.setVisibility(View.GONE);
					patrolMapModel.setVisibility(View.VISIBLE);
					drawGraphLoctionUtil=new DrawGraphLoctionUtil(insMapView,  ShowPatrolDataExpActivity.this);
					drawGraphLoctionUtil.drawGraphLoction(msg.getData().getString("shapestr"));
					break;
					
				case 4:
					updateList(TYPE_WX);
					break;
					
				case 5:
					drawGraphLoctionUtil.clearGraphy();
					showList();
					break;
				
				default:
					break;
				}
			}
		};
		
		insCheckFacRoadList = new ArrayList<InsCheckFacRoad>();
		
		selectRoadList = new ArrayList<InsCheckFacRoad>();
		
		isFirst = false;
		drawGraphLoctionUtil =new DrawGraphLoctionUtil(insMapView,  this);
		insMapView.getMemuButton().setVisibility(View.GONE);
		insMapView.getMap_search_layout().setVisibility(View.GONE);
		
		if(Constant.PLAN_PAICHA_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
			insMapView.getLayerButton().setVisibility(View.GONE);
			insMapView.getClearButton().setVisibility(View.GONE);
			
			ImageButton mainjButton=insMapView.getMianjButton();
			mainjButton.setVisibility(View.VISIBLE);
			mainjButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mapMeterScope==null){
						mapMeterScope = new MapMeterScope(ShowPatrolDataExpActivity.this,insMapView, ShowPatrolDataExpActivity.this,insTablePushTaskVo.getTaskNum());
					}else{
						mapMeterScope.clearAllLayer();
					}
					mapMeterScope.setDrawType(MapMeterScope.POLY);
				}
			});
			
			ImageButton regionButton = insMapView.getRegionButton();
			regionButton.setVisibility(View.VISIBLE);
			regionButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(mapMeterScope==null){
						mapMeterScope=new MapMeterScope(ShowPatrolDataExpActivity.this,insMapView, ShowPatrolDataExpActivity.this,insTablePushTaskVo.getTaskNum());
					}else{
						AppContext.getInstance().setmMapPoint(null);
						mapMeterScope.clearAllLayer();
					}
					mapMeterScope.setDrawType(MapMeterScope.POINTS);
				}
			});
			UserRight userRight = AppContext.getInstance().getUserRight();
			
			String valvePipe=userRight.getValvePipe();
			String[] valvePipes=valvePipe.split(",");
			
			for (int j = 0; j < valvePipes.length; j++) {
			
				String pipeLine=userRight.getPipeline();
				String[] pipeLines=pipeLine.split(",");
				for (int i = 0; i < pipeLines.length; i++) {
					if(AppContext.getInstance().getCurUser().getGroupName().equals(pipeLines[i])||AppContext.getInstance().getCurUser().getGroupName().equals(valvePipes[j])){
						ImageButton pointButton=insMapView.getPointButton();
						pointButton.setVisibility(View.VISIBLE);
						pointButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								if(mapMeterScope==null){
									mapMeterScope=new MapMeterScope(ShowPatrolDataExpActivity.this,insMapView, ShowPatrolDataExpActivity.this,insTablePushTaskVo.getTaskNum());
								}else{
									mapMeterScope.clearAllLayer();
								}
								mapMeterScope.setDrawType(MapMeterScope.POINT);
							}
						});
						
						ImageButton selectButton=insMapView.getSelectButton();
						selectButton.setVisibility(View.GONE);
						selectButton.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(mapMeterScope==null){
									mapMeterScope=new MapMeterScope(ShowPatrolDataExpActivity.this,insMapView, ShowPatrolDataExpActivity.this,insTablePushTaskVo.getTaskNum());
								}else{
									mapMeterScope.clearAllLayer();
								}
								mapMeterScope.setDrawType(MapMeterScope.SELECT);
							}
						});
					}
				}
				
				String valve=userRight.getValve();
				String[] valves=valve.split(",");
				for (int i = 0; i < valves.length; i++) {
					if(AppContext.getInstance().getCurUser().getGroupName().equals(valves[i])||AppContext.getInstance().getCurUser().getGroupName().equals(valvePipes[j])){
						ImageButton valueButton=insMapView.getValueButton();
						valueButton.setVisibility(View.VISIBLE);
						valueButton.setOnClickListener(new OnClickListener() { 
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(mapMeterScope==null){
									mapMeterScope=new MapMeterScope(ShowPatrolDataExpActivity.this,insMapView, ShowPatrolDataExpActivity.this,insTablePushTaskVo.getTaskNum());
								}else{
									mapMeterScope.clearAllLayer();
								}
								mapMeterScope.setDrawType(MapMeterScope.VALUE);
							}
						});
						
						ImageButton selectButton2=insMapView.getSelectButton2();
						selectButton2.setVisibility(View.GONE);
						selectButton2.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								if(mapMeterScope==null){
									mapMeterScope=new MapMeterScope(ShowPatrolDataExpActivity.this,insMapView, ShowPatrolDataExpActivity.this,insTablePushTaskVo.getTaskNum());
								}else{
									mapMeterScope.clearAllLayer();
								}
								mapMeterScope.setDrawType(MapMeterScope.SELECT2);
							}
						});
					}
				}
			}
		}
		
		insMapView.getMapView().setOnStatusChangedListener(new OnStatusChangedListener() {

			private static final long serialVersionUID = 1L;
			
			
			public void onStatusChanged(Object source, STATUS status) {
				if (source == insMapView.getMapView() && status == STATUS.INITIALIZED) {
					if (!isFirst){
						drawGraphLoctionUtil.clearGraphy();
						//等巡检路段
						for(String geo :dataOp.getThisTimeDataWtsGeo()){
							drawGraphLoctionUtil.drawGraphLoctionMeger(geo,Color.YELLOW);//等巡路段
							
						}
						//上次巡查遗留情况
						List<Polyline> preTrace = dataOp.getTaskPreTrace();
						if(null != preTrace){
							for(Polyline geo : preTrace){
								drawGraphLoctionUtil.drawRunTrace(geo,Color.BLUE);//上次路段巡遗留情况
							}
						}
						//历史轨迹
					try {
						List<HistoryTrajectoryVO> historyTrajectoryVOlist = null;
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("time", MyDateTools.getCurDate());
							historyTrajectoryVOlist = 
								 AppContext.getInstance()
									.getAppDbHelper().getDao(HistoryTrajectoryVO.class).queryBuilder().orderBy("id", true).where().eq("time", MyDateTools.getCurDate()).query(); 
						
						if(numer==0){
							numer = historyTrajectoryVOlist.size();
						}	
							
						if(historyTrajectoryVOlist!=null){
							drawGraphLoctionUtil.drawTrajectory(historyTrajectoryVOlist,Color.argb(70, 215, 15, 15));
								}
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						}
					
						//已巡过的路段
						for(String geo :dataOp.getHadDoDataGeo()){
							drawGraphLoctionUtil.drawGraphLoctionMeger(geo,Color.GREEN);//已巡路段
						}
						//绘制当前正在巡检路段
						if ((null != insPatrolOnsiteRecordVO)&&(insPatrolOnsiteRecordVO.getEnterState()==1)){
							drawGraphLoctionUtil.drawGraphLoctionMeger(insPatrolOnsiteRecordVO.getWktGeom(),Color.rgb(152, 251, 152));//正在巡路段
						}
						// 签到点
						if(chkDataOp!=null&&chkDataOp.getThisTimeDataWtsGeo()!=null){
							for(String geo:chkDataOp.getThisTimeDataWtsGeo()){
								drawGraphLoctionUtil.drawGraphLoctionMeger(geo,Color.YELLOW);
							}
						}
						
						//初次定位
						doInsStart(); 
						
						if (AppContext.getInstance().getCurLocation()!=null){
							double x=AppContext.getInstance().getCurLocation().getMapx();
							double y=AppContext.getInstance().getCurLocation().getMapy();
							Point point = new Point(x, y);
							insMapView.zoomToScale(point, insMapView.loadLastMapSacle());							
						}
						isFirst = true;
						insMapView.setFlgmapInitialized(true);
					}
				}
			}
		});

		
		TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
				this, false, false, insTablePushTaskVo.getTaskNum(),
				Constant.UPLOAD_STATUS_WORK, null,
				insTablePushTaskVo.getTitle(),
				insTablePushTaskVo.getTaskCategory(), null, null, null);
		taskFeedBackAsyncTask.execute();
	}

	//启动路段导航示意
	private void doInsStart(){
		if (!timer_start){
			try{
				timer.schedule(new TimerTask() {
	
					@Override
					public void run() {
						// TODO Auto-generated method stub
						myHandler.sendEmptyMessage(1);
					}
				}, 0, 5000);
				
				timer_start = true;
				
			}catch(IllegalStateException ex){
				timer_start = false;
				;
			}
		}
	}
	
	//不同作业状态下数据列表
	private void updateList(String type) {
		if (null != dataOp)
			dataOp.refreshThisTimeData();
		if (null != chkDataOp)
			chkDataOp.refreshThisTimeData();
		
		if (TYPE_YX.equals(type)) {// 已巡
			show_patrol_data_wx.setVisibility(View.VISIBLE);
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this,dataOp.getHadDoDataList(),
						sHandler, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setHandler(sHandler);
				planWorkAdapter.setPdList(dataOp.getHadDoDataList());
				planWorkAdapter.notifyDataSetChanged();
			}		
		}else if (TYPE_DX.equals(type)) {// 待巡
			show_patrol_data_wx.setVisibility(View.VISIBLE);
			show_search_ll.setVisibility(View.GONE);
			show_dx_title.setBackgroundResource(R.drawable.tv_green_bg);
			show_wx_title.setBackgroundResource(0);
			show_yx_title.setBackgroundResource(0);
			show_qdd_title.setBackgroundResource(0);
			show_search_title.setBackgroundResource(0);
			show_nearby_title.setBackgroundResource(0);

			show_dx_title.setTextColor(Color.parseColor("#3a5fcd"));
			show_wx_title.setTextColor(Color.parseColor("#737373"));
			show_yx_title.setTextColor(Color.parseColor("#737373"));
			show_qdd_title.setTextColor(Color.parseColor("#737373"));
			show_search_title.setTextColor(Color.parseColor("#737373"));
			show_nearby_title.setTextColor(Color.parseColor("#737373"));
			
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this,dataOp.getWouldDoDataList(),
						sHandler, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setHandler(sHandler);
				planWorkAdapter.setPdList(dataOp.getWouldDoDataList());
				planWorkAdapter.notifyDataSetChanged();
			}		
		} else if (TYPE_WX.equals(type)) {// 未巡
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this, dataOp.getWouldDoDataList(), null, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setPdList(dataOp.getWouldDoDataList());
				planWorkAdapter.notifyDataSetChanged();
			}
				show_patrol_data_wx.setVisibility(View.VISIBLE);
			
		} else if (TYPE_QDD.equals(type)) {// 簽到
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this, chkDataOp.getWouldDoDataList(),
						null, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setPdList(chkDataOp.getWouldDoDataList());
				planWorkAdapter.notifyDataSetChanged();
			}
			show_patrol_data_wx.setVisibility(View.VISIBLE);
			
		} else if(TYPE_SEARCH.equals(type)){
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this, dataOp.getSearchDataList(), null, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setPdList(dataOp.getSearchDataList());
				planWorkAdapter.notifyDataSetChanged();
			}
			show_patrol_data_wx.setVisibility(View.VISIBLE);
			
		} else if(TYPE_NEARBY.equals(type)){
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this, dataOp.getNearbyDataList(), null, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setPdList(dataOp.getNearbyDataList());
				planWorkAdapter.notifyDataSetChanged();
			}
			show_patrol_data_wx.setVisibility(View.VISIBLE);
		}
	}

	private void init() {
		show_work_op_ll = (LinearLayout) findViewById(R.id.show_work_pause_ll);// 暂停巡检容器
		show_work_pause_ll_lyx = (LinearLayout) findViewById(R.id.show_work_pause_ll_lyx);
		pgview=getLayoutInflater().inflate(R.layout.moredata, null);
		pg= (ProgressBar) pgview.findViewById(R.id.pg);
		
		hadDoDataListName = getIntent().getStringExtra("hadDoDataListName");
		isShowHadDoDataList = getIntent().getBooleanExtra("isShowHadDoDataList", true);
		isShowReport = getIntent().getBooleanExtra("isShowReport", true);
		title = getIntent().getStringExtra("title");
		
		show_task_ex_title = (TextView) findViewById(R.id.show_task_ex_title);// 标题
		if(title!=null){
			show_task_ex_title.setText(title);
		}
/*		show_work_pause_ll = (LinearLayout) findViewById(R.id.show_work_pause_ll);// 暂停巡检容器
*/
		
		show_work_op = (Button) findViewById(R.id.show_work_pause);// 暂停巡检按钮
		show_work_op.setOnClickListener(this);
		show_tv_pause = (TextView) findViewById(R.id.show_tv_pause);
		show_work_op_tv = (TextView) findViewById(R.id.show_work_pause_tv);
		
		show_patrol_data_back = (ImageButton) findViewById(R.id.show_patrol_data_back);// 返回
		show_patrol_data_back.setOnClickListener(this);
		show_patrol_data_wx = (ListView) findViewById(R.id.show_patrol_data_wx);// 未巡列表
		show_patrol_data_write = (Button) findViewById(R.id.show_patrol_data_write);// 填单
		show_patrol_data_write.setOnClickListener(this);
		if(isShowReport){
			show_patrol_data_write.setVisibility(View.VISIBLE);
		}else{
			show_patrol_data_write.setVisibility(View.GONE);
		}
		show_work_content = (TextView) findViewById(R.id.show_work_content);// 当前巡查
		show_work_message = (TextView) findViewById(R.id.show_work_message);// 当前巡查

		show_qdd_msg_rl = (RelativeLayout) findViewById(R.id.show_qdd_msg_rl);// 当前签到点
		show_qdd_msg = (TextView) findViewById(R.id.show_qdd_msg);// 签到提示
		show_work_arrive = (Button) findViewById(R.id.show_work_arrive);// 签到
		show_work_arrive.setOnClickListener(this);
		show_work_im = (Button) findViewById(R.id.show_work_im);// 工地填单
		show_work_im.setOnClickListener(this);
		show_work_message_ly = (LinearLayout) findViewById(R.id.show_work_message_ly);// 当前巡查容器

		show_qdd_title = (TextView) findViewById(R.id.show_qdd_title);// 簽到點
		show_qdd_title.setOnClickListener(this);
		show_yx_title = (TextView) findViewById(R.id.show_yx_title);
		show_yx_title.setOnClickListener(this);
		show_wx_title = (TextView) findViewById(R.id.show_wx_title);
		show_wx_title.setOnClickListener(this);
		show_dx_title = (TextView) findViewById(R.id.show_dx_title);
		show_dx_title.setOnClickListener(this);
		patrolListModel = (LinearLayout) findViewById(R.id.patrolListModel);
		patrolListModel.setVisibility(View.VISIBLE);
		patrolMapModel = (LinearLayout) findViewById(R.id.patrolMapModel);
		patrolMapModel.setVisibility(View.GONE);
		
		data_model = (Button) findViewById(R.id.data_model);// 显示模式
		data_model.setOnClickListener(this);
		// 控件控制
		if(isShowHadDoDataList){
			show_yx_title.setVisibility(View.VISIBLE);
		}else{
			show_yx_title.setVisibility(View.GONE);
			show_wx_title.setText("巡查列表");
		}
		if(hadDoDataListName!=null){
			show_wx_title.setText(hadDoDataListName);
		}
		//show_patrol_data_write.setVisibility(View.GONE);
		show_work_im.setVisibility(View.VISIBLE);
		show_work_arrive.setVisibility(View.GONE);
		show_qdd_title.setVisibility(View.GONE);
		show_work_content.setVisibility(View.GONE);
		show_work_message.setText("温情提示：您还没有进入巡查区域！");
		//show_work_message_ly.setVisibility(View.GONE);
		//show_work_message.setVisibility(View.VISIBLE);
		//地图容器
		insMapView = (MyMapView) findViewById(R.id.insMapView);
		
		isShowMap = getIntent().getBooleanExtra("isShowMap", true);
		if(isShowMap){
			data_model.setVisibility(View.VISIBLE);
		}else{
			data_model.setVisibility(View.GONE);
		}
		isShowMessage = getIntent().getBooleanExtra("isShowMessage", true);
		if(isShowMessage){
			show_work_message_ly.setVisibility(View.VISIBLE);
		}else{
			show_work_message_ly.setVisibility(View.GONE);
		}
		show_search_title = (TextView) findViewById(R.id.show_search_title);// 搜索
		show_search_title.setOnClickListener(this);
		
		show_search_ll = (LinearLayout) findViewById(R.id.show_search_ll);// 搜索容器
		show_search_num = (EditText) findViewById(R.id.show_search_num);// 搜索输入
		show_search_btn = (Button) findViewById(R.id.show_search_btn);// 搜索按钮
		show_search_btn.setOnClickListener(this);
		show_search_code = (Button) findViewById(R.id.show_search_code);// 二维码
		show_search_code.setOnClickListener(this);
		isShowSearch = getIntent().getBooleanExtra("isShowSearch", false);
		if(isShowSearch){
			show_search_title.setVisibility(View.VISIBLE);
		}else{
			show_search_title.setVisibility(View.GONE);
		}
		
		show_nearby_title = (TextView) findViewById(R.id.show_nearby_title);
		show_nearby_title.setOnClickListener(this);
		isShowNearby = getIntent().getBooleanExtra("isShowNearby", false);
		if(isShowNearby){
			show_nearby_title.setVisibility(View.VISIBLE);
		}else{
			show_nearby_title.setVisibility(View.GONE);
		}
	}

	public void zoomToPositon(double x,double y){		
		if (AppContext.getInstance().getCurLocation()!=null){
			double x1=AppContext.getInstance().getCurLocation().getMapx();
			double y1=AppContext.getInstance().getCurLocation().getMapy();
			Point point = new Point(x1, y1);
			insMapView.zoomToScale(point, 600d);
		}
	}
	
	// 刷新计时
	private Handler timeHamdler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				if (dataOp.dataType() == DataType.Pl){
					updateWork(dataOp.getCurTaskLuDuan());
				}else if(dataOp.dataType() == DataType.Pts){
					updateWorkOths(dataOp.getCurentNearObjs());
				}else{
					updateWorkOth(dataOp.getCurentNearObj());
				}
				
				updateWorkQdd(chkDataOp.getCurentNearObj());
			default:
				break;
			}
		}

	};
	
	//进入路段确认对话框
	//状态设置:1:进入问答;2:退出问题
	protected void entRoadDialog(String message,final int state) {
		if (state == -1){
			return ;
		}else{
			dataOp.insPause();
			
			AlertDialog.Builder builder = new Builder(ShowPatrolDataExpActivity.this);
			builder.setMessage(message);
			builder.setTitle("提示");
			builder.setPositiveButton("否", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					dataOp.insGoNo();	
				}
			});
			builder.setNegativeButton("是", new DialogInterface.OnClickListener() {   
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (state == 0){
						dataOp.speObjOp(insPatrolOnsiteRecordVO, 1, "");
					}else if ((state == 3)||(state == 4)){
						dataOp.speObjOp(insPatrolOnsiteRecordVO, state, "");
					}
					dataOp.insGoNo();
				}
			});
	
			final AlertDialog alterDialog = builder.create();
			
			final Handler killDialogHd = new Handler() {  
		        public void handleMessage(Message msg) {  
		            if (msg.what > 0) {  
		                //Log.v("time", msg.what + "");  
		               // textView.setText(msg.what + "秒自动跳转下一首");  

		            } else {  
		            	if (alterDialog.isShowing()){
			            	alterDialog.dismiss();  
			            	dataOp.insGoNo();
		            	}
		               // textView.setText("Over......");  

		            }  
		            super.handleMessage(msg);  
		        }  

		    };  
			
		    alterDialog.show();
		    
			Timer time = new Timer(true);  
            TimerTask tt = new TimerTask() {  
                int countTime = 10;  
                public void run() {  
                    if (countTime > 0) {  
                        countTime--;  
                    }  
                    Message msg = new Message();  
                    msg.what = countTime;  
                    killDialogHd.sendMessage(msg);  
                }  

            };  
            time.schedule(tt, 1000, 1000);  
		}
	}
	
	private void updateWork(InsPatrolOnsiteRecordExtVO tempPatrolOnsiteRecordVO) {
		boolean needSpeed = false;
		boolean exception = false;
		
		insPatrolOnsiteRecordVO = tempPatrolOnsiteRecordVO;
		
		if (null == insPatrolOnsiteRecordVO){
			return;
		}
		
		String warnState = insPatrolOnsiteRecordVO.getWarnState();
		show_work_op_ll.setVisibility(View.GONE);//暂停控制容器不显示
		show_work_pause_ll_lyx.setVisibility(View.GONE);//暂停控制容器不显示
		
		
		if (warnState.equals("00")){
			show_work_message.setText("温情提示：您还没进入巡查区域");
			needSpeed = roadTTsMemo.needSpeed(show_work_message.getText().toString(),warnState);
		}else if (warnState.equals("01")){
			show_work_message.setText("本次无需要巡查的路段");
			needSpeed = roadTTsMemo.needSpeed(show_work_message.getText().toString(),warnState);
		}else if (warnState.equals("02")){
			show_work_message.setText("本次计划路段已巡查完毕");
			needSpeed = roadTTsMemo.needSpeed(show_work_message.getText().toString(),warnState);
		}else if (warnState.equals("03")){
			show_work_message.setText("目前不在计划路段上");
			needSpeed = roadTTsMemo.needSpeed(show_work_message.getText().toString(),warnState);
		}else if (warnState.equals("04")){
			show_work_message.setText("您已不在巡查区域");
			needSpeed = roadTTsMemo.needSpeed(show_work_message.getText().toString(),warnState);
		}else{
			if (null == insPatrolOnsiteRecordVO.getRoadName()){
				show_work_message.setText("");
				return;
			}
			String attState = String.valueOf(insPatrolOnsiteRecordVO.getEnterState());
			if (insPatrolOnsiteRecordVO.getEnterState() != 2){
				show_work_op.setBackgroundResource(R.drawable.xj_pause);
				show_tv_pause.setText("暂停");
				show_work_op_tv.setText("点击暂停巡检：");
			}else{
				show_work_op.setBackgroundResource(R.drawable.xj_stark);
				show_tv_pause.setText("继续");
				show_work_op_tv.setText("点击继续巡检：");
			}
			show_work_op_ll.setVisibility(View.GONE);//暂停控制容器不显示
			show_work_pause_ll_lyx.setVisibility(View.GONE);//暂停控制容器不显示
			
			if (warnState.equals("10")){  //自动模式
										
				switch (insPatrolOnsiteRecordVO.getEnterState()){
				case -1:
					attState = String.valueOf((int) insPatrolOnsiteRecordVO.getMinDistance());
					show_work_message.setText("您距离:"
							+ insPatrolOnsiteRecordVO.getRoadName() + "大约还有"
							+ (int) insPatrolOnsiteRecordVO.getMinDistance() + "米");
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					break;
				case 1:
					if (!insPatrolOnsiteRecordVO.getPreRoadName().equals("")){
						show_work_message.setText("您已离开:"
								+ insPatrolOnsiteRecordVO.getPreRoadName() + "进入:"
								+ insPatrolOnsiteRecordVO.getRoadName());
					}else{
						show_work_message.setText("您已在:"+ insPatrolOnsiteRecordVO.getRoadName());
					}
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					break;
				case 2:
					show_work_message.setText(insPatrolOnsiteRecordVO.getRoadName()+"已被暂停,如需执行请点‘继续’");
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					break;
				case 3:
					show_work_message.setText("您已离开:" + insPatrolOnsiteRecordVO.getRoadName());
					show_work_op_ll.setVisibility(View.GONE);
					show_work_pause_ll_lyx.setVisibility(View.GONE);
					break;
				case 4:
					show_work_message.setText("您已偏离:" + insPatrolOnsiteRecordVO.getRoadName());
					show_work_op_ll.setVisibility(View.GONE);
					show_work_pause_ll_lyx.setVisibility(View.GONE);
					break;
				case 5:
					attState = String.valueOf((int) insPatrolOnsiteRecordVO.getMinDistance());
					show_work_message.setText("您已偏离:"
							+ insPatrolOnsiteRecordVO.getRoadName()+
							+ (int) insPatrolOnsiteRecordVO.getMinDistance() + "米"
							+ "请回到有效范围");
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					break;
				case 6:
					show_work_message.setText(insPatrolOnsiteRecordVO.getRoadName()+"已经可以继续巡查");
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					break;
				}

			}else if (warnState.equals("11")){//手工模式
				
				switch (insPatrolOnsiteRecordVO.getEnterState()){
				case -1:
					attState = String.valueOf((int) insPatrolOnsiteRecordVO.getMinDistance());
					show_work_message.setText("您距离:"
							+ insPatrolOnsiteRecordVO.getRoadName() + "大约还有"
							+ (int) insPatrolOnsiteRecordVO.getMinDistance() + "米");
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					exception = true;
					break;
				case 0:
					show_work_message.setText("已经到达"+ insPatrolOnsiteRecordVO.getRoadName()+"是否确定进入巡查");
					
//					show_work_op.setText("进入");
					show_tv_pause.setText("进入");
					show_work_op_tv.setVisibility(View.GONE);
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					exception = false;
					break;
				case 1:
					show_work_message.setText("您已进入"+ insPatrolOnsiteRecordVO.getRoadName()+"巡查");
					
					show_work_op.setBackgroundResource(R.drawable.xj_pause);
					show_tv_pause.setText("暂停");
					show_work_op_tv.setVisibility(View.VISIBLE);
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					exception = true;
					break;
				case 2:
					show_work_message.setText(insPatrolOnsiteRecordVO.getRoadName()+"已被暂停,如需执行请点‘继续’");
					
					show_work_op.setBackgroundResource(R.drawable.xj_stark);
					show_tv_pause.setText("继续");
					show_work_op_tv.setVisibility(View.VISIBLE);
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					exception = true;
					break;
				case 3:
					show_work_message.setText("是否确定完成" + insPatrolOnsiteRecordVO.getRoadName()+"本次巡查任务");

//					show_work_op.setText("完成");
					show_tv_pause.setText("完成");
					show_work_op_tv.setVisibility(View.GONE);
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					exception = false;
					break;
				case 4:
					attState = String.valueOf((int) insPatrolOnsiteRecordVO.getMinDistance());
					if (null == insPatrolOnsiteRecordVO.getCurNearObj().getObjName()||
							insPatrolOnsiteRecordVO.getCurNearObj().getObjName().equals(insPatrolOnsiteRecordVO.getRoadName())){
						show_work_message.setText("是否确定中断" + insPatrolOnsiteRecordVO.getRoadName()+"本次巡查任务");
					}else{
						show_work_message.setText("是否确定中断" + insPatrolOnsiteRecordVO.getRoadName()+"本次巡查任务,进入"
								+insPatrolOnsiteRecordVO.getCurNearObj().getObjName()+"巡查");
					}
					
//					show_work_op.setText("离开处理");  //暂停、中断、终止
					show_tv_pause.setText("离开处理");  //暂停、中断、终止
					show_work_op_tv.setVisibility(View.GONE);
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					exception = false;
					break;
				case 5:
					attState = String.valueOf((int) insPatrolOnsiteRecordVO.getMinDistance());
					show_work_message.setText("您已偏离:"
							+ insPatrolOnsiteRecordVO.getRoadName()+
							+ (int) insPatrolOnsiteRecordVO.getMinDistance() + "米"
							+ "请回到有效范围");
					
//					show_work_op.setText("离开处理");
					show_tv_pause.setText("离开处理");
					show_work_op_tv.setVisibility(View.GONE);
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					exception = true;
					break;
				case 6:
					show_work_message.setText(insPatrolOnsiteRecordVO.getRoadName()+"已经可以继续巡查");
					show_work_op_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					exception = true;
					break;
				}
			}
			needSpeed = roadTTsMemo.needSpeed(insPatrolOnsiteRecordVO.getRoadName(),warnState+attState);
			
			/*if (warnState.equals("11")){
				if (needSpeed && !exception){
					show_work_op_ll.setVisibility(View.VISIBLE);
					//entRoadDialog(show_work_message.getText().toString(),insPatrolOnsiteRecordVO.getEnterState());
				}
			}*/
		}
				
		updateList(type);
		if (needSpeed){
			if((!isPause)&&(!isWriteTable)){
				showVibrator(this);
			}
			spText(show_work_message.getText().toString());
		}
	}


	private void updateWorkOth(NearObject nearObject) {
		
		if (nearObject != null) {
			synchronized(nearObject){
				//show_qdd_msg_rl.setVisibility(View.VISIBLE);
				if (null != nearObject.getObjName()){
					text= "距离" + nearObject.getObjName() + "大约还有："
							+ (int) nearObject.getMinDistance() + "米";
					show_work_message.setText(text);
					if (dspModel == 1){
						drawGraphLoctionUtil.drawGraphLoctionMeger(nearObject.getWktGeom(),Color.YELLOW);//等巡路段
						if (!timer_start){
							doInsStart();
						}
						
					}			
					boolean needSpeed = chkPntTTsMemo.needSpeed(nearObject.getObjNum(),"00");

					if (needSpeed){
						spText(text);
					}
					
				}
			}
			
		} else {
			//show_qdd_msg_rl.setVisibility(View.GONE);
		}
		
	}
	private void updateWorkOths(List<NearObject> nearObjectList){
		if(nearObjectList!=null&&nearObjectList.size()>0){
			synchronized(nearObjectList){
				String text = "";
				for(NearObject nearObject :nearObjectList){
					text+="距离" + nearObject.getObjName() + "大约还有："+ (int) nearObject.getMinDistance() + "米\n";
					if (dspModel == 1){
						drawGraphLoctionUtil.drawGraphLoctionMeger(nearObject.getWktGeom(),Color.YELLOW);//等巡路段
						
						if (!timer_start){
							doInsStart();
						}
					}
				}
				show_work_message.setText(text);
				spText(show_work_message.getText().toString());
			}
		}
	}
	
	private void updateWorkQdd(NearObject nearObject) {
		
		if (nearObject != null) {
			synchronized(nearObject){
				show_qdd_msg_rl.setVisibility(View.VISIBLE);
				if (null != nearObject.getObjName()){
					show_qdd_msg.setText("距离" + nearObject.getObjName() + "大约还有："
							+ (int) nearObject.getMinDistance() + "米");
					
					if (dspModel == 1){
						drawGraphLoctionUtil.drawGraphLoctionMeger(nearObject.getWktGeom(),Color.YELLOW);//等巡路段
						if (!timer_start){
							doInsStart();
						}
					}
										
					boolean needSpeed = chkPntTTsMemo.needSpeed(nearObject.getObjNum(),"00");

					if (needSpeed){
						spText(show_qdd_msg.getText().toString());
					}
				}
			}
		} else {
			show_qdd_msg_rl.setVisibility(View.GONE);
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.show_wx_title:// 未巡
			if(Constant.PLAN_PAICHA_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
				NotPatrolTask notPatrolTask = new NotPatrolTask(ShowPatrolDataExpActivity.this,insTablePushTaskVo,sHandler,insCheckFacRoadList);
				notPatrolTask.execute("0");
			}else{
				show_search_ll.setVisibility(View.GONE);
				show_wx_title.setBackgroundResource(R.drawable.tv_green_bg);
				show_yx_title.setBackgroundResource(0);
				show_qdd_title.setBackgroundResource(0);
				show_search_title.setBackgroundResource(0);
				show_nearby_title.setBackgroundResource(0);
				show_dx_title.setBackgroundResource(0);
	
				show_wx_title.setTextColor(Color.parseColor("#3a5fcd"));
				show_yx_title.setTextColor(Color.parseColor("#737373"));
				show_qdd_title.setTextColor(Color.parseColor("#737373"));
				show_search_title.setTextColor(Color.parseColor("#737373"));
				show_nearby_title.setTextColor(Color.parseColor("#737373"));
				show_dx_title.setTextColor(Color.parseColor("#737373"));
				
				type = TYPE_WX;
				updateList(type);
			break;
			}
			
		case R.id.show_dx_title:// 待巡
				show_search_ll.setVisibility(View.GONE);
				show_dx_title.setBackgroundResource(R.drawable.tv_green_bg);
				show_wx_title.setBackgroundResource(0);
				show_yx_title.setBackgroundResource(0);
				show_qdd_title.setBackgroundResource(0);
				show_search_title.setBackgroundResource(0);
				show_nearby_title.setBackgroundResource(0);
	
				show_dx_title.setTextColor(Color.parseColor("#3a5fcd"));
				show_wx_title.setTextColor(Color.parseColor("#737373"));
				show_yx_title.setTextColor(Color.parseColor("#737373"));
				show_qdd_title.setTextColor(Color.parseColor("#737373"));
				show_search_title.setTextColor(Color.parseColor("#737373"));
				show_nearby_title.setTextColor(Color.parseColor("#737373"));	
				
				type = TYPE_DX;
				updateList(type);
			break;
			
		case R.id.show_yx_title:// 已巡
			show_search_ll.setVisibility(View.GONE);
			show_wx_title.setBackgroundResource(0);
			show_yx_title.setBackgroundResource(R.drawable.tv_green_bg);
			show_qdd_title.setBackgroundResource(0);
			show_search_title.setBackgroundResource(0);
			show_nearby_title.setBackgroundResource(0);
			show_dx_title.setBackgroundResource(0);
			
			show_wx_title.setTextColor(Color.parseColor("#737373"));
			show_yx_title.setTextColor(Color.parseColor("#3a5fcd"));
			show_qdd_title.setTextColor(Color.parseColor("#737373"));
			show_search_title.setTextColor(Color.parseColor("#737373"));
			show_nearby_title.setTextColor(Color.parseColor("#737373"));
			show_dx_title.setTextColor(Color.parseColor("#737373"));
			
			type = TYPE_YX;
			updateList(type);
			break;
		case R.id.show_qdd_title:// 签到點
			show_search_ll.setVisibility(View.GONE);
			show_wx_title.setBackgroundResource(0);
			show_yx_title.setBackgroundResource(0);
			show_search_title.setBackgroundResource(0);
			show_qdd_title.setBackgroundResource(R.drawable.tv_green_bg);
			show_nearby_title.setBackgroundResource(0);
			show_dx_title.setBackgroundResource(0);
			
			show_wx_title.setTextColor(Color.parseColor("#737373"));
			show_yx_title.setTextColor(Color.parseColor("#737373"));
			show_qdd_title.setTextColor(Color.parseColor("#3a5fcd"));
			show_search_title.setTextColor(Color.parseColor("#737373"));
			show_nearby_title.setTextColor(Color.parseColor("#737373"));
			show_dx_title.setTextColor(Color.parseColor("#737373"));
			type = TYPE_QDD;
			updateList(type);
			break;
		case R.id.show_work_arrive:// 签到
			//chkPntdialog();
			showDialog(chkDataOp);
			break;
		case R.id.show_patrol_data_back:
			//finish();
			AppContext.getInstance().setmMapPoint(null);
			befClose();
			break;
		case R.id.show_patrol_data_write:
			if(Constant.PLAN_PAICHA_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
				if(AppContext.getInstance().getmMapPoint()!=null){
					String prid="";
//					if(insCheckFacRoadLoad!=null){
					if(insCheckFacRoadLoad!=null){
						for (int i = 0; i < insCheckFacRoadLoad.size(); i++) {
							if(insCheckFacRoadLoad.size()==i+1){
								prid+=insCheckFacRoadLoad.get(i).getFacNum();
							}else{
								prid+=insCheckFacRoadLoad.get(i).getFacNum()+",";
							}
						}
					}
						HashMap<String, String> params1 = new HashMap<String, String>();
						params1.put("workTaskNum", insTablePushTaskVo.getTaskNum());
						if(insCheckFacRoadLoad!=null){
							params1.put("pipeId", prid);
						}
						if(AppContext.getInstance().getmMapPoint()!=null){
							String pointx=String.valueOf(AppContext.getInstance().getmMapPoint().getX());
							String pointy=String.valueOf(AppContext.getInstance().getmMapPoint().getY());
							params1.put("buildingCoordinate", pointx+" "+pointy);
						}
						if ((dataOp.dataType() == DataType.Pl)&&(null != dataOp.getCurTaskLuDuan())){
			            		params1.put("sections", dataOp.getCurTaskLuDuan().getRoadName());
			    				params1.put("sectionsNum", dataOp.getCurTaskLuDuan().getRoadNum());
			    				showModuleDialog(taskModule, params1,
			    						prid);
			            }else if ((dataOp.dataType() != DataType.Pl)&&(null != dataOp.getCurentNearObj())){
			            	params1.put("sections", dataOp.getCurentNearObj().getObjName());
							params1.put("sectionsNum", dataOp.getCurentNearObj().getObjNum());
							showModuleDialog(taskModule, params1,
									dataOp.getCurentNearObj().getObjId());
			            }else{
			            	showModuleDialog(taskModule, params1,
			            			prid);
			            }
//					}else{
//						Toast.makeText(ShowPatrolDataExpActivity.this, "必须选取一个以上设施", Toast.LENGTH_SHORT).show();
//					}
	            }else{
	            	Toast.makeText(ShowPatrolDataExpActivity.this, "必须选取一个上报点", Toast.LENGTH_SHORT).show();
	            }
			}else{
				HashMap<String, String> params1 = new HashMap<String, String>();
				params1.put("taskNum", insTablePushTaskVo.getTaskNum());
	            if ((dataOp.dataType() == DataType.Pl)&&(null != dataOp.getCurTaskLuDuan())){
	
	            		params1.put("sections", dataOp.getCurTaskLuDuan().getRoadName());
	    				params1.put("sectionsNum", dataOp.getCurTaskLuDuan().getRoadNum());
	    				showModuleDialog(taskModule, params1,
	    						dataOp.getCurTaskLuDuan().getId());
	            }else if ((dataOp.dataType() != DataType.Pl)&&(null != dataOp.getCurentNearObj())){
	            	params1.put("sections", dataOp.getCurentNearObj().getObjName());
					params1.put("sectionsNum", dataOp.getCurentNearObj().getObjNum());
					showModuleDialog(taskModule, params1,
							dataOp.getCurentNearObj().getObjId());
	            }else{
	            	showModuleDialog(taskModule, params1,
							null);
	            }
			}
			break;
		case R.id.show_work_im:
			showDialog(dataOp);
			break;
		case R.id.data_model:
			if ("地图".equals(data_model.getText())){
				data_model.setText("列表");
				dspModel=1;
				//isFirst = false;
				patrolListModel.setVisibility(View.GONE);
				patrolMapModel.setVisibility(View.VISIBLE);
				if (isFirst){
					//已巡过的路段
					for(String geo :dataOp.getHadDoDataGeo()){
						drawGraphLoctionUtil.drawGraphLoctionMeger(geo,Color.GREEN);//已巡路段
						
					}
					//绘制当前正在巡检路段
					if ((null != insPatrolOnsiteRecordVO)&&(insPatrolOnsiteRecordVO.getEnterState()==1)){
						drawGraphLoctionUtil.drawGraphLoctionMeger(insPatrolOnsiteRecordVO.getWktGeom(),Color.rgb(152, 251, 152));//正在巡路段
					}
					
					if (AppContext.getInstance().getCurLocation()!=null){
						double x=AppContext.getInstance().getCurLocation().getMapx();
						double y=AppContext.getInstance().getCurLocation().getMapy();
						Point point = new Point(x, y);
						insMapView.zoomToScale(point, 600d);
					}
				}
			}else{
				data_model.setText("地图");
				dspModel=0;
				patrolListModel.setVisibility(View.VISIBLE);
				patrolMapModel.setVisibility(View.GONE);
			}
			break;
		case R.id.show_search_title:
			show_search_ll.setVisibility(View.VISIBLE);
			show_wx_title.setBackgroundResource(0);
			show_yx_title.setBackgroundResource(0);
			show_search_title.setBackgroundResource(R.drawable.tv_green_bg);
			show_qdd_title.setBackgroundResource(0);
			show_nearby_title.setBackgroundResource(0);
			show_dx_title.setBackgroundResource(0);
			
			show_wx_title.setTextColor(Color.parseColor("#737373"));
			show_yx_title.setTextColor(Color.parseColor("#737373"));
			show_qdd_title.setTextColor(Color.parseColor("#737373"));
			show_search_title.setTextColor(Color.parseColor("#3a5fcd"));
			show_nearby_title.setTextColor(Color.parseColor("#737373"));
			show_dx_title.setTextColor(Color.parseColor("#737373"));
			
			type = TYPE_SEARCH;
			updateList(type);
			break;
		case R.id.show_nearby_title:
			show_search_ll.setVisibility(View.GONE);
			show_wx_title.setBackgroundResource(0);
			show_yx_title.setBackgroundResource(0);
			show_search_title.setBackgroundResource(0);
			show_qdd_title.setBackgroundResource(0);
			show_nearby_title.setBackgroundResource(R.drawable.tv_green_bg);
			show_dx_title.setBackgroundResource(0);
			
			show_wx_title.setTextColor(Color.parseColor("#737373"));
			show_yx_title.setTextColor(Color.parseColor("#737373"));
			show_qdd_title.setTextColor(Color.parseColor("#737373"));
			show_search_title.setTextColor(Color.parseColor("#737373"));
			show_nearby_title.setTextColor(Color.parseColor("#3a5fcd"));
			show_dx_title.setTextColor(Color.parseColor("#737373"));
			type = TYPE_NEARBY;
			updateList(type);
			break;
		case R.id.show_search_btn:
			if(!"".equals(show_search_num.getText()+"")){
				dataOp.searchData(show_search_num.getText()+"");
				updateList(type);
			}else{
				Toast.makeText(this, "编号不能为空", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.show_search_code:
			Intent intent =new Intent();
			intent.setClass(this, CaptureActivity.class);
			startActivityForResult(intent, CODE_RESULT_CODE);
			break;
		case R.id.show_work_pause:
			try{
				dataOp.insPause();
			
//				String s= show_work_op.getText()+"";
				String s= show_tv_pause.getText()+"";
				if("暂停".equals(s)){
					show_work_op.setBackgroundResource(R.drawable.xj_stark);
					show_tv_pause.setText("继续");
					show_work_op_tv.setText("点击继续巡检：");
					isPause = true;
					//dataOp.pause(insPatrolOnsiteRecordVO);
					dataOp.speObjOp(insPatrolOnsiteRecordVO,2, "");
				}else if ("继续".equals(s)){
					show_work_op.setBackgroundResource(R.drawable.xj_pause);
					show_tv_pause.setText("暂停");
					show_work_op_tv.setText("点击暂停巡检：");
					dataOp.speObjOp(insPatrolOnsiteRecordVO,6, "");
					isPause = false;
				}else if ("进入".equals(s)){
					dataOp.speObjOp(insPatrolOnsiteRecordVO,1, "");
				}else if ("完成".equals(s)){
					dataOp.speObjOp(insPatrolOnsiteRecordVO,3, "");
				}else{
					entRoadDialog(show_work_message.getText().toString(),insPatrolOnsiteRecordVO.getEnterState());
				}
			}finally{
				dataOp.insGoNo();
			}
			break;
		default:
			break;
		}
	}

	private void showDialog(IDataOperater dataOp){
		if (null != dataOp.getCurentNearObj()){
			showModuleDialog(dataOp.getLinkModule(), dataOp.getTransParams(), dataOp.getCurentNearObj().getObjId());
		} else {
			Toast.makeText(this, "还没进入签到区域", Toast.LENGTH_LONG).show();
		}
	}
	
	private void befClose(){
		String[] roadDoOpt = new String[] { "暂停当前路段巡检", "结束当前路段巡检" };
		
//		String s= show_work_op.getText()+"";
		String s= show_tv_pause.getText()+"";

		if (show_work_op_ll.getVisibility() == View.VISIBLE){
			if ("暂停".equals(s)){
				//当前路段不为空&&已经进入到该路段
				if ((null != insPatrolOnsiteRecordVO)&&(insPatrolOnsiteRecordVO.getEnterState()==1)){
					
					//是否暂停巡检，还是结束该路段巡检？
					
					ButtonOnClick buttonOnClick = new ButtonOnClick(0);
		
					new AlertDialog.Builder(ShowPatrolDataExpActivity.this).setTitle("方式选择")
							.setSingleChoiceItems(
							// .setMultiChoiceItems(
									roadDoOpt, 0, buttonOnClick)
							.setPositiveButton("取消", buttonOnClick)
							.setNegativeButton("确定", buttonOnClick).show();
				}else{
					// TODO Auto-generated method stub		
					timer.cancel();
					if (null != chkDataOp){
						chkDataOp.end();
					}
					
					if (null != dataOp){
						dataOp.end();
					}
					finish();
				}
			}
		}else{
			timer.cancel();
			if (null != chkDataOp){
				chkDataOp.end();
			}
			
			if (null != dataOp){
				dataOp.end();
			}
			finish();
		}
		
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			befClose();
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		//记录当前地图的缩放比例
		if (isFirst)
			insMapView.saveCurMapScale();
		// 巡检完设置为默认模式：步行
		AppContext.getInstance().setSpeedType("1");
		
		//BNTTSPlayer.stopTTS();
		
		TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
				ShowPatrolDataExpActivity.this, false, false,
				insTablePushTaskVo.getTaskNum(), Constant.UPLOAD_STATUS_PAUSE,
				null, insTablePushTaskVo.getTitle(),
				insTablePushTaskVo.getTaskCategory(), null, null, null);
		taskFeedBackAsyncTask.execute();
		

		super.onDestroy();

	}

	private void showModuleDialog(final List<Module> taskModule,
			final HashMap<String, String> params, final String pid) {
		if (taskModule == null) {
			Toast.makeText(this, "没有设定上报内容", Toast.LENGTH_LONG).show();
			return;
		}
		if (taskModule.size() == 0) {
			Toast.makeText(this, "数据已经提交", Toast.LENGTH_LONG).show();
			return;
		}
		if (taskModule.size() == 1) {
			if (insTablePushTaskVo != null) {
				TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
						ShowPatrolDataExpActivity.this, false, false,
						insTablePushTaskVo.getTaskNum(),
						Constant.UPLOAD_STATUS_WORK, null,
						insTablePushTaskVo.getTitle(),
						insTablePushTaskVo.getTaskCategory(), null, null, null);
				taskFeedBackAsyncTask.execute();
				Intent newFormInfo = new Intent(ShowPatrolDataExpActivity.this,
						RunForm.class);
				newFormInfo.putExtra("template", taskModule.get(0)
						.getTemplate());
				if(pid!=null){
					newFormInfo.putExtra("pid", pid);
				}
				newFormInfo
						.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
				newFormInfo
						.putExtra("tableName", insTablePushTaskVo.getTitle());
				newFormInfo.putExtra("taskCategory",
						insTablePushTaskVo.getTaskCategory());
				newFormInfo.putExtra("delete", true);
				newFormInfo.putExtra("iParams", params);
				startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
			}
			return;
		}
		String[] itmes = new String[taskModule.size()];

		for (int i = 0; i < itmes.length; ++i) {
			itmes[i] = taskModule.get(i).getName();
		}
		AlertDialog.Builder vDialog = new AlertDialog.Builder(this);
		vDialog.setTitle("选择工作类型");
		vDialog.setItems(itmes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (insTablePushTaskVo != null) {
					TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
							ShowPatrolDataExpActivity.this, false, false,
							insTablePushTaskVo.getTaskNum(),
							Constant.UPLOAD_STATUS_WORK, null,
							insTablePushTaskVo.getTitle(), insTablePushTaskVo
									.getTaskCategory(), null, null, null);
					taskFeedBackAsyncTask.execute();
					Intent newFormInfo = new Intent(
							ShowPatrolDataExpActivity.this, RunForm.class);
					newFormInfo.putExtra("template", taskModule.get(which)
							.getTemplate());
					newFormInfo.putExtra("pid", pid);
					newFormInfo.putExtra("taskNum",
							insTablePushTaskVo.getTaskNum());
					newFormInfo.putExtra("tableName",
							insTablePushTaskVo.getTitle());
					newFormInfo.putExtra("taskCategory",
							insTablePushTaskVo.getTaskCategory());
					newFormInfo.putExtra("delete", true);
					newFormInfo.putExtra("iParams", params);
					isWriteTable = true;
					startActivityForResult(newFormInfo,
							Constant.FROM_REQUEST_CODE);
				}
			}
		});
		vDialog.show();
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub
		/*
		 * if (status == TextToSpeech.SUCCESS) { Toast.makeText(this,
		 * "Text-To-Speech engine is initialized", Toast.LENGTH_LONG).show(); }
		 * else if (status == TextToSpeech.ERROR) { Toast.makeText(this,
		 * "Error occurred while initializing Text-To-Speech engine",
		 * Toast.LENGTH_LONG).show(); }
		 */
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		isWriteTable = false;
		if (Constant.FROM_REQUEST_CODE == requestCode && data != null
				&& Constant.FROM_RESULT_CODE == resultCode) {
			dataOp.callback(data);
			//dataOp.refreshThisTimeData();
			if(Constant.PLAN_PAICHA_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
				showList();
			}
			updateList(type);
		}else if(CODE_RESULT_CODE == requestCode && data != null){
			Bundle bundle =data.getBundleExtra("bundle");
			if(bundle!=null){
				show_search_num.setText(bundle.getString("result"));
				dataOp.searchData(show_search_num.getText()+"");
				updateList(type);
			}
		}
	}
	
	
	class TTsMemo {
		String state = String.valueOf("00");
		String num = "";
		
		public boolean needSpeed(String iNum,String iState){
			if (null==iNum||iNum.equals("")){
				state = iState;
				num = "";
				return true;
			}else if ("".equals(num)){
				state = iState;
				num = iNum;
				return true;
			}else if(num.equals(iNum)&&!state.equals(iState)){
				state = iState;
				num = iNum;
				return false;
			}else if (!num.equals(iNum)){
				state = iState;
				num = iNum;
				return true;
			}
			else{
				return false;
			}
		}
		
	}
	private Handler myHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				 NearObject  nearObject = dataOp.getCurentNearObj();
//				 insMapView.getCallout().hide();//关闭地图上的文字显示框
				 Point facPoint = null;
				 Point peoplePoint = null;
				 if(nearObject!=null){
					 facPoint = nearObject.getCoordinate();
					 TextView tv=new TextView(ShowPatrolDataExpActivity.this);
					 tv.setText("距离"+(int)nearObject.getMinDistance()+"米");
					 insMapView.getCallout().show(facPoint,tv);//把得到的值输出到文本框
				 }
				if (AppContext.getInstance().getCurLocation()!=null){
					double x=AppContext.getInstance().getCurLocation().getMapx();
					double y=AppContext.getInstance().getCurLocation().getMapy();
					peoplePoint = new Point(x, y);
					drawGraphLoctionUtil.drawIconLoction(peoplePoint, R.drawable.map_loc_people);
					//当人坐标不在当前地图视图时按当前缩放比例居中显示
					/*if (!MapUtil.inBound(insMapView,peoplePoint)){
						insMapView.zoomToScale(peoplePoint, insMapView.getScale());
					}*/
					//补充当小人位置不在当前显示视图时，自动居中显示
					//巡检覆盖范围
					if (insPatrolOnsiteRecordVO != null){
						//Polyline pl = drawGraphLoctionUtil.runTrace(peoplePoint);
						//上次巡查到的位置
						/*Geometry pre = insPatrolOnsiteRecordVO.getPreRunTrace();
						if (null != pre && !pre.isEmpty()){
							drawGraphLoctionUtil.drawRunTrace(pre,Color.BLUE);//已巡路段
							//drawGraphLoctionUtil.drawRunTraceBound(pl, AppContext.getInstance().getNavigation().getArrive());
						};*/
						
						Geometry pl = insPatrolOnsiteRecordVO.getNowRunRoadShape();
						if (null != pl && !pl.isEmpty()){
							drawGraphLoctionUtil.drawRunTrace(pl,Color.GREEN);//已巡路段
							//drawGraphLoctionUtil.drawRunTraceBound(pl, AppContext.getInstance().getNavigation().getArrive());
						};
					}
				}
				if(facPoint!=null&&peoplePoint!=null){
					drawGraphLoctionUtil.drawLine(peoplePoint, facPoint, Color.BLACK);
				}
				//实时历史监控
				try {
					List<HistoryTrajectoryVO> historyTrajectoryVOlist = null;
					Map<String, Object> m = new HashMap<String, Object>();
					m.put("time", MyDateTools.getCurDate());
					historyTrajectoryVOlist = AppContext.getInstance()
							.getAppDbHelper().getDao(HistoryTrajectoryVO.class)
							.queryBuilder().orderBy("id", true).where()
							.eq("time", MyDateTools.getCurDate()).query();

					if (historyTrajectoryVOlist != null
							&&numer!=0
							&&numer!=historyTrajectoryVOlist.size()) {
						numer=historyTrajectoryVOlist.size();
						drawGraphLoctionUtil.drawTrajectory(
								historyTrajectoryVOlist,
								Color.argb(70, 215, 15, 15));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}
		
	};
	
	private class ButtonOnClick implements DialogInterface.OnClickListener {
		int index;

		public ButtonOnClick(int index) {
			index = index;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
			if (which >= 0) {
				// 如果单击的是列表项，将当前列表项的索引保存在index中。
				// 如果想单击列表项后关闭对话框，可在此处调用dialog.cancel()
				// 或是用dialog.dismiss()方法。
				index = which;
			} else {
				// 用户单击的是【确定】按钮
				if (which == DialogInterface.BUTTON_NEGATIVE) {
					timer.cancel();
					if (index == 0) {  //0暂停
						if (null != chkDataOp){
							chkDataOp.speObjOp(insPatrolOnsiteRecordVO, 2, "");
						}
						
						if (null != dataOp){
							dataOp.speObjOp(insPatrolOnsiteRecordVO, 2, "");
						}
					} 

					//index=1直接结束,无需暂停操作;如果是暂停还需要结束任务,因为已经到了退出阶段
					if (null != chkDataOp){
						chkDataOp.speObjOp(insPatrolOnsiteRecordVO, 7, "");
						chkDataOp.end();
					}
					
					if (null != dataOp){
						dataOp.speObjOp(insPatrolOnsiteRecordVO, 7, "");
						dataOp.end();
					}
						
					finish();
				} else if (which == DialogInterface.BUTTON_POSITIVE) {// 用户单击的是【取消】按钮
					return;

				}
			}
		}
	}
	private void spText(final String text){
		if((!isPause)&&(!isWriteTable)){
			/*new Thread(new Runnable() {
				
				@Override
				public void run() {*/
					// TODO Auto-generated method stub
//					BNTTSPlayer.playTTSText(text, -1);
				/*}
			}).start();*/
		}
	}

	public List<InsCheckFacRoad> getInsCheckFacRoadList() {
		return insCheckFacRoadList;
	}

	public void setInsCheckFacRoadList(List<InsCheckFacRoad> insCheckFacRoadList) {
		this.insCheckFacRoadList = insCheckFacRoadList;
	}
	public void selectData(){
		if(selectRoadList.get(0).getFacType().equals("阀门")){
			Toast.makeText(ShowPatrolDataExpActivity.this, "阀门"+selectRoadList.get(0).getFacNum(), Toast.LENGTH_SHORT).show();
		}else{
			String[] items = {"影响度因素","管线周边情况"};
			AlertDialog.Builder vDialog = new AlertDialog.Builder(this);
			vDialog.setTitle("管线"+selectRoadList.get(0).getFacNum());
			vDialog.setItems( items , new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			vDialog.show();
			Toast.makeText(ShowPatrolDataExpActivity.this, "管线"+selectRoadList.get(0).getFacNum(), Toast.LENGTH_SHORT).show();
		}
	}

	public void showList() {
		LinearLayout v = (LinearLayout) View.inflate(ShowPatrolDataExpActivity.this,
				R.layout.troubleshoot_list, null);
		troubleshoot_btn_ll = (RelativeLayout) v.findViewById(R.id.troubleshoot_btn_ll);//总框
		ListView troubleshoot_listview =(ListView) v.findViewById(R.id.troubleshoot_listview);//Listview
		troubleshoot_shouqi_btn = (Button) v.findViewById(R.id.troubleshoot_shouqi_btn);//收起任务
		Button troubleshoot_tijiao_btn = (Button) v.findViewById(R.id.troubleshoot_tijiao_btn);//提交任务
		troubleshoot_tijiao_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String canshu = "";
				for (int i = 0; i < insCheckFacRoadLoad.size(); i++) {
					if(insCheckFacRoadLoad.size()==i+1){
						canshu+=insCheckFacRoadLoad.get(i).getId();
					}else{
						canshu+=insCheckFacRoadLoad.get(i).getId()+",";
					}
				}
				String downloadType2 = "";
				if(insCheckFacRoadLoad.get(0).getFacType().equals("阀门")){
					downloadType2 = "valve";
				}else{
					downloadType2 = "pipe";
				}
				RegionAnalyzeTask2 regionAnalyzeTask2 = new RegionAnalyzeTask2(ShowPatrolDataExpActivity.this,sHandler,canshu,insTablePushTaskVo.getTaskNum(),downloadType2);
		        regionAnalyzeTask2.execute("");	
			
			}
		});
		troubleshoot_shouqi_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(troubleshoot_shouqi_btn.getText().equals("收起")){
					troubleshoot_shouqi_btn.setText("展开");
					troubleshoot_btn_ll.setVisibility(View.GONE);
				}else {
					troubleshoot_shouqi_btn.setText("收起");
					troubleshoot_btn_ll.setVisibility(View.VISIBLE);
				}
			}
		});
		
		insCheckFacRoadLoad = new ArrayList<InsCheckFacRoad>();
		try {
			Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			
			List<InsCheckFacRoad> insPatrolAreaDataList = insCheckFacRoadDao.queryForEq("workTaskNum", insTablePushTaskVo.getTaskNum());
			for (int i = 0; i < insPatrolAreaDataList.size(); i++) {
				if(insPatrolAreaDataList.get(i).getState()==null||insPatrolAreaDataList.get(i).getState()==0||insPatrolAreaDataList.get(i).getState()==1){
					insCheckFacRoadLoad.add(insPatrolAreaDataList.get(i));
				}
			}
			for(String geo :dataOp.getThisTimeDataWtsGeo()){
				drawGraphLoctionUtil.drawGraphLoctionMeger(geo,Color.YELLOW);//等巡路段
				
			}
			
			for(String geo :dataOp.getHadDoDataGeo()){
				drawGraphLoctionUtil.drawGraphLoctionMeger(geo,Color.GREEN);//已巡路段
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
			
		bgfxCursorAdapter=new BgfxCursorAdapter(ShowPatrolDataExpActivity.this,insCheckFacRoadLoad);
		troubleshoot_listview.setAdapter(bgfxCursorAdapter);
		
		insMapView.getHeaderContainer().removeAllViews();

		insMapView.getHeaderContainer().addView(v);
	}
	
	private class BgfxCursorAdapter extends BaseAdapter{
		private Context context;
		private List<InsCheckFacRoad> result;

		public BgfxCursorAdapter(Context context, List<InsCheckFacRoad> result){
			this.context=context;
			this.result=result;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return result.size();
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return result.get(arg0);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			 if (convertView == null) {
				 convertView = View.inflate(context, R.layout.troubleshoot_item2, null);
		            TextView trouble_tv = (TextView) convertView.findViewById(R.id.trouble_tv);
		            Button trouble_btn_down = (Button) convertView.findViewById(R.id.trouble_btn_down);
		            trouble_btn_down.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(result.get(position).getAndroidForm().equals("提交")){
								new AlertDialog.Builder(context).setTitle("提示")
								.setIcon(android.R.drawable.ic_menu_help)
								.setMessage("是否提交该设施？")
								.setPositiveButton("取消", new DialogInterface.OnClickListener() {
	
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								})
								.setNegativeButton("确定", new DialogInterface.OnClickListener() {
	
									@Override
									public void onClick(DialogInterface dialog, int which) {
										String ids2 = String.valueOf(result.get(position).getId());
										String downloadType = "";
										if(String.valueOf(result.get(position).getFacType()).equals("阀门")){
											downloadType = "valve";
										}else{
											downloadType = "pipe";
										}
										RegionAnalyzeTask2 regionAnalyzeTask2 = new RegionAnalyzeTask2(ShowPatrolDataExpActivity.this,sHandler,ids2,insTablePushTaskVo.getTaskNum(),downloadType);
								        regionAnalyzeTask2.execute("");	
									}
								}).show();
							}else{
								Toast.makeText(ShowPatrolDataExpActivity.this, "请提交数据后,再提交设施。", Toast.LENGTH_SHORT).show();
								}
							}
						});
		            
		            Button trouble_btn_reture = (Button) convertView.findViewById(R.id.trouble_btn_reture);
		            trouble_btn_reture.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String dd = "";
							final int num = position;
							try {
								final Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
										.getInstance().getAppDbHelper()
										.getDao(InsCheckFacRoad.class);
								
								List<InsCheckFacRoad> insPatrolAreaDataList = insCheckFacRoadDao.queryForEq("workTaskNum", insTablePushTaskVo.getTaskNum());
								for (int j = 0; j < insPatrolAreaDataList.size(); j++) {
									if(insPatrolAreaDataList.get(j).getId().equals(result.get(num).getId())){new AlertDialog.Builder(context).setTitle("提示")
										.setIcon(android.R.drawable.ic_menu_help)
										.setMessage("是否移除该设施？")
										.setPositiveButton("取消", new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
												// TODO Auto-generated method stub
												dialog.dismiss();
											}
										})
										.setNegativeButton("确定", new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {
												try {
													insCheckFacRoadDao.delete(result.get(num));
												} catch (SQLException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												drawGraphLoctionUtil.clearGraphy();
												showList();
											}
										}).show();
									}
								}
							} catch (Exception e) {
								// TODO: handle exception
								e.printStackTrace();
							}
						}
					});
		        //显示内容
		        trouble_tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						HashMap<String, String> params1 = new HashMap<String, String>();
						List<Module> taskModule2=new ArrayList<Module>();
						for(Module m:lstModule){
							String mId=result.get(position).getAndroidForm();
							if(mId!=null){
								String []mIds=mId.split(",");
								for(int i=0;i<mIds.length;++i){
									if(mIds[i].equals(m.getId())){
										taskModule2.add(m);
									}
								}
							}
						}
						params1.put("workTaskNum", insTablePushTaskVo.getTaskNum());
						params1.put("pipeId", result.get(position).getFacNum());
			            if ((dataOp.dataType() == DataType.Pl)&&(null != dataOp.getCurTaskLuDuan())){
			            		params1.put("sections", dataOp.getCurTaskLuDuan().getRoadName());
			    				params1.put("sectionsNum", dataOp.getCurTaskLuDuan().getRoadNum());
			    				showModuleDialog(taskModule2, params1,
			    						result.get(position).getFacNum());
			            }else if ((dataOp.dataType() != DataType.Pl)&&(null != dataOp.getCurentNearObj())){
			            	params1.put("sections", dataOp.getCurentNearObj().getObjName());
							params1.put("sectionsNum", dataOp.getCurentNearObj().getObjNum());
							showModuleDialog(taskModule2, params1,
									dataOp.getCurentNearObj().getObjId());
			            }else{
			            	showModuleDialog(taskModule2, params1,
			            			result.get(position).getFacNum());
			            }
					}
				});
		        trouble_tv.setTextColor(Color.BLUE);
		        trouble_tv.setText(result.get(position).getFacNum());
		        
		        
			 }
			 return convertView;
		}
	}
	
	public List<InsCheckFacRoad> getAllItems() {
        return this.insCheckFacRoadList;
    }

	public void setSelectRoadList(List<InsCheckFacRoad> selectRoadList) {
		this.selectRoadList = selectRoadList;
	}

	public MyMapView getInsMapView() {
		return insMapView;
	}

	public void setInsMapView(MyMapView insMapView) {
		this.insMapView = insMapView;
	}

	public void showList2(Object object) {
		data_model.setText("地图");
		dspModel=0;
		patrolListModel.setVisibility(View.VISIBLE);
		patrolMapModel.setVisibility(View.GONE);
	}

}
