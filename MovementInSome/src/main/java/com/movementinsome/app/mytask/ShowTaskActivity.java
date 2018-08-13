package com.movementinsome.app.mytask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.view.CreateDynamicView;
import com.movementinsome.app.server.DownloadTask;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsComplainantForm;
import com.movementinsome.database.vo.InsDatumInaccurate;
import com.movementinsome.database.vo.InsDredgePTask;
import com.movementinsome.database.vo.InsFacMaintenanceData;
import com.movementinsome.database.vo.InsHiddenDangerReport;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowTaskActivity extends FullActivity implements OnClickListener{

	private ImageView show_task_back;// 返回
	private ScrollView show_task_msg;// 信息
	private TextView show_task_back_msg;// 提示
	private Button show_task_start;// 开始
	private Button show_task_down;// 下载
	private String taskNumber;// 任务编号
	private String key;// 下载标示
	private String taskCategory;// 表单类型
	private String moiNum;
	private InsTablePushTaskVo insTablePushTaskVo;
	private List<Module> lstModule;// 所有表单配置
	private List<Module> taskModule;// 当前任务表单配置
	
	private InsPlanTaskVO insPlanTaskVO;// 计划任务信息
	private InsComplainantForm insComplainantForm;// 投诉信息
	private InsSiteManage insSiteManage;// 工地档案信息
	private InsDredgePTask insDredgePTask;// 清梳 
	private InsFacMaintenanceData insFacMaintenanceData;// 维修设施
	
	private String serialNumber;// 流水号
	private boolean isStartWork=false;
	private boolean isDownloadData=false;
	private InsHiddenDangerReport insHiddenDangerReport;// 问题信息
	private InsDatumInaccurate insDatumInaccurate;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lstModule = AppContext.getInstance().getModuleData();
		taskModule=new ArrayList<Module>();
		setContentView(R.layout.show_task_activity);
		init();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*if(isStartWork){
			uploadState(Constant.UPLOAD_STATUS_PAUSE);
			isStartWork=false;
		}*/
	}
	private void init(){
		show_task_back=(ImageView) findViewById(R.id.show_task_back);// 返回
		show_task_back.setOnClickListener(this);
		show_task_msg=(ScrollView) findViewById(R.id.show_task_msg);// 信息
		show_task_back_msg=(TextView) findViewById(R.id.show_task_back_msg);// 提示
		show_task_start=(Button) findViewById(R.id.show_task_start);// 开始
		show_task_start.setOnClickListener(this);
		show_task_down=(Button) findViewById(R.id.show_task_down);// 下载
		show_task_down.setOnClickListener(this);
		
		
		Intent intent=getIntent();
		insTablePushTaskVo=(InsTablePushTaskVo) intent.getSerializableExtra("insTablePushTaskVo");
		if(insTablePushTaskVo!=null){
			taskCategory=insTablePushTaskVo.getTaskCategory();
			taskNumber=insTablePushTaskVo.getTaskNum();
			key=insTablePushTaskVo.getTitle();
		}
		insPlanTaskVO=(InsPlanTaskVO) intent.getSerializableExtra("insPlanTaskVO");
		insComplainantForm=(InsComplainantForm) intent.getSerializableExtra("insComplainantForm");
		insHiddenDangerReport=(InsHiddenDangerReport) intent.getSerializableExtra("insHiddenDangerReport");
		insDatumInaccurate = (InsDatumInaccurate) intent.getSerializableExtra("insDatumInaccurate");
		insSiteManage=(InsSiteManage) intent.getSerializableExtra("insSiteManage");
		insDredgePTask=(InsDredgePTask) intent.getSerializableExtra("insDredgePTask");
		insFacMaintenanceData=(InsFacMaintenanceData) intent.getSerializableExtra("insFacMaintenanceData");;// 维修设施
		try {
			if(Constant.COMPLAINANT.equals(key)){
				
				if(insComplainantForm!=null){
						List<String>mgData=new ArrayList<String>();
						mgData.add(insComplainantForm.getCfNum());// 投诉编号
						mgData.add(insComplainantForm.getCfSource());// 信息来源
						mgData.add(insComplainantForm.getCfType());// 投诉类型
						mgData.add(insComplainantForm.getCfDate());// 投诉时间
						mgData.add(insComplainantForm.getCfAddr());// 地点
						mgData.add(insComplainantForm.getCfRange());// 投诉范围
						mgData.add(insComplainantForm.getCfContent());// 投诉内容	
						mgData.add(insComplainantForm.getReceptionUnit());// 接待单位
						mgData.add(insComplainantForm.getReceptionist());// 接待员
						mgData.add(insComplainantForm.getCfPeople());// 投诉人
						mgData.add(insComplainantForm.getCfPhone());// 电话
						mgData.add(insComplainantForm.getBeginHandlePeople());// 开始处理人
						mgData.add(insComplainantForm.getBeginHandleDate());// 开始处理时间
						mgData.add(insComplainantForm.getEndHandlePeople());// 结束处理人
						mgData.add(insComplainantForm.getEndHandleDate());// 结束处理时间
						mgData.add(insComplainantForm.getResults());// 处理结果
						
						serialNumber=insComplainantForm.getSerialNumber();// 流水号
						moiNum=insComplainantForm.getGuid();
						show_task_msg.removeAllViews();
						show_task_msg.addView(new CreateDynamicView(this).dynamicAddView(mgData, R.array.tsgd));
						
					
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.GONE);
					isDownloadData=true;
				}else{
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.VISIBLE);
					show_task_back_msg.setText("表单数据尚未下载，请点击下载！");
					isDownloadData=false;
				}
			}else if(Constant.PROBLEM.equals(key)){
				if(insHiddenDangerReport!=null){
					List<String>mgData=new ArrayList<String>();
					mgData.add(insHiddenDangerReport.getSources());// 信息来源
					mgData.add(insHiddenDangerReport.getFacilitiesCaliber()+"");// 设施口径
					mgData.add(insHiddenDangerReport.getFacilitiesMaterial());// 设施材质
					mgData.add(insHiddenDangerReport.getAddr());// 发生地址
					mgData.add(insHiddenDangerReport.getWorkOrder());// 紧急程度
					mgData.add(insHiddenDangerReport.getPatrolPerson());// 巡线员
					mgData.add(insHiddenDangerReport.getPhone());// 联系方式
					mgData.add(insHiddenDangerReport.getFacilitiesType());// 设施类型
					mgData.add(insHiddenDangerReport.getReportedDate());// 反映时间
					mgData.add(insHiddenDangerReport.getReportedType());// 反映类别
					mgData.add(insHiddenDangerReport.getReportedContent());// 反映内容
					mgData.add(insHiddenDangerReport.getHandlePeople());// 处理人
					mgData.add(insHiddenDangerReport.getHandleDate());//处理时间
					mgData.add(insHiddenDangerReport.getResults());// 处理结果
					
					serialNumber=insComplainantForm.getSerialNumber();// 流水号
					moiNum=insComplainantForm.getGuid();
					show_task_msg.removeAllViews();
					show_task_msg.addView(new CreateDynamicView(this).dynamicAddView(mgData, R.array.wtgd));
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.GONE);
					isDownloadData=true;
				}else{
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.VISIBLE);
					show_task_back_msg.setText("表单数据尚未下载，请点击下载！");
					isDownloadData=false;
				}
			}else if(Constant.INSMAPINACCURATE_FORM.equals(key)){
				if(insDatumInaccurate!=null){
					List<String>mgData=new ArrayList<String>();
					mgData.add(insDatumInaccurate.getProblemType());// 问题类型
					mgData.add(insDatumInaccurate.getProblemDesc());// 问题描述
					mgData.add(insDatumInaccurate.getManageUnit());// 管理单位
					mgData.add(insDatumInaccurate.getCoordinate());// 具体位置
					mgData.add(insDatumInaccurate.getReportedPerson());// 反映人
					mgData.add(insDatumInaccurate.getReportedDate());// 反映时间
					mgData.add(insDatumInaccurate.getPhone());// 联系电话
					
					serialNumber=insDatumInaccurate.getSerialNumber();// 流水号
					moiNum=insDatumInaccurate.getGuid();
					show_task_msg.removeAllViews();
					show_task_msg.addView(new CreateDynamicView(this).dynamicAddView(mgData, R.array.tyxc));
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.GONE);
					isDownloadData=true;
				}else{
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.VISIBLE);
					show_task_back_msg.setText("表单数据尚未下载，请点击下载！");
					isDownloadData=false;
				}
			}else if(Constant.CYCLE_PLAN.equals(key)||Constant.INTERIM_PLAN.equals(key)
					||Constant.PLAN_PATROL_ZS_CYCLE.equals(key)||Constant.PLAN_PATROL_ZS_TEMPORARY.equals(key)||Constant.PLAN_PATROL_SCHEDULE.equals(key)  //中山公用巡查类型 add by gordon 2016/3/24
					||Constant.PLAN_CONSTRUCTION_CYCLE.equals(key)||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(key)
					||Constant.XJGL_GJD_CYCLE.equals(key)||Constant.XJGL_GJD_TEMPORARY.equals(key)
					||Constant.QSGL_JHX.equals(key)||Constant.WXGL_JHXWX.equals(key)||Constant.FMGL_JHXWX.equals(key)
					||Constant.PLAN_VALVE_CYCLE.equals(insTablePushTaskVo.getTitle())
					|| Constant.PLAN_VALVE_TEMPORARY.equals(insTablePushTaskVo.getTitle())
					|| Constant.PLAN_HYDRANT_CYCLE.equals(insTablePushTaskVo.getTitle())
					|| Constant.PLAN_HYDRANT_TEMPORARY.equals(insTablePushTaskVo.getTitle())
					|| Constant.PLAN_FAC_CYCLE.equals(insTablePushTaskVo.getTitle())
					|| Constant.PLAN_FAC_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
				if(insPlanTaskVO!=null){
					List<String> listValue = new ArrayList<String>();
					listValue.add(insPlanTaskVO.getWorkTaskNum());// 任务编号
					listValue.add(insPlanTaskVO.getWorkTaskName());// 任务名称
					listValue.add(insPlanTaskVO.getWorkTaskAddr());// 任务地点
					listValue.add(insPlanTaskVO.getWorkTaskWork());// 主要工作
					listValue.add(insPlanTaskVO.getWorkTaskPSDate());// 开始时间
					listValue.add(insPlanTaskVO.getWorkTaskPEDate());// 结束时间
					listValue.add(insPlanTaskVO.getWorkTaskRemarks());// 备注
					
					show_task_msg.removeAllViews();
					show_task_msg.addView(new CreateDynamicView(this).dynamicAddView(listValue, R.array.lsxj));
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.GONE);
					isDownloadData=true;
				}else if(insSiteManage!=null){
					List<String> listValue = new ArrayList<String>();
					listValue.add(insSiteManage.getProjectNum());// 工程档案编号
					listValue.add(insSiteManage.getProjectName());// 项目名称
					listValue.add(insSiteManage.getConstructionAddr());// 详细位置
					listValue.add(insSiteManage.getSections());// 所在路段
					listValue.add(insSiteManage.getProjectLeader());// 项目负责人
					listValue.add(insSiteManage.getConstructionUnit());// 施工单位
					listValue.add(insSiteManage.getConstructionPhone());// 施工方电话
					listValue.add(insSiteManage.getPrjStartDate());// 施工工期开始日期
					listValue.add(insSiteManage.getPrjEndDate());// 施工工期结束日期
					
					show_task_msg.removeAllViews();
					show_task_msg.addView(new CreateDynamicView(this).dynamicAddView(listValue, R.array.gdda));
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.GONE);
					isDownloadData=true;
				}else if(insDredgePTask!=null){
					List<String> listValue = new ArrayList<String>();
					listValue.add(insDredgePTask.getSerialNumber());// 清疏编号
					listValue.add(insDredgePTask.getRoad());// 路段
					listValue.add(insDredgePTask.getManageUnit());// 区所
					listValue.add(insDredgePTask.getSewerLen()+"");// 污水管渠长
					listValue.add(insDredgePTask.getSewerSize());// 污水管渠尺寸
					listValue.add(insDredgePTask.getGulliesLen()+"");// 雨水管渠长
					listValue.add(insDredgePTask.getGulliesSize()+"");// 雨水管渠尺寸
					listValue.add(insDredgePTask.getGulliesSum()+"");// 雨水口数量
					listValue.add(insDredgePTask.getSewageWellSum()+"");// 污水检查井数量
					listValue.add(insDredgePTask.getRainWellSum()+"");// 雨水检查井数量
					
					listValue.add(insDredgePTask.getSewerLenRl()+"");// 已检污水管渠长
					listValue.add(insDredgePTask.getGulliesLenRl()+"");// 已检雨水管渠长
					listValue.add(insDredgePTask.getGulliesSumRl()+"");// 已检雨水口数量
					listValue.add(insDredgePTask.getSewageWellSumRl()+"");// 已检污水检查井数量
					listValue.add(insDredgePTask.getRainWellSumRl()+"");// 已检雨水检查井数量
					
					
					show_task_msg.removeAllViews();
					show_task_msg.addView(new CreateDynamicView(this).dynamicAddView(listValue, R.array.jhqs));
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.GONE);
					isDownloadData=true;
				}else if(insFacMaintenanceData!=null){
					List<String> listValue = new ArrayList<String>();
					listValue.add(insFacMaintenanceData.getWorkTaskNum());// 任务编号
					listValue.add(insFacMaintenanceData.getManageUnit());// 管理单位
					listValue.add(insFacMaintenanceData.getFacName());// 设施名称
					listValue.add(insFacMaintenanceData.getFacNum());// 设施编号
					listValue.add(insFacMaintenanceData.getFmdAddr());// 详细位置
					listValue.add(insFacMaintenanceData.getReportedContent());// 故障内容
					listValue.add(insFacMaintenanceData.getRemarks());// 备注
					show_task_msg.removeAllViews();
					show_task_msg.addView(new CreateDynamicView(this).dynamicAddView(listValue, R.array.fac));
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.GONE);
					isDownloadData=true;
				}else{
					show_task_start.setVisibility(View.GONE);
					show_task_down.setVisibility(View.GONE);
					show_task_back_msg.setVisibility(View.VISIBLE);
					show_task_back_msg.setText("表单数据尚未下载，请点击下载！");
					isDownloadData=false;
				}
			}
		//	uploadState(Constant.UPLOAD_STATUS_START);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
	}
	private void showPushTaskMsg(){

		if(insTablePushTaskVo!=null){
			taskCategory=insTablePushTaskVo.getTaskCategory();
			String names=insTablePushTaskVo.getNames();
			String values=insTablePushTaskVo.getValues();
			show_task_msg.removeAllViews();
			//调用动态创建控件，显示推送下来的信息
			show_task_msg.addView(new CreateDynamicView(this).dynamicAddView2(names,values));
			
		}
	}
	private void uploadState(String state){
		if(isDownloadData){
			TaskFeedBackAsyncTask taskFeedBackAsyncTask
			=new TaskFeedBackAsyncTask(this, false,false, taskNumber, state, moiNum, key, taskCategory,null,null,null);
			taskFeedBackAsyncTask.execute();
		}
	}
	
	private void showTaskDialog(){
		String[] itmes =new String[taskModule.size()];
		
		for(int i=0;i<itmes.length;++i){
			itmes[i]=taskModule.get(i).getName();
		}
		AlertDialog.Builder vDialog = new AlertDialog.Builder(this);
		vDialog.setTitle("选择工作类型");
		vDialog.setItems(itmes, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				uploadState( Constant.UPLOAD_STATUS_WORK);
				isStartWork=true;
				Intent newFormInfo = new Intent(ShowTaskActivity.this,RunForm.class);
        		newFormInfo.putExtra("template", taskModule.get(which).getTemplate());
        		HashMap<String,String>params=new HashMap<String, String>();
        		params.put("pid", taskNumber);
        		params.put("serialNumber", serialNumber);
        		newFormInfo.putExtra("iParams", params);    			
        		startActivity(newFormInfo);
			}
		});

		vDialog.show();
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.show_task_back:
			finish();
			break;
		case R.id.show_task_start:
			/*if(Constant.COMPLAINANT.equals(key)){
				showTaskDialog();
			}else if(Constant.CYCLE_PLAN.equals(key)||Constant.INTERIM_PLAN.equals(key)){
				uploadState( Constant.UPLOAD_STATUS_WORK);
				Intent intent=new Intent();
				intent.setClass(ShowTaskActivity.this, InspectionActivity.class);
				intent.putExtra("insPlanTaskVO", insPlanTaskVOList.get(0));
				intent.putExtra("type", key);
				intent.putExtra("taskNumber", taskNumber);
				isStartWork=true;
				startActivity(intent);
			}*/
			/*TaskFeedBackAsyncTask taskFeedBackAsyncTask=
				new TaskFeedBackAsyncTask(this, true, taskNumber, Constant.UPLOAD_STATUS_RECEIVE, moiNum, key, taskCategory);
			taskFeedBackAsyncTask.execute("");*/
			break;
		case R.id.show_task_down:
			DownloadTask downloadTask = new DownloadTask(this, 
					insTablePushTaskVo,true,null);
			downloadTask.execute(AppContext.getInstance().getCurUser().getUserName());
			break;

		default:
			break;
		}
		
	}

}
