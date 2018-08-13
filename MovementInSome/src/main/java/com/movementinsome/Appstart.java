package com.movementinsome;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.okhttp.OkHttpURL;
import com.movementinsome.database.vo.UserBeanVO;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.updateApk.UpdateApk;
import com.movementinsome.kernel.util.MD5;
import com.movementinsome.map.nearby.ToastUtils;
import com.movementinsome.sysmanager.init.IniCheck;
import com.movementinsome.sysmanager.init.SystemInitActivity;
import com.movementinsome.sysmanager.init.downloadoffmap.Downloadoffmap;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.Calendar;
import java.util.TimeZone;

import okhttp3.Call;

public class Appstart extends FullActivity {

	UpdateApk updateApk;
	private boolean isDownloadMap = false;
	private boolean isWaitDownloadMap = false;
	private static String _HTTP = "http://";
	private static String _APPCONTEXT = "/fisds";
	private TextView tv_version;

	private String version;

	private static final int GO_HOME = 0;//去主页
	private static final int GO_LOGIN = 1;//去登录页
	/**
	 * 跳转判断
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case GO_HOME://去主页
					Intent intent = new Intent(Appstart.this, Main.class);
					startActivity(intent);
					finish();
					break;
				case GO_LOGIN://去登录页
					Intent intent2 = new Intent(Appstart.this, Login.class);
					startActivity(intent2);
					finish();
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.appstart_activity);
		
		tv_version = (TextView) findViewById(R.id.tv_splash_version);
		version = getVersion();
		tv_version.setText("版本号  " + version);
		if (AppContext.getInstance().checkSysIniSuc()){

			if (!this.getIntent().getBooleanExtra("isAlarmStart", false)) {
	
				if (AppContext.getInstance().getAutoStart() != null) {
					if (!"".equals(AppContext.getInstance().getAutoStart()
							.getTiming())) {
						String[] time = AppContext.getInstance().getAutoStart()
								.getTiming().split("\\:");
						// 将时间设置为定时的时间
						Calendar calendar = Calendar.getInstance();// 代表当前时间的日历
						calendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
						calendar.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]));
						calendar.set(Calendar.MINUTE, Integer.valueOf(time[1]));
						calendar.set(Calendar.SECOND, 0);
						calendar.set(Calendar.MILLISECOND, 0);
	
						Intent intent = new Intent(this, AlarmReceiver.class);
						PendingIntent pi = PendingIntent.getBroadcast(this, 0,
								intent, 1);
	
						/***
						 * 获取全局定时器的服务管理器
						 */
						AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
						alarmManager.set(AlarmManager.RTC_WAKEUP,
								calendar.getTimeInMillis(), pi);
	
					}
				}
			}
			
			//run();
			if (AppContext.getInstance().getServerUrl() != null) {
				/*IniCheck.doCheckIni(AppContext.getInstance(), AppContext
						.getInstance().getIniConnSvr(), mHandler);*/
				new Thread(new Runnable() {
	
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String url = AppContext.getInstance().getServerUrl();
						url = url.substring(0, url.lastIndexOf('/'));
						mHandler.sendEmptyMessage(5);
						IniCheck.doCheckIni2(Appstart.this, url,mHandler);
						//url = _HTTP+url+_APPCONTEXT;
						if (IniCheck.sysChange) {
							IniCheck.upSysConfig(Appstart.this, url, mHandler);
						}
						if (IniCheck.xformChange) {
							IniCheck.upXform(Appstart.this, url, mHandler);
						}
						if (IniCheck.offlineChange) {
							mHandler.sendEmptyMessage(4);
							while (!isWaitDownloadMap) {
								try {
									Thread.sleep(2000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							isWaitDownloadMap = false;
							if (isDownloadMap) {
								IniCheck.upOfflineMap(Appstart.this, url, mHandler);
							}
						}
						if (IniCheck.dictChange) {
							IniCheck.upDictInfo(Appstart.this, url, mHandler);
						} else {
							mHandler.sendEmptyMessage(10);
						}
					}
				}).start();
			} else {
				run();
			}
		}else{
			run();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	
	@Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode,  data);
        if(requestCode==8888){  
        	if (resultCode==0){
        		run();
        	}
        } 
    }

	private void run() {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// 检查是否有最新版本
//				if (AppContext.getInstance().isSystemInit()) {
//					UserBeanVO userBeanVO=AppContext.getInstance().getCurUser();
//					if (userBeanVO!=null){
//						//这里使用用户名与用户id做判断条件,后台返回的密码为空
//						if (userBeanVO.getUserName()!=null&&AppContext.getInstance().getCurUserName()!=null){
//							if (userBeanVO.getUserName().equals(AppContext.getInstance().getCurUserName())){
////							startActivity(new Intent(Appstart.this, Main.class));
//								startMainActivity(userBeanVO.getUserName(),AppContext.getInstance().getUserPawword());
//							}else {
//								startActivity(new Intent(Appstart.this, Login.class));
//								Appstart.this.finish();
//							}
//						}else {
//							startActivity(new Intent(Appstart.this, Login.class));
//							Appstart.this.finish();
//						}
//					}else {
//						startActivity(new Intent(Appstart.this, Login.class));
//						Appstart.this.finish();
//					}
//				} else {
//					startActivity(new Intent(Appstart.this,
//							SystemInitActivity.class));
//					Appstart.this.finish();
//				}
				/* startActivity(new Intent (Appstart.this,Login.class)); */
//				Appstart.this.finish();
				// 检查是否有最新版本
				if (AppContext.getInstance().isSystemInit()) {
					startActivity(new Intent(Appstart.this, Login.class));
				} else {
					startActivity(new Intent(Appstart.this,
							SystemInitActivity.class));
				}
				/* startActivity(new Intent (Appstart.this,Login.class)); */
				Appstart.this.finish();
			}
		}, 1000);
	}

	private void startMainActivity(String userName,String password) {
		if (null != AppContext.getInstance().getServerUrl()) {
			String phoneIMEI=AppContext.getInstance().getPhoneIMEI();
			String url= OkHttpURL.serverUrl+ SpringUtil._REST_USERLOGIN
					+ "uuid=" + phoneIMEI
					+"&"
					+getString(R.string.appName)+"=" + getString(R.string.APP_NAME)
					+ "&userName=" + userName + "&password=" + MD5.getInstance().getMD5ofStr(password).toLowerCase();

			OkHttpUtils.post()
					.url(url)
					.build()
					.execute(new StringCallback() {
						@Override
						public void onError(Call call, Exception e, int id) {
							e.printStackTrace();
							startActivity(new Intent(Appstart.this, Login.class));
						}

						@Override
						public void onResponse(String response, int id) {
							if (response!=null&&!response.equals("")){
								Gson gson=new Gson();
								// 用户属性对象
								UserBeanVO userBean = gson.fromJson(response, UserBeanVO.class);
								if (userBean!=null&&userBean.getState().equals("1")){
									finish();
									startActivity(new Intent(Appstart.this, Main.class));
								}else {
									finish();
									startActivity(new Intent(Appstart.this, Login.class));
								}
							}
						}
					});
		}else {
			ToastUtils.show(getString(R.string.error_message2));
			startActivity(new Intent(Appstart.this, Login.class));
			Appstart.this.finish();
		}
	}

	public void doNewVersionUpdate(int state) {
		if (state == 1) {
			if (AppContext.getInstance().getAPP_MIN_CODE() != 0) {

				int minCode = AppContext.getInstance().getAPP_MIN_CODE();
				if (AppContext.getInstance().getVersionCode() < minCode) {
					if (updateApk != null) {
						updateApk.install();
						return;
					}
				}

			}
			new AlertDialog.Builder(this)
					.setTitle("提示")
					.setMessage("检查到本地有下载完成的最新版应用包，是否立刻安装升级？")
					.setNegativeButton("确定安装",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									// update();
									if (updateApk != null) {
										updateApk.install();
									}
								}
							})
					.setPositiveButton("删除应用包",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (updateApk != null) {
										updateApk.delLocalApk();
									}
									mHandler.sendEmptyMessage(12);
								}
							}).create().show();

		} else {
			if (AppContext.getInstance().getAPP_MIN_CODE() != 0) {

				int minCode = AppContext.getInstance().getAPP_MIN_CODE();
				if (AppContext.getInstance().getVersionCode() < minCode) {
					if (updateApk != null) {
						updateApk.download("");
						return;
					}
				}
			}
			String str = updateApk.getUpdateMessage();
			final String title = "下载主程序";
			new AlertDialog.Builder(this)
					.setTitle("软件更新")
					.setMessage(str.replace(";", "\n"))
					// 设置内容
					.setNegativeButton("立即更新",// 设置确定按钮
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if (updateApk != null) {
										updateApk.download("");
									}
								}
							})
					.setPositiveButton("暂不更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									run();
								}
							}).show();// 创建
		}
	}
	Handler mHandler = new Handler() {
		ProgressDialog progress;
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:// 更新离线地图
				int downloadedAllSize = msg.getData().getInt("size");
				progress.setProgress(downloadedAllSize);
				int result = (int) ((float) downloadedAllSize / progress.getMax() * 100);
				progress.setMessage(result + "%%");
				if (progress.getMax() == progress.getProgress()) {
					if (progress != null) {
						progress.dismiss();
					}
				}
				break;
			case 2:
				Toast.makeText(Appstart.this, "更新异常!", Toast.LENGTH_LONG).show();
				if (progress != null) {
					progress.dismiss();
				}
				break;
			case 3:// 开始更新离线地图
				progress = new ProgressDialog(Appstart.this);
				progress.setTitle("正在更新离线地图...");
				progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				progress.setCancelable(false);
				progress.setCanceledOnTouchOutside(false);
				int fileSize = msg.getData().getInt("fileSize");
				progress.setMax(fileSize);
				progress.setButton("取消下载", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Downloadoffmap.isPause = true;
						//new File(downPath + downApkName).delete();
					}
				});
				progress.show();
				break;
			case 4:
				if(IniCheck.listOfflineNameUpdate.size() == 0){
					isWaitDownloadMap = true;
					isDownloadMap = false;
					break;
				}
				final String[] items = new String[IniCheck.listOfflineNameUpdate.size()];
				
				for(int i=0;i<items.length;++i){
					items[i]=IniCheck.listOfflineNameUpdate.get(i);
				}
				IniCheck.setOfflineFile(IniCheck.listOfflineFileUpdate.get(0));
				
				AlertDialog.Builder builder = new Builder(Appstart.this); 
					builder.setTitle("有新的离线地图，是否更新？")
					.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							IniCheck.setOfflineFile(IniCheck.listOfflineFileUpdate.get(which));
						}
					}).setPositiveButton("取消",  new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							isWaitDownloadMap = true;
							isDownloadMap = false;
							/*for(String fileName : IniCheck.listOfflineFileUpdate){
								IniCheck.rePreferencePara(Appstart.this, "offline_"+fileName);
							}*/
						}
					})
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Downloadoffmap.isPause = false;
							isWaitDownloadMap = true;
							isDownloadMap = true;
						}
					});
				AlertDialog alertDialog = builder.create();
				alertDialog.setCancelable(false);
				alertDialog.show();
				break;
			case 5:// 下载更新配置文件
				progress = new ProgressDialog(Appstart.this);
				progress.setCancelable(false);
				progress.setCanceledOnTouchOutside(false);
				progress.setMessage("正在下载更新文件");
				progress.show();
				break;
			case 6:
				progress.dismiss();
				Toast.makeText(Appstart.this, msg.getData().getString("msg"), Toast.LENGTH_LONG).show();
				break;
			case 7:
				progress = new ProgressDialog(Appstart.this);
				progress.setCancelable(false);
				progress.setCanceledOnTouchOutside(false);
				progress.setMessage("正在更新系统配置");
				progress.show();
				break;
			case 8:
				progress = new ProgressDialog(Appstart.this);
				progress.setCancelable(false);
				progress.setCanceledOnTouchOutside(false);
				progress.setMessage("正在更新表单配置");
				progress.show();
				break;
			case 9:
				progress = new ProgressDialog(Appstart.this);
				progress.setCancelable(false);
				progress.setCanceledOnTouchOutside(false);
				progress.setMessage("正在更新用户数据");
				progress.show();
				break;
			case 10:
				if (progress != null) {
					progress.dismiss();
				}
				updateApk = new UpdateApk(Appstart.this, false, AppContext
						.getInstance().getServerUrl());
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						updateApk();
					}
					
				}).start();
				break;
			case 11:
				int extState = msg.arg1;
				doNewVersionUpdate(extState);
				break;
			case 12:
				run();
				break;
			}	
		}
	};
	private void updateApk(){
		int extState = updateApk.existsUpdate();
		if (extState > 0) {
			Message msg = new Message();
			msg.arg1 = extState;
			msg.what = 11;
			mHandler.sendMessage(msg);
		} else {
			Message msg = new Message();
			msg.what = 12;
			mHandler.sendMessage(msg);
		}
	}
	
	private String getVersion()
	{
		try
		{
			PackageManager packageManager = getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			
			return packageInfo.versionName;
		}
		catch (NameNotFoundException e)
		{
			e.printStackTrace();
			return "版本号未知";
		}
	}
}