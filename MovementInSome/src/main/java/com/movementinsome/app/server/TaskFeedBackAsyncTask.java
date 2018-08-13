package com.movementinsome.app.server;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

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
import com.movementinsome.database.vo.CoordinateVO;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsDatumInaccurate;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.InsTableSaveDataVo;
import com.movementinsome.database.vo.TaskUploadStateVO;
import com.movementinsome.database.vo.WsComplainantFormVO;
import com.movementinsome.kernel.util.MyDateTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 上传状态
 * @author gddst
 *
 */
public class TaskFeedBackAsyncTask extends AsyncTask<String, Void, String> {

	/*	状态：
	0：未派工
	1：消息未送达
	2：消息已送达
	3: 已接收
	4：处理中
	5：已完成
	6: 开始
	7: 暂停*/
	private boolean flgShow;// 是否显示进度条
	private ProgressDialog progressDialog;// 进度条
	private Context context;
	private Dao<TaskUploadStateVO, Long> taskUploadStateDao;
	private List<TaskUploadStateVO> taskUploadStateVOList;
	// 上传参数
	private String moiNum;// 终端上传数据的唯一编码
	private String imei;// 手机唯一编码
	private String taskCategory;// 表单类型
	private String tableName;// 表单在数据库中表名
	private String usId;// 用户ID
	private String usUsercode;// 用户编号
	private String usNameZh;// 用户名称
	private String updateDate;// 修改日期
	private String gpsCoord;// 坐标值,"经度，纬度"
	private String mapCoord;// 地图相应坐标值,"x，y"
	private String taskNum;// 任务编号
	private String status;// 最新状态
	private String oldStatus="";// 上次提交的状态
	private String pushState;
	private String results;
	private Map<String, String> parameters;
	private boolean isFinish;
	private String isUpload;
	

