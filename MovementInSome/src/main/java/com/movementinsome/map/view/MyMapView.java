package com.movementinsome.map.view;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.action.IdentifyResultSpinnerAdapter;
import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Unit;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.tasks.identify.IdentifyParameters;
import com.esri.core.tasks.identify.IdentifyResult;
import com.esri.core.tasks.identify.IdentifyTask;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.MapParam;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.map.MapLoad;
import com.movementinsome.map.location.LocationManage;
import com.movementinsome.map.location.LocationMode;
import com.movementinsome.map.menu.SelectPicPopupWindow;
import com.movementinsome.map.utils.DrawGraphicTools;
import com.movementinsome.map.utils.MapCaiCeUtil;
import com.movementinsome.map.utils.MapMeterScope;
import com.movementinsome.map.utils.MapUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyMapView extends RelativeLayout  {

	private enum E_BUTTON_TYPE {
		LOC, COMPASS, FOLLOW
	}

	private E_BUTTON_TYPE mCurBtnType = E_BUTTON_TYPE.LOC;;

	private Context context;
	private AttributeSet attrs;
	private int defStyle;
	private String url;
	private MapMeterScope mapMeterScope;// 测量工具
	private boolean showMagnifier = false;
	private MyTouchListener myTouchListener;
	private Point identifyMagnifierPoint = null; // 查询设施的点
	private IdentifyParameters params;
	//地图设置参数
	private MapParam mapParam;
	private String storePath = AppContext.getInstance().getAppStoreMedioPath();
	//查询结果
	private ArrayList<IdentifyData> identifyDatas = new ArrayList<IdentifyData>();

	private MapTempParam mapTempParam;
	//private boolean idenShow;

	//private DRAW_GRAPHIC_TYPE drawType;  //草图绘制类型

	private ProgressDialog progress;

	private int queryCount = 0;
	private View view;

	//全景状态
	private Boolean showStreetScape = false;
	private PictureMarkerSymbol pmSymbol = null;
	private Graphic gp = null;
	private GraphicsLayer streetScapeGpLayer;
	//private MyPopuPanoramaView popuPanoramaView;

	private MapView mMapView;
	private ImageButton zoominBtn;
	private ImageButton zoomoutBtn;
	private ImageButton locateBtn;
	private ImageButton layerButton;
	private ImageButton clearButton;
	//private ImageButton cameraButton;
	//private ImageButton shotButton;
	private ImageButton streetButton;  //全景

	private LinearLayout mapLinearDraw;// 画图容器
	private Button drawMapbtnAgainDraw;// 重绘
	private Button drawMapbtnRecall;// 撤消
	private Button drawMapbtnConfirm;// 确定

	private LinearLayout mapLinearPipeBro;// 爆管分析容器
	private Button pipeBroMapClear;// 清除
	private Button pipeBroMapConfirm;// 确定

	private LinearLayout footerView;//菜单
	//private Button nearBtn;
	//private Button offValveBtn;
	private Button btnScale;
	private TextView scaleText;

	private Button setBtn;
	// 属性查询ui
	private LinearLayout map_search_layout;// 查询容器
	private ImageView map_search_img;// 查询图片
	private TextView map_search_text;// 查询Text
	private ImageView map_search_voice_img;// 语音

	private ImageButton memuButton;

	private ImageButton regionButton;

	private ImageButton pointButton;

	private ImageButton valueButton;

	private ImageButton selectButton;

	private ImageButton selectButton2;

	private ImageButton mianjButton;

	private LocationManage locationManage;

	private DrawGraphicTools drawGraphic;

	private LayerSelector layerSelect;

	private boolean isLocate = false;

	private boolean flgmapInitialized = false;//地图是否加载完成

	private float identifyX;
	private float identifyY;

	private MapCaiCeUtil mapCaiCeUtil;

	//private MapTempParam mapTempParam;

	public boolean isFlgmapInitialized() {
		return flgmapInitialized;
	}

	public void setFlgmapInitialized(boolean flgmapInitialized) {
		this.flgmapInitialized = flgmapInitialized;
	}

	private SelectPicPopupWindow menuWindow;
	//private PanoramaView mPanoView;
	//返回地图容器坐标系统相关内容
	//返回坐标单位，WKT描述需要从JSON中获取
	private LinearLayout header_container;

	public Unit getMapUnit() {
		if (mMapView.getSpatialReference().getID() !=0){
			return mMapView.getSpatialReference().getUnit();
		}else{
			return Unit.create(LinearUnit.Code.METER);
		}
	}

	public SpatialReference getMapSr() {
		if (mMapView.getSpatialReference().getID() !=0){
			return mMapView.getSpatialReference();
		}else{
			if (!"".equals(mapParam.getSrid())){
				return SpatialReference.create(Integer.valueOf(mapParam.getSrid()));
			}else{
				return SpatialReference.create(2435);  //暂时用中央子午线114，WKID为2435
			}
		}
	}

	private Handler mhandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			if(msg.what == Constant.MSG_IDEN_RETURN){
				queryCount--;
			    if (queryCount<=0){
			    	if (progress != null)
			    		progress.dismiss();
			    	if (identifyDatas.size()>0)
			    		ShowResult();
			    }

			}
		}

	};

