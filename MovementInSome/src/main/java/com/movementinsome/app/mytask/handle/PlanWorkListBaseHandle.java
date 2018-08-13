package com.movementinsome.app.mytask.handle;

import java.util.Map;

import com.movementinsome.app.mytask.adapter.PlanWorkAdapter;

import android.view.View;

public interface PlanWorkListBaseHandle {
	
	// 控制UI
	public void controlUI(Map<String, View> vs);
	// 定位处理
	public void lochandler();
	// 填单出来
	public void writeTable();
	// 显示详细信息
	public void showMsg();
	// title的点击处理方法
	public void titleOnClick(PlanWorkAdapter planWorkAdapter);
	// 退单处理
	public void tuidanTable();
	
}
