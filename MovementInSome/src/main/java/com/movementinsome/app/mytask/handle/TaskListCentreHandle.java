package com.movementinsome.app.mytask.handle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.ShowTaskActivity;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.DownloadTask;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsComplainantForm;
import com.movementinsome.database.vo.InsDatumInaccurate;
import com.movementinsome.database.vo.InsHiddenDangerReport;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.InsTableSaveDataVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.easyform.util.XmlFiledRuleOperater;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.util.JsonAnalysisUtil;
import com.movementinsome.map.MapBizViewer;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;


public class TaskListCentreHandle implements TaskListCentreBaseHandle {
	private Activity context;
	// 推送信息
	private InsTablePushTaskVo insTablePushTaskVo;
	// 任务状态
	private TaskUploadStateVO taskUploadStateVO;
	// 下载任务
	private InsTableSaveDataVo insTableSaveDataVo;
	// 投诉信息
	private InsComplainantForm insComplainantForm;
	// 问题信息
	private InsHiddenDangerReport insHiddenDangerReport;
	// 图于现场不符
	private InsDatumInaccurate insDatumInaccurate;
	// 模板
	private List<Module> taskModule;
	private String taskCategory;
	private String title;

	public TaskListCentreHandle(Activity context,
			InsTablePushTaskVo insTablePushTaskVo,
			TaskUploadStateVO taskUploadStateVO,
			InsTableSaveDataVo insTableSaveDataVo,
			InsComplainantForm insComplainantForm,
			InsHiddenDangerReport insHiddenDangerReport, InsDatumInaccurate insDatumInaccurate, List<Module> taskModule) {
		this.context = context;
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.taskUploadStateVO = taskUploadStateVO;
		this.insTableSaveDataVo = insTableSaveDataVo;
		this.insComplainantForm = insComplainantForm;
		this.insHiddenDangerReport = insHiddenDangerReport;
		this.insDatumInaccurate = insDatumInaccurate;
		this.taskModule = taskModule;
		if(insTablePushTaskVo!=null){
			this.taskCategory = insTablePushTaskVo.getTaskCategory();
			this.title = insTablePushTaskVo.getTitle();
		}
		this.initDataObject();
	}

	private String getShowText() {
		String names = insTablePushTaskVo.getNames();
		String values = insTablePushTaskVo.getValues();
		String state = "";
		String text = "";
		if (taskUploadStateVO != null) {
			state = taskUploadStateVO.getStatus();
		}
		if (names != null && values != null) {
			String[] nameList = names.split(",");
			String[] valuesList = values.split(",");

			text = "任务编号:" + insTablePushTaskVo.getTaskNum() + "\n";
			if (Constant.COMPLAINANT.equals(title)) {
				String t = insComplainantForm.getCfDate();
				t = t == null ? "" : t;
				String d = insComplainantForm.getCfAddr();
				d = d == null ? "" : d;
				String c = insComplainantForm.getCfContent();
				c = c == null ? "" : c;
				text += "投诉时间:" + t + "\n地点:" + d + "\n投诉内容:" + c;
			} else if (Constant.PROBLEM.equals(title)) {
				String t = insHiddenDangerReport.getReportedDate();
				t = t == null ? "" : t;
				String d = insHiddenDangerReport.getAddr();
				d = d == null ? "" : d;
				String c = insHiddenDangerReport.getReportedContent();
				c = c == null ? "" : c;
				text += "发生地址:" + d + "\n反映时间:" + t + "\n反映内容:" + c;
			} else if (Constant.INSMAPINACCURATE_FORM.equals(title)) {
				String t = insDatumInaccurate.getReportedDate();
				t = t == null ? "" : t;
				String d = insDatumInaccurate.getSections();
				d = d == null ? "" : d;
				String c = insDatumInaccurate.getProblemDesc();
				c = c == null ? "" : c;
				text +=	"反映时间:" + t + "\n反映内容:" + c;
			}
			text = text + "\n任务状态:" + Constant.TASK_STATUS.get(state);

		}
		return text;
	}

	private String getDownloadFailedText() {
		String names = insTablePushTaskVo.getNames();
		String values = insTablePushTaskVo.getValues();
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
		text += "该任务还没下载数据，请点击下载按钮下载！";
		return text;
	}

