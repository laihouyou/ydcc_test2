package com.movementinsome.map.nearby;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AmapNaviType;
import com.baidu.BaiduAppProxy;
import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.util.ArcgisToBd09;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.map.utils.BufferQueryTools;
import com.movementinsome.map.view.MyMapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NearByTools implements OnClickListener,
		OnGestureListener {

	private ViewFlipper nearby_viewpager;// 按钮容器
	private LinearLayout pageGuide;// 页码容器
	private int pageCount;
	private int imageShow = 0;
	private GestureDetector detector;
	private ArrayList<HashMap<String, Object>> bnt_data;// 按钮配置
	private ListView nearby_fac_type_list;// 附近设施类型列表
	private ListView nearby_fac_list;// 附近设施列表
	private Button nearby_facList_Back;
	private List<List<FacTypeObj>> fac_type_data;// 附近设施类型配置
	private List<Mapservice> mapServices;
	private List<Ftlayer> ftlayers;
	
	private View nearByView;
	private ContainActivity context;
	private PopupWindow mPopupWindow;
	private int[] location;
	private MyMapView mapView;
	private GraphicsLayer grapLayer;
	private List<Module> lstModule;
	private Button nearby_Back;
	private ViewFlipper neary_vf;

	public NearByTools(ContainActivity context,MyMapView mapView,List<Mapservice> imapService,List<Ftlayer> iftLayer,int[] location) {
		/*super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_nearby);*/
		this.context = context;
		this.mapView = mapView;
		grapLayer = new GraphicsLayer();
		this.mapView.addLayer(grapLayer);
		
		nearByView = context.getLayoutInflater().inflate(
				R.layout.nearby_activity, null);

		nearby_viewpager = (ViewFlipper) nearByView.findViewById(R.id.nearby_viewpager);// 按钮容器
		nearby_Back=(Button) nearByView.findViewById(R.id.nearby_Back);
		nearby_facList_Back= (Button) nearByView.findViewById(R.id.nearby_facList_Back);
		neary_vf = (ViewFlipper) nearByView.findViewById(R.id.neary_vf);
		nearby_facList_Back.setOnClickListener(this);
		nearby_Back.setOnClickListener(this);
		detector = new GestureDetector(this);
		nearby_viewpager.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				detector.onTouchEvent(event);
				return false;
			}
		});
		pageGuide = (LinearLayout) nearByView.findViewById(R.id.pageGuide);// 页码容器
		nearby_fac_type_list = (ListView) nearByView.findViewById(R.id.nearby_fac_type_list);// 附近设施类型列表
		nearby_fac_list = (ListView) nearByView.findViewById(R.id.nearby_fac_list);// 附近设施列表
		
		this.mapServices = imapService;
		this.ftlayers = iftLayer;
		this.location = location;
		
		fac_type_data = new ArrayList<List<FacTypeObj>>();
		
		for (Mapservice mapService : mapServices) {

			List<FacTypeObj> fac_type_d1 = new ArrayList<FacTypeObj>();
			FacTypeObj facTypeObj = new FacTypeObj();
			facTypeObj.setLabel(mapService.getLabel());
			facTypeObj.setForeign(mapService.getForeign());
			facTypeObj.setLocal(mapService.getLocal());

			fac_type_d1.add(facTypeObj);
			for (Ftlayer ftLayer : ftlayers) {
				if (ftLayer.getMapservice() != null && ftLayer.getMapservice().getId() == mapService.getId()) {
					facTypeObj = new FacTypeObj();
					facTypeObj.setLabel(ftLayer.getName());
					facTypeObj.setForeign(mapService.getForeign());
					facTypeObj.setLocal(mapService.getLocal());
					facTypeObj.setLayerIds(ftLayer.getLayerIds().split(","));
					fac_type_d1.add(facTypeObj);
				}
			}
			if (fac_type_d1.size() > 1)
				fac_type_data.add(fac_type_d1);
		}
		nearby_fac_type_list.setAdapter(new FacTypeListAdapter(fac_type_data, context,this.mapView ,grapLayer,this));
		nearby_fac_type_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
			}
		});

		bnt_data = new ArrayList<HashMap<String, Object>>();
		lstModule = AppContext.getInstance().getModuleData(context.getMenu() != null?context.getMenu().getId():null);
		for (Module module : lstModule) {
			if (module.isCanwrite()){
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (module.getIcon()!=null&&!module.getIcon().equals("")){
					int resID = context.getResources().getIdentifier(module.getIcon(), "drawable",
							context.getPackageName());
					map.put("itemImage", resID);//this.getResources().getDrawable(resID)
				}else{
					map.put("itemImage", R.drawable.function);
				}
				map.put("itemText", module.getName());
				map.put("intentId", module.getId());
				bnt_data.add(map);

			}
		}
		setView(bnt_data);
		
		mPopupWindow = new PopupWindow(nearByView,
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(false);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(
				context.getResources(), (Bitmap) null));
		/*mPopupWindow.setTouchInterceptor(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				detector.onTouchEvent(event);
				return false;
			}
		});*/
	}

	public void popu(){
		//int[] location = new int[2];
		//layerButton.getLocationOnScreen(location);
		//mPopupWindow.showAsDropDown(nearByView);
		mPopupWindow.showAtLocation(nearByView, Gravity.CENTER,0, 0);
	}
	
	private void setView(ArrayList<HashMap<String, Object>> data) {
		if (data != null) {
			int n = 0;
			List<ArrayList<HashMap<String, Object>>> ds = new ArrayList<ArrayList<HashMap<String, Object>>>();
			ArrayList<HashMap<String, Object>> d = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < data.size(); ++i) {
				if (n < 8) {
					d.add(data.get(i));
					++n;
				} else {
					ds.add(d);
					d = new ArrayList<HashMap<String, Object>>();
					n = 0;
					d.add(data.get(i));
					++n;
				}
			}
			if (n != 0) {
				ds.add(d);
			}
			for (int j = 0; j < ds.size(); ++j) {
				GridView gridView = (GridView) nearByView.inflate(this.context,
						R.layout.nearbygridview, null);
				gridView.setAdapter(new GridAdapter(this.context, ds.get(j),lstModule));
				nearby_viewpager.addView(gridView);
				ImageView img = new ImageView(this.context);
				if (j == 0) {
					img.setBackgroundResource(R.drawable.yema1_1);
				} else {
					img.setBackgroundResource(R.drawable.yema1_2);
				}
				pageGuide.addView(img);
			}
			pageCount = pageGuide.getChildCount();
			if (pageCount <= 1) {
				pageGuide.setVisibility(View.GONE);
			} else {
				pageGuide.setVisibility(View.VISIBLE);
			}

		}
	}

