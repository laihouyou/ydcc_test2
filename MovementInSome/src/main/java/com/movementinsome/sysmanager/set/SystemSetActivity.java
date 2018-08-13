package com.movementinsome.sysmanager.set;


import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SystemSetActivity extends FullActivity implements OnClickListener ,OnCheckedChangeListener{

	private ToggleButton systemBtnWifi;		//无线网络
	private ToggleButton systemBtnAPN;		//apn设置
	private ToggleButton systemBtnGPS;		//gps定位
	private ToggleButton systemBtnEndCall;	//屏蔽电话
	private ToggleButton systemBtnLight;	//自动调整亮度
	private TextView systemTvGpsNumber;		//卫星颗数
	private Button systemUpdateApk;			//更新升级
	private Button systemBtnGpsUpdata;		//更新gps信息
	private Button systemInitData;			//初始化数据
	private Button systemUpdateData;		//清除所有数据
	private Button systemRestnet;			//设置内网
	private Button systemRestOuterNet;		//设置外网
	private ImageView systemImageBack;		//返回
	
	private LinearLayout systemInitDataLay;//初始化数据容器
	private LinearLayout systemUpdateDataLay;//清除所有数据容器
	private List<ToggleButton> listTBtn;	//ToggleButton数组
	private SeekBar systemSeekbar;			//调整亮度
	private ContentResolver resolver;
	private final int MAXIMUM_BACKLIGHT = 255;
	private WifiManager wifimanager;
	private ConnectivityManager cm;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_set_activity);
		resolver = this.getContentResolver();
		wifimanager = (WifiManager)getSystemService("wifi");
		cm = (ConnectivityManager)getSystemService("connectivity");
		getHandle();
		setData();
	}

	/**
	 * 获取控件句柄
	 */
	public void getHandle(){

		systemTvGpsNumber = (TextView)findViewById(R.id.systemTvGpsNumber);
		systemSeekbar = (SeekBar)findViewById(R.id.systemSeekbar);
		systemImageBack = (ImageView)findViewById(R.id.systemImageBack);

		systemUpdateApk = (Button)findViewById(R.id.systemUpdateApk);
		systemInitData = (Button)findViewById(R.id.systemInitData);
		systemUpdateData = (Button)findViewById(R.id.systemUpdateData);
		systemBtnGpsUpdata = (Button)findViewById(R.id.systemBtnGpsUpdata);
		systemRestnet = (Button)findViewById(R.id.systemRestnet);
		systemRestOuterNet = (Button)findViewById(R.id.systemRestOuterNet);

		systemBtnGPS = (ToggleButton)findViewById(R.id.systemBtnGPS);
		systemBtnAPN = (ToggleButton)findViewById(R.id.systemBtnAPN);
		systemBtnWifi = (ToggleButton)findViewById(R.id.systemBtnWifi);
		systemBtnLight = (ToggleButton)findViewById(R.id.systemBtnLight);
		systemBtnEndCall = (ToggleButton)findViewById(R.id.systemBtnEndCall);
		
		systemInitDataLay=(LinearLayout) findViewById(R.id.systemInitDataLay);//初始化数据容器
		systemUpdateDataLay=(LinearLayout) findViewById(R.id.systemUpdateDataLay);//清除所有数据容器


		listTBtn = new ArrayList<ToggleButton>();
		listTBtn.add(systemBtnGPS);
		listTBtn.add(systemBtnAPN);
		listTBtn.add(systemBtnWifi);
		listTBtn.add(systemBtnLight);
		listTBtn.add(systemBtnEndCall);

		//权限设定
		for(ToggleButton tBtn:listTBtn){
			tBtn.setOnClickListener(this);
/*			if(permissionUser(tBtn.getId())){
				//匹配成功
				tBtn.setEnabled(true);
				tBtn.setTextColor(Color.argb(200, 30, 30, 30));
			}else{
				//匹配失败（无权限）
				tBtn.setEnabled(false);
				tBtn.setTextColor(Color.argb(100, 30, 30, 30));
			}*/
		}

		systemImageBack.setOnClickListener(this);
		systemUpdateApk.setOnClickListener(this);
		systemUpdateData.setOnClickListener(this);
		systemBtnGpsUpdata.setOnClickListener(this);
		systemRestnet.setOnClickListener(this);
		systemRestOuterNet.setOnClickListener(this);
		systemInitData.setOnClickListener(this);

/*		if("".equals(MyPublicData.gpsNumber)){
			systemTvGpsNumber.setText("未找到卫星");
		}else{
			systemTvGpsNumber.setText("找到" + MyPublicData.gpsNumber + "颗");
		}
		
		if("admin".equals(pPrefere.getString("userName", ""))){
			systemUpdateDataLay.setVisibility(View.VISIBLE);
		}else{
			systemUpdateDataLay.setVisibility(View.GONE);
		}*/

	}

	/**
	 * 设置初始值
	 */
	public void setData() {


		//判断自动调节亮度是否开启
		if(isAutoBrightness(resolver)){
			systemBtnLight.setChecked(true);
			systemSeekbar.setVisibility(View.GONE);
		}else{
			systemBtnLight.setChecked(false);
			systemSeekbar.setVisibility(View.VISIBLE);
			systemSeekbar.setMax(MAXIMUM_BACKLIGHT-30);
			systemSeekbar.setProgress(getScreenBrightness()-30);
		}

		systemBtnLight.setOnCheckedChangeListener(this);
		systemSeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromsystem) {
				// TODO Auto-generated method stub
				if(fromsystem){
					if(progress < 10){
						progress = 9;
					}
					setBrightness(progress);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		//判断无线网络是否开启，设置无线网络开关初始值
		if(wifimanager.isWifiEnabled()){
			systemBtnWifi.setChecked(true);
		}else{
			systemBtnWifi.setChecked(false);
		}

		//判断gps是否打开，设置gps开关初始值
		boolean gpsEnabled = Settings.Secure.isLocationProviderEnabled( getContentResolver(), LocationManager.GPS_PROVIDER );
		systemBtnGPS.setChecked(gpsEnabled);

		//判断apn是否开启，设置apn开关初始值
		systemBtnAPN.setChecked(gprsIsOpenMethod("getMobileDataEnabled"));

		//显示是否禁止了拨打电话
		//systemBtnEndCall.setChecked(pPrefere.getBoolean("flgEndCall", true));
	}

	/**
	 * 判断是否开启了自动亮度调节
	 */
	public boolean isAutoBrightness(ContentResolver aContentResolver) {
		boolean automicBrightness = false;
		try {
			automicBrightness = Settings.System.getInt(aContentResolver,
					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return automicBrightness;
	}

	/**
	 * 获取屏幕的亮度
	 */
	public int getScreenBrightness() {
		int nowBrightnessValue = 0;
		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}

	/**
	 * 设置亮度
	 */
	public void setBrightness( int brightness) {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.screenBrightness = (float) (brightness / (MAXIMUM_BACKLIGHT * 1.0));
		//        Float.valueOf(brightness) * (1f / 255f); 
		getWindow().setAttributes(lp);
		Uri uri = android.provider.Settings.System
				.getUriFor("screen_brightness");
		android.provider.Settings.System.putInt(resolver, "screen_brightness",
				brightness);
		resolver.notifyChange(uri, null);
	}

	/**
	 * 停止自动亮度调节
	 */
	public void stopAutoBrightness() {
		Settings.System.putInt(resolver,
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	/**
	 * 开启亮度自动调节
	 * @param activity
	 */
	public void startAutoBrightness() {
		Settings.System.putInt(resolver,
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}

	/**
	 * 设置gps启闭状态
	 */
	private void toggleGPS() {
		Intent gpsIntent = new Intent();
		gpsIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		gpsIntent.addCategory("android.intent.category.ALTERNATIVE");
		gpsIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(this, 0, gpsIntent, 0).send();
		}catch (CanceledException e) {
			e.printStackTrace();
			pToastShow("GPS打开失败");
		}
	}

	/**
	 * 打开或关闭GPRS 
	 */
	private boolean gprsEnabled(boolean bEnable) { 

		boolean isOpen = gprsIsOpenMethod("getMobileDataEnabled"); 
		if(isOpen == !bEnable) { 
			setGprsEnabled("setMobileDataEnabled", bEnable); 
		} 

		return isOpen;   
	} 

	/**
	 * 检测GPRS是否打开 
	 */
	private boolean gprsIsOpenMethod(String methodName) { 
		@SuppressWarnings("rawtypes")
		Class cmClass       = cm.getClass(); 
		@SuppressWarnings("rawtypes")
		Class[] argClasses  = null; 
		Object[] argObject  = null; 

		Boolean isOpen = false; 
		try { 
			Method method = cmClass.getMethod(methodName, argClasses); 

			isOpen = (Boolean) method.invoke(cm, argObject); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 

		return isOpen; 
	} 

	/**
	 * 开启/关闭GPRS 
	 */
	private void setGprsEnabled(String methodName, boolean isEnable) { 
		@SuppressWarnings("rawtypes")
		Class cmClass       = cm.getClass(); 
		@SuppressWarnings("rawtypes")
		Class[] argClasses  = new Class[1]; 
		argClasses[0]       = boolean.class; 

		try { 
			Method method = cmClass.getMethod(methodName, argClasses); 
			method.invoke(cm, isEnable); 
		} catch (Exception e) { 
			e.printStackTrace(); 
		} 
	} 

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.systemUpdateApk://升级更新
			String url;
/*			if(MyPublicData.INTRANET.equals(pPrefere.getString("Net", MyPublicData.OUTERNET))){
				url = pmapPrefere.getString("dataInteractionRest", "");//获取中间层url
			}else{
				url = pmapPrefere.getString("dataInteractionRestForeign", "");//获取中间层url
			}
			new UpdateApk(this,true,url,false);*/
			break;
		case R.id.systemBtnEndCall:
			pToastShow("系统默认值，无修改权限");
			systemBtnEndCall.setChecked(!systemBtnEndCall.isChecked());
			break;
		case R.id.systemBtnWifi://无线网络设置
			boolean wifistate = wifimanager.setWifiEnabled(systemBtnWifi.isChecked());
			if(wifistate){
				pToastShow("无线网络设置成功");
			}else{
				pToastShow("设置无线网络错误，请检查");
				systemBtnWifi.setChecked(wifimanager.isWifiEnabled());
			}
			break;
		case R.id.systemBtnGPS://gps设置
			toggleGPS();
			if(systemBtnGPS.isChecked()){
				pToastShow("gps定位开启");
			}else{
				pToastShow("gps定位关闭");
			}
			break;
		case R.id.systemBtnAPN://APN设置
			gprsEnabled(systemBtnAPN.isChecked());
			if(systemBtnAPN.isChecked()){
				pToastShow("APN开启");
			}else{
				pToastShow("APN关闭");
			}
			break;
		case R.id.systemImageBack:
			this.finish();
			break;
		case R.id.systemUpdateData:	//初始化系统数据
			systemDataInitiaDialog("警告");
			break;
		case R.id.systemBtnGpsUpdata:
/*			if("".equals(MyPublicData.gpsNumber)){
				systemTvGpsNumber.setText("刷新：未找到卫星");
			}else{
				systemTvGpsNumber.setText("刷新：找到" + MyPublicData.gpsNumber + "颗");
			}*/
			break;
/*		case R.id.systemRestnet:
			pEditor.putString("Net", MyPublicData.INTRANET);
			pEditor.commit();
			pToastShow("设置内网成功，请重新登录");
			break;
		case R.id.systemRestOuterNet:
			pEditor.putString("Net", MyPublicData.OUTERNET);
			pEditor.commit();
			pToastShow("设置外网成功，请重新登录");
			break;*/
		case R.id.systemInitData:
			reInitDialog();
			break;
		default:
			break;
		}

	}


	private void systemDataInitiaDialog(final String titiel){
		new AlertDialog.Builder(this).setTitle(titiel)
		.setMessage("是否要重新初始化系统,本操作将会清除所有数据和任务" +
				"(包括未完成和当前工作中),本操作所带来的后果由操作者负责,请谨慎操作……")
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).setNegativeButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if("警告".equals(titiel)){
							systemDataInitiaDialog("再次警告");
						}else{
							//delData();
						}
					}
				}).create().show();
	}


	/**
	 * 清空系统数据
	 */