//	private void ShowResult(){
//		com.gddst.map.view.IdentifyDialog identifyDialog = new IdentifyDialog(
//				context, this, identifyDatas,this.mapParam,false);
//		identifyDialog.show();
//	}
	private IdentifyView2 identifyView;
	private void ShowResult(){
		/*com.gddst.map.view.IdentifyDialog identifyDialog = new IdentifyDialog(
				context, this, identifyDatas, this.mapParam, true);
		identifyDialog.show();*/
		if(identifyView!=null&&identifyView.isShow()){
			identifyView.clear();
			removeView(identifyView);
		}
		identifyView = new IdentifyView2(context,this,identifyDatas,identifyX,identifyY);
		addView(identifyView);
	}

	public MyMapView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		view = View.inflate(context, R.layout.mymap_viewer,null);
		this.addView(view);
		init();

	}

	public MyMapView(Context context, AttributeSet attrs) {
		// TODO Auto-generated constructor stub
		super(context, attrs);
		this.context = context;
		this.attrs = attrs;
		//这一行会报错
		view = View.inflate(context, R.layout.mymap_viewer,null);
		this.addView(view);
		init();
	}

	public MyMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.attrs = attrs;
		this.defStyle = defStyle;
		view = View.inflate(context, R.layout.mymap_viewer,null);
		this.addView(view);
		init();
	}



	public void init(){
		map_search_layout = (LinearLayout) view.findViewById(R.id.map_search_layout);// 查询容器
		map_search_img = (ImageView) view.findViewById(R.id.map_search_img);// 查询图片
		map_search_text = (TextView) view.findViewById(R.id.map_search_text);// 查询Text
		map_search_voice_img = (ImageView) view.findViewById(R.id.map_search_voice_img);// 语音


		mapLinearPipeBro = (LinearLayout) view.findViewById(R.id.mapLinearPipeBro);// 爆管分析容器
		pipeBroMapClear = (Button) view.findViewById(R.id.pipeBroMapClear);// 清除
		pipeBroMapConfirm = (Button) view.findViewById(R.id.pipeBroMapConfirm);// 确定

		mapLinearDraw=(LinearLayout) view.findViewById(R.id.mapLinearDraw);// 画图容器
		drawMapbtnAgainDraw=(Button) view.findViewById(R.id.drawMapbtnAgainDraw);// 重绘
		drawMapbtnRecall=(Button) view.findViewById(R.id.drawMapbtnRecall);// 撤消
		drawMapbtnConfirm=(Button) view.findViewById(R.id.drawMapbtnConfirm);// 确定

		zoominBtn = (ImageButton) view.findViewById(R.id.zoominBtn);
		zoomoutBtn = (ImageButton) view.findViewById(R.id.zoomoutBtn);
		locateBtn = (ImageButton) view.findViewById(R.id.locateBtn);
		layerButton = (ImageButton) view.findViewById(R.id.layerButton);
		clearButton = (ImageButton) view.findViewById(R.id.clearButton);
		//cameraButton = (ImageButton) view.findViewById(R.id.cameraButton);
		//shotButton = (ImageButton) view.findViewById(R.id.shotButton);
		streetButton = (ImageButton) view.findViewById(R.id.streetButton);
		//nearBtn = (Button) view.findViewById(R.id.nearBtn);
		//offValveBtn = (Button) view.findViewById(R.id.offValveBtn);
		memuButton = (ImageButton) view.findViewById(R.id.memuButton);
		regionButton = (ImageButton) view.findViewById(R.id.regionButton);
		pointButton = (ImageButton) view.findViewById(R.id.pointButton);
		valueButton = (ImageButton) view.findViewById(R.id.valueButton);
		selectButton = (ImageButton) view.findViewById(R.id.selectButton);
		selectButton2 = (ImageButton) view.findViewById(R.id.selectButton2);
		mianjButton = (ImageButton) view.findViewById(R.id.mianjButton);
		// 比例尺
		scaleText = (TextView) view.findViewById(R.id.btnScaleText);
		btnScale = (Button) view.findViewById(R.id.btnScale);

		footerView = (LinearLayout) view.findViewById(R.id.footerView);// 画图容器
		footerView.setVisibility(View.GONE);
		setBtn = (Button) view.findViewById(R.id.setBtn);

		mMapView = (MapView) view.findViewById(R.id.arcMapView);
/*		mMapView.getGrid().setVisibility(false);//不显示地图容器网格
		mMapView.setBackgroundColor(Color.WHITE);*/

		mMapView.setMapBackground(getResources().getColor(R.color.white), getResources().getColor(R.color.white), 0, 0);

		header_container = (LinearLayout) view.findViewById(R.id.header_container);

		OnClickListener btnClickListener= new OnClickListener(){
			@SuppressLint("NewApi")
			public void onClick(View v){
				switch(v.getId()){
					case R.id.pointButton:
						if (mapCaiCeUtil==null){
							mapCaiCeUtil=new MapCaiCeUtil(MyMapView.this,context);
						}else {
							mapCaiCeUtil.clearAllLayer();
						}
						mapCaiCeUtil.setDrawType(MapCaiCeUtil.POINT);
						Toast.makeText(AppContext.getInstance(),"请采点",Toast.LENGTH_SHORT).show();
						break;
					case R.id.valueButton:
						if (mapCaiCeUtil==null){
							mapCaiCeUtil=new MapCaiCeUtil(MyMapView.this,context);
						}else {
							mapCaiCeUtil.clearAllLayer();
						}
						mapCaiCeUtil.setDrawType(MapCaiCeUtil.LINE);
						Toast.makeText(AppContext.getInstance(),"请采线",Toast.LENGTH_SHORT).show();
						break;
/*				case R.id.layerButton:
					if (drawGraphic==null){
						drawGraphic = new DrawGraphicTools(MapViewer.this,mMapView);
					}
					drawGraphic.draw(DrawGraphicTools.DRAW_GRAPHIC_TYPE.FRE_PL);
					break;*/
				case R.id.clearButton:
					/*if (drawGraphic != null){
						drawGraphic.exit();
					}*/
					mMapView.removeAll();
					MapLoad.LoadMap(mMapView);
					// resetTouchListener();
					break;
				case R.id.layerButton:
					if (layerSelect == null){
						layerSelect	= new LayerSelector(context,layerButton,mMapView);
					}
					layerSelect.popu();
					break;
				case R.id.setBtn:
					final int h=setBtn.getHeight();
					menuWindow.showAtLocation(setBtn, Gravity.BOTTOM, 0, h); //
					//;
					break;
				case R.id.streetButton:   //全景图
					/*if (!mMapView.isShowStreetScape()){
						mMapView.setStreetScape(true);
						streetButton.setImageDrawable(getResources().getDrawable(R.drawable.main_map_icon_streetscape_normal));
					}else{
						mMapView.setStreetScape(false);
						streetButton.setImageDrawable(getResources().getDrawable(R.drawable.main_map_icon_streetscape));
					}*/
					break;
				case R.id.memuButton:
					menuWindow.showAtLocation(memuButton, Gravity.CENTER, 0, 0);
					break;
//				case R.id.regionButton:
//					if(mapMeterScope==null){
//						mapMeterScope=new MapMeterScope(MyMapView.this, context);
//					}else{
//						mapMeterScope.clearAllLayer();
//					}
//					mapMeterScope.setDrawType(MapMeterScope.POLY);
//					dismiss();
//					break;
				default:
					break;
				}
			}
		};


		// 缩放控制
		OnClickListener zoomClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.zoominBtn:
					mMapView.zoomin();
					break;
				case R.id.zoomoutBtn:
					mMapView.zoomout();
					break;
				}
			}
		};
		// 定位模式
		OnClickListener locatClickListener = new OnClickListener() {
			public void onClick(View v) {
				if (locationManage == null) {
					isLocate = true;
					startLoaction();
					mCurBtnType = E_BUTTON_TYPE.FOLLOW;
					return;
				} else {
					if (!locationManage.isStart()) {
						startLoaction();
					} else {
						switch (mCurBtnType) {
						case LOC:
							// 手动定位请求
							mCurBtnType = E_BUTTON_TYPE.FOLLOW;
							locateBtn
									.setImageResource(R.drawable.ft_loc_normal);
							locationManage.setLocationMode(LocationMode.NORMAL);
							mMapView.setAllowRotationByPinch(false);
							break;
						case COMPASS:// 罗盘
							// myLocationOverlay.setLocationMode(LocationMode.NORMAL);
							locationManage
									.setLocationMode(LocationMode.COMPASS);
							mCurBtnType = E_BUTTON_TYPE.LOC;
							locateBtn
									.setImageResource(R.drawable.ft_loc_compass);// normal
							mMapView.setAllowRotationByPinch(true);
							break;
						case FOLLOW:// 跟随
							// myLocationOverlay.setLocationMode(LocationMode.COMPASS);
							locationManage
									.setLocationMode(LocationMode.FOLLOWING);
							mCurBtnType = E_BUTTON_TYPE.COMPASS;
							locateBtn.setImageResource(R.drawable.ft_loc_fixed);// normal
							mMapView.setAllowRotationByPinch(false);
							break;
						}
					}
				}

			}
		};
		// 属性查询
		OnClickListener searchClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (v.getId()) {
				case R.id.map_search_text:
				case R.id.map_search_img:
//					Intent intent =new Intent(context,AttributeQueryActivity.class);
//					context.startActivity(intent);

					new AttributeQueryView(context,MyMapView.this);

					break;
				case R.id.map_search_voice_img:

					break;
				default:
					break;
				}
			}
		};
		map_search_text.setOnClickListener(searchClickListener);
		map_search_img.setOnClickListener(searchClickListener);
		map_search_voice_img.setOnClickListener(searchClickListener);

		zoominBtn.setOnClickListener(zoomClickListener);
		zoomoutBtn.setOnClickListener(zoomClickListener);
		layerButton.setOnClickListener(btnClickListener);
		clearButton.setOnClickListener(btnClickListener);
		//nearBtn.setOnClickListener(btnClickListener);
		//offValveBtn.setOnClickListener(btnClickListener);
		//cameraButton.setOnClickListener(btnClickListener);
		//shotButton.setOnClickListener(btnClickListener);
		memuButton.setOnClickListener(btnClickListener);
		regionButton.setOnClickListener(btnClickListener);
		pointButton.setOnClickListener(btnClickListener);
		valueButton.setOnClickListener(btnClickListener);
		selectButton.setOnClickListener(btnClickListener);
		selectButton2.setOnClickListener(btnClickListener);
		mianjButton.setOnClickListener(btnClickListener);
		setBtn.setOnClickListener(btnClickListener);
		streetButton.setOnClickListener(btnClickListener);

		locateBtn.setOnClickListener(locatClickListener);

		myTouchListener = new MyTouchListener(this.context, mMapView);
		mMapView.setOnTouchListener(myTouchListener);

		//加载图层
		MapLoad.LoadMap(mMapView);
		setMapParam(MapLoad.mapParam);

		mMapView.setOnStatusChangedListener(new OnStatusChangedListener() {
			public void onStatusChanged(Object source, STATUS status) {
				if (source == mMapView && (status == STATUS.INITIALIZED || status == STATUS.LAYER_LOADED) ){
					startLoaction();
					//重设全局图层变量的layerid
					int cnt =0;
					while (!MapLoad.restLayerId(mMapView)){
						cnt++;
						if (cnt >3){
							break;
						}
					};

					//streetScapeVisible(STREET_SCAPE);

				}else if (status == STATUS.LAYER_LOADING_FAILED){
					System.out.println(source.toString());
				}
				if(status == STATUS.INITIALIZED){
					flgmapInitialized = true;
				}
			}
		});

		//长按使用放大镜来进行设备查询
		setShowMagnifierOnLongPress(true);


		//加载上一次地图范围
		mapTempParam = new MapTempParam(context);
		//loadLastMapExtent();

		/*mPanoView = (PanoramaView) findViewById(R.id.panorama);
		mPanoView.setPanoramaViewListener(this);	
		mPanoView.setShowTopoLink(false);
		mPanoView.setZoomGestureEnabled(false);
		mPanoView.setRotateGestureEnabled(false);
		mPanoView.setPanorama("0100220000130817164838355J5");*/
	}

	public void setExtent(Geometry geometry){
		mMapView.setExtent(geometry);
	}

	public void setExtent(Geometry geometry, int padding, boolean animated){
		mMapView.setExtent(geometry, padding, animated);
	}
	public SpatialReference getSpatialReference(){
		return mMapView.getSpatialReference();
	}
	public void setOnStatusChangedListener(OnStatusChangedListener onStatusChangedListener){
		mMapView.setOnStatusChangedListener(onStatusChangedListener);
	}

	public void zoomToResolution(Point centerPt, double res){
		mMapView.zoomToResolution(centerPt,res);
	}

	public void zoomToScale(Point centerPt, double scale){
		mMapView.zoomToScale(centerPt, scale);
	}

	public Callout getCallout(){
		return mMapView.getCallout();
	}

	public Polygon getExtent(){
		return mMapView.getExtent();
	}

	public double getScale(){
		return mMapView.getScale();
	}

	public void pause(){
		mMapView.pause();
		if (locationManage != null)
			locationManage.pause();
	}

	public void unpause(){
		mMapView.unpause();
		mMapView.invalidate();
		if (locationManage != null)
			locationManage.upPause();
	}

	public int addLayer(Layer layer){
		return mMapView.addLayer(layer);
	}

	public void removeLayer(Layer layer){
		mMapView.removeLayer(layer);
	}

