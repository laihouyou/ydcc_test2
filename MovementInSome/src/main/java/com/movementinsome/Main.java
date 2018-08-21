package com.movementinsome;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.NetBroadcastReceiver.netEventHandler;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.asynctask.GetOffLineAsyncTask;
import com.movementinsome.app.pub.receiver.NotificationReceiver;
import com.movementinsome.app.server.NetListenervice;
import com.movementinsome.app.server.NewTaskMsgTask;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.database.vo.HistoryTrajectoryVO;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.database.vo.ReturnMessage;
import com.movementinsome.kernel.activity.FullTabActivity;
import com.movementinsome.kernel.initial.model.Locate;
import com.movementinsome.kernel.initial.model.MenuClassify;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.location.LocationInfo;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.kernel.location.trace.TraceManager;
import com.movementinsome.kernel.util.DeviceCtrlTools;
import com.movementinsome.kernel.util.MyDateTools;
import com.movementinsome.kernel.util.NetUtils;
import com.movementinsome.kernel.view.ColumnHorizontalScrollView;
import com.movementinsome.map.coordinate.TranslateCoordinateFactory;
import com.movementinsome.map.utils.MapUtil;
import com.movementinsome.xmpp.NotificationService;
import com.movementinsome.xmpp.ServiceManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  主页面
 * */
public class Main extends FullTabActivity implements netEventHandler {
	private String mSampleDirPath;

	private static final String SAMPLE_DIR_NAME = "baiduTTS";
	private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
	private static final String SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male.dat";
	private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";
	private static final String LICENSE_FILE_NAME = "temp_license";
	private static final String ENGLISH_SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female_en.dat";
	private static final String ENGLISH_SPEECH_MALE_MODEL_NAME = "bd_etts_speech_male_en.dat";
	private static final String ENGLISH_TEXT_MODEL_NAME = "bd_etts_text_en.dat";


	private String mSDCardPath = null;

	public static final String ROUTE_PLAN_NODE = "routePlanNode";
	public static final String SHOW_CUSTOM_ITEM = "showCustomItem";
	public static final String RESET_END_NODE = "resetEndNode";
	public static final String VOID_MODE = "voidMode";
	String authinfo = null;

	private final static String authBaseArr[] =
			{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION };
	private final static String authComArr[] = { Manifest.permission.READ_PHONE_STATE };
	private final static int authBaseRequestCode = 1;
	private final static int authComRequestCode = 2;

	private boolean hasInitSuccess = false;
	private boolean hasRequestComAuth = false;
	private static final String APP_FOLDER_NAME = "BNSDKSimpleDemo";

	private TabHost mHost;
	// 在Activity中使用Tabhost必須要有LocalActivityManager物件來設定
//	private LocalActivityManager lam;

	/** 自定义HorizontalScrollView */
	private ColumnHorizontalScrollView mColumnHorizontalScrollView;
	LinearLayout mRadioGroup_content;
	LinearLayout ll_more_columns;
	RelativeLayout rl_column;

	/** 主分类列表 */
	private List<MenuClassify> modulesClassify = new ArrayList<MenuClassify>();
	/** 当前选中的栏目 */
	private int columnSelectIndex = 0;
	/** 左阴影部分 */
	public ImageView shade_left;
	/** 右阴影部分 */
	public ImageView shade_right;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** Item宽度 */
	private int mItemWidth = 0;

	//protected SlidingMenu side_drawer;

	/** head 头部 的左侧菜单 按钮 */
	private ImageView top_head;

	private ServiceManager serviceManager = null;
	private TraceManager traceManager = null;
	//定时上传
	Locate locate;
	private TraceReceiver receiver;
	// 重连
	private ReconnectionReceiver reconnectionReceiver;
	//轨迹上传任务
	private UpTranceTask upTranceTask;

	private LocationInfo lastLocationInfo; //上一次上传的位置信息
	// 显示任务数量的控件
	private List<TextView> tvList;
	private MyReceiver myReceiver;
	// 获取是否需要派单
	private Timer newTaskTimer;
	// 是否定时返回工单
	private Timer newTimerReturnTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		NetBroadcastReceiver.mListeners.add(this);//网络监听、一直判断网络是否有断开
		