	public TaskFeedBackAsyncTask (Context context,boolean flgShow ,boolean isFinish
			,String taskNum,String status,String moiNum,String tableName,String taskCategory
			,String pushState,String results,Map<String, String> parameters){
		this.context = context;
		this.flgShow = flgShow;
		this.isFinish=isFinish;
		
		this.taskNum=taskNum;
		this.status=status;
		this.moiNum=moiNum;
		this.tableName=tableName;
		this.taskCategory=taskCategory;
		this.pushState=pushState;
		this.results=results;
		this.parameters=parameters;
	
		imei=AppContext.getInstance().getPhoneIMEI();
		usId=AppContext.getInstance().getCurUser().getUserId();
		usUsercode=AppContext.getInstance().getCurUser().getUserName();
		usNameZh=AppContext.getInstance().getCurUser().getUserAlias();
		updateDate=MyDateTools.date2String(new Date());
		if (AppContext.getInstance().getCurLocation()!=null){
			gpsCoord=AppContext.getInstance().getCurLocation().getCurGpsPosition();
			mapCoord=AppContext.getInstance().getCurLocation().getCurMapPosition();
		}
	}
	public TaskFeedBackAsyncTask (Context context,boolean flgShow ,boolean isFinish
			,String taskNum,String status,String moiNum,String tableName,String taskCategory
			,String pushState,String results,Map<String, String> parameters,String isUpload){
		this.context = context;
		this.flgShow = flgShow;
		this.isFinish=isFinish;
		
		this.taskNum=taskNum;
		this.status=status;
		this.moiNum=moiNum;
		this.tableName=tableName;
		this.taskCategory=taskCategory;
		this.pushState=pushState;
		this.results=results;
		this.parameters=parameters;
		this.isUpload= isUpload;
	
		imei=AppContext.getInstance().getPhoneIMEI();
		usId=AppContext.getInstance().getCurUser().getUserId();
		usUsercode=AppContext.getInstance().getCurUser().getUserName();
		usNameZh=AppContext.getInstance().getCurUser().getUserAlias();
		updateDate=MyDateTools.date2String(new Date());
		if (AppContext.getInstance().getCurLocation()!=null){
			gpsCoord=AppContext.getInstance().getCurLocation().getCurGpsPosition();
			mapCoord=AppContext.getInstance().getCurLocation().getCurMapPosition();
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if(flgShow){
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage("正在提交中，请稍后");
			progressDialog.show();
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		//SpringUtil spring = new SpringUtil(context);
		String result = "";
		try {
			JSONObject ob=new JSONObject();
			ob.put("moiNum", moiNum);
			ob.put("imei", imei);
			ob.put("tableName", tableName);
			ob.put("usId", usId);
			ob.put("usUsercode", usUsercode);
			ob.put("usNameZh", usNameZh);
			ob.put("updateDate", updateDate);
			ob.put("gpsCoord", gpsCoord);
			ob.put("mapCoord", mapCoord);
			ob.put("taskNum", taskNum);
			ob.put("status", status);
			ob.put("taskCategory", taskCategory);
			ob.put("pushState", pushState);
			ob.put("remarks", results);
			if(parameters!=null){
				Iterator<String> keys=parameters.keySet().iterator();
				while(keys.hasNext()){
					String key=keys.next();
					ob.put(key, parameters.get(key));
				}
				
			}
			
			
			if(!Constant.PUSH_TITLE_MSG_COMMON.equals(tableName)){// 不是普通消息
				taskUploadStateDao = AppContext.getInstance().getAppDbHelper().getDao(TaskUploadStateVO.class);
				Map<String, Object>m=new HashMap<String, Object>();
				m.put("taskNum", taskNum);
				m.put("tableName", tableName);
				m.put("taskCategory", taskCategory);
				taskUploadStateVOList=taskUploadStateDao.queryForFieldValuesArgs(m);
				TaskUploadStateVO iaskUploadStateVO=null;
				if(taskUploadStateVOList!=null&&taskUploadStateVOList.size()>0){// 存在更新数据
					
					iaskUploadStateVO=taskUploadStateVOList.get(0);
					oldStatus=iaskUploadStateVO.getStatus();
					iaskUploadStateVO.setMoiNum(moiNum);
					iaskUploadStateVO.setImei(imei);
					iaskUploadStateVO.setTableName(tableName);
					iaskUploadStateVO.setUsId(usId);
					iaskUploadStateVO.setUsUsercode(usUsercode);
					iaskUploadStateVO.setUsNameZh(usNameZh);
					iaskUploadStateVO.setUpdateDate(updateDate);
					iaskUploadStateVO.setGpsCoord(gpsCoord);
					iaskUploadStateVO.setMapCoord(mapCoord);
					iaskUploadStateVO.setTaskNum(taskNum);
					iaskUploadStateVO.setStatus(status);
					iaskUploadStateVO.setTaskCategory(taskCategory);
					taskUploadStateDao.update(iaskUploadStateVO);
				}else{// 不存在插入数据
					iaskUploadStateVO=new TaskUploadStateVO();// 状态对象
					iaskUploadStateVO.setGuid(UUID.randomUUID().toString());
					iaskUploadStateVO.setMoiNum(moiNum);
					iaskUploadStateVO.setImei(imei);
					iaskUploadStateVO.setTableName(tableName);
					iaskUploadStateVO.setUsId(usId);
					iaskUploadStateVO.setUsUsercode(usUsercode);
					iaskUploadStateVO.setUsNameZh(usNameZh);
					iaskUploadStateVO.setUpdateDate(updateDate);
					iaskUploadStateVO.setGpsCoord(gpsCoord);
					iaskUploadStateVO.setMapCoord(mapCoord);
					iaskUploadStateVO.setTaskNum(taskNum);
					iaskUploadStateVO.setStatus(status);
					iaskUploadStateVO.setTaskCategory(taskCategory);
					taskUploadStateDao.create(iaskUploadStateVO);
				}
			}
			
			String url = AppContext.getInstance().getServerUrl();
			url += SpringUtil._REST_UPLOADPLANTASKSTATE;
			
			
			if(Constant.UPLOAD_STATUS_START.equals(status)&&!Constant.UPLOAD_STATUS_RECEIVE.equals(oldStatus)){
				if(isUpload!=null&&"true".equals(isUpload)){
					
				}else{
					return "3";
				}
			}else if(isUpload!=null&&"false".equals(isUpload)){
				return "3";
			}else if((Constant.UPLOAD_STATUS_WXQ_START.equals(oldStatus)&&Constant.UPLOAD_STATUS_WXQ_START.equals(status))
					||(Constant.UPLOAD_STATUS_WXZ_START.equals(oldStatus)&&Constant.UPLOAD_STATUS_WXZ_START.equals(status))
					||(Constant.UPLOAD_STATUS_WXH_START.equals(oldStatus)&&Constant.UPLOAD_STATUS_WXH_START.equals(status))){
				return "3";
			}
			if(Constant.UPLOAD_STATUS_WORK.equals(status)
					&&(Constant.UPLOAD_STATUS_RECEIVE.equals(oldStatus)
					||Constant.UPLOAD_STATUS_ARRIVE.equals(oldStatus))){
				ob.put("status", Constant.UPLOAD_STATUS_START);
				SpringUtil.postData(url, ob.toString());
				ob.put("status", status);
			}
			result=SpringUtil.postData(url, ob.toString());
			//{"title":"COMPLAINANT","status":1,"msg":"上传状态数据处理中"}
			JSONObject resultOb=new JSONObject(result);
			String code=resultOb.getString("status");
			return code;	
			
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		} 
		
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);

		if(progressDialog != null){
			progressDialog.dismiss();
		}
		if(isFinish){
			((Activity) context).finish();
		}
		if("3".equals(result)){
			return;
		}
		if("1".equals(result)){
			if(Constant.UPLOAD_STATUS_FINISH.equals(status)
					||Constant.UPLOAD_STATUS_RETREAT.equals(status)
					||Constant.UPLOAD_STATUS_IDENTICAL.equals(status)
					||Constant.UPLOAD_STATUS_ABANDONED.equals(status)
					||Constant.UPLOAD_STATUS_DELETE.equals(status)){
				try {
					delete();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//Toast.makeText(context, "提交状态成功", Toast.LENGTH_LONG).show();
		}/*else {
			Toast.makeText(context, "提交状态失败", Toast.LENGTH_LONG).show();
		}*/
		if(!Constant.PUSH_TITLE_MSG_COMMON.equals(tableName)){// 不是普通消息
			if(!Constant.UPLOAD_STATUS_WORK.equals(status)){
				Intent intent=new Intent();
				intent.setAction(Constant.TASK_LIST_UPDATE_ACTION);
				context.sendBroadcast(intent);
			}
		}
		
	}
	private void delete() throws SQLException{
		Dao<InsTablePushTaskVo, Long> tablePushTaskDao = AppContext.getInstance().getAppDbHelper().getDao(InsTablePushTaskVo.class);
		Map<String, Object>m=new HashMap<String, Object>();
		m.put("taskNum", taskNum);
		m.put("title", tableName);
		m.put("taskCategory", taskCategory);
		List<InsTablePushTaskVo> insTablePushTaskList= tablePushTaskDao.queryForFieldValuesArgs(m);
		tablePushTaskDao.delete(insTablePushTaskList);// 删除推送
		// 通知有主界面有新的任务(更新任务数量)
		Intent intent=new Intent();
		intent.setAction("com.main.menu.view");
		context.sendBroadcast(intent);
		Dao<InsTableSaveDataVo, Long> insTableSaveDataDao = AppContext.getInstance().getAppDbHelper().getDao(InsTableSaveDataVo.class);
		
		Dao<DynamicFormVO, Long> dynamicFormDao = AppContext.getInstance().getAppDbHelper().getDao(DynamicFormVO.class);
		List<DynamicFormVO> dynamicFormS = new ArrayList<DynamicFormVO>();
		if(insTablePushTaskList!=null&&insTablePushTaskList.size()>0){
			List<DynamicFormVO> dynamicFormVOList = dynamicFormDao.queryForEq("pid", insTablePushTaskList.get(0).getGuid());
			if(dynamicFormVOList!=null&&dynamicFormVOList.size()>0){
				dynamicFormS.addAll(dynamicFormVOList);
			}
		}
		if(Constant.COMPLAINANT.equals(tableName)
				||Constant.PROBLEM.equals(tableName)){// 删除问题，投诉
			taskUploadStateDao.delete(taskUploadStateVOList);
			Map<String, Object>m1=new HashMap<String, Object>();
			m1.put("mainNumber", taskNum);
			m1.put("taskCategory", taskCategory);
			m1.put("title", tableName);
			insTableSaveDataDao.delete(insTableSaveDataDao.queryForFieldValuesArgs(m1));
		}else if(Constant.INSMAPINACCURATE_FORM.equals(tableName)){
			Dao<InsDatumInaccurate, Long> insDatumInaccurateDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsDatumInaccurate.class);
			insDatumInaccurateDao.delete(insDatumInaccurateDao.queryForEq("serialNumber", taskNum));
		}else if(Constant.COMPLAINANT_FORM.equals(taskCategory)){
			Dao<WsComplainantFormVO, Long> wsComplainantFormVODao = AppContext
			.getInstance().getAppDbHelper()
			.getDao(WsComplainantFormVO.class);
			wsComplainantFormVODao.delete(wsComplainantFormVODao.queryForEq("workTaskNum", taskNum));
		}else if(Constant.RRWO_TASK.equals(tableName)){// 粤海维修单
			Dao<BsRushRepairWorkOrder, Long> bsRushRepairWorkOrderDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsRushRepairWorkOrder.class);
			bsRushRepairWorkOrderDao.delete(bsRushRepairWorkOrderDao.queryForEq("woNum", taskNum));
		}else if(Constant.ROUTINE_INS.equals(tableName)){// 粤海常规巡查
			Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsPnInsTask.class);
			bsPnInsTaskDao.delete(bsPnInsTaskDao.queryForEq("pnitNum", taskNum));
			Dao<BsSupervisionPoint, Long> bsSupervisionPointDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsSupervisionPoint.class);
			bsSupervisionPointDao.delete(bsSupervisionPointDao.queryForEq("workTaskNum", taskNum));
			Dao<BsRoutineInsArea, Long> bsRoutineInsAreaDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsRoutineInsArea.class);
			bsRoutineInsAreaDao.delete(bsRoutineInsAreaDao.queryForEq("workTaskNum", taskNum));
			
		}else if(Constant.SPECIAL_INS.equals(tableName)){// 专项巡查
			Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsPnInsTask.class);
			bsPnInsTaskDao.delete(bsPnInsTaskDao.queryForEq("pnitNum", taskNum));
			
			Dao<BsInsFacInfo, Long> bsInsFacInfoDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsInsFacInfo.class);
			bsInsFacInfoDao.delete(bsInsFacInfoDao.queryForEq("workTaskNum", taskNum));
			
		}else if(Constant.EMPHASIS_INS.equals(tableName)){// 重点区域巡查
			Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsPnInsTask.class);
			bsPnInsTaskDao.delete(bsPnInsTaskDao.queryForEq("pnitNum", taskNum));
			Dao<BsEmphasisInsArea, Long> bsEmphasisInsAreaDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsEmphasisInsArea.class);
			bsEmphasisInsAreaDao.delete(bsEmphasisInsAreaDao.queryForEq("workTaskNum", taskNum));
			
		}else if(Constant.LEAK_INS.equals(tableName)){// 查漏
			Dao<BsPnInsTask, Long> bsPnInsTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsPnInsTask.class);
			bsPnInsTaskDao.delete(bsPnInsTaskDao.queryForEq("pnitNum", taskNum));
			Dao<BsLeakInsArea, Long> bsLeakInsAreaDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(BsLeakInsArea.class);
			bsLeakInsAreaDao.delete(bsLeakInsAreaDao.queryForEq("workTaskNum", taskNum));
			
		}else if(Constant.PLAN_PAICHA_TEMPORARY.equals(tableName)){
			Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsPatrolAreaData.class);
			insPatrolAreaDataDao.delete(insPatrolAreaDataDao.queryForEq("workTaskNum", taskNum));
			Dao<InsCheckFacRoad, Long> insCheckFacRoadDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			insCheckFacRoadDao.delete(insCheckFacRoadDao.queryForEq("workTaskNum", taskNum));
			
		}else {// 删除巡检
			Dao<InsPlanTaskVO, Long> planTaskDao = AppContext.getInstance().getAppDbHelper().getDao(InsPlanTaskVO.class);
			planTaskDao.delete(planTaskDao.queryForEq("workTaskNum", taskNum));
			if(Constant.CYCLE_PLAN.equals(tableName)||Constant.INTERIM_PLAN.equals(tableName)
					||Constant.PLAN_HYDRANT_CYCLE.equals(tableName)||Constant.PLAN_HYDRANT_TEMPORARY.equals(tableName)
					||Constant.PLAN_VALVE_CYCLE.equals(tableName)||Constant.PLAN_VALVE_TEMPORARY.equals(tableName)
					||Constant.PLAN_FAC_CYCLE.equals(tableName)||Constant.PLAN_FAC_TEMPORARY.equals(tableName)){// 删除路面巡检数据
				Dao<InsPatrolDataVO, Long> insPatrolDataDao = AppContext.getInstance().getAppDbHelper().getDao(InsPatrolDataVO.class);
				List<InsPatrolDataVO> insPatrolDatas=insPatrolDataDao.queryForEq("workTaskNum", taskNum);
				for(InsPatrolDataVO insPatrolData:insPatrolDatas){
					List<DynamicFormVO> dynamicFormVOList = dynamicFormDao.queryForEq("pid", insPatrolData.getGuid());
					if(dynamicFormVOList!=null&&dynamicFormVOList.size()>0){
						dynamicFormS.addAll(dynamicFormVOList);
					}
				}
				insPatrolDataDao.delete(insPatrolDatas);
			}else if(Constant.XJGL_GJD_CYCLE.equals(tableName)||Constant.XJGL_GJD_TEMPORARY.equals(tableName)){// 删除关键点数据
				Dao<InsKeyPointPatrolData, Long> insKeyPointPatrolDataDao = AppContext.getInstance().getAppDbHelper().getDao(InsKeyPointPatrolData.class);
				List<InsKeyPointPatrolData> insKeyPointPatrolDatas=insKeyPointPatrolDataDao.queryForEq("workTaskNum", taskNum);
				insKeyPointPatrolDataDao.delete(insKeyPointPatrolDatas);
			}else if(Constant.PLAN_CONSTRUCTION_CYCLE.equals(tableName)||Constant.PLAN_CONSTRUCTION_TEMPORARY.equals(tableName)){// 删除工地数据
				Dao<InsSiteManage, Long> insSiteManageDao = AppContext.getInstance().getAppDbHelper().getDao(InsSiteManage.class);
				List<InsSiteManage> insSiteManages=insSiteManageDao.queryForEq("workTaskNum", taskNum);
				for(InsSiteManage insSiteManage:insSiteManages){
					List<DynamicFormVO> dynamicFormVOList = dynamicFormDao.queryForEq("pid", insSiteManage.getGuid());
					if(dynamicFormVOList!=null&&dynamicFormVOList.size()>0){
						dynamicFormS.addAll(dynamicFormVOList);
					}
				}
				insSiteManageDao.delete(insSiteManages);
			}else if(Constant.COOPERATE.equals(tableName)){// 配合工作
				Dao<CoordinateVO, Long> coordinateVODao = AppContext.getInstance().getAppDbHelper().getDao(CoordinateVO.class);
				coordinateVODao.delete(coordinateVODao.queryForEq("workTaskNum", taskNum));
				Intent intent0=new Intent();
				intent0.setAction(Constant.COORDINATE_ACTION);
				context.sendBroadcast(intent0);
			}
			deleteDynamicFrom(dynamicFormS, dynamicFormDao);
		}
	}
	private void deleteDynamicFrom(List<DynamicFormVO> dynamicFormVOList,Dao<DynamicFormVO, Long> dynamicFormDao){
		// 删除图片
		if(dynamicFormVOList!=null){
			for(DynamicFormVO dynamicFormVO : dynamicFormVOList){
				String c = dynamicFormVO.getContent();
				try {
					JSONObject json = new JSONObject(c);
					String cameras =  json.getString("camera");
					if(cameras!=null){
						String[] p= cameras.split(",");
						for(int i=0;i<p.length;++i){
							File f= new File(p[i]);
							f.delete();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			dynamicFormDao.delete(dynamicFormVOList);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 删除动态表
	}
}
