package com.movementinsome.app.mytask.handle;

import com.j256.ormlite.dao.Dao;
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
import com.movementinsome.kernel.util.JsonAnalysisUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YHDownloadHandle implements DownloadBaseHandle {

	private String result;
	private InsTablePushTaskVo insTablePushTaskVo;

	public YHDownloadHandle(String result, InsTablePushTaskVo insTablePushTaskVo) {
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
					if (Constant.ROUTINE_INS.equals(tableName)
							|| Constant.SPECIAL_INS.equals(tableName)
							|| Constant.EMPHASIS_INS.equals(tableName)
							|| Constant.LEAK_INS.equals(tableName)) {
						Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext
								.getInstance().getAppDbHelper()
								.getDao(BsPnInsTask.class);
						BsPnInsTask bsPnInsTask = new BsPnInsTask();
						if (content != null && content.length() > 0) {
							for (int i = 0; i < content.length(); ++i) {
								JsonAnalysisUtil.setJsonObjectData(
										content.getJSONObject(i), bsPnInsTask);
								bsPnInsTaskDao.delete(bsPnInsTask);
								bsPnInsTaskDao.create(bsPnInsTask);
								/*String formListStr = content.getJSONObject(i)
										.getString("formList");*/
								JSONObject formList =content.getJSONObject(i).getJSONObject("formList");
								if (Constant.ROUTINE_INS.equals(tableName)) {

									Dao<BsSupervisionPoint, Long> bsSupervisionPointDao = AppContext
											.getInstance().getAppDbHelper()
											.getDao(BsSupervisionPoint.class);
									Dao<BsRoutineInsArea, Long> bsRoutineInsAreaDao = AppContext
											.getInstance().getAppDbHelper()
											.getDao(BsRoutineInsArea.class);

									JSONArray bsSupervisionPointS = formList
											.getJSONArray("BsSupervisionPoint");
									JSONArray bsRoutineInsAreaS = formList
											.getJSONArray("BsRoutineInsArea");

									if (bsSupervisionPointS != null
											&& bsSupervisionPointS.length() > 0) {
										for (int j = 0; j < bsSupervisionPointS
												.length(); ++j) {
											BsSupervisionPoint bsSupervisionPoint = new BsSupervisionPoint();
											JsonAnalysisUtil.setJsonObjectData(
													bsSupervisionPointS
															.getJSONObject(j),
													bsSupervisionPoint);
											bsSupervisionPoint.setWorkTaskNum(bsPnInsTask.getPnitNum());
											bsSupervisionPointDao
													.create(bsSupervisionPoint);
										}
									}
									if (bsRoutineInsAreaS != null
											&& bsRoutineInsAreaS.length() > 0) {
										for (int j = 0; j < bsRoutineInsAreaS
												.length(); ++j) {
											BsRoutineInsArea bsRoutineInsArea = new BsRoutineInsArea();
											JsonAnalysisUtil.setJsonObjectData(
													bsRoutineInsAreaS
															.getJSONObject(j),
													bsRoutineInsArea);
											bsRoutineInsArea.setWorkTaskNum(bsPnInsTask.getPnitNum());
											bsRoutineInsAreaDao
													.create(bsRoutineInsArea);
										}
									}
									
								} else if (Constant.SPECIAL_INS
										.equals(tableName)) {

									JSONArray bsInsFacInfoS = formList
											.getJSONArray("BsInsFacInfo");
									Dao<BsInsFacInfo, Long> bsInsFacInfoDao = AppContext
											.getInstance().getAppDbHelper()
											.getDao(BsInsFacInfo.class);
									if (bsInsFacInfoS != null
											&& bsInsFacInfoS.length() > 0) {
										for (int j = 0; j < bsInsFacInfoS
												.length(); ++j) {
											BsInsFacInfo bsInsFacInfo = new BsInsFacInfo();
											JsonAnalysisUtil.setJsonObjectData(
													bsInsFacInfoS
															.getJSONObject(j),
													bsInsFacInfo);
											bsInsFacInfo.setWorkTaskNum(bsPnInsTask.getPnitNum());
											bsInsFacInfoDao
													.create(bsInsFacInfo);
										}
									}
									
								} else if (Constant.EMPHASIS_INS
										.equals(tableName)) {

									JSONArray bsEmphasisInsAreaS = formList
											.getJSONArray("BsEmphasisInsArea");
									Dao<BsEmphasisInsArea, Long> bsEmphasisInsAreaDao = AppContext
											.getInstance().getAppDbHelper()
											.getDao(BsEmphasisInsArea.class);
									if (bsEmphasisInsAreaS != null
											&& bsEmphasisInsAreaS.length() > 0) {
										for (int j = 0; j < bsEmphasisInsAreaS
												.length(); ++j) {
											BsEmphasisInsArea bsEmphasisInsArea = new BsEmphasisInsArea();
											JsonAnalysisUtil.setJsonObjectData(
													bsEmphasisInsAreaS
															.getJSONObject(j),
													bsEmphasisInsArea);
											bsEmphasisInsArea.setWorkTaskNum(bsPnInsTask.getPnitNum());
											bsEmphasisInsAreaDao
													.create(bsEmphasisInsArea);
										}
									}
									
								} else if (Constant.LEAK_INS.equals(tableName)) {

									JSONArray bsLeakInsAreaS = formList
											.getJSONArray("BsLeakInsArea");
									Dao<BsLeakInsArea, Long> bsLeakInsAreaDao = AppContext
											.getInstance().getAppDbHelper()
											.getDao(BsLeakInsArea.class);
									if (bsLeakInsAreaS != null
											&& bsLeakInsAreaS.length() > 0) {
										for (int j = 0; j < bsLeakInsAreaS
												.length(); ++j) {
											BsLeakInsArea bsLeakInsArea = new BsLeakInsArea();
											JsonAnalysisUtil.setJsonObjectData(
													bsLeakInsAreaS
															.getJSONObject(j),
													bsLeakInsArea);
											bsLeakInsArea.setWorkTaskNum(bsPnInsTask.getPnitNum());
											bsLeakInsAreaDao
													.create(bsLeakInsArea);
										}
									}
									
								}
							}
							updatePush(insTablePushTaskVo, bsPnInsTask);
							return "1";
						} else {
							return "0";
						}
					}else if(Constant.RRWO_TASK.equals(tableName)){
						if (content != null && content.length() > 0) {
							Dao<BsRushRepairWorkOrder, Long> bsRushRepairWorkOrderDao = AppContext
							.getInstance().getAppDbHelper()
							.getDao(BsRushRepairWorkOrder.class);
							for (int i = 0; i < content.length(); ++i) {
								BsRushRepairWorkOrder bsRushRepairWorkOrder =new BsRushRepairWorkOrder();
								JsonAnalysisUtil
								.setJsonObjectData(content
										.getJSONObject(i), bsRushRepairWorkOrder);
								bsRushRepairWorkOrderDao.create(bsRushRepairWorkOrder);
							}
							updatePush(insTablePushTaskVo);
							return "1";
						}else{
							// 没有相应的处理类型
							return "0";
						}
					}else{
						return "3";
					}
				} else {
					return "0";
				}
			} catch (Exception e) {
				// TODO: handle exception
				return "0";
			}
		} else {
			return null;
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

	private void updatePush(InsTablePushTaskVo insTablePushTaskVo,
			BsPnInsTask bsPnInsTask) {
		try {
			Dao<InsTablePushTaskVo, Long> insTablePushTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);

			Date workTaskPEDate = stringToDate(bsPnInsTask.getPlanEDateStr());
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
