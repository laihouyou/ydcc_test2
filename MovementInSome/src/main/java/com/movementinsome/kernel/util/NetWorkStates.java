package com.movementinsome.kernel.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetWorkStates {
	private ConnectivityManager cm;
	private NetworkInfo ni;

	public NetWorkStates(Context context) {
		cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		ni = cm.getActiveNetworkInfo();
	}

	/**
	 * 是否有网络连接
	 * 
	 *
	 */
	public boolean isNetWorkOn() {
		if (ni == null) {
			return false;
		} else
			return true;
	}

	/**
	 * 网络连接是否可用
	 * 
	 * @return
	 */
	public boolean isNetWorkAvailable() {
		return ni.isAvailable();
	}

	/**
	 * 获取网络类型：GPRS/WIFI
	 * 
	 * @return
	 */
	public String getNetWorkType() {
		State mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (mobile == State.CONNECTED)
			return "GPRS";
		State wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (wifi == State.CONNECTED)
			return "WIFI";
		
		return "";
	}
}