/*	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}*/

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if (e1.getX() - e2.getX() > 120) {// 下一页
			this.nearby_viewpager.setInAnimation(this.context, R.anim.push_left_in);
			this.nearby_viewpager.setOutAnimation(this.context, R.anim.push_left_out);
			nearby_viewpager.showNext();
			if (pageCount > 1) {
				pageGuide.getChildAt(imageShow).setBackgroundResource(
						R.drawable.yema1_2);
				++imageShow;
				if (imageShow == pageCount) {
					imageShow = 0;
				}
				pageGuide.getChildAt(imageShow).setBackgroundResource(
						R.drawable.yema1_1);
			}
		} else if (e1.getX() - e2.getX() < -120) {// 上一页
			this.nearby_viewpager.setInAnimation(this.context, R.anim.push_right_in);
			this.nearby_viewpager.setOutAnimation(this.context, R.anim.push_right_out);
			nearby_viewpager.showPrevious();
			if (pageCount > 1) {
				pageGuide.getChildAt(imageShow).setBackgroundResource(
						R.drawable.yema1_2);
				--imageShow;
				if (imageShow < 0) {
					imageShow = pageCount - 1;
				}
				pageGuide.getChildAt(imageShow).setBackgroundResource(
						R.drawable.yema1_1);
			}
		}
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.nearby_Back:
			mPopupWindow.dismiss();
			break;
		case R.id.nearby_facList_Back:
			neary_vf.showPrevious();
			break;

		default:
			break;
		}
	}
	public void showFacList(List<FeatureSet> result,List<Graphic> graphicList){
		nearby_fac_list.setAdapter(new FacListAdapter(context, graphicList,result,this));
		neary_vf.showNext();
	}
	public void locFac(Graphic graphic){
		BufferQueryTools nbqTools = new BufferQueryTools(context,mapView);
		
		nbqTools.drawResult2(graphic);
		mPopupWindow.dismiss();
	}
	public void showFacDetails(String names,String values){
		ScrollView sv = new ScrollView(context);
		sv.addView(dynamicAddView2(names, values));
		Dialog dg=new Dialog(context);
		
		dg.addContentView(sv, new LinearLayout.LayoutParams( 
				10000, ViewGroup.LayoutParams.MATCH_PARENT));
		dg.setCancelable(true);
		dg.setCanceledOnTouchOutside(true);
		dg.show();
	}
	public void facArrive(Geometry geo){
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
				String position = ArcgisToBd09.toBd09Position( AppContext.getInstance().getCoordTransform(),mapView.getSpatialReference(),point.getX(), point.getY());
				double x = Double.valueOf(position.split(" ")[0]);
				double y = Double.valueOf(position.split(" ")[1]);
				LatLng latLng=new LatLng(y,x);
				latLngs.add(latLng);
			}
			BaiduAppProxy.CallBaiduNavigationLatLng(latLngs.get(0),latLngs.get(1), AmapNaviType.DRIVER,context);
