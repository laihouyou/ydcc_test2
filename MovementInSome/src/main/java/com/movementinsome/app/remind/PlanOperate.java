package com.movementinsome.app.remind;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.database.vo.BsEmphasisInsArea;
import com.movementinsome.database.vo.BsInsFacInfo;
import com.movementinsome.database.vo.BsLeakInsArea;
import com.movementinsome.database.vo.BsRoutineInsArea;
import com.movementinsome.database.vo.BsSupervisionPoint;
import com.movementinsome.database.vo.InsCheckFacRoad;
import com.movementinsome.database.vo.InsKeyPointPatrolData;
import com.movementinsome.database.vo.InsPatrolAreaData;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPatrolOnsiteRecordVO;
import com.movementinsome.database.vo.InsPlanTaskVO;
import com.movementinsome.database.vo.InsSiteManage;

import java.sql.SQLException;


public class PlanOperate {
	
	//private SpringUtil workUpload;// = new SpringUtil(Main.this);
	
	// List<InsPatrolDataVO> waitForDoData; // 本次等执行数据
	public static Dao<InsPatrolDataVO, Long> insPatrolDataDao;
	public static Dao<InsPlanTaskVO, Long> insPatrolTaskDao;
	public static Dao<InsSiteManage, Long> insSiteManageDao;
	public static Dao<InsKeyPointPatrolData, Long> insKeyPointPatrolDataDao;
	
	public static Dao<InsPatrolOnsiteRecordVO, Long> insPatrolOnsiteRecordDao;
	public static Dao<BsSupervisionPoint, Long> bsSupervisionPointDao;
	public static Dao<BsRoutineInsArea, Long> bsRoutineInsAreaDao;
	public static Dao<BsLeakInsArea, Long> bsLeakInsAreaDao;
	public static Dao<BsInsFacInfo, Long> bsInsFacInfoDao;
	public static Dao<BsEmphasisInsArea, Long> bsEmphasisInsAreaDao;
	
	public static Dao<InsPatrolAreaData, Long> insPatrolAreaDataDao;
	
	public static Dao<InsCheckFacRoad, Long> InsCheckFacRoadDao;
	
	static {
		try {
			insPatrolOnsiteRecordDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsPatrolOnsiteRecordVO.class);
			
			insPatrolDataDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsPatrolDataVO.class);
			insPatrolTaskDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsPlanTaskVO.class);
			insSiteManageDao= AppContext.getInstance().getAppDbHelper()
					.getDao(InsSiteManage.class);
			insKeyPointPatrolDataDao= AppContext.getInstance().getAppDbHelper()
			.getDao(InsKeyPointPatrolData.class);
			bsSupervisionPointDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsSupervisionPoint.class);
			bsRoutineInsAreaDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsRoutineInsArea.class);
			bsLeakInsAreaDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsLeakInsArea.class);
			bsInsFacInfoDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsInsFacInfo.class);
			bsEmphasisInsAreaDao = AppContext.getInstance().getAppDbHelper()
			.getDao(BsEmphasisInsArea.class);
			
			insPatrolAreaDataDao = AppContext.getInstance().getAppDbHelper()
					.getDao(InsPatrolAreaData.class);
			
			InsCheckFacRoadDao =  AppContext.getInstance().getAppDbHelper()
					.getDao(InsCheckFacRoad.class);
			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 监听
	 * 
	 * @author gordon
	 * 
	 */
	public interface DataSetSupervioer {
		public void onChange();
	}

	private DataSetSupervioer dataSetChangeListener;

	public void setDataSetChangeListener(DataSetSupervioer dataSetChangeListener) {
		this.dataSetChangeListener = dataSetChangeListener;
	}

	public void notifyDataSetChange() {
		if (null != dataSetChangeListener) {
			dataSetChangeListener.onChange();
		}
	}
	
/*	public SpringUtil getWorkUpload(Context context){
		if (workUpload == null){
			workUpload = new SpringUtil(context);
		}
		return workUpload;
	}
	
	public String getDymanicUrl(){
	   return AppContext.getInstance().getServerUrl()+SpringUtil._REST_DYMANICFROMUPLOAD;
	}
	*/
	public void dymanicFormUpload(String content){
		SpringUtil.dymanicFormUpload(AppContext.getInstance().getServerUrl(), content);
	}
	//叮当声音+震动
	public void showVibrator(Context context){
		MediaPlayer mediaPlayer=MediaPlayer.create(context.getApplicationContext(), R.raw.push_message_1);;	//启动手机铃声
		Vibrator vibrator= (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);;			//启动手机震动
		
		mediaPlayer.start();
		vibrator.vibrate(1500);
	}
}
