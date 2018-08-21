package com.movementinsome;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.esri.core.geometry.Point;
import com.esri.core.tasks.identify.IdentifyResult;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.database.db.AppDataBaseHelper;
import com.movementinsome.database.vo.MapparentmenusVO;
import com.movementinsome.database.vo.UserBeanVO;
import com.movementinsome.kernel.email.SystemLog;
import com.movementinsome.kernel.exception.MyExceptionHandler;
import com.movementinsome.kernel.initial.model.App;
import com.movementinsome.kernel.initial.model.AutoStart;
import com.movementinsome.kernel.initial.model.City;
import com.movementinsome.kernel.initial.model.CoordParam;
import com.movementinsome.kernel.initial.model.FacilityType;
import com.movementinsome.kernel.initial.model.FileService;
import com.movementinsome.kernel.initial.model.Locate;
import com.movementinsome.kernel.initial.model.MapParam;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.initial.model.MenuClassify;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.initial.model.Movetype;
import com.movementinsome.kernel.initial.model.Navigation;
import com.movementinsome.kernel.initial.model.Number;
import com.movementinsome.kernel.initial.model.ProjectType;
import com.movementinsome.kernel.initial.model.ReportHistory;
import com.movementinsome.kernel.initial.model.Solution;
import com.movementinsome.kernel.initial.model.Sub;
import com.movementinsome.kernel.initial.model.Task;
import com.movementinsome.kernel.initial.model.UserRight;
import com.movementinsome.kernel.initial.model.View;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.kernel.util.CTelephoneInfo;
import com.movementinsome.kernel.util.DeviceCtrlTools;
import com.movementinsome.kernel.util.FileUtils;
import com.movementinsome.kernel.util.NetUtils;
import com.movementinsome.kernel.util.WebAccessTools;
import com.movementinsome.kernel.util.WebAccessTools.NET_STATE;
import com.movementinsome.map.coordinate.Gps2LocaleCoordinate;
import com.movementinsome.map.coordinate.ITranslateCoordinate;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class AppContext extends Application {

    // 百度注册KEY，用于LBS应用
    // public boolean m_bKeyRight = true;
    public NET_STATE ACCESS_NET_STATE = NET_STATE.NULL;

    private static AppContext mInstance = null;
    private SharedPreferences pPrefere; // 定义数据存储
    private Editor pEditor; // sharedpreferred数据提交

    final public String APPNAME = "mapApp";

    private Integer mVerCode = 1;
    private String mVerName = "mapApp";

    private String APP_PATH; // 内部存储
    private File APP_PATH_CONFIG; // 应用配置目录
    private File APP_PATH_DATA; // 应用配置目录
    public File APP_EXT_UPDATE = null; // App升级目录
    private File APP_EXT_STORE_PATH; // 外部存储
    // private File APP_EXT_STORE_PICTURES;//图片存储目录
    private File APP_EXT_STORE_MEDIO; // 相机拍摄照片和视频的标准目录
    private File APP_EXT_STORE_MEDIO_VOIC;// 录音目录
    private File APP_EXT_STORE_MAP; // 本地地图

    private Solution solution = null;
    private int defSolutionId = -1;
    private AppDataBaseHelper appDB; // 数据库
    //private GeoDataBaseHelper geoDB; //本地空间数据库
    private UserBeanVO user;

    private LocationInfoExt curLocation = null;//当前位置信息

    //用于不同activity或是对象之间，需要进行消息交互的情况下使用
    private Handler mHandle = new Handler();
    private String mMapPosition = "";
    private String mMapBound = "";
    private String mMapArea = "";
    private Point mMapPoint;
    private IdentifyResult mMapIden;
    private String pipeBroAnalysisResult;

    private String mServerUrl = null;
    private int APP_MIN_CODE;
    private String phoneVersionNum;
    private String phoneTypeNum;

    private boolean mSysIniSuc = false;

    private TabHost mHost;
    private List<TextView> tvs;
    private List<ImageView> imgs;
    private ProjectVo projectVo;
    private Dao<ProjectVo,Long> projectVoDao;
    private Dao<SavePointVo,Long> savePointVoDao;

//    public BMapManager mBMapManager = null;

    //public LocationInfoExt[] locations = new LocationInfoExt[711];

    private String videopath;//视频路径

    // AppContext单态
    public static AppContext getInstance() {
        return mInstance;
    }

    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //强制打开GPS、数据连接、Wlan开关
		if (!DeviceCtrlTools.isGPSEnable(this)){
			DeviceCtrlTools.toggleGPS(this);
		}
		//DeviceCtrlTools.openGpsSwitch(this);
		DeviceCtrlTools.openMobileDataSwitch(this,true);
        //DeviceCtrlTools.openWifi(this,true);

/*		String battry = DeviceCtrlTools.getBattery(this);
        System.out.println(battry);*/
        // 启动系统广播信息拦截
        //NotificationReceiver.getInstance().start(this);
        //if (NetUtils.isNetworkConnected(this)){
        initEngineManager(this);

        // 捕捉异常信息
        Thread.setDefaultUncaughtExceptionHandler(
                MyExceptionHandler.getInstance(getApplicationContext(), getCurUserName(), getVersionName(), getPhoneIMEI()));
        // 检查是否有异常报告
        //new SystemLogAnalysis(null);
        SystemLog.sendLog();

        //创建APN测试
/*		if (!ApnUtil.checkAPN(this, "whswjt")){
			ApnUtil.addAPN(this, "whswjt");
		}*/
        //trancoor();
/*		}else{
			 Toast.makeText(this, "没有网络连接系统退出!", Toast.LENGTH_LONG).show();
			 android.os.Process.killProcess(android.os.Process.myPid());
			 System.exit(0);
		}*/
        phoneVersionNum = android.os.Build.VERSION.RELEASE;
        phoneTypeNum = android.os.Build.MODEL;

//        //设置采集范围
        if (AppContext.getInstance().getCollectionScope()==0){
            setCollectionScope(500L);
        }

        //爆漏分析使用   百度地图初始化
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
//        SDKInitializer.initialize(getApplicationContext());
//        SDKInitializer.setCoordType(CoordType.BD09LL);//默认为BD09LL坐标
//        initEngineManager2(this);       //全景图初始化

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(30000L, TimeUnit.MILLISECONDS)
                .readTimeout(30000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        //二维码初始化
        ZXingLibrary.initDisplayOpinion(this);

    }

    public void onTerminate() {
        // 启动系统广播信息拦截
        //NotificationReceiver.getInstance().stop(this);
        super.onTerminate();
    }

    public String getPhoneVersionNum() {
        return phoneVersionNum;
    }

    public String getPhoneTypeNum() {
        return phoneTypeNum;
    }

//    /**
//     * 全景图初始化
//     * @param context
//     */
//    public void initEngineManager2(Context context) {
//        try {
//            if (mBMapManager == null) {
//                mBMapManager = new BMapManager(context);
//            }
//
//            if (!mBMapManager.init(new BaiduAppProxy.MyGeneralListener())) {
//                Toast.makeText(this, "BMapManager  初始化错误!",
//                        Toast.LENGTH_LONG).show();
//            }
//            Log.d("ljx", "initEngineManager");
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    private void initEngineManager(Context context) {
        try {
            mVerCode = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionCode;
            mVerName = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 系统相关路径分配
        APP_PATH = super.getFilesDir().getAbsolutePath();
        APP_PATH_CONFIG = new File(APP_PATH + "/Config");
        APP_PATH_DATA = new File(APP_PATH + "/Data");

        APP_EXT_STORE_PATH = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/" + APPNAME);

        if (!APP_EXT_STORE_PATH.exists()) {
            APP_EXT_STORE_PATH.mkdir();
        }

        APP_EXT_UPDATE = new File(APP_EXT_STORE_PATH.getAbsoluteFile() + "/" + "Update");

        if (!APP_EXT_UPDATE.exists()) {
            APP_EXT_UPDATE.mkdir();
        }

        String s = Environment.getExternalStorageDirectory().getAbsolutePath();
        APP_EXT_STORE_MEDIO = new File(s + "/DCIM");// new File(APP_EXT_STORE_PATH + "/Medios");// Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        if (!APP_EXT_STORE_MEDIO.exists()) {
            APP_EXT_STORE_MEDIO.mkdir();
        }
        APP_EXT_STORE_MEDIO_VOIC = new File(s + "/DCIM/VOIC");
        if (!APP_EXT_STORE_MEDIO_VOIC.exists()) {
            APP_EXT_STORE_MEDIO_VOIC.mkdir();
        }
        APP_EXT_STORE_MAP = new File(APP_EXT_STORE_PATH.getAbsoluteFile() + "/MAP");
        if (!APP_EXT_STORE_MAP.exists()) {
            APP_EXT_STORE_MAP.mkdir();
        }

        // APP_EXT_STORE_DCIM = new
        // File(APP_EXT_STORE_PATH+"/Dcim");//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
        appDB = AppDataBaseHelper.getHelper(this);
        //geoDB = GeoDataBaseHelper.getHelper(this);

        // 系统初始参数读取
        if (pPrefere == null) {
            pPrefere = getSharedPreferences(Constant.SPF_NAME, 3);
        }
        pEditor = pPrefere.edit();

        // 模拟用户
		/*String userName = pPrefere.getString(Constant.SPF_DEF_LOGIN_USER, "");
		if ("".equals(userName)) {
			pEditor.putString(Constant.SPF_DEF_LOGIN_USER, "gordon").commit();
		}*/
        // 存储手机机器码
        String phoneIMEI = "";
        String phoneNumber = "";
        CTelephoneInfo telephonyInfo = CTelephoneInfo.getInstance(this);
        if (telephonyInfo.isDualSim()) { //判断是否双卡
            if (telephonyInfo.isSIM1Ready()) {
                phoneIMEI = telephonyInfo.getImeiSIM1();
                phoneNumber = telephonyInfo.getLine1Num1();
            } else if (telephonyInfo.isSIM2Ready()) {
                phoneIMEI = telephonyInfo.getImeiSIM2();
                phoneNumber = telephonyInfo.getLine1Num2();
            } else {
                phoneIMEI = telephonyInfo.getImeiSIM1() == null ? (telephonyInfo.getImeiSIM2() == null ? "" : telephonyInfo.getImeiSIM2()) : telephonyInfo.getImeiSIM1();
                phoneNumber = telephonyInfo.getLine1Num1() == null ? (telephonyInfo.getLine1Num2() == null ? "" : telephonyInfo.getLine1Num2()) : telephonyInfo.getLine1Num1();
            }
        } else {
/*	    	TelephonyManager tm = (TelephonyManager) getBaseContext()
					.getSystemService(Context.TELEPHONY_SERVICE);
			phoneIMEI = "" + tm.getDeviceId()==null?"":tm.getDeviceId().toLowerCase();
			if ("".equals(phoneIMEI)) {
				phoneIMEI = "" + tm.getSimSerialNumber().toLowerCase();
			}   	
    		phoneNumber = tm.getLine1Number();*/
            phoneIMEI = telephonyInfo.getImeiSIM1();
            phoneNumber = telephonyInfo.getLine1Num1();
        }
	    
		/*String phoneIMEI = "";
		TelephonyManager tm = (TelephonyManager) getBaseContext()
				.getSystemService(Context.TELEPHONY_SERVICE);
		tm.getLine1Number()
		/*phoneIMEI = "" + tm.getDeviceId()==null?"":tm.getDeviceId().toLowerCase();
		if ("".equals(phoneIMEI)) {
			phoneIMEI = "" + tm.getSimSerialNumber().toLowerCase();
		}*/
        pEditor.putString(Constant.SPF_PHONE_IMEI, phoneIMEI).commit();
        pEditor.putString(Constant.SPF_PHONE_NUM, phoneNumber).commit();
	
		/*
		// 系统版本号
/*		String versionName = "1.0";
		try {

			versionName = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}
		pEditor.putString(Constant.SPF_VERSION, versionName).commit(); // 系统版本号
*/
        // 初始化下载下来的solution配置信息
        initSolution();
        
/*		try {
			loadRoadNavTestData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

        //百度初始化
        //BaiduAppProxy.bdInit(this.getApplicationContext());

    }

    // 数据库连接实例
    public AppDataBaseHelper getAppDbHelper() {
        return appDB;
    }

    //返回本地空间数据库连接实例
/*	public GeoDataBaseHelper getGeoDbHelper() {
		return geoDB;
	}
*/
	/*
	 * 初始化下载下来的solution配置信息
	 */
    public void initSolution() {
        //initLocalSolution();
        initRemoteSolution();
    }

    //从asset目录获取 配制信息，测试使用
    private void initLocalSolution() {
        // 初始化系统配置参数
        InputStream file = null;
		/*
		 * 从asset目录获取 try { //
		 * 测试使用这个目录 } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

        defSolutionId = pPrefere.getInt(Constant.SPF_DEF_SOLUTION_ID, -1);
        try {
            file = this.getAssets().open("solution.xml");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (file != null) { // file为null，需要进入初始化步骤
            XmlPull xmlPull = new XmlPull();
            solution = xmlPull.readSolutionConfig(file);

            if (solution != null) {
                for (Sub sub : solution.getSubs()) {

                    try {
                        file = this.getAssets().open(sub.getFile());
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if (null == file) {
                        continue;
                    }
                    sub.setConfig(xmlPull.readConfig(file));
                }
            }
            // 系统访问URL方式
            // ACCESS_NET_STATE = checkNetState();
/*
			CheckNetState checkNetState = new CheckNetState();
			checkNetState.execute("");*/

        } else {
            setSystemInit(false);
        }
    }

    //从服务器下载的配制文件获取配制参数
    private void initRemoteSolution() {
        File f = new File(APP_PATH_CONFIG.getAbsolutePath()
                + "/solution.xml");
        if (!f.exists()) {
            setSystemInit(false);
            return;
        }


        // 初始化系统配置参数
        InputStream file = null;//流
		/*
		 * 从asset目录获取 try { file = this.getAssets().open("solution.xml"); //
		 * 测试使用这个目录 } catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

        defSolutionId = pPrefere.getInt(Constant.SPF_DEF_SOLUTION_ID, -1);//solution选择
        file = FileUtils.readFile(APP_PATH_CONFIG.getAbsolutePath()
                + "/solution.xml");//读取路径
        if (file != null) { // file为null，需要进入初始化步骤
            XmlPull xmlPull = new XmlPull();
            solution = xmlPull.readSolutionConfig(file);

            if (solution != null) {
                for (Sub sub : solution.getSubs()) {
					/*
					 * try { file = this.getAssets().open(sub.getFile()); //
					 * 测试使用这个目录 } catch (IOException e) { // TODO Auto-generated
					 * catch block e.printStackTrace(); }
					 */
                    file = FileUtils.readFile(APP_PATH_CONFIG
                            .getAbsolutePath() + "/" + sub.getFile());
                    if (null == file) {
                        continue;
                    }
                    sub.setConfig(xmlPull.readConfig(file));
                    try {
                        file.close();
                        if (null == sub.getConfig()) {
                            mSysIniSuc = false;
                        } else
                            mSysIniSuc = true;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        //初始化出错,需要引导系统到初始化界面重新初始化
                        mSysIniSuc = false;
                        e.printStackTrace();
                    }
                }
            }
            // 系统访问URL方式
            // ACCESS_NET_STATE = checkNetState();

			/*CheckNetState checkNetState = new CheckNetState();
			checkNetState.execute("");*/

        } else {
            setSystemInit(false);
        }
    }

    // 主框架栏目信息
    public List<MenuClassify> getMenuData() {
        if (solution == null) {
            initSolution();
        }
        if (defSolutionId == -1)
            return solution.getDefaultConfig().getMainmenu();
        else
            return solution.getDefaultConfig(defSolutionId).getMainmenu();
        // return solution.getDefaultConfig().getMainmenu();
    }

    public ReportHistory getReportHistory() {
        if (solution == null) {
            initSolution();
        }
        if (defSolutionId == -1)
            return solution.getDefaultConfig().getReportHistory();
        else
            return solution.getDefaultConfig(defSolutionId).getReportHistory();
    }

    //根据模块实例名获取模板所属菜单ID
    public MenuClassify getMenu(String clazz) {
        List<MenuClassify> lstMenu = null;
        if (solution == null) {
            initSolution();
        }
        if (defSolutionId == -1)
            lstMenu = solution.getDefaultConfig().getMainmenu();
        else
            lstMenu = solution.getDefaultConfig(defSolutionId).getMainmenu();
        if (lstMenu != null) {
            for (MenuClassify menu : lstMenu) {
                if (menu.getClazz().equalsIgnoreCase(clazz)) {
                    return menu;
                }
            }
        }
        return null;
        // return solution.getDefaultConfig().getMainmenu();
    }

    // 系统应用功能列表
    public List<Module> getModuleData() {
        if (solution == null) {
            initSolution();
        }
        if (defSolutionId == -1)
            return solution.getDefaultConfig().getModules();
        else
            return solution.getDefaultConfig(defSolutionId).getModules();
        // return solution.getDefaultConfig().getModules();

    }

    public String getSubData() {
        if (solution == null) {
            initSolution();
        }

        if (defSolutionId == -1)
            return "";
        else
            return solution.getDefaultFile(defSolutionId);
    }

    public List<Module> getModuleData(Integer mid) {
        List<Module> modules = new ArrayList();

        if (solution == null) {
            initSolution();
        }
        if (null != mid) {
            if (defSolutionId == -1)
                for (Module module : solution.getDefaultConfig().getModules()) {
                    if (module.getMenu().getId().equals(mid)) {
                        modules.add(module);
                    }
                }
            else
                for (Module module : solution.getDefaultConfig(defSolutionId).getModules()) {
                    if (module.getMenu().getId().equals(mid)) {
                        modules.add(module);
                    }
                }
        }
        return modules;

    }

    public List<Module> getModuleData(Integer mid, boolean canwrite) {
        List<Module> modules = new ArrayList();

        if (solution == null) {
            initSolution();
        }
        if (defSolutionId == -1)
            for (Module module : solution.getDefaultConfig().getModules()) {
                if (module.getMenu().getId().equals(mid)) {
                    if (canwrite && module.isCanwrite())
                        modules.add(module);
                }
            }
        else
            for (Module module : solution.getDefaultConfig(defSolutionId).getModules()) {
                if (module.getMenu().getId().equals(mid)) {
                    if (canwrite && module.isCanwrite())
                        modules.add(module);
                }
            }
        return modules;

    }

    public String getCurUserPhone() {
        return String.valueOf("");
    }

    public String getPhoneIMEI() {
        return pPrefere.getString(Constant.SPF_PHONE_IMEI, "");
    }

    public void setSpeedType(String speedType) {
        pEditor.putString(Constant.SPEED_TYPE, speedType).commit();
    }

    public String getSpeedType() {
        return pPrefere.getString(Constant.SPEED_TYPE, "");
    }

    public String getIniConnSvr() {
        return pPrefere.getString(Constant.SPF_INI_CONNECT_URL, "");
    }

    public void setIniConnSvr(String svr) {
        pEditor.putString(Constant.SPF_INI_CONNECT_URL, svr).commit();
    }
    //设置采测范围
    public boolean setCollectionScope(Long scope) {
        if (pEditor.putLong(Constant.COLLECTION_SCOPE, scope).commit()){
            return true;
        }else {
            return false;
        }
    }
    //获取采测范围
    public Long getCollectionScope(){
        return pPrefere.getLong(Constant.COLLECTION_SCOPE,0);
    }

    //设置可视范围
    public void setDem(float dem){
        pEditor.putFloat(Constant.DEM, dem).commit();
    }
    //获取可视范围
    public float getDem(){
        return pPrefere.getFloat(Constant.DEM,0);
    }

    public String getStoragePath() {
        return APP_EXT_STORE_PATH.toString();
    }

    public String getAppConfigPath() {
        return APP_PATH_CONFIG.getAbsolutePath() + "/";
    }

    public String getAppConfigFileName() {
        return APP_PATH_CONFIG.getAbsolutePath() + "/solution.xml";
    }

    public String getAppStorePath() {
        return APP_EXT_STORE_PATH + "/";
    }

    public String getAppStoreMedioPath() {
        return APP_EXT_STORE_MEDIO + "/";
    }

    public String getAppStoreMedioPath2() {
        return APP_EXT_STORE_MEDIO.getPath();
    }

    public String getAppStoreMapPath() {
        return APP_EXT_STORE_MAP + "/";
    }

    public String getAppStoreMedioVoicPath() {
        return APP_EXT_STORE_MEDIO_VOIC + "/";
    }

    /**
     * 获取默认的地图配置参数
     *
     * @return
     */
    public MapParam getMapParam() {
        if (solution == null) {
            initSolution();
        }
        if (defSolutionId == -1)
            return solution.getDefaultConfig().getMap();
        else
            return solution.getDefaultConfig(defSolutionId).getMap();
    }


    /**
     * 获取默认的底图服务
     *
     * @return
     */
    public List<Mapservice> getBaseMapServices() {
        if (solution == null) {
            initSolution();
        }
        if (defSolutionId == -1)
            return solution.getDefaultConfig().getMap().getBaseMap()
                    .getMapservices();
        else
            return solution.getDefaultConfig(defSolutionId).getMap()
                    .getBaseMap().getMapservices();

    }

    // 返回系统内容解决方案
    public List<Sub> getSolution() {
        if (solution == null) {
            return null;
        } else {
            return solution.getSubs();
        }
    }

    /**
     * 更新默认的解决方案设置
     *
     * @param id
     */
    public void setDefSolution(int id) {
        defSolutionId = id;
        pEditor.putInt(Constant.SPF_DEF_SOLUTION_ID, id).commit();
    }

    public void iniDefSolution(int id) {
        defSolutionId = id;
        pEditor.putInt(Constant.SPF_DEF_SOLUTION_ID, id).commit();
        mServerUrl = null;
    }

    /**
     * 获取默认的解决方案设置
     *
     * @return
     */
    public Sub getDefSolution() {
        if (solution == null) {
            initSolution();
        }
        if (solution != null) {
            if (defSolutionId == -1)
                return solution.getDefault();
            else
                return solution.getDefault(defSolutionId);
        } else {
            return null;
        }
    }

    /**
     * 数据交互服务器URL
     *
     * @return
     */
    public String getServerUrl() {
        if (null == mServerUrl) {
            if (solution == null) {
                initSolution();
            }
            if (defSolutionId == -1) {
                return null;
            }
            // NET_STATE netState = checkNetState();
            if (ACCESS_NET_STATE == NET_STATE.NULL) {
                ACCESS_NET_STATE = checkNetState();
                if (ACCESS_NET_STATE == NET_STATE.NULL)
                    return null;
					/*mServerUrl = solution.getDefaultConfig().getApp().getCommuService()
							.getForeign();*/
            }
/*			if (defSolutionId == -1) {
				if (ACCESS_NET_STATE == NET_STATE.LOCAL) {
					mServerUrl = solution.getDefaultConfig().getApp().getCommuService()
							.getLocal();
				} else {
					mServerUrl =  solution.getDefaultConfig().getApp().getCommuService()
							.getForeign();
				}
			} else {*/
            try {
                if (ACCESS_NET_STATE == NET_STATE.LOCAL) {
                    mServerUrl = solution.getDefaultConfig(defSolutionId).getApp()
                            .getCommuService().getLocal();
                } else {
                    mServerUrl = solution.getDefaultConfig(defSolutionId).getApp()
                            .getCommuService().getForeign();
                }
            } catch (NullPointerException ex1) {
                ex1.printStackTrace();
            }
            //}

        }

        return mServerUrl;
    }

    public String getInitServerUrl() {
        if (null == mServerUrl) {
            if (solution == null) {
                initSolution();
            }
            if (defSolutionId == -1) {
                return null;
            }
            ACCESS_NET_STATE = checkNetState();
            if (ACCESS_NET_STATE == NET_STATE.NULL) {
                return null;
            } else if (ACCESS_NET_STATE == NET_STATE.LOCAL) {
                mServerUrl = solution.getDefaultConfig(defSolutionId).getApp()
                        .getCommuService().getLocal();
            } else {
                mServerUrl = solution.getDefaultConfig(defSolutionId).getApp()
                        .getCommuService().getForeign();
            }
        }
        return mServerUrl;
    }

    public String getForeignServerUrl() {
        if (solution == null) {
            initSolution();
        }
        return solution.getDefaultConfig().getApp().getCommuService()
                .getForeign();
    }

    public String getLocalServerUrl() {
        if (solution == null) {
            initSolution();
        }
        return solution.getDefaultConfig().getApp().getCommuService()
                .getLocal();
    }

    /**
     * 推送服务URL
     *
     * @return
     */
    public String getPushServerUrl() {
        Sub defSub = getDefSolution();
        // NET_STATE netState = checkNetState();
        if (ACCESS_NET_STATE == NET_STATE.NULL) {
            return "";
        } else if (ACCESS_NET_STATE == NET_STATE.LOCAL) {
            return defSub.getConfig().getApp().getPush().getLocal();
        } else {
            return defSub.getConfig().getApp().getPush().getForeign();
        }
    }

    /**
     * 登陆logo
     */
    public com.movementinsome.kernel.initial.model.Login getLoginVo() {
        if (solution == null) {
            initSolution();
        }
        if (defSolutionId == -1) {
            return null;
        }
        if (solution == null || solution.getDefaultConfig(defSolutionId) == null
                || solution.getDefaultConfig(defSolutionId).getApp() == null) {
            return null;
        } else {
            return solution.getDefaultConfig(defSolutionId).getApp().getLogin();
        }
    }

    /**
     * 系统自动定时启动
     *
     * @return
     */
    public AutoStart getAutoStart() {
        Sub defSub = getDefSolution();
        if (defSub == null) {
            return null;
        } else {
            return defSub.getConfig().getApp().getAutoStart();
        }
    }

    /**
     * 导出数据中的设施列表(能显示的)
     * @return
     */
    public List<Movetype> getMovetype() {
        List<Movetype> movetypes=new ArrayList<>();
        List<Movetype> movetypeList=getDefSolution().getConfig().getExport().getMovetype();
        if (movetypeList!=null&&movetypeList.size()>0){
            for (int i = 0; i < movetypeList.size(); i++) {
                if (movetypeList.get(i).getIsshow().equals("true")){
                    movetypes.add(movetypeList.get(i));
                }
            }
        }
        return movetypes;
    }

    /**
     * 城市列表七参数据
     * @return
     */
    public List<City> getCity() {
        return getDefSolution().getConfig().getCityList().getCityList();
    }

    /**
     * 获取地图设施
     * @return
     */
    public List<FacilityType> getFacilityType() {
        return getDefSolution().getConfig().getMapFacility().getFacilityTypes();
    }

    /**
     * 获取采测半径集合
     * @return
     */
    public List<Number> getSettingRanges() {
        return getDefSolution().getConfig().getSetting().getRange().get(0).getNumbers();
    }

    /**
     * 获取工程类型
     * @return
     */
    public List<ProjectType> getProjectType() {
        return getDefSolution().getConfig().getProject().getProjectTypes();
    }

    /**
     * 获取工程类型
     * @return
     */
    public List<View> getViews() {
        return getDefSolution().getConfig().getViews().getViewList();
    }

    /**
     * 获取某个view是否显隐
     * @param viewStringKey
     * @return
     */
    public int getViewIsShow(String viewStringKey){
        int viewVisible= android.view.View.GONE;
        List<View> viewList=getViews();
        if (viewList != null && viewList.size() > 0) {
            for (int i = 0; i < viewList.size(); i++) {
                if (viewList.get(i).getName().equals(viewStringKey)){
                    if (viewList.get(i).getIsShow().equals("true")){
                        viewVisible= android.view.View.VISIBLE;
                    }
                    return viewVisible;
                }
            }
        }
        return viewVisible;
    }

    /**
     * 导航偏离及到达提醒参数
     *
     * @return
     */
    public Navigation getNavigation() {
        Sub defSub = getDefSolution();
        if (defSub == null) {
            return null;
        } else {
            return defSub.getConfig().getApp().getNavigation();
        }
    }

    /**
     * 文件服务器URL
     *
     * @return
     */
    public String getFileServerUrl() {
        Sub defSub = getDefSolution();
        // NET_STATE netState = checkNetState();
        if (ACCESS_NET_STATE == NET_STATE.NULL) {
            return "";
        } else if (ACCESS_NET_STATE == NET_STATE.LOCAL) {
            return defSub.getConfig().getApp().getFileService().getLocal();
        } else {
            return defSub.getConfig().getApp().getFileService().getForeign();
        }
    }

    /**
     * 工单派放URL
     *
     * @return
     */
    public String getSendNewTaskUrl() {
        if (ACCESS_NET_STATE == NET_STATE.NULL) {
            return "";
        } else if (ACCESS_NET_STATE == NET_STATE.LOCAL) {
            return getDefSolution().getConfig().getApp().getSendNewTask().getLocal();
        } else {
            return getDefSolution().getConfig().getApp().getSendNewTask().getForeign();
        }
    }

    /**
     * 文件服务器信息
     *
     * @return
     */
    public FileService getFileServer() {
        return getDefSolution().getConfig().getApp().getFileService();
    }

    /**
     * 设施权限
     *
     * @return
     */
    public UserRight getUserRight() {
        return getDefSolution().getConfig().getApp().getUserRight();
    }

    public String getWorkorderManagementUrl() {
        Sub defSub = getDefSolution();
        if (defSub.getConfig().getApp().getWorkorderManagement() == null) {
            return "";
        }
        if (ACCESS_NET_STATE == NET_STATE.NULL) {
            return "";
        } else if (ACCESS_NET_STATE == NET_STATE.LOCAL) {
            return defSub.getConfig().getApp().getWorkorderManagement().getLocal();
        } else {
            return defSub.getConfig().getApp().getWorkorderManagement().getForeign();
        }
    }

    /**
     * 获取定位参数设置
     *
     * @return
     */
    public Locate getLocateConfig() {
        Sub defSub = getDefSolution();
        if (defSub != null) {
            return defSub.getConfig().getApp().getLocate();
        } else {
            return null;
        }
    }

    /**
     * 获取地图坐标系统
     *
     * @return
     */
    public String getSrid() {
        Sub defSub = getDefSolution();
        if (defSub != null) {
            return defSub.getConfig().getMap().getSrid();
        } else {
            return null;
        }
    }

    /**
     * @return
     */
    public CoordParam getCoordTransform() {
        Sub defSub = getDefSolution();
        if (defSub != null) {
            return defSub.getConfig().getMap().getCoordParam();
        } else {
            return null;
        }
    }

    public MapparentmenusVO getMapparentmenusVO() {
        Sub defSub = getDefSolution();
        if (defSub != null) {
            return defSub.getConfig().getMap().getMapparentmenusVO();
        } else {
            return null;
        }
    }

    public Task getTask(String id) {
        Sub defSub = getDefSolution();
        if (defSub != null) {
            return defSub.getConfig().getMap().findTask(id);
        } else {
            return null;
        }
    }

    //登录信息
    public UserBeanVO getCurUser() {
        if (null != user) {
            return this.user;
        } else {
            String userBean = pPrefere.getString(Constant.SPF_CUR_USER_BEAN, null);
            if (null != userBean) {
                Gson gson = new Gson();
                this.user = gson.fromJson(userBean, UserBeanVO.class);
                return this.user;
            }
            return null;
        }
    }

    public void setCurUser(UserBeanVO user) {
        if (null != user) {
            this.user = user;
            Gson gson = new Gson();
            pEditor.putString(Constant.SPF_CUR_USER_BEAN, gson.toJson(user)).commit();
        }
        //O
    }

    public int getAPP_MIN_CODE() {
        return APP_MIN_CODE;
    }

    public void setAPP_MIN_CODE(int aPP_MIN_CODE) {
        APP_MIN_CODE = aPP_MIN_CODE;
    }

    public String getCurUserName() {
        return pPrefere.getString(Constant.SPF_DEF_LOGIN_USER, "");
    }

    public void setCurUserName(String value) {
        pEditor.putString(Constant.SPF_DEF_LOGIN_USER, value).commit();
    }

    public String getUserPawword() {
        return pPrefere.getString(Constant.SPF_DEF_LOGIN_PASSWORD, "");
    }

    public void setUserPassword(String value) {
        pEditor.putString(Constant.SPF_DEF_LOGIN_PASSWORD, value).commit();
    }

    public void setRember(boolean value) {
        pEditor.putBoolean(Constant.SPF_AUTO_REMBER, value).commit();
    }

    public boolean isRember() {
        return pPrefere.getBoolean(Constant.SPF_AUTO_REMBER, false);
    }

    public void setAutoLogin(boolean value) {
        pEditor.putBoolean(Constant.SPF_AUTO_LOGIN, value).commit();
    }

    public String getVideopath() {
        return videopath;
    }

    public void setVideopath(String videopath) {
        this.videopath = videopath;
    }

    public boolean isAutoLogin() {
        return pPrefere.getBoolean(Constant.SPF_AUTO_LOGIN, false);
    }

    // 系统是否已经初始化
    public boolean isSystemInit() {
        if (mSysIniSuc) {
            return pPrefere.getBoolean(Constant.SPF_SYSTEM_INITED, false);
        } else {
            return false;
        }
    }

    public void setSystemInit(boolean ini) {
        pEditor.putBoolean(Constant.SPF_SYSTEM_INITED, ini).commit();
    }

    //版本信息
    public Integer getVersionCode() {
        return mVerCode;
    }

    public String getVersionName() {
        return mVerName;
    }

    /**
     * 通过心跳接口确定用什么IP地址进行访问
     *
     * @return
     */
    private NET_STATE checkNetState() {
        App app = null;
        if (null == solution) {
            return NET_STATE.NULL;
        }
        try {
            if (defSolutionId == -1)
                app = solution.getDefaultConfig().getApp();
            else
                app = solution.getDefaultConfig(defSolutionId).getApp();
        } catch (Exception ex) {
            if (solution.getSubs().size() > 0) {
                try {
                    app = solution.getSubs().get(0).getConfig().getApp();
                } catch (NullPointerException ex1) {
                    ex1.printStackTrace();
                }
            }
        }
        if (app != null) {
            if ("1".equals(checkUrl(app.getCommuService().getLocal()))) {
                return NET_STATE.LOCAL;
            } else if ("1".equals(checkUrl(app.getCommuService().getForeign()))) {
                return NET_STATE.FOEIGN;
            }
            ;
        } else {
            return NET_STATE.NULL;
        }
        return NET_STATE.NULL;
    }

    public boolean checkSysIniSuc() {
        return mSysIniSuc;
    }

    /**
     * 心跳测试
     *
     * @param url 连接服务器url地址
     * @return "1"连接通过,null无法连接
     */
    public String checkUrl(String url) {
        String msg;
        if (NetUtils.checkNetWork(this)) {
            WebAccessTools access = new WebAccessTools(this);
            return access.getWebContent(url + SpringUtil._REST_HEARTBEAT);
        } else {
            return null;
        }
    }

    public LocationInfoExt getCurLocation() {
        return curLocation;
    }

    public void setCurLocation(LocationInfoExt location) {
        curLocation = location;
    }

    public Handler getmHandle() {
        return mHandle;
    }

    public void setmHandle(Handler mHandle) {
        this.mHandle = mHandle;
    }

    public void setMapPosition(String value) {
        this.mMapPosition = value;
    }

    public String getMapPosition() {
        return mMapPosition;
    }

    public void setMapBound(String value) {
        this.mMapBound = value;
    }

    public String getMapBound() {
        return mMapBound;
    }

    public String getmMapArea() {
        return mMapArea;
    }

    public void setmMapArea(String mMapArea) {
        this.mMapArea = mMapArea;
    }

    public Point getmMapPoint() {
        return mMapPoint;
    }

    public void setmMapPoint(Point mMapPoint) {
        this.mMapPoint = mMapPoint;
    }

    public void setMapIden(IdentifyResult value) {
        this.mMapIden = value;
    }

    public IdentifyResult getmMapIden() {
        return this.mMapIden;
    }

    //获取访问服务网络模式:不连接,内网,外网
    public NET_STATE getAccNetState() {
        return ACCESS_NET_STATE;
    }

    public TabHost getmHost() {
        return mHost;
    }

    public void setmHost(TabHost mHost) {
        this.mHost = mHost;
    }

    public List<TextView> getTvs() {
        return tvs;
    }

    public void setTvs(List<TextView> tvs) {
        this.tvs = tvs;
    }

    public List<ImageView> getImgs() {
        return imgs;
    }

    public void setImgs(List<ImageView> imgs) {
        this.imgs = imgs;
    }

    public ProjectVo getProjectVo() {
        return projectVo;
    }

    public void setProjectVo(ProjectVo projectVo) {
        this.projectVo = projectVo;
    }

    public Dao<ProjectVo,Long> getProjectVoDao() throws SQLException {
        if (projectVoDao==null){
            projectVoDao=getAppDbHelper().getDao(ProjectVo.class);
        }
        return projectVoDao;
    }

    public Dao<SavePointVo,Long> getSavePointVoDao() throws SQLException {
        if (savePointVoDao==null){
            savePointVoDao=getAppDbHelper().getDao(SavePointVo.class);
        }
        return savePointVoDao;
    }

    /**
     * 长供
     */
