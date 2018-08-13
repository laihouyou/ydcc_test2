package com.movementinsome.map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.BaiduAppProxy;
import com.baidu.mapapi.bikenavi.BikeNavigateHelper;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.walknavi.WalkNavigateHelper;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.pano.PanoDemoMain;
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.util.Arith;
import com.movementinsome.app.pub.view.MaxHeightListView;
import com.movementinsome.caice.async.ShowMakreTask;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.okhttp.ProjectRequest;
import com.movementinsome.caice.util.BaiduMapUtil;
import com.movementinsome.caice.util.Bd09toArcgis;
import com.movementinsome.caice.util.ConstantDate;
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.DateUtil;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.util.UUIDUtil;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.CityVo;
import com.movementinsome.caice.vo.LatlogVo;
import com.movementinsome.caice.vo.MarkerDetailsVO;
import com.movementinsome.caice.vo.MiningSurveyVO;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.commonAdapter.adapterView.CommonListViewAdapter;
import com.movementinsome.commonAdapter.adapterView.ViewHolder;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.initial.model.Facility;
import com.movementinsome.kernel.initial.model.FacilityType;
import com.movementinsome.kernel.initial.model.ProjectType;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.kernel.location.coordinate.Gcj022Gps;
import com.movementinsome.kernel.util.ActivityUtil;
import com.movementinsome.map.view.MyMapView;
import com.pop.android.common.util.ToastUtils;
import com.zhd.communication.DeviceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.movementinsome.caice.util.MapMeterMoveScope.DELETELINE;
import static com.movementinsome.caice.util.MapMeterMoveScope.MODIFYLIE;
import static com.movementinsome.kernel.location.coordinate.Gcj022Bd09.bd09Encrypt;

public class MapViewer extends ContainActivity implements View.OnClickListener,
        OnGetGeoCoderResultListener, BaiduMap.OnMarkerClickListener,
        BaiduMap.OnMarkerDragListener,
        BaiduMap.OnMapClickListener, BaiduMap.OnMyLocationClickListener,
        View.OnTouchListener, AdapterView.OnItemClickListener,
        BaiduMap.OnPolylineClickListener, BaiduMap.OnMapStatusChangeListener {

    String flag = this.getClass().getName();

    private MyMapView arcMapview;

    public static final String TAG = ContentValues.TAG;
    public static final String MOVE_MINING_POINT = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/MoveMiningPoint/";
    public static final String MOVE_MINING_LINE = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/MoveMiningLine/";

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LatlogVo latlogVo;

    boolean isFirstLoc = true; // 是否首次定位

    private RelativeLayout reBMapview;
    private List<String> list;
    private Marker marker;
    private CreateFiles createFiles;
    private String pointAddress = new String();
    private GeoCoder mGeoCoder = null;
    private AlertDialog dialog;
    private static LatLng cenpt;    //bd09类型坐标
    private  LatLng cenpt_wgs84;    //wgs84类型坐标
    private Thread thread;
    private BitmapDescriptor bitmap;
    private MarkerOptions markerOption;
    private TextOptions textOption;


    private OkHttpRequest okHttpRequest;

    private MarkerDetailsVO markerVoQue = new MarkerDetailsVO();

    private List<Overlay> mOverlayList = null;//已完成的点图层
    private List<Polyline> mBaidPolyline;

    private String SpinnerStr;
    //	private String[] mType = {"电力", "电信", "给水", "排水", "燃气", "工业", "热力", "综合"};
    private List<String> mType;
    private List<String> symbolList;
    private String[] latlngType = {OkHttpParam.WGS84, OkHttpParam.GCJ02};
    private ImageButton moveGatherDelect;
    private ImageButton moveOpentask;
    private AlertDialog pointDialog;

    public static final String GCJ02 = "gcj02";
    public static final String BD09 = "bd09";
    public static final String ARCGIS = "地方平面坐标";
    private String pointTypees = GCJ02;

    private BaseAdapter moveListAdapter;
    private ListView movelist;

    private TraceReceiver receiver;

    private static List<LatLng> linePointList = new ArrayList();// 连续采点的坐标
    private static List<LatLng> lineList = new ArrayList();// 画图的坐标
    private static List<String> markerIds = new ArrayList();// 点连线的marker 的ID，也是对应百度云的ID
    private static List<String> facLines = new ArrayList();// 设施点的点线表号集合
    private static List<String> facIds = new ArrayList();// 设施点的id集合
    private static List<Boolean> isFarsts = new ArrayList();// 设施点的点线表号集合
    private static List<Overlay> markerOverlayList = new ArrayList<Overlay>();
    private static List<Overlay> lineOverlayList = new ArrayList<Overlay>();//线集合
    private static List<Overlay> linePointOverlayList = new ArrayList<Overlay>();//连续采点线集合
    private static Map<String, SavePointVo> pointVoMap = new HashMap<>();
    private Overlay pointOver;

    private List<List<String>> pointIdSegment;
    private List<List<String>> drawTypesSeg;

    private boolean isPointOrLine;

    private View viewLine = null;

    private View viewPoint = null;

    private String implementorName = Constant.LEAK;
    private String solutionName = "";

    private LatLng touchLatLng;

    private boolean isPointLine = false;

    private String projectId = "";

    private SharedPreferences.Editor editor;

    private  MapViewer context;
    private ShowMakreTask showMakreTask;
    private Dao<MiningSurveyVO, Long> miningSurveyVOdao = null;
    private Dao<SavePointVo, Long> savePointVoLongDao = null;

    private MiningSurveyVO currentProject;      //当前工程
    private String acquisitionState = MapMeterMoveScope.CHECK;   //当前采集状态

    private FrameLayout pointChangeLineBtn;    //切换
    private TextView abnormal_remind;    //异常提醒
    private FrameLayout abnormalFram;    //异常提醒
    private TextView map_changer;    //地图切换
    private TextView coordinate_point;    //坐标点
    private TextView draw_point;    //手绘点
    private TextView continuity_point;    //连续点
    private TextView continuity_line;    //连续线
    private TextView point_connect_line;    //设施点连线
    private TextView delete_point;    //删除设施点
    private TextView tvMoveTitle;    //工程名称
    private TextView over_projcet;    //结束工程
    private TextView locationBuon;    //定位
    private TextView input_line;    //采集完成，录入线数据
    private TextView property;    //填写属性
    private TextView revocation_line;    //撤销
    private TextView go_to_the;    //到这去
    private TextView panorama;    //全景图
    private TextView loading_hint;    //定位提示
    private FrameLayout new_projcet;//新建工程
    private TextView accuratePoint;//精准点
    private ImageView map_blow_up;//放大
    private ImageView map_shrink;//放大

    private FrameLayout facility;//左边的设施
    private MaxHeightListView facilityListview;
    private CommonListViewAdapter facilityAdapter;

    private float currentZoomLevel = 0;
    private float maxZoomLevel = 0;
    private float minZoomLevel = 0;

    private CustomDialog customDialog;
    private CustomDialog customDialog1;
    private CustomDialog.Builder builder;

    private EditText moveName;
    private EditText moveType;
    private EditText moveSharedCode;
    private LinearLayout lineGone;
    private Spinner moveSpinnerType;

    private LatLng pointLatlng;
    private LocationInfoExt locationInfoExt;

    private String parameter = MapMeterMoveScope.MOVE;
    private List<SavePointVo> checkPointVoList;

    private Bundle lineBundle;

    private EditText moveLog;
    private EditText moveLat;
    private Spinner moveInputType;

    private List<DynamicFormVO> dynamicFormList;
    private Dao<DynamicFormVO, Long> dynamicFormDao = null;

    private String gatherType = "";     //采集方式类型
    private String isSuccession = ConstantDate.ISSUCCESSION_NO;  //是否是连续采集
    private double longitude_wg84=0;
    private double latitude_wg84=0;

    private List<String> facilityTextList;
    private List<Integer> facilityImageList0;
    private List<Integer> facilityImageList1;
    private List<FacilityType> facilityTypes;

    private LatLng markerLatlng;    //地图点击的设施点坐标

    private boolean isSubmit;
    private ProgressDialog progressDialog;

    private List<Facility> facilitiePoint;

    private List<ProjectType> projectTypes;

    private ProjectRequest projectRequest;

    private List<String> cityListStr;
    private List<CityVo> cityList;
    private CityVo cityVo;      //当前选中城市七参
    private Dao<CityVo,Long> cityVoDao;

    private EditText record_name;
    private EditText SDx;
    private EditText SDy;
    private EditText SDz;
    private EditText SQx;
    private EditText SQy;
    private EditText SQz;
    private EditText SScale;
    private EditText FDx;
    private EditText FDy;
    private EditText FScale;
    private EditText FRotateangle;
    private EditText PCentralmeridian;
    private EditText PScale;
    private EditText PConstantx;
    private EditText PConstanty;
    private EditText PBenchmarklatitude;
    private EditText Semimajor;
    private EditText Flattening;

    private int pointChangeLineBtn_isshow;      //控制显隐配置
    private int coordinate_point_isshow;      //控制显隐配置
    private int draw_point_isshow;      //控制显隐配置
    private int continuity_point_isshow;      //控制显隐配置
    private int continuity_line_isshow;      //控制显隐配置
    private int accuratePoint_isshow;      //控制显隐配置
    private int point_connect_line_isshow;      //控制显隐配置 点连线

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_map_activity3);
        context = this;
        initView();
        try {
            initOther();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.baiduMap);
        mBaiduMap = mMapView.getMap();

        // 不显示地图缩放控件（按钮控制栏）
        mMapView.showZoomControls(false);

        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setOnMapStatusChangeListener(this);


        reBMapview = (RelativeLayout) findViewById(R.id.reBMapview);

        createFiles = new CreateFiles();

        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);

        arcMapview = (MyMapView) findViewById(R.id.myMapView);//
        arcMapview.setMapPopupWindow(this);
        //加载上一次地图范围
        arcMapview.loadLastMapExtent();

        viewLine = View.inflate(MapViewer.this, R.layout.move_mining_line, null);
        viewPoint = View.inflate(MapViewer.this, R.layout.move_mining_single, null);

        //切换
        pointChangeLineBtn = (FrameLayout) findViewById(R.id.pointChangeLineBtn);
        pointChangeLineBtn.setOnClickListener(this);
        //连续采集录入线数据
        input_line = (TextView) findViewById(R.id.input_line);
        input_line.setOnClickListener(this);
        //填写属性
        property = (TextView) findViewById(R.id.property);
        property.setOnClickListener(this);
        //撤销
        revocation_line = (TextView) findViewById(R.id.revocation_line);
        revocation_line.setOnClickListener(this);
        //到这去
        go_to_the = (TextView) findViewById(R.id.go_to_the);
        go_to_the.setOnClickListener(this);
        //全景图
        panorama = (TextView) findViewById(R.id.panorama);
        panorama.setOnClickListener(this);
        //定位提示
        loading_hint = (TextView) findViewById(R.id.loading_hint);
        loading_hint.setOnClickListener(this);
        //新建工程
        new_projcet = (FrameLayout) findViewById(R.id.new_projcet);
        new_projcet.setOnClickListener(this);
        //左边的设施
        facility = (FrameLayout) findViewById(R.id.facility);
        facility.setOnClickListener(this);
        //精准点
        accuratePoint = (TextView) findViewById(R.id.accuratePoint);
        accuratePoint.setOnClickListener(this);
//        //更多
//        move = (ImageView) findViewById(R.id.move);
//        move.setOnClickListener(this);
        //异常提醒
        abnormal_remind = (TextView) findViewById(R.id.abnormal_remind);
        abnormal_remind.setOnClickListener(this);
        //异常提醒
        abnormalFram = (FrameLayout) findViewById(R.id.abnormalFram);
        //地图提醒
        map_changer = (TextView) findViewById(R.id.map_changer);
        map_changer.setOnClickListener(this);
        //坐标点
        coordinate_point = (TextView) findViewById(R.id.coordinate_point);
        coordinate_point.setOnClickListener(this);
        //手绘点
        draw_point = (TextView) findViewById(R.id.draw_point);
        draw_point.setOnClickListener(this);
        //手绘点
        continuity_point = (TextView) findViewById(R.id.continuity_point);
        continuity_point.setOnClickListener(this);
        //手绘点
        continuity_line = (TextView) findViewById(R.id.continuity_line);
        continuity_line.setOnClickListener(this);
        //设施点连线
        point_connect_line = (TextView) findViewById(R.id.point_connect_line);
        point_connect_line.setOnClickListener(this);
        //删除设施点
        delete_point = (TextView) findViewById(R.id.delete_point);
        delete_point.setOnClickListener(this);
        //工程名称
        tvMoveTitle = (TextView) findViewById(R.id.tvMoveTitle);
        tvMoveTitle.setOnClickListener(this);
        //结束工程
        over_projcet = (TextView) findViewById(R.id.over_projcet);
        over_projcet.setOnClickListener(this);
        //定位
        locationBuon = (TextView) findViewById(R.id.locationBuon);
        locationBuon.setOnClickListener(this);
        //放大
        map_blow_up = (ImageView) findViewById(R.id.map_blow_up);
        map_blow_up.setOnClickListener(this);
        //缩小
        map_shrink = (ImageView) findViewById(R.id.map_shrink);
        map_shrink.setOnClickListener(this);
        //左边的设施列表
        facilityListview = (MaxHeightListView) findViewById(R.id.facilityListview);
        facilityListview.setOnItemClickListener(this);


        /**
         * Maker的拖拽事件
         */
        mBaiduMap.setOnMarkerDragListener(this);


    }

    private void initOther() throws SQLException, IOException {
        // 注册广播
        receiver = new TraceReceiver();
        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Constant.SERVICE_NAME);
        filter1.addAction(Constant.PASSDATE);
        filter1.addAction(Constant.DEVICE_IS_CONNECTED);
        filter1.addAction(Constant.UNIT_EXCEPTION);
