package com.movementinsome.app.mytask.handle;

import android.content.Intent;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TaskUploadStateVO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XJYSMainViewDataHandle implements MainViewDataBaseHandle{

	@Override
	public void updatePlanList(String type,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException {
		// TODO Auto-generated method stub
		Map<String, Object> d = null;

		List<InsTablePushTaskVo> insTablePushTaskVoList = null;
		// 巡检数据
		Dao<InsPatrolDataVO, Long> insPatrolDataDao = AppContext.getInstance()
				.getAppDbHelper().getDao(InsPatrolDataVO.class);
		List<InsPatrolDataVO> pdList;// 路面
		if (Constant.PLAN.equals(type)) {
			insTablePushTaskVoList = AppContext.getInstance()
			.getAppDbHelper().getDao(InsTablePushTaskVo.class)
			.queryForEq("title", Constant.PLAN_FAC_CYCLE);
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForEq("title", Constant.PLAN_FAC_TEMPORARY));
			if (insTablePushTaskVoList != null) {
				for (InsTablePushTaskVo insTablePushTaskVo : insTablePushTaskVoList) {
					String title = insTablePushTaskVo.getTitle();

					// 查询子list数据和添加子list数据
					if (Constant.PLAN_FAC_CYCLE.equals(title)
							|| Constant.PLAN_FAC_TEMPORARY.equals(title)) {// 设施
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
	}

	@Override
	public List<Map<String, Object>> updateList(String type,
			List<Map<String, Object>> data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void blackHandle(Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTaskList(String title, String taskCategory,Map<String, Object> m,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
