package com.movementinsome;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NetBroadcastReceiver extends BroadcastReceiver {
	public static ArrayList<netEventHandler> mListeners = new ArrayList<netEventHandler>();
	private static String NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(NET_CHANGE_ACTION)) {
			//Application.mNetWorkState = NetUtil.getNetworkState(context);
			if (mListeners.size() > 0)// 通知接口完成加载
				for (netEventHandler handler : mListeners) {
					handler.onNetChange();
				}
		}
	}

	public static abstract interface netEventHandler {

		public abstract void onNetChange();
	}
}