		mScreenWidth = getWindowsWidth();
		mItemWidth = mScreenWidth / 5;
		mHost = getTabHost();	//分页框
		tvList = new ArrayList<TextView>();
		myReceiver = new MyReceiver();
		IntentFilter filter0 = new IntentFilter();//控制任务数量
		filter0.addAction("com.main.menu.view");
		registerReceiver(myReceiver, filter0);

	/*	lam = new LocalActivityManager(Main.this, false);
		lam.dispatchCreate(savedInstanceState);*/

		initView();
		//initSlidingMenu();

		// 启动xmpp服务（推送）
		// startXmppService();
		StartXmpp starXmpp = new StartXmpp();
		starXmpp.execute("");
		// startXmppService();
		// 启动系统广播信息拦截（接收推送消息）
		NotificationReceiver.getInstance().start(this);
		// 启动服务（获取坐标）
		StartTrace startTrace = new StartTrace();
		startTrace.execute("");
		//获取离线时派发的任务
		GetOffLineAsyncTask getoffline = new GetOffLineAsyncTask(this);
		getoffline.execute("");
		// startService(new Intent(this, TraceService.class));
		// 注册广播
		receiver = new TraceReceiver();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.SERVICE_NAME);
		registerReceiver(receiver, filter);
		
		//重连
		reconnectionReceiver = new ReconnectionReceiver();
		IntentFilter filter1 = new IntentFilter();
		filter1.addAction(Constant.RECONNECTION);
		registerReceiver(reconnectionReceiver, filter1);
		
		locate = AppContext.getInstance().getLocateConfig();
		if (locate!=null){
			upTranceTask = new UpTranceTask(this);
			upTranceTask.start(Long.valueOf(locate.getRate()).intValue(),Long.valueOf(locate.getRate()).intValue());
		}
//		//百度TTS语音初始化
//		initialEnv();
//		initialTts();
		//百度导航初始化
//		if (initDirs()) {
//			initNavi();
//		}
		//NavLocalGeoData.getInstance().downLocalGeoData();
