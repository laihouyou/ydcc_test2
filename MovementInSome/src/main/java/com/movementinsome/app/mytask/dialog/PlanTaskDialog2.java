package com.movementinsome.app.mytask.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.movementinsome.R;
import com.movementinsome.app.mytask.ShowTaskActivity;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsTablePushTaskVo;

import java.util.ArrayList;
import java.util.List;

public class PlanTaskDialog2 extends AlertDialog implements OnClickListener{
	private LinearLayout ptdlinearP2;		//容器
	private Button ptdsbtn2_WriteT;			//填单
	private Button ptdsbtn2_finish;// 完成
	private Button ptdsbtn2_MSG;// 详细信息

	private Activity activity;
	private String backgroupType;	//dialog 背景图片的指针方向：up,down
	private InsTablePushTaskVo insTablePushTaskVo;
	private String tableType;
	private String taskNumber;

	private int height;
	
	public PlanTaskDialog2(Activity activity, int theme,String backgroupType,InsTablePushTaskVo insTablePushTaskVo) {
		super(activity, theme);
		// TODO Auto-generated constructor stub
		this.activity = activity;
		this.backgroupType = backgroupType;
		this.insTablePushTaskVo=insTablePushTaskVo;
		tableType=insTablePushTaskVo.getTaskCategory();
		taskNumber=insTablePushTaskVo.getTaskNum();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.plantask_dialogshow_2);
		//调用控件初始化
		ptdsInitlnterface();
		
		ptdlinearP2 = (LinearLayout)findViewById(R.id.ptdLinearP2);
		if("up".equals(backgroupType)){
			ptdlinearP2.setBackgroundResource(R.drawable.dialog_up);
			ptdlinearP2.setPadding(20, 15, 20, 10);
		}else if("down".equals(backgroupType)){
			ptdlinearP2.setBackgroundResource(R.drawable.dialog_down);
			ptdlinearP2.setPadding(20, 10, 20, 15);
		}
		height = ptdlinearP2.getMeasuredHeight();
		
		this.setCanceledOnTouchOutside(true);
		
		
		
	}


	/**
	 * 控件ID 初始化
	 */
	public void ptdsInitlnterface(){
		ptdsbtn2_MSG=(Button) findViewById(R.id.ptdsbtn2_MSG);
		ptdsbtn2_MSG.setOnClickListener(this);
		
		ptdsbtn2_WriteT=(Button) findViewById(R.id.ptdsbtn2_WriteT);
		ptdsbtn2_WriteT.setOnClickListener(this);
		
		ptdsbtn2_finish=(Button) findViewById(R.id.ptdsbtn2_finish);
		ptdsbtn2_finish.setOnClickListener(this);
		ptdsbtn2_finish.setVisibility(View.GONE);
		if(Constant.CYCLE_PLAN.equals(tableType)){
			ptdsbtn2_WriteT.setVisibility(View.VISIBLE);
			ptdsbtn2_finish.setVisibility(View.GONE);
		}else if(Constant.INTERIM_PLAN.equals(tableType)){
			ptdsbtn2_WriteT.setVisibility(View.VISIBLE);
			ptdsbtn2_finish.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 设置dialog显示位置
	 * 需在show方法前调用
	 * @param view 设置依附控件
	 * @param dialog 
	 * @param height 加入dialog的自定义view的总长度
	 */
	public void setlocation(View linear,PlanTaskDialog2 dialog){
		int[] location = new int[2];
		linear.getLocationInWindow(location);		//获取控件位置
		int x = location[0];
		int y = location[1];
		DisplayMetrics dm = new DisplayMetrics();  
		dm = activity.getResources().getDisplayMetrics();  

		int screenWidth  = dm.widthPixels;      // 屏幕宽（像素，如：480px）  
		int screenHeight = dm.heightPixels;     // 屏幕高（像素，如：800px）  

		Window win = dialog.getWindow();
		LayoutParams params = new LayoutParams();
		params.x = x-screenWidth/2+linear.getWidth()/2;// 设置x坐标
		if("up".equals(backgroupType)){
			params.y = y-screenHeight/2  + linear.getHeight();// 设置y坐标
		}else{
			params.y = y-screenHeight/2  ;// 设置y坐标 20 :linear的paddingBotton值
		}

		win.setAttributes(params);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		switch (v.getId()) {
		case R.id.ptdsbtn2_WriteT:
			
			this.dismiss();
			break;	
		case R.id.ptdsbtn2_finish:
			Dialog();
			this.dismiss();
			break;
		case R.id.ptdsbtn2_MSG:
			intent.setClass(activity,
					ShowTaskActivity.class);
			intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
			insTablePushTaskVo.getTaskCategory();
			activity.startActivity(intent);
			this.dismiss();
			break;
		default:
			break;
		}
	}

	private void mapLoc(){// 定位
		/*((PActivity)activity).myActivity = null;
		MyPublicData.activityType = "location";
		Intent intent = new Intent(activity, MainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		activity.startActivity(intent);
		this.dismiss();*/
	}
	//接单对话框
	private void Dialog(){
		new AlertDialog.Builder(activity)
		.setTitle("提示")
		.setIcon(android.R.drawable.ic_menu_help)
		.setMessage("确定结束此工地监护，将删除表单")
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				List<String>taskNumberList=new ArrayList<String>();
				if(Constant.CYCLE_PLAN.equals(tableType)){
					
				}else if(Constant.INTERIM_PLAN.equals(tableType)){
					
				}
				TaskFeedBackAsyncTask taskFeedBackAsyncTask
				=new TaskFeedBackAsyncTask(activity, false,false, insTablePushTaskVo.getTaskNum(), Constant.UPLOAD_STATUS_FINISH
						, null, insTablePushTaskVo.getTitle(), insTablePushTaskVo.getTaskCategory(),null,null,null);
				taskFeedBackAsyncTask.execute();
			}
		})
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		}).show();
	}
	
}
