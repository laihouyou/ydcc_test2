package com.movementinsome.map.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polyline;
import com.esri.core.tasks.identify.IdentifyResult;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.app.pub.view.CreateDynamicView;
import com.movementinsome.map.utils.BufferQueryTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdentifyView extends RelativeLayout{

	private Context context;
	private ArrayList<IdentifyData> identifyDatas;
	private ArrayList<Map<String, Object>> identifyDatass;
	private Button idfbtnFin;
	private TextView tvChaxun;
	private ListView dentListview;
	private DenlistAdapter densAdapter;
	private BufferQueryTools nbqTools;
	private MyMapView view;
	private LinearLayout idfLinearFacList;
	private LinearLayout idfLinearTitless;
	private ScrollView idfRlFacMsg;
	private ViewFlipper idfViewFlipperFac;
	
	private LinearLayout idfLinearFacListTitle;
	private ImageView idfViewImgDaoZheLi;
	
	// 触摸状态
	private final int DONE = 0;
	private final int ACTION_DOWN = 1;
	private final int ACTION_MOVE = 2;
	private final int ACTION_UP = 3;
	// 页面切换
	private final int SHOW_NEXT = 4;
	// 显示设施
	private final int SHOW_FAC = 5;
	// 初始显示高度
	private int showH;
	// 底部标题高度
	private int bottomTitleH; 
	// 屏幕高度
	private int screenHeight;
	private MoveViewOnTouchListener moveViewOnTouchListener;
	private TranslateAnimation titlessAnimation1;
	private TranslateAnimation titlessAnimation2;
	// 是否显示
	private boolean isShowTitle = true;
	private float identifyX;
	private float identifyY;
	private Point identifyPoint;
	private boolean isShowNext = false;
	private List<Geometry>geometryListP;
	private List<Geometry>geometryListL;
	private boolean isShowImg = true;
	private ScaleAnimation scaleAnimation1;
	private ScaleAnimation scaleAnimation2;
	private int show_img_h;
	private Button identifyDaoLayoutButton;
	
	public IdentifyView(Context context,MyMapView view,ArrayList<IdentifyData> identifyDatas,float identifyX,float identifyY) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.identifyDatas = identifyDatas;
		this.view = view;
		this.identifyX = identifyX;
		this.identifyY = identifyY;
		identifyPoint = view.toMapPoint(identifyX, identifyY);
		
		View v=View.inflate(context, R.layout.identify_view, null);
		addView(v);
		
		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		screenHeight = dm.heightPixels;
		showH = screenHeight * 1 / 3;
		bottomTitleH = DensityUtil.dip2px(context, 45);
		
		moveViewOnTouchListener = new MoveViewOnTouchListener(myHandler);
		moveViewOnTouchListener.setShowH(showH);
		titlessAnimation1 = new TranslateAnimation(0, 0, 0, -90);
		titlessAnimation1.setDuration(400);
		titlessAnimation1.setFillAfter(true);
		titlessAnimation2 = new TranslateAnimation(0, 0, -90, 0);
		titlessAnimation2.setDuration(400);
		titlessAnimation2.setFillAfter(true);
		
		scaleAnimation1 = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				1.0f);
		scaleAnimation1.setDuration(200);
		scaleAnimation1.setFillAfter(true);
		scaleAnimation2 = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				1.0f);
		scaleAnimation2.setDuration(200);
		scaleAnimation2.setFillAfter(true);
		nbqTools = new BufferQueryTools(context,view);
		idfbtnFin = (Button)findViewById(R.id.idfbtnFin);
		idfbtnFin.setOnClickListener(myMainBtnClickListener);
		tvChaxun = (TextView)findViewById(R.id.tvChaxun);
		dentListview = (ListView)findViewById(R.id.dentListview);
		idfLinearFacList = (LinearLayout) findViewById(R.id.idfLinearFacList);
		idfLinearTitless = (LinearLayout) findViewById(R.id.idfLinearTitless);
		idfRlFacMsg = (ScrollView) findViewById(R.id.idfRlFacMsg);
		idfViewFlipperFac = (ViewFlipper) findViewById(R.id.idfViewFlipperFac);
		idfLinearFacListTitle = (LinearLayout) findViewById(R.id.idfLinearFacListTitle);
		idfViewImgDaoZheLi = (ImageView) findViewById(R.id.idfViewImgDaoZheLi);
		identifyDaoLayoutButton = (Button) findViewById(R.id.identifyDaoLayoutButton);
		idfViewImgDaoZheLi.setVisibility(View.GONE);
		
		identifyDatass = new ArrayList<Map<String,Object>>();
		geometryListP = new ArrayList<Geometry>();
		geometryListL = new ArrayList<Geometry>();
		for(IdentifyData identifyData :identifyDatas ){
			ArrayList<IdentifyResult> irs=identifyData.getData();
			for(IdentifyResult identifyResult1:irs){
				Map<String,Object> m = new HashMap<String, Object>();
				m.put("IdentifyResult", identifyResult1);
				m.put("LayerName", identifyData.getLayerName());
				m.put("FtLayer", identifyData.getFtLayer());
				m.put("LayerId", identifyData.getLayerId());
				m.put("ServiceId", identifyData.getServiceId());
				identifyDatass.add(m);
				Geometry geo=identifyResult1.getGeometry();
				if (geo instanceof Point) {
					geometryListP.add(geo);
				} else if (geo instanceof Polyline) {
					geometryListL.add(geo);
				}
			}
		}
		myHandler.sendEmptyMessage(SHOW_FAC);
		int n=identifyDatass.size();
		tvChaxun.setText("搜索到“"+n+"”条结果");
		updataListview();
		myHandler.sendEmptyMessage(DONE);
	}

	private void updataListview() {
		// TODO Auto-generated method stub
		densAdapter = new DenlistAdapter((Activity)this.context, R.layout.identifys_list_item, identifyDatass,myHandler,moveViewOnTouchListener,identifyDaoLayoutButton);
		dentListview.setAdapter(densAdapter);
	}
	
	android.view.View.OnClickListener myMainBtnClickListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.idfbtnFin:
					if(isShowNext){
						idfViewFlipperFac.showPrevious();
						isShowNext = false;
						myHandler.sendEmptyMessage(SHOW_FAC);
						idfLinearFacListTitle.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,DensityUtil.dip2px(context, 45)));
						idfViewImgDaoZheLi.setVisibility(View.GONE);
						bottomTitleH = DensityUtil.dip2px(context, 45);
						myHandler.sendEmptyMessage(DONE);
					}else{
						view.removeView(IdentifyView.this);
						nbqTools.quit();
					}
					break;
				default:
					break;
			}
		}
		
	};
	private Point identifyPointM;
	private Handler myHandler = new Handler() {

		@SuppressLint("NewApi")
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case DONE:
				idfLinearFacList.setY(showH);
				view.centerAt(identifyPoint, false);
				identifyPointM = view.toMapPoint(view.getWidth()/2, view.getHeight()-showH/2);
				view.centerAt(identifyPointM, false);
				show_img_h = DensityUtil.dip2px(context, 30);
				if(isShowNext){
					idfViewImgDaoZheLi.setY(showH-show_img_h);
				}
				moveViewOnTouchListener
					.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_MIDDLE);
				break;
			case ACTION_DOWN:
				moveViewOnTouchListener.setViewY((int) idfLinearFacList.getY());
				break;
			case ACTION_MOVE:
				float moveY = msg.getData().getFloat("moveY");
				idfLinearFacList.setY(moveY);
				if(isShowNext){
					idfViewImgDaoZheLi.setY(moveY - show_img_h);
					if (moveY < 160 && isShowImg) {
						idfViewImgDaoZheLi.startAnimation(scaleAnimation1);
						isShowImg = false;
					}
					if (moveY > 160 && !isShowImg) {
						idfViewImgDaoZheLi.startAnimation(scaleAnimation2);
						isShowImg = true;
					}
				}
				if (moveY < 160 && isShowTitle) {
					idfLinearTitless.startAnimation(titlessAnimation1);
					isShowTitle = false;
				}
				break;
			case ACTION_UP:
				float y2 = idfLinearFacList.getY();
				int h =getHeight();
				if (y2 > 160 && !isShowTitle) {
					idfLinearTitless.startAnimation(titlessAnimation2);
					isShowTitle = true;
				}
				if (y2 <= 160) {
					/*my_list.setFristMove(false);
					my_list.setMoveDown(false);*/
					idfLinearFacList.setY(0);
					moveViewOnTouchListener
							.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_TOP);
				} else if(y2>=(h-240)){
					view.centerAt(identifyPoint, false);
					idfLinearFacList.setY(h-bottomTitleH);
					if(isShowNext){
						idfViewImgDaoZheLi.setY(h-bottomTitleH-show_img_h);
					}
					moveViewOnTouchListener.setBottomH(h-bottomTitleH);
					moveViewOnTouchListener
							.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_BOTTOM);
				}else {
					idfLinearFacList.setY(showH);
					if(isShowNext){
						idfViewImgDaoZheLi.setY(showH-show_img_h);
					}
					view.centerAt(identifyPoint, false);
					view.centerAt(identifyPointM, false);
					moveViewOnTouchListener
							.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_MIDDLE);
				}
				break;
			case SHOW_NEXT:
				Bundle bundle = msg.getData();
				String names = bundle.getString("names");
				String values = bundle.getString("values");
				IdentifyResult identifyResult = (IdentifyResult) bundle.getSerializable("identifyResult");
				idfViewFlipperFac.showNext();
				isShowNext = true;
				idfRlFacMsg.removeAllViews();
				idfRlFacMsg.addView(new CreateDynamicView(context).dynamicAddView2(names, values));
				nbqTools.clear();
				nbqTools.drawResult3(identifyResult.getGeometry());
				idfLinearFacListTitle.setLayoutParams( new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,DensityUtil.dip2px(context, 60)));
				idfViewImgDaoZheLi.setVisibility(View.VISIBLE);
				bottomTitleH = DensityUtil.dip2px(context, 60);
				break;
			case SHOW_FAC:
				nbqTools.clear();
				for(Geometry geo:geometryListL){
					nbqTools.drawResult3(geo);
				}
				for(Geometry geo:geometryListP){
					nbqTools.drawResult3(geo);
				}
				break;
			default:
				break;
			}
		}

	};
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return moveViewOnTouchListener.onTouchEvent(event);
	}
}
