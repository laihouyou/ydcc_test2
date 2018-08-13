package com.movementinsome.kernel.activity;


import android.app.TabActivity;
import android.content.Context;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.DisplayMetrics;

import com.movementinsome.R;

public class FullTabActivity extends TabActivity {

	MediaPlayer mediaPlayer;
	Vibrator vibrator;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
		/*
		 * requestWindowFeature(Window.FEATURE_NO_TITLE);
		 * setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		 * getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);
		 */
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
	}

	/** 获取屏幕的宽度 */
	public int getWindowsWidth() {
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}
	
	public void showVibrator(Context context){
		if (null != mediaPlayer){
			mediaPlayer.stop();    
			mediaPlayer.release(); 
		}
		mediaPlayer=MediaPlayer.create(this.getApplicationContext(), R.raw.push_message_1);;	//启动手机铃声
		mediaPlayer.start();
		vibrator.vibrate(1500);
	}
}
