package com.movementinsome;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import android.app.Activity;

import com.movementinsome.app.mytask.handle.PublicHandle;
import com.movementinsome.app.server.UploadDataTask;
import com.movementinsome.database.vo.WorkTimeRecords;
import com.movementinsome.kernel.util.MyDateTools;
import com.j256.ormlite.dao.Dao;

public class WorkTimeHandler implements PublicHandle{
	private Activity activity;
	private String moiNum;
	private String workTime ;
	private int type;
	private WorkTimeRecords workTimeRecords;
	public WorkTimeHandler(Activity activity){
		this.activity = activity;
		workTimeRecords = new WorkTimeRecords();
	}
	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		try {
			Dao<WorkTimeRecords, Long> wsComplainantFormVODao = AppContext
			.getInstance().getAppDbHelper()
			.getDao(WorkTimeRecords.class);
			List<WorkTimeRecords> WorkTimeRecordsList = wsComplainantFormVODao.queryForAll();
			if(WorkTimeRecordsList!=null&&WorkTimeRecordsList.size()>0){
				workTimeRecords = WorkTimeRecordsList.get(0);
				workTimeRecords.setMoiNum(moiNum);
				workTimeRecords.setType(type);
				workTimeRecords.setWorkTime(workTime);
				wsComplainantFormVODao.update(workTimeRecords);
			}else{
				workTimeRecords.setId(UUID.randomUUID().toString());
				workTimeRecords.setMoiNum(moiNum);
				workTimeRecords.setType(type);
				workTimeRecords.setWorkTime(workTime);
				wsComplainantFormVODao.create(workTimeRecords);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// choice 0:上班    1:下班
	public void uploadData(int choice){
		JSONObject ob0 = new JSONObject();
		JSONObject ob=new JSONObject();
		type = choice;
		try {
			if(choice == 0){
				moiNum = UUID.randomUUID().toString();
			}else{
				moiNum = workTimeRecords.getMoiNum();
				if(moiNum==null){
					moiNum = UUID.randomUUID().toString();
				}
			}
			ob.put("moiNum", moiNum);
			ob.put("imei", AppContext.getInstance().getPhoneIMEI());
			//ob.put("guid", this.getGuid());
			
			ob.put("tableName","WORK_TIME_RECORDS");
			
			
			ob.put("usId", AppContext.getInstance().getCurUser().getUserId());
			ob.put("usUsercode", AppContext.getInstance().getCurUser().getUserName());
			ob.put("usNameZh", AppContext.getInstance().getCurUser().getUserAlias());
			
			ob.put("createDate", MyDateTools.date2String(new Date()));
			
			String gpsCoord=null;
			String mapCoord=null;
			if (AppContext.getInstance().getCurLocation()!=null){
				gpsCoord=AppContext.getInstance().getCurLocation().getCurGpsPosition();
				mapCoord=AppContext.getInstance().getCurLocation().getCurMapPosition();
			}
			ob.put("gpsCoord",gpsCoord);
			ob.put("mapCoord",mapCoord);
			workTime = MyDateTools.date2String(new Date());
			if(choice == 0){
				ob0.put("workStartTime",workTime);
			}else{
				ob0.put("workFinishTime", workTime);
			}
			ob.put("content",ob0.toString());
			
			new UploadDataTask(activity,this, ob.toString()).execute();
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
	
}
