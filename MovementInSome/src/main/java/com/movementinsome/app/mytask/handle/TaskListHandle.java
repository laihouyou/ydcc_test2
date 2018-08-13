package com.movementinsome.app.mytask.handle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.mytask.DredgePlanActivity;
import com.movementinsome.app.mytask.ShowPatrolDataExpActivity;
import com.movementinsome.app.mytask.ShowTaskActivity;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.DownloadTask;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.app.server.UploadDataTask;
import com.movementinsome.database.vo.InsDredgePTask;
import com.movementinsome.database.vo.InsDredgeWTask;
import com.movementinsome.database.vo.InsFacMaintenanceData;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.util.MyDateTools;
import com.movementinsome.map.MapBizViewer;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TaskListHandle implements TaskListBaseHandle, PublicHandle {
	Activity context;
	// 状态
	private TaskUploadStateVO taskUploadStateVO;
	// 计划信息
	private InsPlanTaskVO insPlanTaskVO;
	// 模板
	private List<Module> taskModule;
	// 推送
	private InsTablePushTaskVo insTablePushTaskVo;

	// 巡检路段
	private InsPatrolDataVO insPatrolDataVO;
	// 工地
	private InsSiteManage insSiteManage;
	private InsSiteManage insSiteManageP;
	// 清梳
	private InsDredgePTask insDredgePTask;
	// 关键点
	private InsKeyPointPatrolData insKeyPointPatrolData;
	// 计划维修（阀门）
	private InsFacMaintenanceData insFacMaintenanceData;
	private InsDredgePTask insDredgePTaskP;
	private String title;
	private int choiceType = 0;
	// 区域
	private InsPatrolAreaData insPatrolAreaData;

	public TaskListHandle(Activity context, List<Module> taskModule,
			InsTablePushTaskVo insTablePushTaskVo,
			InsPatrolDataVO insPatrolDataVO, InsSiteManage insSiteManage,
			InsDredgePTask insDredgePTask,
			InsKeyPointPatrolData insKeyPointPatrolData
			,InsPatrolAreaData insPatrolAreaData) {
		this.context = context;
		this.taskModule = taskModule;
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.insPatrolDataVO = insPatrolDataVO;
		this.insSiteManage = insSiteManage;
		this.insDredgePTask = insDredgePTask;
		this.insKeyPointPatrolData = insKeyPointPatrolData;
		this.insPatrolAreaData = insPatrolAreaData;
		title=insTablePushTaskVo.getTitle();
	}

	public TaskListHandle(Activity context,
			TaskUploadStateVO taskUploadStateVO, InsPlanTaskVO insPlanTaskVO,
			InsTablePushTaskVo insTablePushTaskVo, List<Module> taskModule) {
		this.context = context;
		this.taskUploadStateVO = taskUploadStateVO;
		this.insPlanTaskVO = insPlanTaskVO;
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.taskModule = taskModule;
		if(insTablePushTaskVo!=null){
			title=insTablePushTaskVo.getTitle();
		}
	}

	@Override
	public void childLocHandler() {
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)){
			// 计划路面巡检
			String strGraph = insPatrolDataVO.getGeometryStr();
			gotoMapLoc(strGraph);
		}if(// 中山
				Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				// 计划路面巡检
				String strGraph = insPatrolDataVO.getGeometryStr();
				gotoMapLoc(strGraph);
			}else if(insPatrolAreaData !=null){
				String strGraph = insPatrolAreaData.getGeometryStr();
				gotoMapLoc(strGraph);
			}
		}else if(Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			// 排查系统
			if(insPatrolAreaData !=null){
				String strGraph = insPatrolAreaData.getGeometryStr();
				gotoMapLoc(strGraph);
			}
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			String strGraph = insDredgePTask.getGeometryStr();
			gotoMapLoc(strGraph);
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			String strGraph = insFacMaintenanceData.getCoordinate();
			gotoMapLoc(strGraph);
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			String strGraph = insKeyPointPatrolData.getKpaPosition();
			gotoMapLoc(strGraph);
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划关键点
			String strGraph = insSiteManage.getPrjBound();
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
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			// 计划路面巡检
			
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			Intent intent = new Intent();
			intent.setClass(context, DredgePlanActivity.class);
			intent.putExtra("insDredgePTask", insDredgePTask);
			intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
			context.startActivity(intent);
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("facNum", insFacMaintenanceData.getFacNum());
			params.put("triType", "1");
			showMdDialog(insTablePushTaskVo, taskModule, params, false);
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("facName", insKeyPointPatrolData.getFacName());
			params.put("facNum", insKeyPointPatrolData.getFacNum());
			params.put("coordinate", insKeyPointPatrolData.getKpaPosition());
			params.put("manageUnit", insKeyPointPatrolData.getManageUnit());
			showMdDialog(insTablePushTaskVo, taskModule, params, true);
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划工地巡检
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("constructionNum", insSiteManage.getConstructionNum());
			if (insSiteManage.getSmId() != null) {
				params.put("smId", insSiteManage.getSmId() + "");
			}
			showMdDialog(insTablePushTaskVo, taskModule, params, true);
		}

	}

	@Override
	public void childShowMsg() {
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			// 计划路面巡检
			
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			Intent intent = new Intent();
			intent.setClass(context, ShowTaskActivity.class);
			intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
			intent.putExtra("insDredgePTask", insDredgePTask);
			context.startActivity(intent);
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			Intent intent = new Intent();
			intent.setClass(context, ShowTaskActivity.class);
			intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
			intent.putExtra("insFacMaintenanceData", insFacMaintenanceData);
			context.startActivity(intent);
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划工地
			Intent intent = new Intent();
			intent.setClass(context, ShowTaskActivity.class);
			intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
			intent.putExtra("insSiteManage", insSiteManage);
			context.startActivity(intent);
			
		}
	}

	@Override
	public void childFinish() {
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			// 计划路面巡检
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			dredgeFinish();
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划工地
			smFinish();
		}
	}
	private void smFinish(){
		new AlertDialog.Builder(context)
		.setTitle("提示")
		.setIcon(android.R.drawable.ic_menu_help)
		.setMessage("确定要强制完成,将会删除该工地")
		.setPositiveButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
					}
				})
		.setNegativeButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						try {
							JSONObject smOb = new JSONObject();
							smOb.put("smId", insSiteManage.getSmId());
							smOb.put("finishCheckDate", MyDateTools
									.date2String(new Date()));
							smOb.put("state", 3);
							
							String content = smOb.toString();
							insSiteManage.setState((long) 3);
							insSiteManageP = insSiteManage;
							JSONObject ob = new JSONObject();
							// ob.put("moiNum",
							// this.getId());
							ob.put("imei", AppContext.getInstance()
									.getPhoneIMEI());
							// ob.put("guid",
							// this.getGuid());

							ob.put("workTaskNum",
									insTablePushTaskVo.getTaskNum());
							ob.put("taskCategory",
									insTablePushTaskVo
											.getTaskCategory());
							ob.put("tableName", "INS_SITE_MANAGE");
							ob.put("parentTable",
									insTablePushTaskVo.getTitle());

							ob.put("usId", AppContext.getInstance()
									.getCurUser().getUserId());
							ob.put("usUsercode", AppContext
									.getInstance().getCurUser()
									.getUserName());
							ob.put("usNameZh", AppContext
									.getInstance().getCurUser()
									.getUserAlias());

							ob.put("createDate", MyDateTools
									.date2String(new Date()));

							String gpsCoord = null;
							String mapCoord = null;
							if (AppContext.getInstance()
									.getCurLocation() != null) {
								gpsCoord = AppContext.getInstance()
										.getCurLocation()
										.getCurGpsPosition();
								mapCoord = AppContext.getInstance()
										.getCurLocation()
										.getCurMapPosition();
							}
							ob.put("gpsCoord", gpsCoord);
							ob.put("mapCoord", mapCoord);
							ob.put("content", content);
							new UploadDataTask(context,
									TaskListHandle.this, ob
											.toString()).execute();
						} catch (JSONException e1) {
							// TODO Auto-generated catch
							// block
							e1.printStackTrace();
						}
					}
				}).show();
	} 
	private void dredgeFinish(){
		new AlertDialog.Builder(context)
		.setTitle("提示")
		.setIcon(android.R.drawable.ic_menu_help)
		.setMessage("确定要强制完成")
		.setPositiveButton("取消",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// TODO Auto-generated method
						// stub
						dialog.dismiss();
					}
				})
		.setNegativeButton("确定",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {

						InsDredgeWTask insDredgeWTask = new InsDredgeWTask();
						insDredgeWTask.setDpNum(insDredgePTask
								.getSerialNumber());// 计划清疏编号
						insDredgeWTask.setState((long) 3);
						insDredgeWTask.setDpNum(insDredgePTask
								.getSerialNumber());// 计划清疏编号
						insDredgeWTask
								.setRainWellSum(getNullLong(insDredgePTask
										.getRainWellSumNow()));// 雨水检查井数量
						insDredgeWTask
								.setSewageWellSum(getNullLong(insDredgePTask
										.getSewageWellSumNow()));// 污水检查井数量
						insDredgeWTask
								.setGulliesSum(getNullLong(insDredgePTask
										.getGulliesSumNow()));// 雨水口数量
						insDredgeWTask
								.setGulliesLen(getNullLong(insDredgePTask
										.getGulliesLenNow()));// 雨水管渠长
						insDredgeWTask
								.setSewerLen(getNullLong(insDredgePTask
										.getSewerLenNow()));// 污水管渠长

						insDredgeWTask.setRainWellSumRl(getNullLong(insDredgePTask
								.getRainWellSumRl())
								+ getNullLong(insDredgeWTask
										.getRainWellSum()));// 雨水检查井数量
						insDredgeWTask.setSewageWellSumRl(getNullLong(insDredgePTask
								.getSewageWellSumRl())
								+ getNullLong(insDredgeWTask
										.getSewageWellSum()));// 污水检查井数量
						insDredgeWTask.setGulliesSumRl(getNullLong(insDredgePTask
								.getGulliesSumRl())
								+ getNullLong(insDredgeWTask
										.getGulliesSum()));// 雨水口数量
						insDredgeWTask.setGulliesLenRl(getNullLong(insDredgePTask
								.getGulliesLenRl())
								+ getNullLong(insDredgeWTask
										.getGulliesLen()));// 雨水管渠长
						insDredgeWTask.setSewerLenRl(getNullLong(insDredgePTask
								.getSewerLenRl())
								+ getNullLong(insDredgeWTask
										.getSewerLen()));// 污水管渠长

						String content = new Gson()
								.toJson(insDredgeWTask);
						insDredgePTask.setState((long) 3);
						insDredgePTaskP = insDredgePTask;
						JSONObject ob = new JSONObject();
						try {
							// ob.put("moiNum",
							// this.getId());
							ob.put("imei", AppContext.getInstance()
									.getPhoneIMEI());
							// ob.put("guid",
							// this.getGuid());

							ob.put("workTaskNum",
									insTablePushTaskVo.getTaskNum());
							ob.put("taskCategory",
									insTablePushTaskVo
											.getTaskCategory());
							ob.put("tableName", "Ins_Dredge_W_Task");
							ob.put("parentTable",
									insTablePushTaskVo.getTitle());

							ob.put("usId", AppContext.getInstance()
									.getCurUser().getUserId());
							ob.put("usUsercode", AppContext
									.getInstance().getCurUser()
									.getUserName());
							ob.put("usNameZh", AppContext
									.getInstance().getCurUser()
									.getUserAlias());

							ob.put("createDate", MyDateTools
									.date2String(new Date()));

							String gpsCoord = null;
							String mapCoord = null;
							if (AppContext.getInstance()
									.getCurLocation() != null) {
								gpsCoord = AppContext.getInstance()
										.getCurLocation()
										.getCurGpsPosition();
								mapCoord = AppContext.getInstance()
										.getCurLocation()
										.getCurMapPosition();
							}
							ob.put("gpsCoord", gpsCoord);
							ob.put("mapCoord", mapCoord);
							ob.put("content", content);
							new UploadDataTask(context,
									TaskListHandle.this, ob
											.toString()).execute();
						} catch (JSONException e1) {
							// TODO Auto-generated catch
							// block
							e1.printStackTrace();
						}
					}
				}).show();
	}

	@Override
	public void groupWriteTableHandler() {
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			// 计划路面巡检
			writeTable();
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			writeTable();
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			writeTable();
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			writeTable();
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划关键点
			writeTable();
		}
	}
	private void writeTable(){
		HashMap<String, String> params = new HashMap<String, String>();
		showMdDialog(insTablePushTaskVo, taskModule, params, true);
	}

	@Override
	public void groupStartHandler(String mModuleid) {
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)){
			// 计划路面巡检
			startWork(mModuleid,false);
		}else if(Constant.PLAN_PATROL_ZS_CYCLE.equals(title)// 中山
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)){
			startWork2(mModuleid,false);
		}
		else if(Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)){
			startWork(mModuleid,true);
		}else if(Constant.PLAN_FAC_ZS_CYCLE.equals(title)// 中山
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)){
			startWork2(mModuleid,true);
		}else if(Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			startWork2(mModuleid,true);
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
		//	startWork(mModuleid);
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
		//	startWork(mModuleid);
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			startWork(mModuleid,false);
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划工地
			startWork(mModuleid,false);
		}
		
	}
	private void startWork(final String mModuleid,final boolean isShowNearby){
		if(insPlanTaskVO==null){
			return;
		}
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
		String [] items = new String[]{
				"步行","车辆"
		}; 
		AlertDialog.Builder builder = new Builder(context); 
		builder.setTitle("选择类型")
		.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				choiceType = which;
			}
		}).setPositiveButton("取消",  new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub	
			}
		})
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(choiceType==0){
					AppContext.getInstance().setSpeedType("1");
				}else{
					AppContext.getInstance().setSpeedType("2");
				}
				TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
						context, false, false, insTablePushTaskVo.getTaskNum(),
						Constant.UPLOAD_STATUS_START, null,
						insTablePushTaskVo.getTitle(),
						insTablePushTaskVo.getTaskCategory(), null, null, null);
				taskFeedBackAsyncTask.execute();
				Intent intent = new Intent();
				intent.setClass(context, ShowPatrolDataExpActivity.class);
				intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
				intent.putExtra("moduleid",mModuleid);
				intent.putExtra("isShowNearby",isShowNearby);
				context.startActivity(intent);
			}
		});
		
	AlertDialog alertDialog = builder.create();
	alertDialog.setCancelable(false);
	alertDialog.show();
	}
	
	private void startWork2(final String mModuleid,final boolean isShowNearby){
		if(insPlanTaskVO==null){
			return;
		}
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
				// TODO Auto-generated method stub
//				if(choiceType==0){
//					AppContext.getInstance().setSpeedType("1");
//				}else{
//					AppContext.getInstance().setSpeedType("2");
//				}
				TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
						context, false, false, insTablePushTaskVo.getTaskNum(),
						Constant.UPLOAD_STATUS_START, null,
						insTablePushTaskVo.getTitle(),
						insTablePushTaskVo.getTaskCategory(), null, null, null);
				taskFeedBackAsyncTask.execute();
				Intent intent = new Intent();
				intent.setClass(context, ShowPatrolDataExpActivity.class);
				intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
				intent.putExtra("moduleid",mModuleid);
				intent.putExtra("isShowNearby",isShowNearby);
				context.startActivity(intent);
	}
	
	@Override
	public void groupReturnTableHandler() {
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)
				){
			// 计划路面巡检
			returnTable();
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			returnTable();
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			returnTable();
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			returnTable();
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划关键点
			returnTable();
		}
	}
	private void returnTable(){
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

	@Override
	public void groupFinishHandler() {
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)
				){
			// 计划路面巡检
			finishWorkDialog(null);
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			finishWorkDialog(null);
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			finishWorkDialog(null);
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			finishWorkDialog(null);
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划工地
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
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			// 计划路面巡检
			showMsg();
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			showMsg();
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			showMsg();
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			showMsg();
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划关键点
			showMsg();
		}
	}
	private void showMsg(){
		Intent intent = new Intent();
		intent.setClass(context, ShowTaskActivity.class);
		intent.putExtra("insTablePushTaskVo", insTablePushTaskVo);
		intent.putExtra("insPlanTaskVO", insPlanTaskVO);
		context.startActivity(intent);
	}
	@Override
	public void groupDownloadHandler(Handler myHandler) {
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)
				){
			// 计划路面巡检
			download(myHandler);
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			download(myHandler);
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			download(myHandler);
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			download(myHandler);
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划关键点
			download(myHandler);
		}
	}
	private void download(Handler myHandler){
		DownloadTask downloadTask = new DownloadTask(context,
				insTablePushTaskVo, true, myHandler);
		downloadTask.execute(AppContext.getInstance().getCurUser().getUserName());
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

	private void showMdDialog(final InsTablePushTaskVo insTablePushTaskVo,
			final List<Module> taskModule,
			final HashMap<String, String> params, final boolean delete) {
		if (taskModule == null) {
			Toast.makeText(context, "没有相应的模板", Toast.LENGTH_LONG).show();
			return;
		}
		if (taskModule.size() == 1) {
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, insTablePushTaskVo.getTaskNum(),
					Constant.UPLOAD_STATUS_WORK, null,
					insTablePushTaskVo.getTitle(),
					insTablePushTaskVo.getTaskCategory(), null, null, null);
			taskFeedBackAsyncTask.execute();
			Intent newFormInfo = new Intent(context, RunForm.class);
			newFormInfo.putExtra("template", taskModule.get(0).getTemplate());
			newFormInfo.putExtra("pid", insTablePushTaskVo.getGuid());
			newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
			newFormInfo.putExtra("taskCategory",
					insTablePushTaskVo.getTaskCategory());
			newFormInfo.putExtra("tableName", insTablePushTaskVo.getTitle());
			newFormInfo.putExtra("delete", delete);
			newFormInfo.putExtra("iParams", params);
			context.startActivityForResult(newFormInfo,
					Constant.FROM_REQUEST_CODE);
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
				TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
						context, false, false, insTablePushTaskVo.getTaskNum(),
						Constant.UPLOAD_STATUS_WORK, null, insTablePushTaskVo
								.getTitle(), insTablePushTaskVo
								.getTaskCategory(), null, null, null);
				taskFeedBackAsyncTask.execute();
				Intent newFormInfo = new Intent(context, RunForm.class);
				newFormInfo.putExtra("template", taskModule.get(which)
						.getTemplate());
				newFormInfo.putExtra("pid", insTablePushTaskVo.getGuid());
				newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
				newFormInfo.putExtra("taskCategory",
						insTablePushTaskVo.getTaskCategory());
				newFormInfo.putExtra("tableName", insTablePushTaskVo.getTitle());
				newFormInfo.putExtra("delete", delete);
				newFormInfo.putExtra("iParams", params);
				context.startActivityForResult(newFormInfo,
						Constant.FROM_REQUEST_CODE);
			}
		});

		vDialog.show();
	}

	@SuppressLint("ResourceAsColor")
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
		// 详细信息
		RelativeLayout tlBn_xx = (RelativeLayout) vs.get("tlBn_xx");
		// 完成
		LinearLayout tlBn_wc = (LinearLayout) vs.get("tlBn_wc");
		// 显示内容
		TextView tv = (TextView) vs.get("tv");
		TextView tlTv_Title= (TextView) vs.get("tlTv_Title");
		// layout框
		LinearLayout lys_ks = (LinearLayout) vs.get("lys_ks");
		LinearLayout tlBn_t_xian = (LinearLayout) vs.get("tlBn_t_xian");
		
		tlBn_xx.setVisibility(View.GONE);
		tlBn_ks.setVisibility(View.GONE);
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				||Constant.QSGL_JHX.equals(title)
				||Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)
				||Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)
				||Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)
				){
			
			if ("1".equals(insTablePushTaskVo.getIsDownload())) {
				/*if (Constant.QSGL_JHX.equals(insTablePushTaskVo.getTitle())
						|| Constant.WXGL_JHXWX.equals(insTablePushTaskVo.getTitle())
						|| Constant.FMGL_JHXWX.equals(insTablePushTaskVo.getTitle())) {
					tlBn_ks.setVisibility(View.GONE);
				} else {
					tlBn_ks.setVisibility(View.VISIBLE);
				}*/
				tlBn_td.setVisibility(View.GONE);
				tlBn_xz.setVisibility(View.GONE);
				tlimage_job.setVisibility(View.VISIBLE);
				String names = insTablePushTaskVo.getNames();
				String values = insTablePushTaskVo.getValues();
				String tbType = insTablePushTaskVo.getTbType();
				
				if("4".equals(tbType)){
					tv.setTextColor(Color.RED);
					tlTv_Title.setTextColor(Color.RED);
				}
				
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
			} else {
				tlBn_td.setVisibility(View.GONE);
				tlimage_job.setVisibility(View.GONE);
				tlBn_xz.setVisibility(View.VISIBLE);
				tlTv_Title.setVisibility(View.GONE);
				tlBn_t_xian.setVisibility(View.VISIBLE);
				tv.setText("该任务还没下载数据，请点击下载按钮下载！");
				tv.setTextColor(Color.RED);
			}
			if (Constant.CYCLE_PLAN.equals(insTablePushTaskVo.getTitle())
					|| Constant.PLAN_VALVE_CYCLE.equals(title)
					|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
					// 中山
					|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
					|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
					
					|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
					
					|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)
					
					) {// 巡检管理:计划巡检
				tlBn_wc.setVisibility(View.GONE);
			} else if (Constant.INTERIM_PLAN.equals(insTablePushTaskVo.getTitle())
					|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
					|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
					// 中山
					|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
					|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
					|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
					|| Constant.PLAN_PATROL_SCHEDULE.equals(title)) {// 巡检管理:
																						// 临时巡检
				if ("1".equals(insTablePushTaskVo.getIsDownload())) {
					tlBn_wc.setVisibility(View.VISIBLE);
				}
			} else if (Constant.WXGL_JHXWX.equals(insTablePushTaskVo.getTitle())
					|| Constant.FMGL_JHXWX.equals(insTablePushTaskVo.getTitle())) {// 计划维修
				if ("1".equals(insTablePushTaskVo.getIsDownload())) {
					tlBn_wc.setVisibility(View.VISIBLE);
				}
			}else if (Constant.PLAN_CONSTRUCTION_CYCLE.equals(insTablePushTaskVo.getTitle())
					|| Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(insTablePushTaskVo.getTitle())) {// 计划工地
				tlBn_wc.setVisibility(View.VISIBLE);
			}
