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
import com.movementinsome.app.mytask.ShowTaskMsgActivity;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.DownloadTask;
import com.movementinsome.database.vo.BsRushRepairWorkOrder;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.easyform.util.XmlFiledRuleOperater;
import com.movementinsome.kernel.initial.model.Module;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class YhTaskListCentreHandle implements TaskListCentreBaseHandle{

	private Activity context;
	// 推送信息
	private InsTablePushTaskVo insTablePushTaskVo;
	// 任务状态
	private TaskUploadStateVO taskUploadStateVO;
	// 模板
	private List<Module> taskModule;
	// 派工单
	private BsRushRepairWorkOrder bsRushRepairWorkOrder;
	// 类型
	private String title;
	
	public YhTaskListCentreHandle(Activity context,InsTablePushTaskVo insTablePushTaskVo
			,TaskUploadStateVO taskUploadStateVO,List<Module> taskModule,BsRushRepairWorkOrder bsRushRepairWorkOrder){
		this.context = context;
		this.insTablePushTaskVo=insTablePushTaskVo;
		this.taskUploadStateVO=taskUploadStateVO;
		this.taskModule= taskModule;
		this.bsRushRepairWorkOrder=bsRushRepairWorkOrder;
		if(insTablePushTaskVo!=null){
			title = insTablePushTaskVo.getTitle();
		}
				
	}
	@Override
	public void identicalHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finishHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void returnTableHandler() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void downloadHandler(Handler myHandler) {
		// TODO Auto-generated method stub
		if(Constant.RRWO_TASK.equals(title)
				||Constant.LEAK_INS.equals(title)
				||Constant.EMPHASIS_INS.equals(title)
				||Constant.SPECIAL_INS.equals(title)
				||Constant.ROUTINE_INS.equals(title)){
			DownloadTask downloadTask = new DownloadTask(context,
					insTablePushTaskVo, true, myHandler);
			downloadTask.execute(AppContext.getInstance().getCurUser().getUserName());
		}
	}

	@Override
	public void showMsgHandler() {
		// TODO Auto-generated method stub
		if(Constant.RRWO_TASK.equals(title)){
			Intent intent=new Intent();
			String names=insTablePushTaskVo.getNames();
			String values=insTablePushTaskVo.getValues();
			if(bsRushRepairWorkOrder!=null){
				String guid=bsRushRepairWorkOrder.getGuid();
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
		if(Constant.RRWO_TASK.equals(title)){
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
	public void locHandler() {
		// TODO Auto-generated method stub
		
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
		RelativeLayout tlBn_loc = (RelativeLayout) v.get("tlBn_loc");
		RelativeLayout tlBn_th = (RelativeLayout) v.get("tlBn_th");
		TextView tv = (TextView) v.get("tv");
		TextView tlTv_Title = (TextView) v.get("tlTv_Title");
		LinearLayout tlLinearContent = (LinearLayout) v.get("tlLinearContent");
		if(Constant.RRWO_TASK.equals(title)
				||Constant.LEAK_INS.equals(title)
				||Constant.EMPHASIS_INS.equals(title)
				||Constant.SPECIAL_INS.equals(title)
				||Constant.ROUTINE_INS.equals(title)){
			if ("1".equals(insTablePushTaskVo.getIsDownload())) {
				if(Constant.RRWO_TASK.equals(title)){
					tlBn_td.setVisibility(View.VISIBLE);
					tlBn_ks.setVisibility(View.GONE);
					tlBn_xz.setVisibility(View.GONE);
					tlBn_xx.setVisibility(View.VISIBLE);
					tlBn_th.setVisibility(View.GONE);
					tlBn_t.setVisibility(View.GONE);
					tv.setText(getShowText());
				}
			}else{
				tlBn_xz.setVisibility(View.VISIBLE);
				tlBn_td.setVisibility(View.GONE);
				tlBn_xx.setVisibility(View.GONE);
				tlBn_ks.setVisibility(View.GONE);
				tlBn_t.setVisibility(View.GONE);
				tlBn_wc.setVisibility(View.GONE);
				tv.setText(getDownloadFailedText());
				tv.setTextColor(Color.RED);
				tlTv_Title.setText(Constant.TASK_TITLE_CN.get(insTablePushTaskVo
						.getTitle()));
			}
		}
	}
	private String getDownloadFailedText() {
		String names = insTablePushTaskVo.getNames();
		String values = insTablePushTaskVo.getValues();
		String[] nameList = names.split(",");
		String[] valuesList = values.split(",");
		String text = "";
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
		for (int i = 0; i < nameList.length; ++i) {
			if (i == 3) {
				break;
			}
			text = text + nameList[i] + ":" + valuesList[i] + "\n";
		}
		return text;
	}
	private void action(int which) {
		
		String input = null;
		String v = null;
		String s = null;
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
		newFormInfo.putExtra("guid", bsRushRepairWorkOrder.getGuid());
		newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
		newFormInfo.putExtra("taskCategory", insTablePushTaskVo.getTaskCategory());
		newFormInfo.putExtra("tableName", insTablePushTaskVo.getTitle());
		newFormInfo.putExtra("delete", false);
		HashMap<String, String>m=new HashMap<String, String>();
		m.put("facNum", bsRushRepairWorkOrder.getFacNum());
		m.put("facType", bsRushRepairWorkOrder.getFacType());
		m.put("facDs", bsRushRepairWorkOrder.getFacDs().toString());
		m.put("facMaterial", bsRushRepairWorkOrder.getFacMaterial());
		v=bsRushRepairWorkOrder.getCoordinate();
		if(v!=null&&!"".equals(v)){
			try{
				s = v.substring(v.indexOf("(") + 1,
						v.lastIndexOf(")"));
				m.put("coordinate", s);
			}catch(Exception e){
				
			}
		}
		newFormInfo.putExtra("iParams",m);
		
		context.startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
	}
}
