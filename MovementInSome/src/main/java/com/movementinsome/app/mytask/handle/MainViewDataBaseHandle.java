package com.movementinsome.app.mytask.handle;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import android.content.Intent;

public interface MainViewDataBaseHandle {
	// 更新计划列表数据
	void updatePlanList(String type,List<Map<String, Object>> groupData,List<List<Map<String, Object>>> childData) throws SQLException;
	// 更新派工单列表数据
	List<Map<String, Object>> updateList(String type,List<Map<String, Object>> data);
	// 更新任务列表
	void updateTaskList(String title,String taskCategory,Map<String, Object> condition,
			List<Map<String, Object>> groupData,List<List<Map<String, Object>>> childData)throws SQLException;
	// 返回处理
	void blackHandle(Intent data);
}
