package com.movementinsome.app.mytask.handle;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.InsComplainantForm;
import com.movementinsome.database.vo.InsDatumInaccurate;
import com.movementinsome.database.vo.InsDredgePTask;
import com.movementinsome.database.vo.InsFacMaintenanceData;
import com.movementinsome.database.vo.InsHiddenDangerReport;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.InsTableSaveDataVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.util.JsonAnalysisUtil;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainViewDataHandle implements MainViewDataBaseHandle {
	// 所有表单配置
	private List<Module> lstModule;
	private Activity context;

	public MainViewDataHandle(Activity context) {
		this.context = context;
		lstModule = AppContext.getInstance().getModuleData();
	}

	// 获取应急数据
	public List<Map<String, Object>> updateList(String type,
			List<Map<String, Object>> data) {
		Map<String, Object> d = null;
		List<InsTablePushTaskVo> insTablePushTaskVoList = null;
		try {
			Dao<InsTablePushTaskVo, Long> tablePushTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);
			if (Constant.URGENT.equals(type)) {
				// 应急用taskCategory分类
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("title", Constant.COMPLAINANT);
				insTablePushTaskVoList = tablePushTaskDao
						.queryForFieldValuesArgs(m);
				Map<String, Object> m2 = new HashMap<String, Object>();
				m2.put("title", Constant.PROBLEM);
				List<InsTablePushTaskVo> insTablePushTaskVoList2 = tablePushTaskDao
						.queryForFieldValuesArgs(m2);
				insTablePushTaskVoList.addAll(insTablePushTaskVoList2);
				Map<String, Object> m3 = new HashMap<String, Object>();
				m3.put("title", Constant.INSMAPINACCURATE_FORM);
				List<InsTablePushTaskVo> insTablePushTaskVoList3 = tablePushTaskDao
						.queryForFieldValuesArgs(m3);
				insTablePushTaskVoList.addAll(insTablePushTaskVoList3);
			} else {
				// 应急用taskCategory分类
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("title", Constant.COMPLAINANT);
				m.put("taskCategory", type);
				insTablePushTaskVoList = tablePushTaskDao
						.queryForFieldValuesArgs(m);
				Map<String, Object> m2 = new HashMap<String, Object>();
				m2.put("title", Constant.PROBLEM);
				m2.put("taskCategory", type);
				List<InsTablePushTaskVo> insTablePushTaskVoList2 = tablePushTaskDao
						.queryForFieldValuesArgs(m2);
				insTablePushTaskVoList.addAll(insTablePushTaskVoList2);
				Map<String, Object> m3 = new HashMap<String, Object>();
				m3.put("title", Constant.INSMAPINACCURATE_FORM);
				m3.put("taskCategory", type);
				List<InsTablePushTaskVo> insTablePushTaskVoList3 = tablePushTaskDao
						.queryForFieldValuesArgs(m3);
				insTablePushTaskVoList.addAll(insTablePushTaskVoList3);
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
					Dao<InsTableSaveDataVo, Long> saveDataDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(InsTableSaveDataVo.class);
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("mainNumber", insTablePushTaskVo.getTaskNum());
					mp.put("taskCategory", insTablePushTaskVo.getTaskCategory());
					mp.put("title", insTablePushTaskVo.getTitle());
					List<InsTableSaveDataVo> insTableSaveDataVoList = saveDataDao
							.queryForFieldValuesArgs(mp);

					d = new HashMap<String, Object>();
					d.put("InsTablePushTaskVo", insTablePushTaskVo);
					d.put("Module",
							getTaskModule(insTablePushTaskVo.getTitle(),
									insTablePushTaskVo.getTaskCategory()));
					if (taskUploadStateVOList != null
							&& taskUploadStateVOList.size() > 0) {
						d.put("TaskUploadStateVO", taskUploadStateVOList.get(0));
					}
					if(Constant.INSMAPINACCURATE_FORM.equals(insTablePushTaskVo.getTaskCategory())){
						try {
							Dao<InsDatumInaccurate, Long> InsDatumInaccurateDao = AppContext
									.getInstance().getAppDbHelper()
									.getDao(InsDatumInaccurate.class);
								Map<String, Object> mp1 = new HashMap<String, Object>();
								mp1.put("serialNumber", insTablePushTaskVo.getTaskNum());
								List<InsDatumInaccurate> insDatumInaccurateList2 = InsDatumInaccurateDao.queryForAll();
								List<InsDatumInaccurate> insDatumInaccurateList = InsDatumInaccurateDao
										.queryForFieldValuesArgs(mp1);
								
								if (insDatumInaccurateList != null
										&& insDatumInaccurateList.size() > 0) {
									d.put("InsDatumInaccurate",
											insDatumInaccurateList.get(0));
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					if (insTableSaveDataVoList != null
							&& insTableSaveDataVoList.size() > 0) {
						d.put("InsTableSaveDataVo",
								insTableSaveDataVoList.get(0));
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

	private ArrayList<Module> getTaskModule(String title, String taskCategory) {
		ArrayList<Module> taskModule = new ArrayList<Module>();
		if (Constant.XJFL_YJ.equals(taskCategory)) {// 巡检应急0
			for (Module m : lstModule) {
				if (Constant.REPAIRCOVERT_ID.equals(m.getId())
						|| Constant.INSWORKREGEDT_ID.equals(m.getId())
						|| Constant.HID_DENRPT.equals(m.getId())) {
					taskModule.add(m);
				}
			}
		} else if (Constant.QSGL_YJQS.equals(taskCategory)) {// 清梳
			for (Module m : lstModule) {
				if (Constant.INSDREDGEWORK_ID.equals(m.getId())) {
					taskModule.add(m);
				}
			}
		} else if (Constant.WXGL_XCQX.equals(taskCategory)) {// 维修

			for (Module m : lstModule) {
				if (Constant.COMPLAINANT.equals(title)) {
					if (Constant.REPAIRREC_PIPE_ID.equals(m.getId())) {
						taskModule.add(m);
					}
				} else if (Constant.PROBLEM.equals(title)) {
					if (Constant.REPAIRREC_PIPE_ID.equals(m.getId())) {
						taskModule.add(m);
					}
				}
			}
		} else if (Constant.TASK_VALVE_REPAIRING.equals(taskCategory)) {// 阀门管理:现场抢修
			for (Module m : lstModule) {
				if (Constant.REPAIRREC_VALVE_ID.equals(m.getId())) {
					taskModule.add(m);
				}
			}
		} else if (Constant.INSMAPINACCURATE_FORM.equals(taskCategory)){
			for (Module m : lstModule) {
				if (Constant.GISCORRECT_IDS.equals(m.getId())) {
					taskModule.add(m);
				}
			}
		}
		return taskModule;
	}

	public void blackHandle(Intent data) {
		String taskNum = data.getStringExtra("taskNum");// 任务编号
		String taskCategory = data.getStringExtra("taskCategory");// 表单类型
		String tableName = data.getStringExtra("tableName");// 表单在数据库中表名
		String pid= data.getStringExtra("pid");
		int opState = data.getIntExtra("opState", 0);
		try {
			HashMap<String, String> oParams = (HashMap<String, String>) data
					.getSerializableExtra("oParams");
			if (Constant.PLAN_CONSTRUCTION_CYCLE.equals(tableName)
					|| Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(tableName)) {
				Dao<InsSiteManage, Long> insSiteManageDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsSiteManage.class);
				String state = oParams.get("state");
				if ("4".equals(state)&& opState == 1) {
					insSiteManageDao.delete(insSiteManageDao.queryForEq("id",
							pid));
				}
			} else if (Constant.COMPLAINANT.equals(tableName)
					|| Constant.PROBLEM.equals(tableName)
					|| Constant.INSMAPINACCURATE_FORM.equals(tableName)) {
				Dao<InsTableSaveDataVo, Long> saveDataDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsTableSaveDataVo.class);
				Map<String, Object> mp = new HashMap<String, Object>();
				mp.put("mainNumber", taskNum);
				mp.put("taskCategory", taskCategory);
				mp.put("title", tableName);
				List<InsTableSaveDataVo> insTableSaveDataVoList = saveDataDao
						.queryForFieldValuesArgs(mp);
				if(Constant.INSMAPINACCURATE_FORM.equals(tableName)){
					Dao<InsTablePushTaskVo, Long> InsTablePushTaskVoDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(InsTablePushTaskVo.class);
					Map<String, Object> mp2 = new HashMap<String, Object>();
					mp2.put("taskNum", taskNum);
					List<InsTablePushTaskVo> insTablePushTaskVoList = InsTablePushTaskVoDao
							.queryForFieldValuesArgs(mp2);
					InsTablePushTaskVo insTablePushTaskVo = insTablePushTaskVoList.get(0);
					insTablePushTaskVo.setTbType("2");
					InsTablePushTaskVoDao.update(insTablePushTaskVo);
				}
				if (insTableSaveDataVoList != null
						&& insTableSaveDataVoList.size() > 0) {
					InsTableSaveDataVo insTableSaveDataVo = insTableSaveDataVoList
							.get(0);
					Object ob = null;
					InsComplainantForm insComplainantForm = new InsComplainantForm();
					if (Constant.COMPLAINANT.equals(tableName)) {
						ob = new InsComplainantForm();
					} else if (Constant.PROBLEM.equals(tableName)) {
						ob = new InsHiddenDangerReport();
					} else if (Constant.INSMAPINACCURATE_FORM.equals(tableName)){
						ob = new InsDatumInaccurate();
					}
					if (insTableSaveDataVo != null) {
						JSONObject insComplainantFormJOB = new JSONObject(
								insTableSaveDataVo.getTbData());
						JsonAnalysisUtil.setJsonObjectData(
								insComplainantFormJOB, ob);
					}
					if (oParams != null) {
						Class c = ob.getClass();
						Method[] m = c.getMethods();
						for (int i = 0; i < m.length; i++) {
							Iterator<String> keys = oParams.keySet().iterator();
							String name = m[i].getName();
							if (name.contains("set")) {
								while (keys.hasNext()) {
									String key = keys.next();
									if (name.toLowerCase().equals(
											("set" + key).toLowerCase())) {
										try {
											m[i].invoke(ob,
													new Object[] { oParams
															.get(key) });
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
					if (insTableSaveDataVo != null) {
						insTableSaveDataVo.setTbData(new Gson().toJson(ob));
						insTableSaveDataVo.setTbState(opState + "");
						saveDataDao.update(insTableSaveDataVo);
					}
				}
			}
			if (Constant.PLAN_CONSTRUCTION_CYCLE.equals(tableName)
					|| Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(tableName)
					|| Constant.COMPLAINANT.equals(tableName)
					|| Constant.PROBLEM.equals(tableName)
					|| Constant.CYCLE_PLAN.equals(tableName)
					|| Constant.INTERIM_PLAN.equals(tableName)
					|| Constant.QSGL_JHX.equals(tableName)
					|| Constant.WXGL_JHXWX.equals(tableName)
					|| Constant.FMGL_JHXWX.equals(tableName)
					|| Constant.XJGL_GJD_CYCLE.equals(tableName)
					|| Constant.XJGL_GJD_TEMPORARY.equals(tableName)
					|| Constant.INSMAPINACCURATE_FORM.equals(tableName)) {
				TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
						context, false, false, taskNum,
						Constant.UPLOAD_STATUS_PAUSE, null, tableName,
						taskCategory, null, null, null);
				taskFeedBackAsyncTask.execute();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
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
		Dao<InsKeyPointPatrolData, Long> insKeyPointPatrolDataDao = AppContext
				.getInstance().getAppDbHelper()
				.getDao(InsKeyPointPatrolData.class);
		List<InsKeyPointPatrolData> insKeyPointPatrolDataList;// 关键点

		Dao<InsDredgePTask, Long> insDredgePTaskDao = AppContext.getInstance()
				.getAppDbHelper().getDao(InsDredgePTask.class);
		List<InsDredgePTask> insDredgePTaskList;// 計劃清梳

		Dao<InsFacMaintenanceData, Long> insFacMaintenanceDataDao = AppContext
				.getInstance().getAppDbHelper()
				.getDao(InsFacMaintenanceData.class);
		List<InsFacMaintenanceData> insFacMaintenanceDataList;// 計劃清梳

		// 查询推送消息数据和添加推送消息数据
		if (Constant.CYCLE_PLAN.equals(type)) {// 周期
			insTablePushTaskVoList = AppContext.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.CYCLE_PLAN);
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.XJGL_GJD_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_CONSTRUCTION_CYCLE));
		} else if (Constant.INTERIM_PLAN.equals(type)) {// 临时
			insTablePushTaskVoList = AppContext.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.INTERIM_PLAN);
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.XJGL_GJD_TEMPORARY));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_CONSTRUCTION_TEMPORARY));
		} else if (Constant.QSGL_JHX.equals(type)) {// 計劃清梳
			insTablePushTaskVoList = AppContext.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.QSGL_JHX);
		} else if (Constant.FMGL_JHXWX.equals(type)) {// 计划阀门维修
			insTablePushTaskVoList = AppContext.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.FMGL_JHXWX);
		} else if (Constant.WXGL_JHXWX.equals(type)) {// 计划管道维修
			insTablePushTaskVoList = AppContext.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.WXGL_JHXWX);
		} else if (Constant.PLAN.equals(type)) {
			insTablePushTaskVoList = AppContext.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.CYCLE_PLAN);
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.XJGL_GJD_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_CONSTRUCTION_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.INTERIM_PLAN));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.XJGL_GJD_TEMPORARY));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_CONSTRUCTION_TEMPORARY));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.QSGL_JHX));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.FMGL_JHXWX));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.WXGL_JHXWX));
			
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_VALVE_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_VALVE_TEMPORARY));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_HYDRANT_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_HYDRANT_TEMPORARY));
			// 中山
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_FAC_ZS_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_FAC_ZS_TEMPORARY));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_TIEPAI_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_TIEPAI_TEMPORARY));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_PATROL_SCHEDULE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_PATROL_ZS_CYCLE));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_PATROL_ZS_TEMPORARY));
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_PAICHA_TEMPORARY));
			
		}
		if (insTablePushTaskVoList != null) {

			for (InsTablePushTaskVo insTablePushTaskVo : insTablePushTaskVoList) {
				String title = insTablePushTaskVo.getTitle();

				// 查询子list数据和添加子list数据
				if (Constant.CYCLE_PLAN.equals(title)// 路面
						|| Constant.INTERIM_PLAN.equals(title)
						|| Constant.PLAN_VALVE_CYCLE.equals(title)
						|| Constant.PLAN_VALVE_TEMPORARY.equals(title)
						|| Constant.PLAN_HYDRANT_CYCLE.equals(title)
						|| Constant.PLAN_HYDRANT_TEMPORARY.equals(title)) {
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
				}else if(// 中山
						Constant.PLAN_FAC_ZS_CYCLE.equals(title)
						|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(title)
						|| Constant.PLAN_TIEPAI_CYCLE.equals(title)
						|| Constant.PLAN_TIEPAI_TEMPORARY.equals(title)
						
						|| Constant.PLAN_PATROL_SCHEDULE.equals(title)
						|| Constant.PLAN_PATROL_ZS_CYCLE.equals(title)
						|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(title)
						){
					Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(InsPatrolAreaData.class);
					for (Module m : lstModule) {
						if (Constant.BUIDERRPT_ID.equals(m.getId())
								|| Constant.GISCORRECT_ID.equals(m.getId())
								|| Constant.REPAIRCOVERT_ID.equals(m.getId())) {
							taskModule.add(m);
						}
					}
					List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
					List<InsPatrolAreaData> insPatrolAreaDataList = insPatrolAreaDataDao.queryForEq("workTaskNum", insTablePushTaskVo.getTaskNum());
					for(InsPatrolAreaData insPatrolAreaData : insPatrolAreaDataList){
						d = new HashMap<String, Object>();
						d.put("InsPatrolAreaData", insPatrolAreaData);
						child.add(d);
						// 加载区域下的所有设施
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("workTaskNum", insTablePushTaskVo.getTaskNum());
						m.put("areaId", insPatrolAreaData.getAreaId());
						pdList = insPatrolDataDao.queryForFieldValues(m);
						if (pdList != null) {
							for (InsPatrolDataVO insPatrolDataVO : pdList) {
								d = new HashMap<String, Object>();
								d.put("InsPatrolDataVO", insPatrolDataVO);
								child.add(d);
							}
						}
						childData.add(child);
					}
				}else if(// 排查系统
						Constant.PLAN_PAICHA_TEMPORARY.equals(title)){
					Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(InsPatrolAreaData.class);
					for (Module m : lstModule) {
						if (Constant.BUIDERRPT_ID.equals(m.getId())
								|| Constant.GISCORRECT_ID.equals(m.getId())
								|| Constant.REPAIRCOVERT_ID.equals(m.getId())) {
							taskModule.add(m);
						}
					}
					List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
					List<InsPatrolAreaData> insPatrolAreaDataList = insPatrolAreaDataDao.queryForEq("workTaskNum", insTablePushTaskVo.getTaskNum());
					for(InsPatrolAreaData insPatrolAreaData : insPatrolAreaDataList){
						d = new HashMap<String, Object>();
						d.put("InsPatrolAreaData", insPatrolAreaData);
						child.add(d);
						childData.add(child);
					}
				}else if (Constant.XJGL_GJD_CYCLE.equals(title)
						|| Constant.XJGL_GJD_TEMPORARY.equals(title)) {// 关键点
					for (Module m : lstModule) {
						if (Constant.BIZ_KEYPOINT.equals(m.getId())) {
							taskModule.add(m);
						}
					}
					insKeyPointPatrolDataList = insKeyPointPatrolDataDao
							.queryForEq("workTaskNum",
									insTablePushTaskVo.getTaskNum());
					List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
					if (insKeyPointPatrolDataList != null) {
						for (InsKeyPointPatrolData insKeyPointPatrolData : insKeyPointPatrolDataList) {
							d = new HashMap<String, Object>();
							d.put("InsKeyPointPatrolData",
									insKeyPointPatrolData);
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
							d.put("InsSiteManage",
									insSiteManage);
							child.add(d);
						}
					}
					childData.add(child);
				} else if (Constant.QSGL_JHX.equals(title)) {// 計劃清梳
					for (Module m : lstModule) {
						if (Constant.INSDREDGEWORK_ID.equals(m.getId())
								|| Constant.INSWORKLINK_ID.equals(m.getId())) {
							taskModule.add(m);
						}
					}
					insDredgePTaskList = insDredgePTaskDao.queryForEq(
							"workTaskNum", insTablePushTaskVo.getTaskNum());
					List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
					if (insDredgePTaskList != null) {
						for (InsDredgePTask insDredgePTask : insDredgePTaskList) {
							d = new HashMap<String, Object>();
							d.put("InsDredgePTask",insDredgePTask);
							d.put("Module", taskModule);
							child.add(d);
						}
					}
					childData.add(child);
				} else if (Constant.WXGL_JHXWX.equals(title)
						|| Constant.FMGL_JHXWX.equals(title)) {// 计划维修（阀门）
					if (Constant.WXGL_JHXWX.equals(title)) {
						for (Module m : lstModule) {
							if (Constant.REPAIRREC_PIPE_ID.equals(m.getId())) {
								taskModule.add(m);
							}
						}
					} else {
						for (Module m : lstModule) {
							if (Constant.REPAIRREC_VALVE_ID.equals(m.getId())) {
								taskModule.add(m);
							}
						}
					}
					insFacMaintenanceDataList = insFacMaintenanceDataDao
							.queryForEq("workTaskNum",
									insTablePushTaskVo.getTaskNum());
					List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
					if (insFacMaintenanceDataList != null) {
						for (InsFacMaintenanceData insFacMaintenanceData : insFacMaintenanceDataList) {
							d = new HashMap<String, Object>();
							d.put("InsFacMaintenanceData",insFacMaintenanceData);
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
					d.put("TaskUploadStateVO",taskUploadStateVOList.get(0));
				}
				if (insPlanTaskVOList != null && insPlanTaskVOList.size() > 0) {
					d.put("InsPlanTaskVO", insPlanTaskVOList.get(0));
				}
				groupData.add(d);
			}
		}
	}

	@Override
	public void updateTaskList(String title, String taskCategory,Map<String, Object> m,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException {
		// TODO Auto-generated method stub
		
	}
}
