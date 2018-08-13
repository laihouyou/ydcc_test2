package com.movementinsome.app.remind.leakInsArea;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;

import com.movementinsome.AppContext;
import com.movementinsome.app.remind.nav.NavLocalGeoData;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.kernel.location.LocationInfoExt;

public class LeakInsAreaTranceTask extends TimerTask{
	// true时使用后台进程线程。只要剩下的程序记叙运行，后台进程线程就会执行。
	Timer myTimer;

	private LeakInsAreaOperate planOper;
	private Context context;
	// private List<InsPatrolDataVO> obj;
	private String doId;

	private NavLocalGeoData navLocalGeoData = null;

	private NearObject noSite;

	public LeakInsAreaTranceTask(LeakInsAreaOperate planOper, Context context,
			String doId) {
		this.planOper = planOper;
		this.context = context;
		this.doId = doId;// UUID.randomUUID().toString();
		// this.obj = obj;
	}

	public void start(int delay) {
		myTimer = new Timer();
		myTimer.schedule(this, delay);
	}

	public void start(int delay, int period) {
		myTimer = new Timer();
		myTimer.schedule(this, delay, period); // 利用timer.schedule方法

		// public void schedule(TimerTask task,long time,long period)
		// task被安排在延迟time后执行，执行后将每隔period(毫秒)反复执行。由于规定的时间间隔并不能保证与时钟精准的同不步，所以该方
	}

	public void start(Date time, int period) {
		myTimer = new Timer();
		myTimer.schedule(this, time, period); // 利用timer.schedule方法

		// public void schedule(TimerTask task,Date time,long period)
		// task被安排在time指定的时间执行，执行后将每隔period(毫秒)反复执行。由于规定的时间间隔并不能保证与时钟精准的同不步，所以该方
	}

	public void run() {
		// 执行任务(
		LocationInfoExt location = AppContext.getInstance().getCurLocation();//locations[i];
		if ( location != null) {

			/*
			 * NearObject noQianDD =
			 * NavLocalGeoData.getInstance().findNearQianDaoDian
			 * (AppContext.getInstance().getCurLocation(),50); if (noQianDD !=
			 * null){ if (!"".equals(noQianDD.getObj())){
			 * Toast.makeText(context,
			 * "距离签到点："+noQianDD.getObj()+"还有多少"+String.valueOf
			 * (noQianDD.getMinDistance()),Toast.LENGTH_LONG ).show(); } }
			 */
			if (navLocalGeoData == null) {
				navLocalGeoData = new NavLocalGeoData();
			}
			noSite = navLocalGeoData.findNearBsLeakInsArea(location, planOper.getWaitForDoDataCache(doId));
			if (noSite != null) {
				if (null != noSite.getObjName()) {
					if (planOper.getNearSite() == null) {
						setNearSiteState(noSite);
					} else if (!planOper.getNearSite().getObjName()
							.equals(noSite.getObjName())) {
						setNearSiteState(noSite);
					} else if (planOper.getNearSite().getMinDistance() > noSite
							.getMinDistance()) {
						setNearSiteState(noSite);
					}
				}
			}
		}
	}

	public void end() {
		myTimer.cancel();
		// 终止Timer的功能执行，但不会对正在执行的任务有影响。当执行cancel方法后将不能再用其分配任务。
	}

	/**
	 * 设置最近签到点
	 * 
	 * @param luDuan
	 *            找到的路段名称
	 * @param dateTime
	 *            找到时间
	 */
	public void setNearSiteState(NearObject nearObj) {
		planOper.setNearSite(nearObj);
	}
}
