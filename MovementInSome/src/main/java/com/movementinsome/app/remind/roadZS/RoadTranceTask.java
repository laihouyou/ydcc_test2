package com.movementinsome.app.remind.roadZS;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android.content.Context;

import com.movementinsome.AppContext;
import com.movementinsome.app.remind.nav.NavLocalGeoData;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.database.vo.InsPatrolDataVO;
import com.movementinsome.database.vo.InsPatrolOnsiteRecordVO;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.kernel.util.MyDateTools;
import com.google.gson.Gson;

public class RoadTranceTask extends TimerTask {

	// true时使用后台进程线程。只要剩下的程序记叙运行，后台进程线程就会执行。
	Timer myTimer;
	int delay, period;
	Date time;
	
	protected RoadPlanOperate planOper;
	protected Context context;
	// private List<InsPatrolDataVO> obj;
	protected String doId;

	//private NavLocalGeoData navLocalGeoData = null;

	protected NearObject noLuDuan = null;

	protected InsPatrolOnsiteRecordExtVO curTaskLuDuan,curTaskLuDuanTmp=null;

	boolean unFirstArrive = false; //首次未到达提示
	boolean paused = false;//是否需要暂停线程任务操作
	boolean upLocState = false;//更新路段状态时的锁标志,为true表示还在处理当中，继续行者等
	
	
	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public RoadTranceTask(RoadPlanOperate planOper, Context context, String doId) {
		this.planOper = planOper;
		this.context = context;
		this.doId = doId;// UUID.randomUUID().toString();
		// this.obj = obj;
	}

	public void start(int delay) {
		this.delay = delay;
		myTimer = new Timer();
		myTimer.schedule(this, delay);
	}

	public void start(int delay, int period) {
		this.delay = delay;
		this.period = period;
		myTimer = new Timer();
		myTimer.schedule(this, delay, period); // 利用timer.schedule方法

		// public void schedule(TimerTask task,long time,long period)
		// task被安排在延迟time后执行，执行后将每隔period(毫秒)反复执行。由于规定的时间间隔并不能保证与时钟精准的同不步，所以该方
	}

	public void start(Date time, int period) {
		this.delay = delay;
		this.period = period;
		myTimer = new Timer();
		myTimer.schedule(this, time, period); // 利用timer.schedule方法

		// public void schedule(TimerTask task,Date time,long period)
		// task被安排在time指定的时间执行，执行后将每隔period(毫秒)反复执行。由于规定的时间间隔并不能保证与时钟精准的同不步，所以该方
	}

	public void run() {
		if (paused){
			return;
		}
		synchronized(planOper.getWaitForDoDataCache(doId)){
			// 执行任务(
			LocationInfoExt location = AppContext.getInstance().getCurLocation();
			if ( location != null) {
				/*if (navLocalGeoData == null) {
					navLocalGeoData = new NavLocalGeoData();
				}*/
				noLuDuan = NavLocalGeoData.findNearLuDuan(location//AppContext.getInstance().getCurLocation()//
						, planOper.getWaitForDoDataCache(doId));
	
				setLuDuanState(noLuDuan, false);
			}
		}
	}

	public void end() {
		// 终止Timer的功能执行，但不会对正在执行的任务有影响。当执行cancel方法后将不能再用其分配任务。
		paused = true; 
		myTimer.cancel();
		myTimer.purge();		
		//if (noLuDuan != null) {
		//setLuDuanState(noLuDuan, true);		
	}