/*	public LocationService getLocationService(){
		return mMapView.getLocationService();
	}
	*/
	public LocationDisplayManager getLocationService(){
		return mMapView.getLocationDisplayManager();//.getLocationService();
	}

	public Layer getLayerByID(long layerID){
		return mMapView.getLayerByID(layerID);
	}

	public Layer getLayerByURL(String url){
		return mMapView.getLayerByURL(url);
	}

	public void setRotationAngle(double degree){
		mMapView.setRotationAngle(degree);
	}

	public void setRotationAngle(double degree, Point centerPt, boolean animated){
		mMapView.setRotationAngle(degree, centerPt, animated);
	}
	public void centerAt(Point centerPt, boolean animated){
		mMapView.centerAt(centerPt, animated);
	}

	public Point toMapPoint(Point src){
		return mMapView.toMapPoint(src);
	}

	public Point toMapPoint(float screenx, float screeny){
		return mMapView.toMapPoint(screenx, screeny);
	}

	public boolean isLoaded(){
		return mMapView.isLoaded();
	}

	public void setAllowRotationByPinch(boolean allowRotationByPinch){
		mMapView.setAllowRotationByPinch(allowRotationByPinch);
	}

	public void setOnTouchListener(MapOnTouchListener l){
		mMapView.setOnTouchListener(l);
	}

	public MapView getMapView(){
		return mMapView;
	}


    //设置地图容器主功能菜单
	public void setMapPopupWindow(final ContainActivity context){

	    final OnClickListener itemsOnClick = new OnClickListener(){

			public void onClick(View v) {
				menuWindow.dismiss();
			}

	    };

		menuWindow = new SelectPicPopupWindow(context,this, itemsOnClick);

	}

	public void destroy(){
		if (locationManage != null)
			locationManage.stop();
	}
	public MapParam getMapParam() {
		return mapParam;
	}

	public void setMapParam(MapParam mapParam) {
		this.mapParam = mapParam;
	}

	public ImageButton getMemuButton() {
		return memuButton;
	}

	public void setMemuButton(ImageButton memuButton) {
		this.memuButton = memuButton;
	}

	public void setStreetScape(boolean showStreetScape){
		this.showStreetScape = showStreetScape;
	}

	public boolean isShowStreetScape() {
		return showStreetScape;
	}

	public void saveCurMapExtent(){
		mapTempParam.saveCurMapExtent();
	}

	public void saveCurMapScale(){
		mapTempParam.saveCurMapScale();
	}

	public void loadLastMapExtent(){
		mapTempParam.loadLastMapExtent();
	}

	public double loadLastMapSacle(){
		return mapTempParam.loadLastMapScale();
	}

	//@Override
	public void setShowMagnifierOnLongPress(boolean showMagnifier) {
		this.showMagnifier = showMagnifier;
	}

	public boolean isShowMagnifierOnLongPress() {
		return showMagnifier;
	}

	public Point getIdentifyMagnifierPoint() {
		return identifyMagnifierPoint;
	}

	public void resetTouchListener() {
		setOnTouchListener(myTouchListener);
	}

	private void startLoaction() {
		if (isLocate){
			if (locationManage == null){
				locationManage = new LocationManage(context,mMapView);
			}
			locationManage.setLocationMode(LocationMode.NORMAL);
			if (locationManage.start()) {
				mCurBtnType = E_BUTTON_TYPE.LOC;
				locateBtn.setImageResource(R.drawable.ft_loc_normal);
			}
		}
	}

	//地图截屏
	public void mapviewshot() {
        //System.out.println("进入截屏方法");
        Date date=new Date();
        SimpleDateFormat dateformat1=new SimpleDateFormat("yyyyMMdd_hhmmss");
        String timeString=dateformat1.format(date);

        String filename=storePath+timeString;

        File file_2=new File(storePath);
        if (!file_2.exists()){
            System.out.println("path 文件夹 不存在--开始创建");
            file_2.mkdirs();
        }
        filename=getfilepath(filename);//判断是否有同一秒内的截图，有就改名字
        //存储于sd卡上
        System.out.println("获得的filename--"+filename);
        Bitmap bitmap=MapUtil.getViewBitmap(mMapView);

        File file=new File(filename);
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getfilepath(String filename) {
        String filestr=filename+".png";
        File file=new File(filestr);
        if (file.exists()){
            filename=getfilepath(filename+"_1");
        }
        else {
            filename=filestr;
        }
        System.out.println("getfilename函数返回----"+filename);
        return filename;
    }


	public LinearLayout getMap_search_layout() {
		return map_search_layout;
	}

	public void setMap_search_layout(LinearLayout map_search_layout) {
		this.map_search_layout = map_search_layout;
	}

	public LinearLayout getMapLinearDraw() {
		return mapLinearDraw;
	}

	public void setMapLinearDraw(LinearLayout mapLinearDraw) {
		this.mapLinearDraw = mapLinearDraw;
	}

	public Button getDrawMapbtnAgainDraw() {
		return drawMapbtnAgainDraw;
	}

	public void setDrawMapbtnAgainDraw(Button drawMapbtnAgainDraw) {
		this.drawMapbtnAgainDraw = drawMapbtnAgainDraw;
	}

	public Button getDrawMapbtnRecall() {
		return drawMapbtnRecall;
	}

	public void setDrawMapbtnRecall(Button drawMapbtnRecall) {
		this.drawMapbtnRecall = drawMapbtnRecall;
	}

	public Button getDrawMapbtnConfirm() {
		return drawMapbtnConfirm;
	}

	public void setDrawMapbtnConfirm(Button drawMapbtnConfirm) {
		this.drawMapbtnConfirm = drawMapbtnConfirm;
	}

	protected class MyTouchListener extends MapOnTouchListener {

		private NewMagnifier magnifier;

		private String type;
		private Context context;
		private MapView mapView;
		private boolean showmag = false;

		public MyTouchListener(Context context, MapView view) {
			super(context, view);

			this.context = context;
			this.mapView = view;
		}

		public void setType(String geometryType) {
			this.type = geometryType;
		}

		public String getType() {
			return this.type;
		}

		public boolean onSingleTap(MotionEvent e) {//获取一个屏幕点
			// 查询点附近的设施
			//identifyMagnifierPoint = mapView.toMapPoint(x, y);
			try{
				float x = e.getX();
				float y = e.getY();
				identifyDataTop(x, y); // 偶然发现此段会有空指针异常
				return true;
			}catch(NullPointerException ex ){
				return false;
			}
		}

		public void onLongPress(MotionEvent point) {//长按事件 获取一个Point

			if (showMagnifier) {
				if(!flgmapInitialized){
					Toast.makeText(context, "地图尚未加载完……", Toast.LENGTH_LONG).show();
				}else{
					magnify(point);
					showmag = true;
				}
			}
			// super.onLongPress(point);

		}

		public boolean onPinchPointersUp(MotionEvent event){
			/*if (showStreetScape){
				streetScapeVisible(showStreetScape);
			}*/
			return super.onPinchPointersUp(event);
		}


		public boolean onDragPointerMove(MotionEvent from, final MotionEvent to) {//在拖动指针移动
			if (showmag && showMagnifier) {
				magnify(to);
				return true;
			}
			return super.onDragPointerMove(from, to);
		}

		@Override
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {//点后抬起
			if (showmag && showMagnifier) {
				if (magnifier != null) {
					magnifier.hide();
				}
				magnifier.postInvalidate();
				showmag = false;
				// redrawCache = true;
				float x = to.getX();
				float y = to.getY();
				// 查询点附近的设施
				identifyMagnifierPoint = mapView.toMapPoint(x, y);
				identifyDataVisible(x, y);

				return true;
			}
			/*if (showStreetScape){
				streetScapeVisible(showStreetScape);
			}*/
			return super.onDragPointerUp(from, to);
		}

		private void magnify(MotionEvent to) {

			if (magnifier == null) {
				magnifier = new NewMagnifier(context, mapView);
				mapView.addView(magnifier);
				magnifier.prepareDrawingCacheAt(to.getX(), to.getY());
			} else {
				magnifier.prepareDrawingCacheAt(to.getX(), to.getY());
			}
			// redrawCache = false;
		}

		@SuppressWarnings({ "static-access" })
		private void identifyDataTop(float x, float y) {
			// Identify查询设施参数设置
			identifyMagnifierPoint = mapView.toMapPoint(x, y);

			if (mapParam == null){
				return ;
			}
			//防止在加载地图的时候时间过长，没有成功初始化配置图层的图层ID
			int checkLyrId = 0;
			for (Mapservice mapservice : mapParam.getBizMap()) {
				//mapservice.getType() == LAYERTYPE.dynamic &&
				if (mapParam.getLayerIds(mapservice).length>0) {
					checkLyrId++;
				}
			}
			if (checkLyrId==0){
				MapLoad.restLayerId(mapView);
			}
			//初始化计算器及存储变量
			queryCount = 0;

			identifyDatas = new ArrayList<IdentifyData>();
			Point identifyPoint = mapView.toMapPoint(x, y);
			identifyX = x;
			identifyY = y;
			//循环查询
			for (Mapservice mapservice : mapParam.getBizMap()) {
				//mapservice.getType() == LAYERTYPE.dynamic &&
				if (mapParam.getLayerIds(mapservice).length>0) {
					// set Identify Parameters
					params = new IdentifyParameters();
					params.setTolerance(mapParam.getTolerance());//IDEN_TOLERANCE2
					params.setDPI(96);
					params.setLayers(mapParam.getLayerIds(mapservice));
					params.setLayerMode(IdentifyParameters.TOP_MOST_LAYER);//IdentifyParameters.ALL_LAYERS

					params.setGeometry(identifyPoint);
					if (mapView.getSpatialReference().getID()!=0)
					  params.setSpatialReference(mapView.getSpatialReference());
					params.setMapHeight(mapView.getHeight());
					params.setMapWidth(mapView.getWidth());
					Envelope env = new Envelope();
					mapView.getExtent().queryEnvelope(env);
					params.setMapExtent(env);
					params.setReturnGeometry(true);


					MyIdentifyTask mTask = new MyIdentifyTask(identifyPoint,
							mapView, mapservice.getForeign());
					mTask.execute(params);
					queryCount++;

				}
			}
			if (queryCount>0){
				progress = new ProgressDialog(context);
				progress.setMessage("正在查询当中...");
				progress.setButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						progress.dismiss();
					}
				});
				progress.show();
			}
		}
		/**
		 * 设施查询
		 */
		@SuppressWarnings({ "static-access" })
		private void identifyDataVisible(float x, float y) {
			// Identify查询设施参数设置
			identifyMagnifierPoint = mapView.toMapPoint(x, y);

			if (mapParam == null){
				return ;
			}
			//初始化计算器及存储变量
			queryCount = 0;

			//防止在加载地图的时候时间过长，没有成功初始化配置图层的图层ID
			int checkLyrId = 0;
			for (Mapservice mapservice : mapParam.getBizMap()) {
				//mapservice.getType() == LAYERTYPE.dynamic &&
				if (mapParam.getLayerIds(mapservice).length>0) {
					checkLyrId++;
				}
			}
			if (checkLyrId==0){
				MapLoad.restLayerId(mapView);
			}

			identifyDatas = new ArrayList<IdentifyData>();
			identifyX = x;
			identifyY = y;
			//循环查询
			for (Mapservice mapservice : mapParam.getBizMap()) {
				//mapservice.getType() == LAYERTYPE.dynamic &&
				if (mapParam.getLayerIds(mapservice).length>0) {
					// set Identify Parameters
					params = new IdentifyParameters();
					params.setTolerance(mapParam.getTolerance()); //IDEN_TOLERANCE
					params.setDPI(96);
					params.setLayers(mapParam.getLayerIds(mapservice));
					params.setLayerMode(IdentifyParameters.VISIBLE_LAYERS);//IdentifyParameters.ALL_LAYERS
					Point identifyPoint = mapView.toMapPoint(x, y);
					params.setGeometry(identifyPoint);
					if (mapView.getSpatialReference().getID()!=0)
					  params.setSpatialReference(mapView.getSpatialReference());
					params.setMapHeight(mapView.getHeight());
					params.setMapWidth(mapView.getWidth());
					Envelope env = new Envelope();
					mapView.getExtent().queryEnvelope(env);
					params.setMapExtent(env);
					params.setReturnGeometry(true);


					MyIdentifyTask mTask = new MyIdentifyTask(identifyPoint,
							mapView, mapservice.getForeign());
					mTask.execute(params);
					queryCount++;

				}
			}
			if (queryCount>0){
				progress = new ProgressDialog(context);
				progress.setMessage("正在查询当中...");
				progress.setButton("取消", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						progress.dismiss();
					}
				});
				progress.show();
			}
		}
	}
	
	
