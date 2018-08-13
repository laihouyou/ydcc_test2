package com.movementinsome.app.remind;

import java.sql.SQLException;
import java.util.List;

import com.j256.ormlite.stmt.PreparedQuery;

public interface IFreqData {

	//获取周期性巡检计划路段数据
	public List<?> getData();
	//获取指定任务的巡检计划路段数据
	public List<?> getData(String workTaskNum);
	//获取周期内已巡检数据
	public List<?> getWeekHadoData(String workTaskNum);
	//检查该对象巡查对象是否在本次巡查周期之内
	public Integer checkInThisFreq(FreqObject obj);//-1:本周期结束,0:不在周期,1:本周期要巡,2:本周期没巡
	
	public boolean checkDoInThisFreq(FreqObject obj);//true:周期内已巡过的
	//过滤条件
	public PreparedQuery<?> queryPre(String workTaskNum) throws SQLException;
	//返回实例对象的标志
	public String getClassFlag();
}
