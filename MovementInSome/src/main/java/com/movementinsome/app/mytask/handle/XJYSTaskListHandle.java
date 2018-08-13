package com.movementinsome.app.mytask.handle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.ShowPatrolDataActivity;
import com.movementinsome.app.mytask.ShowTaskActivity;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.DownloadTask;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TaskUploadStateVO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class XJYSTaskListHandle implements TaskListBaseHandle, PublicHandle {

	Activity context;
	// 状态
	private TaskUploadStateVO taskUploadStateVO;
	// 计划信息
	private InsPlanTaskVO insPlanTaskVO;
	// 推送
	private InsTablePushTaskVo insTablePushTaskVo;
	// 设施
	private InsPatrolDataVO insPatrolDataVO;
	private String title;

	public XJYSTaskListHandle(Activity context,
			InsTablePushTaskVo insTablePushTaskVo,
			InsPatrolDataVO insPatrolDataVO) {
		this.context = context;
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.insPatrolDataVO = insPatrolDataVO;
		title = insTablePushTaskVo.getTitle();
	}

	public XJYSTaskListHandle(Activity context,
			InsTablePushTaskVo insTablePushTaskVo,
			TaskUploadStateVO taskUploadStateVO, InsPlanTaskVO insPlanTaskVO) {
		this.context = context;
		this.taskUploadStateVO = taskUploadStateVO;
		this.insPlanTaskVO = insPlanTaskVO;
		this.insTablePushTaskVo = insTablePushTaskVo;
		if (insTablePushTaskVo != null) {
			title = insTablePushTaskVo.getTitle();
		}
	}

	@Override
	public void childLocHandler() {
		// TODO Auto-generated method stub

	}

	@Override
	public void childWriteTableHandler() {
		// TODO Auto-generated method stub

	}

	@Override
	public void childShowMsg() {
		// TODO Auto-generated method stub

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
		if (Constant.PLAN_FAC_CYCLE.equals(title)
				|| Constant.PLAN_FAC_TEMPORARY.equals(title)) {
			startWork(mModuleid);
		}
	}

	private void startWork(String mModuleid){
		if (insPlanTaskVO.getWorkTaskPEDate() != null
				&& !"".equals(insPlanTaskVO.getWorkTaskPEDate())
				&& insPlanTaskVO.getWorkTaskPSDate() != null
				&& !"".equals(insPlanTaskVO.getWorkTaskPSDate())) {
			Date workTaskPEDate = stringToDate(insPlanTaskVO
					.getWorkTaskPEDate());
			Date workTaskPSDate = stringToDate(insPlanTaskVO
					.getWorkTaskPSDate());
			Date toDate = new Date();
			if (workTaskPSDate != null) {
				if (toDate.getTime() < workTaskPSDate.getTime()) {
					new AlertDialog.Builder(context)
							.setTitle("提示")
							.setIcon(android.R.drawable.ic_menu_help)
							.setMessage("还没到计划开始时间")
							.setPositiveButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									}).show();
					return;
				}
			}
			if (workTaskPEDate != null) {
				if (toDate.getTime() > (workTaskPEDate.getTime() + 24 * 60 * 60 * 1000)) {
					new AlertDialog.Builder(context)
							.setTitle("提示")
							.setIcon(android.R.drawable.ic_menu_help)
							.setMessage("任务已结束")
							.setPositiveButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									}).show();
					return;
				}
			}
		}
		TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
				context, false, false, insTablePushTaskVo.getTaskNum(),
				Constant.UPLOAD_STATUS_START, null,
				insTablePushTaskVo.getTitle(),
				insTablePushTaskVo.getTaskCategory(), null, null, null);
		taskFeedBackAsyncTask.execute();
		Intent intent = new Intent();
		intent.setClass(context, ShowPatrolDataActivity.class);
		intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
		intent.putExtra("isShowMap", false);
		intent.putExtra("isShowMessage", false);
		intent.putExtra("isShowSearch", true);
		intent.putExtra("moduleid",mModuleid);
		context.startActivity(intent);
	}
	@Override
	public void groupReturnTableHandler() {
		// TODO Auto-generated method stub
		if (Constant.PLAN_FAC_CYCLE.equals(title)
				|| Constant.PLAN_FAC_TEMPORARY.equals(title)) {
			View v=View.inflate(context, R.layout.return_table_reason_view, null);
			final EditText return_table_reason_et = (EditText) v.findViewById(R.id.return_table_reason_et);
			TextView return_table_reason_title = (TextView) v.findViewById(R.id.return_table_reason_title);
			return_table_reason_title.setText("备注：");
			new AlertDialog.Builder(context).setTitle("提示")
			.setIcon(android.R.drawable.ic_menu_help)
			.setMessage("确定要退单，将删除数据！")
			.setView(v)
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
					String s = return_table_reason_et.getText()+"";
					if("".equals(s)){
						Toast.makeText(context, "备注不能为空", Toast.LENGTH_LONG).show();
					}else{
						TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
								context,
								true,
								false,
								insTablePushTaskVo.getTaskNum(),
								Constant.UPLOAD_STATUS_RETREAT,
								null, insTablePushTaskVo.getTitle(),
								insTablePushTaskVo.getTaskCategory(), null,
								s, null);
						taskFeedBackAsyncTask.execute();
					}
				}
			}).show();
		}
	}

	@Override
	public void groupFinishHandler() {
		// TODO Auto-generated method stub
		if (Constant.PLAN_FAC_CYCLE.equals(title)
				|| Constant.PLAN_FAC_TEMPORARY.equals(title)) {
			finishWorkDialog(null);
		}
	}
	private void finishWorkDialog(final String results) {
		
		new AlertDialog.Builder(context).setTitle("提示")
				.setIcon(android.R.drawable.ic_menu_help)
				.setMessage("确定该任务已完成，将删除数据！")
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
								context, false, false, insTablePushTaskVo
										.getTaskNum(),
								Constant.UPLOAD_STATUS_FINISH, null,
								insTablePushTaskVo.getTitle(),
								insTablePushTaskVo.getTaskCategory(), null,
								results, null);
						taskFeedBackAsyncTask.execute();
					}
				}).show();
	}

	@Override
	public void groupShowMsg() {
		// TODO Auto-generated method stub
		if (Constant.PLAN_FAC_CYCLE.equals(title)
				|| Constant.PLAN_FAC_TEMPORARY.equals(title)) {
			Intent intent = new Intent();
			intent.setClass(context, ShowTaskActivity.class);
			intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
			intent.putExtra("insPlanTaskVO", insPlanTaskVO);
			context.startActivity(intent);
		}
	}

	@Override
	public void groupDownloadHandler(Handler myHandler) {
		// TODO Auto-generated method stub
		if (Constant.PLAN_FAC_CYCLE.equals(title)
				|| Constant.PLAN_FAC_TEMPORARY.equals(title)) {
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
		// 填单
		RelativeLayout tlBn_td = (RelativeLayout) vs.get("tlBn_td");
		// 下载
		RelativeLayout tlBn_xz = (RelativeLayout) vs.get("tlBn_xz");
		// 下拉
		RelativeLayout tlimage_job = (RelativeLayout) vs.get("tlimage_job");
		// 完成
		LinearLayout tlBn_wc = (LinearLayout) vs.get("tlBn_wc");
		// 显示内容
		TextView tv = (TextView) vs.get("tv");
		TextView tlTv_Title = (TextView) vs.get("tlTv_Title");
		if (Constant.PLAN_FAC_CYCLE.equals(title)
				|| Constant.PLAN_FAC_TEMPORARY.equals(title)) {
			if ("1".equals(insTablePushTaskVo.getIsDownload())) {
				tlBn_ks.setVisibility(View.VISIBLE);
				tlBn_td.setVisibility(View.GONE);
				tlBn_xz.setVisibility(View.GONE);
				tlimage_job.setVisibility(View.VISIBLE);
				String names = insTablePushTaskVo.getNames();
				String values = insTablePushTaskVo.getValues();
				String tbType = insTablePushTaskVo.getTbType();
				
				/*if("4".equals(tbType)){
					tv.setTextColor(Color.YELLOW);
					tlTv_Title.setTextColor(Color.YELLOW);
				}*/
				
				String state = "";
				if (taskUploadStateVO != null) {
					state = taskUploadStateVO.getStatus();
				}
				if (names != null && values != null) {
					String[] nameList = names.split(",");
					String[] valuesList = values.split(",");
					String text = "";
					text = "任务编号:" + insTablePushTaskVo.getTaskNum() + "\n";
					for (int i = 0; i < nameList.length; ++i) {
						if (i == 2) {
							break;
						}
						text = text + nameList[i] + ":" + valuesList[i] + "\n";
					}
					text = text + "任务状态：" + Constant.TASK_STATUS.get(state);
					tv.setText(text);
				}
			}else{
				tlBn_td.setVisibility(View.GONE);
				tlimage_job.setVisibility(View.GONE);
				tlBn_xz.setVisibility(View.VISIBLE);
				tv.setText("该任务还没下载数据，请点击下载按钮下载！");
				tv.setTextColor(Color.RED);
			}
			if(Constant.PLAN_FAC_CYCLE.equals(title)){
				tlBn_wc.setVisibility(View.GONE);
			}else if(Constant.PLAN_FAC_TEMPORARY.equals(title)){
				tlBn_wc.setVisibility(View.VISIBLE);
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
		if(Constant.PLAN_FAC_CYCLE.equals(title)
				||Constant.PLAN_FAC_TEMPORARY.equals(title)){
			work_ation_td.setVisibility(View.GONE);
			work_ation_loc.setVisibility(View.GONE);
			work_message.setVisibility(View.VISIBLE);
			work_ation_msg.setVisibility(View.GONE);
			String lastInsDateStr = insPatrolDataVO.getLastInsDateStr();
			lastInsDateStr = lastInsDateStr == null ? "未巡" : lastInsDateStr;
			Long insCount = insPatrolDataVO.getInsCount();
			insCount = insCount == null ? 0 : insCount;
			if(insCount==-1){
				String text = "";
				if(insPatrolDataVO.getName()!=null){
					text = insPatrolDataVO.getName()
					+ insPatrolDataVO.getFacType() + "\n最后巡查日期:"
					+ lastInsDateStr + "\n巡查次数已够";
				}else{
					text = insPatrolDataVO.getNum()
					+ insPatrolDataVO.getFacType() + "\n最后巡查日期:"
					+ lastInsDateStr + "\n巡查次数已够";
				}
				work_type_title.setText(text);
			}else{
				String text = "";
				if(insPatrolDataVO.getName()!=null){
					text = insPatrolDataVO.getName()+insPatrolDataVO.getFacType()
					+"\n最后巡查日期:"+lastInsDateStr
					+"\n巡查次数:"+insCount;
				}else{
					text = insPatrolDataVO.getNum()+insPatrolDataVO.getFacType()
					+"\n最后巡查日期:"+lastInsDateStr
					+"\n巡查次数:"+insCount;
				}
				work_type_title.setText(text);
			}
			work_message.setText(insPatrolDataVO.getFrequencyDesc());
		}
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub

	}
	private Date stringToDate(String dateStr) {
		SimpleDateFormat formatDate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		try {
			Date date = formatDate.parse(dateStr);
			return date;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
