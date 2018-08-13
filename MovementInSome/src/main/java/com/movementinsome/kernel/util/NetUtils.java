package com.movementinsome.kernel.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class NetUtils {
	private static Uri PREFERRED_APN_URI = Uri
			.parse("content://telephony/carriers/preferapn");// 4.0模拟器屏蔽掉该权限
	public static String PROXY_IP;
	public static Integer PROXY_PORT;

	/**
	 * 检查网络
	 * 
	 * @return
	 */
	public static boolean checkNetWork(Context context) {
		// ConnectivityManager//系统服务
		// ①判断WIFI联网情况
		boolean isWifi = isWifi(context);
		// ②判断MOBILE联网情况
		boolean isMobile = isMobile(context);

		if (!isWifi && !isMobile) {
			// 如果都不能联网，提示用户
			return false;
		}

		// ③判断MOBILE是否连接
		if (isMobile) {
			// 如果是，判断一下是否是wap（代理的信息）
			// wap 还是 net？看当前正在连接的去渠道的配置信息（proxy port），如果有值wap
			try {
				readAPN(context);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return true;
	}

	public static String readAPN(Context context) {
		Cursor cursor = context.getContentResolver().query(PREFERRED_APN_URI,
				null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			PROXY_IP = cursor.getString(cursor.getColumnIndex("proxy"));
			PROXY_PORT = cursor.getInt(cursor.getColumnIndex("port"));
		}
		return "";
	}

	/**
	 * 判断Mobile是否处于连接状态
	 */
	public static boolean isMobile(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (null == networkInfo){ //如果没有手机卡是对象为null
			return false;
		}else{
			return networkInfo.isConnected();
		}		
	}

	/**
	 * 判断wifi是否处于连接状态
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return networkInfo.isConnected();
	}

	// 判断wifi是否打开
	public static boolean isWifiActive(Context context) {
		ConnectivityManager mConnectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (mConnectivity != null) {
			NetworkInfo[] infos = mConnectivity.getAllNetworkInfo();

			if (infos != null) {
				for (NetworkInfo ni : infos) {
					if ("WIFI".equals(ni.getTypeName()) && ni.isConnected())
						return true;
				}
			}
		}

		return false;
	}

	// 得到本机IP地址
	public static String getLocalIpAddress() {
		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();
			while (en.hasMoreElements()) {
				NetworkInterface nif = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = nif.getInetAddresses();
				while (enumIpAddr.hasMoreElements()) {
					InetAddress mInetAddress = enumIpAddr.nextElement();
					if (!mInetAddress.isLoopbackAddress()
							&& InetAddressUtils.isIPv4Address(mInetAddress
									.getHostAddress())) {
						return mInetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("error", "获取本地IP地址失败");
		}

		return null;
	}
	
	public static String getBroadCastIP() {
		String ip = getLocalIpAddress().substring(0,
				getLocalIpAddress().lastIndexOf(".") + 1)
				+ "255";
		return ip;
	}

	// 获取本机MAC地址
	public static String getLocalMacAddress(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifi.getConnectionInfo();
		return info.getMacAddress();
	}

	/**
	 * 判断是否有网络
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * @author  获取当前的网络状态 -1：没有网络
	 *         1：WIFI网络2：数据网络
	 * @param context
	 * @return
	 */
	public static int getAPNType(Context context) {
		int netType = -1;
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			/*if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = 3;
			} else {
				netType = 2;
			}*/
			netType = 2;
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = 1;
		}
		return netType;
	}
}