/*	public void trancoor(){
		CoordParam cp = new CoordParam();
		cp.setId(0);
		cp.setCoordinate("54");
		
		cp.setSdx(-109.999016);
		cp.setSdy(241.019612);
		cp.setSdz(160.957851);
		cp.setSqx(-3.70086503312727);
		cp.setSqy(3.88029478808162);
		cp.setSqz(3.02091309933372);
		cp.setSscale(-0.000031609159);
		
		cp.setPprojectionType(3);
		cp.setPscale(1);
		cp.setPbenchmarklatitude(0.00);
		cp.setPconstantx(0);
		cp.setPconstanty(500000);
		cp.setPcentralmeridian(114);
		cp.setSemimajor(6378245.000);
		cp.setFlattening(1.0/298.3);
	
		Gps2Locale.RevTranslate(3385166.302408000,520463.029528000, 0, cp);
		
		
		ITranslateCoordinate tc = new Gps2LocaleCoordinate();
		Point pt = tc.GPS2MapPoint(30.585583608,114.213342369,0,cp,null);
		System.out.println(pt.getX()+","+pt.getY());
	}*/
    public String getPipeBroAnalysisResult() {
        return pipeBroAnalysisResult;
    }

    public void setPipeBroAnalysisResult(String pipeBroAnalysisResult) {
        this.pipeBroAnalysisResult = pipeBroAnalysisResult;
    }

    /**
     * 宜春7+4
     */
    public void trancoor() {
        CoordParam cp = new CoordParam();
        cp.setId("0");
        cp.setCoordinate("80");

        cp.setSdx(4007982.949612);
        cp.setSdy(-9617702.254959);
        cp.setSdz(-4450178.395148);
        cp.setSqx(-811.607727528421);
        cp.setSqy(-717.438375922015);
        cp.setSqz(-2068.79757952497);
        cp.setSscale(1.777270143062);

        cp.setFdx(0);
        cp.setFdy(0);
        cp.setFrotateangle(-0.0048295512);
        cp.setFscale(0);


        cp.setPprojectionType(3);
        cp.setPscale(1);
        cp.setPbenchmarklatitude(0.00);
        cp.setPconstantx(-43.09);
        cp.setPconstanty(502898.62);
        cp.setPcentralmeridian(112.833333333);
        cp.setSemimajor(6378245);
        cp.setFlattening(0.003352329869);


        //Gps2Locale.RevTranslate(76395.6491,101581.011, 0, cp);

        ITranslateCoordinate tc = new Gps2LocaleCoordinate();
        //Point pt = tc.GPS2MapPoint(76395.6491,101581.011,0,cp,null);
        Point pt = tc.GPS2MapPoint(23.1738272902, 112.4943729308, -4.555, cp, null);
        System.out.println(pt.getX() + "," + pt.getY());
    }
	