//
//			BaiduAppProxy.navigatorViaPoints((Activity)context, AppContext.getInstance().getCoordTransform(), mapView.getSpatialReference(), arcPoints);
		}
	}
	/**
	 * 用于动态创建并显示消息推送下来新任务的字段
	 */
	public View dynamicAddView2(String names,String values){
		//拼接任务详细内容
		String []nameList=names.split(",");
		String []valueList=values.split(",");
//		LinearLayout.LayoutParams paramsKey = new LinearLayout.LayoutParams( 
//				ViewGroup.LayoutParams.WRAP_CONTENT, 
//				ViewGroup.LayoutParams.WRAP_CONTENT); 
//
		LinearLayout.LayoutParams paramsImage = new LinearLayout.LayoutParams( 
				10, ViewGroup.LayoutParams.MATCH_PARENT); 
		
		LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 
		
		LinearLayout.LayoutParams paramsPLayout = new LinearLayout.LayoutParams( 
				10000, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 

		LinearLayout pushPLayout = new LinearLayout(context);
		pushPLayout.setOrientation(LinearLayout.VERTICAL);
		pushPLayout.setLayoutParams(paramsPLayout);
		pushPLayout.setGravity(Gravity.CENTER_VERTICAL);

		for(int i = 0 ;i < nameList.length; i++){
			String value = valueList[i];
			
			TextView tvKey = new TextView(context);
			tvKey.setTextColor(Color.BLACK);
			tvKey.setGravity(Gravity.CENTER_VERTICAL);
			tvKey.setText(nameList[i]);
			tvKey.setEms(4);

			TextView tvValue = new TextView(context);
			tvValue.setTextColor(Color.BLACK);
			tvValue.setPadding(5, 0, 0, 0);
			tvValue.setGravity(Gravity.CENTER_VERTICAL);
			tvValue.setText(value);
			
			TextView tvImage = new TextView(context);
			tvImage.setLayoutParams(paramsImage);
			tvImage.setBackgroundResource(R.drawable.task_fg);

			LinearLayout cLayout = new LinearLayout(context);
			cLayout.setBackgroundResource(R.drawable.input_bg);
			cLayout.setGravity(Gravity.CENTER_VERTICAL);
			cLayout.setLayoutParams(paramsLayout);
			cLayout.setPadding(10, 5, 5, 5);
			cLayout.addView(tvKey);
			cLayout.addView(tvImage);
			cLayout.addView(tvValue);
			pushPLayout.addView(cLayout);
		}
		return pushPLayout;
	}

}
