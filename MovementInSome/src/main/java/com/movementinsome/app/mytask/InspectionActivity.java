package com.movementinsome.app.mytask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.view.CreateDynamicView;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.kernel.activity.FullActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InspectionActivity extends FullActivity implements OnClickListener{
	private TextView XxPLd;// 路段/小区
	private TextView XxPQdd;// 签到点
	private TextView xxPtv_Table_Title;// 标题
	private ScrollView xxQtv_Msg;// 任务消息
	
	private Button xxPbtn_Back;// 返回
	
	private Button XxPYHSB;// 隐患上报
	private Button XxPGDSB;// 工地上报
	private Button XxPSSJC;// 设施检查
	private Button XxPNDJL;// 浓度记录
	private Button XxPSJJC;// GIS数据纠错
	
	private final String WORK_TYPE_LD="路段";
	private final String WORK_TYPE_QDD="签到点";
	private String workType=WORK_TYPE_LD;
	private List<String> data;
	
	private ListView xx_work_list;
	private InsPlanTaskVO planTask;
	private String type;
	
	private String taskNumber;
	//巡检数据
	private List<InsPatrolDataVO> pdList;
	private List<InsPatrolDataVO> pdListLDXQ;// 路段，小区
	private List<InsPatrolDataVO> pdListQDD;// 签到点
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inspection_activity);
		init();
		updata();
		updateList();
		
	}
	
	private void init(){
		
		pdListLDXQ=new ArrayList<InsPatrolDataVO>();// 路段，小区
		pdListQDD=new ArrayList<InsPatrolDataVO>();// 签到点
		
		xx_work_list=(ListView) findViewById(R.id.xxLvGd);
		
		XxPLd=(TextView) findViewById(R.id.XxPLd);// 路段/小区
		XxPLd.setOnClickListener(this);
		XxPQdd=(TextView) findViewById(R.id.XxPQdd);// 签到点
		XxPQdd.setOnClickListener(this);
		xxPtv_Table_Title=(TextView) findViewById(R.id.xxPtv_Table_Title);// 标题
		
		xxPbtn_Back=(Button) findViewById(R.id.xxPbtn_Back);// 返回
		xxPbtn_Back.setOnClickListener(this);
		
		XxPYHSB=(Button) findViewById(R.id.XxPYHSB);// 隐患上报
		XxPYHSB.setOnClickListener(this);
		XxPGDSB=(Button) findViewById(R.id.XxPGDSB);// 工地上报
		XxPGDSB.setOnClickListener(this);
		XxPSSJC=(Button) findViewById(R.id.XxPSSJC);// 设施检查
		XxPSSJC.setOnClickListener(this);
		XxPNDJL=(Button) findViewById(R.id.XxPNDJL);// 浓度记录
		XxPNDJL.setOnClickListener(this);
		XxPSJJC=(Button) findViewById(R.id.XxPSJJC);// GIS数据纠错
		XxPSJJC.setOnClickListener(this);
		
		Intent intent=getIntent();
		type=intent.getStringExtra("type");
		taskNumber=intent.getStringExtra("taskNumber");
		planTask=(InsPlanTaskVO) intent.getSerializableExtra("insPlanTaskVO");
		
		xxQtv_Msg=(ScrollView) findViewById(R.id.xxQtv_Msg);// 任务消息
	}
	private void updata(){
		Dao<InsPatrolDataVO, Long> insPatrolDataDao;
		try {
			insPatrolDataDao = AppContext.getInstance().getAppDbHelper().getDao(InsPatrolDataVO.class);
			pdList=insPatrolDataDao.queryForEq("workTaskNum", taskNumber);
			if(pdList!=null){
				for(InsPatrolDataVO insPatrolDataVO:pdList){
					if("签到点".equals(insPatrolDataVO.getFacType())){
						pdListQDD.add(insPatrolDataVO);
					}else{
						pdListLDXQ.add(insPatrolDataVO);
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		CreateDynamicView facView = new CreateDynamicView(this);
		List<String> listValue = new ArrayList<String>();
		if(planTask!=null){
			listValue.add(planTask.getWorkTaskNum()+"");// 任务编号
			listValue.add(planTask.getWorkTaskName()+"");// 任务名称
			listValue.add(planTask.getWorkTaskAddr()+"");// 任务地点
			listValue.add(planTask.getWorkTaskWork()+"");// 主要工作
			if(Constant.INTERIM_PLAN.equals(type)){
				listValue.add(planTask.getWorkTaskPSDate()+"");// 开始时间
				listValue.add(planTask.getWorkTaskPEDate()+"");// 完成时间
			}
			listValue.add(planTask.getWorkTaskRemarks()+"");// 备注
			if(Constant.CYCLE_PLAN.equals(type)){
				
			}else if(Constant.INTERIM_PLAN.equals(type)){
				xxQtv_Msg.addView(facView.dynamicAddView(listValue,R.array.lsxj));
			}
			xxPtv_Table_Title.setText(Constant.TASK_TITLE_CN.get(type));
		}
		/*if(planTask!=null){
			// 上报状态
			TaskFeedBackAsyncTask taskFeedBackAsyncTask=new TaskFeedBackAsyncTask(this, false, taskNumber, Constant.UPLOAD_STATUS_WORK
					, planTask.getGuid(), type, MyPublicData.XJFL_YJ,planTask.getTaskSource());
			taskFeedBackAsyncTask.execute();
		}*/
		
	}
	private void updateList(){
		
		data=new ArrayList<String>();
		if(WORK_TYPE_LD.equals(workType)){
			//xx_work_list.setAdapter(new PlanWorkAdapter(this,pdListLDXQ,myHandler));
		}else if(WORK_TYPE_QDD.equals(workType)){
		//	xx_work_list.setAdapter(new PlanWorkAdapter(this,pdListQDD,myHandler));
		}
	}
	private Handler myHandler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				updateList();
				break;

			default:
				break;
			}
		}
		
	};
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		switch (v.getId()) {
		
		case R.id.xxPbtn_Back:// 返回
			/*if(planTask!=null){
				// 上报状态
				TaskFeedBackAsyncTask taskFeedBackAsyncTask=new TaskFeedBackAsyncTask(this, false, taskNumber
						, Constant.UPLOAD_STATUS_PAUSE, null, type, MyPublicData.XJFL_YJ,planTask.getTaskSource());
				taskFeedBackAsyncTask.execute();
			}*/
			finish();
			break;
		case R.id.XxPLd:// 路段/小区
			XxPLd.setBackgroundResource(R.drawable.wx_bg);
			XxPQdd.setBackgroundResource(0);
			workType=WORK_TYPE_LD;
			updateList();
			break;
		case R.id.XxPQdd:// 签到点
			workType=WORK_TYPE_QDD;
			XxPLd.setBackgroundResource(0);
			XxPQdd.setBackgroundResource(R.drawable.wx_bg);
			updateList();
			break;
		
		case R.id.XxPYHSB:// 隐患上报
			
			break;
		case R.id.XxPGDSB:// 工地上报
			
			break;
		case R.id.XxPSSJC:// 设施检查
			dialogXxPSSJC();
			break;
		case R.id.XxPNDJL:// 浓度记录
			
			break;
		case R.id.XxPSJJC:// GIS数据纠错
			
			break;
		default:
			break;
		}
		
	}
	private void dialogXxPSSJC(){
		String[]itmes={"凝水器巡查记录","户外立管巡查记录","地下燃气管线巡查表"};
		AlertDialog.Builder vDialog = new AlertDialog.Builder(this);
		vDialog.setTitle("设施检查");
		vDialog.setItems(itmes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				switch (which) {
				case 0:
					
					
					break;
				case 1:
					
					break;
				case 2:
					
					break;
				
				default:
					break;
				}
			}
		});
		vDialog.setPositiveButton("返回", null);
		
		vDialog.show();
	}

}
