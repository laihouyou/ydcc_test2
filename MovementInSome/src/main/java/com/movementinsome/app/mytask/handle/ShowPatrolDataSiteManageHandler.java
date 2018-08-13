package com.movementinsome.app.mytask.handle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.mytask.adapter.PlanWorkAdapter;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.remind.InsPlanManage;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.site.SitePlanOperate;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.initial.model.Module;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// 工地巡检处理类
public class ShowPatrolDataSiteManageHandler implements ShowPatrolDataBaseHandle{

	private InsTablePushTaskVo insTablePushTaskVo;
	private String title;
	private List<InsSiteManage> smList;
	private List<InsSiteManage> smHadDoDataCache;
	private SitePlanOperate sitePlanOperate;// 工地
	private NearObject nearObject;
	private String guid;
	private Activity activity;
	private List<Module> lstModule;// 所有表单配置
	//private TextToSpeech tts;
	
	public ShowPatrolDataSiteManageHandler(Activity activity,InsTablePushTaskVo insTablePushTaskVo,List<Module> lstModule,TextToSpeech tts){
		this.insTablePushTaskVo = insTablePushTaskVo;
		this.activity = activity;
		this.lstModule = lstModule;
		//this.tts = tts;
		if(insTablePushTaskVo!=null){
			this.title=insTablePushTaskVo.getTitle();
		}
	};
	@Override
	public void controlUI(Map<String, View> v) {
		// TODO Auto-generated method stub
		if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){// 工地
			LinearLayout show_work_message_ly2=(LinearLayout) v.get("show_work_message_ly2");
			Button show_patrol_data_write2 = (Button) v.get("show_patrol_data_write2");
			
			show_work_message_ly2.setVisibility(View.GONE);
			show_patrol_data_write2.setVisibility(View.GONE);
		}
	}

	@Override
	public void updateUI(Map<String, View> v) {
		// TODO Auto-generated method stub
		if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){// 工地
			RelativeLayout show_qdd_msg_rl = (RelativeLayout) v.get("show_qdd_msg_rl");
			TextView show_qdd_msg =  (TextView) v.get("show_qdd_msg");
			nearObject = sitePlanOperate.getNearSite();
			if (nearObject != null) {
				show_qdd_msg_rl.setVisibility(View.VISIBLE);
				show_qdd_msg.setText("距离" + nearObject.getObjName() + "大约还有："
						+ (int) nearObject.getMinDistance() + "米");
				/*tts.speak(show_qdd_msg.getText().toString(),
						TextToSpeech.QUEUE_ADD, null);*/
//				BNTTSPlayer.playTTSText(show_qdd_msg.getText().toString(), -1);
			} else {
				show_qdd_msg_rl.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void initPatrolData() {
		// TODO Auto-generated method stub
		if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){// 工地
			// 获取数据
			guid=UUID.randomUUID().toString();
			sitePlanOperate=(SitePlanOperate) InsPlanManage.getInstance(InsPlanManage.PLAN_MANAGE_TYPE.SITE);// 工地
			smList=sitePlanOperate.getThisTimeData(insTablePushTaskVo.getTaskNum());
			smHadDoDataCache=sitePlanOperate.getHadDoDataCache2(insTablePushTaskVo.getTaskNum());
			nearObject=sitePlanOperate.getNearSite();
		}
	}

	@Override
	public void backHandler() {
		// TODO Auto-generated method stub
		if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){// 工地
			activity.finish();
		}
	}

	@Override
	public void writeHandlerB() {
		// TODO Auto-generated method stub
		if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){// 工地
			
		}
	}

	@Override
	public void writeHandlerD() {
		// TODO Auto-generated method stub
		if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){// 工地
			siteDialog();
		}
	}

	@Override
	public void patrolN(Map<String, View> v) {
		// TODO Auto-generated method stub
		if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){// 工地
			ListView show_patrol_data_wx=(ListView) v.get("show_patrol_data_wx");
			if(smHadDoDataCache!=null){
				show_patrol_data_wx.setVisibility(View.VISIBLE);
				PlanWorkAdapter planWorkAdapter=new PlanWorkAdapter(activity,getListData(smHadDoDataCache, "InsSiteManage"),null,insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				show_patrol_data_wx.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void patrolY(Map<String, View> v) {
		// TODO Auto-generated method stub
		if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
				||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)){// 工地
			ListView show_patrol_data_wx=(ListView) v.get("show_patrol_data_wx");
			if (smList != null) {
				show_patrol_data_wx.setVisibility(View.VISIBLE);
				PlanWorkAdapter planWorkAdapter = new PlanWorkAdapter(activity, getListData(smList,
						"InsSiteManage"), null,
						insTablePushTaskVo);
				show_patrol_data_wx.setAdapter(planWorkAdapter);
			}else{
				show_patrol_data_wx.setVisibility(View.GONE);
			}
		}
	}
	private List<Map<String, Object>> getListData(List list, String key) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		if (list != null) {
			for (int i = 0; i < list.size(); ++i) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put(key, list.get(i));
				data.add(m);
			}
		}
		return data;
	}
	// 工地巡检填单
	private void siteDialog() {
		List<InsSiteManage> insSiteManageList = null;
		List<Module> mdList = new ArrayList<Module>();
		if (nearObject != null) {
			try {
				Dao<InsSiteManage, Long> insSiteManageDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsSiteManage.class);
				insSiteManageList = insSiteManageDao.queryForEq("id",
						nearObject.getObjId());

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			HashMap<String, String> params2 = new HashMap<String, String>();
			if (insSiteManageList != null && insSiteManageList.size() > 0) {
				if ("1".equals(insSiteManageList.get(0).getIsFirst())) {
					for (Module m : lstModule) {
						String mId = insSiteManageList.get(0).getAndroidForm();
						if (mId != null) {
							String[] mIds = mId.split(",");
							for (int i = 0; i < mIds.length; ++i) {
								if (mIds[i].equals(m.getId())) {
									mdList.add(m);
								}
							}
						}
						/*
						 * if(Constant.BIZ_CONSTRUCTION_TRACK.equals(m.getId())){
						 * mdList.add(m); }
						 */
					}
				} else {
					for (Module m : lstModule) {
						if (Constant.BIZ_CONSTRUCTION.equals(m.getId())) {
							mdList.add(m);
						}
					}
				}
				params2.put("constructionNum", insSiteManageList.get(0)
						.getConstructionNum());
				if (insSiteManageList.get(0).getSmId() != null) {
					params2.put("smId", insSiteManageList.get(0).getSmId() + "");
				}
			}
			showModuleDialog(mdList, params2, nearObject.getObjId());
		} else {
			Toast.makeText(activity, "还没进入巡查区域", Toast.LENGTH_LONG).show();
		}
	}
	private void showModuleDialog(final List<Module> taskModule,
			final HashMap<String, String> params, final String pid) {
		if (taskModule == null) {
			Toast.makeText(activity, "没有相应的模板", Toast.LENGTH_LONG).show();
			return;
		}
		if (taskModule.size() == 1) {
			if (insTablePushTaskVo != null) {
				TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
						activity, false, false,
						insTablePushTaskVo.getTaskNum(),
						Constant.UPLOAD_STATUS_WORK, null,
						insTablePushTaskVo.getTitle(),
						insTablePushTaskVo.getTaskCategory(), null, null, null);
				taskFeedBackAsyncTask.execute();
				Intent newFormInfo = new Intent(activity,
						RunForm.class);
				newFormInfo.putExtra("template", taskModule.get(0)
						.getTemplate());
				newFormInfo.putExtra("pid", pid);
				newFormInfo
						.putExtra("taskNum", insTablePushTaskVo.getTaskNum());
				newFormInfo
						.putExtra("tableName", insTablePushTaskVo.getTitle());
				newFormInfo.putExtra("taskCategory",
						insTablePushTaskVo.getTaskCategory());
				newFormInfo.putExtra("delete", true);
				newFormInfo.putExtra("iParams", params);
				activity.startActivityForResult(newFormInfo, Constant.FROM_REQUEST_CODE);
			}
			return;
		}
		String[] itmes = new String[taskModule.size()];

		for (int i = 0; i < itmes.length; ++i) {
			itmes[i] = taskModule.get(i).getName();
		}
		AlertDialog.Builder vDialog = new AlertDialog.Builder(activity);
		vDialog.setTitle("选择工作类型");
		vDialog.setItems(itmes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (insTablePushTaskVo != null) {
					TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
							activity, false, false,
							insTablePushTaskVo.getTaskNum(),
							Constant.UPLOAD_STATUS_WORK, null,
							insTablePushTaskVo.getTitle(), insTablePushTaskVo
									.getTaskCategory(), null, null, null);
					taskFeedBackAsyncTask.execute();
					Intent newFormInfo = new Intent(
							activity, RunForm.class);
					newFormInfo.putExtra("template", taskModule.get(which)
							.getTemplate());
					newFormInfo.putExtra("pid", pid);
					newFormInfo.putExtra("taskNum",
							insTablePushTaskVo.getTaskNum());
					newFormInfo.putExtra("tableName",
							insTablePushTaskVo.getTitle());
					newFormInfo.putExtra("taskCategory",
							insTablePushTaskVo.getTaskCategory());
					newFormInfo.putExtra("delete", true);
					newFormInfo.putExtra("iParams", params);
					activity.startActivityForResult(newFormInfo,
							Constant.FROM_REQUEST_CODE);
				}
			}
		});
		vDialog.show();
	}

}
