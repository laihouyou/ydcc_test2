package com.movementinsome.kernel.email;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.movementinsome.AppContext;

import android.os.Environment;

public class SystemLog {
	
	public static void sendLog(){
		new SystemLogAnalysis(null);
	}
	
	public static void addLog(String text){
		File file = new File(Environment.
				getExternalStorageDirectory().getAbsolutePath()+"/mapApp/gddstAppLog.txt");
		try {
			FileOutputStream fos = new FileOutputStream(file,true);
			fos.write(text.getBytes());
			fos.write(("\t\t\t\n用户名称：" + AppContext.getInstance().getCurUserName()+";").getBytes());
			fos.write(("\t\n版本编号：" + AppContext.getInstance().getVersionName()+";").getBytes());
			fos.write(("\t\n机器编码：" + AppContext.getInstance().getPhoneIMEI()+";").getBytes());
			fos.write(("\t\n服务地址：" + AppContext.getInstance().getServerUrl()+";").getBytes());
			fos.write("\t\n".getBytes());
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
