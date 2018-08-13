package com.movementinsome.app.mytask.handle;

import java.util.Map;

import android.os.Handler;
import android.view.View;

public interface TaskListCentreBaseHandle {
	// 同号
	void identicalHandler ();
	// 完成
	void finishHandler ();
	// 退单
	void returnTableHandler();
	// 下载
	void downloadHandler(Handler myHandler);
	// 显示详细内容
	void showMsgHandler();
	// 开始
	void startWorkHandler();
	// 填单
	void writeTable();
	// 定位
	void locHandler();
	// 控件控制
	void controlUI(Map<String, View> v);
}
