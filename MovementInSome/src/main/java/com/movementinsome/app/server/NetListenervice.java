package com.movementinsome.app.server;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.movementinsome.AppContext;
import com.movementinsome.Main;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.NotificationUtils;
import com.movementinsome.kernel.util.WebAccessTools;

import java.util.Timer;
import java.util.TimerTask;

public class NetListenervice extends Service{

	private Timer timer;// 定时器
	public static final String ANDROID_CHANNEL_ID = "com.baidu.baidulocationdemo";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		if (Build.VERSION.SDK_INT >= 26){

			NotificationUtils mNotificationUtils = new NotificationUtils(this);

			Notification notification=mNotificationUtils.getAndroidChannelNotification
					("后台定位功能","正在后台定位")
					.build();

			startForeground(1, notification);
		}else {
			//获取一个Notification构造器
			Intent nfIntent = new Intent(AppContext.getInstance(),Main.class );
			Notification notification = new Notification.Builder(AppContext.getInstance())
					.setContentIntent(PendingIntent
							.getActivity(
									AppContext.getInstance(),
									0,
									nfIntent,
									0)) // 设置PendingIntent
					.setContentTitle("后台定位功能") // 设置下拉列表里的标题
					.setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
					.setContentText("正在后台定位") // 设置上下文内容
					.setWhen(System.currentTimeMillis()).build(); // 设置该通知发生的时间

			startForeground(1, notification);
		}

		timer = new Timer();
		final String url=AppContext.getInstance().getServerUrl();
		final String imei=AppContext.getInstance().getPhoneIMEI();
		final String userName=AppContext.getInstance().getCurUserName();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				WebAccessTools access = new WebAccessTools(NetListenervice.this);
				access.getWebContent(url+SpringUtil._REST_HEARTBEAT+"?uuid="+userName+"_"+imei);
			}
		}, 0, 20000);
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null)
			timer.cancel();
	}

}
