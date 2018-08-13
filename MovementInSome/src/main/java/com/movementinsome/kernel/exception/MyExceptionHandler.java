package com.movementinsome.kernel.exception;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.movementinsome.AppContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyExceptionHandler implements UncaughtExceptionHandler {

	private static MyExceptionHandler mHandler;
	private static Context mContext;
	private String[] build = new String[]{"device","display"};//"model","fingerprint"
	private String userName;// 用户名称
	private String versionName;// 版本编号
	private String phoneIMEI;// 机器编码
	private MyExceptionHandler(String userName,String versionName,String phoneIMEI){
		this.userName=userName;
		this.versionName=versionName;
		this.phoneIMEI=phoneIMEI;
	}
	public synchronized static MyExceptionHandler getInstance(Context context,String userName,String versionName,String phoneIMEI){
		if(mHandler==null){
			mHandler = new MyExceptionHandler(userName,versionName,phoneIMEI);
			mContext = context;
		}
		return mHandler;
	}



	@Override
	public void uncaughtException(Thread arg0, Throwable ex) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			Log.e("error",pw.toString());
			File file = new File(Environment.
					getExternalStorageDirectory().getAbsolutePath()+"/mapApp/gddstAppLog.txt");
			FileOutputStream fos = new FileOutputStream(file,true);
			fos.write(("时间:"+new SimpleDateFormat("yyyy年MM月dd日HH时mm分").format(new Date())+"\t").getBytes());
			fos.flush();

			fos.write((sw.toString()).getBytes());
			fos.flush();

			// 获取手机的版本信息
			fos.write(("\t\t\t\n用户名称：" + userName+";").getBytes());
			fos.write(("\t\n版本编号：" + versionName+";").getBytes());
			fos.write(("\t\n机器编码：" + phoneIMEI+";").getBytes());
			fos.write(("\t\n服务地址：" + AppContext.getInstance().getServerUrl()+";").getBytes());
			fos.flush();
			Field[] fields = Build.class.getFields();
			for (Field field : fields) {
				field.setAccessible(true); // 暴力反射
				String key = field.getName();
				String value = field.get(null).toString();
				for(int i = 0 ;i< build.length;i++){
					if(build[i].equals(key.toLowerCase().trim())){
						fos.write(("\t\n" + key +"：" + value + ";").getBytes());
						fos.flush();
					}
				}
			}
			fos.close();
			Toast.makeText(mContext.getApplicationContext(), "系统出现异常", Toast.LENGTH_SHORT).show();
			android.os.Process.killProcess(android.os.Process.myPid());
			//启动错误发送服务功能

			/*new Thread(){public void run() {
				Looper.prepare();
				CustomToast toast = new CustomToast(mContext);
				toast.show("系统出现异常，请先退出后再重新登录使用", -1);
				Looper.loop();
			};}.start();
			new Thread(){
				public void run() {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					android.os.Process.killProcess(android.os.Process.myPid());
				};
			}.start();*/
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
