package com.movementinsome.map;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aMap.overlay.InfoWindowPoiOverlay;
import com.aMap.overlay.LineOverlay;
import com.aMap.overlay.PoiOverlay2;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.pub.view.MaxHeightListView;
import com.movementinsome.caice.async.InputDateTask;
import com.movementinsome.caice.okhttp.OkHttpParam;
import com.movementinsome.caice.okhttp.OkHttpRequest;
import com.movementinsome.caice.okhttp.ProjectRequest;
import com.movementinsome.caice.project.ProjectManipulation;
import com.movementinsome.caice.util.BaiduCoordinateTransformation;
import com.movementinsome.caice.util.ConstantDate;
import com.movementinsome.caice.util.CreateFiles;
import com.movementinsome.caice.util.MapMeterMoveScope;
import com.movementinsome.caice.util.ToastUtil;
import com.movementinsome.caice.view.CustomDialog;
import com.movementinsome.caice.vo.ProjectVo;
import com.movementinsome.caice.vo.SavePointVo;
import com.movementinsome.commonAdapter.adapterView.CommonListViewAdapter;
import com.movementinsome.commonAdapter.adapterView.ViewHolder;
import com.movementinsome.database.vo.DynamicFormVO;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.initial.model.Facility;
import com.movementinsome.kernel.initial.model.FacilityType;
import com.movementinsome.kernel.initial.model.ProjectType;
import com.movementinsome.kernel.location.LocationInfoExt;
import com.movementinsome.kernel.location.coordinate.Gcj022Gps;
import com.movementinsome.kernel.util.ActivityUtil;
import com.movementinsome.kernel.util.FileUtils;
import com.movementinsome.map.nearby.ToastUtils;

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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.movementinsome.caice.util.MapMeterMoveScope.DELETELINE;

/**
 * 地图页面
 */
public class MapViewer extends ContainActivity implements
        View.OnClickListener,
        GeocodeSearch.OnGeocodeSearchListener,
        AMap.OnMarkerClickListener,
        AMap.OnMarkerDragListener,
        AMap.OnMapClickListener,
        AMap.OnMyLocationChangeListener,
        View.OnTouchListener,
        AdapterView.OnItemClickListener,
        AMap.OnPolylineClickListener,
        LocationSource,
        AMap.OnCameraChangeListener
{
//    private MyMapView arcMapview;

    public static final String TAG = ContentValues.TAG;
    public static final String MOVE_MINING_POINT = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/MoveMiningPoint/";
    public static final String MOVE_MINING_LINE = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/MoveMiningLine/";

    public static final int POINT_SELECT_CODE = 1;
    public static final int LINE_SELECT_CODE = 2;

    private MapView mMapView;
    private AMap aMap;

    boolean isFirstLoc = true; // 是否首次定位

    private RelativeLayout reBMapview;
//    private Marker marker;
    private CreateFiles createFiles;
    private String pointAddress = new String();
    private GeocodeSearch mGeoCoder = null;
    private static LatLng cenpt;    //bd09类型坐标
    private LatLng cenpt_wgs84;    //wgs84类型坐标
    private BitmapDescriptor bitmap;

    private OkHttpRequest okHttpRequest;

    public List<String> mType;
    private List<String> symbolList;
    private AlertDialog pointDialog;

    private TraceReceiver receiver;

    private static List<LatLng> linePointList = new ArrayList();// 连续采点的坐标
    private static List<LatLng> lineList = new ArrayList();// 画图的坐标
    private static List<String> facLines = new ArrayList();// 管线上的设施表号集合
//    private MarkerOptions markerOption;
    private static Map<String, SavePointVo> pointVoMap = new HashMap<>();

    private InfoWindowPoiOverlay infoWindowPoiOverlay;  //绘制中的点数据
//    private PoiOverlay2 poiOverlay;      //绘制后的单种类型的点数据
    private Map<String,MultiPointOverlay> multiPointOverlayMap; //存放所有类型的绘制后的点数据
    private Map<String,List<SavePointVo>> facTypeSavePoints;  //存放不同类型的点数据源
    private LineOverlay lineOverlay;
    private  List<Marker> markerOverlayList = new ArrayList<>();    //绘制线中的marker集合
    private  List<Polyline> lineOverlayList = new ArrayList<>();//绘制线中的线段
    private  List<Polyline> linePointOverlayList = new ArrayList<>();//连续采点线集合

    private String implementorName = "";
    private String solutionName = "";

    private String projectId = "";

    private SharedPreferences.Editor editor;

    private MapViewer context;
    private Dao<ProjectVo, Long> miningSurveyVOdao = null;
    private Dao<SavePointVo, Long> savePointVoLongDao = null;

    private ProjectVo currentProject;      //当前工程
    private String acquisitionState = MapMeterMoveScope.CHECK;   //当前采集状态

    private FrameLayout pointChangeLineBtn;    //切换
    private TextView abnormal_remind;    //异常提醒
    private FrameLayout abnormalFram;    //异常提醒
    private TextView map_changer;    //地图切换
    private TextView coordinate_point;    //坐标点
    private TextView draw_point;    //手绘点
    private TextView line_add_point;    //管线加点
    private TextView continuity_point;    //连续点
    private TextView continuity_line;    //连续线
    private TextView point_connect_line;    //设施点连线
    private TextView delete_point;    //删除设施点
    private TextView input_data;    //导入
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

    public CustomDialog customDialog;
    public CustomDialog customDialog1;
    private CustomDialog.Builder builder;

    private LatLng pointLatlng;
    private LocationInfoExt locationInfoExt;

    public String parameter = MapMeterMoveScope.MOVE;
    private List<SavePointVo> checkPointVoList;

    private Bundle lineBundle;

    private List<DynamicFormVO> dynamicFormList;
    private Dao<DynamicFormVO, Long> dynamicFormDao = null;

    private String gatherType = "";     //采集方式类型
    private String isSuccession = ConstantDate.ISSUCCESSION_NO;  //是否是连续采集
    private double longitude_wg84 = 0;
    private double latitude_wg84 = 0;

    private List<String> facilityTextList;
    private List<Integer> facilityImageList0;
    private List<Integer> facilityImageList1;
    private List<FacilityType> facilityTypes;

    private LatLng markerLatlng;    //地图点击的设施点坐标

    private boolean isSubmit = false;
    private ProgressDialog progressDialog;

    private List<Facility> facilitiePoint;

    private List<ProjectType> projectTypes;

    private int pointChangeLineBtn_isshow;      //控制显隐配置
    private int coordinate_point_isshow;      //控制显隐配置
    private int draw_point_isshow;      //控制显隐配置
    private int continuity_point_isshow;      //控制显隐配置
    private int continuity_line_isshow;      //控制显隐配置
    private int accuratePoint_isshow;      //控制显隐配置
    private int line_add_point_isshow;      //控制管线加点显隐配置

    private ProjectManipulation projectManipulation;

    private LatLng centerLatlng;

    private SavePointVo lineAddPointVo=null;

//    private BitmapDescriptor mBlueTexture = BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png");
//    private BitmapDescriptor mGreenTexture = BitmapDescriptorFactory.fromAsset("icon_road_green_arrow.png");
//    private BitmapDescriptor mRedTexture = BitmapDescriptorFactory.fromAsset("icon_road_red_arrow.png");

    private  BitmapDescriptor shareLineDataBitmap= BitmapDescriptorFactory.fromResource(R.drawable.map_alr_night);

    private GeocodeSearch geocoderSearch;

    private OnLocationChangedListener mListener;
    private LocationManager mAMapLocationManager;


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
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.baiduMap);
        aMap = mMapView.getMap();
        setUpMap();

//        // 不显示地图缩放控件（按钮控制栏）
//        mMapView.showZoomControls(true);

        // 开启定位图层
        aMap.setMyLocationEnabled(true);
        aMap.setOnCameraChangeListener(this);

//        // 定义点聚合管理类ClusterManager
//        mClusterManager = new ClusterManager<MyItem>(this, aMap);
//
//        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
//        aMap.setOnMapStatusChangeListener(mClusterManager);


        reBMapview = (RelativeLayout) findViewById(R.id.reBMapview);

        createFiles = new CreateFiles();

        mGeoCoder = new GeocodeSearch(this);
        mGeoCoder.setOnGeocodeSearchListener(this);

