package com.movementinsome.app.mytask.handle;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.InsDredgePTask;
import com.movementinsome.database.vo.InsFacMaintenanceData;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.InsTableSaveDataVo;
import com.movementinsome.kernel.util.JsonAnalysisUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DownloadHandle implements DownloadBaseHandle{

	private String result;
	private InsTablePushTaskVo insTablePushTaskVo;
	public DownloadHandle(String result,InsTablePushTaskVo insTablePushTaskVo){
		this.result=result;
		this.insTablePushTaskVo=insTablePushTaskVo;
	}
	// 下载数据处理
	public String handle() {
		if (result != null) {
			JSONObject downloadObject;
			try {
				downloadObject = new JSONObject(result);
				String content0 = (String) downloadObject.get("content");
				String tableName = downloadObject.getString("tableName");
				JSONArray content = new JSONArray(content0);
				String code = downloadObject.getString("code");
				if ("1".equals(code)) {
					if (Constant.XJGL_GJD_CYCLE.equals(tableName)
							|| Constant.XJGL_GJD_TEMPORARY.equals(tableName)) {// 关键点
						Dao<InsKeyPointPatrolData, Long> insKeyPointPatrolDataDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsKeyPointPatrolData.class);
						Dao<InsPlanTaskVO, Long> insPlanTaskDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsPlanTaskVO.class);
						InsPlanTaskVO insPlanTaskVO = new InsPlanTaskVO();
						if (content != null && content.length() > 0) {
							for (int i = 0; i < content.length(); ++i) {
								JsonAnalysisUtil
										.setJsonObjectData(
												content.getJSONObject(i),
												insPlanTaskVO);
								insPlanTaskVO.setGuid(UUID.randomUUID()
										.toString());
								insPlanTaskDao.create(insPlanTaskVO);
								String formListStr = content.getJSONObject(i)
										.getString("formList");
								JSONArray formList = new JSONArray(formListStr);
								if (formList != null) {
									for (int j = 0; j < formList.length(); ++j) {
										InsKeyPointPatrolData insKeyPointPatrolData = new InsKeyPointPatrolData();
										JsonAnalysisUtil.setJsonObjectData(
												formList.getJSONObject(j),
												insKeyPointPatrolData);
										insKeyPointPatrolData.setId(UUID
												.randomUUID().toString());
										insKeyPointPatrolDataDao
												.create(insKeyPointPatrolData);
									}
								}
							}
							updatePush2(insTablePushTaskVo, insPlanTaskVO);
							return "1";
						} else {
							return "0";
						}
					} else if (Constant.PLAN_CONSTRUCTION_CYCLE
							.equals(tableName)
							|| Constant.PLAN_CONSTRUCTION_TEMPORARY
									.equals(tableName)) {// 工地
						Dao<InsSiteManage, Long> insSiteManageDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsSiteManage.class);
						Dao<InsPlanTaskVO, Long> insPlanTaskDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsPlanTaskVO.class);
						InsPlanTaskVO insPlanTaskVO = new InsPlanTaskVO();
						if (content != null && content.length() > 0) {
							for (int i = 0; i < content.length(); ++i) {
								JsonAnalysisUtil
										.setJsonObjectData(
												content.getJSONObject(i),
												insPlanTaskVO);
								insPlanTaskVO.setGuid(UUID.randomUUID()
										.toString());
								insPlanTaskVO.setWorkOrder((long) 1);
								insPlanTaskDao.create(insPlanTaskVO);
								String formListStr = content.getJSONObject(i)
										.getString("formList");
								JSONArray formList = new JSONArray(formListStr);
								if (formList != null) {
									for (int j = 0; j < formList.length(); ++j) {
										InsSiteManage insSiteManage = new InsSiteManage();
										JsonAnalysisUtil.setJsonObjectData(
												formList.getJSONObject(j),
												insSiteManage);
										insSiteManage.setId(UUID.randomUUID()
												.toString());
										insSiteManage.setIsFirst("0");
										insSiteManageDao.create(insSiteManage);
									}
								}
							}
							updatePush2(insTablePushTaskVo, insPlanTaskVO);
							return "1";
						} else {
							return "0";
						}
					} else if (Constant.CYCLE_PLAN.equals(tableName)
							|| Constant.INTERIM_PLAN.equals(tableName)
							|| Constant.PLAN_VALVE_CYCLE.equals(tableName)
							|| Constant.PLAN_VALVE_TEMPORARY.equals(tableName)
							|| Constant.PLAN_HYDRANT_CYCLE.equals(tableName)
							|| Constant.PLAN_HYDRANT_TEMPORARY.equals(tableName)) {// 计划巡检

						Dao<InsPlanTaskVO, Long> insPlanTaskDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsPlanTaskVO.class);
						Dao<InsPatrolDataVO, Long> patrolDataDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsPatrolDataVO.class);
						InsPlanTaskVO insPlanTaskVO = new InsPlanTaskVO();

						if (content != null && content.length() > 0) {
							for (int i = 0; i < content.length(); ++i) {
								JsonAnalysisUtil
										.setJsonObjectData(
												content.getJSONObject(i),
												insPlanTaskVO);
								insPlanTaskVO.setGuid(UUID.randomUUID()
										.toString());
								insPlanTaskDao.create(insPlanTaskVO);
								String formListStr = content.getJSONObject(i)
										.getString("formList");
								JSONArray formList = new JSONArray(formListStr);
								if (formList != null) {
									for (int j = 0; j < formList.length(); ++j) {
										InsPatrolDataVO insPatrolDataVO = new InsPatrolDataVO();
										JsonAnalysisUtil.setJsonObjectData(
												formList.getJSONObject(j),
												insPatrolDataVO);
										insPatrolDataVO.setGuid(UUID
												.randomUUID().toString());
										insPatrolDataVO
												.setTaskCategory(insTablePushTaskVo
														.getTaskCategory());
										insPatrolDataVO.setTitle(tableName);
										patrolDataDao.create(insPatrolDataVO);
									}
								}
							}
							updatePush2(insTablePushTaskVo, insPlanTaskVO);
							return "1";
						} else {
							return "0";
						}

					} else if (Constant.QSGL_JHX.equals(tableName)) {// 计划清梳
						Dao<InsPlanTaskVO, Long> insPlanTaskDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsPlanTaskVO.class);
						Dao<InsDredgePTask, Long> insDredgePTaskDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsDredgePTask.class);
						InsPlanTaskVO insPlanTaskVO = new InsPlanTaskVO();
		
						if (content != null && content.length() > 0) {
							for (int i = 0; i < content.length(); ++i) {
								JsonAnalysisUtil
										.setJsonObjectData(
												content.getJSONObject(i),
												insPlanTaskVO);
								insPlanTaskVO.setGuid(UUID.randomUUID()
										.toString());
								insPlanTaskDao.create(insPlanTaskVO);
								String formListStr = content.getJSONObject(i)
										.getString("formList");
								JSONArray formList = new JSONArray(formListStr);
								if (formList != null) {
									for (int j = 0; j < formList.length(); ++j) {
										InsDredgePTask insDredgePTask = new InsDredgePTask();
										JsonAnalysisUtil.setJsonObjectData(
												formList.getJSONObject(j),
												insDredgePTask);
										insDredgePTask.setId(UUID
												.randomUUID().toString());
										insDredgePTaskDao.create(insDredgePTask);
									}
								}
							}
							updatePush2(insTablePushTaskVo, insPlanTaskVO);
							return "1";
						} else {
							return "0";
						}
						
					}else if(Constant.WXGL_JHXWX.equals(tableName)
							||Constant.FMGL_JHXWX.equals(tableName)){// 计划维修
						Dao<InsPlanTaskVO, Long> insPlanTaskDao = AppContext
						.getInstance().getAppDbHelper()
						.getDao(InsPlanTaskVO.class);
						Dao<InsFacMaintenanceData, Long> insFacMaintenanceDataDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsFacMaintenanceData.class);
						InsPlanTaskVO insPlanTaskVO = new InsPlanTaskVO();
		
						if (content != null && content.length() > 0) {
							for (int i = 0; i < content.length(); ++i) {
								JsonAnalysisUtil
										.setJsonObjectData(
												content.getJSONObject(i),
												insPlanTaskVO);
								insPlanTaskVO.setGuid(UUID.randomUUID()
										.toString());
								insPlanTaskDao.create(insPlanTaskVO);
								String formListStr = content.getJSONObject(i)
										.getString("formList");
								JSONArray formList = new JSONArray(formListStr);
								if (formList != null) {
									for (int j = 0; j < formList.length(); ++j) {
										InsFacMaintenanceData insFacMaintenanceData = new InsFacMaintenanceData();
										JsonAnalysisUtil.setJsonObjectData(
												formList.getJSONObject(j),
												insFacMaintenanceData);
										insFacMaintenanceData.setId(UUID
												.randomUUID().toString());
										insFacMaintenanceDataDao.create(insFacMaintenanceData);
									}
								}
							}
							updatePush2(insTablePushTaskVo, insPlanTaskVO);
							return "1";
						} else {
							return "0";
						}
					} else if (Constant.COMPLAINANT.equals(tableName)
							|| Constant.PROBLEM.equals(tableName)) {// 投诉单
						if (content != null && content.length() > 0) {
							for (int i = 0; i < content.length(); ++i) {
								
								Dao<InsTableSaveDataVo, Long> saveDataDao = AppContext
										.getInstance().getAppDbHelper()
										.getDao(InsTableSaveDataVo.class);
								InsTableSaveDataVo insTableSaveDataVo = new InsTableSaveDataVo();
								insTableSaveDataVo
										.setMainNumber(insTablePushTaskVo
												.getTaskNum());
								insTableSaveDataVo.setGuid(UUID.randomUUID()
										.toString());
								insTableSaveDataVo.setTbData(content
										.getJSONObject(i).toString());
								insTableSaveDataVo.setTitle(insTablePushTaskVo
										.getTitle());
								insTableSaveDataVo
										.setTaskCategory(insTablePushTaskVo
												.getTaskCategory());
								saveDataDao.create(insTableSaveDataVo);
							}
							updatePush(insTablePushTaskVo);
							return "1";
						} else {
							return "0";
						}
					} else {
						// 没有相应的处理类型
						return "3";
					}
				} else {
					return "0";
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "0";
			}
		}else{
			return null;
		}
		
	}

	private void updatePush(InsTablePushTaskVo insTablePushTaskVo) {
		try {
			Dao<InsTablePushTaskVo, Long> insTablePushTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);

			insTablePushTaskVo.setIsDownload("1");
			insTablePushTaskDao.update(insTablePushTaskVo);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private  void updatePush2(InsTablePushTaskVo insTablePushTaskVo,InsPlanTaskVO insPlanTaskVO) {
		try {
			Dao<InsTablePushTaskVo, Long> insTablePushTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);

			Date workTaskPEDate = stringToDate(insPlanTaskVO.getWorkTaskPEDate());
			if (null!=workTaskPEDate && new Date().getTime() > (workTaskPEDate.getTime() + 24 * 60 * 60 * 1000)) {
				insTablePushTaskVo.setTbType("3");
			}else if(null!=workTaskPEDate && new Date().getTime() > workTaskPEDate.getTime()- 24 * 60 * 60 * 1000){
				insTablePushTaskVo.setTbType("4");
			}
			insTablePushTaskVo.setIsDownload("1");
			insTablePushTaskDao.update(insTablePushTaskVo);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
