package com.movementinsome.app.mytask.handle;

import android.content.Context;
import android.content.Intent;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.database.vo.WsComplainantFormVO;
import com.movementinsome.kernel.initial.model.Module;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WsMainViewDataHandle implements MainViewDataBaseHandle {

	// 所有表单配置
	private List<Module> lstModule;
	private Context context;

	public WsMainViewDataHandle(Context context) {
		lstModule = AppContext.getInstance().getModuleData();
		this.context = context;
	}

	@Override
	public void updatePlanList(String type,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> d = null;
		// 任务表单模板
		ArrayList<Module> taskModule = new ArrayList<Module>();

		List<InsTablePushTaskVo> insTablePushTaskVoList = null;
		// 巡检数据
		Dao<InsPatrolDataVO, Long> insPatrolDataDao = AppContext.getInstance()
				.getAppDbHelper().getDao(InsPatrolDataVO.class);
		List<InsPatrolDataVO> pdList;// 路面
		Dao<InsSiteManage, Long> insSiteManageDao = AppContext.getInstance()
				.getAppDbHelper().getDao(InsSiteManage.class);
		List<InsSiteManage> smList;// 工地
		if (Constant.PLAN_PATROL_WS.equals(type)) {
			insTablePushTaskVoList = AppContext.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.CYCLE_PLAN);
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_CONSTRUCTION_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_VALVE_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_HYDRANT_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.INTERIM_PLAN));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_CONSTRUCTION_TEMPORARY));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_VALVE_TEMPORARY));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_HYDRANT_TEMPORARY));
			List<InsTablePushTaskVo> insTablePushTaskVoS = new ArrayList<InsTablePushTaskVo>();
			if (insTablePushTaskVoList != null) {
				for (int i = 0; i < insTablePushTaskVoList.size(); ++i) {
					if (!"3".equals(insTablePushTaskVoList.get(i).getTbType())) {
						insTablePushTaskVoS.add(insTablePushTaskVoList.get(i));
					}
				}
			}
			insTablePushTaskVoList = insTablePushTaskVoS;
		} else if (Constant.PLAN_PATROL_TEMPORARY_CS_WS.equals(type)) {
			Map<String, Object> mp0 = new HashMap<String, Object>();
			mp0.put("title", Constant.INTERIM_PLAN);
			mp0.put("tbType", "3");
			Map<String, Object> mp1 = new HashMap<String, Object>();
			mp1.put("title", Constant.PLAN_CONSTRUCTION_TEMPORARY);
			mp1.put("tbType", "3");
			Map<String, Object> mp2 = new HashMap<String, Object>();
			mp2.put("title", Constant.PLAN_VALVE_TEMPORARY);
			mp2.put("tbType", "3");
			Map<String, Object> mp3 = new HashMap<String, Object>();
			mp2.put("title", Constant.PLAN_HYDRANT_TEMPORARY);
			mp2.put("tbType", "3");

			Map<String, Object> mp4 = new HashMap<String, Object>();
			mp2.put("title", Constant.CYCLE_PLAN);
			mp2.put("tbType", "3");
			Map<String, Object> mp5 = new HashMap<String, Object>();
			mp2.put("title", Constant.PLAN_CONSTRUCTION_CYCLE);
			mp2.put("tbType", "3");
			Map<String, Object> mp6 = new HashMap<String, Object>();
			mp2.put("title", Constant.PLAN_VALVE_CYCLE);
			mp2.put("tbType", "3");
			Map<String, Object> mp7 = new HashMap<String, Object>();
			mp2.put("title", Constant.PLAN_HYDRANT_CYCLE);
			mp2.put("tbType", "3");
			
			insTablePushTaskVoList = AppContext.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(mp0);
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(mp1));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(mp2));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(mp3));
			
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(mp4));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(mp5));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(mp6));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(mp7));

		}
		if (insTablePushTaskVoList != null) {

			for (InsTablePushTaskVo insTablePushTaskVo : insTablePushTaskVoList) {
				String title = insTablePushTaskVo.getTitle();

				// 查询子list数据和添加子list数据
				if (Constant.CYCLE_PLAN.equals(title)
						|| Constant.INTERIM_PLAN.equals(title)
						|| Constant.PLAN_VALVE_CYCLE.equals(title)
						|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
						|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
						|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)) {// 路面
					for (Module m : lstModule) {
						if (Constant.BUIDERRPT_ID.equals(m.getId())
								|| Constant.GISCORRECT_ID.equals(m.getId())
								|| Constant.REPAIRCOVERT_ID.equals(m.getId())) {
							taskModule.add(m);
						}
					}
					pdList = insPatrolDataDao.queryForEq("workTaskNum",
							insTablePushTaskVo.getTaskNum());
					List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
					if (pdList != null) {
						for (InsPatrolDataVO insPatrolDataVO : pdList) {
							d = new HashMap<String, Object>();
							d.put("InsPatrolDataVO", insPatrolDataVO);
							child.add(d);
						}
					}
					childData.add(child);
				} else if (Constant.PLAN_CONSTRUCTION_CYCLE.equals(title)
						|| Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(title)) {// 工地
					for (Module m : lstModule) {
						if (Constant.BIZ_CONSTRUCTION.equals(m.getId())) {
							taskModule.add(m);
						}
					}
					smList = insSiteManageDao.queryForEq("workTaskNum",
							insTablePushTaskVo.getTaskNum());
					List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
					if (smList != null) {
						for (InsSiteManage insSiteManage : smList) {
							d = new HashMap<String, Object>();
							d.put("InsSiteManage", insSiteManage);
							child.add(d);
						}
					}
					childData.add(child);
				}
				// 查询父list数据
				Dao<InsPlanTaskVO, Long> planTaskDao = AppContext.getInstance()
						.getAppDbHelper().getDao(InsPlanTaskVO.class);
				Map<String, Object> mp = new HashMap<String, Object>();
				mp.put("workTaskNum", insTablePushTaskVo.getTaskNum());
				mp.put("workTaskType", insTablePushTaskVo.getTitle());
				List<InsPlanTaskVO> insPlanTaskVOList = planTaskDao
						.queryForFieldValuesArgs(mp);

				Dao<TaskUploadStateVO, Long> taskUploadStateDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(TaskUploadStateVO.class);
				Map<String, Object> mp0 = new HashMap<String, Object>();
				mp0.put("taskNum", insTablePushTaskVo.getTaskNum());
				mp0.put("taskCategory", insTablePushTaskVo.getTaskCategory());
				mp0.put("tableName", insTablePushTaskVo.getTitle());
				List<TaskUploadStateVO> taskUploadStateVOList = taskUploadStateDao
						.queryForFieldValuesArgs(mp0);
				// 添加父list数据
				d = new HashMap<String, Object>();
				d.put("InsTablePushTaskVo", insTablePushTaskVo);
				d.put("Module", taskModule);
				if (taskUploadStateVOList != null
						&& taskUploadStateVOList.size() > 0) {
					d.put("TaskUploadStateVO", taskUploadStateVOList.get(0));
				}
				if (insPlanTaskVOList != null && insPlanTaskVOList.size() > 0) {
					d.put("InsPlanTaskVO", insPlanTaskVOList.get(0));
				}
				groupData.add(d);
			}
		}

	}

	@Override
	public List<Map<String, Object>> updateList(String type,
			List<Map<String, Object>> data) {
		// TODO Auto-generated method stub
		Map<String, Object> d = null;
		List<InsTablePushTaskVo> insTablePushTaskVoList = null;
		try {
			Dao<InsTablePushTaskVo, Long> tablePushTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);
			if (Constant.URGENT.equals(type)) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("title", Constant.URGENT);
				insTablePushTaskVoList = tablePushTaskDao
						.queryForFieldValuesArgs(m);
			}
			if (insTablePushTaskVoList != null) {
				for (InsTablePushTaskVo insTablePushTaskVo : insTablePushTaskVoList) {
					Dao<TaskUploadStateVO, Long> taskUploadStateDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(TaskUploadStateVO.class);
					Map<String, Object> mp0 = new HashMap<String, Object>();
					mp0.put("taskNum", insTablePushTaskVo.getTaskNum());
					mp0.put("taskCategory",
							insTablePushTaskVo.getTaskCategory());
					mp0.put("tableName", insTablePushTaskVo.getTitle());
					List<TaskUploadStateVO> taskUploadStateVOList = taskUploadStateDao
							.queryForFieldValuesArgs(mp0);
					Dao<WsComplainantFormVO, Long> wsComplainantFormVODao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(WsComplainantFormVO.class);
					Map<String, Object> mp1 = new HashMap<String, Object>();
					mp1.put("workTaskNum", insTablePushTaskVo.getTaskNum());
					List<WsComplainantFormVO> wsComplainantFormVOList = wsComplainantFormVODao
							.queryForFieldValuesArgs(mp1);
					d = new HashMap<String, Object>();
					d.put("InsTablePushTaskVo", insTablePushTaskVo);
					if (wsComplainantFormVOList != null
							&& wsComplainantFormVOList.size() > 0) {
						d.put("Module", getTaskModule(wsComplainantFormVOList
								.get(0).getAndroidForm()));
						d.put("WsComplainantFormVO",
								wsComplainantFormVOList.get(0));
					}
					if (taskUploadStateVOList != null
							&& taskUploadStateVOList.size() > 0) {
						d.put("TaskUploadStateVO", taskUploadStateVOList.get(0));
					}
					data.add(d);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}

	@Override
	public void blackHandle(Intent data) {
		// TODO Auto-generated method stub
		String taskNum = data.getStringExtra("taskNum");// 任务编号
		String taskCategory = data.getStringExtra("taskCategory");// 表单类型
		String tableName = data.getStringExtra("tableName");// 表单在数据库中表名
		String pid= data.getStringExtra("pid");
		int opState = data.getIntExtra("opState", 0);
		HashMap<String, String> oParams = (HashMap<String, String>) data
				.getSerializableExtra("oParams");
		if (Constant.COMPLAINANT_FORM.equals(taskCategory)) {
			try {
				Dao<InsTablePushTaskVo, Long> wsInsTablePushTaskVoDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsTablePushTaskVo.class);
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("taskNum", taskNum);
				m.put("taskCategory", taskCategory);
				m.put("title", tableName);
				String noPresentStr = null;
				String isFullyCommit = data.getStringExtra("isFullyCommit");
				if(oParams!=null){
					noPresentStr = oParams.get("noPresentStr");
				}
				List<InsTablePushTaskVo> insTablePushTaskVoList = wsInsTablePushTaskVoDao
						.queryForFieldValues(m);
				if (insTablePushTaskVoList != null
						&& insTablePushTaskVoList.size() > 0) {
					InsTablePushTaskVo insTablePushTaskVo = insTablePushTaskVoList
							.get(0);
					if (opState == 1&&(!"否".equals(noPresentStr)||"true".equals(isFullyCommit))) {
						insTablePushTaskVo.setTbType("2");
					}else if(insTablePushTaskVo.getTbType()==null){
						insTablePushTaskVo.setTbType("1");
					}
					wsInsTablePushTaskVoDao.update(insTablePushTaskVo);
					Intent intent = new Intent();
					intent.setAction(com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION);
					context.sendBroadcast(intent);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if (Constant.PLAN_CONSTRUCTION_CYCLE.equals(tableName)) {
			TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
					context, false, false, taskNum,
					Constant.UPLOAD_STATUS_PAUSE, null, tableName,
					taskCategory, null, null, null);
			taskFeedBackAsyncTask.execute();
		}
		
	}

	private ArrayList<Module> getTaskModule(String mdIds) {
		ArrayList<Module> taskModule = new ArrayList<Module>();
		if (mdIds != null) {
			String[] ids = mdIds.split(",");
			for (Module m : lstModule) {
				for (int i = 0; i < ids.length; ++i) {
					if (ids[i].equals(m.getId())) {
						taskModule.add(m);
					}
				}
			}
		}
		return taskModule;
	}

	@Override
	public void updateTaskList(String title, String taskCategory,Map<String, Object> m,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
