package com.movementinsome.kernel.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.movementinsome.R;
import com.movementinsome.caice.util.ActivityCollector;
import com.movementinsome.map.nearby.ToastUtils;

public class FullActivity extends AppCompatActivity {

//	private static Toast toast;
	private IntentFilter mIntentFilter;
	MediaPlayer mediaPlayer;
	Vibrator vibrator;

	private Snackbar snackbar;

	/*
	 * protected abstract void setContentView();
	 * 
	 * protected abstract void findViewById();
	 * 
	 * protected abstract void controll();
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * setContentView(); findViewById(); controll();
		 */
		/*
		 * requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
		//将activity添加
		ActivityCollector.addActivity(this);

		// 电池相关
		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
		mediaPlayer=MediaPlayer.create(this.getApplicationContext(), R.raw.push_message_1);;	//启动手机铃声
		Log.i("当前包名:", this.getComponentName().getClassName());
		mediaPlayer.setOnErrorListener(new OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra){
                    mediaPlayer.reset();
                    return false;
            }
		});
		vibrator= (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);;			//启动手机震动
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	@Override
	protected void onResume() {
		super.onResume(); 
		registerReceiver(mIntentReceiver, mIntentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause(); 
		unregisterReceiver(mIntentReceiver);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}

	/** 获取屏幕的宽度 */
	public int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public static void pToastShow(String showContent) {
//		if (toast == null) {
//			toast = Toast.makeText(AppContext.getInstance(), "", Toast.LENGTH_SHORT);
//		}
//		toast.setText(showContent);
//		toast.show();
		ToastUtils.show(showContent);
	}


	//叮当声音+震动
	public void showVibrator(Context context){
		if (null != mediaPlayer){
			mediaPlayer.stop();    
			mediaPlayer.release(); 
		}
		mediaPlayer=MediaPlayer.create(this.getApplicationContext(), R.raw.push_message_1);;	//启动手机铃声
		mediaPlayer.start();
		vibrator.vibrate(1500);
	}
	
	// 声明消息处理过程
	private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {  
                int level = intent.getIntExtra("level", 0);  
                int scale = intent.getIntExtra("scale", 100);  
                int power = level * 100 / scale;  
               // Log.d("Deom", "电池电量：:" + power);  
                if (power <=2){
                	pToastShow("电池电量只剩余"+String.valueOf(power)+"%,建议立即退出系统!");
                }
               // mBatteryView.setPower(power);  
            }  
		}
	};

	public void showSnackbar(String msg){
		if (snackbar==null){
			snackbar=Snackbar.make(getWindow().getDecorView(),msg,Snackbar.LENGTH_LONG);
		}

		//获取当前软件盘是否打开,打开则先隐藏在显示
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		//去掉虚拟按键
		getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //隐藏虚拟按键栏
						| View.SYSTEM_UI_FLAG_IMMERSIVE //防止点击屏幕时,隐藏虚拟按键栏又弹了出来
		);

		snackbar.setAction(getString(R.string.determine), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				snackbar.dismiss();
				//隐藏SnackBar时记得恢复隐藏虚拟按键栏,不然屏幕底部会多出一块空白布局出来,和难看
				getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
			}
		});
		snackbar.show();
	}

}