	private void action(int which) {
		TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
				context, false, false, insTablePushTaskVo.getTaskNum(),
				Constant.UPLOAD_STATUS_WORK, null,
				title,
				taskCategory, null, null, null);
		taskFeedBackAsyncTask.execute();
		String input = null;
		try {
			input = XmlFiledRuleOperater.getFormInParams(context, taskModule
					.get(which).getTemplate());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent newFormInfo = new Intent(context, RunForm.class);
		newFormInfo.putExtra("template", taskModule.get(which).getTemplate());
		newFormInfo.putExtra("pid", insTablePushTaskVo.getGuid());
		newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
		newFormInfo.putExtra("taskCategory",
				taskCategory);
		newFormInfo.putExtra("tableName", title);
		if (Constant.COMPLAINANT.equals(title)) {
			newFormInfo.putExtra("iParams",
					getParams(insComplainantForm, input));
		} else if (Constant.PROBLEM.equals(title)) {
			newFormInfo.putExtra("iParams",
					getParams(insHiddenDangerReport, input));
		} else if (Constant.INSMAPINACCURATE_FORM.equals(title)){
			newFormInfo.putExtra("guid", insDatumInaccurate.getGuid());
			newFormInfo.putExtra("iParams",
					getParams(insDatumInaccurate, input));
			HashMap<String, String>m=new HashMap<String, String>();
			m.put("facilitiesNum", insDatumInaccurate.getFacilitiesNum());
			m.put("deviceType", insDatumInaccurate.getDeviceType());
			m.put("facilitiesCaliber", String.valueOf(insDatumInaccurate.getFacilitiesCaliber()));
			m.put("facilitiesMaterial", insDatumInaccurate.getFacilitiesMaterial());
			m.put("guid", insDatumInaccurate.getGuid());
			m.put("problemType", insDatumInaccurate.getProblemType());
			m.put("problemDesc", insDatumInaccurate.getProblemDesc());
			m.put("sections", insDatumInaccurate.getSections());
			m.put("sectionsNum", insDatumInaccurate.getSectionsNum());
			m.put("serialNumber", insDatumInaccurate.getSerialNumber());
			newFormInfo.putExtra("iParams",m);
		}
		context.startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
	}

