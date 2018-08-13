package com.movementinsome.app.remind;

import java.util.List;

import android.content.Context;

public interface IPlanOperate {

	/**
	 * 发出开始巡查动作
	 * 
	 * @param doId
	 *            每次执行产生的ID识别码,用UUID表达
	 * @param obj
	 *            本次本巡内容
	 */
	public void insBeginDo(Context context, String doId,String workTaskNum);
	
	/**
	 * 发出停止执行动作
	 * 
	 * @param doId
	 *            开始执行时产生的识别码
	 */
	public void insEndDo(String doId) ;
	
	/**
	 * 获取所有周期性数据
	 * 
	 * @return
	 */
	public List<?> getAllFreqData();
	
	/**
	 * 获取指定任务内容数据
	 * 
	 * @return
	 */
	public List<?> getAllData(String workTaskNum);
	
	/**
	 * 获取本次等执行周期性巡查数据
	 * 
	 * @return
	 */
	public List<?> getThisTimeFreqData() ;
	
	/**
	 * 获取本次等执行巡查数据
	 * 
	 * @return
	 */
	public List<?> getThisTimeData(String workTaskNum);
}