	/**
	 * 设置当前路段状态
	 * 
	 * @param luDuan
	 *            找到的路段名称
	 * @param dateTime
	 *            找到时间
	 */
	public void setLuDuanState(NearObject nearObj, boolean end) {
		/*try{
			paused = true;
			//初始启动判断,用于首次提示“未进入巡查区域”
			if ((curTaskLuDuan == null)&&(null == nearObj || null == nearObj.getObjName())) {
				if (!unFirstArrive){
					unFirstArrive = true;
					planOper.setCurTaskLuDuan(null);
				}else if (planOper.getWaitForDoDataCache(doId).size()==0){//本次无待巡记录
					curTaskLuDuan = new InsPatrolOnsiteRecordExtVO(); 
					curTaskLuDuan.setWarnState(4);
					planOper.setCurTaskLuDuan(curTaskLuDuan);
					planOper.rest();  //重新复位，防止连续多次巡查
					unFirstArrive = false;
				}
				return;
			}
			if((curTaskLuDuan != null)&&(curTaskLuDuan.getId()==null)&&!unFirstArrive){
				return;
			}
			//还在执行
			if (!end){
				//第1路段判断
				if (curTaskLuDuan == null) {
					curTaskLuDuan = new InsPatrolOnsiteRecordExtVO();
	
					curTaskLuDuan.setId(nearObj.getObjId());
					
					// 路段编号
					curTaskLuDuan.setRoadNum(nearObj.getObjNum());
					// 路名
					curTaskLuDuan.setRoadName(nearObj.getObjName());
					
					// 上一个路段编号
					curTaskLuDuan.setPreRoadNum("");
					// 上一个路段名称
					curTaskLuDuan.setPreRoadName("");
					// 上一个路段巡查开始时间
					curTaskLuDuan.setPrePsDate("");
					// 上一个路段开始巡查坐标
					curTaskLuDuan.setPrePsCoordinate("");
					// 上一个路段巡查结束时间
					curTaskLuDuan.setPrePeDate("");
					// 上一个路段结束巡查坐标
					curTaskLuDuan.setPrePeCoordinate("");

					curTaskLuDuan.setEnterState(0);
					
					curTaskLuDuan.setWktGeom(nearObj.getWktGeom());
				}
				curTaskLuDuan.setCurNearObj(nearObj);
				//是还还在目标上
				if ((null != nearObj.getObjId())&&(curTaskLuDuan.getId().equals(nearObj.getObjId()))){
					// 巡查结束时间
					curTaskLuDuan.setPatrolEndDate("");
					// 结束巡查坐标
					curTaskLuDuan.setPatrolEndCoordinate("");
	
					curTaskLuDuan.setMinDistance(nearObj.getMinDistance());
					curTaskLuDuan.setWktGeom(nearObj.getWktGeom());
					if ("pause".equals(nearObj.getState())){
						curTaskLuDuan.setWarnState(3); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
						planOper.setCurTaskLuDuan(curTaskLuDuan);
					}else if (curTaskLuDuan.getWarnState() != 3){
						// 提交离开路段状态到服务器
						if (curTaskLuDuan.getEnterState()==0 || curTaskLuDuan.getEnterState()==3  ){  //未进入该路段
		
							if (nearObj.isCanPost()){//是还达到进入距离,如果是提交进入状态并提示
								// 巡查开始时间
								curTaskLuDuan.setPatrolStartDate(nearObj.getDateTime());
								// 开始巡查坐标
								curTaskLuDuan.setPatrolStartCoordinate(nearObj.getMapXY());
								curTaskLuDuan.setEnterState(1);//设置进入路段状态
								curTaskLuDuan.setWarnState(1); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
								planOper.setCurTaskLuDuan(curTaskLuDuan);
							}
							
						}else if (curTaskLuDuan.getEnterState()==1){  //进入该路段
							if (nearObj.isOutWarn()){
								curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
								// 结束巡查坐标
								curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
								curTaskLuDuan.setWarnState(2); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
								updateObjState(nearObj,3); //偏离警告
								planOper.setCurTaskLuDuan(curTaskLuDuan);
							}else if (nearObj.isDeviate()){
								if (null == nearObj.getState()){  //如果该路段没有被暂停执行
									// 上一个路段编号
									curTaskLuDuan.setPreRoadNum("");
									// 上一个路段名称
									curTaskLuDuan.setPreRoadName("");
									// 上一个路段巡查开始时间
									curTaskLuDuan.setPrePsDate("");
									// 上一个路段开始巡查坐标
									curTaskLuDuan.setPrePsCoordinate("");
									// 上一个路段巡查结束时间
									curTaskLuDuan.setPrePeDate("");
									// 上一个路段结束巡查坐标
									curTaskLuDuan.setPrePeCoordinate("");
									
									curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
									// 结束巡查坐标
									curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
									
									curTaskLuDuan.setEnterState(2);
									curTaskLuDuan.setWarnState(1); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
									
									updateObjState(nearObj,1);//离开记录
									planOper.setCurTaskLuDuan(curTaskLuDuan);		
								}
							}else if (curTaskLuDuan.getWarnState() == 2){//原来偏离现在重新进入该路段
								// 上一个路段编号
								curTaskLuDuan.setPreRoadNum("");
								// 上一个路段名称
								curTaskLuDuan.setPreRoadName("");
								// 上一个路段巡查开始时间
								curTaskLuDuan.setPrePsDate("");
								// 上一个路段开始巡查坐标
								curTaskLuDuan.setPrePsCoordinate("");
								// 上一个路段巡查结束时间
								curTaskLuDuan.setPrePeDate("");
								// 上一个路段结束巡查坐标
								curTaskLuDuan.setPrePeCoordinate("");
								curTaskLuDuan.setPatrolStartDate(nearObj.getDateTime());
								// 开始巡查坐标
								curTaskLuDuan.setPatrolStartCoordinate(nearObj.getMapXY());
								curTaskLuDuan.setWarnState(1);
								planOper.setCurTaskLuDuan(curTaskLuDuan);  
							} 
						}
					}
				}else{//传入路段已经变化
					if (null == nearObj.getObjName()){//有可能信号丢失或是偏离缓冲范围,告警提示; 有可以偏离正式巡检路段，也有可能要出该路段并后续路段闹中间隔有点远
						if (curTaskLuDuan.getWarnState() != 3){
							if (null != planOper.getWaitForDoDataCache(doId) && planOper.getWaitForDoDataCache(doId).size()>0){  //判断是否已经没有后续巡查的路段,些段处理在逻辑上显得有点古怪，但目前未有好的方法 

								if (curTaskLuDuan.getEnterState() == 1)  //如果进入过该路段巡查，则需要记录偏离的时间及位置信息
								{
									if (nearObj.isOutWarn2(curTaskLuDuan.getPatrolStartCoordinate())){
										// 上一个路段编号
										curTaskLuDuan.setPreRoadNum("");
										// 上一个路段名称
										curTaskLuDuan.setPreRoadName("");
										// 上一个路段巡查开始时间
										curTaskLuDuan.setPrePsDate("");
										// 上一个路段开始巡查坐标
										curTaskLuDuan.setPrePsCoordinate("");
										// 上一个路段巡查结束时间
										curTaskLuDuan.setPrePeDate("");
										// 上一个路段结束巡查坐标
										curTaskLuDuan.setPrePeCoordinate("");
										
										curTaskLuDuan.setWarnState(2); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
										//updateObjState(nearObj,2);
										planOper.setCurTaskLuDuan(curTaskLuDuan);
									}else if (nearObj.isDeviate2(curTaskLuDuan.getPatrolStartCoordinate())){
										
										curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
										// 结束巡查坐标
										curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
										if (planOper.getWaitForDoDataCache(doId).size() == 1){//只有一条路而且是当前路段，则判断为正常离开
											if (planOper.getWaitForDoDataCache(doId).get(0).getGuid().equals(curTaskLuDuan.getId())){
												curTaskLuDuan.setWarnState(1); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
											}
										}else{
											curTaskLuDuan.setWarnState(2); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
										}
										
										//if (curTaskLuDuan.getWarnState() != 3){
											curTaskLuDuan.setEnterState(2); //离开
											updateObjState(nearObj,1);
											planOper.setCurTaskLuDuan(curTaskLuDuan);
											
											curTaskLuDuan = null;
										//}
										planOper.setCurTaskLuDuan(curTaskLuDuan);
									}
								}else if (curTaskLuDuan.getEnterState() == 0){  //有可能是经过巡查路段附近，由于缓冲范围设置太大,寻找到了该路段。但巡查人员并不想巡查该路段{
									if (curTaskLuDuan.getWarnState() == 0){
										curTaskLuDuan.setWarnState(1); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
									}
									curTaskLuDuan.setEnterState(3); //未进入该路段
									planOper.setCurTaskLuDuan(curTaskLuDuan);
									//curTaskLuDuan = null;
								}
								
							}else{//已经没有后续巡查路段则当成离开本次巡查路段
								// 巡查结束时间
								curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
								// 结束巡查坐标
								curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
								curTaskLuDuan.setWarnState(4); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
								//制造nearObj,用于提交对象使用
								nearObj.setObjId(curTaskLuDuan.getId());
								nearObj.setObjName(curTaskLuDuan.getRoadName());
								nearObj.setObjNum(curTaskLuDuan.getRoadNum());
								// 更新新的路段状态
								if ((curTaskLuDuan.getEnterState() == 1)&&(curTaskLuDuan.getWarnState() != 3)){
									curTaskLuDuan.setEnterState(2); //离开该路段

									updateObjState(nearObj,1);
									planOper.setCurTaskLuDuan(curTaskLuDuan);
									//planOper.showVibrator(context);
								}
								curTaskLuDuan = null;
								planOper.setCurTaskLuDuan(curTaskLuDuan);
								//重置巡查计划初始条件,用于判断是否可以连续重复巡查
								planOper.rest();
							}
						}else{
							curTaskLuDuan.setEnterState(2); //离开该路段
							planOper.setCurTaskLuDuan(curTaskLuDuan);
						}
					}else{ //如果只是路段ID不一致，则应该是进入另一路段
						// 处理原路段
						if (curTaskLuDuan.getEnterState() == 1){ //如果变化前路段已经进入过
							// 巡查结束时间
							curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
							// 结束巡查坐标
							curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
							
							curTaskLuDuan.setEnterState(2); //离开该路段

							if (curTaskLuDuan.getWarnState() != 3){
								updateObjState(nearObj,1);
							}	
						}
						
						//下一查询到路段信息初始化
						curTaskLuDuanTmp = copy(curTaskLuDuan);
						//if (curTaskLuDuan.getEnterState() != 0){ //如果有进入过上一路段
						if (curTaskLuDuan.getWarnState() != 3){
							// 上一个路段编号
							curTaskLuDuanTmp.setPreRoadNum(curTaskLuDuan.getRoadNum());
							// 上一个路段名称
							curTaskLuDuanTmp.setPreRoadName(curTaskLuDuan.getRoadName());
							// 上一个路段巡查开始时间
							curTaskLuDuanTmp.setPrePsDate(curTaskLuDuan.getPatrolStartDate());
							// 上一个路段开始巡查坐标
							curTaskLuDuanTmp.setPrePsCoordinate(curTaskLuDuan
									.getPatrolStartCoordinate());
							// 上一个路段巡查结束时间
							curTaskLuDuanTmp.setPrePeDate(curTaskLuDuan.getPatrolEndDate());
							// 上一个路段结束巡查坐标
							curTaskLuDuanTmp.setPrePeCoordinate(curTaskLuDuan
									.getPatrolEndCoordinate());
						}else{
							// 上一个路段编号
							curTaskLuDuanTmp.setPreRoadNum("");
							// 上一个路段名称
							curTaskLuDuanTmp.setPreRoadName("");
							// 上一个路段巡查开始时间
							curTaskLuDuanTmp.setPrePsDate("");
							// 上一个路段开始巡查坐标
							curTaskLuDuanTmp.setPrePsCoordinate("");
							// 上一个路段巡查结束时间
							curTaskLuDuanTmp.setPrePeDate("");
							// 上一个路段结束巡查坐标
							curTaskLuDuanTmp.setPrePeCoordinate("");
						}
						//}					
						//当前路段
						curTaskLuDuanTmp.setId(nearObj.getObjId());
						// 路段编号
						curTaskLuDuanTmp.setRoadNum(nearObj.getObjNum());
						// 路名
						curTaskLuDuanTmp.setRoadName(nearObj.getObjName());
						// 巡查开始时间
						curTaskLuDuanTmp.setPatrolStartDate("");
						// 开始巡查坐标
						curTaskLuDuanTmp.setPatrolStartCoordinate("");
						// 巡查结束时间
						curTaskLuDuanTmp.setPatrolEndDate("");
						// 结束巡查坐标
						curTaskLuDuanTmp.setPatrolEndCoordinate("");
						curTaskLuDuanTmp.setMinDistance(nearObj.getMinDistance());
	
						curTaskLuDuanTmp.setWktGeom(nearObj.getWktGeom());
						
						curTaskLuDuanTmp.setEnterState(0);  //重置新路段的进入状态
						
						curTaskLuDuan = copy(curTaskLuDuanTmp);
						curTaskLuDuanTmp = null;
						
						//if (null == preNearObj){
						//当前路段处理
						if (null == nearObj.getState()){
							curTaskLuDuan.setWarnState(1); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
							if (nearObj.isCanPost()){//是还达到进入距离,如果是提交进入状态并提示
								// 巡查开始时间
								curTaskLuDuan.setPatrolStartDate(nearObj.getDateTime());
								// 开始巡查坐标
								curTaskLuDuan.setPatrolStartCoordinate(nearObj.getMapXY());
								curTaskLuDuan.setEnterState(1);//设置进入路段状态
							}
						}else if (nearObj.getState().equals("pause")){  //如果该路段没有被暂停执行
							curTaskLuDuan.setWarnState(3); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
						}else{
							curTaskLuDuanTmp.setWarnState(1); //路段提示状态,1:正常  2:偏离 ,3:暂停,4:没有检查到计划路段
						}
												
						planOper.setCurTaskLuDuan(curTaskLuDuan);
						//}
						
					}
				}
	
			}
		}finally{
			planOper.setCurTaskLuDuan(curTaskLuDuan);
			paused = false;
		}*/
	}

	
	private InsPatrolOnsiteRecordVO copy(InsPatrolOnsiteRecordVO source){
		Gson gson = new Gson();
		String json = gson.toJson(source);
		return gson.fromJson(json, InsPatrolOnsiteRecordVO.class);
	}
	
