package com.movementinsome.app.remind.roadZS;

import android.content.Context;

import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.movementinsome.AppContext;
import com.movementinsome.app.dataop.RoadDOType;
import com.movementinsome.app.remind.nav.NavLocalGeoData;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.kernel.location.LocationInfoExt;


/**
 * 路段巡查的整个过程的主要参与者是“路段”
 * “路段”在巡查过程中的控制状态：待巡、在巡、偏离、暂信/继续、完成、终止
 * 巡查任务过程管理的主要判断
 * 1、当前所有路段
 * 2、能否进入下一路段
 * 3、响应用户手动操作
 * 4、是否完成本次路段
 * 
 * enterstate值表示
 * 1.进入巡查,2.暂停,3：完成,4:偏离,5:偏离警告,6.续巡,7.结束(由系统对传入为1的进行计算判断),8。终止    
 * 
 * 事件通知巡查过程进展情况，状态定义如下：
 *  00,"还未进入到巡查区域";
 *  01,"无须巡查”；
 *  02,“已完成”
 *  03，“在非计划路段上”
 *  10:自动模式
 *  11:手动模式
 *  12:偏离
 *  13:暂停
 *  14:完成
 *  15:终止
 * 2、curTaskLuDuan == null
 */

public class RoadTranceTaskExp extends RoadTranceTask {

	//路段处理模式，系统自动处理进入、嫁出；手动处理进入、退出
	private RoadDOType roadDoType;
	
	private boolean hasEnter = false;//已经进入过巡检区域
	
	private Point runPrePoint = null;
	private Polyline runTrace = null; //路段巡检轨迹
	
	private double _TORLENCE = 0.15d;
	
	//private InsPatrolOnsiteRecordExtVO nowDoRoad;//巡查人员当前正在处理路段
	
	public RoadTranceTaskExp(RoadPlanOperate planOper, Context context,
			String doId) {
		super(planOper, context, doId);
		this.roadDoType = RoadDOType.auto;
		// TODO Auto-generated constructor stub
	}

	public RoadTranceTaskExp(RoadPlanOperate planOper, Context context,
			String doId,RoadDOType doType) {
		super(planOper, context, doId);
		this.roadDoType = doType;
		// TODO Auto-generated constructor stub
	}
	
	//当前处理路段
	public InsPatrolOnsiteRecordExtVO getCurTaskLuDuan() {
		return super.curTaskLuDuan;
	}

	//指定当前处理路段
	public void setCurTaskLuDuan(InsPatrolOnsiteRecordExtVO curTaskLuDuan) {
		super.curTaskLuDuan = curTaskLuDuan;
	}

	//通过GPS判断到的路段信息	
	public NearObject getNoLuDuan() {
		return super.noLuDuan;
	}
	
	public RoadDOType getRoadDoType() {
		return roadDoType;
	}

	public void setRoadDoType(RoadDOType roadDoType) {
		this.roadDoType = roadDoType;
	}
	
/*	public InsPatrolOnsiteRecordExtVO getNowDoRoad() {
		return nowDoRoad;
	}

	public void setNowDoRoad(InsPatrolOnsiteRecordExtVO nowDoRoad) {
		this.nowDoRoad = nowDoRoad;
	}*/

	@Override
	public void run() {
		if (this.roadDoType == RoadDOType.auto){ //自动判断模式
			super.run();
		}else{
			if (paused){
				return;
			}
			
			LocationInfoExt location = AppContext.getInstance().getCurLocation();
			if ( location != null) {
				/*if (navLocalGeoData == null) {
					navLocalGeoData = new NavLocalGeoData();
				}*/
				noLuDuan = NavLocalGeoData.findNearLuDuan(location//AppContext.getInstance().getCurLocation()//
						, planOper.getWaitForDoDataCache(doId));
	
				setLuDuanState2(noLuDuan, false);
			}
		}
	}
	
