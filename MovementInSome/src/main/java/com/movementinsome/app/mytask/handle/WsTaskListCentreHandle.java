package com.movementinsome.app.mytask.handle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.mytask.ShowTaskMsgActivity;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.DownloadTask;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsDatumInaccurate;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.database.vo.WsComplainantFormVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.easyform.util.XmlFiledRuleOperater;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.map.MapBizViewer;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;


public class WsTaskListCentreHandle implements TaskListCentreBaseHandle {

	private Activity context;
	// 推送信息
	private InsTablePushTaskVo insTablePushTaskVo;
	// 任务状态
	private TaskUploadStateVO taskUploadStateVO;
	// 模板
	private List<Module> taskModule;
	// 派工单
	private WsComplainantFormVO wsComplainantFormVO;
	private InsDatumInaccurate insDatumInaccurate;
	private String taskCategory;

	public WsTaskListCentreHandle(Activity context,
			InsTablePushTaskVo insTablePushTaskVo,
			TaskUploadStateVO taskUploadStateVO, InsDatumInaccurate insDatumInaccurate, List<Module> taskModule,
			WsComplainantFormVO wsComplainantFormVO) {
		this.context = context;
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.taskUploadStateVO = taskUploadStateVO;
		this.taskModule = taskModule;
		this.wsComplainantFormVO = wsComplainantFormVO;
		this.insDatumInaccurate = insDatumInaccurate;
		if(insTablePushTaskVo!=null){
			this.taskCategory = insTablePushTaskVo.getTaskCategory();
		}
	}