	/**
	 * 设置最近签到点
	 * 
	 * @param luDuan
	 *            找到的路段名称
	 * @param dateTime
	 *            找到时间
	 */
	/*
	 * public void setChkPntState(NearObject nearObj){
	 * planOper.setCurTaskChkPnt(nearObj); }
	 */
	

	// 更新巡查记录
	//NearObject nearObj 路段对象
	protected void updateObjState(InsPatrolOnsiteRecordExtVO nearObj) {
		/*if (!nearObj.isCanPost()){
			return;
		}*/
		
		while (upLocState){
			;
		}
		try{
			upLocState = true;
			InsPatrolDataVO patrolDataRemove = null;
			List list =planOper.getWaitForDoDataCache(doId);
	
			//for (InsPatrolDataVO patrolData : planOper.getWaitForDoDataCache(doId)) {
			Iterator<InsPatrolDataVO> iterator = list.iterator();
	        while(iterator.hasNext()){
	        	
	        	InsPatrolDataVO patrolData = iterator.next();
				if (patrolData.getGuid().equals(nearObj.getId())) {
					patrolDataRemove = patrolData;
					int state = nearObj.getEnterState(); //路段状态标识 1.进入巡查,2.暂停,3：完成,4:偏离,5:偏离警告,6.续巡,7.结束(由系统对传入为1的进行计算判断),8。终止  
					// 更新路段记录巡检时间
					try {
						//判断巡查周期及巡查次数需不需要初始化
						if(patrolData.getLastInsDateStr()!=null&&nearObj.getPatrolStartDate()!=null){
							if (!patrolData.getLastInsDateStr().subSequence(0, 10).equals(nearObj.getPatrolStartDate().subSequence(0, 10))){
								if (patrolData.getInsCount() == null) {
									patrolData.setInsCount(Long.valueOf(0));
								} else if (patrolData.getInsCount() == Long.valueOf(-1)) {
									patrolData.setInsCount(Long.valueOf(0));
								} else if (planOper.checkIsThisFreqEnd(patrolData) == 2) {
									patrolData.setInsCount(Long.valueOf(0));
								}
							}
						}
						patrolData.setLastInsDateStr(nearObj.getPatrolStartDate());
						if (state ==3){//结束巡查要计算次数
							patrolData.setState(null);
							if (patrolData.getInsCount() == null) {
								patrolData.setInsCount(Long.valueOf(1));
							} else if (patrolData.getInsCount() == Long.valueOf(-1)) {
								patrolData.setInsCount(Long.valueOf(1));
							} else {
								if (planOper.checkIsThisFreqEnd(patrolData) == 2) {
									patrolData.setInsCount(Long.valueOf(1));
								} else {
									patrolData
											.setInsCount(patrolData.getInsCount() + 1);
								}
							}
							patrolData.delRoadPreTrace();
						}else{//如果暂停巡检需要补充暂停时间和坐标
							if (state ==2 ){
								patrolData.setState("pause");
								/*patrolData.setLastInsDateStr(MyDateTools
										.date2String(new Date()) );*/
								patrolData.saveRoadPreTrace(nearObj.getHadRunRoadShapeStr());
							}else if (state ==4 || state ==5){
								/*patrolData.setLastInsDateStr(MyDateTools
										.date2String(new Date()) );*/
								patrolData.saveRoadPreTrace(nearObj.getHadRunRoadShapeStr());
							}else if (state ==6){
								patrolData.setState(null);
								//nearObj.setRunTrace(patrolData.getRoadPreTrace(patrolData.getGuid()));
							}else if (state==8){
								patrolData.delRoadPreTrace();
							}
						}
						planOper.insPatrolDataDao.update(patrolData); // 理新路段的巡查时间和巡查次数
	
						
						InsPatrolOnsiteRecordVO curTaskLuDuanStore = new InsPatrolOnsiteRecordVO();
						//curTaskLuDuanStore = copy(curTaskLuDuan);			
						
						// 保存路面巡查记录到本地数据库
						curTaskLuDuanStore.setId(UUID.randomUUID().toString());
						curTaskLuDuanStore.setWorkTaskNum(patrolData.getWorkTaskNum());
						curTaskLuDuanStore.setWpdId(patrolData.getWpdId());
						
						curTaskLuDuanStore.setRoadNum(nearObj.getRoadNum());
						curTaskLuDuanStore.setRoadName(nearObj.getRoadName());
						curTaskLuDuanStore.setPatrolEndCoordinate(nearObj.getPatrolEndCoordinate());
						curTaskLuDuanStore.setPatrolEndDate(nearObj.getPatrolEndDate());
						curTaskLuDuanStore.setPatrolStartCoordinate(nearObj.getPatrolStartCoordinate());
						curTaskLuDuanStore.setPatrolStartDate(nearObj.getPatrolStartDate());						
						
						
						curTaskLuDuanStore.setReportedCoordinate(AppContext.getInstance().getCurLocation().getCurMapPosition());
						curTaskLuDuanStore.setGuid(UUID.randomUUID().toString());
						curTaskLuDuanStore.setPhoneImei(AppContext.getInstance()
								.getPhoneIMEI());
						curTaskLuDuanStore.setPatrolPeople(AppContext.getInstance()
								.getCurUser().getUserAlias());
						curTaskLuDuanStore.setSerialNumber(patrolData.getSerialNumber());
						curTaskLuDuanStore.setMoiNum(nearObj.getMoiNum());
						
	
						curTaskLuDuanStore.setCreateDate(MyDateTools
								.date2String(new Date()));
						curTaskLuDuanStore.setCreateUId(Long.valueOf(AppContext
								.getInstance().getCurUser().getUserId()));
						curTaskLuDuanStore.setCreateUNum(AppContext.getInstance()
								.getCurUser().getUserName());
						curTaskLuDuanStore.setCreateUName(AppContext.getInstance()
								.getCurUser().getUserAlias());
						curTaskLuDuanStore.setTaskCategory(patrolData.getTaskCategory());
						curTaskLuDuanStore.setInsCount(patrolData.getInsCount());
						
	
						planOper.insPatrolOnsiteRecordDao.create(curTaskLuDuanStore);
	
						
						
						//state :1.进入巡查,2.暂停,3：完成,4:偏离,5:偏离警告,6.续巡,7.结束(由系统对传入为1的进行计算判断),8。终止  
						// 判断路段巡查过滤条件是否需要复位
						if (state ==3){//结束巡查要计算次数
							// 提交路面巡查记录到服务器
							if (planOper.checkIsThisFreqEnd(patrolData) == -1) {
								planOper.dymanicFormUpload(curTaskLuDuanStore.toJson(Long.valueOf(7))); //完成本次巡查:7
								patrolData.setInsCount(Long.valueOf(-1));
								planOper.insPatrolDataDao.update(patrolData);
							}else{
								planOper.dymanicFormUpload(curTaskLuDuanStore.toJson(Long.valueOf(3))); //完成当次巡查
							}
							
							if (patrolDataRemove != null) {
								// lstPatrolData.remove(patrolDataRemove);
								planOper.addHadDoDataCache(doId, patrolDataRemove);
								planOper.removeWaitForDoDataCache(doId, patrolDataRemove);
							}
						}else if (state ==8){
							planOper.dymanicFormUpload(curTaskLuDuanStore.toJson(Long.valueOf(8))); //终止
							if (patrolDataRemove != null) {
								// lstPatrolData.remove(patrolDataRemove);
								planOper.addHadDoDataCache(doId, patrolDataRemove);
								planOper.removeWaitForDoDataCache(doId, patrolDataRemove);
							}
						}else{
							planOper.dymanicFormUpload(curTaskLuDuanStore.toJson(Long.valueOf(state)));  
							planOper.replaceWaitForDoDataCache(doId, patrolData);
						}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
			
		}finally{
			upLocState = false;
		}
	}

	private void removeWaitForDoDataCache(String id){
		for (InsPatrolDataVO patrolData : planOper.getWaitForDoDataCache(doId)) {
			if (patrolData.getGuid().equals(id)) {
				planOper.removeWaitForDoDataCache(doId, patrolData);
				return;
			}
		}

	}
	
	private void replaceWaitForDoDataCache(String id){
		for (InsPatrolDataVO patrolData : planOper.getWaitForDoDataCache(doId)) {
			if (patrolData.getGuid().equals(id)) {
				planOper.replaceWaitForDoDataCache(doId, patrolData);
				return;
			}
		}

	}

/*	public void pause(InsPatrolOnsiteRecordExtVO pauseRoad){
		try{
			paused = true;
		
			if (null != pauseRoad){
				// 提交路面巡查记录到服务器
				if (curTaskLuDuan.getId().equals(pauseRoad.getId())){
					curTaskLuDuan.setEnterState(2);
					curTaskLuDuan.setPatrolEndCoordinate(noLuDuan.getMapXY());
					curTaskLuDuan.setPatrolEndDate(noLuDuan.getDateTime());
				}
				
				NearObject tmpLuDuan = new NearObject();
				tmpLuDuan.setObjId(pauseRoad.getId());
				tmpLuDuan.setObjName(pauseRoad.getRoadName());
				tmpLuDuan.setObjNum(pauseRoad.getRoadNum());
				tmpLuDuan.setDateTime(noLuDuan.getDateTime());
				tmpLuDuan.setMapXY(noLuDuan.getMapXY());
				
				updateObjState(tmpLuDuan,2);
				planOper.setCurTaskLuDuan(curTaskLuDuan);
				//planOper.dymanicFormUpload(curTaskLuDuan.toJson((long) 2));
			}
		}finally{
			paused = false;
		}
	}
	public void goNo(InsPatrolOnsiteRecordExtVO pauseRoad){
		try{
			paused = true;
			
			if (null != pauseRoad){
				// 提交路面巡查记录到服务器
				if (curTaskLuDuan.getId().equals(pauseRoad.getId())){
					curTaskLuDuan.setWarnState(1);
				}
				for (InsPatrolDataVO patrolData : planOper.getWaitForDoDataCache(doId)) {
					try {
						if (patrolData.getGuid().equals(pauseRoad.getId())) {
							patrolData.setState(null);//取沙本地暂停标志
							planOper.insPatrolDataDao.update(patrolData);
							
								// 提交路面巡查记录到服务器
								if (curTaskLuDuan.getId().equals(pauseRoad.getId())){
									curTaskLuDuan.setEnterState(6);
									curTaskLuDuan.setPatrolEndCoordinate(noLuDuan.getMapXY());
									curTaskLuDuan.setPatrolEndDate(noLuDuan.getDateTime());
								}
								
								NearObject tmpLuDuan = new NearObject();
								tmpLuDuan.setObjId(pauseRoad.getId());
								tmpLuDuan.setObjName(pauseRoad.getRoadName());
								tmpLuDuan.setObjNum(pauseRoad.getRoadNum());
								tmpLuDuan.setDateTime(noLuDuan.getDateTime());
								tmpLuDuan.setMapXY(noLuDuan.getMapXY());
								
								updateObjState(tmpLuDuan,6);
								replaceWaitForDoDataCache(patrolData.getGuid());
								planOper.setCurTaskLuDuan(curTaskLuDuan);
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return;
				}		   
			}
		}finally{
			paused = false;
		}

	}*/
}