	private HashMap<String, String> getParams(Object ob, String input) {

		HashMap<String, String> params = new HashMap<String, String>();
		if (input != null) {
			Class c = ob.getClass();
			Method[] m = c.getMethods();
			for (int j = 0; j < m.length; j++) {
				String name = m[j].getName();
				if (name.contains("get")) {
					for (String i : input.split("\\,")) {
						if (name.toLowerCase()
								.equals(("get" + i).toLowerCase())) {
							try {
								params.put(i, m[j].invoke(ob, new Object[] {})
										.toString());
								break;
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}
			}

		}
		params.put("triType", "0");
		return params;
	}
	
	@Override
	public void returnTableHandler() {
		if (Constant.COMPLAINANT.equals(title)
				||Constant.PROBLEM.equals(title)
				||Constant.INSMAPINACCURATE_FORM.equals(title)) {
			new AlertDialog.Builder(context).setTitle("提示")
			.setIcon(android.R.drawable.ic_menu_help)
			.setMessage("确定退单，将删除数据！")
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
							null, title,
							taskCategory, null,
							null, null);
					taskFeedBackAsyncTask.execute();
				}
			}).show();
		}
		
	}

	// 完成按钮处理事件
	@Override
	public void finishHandler() {
		if (Constant.COMPLAINANT.equals(title)
				||Constant.PROBLEM.equals(title)) {
			if (insTableSaveDataVo != null
					&& "1".equals(insTableSaveDataVo.getTbState())) {
				if (Constant.COMPLAINANT.equals(title)) {
					finishWorkDialog(insComplainantForm.getResults());
				} else if (Constant.PROBLEM.equals(title)) {
					finishWorkDialog(insHiddenDangerReport.getResults());
				} else if (Constant.INSMAPINACCURATE_FORM.equals(title)){
					finishWorkDialog(insDatumInaccurate.getRemarks());
				}
			} else {
				forceFinishWorkDialog();
			}
		}else if(Constant.INSMAPINACCURATE_FORM.equals(title)){
			if (insTablePushTaskVo != null
					&& "2".equals(insTablePushTaskVo.getTbType())) {
				finishWorkDialog(insDatumInaccurate.getHandleResult());
			}
		}
	}

	private void finishWorkDialog(final String results) {
		new AlertDialog.Builder(context).setTitle("提示")
				.setIcon(android.R.drawable.ic_menu_help)
				.setMessage("确定要结束任务，将删除数据！")
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
								context, true, false, insTablePushTaskVo
										.getTaskNum(),
								Constant.UPLOAD_STATUS_FINISH, null,
								title,
								taskCategory, null,
								results, null);
						taskFeedBackAsyncTask.execute();
					}
				}).show();
	}

	int choice = 0;

	private void forceFinishWorkDialog() {
		final String[] remarks = new String[] { "没发现问题", "不在工作范围内" };

		new AlertDialog.Builder(context)
				.setTitle("确定要结束任务，将删除数据！")
				.setSingleChoiceItems(remarks, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								choice = which;
							}
						})
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
						Map<String, String> parameters = new HashMap<String, String>();
						parameters.put("remarks", remarks[choice]);
						TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
								context, true, false, insTablePushTaskVo
										.getTaskNum(),
								Constant.UPLOAD_STATUS_FINISH, null,
								title,
								taskCategory, null,
								null, parameters);
						taskFeedBackAsyncTask.execute();
					}
				}).show();
	}
	@Override
	public void downloadHandler(Handler myHandler) {
		if (Constant.COMPLAINANT.equals(title)
				||Constant.PROBLEM.equals(title)
				||Constant.INSMAPINACCURATE_FORM.equals(title)) {
			DownloadTask downloadTask = new DownloadTask(context,
					insTablePushTaskVo, true, myHandler);
			downloadTask.execute(AppContext.getInstance().getCurUser().getUserName());
		}
	}
	
	private void initDataObject() {
		if (Constant.XJFL_YJ.equals(taskCategory)// 巡检管理:应急
				|| Constant.QSGL_YJQS.equals(insTablePushTaskVo
						.getTaskCategory())// 应急清疏
				|| Constant.WXGL_XCQX.equals(insTablePushTaskVo
						.getTaskCategory())// 现场抢修
				|| Constant.TASK_VALVE_REPAIRING.equals(insTablePushTaskVo
						.getTaskCategory())) {// 阀门管理:现场抢修

			if (insTableSaveDataVo != null) {
				try {
					JSONObject insComplainantFormJOB = new JSONObject(
							insTableSaveDataVo.getTbData());
					if (Constant.COMPLAINANT.equals(insTablePushTaskVo
							.getTitle())) {
						JsonAnalysisUtil.setJsonObjectData(
								insComplainantFormJOB, insComplainantForm);
					} else if (Constant.PROBLEM.equals(insTablePushTaskVo
							.getTitle())) {
						JsonAnalysisUtil.setJsonObjectData(
								insComplainantFormJOB, insHiddenDangerReport);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	@Override
	public void showMsgHandler() {
		if (Constant.COMPLAINANT.equals(title)
				||Constant.PROBLEM.equals(title)
				//||Constant.INSMAPINACCURATE_FORM.equals(title)
				) {
			Intent intent = new Intent();
			intent.setClass(context, ShowTaskActivity.class);
			intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
			intent.putExtra("insComplainantForm", insComplainantForm);
			intent.putExtra("insHiddenDangerReport", insHiddenDangerReport);
			intent.putExtra("insDatumInaccurate", insDatumInaccurate);
			context.startActivity(intent);
		}
		
	}
	@Override
	public void startWorkHandler() {
		if (Constant.COMPLAINANT.equals(title)
				||Constant.PROBLEM.equals(title)
				||Constant.INSMAPINACCURATE_FORM.equals(title)) {
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, insTablePushTaskVo.getTaskNum(),
					Constant.UPLOAD_STATUS_START, null,
					title,
					taskCategory, null, null, null);
			taskFeedBackAsyncTask.execute();
		}
	}

	@Override
	public void writeTable() {
		if (Constant.COMPLAINANT.equals(title)
				||Constant.PROBLEM.equals(title)
				||Constant.INSMAPINACCURATE_FORM.equals(title)) {
			if (taskModule == null) {
				Toast.makeText(context, "没有相应的模板", Toast.LENGTH_LONG).show();
				return;
			}
			if (taskModule.size() == 1) {
				action(0);
				return;
			}
			String[] itmes = new String[taskModule.size()];

			for (int i = 0; i < itmes.length; ++i) {
				itmes[i] = taskModule.get(i).getName();
			}
			AlertDialog.Builder vDialog = new AlertDialog.Builder(context);
			vDialog.setTitle("选择工作类型");
			vDialog.setItems(itmes, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					action(which);
				}
			});
			vDialog.show();
		}
	}

	@Override
	public void controlUI(Map<String, View> v) {
		// TODO Auto-generated method stub
		RelativeLayout tlBn_td=(RelativeLayout) v.get("tlBn_td");
		RelativeLayout tlBn_ks=(RelativeLayout) v.get("tlBn_ks");
		RelativeLayout tlBn_xx=(RelativeLayout) v.get("tlBn_xx");
		RelativeLayout tlBn_xz= (RelativeLayout) v.get("tlBn_xz");
		RelativeLayout tlBn_t=(RelativeLayout) v.get("tlBn_t");
		RelativeLayout tlBn_loc = (RelativeLayout) v.get("tlBn_loc");
		LinearLayout tlBn_wc =(LinearLayout) v.get("tlBn_wc");
		TextView tltv_titilename = (TextView) v.get("tltv_titilename");
		TextView tv_tlBn_wc = (TextView) v.get("tv_tlBn_wc");
		TextView img_tlBn_wc = (TextView) v.get("img_tlBn_wc");
		
		TextView tv=(TextView) v.get("tv");
		TextView tlTv_Title=(TextView) v.get("tlTv_Title");
		if (Constant.COMPLAINANT.equals(title)
				||Constant.PROBLEM.equals(title)) {
			if ("1".equals(insTablePushTaskVo.getIsDownload())) {
				tlBn_td.setVisibility(View.VISIBLE);
				tlBn_ks.setVisibility(View.GONE);
				tlBn_xz.setVisibility(View.GONE);
				tlBn_xx.setVisibility(View.VISIBLE);
				tv.setText(getShowText());
			} else {
				tlBn_xz.setVisibility(View.VISIBLE);
				tlBn_td.setVisibility(View.GONE);
				tlBn_xx.setVisibility(View.GONE);
				tlBn_ks.setVisibility(View.GONE);

				tv.setText(getDownloadFailedText());
				tv.setTextColor(Color.RED);
			}
//			tlTv_Title.setText(Constant.TASK_TITLE_CN.get(insTablePushTaskVo
//					.getTaskCategory()));
		}else if(Constant.INSMAPINACCURATE_FORM.equals(title)){
			if ("1".equals(insTablePushTaskVo.getIsDownload())) {
				tlBn_xx.setVisibility(View.VISIBLE);
				tv.setText(getShowText());
				tltv_titilename.setText("图形纠错");
				tlBn_loc.setVisibility(View.VISIBLE);
			} else {
				tlBn_xz.setVisibility(View.VISIBLE);
				tlBn_td.setVisibility(View.GONE);
				tlBn_xx.setVisibility(View.GONE);
				tlBn_ks.setVisibility(View.GONE);
				tlTv_Title.setVisibility(View.GONE);

				tv.setText(getDownloadFailedText());
				tv.setTextColor(Color.RED);
				tltv_titilename.setText("图形纠错");
			}
//			tlTv_Title.setText(Constant.TASK_TITLE_CN.get(insTablePushTaskVo
//					.getTaskCategory()));
			if("2".equals(insTablePushTaskVo.getTbType())){
				tlBn_wc.setEnabled(true);
				tv_tlBn_wc.setTextColor(Color.rgb(46, 114, 176));
				img_tlBn_wc.setBackgroundResource(R.drawable.pgd_perform2);
			}else{
				tlBn_wc.setEnabled(false);
				tv_tlBn_wc.setTextColor(Color.rgb(200, 200, 200));
				img_tlBn_wc.setBackgroundResource(R.drawable.pgd_perform1);
			}
		}else if(Constant.RRWO_TASK.equals(title)
				||Constant.LEAK_INS.equals(title)
				||Constant.EMPHASIS_INS.equals(title)
				||Constant.SPECIAL_INS.equals(title)
				||Constant.ROUTINE_INS.equals(title)){
			
		}else{
			tltv_titilename.setText("新任务");
		}
		
	}

	@Override
	public void locHandler() {
		// TODO Auto-generated method stub
		if(Constant.INSMAPINACCURATE_FORM.equals(title)){
			gotoMapLoc(insDatumInaccurate.getCoordinate());
		}
	}

	private void gotoMapLoc(String sections) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClass(context, MapBizViewer.class);
		intent.putExtra("strGraph", sections);
		intent.putExtra("type", 10006);
		((Activity) context).startActivity(intent);
	}

	@Override
	public void identicalHandler() {
		// TODO Auto-generated method stub
		
	}
}