//			if(Constant.CYCLE_PLAN.equals(insTablePushTaskVo.getTitle())||Constant.INTERIM_PLAN.equals(insTablePushTaskVo.getTitle())){
//				lys_ks.setBackgroundResource(R.drawable.task_gzz_yellow2);
//			}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(insTablePushTaskVo.getTitle())||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
//				lys_ks.setBackgroundResource(R.drawable.task_gzz_green2);
//			}else if(Constant.PLAN_VALVE_CYCLE.equals(insTablePushTaskVo.getTitle())||Constant.PLAN_VALVE_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
//				lys_ks.setBackgroundResource(R.drawable.task_gzz_blue_2);
//			}else if(Constant.PLAN_HYDRANT_CYCLE.equals(insTablePushTaskVo.getTitle())||Constant.PLAN_HYDRANT_TEMPORARY.equals(insTablePushTaskVo.getTitle())){
//				lys_ks.setBackgroundResource(R.drawable.task_gzz_red2);
//			}
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
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				){
			// 计划路面巡检
			if ("签到点".equals(insPatrolDataVO.getFacType())) {
				work_type_title.setText(insPatrolDataVO.getName());
				work_ation_td.setVisibility(View.GONE);
				work_message.setVisibility(View.VISIBLE);
				work_message.setText("签到点");
				work_ation_loc.setVisibility(View.VISIBLE);
				work_ation_msg.setVisibility(View.GONE);
			} else {
				work_ation_td.setVisibility(View.GONE);
				work_ation_loc.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				work_ation_msg.setVisibility(View.GONE);
				String lastInsDateStr = insPatrolDataVO.getLastInsDateStr();
				lastInsDateStr = lastInsDateStr == null ? "未巡" : lastInsDateStr;
				Long insCount = insPatrolDataVO.getInsCount();
				insCount = insCount == null ? 0 : insCount;
				if(insCount==-1){
					work_type_title.setText(insPatrolDataVO.getName()
							+ insPatrolDataVO.getFacType() + "\n最后巡查日期:"
							+ lastInsDateStr + "\n巡查次数已够");
				}else{
					work_type_title.setText(insPatrolDataVO.getName()
							+ insPatrolDataVO.getFacType() + "\n最后巡查日期:"
							+ lastInsDateStr + "\n巡查次数:" + insCount);
				}
				work_message.setText(insPatrolDataVO.getFrequencyDesc());
			}
		}else if(// 中山
				Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)){
			if(insPatrolDataVO!=null){
				// 计划路面巡检
				if ("签到点".equals(insPatrolDataVO.getFacType())) {
					String cishu="";
					if(insPatrolDataVO.getInsCount()==null){
						cishu="0";
					}else{
						cishu=insPatrolDataVO.getInsCount().toString();
					}
					work_type_title.setText(insPatrolDataVO.getName()+"\n"+"巡查次数:"+insPatrolDataVO.getFrequencyNumber()+"\n"+"已巡次数:"+cishu);
					work_ation_td.setVisibility(View.GONE);
					work_message.setVisibility(View.GONE);
					work_ation_loc.setVisibility(View.VISIBLE);
					work_ation_msg.setVisibility(View.GONE);
				} else {
					work_ation_td.setVisibility(View.GONE);
					work_ation_loc.setVisibility(View.VISIBLE);
					work_message.setVisibility(View.VISIBLE);
					work_ation_msg.setVisibility(View.GONE);
					String lastInsDateStr = insPatrolDataVO.getLastInsDateStr();
					lastInsDateStr = lastInsDateStr == null ? "未巡" : lastInsDateStr;
					Long insCount = insPatrolDataVO.getInsCount();
					insCount = insCount == null ? 0 : insCount;
					if(insCount==-1){
						work_type_title.setText(insPatrolDataVO.getName()
								+ insPatrolDataVO.getFacType() + "\n最后巡查日期:"
								+ lastInsDateStr + "\n巡查次数已够");
					}else{
						work_type_title.setText(insPatrolDataVO.getName()
								+ insPatrolDataVO.getFacType() + "\n最后巡查日期:"
								+ lastInsDateStr + "\n巡查次数:" + insCount);
					}
					work_message.setText(insPatrolDataVO.getFrequencyDesc());
				}
			}else if(insPatrolAreaData!=null){
				work_ation_td.setVisibility(View.GONE);
				work_ation_loc.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				work_ation_msg.setVisibility(View.GONE);
				String text = insPatrolAreaData.getAreaName();
				work_type_title.setText(text);
				work_message.setText("区域名称");
			}
		}else if(// 排查
				Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			if(insPatrolAreaData!=null){
				work_ation_td.setVisibility(View.GONE);
				work_ation_loc.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				work_ation_msg.setVisibility(View.GONE);
				String text = insPatrolAreaData.getAreaName();
				work_type_title.setText(text);
				work_message.setText("区域名称");
			}
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			Long state = insDredgePTask.getState();// 状态 0未有处理1正在处理2已完成3强制完成
			if (state == 2 || state == 3) {
				work_ation_loc.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.VISIBLE);
				work_ation_td.setVisibility(View.GONE);
				work_ation_wc.setVisibility(View.GONE);
				work_type_title.setText("路段:" + insDredgePTask.getRoad() + "\n"
						+ "作业开始时间段:" + insDredgePTask.getPlanSTime()
						+ "\n作业结束时间段:" + insDredgePTask.getPlanETime());
				work_message.setText("该路段已完成");
			} else {
				work_ation_td.setVisibility(View.VISIBLE);
				work_ation_wc.setVisibility(View.VISIBLE);
				work_ation_loc.setVisibility(View.VISIBLE);
				work_message.setVisibility(View.GONE);
				work_type_title.setText("路段:" + insDredgePTask.getRoad() + "\n"
						+ "作业开始时间段:" + insDredgePTask.getPlanSTime()
						+ "\n作业结束时间段:" + insDredgePTask.getPlanETime());
			}
			work_ation_msg.setVisibility(View.VISIBLE);
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			work_ation_loc.setVisibility(View.VISIBLE);
			work_message.setVisibility(View.GONE);
			work_type_title.setText("设施名称:"
					+ insFacMaintenanceData.getFacName() + "\n" + "详细位置:"
					+ insFacMaintenanceData.getFmdAddr() + "\n故障内容:"
					+ insFacMaintenanceData.getReportedContent());
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			work_ation_td.setVisibility(View.GONE);
			work_ation_loc.setVisibility(View.VISIBLE);
			work_message.setVisibility(View.VISIBLE);
			work_type_title.setText("关键点名称:"
					+ insKeyPointPatrolData.getKpaName() + "\n" + "详细位置:"
					+ insKeyPointPatrolData.getKpaAddr() + "\n巡查次数:"
					+ insKeyPointPatrolData.getInsCount());
			work_message.setText(insKeyPointPatrolData.getFrequencyDesc());
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划工地
			work_ation_td.setVisibility(View.GONE);
			work_ation_loc.setVisibility(View.VISIBLE);
			work_message.setVisibility(View.VISIBLE);
			work_ation_wc.setVisibility(View.VISIBLE);
			work_ation_wg.setVisibility(View.VISIBLE);
			work_type_title.setText("项目名称:" + insSiteManage.getProjectName()
					+ "\n" + "详细位置:" + insSiteManage.getConstructionAddr()
					+ "\n项目负责人:" + insSiteManage.getProjectLeader());
			work_message.setText(insSiteManage.getFrequencyDesc());
			work_ation_msg.setVisibility(View.VISIBLE);
		}
	}

	private Long getNullLong(Long v) {
		return v == null ? 0 : v;
	}

	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		if(Constant.CYCLE_PLAN.equals(title)
				||Constant.INTERIM_PLAN.equals(title)
				|| Constant.PLAN_VALVE_CYCLE.equals(title)
				|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
				|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
				|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)
				// 中山
				|| Constant.PLAN_FAC_ZS_CYCLE.equals(title)
				|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
				|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
				
				|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
				|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
				|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
				|| Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
			// 计划路面巡检
			Intent intent = new Intent();
			intent.setAction(com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION);
			context.sendBroadcast(intent);
		}else if(Constant.QSGL_JHX.equals(title)){
			// 计划清梳
			// 清梳更新
			if (insDredgePTaskP != null) {
				Dao<InsDredgePTask, Long> insDredgePTaskDao;
				try {
					insDredgePTaskDao = AppContext.getInstance().getAppDbHelper()
							.getDao(InsDredgePTask.class);
					insDredgePTaskDao.update(insDredgePTaskP);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Intent intent = new Intent();
			intent.setAction(com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION);
			context.sendBroadcast(intent);
		}else if(Constant.WXGL_JHXWX.equals(title)
				||Constant.FMGL_JHXWX.equals(title)){
			// 计划维修
			Intent intent = new Intent();
			intent.setAction(com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION);
			context.sendBroadcast(intent);
		}else if(Constant.XJGL_GJD_CYCLE.equals(title)
				||Constant.XJGL_GJD_TEMPORARY.equals(title)){
			// 计划关键点
			Intent intent = new Intent();
			intent.setAction(com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION);
			context.sendBroadcast(intent);
		}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			// 计划工地
			if(insSiteManageP!=null){
				Dao<InsSiteManage, Long> insSiteManageDao;
				try {
					insSiteManageDao = AppContext.getInstance().getAppDbHelper()
							.getDao(InsSiteManage.class);
					insSiteManageDao.delete(insSiteManageP);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Intent intent = new Intent();
			intent.setAction(com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION);
			context.sendBroadcast(intent);
		}
		
		
	}

	@Override
	public void childComplete() {
		// TODO Auto-generated method stub
		if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){
			List<Module> lstModule = new ArrayList<Module>();
			for(Module module : AppContext.getInstance().getModuleData()){
				if(Constant.BIZ_CONSTRUCTION.equals(module.getId())){
					lstModule.add(module);
					break;
				}
			}
			if(lstModule.size()>0){
				// 计划工地
				Intent newFormInfo = new Intent(context, RunForm.class);
				newFormInfo.putExtra("template", lstModule.get(0).getTemplate());
				newFormInfo.putExtra("pid", insSiteManage.getId());
				newFormInfo.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
				newFormInfo.putExtra("taskCategory", insTablePushTaskVo.getTaskCategory());
				newFormInfo.putExtra("tableName", insTablePushTaskVo.getTitle());
				newFormInfo.putExtra("delete", true);
				HashMap<String, String>params=new HashMap<String, String>();
				params.put("constructionNum", insSiteManage.getConstructionNum());
				if (insSiteManage.getSmId() != null) {
					params.put("smId", insSiteManage.getSmId() + "");
				}
				params.put("state", "4");
				newFormInfo.putExtra("iParams",params);
				context.startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
			}else{
				Toast.makeText(context, "没有相应的表单填写，无法完工", Toast.LENGTH_LONG).show();
			}
			
		}
	}
}
