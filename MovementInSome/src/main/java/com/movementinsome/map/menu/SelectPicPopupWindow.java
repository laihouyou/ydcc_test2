package com.movementinsome.map.menu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.database.vo.MapmenuVO;
import com.movementinsome.database.vo.MapparentmenuVO;
import com.movementinsome.database.vo.MapparentmenusVO;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.map.facedit.MapEditFac;
import com.movementinsome.map.nearby.NearByTools;
import com.movementinsome.map.offvalve.PipeBroAnalysisTools;
import com.movementinsome.map.search.SearchDialog;
import com.movementinsome.map.utils.DrawGraphicTools;
import com.movementinsome.map.utils.MapMeterUtil;
import com.movementinsome.map.view.MapScreenshotView;
import com.movementinsome.map.view.MyMapView;

import java.util.ArrayList;
import java.util.List;

public class SelectPicPopupWindow extends PopupWindow {


	private Button btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;
	private MyMapView map;
	private MapMeterUtil mapMeterUtil;// 测量工具
	private PipeBroAnalysisTools pipeBroAnalysisTools;// 关阀分析工具
	private DrawGraphicTools drawGraphicTools;// 绘图
	private MapScreenshotView mapScreenshotView;
	private MapparentmenusVO mapparentmenusVO;// 菜单对象
	private List<MapparentmenuVO> mapparentmenuList;// 父菜单列表
	private MapEditFac mapEditFac;
	private int screenWidths;

	public SelectPicPopupWindow(final ContainActivity context,MyMapView map,OnClickListener itemsOnClick) {
		super(context);
		this.map=map;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.alert_dialog, null);
		final LinearLayout pop_layout=(LinearLayout) mMenuView.findViewById(R.id.pop_layout);
		final LinearLayout pop_layout_submenu=(LinearLayout) mMenuView.findViewById(R.id.pop_layout_submenu);
		LinearLayout tableRow=new LinearLayout(context);
		
		mapparentmenusVO=AppContext.getInstance().getMapparentmenusVO();
		if(mapparentmenusVO!=null){
			mapparentmenuList=mapparentmenusVO.getMapparentmenuList();
		}
		// 菜单数据
		/*final List<String>menuNameF=new ArrayList<String>();
	//	menuNameF.add("设施编辑");
		menuNameF.add("测量");
		menuNameF.add("绘图");
		menuNameF.add("关阀分析");
		menuNameF.add("附近");
	//	menuNameF.add("地名搜索");
		menuNameF.add("截图");
		// 子菜单数据
		final Map<String , List<String>>submenuData=new HashMap<String, List<String>>();
		List<String>submenuNames=new ArrayList<String>();
		submenuNames.add("画点");
		submenuNames.add("画直线");
		submenuNames.add("画矩型");
		submenuNames.add("画圆型");
		submenuNames.add("多边型");
		submenuNames.add("自由线");
		submenuNames.add("截图");
		submenuNames.add("清除");
		submenuNames.add("返回");
		submenuData.put("绘图", submenuNames);
		submenuNames=new ArrayList<String>();
		submenuNames.add("坐标");
		submenuNames.add("距离");
		submenuNames.add("面积");
		submenuNames.add("撤销");
		submenuNames.add("重绘");
		submenuNames.add("返回");
		submenuData.put("测量", submenuNames);
		submenuNames=new ArrayList<String>();
		submenuNames.add("管道");
		submenuNames.add("设施");
		submenuNames.add("返回");
		submenuData.put("关阀分析", submenuNames);*/
		
		final List<Button>bs=new ArrayList<Button>();
		int n=0;
		
