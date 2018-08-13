package com.movementinsome.kernel.util;

import java.util.Map;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class ActivityUtil {

	/** 获取屏幕的宽度 */
	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	/** 获取屏幕的高度度 */
	public final static int getWindowsHetght(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}
	
	/**
	 * 设置Activity全屏显示
	 * 
	 * @param activity
	 *            Activity引用
	 * @param isFull
	 *            true为全屏，false为非全屏
	 */
	public static void setFullScreen(Activity activity, boolean isFull) {
		Window window = activity.getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		if (isFull) {
			params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
			window.setAttributes(params);
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		} else {
			params.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.setAttributes(params);
			window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
		}
	}

	/**
	 * 隐藏Activity的系统默认标题栏
	 * 
	 * @param activity
	 *            Activity对象
	 */
	public static void hideTitleBar(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 强制设置Actiity的显示方向为垂直方向
	 * 
	 * @param activity
	 *            Activity对象
	 */
	public static void setScreenVertical(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	/**
	 * 强制设置Actiity的显示方向为横向
	 * 
	 * @param activity
	 *            Activity对象
	 */
	public static void setScreenHorizontal(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}

	/**
	 * 隐藏软件输入法
	 * 
	 * @param activity
	 */
	public static void hideSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	/**
	 * 使UI适配输入法
	 * 
	 * @param activity
	 */
	public static void adjustSoftInput(Activity activity) {
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}

	/**
	 * 跳转到某个Activity
	 * 
	 * @param activity
	 *            本Activity
	 * @param targetActivity
	 *            目标Activity的Class
	 */
	public static void switchTo(Activity activity,
			Class<? extends Activity> targetActivity) {
		switchTo(activity, new Intent(activity, targetActivity));
	}

	/**
	 * 根据给定的Intent进行Activity跳转
	 * 
	 * @param activity
	 *            Activity对象
	 * @param intent
	 *            要传递的Intent对象
	 */
	public static void switchTo(Activity activity, Intent intent) {
		activity.startActivity(intent);
	}

	/**
	 * 带参数进行Activity跳转
	 * 
	 * @param activity
	 *            Activity对象
	 * @param targetActivity
	 *            目标Activity的Class
	 * @param params
	 *            跳转所带的参数
	 */
	public static void switchTo(Activity activity,
			Class<? extends Activity> targetActivity, Map<String, Object> params) {
		if (null != params) {
			Intent intent = new Intent(activity, targetActivity);
			for (Map.Entry<String, Object> entry : params.entrySet()) {
				setValueToIntent(intent, entry.getKey(), entry.getValue());
			}
			switchTo(activity, intent);
		}
	}

	/**
	 * 带参数进行Activity跳转
	 * 
	 * @param activity
	 * @param target
	 * @param params
	 */
	public static void switchTo(Activity activity,
			Class<? extends Activity> target, NameValuePair... params) {
		if (null != params) {
			Intent intent = new Intent(activity, target);
			for (NameValuePair param : params) {
				setValueToIntent(intent, param.getName(), param.getValue());
			}
			switchTo(activity, intent);
		}
	}

	/**
	 * 显示Toast消息，并保证运行在UI线程中
	 * 
	 * @param activity
	 * @param message
	 */
	public static void toastShow(final Activity activity, final String message) {
		activity.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 将值设置到Intent里
	 * 
	 * @param intent
	 *            Inent对象
	 * @param key
	 * @param val
	 */
	public static void setValueToIntent(Intent intent, String key, Object val) {
		if (val instanceof Boolean)
			intent.putExtra(key, (Boolean) val);
		else if (val instanceof Boolean[])
			intent.putExtra(key, (Boolean[]) val);
		else if (val instanceof String)
			intent.putExtra(key, (String) val);
		else if (val instanceof String[])
			intent.putExtra(key, (String[]) val);
		else if (val instanceof Integer)
			intent.putExtra(key, (Integer) val);
		else if (val instanceof Integer[])
			intent.putExtra(key, (Integer[]) val);
		else if (val instanceof Long)
			intent.putExtra(key, (Long) val);
		else if (val instanceof Long[])
			intent.putExtra(key, (Long[]) val);
		else if (val instanceof Double)
			intent.putExtra(key, (Double) val);
		else if (val instanceof Double[])
			intent.putExtra(key, (Double[]) val);
		else if (val instanceof Float)
			intent.putExtra(key, (Float) val);
		else if (val instanceof Float[])
			intent.putExtra(key, (Float[]) val);
	}
}
