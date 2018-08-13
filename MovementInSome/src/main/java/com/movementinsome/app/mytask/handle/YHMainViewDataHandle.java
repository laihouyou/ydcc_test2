package com.movementinsome.app.mytask.handle;

import android.content.Context;
import android.content.Intent;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.SpatialReference;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.movementinsome.database.vo.BsPnInsTask;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsRushRepairWorkOrder;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.map.utils.MapUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YHMainViewDataHandle implements MainViewDataBaseHandle{

	// 所有表单配置
		private List<Module> lstModule;
		private Context context;
		private SpatialReference sr;

		public YHMainViewDataHandle(Context context) {
			lstModule = AppContext.getInstance().getModuleData();
			this.context = context;
			this.sr = SpatialReference.create(Integer.valueOf(AppContext.getInstance().getSrid()));
		}
	@Override
	public void updatePlanList(String type,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException {
		// TODO Auto-generated method stub
		List<InsTablePushTaskVo> insTablePushTaskVoList = null;
		if (Constant.PLAN.equals(type)) {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("isDownload", "1");
			m.put("title", Constant.ROUTINE_INS);
			insTablePushTaskVoList = AppContext.getInstance()
				.getAppDbHelper().getDao(InsTablePushTaskVo.class)
				.queryForFieldValuesArgs(m);
			m = new HashMap<String, Object>();
			m.put("isDownload", "1");
			m.put("title", Constant.SPECIAL_INS);
			insTablePushTaskVoList.addAll(AppContext.getInstance()
				.getAppDbHelper().getDao(InsTablePushTaskVo.class)
				.queryForFieldValuesArgs(m));
			m = new HashMap<String, Object>();
			m.put("isDownload", "1");
			m.put("title", Constant.EMPHASIS_INS);
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(m));
			m = new HashMap<String, Object>();
			m.put("isDownload", "1");
			m.put("title", Constant.LEAK_INS);
			insTablePushTaskVoList.addAll(AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(m));
			addData(insTablePushTaskVoList, groupData, childData);
		}
	}
	private void addData(List<InsTablePushTaskVo> insTablePushTaskVoList,List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException{
		Map<String, Object> d = null;
		// 巡检数据
		Dao<BsSupervisionPoint, Long> bsSupervisionPointDao = AppContext.getInstance()
				.getAppDbHelper().getDao(BsSupervisionPoint.class);
		List<BsSupervisionPoint> bsSupervisionPointList;// 常规巡查
		Dao<BsRoutineInsArea, Long> bsRoutineInsAreaDao = AppContext.getInstance()
				.getAppDbHelper().getDao(BsRoutineInsArea.class);
		List<BsRoutineInsArea> bsRoutineInsAreaList;// 常规巡查
		Dao<BsInsFacInfo, Long> bsInsFacInfoDao = AppContext.getInstance()
				.getAppDbHelper().getDao(BsInsFacInfo.class);
		List<BsInsFacInfo> bsInsFacInfoList;// 专项巡查	
		Dao<BsEmphasisInsArea, Long> bsEmphasisInsAreaDao = AppContext.getInstance()
				.getAppDbHelper().getDao(BsEmphasisInsArea.class);
		List<BsEmphasisInsArea> bsEmphasisInsAreaList;// 重点区域巡查
				
		Dao<BsLeakInsArea, Long> bsLeakInsAreaDao = AppContext.getInstance()
				.getAppDbHelper().getDao(BsLeakInsArea.class);
		List<BsLeakInsArea> bsLeakInsAreaList;// 查漏
		if (insTablePushTaskVoList != null) {
			for (InsTablePushTaskVo insTablePushTaskVo : insTablePushTaskVoList) {
				if(Constant.ROUTINE_INS.equals(insTablePushTaskVo.getTitle())
						||Constant.SPECIAL_INS.equals(insTablePushTaskVo.getTitle())
						||Constant.EMPHASIS_INS.equals(insTablePushTaskVo.getTitle())
						||Constant.LEAK_INS.equals(insTablePushTaskVo.getTitle())){
					String title = insTablePushTaskVo.getTitle();
					// 查询父list数据
					Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext.getInstance()
							.getAppDbHelper().getDao(BsPnInsTask.class);
					Map<String, Object> mp = new HashMap<String, Object>();
					mp.put("pnitNum", insTablePushTaskVo.getTaskNum());
					mp.put("pnitType", insTablePushTaskVo.getTitle());
					List<BsPnInsTask> bsPnInsTaskList = bsPnInsTaskDao
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
					BsPnInsTask bsPnInsTask = null;
					if (bsPnInsTaskList != null && bsPnInsTaskList.size() > 0) {
						bsPnInsTask = bsPnInsTaskList.get(0);
						d.put("BsPnInsTask", bsPnInsTask);
					}
					groupData.add(d);
					if (bsPnInsTask != null) {
						
						// 查询子list数据和添加子list数据
						if (Constant.ROUTINE_INS.equals(title)) {// 常规巡查
							bsSupervisionPointList = bsSupervisionPointDao.queryForEq("pnitId",
									bsPnInsTask.getPnitId());
							List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
							
							bsRoutineInsAreaList = bsRoutineInsAreaDao.queryForEq("pnitId",
									bsPnInsTask.getPnitId());
							if (bsRoutineInsAreaList != null) {
								for (BsRoutineInsArea bsRoutineInsArea : bsRoutineInsAreaList) {
									d = new HashMap<String, Object>();
									d.put("BsRoutineInsArea", bsRoutineInsArea);
									child.add(d);
									String riaCoordinate = bsRoutineInsArea.getRiaCoordinate();
									Geometry geo1 = MapUtil.wkt2Geometry(riaCoordinate);
									if (bsSupervisionPointList != null) {
										for (BsSupervisionPoint bsSupervisionPoint : bsSupervisionPointList) {
											String spCoordinate =bsSupervisionPoint.getSpCoordinate();
											Geometry geo2 = MapUtil.wkt2Geometry(spCoordinate);
											try{
												if(GeometryEngine.contains(geo1, geo2, sr)){
													d = new HashMap<String, Object>();
													bsSupervisionPoint.setAreaNum(bsRoutineInsArea.getAreaNum());
													d.put("BsSupervisionPoint", bsSupervisionPoint);
													child.add(d);
												}
											}catch(Exception e){
												
											}
										}
									}
								}
							}
							childData.add(child);
						}else if(Constant.SPECIAL_INS.equals(title)){// 专项巡查
							bsInsFacInfoList = bsInsFacInfoDao.queryForEq("pnitId",
									bsPnInsTask.getPnitId());
							List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
							if (bsInsFacInfoList != null) {
								for (BsInsFacInfo bsInsFacInfo : bsInsFacInfoList) {
									d = new HashMap<String, Object>();
									d.put("BsInsFacInfo", bsInsFacInfo);
									child.add(d);
								}
							}
							childData.add(child);
						}else if(Constant.EMPHASIS_INS.equals(title)){// 重点区域巡查
							bsEmphasisInsAreaList = bsEmphasisInsAreaDao.queryForEq("pnitId",
									bsPnInsTask.getPnitId());
							List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
							if (bsEmphasisInsAreaList != null) {
								for (BsEmphasisInsArea bsEmphasisInsArea : bsEmphasisInsAreaList) {
									d = new HashMap<String, Object>();
									d.put("BsEmphasisInsArea", bsEmphasisInsArea);
									child.add(d);
								}
							}
							childData.add(child);
						}else if(Constant.LEAK_INS.equals(title)){// 查漏
							bsLeakInsAreaList = bsLeakInsAreaDao.queryForEq("pnitId",
									bsPnInsTask.getPnitId());
							List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
							if (bsLeakInsAreaList != null) {
								for (BsLeakInsArea bsLeakInsArea : bsLeakInsAreaList) {
									d = new HashMap<String, Object>();
									d.put("BsLeakInsArea", bsLeakInsArea);
									child.add(d);
								}
							}
							childData.add(child);
						}
					}
				}
				
			}
		}
	}

	@Override
	public List<Map<String, Object>> updateList(String type,
			List<Map<String, Object>> data) {
		// TODO Auto-generated method stub
		List<InsTablePushTaskVo> insTablePushTaskVoList = null;
		if (Constant.TASK_DOWNLOAD_N.equals(type)) {
			Map<String, Object> d = null;
			try {
				Dao<InsTablePushTaskVo, Long> tablePushTaskDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsTablePushTaskVo.class);
				/*Map<String, Object> m = new HashMap<String, Object>();
				m.put("isDownload", null);*/
				QueryBuilder<InsTablePushTaskVo, Long> queryBuilder = tablePushTaskDao.queryBuilder();
				queryBuilder.where().isNull("isDownload");
				insTablePushTaskVoList = tablePushTaskDao.query(queryBuilder.prepare());
				if(insTablePushTaskVoList!=null){
					for(InsTablePushTaskVo insTablePushTaskVo :insTablePushTaskVoList){
						d = new HashMap<String, Object>();
						d.put("InsTablePushTaskVo", insTablePushTaskVo);
						data.add(d);
					}
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(Constant.URGENT.equals(type)){
			Dao<InsTablePushTaskVo, Long> tablePushTaskDao = null;
			try {
				tablePushTaskDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsTablePushTaskVo.class);
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("isDownload", "1");
				m.put("title", Constant.RRWO_TASK);
				insTablePushTaskVoList = tablePushTaskDao
						.queryForFieldValuesArgs(m);
				addData2(insTablePushTaskVoList, data,null);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return data;
	}
	private void addData2(List<InsTablePushTaskVo> insTablePushTaskVoList,List<Map<String, Object>> groupData
			,List<List<Map<String, Object>>> childData) throws SQLException{
		if (insTablePushTaskVoList != null) {
			Map<String, Object> d = null;
			for (InsTablePushTaskVo insTablePushTaskVo : insTablePushTaskVoList) {
				if(Constant.RRWO_TASK.equals(insTablePushTaskVo.getTitle())){
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
					Dao<BsRushRepairWorkOrder, Long> bsRushRepairWorkOrderDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(BsRushRepairWorkOrder.class);
					Map<String, Object> mp1 = new HashMap<String, Object>();
					mp1.put("rrwoNum", insTablePushTaskVo.getTaskNum());
					List<BsRushRepairWorkOrder> bsRushRepairWorkOrderList = bsRushRepairWorkOrderDao
							.queryForFieldValuesArgs(mp1);
					d = new HashMap<String, Object>();
					d.put("InsTablePushTaskVo", insTablePushTaskVo);
					if (bsRushRepairWorkOrderList != null
							&& bsRushRepairWorkOrderList.size() > 0) {
						d.put("Module", getTaskModule("biz.rush_repair_work_order_yh"));
						d.put("BsRushRepairWorkOrder",
								bsRushRepairWorkOrderList.get(0));
					}
					if (taskUploadStateVOList != null
							&& taskUploadStateVOList.size() > 0) {
						d.put("TaskUploadStateVO", taskUploadStateVOList.get(0));
					}
					groupData.add(d);
					if(childData!=null){
						List<Map<String, Object>> child = new ArrayList<Map<String, Object>>();
						childData.add(child);
					}
				}
			}
		}
	}

	@Override
	public void blackHandle(Intent data) {
		// TODO Auto-generated method stub
		
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
	public void updateTaskList(String title, String taskCategory,Map<String, Object> condition,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException {
		// TODO Auto-generated method stub
		List<InsTablePushTaskVo> insTablePushTaskVoList = new ArrayList<InsTablePushTaskVo>();
		Map<String, Object> m;
		
		if(title!=null){
			if(title.equals(Constant.PIPE_PATROL_YH)){
				m = new HashMap<String, Object>();
				m.put("isDownload", "1");
				m.put("title", Constant.EMPHASIS_INS);
				
				insTablePushTaskVoList.addAll( AppContext.getInstance()
						.getAppDbHelper().getDao(InsTablePushTaskVo.class)
						.queryForFieldValuesArgs(m));
				m = new HashMap<String, Object>();
				m.put("isDownload", "1");
				m.put("title", Constant.ROUTINE_INS);
				
				insTablePushTaskVoList.addAll( AppContext.getInstance()
						.getAppDbHelper().getDao(InsTablePushTaskVo.class)
						.queryForFieldValuesArgs(m));
				m = new HashMap<String, Object>();
				m.put("isDownload", "1");
				m.put("title", Constant.SPECIAL_INS);
				
				insTablePushTaskVoList.addAll( AppContext.getInstance()
						.getAppDbHelper().getDao(InsTablePushTaskVo.class)
						.queryForFieldValuesArgs(m));
			}else{
				m = new HashMap<String, Object>();
				m.put("isDownload", "1");
				m.put("title", title);
				
				insTablePushTaskVoList.addAll( AppContext.getInstance()
						.getAppDbHelper().getDao(InsTablePushTaskVo.class)
						.queryForFieldValuesArgs(m));
			}
			
		}else{
			m = new HashMap<String, Object>();
			m.put("isDownload", "1");
			insTablePushTaskVoList.addAll( AppContext.getInstance()
					.getAppDbHelper().getDao(InsTablePushTaskVo.class)
					.queryForFieldValuesArgs(m));
		}
		
		addData2(insTablePushTaskVoList, groupData,childData);
		addData(insTablePushTaskVoList, groupData, childData);
		
	}

}
