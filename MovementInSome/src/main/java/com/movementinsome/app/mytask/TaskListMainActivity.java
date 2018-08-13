package com.movementinsome.app.mytask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.mytask.adapter.TaskListAdapter;
import com.movementinsome.app.mytask.adapter.TaskListAdapter2;
import com.movementinsome.app.mytask.adapter.TaskListCentreAdapter3;
import com.movementinsome.app.mytask.handle.MainViewDataBaseHandle;
import com.movementinsome.app.mytask.handle.MainViewDataHandle;
import com.movementinsome.app.mytask.handle.TaskHandle;
import com.movementinsome.app.mytask.handle.WsMainViewDataHandle;
import com.movementinsome.app.mytask.handle.XJYSMainViewDataHandle;
import com.movementinsome.app.mytask.handle.YHMainViewDataHandle;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.initial.model.Module;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 总任务列表显示
 * 
 * @author gddst
 * 
 */
public class TaskListMainActivity extends ContainActivity implements
		OnClickListener {

	private ExpandableListView tlcExListView;
	private ListView tlcListview;
	private TextView tlcTvtitle;

	private String type = Constant.CYCLE_PLAN;

	private Button tlcYWLX;// 业务类型
	private Button tlcSelectTask;// 选择任务
	private String[] tlcYWLXItems; // 业务类型下拉值
	private HashMap<String, String> tlcYWLXItemsKV = new HashMap();

	private LinearLayout tclLL_XJGL;// 巡检管理
	private LinearLayout tclLL_WBGL;// 清疏管理
	private LinearLayout tclLL_WXGL;// 维修管理
	private LinearLayout tclLL_HMGL;// 维修管理
	private LinearLayout tclLL_YW;// 任务管理
	private LinearLayout tclLL_YW_WS;// 任务管理(武水)
	private LinearLayout tclLL_YW_YH;// 任务管理（粤海）
	
	private TextView tclTv_NDL_YH;// 未下载
	private TextView tclTv_JH_YH;// 计划任务
	private TextView tclTv_YJ_YH; // 应急
	private TextView tclTv_YDL_YH;// 已下载

	private TextView tclTv_GWXC_WS;// 管网巡查
	private TextView tclTv_CQ_WS;// 超期任务(武水)
	private TextView tclTv_WX_WS;// 维修(武水)

	private TextView tclTv_JH;// 计划
	private TextView tclTv_YJ;// 应急

	private TextView tclTv_XJGL_JH;// 巡检管理:计划性
	private TextView tclTv_XJGL_LSX;// 巡检管理:临时性
	private TextView tclTv_XJGL_YJ;// 巡检管理:应急

	private TextView tclTv_WBGL_JHX;// 清疏管理:计划性
	private TextView tclTv_WBGL_XCWB;// 清疏管理:应急清疏

	private TextView tclTv_WXGL_JHXWX;// 维修管理:计划性维修
	private TextView tclTv_WXGL_XCQX;// 维修管理:现场抢修

	private TextView tclTv_HMGL_JHXWX;// 阀门管理:计划性维修
	private TextView tclTv_HMGL_XCQX;// 阀门管理:现场抢修

	private InputStream file = null;
	private String stask = null;
	private Gson gson = new Gson();

	private List<Module> mainModule;// 当前页面
	private boolean isStartWork = false;
	private TaskListAdapter taskListAdapter;
	private TaskListMainReceiver taskListMainReceiver;
	// 数据处理类
	private List<MainViewDataBaseHandle> mainViewDataBaseHandleList;
	private TaskHandle taskHandle;
	private String title;

	// protected SlidingMenu side_drawer;
	/** head 头部 的左侧菜单 按钮 */
	// private ImageView top_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tasklist_centre_activity);
		init();
		taskHandle=new TaskHandle(this);
		taskHandle.timingUpdatePlanList();
	}

	private void init() {
		mainViewDataBaseHandleList = new ArrayList<MainViewDataBaseHandle>();
		mainViewDataBaseHandleList.add(new MainViewDataHandle(this));
		mainViewDataBaseHandleList.add(new WsMainViewDataHandle(this));
		mainViewDataBaseHandleList.add(new XJYSMainViewDataHandle());
		mainViewDataBaseHandleList.add(new YHMainViewDataHandle(this));

		taskListMainReceiver = new TaskListMainReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION);
		registerReceiver(taskListMainReceiver, filter);//更新任务广播

		tlcYWLX = (Button) findViewById(R.id.tlcYWLX);// 业务类型
		tlcYWLX.setOnClickListener(this);

		mainModule = AppContext.getInstance().getModuleData(
				this.getMenu().getId(), true);
		if (mainModule != null) {
			tlcYWLXItems = new String[mainModule.size()];
			for (int i = 0; i < mainModule.size(); i++) {
				tlcYWLXItems[i] = mainModule.get(i).getTemplate();
				tlcYWLXItemsKV.put(tlcYWLXItems[i], mainModule.get(i).getId());
			}
		}
		// 获取控件句柄

		tclLL_YW_WS = (LinearLayout) findViewById(R.id.tclLL_YW_WS);// 任务管理(武水)

		tclTv_GWXC_WS = (TextView) findViewById(R.id.tclTv_GWXC_WS);// 计划性(武水)
		tclTv_GWXC_WS.setOnClickListener(this);
		
		tclTv_CQ_WS = (TextView) findViewById(R.id.tclTv_CQ_WS);// 超期任务(武水)
		tclTv_CQ_WS.setOnClickListener(this);
		tclTv_WX_WS = (TextView) findViewById(R.id.tclTv_WX_WS);// 维修(武水)
		tclTv_WX_WS.setOnClickListener(this);

		tlcListview = (ListView) findViewById(R.id.tlcListView);
		tlcTvtitle = (TextView) findViewById(R.id.tlcTvtitle);

		tlcExListView = (ExpandableListView) findViewById(R.id.tlcExListView);
		tlcExListView.setGroupIndicator(null);
		tlcExListView.setFadingEdgeLength(0);

		tclLL_XJGL = (LinearLayout) findViewById(R.id.tclLL_XJGL);// 巡检管理
		tclLL_WBGL = (LinearLayout) findViewById(R.id.tclLL_WBGL);// 清疏管理
		tclLL_WXGL = (LinearLayout) findViewById(R.id.tclLL_WXGL);// 巡检管理
		tclLL_HMGL = (LinearLayout) findViewById(R.id.tclLL_HMGL);// 巡检管理

		tclLL_YW = (LinearLayout) findViewById(R.id.tclLL_YW);// 任务管理

		tclTv_JH = (TextView) findViewById(R.id.tclTv_JH);// 计划
		tclTv_JH.setOnClickListener(this);
		tclTv_YJ = (TextView) findViewById(R.id.tclTv_YJ);// 应急
		tclTv_YJ.setOnClickListener(this);

		tclTv_XJGL_JH = (TextView) findViewById(R.id.tclTv_XJGL_JH);// 巡检管理:计划性
		tclTv_XJGL_JH.setOnClickListener(this);
		tclTv_XJGL_LSX = (TextView) findViewById(R.id.tclTv_XJGL_LSX);// 巡检管理:临时性
		tclTv_XJGL_LSX.setOnClickListener(this);
		tclTv_XJGL_YJ = (TextView) findViewById(R.id.tclTv_XJGL_YJ);// 巡检管理:应急
		tclTv_XJGL_YJ.setOnClickListener(this);

		tclTv_WBGL_JHX = (TextView) findViewById(R.id.tclTv_WBGL_JHX);// 清疏管理:计划性
		tclTv_WBGL_JHX.setOnClickListener(this);
		tclTv_WBGL_XCWB = (TextView) findViewById(R.id.tclTv_WBGL_XCWB);// 清疏管理:应急清疏
		tclTv_WBGL_XCWB.setOnClickListener(this);

		tclTv_WXGL_JHXWX = (TextView) findViewById(R.id.tclTv_WXGL_JHXWX);// 维修管理:计划性维修
		tclTv_WXGL_JHXWX.setOnClickListener(this);
		tclTv_WXGL_XCQX = (TextView) findViewById(R.id.tclTv_WXGL_XCQX);// 维修管理:现场抢修
		tclTv_WXGL_XCQX.setOnClickListener(this);

		tclTv_HMGL_JHXWX = (TextView) findViewById(R.id.tclTv_HMGL_JHXWX);// 阀门管理:计划性维修
		tclTv_HMGL_JHXWX.setOnClickListener(this);
		tclTv_HMGL_XCQX = (TextView) findViewById(R.id.tclTv_HMGL_XCQX);// 阀门管理:现场抢修
		tclTv_HMGL_XCQX.setOnClickListener(this);
		
		tclLL_YW_YH = (LinearLayout) findViewById(R.id.tclLL_YW_YH);// 任务管理（粤海）
		
		tclTv_NDL_YH = (TextView) findViewById(R.id.tclTv_NDL_YH);// 未下载
		tclTv_NDL_YH.setOnClickListener(this);
		tclTv_JH_YH = (TextView) findViewById(R.id.tclTv_JH_YH);// 计划任务
		tclTv_JH_YH.setOnClickListener(this);
		tclTv_YJ_YH = (TextView) findViewById(R.id.tclTv_YJ_YH); // 应急
		tclTv_YJ_YH.setOnClickListener(this);
		tclTv_YDL_YH = (TextView) findViewById(R.id.tclTv_YDL_YH);// 已下载
		tclTv_YDL_YH.setOnClickListener(this);
		
		tlcSelectTask =(Button) findViewById(R.id.tlcSelectTask);// 选择任务
		tlcSelectTask.setOnClickListener(this);
		
		if (tlcYWLXItems != null && tlcYWLXItems.length > 0) {
			tlcTvtitle.setText(tlcYWLXItems[0]);
			updateYWLX(tlcYWLXItems[0]);
			if(tlcYWLXItems.length==1){
				tlcYWLX.setVisibility(View.GONE);
			}
		} else {
			tlcYWLX.setVisibility(View.GONE);
			tclLL_XJGL.setVisibility(View.GONE);
			tclLL_YW.setVisibility(View.VISIBLE);
			type = Constant.PLAN;
			updateList(type);
		}
		if(tclLL_YW_YH.isShown()){
			tlcSelectTask.setVisibility(View.VISIBLE);
		}else{
			tlcSelectTask.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(taskListMainReceiver);
		taskHandle.shopTimer();
	}

	String inputStream2String(InputStream in) throws IOException {

		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		int n;
		while ((n = in.read(b)) != -1) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tlcYWLX:// 业务类型
			showDialogYWLX(tlcYWLX);
			break;
		case R.id.tlcSelectTask:
			showDialog(tlcSelectTask);
			break;
		case R.id.tclTv_XJGL_JH:// 巡检管理:计划性
			tclTv_XJGL_JH.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_XJGL_LSX.setBackgroundResource(0);
			tclTv_XJGL_YJ.setBackgroundResource(0);
			tclTv_XJGL_JH.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_XJGL_LSX.setTextColor(Color.parseColor("#737373"));
			tclTv_XJGL_YJ.setTextColor(Color.parseColor("#737373"));
			type = Constant.CYCLE_PLAN;
			updateList(type);
			break;
		case R.id.tclTv_XJGL_LSX:// 巡检管理:临时性
			tclTv_XJGL_JH.setBackgroundResource(0);
			tclTv_XJGL_LSX.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_XJGL_YJ.setBackgroundResource(0);
			tclTv_XJGL_JH.setTextColor(Color.parseColor("#737373"));
			tclTv_XJGL_LSX.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_XJGL_YJ.setTextColor(Color.parseColor("#737373"));
			type = Constant.INTERIM_PLAN;
			updateList(type);
			break;
		case R.id.tclTv_XJGL_YJ:// 巡检管理:应急
			tclTv_XJGL_JH.setBackgroundResource(0);
			tclTv_XJGL_LSX.setBackgroundResource(0);
			tclTv_XJGL_YJ.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_XJGL_JH.setTextColor(Color.parseColor("#737373"));
			tclTv_XJGL_LSX.setTextColor(Color.parseColor("#737373"));
			tclTv_XJGL_YJ.setTextColor(Color.parseColor("#3f63ce"));
			type = Constant.XJFL_YJ;
			updateList(type);
			break;
		case R.id.tclTv_WBGL_JHX:// 清疏管理:计划性
			tclTv_WBGL_JHX.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_WBGL_XCWB.setBackgroundResource(0);
			tclTv_WBGL_JHX.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_WBGL_XCWB.setTextColor(Color.parseColor("#737373"));
			type = Constant.QSGL_JHX;
			updateList(type);
			break;
		case R.id.tclTv_WBGL_XCWB:// 清疏管理:应急清疏
			tclTv_WBGL_JHX.setBackgroundResource(0);
			tclTv_WBGL_XCWB.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_WBGL_JHX.setTextColor(Color.parseColor("#737373"));
			tclTv_WBGL_XCWB.setTextColor(Color.parseColor("#3f63ce"));
			type = Constant.QSGL_YJQS;
			updateList(type);
			break;
		case R.id.tclTv_WXGL_JHXWX:// 维修管理:计划性维修
			tclTv_WXGL_JHXWX.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_WXGL_XCQX.setBackgroundResource(0);
			tclTv_WXGL_JHXWX.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_WXGL_XCQX.setTextColor(Color.parseColor("#737373"));
			type = Constant.WXGL_JHXWX;
			updateList(type);
			break;
		case R.id.tclTv_WXGL_XCQX:// 维修管理:现场抢修
			tclTv_WXGL_JHXWX.setBackgroundResource(0);
			tclTv_WXGL_XCQX.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_WXGL_JHXWX.setTextColor(Color.parseColor("#737373"));
			tclTv_WXGL_XCQX.setTextColor(Color.parseColor("#3f63ce"));
			type = Constant.WXGL_XCQX;
			updateList(type);
			break;
		case R.id.tclTv_HMGL_JHXWX:// 阀门管理:计划性维修
			tclTv_HMGL_JHXWX.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_HMGL_XCQX.setBackgroundResource(0);
			tclTv_HMGL_JHXWX.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_HMGL_XCQX.setTextColor(Color.parseColor("#737373"));
			type = Constant.FMGL_JHXWX;
			updateList(type);
			break;
		case R.id.tclTv_HMGL_XCQX:// 阀门管理:现场抢修
			tclTv_HMGL_JHXWX.setBackgroundResource(0);
			tclTv_HMGL_XCQX.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_HMGL_JHXWX.setTextColor(Color.parseColor("#737373"));
			tclTv_HMGL_XCQX.setTextColor(Color.parseColor("#3f63ce"));
			type = Constant.TASK_VALVE_REPAIRING;
			updateList(type);
			break;
		case R.id.tclTv_JH:
			tclTv_YJ.setBackgroundResource(0);
			tclTv_JH.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_YJ.setTextColor(Color.parseColor("#737373"));
			tclTv_JH.setTextColor(Color.parseColor("#3f63ce"));
			type = Constant.PLAN;
			updateList(type);
			break;
		case R.id.tclTv_YJ:
			tclTv_JH.setBackgroundResource(0);
			tclTv_YJ.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_JH.setTextColor(Color.parseColor("#737373"));
			tclTv_YJ.setTextColor(Color.parseColor("#3f63ce"));
			type = Constant.URGENT;
			updateList(type);
			break;
		case R.id.tclTv_GWXC_WS:// 计划性(武水)
			tclTv_CQ_WS.setBackgroundResource(0);
			tclTv_WX_WS.setBackgroundResource(0);
			tclTv_GWXC_WS.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_CQ_WS.setTextColor(Color.parseColor("#737373"));
			tclTv_WX_WS.setTextColor(Color.parseColor("#737373"));
			tclTv_GWXC_WS.setTextColor(Color.parseColor("#3f63ce"));
			type = Constant.PLAN_PATROL_WS;
			updateList(type);
			break;
		case R.id.tclTv_CQ_WS:// 超期任务(武水)
			tclTv_CQ_WS.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_WX_WS.setBackgroundResource(0);
			tclTv_GWXC_WS.setBackgroundResource(0);
			tclTv_CQ_WS.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_WX_WS.setTextColor(Color.parseColor("#737373"));
			tclTv_GWXC_WS.setTextColor(Color.parseColor("#737373"));
			type = Constant.PLAN_PATROL_TEMPORARY_CS_WS;
			updateList(type);
			break;
		case R.id.tclTv_WX_WS:// 维修(武水)
			tclTv_CQ_WS.setBackgroundResource(0);
			tclTv_WX_WS.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_GWXC_WS.setBackgroundResource(0);
			tclTv_CQ_WS.setTextColor(Color.parseColor("#737373"));
			tclTv_WX_WS.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_GWXC_WS.setTextColor(Color.parseColor("#737373"));
			type = Constant.URGENT;
			updateList(type);
			break;		
		case R.id.tclTv_NDL_YH:// 未下载(粤海)
			tclTv_JH_YH.setBackgroundResource(0);
			tclTv_NDL_YH.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_YJ_YH.setBackgroundResource(0);
			tclTv_YDL_YH.setBackgroundResource(0);
			tclTv_JH_YH.setTextColor(Color.parseColor("#737373"));
			tclTv_NDL_YH.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_YJ_YH.setTextColor(Color.parseColor("#737373"));
			tclTv_YDL_YH.setTextColor(Color.parseColor("#737373"));
			tlcSelectTask.setVisibility(View.GONE);
			type = Constant.TASK_DOWNLOAD_N;
			updateList(type);
			break;
		case R.id.tclTv_JH_YH:// 计划任务(粤海)
			tclTv_NDL_YH.setBackgroundResource(0);
			tclTv_JH_YH.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_YJ_YH.setBackgroundResource(0);
			tclTv_YDL_YH.setBackgroundResource(0);
			tclTv_NDL_YH.setTextColor(Color.parseColor("#737373"));
			tclTv_JH_YH.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_YJ_YH.setTextColor(Color.parseColor("#737373"));
			tclTv_YDL_YH.setTextColor(Color.parseColor("#737373"));
			type = Constant.PLAN;
			updateList(type);
			break;		
		case R.id.tclTv_YJ_YH: // 应急(粤海)
			tclTv_JH_YH.setBackgroundResource(0);
			tclTv_YJ_YH.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_NDL_YH.setBackgroundResource(0);
			tclTv_YDL_YH.setBackgroundResource(0);
			tclTv_JH_YH.setTextColor(Color.parseColor("#737373"));
			tclTv_YJ_YH.setTextColor(Color.parseColor("#3f63ce"));
			tclTv_NDL_YH.setTextColor(Color.parseColor("#737373"));
			tclTv_YDL_YH.setTextColor(Color.parseColor("#737373"));
			type = Constant.URGENT;
			updateList(type);
			break;
		case R.id.tclTv_YDL_YH:
			tclTv_JH_YH.setBackgroundResource(0);
			tclTv_YJ_YH.setBackgroundResource(0);
			tclTv_NDL_YH.setBackgroundResource(0);
			tclTv_YDL_YH.setBackgroundResource(R.drawable.tv_green_bg);
			tclTv_JH_YH.setTextColor(Color.parseColor("#737373"));
			tclTv_YJ_YH.setTextColor(Color.parseColor("#737373"));
			tclTv_NDL_YH.setTextColor(Color.parseColor("#737373"));
			tclTv_YDL_YH.setTextColor(Color.parseColor("#3f63ce"));
			tlcSelectTask.setVisibility(View.VISIBLE);
			type = Constant.TASK_DOWNLOAD_Y;
			updateList(type);
			break;

		default:
			break;
		}

	}

	// 更新巡检列表
	private void updateListJH() throws SQLException {
		List<Map<String, Object>> groupData = new ArrayList<Map<String, Object>>();
		List<List<Map<String, Object>>> childData = new ArrayList<List<Map<String, Object>>>();
		for (int i = 0; i < mainViewDataBaseHandleList.size(); ++i) {
			mainViewDataBaseHandleList.get(i).updatePlanList(type, groupData,
					childData);
		}

		String mid = tlcYWLXItemsKV.get(tlcTvtitle.getText());
		
		taskListAdapter = new TaskListAdapter(this, groupData, childData,
				myHandler, tlcExListView, mid);
		tlcExListView.setAdapter(taskListAdapter);
		tlcExListView.setVisibility(View.VISIBLE);
		tlcListview.setVisibility(View.GONE);
	}

	private void updateList(String type) {
		if(Constant.TASK_DOWNLOAD_Y.equals(type)){
			try {
				updateList(title,null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		try {
			if (Constant.CYCLE_PLAN.equals(type)
					|| Constant.INTERIM_PLAN.equals(type)
					|| Constant.QSGL_JHX.equals(type)
					|| Constant.WXGL_JHXWX.equals(type)
					|| Constant.FMGL_JHXWX.equals(type)
					|| Constant.PLAN.equals(type)
					|| Constant.PLAN_PATROL_WS.equals(type)
					|| Constant.PLAN_PATROL_TEMPORARY_CS_WS.equals(type)) {// 计划性用title分类
				updateListJH();
				return;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tlcExListView.setVisibility(View.VISIBLE);
		tlcListview.setVisibility(View.GONE);
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < mainViewDataBaseHandleList.size(); ++i) {
			mainViewDataBaseHandleList.get(i).updateList(type, data);
		}
		tlcExListView.setAdapter(new TaskListCentreAdapter3(data,
				TaskListMainActivity.this, myHandler));
	}
	// 更新巡检列表
		private void updateList(String title,String taskCategory) throws SQLException {
			List<Map<String, Object>> groupData = new ArrayList<Map<String, Object>>();
			List<List<Map<String, Object>>> childData = new ArrayList<List<Map<String, Object>>>();
			for (int i = 0; i < mainViewDataBaseHandleList.size(); ++i) {
				mainViewDataBaseHandleList.get(i).updateTaskList(title, taskCategory, null, groupData, childData);
			}

			String mid = tlcYWLXItemsKV.get(tlcTvtitle.getText());
			
			TaskListAdapter2 taskListAdapter2 = new TaskListAdapter2(this, groupData, childData,
					myHandler, tlcExListView, mid);
			tlcExListView.setAdapter(taskListAdapter2);
			tlcExListView.setVisibility(View.VISIBLE);
			tlcListview.setVisibility(View.GONE);
		}

	private void showDialogYWLX(View view) {
		View layerView = getLayoutInflater().inflate(
				R.layout.tasklist_ywlx_dialog, null);

		ListView lv = (ListView) layerView
				.findViewById(R.id.tasklist_ywlx_List);
		lv.setAdapter(new ArrayAdapter<String>(this, R.anim.myspinner,
				tlcYWLXItems));
		final PopupWindow mPopupWindow;
		mPopupWindow = new PopupWindow(layerView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				updateYWLX(position);
				mPopupWindow.dismiss();
			}
		});

		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));
		
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		mPopupWindow.showAtLocation(layerView, Gravity.NO_GRAVITY, location[0]
				- layerView.getWidth(), location[1] + view.getHeight());
	}
	private void showDialog(View view) {
		final String key[]={Constant.PIPE_PATROL_YH,Constant.LEAK_INS,Constant.RRWO_TASK,null};
		String name[]={Constant.TASK_TITLE_CN.get(Constant.PIPE_PATROL_YH),Constant.TASK_TITLE_CN.get(Constant.LEAK_INS),
				Constant.TASK_TITLE_CN.get(Constant.RRWO_TASK),"全部"};
		View layerView = getLayoutInflater().inflate(
				R.layout.tasklist_ywlx_dialog, null);

		ListView lv = (ListView) layerView
				.findViewById(R.id.tasklist_ywlx_List);
		lv.setAdapter(new ArrayAdapter<String>(this, R.anim.myspinner,name));
		final PopupWindow mPopupWindow;
		mPopupWindow = new PopupWindow(layerView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				title=key[position];
				try {
					updateList(title,null);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mPopupWindow.dismiss();
			}
		});

		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(),
				(Bitmap) null));
		
		int[] location = new int[2];
		view.getLocationOnScreen(location);
		mPopupWindow.showAtLocation(layerView, Gravity.NO_GRAVITY, location[0]
				- layerView.getWidth(), location[1] + view.getHeight());
	}

	// 容器初始加载
	private void updateYWLX(String template) {
		if (!"".equals(template)) {
			String mid = tlcYWLXItemsKV.get(template);
			if (Constant.TSK_INSPECTION.equals(mid)) {// 巡检管理
				tclLL_XJGL.setVisibility(View.VISIBLE);// 巡检管理
				tclLL_WBGL.setVisibility(View.GONE);// 清疏管理
				tclLL_WXGL.setVisibility(View.GONE);// 维修管理
				tclLL_HMGL.setVisibility(View.GONE);
				tlcTvtitle.setText(template);
				tclTv_XJGL_JH.setBackgroundResource(R.drawable.tv_green_bg);
				tclTv_XJGL_LSX.setBackgroundResource(0);
				tclTv_XJGL_YJ.setBackgroundResource(0);
				type = Constant.CYCLE_PLAN;
				updateList(type);
			} else if (Constant.TSK_DREGING.equals(mid)) {// 清疏管理
				tclLL_XJGL.setVisibility(View.GONE);// 巡检管理
				tclLL_WBGL.setVisibility(View.VISIBLE);// 清疏管理
				tclLL_WXGL.setVisibility(View.GONE);// 维修管理
				tclLL_HMGL.setVisibility(View.GONE);
				tlcTvtitle.setText(template);
				tclTv_WBGL_JHX.setBackgroundResource(R.drawable.tv_green_bg);
				tclTv_WBGL_XCWB.setBackgroundResource(0);
				type = Constant.QSGL_JHX;
				updateList(type);
			} else if (Constant.TSK_REPAIR.equals(mid)) {// 维修管理
				tclLL_XJGL.setVisibility(View.GONE);// 巡检管理
				tclLL_WBGL.setVisibility(View.GONE);// 清疏管理
				tclLL_WXGL.setVisibility(View.VISIBLE);// 维修管理
				tclLL_HMGL.setVisibility(View.GONE);
				tlcTvtitle.setText(template);
				tclTv_WXGL_JHXWX.setBackgroundResource(R.drawable.tv_green_bg);
				tclTv_WXGL_XCQX.setBackgroundResource(0);
				type = Constant.WXGL_JHXWX;
				updateList(type);
			} else if (Constant.TSK_VALVE.equals(mid)) {// 阀门管理
				tclLL_XJGL.setVisibility(View.GONE);// 巡检管理
				tclLL_WBGL.setVisibility(View.GONE);// 清疏管理
				tclLL_WXGL.setVisibility(View.GONE);// 维修管理
				tclLL_HMGL.setVisibility(View.VISIBLE);
				tlcTvtitle.setText(template);
				tclTv_HMGL_JHXWX.setBackgroundResource(R.drawable.tv_green_bg);
				tclTv_HMGL_XCQX.setBackgroundResource(0);
				type = Constant.FMGL_JHXWX;
				updateList(type);
			} else if(Constant.TSK_WS.equals(mid)){
				tclLL_XJGL.setVisibility(View.GONE);// 巡检管理
				tclLL_WBGL.setVisibility(View.GONE);// 清疏管理
				tclLL_WXGL.setVisibility(View.GONE);// 维修管理
				tclLL_HMGL.setVisibility(View.GONE);// 维修管理
				tclLL_YW.setVisibility(View.GONE);// 任务管理
				tclLL_YW_WS.setVisibility(View.VISIBLE);// 任务管理(武水)
				tlcTvtitle.setText(template);
				tclTv_CQ_WS.setBackgroundResource(0);
				tclTv_WX_WS.setBackgroundResource(0);
				tclTv_GWXC_WS.setBackgroundResource(R.drawable.tv_green_bg);
				type = Constant.PLAN_PATROL_WS;
				updateList(type);
			} else if(Constant.TSK_YH.equals(mid)){
				tclLL_XJGL.setVisibility(View.GONE);// 巡检管理
				tclLL_WBGL.setVisibility(View.GONE);// 清疏管理
				tclLL_WXGL.setVisibility(View.GONE);// 维修管理
				tclLL_HMGL.setVisibility(View.GONE);// 维修管理
				tclLL_YW.setVisibility(View.GONE);// 任务管理
				tclLL_YW_WS.setVisibility(View.GONE);// 任务管理(武水)
				tclLL_YW_YH.setVisibility(View.VISIBLE);// 任务管理(粤海)
				tlcTvtitle.setText(template);
				tclTv_YJ_YH.setBackgroundResource(0);
				tclTv_JH_YH.setBackgroundResource(0);
				tclTv_NDL_YH.setBackgroundResource(R.drawable.tv_green_bg);
				tclTv_YDL_YH.setBackgroundResource(0);
				type = Constant.TASK_DOWNLOAD_N;
				updateList(type);
			}
		} else {
			Toast.makeText(this, "不存在此类型", Toast.LENGTH_LONG).show();
		}
	}

	// 容器初始加载
	private void updateYWLX(int position) {
		String template = tlcYWLXItems[position];
		if (!"".equals(template)) {
			String mid = tlcYWLXItemsKV.get(template);
			if (Constant.TSK_INSPECTION.equals(mid)) {// 巡检管理
				tclLL_XJGL.setVisibility(View.VISIBLE);// 巡检管理
				tclLL_WBGL.setVisibility(View.GONE);// 清疏管理
				tclLL_WXGL.setVisibility(View.GONE);// 维修管理
				tclLL_HMGL.setVisibility(View.GONE);
				tlcTvtitle.setText(template);
				tclTv_XJGL_JH.setBackgroundResource(R.drawable.tv_green_bg);
				tclTv_XJGL_LSX.setBackgroundResource(0);
				tclTv_XJGL_YJ.setBackgroundResource(0);
				type = Constant.CYCLE_PLAN;
				updateList(type);
			} else if (Constant.TSK_DREGING.equals(mid)) {// 清疏管理
				tclLL_XJGL.setVisibility(View.GONE);// 巡检管理
				tclLL_WBGL.setVisibility(View.VISIBLE);// 清疏管理
				tclLL_WXGL.setVisibility(View.GONE);// 维修管理
				tclLL_HMGL.setVisibility(View.GONE);
				tlcTvtitle.setText(template);
				tclTv_WBGL_JHX.setBackgroundResource(R.drawable.tv_green_bg);
				tclTv_WBGL_XCWB.setBackgroundResource(0);
				type = Constant.QSGL_JHX;
				updateList(type);
			} else if (Constant.TSK_REPAIR.equals(mid)) {// 维修管理
				tclLL_XJGL.setVisibility(View.GONE);// 巡检管理
				tclLL_WBGL.setVisibility(View.GONE);// 清疏管理
				tclLL_WXGL.setVisibility(View.VISIBLE);// 维修管理
				tclLL_HMGL.setVisibility(View.GONE);
				tlcTvtitle.setText(template);
				tclTv_WXGL_JHXWX.setBackgroundResource(R.drawable.tv_green_bg);
				tclTv_WXGL_XCQX.setBackgroundResource(0);
				type = Constant.WXGL_JHXWX;
				updateList(type);
			} else if (Constant.TSK_VALVE.equals(mid)) {// 阀门管理
				tclLL_XJGL.setVisibility(View.GONE);// 巡检管理
				tclLL_WBGL.setVisibility(View.GONE);// 清疏管理
				tclLL_WXGL.setVisibility(View.GONE);// 维修管理
				tclLL_HMGL.setVisibility(View.VISIBLE);
				tlcTvtitle.setText(template);
				tclTv_HMGL_JHXWX.setBackgroundResource(R.drawable.tv_green_bg);
				tclTv_HMGL_XCQX.setBackgroundResource(0);
				type = Constant.FMGL_JHXWX;
				updateList(type);
			}else if(Constant.TSK_WS.equals(mid)){
				tclLL_XJGL.setVisibility(View.GONE);// 巡检管理
				tclLL_WBGL.setVisibility(View.GONE);// 清疏管理
				tclLL_WXGL.setVisibility(View.GONE);// 维修管理
				tclLL_HMGL.setVisibility(View.GONE);// 维修管理
				tclLL_YW.setVisibility(View.GONE);// 任务管理
				tclLL_YW_WS.setVisibility(View.VISIBLE);// 任务管理(武水)
				tlcTvtitle.setText(template);
				tclTv_CQ_WS.setBackgroundResource(0);
				tclTv_WX_WS.setBackgroundResource(0);
				tclTv_GWXC_WS.setBackgroundResource(R.drawable.tv_green_bg);
				type = Constant.PLAN_PATROL_WS;
				updateList(type);
			}
		} else {
			Toast.makeText(this, "不存在此类型", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 再次点击退出应用
	 */
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
/*			if (side_drawer.isMenuShowing()
					|| side_drawer.isSecondaryMenuShowing()) {
				side_drawer.showContent();
			} else {*/
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					//(new UserLogoutAsyncTask()).execute(null,null,null);
					finish();
					// System.exit(0);
				}
			//}
			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	private class TaskListMainReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION
					.equals(action)) {
				updateList(type);
			}
		}

	}

	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				updateList(type);
				break;

			default:
				break;
			}
		}

	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (Constant.FROM_REQUEST_CODE == requestCode && data != null
				&& Constant.FROM_RESULT_CODE == resultCode) {
			for (int i = 0; i < mainViewDataBaseHandleList.size(); ++i) {
				mainViewDataBaseHandleList.get(i).blackHandle(data);
			}
		}
	}

}
