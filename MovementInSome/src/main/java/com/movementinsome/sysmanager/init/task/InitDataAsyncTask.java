package com.movementinsome.sysmanager.init.task;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.Login;
import com.movementinsome.sysmanager.init.IniCheck;
import com.movementinsome.sysmanager.init.SystemInitActivity;
import com.movementinsome.sysmanager.init.downloadoffmap.Downloadoffmap;

public class InitDataAsyncTask extends AsyncTask<String, Void, String> {

	//private SpringUtil upload;
	private SystemInitActivity context;
	private List<String> listLog;
	private ProgressDialog progress;
	private boolean isDownloadMap = false;
	private boolean isWaitDownloadMap = false;
	private Button sysInitBtnInit;
	private Button sysInitBtnClear;
	//private SharedPreferences sharedPrefs; //推送信息储存

	public InitDataAsyncTask(SystemInitActivity context, Button sysInitBtnInit, Button sysInitBtnClear) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.sysInitBtnInit = sysInitBtnInit;
		this.sysInitBtnClear = sysInitBtnClear;
		listLog = new ArrayList<String>();
		listLog.add("正在连接下载数据……");
		showLog();
	}

	@SuppressWarnings("static-access")
	@Override
	protected String doInBackground(String... params) {
		try {
			boolean doOk = false;
			msgSend("系统配置下载……", 1);
			doOk = IniCheck.doByStep(context,params[0],0,mHandler);
			if (doOk){
				msgSend("系统配置下载成功……", 3);
			}else{
				msgSend("系统配置下载失败……", 2);
			}
			msgSend("系统参数下载……", 1);
			doOk = IniCheck.doByStep(context,params[0],1,mHandler);
			if (doOk){
				msgSend("系统参数下载成功……", 3);
			}else{
				msgSend("系统参数下载失败……", 2);
			}
			msgSend("离线地图下载", 1);
			mHandler.sendEmptyMessage(4);
			while(!isWaitDownloadMap){
				Thread.sleep(2000);
			}
			isWaitDownloadMap = false;
			if(isDownloadMap){
				doOk = IniCheck.doByStep(context, params[0], 3,mHandler);
				if (doOk){
					msgSend("离线地图下载成功……",3);
				}else{
					msgSend("离线地图下载失败……",  2);
				}
			}else{
				msgSend("已取消下载离线地图……",  3);
			}
			
			msgSend("功能模板下载……", 1);
			doOk = IniCheck.doByStep(context,params[0],2,mHandler);
			if (doOk){
				msgSend("功能模板下载成功……", 3);
			}else{
				msgSend("功能模板下载失败……", 2);
			}
			
			msgSend("数据字典下载……", 1);
			doOk = IniCheck.doByStep(context,params[2],4,mHandler);
			if (doOk){
				msgSend("数据字典下载成功……", 3);
				AppContext.getInstance().setSystemInit(true);
				context.finish();
			}else{
				AppContext.getInstance().setSystemInit(false);
				msgSend("数据字典下载失败……", 2);
			}
			msgSend("初始化完成……", 4);
		} catch (Exception e) {
			// TODO: handle exception
			msgSend("检测到<初始化数据>不完整,或者不符合要求,系统已停止存储", 2);
		}finally{
			//结束事务
			//GddstApplication.ZZDB.getDb().endTransaction();
		}
		return null;
	}

	public void cancel(){
		cancel(true);
	}


	public final static int MSG =  90001;
	public final static int GONE =  90002;
	public final static int ENABLED =  90003;
	public final static int LOGIN = 90004;
	
	private Handler myHadler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case GONE:
				context.sysInitEtUrl.setEnabled(true);
				context.sysInitEtUrl_P.setEnabled(true);
				context.sysInitBtnBack.setEnabled(true);
				context.sysInitBtnInit.setEnabled(true);
				context.sysInitBtnClear.setEnabled(true);
				context.sysInitLinearHint.setVisibility(View.GONE);
				context.sysInitBtnBack.setTextColor(Color.argb(200, 255, 255, 255));
				context.sysInitBtnInit.setTextColor(Color.argb(200, 255, 255, 255));
				context.sysInitBtnClear.setTextColor(Color.argb(200, 255, 255, 255));
			case MSG:
				showLog();
				break;
			case ENABLED:
				sysInitBtnInit.setTextColor(Color.rgb(200, 200, 200));
				sysInitBtnInit.setEnabled(false);
				sysInitBtnClear.setTextColor(Color.rgb(200, 200, 200));
				sysInitBtnClear.setEnabled(false);
				context.sysInitBtnBack.setTextColor(Color.rgb(200, 200, 200));
				context.sysInitBtnBack.setEnabled(false);
				break;
			case LOGIN:
				context.startActivity(new Intent (context,Login.class));
				context.finish();
			default:
				break;
			}
		};
	};
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
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
				Toast.makeText(context, "下载异常!", Toast.LENGTH_LONG).show();
				if (progress != null) {
					progress.dismiss();
				}
				break;
			case 3:// 初始化控件
				progress = new ProgressDialog(context);
				progress.setTitle("正在下载离线地图...");
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
				if(IniCheck.listOfflineName.size() == 0){
					isWaitDownloadMap = true;
					isDownloadMap = false;
					break;
				}
				final String[] items = new String[IniCheck.listOfflineName.size()];
				
				for(int i=0;i<items.length;++i){
					items[i]=IniCheck.listOfflineName.get(i);
				}
				IniCheck.setOfflineFile(IniCheck.listOfflineFile.get(0));
				AlertDialog.Builder builder = new Builder(context); 
					builder.setTitle("离线地图选择")
					.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							IniCheck.setOfflineFile(IniCheck.listOfflineFile.get(which));
						}
					}).setPositiveButton("取消",  new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							isWaitDownloadMap = true;
							isDownloadMap = false;
							/*for(String fileName : IniCheck.listOfflineFileUpdate){
								IniCheck.rePreferencePara(context, "offline_"+fileName);
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
					/*.setItems(items, new DialogInterface.OnClickListener() {
							@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
								isDownloadMap = true;
								IniCheck.setOfflineFile(IniCheck.listOfflineFile.get(which));
							}
						});*/
				AlertDialog alertDialog = builder.create();
				alertDialog.setCancelable(false);
				alertDialog.show();
				break;
			}	
			
		}
	};

	private void msgSend(String strLog,int what){
		listLog.add(strLog);
		Message msg = new Message();
		if(what == 1){
			msg.what = MSG;
		}else if(what == 2){
			msg.what = GONE;
		}else if(what == 3){
			msg.what = ENABLED;
		}else if(what == 4){
			msg.what = LOGIN;
		}
		myHadler.sendMessage(msg);
	}


	private void showLog(){
		try {
			String strLog = "";
			for (String str : listLog) {
				strLog += str + "\n";
			}
			context.sysInitTvMsg.setText(strLog);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
