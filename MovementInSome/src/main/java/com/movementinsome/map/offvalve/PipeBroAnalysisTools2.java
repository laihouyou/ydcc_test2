package com.movementinsome.map.offvalve;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.Symbol;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.map.offvalve.asynctask.PipeBroAnalysis2;
import com.movementinsome.map.offvalve.asynctask.PipeBrokenAnalysisUpload2;
import com.movementinsome.map.utils.BigDecimalUtil;
import com.movementinsome.map.view.MyMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PipeBroAnalysisTools2 {

	private Context context;

	private MyMapView map;
	private GraphicsLayer grapLayer;			//查询到的设施渲染
	private String analysisType="-1";//修正类型
	private Point analysispoint=null;// 分析点
	private String activityName="analysis";
	private String pipeBreakenData = "";		//爆管分析数据传递
	private String mustBeClosed ="";
	public static final String  LINE="true";
	public static final String FAC="false";
	
	private List<String> bound;
	private List<Map<String, String>> data;
	private String result;
	private List<Module> taskModule;
	
	private InsTablePushTaskVo insTablePushTaskVo;
	
	private LinearLayout mapLinearPipeBro;// 爆管分析容器
	private Button pipeBroMapClear;// 清除
	private Button pipeBroMapConfirm;// 确定
	private RelativeLayout pipe_bro_list_ll;
	private Button pipe_bro_list_pack_up;
	private TextView pipe_bro_enlarge_count;
	
	private List<String> badSwitchs;// 损害阀门
	private boolean[] checks; //用于保存checkBox的选择状态  

	public PipeBroAnalysisTools2(final Context context, MyMapView map, String value) {
		this.context = context;
		this.map = map;

		badSwitchs = new ArrayList<String>();
		grapLayer = new GraphicsLayer();
		map.addLayer(grapLayer);
		map.setAllowRotationByPinch(true);
		map.setOnTouchListener(new MyTouchListener(context,map.getMapView()));
		
		mapLinearPipeBro = map.getMapLinearPipeBro();// 爆管分析容器
		mapLinearPipeBro.setVisibility(View.VISIBLE);
		map.getMap_search_layout().setVisibility(View.GONE);
		pipeBroMapClear = map.getPipeBroMapClear();// 清除
		pipeBroMapConfirm = map.getPipeBroMapConfirm();// 确定
		map.getFooterView().setVisibility(View.GONE);
		if(value!=null&&!"".equals(value)){
			PipeBroAnalysis2 analysisTask = new PipeBroAnalysis2(this, true, null,value);
			analysisTask.execute("SHOW");
		}
		pipeBroMapConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(result!=null){
					AppContext.getInstance().setPipeBroAnalysisResult(result);
					AppContext.getInstance().getmHandle().sendEmptyMessage(1);
					((Activity) context).finish();
				}else{
					Toast.makeText(context, "请爆管分析后，才确定", Toast.LENGTH_LONG).show();
				}
			}
		});
		pipeBroMapClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clear();
			}
		});
	}
	
	class MyTouchListener extends MapOnTouchListener {

		public MyTouchListener(Context context, MapView view) {
			super(context, view);
			// TODO Auto-generated constructor stub
		}
		@Override
		public boolean onSingleTap(MotionEvent point) {
			// TODO Auto-generated method stub
			if("analysis".equals(activityName)){//是否爆管分析
				analysisGraphicPoint(point.getX(), point.getY(), "0");
			}else if("analysisFind".equals(activityName)){//是否选择爆管分析的点
				map.getCallout().hide();
				showFindPoint(point.getX(), point.getY());
			}
			return super.onSingleTap(point);
		}
	
	}
	public void clear() {
		activityName="analysis";
		grapLayer.removeAll();
		map.getCallout().hide();
		map.getHeaderContainer().removeAllViews();
	}

	public void stop() {
		
	}

	public void exit() {
		grapLayer.removeAll();
		map.removeLayer(grapLayer);
		map.resetTouchListener();
		map.getCallout().hide();
		map.getHeaderContainer().removeAllViews();
	}
	/*public void show(){
		PipeBroAnalysis analysisTask = new PipeBroAnalysis(PipeBroAnalysisTools.this, false);
		analysisTask.execute("SHOW");
	}*/
	/**
	 * 爆管分析点的渲染（修正点，最后确定点）
	 * @param x
	 * @param y
	 * @param isTrue:修正点0，还是最后确定点1
	 */
	@SuppressWarnings("static-access")
	public void analysisGraphicPoint(final float x, final float y,final String isTrue){
		LinearLayout ll =new LinearLayout(context);
		
		TextView analysisFac= new TextView(context);
		TextView analysisPipe= new TextView(context);
		analysisFac.setText("设施");
		analysisPipe.setText("管线|");
		analysisPipe.setTextSize(20);
		analysisFac.setTextSize(20);
		ll.addView(analysisPipe);
		ll.addView(analysisFac);
		if(isTrue.equals("1")){
			analysispoint = new Point(x, y);
			map.zoomToScale(analysispoint, map.getScale());
		}else{
			analysispoint = map.toMapPoint(x, y);
		}
		
		analysisFac.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					upload(FAC);
				}
			});
		analysisPipe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				upload(LINE);
			}
		});
		

		PictureMarkerSymbol symbol = null;
		//修正前的点渲染
		symbol = new PictureMarkerSymbol(
					context.getResources().getDrawable(R.drawable.dot3));
			

		map.getCallout().show(analysispoint,ll);
		grapLayer.removeAll();

		Graphic graphic = new Graphic(analysispoint, symbol);
		grapLayer.addGraphic(graphic);
	}/**
	 * 修正提示Dialog
	 * @param graphType：图形类型（1点，0线）
	 * @param point：爆管点
	 */
	private void reviseDialog(){
		
		String item[] = new String[]{"设施","管线"};
		new AlertDialog.Builder(context)
		.setTitle("请选择修正类型")
		.setSingleChoiceItems(item, -1, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				switch (which) {
				case 0:
					analysisType = FAC;
					break;
				case 1:
					analysisType = LINE;
					break;
				default:
					break;
				}
			}
		})
		.setNegativeButton("修正", new DialogInterface.OnClickListener() {
			
			@SuppressWarnings("unchecked")
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if("-1".equals(analysisType)){
					Toast.makeText(context, "请选择爆管设施图形类型", 4).show();
					return;
				}
				List<String> liststring = new ArrayList<String>();
				liststring.add(BigDecimalUtil.getBigDecimal(analysispoint.getX())+"");
				liststring.add(BigDecimalUtil.getBigDecimal(analysispoint.getY())+"");
				liststring.add(analysisType+"");
				analysisType = "-1";
				PipeBrokenAnalysisUpload2 analysisUpload = 
					new PipeBrokenAnalysisUpload2(PipeBroAnalysisTools2.this);
				analysisUpload.execute(liststring);
			}
		})
		.setPositiveButton("取消", null)
		.setCancelable(false)
		.show();
	}
	private void upload(String analysisType){
		if(LINE.equals(analysisType)||FAC.equals(analysisType)){
			List<String> liststring = new ArrayList<String>();
			liststring.add(BigDecimalUtil.getBigDecimal(analysispoint.getX())+"");
			liststring.add(BigDecimalUtil.getBigDecimal(analysispoint.getY())+"");
			liststring.add(analysisType+"");
			PipeBrokenAnalysisUpload2 analysisUpload = 
				new PipeBrokenAnalysisUpload2(PipeBroAnalysisTools2.this);
			analysisUpload.execute(liststring);
		}else{
			Toast.makeText(context, "请选择爆管设施图形类型", Toast.LENGTH_LONG).show();
		}
		
	}

	/**
	 * 爆管分析提示Dialog
	 * @param graphType：图形类型（1点，0线）
	 * @param point：爆管点
	 */
	private void analysisDialog(){
		new AlertDialog.Builder(context)
		.setTitle("分析提示")
		.setMessage("爆点位置如图所示 ，确认是否开始分析？")
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				/*PipeBroAnalysis analysisTask = new PipeBroAnalysis(PipeBroAnalysisTools.this, true);
				analysisTask.execute("ANALYSIS");*/
			}
		})
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		})
		.create().show();

	}
	public void showFindPoint(float x ,float y){
		int[] ids = null;
		try {
			ids = grapLayer.getGraphicIDs(x, y, 10);
		
			if(ids.length <= 0 ){
				return;
			}
			Graphic graphic = grapLayer.getGraphic(ids[0]);
			String EID = (String) graphic.getAttributeValue("EID");
			String ISAbnormal = (String)graphic.getAttributeValue("ISAbnormal");
			String facNum = (String)graphic.getAttributeValue("Ord");
			mustBeClosed = (String)graphic.getAttributeValue("MustBeClosed");
			String WORK_STATS = (String)graphic.getAttributeValue("WORK_STATS");
			String LANE_WAY = (String)graphic.getAttributeValue("LANE_WAY");
			String X = (String)graphic.getAttributeValue("x");
			String Y = (String)graphic.getAttributeValue("y");
			final String state = (String)graphic.getAttributeValue("state");
			
			boolean isAnalysisPoint = (Boolean)graphic.getAttributeValue("analysisPoint");
			if(isAnalysisPoint){
				TextView mapTvCallout = new TextView(context);
				mapTvCallout.setTextColor(Color.BLACK);
				mapTvCallout.setText("分析点");
				Point point = new Point(Float.parseFloat(X), Float.parseFloat(Y));
				map.zoomToScale(point,
						map.getScale());
				map.getCallout().show(point, mapTvCallout);
				return;
			}
			
			if(ISAbnormal.equals("true")){
				ISAbnormal = "是";
			}else{
				ISAbnormal = "否";
			}
			
			if(mustBeClosed.equals("true")){
				mustBeClosed = "是";
			}else{
				mustBeClosed = "否";
			}
			
			String content = "阀门编码：" + EID + "\n序号："
			+ facNum+ "\n是否异常："+ ISAbnormal + "\n必须关闭：" + mustBeClosed 
			+ "\n运行状态："+ WORK_STATS+ "\n所在道路："+ LANE_WAY ;
			
			TextView mapTvCallout = new TextView(context);
			mapTvCallout.setTextColor(Color.BLACK);
			mapTvCallout.setEms(13);
			mapTvCallout.setClickable(true);
			mapTvCallout.setOnClickListener(new OnClickListener() { 
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

						if(state.equals("true")){
							Toast.makeText(context, "已提交，不可修改", 4).show();
							return;
						}
						if(mustBeClosed.equals("否")){
							Toast.makeText(context, "此阀门不需要进行操作", 4).show();
							return;
						}
					/*	Intent intent = new Intent(context, PipeBroFmqbActivity.class);
						context.startActivity(intent);*/
				}
			});
			Point point = new Point(Float.parseFloat(X), Float.parseFloat(Y));
			mapTvCallout.setText(content);
			map.zoomToScale(point,
					map.getScale());
			map.getCallout().show(point, mapTvCallout);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	public void showFindPoint2(Map<String, String> d){
		try {
			String EID = d.get("EID");
			String ISAbnormal = d.get("ISAbnormal");
			String facNum = d.get("Ord");
			mustBeClosed = d.get("MustBeClosed");
			String WORK_STATS = d.get("WORK_STATS");
			String LANE_WAY = d.get("LANE_WAY");
			
			final String state = d.get("state");
			if(ISAbnormal.equals("true")){
				ISAbnormal = "是";
			}else{
				ISAbnormal = "否";
			}
			
			if(mustBeClosed.equals("true")){
				mustBeClosed = "是";
			}else{
				mustBeClosed = "否";
			}
			
			String content = "阀门编码：" + EID + "\n序号："
			+ facNum+ "\n是否异常："+ ISAbnormal + "\n必须关闭：" + mustBeClosed 
			+ "\n运行状态："+ WORK_STATS+ "\n所在道路："+ LANE_WAY ;
			
			TextView mapTvCallout = new TextView(context);
			mapTvCallout.setTextColor(Color.BLACK);
			mapTvCallout.setEms(13);
			mapTvCallout.setClickable(true);
			mapTvCallout.setOnClickListener(new OnClickListener() { 
	
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

						if(state.equals("true")){
							Toast.makeText(context, "已提交，不可修改", 4).show();
							return;
						}
						if(mustBeClosed.equals("否")){
							Toast.makeText(context, "此阀门不需要进行操作", 4).show();
							return;
						}
				}
			});
			 Type type0 = new TypeToken<List<String>>() {
				}.getType();
			List<String> points = new Gson().fromJson(d.get("points"), type0);
			float x = Float.parseFloat(points.get(0));
			float y = Float.parseFloat(points.get(1));
			Point point = new Point(x, y);
			mapTvCallout.setText(content);
			map.zoomToScale(point,
					map.getScale());
			map.getCallout().show(point, mapTvCallout);
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}
	public Context getContext() {
		return context;
	}
	public void setContext(Context context) {
		this.context = context;
	}
	public MyMapView getMap() {
		return map;
	}
	public void setMap(MyMapView map) {
		this.map = map;
	}
	public GraphicsLayer getGrapLayer() {
		return grapLayer;
	}
	public void setGrapLayer(GraphicsLayer grapLayer) {
		this.grapLayer = grapLayer;
	}
	public String getAnalysisType() {
		return analysisType;
	}
	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}
	public Point getAnalysispoint() {
		return analysispoint;
	}
	public void setAnalysispoint(Point analysispoint) {
		this.analysispoint = analysispoint;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getPipeBreakenData() {
		return pipeBreakenData;
	}
	public void setPipeBreakenData(String pipeBreakenData) {
		this.pipeBreakenData = pipeBreakenData;
	}
	public List<String> getBound() {
		return bound;
	}

	public void setBound(List<String> bound) {
		this.bound = bound;
	}

	public List<Map<String, String>> getData() {
		return data;
	}

	public void setData(List<Map<String, String>> data) {
		this.data = data;
	}

	public void showList(){
		if (data != null) {
			map.getHeaderContainer().removeAllViews();
			Point point = null;
			grapLayer.removeAll();
			map.getCallout().hide();
			grapLayer.setVisible(true);
			PipeBroAnalysisTools2.this.setActivityName("analysisFind");
//			PipeBroAnalysisTools2.this.setPipeBreakenData("");
			if (bound != null && bound.size() > 0) {
				Envelope envSrc = new Envelope(Float.parseFloat(bound
						.get(2)), Float.parseFloat(bound.get(3)),
						Float.parseFloat(bound.get(0)),
						Float.parseFloat(bound.get(1)));
				Polygon poly = new Polygon();
				poly.addEnvelope(envSrc, false);
				SimpleFillSymbol sfs = new SimpleFillSymbol(Color.BLUE);
				sfs.setAlpha(30);
				grapLayer.addGraphic(new Graphic(poly, sfs));
				point = new Point(envSrc.getCenterX(), envSrc.getCenterY());
				map.zoomToScale(point, map.getScale());
			}
			List<String> points;
			// PictureMarkerSymbol symbol;
			Symbol symbol;
			int i = 0;
			final Gson gson = new Gson();
			final Type type0 = new TypeToken<List<String>>() {
			}.getType();
			for (Map<String, String> p : data) {
				points = gson.fromJson(p.get("points"), type0);
				String ISAbnormal = p.get("ISAbnormal");
				String type = p.get("MustBeClosed");
				point = new Point(Float.parseFloat(points.get(0)),
						Float.parseFloat(points.get(1)));
				Map<String, Object> attr = new HashMap<String, Object>();
				try {
					attr.put("EID", p.get("EID"));
				} catch (Exception e) {
					// TODO: handle exception
					try {
						attr.put("EID", p.get("SID"));
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
				try {
					attr.put("Ord", p.get("Ord"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					attr.put("ISAbnormal", p.get("ISAbnormal"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					attr.put("MustBeClosed", p.get("MustBeClosed"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					attr.put("WORK_STATS", p.get("WORK_STATS"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					attr.put("LANE_WAY", p.get("LANE_WAY"));
				} catch (Exception e) {
					// TODO: handle exception
				}
				attr.put("state", p.get("state"));
				attr.put("analysisPoint", false);
				attr.put("x", points.get(0));
				attr.put("y", points.get(1));
				if (ISAbnormal == null)
					ISAbnormal = "true";
				if (ISAbnormal.equals("true")) {// 先判断阀门是否异常
					symbol = new PictureMarkerSymbol(context.getResources()
							.getDrawable(R.drawable.dot1));
				} else {// 判断阀门是否必须关闭
					if (type == null) {
						type = "true";
					}
					if (type.endsWith("true")) {
						symbol = new PictureMarkerSymbol(context
								.getResources()
								.getDrawable(R.drawable.dot0));
					} else {
						symbol = new PictureMarkerSymbol(context
								.getResources()
								.getDrawable(R.drawable.dot3));
					}
				}
				grapLayer.addGraphic(new Graphic(point, symbol, attr, i++));
			}
			symbol = new PictureMarkerSymbol(context.getResources()
					.getDrawable(R.drawable.dot2));
			Map<String, Object> attr2 = new HashMap<String, Object>();
			attr2.put("x", point.getX());
			attr2.put("y", point.getY());
			attr2.put("analysisPoint", true);
			grapLayer.addGraphic(new Graphic(analysispoint, symbol, attr2, i++));
			if (bound == null) {
				map.zoomToScale(point, map.getScale());
			}
			LinearLayout v = (LinearLayout) View.inflate(context,
					R.layout.pipe_bro_analysis_list, null);
			pipe_bro_list_ll = (RelativeLayout) v.findViewById(R.id.pipe_bro_list_ll);
			ListView pipe_bro_list =(ListView) v.findViewById(R.id.pipe_bro_list);
			pipe_bro_list_pack_up = (Button) v.findViewById(R.id.pipe_bro_list_pack_up);
			Button pipe_bro_list_project = (Button) v.findViewById(R.id.pipe_bro_list_project);
			pipe_bro_enlarge_count = (TextView) v.findViewById(R.id.pipe_bro_enlarge_count);
			Button pipe_bro_enlarge_btn =(Button) v.findViewById(R.id.pipe_bro_enlarge_btn);
			pipe_bro_enlarge_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if(badSwitchs.size()>0){
						JSONObject json;
						try {
							json = new JSONObject(pipeBreakenData);
							JSONArray jsonBadSwitchs = new JSONArray();
							for(int i = 0; i <badSwitchs.size(); i++){
								String Switch = badSwitchs.get(i);
								JSONObject switchs = new JSONObject(Switch);
								switchs.put("bad", true);
								jsonBadSwitchs.put(switchs);
							}
							json.put("badSwitchs", jsonBadSwitchs);
							pipeBreakenData = json.toString();
							PipeBroAnalysis2 analysisTask = new PipeBroAnalysis2(PipeBroAnalysisTools2.this, true,analysispoint,"");
							analysisTask.execute("EXTEND");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else{
						Toast.makeText(context, "请选损坏的阀门", Toast.LENGTH_LONG).show();
					}
				}
			});
			pipe_bro_list_project.setVisibility(View.GONE);
			pipe_bro_list_project.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 List<Module> md = AppContext.getInstance().getModuleData();
					 for(int i=0;i<md.size();++i){
						 if(Constant.BIZ_OFF_VALVE_PROJECT.equals(md.get(i).getId())){
							 Intent intent =new Intent(context, RunForm.class);
							 intent.putExtra("template", md.get(i).getTemplate());
							 intent.putExtra("delete", false);
							 HashMap<String, String> params = new HashMap<String, String>();
							 params.put("analytic", result.replace("\"", "\\\""));
							 intent.putExtra("iParams", params);
							 context.startActivity(intent);
							 break;
						 }
					 }
				}
			});
			pipe_bro_list.setAdapter(new BgfxCursorAdapter(context));
			/*pipe_bro_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					
				}
			});*/
			Button pipe_bro_list_close = (Button) v
					.findViewById(R.id.pipe_bro_list_close);
			pipe_bro_list_close.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					map.getHeaderContainer().removeAllViews();
				}
			});
			
			pipe_bro_list_pack_up.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(pipe_bro_list_ll.isShown()){
						pipe_bro_list_ll.setVisibility(View.GONE);
						pipe_bro_list_pack_up.setText("显示");
					}else{
						pipe_bro_list_ll.setVisibility(View.VISIBLE);
						pipe_bro_list_pack_up.setText("收起");
					}
					
				}
			});
			map.getHeaderContainer().addView(v);
		}else{
			Toast.makeText(context, "尚无数据显示，请分析后才显示", Toast.LENGTH_LONG).show();
		}
	}
	private class BgfxCursorAdapter extends BaseAdapter{
		private Context context;
		public BgfxCursorAdapter(Context context){
			this.context=context;
			 checks = new boolean[data.size()];  
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}
		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return data.get(arg0);
		}
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null)
				convertView = View.inflate(context, R.layout.pt_list_text, null);
			LinearLayout layout = (LinearLayout)convertView.findViewById(R.id.ptlist_Linear);
			TextView tvNumber = (TextView)convertView.findViewById(R.id.ptlist_Number);
			TextView tvContent = (TextView)convertView.findViewById(R.id.ptlist_Content);
			TextView ptlistClose = (TextView)convertView.findViewById(R.id.ptlistClose);
			CheckBox cbBroken = (CheckBox)convertView.findViewById(R.id.ptCbBroken);
			cbBroken.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					checks[position] = isChecked;  
					badSwitchs.remove(data.get(position).get("netElement"));
					if(isChecked){
						badSwitchs.add(data.get(position).get("netElement"));
					}
					if(badSwitchs.size()>0){
						pipe_bro_enlarge_count.setText("合计："+badSwitchs.size()+"项");
					}else{
						pipe_bro_enlarge_count.setText("");
					}
				}
			});
			cbBroken.setChecked(checks[position]);
			ptlistClose.setVisibility(View.VISIBLE);
			ptlistClose.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showFac(position);
				}
			});
			tvContent.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					showFac(position);
				}
			});
			
			String taskid = null;
			try {
				
				taskid = data.get(position).get("EID");
				if("null".equals(taskid)){
					taskid = data.get(position).get("SID");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			String address = null;
			try {
				address = data.get(position).get("LANE_WAY");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String ISAbnormal = "";
			try {
				ISAbnormal = data.get(position).get("ISAbnormal");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String mustClose = "";
			try {
				mustClose = data.get(position).get("MustBeClosed");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String Ord = "";
			try {
				Ord = data.get(position).get("Ord");
			} catch (Exception e) {
				// TODO: handle exception
			}
			String state = data.get(position).get("state");
			if(state.equals("true")){
				layout.setBackgroundResource(R.drawable.task_true);
			}else if(state.equals("busy")){
				layout.setBackgroundResource(R.drawable.task_busy);
			}else{
				layout.setBackgroundResource(R.drawable.task_false);
			}
			tvNumber.setText(Ord);
			tvContent.setText("设施编号：" + taskid+"\n所在道路：" + address);
			if(ISAbnormal.equals("true")){
				ISAbnormal = "<font color=\"#FFB90F\">是否异常：是</font><br> ";
			}else{
				ISAbnormal = "<font color=\"#0000ff\">是否异常：否</font><br> ";
			}
			if(mustClose.endsWith("true")){
				mustClose = "<font color=\"#ff0000\">必须关闭：是</font>";
			}else{
				mustClose = "<font color=\"#0000ff\">必须关闭：否</font>";
			}
			ptlistClose.setText(Html.fromHtml(ISAbnormal+mustClose));
			return convertView;
		}
	}
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	private void showFac(int arg2){
		pipe_bro_list_ll.setVisibility(View.GONE);
		pipe_bro_list_pack_up.setText("显示");
		/*List<String> points = gson.fromJson(data.get(arg2).get("points"), type0);
		float x = Float.parseFloat(points.get(0));
		float y = Float.parseFloat(points.get(1));*/
		PipeBroAnalysisTools2.this.showFindPoint2(data.get(arg2));
	}
	public List<String> getBadSwitchs() {
		return badSwitchs;
	}

	public void setBadSwitchs(List<String> badSwitchs) {
		this.badSwitchs = badSwitchs;
	}
	
}