//        filter1.addAction(Constant.PROJECT_CREATE);
        registerReceiver(receiver, filter1);

        setLocationData(null);

        EventBus.getDefault().register(this);

        mOverlayList = new ArrayList<>();
        okHttpRequest = new OkHttpRequest(this, mBaiduMap, mOverlayList);

        projectRequest = new ProjectRequest();

        mBaidPolyline = new ArrayList<>();

        miningSurveyVOdao = AppContext
                .getInstance().getAppDbHelper()
                .getDao(MiningSurveyVO.class);

        savePointVoLongDao =
                AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);

        dynamicFormDao = AppContext.getInstance()
                .getAppDbHelper().getDao(DynamicFormVO.class);

        cityVoDao=AppContext.getInstance().getAppDbHelper().getDao(CityVo.class);

        builder = new CustomDialog.Builder(this);

        cityVo=new CityVo();

        facilityTypes = AppContext.getInstance().getFacilityType();
        Log.i("facilityTypes", facilityTypes.toString());

        List<Facility> facilitiePoints = facilityTypes.get(0).getFacilities();

        facilityTextList = new ArrayList<>();
        facilityImageList0 = new ArrayList<>();
        facilityImageList1 = new ArrayList<>();
        facilitiePoint = new ArrayList<>();

        IntegratedFacData(facilitiePoints);

        facilityAdapter = new CommonListViewAdapter<String>(context, R.layout.facility_left_list, facilityTextList) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.fac_tv, item);
                viewHolder.setBackgroundRes(R.id.fac_im, facilityImageList0.get(position));
                if (defaultSelection == position) {     //选中
                    viewHolder.setTextColorRes(R.id.fac_tv, R.color.cornflowerblue6);
                    viewHolder.setBackgroundRes(R.id.fac_im, facilityImageList1.get(position));
                } else {     //未选中
                    viewHolder.setTextColorRes(R.id.fac_tv, R.color.point_facility);
                    viewHolder.setBackgroundRes(R.id.fac_im, facilityImageList0.get(position));

                }
            }
        };

        facilityListview.setAdapter(facilityAdapter);
        facilityAdapter.setSelectPosition(0);   //设置第一个高亮

        progressDialog = new ProgressDialog(context);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);

        mType = new ArrayList<>();
        symbolList = new ArrayList<>();
        projectTypes = AppContext.getInstance().getProjectType();
        if (projectTypes != null && projectTypes != null) {
            for (int i = 0; i < projectTypes.size(); i++) {
                mType.add(projectTypes.get(i).getName());
                symbolList.add(projectTypes.get(i).getSymbol());
            }
        }

        Map<String, Object> map = new HashMap<>();
        map.put(OkHttpParam.USED_ID,  AppContext.getInstance().getCurUser().getUserId());
        map.put(OkHttpParam.PROJECT_STATUS, "1");
        List<MiningSurveyVO> miningSurveyVOs = miningSurveyVOdao.queryForFieldValues(map);
        if (miningSurveyVOs.size() == 1) {
//            GatherMove(miningSurveyVOs.get(0),true);
            //获取服务器数据
            ProjectRequest.ProjectList( context, miningSurveyVOs.get(0));
        } else {
            //获取服务器数据
            ProjectRequest.ProjectList( context, null);
        }

        cityListStr=new ArrayList<>();
        getQiCanCityData();

        List<com.movementinsome.kernel.initial.model.View> viewList=AppContext.getInstance().getViews();
        if (viewList!=null&&viewList.size()>0){
            View viewLatout=getLayoutInflater().inflate(R.layout.main_map_activity3,null);
            ViewGroup viewGroup= (ViewGroup) viewLatout;

            for (int i = 0; i < viewList.size(); i++) {
                com.movementinsome.kernel.initial.model.View view=viewList.get(i);
                if (view.getName().equals("切换")){
                    if (view.getIsShow().equals("true")){
                        pointChangeLineBtn_isshow=View.VISIBLE;
                    }else if (view.getIsShow().equals("false")){
                        pointChangeLineBtn_isshow=View.GONE;
                    }
                }
                if (view.getName().equals("坐标点")){
                    if (view.getIsShow().equals("true")){
                        coordinate_point_isshow=View.VISIBLE;
                    }else if (view.getIsShow().equals("false")){
                        coordinate_point_isshow=View.GONE;
                    }
                }
                if (view.getName().equals("手绘点")){
                    if (view.getIsShow().equals("true")){
                        draw_point_isshow=View.VISIBLE;
                    }else if (view.getIsShow().equals("false")){
                        draw_point_isshow=View.GONE;
                    }
                }
                if (view.getName().equals("连续点")){
                    if (view.getIsShow().equals("true")){
                        continuity_point_isshow=View.VISIBLE;
                    }else if (view.getIsShow().equals("false")){
                        continuity_point_isshow=View.GONE;
                    }
                }
                if (view.getName().equals("连续线")){
                    if (view.getIsShow().equals("true")){
                        continuity_line_isshow=View.VISIBLE;
                    }else if (view.getIsShow().equals("false")){
                        continuity_line_isshow=View.GONE;
                    }
                }
                if (view.getName().equals("精准点")){
                    if (view.getIsShow().equals("true")){
                        accuratePoint_isshow=View.VISIBLE;
                    }else if (view.getIsShow().equals("false")){
                        accuratePoint_isshow=View.GONE;
                    }
                }
                if (view.getName().equals("点连线")){
                    if (view.getIsShow().equals("true")){
                        point_connect_line_isshow=View.VISIBLE;
                    }else if (view.getIsShow().equals("false")){
                        point_connect_line_isshow=View.GONE;
                    }
                }
            }
        }

    }


    private void getQiCanCityData() throws SQLException {
        cityListStr.clear();
        cityList=cityVoDao.queryForAll();
        if (cityList!=null){
            for (int i = 0; i < cityList.size(); i++) {
                if (cityList.get(i).getCityName()!=null&&!cityList.get(i).getCityName().equals("")){
                    cityListStr.add(cityList.get(i).getCityName());
                }
            }
        }
    }

    private void IntegratedFacData(List<Facility> facilitiePoints) {
        facilityTextList.clear();
        facilityImageList0.clear();
        facilityImageList1.clear();
        facilitiePoint.clear();

        if (facilitiePoints != null && facilitiePoints.size() > 0) {
            for (int i = 0; i < facilitiePoints.size(); i++) {
                if (facilitiePoints.get(i).getIsshow().equals("true")) {     //是否显示

                    facilitiePoint.add(facilitiePoints.get(i));

                    //文字
                    facilityTextList.add(facilitiePoints.get(i).getName());

                    int resID0 = 0;
                    int resID1 = 0;
                    //图片
                    if (facilitiePoints.get(i).getIcon() != null && !facilitiePoints.get(i).getIcon().equals("")) {
                        resID0 = this.getResources().getIdentifier(facilitiePoints.get(i).getIcon() + 0, "drawable",
                                this.getPackageName());
                        resID1 = this.getResources().getIdentifier(facilitiePoints.get(i).getIcon() + 1, "drawable",
                                this.getPackageName());
                    } else {
                        resID0 = R.drawable.valve0;
                        resID1 = R.drawable.valve1;
                    }
                    facilityImageList0.add(resID0);
                    facilityImageList1.add(resID1);

                }
            }

            if (facilityAdapter != null) {
                facilityAdapter.setmDatas(facilityTextList);
                facilityAdapter.setSelectPosition(0);
            }
            if (facilitiePoint != null && facilitiePoint.size() > 0) {
                solutionName = facilitiePoint.get(0).getTableName();  //设置第一个设施的 solution名字
            }
        }
    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus) {

    }

    @Override
    public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

    }

    @Override
    public void onMapStatusChange(MapStatus mapStatus) {
        maxZoomLevel = mBaiduMap.getMaxZoomLevel();
        minZoomLevel = mBaiduMap.getMinZoomLevel();

        currentZoomLevel = mapStatus.zoom;

        if (currentZoomLevel >= maxZoomLevel) {
            currentZoomLevel = maxZoomLevel;
        } else if (currentZoomLevel <= minZoomLevel) {
            currentZoomLevel = minZoomLevel;
        }

        if (currentZoomLevel == maxZoomLevel) {
            //设置地图缩放等级为上限
            MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(currentZoomLevel);
            mBaiduMap.animateMapStatus(u);
            map_blow_up.setEnabled(false);
        } else if (currentZoomLevel == minZoomLevel) {
            //设置地图缩放等级为下限
            MapStatusUpdate u = MapStatusUpdateFactory.zoomTo(currentZoomLevel);
            mBaiduMap.animateMapStatus(u);
            map_shrink.setEnabled(false);
        } else {
            if (!map_blow_up.isEnabled() || !map_shrink.isEnabled()) {
                map_blow_up.setEnabled(true);
                map_shrink.setEnabled(true);
            }
        }
    }

    @Override
    public void onMapStatusChangeFinish(MapStatus mapStatus) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        facilityAdapter.setSelectPosition(position);
        if (pointChangeLineBtn.getTag().equals(MapMeterMoveScope.POINT)) {
            implementorName = facilityTextList.get(position);
            solutionName = facilitiePoint.get(position).getTableName();
        } else if (pointChangeLineBtn.getTag().equals(MapMeterMoveScope.LINE)) {
            implementorName = facilityTextList.get(position);
            solutionName = facilitiePoint.get(position).getTableName();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.locationBuon:
                isFirstLoc = true;
                setLocationData(null);
                //获取marker数
                if (cenpt != null && currentProject != null && acquisitionState != null) {
                    if (!projectId.equals("")){
                        OkHttpRequest.getLatlogData(cenpt, currentProject, acquisitionState);
                    }
                }
                break;

            case R.id.new_projcet:    //新建工程

                customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_mining_item)
                        .heightpx(ActivityUtil.getWindowsHetght(this))
                        .widthpx(ActivityUtil.getWindowsWidth(MapViewer.this))
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.confirmBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    if (moveName.getText().toString() != null && !moveName.getText().toString().equals("") &&
                                            moveType.getText().toString() != null && !moveType.getText().toString().equals("")) {

                                        MiningSurveyVO miningSurveyVO = new MiningSurveyVO();
                                        miningSurveyVO.setProjectName(moveName.getText().toString());
                                        miningSurveyVO.setProjectId(UUID.randomUUID().toString());
                                        miningSurveyVO.setProjectNum(UUIDUtil.getUUID());
                                        miningSurveyVO.setProjectType(SpinnerStr);
                                        miningSurveyVO.setProjectSDateStr(DateUtil.getNow());
                                        miningSurveyVO.setProjectEDateUpd(DateUtil.getNow());
                                        miningSurveyVO.setTaskNum(moveType.getText().toString());
                                        miningSurveyVO.setUsedName(AppContext.getInstance().getCurUserName());
                                        miningSurveyVO.setAutoNumber(1);
                                        miningSurveyVO.setAutoNumberLine(1);
                                        miningSurveyVO.setShareCode(moveSharedCode.getText().toString());
                                        if (moveSharedCode.getText().toString().equals("")) {
                                            miningSurveyVO.setIsProjectShare("1");
                                        } else {
                                            miningSurveyVO.setIsProjectShare("0");
                                        }
                                        miningSurveyVO.setIsSubmit("0");
                                        miningSurveyVO.setIsCompile("0");
                                        miningSurveyVO.setUsedId(AppContext.getInstance().getCurUser().getUserId());
                                        if (cityVo!=null){
                                            miningSurveyVO.setQicanStr(new Gson().toJson(cityVo));
                                        }

                                        //向服务器请求
                                        ProjectRequest.ProjectCreate(miningSurveyVO, MapViewer.this, false);
                                    } else {
                                        Toast.makeText(context, "工程名称或任务编号不能为空", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .build();
                customDialog.show();

                View view = customDialog.getView();

                final LinearLayout share_lin_account= (LinearLayout) view.findViewById(R.id.share_lin_account);
                final LinearLayout share_lin_code= (LinearLayout) view.findViewById(R.id.share_lin_code);

                RadioGroup share_radiogroup= (RadioGroup) view.findViewById(R.id.share_radiogroup);
                share_radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        if (checkedId==R.id.radioButton_share_no){
                            share_lin_account.setVisibility(View.GONE);
                            share_lin_code.setVisibility(View.GONE);
                        }else if (checkedId==R.id.radioButton_share_yes){
                            share_lin_account.setVisibility(View.VISIBLE);
                            share_lin_code.setVisibility(View.VISIBLE);
                        }
                    }
                });

                final Spinner qican_pulldown= (Spinner) view.findViewById(R.id.qican_pulldown);
                final ArrayAdapter<String> qican_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityListStr);
                //设置下拉列表的风格
                qican_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //将adapter 添加到spinner中
                qican_pulldown.setAdapter(qican_adapter);
                qican_pulldown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        cityVo=cityList.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                Button manual_write_btn= (Button) view.findViewById(R.id.manual_write_btn);
                manual_write_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String[] areaStr = new String[1];

                        customDialog1 = builder.cancelTouchout(false)
                                .view(R.layout.qican_write_dialog)
                                .heightpx(ActivityUtil.getWindowsHetght(MapViewer.this))
                                .widthpx(ActivityUtil.getWindowsWidth(MapViewer.this))
                                .style(R.style.dialog)
                                .addViewOnclick(R.id.cancelIm, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (customDialog1.isShowing()) {
                                            customDialog1.dismiss();
                                        }
                                    }
                                })
                                .addViewOnclick(R.id.confirmBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (ifNoNull()){
                                            try {
                                                Dao<CityVo,Long> cityDao=AppContext.getInstance()
                                                        .getAppDbHelper().getDao(CityVo.class);
                                                CityVo cityVo=new CityVo();
                                                cityVo.setId(UUIDUtil.getUUID());
                                                cityVo.setCityName(record_name.getText().toString());
                                                cityVo.setSdx(Double.parseDouble(SDx.getText().toString()));
                                                cityVo.setSdy(Double.parseDouble(SDy.getText().toString()));
                                                cityVo.setSdz(Double.parseDouble(SDz.getText().toString()));
                                                cityVo.setSqx(Double.parseDouble(SQx.getText().toString()));
                                                cityVo.setSqy(Double.parseDouble(SQy.getText().toString()));
                                                cityVo.setSqz(Double.parseDouble(SQz.getText().toString()));
                                                cityVo.setSscale(Double.parseDouble(SScale.getText().toString()));
                                                cityVo.setFdx(Double.parseDouble(FDx.getText().toString()));
                                                cityVo.setFdy(Double.parseDouble(FDy.getText().toString()));
                                                cityVo.setFscale(Double.parseDouble(FScale.getText().toString()));
                                                cityVo.setFrotateangle(Double.parseDouble(FRotateangle.getText().toString()));
                                                cityVo.setPcentralmeridian(Double.parseDouble(PCentralmeridian.getText().toString()));
                                                cityVo.setPscale(Double.parseDouble(PScale.getText().toString()));
                                                cityVo.setPconstantx(Double.parseDouble(PConstantx.getText().toString()));
                                                cityVo.setPconstanty(Double.parseDouble(PConstanty.getText().toString()));
                                                cityVo.setPbenchmarklatitude(Double.parseDouble(PBenchmarklatitude.getText().toString()));
                                                cityVo.setSemimajor(Double.parseDouble(Semimajor.getText().toString()));
                                                cityVo.setFlattening(Double.parseDouble(Flattening.getText().toString()));
                                                cityVo.setPprojectionType(Long.parseLong(areaStr[0]));

                                                int s=cityDao.create(cityVo);
                                                if (s==1){
                                                    ToastUtils.showToast(context,"新增成功");
                                                    context.getQiCanCityData();
                                                    qican_adapter.notifyDataSetChanged();
                                                    for (int i = 0; i < cityList.size(); i++) {
                                                        if (cityList.get(i).getId().equals(cityVo.getId())){
                                                            qican_pulldown.setSelection(i);
                                                            break;
                                                        }
                                                    }
                                                    if (customDialog1.isShowing()) {
                                                        customDialog1.dismiss();
                                                    }
                                                }
                                            } catch (SQLException e) {
                                                e.printStackTrace();
                                            }
                                        }else {
                                            ToastUtils.showToast(context,context.getResources()
                                                    .getString(R.string.please_enter_all_parameters));
                                        }
                                    }

                                    private boolean ifNoNull() {
                                        if (
                                                !record_name.getText().toString().equals("") &&
                                                        !SDx.getText().toString().equals("") &&
                                                        !SDy.getText().toString().equals("") &&
                                                        !SDz.getText().toString().equals("") &&
                                                        !SQx.getText().toString().equals("") &&
                                                        !SQy.getText().toString().equals("") &&
                                                        !SQz.getText().toString().equals("") &&
                                                        !SScale.getText().toString().equals("") &&
                                                        !FDx.getText().toString().equals("") &&
                                                        !FDy.getText().toString().equals("") &&
                                                        !FScale.getText().toString().equals("") &&
                                                        !FRotateangle.getText().toString().equals("") &&
                                                        !PCentralmeridian.getText().toString().equals("") &&
                                                        !PScale.getText().toString().equals("") &&
                                                        !PConstantx.getText().toString().equals("") &&
                                                        !PConstanty.getText().toString().equals("") &&
                                                        !PBenchmarklatitude.getText().toString().equals("") &&
                                                        !Semimajor.getText().toString().equals("") &&
                                                        !Flattening.getText().toString().equals("")
                                                ) {
                                            return true;
                                        }else {
                                            return false;
                                        }
                                    }
                                })
                                .build();
                        customDialog1.show();

                        View qican_view = customDialog1.getView();
                        record_name = (EditText) qican_view.findViewById(R.id.record_name);
                        SDx = (EditText) qican_view.findViewById(R.id.SDx);
                        SDy = (EditText) qican_view.findViewById(R.id.SDy);
                        SDz = (EditText) qican_view.findViewById(R.id.SDz);
                        SQx = (EditText) qican_view.findViewById(R.id.SQx);
                        SQy = (EditText) qican_view.findViewById(R.id.SQy);
                        SQz = (EditText) qican_view.findViewById(R.id.SQz);
                        SScale = (EditText) qican_view.findViewById(R.id.SScale);
                        FDx = (EditText) qican_view.findViewById(R.id.FDx);
                        FDy = (EditText) qican_view.findViewById(R.id.FDy);
                        FScale = (EditText) qican_view.findViewById(R.id.FScale);
                        FRotateangle = (EditText) qican_view.findViewById(R.id.FRotateangle);
                        PCentralmeridian = (EditText) qican_view.findViewById(R.id.PCentralmeridian);
                        PScale = (EditText) qican_view.findViewById(R.id.PScale);
                        PConstantx = (EditText) qican_view.findViewById(R.id.PConstantx);
                        PConstanty = (EditText) qican_view.findViewById(R.id.PConstanty);
                        PBenchmarklatitude = (EditText) qican_view.findViewById(R.id.PBenchmarklatitude);
                        Semimajor = (EditText) qican_view.findViewById(R.id.Semimajor);
                        Flattening = (EditText) qican_view.findViewById(R.id.Flattening);


                        final Spinner area = (Spinner) qican_view.findViewById(R.id.area);
                        area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                areaStr[0] = area.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                });

                moveName = (EditText) view.findViewById(R.id.moveName);
                moveType = (EditText) view.findViewById(R.id.moveType);
                moveSharedCode = (EditText) view.findViewById(R.id.moveSharedCode);
                lineGone = (LinearLayout) view.findViewById(R.id.lineGone);
                moveSpinnerType = (Spinner) view.findViewById(R.id.moveSpinnerType);
                SpinnerStr = mType.get(0);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mType);
                //设置下拉列表的风格
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                //将adapter 添加到spinner中
                moveSpinnerType.setAdapter(adapter);
                moveSpinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // TODO Auto-generated method stub
                        SpinnerStr = mType.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub

                    }
                });
                final List<String> taskNums = new ArrayList<>();
                try {
                    List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOdao.
                            queryForAll();
                    for (MiningSurveyVO miningSurveyVO : miningSurveyVOList) {
                        taskNums.add(miningSurveyVO.getTaskNum());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                break;


            case R.id.accuratePoint:        //精准点
                if (!DeviceManager.getInstance().isConnected()) {
//                    abnormal_remind.setText("当前使用" + locationInfoExt.getLocationModel() + "定位"+
//                            "\n"+ "解状态:"+locationInfoExt.getQualityStr()+
//                            "\n"+ "经度:"+locationInfoExt.getLongitudeWg84()+
//                            "\n"+ "纬度:"+locationInfoExt.getLatitudeWg84()+
//                            "\n"+ "卫星数量:"+locationInfoExt.getSolutionUsedSats()+
//                            "\n"+ "海拔:"+locationInfoExt.getAltitude()+
//                            "\n"+ "精度:"+locationInfoExt.getAccuracy()
//                    );
                } else {
                    if (locationInfoExt != null) {
                        if (locationInfoExt.getQuality() == 0) {
                            abnormal_remind.setText("设备异常，当前使用" + locationInfoExt.getLocationModel() + "定位"
                            );
                        } else {
                            abnormal_remind.setText("当前使用" + locationInfoExt.getLocationModel() + "定位"
                            );
                        }
                    }
                }

                switch ((String) pointChangeLineBtn.getTag()) {
                    case MapMeterMoveScope.POINT:       //精准采点
                        if (cenpt != null) {
                            if (continuity_point.getTag().toString().equals("yes")) {    //说明是连续采点
                                consecutiveCollection(cenpt,cenpt_wgs84, "精准采点");
                            } else if (continuity_line.getTag().equals("yes")) {        //连续线
                                consecutiveCollection(cenpt,cenpt_wgs84, "精准采点");
                            } else {             //普通
                                showMarker(cenpt,cenpt_wgs84, "精准采点");
                            }
                        }
                        break;
                    case MapMeterMoveScope.LINE:       //精准采线
                        if (cenpt != null) {
                            //绘制线
                            showLine(cenpt, "精准采线", "");
                        }
                        break;
                }

                MapStatus.Builder builder1 = new MapStatus.Builder();
                builder1.target(cenpt);
                builder1.zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));

                break;

            case R.id.map_blow_up:        //放大
                float zoomLevel = mBaiduMap.getMapStatus().zoom;
                if (zoomLevel <= 21) {
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomIn());    //已动画的形式改变地图状态
                    map_blow_up.setEnabled(true);
                } else {
                    Toast.makeText(context, "已经放至最大！", Toast.LENGTH_SHORT).show();
                    map_blow_up.setEnabled(false);
                }
                break;

            case R.id.map_shrink:        //缩小
                float zoomLevel1 = mBaiduMap.getMapStatus().zoom;
                if (zoomLevel1 > 2) {
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomOut());    //已动画的形式改变地图状态
                    map_shrink.setEnabled(true);
                } else {
                    map_shrink.setEnabled(false);
                    Toast.makeText(context, "已经缩至最小！", Toast.LENGTH_SHORT).show();
                }
                break;


            case R.id.map_changer:        //地图切换
                if (mBaiduMap.getMapType() == BaiduMap.MAP_TYPE_NORMAL) {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                } else {
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                }

                break;

            case R.id.pointChangeLineBtn:        //采点采线切换
                if (linePointList.size() > 0) {
                    cleanLinePoint(R.id.pointChangeLineBtn);
                } else {
                    pointChangeLineBtn_click();
                }
                break;
            case R.id.coordinate_point:        //坐标点
                final String pointType = "";
                switch ((String) pointChangeLineBtn.getTag()) {
                    case MapMeterMoveScope.POINT:        //坐标点
                        customDialog = builder.cancelTouchout(false)
                                .view(R.layout.move_input_point)
                                .heightpx(ActivityUtil.getWindowsHetght(this))
                                .widthpx(ActivityUtil.getWindowsWidth(this))
                                .style(R.style.dialog)
                                .addViewOnclick(R.id.confirmInputBtn, new View.OnClickListener() {    //确定
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            MapStatus.Builder builder = new MapStatus.Builder();

                                            if (moveLog.getText().toString().equals("") || moveLat.getText().toString().equals("")) {
                                                Toast.makeText(context, "经纬度不能为空", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            ;

                                            double edlog = Double.parseDouble(moveLog.getText().toString());
                                            double edlat = Double.parseDouble(moveLat.getText().toString());
                                            switch (SpinnerStr) {
                                                case "WGS84":
                                                    Map<String, Double> localHashMap = Gcj022Gps.wgs2gcj(edlog, edlat);
                                                    LatLng latlng1 = new LatLng(localHashMap.get("lat"), localHashMap.get("lon"));

                                                    if (continuity_point.getTag().toString().equals("yes")) {    //说明是连续采点
                                                        consecutiveCollection(latlng1, "坐标采点",edlog,edlat);
                                                    }
//                                                    if (continuity_line.getTag().equals("yes")){
//                                                        consecutiveCollection(latlng1, "坐标采点");
//                                                    }
//                                                    if (draw_point.getTag().equals("yes")){
//                                                        showMarker(latlng1, "坐标采点");
//                                                    }
                                                    else {
                                                        showMarker(latlng1, "坐标采点",edlog,edlat);
                                                    }

                                                    builder.target(latlng1);
                                                    builder.zoom(18.0f);
                                                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                                    customDialog.dismiss();
                                                    break;
                                                case "GCJ02":
                                                    Map<String, Double> localHashMap1 = Gcj022Gps.gcj2wgs(edlog, edlat);
                                                    LatLng latlng = new LatLng(edlat, edlog);

                                                    if (continuity_point.getTag().toString().equals("yes")) {    //说明是连续采点
                                                        consecutiveCollection(latlng, "坐标采点",localHashMap1.get("lon"),localHashMap1.get("lat"));
                                                    }
//                                                    if (continuity_line.getTag().equals("yes")){
//                                                        consecutiveCollection(latlng, "坐标采点");
//                                                    }
//                                                    if (draw_point.getTag().equals("yes")){
//                                                        showMarker(latlng, "坐标采点");
//                                                    }
                                                    else {
                                                        showMarker(latlng, "坐标采点",localHashMap1.get("lon"),localHashMap1.get("lat"));
                                                    }

                                                    builder.target(latlng);
                                                    builder.zoom(18.0f);
                                                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                                    customDialog.dismiss();
                                                    break;
                                            }
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .addViewOnclick(R.id.cancelInputIm, new View.OnClickListener() {        //取消
                                    @Override
                                    public void onClick(View v) {
                                        customDialog.dismiss();
                                    }
                                })
                                .build();
                        customDialog.show();

                        View inputView = customDialog.getView();
                        moveLog = (EditText) inputView.findViewById(R.id.moveLog);
                        moveLat = (EditText) inputView.findViewById(R.id.moveLat);
                        moveInputType = (Spinner) inputView.findViewById(R.id.moveInputType);

                        moveLog.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        moveLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        SpinnerStr = latlngType[0];
                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, latlngType);
                        //设置下拉列表的风格
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //将adapter 添加到spinner中
                        moveInputType.setAdapter(adapter1);
                        moveInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int position, long id) {
                                // TODO Auto-generated method stub
                                SpinnerStr = latlngType[position].toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // TODO Auto-generated method stub

                            }
                        });

                        break;
                    case MapMeterMoveScope.LINE:        //坐标线
                        customDialog = builder.cancelTouchout(false)
                                .view(R.layout.move_input_point)
                                .heightpx(ActivityUtil.getWindowsHetght(this))
                                .widthpx(ActivityUtil.getWindowsWidth(this))
                                .style(R.style.dialog)
                                .addViewOnclick(R.id.confirmInputBtn, new View.OnClickListener() {    //确定
                                    @Override
                                    public void onClick(View v) {
                                        try {
                                            MapStatus.Builder builder = new MapStatus.Builder();

                                            if (moveLog.getText().toString().equals("") || moveLat.getText().toString().equals("")) {
                                                Toast.makeText(context, "经纬度不能为空", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            double edlog = Double.parseDouble(moveLog.getText().toString());
                                            double edlat = Double.parseDouble(moveLat.getText().toString());
                                            switch (SpinnerStr) {
                                                case "wg84":
                                                    Map<String, Double> localHashMap = Gcj022Gps.wgs2gcj(edlog, edlat);
                                                    double lonlat1[] = bd09Encrypt(localHashMap.get("lat"), localHashMap.get("lon"));
                                                    LatLng latlng1 = new LatLng(lonlat1[1], lonlat1[0]);

                                                    builder.target(latlng1);
                                                    builder.zoom(18.0f);
                                                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                                    customDialog.dismiss();

                                                    //绘制线
                                                    showLine(latlng1, "坐标采线", "");

                                                    break;
                                                case "gcj02":
                                                    double lonlat[] = bd09Encrypt(edlat, edlog);
                                                    LatLng latlng = new LatLng(lonlat[1], lonlat[0]);

                                                    builder.target(latlng);
                                                    builder.zoom(18.0f);
                                                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                                                    customDialog.dismiss();

                                                    //绘制线
                                                    showLine(latlng, "坐标采线", "");
                                                    break;
                                            }
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .addViewOnclick(R.id.cancelInputIm, new View.OnClickListener() {        //取消
                                    @Override
                                    public void onClick(View v) {
                                        customDialog.dismiss();
                                    }
                                })
                                .build();
                        customDialog.show();

                        View inputViewline = customDialog.getView();
                        moveLog = (EditText) inputViewline.findViewById(R.id.moveLog);
                        moveLat = (EditText) inputViewline.findViewById(R.id.moveLat);
                        moveInputType = (Spinner) inputViewline.findViewById(R.id.moveInputType);

                        moveLog.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        moveLat.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                        SpinnerStr = latlngType[0];
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, latlngType);
                        //设置下拉列表的风格
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //将adapter 添加到spinner中
                        moveInputType.setAdapter(adapter2);
                        moveInputType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> parent,
                                                       View view, int position, long id) {
                                // TODO Auto-generated method stub
                                SpinnerStr = latlngType[position].toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // TODO Auto-generated method stub

                            }
                        });
                        break;
                }
                break;

            case R.id.draw_point:        //手绘点
                if (linePointList.size() > 0) {
                    cleanLinePoint(R.id.draw_point);
                } else {
                    draw_point_click();

                }
                break;

            case R.id.continuity_point:         //连续采点
                if (linePointList.size() > 0) {
                    cleanLinePoint(R.id.continuity_point);
                } else {
                    continuity_point_click();
                }
                break;

            case R.id.continuity_line:      //连续线
                if (linePointList.size() > 0) {
                    cleanLinePoint(R.id.continuity_line);
                } else {
                    continuity_line_click();
                }

                break;

            case R.id.point_connect_line:       //设施点连线
                switch ((String) point_connect_line.getTag()) {
                    case "yes":     //设施点连线

                        /**
                         * 百度事件
                         */

                        mBaiduMap.setOnMapClickListener(null);
                        mBaiduMap.setOnPolylineClickListener(null);
                        mBaiduMap.removeMarkerClickListener(this);

                        point_connect_line.setTag("no");
                        point_connect_line.setBackgroundResource(R.drawable.point_connect_line_blck);
                        point_connect_line.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                        //将采集模式改为查看模式
                        parameter = MapMeterMoveScope.CHECK;

                        if (markerOverlayList != null) {
                            for (int i = 0; i < markerOverlayList.size(); i++) {
                                markerOverlayList.get(i).remove();
                            }
                        }
                        if (lineOverlayList != null) {
                            for (int i = 0; i < lineOverlayList.size(); i++) {
                                lineOverlayList.get(i).remove();
                            }
                        }
                        if (linePointOverlayList != null) {
                            for (int i = 0; i < linePointOverlayList.size(); i++) {
                                linePointOverlayList.get(i).remove();
                            }
                        }
                        lineOverlayList.clear();
                        linePointOverlayList.clear();
                        lineList.removeAll(lineList);
                        markerIds.removeAll(markerIds);

                        if (marker != null) {
                            marker.remove();
                            mBaiduMap.hideInfoWindow();
                        }

                        break;

                    case "no":      //普通

                        mBaiduMap.setOnMapClickListener(null);
                        mBaiduMap.setOnPolylineClickListener(null);
                        mBaiduMap.setOnMarkerClickListener(this);

                        point_connect_line.setTag("yes");
                        point_connect_line.setBackgroundResource(R.drawable.point_connect_line_blue);
                        point_connect_line.setTextColor(getResources().getColor(R.color.cornflowerblue9));

                        draw_point.setTag("no");
                        draw_point.setBackgroundResource(R.drawable.draw_point);
                        draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                        //将采集模式改为采集模式
                        parameter = MapMeterMoveScope.MOVE;

                        if (markerOverlayList != null) {
                            for (int i = 0; i < markerOverlayList.size(); i++) {
                                markerOverlayList.get(i).remove();
                            }
                        }
                        if (lineOverlayList != null) {
                            for (int i = 0; i < lineOverlayList.size(); i++) {
                                lineOverlayList.get(i).remove();
                            }
                        }
                        if (linePointOverlayList != null) {
                            for (int i = 0; i < linePointOverlayList.size(); i++) {
                                linePointOverlayList.get(i).remove();
                            }
                        }
                        lineOverlayList.clear();
                        linePointOverlayList.clear();
                        lineList.removeAll(lineList);
                        markerIds.removeAll(markerIds);

                        if (marker != null) {
                            marker.remove();
                            mBaiduMap.hideInfoWindow();
                        }

                        break;
                }

                break;

            case R.id.input_line:       //录入连续采集管线信息
                if (linePointList != null && linePointList.size() >= 2) {
                    subLine(linePointList, markerIds);
                }
                break;

            case R.id.property:        //填写属性
                Intent newFormInfo;
                HashMap<String, String> params;

                switch ((String) property.getTag()) {
                    case MapMeterMoveScope.MOVE:    //采集模式
                        switch ((String) pointChangeLineBtn.getTag()) {
                            case MapMeterMoveScope.POINT:        //采集点
                                newFormInfo = new Intent(context, RunForm.class);
                                params = new HashMap<String, String>();
                                if (pointLatlng != null) {
                                    params.put("longitude", pointLatlng.longitude + "");
                                    params.put("latitude", pointLatlng.latitude + "");

                                    //地方坐标
                                    com.esri.core.geometry.Point point= Bd09toArcgis
                                            .bd09ToArcgis(pointLatlng,currentProject.getCoordTransform());
                                    params.put(OkHttpParam.MAP_X,point.getX()+"");
                                    params.put(OkHttpParam.MAP_Y,point.getY()+"");

                                    if (longitude_wg84==0&&latitude_wg84==0){
                                        Map<String,Double> point_wg84=Bd09toArcgis.bd09ToWg84(pointLatlng);
                                        params.put(OkHttpParam.LONGITUDE_WG84,point_wg84.get("lon")+"");
                                        params.put(OkHttpParam.LATITUDE_WG84,point_wg84.get("lat")+"");
                                    }else {
                                        params.put(OkHttpParam.LONGITUDE_WG84,longitude_wg84+"");
                                        params.put(OkHttpParam.LATITUDE_WG84,latitude_wg84+"");

                                    }

                                }

                                params.put("groundElevation", locationInfoExt.getAltitude() + "");        //海拔，地面高程
                                params.put("drawType", MapMeterMoveScope.POINT);        //绘制类型
                                params.put("happenAddr", pointAddress);        //地址
                                params.put("isEdit", MapMeterMoveScope.GATHER);        //是否编辑
                                params.put("gatherType", gatherType);        //采集类型
                                params.put("isSuccession", isSuccession);        //是否是连续采集

                                params.put("implementorName", implementorName);        //采集类型
                                params.put(OkHttpParam.LOCATION_JSON, new Gson().toJson(locationInfoExt));        //定位vo 数据集合
                                if (!projectId.equals("")) {
                                    params.put(OkHttpParam.PROJECT_ID, projectId);        //工程ID

                                    try {
                                        int pointSize = 0;
                                        //点击时查询当前工程有多少数据
                                        Map<String, Object> map = new HashMap<>();
                                        map.put("usedName", AppContext.getInstance().getCurUserName());
                                        map.put("projectId", projectId);
                                        List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOdao.queryForFieldValues(map);
                                        if (miningSurveyVOList.size() == 1) {
                                            if (miningSurveyVOList.get(0).getShareCode() != null &&
                                                    !miningSurveyVOList.get(0).getShareCode().equals("")) {
                                                params.put(OkHttpParam.SHARE_CODE, miningSurveyVOList.get(0).getShareCode());        //共享码
                                                params.put(OkHttpParam.IS_PROJECT_SHARE, "0");    //表示共享
                                                params.put(OkHttpParam.PROJECT_NAME, miningSurveyVOList.get(0).getProjectName());    //工程名字
                                                params.put(OkHttpParam.PROJECT_TYPE, miningSurveyVOList.get(0).getProjectType());    //工程类型
                                            } else {
                                                params.put(OkHttpParam.SHARE_CODE, miningSurveyVOList.get(0).getShareCode());        //共享码
                                                params.put(OkHttpParam.IS_PROJECT_SHARE, "1");    //表示不共享
                                                params.put(OkHttpParam.PROJECT_NAME, miningSurveyVOList.get(0).getProjectName());    //工程名字
                                                params.put(OkHttpParam.PROJECT_TYPE, miningSurveyVOList.get(0).getProjectType());    //工程类型
                                            }
                                            String projectType = miningSurveyVOList.get(0).getProjectType();
                                            String taskNum = miningSurveyVOList.get(0).getTaskNum();
                                            String projectNum = UUIDUtil.getUUID();

                                            pointSize = miningSurveyVOList.get(0).getAutoNumber();        //自动编号

                                            if (projectType != null) {

                                                int pos = mType.lastIndexOf(projectType);
                                                params.put(OkHttpParam.FAC_NUM, projectNum+"_"+symbolList.get(pos) + taskNum + pointSize);
                                            }
                                        }
                                    } catch (SQLException e) {
                                        e.printStackTrace();
                                    }
                                }
                                newFormInfo.putExtra("iParams", params);
//                                newFormInfo.putExtra("template", solutionName);

                                if (continuity_point.getTag().equals("yes")) {
                                    if (dynamicFormList != null && dynamicFormList.size() > 0) {
                                        newFormInfo.putExtra("isPointLinkLine", true);   //用来判断是不是点连线，是则需要
                                        newFormInfo.putExtra("id", dynamicFormList.get(0).getId());
                                        newFormInfo.putExtra("template", dynamicFormList.get(0).getForm());
                                    } else {
                                        newFormInfo.putExtra("template", solutionName);
                                    }
                                } else if (continuity_line.getTag().equals("yes")) {
                                    if (dynamicFormList != null && dynamicFormList.size() > 0) {
                                        newFormInfo.putExtra("isPointLinkLine", true);   //用来判断是不是点连线，是则需要
                                        newFormInfo.putExtra("id", dynamicFormList.get(0).getId());
                                        newFormInfo.putExtra("template", dynamicFormList.get(0).getForm());
                                    } else {
                                        newFormInfo.putExtra("template", solutionName);
                                    }
                                } else {
                                    newFormInfo.putExtra("template", solutionName);
                                }
                                startActivityForResult(newFormInfo, 5);
                                break;
                            case MapMeterMoveScope.LINE:        //采集线
                                if (lineList.size() == 1) {
                                    Toast.makeText(this, R.string.move_msg, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                if (lineList != null && lineList.size() >= 2) {
                                    subLine(lineList, markerIds);
                                }
                                break;

                        }

                        break;

                    case MapMeterMoveScope.CHECK:    //查看点属性
                        try {
                            if (checkPointVoList != null && checkPointVoList.size() == 1) {
                                SavePointVo savePointVo = checkPointVoList.get(0);
//                                if (savePointVo.getUsedId().equals(AppContext.getInstance().getCurUser().getUserId())){
//                                    ProjectVo miningSurveyVO = null;
//                                    List<ProjectVo> miningSurveyVOList = miningSurveyVOdao.
//                                            queryForEq(OkHttpParam.PROJECT_ID, projectId);
//                                    if (miningSurveyVOList != null && miningSurveyVOList.size() > 0) {
//                                        miningSurveyVO = miningSurveyVOList.get(0);
//
//                                    }
//                                    if (dynamicFormList != null && dynamicFormList.size() > 0) {
//
//                                        newFormInfo = new Intent(context, RunForm.class);
//                                        params = new HashMap<String, String>();
//                                        params.put("drawType", MapMeterMoveScope.POINT);        //绘制类型
//                                        params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据
//                                        newFormInfo.putExtra("iParams", params);
//
//                                        Bundle bundle = new Bundle();
//                                        bundle.putSerializable("editPointVo", savePointVo);
//                                        newFormInfo.putExtra("pointLineBundle", bundle);
//
//                                        newFormInfo.putExtra("id", dynamicFormList.get(0).getId());
//                                        newFormInfo.putExtra("template", dynamicFormList.get(0).getForm());
//                                        newFormInfo.putExtra("miningSurveyVO", miningSurveyVO != null ? miningSurveyVO : new ProjectVo());
//                                        startActivity(newFormInfo);
//                                    } else {     //说明本地表单为空，不是本机保存的，是从服务器下来的数据
//
//                                        //入本地表单数据库
//                                        DynamicFormVO dynamicFormVO=new DynamicFormVO();
//                                        dynamicFormVO.setId(savePointVo.getDynamicFormVOId());
//                                        dynamicFormVO.setGuid(savePointVo.getGuid());
//                                        dynamicFormVO.setContent(savePointVo.getContext());
//                                        dynamicFormVO.setForm(solutionName);
//                                        int s=dynamicFormDao.create(dynamicFormVO);
//                                        if (s==1){
//                                            newFormInfo = new Intent(context, RunForm.class);
//                                            params = new HashMap<String, String>();
//                                            params.put("drawType", MapMeterMoveScope.POINT);        //绘制类型
//                                            params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据
//                                            newFormInfo.putExtra("iParams", params);
//
//                                            Bundle bundle = new Bundle();
//                                            bundle.putSerializable("editPointVo", savePointVo);
//                                            newFormInfo.putExtra("pointLineBundle", bundle);
//
//                                            newFormInfo.putExtra("id", dynamicFormVO.getId());
//                                            newFormInfo.putExtra("template", dynamicFormVO.getForm());
//                                            newFormInfo.putExtra("miningSurveyVO", miningSurveyVO != null ? miningSurveyVO : new ProjectVo());
//                                            startActivity(newFormInfo);
//                                        }
//
//                                        Toast.makeText(context, "不是本机数据无法编辑", Toast.LENGTH_SHORT).show();
//                                    }
//                                }else {
                                    //跳转新表单,确定探漏信息
                                    newFormInfo = new Intent(context, RunForm.class);

                                    String saveJson=JSON.toJSONString(savePointVo);
                                    Map map= JSON.parseObject(saveJson);
                                    params=new HashMap<>(map);
                                    Log.i("tag",map.toString());

                                    params.put("drawType", MapMeterMoveScope.POINT);        //绘制类型
                                    params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据
                                    params.put("type", OkHttpParam.LEAK);  //是否是确定探漏点

                                    //如果该用户已经确定过,则获取已经探漏过得数据
                                    List<SavePointVo> savePointVoList=savePointVoLongDao
                                            .queryForEq(OkHttpParam.PATROL_ID,savePointVo.getId());
                                    if (savePointVoList.size()==1){
                                        String context_leak=savePointVoList.get(0).getContext_leak();
                                        Map map1=JSON.parseObject(context_leak);
                                        params.putAll(map1);
                                    }

                                    if (cenpt != null) {
                                        params.put("longitude_leak", cenpt.longitude + "");
                                        params.put("latitude_leak", cenpt.latitude + "");
                                    }

                                    params.put(OkHttpParam.LOCATION_JSON, new Gson().toJson(locationInfoExt));        //定位vo 数据集合

                                    if (currentProject!=null){
                                        params.put(OkHttpParam.PROJECT_ID, currentProject.getProjectId());        //工程ID
                                        params.put(OkHttpParam.SHARE_CODE, currentProject.getShareCode());        //共享码
                                        params.put(OkHttpParam.IS_PROJECT_SHARE, currentProject.getIsProjectShare());    //表示共享
                                        params.put(OkHttpParam.PROJECT_NAME, currentProject.getProjectName());    //工程名字
                                        params.put(OkHttpParam.PROJECT_TYPE, currentProject.getProjectType());    //工程类型
                                    }

                                    newFormInfo.putExtra("iParams",params);
                                    newFormInfo.putExtra("template", "ins_data_entry_inspect_leak.xml");
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("editPointVo", savePointVo);
                                    if (savePointVoList.size()==1){
                                        bundle.putSerializable("savePointVo_leak", savePointVoList.get(0));
                                    }
                                    newFormInfo.putExtra("pointLineBundle", bundle);
                                    startActivity(newFormInfo);

//                                }
                            } else {
                                Toast.makeText(context, "不是本机数据无法编辑", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case MODIFYLIE:    //查看线属性
                        final String facNum = lineBundle.getString("facNum");
                        final String drawType = lineBundle.getString("drawType");
                        final String facNumLine = lineBundle.getString("projectId");
                        final ArrayList<String> facNumLines = lineBundle.getStringArrayList("facNumLines");
                        final ArrayList<String> facDrawNum = lineBundle.getStringArrayList("facDrawNum");//绘制顺序
                        final ArrayList<String> facDrawNums = lineBundle.getStringArrayList("facDrawNums");//绘制顺序集合
                        final ArrayList<String> ids = lineBundle.getStringArrayList("ids");

                        List<SavePointVo> saveLineVoList = null;
                        if (ids != null && ids.size() > 0) {
                            for (int i = 0; i < ids.size(); i++) {
                                try {
                                    saveLineVoList = savePointVoLongDao.queryForEq("id", ids.get(i));
//									saveLineVoList=saveLineVoLongDao.queryForAll();
                                    if (saveLineVoList != null && saveLineVoList.size() == 1) {
                                        break;
                                    }
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (saveLineVoList != null && saveLineVoList.size() == 1) {
                                try {
                                    SavePointVo savePointVo = saveLineVoList.get(0);
                                    if (savePointVo.getUsedId().equals(AppContext.getInstance().getCurUser().getUserId())){
                                        QueryBuilder<DynamicFormVO, Long> queryBuilder = dynamicFormDao.queryBuilder();
                                        Where<DynamicFormVO, Long> where = queryBuilder.where();
                                        where.isNotNull("form");
                                        where.and();
                                        where.eq("id", savePointVo.getDynamicFormVOId());
                                        dynamicFormList = dynamicFormDao.query(queryBuilder.prepare());
                                        if (dynamicFormList != null && dynamicFormList.size() == 1) {
                                            newFormInfo = new Intent(context, RunForm.class);
                                            params = new HashMap<String, String>();
                                            params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据params.put("savePointVoId",savePointVo.getId());  //编辑对应的数据ID
                                            params.put("drawType", MapMeterMoveScope.LINE);        //绘制类型
                                            newFormInfo.putExtra("iParams", params);

                                            Bundle bundle = new Bundle();
                                            bundle.putStringArrayList("ids", ids);
                                            bundle.putSerializable("editLineVo", savePointVo);
                                            bundle.putString("lineOrpointline", MapMeterMoveScope.POINT);      //判断提交的是线还是点线
                                            newFormInfo.putExtra("pointLineBundle", bundle);

                                            newFormInfo.putExtra("id", dynamicFormList.get(0).getId());
                                            newFormInfo.putExtra("template", dynamicFormList.get(0).getForm());
                                            startActivityForResult(newFormInfo, 5);

                                        } else {
                                            DynamicFormVO dynamicFormVO=new DynamicFormVO();
                                            dynamicFormVO.setId(savePointVo.getDynamicFormVOId());
                                            dynamicFormVO.setGuid(savePointVo.getGuid());
                                            dynamicFormVO.setContent(savePointVo.getContext());
                                            dynamicFormVO.setForm(solutionName);
                                            int s=dynamicFormDao.create(dynamicFormVO);
                                            if (s==1){
                                                newFormInfo = new Intent(context, RunForm.class);
                                                params = new HashMap<String, String>();
                                                params.put("isEdit", MapMeterMoveScope.ISEDIT);  //是否是编辑数据params.put("savePointVoId",savePointVo.getId());  //编辑对应的数据ID
                                                params.put("drawType", MapMeterMoveScope.LINE);        //绘制类型
                                                newFormInfo.putExtra("iParams", params);

                                                Bundle bundle = new Bundle();
                                                bundle.putStringArrayList("ids", ids);
                                                bundle.putSerializable("editLineVo", savePointVo);
                                                bundle.putString("lineOrpointline", MapMeterMoveScope.POINT);      //判断提交的是线还是点线
                                                newFormInfo.putExtra("pointLineBundle", bundle);

                                                newFormInfo.putExtra("id", dynamicFormVO.getId());
                                                newFormInfo.putExtra("template", dynamicFormVO.getForm());
                                                startActivityForResult(newFormInfo, 5);
                                            }

                                        }
                                    }else {
                                        Toast.makeText(context, "不是本机数据无法编辑", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(context, "不是本机数据无法编辑", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }

                break;

            case R.id.over_projcet:        //暂停工程
                if (linePointList.size() > 0) {
                    cleanLinePoint(R.id.over_projcet);
                } else {
                    over_projcet_click(false);
                }
                break;

            case R.id.abnormal_remind:    //设备异常提示
                if (abnormal_remind.getVisibility() == View.VISIBLE) {
                    abnormal_remind.setVisibility(View.GONE);
                    abnormalFram.setVisibility(View.GONE);
                }
                break;

            case R.id.delete_point:        //删除
                switch ((String) delete_point.getTag()) {
                    case MapMeterMoveScope.CHECK:        //删除点
                        customDialog = builder.cancelTouchout(false)
                                .view(R.layout.move_mining_over_dlaio)
                                .heightpx(ActivityUtil.getWindowsHetght(this))
                                .widthpx(ActivityUtil.getWindowsWidth(this))
                                .setTitle("删除提醒")
                                .setMsg("是否删除？")
                                .style(R.style.dialog)
                                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (checkPointVoList != null && checkPointVoList.size() == 1) {
                                            SavePointVo savePointVo = checkPointVoList.get(0);
                                            String markreId = savePointVo.getMarkerId();
                                            //删除百度云
                                            okHttpRequest.DeleteMarkerPoint(cenpt, markreId, currentProject, acquisitionState);

                                            revocation_line.setVisibility(View.GONE);
                                            delete_point.setVisibility(View.GONE);
                                            property.setVisibility(View.GONE);
                                            go_to_the.setVisibility(View.GONE);
                                            panorama.setVisibility(View.GONE);

                                            customDialog.dismiss();

                                        } else {
                                            Toast.makeText(context, "不是本机数据无法删除", Toast.LENGTH_SHORT).show();
                                            customDialog.dismiss();
                                        }
                                    }
                                })
                                .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        customDialog.dismiss();
                                    }
                                })
                                .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        customDialog.dismiss();
                                    }
                                })
                                .build();
                        customDialog.show();
                        break;

                    case DELETELINE:        //删除线
                        customDialog = builder.cancelTouchout(false)
                                .view(R.layout.move_mining_over_dlaio)
                                .heightpx(ActivityUtil.getWindowsHetght(this))
                                .widthpx(ActivityUtil.getWindowsWidth(this))
                                .setTitle("删除提醒")
                                .setMsg("是否删除？")
                                .style(R.style.dialog)
                                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        final ArrayList<String> ids = lineBundle.getStringArrayList("ids");

                                        String idStr = "";
                                        if (ids != null && ids.size() > 0) {
                                            for (int i = 0; i < ids.size(); i++) {
                                                if (i == ids.size() - 1) {
                                                    idStr += ids.get(i);
                                                } else {
                                                    idStr += ids.get(i) + ",";
                                                }
                                            }
                                        }

                                        okHttpRequest.DeleteMarkerPoint(cenpt, idStr, currentProject, acquisitionState);

                                        //提交成功后把以下属性隐藏
                                        revocation_line.setVisibility(View.GONE);
                                        delete_point.setVisibility(View.GONE);
                                        property.setVisibility(View.GONE);
                                        go_to_the.setVisibility(View.GONE);
                                        panorama.setVisibility(View.GONE);

                                        customDialog.dismiss();

                                    }
                                })
                                .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        customDialog.dismiss();
                                    }
                                })
                                .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        customDialog.dismiss();
                                    }
                                })
                                .build();
                        customDialog.show();
                        break;

                }

                break;

            case R.id.revocation_line:  //撤销
                if (continuity_point.getTag().equals("yes")) {
                    mBaiduMap.hideInfoWindow();

                    if (marker != null) {
                        marker.remove();
                    }

                    revocation_line.setVisibility(View.GONE);
                    property.setVisibility(View.GONE);

                    if (linePointList.size() >= 2) {
                        input_line.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (markerOverlayList != null && markerOverlayList.size() > 0) {
                        markerOverlayList.get(markerOverlayList.size() - 1).remove();//移除集合的marker点
                        markerOverlayList.remove(markerOverlayList.size() - 1);
                    }
                    if (lineList != null && lineList.size() > 0) {
                        lineList.remove(lineList.size() - 1);//移除集合里的坐标点
                    }
                    if (markerIds != null && markerIds.size() > 0) {
                        markerIds.remove(markerIds.size() - 1);//移除集合里的坐标点
                    }
                    if (facIds != null && facIds.size() > 0) {
                        facIds.remove(facIds.size() - 1);//移除集合里的坐标点
                    }
                    if (lineOverlayList != null && lineOverlayList.size() > 0) {
                        lineOverlayList.get(lineOverlayList.size() - 1).remove();
                        lineOverlayList.remove(lineOverlayList.size() - 1);
                    }
                    mBaiduMap.hideInfoWindow();
                    if (lineList.size() < 1) {
                        revocation_line.setVisibility(View.GONE);
                        property.setVisibility(View.GONE);

                    }
                }

                break;

            case R.id.go_to_the:    //到这去
//                if (cenpt != null) {
//                    progressDialog.setMessage("加载中,请稍后...");
//                    progressDialog.show();
//                    BaiduAppProxy.CallBaiduNavigationLatLng(
//                            (Activity) context,
//                            BNRoutePlanNode.CoordinateType.BD09LL,
//                            cenpt,
//                            markerLatlng);
//                }

                final BikeNavigateHelper mNaviHelper;
                WalkNavigateHelper mWNaviHelper;

                mNaviHelper = BikeNavigateHelper.getInstance();
                mWNaviHelper = WalkNavigateHelper.getInstance();

                customDialog1 = builder.cancelTouchout(false)
                        .view(R.layout.item_nav_dialog)
                        .heightpx(ActivityUtil.getWindowsHetght(MapViewer.this))
                        .widthpx(ActivityUtil.getWindowsWidth(MapViewer.this))
                        .style(R.style.dialog)
                        .addViewOnclick(R.id.tv_driving_search, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showToast(context,"驾车");
                                if (customDialog1.isShowing()){
                                    customDialog1.dismiss();
                                }
                                if (cenpt != null) {
                                    progressDialog.setMessage("加载中,请稍后...");
                                    progressDialog.show();
                                    BaiduAppProxy.CallBaiduNavigationLatLng(
                                            (Activity) context,
                                            BNRoutePlanNode.CoordinateType.BD09LL,
                                            cenpt,
                                            markerLatlng);
                                }
                            }
                        })
                        .addViewOnclick(R.id.tv_riding_search, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showToast(context,"骑行");
                                if (customDialog1.isShowing()){
                                    customDialog1.dismiss();
                                }
                                if (cenpt != null) {
                                    progressDialog.setMessage("加载中,请稍后...");
                                    progressDialog.show();
                                    BaiduAppProxy.CyclingNavigation(context,cenpt,markerLatlng);
                                }
                            }
                        })
                        .addViewOnclick(R.id.tv_foot_search, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtils.showToast(context,"步行");
                                if (customDialog1.isShowing()){
                                    customDialog1.dismiss();
                                }
                                if (cenpt != null) {
                                    progressDialog.setMessage("加载中,请稍后...");
                                    progressDialog.show();
                                    BaiduAppProxy.PedestrianNavigation(context,cenpt,markerLatlng);
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelInputIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (customDialog1.isShowing()){
                                    customDialog1.dismiss();
                                }
                            }
                        })
                        .build();

                customDialog1.show();

                break;

            case R.id.panorama:     //全景图
                if (markerLatlng != null) {
                    SavePointVo savePointVo=null;
                    if (checkPointVoList.size()>0){
                        savePointVo= checkPointVoList.get(0);
                    }
                    progressDialog.setMessage("加载中,请稍后...");
                    progressDialog.show();
                    Intent i = new Intent(this, PanoDemoMain.class);
                    i.putExtra("lat", markerLatlng.latitude);
                    i.putExtra("lon", markerLatlng.longitude);
                    i.putExtra(OkHttpParam.SAVEPOINTVO, savePointVo);
                    startActivity(i);
                }
                break;
            case R.id.loading_hint:     //定位提示
                if (loading_hint.getTag().equals("yes")){
                    setHintVible(false);

                }else if (loading_hint.getTag().equals("no")){
                    setHintVible(true);

                }
                break;

            default:

                break;

        }
    }

    /**
     * 显示暂停采测/查看对话框
     *
     * @param isContinuous 是否是连续采集模式
     */
    private void over_projcet_click(boolean isContinuous) {
        try {
            MiningSurveyVO miningSurveyVO = null;
            List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOdao.
                    queryForEq(OkHttpParam.PROJECT_ID, projectId);
            if (miningSurveyVOList != null && miningSurveyVOList.size() > 0) {
                miningSurveyVO = miningSurveyVOList.get(0);

            }

            if (isContinuous) {
                suspend(miningSurveyVO);
            } else {
                String title = "";
                String msg = "";
                if (miningSurveyVO.getIsSubmit().equals("1")) {      //采测中
                    title = "暂停提醒";
                    msg = "是否暂停采测？";
                } else {
                    title = "退出提醒";
                    msg = "是否退出查看？";
                }

                final MiningSurveyVO finalMiningSurveyVO = miningSurveyVO;
                customDialog = builder.cancelTouchout(false)
                        .view(R.layout.move_mining_over_dlaio)
                        .heightpx(ActivityUtil.getWindowsHetght(this))
                        .widthpx(ActivityUtil.getWindowsWidth(this))
                        .style(R.style.dialog)
                        .setTitle(title)
                        .setMsg(msg)
                        .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    suspend(finalMiningSurveyVO);
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                customDialog.dismiss();
                            }
                        })
                        .build();
                customDialog.show();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 暂停采测/查看
     *
     * @param miningSurveyVO
     * @throws SQLException
     */
    private void suspend(MiningSurveyVO miningSurveyVO) throws SQLException {
        if (miningSurveyVO != null) {
            projectId = "";
            if (miningSurveyVO.getIsSubmit().equals("1")) {       //进行中工程
                miningSurveyVO.setIsSubmit("0");        //工程已暂停

                int s = miningSurveyVOdao.update(miningSurveyVO);
                if (s == 1) {
                    Toast.makeText(context, "工程结束完成", Toast.LENGTH_SHORT).show();
                    customDialog.dismiss();

                    initializeView();

                    mapClean(false);

                    moveProjcetUpdate();

                }
            } else if (miningSurveyVO.getIsSubmit().equals("2")
                    || miningSurveyVO.getIsSubmit().equals("0")) {    //已完成工程  与未完成工程
                customDialog.dismiss();

                initializeView();

                mapClean(false);

                moveProjcetUpdate();
            }

        }
    }

    private void pointChangeLineBtn_click() {
        switch ((String) pointChangeLineBtn.getTag()) {
            //点
            case MapMeterMoveScope.POINT:
                pointChangeLineBtn.setBackgroundResource(R.drawable.line_change_point);
                pointChangeLineBtn.setTag(MapMeterMoveScope.LINE);
                coordinate_point.setText("坐标线");
                draw_point.setText("手绘线");
                accuratePoint.setText("精准点采线");
                property.setVisibility(View.GONE);
                point_connect_line.setVisibility(point_connect_line_isshow);
                continuity_point.setVisibility(View.GONE);
                continuity_line.setVisibility(View.GONE);

                if (markerOverlayList != null) {
                    for (int i = 0; i < markerOverlayList.size(); i++) {
                        markerOverlayList.get(i).remove();
                    }
                }
                if (lineOverlayList != null) {
                    for (int i = 0; i < lineOverlayList.size(); i++) {
                        lineOverlayList.get(i).remove();
                    }
                }
                if (linePointOverlayList != null) {
                    for (int i = 0; i < linePointOverlayList.size(); i++) {
                        linePointOverlayList.get(i).remove();
                    }
                }
                lineOverlayList.clear();
                linePointOverlayList.clear();
                lineList.removeAll(lineList);
                markerIds.removeAll(markerIds);


                if (marker != null) {
                    marker.remove();
                    mBaiduMap.hideInfoWindow();
                }

                if (facilityTypes != null && facilityTypes.size() >= 2) {
                    IntegratedFacData(facilityTypes.get(1).getFacilities());
                }

                revocation_line.setVisibility(View.GONE);

                //线切换成点时根所有变灰色且不设置地图事件
                draw_point.setTag("no");
                draw_point.setBackgroundResource(R.drawable.draw_point);

                point_connect_line.setTag("no");
                point_connect_line.setBackgroundResource(R.drawable.point_connect_line_blck);
                point_connect_line.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                mBaiduMap.setOnMapClickListener(null);
                mBaiduMap.removeMarkerClickListener(this);

                break;
            //线
            case MapMeterMoveScope.LINE:
                pointChangeLineBtn.setBackgroundResource(R.drawable.point_change_line);
                pointChangeLineBtn.setTag(MapMeterMoveScope.POINT);
                coordinate_point.setText("坐标点");
                draw_point.setText("手绘点");
                accuratePoint.setText("精准点");
                property.setVisibility(View.GONE);
                point_connect_line.setVisibility(View.GONE);
                continuity_point.setVisibility(continuity_point_isshow);
                continuity_line.setVisibility(continuity_line_isshow);

                if (markerOverlayList != null) {
                    for (int i = 0; i < markerOverlayList.size(); i++) {
                        markerOverlayList.get(i).remove();
                    }
                }
                if (lineOverlayList != null) {
                    for (int i = 0; i < lineOverlayList.size(); i++) {
                        lineOverlayList.get(i).remove();
                    }
                }
                lineOverlayList.clear();

                if (linePointOverlayList != null) {
                    for (int i = 0; i < linePointOverlayList.size(); i++) {
                        linePointOverlayList.get(i).remove();
                    }
                }
                linePointOverlayList.clear();
                lineList.removeAll(lineList);
                markerIds.removeAll(markerIds);


                if (marker != null) {
                    marker.remove();
                    mBaiduMap.hideInfoWindow();
                }

                if (facilityTypes != null && facilityTypes.size() >= 2) {
                    IntegratedFacData(facilityTypes.get(0).getFacilities());
                }
                revocation_line.setVisibility(View.GONE);

                //线切换成点时根所有变灰色且不设置地图事件
                draw_point.setTag("no");
                draw_point.setBackgroundResource(R.drawable.draw_point);

                continuity_point.setTag("no");
                continuity_point.setBackgroundResource(R.drawable.continuity_point_blck);

                continuity_line.setTag("no");
                continuity_line.setBackgroundResource(R.drawable.continuity_line_blck);

                mBaiduMap.setOnMapClickListener(null);
                mBaiduMap.removeMarkerClickListener(this);

                break;

            default:
                break;
        }
    }

    private void draw_point_click() {
        switch ((String) draw_point.getTag()) {
            case "yes":
                /**
                 * 百度地图的点击事件
                 */
                mBaiduMap.setOnMapClickListener(null);

                mBaiduMap.setOnMarkerClickListener(this);

                property.setVisibility(View.GONE);

                if (markerOverlayList != null) {
                    for (int i = 0; i < markerOverlayList.size(); i++) {
                        markerOverlayList.get(i).remove();
                    }
                }
                if (lineOverlayList != null) {
                    for (int i = 0; i < lineOverlayList.size(); i++) {
                        lineOverlayList.get(i).remove();
                    }
                }
                if (linePointOverlayList != null) {
                    for (int i = 0; i < linePointOverlayList.size(); i++) {
                        linePointOverlayList.get(i).remove();
                    }
                }
                lineOverlayList.clear();
                linePointOverlayList.clear();
                lineList.removeAll(lineList);
                markerIds.removeAll(markerIds);

                if (marker != null) {
                    marker.remove();
                    mBaiduMap.hideInfoWindow();
                }

                set_point_view_IsChecked(false,false,false);

//                draw_point.setBackgroundResource(R.drawable.draw_point);
//                draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));
//                draw_point.setTag("no");

                revocation_line.setVisibility(View.GONE);

                break;
            case "no":

                /**
                 * 百度地图的点击事件
                 */
                mBaiduMap.setOnMapClickListener(this);

                mBaiduMap.removeMarkerClickListener(this);

                property.setVisibility(View.GONE);

                revocation_line.setVisibility(View.GONE);

                set_point_view_IsChecked(true,false,false);

//                draw_point.setTag("yes");
//                draw_point.setBackgroundResource(R.drawable.draw_point_blue);
//                draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue9));
//
//
//
//                continuity_point.setTag("no");
//                continuity_point.setBackgroundResource(R.drawable.continuity_point_blck);
//
//                continuity_line.setTag("no");
//                continuity_line.setBackgroundResource(R.drawable.continuity_line_blck);

                point_connect_line.setTag("no");
                point_connect_line.setBackgroundResource(R.drawable.point_connect_line_blck);
                point_connect_line.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                //将采集模式改为查看模式
                parameter = MapMeterMoveScope.CHECK;
                break;
        }
    }

    private void set_point_view_IsChecked(
            boolean draw_point_isChecked,boolean continuity_point_isChecked,
            boolean continuity_line_isChecked
    ){
        if (draw_point_isChecked){
            draw_point.setTag("yes");
            draw_point.setBackgroundResource(R.drawable.draw_point_blue);
            draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue9));
        }else {
            draw_point.setBackgroundResource(R.drawable.draw_point);
            draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));
            draw_point.setTag("no");
        }
        if (continuity_point_isChecked){
            continuity_point.setTag("yes");
            continuity_point.setBackgroundResource(R.drawable.continuity_point_blue);
            continuity_point.setTextColor(getResources().getColor(R.color.cornflowerblue9));
        }else {
            continuity_point.setTag("no");
            continuity_point.setBackgroundResource(R.drawable.continuity_point_blck);
            continuity_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));
        }
        if (continuity_line_isChecked){
            continuity_line.setTag("yes");
            continuity_line.setBackgroundResource(R.drawable.continuity_line_blue);
            continuity_line.setTextColor(getResources().getColor(R.color.cornflowerblue9));
        }else {
            continuity_line.setTag("no");
            continuity_line.setBackgroundResource(R.drawable.continuity_line_blck);
            continuity_line.setTextColor(getResources().getColor(R.color.cornflowerblue8));
        }
    }


    private void continuity_point_click() {
        switch ((String) continuity_point.getTag()) {
            case "yes":
                mBaiduMap.setOnMapClickListener(null);

                set_point_view_IsChecked(false,false,false);

//                continuity_point.setTag("no");
//                continuity_point.setBackgroundResource(R.drawable.continuity_point_blck);
//                continuity_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                if (markerOverlayList != null) {
                    for (int i = 0; i < markerOverlayList.size(); i++) {
                        markerOverlayList.get(i).remove();
                    }
                }
                if (lineOverlayList != null) {
                    for (int i = 0; i < lineOverlayList.size(); i++) {
                        lineOverlayList.get(i).remove();
                    }
                }
                if (linePointOverlayList != null) {
                    for (int i = 0; i < linePointOverlayList.size(); i++) {
                        linePointOverlayList.get(i).remove();
                    }
                }
                lineOverlayList.clear();
                linePointOverlayList.clear();
                lineList.removeAll(lineList);
                markerIds.removeAll(markerIds);

                if (marker != null) {
                    marker.remove();
                    mBaiduMap.hideInfoWindow();
                }
                break;

            case "no":
                mBaiduMap.setOnMapClickListener(this);

                set_point_view_IsChecked(false,true,false);

//                continuity_point.setTag("yes");
//                continuity_point.setBackgroundResource(R.drawable.continuity_point_blue);
//                continuity_point.setTextColor(getResources().getColor(R.color.cornflowerblue9));
//
//                draw_point.setTag("no");
//                draw_point.setBackgroundResource(R.drawable.draw_point);
//
//                continuity_line.setTag("no");
//                continuity_line.setBackgroundResource(R.drawable.continuity_line_blck);

                if (markerOverlayList != null) {
                    for (int i = 0; i < markerOverlayList.size(); i++) {
                        markerOverlayList.get(i).remove();
                    }
                }
                if (lineOverlayList != null) {
                    for (int i = 0; i < lineOverlayList.size(); i++) {
                        lineOverlayList.get(i).remove();
                    }
                }
                if (linePointOverlayList != null) {
                    for (int i = 0; i < linePointOverlayList.size(); i++) {
                        linePointOverlayList.get(i).remove();
                    }
                }
                lineOverlayList.clear();
                linePointOverlayList.clear();
                lineList.removeAll(lineList);
                markerIds.removeAll(markerIds);

                facLines.clear();

                if (marker != null) {
                    marker.remove();
                    mBaiduMap.hideInfoWindow();
                }
                break;
        }
    }

    private void continuity_line_click() {
        switch ((String) continuity_line.getTag()) {
            case "yes":
                mBaiduMap.setOnMapClickListener(null);
                mBaiduMap.removeMarkerClickListener(this);
                mBaiduMap.setOnPolylineClickListener(null);

                set_point_view_IsChecked(false,false,false);

//                continuity_line.setTag("no");
//                continuity_line.setBackgroundResource(R.drawable.continuity_line_blck);
//                continuity_line.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                if (markerOverlayList != null) {
                    for (int i = 0; i < markerOverlayList.size(); i++) {
                        markerOverlayList.get(i).remove();
                    }
                }
                if (lineOverlayList != null) {
                    for (int i = 0; i < lineOverlayList.size(); i++) {
                        lineOverlayList.get(i).remove();
                    }
                }
                if (linePointOverlayList != null) {
                    for (int i = 0; i < linePointOverlayList.size(); i++) {
                        linePointOverlayList.get(i).remove();
                    }
                }
                lineOverlayList.clear();
                linePointOverlayList.clear();
                lineList.removeAll(lineList);
                markerIds.removeAll(markerIds);

                if (marker != null) {
                    marker.remove();
                    mBaiduMap.hideInfoWindow();
                }

                //由点亮变为灰色时需要将集合清空
                linePointList.clear();
                markerIds.clear();

                break;
            case "no":
                mBaiduMap.setOnMapClickListener(this);
                mBaiduMap.removeMarkerClickListener(this);
                mBaiduMap.setOnPolylineClickListener(null);

                set_point_view_IsChecked(false,false,true);

//                continuity_line.setTag("yes");
//                continuity_line.setBackgroundResource(R.drawable.continuity_line_blue);
//                continuity_line.setTextColor(getResources().getColor(R.color.cornflowerblue9));
//
//                continuity_point.setTag("no");
//                continuity_point.setBackgroundResource(R.drawable.continuity_point_blck);
//
//                draw_point.setTag("no");
//                draw_point.setBackgroundResource(R.drawable.draw_point);

                facLines.clear();
                break;
        }
    }

    //重置所有UI
    private void initializeView() {
        tvMoveTitle.setVisibility(View.GONE);
        over_projcet.setVisibility(View.GONE);
        pointChangeLineBtn.setVisibility(View.GONE);
        coordinate_point.setVisibility(View.GONE);
        draw_point.setVisibility(View.GONE);
        continuity_point.setVisibility(View.GONE);
        continuity_line.setVisibility(View.GONE);
        accuratePoint.setVisibility(View.GONE);
        new_projcet.setVisibility(View.VISIBLE);
        facility.setVisibility(View.GONE);
        revocation_line.setVisibility(View.GONE);
        point_connect_line.setVisibility(View.GONE);
        delete_point.setVisibility(View.GONE);
        go_to_the.setVisibility(View.GONE);
        panorama.setVisibility(View.GONE);
        property.setVisibility(View.GONE);
        abnormal_remind.setVisibility(View.GONE);
        abnormalFram.setVisibility(View.GONE);
        input_line.setVisibility(View.GONE);
        loading_hint.setVisibility(View.GONE);

        mBaiduMap.setOnMapClickListener(null);
        mBaiduMap.removeMarkerClickListener(this);
        mBaiduMap.setOnPolylineClickListener(null);
        mBaiduMap.setOnMyLocationClickListener(null);

    }

    private void cleanLinePoint(final int viewId) {

        customDialog = builder.cancelTouchout(false)
                .view(R.layout.move_mining_over_dlaio)
                .heightpx(ActivityUtil.getWindowsHetght(this))
                .widthpx(ActivityUtil.getWindowsWidth(this))
                .style(R.style.dialog)
                .setTitle("提醒")
                .setMsg("是否结束本次连续采集？")
                .addViewOnclick(R.id.confirmOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linePointList.clear();
                        if (linePointOverlayList.size() > 0) {
                            for (int i = 0; i < linePointOverlayList.size(); i++) {
                                linePointOverlayList.get(i).remove();
                            }
                        }
                        input_line.setVisibility(View.GONE);
                        customDialog.dismiss();

                        //点击事件的执行事件
                        switch (viewId) {
                            case R.id.continuity_line:  //连续线
                                continuity_line_click();
                                break;
                            case R.id.continuity_point:  //连续点
                                continuity_point_click();
                                break;
                            case R.id.draw_point:  //手绘点
                                draw_point_click();
                                break;
                            case R.id.pointChangeLineBtn:  //点线切换
                                pointChangeLineBtn_click();
                                break;
                            case R.id.over_projcet:  //暂停工程
                                over_projcet_click(true);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .addViewOnclick(R.id.cancelOverBtn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                        return;
                    }
                })
                .addViewOnclick(R.id.cancelOverIm, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                        return;
                    }
                })
                .build();
        customDialog.show();
    }


    /**
     * 打开工程
     */
    public void GatherMove(MiningSurveyVO miningSurveyVO, boolean isStart) {
        String wpiId = miningSurveyVO.getProjectId();
        projectId = wpiId;
        currentProject = miningSurveyVO;
        acquisitionState = MapMeterMoveScope.MOVE;
        try {
            if (!isStart) {       //判断是不是重新启动，是则不判断
                List<MiningSurveyVO> miningSurveyVO2 = miningSurveyVOdao.queryForEq("projectId", wpiId);
                if (miningSurveyVO2 != null && miningSurveyVO2.size() > 0) {
                    if (miningSurveyVO2.get(0).getIsSubmit().equals("1")) {     //表示该工程是当前正在采测的工程
                        return;
                    }
                }
            }

            //先把之前采测的工程状态改成0
            Map<String, Object> map = new HashMap<>();
            map.put("usedName", AppContext.getInstance().getCurUserName());
            map.put(OkHttpParam.PROJECT_STATUS, "1");
            List<MiningSurveyVO> miningSurveyVOs = miningSurveyVOdao.queryForFieldValues(map);
            if (miningSurveyVOs != null && miningSurveyVOs.size() == 1) {
                MiningSurveyVO miningSurveyVO1 = miningSurveyVOs.get(0);
                miningSurveyVO1.setIsSubmit("0");
                int s = miningSurveyVOdao.update(miningSurveyVO1);
                if (s == 1) {
                    miningSurveyVO.setIsSubmit("1");    //进行中
                    int s1 = miningSurveyVOdao.update(miningSurveyVO);
                    if (s1 == 1) {

                    }
                }
            } else {
                miningSurveyVO.setIsSubmit("1");    //进行中
                int s1 = miningSurveyVOdao.update(miningSurveyVO);
                if (s1 == 1) {

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (pointDialog != null && pointDialog.isShowing()) {
            pointDialog.dismiss();
        }
        if (wpiId != null && !wpiId.equals("")) {
            tvMoveTitle.setVisibility(View.VISIBLE);
            tvMoveTitle.setText(miningSurveyVO.getProjectName());
            over_projcet.setVisibility(View.VISIBLE);
            pointChangeLineBtn.setVisibility(pointChangeLineBtn_isshow);
            coordinate_point.setVisibility(coordinate_point_isshow);
            draw_point.setVisibility(draw_point_isshow);
            draw_point.setTag("no");
            draw_point.setBackgroundResource(R.drawable.draw_point);
            accuratePoint.setVisibility(accuratePoint_isshow);
            new_projcet.setVisibility(View.GONE);
            property.setText("填写属性");
            property.setVisibility(View.GONE);
            property.setTag(MapMeterMoveScope.MOVE);
            delete_point.setVisibility(View.GONE);
            go_to_the.setVisibility(View.GONE);
            panorama.setVisibility(View.GONE);
            locationBuon.setVisibility(View.VISIBLE);
            facility.setVisibility(View.VISIBLE);
            over_projcet.setText("暂停采测");
            loading_hint.setVisibility(View.VISIBLE);
            loading_hint.setTag("yes");
            setHintVible(true);

            set_point_view_IsChecked(false,false,false);

            //根据当前是采点还是采线确定连续点连续线是否显示
            if (pointChangeLineBtn.getTag().equals("point")) {
                continuity_point.setVisibility(continuity_point_isshow);
                continuity_point.setTag("no");
                continuity_point.setBackgroundResource(R.drawable.continuity_point_blck);
                continuity_line.setVisibility(continuity_line_isshow);
                continuity_line.setTag("no");
                continuity_line.setBackgroundResource(R.drawable.continuity_line_blck);
            } else if (pointChangeLineBtn.getTag().equals("line")) {
                continuity_point.setVisibility(continuity_point_isshow);
                continuity_line.setVisibility(continuity_line_isshow);
            }

            if (markerOverlayList != null) {
                for (int i = 0; i < markerOverlayList.size(); i++) {
                    markerOverlayList.get(i).remove();
                }
            }
            if (lineOverlayList != null) {
                for (int i = 0; i < lineOverlayList.size(); i++) {
                    lineOverlayList.get(i).remove();
                }
            }
            if (linePointOverlayList != null) {
                for (int i = 0; i < linePointOverlayList.size(); i++) {
                    linePointOverlayList.get(i).remove();
                }
            }
            lineOverlayList.clear();
            linePointOverlayList.clear();
            lineList.removeAll(lineList);
            markerIds.removeAll(markerIds);

            if (marker != null) {
                marker.remove();
            }
            mBaiduMap.hideInfoWindow();

            if (draw_point.getTag().toString().equals("yes")) {
                mBaiduMap.setOnMapClickListener(this);
                mBaiduMap.setOnPolylineClickListener(null);
                mBaiduMap.removeMarkerClickListener(this);
            } else {
                mBaiduMap.setOnMapClickListener(null);
                mBaiduMap.setOnPolylineClickListener(null);
                mBaiduMap.removeMarkerClickListener(this);
            }

            if (continuity_point.getTag().toString().equals("yes")) {
                mBaiduMap.setOnMapClickListener(this);
            } else {
                mBaiduMap.setOnMapClickListener(null);
            }

            //打开一个工程后获取数据
            OkHttpRequest.getLatlogData(cenpt, miningSurveyVO, acquisitionState);

            moveProjcetUpdate();

        }
    }

    /**
     * 查看工程
     */
    public void CheckProjcet(MiningSurveyVO miningSurveyVO) throws SQLException {
        String wpiId = miningSurveyVO.getProjectId();
        projectId = wpiId;
        currentProject = miningSurveyVO;
        acquisitionState = MapMeterMoveScope.CHECK;

        if (pointDialog != null && pointDialog.isShowing()) {
            pointDialog.dismiss();
        }
        if (wpiId != null && !wpiId.equals("")) {

            //如果是已查看完成工程，把所有进行中的工程改为未完成
            Map<String, Object> map = new HashMap<>();
            map.put("usedName", AppContext.getInstance().getCurUserName());
            map.put(OkHttpParam.PROJECT_STATUS, "1");
            List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOdao.queryForFieldValues(map);
            if (miningSurveyVOList != null && miningSurveyVOList.size() > 0) {
                for (int i = 0; i < miningSurveyVOList.size(); i++) {
                    miningSurveyVOList.get(i).setIsSubmit("0");
                    int s = miningSurveyVOdao.update(miningSurveyVOList.get(i));
                    if (s == 1) {

                    }
                }
            }

            tvMoveTitle.setText(miningSurveyVO.getProjectName());
            over_projcet.setText("退出查看");
            loading_hint.setVisibility(View.GONE);
            loading_hint.setTag("no");
            setHintVible(false);

            if (markerOverlayList != null) {
                for (int i = 0; i < markerOverlayList.size(); i++) {
                    markerOverlayList.get(i).remove();
                }
            }
            if (lineOverlayList != null) {
                for (int i = 0; i < lineOverlayList.size(); i++) {
                    lineOverlayList.get(i).remove();
                }
            }
            if (linePointOverlayList != null) {
                for (int i = 0; i < linePointOverlayList.size(); i++) {
                    linePointOverlayList.get(i).remove();
                }
            }
            lineOverlayList.clear();
            linePointOverlayList.clear();
            lineList.removeAll(lineList);
            markerIds.removeAll(markerIds);

            if (mBaidPolyline != null) {
                for (int i = 0; i < mBaidPolyline.size(); i++) {
                    if (mBaidPolyline.get(i) != null) {
                        mBaidPolyline.get(i).remove();
                    }
                }
            }

            if (marker != null) {
                marker.remove();
            }
            mBaiduMap.hideInfoWindow();

            Map<String ,Object> map1=new HashMap<>();
            map1.put(OkHttpParam.PROJECT_ID,miningSurveyVO.getProjectId());
//            map1.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());

            List<SavePointVo> savePointVos=savePointVoLongDao.queryForFieldValues(map1);
            //如果查出来的数据为0说明是第一次点击查看，需要联网拿数据
            if (savePointVos.size()==0){
                //打开一个工程后获取数据
                OkHttpRequest.getLatlogData(null, miningSurveyVO, acquisitionState);
            }else {
                //否则直接加载数据
                OkHttpRequest.showPoint(miningSurveyVO);
            }

            mBaiduMap.setOnMapClickListener(null);
            mBaiduMap.setOnMarkerClickListener(this);
            mBaiduMap.setOnPolylineClickListener(this);
            mBaiduMap.setOnMyLocationClickListener(null);

            moveProjcetUpdate();

        }
    }

    /**
     * 統一转成gcj02类型坐标在来调用这个方法
     *
     * @param latLng    gcj02类型的坐标
     * @param gatherType    采集类型
     */
    private void showMarker(final LatLng latLng,LatLng latLng_wgs84, String gatherType) {
        this.gatherType = gatherType;
        this.latitude_wg84=0;
        this.longitude_wg84=0;
        isSuccession = ConstantDate.ISSUCCESSION_NO;
        pointLatlng = latLng;
        property.setVisibility(View.VISIBLE);
        TextView infoTv = new TextView(this);
        infoTv.setTextColor(getResources().getColor(R.color.red));
        infoTv.setGravity(Gravity.CENTER);
        infoTv.setPadding(10, 10, 10, 10);
        infoTv.setTextColor(getResources().getColor(R.color.white));
        infoTv.setBackgroundResource(R.color.darkblue1);
        DecimalFormat df = new DecimalFormat("0.000000");    //保留6为有效数字
        infoTv.setText("经度:" + df.format(latLng_wgs84.longitude)
                + "\n" + "纬度:" + df.format(latLng_wgs84.latitude));

        switch (implementorName) {
            case Constant.VALVE:    //阀门
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.valve_inspection);
                break;
            case Constant.MUD_VALVE:    //排泥阀
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.mud_valve);
                break;
            case Constant.VENT_VALVE:    //排气阀
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.drain_tap);
                break;
            case Constant.WATER_METER:    //水表
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.meter_reading);
                break;
            case Constant.FIRE_HYDRANT:    //消防栓
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.hydrant);
                break;
            case Constant.DISCHARGE_OUTLET:    //出水口
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.water_outlet);
                break;
            case Constant.PLUG_SEAL:    //封头堵坂
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.plug_seal_plate);
                break;
            case Constant.NODE_BLACK:    //节点
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.node);
                break;
            default:
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka);

                break;
        }
        if (marker != null) {
            marker.remove();
        }
        //构建MarkerOption，用于在地图上添加Marker
        markerOption = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        marker = (Marker) mBaiduMap.addOverlay(markerOption);

        InfoWindow infoWindow = new InfoWindow(infoTv, latLng, -50);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    /**
     * 統一转成gcj02类型坐标在来调用这个方法
     *
     * @param latLng    gcj02类型的坐标
     * @param gatherType    采集类型
     */
    private void showMarker(final LatLng latLng, String gatherType,double longitude_wg84,double latitude_wg84) {
        this.gatherType = gatherType;
        this.longitude_wg84=longitude_wg84;
        this.latitude_wg84=latitude_wg84;
        isSuccession = ConstantDate.ISSUCCESSION_NO;
        pointLatlng = latLng;
        property.setVisibility(View.VISIBLE);
        TextView infoTv = new TextView(this);
        infoTv.setTextColor(getResources().getColor(R.color.red));
        infoTv.setGravity(Gravity.CENTER);
        infoTv.setPadding(10, 10, 10, 10);
        infoTv.setTextColor(getResources().getColor(R.color.white));
        infoTv.setBackgroundResource(R.color.darkblue1);
        DecimalFormat df = new DecimalFormat("0.000000");    //保留6为有效数字
        infoTv.setText("经度:" + df.format(longitude_wg84) + "\n" + "纬度:" + df.format(latitude_wg84));

        switch (implementorName) {
            case Constant.VALVE:    //阀门
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.valve_inspection);
                break;
            case Constant.MUD_VALVE:    //排泥阀
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.mud_valve);
                break;
            case Constant.VENT_VALVE:    //排气阀
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.drain_tap);
                break;
            case Constant.WATER_METER:    //水表
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.meter_reading);
                break;
            case Constant.FIRE_HYDRANT:    //消防栓
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.hydrant);
                break;
            case Constant.DISCHARGE_OUTLET:    //出水口
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.water_outlet);
                break;
            case Constant.PLUG_SEAL:    //封头堵坂
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.plug_seal_plate);
                break;
            case Constant.NODE_BLACK:    //节点
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.node);
                break;
            default:
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka);

                break;
        }
        if (marker != null) {
            marker.remove();
        }
        //构建MarkerOption，用于在地图上添加Marker
        markerOption = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        marker = (Marker) mBaiduMap.addOverlay(markerOption);

        InfoWindow infoWindow = new InfoWindow(infoTv, latLng, -50);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    /**
     * 統一转成bd09类型坐标在来调用这个方法
     *
     * @param latLng    bd09类型的坐标
     * @param latLng_wgs84    wgs84类型的坐标
     * @param gatherType    采集类型
     */
    private void consecutiveCollection(final LatLng latLng,LatLng latLng_wgs84, String gatherType) {
        this.gatherType = gatherType;
        this.latitude_wg84=0;
        this.longitude_wg84=0;
        isSuccession = ConstantDate.ISSUCCESSION_YES;
        pointLatlng = latLng;
        property.setVisibility(View.VISIBLE);
        revocation_line.setVisibility(View.VISIBLE);
        TextView infoTv = new TextView(this);
        infoTv.setTextColor(getResources().getColor(R.color.red));
        infoTv.setGravity(Gravity.CENTER);
        infoTv.setPadding(10, 10, 10, 10);
        infoTv.setTextColor(getResources().getColor(R.color.white));
        infoTv.setBackgroundResource(R.color.darkblue1);
        DecimalFormat df = new DecimalFormat("0.000000");    //保留6为有效数字
        infoTv.setText("经度:" + df.format(latLng_wgs84.longitude)
                + "\n" + "纬度:" + df.format(latLng_wgs84.latitude));

        switch (implementorName) {
            case Constant.VALVE:    //阀门
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.valve_inspection);
                break;
            case Constant.MUD_VALVE:    //排泥阀
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.mud_valve);
                break;
            case Constant.VENT_VALVE:    //排气阀
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.drain_tap);
                break;
            case Constant.WATER_METER:    //水表
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.meter_reading);
                break;
            case Constant.FIRE_HYDRANT:    //消防栓
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.hydrant);
                break;
            case Constant.DISCHARGE_OUTLET:    //出水口
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.water_outlet);
                break;
            case Constant.PLUG_SEAL:    //封头堵坂
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.plug_seal_plate);
                break;
            case Constant.NODE_BLACK:    //节点
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.node);
                break;
            default:
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka);

                break;
        }
        if (marker != null) {
            marker.remove();
        }
        //构建MarkerOption，用于在地图上添加Marker
        markerOption = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        marker = (Marker) mBaiduMap.addOverlay(markerOption);

        InfoWindow infoWindow = new InfoWindow(infoTv, latLng, -50);
        mBaiduMap.showInfoWindow(infoWindow);
    }
    /**
     * 統一转成gcj02类型坐标在来调用这个方法
     *
     * @param latLng    gcj02类型的坐标
     * @param gatherType    采集类型
     */
    private void consecutiveCollection(final LatLng latLng, String gatherType,double longitude_wg84,double latitude_wg84) {
        this.gatherType = gatherType;
        this.latitude_wg84=latitude_wg84;
        this.longitude_wg84=longitude_wg84;
        isSuccession = ConstantDate.ISSUCCESSION_YES;
        pointLatlng = latLng;
        property.setVisibility(View.VISIBLE);
        revocation_line.setVisibility(View.VISIBLE);
        TextView infoTv = new TextView(this);
        infoTv.setTextColor(getResources().getColor(R.color.red));
        infoTv.setGravity(Gravity.CENTER);
        infoTv.setPadding(10, 10, 10, 10);
        infoTv.setTextColor(getResources().getColor(R.color.white));
        infoTv.setBackgroundResource(R.color.darkblue1);
        DecimalFormat df = new DecimalFormat("0.000000");    //保留6为有效数字
        infoTv.setText("经度:" + df.format(longitude_wg84) + "\n" + "纬度:" + df.format(latitude_wg84));

        switch (implementorName) {
            case Constant.VALVE:    //阀门
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.valve_inspection);
                break;
            case Constant.MUD_VALVE:    //排泥阀
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.mud_valve);
                break;
            case Constant.VENT_VALVE:    //排气阀
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.drain_tap);
                break;
            case Constant.WATER_METER:    //水表
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.meter_reading);
                break;
            case Constant.FIRE_HYDRANT:    //消防栓
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.hydrant);
                break;
            case Constant.DISCHARGE_OUTLET:    //出水口
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.water_outlet);
                break;
            case Constant.PLUG_SEAL:    //封头堵坂
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.plug_seal_plate);
                break;
            case Constant.NODE_BLACK:    //节点
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.node);
                break;
            default:
                //构建Marker图标
                bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka);

                break;
        }
        if (marker != null) {
            marker.remove();
        }
        //构建MarkerOption，用于在地图上添加Marker
        markerOption = new MarkerOptions()
                .position(latLng)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        marker = (Marker) mBaiduMap.addOverlay(markerOption);

        InfoWindow infoWindow = new InfoWindow(infoTv, latLng, -50);
        mBaiduMap.showInfoWindow(infoWindow);
    }

    private void setLocationData(LocationInfoExt locationInfoExt) {
        if (locationInfoExt != null) {

            abnormal_remind.setText("当前使用" + locationInfoExt.getLocationModel() + "定位"+
                    "\n"+ "解状态:"+locationInfoExt.getQualityStr()+
                    "\n"+ "经度:"+locationInfoExt.getLongitude()+
                    "\n"+ "纬度:"+locationInfoExt.getLatitude()+
                    "\n"+ "卫星数量:"+locationInfoExt.getSolutionUsedSats()+
                    "\n"+ "海拔:"+locationInfoExt.getAltitude()+
                    "\n"+ "精度:"+locationInfoExt.getAccuracy()
            );

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(0)            //半径设置0米
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(locationInfoExt.getBearing())
                    .latitude(locationInfoExt.getLatitude_bd09())
                    .longitude(locationInfoExt.getLongitude_bd09())
                    .build();
            mBaiduMap.setMyLocationData(locData);
            cenpt = new LatLng(locationInfoExt.getLatitude_bd09(),
                    locationInfoExt.getLongitude_bd09());
            cenpt_wgs84 = new LatLng(locationInfoExt.getLatitude(),
                    locationInfoExt.getLongitude());
            Log.i("cenpt", "(" + cenpt.longitude + "," + cenpt.latitude + ")");

            if (isFirstLoc) {
                isFirstLoc = false;
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(cenpt);
                builder.zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        } else {
            if (AppContext.getInstance().getCurLocation() != null) {
                locationInfoExt = AppContext.getInstance().getCurLocation();
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(0)            //半径设置0米
//                    // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(locationInfoExt.getBearing())
                        .latitude(locationInfoExt.getLatitude_bd09())
                        .longitude(locationInfoExt.getLongitude_bd09())
                        .build();
                mBaiduMap.setMyLocationData(locData);
                cenpt = new LatLng(locationInfoExt.getLatitude_bd09(),
                        locationInfoExt.getLongitude_bd09());
                cenpt_wgs84 = new LatLng(locationInfoExt.getLatitude(),
                        locationInfoExt.getLongitude());
                Log.i("cenpt", "(" + cenpt.longitude + "," + cenpt.latitude + ")");

                if (isFirstLoc) {
                    isFirstLoc = false;
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(cenpt);
                    builder.zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }
            } else {
                if (thread == null) {
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(100);
                                setLocationData(null);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
            }
        }
    }

    public void showMaker(List<SavePointVo> savePointVoList, MiningSurveyVO miningSurveyVO) throws SQLException {

        mapClean(true);
        //下载图片异步
        OkHttpRequest.Picture(context,savePointVoList,miningSurveyVO);

        showMakreTask = new ShowMakreTask(context, mBaiduMap, markerOption,
                pointOver, mOverlayList, textOption, mBaidPolyline, miningSurveyVO);
        showMakreTask.execute(savePointVoList);
    }

    /**
     * 清除地图绘制的图层
     *
     * @param isShowMarker 是否是绘制图层的异步任务调用
     */
    private void mapClean(boolean isShowMarker) {
        //绘制前先把之前的清除掉
        if (mBaidPolyline != null && mBaidPolyline.size() > 0) {
            for (int j = 0; j < mBaidPolyline.size(); j++) {
                if (mBaidPolyline.get(j) != null) {
                    mBaidPolyline.get(j).remove();
                }
            }
            mBaidPolyline.removeAll(mBaidPolyline);
        }

        if (mOverlayList != null && mOverlayList.size() > 0) {
            for (int i = 0; i < mOverlayList.size(); i++) {
                if (mOverlayList.get(i) != null) {
                    mOverlayList.get(i).remove();
                }
            }
            mOverlayList.removeAll(mOverlayList);
        }


        if (markerOverlayList != null && markerOverlayList.size() > 0) {
            for (int i = 0; i < markerOverlayList.size(); i++) {
                if (markerOverlayList.get(i) != null) {
                    markerOverlayList.get(i).remove();
                }
            }
            markerOverlayList.removeAll(markerOverlayList);
        }
        if (lineOverlayList != null && lineOverlayList.size() > 0) {
            for (int i = 0; i < lineOverlayList.size(); i++) {
                if (lineOverlayList.get(i) != null) {
                    lineOverlayList.get(i).remove();
                }
            }
        }
        lineOverlayList.clear();

        if (!isShowMarker) {     //如果不是显示marker调用则清除连续点线
            if (linePointOverlayList != null) {
                for (int i = 0; i < linePointOverlayList.size(); i++) {
                    linePointOverlayList.get(i).remove();
                }
            }
            linePointOverlayList.clear();
        }

        if (marker != null) {
            marker.remove();
        }
        mBaiduMap.hideInfoWindow();


    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                .location(latLng));
        switch ((String) pointChangeLineBtn.getTag()) {
            //点
            case MapMeterMoveScope.POINT:
                Map<String,Double> map_wgs84=Bd09toArcgis.bd09ToWg84(latLng);
                LatLng latLng_wgs84=new LatLng(map_wgs84.get("lat"),map_wgs84.get("lon"));
                if (continuity_point.getTag().toString().equals("yes")) {    //说明是连续采点
                    consecutiveCollection(latLng, latLng_wgs84,"手绘采点");
                }
                if (continuity_line.getTag().toString().equals("yes")) {     //说明是连续采线
                    consecutiveCollection(latLng,latLng_wgs84, "手绘采点");
                }
                if (draw_point.getTag().toString().equals("yes")) {          //手绘点
                    showMarker(latLng, latLng_wgs84,"手绘采点");
                }

                break;
            //线
            case MapMeterMoveScope.LINE:
                showLine(latLng, "手绘采线", "");

                break;
        }
        Map<String, Double> localHashMap = Gcj022Gps.gcj2wgs(latLng.longitude, latLng.latitude);
        double log=localHashMap.get("lon");
        double lat=localHashMap.get("lat");
        Map<String, Double> localHashMap2 = Gcj022Gps.gcj2wgs(latLng.longitude, latLng.latitude);
        double log2=localHashMap2.get("lon");
        double lat2=localHashMap2.get("lat");
        Log.i("00",log+","+lat);

    }

    /**
     * 绘制线
     *
     * @param latLng
     */
    private void showLine(LatLng latLng, String gatherType, String markerId) {
        if (gatherType.equals("设施点连线")) {
            if (lineList != null && lineList.size() == 0) {
                lineList.add(latLng);
                markerIds.add(markerId);
            } else if (lineList != null && lineList.size() > 0) {
                if (!lineList.contains(latLng)) {
                    lineList.add(latLng);
                    markerIds.add(markerId);
                } else {
                    Toast.makeText(context, "不允许重复勾选", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else {
            lineList.add(latLng);
            markerIds.add(markerId);
        }

        isSuccession = ConstantDate.ISSUCCESSION_NO;
        this.gatherType = gatherType;
        Bundle bundle = new Bundle();
        bundle.putString("drawType", ConstantDate.LINE);
        bundle.putDouble("positionX", latLng.longitude);
        bundle.putDouble("positionY", latLng.latitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        OverlayOptions option = new MarkerOptions().draggable(true).position(latLng).icon(bitmap).extraInfo(bundle);
        marker = (Marker) mBaiduMap.addOverlay(option);
        markerOverlayList.add(marker);
        if (lineList.size() >= 2) {
            final List<LatLng> lineList2 = new ArrayList();
            for (int i = 0; i < lineList.size(); i++) {
                if (lineList.size() - 2 <= i) {
                    lineList2.add(lineList.get(i));
                }
            }
            // 画线 markerOptions画图
            PolylineOptions polylineOptions = new PolylineOptions();
            // 画线用到的点
            polylineOptions.points(lineList2);
            polylineOptions.color(0xAABBAA00);
            polylineOptions.width(10);
            lineOverlayList.add(mBaiduMap.addOverlay(polylineOptions));

        }
        if (lineList.size() >= 1) {
            property.setVisibility(View.VISIBLE);
            revocation_line.setVisibility(View.VISIBLE);
        } else {
            property.setVisibility(View.GONE);
            revocation_line.setVisibility(View.GONE);
        }
    }

    private void subLine(List<LatLng> lineList, List<String> markerIds) {
        SavePointVo savePointVo = pointVoMap.get("savePointVo");
        String lineLatlngList = "";
        double pipelineLinght = 0; //管线长度
        for (int i = 0; i < lineList.size(); i++) {
            if (i == lineList.size() - 1) {
                lineLatlngList += lineList.get(i).longitude + " " + lineList.get(i).latitude;
            } else {
                lineLatlngList += lineList.get(i).longitude + " " + lineList.get(i).latitude + ",";
            }
            if (i >= 1) {
                pipelineLinght = Arith.add(pipelineLinght, BaiduMapUtil.getDistanceOfMeter(
                        lineList.get(i - 1).latitude,
                        lineList.get(i - 1).longitude,
                        lineList.get(i).latitude,
                        lineList.get(i).longitude));
            }
        }
        String lineMakreIds = "";
        if (markerIds != null && markerIds.size() > 0) {
            for (int i = 0; i < markerIds.size(); i++) {
                if (i == markerIds.size() - 1) {
                    lineMakreIds += markerIds.get(i);
                } else {
                    lineMakreIds += markerIds.get(i) + ",";
                }
            }
        }

        String facNums = "";
        if (facLines != null && facLines.size() > 0) {
            for (int i = 0; i < facLines.size(); i++) {
                if (i == facLines.size() - 1) {
                    facNums += facLines.get(i);
                } else {
                    facNums += facLines.get(i) + ",";
                }
            }
        }
        Intent newFormInfo = new Intent(context, RunForm.class);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("lineMakreIds", lineMakreIds);    //管线上设施ID集合
        params.put("facNums", facNums);    //管线上设施编号集合
        params.put("happenAddr", pointAddress);        //地址
        params.put("pointList", lineLatlngList);        //坐标集合
        params.put("drawType", MapMeterMoveScope.LINE);        //绘制类型
        params.put("isEdit", MapMeterMoveScope.GATHER);        //绘制类型
        params.put(OkHttpParam.LOCATION_JSON, new Gson().toJson(locationInfoExt));        //定位vo 数据集合

        //根据是否是连续采集根据设施的采集类型确定管线的采集类型
        if (isSuccession.equals(ConstantDate.ISSUCCESSION_YES)) {
            switch (gatherType) {
                case "手绘采点":
                    params.put("gatherType", "手绘采线");        //采集类型

                    break;

                case "精准采点":
                    params.put("gatherType", "精准采线");        //采集类型

                    break;

                case "坐标采点":
                    params.put("gatherType", "坐标采线");        //采集类型

                    break;

                default:
                    params.put("gatherType", gatherType);        //采集类型
                    break;
            }
        } else {
            params.put("gatherType", gatherType);        //采集类型
        }

        params.put("isSuccession", isSuccession);        //是否是连续采集
        params.put("pipelineLinght", pipelineLinght + "");        //管线长度

        //设置提交页面默认值
        params.put("tubularProduct", "铸铁");        //管材
        params.put("pipType", "普通供水管线");        //管线类型
        params.put("layingType", "直埋");        //敷设类型
        if (savePointVo == null) {
            params.put("caliber", "20");        //口径
            params.put("administrativeRegion", "");    //行政区
        } else {
            params.put("caliber", savePointVo.getCaliber() == null ? "20" : savePointVo.getCaliber());        //口径
            params.put("administrativeRegion", savePointVo.getAdministrativeRegion() == null
                    ? "" : savePointVo.getAdministrativeRegion());    //行政区
        }


        if (!projectId.equals("")) {
            params.put(OkHttpParam.PROJECT_ID, projectId);        //工程ID

            int pointSize = 0;
            //点击时查询当前工程有多少数据
            try {
                Map<String, Object> map = new HashMap<>();
                map.put("usedName", AppContext.getInstance().getCurUserName());
                map.put("projectId", projectId);
                List<MiningSurveyVO> miningSurveyVOList = miningSurveyVOdao.queryForFieldValues(map);
                if (miningSurveyVOList.size() == 1) {
                    if (miningSurveyVOList.get(0).getShareCode() != null &&
                            !miningSurveyVOList.get(0).getShareCode().equals("")) {
                        params.put(OkHttpParam.SHARE_CODE, miningSurveyVOList.get(0).getShareCode());        //共享码
                        params.put(OkHttpParam.IS_PROJECT_SHARE, "0");    //表示共享
                        params.put(OkHttpParam.PROJECT_NAME, miningSurveyVOList.get(0).getProjectName());    //工程名字
                        params.put(OkHttpParam.PROJECT_TYPE, miningSurveyVOList.get(0).getProjectType());    //工程类型
                    } else {
                        params.put(OkHttpParam.SHARE_CODE, miningSurveyVOList.get(0).getShareCode());        //共享码
                        params.put(OkHttpParam.IS_PROJECT_SHARE, "1");    //表示不共享
                        params.put(OkHttpParam.PROJECT_NAME, miningSurveyVOList.get(0).getProjectName());    //工程名字
                        params.put(OkHttpParam.PROJECT_TYPE, miningSurveyVOList.get(0).getProjectType());    //工程类型
                    }
                    String projectType = miningSurveyVOList.get(0).getProjectType();
                    String taskNum = miningSurveyVOList.get(0).getTaskNum();
                    String projectNum = UUIDUtil.getUUID();

                    pointSize = miningSurveyVOList.get(0).getAutoNumberLine();

                    if (projectType != null) {
                        int pos = mType.lastIndexOf(projectType);
                        params.put(OkHttpParam.PIP_NAME, projectNum+"_"+symbolList.get(pos) + "L" + taskNum + pointSize);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        newFormInfo.putExtra("iParams", params);
        newFormInfo.putExtra("template", "ins_data_entry_line_cc.xml");     //连续点连续线需要写死这个配置
//        newFormInfo.putExtra("template", solutionName);
        Bundle bundle = new Bundle();
        bundle.putString("lineOrpointline", MapMeterMoveScope.POINT);      //判断提交的是线还是点线
        newFormInfo.putExtra("pointLineBundle", bundle);
        startActivityForResult(newFormInfo, 5);
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            String id = marker.getTitle();
            checkPointVoList = savePointVoLongDao.queryForEq("id", id);
            if (parameter.equals(MapMeterMoveScope.CHECK)) {        //查看模式
                markerLatlng = marker.getPosition();

                LatLng latLng=new LatLng(23.089885151172744,113.31084092513468);
                // 将GPS设备采集的原始GPS坐标转换成百度坐标
                CoordinateConverter converter  = new CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                // sourceLatLng待转换坐标
                converter.coord(latLng);
                LatLng desLatLng = converter.convert();
                Log.i("tag",desLatLng.longitude+","+desLatLng.latitude);

                TextView infoTv = new TextView(this);
                infoTv.setTextColor(getResources().getColor(R.color.red));
                infoTv.setGravity(Gravity.CENTER);
                infoTv.setPadding(10, 10, 10, 10);
                infoTv.setTextColor(getResources().getColor(R.color.white));
                infoTv.setBackgroundResource(R.color.darkblue1);
                DecimalFormat df = new DecimalFormat("0.000000");    //保留6为有效数字
                infoTv.setText("经度:" + df.format(marker.getPosition().longitude) + "\n" + "纬度:" + df.format(marker.getPosition().latitude));
                InfoWindow infoWindow = new InfoWindow(infoTv, marker.getPosition(), -80);
                mBaiduMap.showInfoWindow(infoWindow);

                property.setVisibility(View.VISIBLE);
                property.setText("查看/修改属性");
                property.setTag(MapMeterMoveScope.CHECK);

                delete_point.setVisibility(View.VISIBLE);
                delete_point.setTag(MapMeterMoveScope.CHECK);

                go_to_the.setVisibility(View.VISIBLE);
                panorama.setVisibility(View.VISIBLE);

                //将线改成默认颜色
                for (int i = 0; i < mBaidPolyline.size(); i++) {
//                    mBaidPolyline.get(i).setColor(0xAABBAA00);
                    String projectid = mBaidPolyline.get(i).getExtraInfo().getString(OkHttpParam.PROJECT_ID);
                    if (projectid.equals(this.projectId)) {
                        mBaidPolyline.get(i).setColor(getResources().getColor(R.color.color_bbaa00));
                    } else {
                        mBaidPolyline.get(i).setColor(getResources().getColor(R.color.olivedrab));
                    }
                }

                if (checkPointVoList != null && checkPointVoList.size() == 1) {
                    if (checkPointVoList.get(0).getUsedId().equals(AppContext.getInstance().getCurUser().getUserId())){
                        QueryBuilder<DynamicFormVO, Long> queryBuilder = dynamicFormDao.queryBuilder();
                        Where<DynamicFormVO, Long> where = queryBuilder.where();
                        where.isNotNull("form");
                        where.and();
                        where.eq("id", checkPointVoList.get(0).getDynamicFormVOId());
                        dynamicFormList = dynamicFormDao.query(queryBuilder.prepare());
                    }else {
                        dynamicFormList=null;
                    }
                }


            } else if (parameter.equals(MapMeterMoveScope.MOVE)) {            //采集模式

                if (point_connect_line.getTag().equals("yes")) {
                    showLine(marker.getPosition(), "设施点连线", marker.getTitle());

                    if (checkPointVoList != null && checkPointVoList.size() == 1) {
                        facLines.add(checkPointVoList.get(0).getFacName());
                    } else {
                        facLines.add("");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        List<String> childLineFacId = new ArrayList<>();

        String markerId = marker.getTitle();
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("projectId", projectId);
            map.put("uploadName", AppContext.getInstance().getCurUserName());
            List<SavePointVo> saveLineVos = savePointVoLongDao.queryForFieldValues(map);
            if (saveLineVos.size() > 0) {
                for (int i = 0; i < saveLineVos.size(); i++) {
                    String sIds = saveLineVos.get(i).getsIds();
                    if (sIds != null && !sIds.equals("")) {
                        List<String> sIdsArr = Arrays.asList(sIds.split(","));
                        if (sIdsArr != null && sIdsArr.size() > 0) {
                            if (sIdsArr.contains(markerId)) {
                                int num = sIdsArr.indexOf(markerId);

                                String ids = saveLineVos.get(i).getIds();
                                List<String> idsArr = Arrays.asList(ids.split(","));
                                childLineFacId.add(idsArr.get(num));
//                                break;
                            }
                        }
                    }
                }
            }

            childLineFacId.add(markerId);
            if (childLineFacId.size() > 0) {

                List<SavePointVo> savePointVoArrayList = new ArrayList<>();

                //去重
                childLineFacId = CreateFiles.removeDuplicateWithList(childLineFacId);
                for (int i = 0; i < childLineFacId.size(); i++) {
                    List<SavePointVo> savePointVoList = savePointVoLongDao.queryForEq(OkHttpParam.FAC_ID, childLineFacId.get(i));
                    if (savePointVoList != null && savePointVoList.size() == 1) {
                        SavePointVo savePointVo = savePointVoList.get(0);
                        savePointVo.setLongitude(marker.getPosition().longitude + "");
                        savePointVo.setLatitude(marker.getPosition().latitude + "");
                        int s = savePointVoLongDao.update(savePointVo);
                        if (s == 1) {
                            savePointVoArrayList.add(savePointVo);
                        }
                    }
                }
                //修改服务器
                OkHttpRequest.IsUpdatePio(savePointVoArrayList, context, currentProject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public boolean onMyLocationClick() {
        return false;
    }

    @Override
    public boolean onPolylineClick(Polyline polyline) {

        lineBundle = polyline.getExtraInfo();
        String projectId = lineBundle.getString(OkHttpParam.PROJECT_ID);
        if (projectId.equals(this.projectId)) {
            delete_point.setVisibility(View.VISIBLE);
            delete_point.setTag(DELETELINE);
            property.setVisibility(View.VISIBLE);
            property.setTag(MODIFYLIE);    //修改线


            for (int i = 0; i < mBaidPolyline.size(); i++) {
                if (mBaidPolyline.get(i) != null) {
                    if (mBaidPolyline.get(i) == polyline) {
                        if (projectId.equals(this.projectId)) {      //是当前工程数据
                            mBaidPolyline.get(i).setColor(R.color.greenyellow);
                        }
                    } else {
                        //获取其他线段的工程ID
                        String id = mBaidPolyline.get(i).getExtraInfo().getString(OkHttpParam.PROJECT_ID);
                        if (id.equals(this.projectId)) {
                            mBaidPolyline.get(i).setColor(0xAABBAA00);
                        }
                    }
                }
            }
            mBaiduMap.hideInfoWindow();
        }
        return false;
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(context, "定位失败，请移动到空旷地带", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        pointAddress = reverseGeoCodeResult.getAddress();
    }


    public MyMapView getMapView() {
        return arcMapview;
    }

    @Override
    protected void onDestroy() {
        //退出前保存当前地图的位置
        arcMapview.saveCurMapExtent();
        arcMapview.destroy();
        mMapView.onDestroy();

        unregisterReceiver(receiver);

        EventBus.getDefault().unregister(this);

        super.onDestroy();

    }

    @Override
    protected void onPause() {

        Point pointLeftTop = new Point(0, 0);  //  屏幕左上角
        Point pointRightBeomen = new Point(ActivityUtil.getWindowsWidth(this)
                , ActivityUtil.getWindowsHetght(this));
        if (mBaiduMap!=null){
            LatLng latlngLeft = mBaiduMap.getProjection().fromScreenLocation(pointLeftTop);
            LatLng latlngRight = mBaiduMap.getProjection().fromScreenLocation(pointRightBeomen);
            double dem = BaiduMapUtil.getDistanceOfMeter(latlngLeft.latitude, latlngLeft.longitude,
                    latlngRight.latitude, latlngRight.longitude) / 2;

            AppContext.getInstance().setDem((float) dem);

            mMapView.onPause();
        }

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        arcMapview.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        arcMapview.unpause();
        mMapView.onResume();
        try {
            if (isSubmit) {      //是否提交成功，提交成功才刷新地图
                if (acquisitionState != null && !acquisitionState.equals("") && currentProject != null ) {
                    if (!projectId.equals("")) {      //说明工程已经暂停，把工程ID设为空
                        List<SavePointVo> savePointVos=savePointVoLongDao.queryForEq(OkHttpParam.PROJECT_ID,currentProject.getProjectId());
                        //如果查出来的数据为0说明是第一次点击查看，需要联网拿数据
                        if (savePointVos.size()==0){
                            //打开一个工程后获取数据
                            OkHttpRequest.getLatlogData(cenpt, currentProject, "");
                        }else {
                            //否则直接加载数据
                            OkHttpRequest.showPoint(currentProject);
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getSavepointvo(SavePointVo savePointVo) {
        Log.d(TAG, "savePointVo:" + savePointVo);
        if (continuity_point.getTag().toString().equals("yes") || continuity_line.getTag().toString().equals("yes")) {
            //将marker点ID加入集合
            markerIds.add(savePointVo.getMarkerId());
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getProjId(String projectId) {
        Log.d(TAG, "projectId:" + projectId);
        if (projectId.equals(this.projectId)) {      //说明删除的工程是正在查看的工程
            initializeView();
            mapClean(false);
        }
        if (projectId.equals("getLatlogData")) {
            OkHttpRequest.getLatlogData(cenpt, currentProject, acquisitionState);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(MiningSurveyVO miningSurveyVO) throws SQLException {
        Toast.makeText(context, "新建成功", Toast.LENGTH_SHORT).show();
        GatherMove(miningSurveyVO, false);//新建工程成功后直接打开该工程
        if (customDialog.isShowing()) {
            customDialog.dismiss();
        }
        moveProjcetUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 5:        //点线表号提交成功后要把集合清空
                if (data != null) {
                    isSubmit = data.getBooleanExtra(OkHttpParam.PROJECT_STATUS, false);

                    if (isSubmit) {
                        isFarsts.clear();
                        facIds.clear();
                        lineList.clear();

                        SavePointVo savePointVo = (SavePointVo) data.getSerializableExtra("savePointVo");
                        if (savePointVo != null && savePointVo.getIsSuccession().equals(ConstantDate.ISSUCCESSION_YES)) {
                            //把连续采点中已成功提交的点加入到集合中
                            if (savePointVo.getLatitude()!=null&&savePointVo.getLongitude()!=null){
                                LatLng latLng = new LatLng(Double.parseDouble(savePointVo.getLatitude())
                                        , Double.parseDouble(savePointVo.getLongitude()));
                                linePointList.add(latLng);
                            }
                            facLines.add(savePointVo.getFacName());

                            //把最后一个设施点信息加入集合中
                            pointVoMap.put("savePointVo", savePointVo);

                            //绘制已完成的连续线

                            if (linePointList.size() >= 2) {
                                final List<LatLng> lineList2 = new ArrayList();
                                for (int i = 0; i < linePointList.size(); i++) {
                                    if (linePointList.size() - 2 <= i) {
                                        lineList2.add(linePointList.get(i));
                                    }
                                }
                                // 画线 markerOptions画图
                                PolylineOptions polylineOptions = new PolylineOptions();
                                // 画线用到的点
                                polylineOptions.points(lineList2);
                                polylineOptions.color(0xAABBAA00);
                                polylineOptions.width(10);
                                linePointOverlayList.add(mBaiduMap.addOverlay(polylineOptions));

                                input_line.setVisibility(View.VISIBLE);     //显示录入管线按钮
                            } else {
                                input_line.setVisibility(View.GONE);     //显示录入管线按钮
                            }

                            //获取上一次采点的数据库数据，自动赋值到下一个点
                            try {
                                QueryBuilder<DynamicFormVO, Long> queryBuilder = dynamicFormDao.queryBuilder();
                                Where<DynamicFormVO, Long> where = queryBuilder.where();
                                where.isNotNull("form");
                                where.and();
                                where.eq("id", savePointVo.getDynamicFormVOId());
                                dynamicFormList = dynamicFormDao.query(queryBuilder.prepare());
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }


                        }

                        boolean isLineEnd = data.getBooleanExtra("isLineEnd", false);      //判断是不是连续采点后管线是否提交成功
                        if (isLineEnd) {
                            input_line.setVisibility(View.GONE);     //管线提交成功后管线按钮隐藏
                            if (linePointOverlayList.size() > 0) {
                                for (int i = 0; i < linePointOverlayList.size(); i++) {
                                    linePointOverlayList.get(i).remove();
                                }
                            }

                            if (continuity_line.getTag().toString().equals("yes")) {     //说明是连续线，从上一次的最后一个点接着连线
                                //获取最后一个
                                LatLng endPoint = null;
                                String endId = "";
                                if (linePointList != null && linePointList.size() > 0) {
                                    endPoint = linePointList.get(linePointList.size() - 1);
                                }
                                if (markerIds.size() > 0) {
                                    endId = markerIds.get(markerIds.size() - 1);
                                }
                                //清空集合
                                linePointList.clear();
                                markerIds.clear();
                                //加上最后一个
                                if (endPoint != null) {
                                    linePointList.add(endPoint);
                                }
                                if (!endId.equals("")) {
                                    markerIds.add(endId);
                                }

                                //如果是最后一个，则加上次管线的最后一个设施编号
                                String endFacName = "";
                                if (facLines != null && facLines.size() > 0) {
                                    endFacName = facLines.get(facLines.size() - 1);
                                }
                                //清空集合
                                facLines.clear();
                                //加上最后一个
                                facLines.add(endFacName);

                            } else {
                                linePointList.clear();
                                markerIds.clear();
                                //管线提交成功后把设施点编号集合清空
                                facLines.clear();
                            }


                        }

                        revocation_line.setVisibility(View.GONE);
                        property.setVisibility(View.GONE);

                        if (markerOverlayList != null && markerOverlayList.size() > 0) {
                            for (int i = 0; i < markerOverlayList.size(); i++) {
                                markerOverlayList.get(i).remove();
                            }
                        }

                        if (marker != null) {
                            marker.remove();
                            mBaiduMap.hideInfoWindow();
                        }
                    }
                }

                moveProjcetUpdate();

                OkHttpRequest.getLatlogData(cenpt,currentProject,"");

                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    // 获取实时坐标广播数据
    private class TraceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.SERVICE_NAME.equals(action)) {    //获取坐标
                locationInfoExt = (LocationInfoExt) intent
                        .getSerializableExtra(Constant.TRACE_INFO);
                setLocationData(locationInfoExt);
            } else if (Constant.PASSDATE.equals(action)) {        //获得工程VO
                MiningSurveyVO miningSurveyVO = AppContext.getInstance().getMiningSurveyVO();
                parameter = intent.getStringExtra("parameter");
                if (parameter.equals("move")) {        //采测
                    GatherMove(miningSurveyVO, false);
                } else if (parameter.equals("check")) {        //查看
                    initializeView();
                    new_projcet.setVisibility(View.GONE);
//                    locationBuon.setVisibility(View.GONE);
                    property.setText("查看/修改信息");
                    property.setVisibility(View.GONE);
                    tvMoveTitle.setVisibility(View.VISIBLE);
                    over_projcet.setVisibility(View.VISIBLE);
                    point_connect_line.setVisibility(View.GONE);

                    try {
                        CheckProjcet(miningSurveyVO);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else if (parameter.equals("complete")) {        //完成
                    if (miningSurveyVO.getProjectId().equals(projectId)) {      //说明完成的工程时当前正在采集的工程
                        initializeView();
                        mapClean(false);
                    }
                }

                //把地图移动到当前位置
                if (cenpt != null) {
                    MapStatus.Builder builder = new MapStatus.Builder();
                    builder.target(cenpt);
                    builder.zoom(18.0f);
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                }

                Toast.makeText(context, miningSurveyVO.getProjectName(), Toast.LENGTH_SHORT).show();
            } else if (Constant.DEVICE_IS_CONNECTED.equals(action)) {    //设备已连接
                abnormal_remind.setVisibility(View.GONE);
                abnormalFram.setVisibility(View.GONE);
                abnormal_remind.setText("设备异常，使用GPS定位");
            } else if (Constant.UNIT_EXCEPTION.equals(action)) {    //设备异常
                abnormal_remind.setVisibility(View.VISIBLE);
                abnormalFram.setVisibility(View.VISIBLE);
                abnormal_remind.setText("设备异常，使用GPS定位");
            }
//            else if (Constant.PROJECT_CREATE.equals(action)) {    //创建工程
//                ProjectVo miningSurveyVO= (ProjectVo) intent.getSerializableExtra("miningSurveyVO");
//                Toast.makeText(context, "新建成功", Toast.LENGTH_SHORT).show();
//                GatherMove(miningSurveyVO, false);//新建工程成功后直接打开该工程
//                if (customDialog.isShowing()) {
//                    customDialog.dismiss();
//                }
//                moveProjcetUpdate();
//            }
        }
    }

    /**
     * 更新任务列表
     */
    public static void moveProjcetUpdate() {
        Intent intent = new Intent();
        intent.setPackage(AppContext.getInstance().getPackageName());
        intent.setAction(Constant.MOVE_TASK_LIST_UPDATE_ACTION);
        AppContext.getInstance().sendBroadcast(intent);
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

    public List<List<String>> getPointIdSegment() {
        return pointIdSegment;
    }

    public void setPointIdSegment(List<List<String>> pointIdSegment) {
        this.pointIdSegment = pointIdSegment;
    }

    public List<List<String>> getDrawTypesSeg() {
        return drawTypesSeg;
    }

    public void setDrawTypesSeg(List<List<String>> drawTypesSeg) {
        this.drawTypesSeg = drawTypesSeg;
    }

    public List<Polyline> getmBaidPolyline() {
        return mBaidPolyline;
    }

    public void setmBaidPolyline(List<Polyline> mBaidPolyline) {
        this.mBaidPolyline = mBaidPolyline;
    }


    public List<Overlay> getmOverlayList() {
        return mOverlayList;
    }

    public void setmOverlayList(List<Overlay> mOverlayList) {
        this.mOverlayList = mOverlayList;
    }

    private void setHintVible(boolean isVible){
        if (isVible){
            abnormal_remind.setVisibility(View.VISIBLE);
            abnormalFram.setVisibility(View.VISIBLE);
            loading_hint.setTag("yes");
            loading_hint.setBackgroundResource(R.drawable.loading_hint_blue);
            loading_hint.setTextColor(getResources().getColor(R.color.cornflowerblue9));
        }else {
            abnormal_remind.setVisibility(View.GONE);
            abnormalFram.setVisibility(View.GONE);
            loading_hint.setTag("no");
            loading_hint.setBackgroundResource(R.drawable.loading_hint);
            loading_hint.setTextColor(getResources().getColor(R.color.cornflowerblue8));
        }
    }

    //	String flag = this.getClass().getName();
//
//	private MyMapView arcMapview;
//
//	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main_map_activity);
//		arcMapview = (MyMapView)findViewById(R.id.myMapView);//
//		arcMapview.setMapPopupWindow(this);
//		//加载上一次地图范围
//		arcMapview.loadLastMapExtent();
//	}
//
//	public MyMapView getMapView(){
//		return arcMapview;
//	}
//
//	@Override
//	protected void onDestroy() {
//		//退出前保存当前地图的位置
//		arcMapview.saveCurMapExtent();
//		arcMapview.destroy();
//		super.onDestroy();
//
//	}
//
//	@Override
//	protected void onPause() {
//		arcMapview.pause();
//		super.onPause();
//
//	}
//
//	@Override
//	protected void onResume() {
//		arcMapview.unpause();
//		super.onResume();
//	}
//
//	/**
//	 * 再次点击退出应用
//	 */
//	private long mExitTime;
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
///*			if (side_drawer.isMenuShowing()
//					|| side_drawer.isSecondaryMenuShowing()) {
//				side_drawer.showContent();
//			} else {*/
//				if ((System.currentTimeMillis() - mExitTime) > 2000) {
//					Toast.makeText(this, "在按一次退出", Toast.LENGTH_SHORT).show();
//					mExitTime = System.currentTimeMillis();
//				} else {
//					//(new UserLogoutAsyncTask()).execute(null,null,null);
//					finish();
//					// System.exit(0);
//				}
//			//}
//			return true;
//		}
//		// 拦截MENU按钮点击事件，让他无任何操作
//		if (keyCode == KeyEvent.KEYCODE_MENU) {
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
}

