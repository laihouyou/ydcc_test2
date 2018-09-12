package com.movementinsome.map.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AmapNaviType;
import com.baidu.BaiduAppProxy;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.identify.IdentifyResult;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.activity.FormsActivity;
import com.movementinsome.app.pub.activity.WebCheckPhotoActivity;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.app.pub.view.CreateDynamicView;
import com.movementinsome.caice.util.ArcgisToBd09;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.map.utils.BufferQueryTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class IdentifyView2 extends RelativeLayout {

	private Context context;
	private ArrayList<IdentifyData> identifyDatas;
	private ArrayList<Map<String, Object>> identifyDataGs;
	private ArrayList<Map<String, Object>> identifyDatass;
	private Button idfbtnFin;
	private TextView tvChaxun;
	private IdentifyFacListView dentListview;
	private DenlistAdapter densAdapter;
	private AttributelistAdapter attibuteApapter;
	private BufferQueryTools nbqTools;
	private MyMapView view;
	private LinearLayout idfLinearFacList;
	private LinearLayout idfLinearTitless;
	private IdentifyFacMsgScrollView idfRlFacMsg;
	private ViewFlipper idfViewFlipperFac;
	private TextView tvForms; // 填单
	private TextView tvImage; // 图片

	private LinearLayout idfLinearFacListTitle;
	private ImageView idfViewImgDaoZheLi;

	private RelativeLayout identifyHeadLayout;
	private TextView identifyHeadLayoutText;
	private TextView identifyListHeadText;

	private Button identifyHeadLayoutButton;
	private Button identifyDaoLayoutButton;

	private IdentifyResult identifyResult;

	private Graphic graphic;

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
	private List<Geometry> geometryListP;
	private List<Geometry> geometryListL;
	private boolean isShowImg = true;
	private ScaleAnimation scaleAnimation1;
	private ScaleAnimation scaleAnimation2;
	private int show_img_h;
	int n;
	int screenWidths;
	private String keys;
	private String value2;

	LinearLayout identify_list_head;
	private boolean isShow;

	public IdentifyView2(Context context, MyMapView view,
			ArrayList<IdentifyData> identifyDatas, float identifyX,
			float identifyY) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.identifyDatas = identifyDatas;
		this.view = view;
		this.identifyX = identifyX;
		this.identifyY = identifyY;
		isShow = true;

		init();

		for (IdentifyData identifyData : identifyDatas) {
			ArrayList<IdentifyResult> irs = identifyData.getData();
			for (IdentifyResult identifyResult1 : irs) {
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("IdentifyResult", identifyResult1);
				m.put("LayerName", identifyData.getLayerName());
				m.put("FtLayer", identifyData.getFtLayer());
				m.put("LayerId", identifyData.getLayerId());
				m.put("ServiceId", identifyData.getServiceId());
				identifyDatass.add(m);
				Geometry geo = identifyResult1.getGeometry();
				if (geo instanceof Point) {
					geometryListP.add(geo);
				} else if (geo instanceof Polyline) {
					geometryListL.add(geo);
				}
			}
		}
		myHandler.sendEmptyMessage(SHOW_FAC);
		n = identifyDatass.size();
		identifyHeadLayoutText.setText("搜索到“" + n + "”条结果");
		identifyListHeadText.setText("搜索到“" + n + "”条结果");
		updataListview();
		myHandler.sendEmptyMessage(DONE);
	}

	public IdentifyView2(Context context, MyMapView view,
			ArrayList<Map<String, Object>> identifyDataGs) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.identifyDataGs = identifyDataGs;
		this.view = view;

		init();

		myHandler.sendEmptyMessage(SHOW_FAC);
		n = identifyDataGs.size();
		identifyHeadLayoutText.setText("搜索到“" + n + "”条结果");
		identifyListHeadText.setText("搜索到“" + n + "”条结果");
		upbuteListview();
		myHandler.sendEmptyMessage(DONE);
	}

	private void init() {
		// TODO Auto-generated method stub
		view.getMap_search_layout().setVisibility(View.GONE);
		identifyPoint = view.toMapPoint(identifyX, identifyY);

		View v = View.inflate(context, R.layout.identify_view2, null);
		addView(v);

		DisplayMetrics dm = new DisplayMetrics();
		// 获取屏幕信息
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);
		screenHeight = dm.heightPixels;
		showH = screenHeight * 1 / 3;
		bottomTitleH = DensityUtil.dip2px(context, 45);

		int screenWidth = dm.widthPixels;
		screenWidths = screenWidth / 2 - 1;

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
		nbqTools = new BufferQueryTools(context, view);
		idfbtnFin = (Button) findViewById(R.id.idfbtnFin);
		idfbtnFin.setOnClickListener(myMainBtnClickListener);
		tvChaxun = (TextView) findViewById(R.id.tvChaxun);
		identifyHeadLayout = (RelativeLayout) findViewById(R.id.identifyHeadLayout);
		identifyHeadLayoutText = (TextView) findViewById(R.id.identifyHeadLayoutText);
		identifyHeadLayoutButton = (Button) findViewById(R.id.identifyHeadLayoutButton);
		identifyDaoLayoutButton = (Button) findViewById(R.id.identifyDaoLayoutButton);
		identifyHeadLayoutButton.setOnClickListener(myMainBtnClickListener);
		identifyDaoLayoutButton.setOnClickListener(myMainBtnClickListener);
		tvForms = (TextView) findViewById(R.id.tvForms);
		tvForms.setOnClickListener(myMainBtnClickListener);
		tvImage = (TextView) findViewById(R.id.tvImage);
		tvImage.setOnClickListener(myMainBtnClickListener);
		tvForms.setWidth(screenWidths);
		tvImage.setWidth(screenWidths);

		identify_list_head = (LinearLayout) View.inflate(context,
				R.layout.identify_list_head, null);
		identifyListHeadText = (TextView) identify_list_head
				.findViewById(R.id.identifyListHeadText);
		identifyListHeadText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		dentListview = (IdentifyFacListView) findViewById(R.id.dentListview);
		dentListview.setMyHandler(myHandler);
		dentListview.setMoveViewOnTouchListener(moveViewOnTouchListener);
		dentListview.setOnScrollListener(dentListview);
		dentListview.addHeaderView(identify_list_head);

		idfLinearFacList = (LinearLayout) findViewById(R.id.idfLinearFacList);
		idfLinearTitless = (LinearLayout) findViewById(R.id.idfLinearTitless);
		idfRlFacMsg = (IdentifyFacMsgScrollView) findViewById(R.id.idfRlFacMsg);
		idfViewFlipperFac = (ViewFlipper) findViewById(R.id.idfViewFlipperFac);
		idfLinearFacListTitle = (LinearLayout) findViewById(R.id.idfLinearFacListTitle);
		idfViewImgDaoZheLi = (ImageView) findViewById(R.id.idfViewImgDaoZheLi);
		idfViewImgDaoZheLi.setOnClickListener(myMainBtnClickListener);
		idfViewImgDaoZheLi.setVisibility(View.GONE);

		identifyDatass = new ArrayList<Map<String, Object>>();
		geometryListP = new ArrayList<Geometry>();
		geometryListL = new ArrayList<Geometry>();
	}

	private void updataListview() {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("itemH", screenHeight);
		identifyDatass.add(m);
		densAdapter = new DenlistAdapter((Activity) this.context,
				R.layout.identifys_list_item, identifyDatass, myHandler,
				moveViewOnTouchListener, identifyDaoLayoutButton);
		dentListview.setAdapter(densAdapter);
	}

	private void upbuteListview() {
		// TODO Auto-generated method stub
		Map<String, Object> m = new HashMap<String, Object>();
		identifyDataGs.add(m);
		attibuteApapter = new AttributelistAdapter((Activity) this.context,
				R.layout.identifys_list_item, identifyDataGs, myHandler,
				moveViewOnTouchListener, identifyDaoLayoutButton, screenHeight);
		dentListview.setAdapter(attibuteApapter);
	}

	android.view.View.OnClickListener myMainBtnClickListener = new android.view.View.OnClickListener() {

		@SuppressLint("NewApi")
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.idfbtnFin:
				if (isShowNext) {

					isShowNext = false;
					myHandler.sendEmptyMessage(SHOW_FAC);
					identifyHeadLayoutText.setText("搜索到“" + n + "”条结果");
					identifyHeadLayout
							.setLayoutParams(new RelativeLayout.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT,
									DensityUtil.dip2px(context, 45)));
					identifyDaoLayoutButton.setVisibility(View.GONE);
					bottomTitleH = DensityUtil.dip2px(context, 45);
					idfViewImgDaoZheLi.startAnimation(scaleAnimation1);
					if (dentListview.getDisplayStatus() == MoveViewOnTouchListener.DISPLAY_MIDDLE) {
						identifyHeadLayout.setVisibility(View.GONE);
						myHandler.sendEmptyMessage(DONE);
					} else {
						identifyHeadLayout.setVisibility(View.VISIBLE);
						idfLinearTitless.startAnimation(titlessAnimation2);
						idfLinearFacList.setY(0);
						moveViewOnTouchListener
								.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_TOP);
					}
					idfViewFlipperFac.showPrevious();
				} else {
					isShow = false;
					view.getMap_search_layout().setVisibility(View.VISIBLE);
					view.removeView(IdentifyView2.this);
					nbqTools.quit();
				}
				break;

			case R.id.identifyHeadLayoutButton:
				if (isShowNext) {
					idfViewFlipperFac.showPrevious();
					isShowNext = false;
					myHandler.sendEmptyMessage(SHOW_FAC);
					identifyHeadLayoutText.setText("搜索到“" + n + "”条结果");
					identifyHeadLayout
							.setLayoutParams(new RelativeLayout.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT,
									DensityUtil.dip2px(context, 45)));
					// identifyHeadLayout.setVisibility(View.GONE);
					identifyDaoLayoutButton.setVisibility(View.GONE);
					bottomTitleH = DensityUtil.dip2px(context, 45);
					idfViewImgDaoZheLi.startAnimation(scaleAnimation1);
					if (dentListview.getDisplayStatus() == MoveViewOnTouchListener.DISPLAY_MIDDLE) {
						identifyHeadLayout.setVisibility(View.GONE);
						myHandler.sendEmptyMessage(DONE);
					} else {
						identifyHeadLayout.setVisibility(View.VISIBLE);
						idfLinearTitless.startAnimation(titlessAnimation2);
						idfLinearFacList.setY(0);
						moveViewOnTouchListener
								.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_TOP);
					}
				} else {
					isShow = false;
					view.getMap_search_layout().setVisibility(View.VISIBLE);
					view.removeView(IdentifyView2.this);
					nbqTools.quit();
				}
				break;
			case R.id.identifyDaoLayoutButton:
				Geometry geo = null;
				// = identifyResult.getGeometry()
				if (identifyResult != null) {
					geo = identifyResult.getGeometry();
				}
				if (graphic != null) {
					geo = graphic.getGeometry();
				}
				if (geo != null) {
					List<Point> arcPoints = new ArrayList();
					if (AppContext.getInstance().getCurLocation() != null) {

						arcPoints.add(new Point(AppContext.getInstance()
								.getCurLocation().getMapx(), AppContext
								.getInstance().getCurLocation().getMapy()));

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
							String position = ArcgisToBd09.toBd09Position( AppContext.getInstance().getCoordTransform(),view.getSpatialReference(),point.getX(), point.getY());
							double x = Double.valueOf(position.split(" ")[0]);
							double y = Double.valueOf(position.split(" ")[1]);
							LatLng latLng=new LatLng(y,x);
							latLngs.add(latLng);
						}
						BaiduAppProxy.CallBaiduNavigationLatLng(latLngs.get(0),latLngs.get(1), AmapNaviType.DRIVER,context);
//						BaiduAppProxy.navigatorViaPoints((Activity) context,
//								AppContext.getInstance().getCoordTransform(),
//								view.getSpatialReference(), arcPoints);
					}
				}
				break;
			case R.id.idfViewImgDaoZheLi:
				Geometry geos = null;
				if (identifyResult != null) {
					geos = identifyResult.getGeometry();
				}
				if (graphic != null) {
					geos = graphic.getGeometry();
				}
				if (geos != null) {
					List<Point> arcPointss = new ArrayList();
					if (AppContext.getInstance().getCurLocation() != null) {

						arcPointss.add(new Point(AppContext.getInstance()
								.getCurLocation().getMapx(), AppContext
								.getInstance().getCurLocation().getMapy()));

						if (geos instanceof Point) {
							Point point = (Point) geos;
							arcPointss.add(point);
						} else if (geos instanceof Polyline) {
							Polyline pline = (Polyline) geos;

							arcPointss.add(pline.getPoint(0));
						} else if (geos instanceof Polygon) {
							// Polygon pgon = (Polygon)
							// mMapIden.getGeometry();
							Envelope env = new Envelope();
							geos.queryEnvelope(env);
							arcPointss.add(env.getCenter());
						}
						List<LatLng> latLngs=new ArrayList<>();
						latLngs.removeAll(latLngs);
						for (Point point :arcPointss){
							String position = ArcgisToBd09.toBd09Position( AppContext.getInstance().getCoordTransform(),view.getSpatialReference(),point.getX(), point.getY());
							double x = Double.valueOf(position.split(" ")[0]);
							double y = Double.valueOf(position.split(" ")[1]);
							LatLng latLng=new LatLng(y,x);
							latLngs.add(latLng);
						}
						BaiduAppProxy.CallBaiduNavigationLatLng(latLngs.get(0),latLngs.get(1),AmapNaviType.DRIVER,context);
//						BaiduAppProxy.navigatorViaPoints((Activity) context,
//								AppContext.getInstance().getCoordTransform(),
//								view.getSpatialReference(), arcPointss);
					}
				}
				break;
			case R.id.tvForms:
				Intent intent = new Intent(context, FormsActivity.class);
				if (context instanceof ContainActivity) {
					intent.putExtra("mid", ((ContainActivity) context)
							.getMenu().getId());
				}
				intent.putExtra("keys", keys);
				intent.putExtra("value2", value2);
				context.startActivity(intent);
				break;
			case R.id.tvImage:
				String gid = "";
				if (identifyResult != null) {
					gid = identifyResult.getAttributes().get("GID") + "";
				} else if (graphic != null) {
					gid = graphic.getAttributes().get("GID") + "";
				}
				if ("null".equals(gid) && "".equals(gid)) {
					if (identifyResult != null) {
						gid = identifyResult.getAttributes().get("业务唯一标识码")
								+ "";
					} else if (graphic != null) {
						gid = graphic.getAttributes().get("业务唯一标识码") + "";
					}
				}
				Intent intent2 = new Intent();
				String serverUrl = AppContext.getInstance().getFileServerUrl()
						+ "/img.jsp?findKey=" + gid;
				intent2.putExtra("serverUrl", serverUrl);
				intent2.setClass(context, WebCheckPhotoActivity.class);
				context.startActivity(intent2);
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
			case DONE:// 初始化加载
				view.centerAt(identifyPoint, false);
				identifyPointM = view.toMapPoint(view.getWidth() / 2,
						view.getHeight() - showH / 2);
				view.centerAt(identifyPointM, false);
				show_img_h = DensityUtil.dip2px(context, 30);
				if (isShowNext) {
					idfLinearFacList.setY(showH);
					idfViewImgDaoZheLi.setY(showH - show_img_h);
				} else {
					idfLinearFacList.setY(0);
					identify_list_head.setPadding(0, showH, 0, 0);
				}
				moveViewOnTouchListener
						.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_MIDDLE);
				break;
			case ACTION_DOWN:// 点下显示页面的时候
				if (isShowNext) {
					moveViewOnTouchListener.setViewY((int) idfLinearFacList
							.getY());
				} else {
					moveViewOnTouchListener.setViewY(identify_list_head
							.getPaddingTop());
				}
				break;
			case ACTION_MOVE:// 移动的时候
				float moveY = msg.getData().getFloat("moveY");
				if (identifyHeadLayout.isShown()) {
					if (isShowNext) {
						if (idfLinearFacList.getY() > 0) {
							identifyHeadLayout.setVisibility(View.GONE);
						}
					} else {
						if (identify_list_head.getPaddingTop() > 0) {
							identifyHeadLayout.setVisibility(View.GONE);
						}
					}
				}
				if (isShowNext) {
					idfLinearFacList.setY(moveY);
					idfViewImgDaoZheLi.setY(moveY - show_img_h);
					if (moveY < 160 && isShowImg) {
						idfViewImgDaoZheLi.startAnimation(scaleAnimation1);
						isShowImg = false;
					}
					if (moveY > 160 && !isShowImg) {
						idfViewImgDaoZheLi.startAnimation(scaleAnimation2);
						isShowImg = true;
					}
				} else {
					identify_list_head.setPadding(0, (int) moveY, 0, 0);
				}
				if (moveY < 160 && isShowTitle) {
					idfLinearTitless.startAnimation(titlessAnimation1);
					isShowTitle = false;
				}
				break;
			case ACTION_UP:// 手指离开屏幕的时候
				float y2 = 0;
				if (isShowNext) {
					y2 = idfLinearFacList.getY();
				} else {
					y2 = identify_list_head.getPaddingTop();
				}
				int h = getHeight();
				if (y2 > 160 && !isShowTitle) {
					idfLinearTitless.startAnimation(titlessAnimation2);
					isShowTitle = true;
				}
				if (y2 <= 160) {
					identifyHeadLayout.setVisibility(View.VISIBLE);
					if (isShowNext) {
						idfLinearFacList.setY(0);
					} else {
						identify_list_head.setPadding(0, 0, 0, 0);
					}
					moveViewOnTouchListener
							.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_TOP);
				} else if (y2 >= (h - 240)) {
					view.centerAt(identifyPoint, false);
					if (isShowNext) {
						idfLinearFacList.setY(h - bottomTitleH);
						idfViewImgDaoZheLi.setY(h - bottomTitleH - show_img_h);
					} else {
						identify_list_head
								.setPadding(0, h - bottomTitleH, 0, 0);
					}
					moveViewOnTouchListener.setBottomH(h - bottomTitleH);
					moveViewOnTouchListener
							.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_BOTTOM);
				} else {
					if (isShowNext) {
						idfLinearFacList.setY(showH);
						idfViewImgDaoZheLi.setY(showH - show_img_h);
					} else {
						identify_list_head.setPadding(0, showH, 0, 0);
					}
					view.centerAt(identifyPoint, false);
					view.centerAt(identifyPointM, false);
					moveViewOnTouchListener
							.setDisplayStatus(MoveViewOnTouchListener.DISPLAY_MIDDLE);
				}

				break;
			case SHOW_NEXT:// 查看详细信息的时候
				Bundle bundle = msg.getData();
				String names = bundle.getString("names");
				String values = bundle.getString("values");
				keys = bundle.getString("keys");
				value2 = bundle.getString("value2");
				identifyResult = (IdentifyResult) bundle
						.getSerializable("identifyResult");

				graphic = (Graphic) bundle.getSerializable("graphic");
				idfViewFlipperFac.showNext();
				dentListview.setDisplayStatus(moveViewOnTouchListener
						.getDisplayStatus());
				isShowNext = true;
				idfRlFacMsg.removeAllViews();
				idfRlFacMsg.addView(new CreateDynamicView(context)
						.dynamicAddView2(names, values));
				nbqTools.clear();
				if (identifyResult != null) {
					nbqTools.drawResult3(identifyResult.getGeometry());
				} else if (graphic != null) {
					nbqTools.drawResult3(graphic.getGeometry());
					Geometry geometry = graphic.getGeometry();
					if (geometry instanceof Point) {
						identifyPoint = (Point) geometry;
					} else if (geometry instanceof Polyline) {
						Polyline pline = (Polyline) geometry;
						if (pline.getPointCount() > 0)
							identifyPoint = pline.getPoint(0);
					} else if (geometry instanceof Polygon) {
						Envelope env = new Envelope();
						geometry.queryEnvelope(env);
						identifyPoint = env.getCenter();
					}
				}
				if (moveViewOnTouchListener.getDisplayStatus() == MoveViewOnTouchListener.DISPLAY_TOP) {
					idfLinearTitless.startAnimation(titlessAnimation2);
				}
				String layerName = bundle.getString("layerName");
				tvChaxun.setText(layerName);
				identifyHeadLayoutText.setText(layerName);
				identifyHeadLayout
						.setLayoutParams(new RelativeLayout.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								DensityUtil.dip2px(context, 60)));
				identifyHeadLayout.setVisibility(View.GONE);

				idfViewImgDaoZheLi.setVisibility(View.VISIBLE);
				idfViewImgDaoZheLi.startAnimation(scaleAnimation2);
				bottomTitleH = DensityUtil.dip2px(context, 60);
				break;
			case SHOW_FAC:// 查看设施
				nbqTools.clear();
				for (Geometry geo : geometryListL) {
					nbqTools.drawResult3(geo);
				}
				for (Geometry geo : geometryListP) {
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
		if (isShowNext) {
			return moveViewOnTouchListener.onTouchEvent(event);
		} else {
			return super.onTouchEvent(event);
		}

	}

	public void clear() {
		nbqTools.quit();
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

}
