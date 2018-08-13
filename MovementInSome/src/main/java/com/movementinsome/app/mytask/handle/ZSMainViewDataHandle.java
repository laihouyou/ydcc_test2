package com.movementinsome.app.mytask.handle;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import android.content.Intent;

public class ZSMainViewDataHandle  implements MainViewDataBaseHandle {

	@Override
	public void updatePlanList(String type,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Map<String, Object>> updateList(String type,
			List<Map<String, Object>> data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void blackHandle(Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateTaskList(String title, String taskCategory,Map<String, Object> m,
			List<Map<String, Object>> groupData,
			List<List<Map<String, Object>>> childData) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
