package com.movementinsome.kernel.email;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.movementinsome.kernel.email.model.MailSenderInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;


public class SystemLogAnalysis  extends AsyncTask<String, Void, Boolean>{

	private String sendContent = "";
	private File file;
	private String title;

	public SystemLogAnalysis(String sendContent){
		if(TextUtils.isEmpty(sendContent)){
			//如果SD卡存在
			file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/mapApp/gddstAppLog.txt");
			if(file.exists()){
				this.sendContent = readRecord();
				if(this.sendContent.length() > 0){
					this.title = "异常报告";
					execute("");
				}
			}
		}else{
			this.title = "意见反馈";
			this.sendContent = sendContent;
			execute("");
		}
	}


	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		MailSenderInfo mailInfo = new MailSenderInfo();
		mailInfo.setMailServerHost("smtp.163.com");
		mailInfo.setMailServerPort("25");
		mailInfo.setValidate(true);
		mailInfo.setUserName("gddstydce@163.com");
		mailInfo.setPassword("gddst89225300");
		mailInfo.setFromAddress("gddstydce@163.com");
		mailInfo.setToAddress("gddstydce@163.com");
		mailInfo.setSubject(title);
		mailInfo.setContent(sendContent);
		SimpleMailSender sms = new SimpleMailSender();
		boolean flgsend = sms.sendTextMail(mailInfo);
		return flgsend;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(file != null && file.exists()){
			file.delete();
		}
	}

	//获取异常文件流
	private String readRecord(){
		StringBuffer sb = new StringBuffer("");
		try {
			FileInputStream fin = new FileInputStream(file.getAbsolutePath());
			BufferedReader br = new BufferedReader(new InputStreamReader(fin));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return sb.toString();
	}
}
