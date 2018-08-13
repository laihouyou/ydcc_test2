package com.movementinsome.app.bizcenter;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.gpswidget.GpsViewActivity;
import com.movementinsome.kernel.util.MD5;
import com.movementinsome.sysmanager.init.IniCheck;
import com.movementinsome.sysmanager.init.downloadoffmap.Downloadoffmap;
import com.movementinsome.sysmanager.upwd.UpwdActivity;

import java.io.File;

public class MainMineActivity extends ContainActivity implements OnClickListener{

	private LinearLayout mine_rmaps;// 离线地图下载
	private LinearLayout mine_pwd;// 更改密码
	private LinearLayout mine_sat;// 卫星信号
	private LinearLayout mine_msg_rd;// 消息记录
	private LinearLayout mine_tb_rd;// 表单记录
	private LinearLayout mine_submit_rd;//提交记录
	private static boolean isWaitDownloadMap = false;
	private static String offlineFile;
	private static String offlineWifi;//离线地图
	private static String offlineFoce;
	private static String _DOWNLOAD_LOCALMAP_URL = "/download/LocalMap";
	private static ProgressDialog progress;
	private static boolean isDownloadMap = false;
	private boolean isNews;
	private TextView mine_name;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_mine_activity);
		
		mine_name = (TextView) findViewById(R.id.mine_name);
		String name = AppContext.getInstance().getCurUserName();
		mine_name.setText(name);
		mine_rmaps = (LinearLayout) findViewById(R.id.mine_rmaps);// 离线地图下载
		mine_rmaps.setOnClickListener(this);
		mine_pwd = (LinearLayout) findViewById(R.id.mine_pwd);// 更改密码
		mine_pwd.setOnClickListener(this);
		mine_sat = (LinearLayout) findViewById(R.id.mine_sat);// 卫星信号
		mine_sat.setOnClickListener(this);
		mine_msg_rd = (LinearLayout) findViewById(R.id.mine_msg_rd);// 消息记录
		mine_msg_rd.setOnClickListener(this);
		mine_tb_rd = (LinearLayout) findViewById(R.id.mine_tb_rd);// 表单记录
		mine_tb_rd.setOnClickListener(this);
		mine_submit_rd = (LinearLayout) findViewById(R.id.mine_submit_rd);//提交记录
		mine_submit_rd.setOnClickListener(this);
		if(AppContext.getInstance().getReportHistory()==null){
			mine_submit_rd.setVisibility(View.GONE);
		}else{
			mine_submit_rd.setVisibility(View.VISIBLE);
		}
	}
	
	/**
	 * 再次点击退出应用
	 */
	private long mExitTime;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
