package com.movementinsome.app.mytask.handle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.ShowPatrolDataExpActivity;
import com.movementinsome.app.mytask.ShowTaskMsgActivity;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.DownloadTask;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.movementinsome.database.vo.BsPnInsTask;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.map.MapBizViewer;

import java.util.Map;

public class YHTaskListHandle implements TaskListBaseHandle, PublicHandle {

	Activity context;
	// 状态
	private TaskUploadStateVO taskUploadStateVO;
	// 计划信息
	private BsPnInsTask bsPnInsTask;
	// 推送
	private InsTablePushTaskVo insTablePushTaskVo;
	// 
	private BsSupervisionPoint bsSupervisionPoint;
	
	private BsRoutineInsArea bsRoutineInsArea;
	private BsInsFacInfo bsInsFacInfo;
	private BsEmphasisInsArea bsEmphasisInsArea;
	private BsLeakInsArea bsLeakInsArea;
	
	private String title;
	public YHTaskListHandle(Activity context,
			InsTablePushTaskVo insTablePushTaskVo,
			BsPnInsTask bsPnInsTask,TaskUploadStateVO taskUploadStateVO) {
		this.context = context;
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.bsPnInsTask = bsPnInsTask;
		this.taskUploadStateVO = taskUploadStateVO;
		title = insTablePushTaskVo.getTitle();
	}
	public YHTaskListHandle(Activity context,
			InsTablePushTaskVo insTablePushTaskVo,
			BsSupervisionPoint bsSupervisionPoint,BsRoutineInsArea bsRoutineInsArea
			,BsInsFacInfo bsInsFacInfo,BsEmphasisInsArea bsEmphasisInsArea
			,BsLeakInsArea bsLeakInsArea) {
		this.context = context;
		this.insTablePushTaskVo = insTablePushTaskVo;
		
		this.bsSupervisionPoint = bsSupervisionPoint;
		this.bsRoutineInsArea = bsRoutineInsArea;
		this.bsInsFacInfo = bsInsFacInfo;
		this.bsEmphasisInsArea = bsEmphasisInsArea;
		this.bsLeakInsArea = bsLeakInsArea;
		
		title = insTablePushTaskVo.getTitle();
	}
	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void childLocHandler() {
		// TODO Auto-generated method stub
		if (Constant.ROUTINE_INS.equals(title)){
			String strGraph = "";
			if(bsSupervisionPoint!=null){
				strGraph = bsSupervisionPoint.getSpCoordinate();
			}else if(bsRoutineInsArea!=null){
				strGraph = bsRoutineInsArea.getRiaCoordinate();
			}
			gotoMapLoc(strGraph);
		}else if(Constant.SPECIAL_INS.equals(title)){
			String strGraph = bsInsFacInfo.getIfiFacCoordinate();
			gotoMapLoc(strGraph);
		}else if(Constant.EMPHASIS_INS.equals(title)){
			String strGraph = bsEmphasisInsArea.getEiaCoordinate();
			gotoMapLoc(strGraph);
		}else if(Constant.LEAK_INS.equals(title)){
			String strGraph = bsLeakInsArea.getLiaCoordinate();
			gotoMapLoc(strGraph);
		}
	}
	private void gotoMapLoc(String strGraph){
		Intent intent = new Intent();
		intent.setClass(context, MapBizViewer.class);
		
		intent.putExtra("strGraph", strGraph);
		intent.putExtra("type", 10006);
		((Activity) context).startActivity(intent);
	}

	@Override
	public void childWriteTableHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void childShowMsg() {
		// TODO Auto-generated method stub
		if (Constant.ROUTINE_INS.equals(title)){
			if(bsSupervisionPoint!=null){
				
			}else if(bsRoutineInsArea!=null){
				
			}
		}else if(Constant.SPECIAL_INS.equals(title)){
			
			
		}else if(Constant.EMPHASIS_INS.equals(title)){
			
		}else if(Constant.LEAK_INS.equals(title)){
			
		}
	}