	//当前路段处理决定
	public boolean nowDoRoadOp(InsPatrolOnsiteRecordExtVO object,int state,String memo){
		/*
		 * 1.进入巡查,2.暂停,3：完成(由系统对传入为1的进行计算判断),4:偏离,5:偏离警告,6.续巡,7.结束,8。终止
		 */
		if (object.getId().equals(curTaskLuDuan.getId())){
			curTaskLuDuan.setEnterState(state);
			curTaskLuDuan.setMemo(memo);
			if (state == 6){
				curTaskLuDuan.setPatrolStartDate(noLuDuan.getDateTime());
				curTaskLuDuan.setPatrolStartCoordinate(noLuDuan.getMapXY());
			}
			curTaskLuDuan.setPatrolEndDate(noLuDuan.getDateTime());
			curTaskLuDuan.setPatrolEndCoordinate(noLuDuan.getMapXY());
			
			updateObjState(curTaskLuDuan); 
			planOper.setCurTaskLuDuan(curTaskLuDuan);
			
		}else{
			object.setEnterState(state);
			object.setMemo(memo);
			if (state == 6){
				object.setPatrolStartDate(noLuDuan.getDateTime());
				object.setPatrolStartCoordinate(noLuDuan.getMapXY());
			}
			object.setPatrolEndDate(noLuDuan.getDateTime());
			object.setPatrolEndCoordinate(noLuDuan.getMapXY());
			updateObjState(object); 
		}
		return true;
	}
	
