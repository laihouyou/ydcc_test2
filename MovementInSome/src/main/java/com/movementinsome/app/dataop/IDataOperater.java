package com.movementinsome.app.dataop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Handler;

import com.esri.core.geometry.Polyline;
import com.movementinsome.app.remind.nav.NearObject;
import com.movementinsome.app.remind.road.InsPatrolOnsiteRecordExtVO;
import com.movementinsome.kernel.initial.model.Module;

public interface IDataOperater {
	// 网络访问状态
	public enum  DataType{   //路段:PL	//一般巡查：PT   //带区域的：Pts
		Pt, Pl, Pg,Pts
	}

	//初始化数据
	public int initial(final Handler handler);
	//设置数据变化监听器
	public void end();
	//public void DataSetChangeListener(final Handler handler);
	//刷新本次待巡数据
	public void refreshThisTimeData();
	// 搜索
	public void searchData(String num);
	
	public List<String> getThisTimeDataWtsGeo();
	public List<String> getHadDoDataGeo();
	public List<Polyline> getTaskPreTrace();
	//获取已巡数据
	public List<Map<String, Object>> getHadDoDataList();
	//获取未巡数据
	public List<Map<String, Object>> getWouldDoDataList();
	// 获取搜索的数据
	public List<Map<String, Object>> getSearchDataList();
	// 获取附近的数据
	public List<Map<String, Object>> getNearbyDataList();
	//获取当前位置点目标
	public NearObject getCurentNearObj();
	public List<NearObject> getCurentNearObjs();
	//获取当前位置路段
	public InsPatrolOnsiteRecordExtVO getCurTaskLuDuan();
	//获取目标关联操作模块
	public List<Module> getLinkModule();
	//获取传输参数定义
	public HashMap<String, String> getTransParams();
	//当前巡查对象处理
	public void speObjOp(InsPatrolOnsiteRecordExtVO object,int state,String memo);
/*	// 指定目标暂停巡检
	public void pause(InsPatrolOnsiteRecordExtVO pauseRoad);
	// 指定目标继续巡检
	public void goNo(InsPatrolOnsiteRecordExtVO pauseRoad);*/
	
	// 任务暂停巡检
	public void insPause();
	// 任务继续巡检
	public void insGoNo();
	
	public DataType dataType();
	
	public void callback(Intent data);

}
