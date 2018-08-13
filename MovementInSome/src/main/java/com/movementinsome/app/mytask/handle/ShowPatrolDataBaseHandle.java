package com.movementinsome.app.mytask.handle;

import java.util.Map;

import android.view.View;

public interface ShowPatrolDataBaseHandle {

	void controlUI(Map<String, View> v);

	void updateUI(Map<String, View> v);

	void initPatrolData();

	void backHandler();

	void writeHandlerB();

	void writeHandlerD();

	void patrolN(Map<String, View> v);

	void patrolY(Map<String, View> v);

}