/*	public void streetScapeVisible(boolean visible) {

		if (popuPanoramaView ==null){
			popuPanoramaView = new MyPopuPanoramaView(context,null);
		}
		if (streetScapeGpLayer == null){
			streetScapeGpLayer = new GraphicsLayer();
			pmSymbol = new PictureMarkerSymbol(getResources().getDrawable(
					R.drawable.main_map_icon_streetscape_selected));
			mMapView.addLayer(streetScapeGpLayer);
		}
		streetScapeGpLayer.removeAll();
		gp = new Graphic(mMapView.getCenter(), pmSymbol);
		streetScapeGpLayer.addGraphic(gp);
		streetScapeGpLayer.setVisible(visible);
		
		if (visible){
			popuPanoramaView.showPopu(AppContext.getInstance().getCoordTransform(),
					mMapView.getSpatialReference(), mMapView.getCenter());
		}else{
			popuPanoramaView.hidePopu();
		}
		
		String position = BaiduAppProxy.toBd09Position(AppContext.getInstance().getCoordTransform(),
				getSpatialReference(),	getCenter().getX(),getCenter().getY());
		BaiduAppProxy.callStreetScape(context, position);
	}*/
	/**
	 * This class allows the user to customize the string shown in the callout.
	 * By default its the display field name.
	 *
	 */
	protected class MyIdentifyAdapter extends IdentifyResultSpinnerAdapter {
		String m_show = null;
		List<IdentifyResult> resultList;
		int currentDataViewed = -1;
		Context m_context;

		public MyIdentifyAdapter(Context context, List<IdentifyResult> results) {
			super(context, results);
			this.resultList = results;
			this.m_context = context;

		}

		// This is the view that will get added to the callout
		// Create a text view and assign the text that should be visible in the
		// callout
		public View getView(int position, View convertView, ViewGroup parent) {
			String outputVal = null;
			TextView txtView;
			IdentifyResult curResult = this.resultList.get(position);

			if (curResult.getAttributes().containsKey("Name")) {
				outputVal = curResult.getAttributes().get("Name").toString();
			}

			txtView = new TextView(this.m_context);
			txtView.setText(outputVal);
			txtView.setTextColor(Color.BLACK);
			txtView.setLayoutParams(new ListView.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			txtView.setGravity(Gravity.CENTER_VERTICAL);

			return txtView;
		}

	}

	private class MyIdentifyTask extends
			AsyncTask<IdentifyParameters, Void, IdentifyResult[]> {

		IdentifyTask mIdentifyTask;
		// Point mAnchor;
		MapView view;

		String identifyTaskUrl;

		private int serviceId;
		private Mapservice mapService;

		MyIdentifyTask(Point anchorPoint, MapView view, String identifyTaskUrl) {
			// mAnchor = anchorPoint;
			this.view = view;
			this.identifyTaskUrl = identifyTaskUrl;
			mapService = mapParam.findMapservice(identifyTaskUrl);
			if (mapService!=null){
				serviceId = mapService.getId();
			}else{
				serviceId = -1;
			}
		}

		@Override
		protected IdentifyResult[] doInBackground(IdentifyParameters... params) {
			if (view.isLoaded()){
				IdentifyResult[] mResult = null;
				if (params != null && params.length > 0) {
					IdentifyParameters mParams = params[0];
					try {
						mResult = mIdentifyTask.execute(mParams);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//如果查询异常计算器减1
						//queryCount--;
						e.printStackTrace();
						mhandler.sendEmptyMessage(Constant.MSG_IDEN_RETURN);
					}
				}
				return mResult;
			}
			return null;
		}

		@Override
		protected void onPostExecute(IdentifyResult[] results) {
			// TODO Auto-generated method stub
			if (results != null && results.length > 0) {
				analysisIdentify(results,serviceId);
			}else{
				//查询没有结果计算器减1
				//queryCount--;
				mhandler.sendEmptyMessage(Constant.MSG_IDEN_RETURN);
			}
		}

		@Override
		protected void onPreExecute() {
			try{
				mIdentifyTask = new IdentifyTask(identifyTaskUrl);//初始化Identify服务
			}catch(Exception ex){
				mhandler.sendEmptyMessage(Constant.MSG_IDEN_RETURN);
			}
		}

		//查询结果初步处理
		private synchronized void  analysisIdentify(IdentifyResult[] identifyAll,int serviceId) {

			//
			try{
/*				for(int r=0;r<identifyAll.length;r++){
					if (identifyAll[r] == null){
						continue;
					}
					IdentifyResult idenResult = identifyAll[r];*/
				for (IdentifyResult idenResult : identifyAll) {
					//
					if (identifyDatas.size()==0){
						Ftlayer ftLyr = mapParam.findFtlayer(serviceId, idenResult.getLayerId());

						IdentifyData identifyData = new IdentifyData();
						identifyData.setServiceId(serviceId);
						identifyData.setLayerId(idenResult.getLayerId());
						identifyData.setLayerName(idenResult.getLayerName());
						identifyData.setFtLayer(ftLyr);
						identifyData.getData().add(idenResult);
						identifyDatas.add(identifyData);
					}else{
						int i=0;
						for(IdentifyData identifyData :identifyDatas){
							i++;
							Ftlayer ftLyr = mapParam.findFtlayer(serviceId, idenResult.getLayerId());
							/*if (ftLyr == null){
								break;
							}*/
							//if (identifyData.getLayerId() == idenResult.getLayerId()){
							if (identifyData.getFtLayer().getId() == ftLyr.getId()){
								boolean isExist = false;
								for(IdentifyResult rlt : identifyData.getData()){
									String keyField = ftLyr.getKeyfield();
									if (rlt.getAttributes().get(keyField).toString().equals(idenResult.getAttributes().get(keyField).toString())){
									//if (!GeometryEngine.equals(rlt.getGeometry(), idenResult.getGeometry(), MyMapView.this.getSpatialReference())){
										//identifyData.getData().add(idenResult);
										isExist = true;
										break;
									}
								}
								if (!isExist){
									identifyData.getData().add(idenResult);
									break;
								}
	/*							if(!identifyData.getData().contains(idenResult))
									identifyData.getData().add(idenResult);
								break;*/
							}else{
								if (i==identifyDatas.size()){
									IdentifyData identifyData1 = new IdentifyData();
									identifyData1.setServiceId(serviceId);
									identifyData1.setLayerId(idenResult.getLayerId());
									identifyData1.setLayerName(idenResult.getLayerName());
									identifyData1.getData().add(idenResult);
									identifyData1.setFtLayer(ftLyr);
									identifyDatas.add(identifyData1);
									break;
								}
							}
						}
					}
				}
			}catch(NullPointerException ex){
				ex.printStackTrace();
			}
			//有结果计算器减1
			//queryCount--;
			mhandler.sendEmptyMessage(Constant.MSG_IDEN_RETURN);
		}
	}

	private class MapTempParam{
		SharedPreferences pPrefere;
		Editor pEditor;
		String CUR_MAP_BOUND="mapBound";//记录当前地图范围
		String CUR_MAP_SCALE="mapScale";//记录前地图缩放比例

		MapTempParam(Context context){
			if (pPrefere == null) {
				pPrefere = context.getSharedPreferences(AppContext.getInstance().getDefSolution().getName(), 3);
			}
			pEditor = pPrefere.edit();
		}

		public void saveCurMapExtent(){
			Envelope env = new Envelope();
			mMapView.getExtent().queryEnvelope(env);
			String lLeft = env.getLowerLeft().getX()+" "+env.getLowerLeft().getY();
			String uRight = env.getUpperRight().getX()+" "+env.getUpperRight().getY();

			pEditor.putString(CUR_MAP_BOUND,lLeft+","+uRight);
			pEditor.commit();
		}

		//保存当前地图缩放比例
		public void saveCurMapScale(){
			pEditor.putString(CUR_MAP_SCALE,String.valueOf(mMapView.getScale()));
			pEditor.commit();
		}

		public void loadLastMapExtent(){
			String senv = pPrefere.getString(CUR_MAP_BOUND, "");
			if (!"".equals(senv)){
				String cor[] = senv.split("\\,");

				String lLeft[] = cor[0].split("\\ ");
				String uRight[] = cor[1].split("\\ ");

				Envelope env = new Envelope();

				env.setXMin(Double.parseDouble(_transformE(lLeft[0])));
				env.setYMin(Double.parseDouble(_transformE(lLeft[1])));
				env.setXMax(Double.parseDouble(_transformE(uRight[0])));
				env.setYMax(Double.parseDouble(uRight[1]));
				mMapView.setExtent(env);
			}
		}

		//加载最后一次保存缩放比例,如果没有以600为初始值
		public double loadLastMapScale(){
			return Double.valueOf(pPrefere.getString(CUR_MAP_SCALE, "600"));
		}
		private String _transformE(String x){
			StringBuffer coord = new StringBuffer();
			int idx = x.indexOf("E");
			if (idx<0){
				return x;
			}
			String startStr = x.substring(0, idx);
			String endStr = x.substring(idx+1);
			idx = startStr.indexOf(".");
			coord.append(startStr.substring(0, idx));
			startStr = startStr.substring(idx+1);
			idx = Integer.valueOf(endStr);
			coord.append(startStr.substring(0, idx));
			if(idx<startStr.length()){
				coord.append(".");
				coord.append(startStr.substring(idx));
			}
			return coord.toString();
		}
	}


/*	@Override
	public void onLoadPanoramBegin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadPanoramaEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadPanoramaError() {
		// TODO Auto-generated method stub
		
	}*/

	public LinearLayout getHeaderContainer(){
		return header_container;
	}

	public LinearLayout getMapLinearPipeBro() {
		return mapLinearPipeBro;
	}

	public void setMapLinearPipeBro(LinearLayout mapLinearPipeBro) {
		this.mapLinearPipeBro = mapLinearPipeBro;
	}

	public Button getPipeBroMapClear() {
		return pipeBroMapClear;
	}

	public void setPipeBroMapClear(Button pipeBroMapClear) {
		this.pipeBroMapClear = pipeBroMapClear;
	}

	public Button getPipeBroMapConfirm() {
		return pipeBroMapConfirm;
	}

	public void setPipeBroMapConfirm(Button pipeBroMapConfirm) {
		this.pipeBroMapConfirm = pipeBroMapConfirm;
	}

	public LinearLayout getFooterView() {
		return footerView;
	}

	public void setFooterView(LinearLayout footerView) {
		this.footerView = footerView;
	}

	public Button getSetBtn() {
		return setBtn;
	}

	public void setSetBtn(Button setBtn) {
		this.setBtn = setBtn;
	}

	public ImageButton getRegionButton() {
		return regionButton;
	}

	public ImageButton getPointButton() {
		return pointButton;
	}

	public ImageButton getValueButton() {
		return valueButton;
	}

	public ImageButton getSelectButton() {
		return selectButton;
	}

	public ImageButton getSelectButton2() {
		return selectButton2;
	}

	public ImageButton getMianjButton() {
		return mianjButton;
	}

	public void setMianjButton(ImageButton mianjButton) {
		this.mianjButton = mianjButton;
	}

	public ImageButton getLayerButton() {
		return layerButton;
	}

	public void setLayerButton(ImageButton layerButton) {
		this.layerButton = layerButton;
	}

	public ImageButton getClearButton() {
		return clearButton;
	}

	public void setClearButton(ImageButton clearButton) {
		this.clearButton = clearButton;
	}



}
