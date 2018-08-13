package com.movementinsome.kernel.updateApk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.Login;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.kernel.util.MD5;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class UpdateApk {

	// ----------------主程序--------------------\\
	//private int oldVersionCode; // 当前版本号
	private int newVersionCode; // 版本编号
	private String newVersionName = ""; // 下载版本名称
	//private String oldVersionName = ""; // 当前版本名称
	private String updateMessage = ""; // 主程序版本更新说明
	// private String newApkName = ""; //新的apk名称
	// private String versionTime = ""; //版本更新时间
	// private String apkType = ""; //apk类型：插件还是主件
	private String url = "";

	private Toast toast;
	private Activity context;
	private boolean flgShowdialog;
	private DownloadService servcie; // 下载服务
	private ProgressDialog progress;
	private ProgressDialog progressDialog;
	private String downApkName = ""; // 当前正在下载的apk名称
	private Map<String, String> mapApkName; // 需要下载更新的apk名称

	// 版本更新路径
	private String downPath = "";
	
	private boolean isFinished = false;

	public UpdateApk(Activity context, boolean flgShowdialog, String url) {
		super();
		this.url = url;
		this.context = context;
		this.flgShowdialog = flgShowdialog;
		mapApkName = new HashMap<String, String>();
		mapApkName.put("main", "");
		mapApkName.put("plugIn", "");
		toast = Toast.makeText(context, null, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.CENTER, 0, 0);
/*		downPath = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/" + this.context.getPackageName() + "/apk/";*/
		downPath = AppContext.getInstance().APP_EXT_UPDATE.getPath()+ "/apk/";
	}

	/**
	 * 检查是否有更新包
	 */
	public int existsUpdate() {
		// 获取系统的版本号：主系统和插件
		//getApkSystemCode();
		// 判断本地是否存在
		checkDownLoadApkVersionCode();
		// 如果检查到本地存在者直接安装
		if (newVersionCode > AppContext.getInstance().getVersionCode()) {
			//upDateDialog();
			return 1;
		} else {
			File fileApk = new File(downPath);// 安装包路径
			if (fileApk.exists()) {
				File files[] = fileApk.listFiles();
				// 遍历目录下所有的文件
				for (File file : files) {
					File delFile = new File(file.getAbsolutePath());
					delFile.delete();
				}
			}
			CheckUpDateApk upDateApk = new CheckUpDateApk();
			upDateApk.run();
//			upDateApk.run2();

			//boolean isFinished = false;
			/*while (!isFinished){
				isFinished = upDateApk.isFinished;*/
				//if (isFinished){
					downApkName = mapApkName.get("main");
					//doNewVersionUpdate(mainMessage + "是否立即更新?", "下载主程序");
					return upDateApk.haveUpdateAp()==false?0:2;
				//}
			//}
			//return 0;
		}
	}

	public boolean isFinished(){
		return isFinished;
	}
	
/*	private void upDateDialog() {
		new AlertDialog.Builder(context)
				.setTitle("提示")
				.setMessage("检查到本地有下载完成的最新版应用包，是否立刻安装升级？")
				.setNegativeButton("确定安装",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								update();
							}
						})
				.setPositiveButton("删除应用包",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								File fileApk = new File(downPath);// 安装包路径
								if (fileApk.exists()) {
									File files[] = fileApk.listFiles();
									// 遍历目录下所有的文件
									for (File file : files) {
										File delFile = new File(file
												.getAbsolutePath());
										delFile.delete();
									}
								}
								CheckUpDateApk upDateApk = new CheckUpDateApk();
								upDateApk.execute("");
							}
						}).create().show();
	}*/

	/**
	 * 得到下载后的最新APK版本信息 (判断本地是否存在)
	 * 
	 * @return
	 */
	private void checkDownLoadApkVersionCode() {
		PackageManager pm = context.getPackageManager();
		File fileApk = new File(downPath);// 安装包路径
		if (!fileApk.exists()) {
			fileApk.mkdirs();
			return;
		}
		File files[] = fileApk.listFiles();
		// 遍历目录下所有的文件
		for (File file : files) {
			PackageInfo info = pm.getPackageArchiveInfo(file.getAbsolutePath(),
					PackageManager.GET_ACTIVITIES);
			if (info != null) {
				if (info.versionName.contains("mapApp")) {
					newVersionCode = info.versionCode;// 得到版本编号
					newVersionName = info.versionName;// 名称
				}
			}
		}
	}
/*
	*//**
	 * 获取本系统的 VersionCode
	 * 
	 * @param context
	 * @return
	 *//*
	private void getApkSystemCode() {
		try {
			// 获取主系统版本号
			oldVersionCode = context.getPackageManager().getPackageInfo(
					this.context.getPackageName(), 0).versionCode;
			oldVersionName = context.getPackageManager().getPackageInfo(
					this.context.getPackageName(), 0).versionName;

		} catch (NameNotFoundException e) {
		}
	}*/
	
	/**
	 * 删除本地安装包
	 */
	public void delLocalApk(){
		File fileApk = new File(downPath);// 安装包路径
		if (fileApk.exists()) {
			File files[] = fileApk.listFiles();
			// 遍历目录下所有的文件
			for (File file : files) {
				File delFile = new File(file
						.getAbsolutePath());
				delFile.delete();
			}
		}
	}

	/**
	 * 更新升级
	 */
	public void install() {
		//MyPublicData.flgTob = true;// 安装程序时允许应用不运行在顶层
		File fileApk = new File(downPath);// 安装包路径
		File files[] = fileApk.listFiles();
		// 遍历目录下所有的文件
		for (File file : files) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(
					Uri.fromFile(new File(file.getAbsolutePath())),
					"application/vnd.android.package-archive");
			//context.startActivity(intent);
			context.startActivityForResult(intent, 8888);
		}
	}

	private void notNewVersionShow(String type) {
		String str = "".equals(type) ? "获取信息失败!" : "当前已是最新版本,无需更新!";
		new AlertDialog.Builder(context).setTitle("软件更新").setMessage(str)
				.setIcon(android.R.drawable.ic_menu_more)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				}).show();
	}

	public void download(String title){
		getProgress(title);
		DownloadTask task;
		try {
			String url1 = url;
			url1 = url1.replace("rest", "");
			url1 = url1 + "download/UpApk/" + downApkName+"?key="+MD5.getConfigKey();
			task = new DownloadTask(url1);
			new Thread(task).start();
		} catch (Exception e) {
			String s = e.toString();
		}
	}
	