		if(mapparentmenuList!=null){
			for( int i=0;i<mapparentmenuList.size();++i){
				final Button b=new Button(context);
				final List<MapmenuVO> mapmenuList=mapparentmenuList.get(i).getMapmenuList();// 子菜单
				if(!mapparentmenuList.get(i).isIsdisplay()){
					break;
				}
				DisplayMetrics dm = new DisplayMetrics();
				 //获取屏幕信息
				    context.getWindowManager().getDefaultDisplay().getMetrics(dm);
				 
				    int screenWidth = dm.widthPixels;
				    screenWidths = screenWidth/3;
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 
						screenWidths, 
						DensityUtil.dip2px(context, 40),1.0f); 
				b.setLayoutParams(params);

				b.setBackgroundResource(R.drawable.map_layer_background);
				final String typeName=mapparentmenuList.get(i).getName();
				final String pid=mapparentmenuList.get(i).getId();
				b.setText(typeName);
				bs.add(b);
				b.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
					//	List<String>submenuNames=submenuData.get(typeName);
						for(int i=0;i<bs.size();++i){
							if(b==bs.get(i)){
								b.setBackgroundColor(Color.argb(100, 200, 0, 0));
							}else{
								bs.get(i).setBackgroundResource(R.drawable.map_layer_background);
							}
						}
						if(mapmenuList!=null&&mapmenuList.size()>0){
							showSubmenu(context, mapmenuList, pop_layout_submenu,pop_layout);
						}else{
							Toast.makeText(context, typeName, Toast.LENGTH_LONG).show();
							dismiss();
						}
						if("screenshot".equals(pid)){// 截图
							mapScreenshotView=new MapScreenshotView(context,SelectPicPopupWindow.this.map);
							mapScreenshotView.show();
						}else if("placesearch".equals(pid)){// 地名搜索
							Intent intent =new Intent();
							intent.setClass(context, SearchDialog.class);
							context.startActivity(intent);
						}else if("nearby".equals(pid)){// 附近
							int[] location = new int[2];
							NearByTools nbTools = new NearByTools(context, SelectPicPopupWindow.this.map,SelectPicPopupWindow.this.map.getMapParam().getBizMap(),SelectPicPopupWindow.this.map.getMapParam().getFtlayers(),location);
							nbTools.popu();
						}else if("facEdit".equals(pid)){
							if(mapEditFac==null){
								mapEditFac=new MapEditFac(context, SelectPicPopupWindow.this.map);
							}
						}
					}
				});
				if(n<3){
					tableRow.addView(b);
					++n;
				}else{
					n=0;
					pop_layout.addView(tableRow);
					tableRow=new LinearLayout(context);
					tableRow.addView(b);
					++n;
				}
			}
		}
		
		if(n!=0){
			pop_layout.addView(tableRow);
		}
		btn_take_photo = (Button) mMenuView.findViewById(R.id.btn_take_photo);
		btn_pick_photo = (Button) mMenuView.findViewById(R.id.btn_pick_photo);
		btn_cancel = (Button) mMenuView.findViewById(R.id.btn_cancel);
		//取消按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//销毁弹出框
				dismiss();
			}
		});
		//设置按钮监听
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		//设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		//设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		//设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
//		//设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.AnimBottom);
//		//实例化一个ColorDrawable颜色为半透明
//		ColorDrawable dw = new ColorDrawable(0xb0000000);
//		//设置SelectPicPopupWindow弹出窗体的背景
//		this.setBackgroundDrawable(dw);
		//mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}
	private void showSubmenu(final Context context,List<MapmenuVO> mapmenuList,final LinearLayout pop_layout_submenu,final LinearLayout pop_layout){
		pop_layout_submenu.removeAllViews();
		LinearLayout tableRow=new LinearLayout(context);
		int n=0;
		final List<Button>bs=new ArrayList<Button>();
		for(int i=0;i<mapmenuList.size();++i){
			if(!mapmenuList.get(i).isIsdisplay()){
				break;
			}
			
			final String id=mapmenuList.get(i).getId();
			if (null == id){ //配制文件中的ID值为空值
				break;
			}
			final String submenuName=mapmenuList.get(i).getName();
			final Button b=new Button(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 
					screenWidths, 
					DensityUtil.dip2px(context, 40),1.0f); 
			b.setLayoutParams(params);
			b.setWidth(50);
			b.setBackgroundResource(R.drawable.map_layer_background);
			bs.add(b);
			b.setText(submenuName);
			b.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					if("back".equals(id)){// 返回
						pop_layout.setVisibility(View.VISIBLE);
						pop_layout_submenu.setVisibility(View.GONE);
						if(mapMeterUtil!=null){
							mapMeterUtil.exit();
							mapMeterUtil=null;
						}
						if(pipeBroAnalysisTools!=null){
							pipeBroAnalysisTools.exit();
							map.getMap_search_layout().setVisibility(View.VISIBLE);
							pipeBroAnalysisTools=null;
						}
						if(drawGraphicTools!=null){
							drawGraphicTools.exit();
							drawGraphicTools=null;
						}
						if(mapEditFac!=null){
							mapEditFac.exit();
							mapEditFac = null;
						}
						
					}else{
						for(int i=0;i<bs.size();++i){
							if(b==bs.get(i)){
								b.setBackgroundColor(Color.argb(100, 200, 0, 0));
							}else{
								bs.get(i).setBackgroundResource(R.drawable.map_layer_background);
							}
						}
						if("coord".equals(id)){// 坐标
							if(mapMeterUtil==null){
								mapMeterUtil=new MapMeterUtil(map, context);
							}else{
								mapMeterUtil.clearAllLayer();
							}
							mapMeterUtil.setDrawType(MapMeterUtil.POINT);
							dismiss();
						}else if("distance".equals(id)){// 距离
							if(mapMeterUtil==null){
								mapMeterUtil=new MapMeterUtil(map, context);
							}else{
								mapMeterUtil.clearAllLayer();
							}
							mapMeterUtil.setDrawType(MapMeterUtil.LINE);
							dismiss();
						}else if("area".equals(id)){// 面积
							if(mapMeterUtil==null){
								mapMeterUtil=new MapMeterUtil(map, context);
							}else{
								mapMeterUtil.clearAllLayer();
							}
							mapMeterUtil.setDrawType(MapMeterUtil.POLY);
							dismiss();
						}else if("backout".equals(id)){// 撤销
							if(mapMeterUtil==null){
								mapMeterUtil=new MapMeterUtil(map, context);
							}
							mapMeterUtil.recallPoint();
						}else if("redraw".equals(id)){// 重绘
							if(mapMeterUtil==null){
								mapMeterUtil=new MapMeterUtil(map, context);
							}
							mapMeterUtil.clearAllLayer();
							dismiss();
						}else if("pipeline".equals(id)){// 管道
							if(pipeBroAnalysisTools==null){
								map.getMap_search_layout().setVisibility(View.GONE);
								pipeBroAnalysisTools=new PipeBroAnalysisTools(context, map);
							}
							pipeBroAnalysisTools.setAnalysisType(PipeBroAnalysisTools.LINE);
							dismiss();
						}else if("facility".equals(id)){// 设施
							if(pipeBroAnalysisTools==null){
								map.getMap_search_layout().setVisibility(View.GONE);
								pipeBroAnalysisTools=new PipeBroAnalysisTools(context, map);
							}
							pipeBroAnalysisTools.setAnalysisType(PipeBroAnalysisTools.FAC);
							dismiss();
						}else if("point".equals(id)){// 画点
							if(drawGraphicTools==null){
								drawGraphicTools=new DrawGraphicTools(context, map);
								drawGraphicTools.draw(DrawGraphicTools.DRAW_GRAPHIC_TYPE.PT);
							}else{
								drawGraphicTools.setDrawType(DrawGraphicTools.DRAW_GRAPHIC_TYPE.PT);
							}
							dismiss();
						}else if("straightline".equals(id)){// 画直线
							if(drawGraphicTools==null){
								drawGraphicTools=new DrawGraphicTools(context, map);
								drawGraphicTools.draw(DrawGraphicTools.DRAW_GRAPHIC_TYPE.PL);
							}else{
								drawGraphicTools.setDrawType(DrawGraphicTools.DRAW_GRAPHIC_TYPE.PL);
							}
							dismiss();
						}else if("rectangle".equals(id)){// 画矩型
							if(drawGraphicTools==null){
								drawGraphicTools=new DrawGraphicTools(context, map);
								drawGraphicTools.draw(DrawGraphicTools.DRAW_GRAPHIC_TYPE.ENV);
							}else{
								drawGraphicTools.setDrawType(DrawGraphicTools.DRAW_GRAPHIC_TYPE.ENV);
							}
							dismiss();
						}else if("circle".equals(id)){// 画圆型
							if(drawGraphicTools==null){
								drawGraphicTools=new DrawGraphicTools(context, map);
								drawGraphicTools.draw(DrawGraphicTools.DRAW_GRAPHIC_TYPE.CIRCLE);
							}else{
								drawGraphicTools.setDrawType(DrawGraphicTools.DRAW_GRAPHIC_TYPE.CIRCLE);
							}
							dismiss();
						}else if("freelinear".equals(id)){// 自由线
							if(drawGraphicTools==null){
								drawGraphicTools=new DrawGraphicTools(context, map);
								drawGraphicTools.draw(DrawGraphicTools.DRAW_GRAPHIC_TYPE.FRE_PL);
							}else{
								drawGraphicTools.setDrawType(DrawGraphicTools.DRAW_GRAPHIC_TYPE.FRE_PL);
							}
							dismiss();
						}else if("polygon".equals(id)){// 多边型
							if(drawGraphicTools==null){
								drawGraphicTools=new DrawGraphicTools(context, map);
								drawGraphicTools.draw(DrawGraphicTools.DRAW_GRAPHIC_TYPE.PG);
							}else{
								drawGraphicTools.setDrawType(DrawGraphicTools.DRAW_GRAPHIC_TYPE.PG);
							}
							dismiss();
						}else if("clear".equals(id)){// 清除
							if(drawGraphicTools!=null){
								drawGraphicTools.clear();
							}else if(pipeBroAnalysisTools!=null){
								pipeBroAnalysisTools.clear();
							}
						}else if("screenshot".equals(id)){// 截图
							mapScreenshotView=new MapScreenshotView(context,SelectPicPopupWindow.this.map);
							mapScreenshotView.show();
						}else if("showList".equals(id)){ // 显示列表
							if(pipeBroAnalysisTools!=null)
								pipeBroAnalysisTools.showList();
						}else if("facAdd".equals(id)){// 设施增加
							
							mapEditFac.setType("add");
							mapEditFac.getMapEditFacListView().showFacListNameDialog("add");
							mapEditFac.clear();
						}else if("facAlter".equals(id)){// 修改设施
							
							mapEditFac.setType("edit");
							mapEditFac.getMapEditFacListView().showFacListNameDialog("edit");
							mapEditFac.clear();
						}else if("facDelete".equals(id)){ // 删除设施
							
							mapEditFac.setType("delect");
							mapEditFac.getMapEditFacListView().showFacListNameDialog("delect");
							mapEditFac.clear();
						}
					}
				}
			});
			if(n<3){
				tableRow.addView(b);
				++n;
			}else{
				n=0;
				pop_layout_submenu.addView(tableRow);
				tableRow=new LinearLayout(context);
				tableRow.addView(b);
				++n;
			}
		}
		if(n!=0){
			pop_layout_submenu.addView(tableRow);
		}
		pop_layout.setVisibility(View.GONE);
		pop_layout_submenu.setVisibility(View.VISIBLE);
	}

}