	@Override
	public void childFinish() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void childComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupWriteTableHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupStartHandler(String mModuleid) {
		// TODO Auto-generated method stub
		if (Constant.ROUTINE_INS.equals(title)
				|| Constant.SPECIAL_INS.equals(title)
				|| Constant.EMPHASIS_INS.equals(title)
				|| Constant.LEAK_INS.equals(title)) {
			/*TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, insTablePushTaskVo.getTaskNum(),
					Constant.UPLOAD_STATUS_START, null,
					insTablePushTaskVo.getTitle(),
					insTablePushTaskVo.getTaskCategory(), null, null, null);
			taskFeedBackAsyncTask.execute();*/
			Intent intent = new Intent();
			intent.setClass(context, ShowPatrolDataExpActivity.class);
			intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
			intent.putExtra("isShowMap", true);
			intent.putExtra("isShowMessage", true);
			intent.putExtra("isShowSearch", false);
			intent.putExtra("moduleid",mModuleid);
			intent.putExtra("isShowNearby",true);
			intent.putExtra("isShowReport", false);
			intent.putExtra("isShowHadDoDataList", false);
			if(Constant.LEAK_INS.equals(title)){
				intent.putExtra("hadDoDataListName", "查漏区域列表");
			}
			String title = Constant.TASK_TITLE_CN.get(insTablePushTaskVo.getTitle());
			if(title!=null&&!"".equals(title)){
				intent.putExtra("title",title);
			}
			context.startActivity(intent);
		}
	}