/*	public void doNewVersionUpdate(String str, final String title) {
		new AlertDialog.Builder(context)
				.setTitle("软件更新")
				.setMessage(str.replace(";", "\n"))
				// 设置内容
				.setNegativeButton("立即更新",// 设置确定按钮
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								isFinished = false;
								getProgress(title);
								DownloadTask task;
								try {
									String url1 = url;
									url1 = url1.replace("rest", "");
									url1 = url1 + "download/" + downApkName;
									task = new DownloadTask(url1);
									new Thread(task).start();
								} catch (Exception e) {
									String s = e.toString();
									isFinished = true;
								}
							}
						})
				.setPositiveButton("暂不更新",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								isFinished = true;
							}
						}).show();// 创建
	}*/

	private void getProgress(String title) {
		progress = new ProgressDialog(context);
		progress.setTitle(title);
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		if (AppContext.getInstance().getAPP_MIN_CODE() != 0) {

			int minCode = AppContext.getInstance().getAPP_MIN_CODE();
			if (AppContext.getInstance().getVersionCode() < minCode) {
				return;
			}
		}
		progress.setButton("取消下载", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				servcie.isPause = true;
				toast.setText("取消下载,并删除数据");
				toast.show();
				new File(downPath + downApkName).delete();
				context.startActivity(new Intent(context, Login.class));
			}
		});
	}
	
	private class CheckUpDateApk{
		
		boolean isFinished = false;
		
		
		
		public boolean haveUpdateAp(){
			return !"".equals(updateMessage);
		}
		
		public void run(){
			//SpringUtil springUtil = new SpringUtil(context);
			ResponseEntity<UpdateApkInfo[]> response = SpringUtil
					.upDateApk(url);
			//String getDate = "";
			if (response != null) {
				UpdateApkInfo[] upDateApk = response.getBody();
				if (upDateApk != null && upDateApk.length > 0) {
					for (UpdateApkInfo apk : upDateApk) {
						//getDate = "apk";
						if (AppContext.getInstance().getVersionName()
								.contains(apk.getApkType().toString())) {
							newVersionCode = Integer.parseInt(apk
									.getVersionCode());
							if (newVersionCode > AppContext.getInstance().getVersionCode()) {
								updateMessage = "版本编号：" + newVersionCode
										+ " (主程序);版本名称：" + apk.getVersionName()
										+ ";发布日期：" + apk.getVersionTime()
										+ ";更新说明：;" + apk.getVersionExplain()
										+ ";;";
								mapApkName.put("main", apk.getAkpName());
							}
						}
					}
				}
			}
			isFinished = true;
			//return getDate;
		}
		public void run2(){
			//SpringUtil springUtil = new SpringUtil(context);
//			ResponseEntity<UpdateApkInfo[]> response = SpringUtil
//					.upDateApk(url);
			//String getDate = "";

			OkHttpUtils.post()
					.url(url+SpringUtil._REST_UPDATEAPK)
					//.mediaType(MediaType.parse("application/json; charset=utf-8"))
					.build()
					.execute(new StringCallback() {
						public static final String TAG = "100";

						@Override
						public void onError(Call call, Exception e, int id) {
							Log.e("tag", "onError: ",e );
						}

						@Override
						public void onResponse(String response, int id) {
							Log.i(TAG, "onResponse: " + response);

							if (response != null) {
								//getDate = "apk";
								response = response.replace("[", "");
								response = response.replace("]", "");
								Gson gson = new Gson();
								UpdateApkInfo apk = gson.fromJson(response, UpdateApkInfo.class);
								if (apk != null) {
									if (AppContext.getInstance().getVersionName()
											.contains(apk.getApkType().toString())) {
										newVersionCode = Integer.parseInt(apk
												.getVersionCode());
										if (newVersionCode > AppContext.getInstance().getVersionCode()) {
											updateMessage = "版本编号：" + newVersionCode
													+ " (主程序);版本名称：" + apk.getVersionName()
													+ ";发布日期：" + apk.getVersionTime()
													+ ";更新说明：;" + apk.getVersionExplain()
													+ ";;";
											mapApkName.put("main", apk.getAkpName());
										}
									}

								}
							}
						}
					});


			isFinished = true;
			//return getDate;
		}
	}

	public String getUpdateMessage(){
		return updateMessage + "是否立即更新?";
	}
	private final class DownloadTask implements Runnable {

		private String target;

		public DownloadTask(String target) throws Exception {
			this.target = target;
		}

		public void run() {
			try {
				File destination = new File(downPath);
				if (!destination.exists()) {
					destination.mkdir();
				}
				servcie = new DownloadService(target, destination, 3, context);
				myHandler.sendEmptyMessage(3);
				servcie.download(new DownloadListener() {

					public void onDownload(int downloaded_size) {
						if (servcie.fileSize>downloaded_size){
							Message message = new Message();
							message.what = 1;
							message.getData().putInt("size", downloaded_size);
							myHandler.sendMessage(message);
						}else{
							myHandler.sendEmptyMessage(4);
						}
					}
				});
			} catch (Exception e) {
				Message message = new Message();
				message.what = 2;
				myHandler.sendMessage(message);
				e.printStackTrace();
			}

		}
	}

	private Handler myHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				int downloaded_size = msg.getData().getInt("size");
				progress.setProgress(downloaded_size);
				int result = (int) ((float) downloaded_size / progress.getMax() * 100);
				progress.setMessage(result + "%%");
				/*if (progress.getMax() == progress.getProgress()) {
					if (progress != null) {
						progress.dismiss();
					}
					install();
					if (!mapApkName.get("main").equals("")
							&& !mapApkName.get("main").equals(downApkName)) {
						getProgress("下载主程序");
						DownloadTask task;
						try {
							downApkName = mapApkName.get("main");
							String url1 = url;
							url1 = url1.replace("rest/", "");
							task = new DownloadTask(url1 + "download/"
									+ downApkName);
							servcie.isPause = false;
							new Thread(task).start();
						} catch (Exception e) {
							String s = e.toString();
						}
					}
				}*/
				break;
			case 2:
				Toast.makeText(context, "下载异常!", Toast.LENGTH_LONG).show();
				if (progress != null) {
					progress.dismiss();
				}
				break;
			case 3:// 初始化控件
				progress.setMax(servcie.fileSize);
				progress.show();
				servcie.isPause = false;
				break;
			case 4:
				if (progress != null) {
					progress.dismiss();
				}
				install();
				if (!mapApkName.get("main").equals("")
						&& !mapApkName.get("main").equals(downApkName)) {
					getProgress("下载主程序");
					DownloadTask task;
					try {
						downApkName = mapApkName.get("main");
						String url1 = url;
						url1 = url1.replace("rest/", "");
						task = new DownloadTask(url1 + "download/"
								+ downApkName);
						servcie.isPause = false;
						new Thread(task).start();
					} catch (Exception e) {
						String s = e.toString();
					}
				}
				break;
			}
		};
	};
}