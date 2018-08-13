package com.movementinsome.app.mytask.handle;

import java.util.Map;

import android.os.Handler;
import android.view.View;

public interface TaskListBaseHandle {
	// 定位处理（子单）
	void childLocHandler();

	// 填单处理（子单）
	void childWriteTableHandler();

	// 显示详细信息（子单）
	void childShowMsg();

	// 完成（子单）
	void childFinish();
	
	// 完工
	void childComplete();

	// 填单处理（主单）
	void groupWriteTableHandler();

	// 开始(主单)
	void groupStartHandler(String mModuleid);

	// 退单(主单)
	void groupReturnTableHandler();

	// 完成(主单)
	void groupFinishHandler();
	
	// 显示详细(主单)
	void groupShowMsg();

	// 下载(主单)
	void groupDownloadHandler(Handler myHandler);

	// 控件控制(主单)
	void groupControlUI(Map<String, View> vs);

	// 控件控制(子单)
	void childControlUI(Map<String, View> vs);
}