/*	//路线导航模拟测试数据
	private LocationInfoExt[] loadRoadNavTestData() throws IOException{
		
		
		InputStream inputfile = this.getAssets().open("trace.csv");//存储到新文件的路径
		  try {
			   InputStreamReader isr = new InputStreamReader(inputfile);//待处理数据的文件路径
			   BufferedReader reader = new BufferedReader(isr);
			   //BufferedWriter bw = new BufferedWriter(new FileWriter(outfile));
			   int i=0;
			   String line;
			   while((line=reader.readLine())!=null){
				   if (i>0){
					   String item[] = line.split(",");
					   LocationInfoExt location = new LocationInfoExt();
					   location.setLatitude(Double.parseDouble(item[2].trim()));
					   location.setLongitude(Double.parseDouble(item[3].trim()));
					   location.setAltitude(Double.parseDouble(item[4].trim()));
					   location.setAccuracy(Float.parseFloat(item[5].trim()));
					   location.setMapx(Double.parseDouble(item[9].trim()));
					   location.setMapy(Double.parseDouble(item[10].trim()));
					   locations[i-1]=location;
				   }
				   i++;
			   }

		  } catch (FileNotFoundException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  
		return locations;
	}
	
	public LocationInfoExt[] getRoadNavTestData(){
		return locations;
	}*/
}