//		BaiduAppProxy.bdNavInit(this);
		Intent service = new Intent(Main.this, NetListenervice.class);
		if (Build.VERSION.SDK_INT >= 26){
			this.startForegroundService(service);
		}else {
			this.startService(service);
		}
		List<Module> ml =AppContext.getInstance().getModuleData();
		for(Module module:ml){
			if("biz.bizwebsend".equals(module.getId())){
				newTaskTimer = new Timer();
				newTaskTimer.schedule(new TimerTask() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						new NewTaskMsgTask(Main.this).execute("");					}
				}, 0, 30000);
				break;
			}
		}
		
	}

	private boolean initDirs() {
		mSDCardPath = getSdcardDir();
		if (mSDCardPath == null) {
			return false;
		}
		File f = new File(mSDCardPath, APP_FOLDER_NAME);
		if (!f.exists()) {
			try {
				f.mkdir();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

//	private void initNavi() {
//		BNOuterTTSPlayerCallback ttsCallback = null;
//
//		// 申请权限
//		if (android.os.Build.VERSION.SDK_INT >= 23) {
//
//			if (!hasBasePhoneAuth()) {
//
////				(Activity)this.requestPermissions(authBaseArr, authBaseRequestCode);
//				return;
//
//			}
//		}
//
//		BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME, new BaiduNaviManager.NaviInitListener() {
//			@Override
//			public void onAuthResult(int status, String msg) {
//				if (0 == status) {
//					authinfo = "key校验成功!";
//				} else {
//					authinfo = "key校验失败, " + msg;
//				}
////				Main.this.runOnUiThread(new Runnable() {
////
////					@Override
////					public void run() {
////						Toast.makeText(Main.this, authinfo, Toast.LENGTH_LONG).show();
////					}
////				});
//			}
//
//			public void initSuccess() {
////				Toast.makeText(Main.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
//				hasInitSuccess = true;
//				initSetting();
//			}
//
//			public void initStart() {
////				Toast.makeText(Main.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
//			}
//
//			public void initFailed() {
////				Toast.makeText(Main.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
//			}
//
//		}, null, ttsHandler, ttsPlayStateListener);
//	}
//
//	/**
//	 * 内部TTS播报状态回传handler
//	 */
//	private Handler ttsHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			int type = msg.what;
//			switch (type) {
//				case BaiduNaviManager.TTSPlayMsgType.PLAY_START_MSG: {
//					showToastMsg("Handler : TTS play start");
//					break;
//				}
//				case BaiduNaviManager.TTSPlayMsgType.PLAY_END_MSG: {
//					showToastMsg("Handler : TTS play end");
//					break;
//				}
//				default:
//					break;
//			}
//		}
//	};
//
//	/**
//	 * 内部TTS播报状态回调接口
//	 */
//	private BaiduNaviManager.TTSPlayStateListener ttsPlayStateListener = new BaiduNaviManager.TTSPlayStateListener() {
//
//		@Override
//		public void playEnd() {
//			showToastMsg("TTSPlayStateListener : TTS play end");
//		}
//
//		@Override
//		public void playStart() {
//			showToastMsg("TTSPlayStateListener : TTS play start");
//		}
//	};
//
//	public void showToastMsg(final String msg) {
//		Main.this.runOnUiThread(new Runnable() {
//
//			@Override
//			public void run() {
//				Toast.makeText(Main.this, msg, Toast.LENGTH_SHORT).show();
//			}
//		});
//	}
//	private boolean hasBasePhoneAuth() {
//		// TODO Auto-generated method stub
//
//		PackageManager pm = this.getPackageManager();
//		for (String auth : authBaseArr) {
//			if (pm.checkPermission(auth, this.getPackageName()) != PackageManager.PERMISSION_GRANTED) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	private void initSetting() {
//		 BNaviSettingManager.setDayNightMode(BNaviSettingManager.DayNightMode.DAY_NIGHT_MODE_DAY);
//		BNaviSettingManager
//				.setShowTotalRoadConditionBar(BNaviSettingManager.PreViewRoadCondition.ROAD_CONDITION_BAR_SHOW_ON);
//		BNaviSettingManager.setVoiceMode(BNaviSettingManager.VoiceMode.Veteran);
//		BNaviSettingManager.setRealRoadCondition(BNaviSettingManager.RealRoadCondition.NAVI_ITS_ON);
//
//		//设置APPid
//		Bundle bundle = new Bundle();
//		// 必须设置APPID，否则会静音
//		bundle.putString(BNCommonSettingParam.TTS_APP_ID, OkHttpParam.TTS_APP_ID);
//		BNaviSettingManager.setNaviSdkParam(bundle);
//	}

	/**
	 * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
	 *
	 * @param isCover 是否覆盖已存在的目标文件
	 * @param source
	 * @param dest
	 */
	private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
		File file = new File(dest);
		if (isCover || (!isCover && !file.exists())) {
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = getResources().getAssets().open(source);
				String path = dest;
				fos = new FileOutputStream(path);
				byte[] buffer = new byte[1024];
				int size = 0;
				while ((size = is.read(buffer, 0, 1024)) >= 0) {
					fos.write(buffer, 0, size);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	private void makeDir(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	private void initialEnv() {
		if (mSampleDirPath == null) {
			String sdcardPath = Environment.getExternalStorageDirectory().toString();
			mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
		}
		makeDir(mSampleDirPath);
		copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
		copyFromAssetsToSdcard(false, SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_MALE_MODEL_NAME);
		copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
		copyFromAssetsToSdcard(false, LICENSE_FILE_NAME, mSampleDirPath + "/" + LICENSE_FILE_NAME);
		copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/"
				+ ENGLISH_SPEECH_FEMALE_MODEL_NAME);
		copyFromAssetsToSdcard(false, "english/" + ENGLISH_SPEECH_MALE_MODEL_NAME, mSampleDirPath + "/"
				+ ENGLISH_SPEECH_MALE_MODEL_NAME);
		copyFromAssetsToSdcard(false, "english/" + ENGLISH_TEXT_MODEL_NAME, mSampleDirPath + "/"
				+ ENGLISH_TEXT_MODEL_NAME);
	}

	/** 初始化layout控件 */
	private void initView() {
		mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
		mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
		rl_column = (RelativeLayout) findViewById(R.id.rl_column);
		ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
		shade_left = (ImageView) findViewById(R.id.shade_left);
		shade_right = (ImageView) findViewById(R.id.shade_right);
		top_head = (ImageView) findViewById(R.id.top_head);
		/*top_head.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (side_drawer.isMenuShowing()) {
					side_drawer.showContent();
				} else {
					side_drawer.showMenu();
				}
			}
		});*/
		setChangelView();
	}

	/**
	 * 当栏目项发生变化时候调用
	 * */
	private void setChangelView() {
		initColumnData();
		initTabColumn();
		initTabhost();
	}

	/** 获取Column栏目 数据 */
	private void initColumnData() {
		modulesClassify = AppContext.getInstance().getMenuData();
		List<MenuClassify> menuClassifyList = new ArrayList<MenuClassify>();
		List<String> userList=AppContext.getInstance().getCurUser().getListPrem();
		if(userList!=null){
			for(MenuClassify menuClassify : modulesClassify){
				for(String name:userList){
					if(name!=null&&name.equals(menuClassify.getClazz())){
						menuClassifyList.add(menuClassify);
					}
				}
			}
			modulesClassify = menuClassifyList;
		}
	}

	/**
	 * 初始化Column栏目项
	 * */
	private void initTabColumn() {
		mRadioGroup_content.removeAllViews();//先清除之前的分类信息重新加载
		String name = "";
		String values = "";
		List<String> userList = AppContext.getInstance().getCurUser()
				.getListPrem();

		int count = modulesClassify.size();
		if(count<5&&count>0){
			mItemWidth = mScreenWidth/count;
		}
		mColumnHorizontalScrollView.setParam(this, mScreenWidth,
				mRadioGroup_content, shade_left, shade_right, null, rl_column);
		final List<TextView> tvs = new ArrayList<TextView>();
		final List<ImageView> imgs = new ArrayList<ImageView>();
		final List<Integer> imgResIDs0 = new ArrayList<Integer>();
		final List<Integer> imgResIDs1 = new ArrayList<Integer>();
		
		final List<Integer> ids = new ArrayList<Integer>();
		for (int i = 0; i < count; i++) {

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					mItemWidth, LayoutParams.WRAP_CONTENT,1);
			TextView columnTextView = new TextView(this);
			View v = View.inflate(this, R.layout.main_bottom_menu_view, null);
			
			final ImageView main_img = (ImageView) v.findViewById(R.id.main_img);
			main_img.setClickable(true);
			main_img.setFocusable(true);
			imgs.add(main_img);
			AppContext.getInstance().setImgs(imgs);
			int resID =0;
			int resID1 =0;
			if(modulesClassify.get(i).getIcon()!=null&&!"".equals(modulesClassify.get(i).getIcon())){
				resID = this.getResources().getIdentifier(modulesClassify.get(i).getIcon()+0, "drawable",
						this.getPackageName());
				resID1 = this.getResources().getIdentifier(modulesClassify.get(i).getIcon()+1, "drawable",
						this.getPackageName());
			}else{
				resID = R.drawable.main_default_icon0;
				resID1 = R.drawable.main_default_icon1;
			}
			imgResIDs0.add(resID);
			imgResIDs1.add(resID1);
			main_img.setBackgroundResource(resID);
			TextView main_bottom_menu_text = (TextView) v
					.findViewById(R.id.main_bottom_menu_text);
			TextView main_bottom_menu_img = (TextView) v
					.findViewById(R.id.main_bottom_menu_img);
			
			tvs.add(main_bottom_menu_text);
			AppContext.getInstance().setTvs(tvs);

			main_bottom_menu_text.setTextAppearance(this,
					R.style.top_category_scroll_view_item_text);
			// columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);

			main_bottom_menu_text.setGravity(Gravity.CENTER);
			main_bottom_menu_text.setPadding(5, 5, 5, 5);
			main_bottom_menu_text.setText(modulesClassify.get(i).getTitle());
			//main_bottom_menu_text.setTextSize(DensityUtil.dip2px(this, 6));
			main_bottom_menu_text.setTextColor(getResources()
					.getColorStateList(
							R.color.top_category_scroll_text_color_day));

			if (columnSelectIndex == i) {
				main_bottom_menu_text.setSelected(true);
				main_img.setBackgroundResource(resID1);
			}
			if ("true".equals(modulesClassify.get(i).getIsDisplayTaskCount())) {
				tvList.add(main_bottom_menu_img);
				main_bottom_menu_img.setVisibility(View.VISIBLE);
				int c = getTaskNum();
				main_bottom_menu_img.setText("" + c);
			} else {
				main_bottom_menu_img.setVisibility(View.GONE);
			}

			main_img.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < tvs.size(); i++) {
						ImageView tv = imgs.get(i);
						if (tv != v){
							tvs.get(i).setSelected(false);
							imgs.get(i).setBackgroundResource(imgResIDs0.get(i));
						}else {
							// lam.dispatchPause(isFinishing());
							// lam.dispatchResume();
							
							tvs.get(i).setSelected(true);
							imgs.get(i).setBackgroundResource(imgResIDs1.get(i));
							try{
								mHost.setCurrentTabByTag(String
										.valueOf(modulesClassify.get(i).getId()));
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}
			});
			main_bottom_menu_text.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					for (int i = 0; i < tvs.size(); i++) {
						TextView tv = tvs.get(i);
						if (tv != v){
							tv.setSelected(false);
							imgs.get(i).setBackgroundResource(imgResIDs0.get(i));
						}
						else {
							// lam.dispatchPause(isFinishing());
							// lam.dispatchResume();
							tv.setSelected(true);
							imgs.get(i).setBackgroundResource(imgResIDs1.get(i));
							try{
								mHost.setCurrentTabByTag(String
											.valueOf(modulesClassify.get(i).getId()));
							}catch(Exception ex){
								ex.printStackTrace();
							}
						}
					}
				}
			});
			mRadioGroup_content.addView(v, i, params);
		}
	}
	
	/**
	 * 初始化Tabhost
	 * */
	private void initTabhost() {
		int count = modulesClassify.size();

		for (int i = 0; i < count; i++) {
			if(modulesClassify.get(i).getClazz()!=null&&!modulesClassify.get(i).getClazz().equals("")){
				Intent intent = new Intent(modulesClassify.get(i).getClazz());
				intent.setPackage(AppContext.getInstance().getPackageName());
				intent.putExtra("menu", AppContext.getInstance().getMenu(modulesClassify.get(i).getClazz()));
				
				mHost.addTab(mHost
						.newTabSpec(String.valueOf(modulesClassify.get(i).getId()))
						.setIndicator("Tab" + i)
						.setContent(intent));
			}
		}
		AppContext.getInstance().setmHost(mHost);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

/*	protected void initSlidingMenu() {
		side_drawer = new DrawerView(this).initSlidingMenu();
	}*/

	private long mExitTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
/*		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (side_drawer.isMenuShowing()
					|| side_drawer.isSecondaryMenuShowing()) {
				side_drawer.showContent();
			} else {
				if ((System.currentTimeMillis() - mExitTime) > 2000) {
					Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
					mExitTime = System.currentTimeMillis();
				} else {
					finish();
					// System.exit(0);
				}
			}
			return true;
		}*/
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		// 漏掉這行一定出錯
//		lam.dispatchPause(isFinishing());
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Intent service = new Intent(this,NetListenervice.class);
		this.stopService(service);
		if (serviceManager != null) {
			serviceManager.stopService();
		}
		// 结束服务，如果想让服务一直运行就注销此句
		if (traceManager != null) {
			traceManager.stopService();
			// stopService(new Intent(this, TraceService.class));
		}
		// 注销位置追踪信息接收
		if (receiver != null)
			unregisterReceiver(receiver);
		if (upTranceTask != null)
			upTranceTask.end();
		if(newTaskTimer!=null){
			newTaskTimer.cancel();
		}
		unregisterReceiver(myReceiver);
		NotificationReceiver.getInstance().stop(this);
		super.onDestroy();
		AppContext.getInstance().onTerminate();
		System.exit(0);
	}

	@Override
	protected void onResume() {
		// 漏掉這行一定出錯
	//	lam.dispatchResume();
		super.onResume();
	}

	// 启动位置追踪服务
	private class StartTrace extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			//Locate locate = AppContext.getInstance().getLocateConfig();// .getDistance()

			/*traceManager = new TraceManager(Main.this, locate.getRate(),
					locate.getDistance());*/
			traceManager = new TraceManager(Main.this, 1000,0);//10ms,0m
			traceManager.startService();
			return null;
		}

	}

	// 启动消息推送服务连接
	private class StartXmpp extends AsyncTask<String, Void, String> {
		String[] conn;
		String phoneIMEI;
		String curUserName;
		
		public StartXmpp(){
			conn = AppContext.getInstance().getPushServerUrl()
						.split(":");
			phoneIMEI = AppContext.getInstance().getPhoneIMEI();
			curUserName =AppContext.getInstance().getCurUser().getUserName();
		}
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			if (null != curUserName & null != phoneIMEI & conn.length>1 ){
				serviceManager = new ServiceManager(Main.this, conn[0],
						Integer.parseInt(conn[1]),curUserName , phoneIMEI);
				serviceManager.startService();
			}
			return null;
		}

	}

	// 获取广播数据
	private class TraceReceiver extends BroadcastReceiver {
		
		String userId;
		String phoneIMEI;
		String curUserName;
		//SpringUtil traceUpload;
		public TraceReceiver(){
			userId=AppContext.getInstance().getCurUser()==null?null:AppContext.getInstance().getCurUser().getUserId();
			phoneIMEI=AppContext.getInstance().getPhoneIMEI();
			curUserName=AppContext.getInstance().getCurUser().getUserName();
		}

		//int i=0;//模拟定位坐标索引值
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras();
			LocationInfoExt location = (LocationInfoExt) intent
					.getSerializableExtra(Constant.TRACE_INFO);
			if (location != null) {

				location = (LocationInfoExt) TranslateCoordinateFactory.gpsTranslate(AppContext
						.getInstance().getCoordTransform(), AppContext
						.getInstance().getSrid(), location);

				location.setImei(phoneIMEI);
				location.setUsnum(curUserName);
				
				if(userId!=null&&!"null".equals(userId)){
					location.setUsid(userId);
				}
				
/*				if ((AppContext.getInstance().locations.length>0)){
					if (i>=AppContext.getInstance().locations.length){
						i = 0;
					}
					if (null != AppContext.getInstance().locations[i])
							AppContext.getInstance().setCurLocation(AppContext.getInstance().locations[i]);
					
					i++;

				}else{*/
					// 作为全局位置信息获取来源
					AppContext.getInstance().setCurLocation(location);
				//}

/*				List<LocationInfo> locationInfos = new ArrayList();
				locationInfos.add(location);*/
			}
		}
	}
	// 重连广播
	private class ReconnectionReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			if (serviceManager != null) {
				NotificationService.setReconnectionHandler(reconnectionHandler);
				serviceManager.stopService();
			}
		}
		
	}
	private Handler reconnectionHandler =new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// 启动xmpp服务
				// startXmppService();
				StartXmpp starXmpp = new StartXmpp();
				starXmpp.execute("");
				NotificationService.setReconnectionHandler(null);
				//获取离线时派发的任务
				GetOffLineAsyncTask getoffline = new GetOffLineAsyncTask(Main.this);
				getoffline.execute("");
				break;

			default:
				break;
			}
		}
		
	};
	// 异步线程上传

	private class UpTranceTask extends TimerTask {
		// true时使用后台进程线程。只要剩下的程序记叙运行，后台进程线程就会执行。
		Timer myTimer;
		//private SpringUtil traceUpload;// = new SpringUtil(Main.this);

		private Context context;

		public UpTranceTask(Context context){
			this.context = context;
			//traceUpload = new SpringUtil(this.context);
		}
		public void start(int delay) {
			myTimer = new Timer();
			myTimer.schedule(this, delay); 
		}
		
		public void start(int delay, int period) {
			myTimer = new Timer();
			myTimer.schedule(this, delay,period); // 利用timer.schedule方法

			// public void schedule(TimerTask task,long time,long period)
			// task被安排在延迟time后执行，执行后将每隔period(毫秒)反复执行。由于规定的时间间隔并不能保证与时钟精准的同不步，所以该方
		}

		public void start(Date time, int period) {
			myTimer = new Timer();
			myTimer.schedule(this, time, period); // 利用timer.schedule方法

			// public void schedule(TimerTask task,Date time,long period)
			// task被安排在time指定的时间执行，执行后将每隔period(毫秒)反复执行。由于规定的时间间隔并不能保证与时钟精准的同不步，所以该方
		}

		public void run() {
			// 执行任务(
			if (AppContext.getInstance().getCurLocation() != null) {
				if (lastLocationInfo != null){
					//防止重复节点
					if (lastLocationInfo.toString().equals(AppContext.getInstance().getCurLocation().toString())){
						return;
					}else{
						double dist = MapUtil.gps2m(AppContext.getInstance().getCurLocation().getLatitude(), AppContext.getInstance().getCurLocation().getLongitude(),
								lastLocationInfo.getLatitude(), lastLocationInfo.getLongitude());
						//过滤位置间距
						if (dist<locate.getDistance()){
							return;
						}
					}
				}
					
				lastLocationInfo = AppContext.getInstance().getCurLocation();
				List<LocationInfo> locationInfos = new ArrayList();
				if(lastLocationInfo!=null
						&&lastLocationInfo.getTime()!=null
						&&lastLocationInfo.getLatitude()!=0
						&&lastLocationInfo.getLongitude()!=0){
					if(!"".equals(AppContext.getInstance().getSpeedType())){
						lastLocationInfo.setSpeedType(Long.parseLong(AppContext.getInstance().getSpeedType()));
					}
					locationInfos.add(lastLocationInfo);
					for (int i = 0; i < locationInfos.size(); i++) {
						try {
							Dao<HistoryTrajectoryVO, Long> historyTrajectoryDao = AppContext
									.getInstance().getAppDbHelper()
									.getDao(HistoryTrajectoryVO.class);
							
							Date as = new Date(new Date().getTime()-24*60*60*1000);
							SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-dd");
							String jtime = matter1.format(as);
							
							List<HistoryTrajectoryVO> historyTrajectoryVOlist2 = null;
							Map<String, Object> n = new HashMap<String, Object>();
							n.put("time", jtime);
							historyTrajectoryVOlist2 = AppContext.getInstance()
								.getAppDbHelper().getDao(HistoryTrajectoryVO.class)
								.queryForFieldValuesArgs(n);
							
							if(historyTrajectoryVOlist2!=null){
								historyTrajectoryDao.delete(historyTrajectoryVOlist2);
							}
							
						
								Double Mapx=locationInfos.get(i).getMapx();
								Double Mapy=locationInfos.get(i).getMapy();
								String time=MyDateTools.getCurDate();
								
								List<HistoryTrajectoryVO> historyTrajectoryVOlist = null;
								Map<String, Object> m = new HashMap<String, Object>();
								m.put("time", MyDateTools.getCurDate());
								historyTrajectoryVOlist = AppContext.getInstance()
									.getAppDbHelper().getDao(HistoryTrajectoryVO.class)
									.queryForFieldValuesArgs(m);
								
								Long id= (long) historyTrajectoryVOlist.size()+1;
								HistoryTrajectoryVO historyTrajectoryVO=new HistoryTrajectoryVO();
								historyTrajectoryVO.setCoordinatesX(Mapx);
								historyTrajectoryVO.setCoordinatesY(Mapy);
								historyTrajectoryVO.setTime(time);
								historyTrajectoryVO.setId(id);
								historyTrajectoryDao.create(historyTrajectoryVO);
								
							} 
						catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					try {
						ReturnMessage rlt = SpringUtil.traceUpload(AppContext.getInstance().getServerUrl(),locationInfos);
					} catch (Exception ex) {
						;
					}
				}
			}
		}
		public void end() {
			myTimer.cancel();
			// 终止Timer的功能执行，但不会对正在执行的任务有影响。当执行cancel方法后将不能再用其分配任务。
		}
	}
	private  class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			int c= getTaskNum();
			for(int i=0;i<tvList.size();++i){
				tvList.get(i).setText(""+c);
			}
		}
		
	}
	private int getTaskNum(){
		int c = 0;
		try {
			Dao<InsTablePushTaskVo, Long> insTablePushTaskDao = AppContext
					.getInstance().getAppDbHelper()
					.getDao(InsTablePushTaskVo.class);
			List<InsTablePushTaskVo> insTablePushTaskVoList = insTablePushTaskDao
					.queryForAll();
			if (insTablePushTaskVoList != null) {
				c = insTablePushTaskVoList.size();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}

	@Override
	public void onNetChange() {
		// TODO Auto-generated method stub
		try{
			if (NetUtils.checkNetWork(this)){
//				SystemLog.addLog("网络恢复连接:"+MyDateTools.getCurDateTime());
//				SystemLog.sendLog();
				//获取离线时派发的任务
				GetOffLineAsyncTask getoffline = new GetOffLineAsyncTask(this);
				getoffline.execute("");
			}else{
//				SystemLog.addLog("网络断开连接:"+MyDateTools.getCurDateTime());
				DeviceCtrlTools.openMobileDataSwitch(this,true);
				showVibrator(this);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
}
