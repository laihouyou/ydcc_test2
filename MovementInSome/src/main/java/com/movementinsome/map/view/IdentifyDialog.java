package com.movementinsome.map.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.baidu.BaiduAppProxy;
import com.baidu.mapapi.model.LatLng;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.identify.IdentifyResult;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.initial.model.Field;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.MapParam;
import com.movementinsome.map.utils.BufferQueryTools;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.BaiduAppProxy.toBd09Position;

public class IdentifyDialog extends AlertDialog implements OnGestureListener,
		OnTouchListener, OnLongClickListener, OnCheckedChangeListener {

	private LinearLayout idfLinearTitle; // 添加查询到的设施标题
	private LinearLayout idfLinearMessage; // 添加单个设施的详细信息
	private LinearLayout idfLinearWTable; // 填写表单选项
	private LinearLayout idfLinearButton; // 按钮容器
	private LinearLayout idfLinearButton2; // 按钮容器2（填表选项：返回，确定）

	private ViewFlipper idfFlipper; // 界面切换
	private ListView idfListview; // 查询到的数据
	private Button idfbtnFinish; // 关闭查询Dialog
	private Button idfbtnFlipper; // 查看详细和列表之间切换
	private Button idfbtnWTable; // 选项填表
	private Button idfbtnGoMap; // 定位到地图
	private Button idfBtnConfirm; // 确定填表
	private Button idfBtnBack; // 返回（取消填表）
	private Button idfbtnGoto; // 导航到这去

	private RadioGroup idfRadioGroupQUB; // 单选组
	private int tableQU = 3; // 选择那个选项

	private Intent intentTable;
	private GestureDetector detector;
	//private List<String> listName; // 查询到的设施的名称
	private List<String> listNumber; // 相应设施的显示编号
	//private List<List<IdentifyResult>> listIdentify;
	private List<TextView> listTextView; // 根据设施名称来创建显示textview
	//private int listNameID = 0; // 所选择设施名称的序号
	private int position = 0; // Listview中所选择的position
	private boolean flgFilpper = false; // 设施列表显示和单个设施详细显示的切换
	private String layerId = ""; // 当前设施类型的图层id号
	private IdentifyAdapter idfadapter;
	//private IdentifyMapData identiData;
	private MyMapView view;
	public TextView tv; // 渲染显示的信息
	public Graphic graphic; // 渲染的图标
	private Context context;
	
	private ArrayList<IdentifyData> identifyDatas;
	private MapParam mapParam;
	private int index=-1;
	private BufferQueryTools nbqTools;
	private boolean isFinishMapActivity;

	public IdentifyDialog(Context context, MyMapView view,ArrayList<IdentifyData> identifyDatas,MapParam mapParam,boolean isFinishMapActivity) {
		super(context);
		this.context = context;
		//this.listIdentify = listIdentify;
		//this.listName = listName;
		this.view = view;
		this.identifyDatas = identifyDatas;
		this.mapParam = mapParam;
		this.isFinishMapActivity=isFinishMapActivity;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.identify_layouter_dialog);
		//identiData = new IdentifyMapData();
		detector = new GestureDetector(this);
		idfListview = (ListView) findViewById(R.id.idfListview);
		idfFlipper = (ViewFlipper) findViewById(R.id.idfFlipper);
		idfLinearTitle = (LinearLayout) findViewById(R.id.idfLinearTitle);
		idfLinearWTable = (LinearLayout) findViewById(R.id.idfLinearWTable);
		idfLinearButton = (LinearLayout) findViewById(R.id.idfLinearButton);
		idfLinearButton2 = (LinearLayout) findViewById(R.id.idfLinearButton2);
		idfLinearMessage = (LinearLayout) findViewById(R.id.idfLinearMessage);
		idfFlipper.setOnLongClickListener(this);
		idfFlipper.setOnTouchListener(this);
		idfBtnBack = (Button) findViewById(R.id.idfBtnBack);
		idfbtnGoMap = (Button) findViewById(R.id.idfbtnGoMap);
		idfbtnWTable = (Button) findViewById(R.id.idfbtnWTable);
		idfbtnFinish = (Button) findViewById(R.id.idfbtnFinish);
		idfbtnFlipper = (Button) findViewById(R.id.idfbtnFlipper);
		idfBtnConfirm = (Button) findViewById(R.id.idfBtnConfirm);
		idfbtnGoto = (Button) findViewById(R.id.idfbtnGoto);
		if(isFinishMapActivity){
			idfbtnWTable.setVisibility(View.VISIBLE);
		}else{
			idfbtnWTable.setVisibility(View.GONE);
		}

		
		android.view.View.OnClickListener myMainBtnClickListener = new android.view.View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				position = idfadapter.getPosition();
				switch (v.getId()) {
				case R.id.idfbtnFinish:
					if (nbqTools!=null){
						nbqTools.quit();
					}
					IdentifyDialog.this.dismiss();
					break;

				case R.id.idfbtnGoMap:
					/*goMapLocation(listIdentify.get(listNameID).get(position),"location");*/
					nbqTools = new BufferQueryTools(context,view);
					
					nbqTools.drawResult(identifyDatas.get(index).getData().get(position));
					IdentifyDialog.this.hide();
					break;

				case R.id.idfbtnFlipper:
					if(!flgFilpper){
						idfbtnFlipper.setText("返回");
						idfFlipper.showNext();

						
						showDetail(identifyDatas.get(index).getData().get(position),identifyDatas.get(index).getServiceId());
					}else{
						idfbtnFlipper.setText("查看");
						idfFlipper.showPrevious();
					}
					flgFilpper = !flgFilpper;
					break;

				case R.id.idfbtnWTable:
					if(isFinishMapActivity){
						if(AppContext.getInstance().getmHandle()!=null){
							AppContext.getInstance().getmHandle().sendEmptyMessage(1);
						}
						AppContext.getInstance().setMapIden(identifyDatas.get(index).getData().get(position));
						((Activity) context).finish();
						dismiss();
					}
					break;
				case R.id.idfBtnConfirm:
					
					//writeTableClick(tableQU);
					break;
				case R.id.idfBtnBack:
					//设置隐藏radio选择，和确定按钮；显示list数据
					idfLinearWTable.setVisibility(View.GONE);
					idfLinearButton2.setVisibility(View.GONE);
					idfLinearButton.setVisibility(View.VISIBLE);
					break;
				case R.id.idfbtnGoto://到这去
					Geometry geo = identifyDatas.get(index).getData().get(position).getGeometry();
					List<Point> arcPoints = new ArrayList();
					if (AppContext.getInstance().getCurLocation() != null){
						
						arcPoints.add(new Point(AppContext.getInstance().getCurLocation().getMapx(),AppContext.getInstance().getCurLocation().getMapy()));
						
						if (geo instanceof Point) {
							Point point = (Point) geo;
							arcPoints.add(point);
						} else if (geo instanceof Polyline) {
							Polyline pline = (Polyline) geo;
							
							arcPoints.add(pline.getPoint(0));
						} else if (geo instanceof Polygon) {
							// Polygon pgon = (Polygon)
							// mMapIden.getGeometry();
							Envelope env = new Envelope();
							geo.queryEnvelope(env);
							arcPoints.add(env.getCenter());
						}
						List<LatLng> latLngs=new ArrayList<>();
						latLngs.removeAll(latLngs);
						for (Point point :arcPoints){
							String position = toBd09Position( AppContext.getInstance().getCoordTransform(),view.getSpatialReference(),point.getX(), point.getY());
							double x = Double.valueOf(position.split(" ")[0]);
							double y = Double.valueOf(position.split(" ")[1]);
							LatLng latLng=new LatLng(y,x);
							latLngs.add(latLng);
						}
						BaiduAppProxy.CallBaiduNavigationLatLng((Activity) context, BNRoutePlanNode.CoordinateType.BD09LL,latLngs.get(0),latLngs.get(1));
//						BaiduAppProxy.navigatorViaPoints((Activity)context, AppContext.getInstance().getCoordTransform(), view.getSpatialReference(), arcPoints);
					}
					break;
				default:
					break;
				}
			}
		};
		
		idfbtnGoMap.setOnClickListener(myMainBtnClickListener);
        idfbtnWTable.setOnClickListener(myMainBtnClickListener);
		idfbtnFinish.setOnClickListener(myMainBtnClickListener);
		idfbtnFlipper.setOnClickListener(myMainBtnClickListener);
		idfBtnConfirm.setOnClickListener(myMainBtnClickListener);
		idfbtnGoto.setOnClickListener(myMainBtnClickListener); 
		
		idfRadioGroupQUB = (RadioGroup) findViewById(R.id.idfRadioGroupBUQ);
		idfRadioGroupQUB.setOnCheckedChangeListener(this);

		// 创建设施标题
		IdentifyData();
		// 更新标题（颜色区分）
		addIdentifyTitle(0);

		// 查询数据显示和对应事件
		tv = new TextView(this.getContext());
		tv.setBackgroundColor(Color.LTGRAY);
		tv.setTextColor(Color.BLUE);
		tv.setOnClickListener(new android.view.View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IdentifyDialog.this.show();
				view.getCallout().hide();
			}
		});
	}

	
	public void reShow(){
		IdentifyDialog.this.show();
	}
	/**
	 * 1.创建标题
	 */
	private void IdentifyData() {

		listTextView = new ArrayList<TextView>();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.FILL_PARENT);
		params.weight = 1.0f;
		for (int i = 0; i < identifyDatas.size(); i++) {
			final TextView tv = new TextView(this.getContext());
			tv.setText(identifyDatas.get(i).getLayerName());
			tv.setPadding(5, 0, 5, 0);
			// if(mapActivity.pRes.getString(R.string.VersionsType).equals("HD"))//判断是否为HD版本
			/*
			 * if(mapActivity.dm.widthPixels>480&&mapActivity.dm.heightPixels>800
			 * ) tv.setTextSize(23);
			 */
			tv.setGravity(Gravity.CENTER);
			tv.setEms(4);
			tv.setBackgroundResource(R.drawable.btn_bg);
			tv.setLayoutParams(params);
			//tv.setId(identifyDatas.get(i).getLayerId());
			tv.setId(i);
			tv.setOnClickListener(new android.view.View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//listNameID = (int) (tv.getId() - 1000);
					addIdentifyTitle(tv.getId());
				}
			});

			listTextView.add(tv);
			/*if (i >= 4) {
				break;
			}*/
		}

	}

	/**
	 * 2.动态显示和更新设施的类型（颜色区分）
	 * 
	 */
	private void addIdentifyTitle(int vid) {
		idfLinearTitle.removeAllViews();

		//for (int i = 0; i < listTextView.size(); i++) {
		for(int i=0; i<identifyDatas.size();i++){
			//if (identifyDatas.get(i).getLayerId() == vid) {
			if (i == vid) {
				listTextView.get(i).setTextColor(Color.argb(200, 255, 0, 0));
				index = i;
			} else {
				listTextView.get(i)
						.setTextColor(Color.argb(200, 255, 255, 255));
			}
			idfLinearTitle.addView(listTextView.get(i));
		}
		addIdentifyNumber(vid);
	}

	/**
	 * 3.动态创建添加查询到的部件编号
	 */
	private void addIdentifyNumber(int vid) {

		if (identifyDatas.size() == 0){
			return;
		}
		/**
		 * 根据滑动选择不同的设施层 显示不同的列表数据
		 */
		listNumber = new ArrayList<String>();
		//String strNumber = "设备编号";
		
		
		//for(int i=0;i<identifyDatas.size();i++){
			//for(Ftlayer ftLayer :mapParam.getFtlayers()){
				//if (ftLayer.getMapservice() != null && identifyDatas.get(i).getServiceId()== ftLayer.getMapservice().getId() && identifyDatas.get(i).getLayerId() == vid) {
					Ftlayer ftLayer =identifyDatas.get(vid).getFtLayer();
					for(IdentifyResult iden: identifyDatas.get(vid).getData()){						
						listNumber.add(iden.getAttributes().get(ftLayer.getKeyfield())==null?"":iden.getAttributes().get(ftLayer.getKeyfield()).toString());						
					}
					//break;
				//}
			//}
		//}
		
		updataListview();
	}

	private void updataListview() {
		idfadapter = new IdentifyAdapter((Activity)this.context,R.layout.identify_list_item, listNumber,idfListview);
		idfListview.setAdapter(idfadapter);
		idfListview.setItemsCanFocus(false);
	}

	/*	*//**
	 * 返回到地图定位渲染
	 */
	/*
	 * @SuppressWarnings("static-access") private void
	 * goMapLocation(IdentifyResult attr ,String showType){ Point point = null;
	 * //渲染到对应的点
	 * 
	 * String strGeometry = attr.getGeometry().getType().name();
	 * identiData.geometryType = strGeometry;
	 * 
	 * //---创建一个获取设施实体坐标点信息的数组容器 //identiData.points = new ArrayList<Object>();
	 * 
	 * if(strGeometry.equalsIgnoreCase("Polyline")){ Polyline polyline =
	 * (Polyline)attr.getGeometry(); // Envelope enve = new Envelope(); //
	 * polyline.queryEnvelope(enve);
	 * 
	 * SimpleLineSymbol sline = new SimpleLineSymbol(Color.BLUE, 3); graphic =
	 * new Graphic(polyline, sline); //定位到选择的点 point = new
	 * Point(identiData.disLongitude, identiData.disLatitude);
	 * 
	 * 
	 * 获取设施实体坐标信息 目前被注释，将用手指点击屏幕的点代替 不管线和点用一样，只有一个点
	 * 
	 * List<String> pointXY; for(int i = 0; i< polyline.getPointCount() ; i++ ){
	 * pointXY = new ArrayList<String>(); point = polyline.getPoint(i);
	 * pointXY.add(point.getX()+""); pointXY.add(point.getY()+"");
	 * identiData.points.add(pointXY); } //定位到中点 //point = new
	 * Point(enve.getCenterX(), enve.getCenterY()); //获取点用于表单显示和反定位到地图
	 * //identiData.latitude = point.getY(); //identiData.longitude =
	 * point.getX(); }else if(strGeometry.equalsIgnoreCase("Point")){ point =
	 * (Point)attr.getGeometry(); graphic = new Graphic(point, new
	 * PictureMarkerSymbol(
	 * mapActivity.pRes.getDrawable(R.drawable.map_inlocation)));
	 * 
	 * }
	 * 
	 * //添加图层 mapActivity.grapLayer.removeAll();
	 * mapActivity.grapLayer.addGraphic(graphic); //显示信息（冒泡模式）
	 * tv.setText(attr.getLayerName()+":\n"+listNumber.get(position));
	 * mapActivity.mMapView.zoomToScale(point, 300d);
	 * mapActivity.mMapView.getCallout().show(point, tv); //如果是填写表单，则暂时隐藏
	 * if(!"location".equals(showType)){
	 * mapActivity.mMapView.getCallout().hide(); } }
	 */

	
	private void showDetail(IdentifyResult attr,int serviceId){

		LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams( 
				10, ViewGroup.LayoutParams.MATCH_PARENT); 
		
		LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 
		
		LinearLayout.LayoutParams paramsPLayout = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 

		LinearLayout pushPLayout = new LinearLayout(context);
		pushPLayout.setOrientation(LinearLayout.VERTICAL);
		pushPLayout.setLayoutParams(paramsPLayout);
		pushPLayout.setGravity(Gravity.CENTER_VERTICAL);

		
		
		//Cursor cursor = GddstApplication.ZZDB.fieldsSelect("identify",attr.getLayerId()+"");
		//while(cursor.moveToNext()){
		
		Ftlayer ftlayer = mapParam.findFtlayer(serviceId, attr.getLayerId());
		if (ftlayer == null){
			return;
		}
		
		for(Field field : ftlayer.getFields()){
			
			if (field.isVisible()){
		
				String value=attr.getAttributes().get(field.getName())+"";
				if ("null".equals(value)){
					value=attr.getAttributes().get(field.getAlias())+"";
				}
				String str = field.getAlias(); //cursor.getString(cursor.getColumnIndex("alias"));
				
				/*if(attr!=null&& attr.getAttributes()!=null&&attr.getAttributes().get(str)!=null){
					 value = 
				}*/
				TextView tvValue = new TextView(context);
				tvValue.setTextColor(Color.BLACK);
				tvValue.setPadding(5, 0, 0, 0);
				tvValue.setGravity(Gravity.CENTER_VERTICAL);
				tvValue.setText(value);
				
				TextView tvKey = new TextView(context);
				tvKey.setTextColor(Color.BLACK);
				tvKey.setGravity(Gravity.CENTER_VERTICAL);
				tvKey.setText(str);
				tvKey.setEms(4);
	
				/*if(PActivity.pRes.getString(R.string.VersionsType).equals("HD")){
					tvKey.setTextSize(25);
					tvValue.setTextSize(25);
				}*/
				
		/*		if(mapActivity.dm.widthPixels>480&&mapActivity.dm.heightPixels>800)	{
					tvKey.setTextSize(18);
					tvValue.setTextSize(18);
				}*/
				TextView tvImage = new TextView(context);
				tvImage.setLayoutParams(paramsImage);
				tvImage.setBackgroundResource(R.drawable.task_fg);
				
				LinearLayout cLayout = new LinearLayout(context);
				cLayout.setBackgroundResource(R.drawable.input_bg);
				cLayout.setGravity(Gravity.CENTER_VERTICAL);
				cLayout.setLayoutParams(paramsLayout);
				cLayout.addView(tvKey);
				cLayout.addView(tvImage);
				cLayout.addView(tvValue);
				cLayout.setPadding(10, 5, 5, 5);
				
				pushPLayout.addView(cLayout);
			}
		}
		idfLinearMessage.removeAllViews();
		idfLinearMessage.addView(pushPLayout);
	}


	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
/*		boolean flgonFling = false;
		if (!idfLinearWTable.isShown())
			if (!flgFilpper) {
				if (e1.getX() - e2.getX() > 120) {// 下一页

					listNameID++;
					if (listNameID >= identifyDatas.size()) {
						listNameID = 0;
					}
					flgonFling = true;
				} else if (e1.getX() - e2.getX() < -120) {// 上一页

					listNameID--;
					if (listNameID < 0) {
						listNameID = identifyDatas.size() - 1;
					}
					flgonFling = true;
				}
				if (flgonFling) {
					position = 0;
					addIdentifyTitle();
				}
			}*/
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		detector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * 屏蔽按键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int arg1) {
		// TODO Auto-generated method stub

	}
}