//        arcMapview = (MyMapView) findViewById(R.id.myMapView3);//
//        arcMapview.setMapPopupWindow(this);
//        //加载上一次地图范围
//        arcMapview.loadLastMapExtent();

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
        map_changer.setVisibility(View.GONE);
        //坐标点
        coordinate_point = (TextView) findViewById(R.id.coordinate_point);
        coordinate_point.setOnClickListener(this);
        //手绘点
        draw_point = (TextView) findViewById(R.id.draw_point);
        draw_point.setOnClickListener(this);
        //管线加点
        line_add_point = (TextView) findViewById(R.id.line_add_point);
        line_add_point.setOnClickListener(this);
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
        //导入
        input_data = (TextView) findViewById(R.id.input_data);
        input_data.setOnClickListener(this);
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
        aMap.setOnMarkerDragListener(this);


    }

    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(
                BitmapDescriptorFactory.fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
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

        EventBus.getDefault().register(this);

        okHttpRequest = new OkHttpRequest(this, aMap);

        checkPointVoList = new ArrayList<>();

        miningSurveyVOdao = AppContext
                .getInstance().getAppDbHelper()
                .getDao(ProjectVo.class);

        savePointVoLongDao =
                AppContext.getInstance().getAppDbHelper().getDao(SavePointVo.class);

        dynamicFormDao = AppContext.getInstance()
                .getAppDbHelper().getDao(DynamicFormVO.class);

        builder = new CustomDialog.Builder(this);


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
        //获取服务器工程数据
        ProjectRequest.ProjectList(context);
        //获取 设施数据
        OkHttpRequest.getFacilityList(this);

        List<com.movementinsome.kernel.initial.model.View> viewList = AppContext.getInstance().getViews();
        if (viewList != null && viewList.size() > 0) {
            View viewLatout = getLayoutInflater().inflate(R.layout.main_map_activity3, null);
            ViewGroup viewGroup = (ViewGroup) viewLatout;

            for (int i = 0; i < viewList.size(); i++) {
                com.movementinsome.kernel.initial.model.View view = viewList.get(i);
                if (view.getName().equals("切换")) {
                    if (view.getIsShow().equals("true")) {
                        pointChangeLineBtn_isshow = View.VISIBLE;
                    } else if (view.getIsShow().equals("false")) {
                        pointChangeLineBtn_isshow = View.GONE;
                    }
                }
                if (view.getName().equals("坐标点")) {
                    if (view.getIsShow().equals("true")) {
                        coordinate_point_isshow = View.VISIBLE;
                    } else if (view.getIsShow().equals("false")) {
                        coordinate_point_isshow = View.GONE;
                    }
                }
                if (view.getName().equals("手绘点")) {
                    if (view.getIsShow().equals("true")) {
                        draw_point_isshow = View.VISIBLE;
                    } else if (view.getIsShow().equals("false")) {
                        draw_point_isshow = View.GONE;
                    }
                }
                if (view.getName().equals("连续点")) {
                    if (view.getIsShow().equals("true")) {
                        continuity_point_isshow = View.VISIBLE;
                    } else if (view.getIsShow().equals("false")) {
                        continuity_point_isshow = View.GONE;
                    }
                }
                if (view.getName().equals("连续线")) {
                    if (view.getIsShow().equals("true")) {
                        continuity_line_isshow = View.VISIBLE;
                    } else if (view.getIsShow().equals("false")) {
                        continuity_line_isshow = View.GONE;
                    }
                }
                if (view.getName().equals("精准点")) {
                    if (view.getIsShow().equals("true")) {
                        accuratePoint_isshow = View.VISIBLE;
                    } else if (view.getIsShow().equals("false")) {
                        accuratePoint_isshow = View.GONE;
                    }
                }
                if (view.getName().equals("管线加点")) {
                    if (view.getIsShow().equals("true")) {
                        line_add_point_isshow = View.VISIBLE;
                    } else if (view.getIsShow().equals("false")) {
                        line_add_point_isshow = View.GONE;
                    }
                }
            }
        }

        projectManipulation = new ProjectManipulation(this, mMapView);


        lineOverlay = new LineOverlay(aMap);
//        poiOverlay = new PoiOverlay2(aMap);
        multiPointOverlayMap = new HashMap<>();
        facTypeSavePoints = new HashMap<>();

        infoWindowPoiOverlay=new InfoWindowPoiOverlay(aMap);
        geocoderSearch=new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

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

            implementorName = facilityTextList.get(0);

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
                //获取marker数
                if ( currentProject != null && acquisitionState != null) {
                    if (!projectId.equals("")) {
                        OkHttpRequest.getLatlogData( currentProject,this);
                    }
                }
                break;

            case R.id.new_projcet:    //新建工程

                projectManipulation.CreateProject();

                break;


            case R.id.accuratePoint:        //精准点

                projectManipulation.accuratePointOnclick(pointChangeLineBtn, continuity_point,
                        continuity_line,line_add_point,lineAddPointVo,cenpt, cenpt_wgs84);

                break;

//            case R.id.map_blow_up:        //放大
//                float zoomLevel = aMap.getMapStatus().zoom;
//                if (zoomLevel <= 21) {
//                    aMap.animateMapStatus(MapStatusUpdateFactory.zoomIn());    //已动画的形式改变地图状态
//                    map_blow_up.setEnabled(true);
//                } else {
//                    Toast.makeText(context, "已经放至最大！", Toast.LENGTH_SHORT).show();
//                    map_blow_up.setEnabled(false);
//                }
//                break;
//
//            case R.id.map_shrink:        //缩小
//                float zoomLevel1 = aMap.getMapStatus().zoom;
//                if (zoomLevel1 > 2) {
//                    aMap.animateMapStatus(MapStatusUpdateFactory.zoomOut());    //已动画的形式改变地图状态
//                    map_shrink.setEnabled(true);
//                } else {
//                    map_shrink.setEnabled(false);
//                    Toast.makeText(context, "已经缩至最小！", Toast.LENGTH_SHORT).show();
//                }
//                break;


            case R.id.map_changer:        //地图切换
                if (aMap.getMapType() == AMap.MAP_TYPE_NORMAL) {
                    aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                } else {
                    aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                }

                break;

            case R.id.pointChangeLineBtn:        //采点采线切换
                projectManipulation.pointChangeLineBtn_Onclick(linePointList, linePointOverlayList, input_line,isSuccession);

                break;
            case R.id.coordinate_point:        //坐标点

                projectManipulation.coordinate_pointOnclick(pointChangeLineBtn, continuity_point);
                break;

            case R.id.draw_point:        //手绘点
                projectManipulation.draw_point_Onclick(linePointList, linePointOverlayList, input_line,isSuccession);

                break;

            case R.id.line_add_point:        //管线加点
                projectManipulation.line_add_point_Onclick(linePointList, linePointOverlayList, input_line,isSuccession);

                break;

            case R.id.input_data:       //导入
                if (currentProject.getCityVo() != null) {
                    projectManipulation.input_data_Onclick();
                    ToastUtils.show(getString(R.string.input_city_name) + currentProject.getCityVo().getCityName());
                } else {
                    ToastUtils.show(getString(R.string.input_error));
                }
                break;

            case R.id.continuity_point:         //连续采点
                projectManipulation.continuity_point_Onclick(linePointList, linePointOverlayList, input_line,isSuccession);
                break;

            case R.id.continuity_line:      //连续线
                projectManipulation.continuity_line_Onclick(linePointList, linePointOverlayList, input_line,isSuccession);

                break;

            case R.id.point_connect_line:       //设施点连线
                projectManipulation.point_connect_line_Onclick(point_connect_line, draw_point,
                        markerOverlayList, lineOverlayList, linePointOverlayList, lineList, infoWindowPoiOverlay);

                break;

            case R.id.input_line:       //录入连续采集管线信息 （录入管线加点线信息）
                projectManipulation.input_line_Onclick(linePointList, pointVoMap,
                        pointAddress, locationInfoExt, isSuccession, gatherType, currentProject, facLines,lineAddPointVo,context);

                break;

            case R.id.property:        //填写属性

                try {
                    projectManipulation.property_Onclick(property, pointChangeLineBtn, pointLatlng,
                            longitude_wg84, latitude_wg84, currentProject, locationInfoExt, pointAddress,
                            gatherType, isSuccession, implementorName, currentProject, continuity_point,
                            dynamicFormList, solutionName, continuity_line, lineList,
                            pointVoMap, checkPointVoList, dynamicFormDao, lineBundle, facLines);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;

            case R.id.over_projcet:        //暂停工程
                projectManipulation.over_projcet_Onclick(linePointList, linePointOverlayList, over_projcet,isSuccession);

                break;

            case R.id.abnormal_remind:    //设备异常提示
                if (abnormal_remind.getVisibility() == View.VISIBLE) {
                    abnormal_remind.setVisibility(View.GONE);
                    abnormalFram.setVisibility(View.GONE);
                }
                break;

            case R.id.delete_point:        //删除
                projectManipulation.detele_point_Onclick(delete_point, checkPointVoList,
                        currentProject);

                break;

            case R.id.revocation_line:  //撤销
                projectManipulation.revocation_line_Onclick(continuity_point, infoWindowPoiOverlay, revocation_line, property, input_line,
                        linePointList, markerOverlayList, lineOverlayList, lineList, facLines);

                break;

            case R.id.go_to_the:    //到这去
                projectManipulation.go_to_the_Onclick(cenpt, markerLatlng, progressDialog);

                break;

            case R.id.panorama:     //全景图
                if (markerLatlng != null) {
                    SavePointVo savePointVo = null;
                    if (checkPointVoList.size() > 0) {
                        savePointVo = checkPointVoList.get(0);
                    }
//                    progressDialog.setMessage("加载中,请稍后...");
//                    progressDialog.show();
//                    Intent i = new Intent(this, PanoDemoMain.class);
//                    i.putExtra("lat", markerLatlng.latitude);
//                    i.putExtra("lon", markerLatlng.longitude);
//                    i.putExtra(OkHttpParam.SAVEPOINTVO, savePointVo);
//                    startActivity(i);
                }
                break;
            case R.id.loading_hint:     //定位提示
                if (loading_hint.getTag().equals("yes")) {
                    setHintVible(false);

                } else if (loading_hint.getTag().equals("no")) {
                    setHintVible(true);

                }
                break;

            default:

                break;

        }
    }

    public void view_gone() {
        //提交成功后把以下属性隐藏
        revocation_line.setVisibility(View.GONE);
        delete_point.setVisibility(View.GONE);
        property.setVisibility(View.GONE);
        go_to_the.setVisibility(View.GONE);
        panorama.setVisibility(View.GONE);
    }

    /**
     * 显示暂停采测/查看对话框
     *
     * @param isContinuous 是否是连续采集模式
     */
    public void over_projcet_click(boolean isContinuous) {
        try {
            ProjectVo projectVo = null;
            List<ProjectVo> projectVoList = miningSurveyVOdao.
                    queryForEq(OkHttpParam.PROJECT_ID, projectId);
            if (projectVoList != null && projectVoList.size() > 0) {
                projectVo = projectVoList.get(0);

            }

            if (isContinuous) {
                suspend(projectVo);
            } else {
                String title = "";
                String msg = "";
                if (projectVo.getProjectStatus() != null) {
                    if (projectVo.getProjectStatus().equals("1")) {      //采测中
                        title = "暂停提醒";
                        msg = "是否暂停采测？";
                    } else {
                        title = "退出提醒";
                        msg = "是否退出查看？";
                    }
                }

                final ProjectVo finalProjectVo = projectVo;
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
                                    suspend(finalProjectVo);
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
     * @param projectVo
     * @throws SQLException
     */
    private void suspend(ProjectVo projectVo) throws SQLException {
        if (projectVo != null) {
            projectId = "";
            if (projectVo.getProjectStatus().equals("1")) {       //进行中工程
                projectVo.setProjectStatus("0");        //工程已暂停

                int s = miningSurveyVOdao.update(projectVo);
                if (s == 1) {
                    Toast.makeText(context, "工程结束完成", Toast.LENGTH_SHORT).show();
                    customDialog.dismiss();
                }
            } else if (projectVo.getProjectStatus().equals("2")
                    || projectVo.getProjectStatus().equals("0")) {    //已完成工程  与未完成工程
                customDialog.dismiss();

            }
            initializeView();

            mapClean(false,false);

            moveProjcetUpdate();

            lineAddPointVo=null;
            input_line.setVisibility(View.GONE);
            input_line.setText("采集完成,录入管线线数据");

        }
    }

    public void pointChangeLineBtn_click() {
        switch ((String) pointChangeLineBtn.getTag()) {
            //点
            case MapMeterMoveScope.POINT:
                pointChangeLineBtn.setBackgroundResource(R.drawable.line_change_point);
                pointChangeLineBtn.setTag(MapMeterMoveScope.LINE);
                coordinate_point.setText("坐标线");
                draw_point.setText("手绘线");
                accuratePoint.setText("精准点采线");
                property.setVisibility(View.GONE);
                point_connect_line.setVisibility(View.VISIBLE);
                continuity_point.setVisibility(View.GONE);
                continuity_line.setVisibility(View.GONE);
                line_add_point.setVisibility(View.GONE);

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


//                if (marker != null) {
//                    marker.hideInfoWindow();
//                    marker.remove();
//                }
                infoWindowPoiOverlay.removeFromMap();

                if (facilityTypes != null && facilityTypes.size() >= 2) {
                    IntegratedFacData(facilityTypes.get(1).getFacilities());
                }

                revocation_line.setVisibility(View.GONE);

                //线切换成点时根所有变灰色且不设置地图事件
                draw_point.setTag("no");
                draw_point.setBackgroundResource(R.drawable.draw_point);
                draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                line_add_point.setTag("no");
                line_add_point.setBackgroundResource(R.drawable.draw_point);
                line_add_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                point_connect_line.setTag("no");
                point_connect_line.setBackgroundResource(R.drawable.point_connect_line_blck);
                point_connect_line.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                aMap.setOnMapClickListener(null);
                aMap.setOnMarkerClickListener(this);

                EventBus.getDefault().post(OkHttpParam.DETELE_LINE);
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
                line_add_point.setVisibility(View.VISIBLE);

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


//                if (marker != null) {
//                    marker.hideInfoWindow();
//                    marker.remove();
//                }
                infoWindowPoiOverlay.removeFromMap();

                if (facilityTypes != null && facilityTypes.size() >= 2) {
                    IntegratedFacData(facilityTypes.get(0).getFacilities());
                }
                revocation_line.setVisibility(View.GONE);

                //线切换成点时根所有变灰色且不设置地图事件
                draw_point.setTag("no");
                draw_point.setBackgroundResource(R.drawable.draw_point);
                draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                line_add_point.setTag("no");
                line_add_point.setBackgroundResource(R.drawable.draw_point);
                line_add_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                continuity_point.setTag("no");
                continuity_point.setBackgroundResource(R.drawable.continuity_point_blck);
                continuity_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                continuity_line.setTag("no");
                continuity_line.setBackgroundResource(R.drawable.continuity_line_blck);
                continuity_line.setTextColor(getResources().getColor(R.color.cornflowerblue8));

                aMap.setOnMapClickListener(null);
                aMap.setOnMarkerClickListener(null);

                break;

            default:
                break;
        }
    }

    public void draw_point_click() {
        switch ((String) draw_point.getTag()) {
            case "yes":
                /**
                 * 百度地图的点击事件
                 */
                aMap.setOnMapClickListener(null);

                aMap.setOnMarkerClickListener(this);

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

//                if (marker != null) {
//                    marker.hideInfoWindow();
//                    marker.remove();
//                }
                infoWindowPoiOverlay.removeFromMap();

                set_point_view_IsChecked(
                        false,
                        false,
                        false,
                        false);

//                draw_point.setBackgroundResource(R.drawable.draw_point);
//                draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));
//                draw_point.setTag("no");

                revocation_line.setVisibility(View.GONE);

                break;
            case "no":

                /**
                 * 百度地图的点击事件
                 */
                aMap.setOnMapClickListener(this);

                aMap.setOnMarkerClickListener(null);

                property.setVisibility(View.GONE);

                revocation_line.setVisibility(View.GONE);

                set_point_view_IsChecked(
                        true,
                        false,
                        false,
                        false);

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

                EventBus.getDefault().post(OkHttpParam.DETELE_LINE);
                break;
        }
    }

    private void set_point_view_IsChecked(
            boolean draw_point_isChecked, boolean continuity_point_isChecked,
            boolean continuity_line_isChecked, boolean line_add_point_isChecked
    ) {
        if (draw_point_isChecked) {
            draw_point.setTag("yes");
            draw_point.setBackgroundResource(R.drawable.draw_point_blue);
            draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue9));
        } else {
            draw_point.setBackgroundResource(R.drawable.draw_point);
            draw_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));
            draw_point.setTag("no");
        }
        if (line_add_point_isChecked) {
            line_add_point.setTag("yes");
            line_add_point.setBackgroundResource(R.drawable.draw_point_blue);
            line_add_point.setTextColor(getResources().getColor(R.color.cornflowerblue9));
        } else {
            line_add_point.setBackgroundResource(R.drawable.draw_point);
            line_add_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));
            line_add_point.setTag("no");
        }
        if (continuity_point_isChecked) {
            continuity_point.setTag("yes");
            continuity_point.setBackgroundResource(R.drawable.continuity_point_blue);
            continuity_point.setTextColor(getResources().getColor(R.color.cornflowerblue9));
        } else {
            continuity_point.setTag("no");
            continuity_point.setBackgroundResource(R.drawable.continuity_point_blck);
            continuity_point.setTextColor(getResources().getColor(R.color.cornflowerblue8));
        }
        if (continuity_line_isChecked) {
            continuity_line.setTag("yes");
            continuity_line.setBackgroundResource(R.drawable.continuity_line_blue);
            continuity_line.setTextColor(getResources().getColor(R.color.cornflowerblue9));
        } else {
            continuity_line.setTag("no");
            continuity_line.setBackgroundResource(R.drawable.continuity_line_blck);
            continuity_line.setTextColor(getResources().getColor(R.color.cornflowerblue8));
        }
    }


    public void continuity_point_click() {
        switch ((String) continuity_point.getTag()) {
            case "yes":
                aMap.setOnMapClickListener(null);

                set_point_view_IsChecked(false, false, false, false);

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

//                if (marker != null) {
//                    marker.hideInfoWindow();
//                    marker.remove();
//                }
                infoWindowPoiOverlay.removeFromMap();
                break;

            case "no":
                aMap.setOnMapClickListener(this);

                set_point_view_IsChecked(false, true, false, false);

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

                facLines.clear();

//                if (marker != null) {
//                    marker.hideInfoWindow();
//                    marker.remove();
//                }
                infoWindowPoiOverlay.removeFromMap();
                EventBus.getDefault().post(OkHttpParam.DETELE_LINE);
                break;
        }
    }

    public void continuity_line_click() {
        switch ((String) continuity_line.getTag()) {
            case "yes":
                aMap.setOnMapClickListener(null);
                aMap.setOnMarkerClickListener(null);
                aMap.setOnPolylineClickListener(null);

                set_point_view_IsChecked(false, false, false, false);

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

//                if (marker != null) {
//                    marker.remove();
//                    marker.hideInfoWindow();
//                }
                infoWindowPoiOverlay.removeFromMap();

                //由点亮变为灰色时需要将集合清空
                linePointList.clear();

                break;
            case "no":
                aMap.setOnMapClickListener(this);
                aMap.setOnMarkerClickListener(null);
                aMap.setOnPolylineClickListener(null);

                set_point_view_IsChecked(
                        false,
                        false,
                        true,
                        false
                );

                facLines.clear();

//                EventBus.getDefault().post(OkHttpParam.DETELE_LINE);
                break;
        }
    }

    public void line_add_point_click() {
        switch ((String) line_add_point.getTag()) {
            case "yes":
                aMap.setOnMapClickListener(this);
                aMap.setOnPolylineClickListener(null);
                aMap.setOnMarkerClickListener(null);

                set_point_view_IsChecked(false,
                        false,
                        false,
                        false);

                EventBus.getDefault().post(OkHttpParam.DETELE_LINE);

//                ToastUtils.show("取消了管线加点");
                break;
            case "no":
                aMap.setOnMapClickListener(null);
                aMap.setOnPolylineClickListener(this);
                aMap.setOnMarkerClickListener(null);

                set_point_view_IsChecked(
                        false,
                        false,
                        false,
                        true
                        );

                facLines.clear();

//                if (marker!=null){
//                    marker.hideInfoWindow();
//                    marker.remove();
//                }
                infoWindowPoiOverlay.removeFromMap();
//
//                ToastUtils.show("开始管线加点");

                break;

            default:
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
        line_add_point.setVisibility(View.GONE);
        input_data.setVisibility(View.GONE);
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

        aMap.setOnMapClickListener(null);
        aMap.setOnMarkerClickListener(null);
        aMap.setOnPolylineClickListener(null);
        aMap.setOnMyLocationChangeListener(null);

    }

    /**
     * 打开工程
     */
    public void GatherMove(ProjectVo projectVo, boolean isStart) throws SQLException {
        projectVo = AppContext.getInstance().getProjectVoDao().queryForSameId(projectVo);
        String wpiId = projectVo.getProjectId();
        projectId = wpiId;
        currentProject = projectVo;
        acquisitionState = MapMeterMoveScope.MOVE;
        if (!isStart) {       //判断是不是重新启动，是则不判断
//                List<ProjectVo> miningSurveyVO2 = miningSurveyVOdao.queryForEq("projectId", wpiId);
//                if (miningSurveyVO2 != null && miningSurveyVO2.size() > 0) {
//                    if (!miningSurveyVO2.get(0).getIsSubmit().equals("1")) {     //表示该工程不是当前正在采测的工程

            //先把之前采测的工程状态改成0
            Map<String, Object> map = new HashMap<>();
            map.put(OkHttpParam.USER_ID, AppContext.getInstance().getCurUser().getUserId());
            map.put(OkHttpParam.PROJECT_STATUS, "1");
            List<ProjectVo> projectVos = miningSurveyVOdao.queryForFieldValues(map);
            if (projectVos != null && projectVos.size() == 1) {
                ProjectVo projectVo1 = projectVos.get(0);
                projectVo1.setProjectStatus("0");
                int s = miningSurveyVOdao.update(projectVo1);
                if (s == 1) {
                    projectVo.setProjectStatus("1");    //进行中
                    int s1 = miningSurveyVOdao.update(projectVo);
                    if (s1 == 1) {

                    }
                }
            } else {
                projectVo.setProjectStatus("1");    //进行中
                int s1 = miningSurveyVOdao.update(projectVo);
                if (s1 == 1) {

                }
            }

            if (pointDialog != null && pointDialog.isShowing()) {
                pointDialog.dismiss();
            }
            if (wpiId != null && !wpiId.equals("")) {
                tvMoveTitle.setVisibility(View.VISIBLE);
                tvMoveTitle.setText(projectVo.getProjectName());
                over_projcet.setVisibility(View.VISIBLE);
                pointChangeLineBtn.setVisibility(pointChangeLineBtn_isshow);
                coordinate_point.setVisibility(coordinate_point_isshow);
                draw_point.setVisibility(draw_point_isshow);
                draw_point.setTag("no");
                draw_point.setBackgroundResource(R.drawable.draw_point);
                line_add_point.setVisibility(line_add_point_isshow);
                line_add_point.setTag("no");
//                input_data.setVisibility(View.VISIBLE);
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

                set_point_view_IsChecked(
                        false,
                        false,
                        false,
                        false);

                //根据当前是采点还是采线确定连续点连续线是否显示
                if (pointChangeLineBtn.getTag().equals("point")) {
                    continuity_point.setVisibility(continuity_point_isshow);
                    continuity_point.setTag("no");
                    continuity_point.setBackgroundResource(R.drawable.continuity_point_blck);
                    continuity_line.setVisibility(continuity_line_isshow);
                    continuity_line.setTag("no");
                    continuity_line.setBackgroundResource(R.drawable.continuity_line_blck);
                } else if (pointChangeLineBtn.getTag().equals("line")) {
                    continuity_point.setVisibility(View.GONE);
                    continuity_line.setVisibility(View.GONE);
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

//                if (marker != null) {
//                    marker.hideInfoWindow();
//                    marker.remove();
//                }
                infoWindowPoiOverlay.removeFromMap();

                if (draw_point.getTag().toString().equals("yes")) {
                    aMap.setOnMapClickListener(this);
                    aMap.setOnPolylineClickListener(null);
                    aMap.setOnMarkerClickListener(this);
                } else {
                    aMap.setOnMapClickListener(null);
                    aMap.setOnPolylineClickListener(null);
                    aMap.setOnMarkerClickListener(this);
                }

                if (continuity_point.getTag().toString().equals("yes")) {
                    aMap.setOnMapClickListener(this);
                } else {
                    aMap.setOnMapClickListener(null);
                }

                //打开一个工程后获取数据
                showMaker(projectVo,false);

                moveProjcetUpdate();

            }
//                    }
//                }

        }


    }

    /**
     * 查看工程
     */
    public void CheckProjcet(ProjectVo projectVo) throws SQLException {
        String wpiId = projectVo.getProjectId();
        projectId = wpiId;
        currentProject = projectVo;
        acquisitionState = MapMeterMoveScope.CHECK;

        if (pointDialog != null && pointDialog.isShowing()) {
            pointDialog.dismiss();
        }
        if (wpiId != null && !wpiId.equals("")) {

            //如果是已查看完成工程，把所有进行中的工程改为未完成
            Map<String, Object> map = new HashMap<>();
            map.put(OkHttpParam.USER_ID, AppContext.getInstance().getCurUser().getUserId());
            map.put(OkHttpParam.PROJECT_STATUS, "1");
            List<ProjectVo> projectVoList = miningSurveyVOdao.queryForFieldValues(map);
            if (projectVoList != null && projectVoList.size() > 0) {
                for (int i = 0; i < projectVoList.size(); i++) {
                    projectVoList.get(i).setProjectStatus("0");
                    int s = miningSurveyVOdao.update(projectVoList.get(i));
                    if (s == 1) {

                    }
                }
            }

            tvMoveTitle.setText(projectVo.getProjectName());
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

//            if (marker != null) {
//                marker.hideInfoWindow();
//                marker.remove();
//            }
            infoWindowPoiOverlay.removeFromMap();

//            Map<String, Object> map1 = new HashMap<>();
//            map1.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
////            map1.put(OkHttpParam.USER_ID,AppContext.getInstance().getCurUser().getUserId());
//
//            List<SavePointVo> savePointVos = savePointVoLongDao.queryForFieldValues(map1);
//            //如果查出来的数据为0说明是第一次点击查看，需要联网拿数据
//            if (savePointVos.size() == 0) {
//                //打开一个工程后获取数据
//                OkHttpRequest.getLatlogData(projectVo, this);
//            } else {
//                //否则直接加载数据
//                showMaker(projectVo);
//            }
            //否则直接加载数据

            showMaker(projectVo,false);

            aMap.setOnMapClickListener(null);
            aMap.setOnMarkerClickListener(this);
            aMap.setOnPolylineClickListener(this);
            aMap.setOnMyLocationChangeListener(null);

            moveProjcetUpdate();

        }
    }

    /**
     * 統一转成bd09类型坐标在来调用这个方法
     *
     * @param latLng     bd09类型的坐标
     * @param gatherType 采集类型
     */
    public void consecutiveCollection(final LatLng latLng, String gatherType,
                                      double longitude_wg84, double latitude_wg84,String isSuccession) {
        this.gatherType = gatherType;
        this.latitude_wg84 = latitude_wg84;
        this.longitude_wg84 = longitude_wg84;
        this.isSuccession = isSuccession;
        pointLatlng = latLng;
        property.setVisibility(View.VISIBLE);
        revocation_line.setVisibility(View.VISIBLE);

        List<LatLng> latLngs=new ArrayList<>();
        latLngs.add(latLng);
        List<LatLng> wgs84LatLngs=new ArrayList<>();
        wgs84LatLngs.add(new LatLng(latitude_wg84,longitude_wg84));

        infoWindowPoiOverlay.setImplementorName(implementorName);
        infoWindowPoiOverlay.setData(latLngs,wgs84LatLngs);
        infoWindowPoiOverlay.addToMap();

//        if (marker != null) {
//            marker.remove();
//        }
//        //构建MarkerOption，用于在地图上添加Marker
//        markerOption = new MarkerOptions()
//                .position(latLng)
//                .icon(bitmap);
//        //在地图上添加Marker，并显示
//        marker = (Marker) aMap.addOverlay(markerOption);
//        marker.setTitle(OkHttpParam.TITLE);
//
//        InfoWindow infoWindow = new InfoWindow(infoTv, latLng, -50);
//        aMap.showInfoWindow(infoWindow);
    }

    private void setLocationData(LocationInfoExt locationInfoExt) {
        if (locationInfoExt != null) {

            abnormal_remind.setText("当前使用" + locationInfoExt.getLocationModel() + "定位" +
                    "\n" + "解状态:" + locationInfoExt.getQualityStr() +
                    "\n" + "经度:" + locationInfoExt.getLongitude() +
                    "\n" + "纬度:" + locationInfoExt.getLatitude() +
                    "\n" + "卫星数量:" + locationInfoExt.getSolutionUsedSats() +
                    "\n" + "海拔:" + locationInfoExt.getAltitude() +
                    "\n" + "精度:" + locationInfoExt.getAccuracy()
            );


            cenpt = new LatLng(locationInfoExt.getLatitude_gcj02(),
                    locationInfoExt.getLongitude_gcj02());
            cenpt_wgs84 = new LatLng(locationInfoExt.getLatitude(),
                    locationInfoExt.getLongitude());
            Log.i("cenpt", "(" + cenpt.longitude + "," + cenpt.latitude + ")");

            if (isFirstLoc) {
                isFirstLoc = false;
               aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
               aMap.moveCamera(CameraUpdateFactory.newLatLng(
                       BaiduCoordinateTransformation.toGcj02(
                               cenpt_wgs84.longitude,
                               cenpt_wgs84.latitude,
                               CoordinateConverter.CoordType.GPS)));
            }
            //设置定位数据
            if (mListener!=null&&locationInfoExt.getaMapLocation()!=null){
                mListener.onLocationChanged(locationInfoExt.getaMapLocation());// 显示系统小蓝点
            }
        }
    }

    public void showMaker(final ProjectVo projectVo, final boolean isLineAddPoint) {
        getObservable(projectVo,isLineAddPoint)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver());
    }

    private Observable<String> getObservable(final ProjectVo projectVo, final boolean isLineAddPoint) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                e.onNext("");

                mapClean(true,isLineAddPoint);

                List<SavePointVo> savePointVoList = OkHttpRequest.showPoint(projectVo);

                //测试
                List<String> implementorNameList=new ArrayList<>();

                List<SavePointVo> savePointVos = new ArrayList<>();
                List<SavePointVo> saveLinetVos = new ArrayList<>();
                if (savePointVoList.size() > 0) {
                    for (SavePointVo savePointVo : savePointVoList) {
                        if (savePointVo.getDataType().equals(MapMeterMoveScope.POINT)) {
//                            LatLng latLng = savePointVo.getLatlng();
//                            double dis = AMapUtils.calculateArea(centerLatlng, latLng);
//                            long cos = AppContext.getInstance().getCollectionScope();
//                            if (dis < cos) {
//                                savePointVos.add(savePointVo);
//                            }

                            savePointVos.add(savePointVo);

                            if (!implementorNameList.contains(savePointVo.getImplementorName())){
                                implementorNameList.add(savePointVo.getImplementorName());
                            }

                        } else if (savePointVo.getDataType().equals(MapMeterMoveScope.LINE)) {
//                            List<LatLng> latLngs = savePointVo.getLineLatlngList();
//                            boolean isAdd = true;
//                            for (int i = 0; i < latLngs.size(); i++) {
//                                LatLng latLng = latLngs.get(i);
//                                double dis =  AMapUtils.calculateArea(centerLatlng, latLng);
//                                long cos = AppContext.getInstance().getCollectionScope();
//                                if (dis < cos) {
//                                    isAdd = false;
//                                    break;
//                                }
//                            }
//                            if (!isAdd) {
//                                saveLinetVos.add(savePointVo);
//                            }
                            saveLinetVos.add(savePointVo);
                        }
                    }
                }

                lineOverlay.setProjectVo(currentProject);
                lineOverlay.setData(saveLinetVos);
                lineOverlay.addToMap();
//                lineOverlay.zoomToSpan();

                //测试
                for (int i = 0; i < implementorNameList.size(); i++) {
                    BitmapDescriptor bitmap = null;
                    String implementorName=implementorNameList.get(i);
                    if (implementorName != null&&!implementorName.equals("")) {
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
                            case Constant.POOL:    //水池
                                //构建Marker图标
                                bitmap = BitmapDescriptorFactory
                                        .fromResource(R.drawable.pool);
                                break;

                            case Constant.METER_READING_UNDONE:    //水表(未完成)
                                //构建Marker图标
                                bitmap = BitmapDescriptorFactory
                                        .fromResource(R.drawable.meter_reading_undone);
                                break;

                            case Constant.METER_READING_COMPLETED:    //水表(已完成)
                                //构建Marker图标
                                bitmap = BitmapDescriptorFactory
                                        .fromResource(R.drawable.meter_reading_completed);
                                break;

                            default:
                                //构建Marker图标
                                bitmap = BitmapDescriptorFactory
                                        .fromResource(R.drawable.icon_marka_b_yellow);

                                break;
                        }
                    }

                    Map map=new HashMap();
                    map.put(OkHttpParam.IMPLEMENTORNAME,implementorName);
                    if (projectVo.getProjectShareCode()!=null&&!projectVo.getProjectShareCode().equals("")){    //共享
                        map.put(OkHttpParam.SHARE_CODE, projectVo.getProjectShareCode());
                    }else {     //不共享
                        map.put(OkHttpParam.PROJECT_ID, projectVo.getProjectId());
                    }
                    List<SavePointVo> facTypeSavePoints=savePointVoLongDao.queryForFieldValues(map);

                    PoiOverlay2 poiOverlay=new PoiOverlay2(aMap);
                    poiOverlay.setProjectVo(projectVo);
                    poiOverlay.setData(facTypeSavePoints,bitmap);
                    poiOverlay.addToMap();
                }



                e.onComplete();
            }
        });
    }

    private Observer<String> getObserver() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.setTitle(getString(R.string.load_msg));
        progressDialog.show();
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String s) {
//                progressDialog.setMessage(getString(R.string.load_msg));
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(getString(R.string.load_error_mag));
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onComplete() {
//                ToastUtils.show(getString(R.string.load_msg2));
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        };
    }

    /**
     * 清除地图绘制的图层
     *
     * @param isShowMarker 是否是绘制图层的异步任务调用
     * @param isLineAddPoint 是否是管线加点  管线加点不清除已绘制的线
     */
    private void mapClean(boolean isShowMarker,boolean isLineAddPoint) {
        //绘制前先把之前的清除掉

        if (markerOverlayList != null && markerOverlayList.size() > 0) {
            for (int i = 0; i < markerOverlayList.size(); i++) {
                if (markerOverlayList.get(i) != null) {
                    markerOverlayList.get(i).remove();
                }
            }
            markerOverlayList.clear();
        }

        if (lineOverlayList != null && lineOverlayList.size() > 0) {
            for (int i = 0; i < lineOverlayList.size(); i++) {
                if (lineOverlayList.get(i) != null) {
                    lineOverlayList.get(i).remove();
                }
            }
            lineOverlayList.clear();
        }

        if (!isShowMarker) {     //如果不是显示marker调用则清除连续点线
            if (linePointOverlayList != null) {
                for (int i = 0; i < linePointOverlayList.size(); i++) {
                    linePointOverlayList.get(i).remove();
                }
                linePointOverlayList.clear();
            }
        }

//        if (marker != null) {
//            if (marker.getTitle()!=null&&!marker.getTitle().equals(OkHttpParam.TITLE)){
//                marker.remove();
//            }
//            marker.hideInfoWindow();
//        }
        infoWindowPoiOverlay.removeFromMap();

        if (!isLineAddPoint){
            for (String implementorName:multiPointOverlayMap.keySet()){
                multiPointOverlayMap.get(implementorName).remove();
            }
            lineOverlay.removeFromMap();
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        LatLonPoint latLonPoint=new LatLonPoint(latLng.latitude,latLng.longitude);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
        switch ((String) pointChangeLineBtn.getTag()) {
            //点
            case MapMeterMoveScope.POINT:
                Map<String, Double> map_wgs84 = Gcj022Gps.gcj2wgs(latLng.longitude,latLng.latitude);
                LatLng latLng_wgs84 = new LatLng(map_wgs84.get("lat"), map_wgs84.get("lon"));
                if (continuity_point.getTag().toString().equals("yes")) {    //说明是连续采点
                    consecutiveCollection(latLng, "手绘采点",latLng_wgs84.longitude,
                            latLng_wgs84.latitude,ConstantDate.ISSUCCESSION_YES);
                }
                if (continuity_line.getTag().toString().equals("yes")) {     //说明是连续采线
                    consecutiveCollection(latLng,"手绘采点",latLng_wgs84.longitude,
                            latLng_wgs84.latitude,ConstantDate.ISSUCCESSION_YES);
                }
                if (draw_point.getTag().toString().equals("yes")) {          //手绘点
                    consecutiveCollection(latLng,"手绘采点",latLng_wgs84.longitude,
                            latLng_wgs84.latitude,ConstantDate.ISSUCCESSION_NO);
                }
                if (line_add_point.getTag().toString().equals("yes")){      //管线加点
                    consecutiveCollection(latLng,"手绘采点",latLng_wgs84.longitude,
                            latLng_wgs84.latitude,ConstantDate.ISSUCCESSION_LINE_ADD_POIN);
                }

                break;
            //线
            case MapMeterMoveScope.LINE:
                showLine(latLng, "手绘采线", "");

                break;
        }
        Map<String, Double> localHashMap = Gcj022Gps.gcj2wgs(latLng.longitude, latLng.latitude);
        double log = localHashMap.get("lon");
        double lat = localHashMap.get("lat");
        Map<String, Double> localHashMap2 = Gcj022Gps.gcj2wgs(latLng.longitude, latLng.latitude);
        double log2 = localHashMap2.get("lon");
        double lat2 = localHashMap2.get("lat");
        Log.i("00", log + "," + lat);

    }

    /**
     * 绘制线
     *
     * @param latLng
     */
    public void showLine(LatLng latLng, String gatherType, String markerId) {
        if (gatherType.equals("设施点连线")) {
            if (lineList != null && lineList.size() == 0) {
                lineList.add(latLng);
            } else if (lineList != null && lineList.size() > 0) {
                if (!lineList.contains(latLng)) {
                    lineList.add(latLng);
                } else {
                    Toast.makeText(context, "不允许重复勾选", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        } else {
            lineList.add(latLng);
        }

        isSuccession = ConstantDate.ISSUCCESSION_NO;
        this.gatherType = gatherType;
        Bundle bundle = new Bundle();
        bundle.putString("drawType", ConstantDate.LINE);
        bundle.putDouble("positionX", latLng.longitude);
        bundle.putDouble("positionY", latLng.latitude);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
        MarkerOptions option = new MarkerOptions().draggable(true).position(latLng).icon(bitmap);
        markerOverlayList.add(aMap.addMarker(option));
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
            polylineOptions.setPoints(lineList2);
            polylineOptions.color(0xAABBAA00);
            polylineOptions.width(10);
            lineOverlayList.add(aMap.addPolyline(polylineOptions));

        }
        if (lineList.size() >= 1) {
            property.setVisibility(View.VISIBLE);
            revocation_line.setVisibility(View.VISIBLE);
        } else {
            property.setVisibility(View.GONE);
            revocation_line.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            String id = marker.getTitle();
            checkPointVoList = savePointVoLongDao.queryForEq(OkHttpParam.FAC_ID, id);
            if (parameter.equals(MapMeterMoveScope.CHECK)) {        //查看模式
                markerLatlng = marker.getPosition();
//                TextView infoTv = new TextView(this);
//                infoTv.setTextColor(getResources().getColor(R.color.red));
//                infoTv.setGravity(Gravity.CENTER);
//                infoTv.setPadding(10, 10, 10, 10);
//                infoTv.setTextColor(getResources().getColor(R.color.white));
//                infoTv.setBackgroundResource(R.color.darkblue1);
                DecimalFormat df = new DecimalFormat("0.000000");    //保留6为有效数字
//                infoTv.setText("经度:" + df.format(marker.getPosition().longitude) + "\n" + "纬度:" + df.format(marker.getPosition().latitude));
//                InfoWindow infoWindow = new InfoWindow(infoTv, marker.getPosition(), -80);
//                aMap.showInfoWindow(infoWindow);

                marker.setTitle("经度:" +df.format(marker.getPosition().longitude));
                marker.setSnippet("纬度:" + df.format(marker.getPosition().latitude));
                marker.showInfoWindow();

                property.setVisibility(View.VISIBLE);
                property.setText("查看/修改属性");
                property.setTag(MapMeterMoveScope.CHECK);

                delete_point.setVisibility(View.VISIBLE);
                delete_point.setTag(MapMeterMoveScope.CHECK);

                go_to_the.setVisibility(View.VISIBLE);
                panorama.setVisibility(View.VISIBLE);

                //将线改成默认颜色

                lineOverlay.addToMap();

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
        List<SavePointVo> savePointVoList = new ArrayList<>();

        //        List<String> childLineFacId = new ArrayList<>();
        Map<String, Double> map_wgs84 = Gcj022Gps.gcj2wgs(marker.getPosition().longitude,marker.getPosition().latitude);

        String markerId = marker.getTitle();
        try {
            String longitude = "";
            String latitude = "";
            //修改单个设施点
            SavePointVo savePointVo= (SavePointVo) marker.getObject();
            if (savePointVo!=null){
                savePointVoList.add(savePointVo);
            }
            if (savePointVoList.size() == 1) {
                longitude = savePointVoList.get(0).getLongitude();
                latitude = savePointVoList.get(0).getLatitude();
                savePointVoList.get(0).setLongitude(marker.getPosition().longitude + "");
                savePointVoList.get(0).setLatitude(marker.getPosition().latitude + "");
                savePointVoList.get(0).setLongitudeWg84(map_wgs84.get("lon") + "");
                savePointVoList.get(0).setLatitudeWg84(map_wgs84.get("lat") + "");

            }
            //修改管线
            Map<String, Object> map = new HashMap<>();
            map.put(OkHttpParam.PROJECT_ID, projectId);
            map.put(OkHttpParam.DATA_TYPE, MapMeterMoveScope.LINE);
            List<SavePointVo> saveLineVos = savePointVoLongDao.queryForFieldValues(map);
            if (saveLineVos.size() > 0) {
                for (int i = 0; i < saveLineVos.size(); i++) {
//                    String sIds = saveLineVos.get(i).getsIds();
                    String pointList = saveLineVos.get(i).getPointList();
                    if (pointList != null && !pointList.equals("")) {
                        List<String> pointStrList = Arrays.asList(pointList.split(","));
                        for (int k = 0; k < pointStrList.size(); k++) {
                            String pointStr = pointStrList.get(k);
                            String pointStr_longitude = pointStr.split(" ")[0];
                            String pointStr_latitude = pointStr.split(" ")[1];
                            if (longitude.equals(pointStr_longitude) && latitude.equals(pointStr_latitude)) {
                                String new_pointList = "";
                                //bd09类型
                                String bd09 = marker.getPosition().longitude + " " + marker.getPosition().latitude;
                                //替换坐标值
                                pointStrList.set(k, bd09);
                                for (int j = 0; j < pointStrList.size(); j++) {
                                    if (j == pointStrList.size() - 1) {
                                        new_pointList += pointStrList.get(j);
                                    } else {
                                        new_pointList += pointStrList.get(j) + ",";
                                    }
                                }
                                saveLineVos.get(i).setPointList(new_pointList);

                                savePointVoList.add(saveLineVos.get(i));
                                break;
                            }
                        }
                    }
                }
            }
            //修改服务器
            OkHttpRequest.IsUpdatePio(savePointVoList, context, currentProject);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        try {
            if (acquisitionState.equals(MapMeterMoveScope.CHECK)) {       //查看模式
//                lineBundle = polyline.getExtraInfo();
                SavePointVo savePointVo = lineOverlay.getSavePointVo(lineOverlay.getPoiIndex(polyline));
                checkPointVoList.clear();
                checkPointVoList.add(savePointVo);

                String projectId = savePointVo.getProjectId();
                if (projectId.equals(this.projectId)) {
                    delete_point.setVisibility(View.VISIBLE);
                    delete_point.setTag(DELETELINE);
                    property.setVisibility(View.VISIBLE);
                    property.setText("查看/修改线属性");
                    property.setTag(MapMeterMoveScope.MODIFYLIE);    //修改线

//                   if (lineOverlay.getmPolyline().equals(polyline)){
//
//                   }
                }
            }else if (acquisitionState.equals(MapMeterMoveScope.MOVE)){     //采集模式
                if (line_add_point.getTag().toString().equals("yes")){
                    aMap.setOnMapClickListener(this);
                    aMap.setOnPolylineClickListener(null);
                    aMap.setOnMarkerClickListener(null);

//                    lineBundle = polyline.getExtraInfo();
                    SavePointVo savePointVo =lineOverlay.getSavePointVo(lineOverlay.getPoiIndex(polyline));
                    if (lineAddPointVo==null){
                        lineAddPointVo = savePointVo;
                        String facId = lineAddPointVo.getFacId();
                        String projectId = lineAddPointVo.getProjectId();
                        if (projectId.equals(this.projectId)) {

//                            List<OverlayOptions> lineOverlayList = lineOverlay.getOverlayOptions();
//                            for (int i = 0; i < lineOverlayList.size(); i++) {
//                                if (i % 2 == 0) {        //表示是 PolylineOptions
//                                    PolylineOptions polylineOptions = (PolylineOptions) lineOverlayList.get(i);
//                                    if (polylineOptions != null) {
//                                        String facId_polyline = polylineOptions.getExtraInfo().getString(OkHttpParam.FAC_ID);
//                                        if (facId.equals(facId_polyline)) {
//                                            polylineOptions.customTexture(mGreenTexture);
//                                        } else {
//                                            polylineOptions.customTexture(mBlueTexture);
//                                        }
//                                    }
//                                }
//                            }
//                            lineOverlay.addToMap(lineOverlayList);
//                            marker.hideInfoWindow();
                            ToastUtils.show("已选择管线,请添加设施点");
                        }
                    }else {
                        ToastUtils.show("已选择管线,请添加设施点");
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                pointAddress = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }


//    public MyMapView getMapView() {
//        return arcMapview;
//    }

    @Override
    protected void onDestroy() {
        //退出前保存当前地图的位置
//        arcMapview.saveCurMapExtent();
//        arcMapview.destroy();
        // 关闭定位图层
        aMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        unregisterReceiver(receiver);

        EventBus.getDefault().unregister(this);

        super.onDestroy();

    }

    @Override
    protected void onPause() {

        mMapView.onPause();

        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

//        arcMapview.pause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        super.onStop();
    }

    @Override
    protected void onResume() {
//        arcMapview.unpause();
        mMapView.onResume();
        try {
            if (currentProject!=null){
                //刷新工程数据
                currentProject=miningSurveyVOdao.queryForSameId(currentProject);
            }
            if (isSubmit) {      //是否提交成功，提交成功才刷新地图
                if (acquisitionState != null && !acquisitionState.equals("") && currentProject != null) {
                    if (!projectId.equals("")) {      //说明工程已经暂停，把工程ID设为空
                        OkHttpRequest.getLatlogData(currentProject, this);
//                        List<SavePointVo> savePointVos=savePointVoLongDao.queryForEq(OkHttpParam.PROJECT_ID,currentProject.getProjectId());
//                        //如果查出来的数据为0说明是第一次点击查看，需要联网拿数据
//                        if (savePointVos.size()==0){
//                            //打开一个工程后获取数据
//                            OkHttpRequest.getLatlogData(currentProject, "");
//                        }else {
//                            //否则直接加载数据
//                            showMaker(savePointVos,currentProject);
//                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getProjId(String pamcerStr) {
        if (pamcerStr.equals(this.projectId)) {      //说明删除的工程是正在查看的工程
            initializeView();
            mapClean(false,false);
        }

        if (pamcerStr.equals("getLatlogData")) {
            OkHttpRequest.getLatlogData(currentProject, this);
        }
        if (pamcerStr.equals(OkHttpParam.SHOW_ALERT_DIALOG1)) {
            android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this)
                    .setMessage("上传数据成功")
                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            alertDialog.show();
        }
        if (pamcerStr.equals(OkHttpParam.DETELE_LINE)){     //
            input_line.setVisibility(View.GONE);
            lineAddPointVo=null;
            linePointList.clear();
            //管线提交成功后把设施点编号集合清空
            facLines.clear();

//            if (marker!=null){
//                marker.hideInfoWindow();
//                marker.remove();
//            }
            infoWindowPoiOverlay.removeFromMap();

//            List<OverlayOptions> lineOverlayList = lineOverlay.getOverlayOptions();
//            for (int i = 0; i < lineOverlayList.size(); i++) {
//                if (i % 2 == 0) {        //表示是 PolylineOptions
//                    PolylineOptions polylineOptions = (PolylineOptions) lineOverlayList.get(i);
//                    if (polylineOptions != null) {
//                        polylineOptions.customTexture(mBlueTexture);
//                    }
//                }
//            }
//            lineOverlay.addToMap(lineOverlayList);
        }
        if (pamcerStr.equals(OkHttpParam.SHOW_MARKER)){     //显示marker 信息，确保运行在主线程
            showMaker(currentProject,false);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceived(ProjectVo projectVo) throws SQLException {
        GatherMove(projectVo, false);//新建工程成功后直接打开该工程
        if (customDialog.isShowing()) {
            customDialog.dismiss();
        }
        moveProjcetUpdate();
    }

    /**
     *
     * @param requestCode       跳转activity时设置的状态码
     * @param resultCode        新activity关闭时设置的状态码
     * @param data      数据
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case POINT_SELECT_CODE:      //导入 点表 数据
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = FileUtils.getPath(this, uri);
//                        ExcelUtils.readExcle(path,0,currentProject);
                        InputDateTask inputDateTask = new InputDateTask(context, path, 0, currentProject);
                        inputDateTask.execute("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "File Path: " + path);
                }
                break;

            case LINE_SELECT_CODE:      //导入 线表 数据
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    Log.d(TAG, "File Uri: " + uri.toString());
                    // Get the path
                    String path = null;
                    try {
                        path = FileUtils.getPath(this, uri);
//                        ExcelUtils.readExcle(path,1,currentProject);
                        InputDateTask inputDateTask = new InputDateTask(context, path, 1, currentProject);
                        inputDateTask.execute("");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "File Path: " + path);
                }
                break;

            case 5:        //点线表号提交成功后要把集合清空
                if (data != null) {
                    isSubmit = data.getBooleanExtra(OkHttpParam.PROJECT_STATUS, false);

                    if (isSubmit) {
                        lineList.clear();

                        SavePointVo savePointVo = (SavePointVo) data.getSerializableExtra("savePointVo");
                        if (savePointVo != null && savePointVo.getIsSuccession().equals(ConstantDate.ISSUCCESSION_YES)) {
                            //把连续采点中已成功提交的点加入到集合中
                            if (savePointVo.getLatitude() != null && savePointVo.getLongitude() != null) {
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
                                polylineOptions.setPoints(lineList2);
                                polylineOptions.color(0xAABBAA00);
                                polylineOptions.width(10);
                                linePointOverlayList.add(aMap.addPolyline(polylineOptions));

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
                                where.eq(OkHttpParam.GUID, savePointVo.getGuid());
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
                                if (linePointList != null && linePointList.size() > 0) {
                                    endPoint = linePointList.get(linePointList.size() - 1);
                                }
                                //清空集合
                                linePointList.clear();
                                //加上最后一个
                                if (endPoint != null) {
                                    linePointList.add(endPoint);
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

//                        if (marker != null) {
//                            marker.hideInfoWindow();
//                            marker.remove();
//                        }
                        infoWindowPoiOverlay.removeFromMap();

                        showMaker(currentProject,false);
                    }
                    isSubmit = false;
                }

                moveProjcetUpdate();


                break;

            case 6:
                if (data != null) {
                    isSubmit = data.getBooleanExtra(OkHttpParam.PROJECT_STATUS, false);

                    if (isSubmit) {
                        SavePointVo savePointVo = (SavePointVo) data.getSerializableExtra("savePointVo");
                        if (savePointVo != null && savePointVo.getIsSuccession().equals(ConstantDate.ISSUCCESSION_LINE_ADD_POIN)) {
                            Log.i("tag",lineAddPointVo.toString());
                            String pointList=lineAddPointVo.getPointList();
                            String [] pointdata= pointList.split(",");
                            LatLng startLatlng=new LatLng(
                                    Double.parseDouble(pointdata[0].split(" ")[1]),
                                    Double.parseDouble(pointdata[0].split(" ")[0]));
                            LatLng endLatlng=new LatLng(
                                    Double.parseDouble(pointdata[1].split(" ")[1]),
                                    Double.parseDouble(pointdata[1].split(" ")[0]));

                            if (linePointList.size()==0){
                                linePointList.add(0,startLatlng);
                                linePointList.add(endLatlng);
                            }else {
                                boolean isStrrtAdd=false;
                                boolean isEndAdd=false;
                                for (int i = 0; i < linePointList.size(); i++) {
                                    LatLng latLng=linePointList.get(i);
                                    if (latLng.longitude==startLatlng.longitude&&latLng.latitude==startLatlng.latitude){
                                        isStrrtAdd=true;
                                        break;
                                    }
                                }
                                if (!isStrrtAdd){
                                    linePointList.add(0,startLatlng);
                                }

                                for (int i = linePointList.size()-1; i >= 0; i--) {
                                    LatLng latLng=linePointList.get(i);
                                    if (latLng.longitude==endLatlng.longitude&&latLng.latitude==endLatlng.latitude){
                                        isEndAdd=true;
                                        break;
                                    }
                                }
                                if (!isEndAdd){
                                    linePointList.add(endLatlng);
                                }
                            }

//                            if (!linePointList.contains(startLatlng)){
//                                linePointList.add(0,startLatlng);
//                            }
//                            if (!linePointList.contains(endLatlng)){
//                                linePointList.add(endLatlng);
//                            }

                            if (savePointVo.getLatitude() != null && savePointVo.getLongitude() != null) {
                                LatLng latLng = new LatLng(Double.parseDouble(savePointVo.getLatitude())
                                        , Double.parseDouble(savePointVo.getLongitude()));
                                linePointList.add(linePointList.size()-1,latLng);
                            }

                            String facNames=lineAddPointVo.getFacNameList();
                            String [] facNamesData=facNames.split(",");
                            if (!facLines.contains(facNamesData[0])){
                                facLines.add(0,facNamesData[0]);
                            }
                            if (facNamesData.length>1){
                                if (!facLines.contains(facNamesData[1])){
                                    facLines.add(facNamesData[1]);
                                }
                            }
                            facLines.add(facLines.size()-1,savePointVo.getFacName());

                            for (int i = 0; i < linePointOverlayList.size(); i++) {
                                linePointOverlayList.get(i).remove();
                            }
                            //绘制管线加点后的管线渲染图
                            if (linePointList.size() >= 1) {
//                                List<LatLng> latLngList=new ArrayList<>();
//                                latLngList.addAll(linePointList);
//                                latLngList.add(0,startLatlng);
//                                latLngList.add(endLatlng);

                                for (int i = 1; i < linePointList.size(); i++) {
                                    List<LatLng> lineList2=new ArrayList<>();
                                    lineList2.add(linePointList.get(i-1));
                                    lineList2.add(linePointList.get(i));
                                    // 画线 markerOptions画图
                                    PolylineOptions polylineOptions = new PolylineOptions();
                                    // 画线用到的点
                                    polylineOptions
                                            .addAll(lineList2)
                                            .setCustomTexture(shareLineDataBitmap)
                                            .setDottedLine(true)
                                            .width(10);
                                    linePointOverlayList.add(aMap.addPolyline(polylineOptions));

                                }
                                input_line.setVisibility(View.VISIBLE);     //显示录入管线按钮
                                input_line.setText("完成管线加点");
                            }else {
                                input_line.setVisibility(View.GONE);     //显示录入管线按钮
                            }
                        }

                        property.setVisibility(View.GONE);

                        if (markerOverlayList != null && markerOverlayList.size() > 0) {
                            for (int i = 0; i < markerOverlayList.size(); i++) {
                                markerOverlayList.get(i).remove();
                            }
                        }

//                        if (marker != null) {
//                            marker.remove();
//                            marker.hideInfoWindow();
//                        }
                        infoWindowPoiOverlay.removeFromMap();

                        showMaker(currentProject,true);
                    }
                    isSubmit = false;
                }

                moveProjcetUpdate();

                break;

            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    @Override
    public void onMyLocationChange(Location location) {

    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.i("tag","地图正在改变：+++++"+cameraPosition.target);
    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
//        if (centerLatlng == null) {
//            centerLatlng = cameraPosition.target;
//        } else {
//            if (AMapUtils.calculateArea(cameraPosition.target, centerLatlng) > AppContext.getInstance().getCollectionScope()) {
//                centerLatlng = cameraPosition.target;
//                if (!projectId.equals("")){
//                    showMaker(currentProject,false);
//                }
//            }
//        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
    }

    @Override
    public void deactivate() {
        mListener = null;
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
                ProjectVo projectVo = AppContext.getInstance().getProjectVo();
                parameter = intent.getStringExtra("parameter");
                if (parameter.equals("move")) {        //采测
                    try {
                        GatherMove(projectVo, false);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
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
                        CheckProjcet(projectVo);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                } else if (parameter.equals("complete")) {        //完成
                    if (projectVo.getProjectId().equals(projectId)) {      //说明完成的工程时当前正在采集的工程
                        initializeView();
                        mapClean(false,false);
                    }
                }

                //把地图移动到当前位置
                if (cenpt != null) {
                    CameraPosition cameraPosition=new CameraPosition(cenpt,18,0,0);
                    aMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }

                Toast.makeText(context, projectVo.getProjectName(), Toast.LENGTH_SHORT).show();
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


    private void setHintVible(boolean isVible) {
        if (isVible) {
            abnormal_remind.setVisibility(View.VISIBLE);
            abnormalFram.setVisibility(View.VISIBLE);
            loading_hint.setTag("yes");
            loading_hint.setBackgroundResource(R.drawable.loading_hint_blue);
            loading_hint.setTextColor(getResources().getColor(R.color.cornflowerblue9));
        } else {
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

