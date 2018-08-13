package com.movementinsome.app.mytask.handle;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.movementinsome.AppContext;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.j256.ormlite.dao.Dao;

import android.content.Context;
import android.content.Intent;

public class TaskHandle {

	private Timer timer;// 定时器
	private Dao<InsPlanTaskVO, Long> planTaskDao = null;
	private Dao<InsTablePushTaskVo, Long> insTablePushTaskVoDao = null;
	private Context context;

	public TaskHandle(Context context){
		this.context = context;
	}
	public void timingUpdatePlanList() {
		timer = new Timer();
		try {
			planTaskDao = AppContext.getInstance()
			.getAppDbHelper().getDao(InsPlanTaskVO.class);
			insTablePushTaskVoDao= AppContext.getInstance()
			.getAppDbHelper().getDao(InsTablePushTaskVo.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(planTaskDao!=null&&insTablePushTaskVoDao!=null){
					try {
						List<InsPlanTaskVO> insPlanTaskVOList = planTaskDao.queryForAll();
						if(insPlanTaskVOList!=null){
							for(int i=0;i<insPlanTaskVOList.size();++i){
								Date workTaskPEDate = stringToDate(insPlanTaskVOList.get(i)
										.getWorkTaskPEDate());
								if (null!=workTaskPEDate && new Date().getTime() > (workTaskPEDate.getTime() + 24 * 60 * 60 * 1000)) {
									Map<String, Object> mp0 = new HashMap<String, Object>();
									mp0.put("taskNum", insPlanTaskVOList.get(i).getWorkTaskNum());
									mp0.put("title", insPlanTaskVOList.get(i).getWorkTaskType());
									List<InsTablePushTaskVo> insTablePushTaskVoList= insTablePushTaskVoDao.queryForFieldValuesArgs(mp0);
									if(insTablePushTaskVoList!=null&&insTablePushTaskVoList.size()>0){
										InsTablePushTaskVo insTablePushTaskVo = insTablePushTaskVoList.get(0);
										// 超期设置
										insTablePushTaskVo.setTbType("3");
										insTablePushTaskVoDao.update(insTablePushTaskVo);
										Intent intent=new Intent();
										intent.setAction(com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION);
										context.sendBroadcast(intent);
									}
								}else if(null!=workTaskPEDate && new Date().getTime() > workTaskPEDate.getTime()- 24 * 60 * 60 * 1000){
									Map<String, Object> mp0 = new HashMap<String, Object>();
									mp0.put("taskNum", insPlanTaskVOList.get(i).getWorkTaskNum());
									mp0.put("title", insPlanTaskVOList.get(i).getWorkTaskType());
									List<InsTablePushTaskVo> insTablePushTaskVoList= insTablePushTaskVoDao.queryForFieldValuesArgs(mp0);
									if(insTablePushTaskVoList!=null&&insTablePushTaskVoList.size()>0){
										InsTablePushTaskVo insTablePushTaskVo = insTablePushTaskVoList.get(0);
										// 超期设置
										insTablePushTaskVo.setTbType("4");
										insTablePushTaskVoDao.update(insTablePushTaskVo);
										Intent intent=new Intent();
										intent.setAction(com.movementinsome.app.pub.Constant.TASK_LIST_UPDATE_ACTION);
										context.sendBroadcast(intent);
									}
								}
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}, 0, 3600000);
	}

	public void shopTimer() {
		if (timer != null) {
			timer.cancel();
		}
	}

	private Date stringToDate(String dateStr) {
		if (null != dateStr){
			SimpleDateFormat formatDate = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			try {
				Date date = formatDate.parse(dateStr);
				return date;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
