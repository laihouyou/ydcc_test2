package com.movementinsome.app.mytask;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Point;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.dataop.DataOperaterFactory;
import com.movementinsome.app.dataop.IDataOperater;
import com.movementinsome.app.dataop.IDataOperater.DataType;
import com.movementinsome.app.mytask.adapter.PlanWorkAdapter;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.map.utils.DrawGraphLoctionUtil;
import com.movementinsome.map.utils.MapUtil;
import com.movementinsome.map.view.MyMapView;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class ShowPatrolDataActivity extends FullActivity implements
		OnClickListener, OnInitListener {

	private ImageView show_patrol_data_back;// 返回

	private TextView show_yx_title;// 已巡
	private TextView show_wx_title;// 未巡
	private TextView show_qdd_title;// 簽到點
	private TextView show_search_title;// 搜索
	private TextView show_nearby_title;// 附近列表
	
	private LinearLayout show_search_ll;// 搜索容器
	private EditText show_search_num;// 搜索输入
	private Button show_search_btn;// 搜索按钮
	private Button show_search_code;// 二维码

	private final String TYPE_YX = "type_yx";// 已巡
	private final String TYPE_WX = "type_wx";// 未巡
	private final String TYPE_QDD = "type_qdd";// 簽到點
	private final String TYPE_SEARCH = "TYPE_SEARCH";// 搜索
	private final String TYPE_NEARBY = "TYPE_NEARBY";// 附近
	private String type = TYPE_WX;

	private ListView show_patrol_data_wx;// 巡查列表

	private String guid;
	private Button show_patrol_data_write;// 填单

	private List<Module> lstModule;// 所有表单配置
	private List<Module> taskModule;// 当前任务表单配置
	private InsTablePushTaskVo insTablePushTaskVo;  //当前巡查任务
	private PlanWorkAdapter planWorkAdapter; //详细内容显示适配器
	
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
	
	private LinearLayout show_work_pause_ll;// 暂停巡检容器
	private LinearLayout show_work_pause_ll_lyx;
	private Button show_work_pause;// 暂停巡检按钮
	private TextView show_tv_pause;// 暂停字体
	private TextView show_work_pause_tv;// 
	
	private LinearLayout patrolListModel;//列表模式容器
	private LinearLayout patrolMapModel; //地图模式容器
	private Button data_model;
	private boolean isShowMap;// 是否显示地图
	private boolean isShowSearch;
	private boolean isShowNearby;

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
	
	
	private boolean isShowReport;

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
					||"biz.hidden_trouble_form_zs".equals(module.getId())
					||"biz.hidden_danger_report2_zs".equals(module.getId())){
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
		
		//初始巡查列表内容
		updateList(type);
		
		isFirst = false;
		drawGraphLoctionUtil =new DrawGraphLoctionUtil(insMapView,  this);
		
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
						//已巡过的路段
						for(String geo :dataOp.getHadDoDataGeo()){
							drawGraphLoctionUtil.drawGraphLoctionMeger(geo,Color.GREEN);//已巡路段
							
						}
						//绘制当前正在巡检路段
						if ((null != insPatrolOnsiteRecordVO)&&(insPatrolOnsiteRecordVO.getEnterState()==1)){
							drawGraphLoctionUtil.drawGraphLoctionMeger(insPatrolOnsiteRecordVO.getWktGeom(),Color.rgb(152, 251, 152));//正在巡路段
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
				}, 0, 1000);
				
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
						null, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setPdList(dataOp.getHadDoDataList());
				planWorkAdapter.notifyDataSetChanged();
			}			
		} else if (TYPE_WX.equals(type)) {// 未巡
			show_patrol_data_wx.setVisibility(View.VISIBLE);
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this, dataOp.getWouldDoDataList(), null, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setPdList(dataOp.getWouldDoDataList());
				planWorkAdapter.notifyDataSetChanged();
			}
		} else if (TYPE_QDD.equals(type)) {// 簽到
			show_patrol_data_wx.setVisibility(View.VISIBLE);
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this, chkDataOp.getHadDoDataList(),
						null, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setPdList(chkDataOp.getHadDoDataList());
				planWorkAdapter.notifyDataSetChanged();
			}
		} else if(TYPE_SEARCH.equals(type)){
			show_patrol_data_wx.setVisibility(View.VISIBLE);
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this, dataOp.getSearchDataList(), null, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setPdList(dataOp.getSearchDataList());
				planWorkAdapter.notifyDataSetChanged();
			}
		} else if(TYPE_NEARBY.equals(type)){
			show_patrol_data_wx.setVisibility(View.VISIBLE);
			if(planWorkAdapter==null){
				planWorkAdapter = new PlanWorkAdapter(this, dataOp.getNearbyDataList(), null, insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				planWorkAdapter.setPdList(dataOp.getNearbyDataList());
				planWorkAdapter.notifyDataSetChanged();
			}
		}
	}

	private void init() {
		
		isShowReport = getIntent().getBooleanExtra("isShowReport", true);
		show_work_pause_ll = (LinearLayout) findViewById(R.id.show_work_pause_ll);// 暂停巡检容器
		show_work_pause_ll_lyx = (LinearLayout) findViewById(R.id.show_work_pause_ll_lyx);
		
		show_work_pause = (Button) findViewById(R.id.show_work_pause);// 暂停巡检按钮
		show_work_pause.setOnClickListener(this);
		show_tv_pause = (TextView) findViewById(R.id.show_tv_pause);// 暂停字体
		show_work_pause_tv = (TextView) findViewById(R.id.show_work_pause_tv);
		
		show_patrol_data_back = (ImageView) findViewById(R.id.show_patrol_data_back);// 返回
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
		
		patrolListModel = (LinearLayout) findViewById(R.id.patrolListModel);
		patrolListModel.setVisibility(View.VISIBLE);
		patrolMapModel = (LinearLayout) findViewById(R.id.patrolMapModel);
		patrolMapModel.setVisibility(View.GONE);
		
		data_model = (Button) findViewById(R.id.data_model);// 显示模式
		data_model.setOnClickListener(this);
		// 控件控制
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

	private void updateWork(InsPatrolOnsiteRecordExtVO tempPatrolOnsiteRecordVO) {
		boolean needSpeed = false;
		//insPatrolOnsiteRecordVO = tempPatrolOnsiteRecordVO;
		if (tempPatrolOnsiteRecordVO != null&& null != tempPatrolOnsiteRecordVO.getId()) { //有处理的巡查路段
			synchronized(tempPatrolOnsiteRecordVO){
				//if (null == preInsPatrolOnsiteRecordVO){
/*				if (null != preInsPatrolOnsiteRecordVO){
					if (preInsPatrolOnsiteRecordVO.getRoadName().equals(tempPatrolOnsiteRecordVO.getRoadName())){
						preInsPatrolOnsiteRecordVO = insPatrolOnsiteRecordVO;
					}
				}else if ((null !=insPatrolOnsiteRecordVO)&& !insPatrolOnsiteRecordVO.getRoadName().equals(tempPatrolOnsiteRecordVO.getRoadName())){
					preInsPatrolOnsiteRecordVO = insPatrolOnsiteRecordVO;
				}*/
				insPatrolOnsiteRecordVO = tempPatrolOnsiteRecordVO;
				//}
				
				if (!insPatrolOnsiteRecordVO.getWarnState().equals("3")){
					show_work_pause.setBackgroundResource(R.drawable.xj_pause);
					show_tv_pause.setText("暂停");
					show_work_pause_tv.setText("点击暂停巡检：");
				}else{
					show_work_pause.setBackgroundResource(R.drawable.xj_stark);
					show_tv_pause.setText("继续");
					show_work_pause_tv.setText("点击继续巡检：");
				}
				show_work_pause_ll.setVisibility(View.GONE);//暂停控制容器不显示
				show_work_pause_ll_lyx.setVisibility(View.GONE);
				
				if (insPatrolOnsiteRecordVO.getWarnState().equals("1")) {  //正常状态
					
					if (insPatrolOnsiteRecordVO.getEnterState() == 0) {  //未进入路段
						show_work_message.setText("您距离:"
								+ insPatrolOnsiteRecordVO.getRoadName() + "大约还有"
								+ (int) insPatrolOnsiteRecordVO.getMinDistance() + "米");
						show_work_pause_ll.setVisibility(View.VISIBLE);
						show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					} else if (insPatrolOnsiteRecordVO.getEnterState() == 1) {  //已进入路段
						show_work_pause_ll.setVisibility(View.VISIBLE);
						show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
						Long insCount = insPatrolOnsiteRecordVO.getInsCount();
						insCount = insCount == null ? 0 : insCount;
						show_work_content.setVisibility(View.VISIBLE);
						show_work_content.setText("巡查开始时间:"
								+ insPatrolOnsiteRecordVO.getPatrolStartDate());
						
						if("".equals(insPatrolOnsiteRecordVO.getPreRoadName())){
							show_work_message.setText("您已在:"
									+ insPatrolOnsiteRecordVO.getRoadName());
							show_work_pause_ll.setVisibility(View.VISIBLE);
							show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
							if (dspModel == 1){
								drawGraphLoctionUtil.drawGraphLoctionMeger(insPatrolOnsiteRecordVO.getWktGeom(),Color.rgb(152, 251, 152));//正在巡路段
								if (!timer_start){
									doInsStart();
								}
								/*String[] xy = insPatrolOnsiteRecordVO.getPatrolStartCoordinate().split("\\ ");
								zoomToPositon(Double.parseDouble(xy[0]),Double.parseDouble(xy[1]));*/
							}
						}else if (!insPatrolOnsiteRecordVO.getPreRoadName().equals(insPatrolOnsiteRecordVO.getRoadName())){
							show_work_message.setText("您已离开:"
									+ insPatrolOnsiteRecordVO.getPreRoadName() + "进入:"
									+ insPatrolOnsiteRecordVO.getRoadName());
							show_work_pause_ll.setVisibility(View.VISIBLE);	
							show_work_pause_ll_lyx.setVisibility(View.VISIBLE);	
							if (dspModel == 1){
								//drawGraphLoctionUtil.drawGraphLoctionMeger(preInsPatrolOnsiteRecordVO.getWktGeom(),Color.GREEN);//已离开路段
								drawGraphLoctionUtil.drawGraphLoctionMeger(insPatrolOnsiteRecordVO.getWktGeom(),Color.rgb(152, 251, 152));//正在巡路段
								if (!timer_start){
									doInsStart();
								}
								/*String[] xy = insPatrolOnsiteRecordVO.getPatrolStartCoordinate().split("\\ ");
								zoomToPositon(Double.parseDouble(xy[0]),Double.parseDouble(xy[1]));*/
							}
						}
					}else if (insPatrolOnsiteRecordVO.getEnterState() == 2) {  //进入状态为2时，已经离开该路段
					 
						show_work_message.setText("您已离开:" + insPatrolOnsiteRecordVO.getRoadName());
						show_work_pause_ll.setVisibility(View.GONE);
						show_work_pause_ll_lyx.setVisibility(View.GONE);
						if (dspModel == 1){
							drawGraphLoctionUtil.drawGraphLoctionMeger(insPatrolOnsiteRecordVO.getWktGeom(),Color.GREEN);//已离开路段
							//drawGraphLoctionUtil.drawGraphLoctionMeger(insPatrolOnsiteRecordVO.getWktGeom(),Color.rgb(152, 251, 152));//正在巡路段
							if (!timer_start){
								doInsStart();
							}
							/*String[] xy = insPatrolOnsiteRecordVO.getPatrolStartCoordinate().split("\\ ");
							zoomToPositon(Double.parseDouble(xy[0]),Double.parseDouble(xy[1]));*/
						}
					}else if (insPatrolOnsiteRecordVO.getEnterState() == 3){//未进入路段
						show_work_message.setText("您未进入:" + insPatrolOnsiteRecordVO.getRoadName());
						show_work_pause_ll.setVisibility(View.GONE);
						show_work_pause_ll_lyx.setVisibility(View.GONE);
					}
				}else if (insPatrolOnsiteRecordVO.getWarnState().equals("2")) {  //偏离警告
					if (insPatrolOnsiteRecordVO.getEnterState() == 1){
						show_work_message.setText("您已偏离:"
								+ insPatrolOnsiteRecordVO.getRoadName()+
								+ (int) insPatrolOnsiteRecordVO.getMinDistance() + "米"
								+ "请回到有效范围");
						show_work_pause_ll.setVisibility(View.VISIBLE);
						show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
					}else{
						show_work_message.setText("您已偏离:"
								+ insPatrolOnsiteRecordVO.getRoadName()+"该路段本次巡查已被结束");
						show_work_pause_ll.setVisibility(View.GONE);
						show_work_pause_ll_lyx.setVisibility(View.GONE);
						
						if (dspModel == 1){
							drawGraphLoctionUtil.drawGraphLoctionMeger(insPatrolOnsiteRecordVO.getWktGeom(),Color.RED);//偏离路段,红色警告
							if (!timer_start){
								doInsStart();
							}
						}
					}
				}else if (insPatrolOnsiteRecordVO.getWarnState().equals("3")){ //被暂停的路段
					show_work_message.setText(insPatrolOnsiteRecordVO.getRoadName()+"已被暂停,如需执行请点‘继续’");
					show_work_pause_ll.setVisibility(View.VISIBLE);
					show_work_pause_ll_lyx.setVisibility(View.VISIBLE);
				}

				if (null ==insPatrolOnsiteRecordVO){
					needSpeed = roadTTsMemo.needSpeed("","00");
				}else{
					needSpeed = roadTTsMemo.needSpeed(insPatrolOnsiteRecordVO.getRoadName(),
							String.valueOf(insPatrolOnsiteRecordVO.getWarnState())+String.valueOf(insPatrolOnsiteRecordVO.getEnterState()));
				}				
			}
		} else{
			show_work_pause_ll.setVisibility(View.GONE);//暂停控制容器不显示
			show_work_pause_ll_lyx.setVisibility(View.GONE);
			if ((null != tempPatrolOnsiteRecordVO)&&(tempPatrolOnsiteRecordVO.getWarnState().equals("4"))&&(insPatrolOnsiteRecordVO != null)){
				show_work_message.setText("当次计划路段您已巡查完毕");
			}else if (null == tempPatrolOnsiteRecordVO && insPatrolOnsiteRecordVO == null){
				show_work_message.setText("温情提示：您还没进入巡查区域");
			}else if (null == tempPatrolOnsiteRecordVO && insPatrolOnsiteRecordVO == null){
				show_work_message.setText(insPatrolOnsiteRecordVO.getRoadNum());
			}
			
			needSpeed = roadTTsMemo.needSpeed(show_work_message.getText().toString(),"00");
			
		}

		updateList(type);
		
		if (needSpeed){
			showVibrator(this);
//			BNTTSPlayer.playTTSText(show_work_message.getText().toString(), -1);
		}
	}


	private void updateWorkOth(NearObject nearObject) {
		
		if (nearObject != null) {
			synchronized(nearObject){
				//show_qdd_msg_rl.setVisibility(View.VISIBLE);
				if (null != nearObject.getObjName()){
					show_work_message.setText("距离" + nearObject.getObjName() + "大约还有："
							+ (int) nearObject.getMinDistance() + "米");
					
					if (dspModel == 1){
						drawGraphLoctionUtil.drawGraphLoctionMeger(nearObject.getWktGeom(),Color.YELLOW);//等巡路段
						if (!timer_start){
							doInsStart();
						}
						
						/*String[] xy = nearObject.getMapXY().split("\\ ");
						zoomToPositon(Double.parseDouble(xy[0]),Double.parseDouble(xy[1]));*/
					}
										
					boolean needSpeed = chkPntTTsMemo.needSpeed(nearObject.getObjNum(),"00");
					/*if (null ==insPatrolOnsiteRecordVO){
						needSpeed = chkPntTTsMemo.needSpeed("",0);
					}else{
						needSpeed = chkPntTTsMemo.needSpeed(nearObject.getObjNum(),0);
					}*/
					if (needSpeed){
						//BNTTSPlayer.initPlayer();    
//						BNTTSPlayer.playTTSText(show_work_message.getText().toString(), -1);
					}
						/*tts.speak(show_work_message.getText().toString(),
								TextToSpeech.QUEUE_ADD, null);*/
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
						
						/*String[] xy = nearObject.getMapXY().split("\\ ");
						zoomToPositon(Double.parseDouble(xy[0]),Double.parseDouble(xy[1]));*/
					}
				}
				show_work_message.setText(text);
//				BNTTSPlayer.playTTSText(show_work_message.getText().toString(), -1);
				/*tts.speak(show_work_message.getText().toString(),
							TextToSpeech.QUEUE_ADD, null);*/
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
						/*String[] xy = insPatrolOnsiteRecordVO.getPatrolEndCoordinate().split("\\ ");
						zoomToPositon(Double.parseDouble(xy[0]),Double.parseDouble(xy[1]));*/
					}
										
					boolean needSpeed = chkPntTTsMemo.needSpeed(nearObject.getObjNum(),"00");
					/*if (null ==insPatrolOnsiteRecordVO){
						needSpeed = chkPntTTsMemo.needSpeed("",0);
					}else{
						needSpeed = chkPntTTsMemo.needSpeed(nearObject.getObjNum(),0);
					}*/
					if (needSpeed){
						//BNTTSPlayer.initPlayer();    
//						BNTTSPlayer.playTTSText(show_qdd_msg.getText().toString(), -1);
					}
						/*tts.speak(show_qdd_msg.getText().toString(),
								TextToSpeech.QUEUE_ADD, null);*/
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
			show_search_ll.setVisibility(View.GONE);
			show_wx_title.setBackgroundResource(R.drawable.tv_green_bg);
			show_yx_title.setBackgroundResource(0);
			show_qdd_title.setBackgroundResource(0);
			show_search_title.setBackgroundResource(0);
			show_nearby_title.setBackgroundResource(0);
			
			show_wx_title.setTextColor(Color.parseColor("#3a5fcd"));
			show_yx_title.setTextColor(Color.parseColor("#737373"));
			show_qdd_title.setTextColor(Color.parseColor("#737373"));
			show_search_title.setTextColor(Color.parseColor("#737373"));
			show_nearby_title.setTextColor(Color.parseColor("#737373"));
			
			type = TYPE_WX;
			updateList(type);
			break;
		case R.id.show_yx_title:// 已巡
			show_search_ll.setVisibility(View.GONE);
			show_wx_title.setBackgroundResource(0);
			show_yx_title.setBackgroundResource(R.drawable.tv_green_bg);
			show_qdd_title.setBackgroundResource(0);
			show_search_title.setBackgroundResource(0);
			show_nearby_title.setBackgroundResource(0);
			
			show_wx_title.setTextColor(Color.parseColor("#737373"));
			show_yx_title.setTextColor(Color.parseColor("#3a5fcd"));
			show_qdd_title.setTextColor(Color.parseColor("#737373"));
			show_search_title.setTextColor(Color.parseColor("#737373"));
			show_nearby_title.setTextColor(Color.parseColor("#737373"));
			
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
			
			show_wx_title.setTextColor(Color.parseColor("#737373"));
			show_yx_title.setTextColor(Color.parseColor("#737373"));
			show_search_title.setTextColor(Color.parseColor("#737373"));
			show_qdd_title.setTextColor(Color.parseColor("#3a5fcd"));
			show_nearby_title.setTextColor(Color.parseColor("#737373"));
			type = TYPE_QDD;
			updateList(type);
			break;
		case R.id.show_work_arrive:// 签到
			//chkPntdialog();
			showDialog(chkDataOp);
			break;
		case R.id.show_patrol_data_back:
			//finish();
			befClose();
			break;
		case R.id.show_patrol_data_write:
			HashMap<String, String> params1 = new HashMap<String, String>();
			//params1.put("taskNum", insTablePushTaskVo.getTaskNum());

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
			
			show_wx_title.setTextColor(Color.parseColor("#737373"));
			show_yx_title.setTextColor(Color.parseColor("#737373"));
			show_search_title.setTextColor(Color.parseColor("#3a5fcd"));
			show_qdd_title.setTextColor(Color.parseColor("#737373"));
			show_nearby_title.setTextColor(Color.parseColor("#737373"));
			
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
			
			show_wx_title.setTextColor(Color.parseColor("#737373"));
			show_yx_title.setTextColor(Color.parseColor("#737373"));
			show_search_title.setTextColor(Color.parseColor("#737373"));
			show_qdd_title.setTextColor(Color.parseColor("#737373"));
			show_nearby_title.setTextColor(Color.parseColor("#3a5fcd"));
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
//			String s= show_work_pause.getText()+"";
			String s= show_tv_pause.getText()+"";
			if("暂停".equals(s)){
				show_work_pause.setBackgroundResource(R.drawable.xj_stark);
				show_tv_pause.setText("继续");
				show_work_pause_tv.setText("点击继续巡检：");
				//dataOp.pause(insPatrolOnsiteRecordVO);
			}else{
				show_work_pause.setBackgroundResource(R.drawable.xj_pause);
				show_tv_pause.setText("暂停");
				show_work_pause_tv.setText("点击暂停巡检：");
				//dataOp.goNo(insPatrolOnsiteRecordVO);
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
		
//		String s= show_work_pause.getText()+"";
		String s= show_tv_pause.getText()+"";

		if (show_work_pause_ll.getVisibility() == View.VISIBLE){
			if ("暂停".equals(s)){
				//当前路段不为空&&已经进入到该路段
				if ((null != insPatrolOnsiteRecordVO)&&(insPatrolOnsiteRecordVO.getEnterState()==1)){
					
					//是否暂停巡检，还是结束该路段巡检？
					
					ButtonOnClick buttonOnClick = new ButtonOnClick(0);
		
					new AlertDialog.Builder(ShowPatrolDataActivity.this).setTitle("方式选择")
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
				ShowPatrolDataActivity.this, false, false,
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
		if (taskModule.size() == 1) {
			if (insTablePushTaskVo != null) {
				TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
						ShowPatrolDataActivity.this, false, false,
						insTablePushTaskVo.getTaskNum(),
						Constant.UPLOAD_STATUS_WORK, null,
						insTablePushTaskVo.getTitle(),
						insTablePushTaskVo.getTaskCategory(), null, null, null);
				taskFeedBackAsyncTask.execute();
				Intent newFormInfo = new Intent(ShowPatrolDataActivity.this,
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
							ShowPatrolDataActivity.this, false, false,
							insTablePushTaskVo.getTaskNum(),
							Constant.UPLOAD_STATUS_WORK, null,
							insTablePushTaskVo.getTitle(), insTablePushTaskVo
									.getTaskCategory(), null, null, null);
					taskFeedBackAsyncTask.execute();
					Intent newFormInfo = new Intent(
							ShowPatrolDataActivity.this, RunForm.class);
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
		if (Constant.FROM_REQUEST_CODE == requestCode && data != null
				&& Constant.FROM_RESULT_CODE == resultCode) {
			dataOp.callback(data);
			//dataOp.refreshThisTimeData();
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
				num = iNum;
				return true;
			}else if ("".equals(num)||num.equals(iNum)&&!state.equals(iState)){
				state = iState;
				num = iNum;
				return true;
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
				 insMapView.getCallout().hide();
				 Point facPoint = null;
				 Point peoplePoint = null;
				 if(nearObject!=null){
					 facPoint = nearObject.getCoordinate();
					 TextView tv=new TextView(ShowPatrolDataActivity.this);
					 tv.setText("距离"+(int)nearObject.getMinDistance()+"米");
					 insMapView.getCallout().show(facPoint,tv);
				 }
				if (AppContext.getInstance().getCurLocation()!=null){
					double x=AppContext.getInstance().getCurLocation().getMapx();
					double y=AppContext.getInstance().getCurLocation().getMapy();
					peoplePoint = new Point(x, y);
					drawGraphLoctionUtil.drawIconLoction(peoplePoint, R.drawable.map_loc_people);
					//当人坐标不在当前地图视图时按当前缩放比例居中显示
					if (!MapUtil.inBound(insMapView,peoplePoint)){
						insMapView.zoomToScale(peoplePoint, insMapView.getScale());
					}
					//补充当小人位置不在当前显示视图时，自动居中显示
					
					//巡检覆盖范围
					/*if (insPatrolOnsiteRecordVO != null){
						Polyline pl = drawGraphLoctionUtil.runTrace(peoplePoint);
						if (!pl.isEmpty()){
							drawGraphLoctionUtil.drawRunTraceBound(pl, AppContext.getInstance().getNavigation().getArrive());
						};
					}*/
				}
				if(facPoint!=null&&peoplePoint!=null){
					drawGraphLoctionUtil.drawLine(peoplePoint, facPoint, Color.BLACK);
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
							//chkDataOp.pause(insPatrolOnsiteRecordVO);
						}
						
						if (null != dataOp){
							//dataOp.pause(insPatrolOnsiteRecordVO);
						}
					} 

					//index=1直接结束,无需暂停操作;如果是暂停还需要结束任务,因为已经到了退出阶段
					if (null != chkDataOp){
						chkDataOp.end();
					}
					
					if (null != dataOp){
						dataOp.end();
					}
						
					finish();
				} else if (which == DialogInterface.BUTTON_POSITIVE) {// 用户单击的是【取消】按钮
					return;

				}
			}
		}
	}
	

}
