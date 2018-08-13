package com.movementinsome.app.mytask.handle;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.InsDatumInaccurate;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.util.JsonAnalysisUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ZSDownloadHandle implements DownloadBaseHandle {

	private String result;
	private InsTablePushTaskVo insTablePushTaskVo;

	public ZSDownloadHandle(String result, InsTablePushTaskVo insTablePushTaskVo) {
		this.result = result;
		this.insTablePushTaskVo = insTablePushTaskVo;
	}

	@Override
	public String handle() {
		// TODO Auto-generated method stub
		if (result != null) {
			try {
				JSONObject downloadObject = new JSONObject(result);
				String content0 = (String) downloadObject.get("content");
				String tableName = downloadObject.getString("tableName");
				JSONArray content = new JSONArray(content0);
				String code = downloadObject.getString("code");
				if ("1".equals(code)) {
					if (Constant.PLAN_FAC_ZS_CYCLE.equals(tableName)
							|| Constant.PLAN_FAC_ZS_TEMPORARY.equals(tableName)
							|| Constant.PLAN_TIEPAI_CYCLE.equals(tableName)
							|| Constant.PLAN_TIEPAI_TEMPORARY.equals(tableName)
							
							|| Constant.PLAN_PATROL_SCHEDULE.equals(tableName)
							|| Constant.PLAN_PATROL_ZS_CYCLE.equals(tableName)
							|| Constant.PLAN_PATROL_ZS_TEMPORARY.equals(tableName)) {
						Dao<InsPlanTaskVO, Long> insPlanTaskDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsPlanTaskVO.class);
						Dao<InsPatrolDataVO, Long> patrolDataDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsPatrolDataVO.class);
						
						Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(InsPatrolAreaData.class);
						
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
								JSONObject formList = content.getJSONObject(i)
										.getJSONObject("formList");
								
								if (formList != null) {
									String insPatrolDataStr = formList
											.getString("InsPatrolData");
									JSONArray insPatrolDataVOS = new JSONArray(insPatrolDataStr);
									
									String insPatrolAreaDataStr = formList
											.getString("InsPatrolAreaData");
									JSONArray insPatrolAreaDataS = new JSONArray(insPatrolAreaDataStr);
									
									for (int j = 0; j < insPatrolDataVOS.length(); ++j) {
										InsPatrolDataVO insPatrolDataVO = new InsPatrolDataVO();
										JsonAnalysisUtil.setJsonObjectData(
												insPatrolDataVOS.getJSONObject(j),
												insPatrolDataVO);
										insPatrolDataVO.setGuid(UUID
												.randomUUID().toString());
										insPatrolDataVO
												.setTaskCategory(insTablePushTaskVo
														.getTaskCategory());
										insPatrolDataVO.setTitle(tableName);
										patrolDataDao.create(insPatrolDataVO);
									}
									for (int j = 0; j < insPatrolAreaDataS.length(); ++j) {
										InsPatrolAreaData insPatrolAreaData = new InsPatrolAreaData();
										JsonAnalysisUtil.setJsonObjectData(
												insPatrolAreaDataS.getJSONObject(j),
												insPatrolAreaData);
										insPatrolAreaData
											.setTaskCategory(insTablePushTaskVo
												.getTaskCategory());
										insPatrolAreaData.setTitle(tableName);
										insPatrolAreaDataDao.create(insPatrolAreaData);
									}
								}
							}
							updatePush2(insTablePushTaskVo, insPlanTaskVO);
							return "1";
						} else {
							return "0";
						}
					}else if(Constant.INSMAPINACCURATE_FORM.equals(tableName)){
						if (content != null && content.length() > 0) {
							Dao<InsDatumInaccurate, Long> insDatumInaccurateDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(InsDatumInaccurate.class);
							for (int i = 0; i < content.length(); ++i) {
								InsDatumInaccurate insDatumInaccurate =new InsDatumInaccurate();
								JsonAnalysisUtil
								.setJsonObjectData(content
										.getJSONObject(i), insDatumInaccurate);
								insDatumInaccurateDao.create(insDatumInaccurate);
							}
							updatePush(insTablePushTaskVo);
							return "1";
						}else{
							// 没有相应的处理类型
							return "0";
						}
					} else {
						return "3";
					}
				} else {
					return "0";
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return "0";
			}
		} else {
			return null;
		}
	}

	private void updatePush2(InsTablePushTaskVo insTablePushTaskVo,
			InsPlanTaskVO insPlanTaskVO) {
		try {
			Dao<InsTablePushTaskVo, Long> insTablePushTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);

			Date workTaskPEDate = stringToDate(insPlanTaskVO
					.getWorkTaskPEDate());
			if (null != workTaskPEDate
					&& new Date().getTime() > (workTaskPEDate.getTime() + 24 * 60 * 60 * 1000)) {
				insTablePushTaskVo.setTbType("3");
			} else if (null != workTaskPEDate
					&& new Date().getTime() > workTaskPEDate.getTime() - 24
							* 60 * 60 * 1000) {
				insTablePushTaskVo.setTbType("4");
			}
			insTablePushTaskVo.setIsDownload("1");
			insTablePushTaskDao.update(insTablePushTaskVo);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void updatePush(InsTablePushTaskVo insTablePushTaskVo) {
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