	/**
	 * 设置当前路段状态
	 * 
	 * @param luDuan
	 *            找到的路段名称
	 * @param dateTime
	 *            找到时间
	 * 1.进入巡查,2.暂停,3：完成,4:偏离,5:偏离警告,6.续巡,7.结束(由系统对传入为1的进行计算判断),8。终止    
	 */
	public void setLuDuanState2(NearObject nearObj, boolean end) {
		if (nearObj.getObjId() == null && curTaskLuDuan == null){
			hasEnter = false;
			curTaskLuDuan = new InsPatrolOnsiteRecordExtVO();
			if (planOper.getWaitForDoDataCache(doId).size()>0){
				curTaskLuDuan.setWarnState("00"); //未进入巡查区域
			}else{
				curTaskLuDuan.setWarnState("01");//无需要巡查路段
			}
			planOper.setCurTaskLuDuan(curTaskLuDuan);
		}else if (planOper.getWaitForDoDataCache(doId).size()>0){  //有需要巡查的路段
			
			if ((curTaskLuDuan == null)||(curTaskLuDuan.getId() == null && nearObj.getObjId() == null)){
				curTaskLuDuan = new InsPatrolOnsiteRecordExtVO();
				if (!hasEnter)
					curTaskLuDuan.setWarnState("00"); //未进入巡查区域
				else{
					curTaskLuDuan.setWarnState("04"); //已不丰巡查区域
				}
				planOper.setCurTaskLuDuan(curTaskLuDuan);
			}else{
				//记录路段的巡查轨迹
				if (null != curTaskLuDuan.getId() && curTaskLuDuan.getEnterState() != 2){
					addRunTrace(nearObj.getMapXY());
				}
				
				if (curTaskLuDuan.getId() == null||curTaskLuDuan.getEnterState()==3){
					curTaskLuDuan = createLuDuanState(nearObj);				
				}else if (curTaskLuDuan.getRoadName().equals(nearObj.getObjName())){ //在当前路段上
					hasEnter = true;
					
					curTaskLuDuan.setMinDistance(nearObj.getMinDistance());
					curTaskLuDuan.setWktGeom(nearObj.getWktGeom());
					curTaskLuDuan.setCurNearObj(nearObj);
					curTaskLuDuan.setPreRoadName("");
					
					//如果路段还未进入或是刚允许续巡查
					if ((curTaskLuDuan.getEnterState() == -1 || curTaskLuDuan.getEnterState() == 6)&& nearObj.isCanPost()){//是还达到进入距离,如果是提交进入状态并提示
						// 巡查开始时间
						curTaskLuDuan.setPatrolStartDate(nearObj.getDateTime());
						// 开始巡查坐标
						curTaskLuDuan.setPatrolStartCoordinate(nearObj.getMapXY());
						
						curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
						// 结束巡查坐标
						curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
						
						curTaskLuDuan.setEnterState(0);//设置进入路段状态
					}else if (curTaskLuDuan.getEnterState() > 0 && curTaskLuDuan.getEnterState() != 6){ //已经路段上了
						if (curTaskLuDuan.getEnterState() != 2){ //不是被暂停的
							if (nearObj.isDeviate()){
								curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
								// 结束巡查坐标
								curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
	
								if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
									curTaskLuDuan.setEnterState(3);
									planOper.setCurTaskLuDuan(curTaskLuDuan);
								}else{
									curTaskLuDuan.setEnterState(4);
								}
								
							}else if (nearObj.isOutWarn()){
								curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
								// 结束巡查坐标
								curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
								
								if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
									curTaskLuDuan.setEnterState(3);
								}else{
									curTaskLuDuan.setEnterState(5);
								} 
	
							}else if (nearObj.isCanPost()){  //当前路段自我判断，应该没什么作用？续巡？
								curTaskLuDuan.setEnterState(1);//设置进入路段状态
							}
						}
					}else if (curTaskLuDuan.getEnterState() == 6){//如果前面一路判断下来处于续巡状态，把调整为初始状态
						curTaskLuDuan.setEnterState(-1);
					}
					planOper.setCurTaskLuDuan(curTaskLuDuan);	
				}else{ //接下路段发生变化
					// 上一个路段名称
					String preRoadName = "";
					curTaskLuDuan.setMinDistance(nearObj.getMinDistance());
					curTaskLuDuan.setCurNearObj(nearObj);
					//上一路段是否处于未进入巡查状态
					if (curTaskLuDuan.getEnterState() > 0 && curTaskLuDuan.getEnterState() != 6){
						hasEnter = true;
						if (curTaskLuDuan.getEnterState() != 2){ //不是被暂停的
							if (nearObj.isDeviate3(curTaskLuDuan.getWktGeom())){  //离开判断，当前坐标位置与当前处理路段（不是当前路段哦）
								
								curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
								// 结束巡查坐标
								curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
								
								if (nearObj.isCanPost()){ //已经进入到新发现的路段范围
									preRoadName = curTaskLuDuan.getRoadName();
									if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
										curTaskLuDuan.setEnterState(3);
										planOper.setCurTaskLuDuan(curTaskLuDuan);
									}else{
										curTaskLuDuan.setEnterState(4);
									}
								}else{ //未进入新发现路段
									//是否已经处于路段巡查完成状态
									if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
										curTaskLuDuan.setEnterState(3);
										planOper.setCurTaskLuDuan(curTaskLuDuan);
									}else{
										curTaskLuDuan.setEnterState(4);
										planOper.setCurTaskLuDuan(curTaskLuDuan);
									} 
								}
							}else if (nearObj.isOutWarn3(curTaskLuDuan.getWktGeom())){   //偏离警告判断，当前坐标位置与当前处理路段（不是当前路段哦）
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
								//是否已经处于路段巡查完成状态
								if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
									curTaskLuDuan.setEnterState(3);
									planOper.setCurTaskLuDuan(curTaskLuDuan);
								}else{
									curTaskLuDuan.setEnterState(5);
									planOper.setCurTaskLuDuan(curTaskLuDuan);
								} 
								
							}
						}else{//上一路段原来被暂停
							curTaskLuDuan = createLuDuanState(nearObj);	//创建当前路段
							curTaskLuDuan.setPatrolStartDate(nearObj.getDateTime());
							// 开始巡查坐标
							curTaskLuDuan.setPatrolStartCoordinate(nearObj.getMapXY());
							curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
							// 结束巡查坐标
							curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
							
							if ((curTaskLuDuan.getEnterState() == -1 ||curTaskLuDuan.getEnterState() == 6 )&& nearObj.isCanPost()){
								curTaskLuDuan.setEnterState(1);//设置进入路段状态
								planOper.setCurTaskLuDuan(curTaskLuDuan);
							}
						}
						planOper.setCurTaskLuDuan(curTaskLuDuan);
					}else{
						curTaskLuDuan = createLuDuanState(nearObj);	
						if (null == curTaskLuDuan.getId()){
							curTaskLuDuan.setWarnState("03");
						}
					}
					
					if (curTaskLuDuan.getEnterState() == 7){
						curTaskLuDuan = createLuDuanState(nearObj);		
					}					
				}
				
				planOper.setCurTaskLuDuan(curTaskLuDuan);
			}

		} else{ //没有需要巡查的路段
			if (curTaskLuDuan == null){
				curTaskLuDuan = new InsPatrolOnsiteRecordExtVO();
				curTaskLuDuan.setWarnState("01");//
			}else{
				curTaskLuDuan.setWarnState("02");//已完成
			}
			
			planOper.setCurTaskLuDuan(curTaskLuDuan);
		}
	}
	
	/**
	 * 设置当前路段状态
	 * 
	 * @param luDuan
	 *            找到的路段名称
	 * @param dateTime
	 *            找到时间
	 * 1.进入巡查,2.暂停,3：完成,4:偏离,5:偏离警告,6.续巡,7.结束(由系统对传入为1的进行计算判断),8。终止    
	 */
	@Override
	public void setLuDuanState(NearObject nearObj, boolean end) {
		if (nearObj.getObjId() == null && curTaskLuDuan == null){
			hasEnter = false;
			curTaskLuDuan = new InsPatrolOnsiteRecordExtVO();
			if (planOper.getWaitForDoDataCache(doId).size()>0){
				curTaskLuDuan.setWarnState("00"); //未进入巡查区域
			}else{
				curTaskLuDuan.setWarnState("01");//无需要巡查路段
			}
			planOper.setCurTaskLuDuan(curTaskLuDuan);
		}else if (planOper.getWaitForDoDataCache(doId).size()>0){  //有需要巡查的路段
			
			if ((curTaskLuDuan == null)||(curTaskLuDuan.getId() == null && nearObj.getObjId() == null)){
				curTaskLuDuan = new InsPatrolOnsiteRecordExtVO();
				if (!hasEnter)
					curTaskLuDuan.setWarnState("00"); //未进入巡查区域
				else{
					curTaskLuDuan.setWarnState("04"); //已不丰巡查区域
				}
				planOper.setCurTaskLuDuan(curTaskLuDuan);
			}else{
				//记录路段的巡查轨迹
				if (null != curTaskLuDuan.getId() && curTaskLuDuan.getEnterState() != 2){
					addRunTrace(nearObj.getMapXY());
				}
				
				if (curTaskLuDuan.getId() == null){
					curTaskLuDuan = createLuDuanState(nearObj);				
				}else if (curTaskLuDuan.getRoadName().equals(nearObj.getObjName())){ //在当前路段上
					hasEnter = true;
					
					curTaskLuDuan.setWktGeom(nearObj.getWktGeom());
					curTaskLuDuan.setMinDistance(nearObj.getMinDistance());
					curTaskLuDuan.setCurNearObj(nearObj);
					curTaskLuDuan.setPreRoadName("");
					
					//如果路段还未进入或是刚允许续巡查
					if ((curTaskLuDuan.getEnterState() == -1 || curTaskLuDuan.getEnterState() == 6)&& nearObj.isCanPost()){//是还达到进入距离,如果是提交进入状态并提示
						// 巡查开始时间
						curTaskLuDuan.setPatrolStartDate(nearObj.getDateTime());
						// 开始巡查坐标
						curTaskLuDuan.setPatrolStartCoordinate(nearObj.getMapXY());
						
						curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
						// 结束巡查坐标
						curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
						
						curTaskLuDuan.setEnterState(1);//设置进入路段状态
						updateObjState(curTaskLuDuan); 
					}else if (curTaskLuDuan.getEnterState() > 0 && curTaskLuDuan.getEnterState() != 6){ //已经路段上了
						if (curTaskLuDuan.getEnterState() != 2){ //不是被暂停的
							if (nearObj.isDeviate()){
								curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
								// 结束巡查坐标
								curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
	
								if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
									curTaskLuDuan.setEnterState(3);
									updateObjState(curTaskLuDuan);//离开记录
									planOper.setCurTaskLuDuan(curTaskLuDuan);
								}else{
									curTaskLuDuan.setEnterState(4);
									updateObjState(curTaskLuDuan); //偏离
								}
							}else if (nearObj.isOutWarn()){
								curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
								// 结束巡查坐标
								curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
								
								if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
									curTaskLuDuan.setEnterState(3);
									updateObjState(curTaskLuDuan);//离开
									//planOper.setCurTaskLuDuan(curTaskLuDuan);									
								}else{
									curTaskLuDuan.setEnterState(5);
									updateObjState(curTaskLuDuan); //1.进入巡查,2.暂停,3：完成(由系统对传入为1的进行计算判断),4:偏离,5:偏离警告,6.续巡,7.结束,8。终止     
								} 
	
							}else if (nearObj.isCanPost()){  //当前路段自我判断，应该没什么作用？续巡？
								curTaskLuDuan.setEnterState(1);//设置进入路段状态
							}
						}
					}else if (curTaskLuDuan.getEnterState() == 6){//如果前面一路判断下来处于续巡状态，把调整为初始状态
						curTaskLuDuan.setEnterState(-1);
					}
					planOper.setCurTaskLuDuan(curTaskLuDuan);	
				}else{ //接下路段发生变化
					// 上一个路段名称
					String preRoadName = "";
					curTaskLuDuan.setMinDistance(nearObj.getMinDistance());
					curTaskLuDuan.setCurNearObj(nearObj);
					//上一路段是否处于未进入巡查状态
					if (curTaskLuDuan.getEnterState() > 0 && curTaskLuDuan.getEnterState() != 6){
						hasEnter = true;
						if (curTaskLuDuan.getEnterState() != 2){ //不是被暂停的
							if (nearObj.isDeviate3(curTaskLuDuan.getWktGeom())){  //离开判断，当前坐标位置与当前处理路段（不是当前路段哦）
								
								curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
								// 结束巡查坐标
								curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
								
								if (nearObj.isCanPost()){ //已经进入到新发现的路段范围
									preRoadName = curTaskLuDuan.getRoadName();
									if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
										curTaskLuDuan.setEnterState(3);
										updateObjState(curTaskLuDuan);//上一路段离开记录
										planOper.setCurTaskLuDuan(curTaskLuDuan);
									}else{
										curTaskLuDuan.setEnterState(4);//2暂停,改成路段变化时为离开,减少“暂停”提示及操作
										updateObjState(curTaskLuDuan); //暂停
									}
									
									//创建当前新发现的路段
									curTaskLuDuan = createLuDuanState(nearObj);	
									curTaskLuDuan.setPreRoadName(preRoadName);
									
									//if (this.roadDoType == RoadDOType.auto){
									curTaskLuDuan.setPatrolStartDate(nearObj.getDateTime());
									// 开始巡查坐标
									curTaskLuDuan.setPatrolStartCoordinate(nearObj.getMapXY());
									curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
									// 结束巡查坐标
									curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
									//发现的新路段，如果已经在进入范围内
									if (curTaskLuDuan.getEnterState() == -1 ||curTaskLuDuan.getEnterState() == 6){
										curTaskLuDuan.setEnterState(1);//设置进入路段状态
										updateObjState(curTaskLuDuan); 
										planOper.setCurTaskLuDuan(curTaskLuDuan);
									}
								}else{ //未进入新发现路段
									//是否已经处于路段巡查完成状态
									if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
										curTaskLuDuan.setEnterState(3);
										updateObjState(curTaskLuDuan);//离开
										planOper.setCurTaskLuDuan(curTaskLuDuan);
									}else{
										curTaskLuDuan.setEnterState(4);//2暂停,改成路段变化时为离开,减少“暂停”提示及操作
										updateObjState(curTaskLuDuan); //暂停
										planOper.setCurTaskLuDuan(curTaskLuDuan);
									} 
									
									curTaskLuDuan = createLuDuanState(nearObj);	//创建当前路段
								}
							}else if (nearObj.isOutWarn3(curTaskLuDuan.getWktGeom())){   //偏离警告判断，当前坐标位置与当前处理路段（不是当前路段哦）
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
								//是否已经处于路段巡查完成状态
								if (NavLocalGeoData.isContain(curTaskLuDuan.getHadRunTrace(), curTaskLuDuan.getRoadShape(), _TORLENCE)){
									curTaskLuDuan.setEnterState(3);
									updateObjState(curTaskLuDuan);//离开
									planOper.setCurTaskLuDuan(curTaskLuDuan);
									
									curTaskLuDuan = createLuDuanState(nearObj);	//创建当前路段
								}else{
									curTaskLuDuan.setEnterState(5);
									updateObjState(curTaskLuDuan); //1.进入巡查,2.暂停,3：完成(由系统对传入为1的进行计算判断),4:偏离,5:偏离警告,6.续巡,7.结束,8。终止  
									planOper.setCurTaskLuDuan(curTaskLuDuan);
								} 
							}
						}else{//上一路段原来被暂停
							curTaskLuDuan = createLuDuanState(nearObj);	//创建当前路段
							//if (this.roadDoType == RoadDOType.auto){
							curTaskLuDuan.setPatrolStartDate(nearObj.getDateTime());
							// 开始巡查坐标
							curTaskLuDuan.setPatrolStartCoordinate(nearObj.getMapXY());
							curTaskLuDuan.setPatrolEndDate(nearObj.getDateTime());
							// 结束巡查坐标
							curTaskLuDuan.setPatrolEndCoordinate(nearObj.getMapXY());
							
							if ((curTaskLuDuan.getEnterState() == -1 ||curTaskLuDuan.getEnterState() == 6 )&& nearObj.isCanPost()){
								curTaskLuDuan.setEnterState(1);//设置进入路段状态
								updateObjState(curTaskLuDuan); 
								planOper.setCurTaskLuDuan(curTaskLuDuan);
							}
						}
						planOper.setCurTaskLuDuan(curTaskLuDuan);
					}else{
						curTaskLuDuan = createLuDuanState(nearObj);	
						if (null == curTaskLuDuan.getId()){
							curTaskLuDuan.setWarnState("03");
						}
					}
					
					if (curTaskLuDuan.getEnterState() == 7){
						curTaskLuDuan = createLuDuanState(nearObj);		
					}					
				}
				
				planOper.setCurTaskLuDuan(curTaskLuDuan);
			}

		} else{ //没有需要巡查的路段
			if (curTaskLuDuan == null){
				curTaskLuDuan = new InsPatrolOnsiteRecordExtVO();
				curTaskLuDuan.setWarnState("01");//
			}else{
				curTaskLuDuan.setWarnState("02");//已完成
			}
			
			planOper.setCurTaskLuDuan(curTaskLuDuan);
		}
	}
	
	public InsPatrolOnsiteRecordExtVO createLuDuanState(NearObject nearObj){
		//建立已巡路线
		runTrace = new Polyline();
		runPrePoint = null;
		
		InsPatrolOnsiteRecordExtVO tmpLuDuan = new InsPatrolOnsiteRecordExtVO();
		
		/*if (null != nearObj.getPreRunTrace()){
			runTrace = nearObj.getPreRunTrace();
		}*/
				
		tmpLuDuan.setId(nearObj.getObjId());
		
		// 路段编号
		tmpLuDuan.setRoadNum(nearObj.getObjNum());
		// 路名
		tmpLuDuan.setRoadName(nearObj.getObjName());
		
		// 上一个路段编号
		tmpLuDuan.setPreRoadNum("");
		// 上一个路段名称
		tmpLuDuan.setPreRoadName("");
		// 上一个路段巡查开始时间
		tmpLuDuan.setPrePsDate("");
		// 上一个路段开始巡查坐标
		tmpLuDuan.setPrePsCoordinate("");
		// 上一个路段巡查结束时间
		tmpLuDuan.setPrePeDate("");
		// 上一个路段结束巡查坐标
		tmpLuDuan.setPrePeCoordinate("");
		
		if (null != nearObj.getState() && nearObj.getState().equals("pause")){
			tmpLuDuan.setEnterState(2);//已经补暂停
		}else{
			tmpLuDuan.setEnterState(-1);
		}
		
		if (this.roadDoType == RoadDOType.auto){
			tmpLuDuan.setWarnState("10"); //自动模式
		}else{
			tmpLuDuan.setWarnState("11"); //手模式
		}
		
		
		tmpLuDuan.setMinDistance(nearObj.getMinDistance());
		tmpLuDuan.setWktGeom(nearObj.getWktGeom());
		tmpLuDuan.setCurNearObj(nearObj);
		tmpLuDuan.setRunTrace(runTrace);
		tmpLuDuan.setPreRunTrace(nearObj.getPreRunTrace());
		return tmpLuDuan;
	}
	
	public void addRunTrace(String coord){
		if (null ==runTrace){
			return ;
		}
		Point nowPoint = new Point(Double.valueOf(coord.split("\\ ")[0]),Double.valueOf(coord.split("\\ ")[1]));
		if (runPrePoint == null){
			runPrePoint = nowPoint;
		}else{
			Line line = new Line();
			line.setStart(runPrePoint);
			line.setEnd(nowPoint);
			runTrace.addSegment(line, false);
		}
	}
}
