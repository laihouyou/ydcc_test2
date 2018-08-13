package com.movementinsome.app.mytask.handle;

import android.view.View;

import java.sql.SQLException;
import java.util.Map;

public interface MoveTaskListCentreBaseHandle {
	// 查看
	void showMsgHandler();
	// 采集
	void startWorkHandler();
	// 删除
	void delect() throws SQLException;
	// 完成
	void finishHandler();
	// 提交
	void subHandler();
	// 进入设施点列表
	void facilityList();
	// 控件控制
	void controlUI(Map<String, View> v);
}
