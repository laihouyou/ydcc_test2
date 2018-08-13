package com.movementinsome.app.mytask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.handle.ShowPatrolDataBaseHandle;
import com.movementinsome.app.mytask.handle.ShowPatrolDataSiteManageHandler;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowPatrolDataActivity2 extends FullActivity implements
		OnClickListener, OnInitListener {

	// 返回
	private ImageView show_patrol_data_back2;
	// 填单
	private Button show_patrol_data_write2;

	// 上方提示框
	private LinearLayout show_work_message_ly2;
	// 上方提示框标题
	private TextView show_work_message2;
	// 上方提示框内容
	private TextView show_work_content2;
	// 分类标签
	private TextView show_wx_title2;
	// 分类标签
	private TextView show_yx_title2;
	// 列表
	private ListView show_patrol_data_list2;
	// 底部提示框
	private RelativeLayout show_patrol_msg_rl2;
	// 底部提示框信息
	private TextView show_patrol_msg2;
	// 底部提示框填单
	private Button show_patrol_rl_write;
	// 处理接口
	private List<ShowPatrolDataBaseHandle> showPatrolDataBaseHandleList;
	private Map<String, View>vs;
	private List<Module> lstModule;// 所有表单配置
	//private TextToSpeech tts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.show_patrol_data_activity2);
		initUI();
		InsTablePushTaskVo insTablePushTaskVo=(InsTablePushTaskVo) getIntent().getSerializableExtra("insTablePushTaskVo");
		showPatrolDataBaseHandleList=new ArrayList<ShowPatrolDataBaseHandle>();
		vs=new HashMap<String, View>();
		vs.put("show_work_message_ly2", show_work_message_ly2);
		vs.put("show_work_message2", show_work_message2);
		vs.put("show_work_content2", show_work_content2);
		vs.put("show_wx_title2", show_wx_title2);
		vs.put("show_yx_title2", show_yx_title2);
		vs.put("show_patrol_data_list2", show_patrol_data_list2);
		vs.put("show_patrol_msg_rl2", show_patrol_msg_rl2);
		vs.put("show_patrol_msg2", show_patrol_msg2);
		vs.put("show_patrol_rl_write", show_patrol_rl_write);
		lstModule = AppContext.getInstance().getModuleData();
		
		//showPatrolDataBaseHandleList.add(new ShowPatrolDataSiteManageHandler(this,insTablePushTaskVo,lstModule,tts));
		showPatrolDataBaseHandleList.add(new ShowPatrolDataSiteManageHandler(this,insTablePushTaskVo,lstModule,null));
		for(ShowPatrolDataBaseHandle showPatrolDataBaseHandle:showPatrolDataBaseHandleList){
			showPatrolDataBaseHandle.controlUI(vs);
			showPatrolDataBaseHandle.initPatrolData();
		}
	}

	private void initUI() {
		show_patrol_data_back2 = (ImageView) findViewById(R.id.show_patrol_data_back2);
		show_patrol_data_back2.setOnClickListener(this);
		// 填单
		show_patrol_data_write2 = (Button) findViewById(R.id.show_patrol_data_write2);
		show_patrol_data_write2.setOnClickListener(this);

		// 上方提示框
		show_work_message_ly2 = (LinearLayout) findViewById(R.id.show_work_message_ly2);
		// 上方提示框标题
		show_work_message2 = (TextView) findViewById(R.id.show_work_message2);
		// 上方提示框内容
		show_work_content2 = (TextView) findViewById(R.id.show_work_content2);
		// 分类标签
		show_wx_title2 = (TextView) findViewById(R.id.show_wx_title2);
		show_wx_title2.setOnClickListener(this);
		// 分类标签
		show_yx_title2 = (TextView) findViewById(R.id.show_yx_title2);
		show_yx_title2.setOnClickListener(this);
		// 列表
		show_patrol_data_list2 = (ListView) findViewById(R.id.show_patrol_data_list2);
		// 底部提示框
		show_patrol_msg_rl2 = (RelativeLayout) findViewById(R.id.show_patrol_msg_rl2);
		// 底部提示框信息
		show_patrol_msg2 = (TextView) findViewById(R.id.show_patrol_msg2);
		// 底部提示框填单
		show_patrol_rl_write = (Button) findViewById(R.id.show_patrol_rl_write);
		show_patrol_rl_write.setOnClickListener(this);
	}

	@Override
	public void onInit(int status) {
		// TODO Auto-generated method stub

	}

	private Handler timeHamdler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				for(ShowPatrolDataBaseHandle showPatrolDataBaseHandle:showPatrolDataBaseHandleList){
					showPatrolDataBaseHandle.updateUI(vs);
				}
			default:
				break;
			}
		}

	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.show_patrol_data_back2:
			for(ShowPatrolDataBaseHandle showPatrolDataBaseHandle:showPatrolDataBaseHandleList){
				showPatrolDataBaseHandle.backHandler();
			}
			break;
		case R.id.show_patrol_data_write2:
			for(ShowPatrolDataBaseHandle showPatrolDataBaseHandle:showPatrolDataBaseHandleList){
				showPatrolDataBaseHandle.writeHandlerB();
			}
			break;
		case R.id.show_patrol_rl_write:
			for(ShowPatrolDataBaseHandle showPatrolDataBaseHandle:showPatrolDataBaseHandleList){
				showPatrolDataBaseHandle.writeHandlerD();
			}
			break;
		case R.id.show_wx_title2:
			show_yx_title2.setBackgroundResource(0);
			show_wx_title2.setBackgroundResource(R.drawable.tv_green_bg);
			
			for(ShowPatrolDataBaseHandle showPatrolDataBaseHandle:showPatrolDataBaseHandleList){
				showPatrolDataBaseHandle.patrolN(vs);
			}
			break;
		case R.id.show_yx_title2:
			show_wx_title2.setBackgroundResource(0);
			show_yx_title2.setBackgroundResource(R.drawable.tv_green_bg);
			
			for(ShowPatrolDataBaseHandle showPatrolDataBaseHandle:showPatrolDataBaseHandleList){
				showPatrolDataBaseHandle.patrolY(vs);
			}
			break;

		default:
			break;
		}
	}

}