/*	private void delData(){
		try {
			this.finish();
			//删除文件夹
			MyPublicData.deleteImage(pScardzzSystem + pPrefere.getString("userName", ""));

			//删除数据库
			GddstApplication.ZZDB.deleteDataBase();
			GddstApplication.ZZDB = null;

			//清除SharedPreferences
			pEditor.clear();
			pEditor.commit();
			pmapEditor.clear();
			pmapEditor.commit();
			wxEditor.clear();
			wxEditor.commit();

			Intent first = new Intent(getApplicationContext(),FirstActivity.class);
			startActivity(first);

		} catch (Exception e) {
			// TODO: handle exception
		}
	}*/

	/**
	 * 重新初始化
	 */
	private void reInitDialog(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setMessage("是否要重新初始化？")
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
/*				GddstApplication.ZZDB.deleteInit();
				pEditor.clear();
				pEditor.commit();
				pmapEditor.clear();
				pmapEditor.commit();

				Intent first = new Intent(getApplicationContext(),FirstActivity.class);
				startActivity(first);*/
			}
		})
		.setPositiveButton("取消", null)
		.show();
	}

	/**
	 * 更改屏幕亮度点击事件
	 */
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub

		switch (buttonView.getId()) {
		case R.id.systemBtnLight:
			if(systemBtnLight.isChecked()){	//开启
				startAutoBrightness();
				systemSeekbar.setVisibility(View.GONE);
			}else{							//关闭
				stopAutoBrightness();
				systemSeekbar.setVisibility(View.VISIBLE);
				systemSeekbar.setMax(MAXIMUM_BACKLIGHT-30);
				systemSeekbar.setProgress(getScreenBrightness()-30);
				setBrightness(getScreenBrightness());
			}
			break;

		default:
			break;
		}
	}

}