/*			if (side_drawer.isMenuShowing()
					|| side_drawer.isSecondaryMenuShowing()) {
				side_drawer.showContent();
			} else {*/
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					//(new UserLogoutAsyncTask()).execute(null,null,null);
					finish();
					// System.exit(0);
				}
			//}
			return true;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mine_rmaps:// 离线地图下载
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mHandler.sendEmptyMessage(4);
					while(!isWaitDownloadMap){
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					isWaitDownloadMap = false;
					if(isDownloadMap){
						upOfflineMap(MainMineActivity.this,mHandler);
					}else{
						mHandler.sendEmptyMessage(6);
					}
				}
			}).start();
			break;
		case R.id.mine_pwd:// 更改密码
			startActivity(new Intent(this,UpwdActivity.class));
			break;
		case R.id.mine_sat:// 卫星信号
			startActivity(new Intent(this,GpsViewActivity.class));
			break;
		case R.id.mine_msg_rd:// 消息记录
			startActivity(new Intent(this,ReceiveMsgActivity.class));
			break;
		case R.id.mine_tb_rd:// 表单记录
			startActivity(new Intent(this,HistoryTableActivity.class));
			break;
		case R.id.mine_submit_rd:// 提交记录
			startActivity(new Intent(this,HistoryReportActivity.class));
			break;

		default:
			break;
		}
	}
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
				Toast.makeText(MainMineActivity.this, "下载异常!", Toast.LENGTH_LONG).show();
				if (progress != null) {
					progress.dismiss();
				}
				break;
			case 3:// 初始化控件
				progress = new ProgressDialog(MainMineActivity.this);
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
					Toast.makeText(MainMineActivity.this, "没有可更新的地图", Toast.LENGTH_LONG).show();
					break;
				}
				final String[] items = new String[IniCheck.listOfflineName.size()];
				
				for(int i=0;i<items.length;++i){
					String offlineconfig = IniCheck.getPreferencePara(MainMineActivity.this,"offline_"+IniCheck.listOfflineFile.get(i));
					String s="";
					if ("".equals(offlineconfig)){
						s="(尚未下载)";
					}else{
						String msg1 = IniCheck.listOfflineMsg.get("offline_"+IniCheck.listOfflineFile.get(i));
						String values[] = offlineconfig.split("\\,");
						if(msg1!=null){
							String values1[] = msg1.split("\\,");
							if (Float.parseFloat(values[2]) < Float.parseFloat(values1[2])){
								s="(当前版本号:"+values[2]+"最新版本号:"+values1[2]+")";
							}else{
								if(i==0){
									isNews = true;
								}
								s="(当前已最新版本)";
							}
						}else{
							s="(当前版本号："+values[2]+"最新版本信息获取失败)";
						}
					}
					//String old
					items[i]=IniCheck.listOfflineName.get(i)+s;
				}
				offlineFile = IniCheck.listOfflineFile.get(0);
				AlertDialog.Builder builder = new Builder(MainMineActivity.this); 
					builder.setTitle("离线地图选择")
					.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							offlineFile = IniCheck.listOfflineFile.get(which);
							String offlineconfig = IniCheck.getPreferencePara(MainMineActivity.this,"offline_"+IniCheck.listOfflineFile.get(which));
							
							if ("".equals(offlineconfig)){
								isNews = false;
							}else{
								String msg1 = IniCheck.listOfflineMsg.get("offline_"+IniCheck.listOfflineFile.get(which));
								String values[] = offlineconfig.split("\\,");
								if(msg1!=null){
									String values1[] = msg1.split("\\,");
									if (Float.parseFloat(values[2]) < Float.parseFloat(values1[2])){
										isNews = false;
									}else{
										isNews = true;
									}
								}
							}
						}
					}).setPositiveButton("取消",  new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
							isWaitDownloadMap = true;
							isDownloadMap = false;
							/*for(String fileName : IniCheck.listOfflineFileUpdate){
								IniCheck.rePreferencePara(activity, "offline_"+fileName);
							}*/
						}
					})
					.setNegativeButton("确定", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if(isNews){
								isWaitDownloadMap = true;
								isDownloadMap = false;
								Toast.makeText(MainMineActivity.this, "当前已最新版本", Toast.LENGTH_LONG).show();
								return ;
							}
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
	private void upOfflineMap(Context context,Handler mHandler) {
		// 如果不是WIFI情况下不下载
			String filepath = AppContext.getInstance().getAppStoreMapPath() + offlineFile;  
			File file = new File(filepath);
			String url = AppContext.getInstance().getServerUrl();
			url = url.substring(0, url.lastIndexOf('/'));
			String downloadUrl = url+_DOWNLOAD_LOCALMAP_URL+"/"+offlineFile+"?key="+MD5.getConfigKey();
			int threadNum = 10;
			Downloadoffmap downloadoffmap = new Downloadoffmap(mHandler);
			downloadoffmap.download(downloadUrl, threadNum, filepath );
			while(downloadoffmap.isFail()){
				try {
					if(downloadoffmap.getFinishThreadNum()== threadNum){
						if(!Downloadoffmap.isPause){
							IniCheck.setPreferencePara(context, "offline_"+offlineFile, IniCheck.listOfflineMsg.get("offline_"+offlineFile), IniCheck._NO_UP_FLAG);
						}
						mHandler.sendEmptyMessage(5);
						return;
					}
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			
			Message message = new Message();
			message.what = 2;
			mHandler.sendMessage(message);
	}
}