	@Override
	public void finishHandler() {
		// TODO Auto-generated method stub
		if(Constant.COMPLAINANT_FORM.equals(taskCategory)){
			new AlertDialog.Builder(context).setTitle("提示")
			/*.setIcon(android.R.drawable.ic_menu_help)*/
			.setMessage("是否完成任务，并删除本地临时数据")
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
							insTablePushTaskVo.getTitle(),
							taskCategory, null,
							"", null);
					taskFeedBackAsyncTask.execute();
				}
			}).show();
		}
	}

	@Override
	public void returnTableHandler() {
		// TODO Auto-generated method stub
		
		if(Constant.COMPLAINANT_FORM.equals(taskCategory)){
			// v 是退单提示用的
			View v=View.inflate(context, R.layout.return_table_reason_view, null);
			final EditText return_table_reason_et = (EditText) v.findViewById(R.id.return_table_reason_et);
			new AlertDialog.Builder(context).setTitle("提示")
			.setIcon(android.R.drawable.ic_menu_help)
			.setMessage("确定退单，将删除数据！")
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
						Toast.makeText(context, "退单原因不能为空", Toast.LENGTH_LONG).show();
					}else{
						TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
								context,
								true,
								false,
								insTablePushTaskVo.getTaskNum(),
								Constant.UPLOAD_STATUS_RETREAT,
								null, insTablePushTaskVo.getTitle(),
								taskCategory, null,
								s, null);
						taskFeedBackAsyncTask.execute();
					}
				}
			}).show();
		}
	}

	@Override
	public void downloadHandler(Handler myHandler) {
		// TODO Auto-generated method stub
		if(Constant.COMPLAINANT_FORM.equals(taskCategory)){
			DownloadTask downloadTask = new DownloadTask(context,
					insTablePushTaskVo, true, myHandler);
			downloadTask.execute(AppContext.getInstance().getCurUser().getUserName());
		}
	}

	@Override
	public void showMsgHandler() {
		// TODO Auto-generated method stub
		if(Constant.COMPLAINANT_FORM.equals(taskCategory)||Constant.INSMAPINACCURATE_FORM.equals(taskCategory)){
			Intent intent=new Intent();
			String names=insTablePushTaskVo.getNames();
			String values=insTablePushTaskVo.getValues();
			if(wsComplainantFormVO!=null){
				String guid=wsComplainantFormVO.getGuid();
				intent.putExtra("guid", guid);
			}
			if(insDatumInaccurate!=null){
				String guid=insDatumInaccurate.getGuid();
				intent.putExtra("guid", guid);
			}
			intent.putExtra("names", names);
			intent.putExtra("values", values);
			intent.setClass(context, ShowTaskMsgActivity.class);
			context.startActivity(intent);
		}
	}

	@Override
	public void startWorkHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeTable() {
		// TODO Auto-generated method stub
		if(Constant.COMPLAINANT_FORM.equals(taskCategory)){
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
		RelativeLayout tlBn_td = (RelativeLayout) v.get("tlBn_td");
		RelativeLayout tlBn_ks = (RelativeLayout) v.get("tlBn_ks");
		RelativeLayout tlBn_xx = (RelativeLayout) v.get("tlBn_xx");
		RelativeLayout tlBn_xz = (RelativeLayout) v.get("tlBn_xz");
		RelativeLayout tlBn_t = (RelativeLayout) v.get("tlBn_t");
		LinearLayout tlBn_wc = (LinearLayout) v.get("tlBn_wc");
		LinearLayout tlBn_wc_xian = (LinearLayout) v.get("tlBn_wc_xian");
		TextView tv_tlBn_wc = (TextView) v.get("tv_tlBn_wc");
		TextView img_tlBn_wc = (TextView) v.get("img_tlBn_wc");
		RelativeLayout tlBn_loc = (RelativeLayout) v.get("tlBn_loc");
		RelativeLayout tlBn_th = (RelativeLayout) v.get("tlBn_th");
		TextView tv = (TextView) v.get("tv");
		TextView tlTv_Title = (TextView) v.get("tlTv_Title");
		LinearLayout tlLinearContent = (LinearLayout) v.get("tlLinearContent");
		
		if(Constant.COMPLAINANT_FORM.equals(taskCategory)){
			if ("1".equals(insTablePushTaskVo.getIsDownload())) {
				tlBn_td.setVisibility(View.GONE);
				tlBn_ks.setVisibility(View.GONE);
				tlBn_xz.setVisibility(View.GONE);
				tlBn_xx.setVisibility(View.GONE);
				tlBn_th.setVisibility(View.VISIBLE);
				tv.setText(getShowText());
			} else {
				tlBn_xz.setVisibility(View.VISIBLE);
				tlBn_td.setVisibility(View.GONE);
				tlBn_xx.setVisibility(View.GONE);
				tlBn_ks.setVisibility(View.GONE);
				tlTv_Title.setVisibility(View.GONE);

				tv.setText(getDownloadFailedText());
				tv.setTextColor(Color.RED);
				tlTv_Title.setText(Constant.TASK_TITLE_CN.get(insTablePushTaskVo
						.getTaskCategory()));
			}
			if("2".equals(insTablePushTaskVo.getTbType())){
				tlBn_wc.setEnabled(true);
				tv_tlBn_wc.setTextColor(Color.rgb(46, 114, 176));
				img_tlBn_wc.setBackgroundResource(R.drawable.pgd_perform2);
			}else{
				tlBn_wc.setEnabled(false);
				tv_tlBn_wc.setTextColor(Color.rgb(200, 200, 200));
				img_tlBn_wc.setBackgroundResource(R.drawable.pgd_perform1);
			}
//			if("1".equals(insTablePushTaskVo.getTbType())){
//				tlLinearContent.setBackgroundResource(R.drawable.task_busy);
//				tlBn_wc.setEnabled(false);
//				tlBn_wc.setTextColor(Color.rgb(200, 200, 200));
//			}else if("2".equals(insTablePushTaskVo.getTbType())){
//				tlLinearContent.setBackgroundResource(R.drawable.task_true);
//				tlBn_wc.setEnabled(true);
//				tlBn_wc.setTextColor(Color.WHITE);
//			}else{
//				tlLinearContent.setBackgroundResource(R.drawable.task_gzz_2);
//				tlBn_wc.setEnabled(false);
//				tlBn_wc.setTextColor(Color.rgb(200, 200, 200));
//			}
			
			
			if(null != wsComplainantFormVO && null !=wsComplainantFormVO.getReportedCoordinate()){
				tlBn_wc_xian.setVisibility(View.VISIBLE);
			}
			if(null != wsComplainantFormVO){//中山取消同号
				if(wsComplainantFormVO.getAndroidForm().equals("biz.work_task_order_zs")){
					tlBn_th.setVisibility(View.GONE);
					tlBn_wc_xian.setVisibility(View.GONE);
				}
			}
		}
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
	private String getShowText() {
		String names = insTablePushTaskVo.getNames();
		String values = insTablePushTaskVo.getValues();
		String[] nameList = names.split(",");
		String[] valuesList = values.split(",");
		String text = "";
		text = "任务编号:" + insTablePushTaskVo.getTaskNum() + "\n";
		if(wsComplainantFormVO!=null){
			text+=	"地址:" + wsComplainantFormVO.getHappenAddr() + "\n";
		}
		for (int i = 0; i < nameList.length; ++i) {
			if (i == 3) {
				break;
			}
			text = text + nameList[i] + ":" + valuesList[i] + "\n";
		}
		return text;
	}
	private void action(int which) {
		TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
				context, false, false, insTablePushTaskVo.getTaskNum(),
				Constant.UPLOAD_STATUS_WORK, null,
				insTablePushTaskVo.getTitle(),
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
		newFormInfo.putExtra("guid", wsComplainantFormVO.getGuid());//中山图片需要guid
		newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
		newFormInfo.putExtra("taskCategory", taskCategory);
		newFormInfo.putExtra("tableName", insTablePushTaskVo.getTitle());
		newFormInfo.putExtra("delete", false);
		HashMap<String, String>m=new HashMap<String, String>();
		m.put("deviceNum", wsComplainantFormVO.getDeviceNum());
		m.put("deviceType", wsComplainantFormVO.getDeviceType());
		m.put("tel", wsComplainantFormVO.getTel());
		m.put("callPhone", wsComplainantFormVO.getCallPhone());
		m.put("happenAddr", wsComplainantFormVO.getHappenAddr());
		m.put("mapLocCoordinate", wsComplainantFormVO.getReportedCoordinate());
		m.put("reportedContent", wsComplainantFormVO.getReportedContent());
		m.put("reportedCategory", wsComplainantFormVO.getReportedCategory());
		newFormInfo.putExtra("iParams",m);
		
		context.startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
	}

	@Override
	public void locHandler() {
		// TODO Auto-generated method stub
		if(Constant.COMPLAINANT_FORM.equals(taskCategory)){
			gotoMapLoc(wsComplainantFormVO.getReportedCoordinate());
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
	public void identicalHandler() {
		// TODO Auto-generated method stub
		if(Constant.COMPLAINANT_FORM.equals(taskCategory)){
			LayoutInflater factory = LayoutInflater.from(context);  
			//获得自定义对话框  
			View view = factory.inflate(R.layout.beizhu_title, null);
			final EditText edit=(EditText) view.findViewById(R.id.edit_beizhu);
			
			new AlertDialog.Builder(context)
			.setTitle("提示")
			.setView(view)
			.setIcon(android.R.drawable.ic_menu_help)
			.setMessage("确定此任务为同号任务，将删除数据！")
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
					String results = "";
					results=edit.getText().toString();
					if(!results.equals("")){
					TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
							context, true, false, insTablePushTaskVo
									.getTaskNum(),
							Constant.UPLOAD_STATUS_IDENTICAL, null,
							insTablePushTaskVo.getTitle(),
							taskCategory, null,
							results, null);
					taskFeedBackAsyncTask.execute();
					}else {
						Toast.makeText(context, "必须填写同号备注", Toast.LENGTH_SHORT).show();
					}
				}
			}).show();
		}
	}

}