	@Override
	public void groupReturnTableHandler() {
		// TODO Auto-generated method stub
		if (Constant.ROUTINE_INS.equals(title)
				|| Constant.SPECIAL_INS.equals(title)
				|| Constant.EMPHASIS_INS.equals(title)
				|| Constant.LEAK_INS.equals(title)) {
			new AlertDialog.Builder(context).setTitle("提示")
			.setIcon(android.R.drawable.ic_menu_help)
			.setMessage("确定要退单，将删除数据！")
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
					TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
							context,
							true,
							false,
							insTablePushTaskVo.getTaskNum(),
							Constant.UPLOAD_STATUS_RETREAT,
							null, insTablePushTaskVo.getTitle(),
							insTablePushTaskVo.getTaskCategory(), null,
							null, null);
					taskFeedBackAsyncTask.execute();
				}
			}).show();
		}
	}

	@Override
	public void groupFinishHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void groupShowMsg() {
		// TODO Auto-generated method stub
		if (Constant.ROUTINE_INS.equals(title)
				|| Constant.SPECIAL_INS.equals(title)
				|| Constant.EMPHASIS_INS.equals(title)
				|| Constant.LEAK_INS.equals(title)) {
			String names ="巡查任务编号"+","+"巡查任务名称"+","
						+"工作内容描述"+","+"计划开始时间"+","
						+"计划结束时间"+","+"备注"+","+"管理单位";
			String values = bsPnInsTask.getPnitNum()+","
							+bsPnInsTask.getPnitName()+","
							+bsPnInsTask.getWorkDesc()+","
							+bsPnInsTask.getPlanSDateStr()+","
							+bsPnInsTask.getPlanEDateStr()+","
							+bsPnInsTask.getRemark()+","
							+bsPnInsTask.getManageUnit();
			Intent intent = new Intent();
			intent.setClass(context, ShowTaskMsgActivity.class);
			intent.putExtra("names", names);
			intent.putExtra("values", values);
			context.startActivity(intent);
		}
	}

	@Override
	public void groupDownloadHandler(Handler myHandler) {
		// TODO Auto-generated method stub
		if (Constant.ROUTINE_INS.equals(title)
				|| Constant.SPECIAL_INS.equals(title)
				|| Constant.EMPHASIS_INS.equals(title)
				|| Constant.LEAK_INS.equals(title)) {
			DownloadTask downloadTask = new DownloadTask(context,
					insTablePushTaskVo, true, myHandler);
			downloadTask.execute(AppContext.getInstance().getCurUser().getUserName());
		}
	}

	@Override
	public void groupControlUI(Map<String, View> vs) {
		// TODO Auto-generated method stub
		// 开始
		RelativeLayout tlBn_ks = (RelativeLayout) vs.get("tlBn_ks");
		// 开始字
		TextView tlBn_ks_tv = (TextView) vs.get("tlBn_ks_tv");
		// 填单
		RelativeLayout tlBn_td = (RelativeLayout) vs.get("tlBn_td");
		// 下载
		RelativeLayout tlBn_xz = (RelativeLayout) vs.get("tlBn_xz");
		// 下拉
		RelativeLayout tlimage_job = (RelativeLayout) vs.get("tlimage_job");
		// 完成
		LinearLayout tlBn_wc = (LinearLayout) vs.get("tlBn_wc");
		RelativeLayout tlBn_t = (RelativeLayout) vs.get("tlBn_t");
		// 显示内容
		TextView tv = (TextView) vs.get("tv");
		TextView tlTv_Title = (TextView) vs.get("tlTv_Title");
		if (Constant.ROUTINE_INS.equals(title)
				|| Constant.SPECIAL_INS.equals(title)
				|| Constant.EMPHASIS_INS.equals(title)
				|| Constant.LEAK_INS.equals(title)) {
			tlBn_ks_tv.setText("进入");
			tlBn_t.setVisibility(View.GONE);
			if ("1".equals(insTablePushTaskVo.getIsDownload())) {
				tlBn_ks.setVisibility(View.VISIBLE);
				tlBn_td.setVisibility(View.GONE);
				tlBn_xz.setVisibility(View.GONE);
				tlimage_job.setVisibility(View.VISIBLE);
				String names = insTablePushTaskVo.getNames();
				String values = insTablePushTaskVo.getValues();
				String tbType = insTablePushTaskVo.getTbType();
				
				if("4".equals(tbType)){
					tv.setTextColor(Color.YELLOW);
					tlTv_Title.setTextColor(Color.YELLOW);
				}
				
				String state = "";
				if (taskUploadStateVO != null) {
					state = taskUploadStateVO.getStatus();
				}
				if (names != null && values != null) {
					String[] nameList = names.split(",");
					String[] valuesList = values.split(",");
					String text = "";
					for (int i = 0; i < nameList.length; ++i) {
						if (i == 2) {
							break;
						}
						text = text + nameList[i] + ":" + valuesList[i] + "\n";
					}
					text = text + "任务状态：" + Constant.TASK_STATUS.get(state);
					if(bsPnInsTask!=null){
						text+="\n"+bsPnInsTask.getFrequencyDesc();
					}
					tv.setText(text);
				}
			}else{
				tlBn_td.setVisibility(View.GONE);
				tlimage_job.setVisibility(View.GONE);
				tlBn_xz.setVisibility(View.VISIBLE);
				tv.setText("该任务还没下载数据，请点击下载按钮下载！");
				tv.setTextColor(Color.RED);
			}
			
		}
	}

	@Override
	public void childControlUI(Map<String, View> vs) {
		// TODO Auto-generated method stub
		Button work_ation_loc = (Button) vs.get("work_ation_loc");
		Button work_ation_td = (Button) vs.get("work_ation_td");
		Button work_ation_msg = (Button) vs.get("work_ation_msg");
		Button work_ation_wc = (Button) vs.get("work_ation_wc");
		Button work_ation_wg = (Button) vs.get("work_ation_wg");
		TextView work_type_title = (TextView) vs.get("work_type_title");
		TextView work_message = (TextView) vs.get("work_message");
		LinearLayout planning_work_item = (LinearLayout) vs.get("planning_work_item");
		
		if(Constant.ROUTINE_INS.equals(title)){
			work_ation_td.setVisibility(View.GONE);
			work_ation_loc.setVisibility(View.VISIBLE);
			work_message.setVisibility(View.GONE);
			work_ation_msg.setVisibility(View.GONE);
			if(bsSupervisionPoint!=null){
				String lastInsDateStr = bsSupervisionPoint.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr == null ? "未巡" : lastInsDateStr;
				Long insCount = bsSupervisionPoint.getInsCount();
				insCount = insCount == null ? 0 : insCount;
				if(insCount==-1){
					String text = "";
					text = bsSupervisionPoint.getSpName()
					+ bsSupervisionPoint.getSpType() + "\n最后巡查日期:"
					+ lastInsDateStr + "\n巡查次数已够";
					work_type_title.setText(text);
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_blue);
				}else{
					String text = "";
					text = bsSupervisionPoint.getSpName()+bsSupervisionPoint.getSpType()
					+"\n最后巡查日期:"+lastInsDateStr
					+"\n巡查次数:"+insCount+"/"+bsSupervisionPoint.getFrequencyNumber();
					work_type_title.setText(text);
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_red);
				}
				work_type_title.setGravity(Gravity.LEFT);
				work_message.setText(bsSupervisionPoint.getFrequencyDesc());
			}else if(bsRoutineInsArea!=null){
				work_message.setVisibility(View.GONE);
				/*String lastInsDateStr = bsRoutineInsArea.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr == null ? "未巡" : lastInsDateStr;
				Long insCount = bsRoutineInsArea.getInsCount();
				insCount = insCount == null ? 0 : insCount;
				if(insCount==-1){
					String text = "";
					text = bsRoutineInsArea.getAreaName() + "\n最后巡查日期:"
					+ lastInsDateStr + "\n巡查次数已够";
					work_type_title.setText(text);
				}else{
					String text = "";
					text = bsRoutineInsArea.getAreaName()
					+"\n最后巡查日期:"+lastInsDateStr
					+"\n巡查次数:"+insCount;
					work_type_title.setText(text);
				}*/
				work_type_title.setText(bsRoutineInsArea.getAreaName()+
						"\n区域描述:"+bsRoutineInsArea.getAreaDesc()+
						"\n管理单位:"+bsRoutineInsArea.getManageUnit());
				work_type_title.setGravity(Gravity.CENTER);
				work_message.setText(bsRoutineInsArea.getFrequencyDesc());
				planning_work_item.setBackgroundResource(R.drawable.wx_bg);
			}
		}else if(Constant.SPECIAL_INS.equals(title)){
			work_ation_td.setVisibility(View.GONE);
			work_ation_loc.setVisibility(View.VISIBLE);
			work_message.setVisibility(View.GONE);
			work_ation_msg.setVisibility(View.GONE);
			if(bsInsFacInfo!=null){
				String lastInsDateStr = bsInsFacInfo.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr == null ? "未巡" : lastInsDateStr;
				Long insCount = bsInsFacInfo.getInsCount();
				insCount = insCount == null ? 0 : insCount;
				if(insCount==-1){
					String text = "";
					text = bsInsFacInfo.getIfiFacType()+ "\n最后巡查日期:"
					+ lastInsDateStr + "\n巡查次数已够";
					work_type_title.setText(text);
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_blue);
				}else{
					String text = "";
					text = "设施类型:"+bsInsFacInfo.getIfiFacType()
					+"\n最后巡查日期:"+lastInsDateStr
					+"\n巡查次数:"+insCount+"/"+bsInsFacInfo.getFrequencyNumber();
					work_type_title.setText(text);
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_red);
				}
				work_message.setText(bsInsFacInfo.getFrequencyDesc());
			}
		}else if(Constant.EMPHASIS_INS.equals(title)){
			work_ation_td.setVisibility(View.GONE);
			work_ation_loc.setVisibility(View.VISIBLE);
			work_message.setVisibility(View.GONE);
			work_ation_msg.setVisibility(View.GONE);
			if(bsEmphasisInsArea!=null){
				String lastInsDateStr = bsEmphasisInsArea.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr == null ? "未巡" : lastInsDateStr;
				Long insCount = bsEmphasisInsArea.getInsCount();
				insCount = insCount == null ? 0 : insCount;
				if(insCount==-1){
					String text = "";
					text = bsEmphasisInsArea.getAreaName() + "\n最后巡查日期:"
					+ lastInsDateStr + "\n巡查次数已够";
					work_type_title.setText(text);
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_blue);
				}else{
					String text = "";
					text = bsEmphasisInsArea.getAreaName()
					+"\n最后巡查日期:"+lastInsDateStr
					+"\n巡查次数:"+insCount+"/"+bsEmphasisInsArea.getFrequencyNumber();
					work_type_title.setText(text);
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_red);
				}
				work_message.setText(bsEmphasisInsArea.getFrequencyDesc());
			}
		}else if(Constant.LEAK_INS.equals(title)){
			work_ation_td.setVisibility(View.GONE);
			work_ation_loc.setVisibility(View.VISIBLE);
			work_message.setVisibility(View.GONE);
			work_ation_msg.setVisibility(View.GONE);
			if(bsLeakInsArea!=null){
				String lastInsDateStr = bsLeakInsArea.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr == null ? "未巡" : lastInsDateStr;
				Long insCount = bsLeakInsArea.getInsCount();
				insCount = insCount == null ? 0 : insCount;
				if(insCount==-1){
					String text = "";
					text = bsLeakInsArea.getAreaName() + "\n最后巡查日期:"
					+ lastInsDateStr + "\n巡查次数已够";
					work_type_title.setText(text);
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_blue);
				}else{
					String text = "";
					text = bsLeakInsArea.getAreaName  ()
					+"\n最后巡查日期:"+lastInsDateStr
					+"\n巡查次数:"+insCount+"/"+bsLeakInsArea.getFrequencyNumber();
					work_type_title.setText(text);
					planning_work_item.setBackgroundResource(R.drawable.wx_bg_red);
				}
				work_message.setText(bsLeakInsArea.getFrequencyDesc());
			}
		}
		
	}

}
