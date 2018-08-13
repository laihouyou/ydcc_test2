package com.movementinsome.kernel.util;

import java.lang.reflect.InvocationTargetException;

import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;

/**
 * 设备状态控制及信息获取
 * 
 * @author gordon
 * 
 */
public class DeviceCtrlTools {

	private static String batterLevel ="";
	
	private static final int BUTTON_BLUETOOTH = 4;
	private static final int BUTTON_BRIGHTNESS = 1;
	private static final int BUTTON_GPS = 3;
	private static final int BUTTON_SYNC = 2;
	private static final int BUTTON_WIFI = 0; 
	/**
	 * 获取SDK版本号
	 */
	private static int getSDKVersion() {
		int sdk_int = Build.VERSION.SDK_INT;
		//Log.d(TAG, "SDK版本：" + sdk_int);
		return sdk_int;
	}


	public static void toggleGPS(Context con) {
		//if (getSDKVersion() < 8) 
			toggleGPSBelowSDK8(con);
/*		 else
		toggleGPSAfterSDK8(con);*/	
	}
	
	public static void toggleGPSAfterSDK8(Context con){
		Settings.Secure.setLocationProviderEnabled(con.getContentResolver(), LocationManager.GPS_PROVIDER, true);
	}

	/**
	 * Level8(SDK2.2)之前使用的方法
	 * <p>
	 * 开关GPS。 如果此时是关闭的则打开，否则相反
	 * 
	 * @param con
	 */
	public static void toggleGPSBelowSDK8(Context con) {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(con, 0, gpsIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 查看GPS状态
	 * 
	 * @param con
	 * @return true为打开,false为关闭
	 */
	public static boolean isGPSEnable(Context con) {
		/*
		 * 用Setting.System来读取也可以，只是这是更旧的用法 String str =
		 * Settings.System.getString(getContentResolver(),
		 * Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		 */
		String str = Settings.Secure.getString(con.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		//Log.d("GPS", "FUCK!!!!!!!!!!!!" + str);
		if (str != null) {
			return str.contains("gps");
		} else {
			return false;
		}
	}
	
	/**
	 * 打开数据连接开关s
	 * 
	 * @param enabled
	 */
	public static void openMobileDataSwitch(Context context, boolean enabled) {
		
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk>20){
			return;
		}
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// ConnectivityManager类
		Class<?> conMgrClass = null;
		// ConnectivityManager类中的字段
		java.lang.reflect.Field iConMgrField = null;
		// IConnectivityManager类的引用
		Object iConMgr = null;
		// IConnectivityManager类
		Class<?> iConMgrClass = null;
		// setMobileDataEnabled方法
		java.lang.reflect.Method setMobileDataEnabledMethod = null;
		try {
			// 取得ConnectivityManager类
			conMgrClass = Class.forName(conMgr.getClass().getName());
			// 取得ConnectivityManager类中的对象Mservice
			iConMgrField = conMgrClass.getDeclaredField("mService");
			// 设置mService可访问
			iConMgrField.setAccessible(true);
			// 取得mService的实例化类IConnectivityManager
			iConMgr = iConMgrField.get(conMgr);
			// 取得IConnectivityManager类
			iConMgrClass = Class.forName(iConMgr.getClass().getName());
			// 取得IConnectivityManager类中的setMobileDataEnabled(boolean)方法
			setMobileDataEnabledMethod = iConMgrClass.getDeclaredMethod(
					"setMobileDataEnabled", Boolean.TYPE);
			// 设置setMobileDataEnabled方法是否可访问
			setMobileDataEnabledMethod.setAccessible(true);
			// 调用setMobileDataEnabled方法
			setMobileDataEnabledMethod.invoke(iConMgr, enabled);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void openWifi(Context context, boolean isEnable) {
		WifiManager mWm = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		// System.out.println("wifi===="+mWm.isWifiEnabled());
		if (isEnable) {// 开启wifi
			if (!mWm.isWifiEnabled()) {
				mWm.setWifiEnabled(true);
			}
		} else {
			// 关闭 wifi
			if (mWm.isWifiEnabled()) {
				mWm.setWifiEnabled(false);
			}
		}
	}

	public static String getBattery(Context context) {
		try {
			IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			context.registerReceiver(BatteryReceiver.getInstance(), batteryLevelFilter);
			
			while (true){
				if (!"".equals(batterLevel)){
					return batterLevel;
				}
			}
			
		} finally {
			//context.unregisterReceiver(BatteryReceiver.getInstance());
		}
	}

	private static class BatteryReceiver extends BroadcastReceiver {
		static BatteryReceiver batteryReceiver = null;
		
		/*public BatteryReceiver(){
			
		}*/
		
		public static BatteryReceiver getInstance(){
			if (batteryReceiver == null){
				batteryReceiver = new BatteryReceiver();
			}
			return batteryReceiver;
		}
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			int rawlevel = intent.getIntExtra("level", -1);
			int scale = intent.getIntExtra("scale", -1);
			int status = intent.getIntExtra("status", -1);
			int health = intent.getIntExtra("health", -1);
			int level = -1; // percentage, or -1 for unknown
			if (rawlevel >= 0 && scale > 0) {
				level = (rawlevel * 100) / scale;
			}
			sb.append("The phone");
			if (BatteryManager.BATTERY_HEALTH_OVERHEAT == health) {
				sb.append("'s battery feels very hot!");
			} else {
				switch (status) {
				case BatteryManager.BATTERY_STATUS_UNKNOWN:
					sb.append("no battery.");
					break;
				case BatteryManager.BATTERY_STATUS_CHARGING:
					sb.append("'s battery");
					if (level <= 33)
						sb.append(" is charging, battery level is low" + "["
								+ level + "]");
					else if (level <= 84)
						sb.append(" is charging." + "[" + level + "]");
					else
						sb.append(" will be fully charged.");
					break;
				case BatteryManager.BATTERY_STATUS_DISCHARGING:
				case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
					if (level == 0)
						sb.append(" needs charging right away.");
					else if (level > 0 && level <= 33)
						sb.append(" is about ready to be recharged, battery level is low"
								+ "[" + level + "]");
					else
						sb.append("'s battery level is" + "[" + level + "]");
					break;
				case BatteryManager.BATTERY_STATUS_FULL:
					sb.append(" is fully charged.");
					break;
				default:
					sb.append("'s battery is indescribable!");
					break;
				}
			}
			sb.append(' ');
			batterLevel = sb.toString();
		}

	}
}
