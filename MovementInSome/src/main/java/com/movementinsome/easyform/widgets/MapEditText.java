package com.movementinsome.easyform.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.core.tasks.identify.IdentifyResult;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.map.MapBizViewer;

public class MapEditText extends EditText {
	private Context context;

	private Drawable mRightDrawable;
	private boolean isHasFocus;
	private Drawable img_right;
	private int REQUEST_CODE = 999999;
	private String defvalue;
	private String[] mapCoordOpt = new String[] { "直接获取当前位置坐标", "地图上自定义获取坐标" };
	private String[] mapCoordOpt1 = new String[]{"查询周边设施","按类型查询设施","地图上选择"};
	private String [] mapCoordBoundPpt = new String [] {"直接获取当前位置坐标", "地图上自定义获取坐标","地图上定义获取范围"};
	
	private String mMapPosition = "";// 点
	private String mMapBound = "";// 面
	private String mMapArea = "";
	private IdentifyResult mMapIden;// 设施属性
	
	private String rule;
	private int index; // 表示选项的索引
	private boolean readOnly;
	private TextView showTv;

	public MapEditText(Context context, String defvalue,String rule,boolean readOnly,TextView showTv) {
		super(context);
		
		this.context = context;
		this.defvalue = defvalue;
		this.rule = rule;
		this.readOnly=readOnly;
		this.showTv = showTv;
		showTv.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					showMap();
					break;

				default:
					break;
				}
				return true;
			}
		});
		if(readOnly){
			setEnabled(false);
		}
		init(context);
	}
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1){
				mMapBound=AppContext.getInstance().getMapBound();
				mMapArea=AppContext.getInstance().getmMapArea();
				IdentifyResult mMapIden=AppContext.getInstance().getmMapIden();
				mMapPosition=AppContext.getInstance().getMapPosition();
				//Toast.makeText(context, mMapIden+"", Toast.LENGTH_LONG).show();
				if (defvalue.contains("mapcoord()")
						||defvalue.contains("mapcoord(N)")) {
					setText(mMapPosition);
					showTv.setText("已获取坐标");
				} else if (defvalue.contains("mapbound()")){
					//进入地图画面
					setText(mMapBound);
					showTv.setText("已获取区域");
				} else if (defvalue.contains("maparea()")){
					//进入地图画面
					setText(mMapArea);
					showTv.setText(mMapArea);
				} else if (defvalue.contains("mapfac()")){
					//选择设施
					Intent intent = new Intent("RunForm");
					intent.putExtra("req", rule);  
					intent.putExtra("value","");
					MapEditText.this.context.sendBroadcast(intent);
				}else if(defvalue.contains("mapcoordbound()")){
					//setText(mMapBound);
					if(index==0||index==1){
						showTv.setText("已获取坐标");
						setText(mMapPosition);
					}else if(index==2){
						//进入地图画面
						setText(mMapBound);
						showTv.setText("已获取区域");
					}
				}
			}
		};
	};

	public MapEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MapEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(final Context context) {
		// getCompoundDrawables:
		// Returns drawables for the left, top, right, and bottom borders.
		this.context = context;

		Resources res = getResources();
		img_right = res.getDrawable(R.drawable.loc_icon);

		img_right.setBounds(0, 0, img_right.getMinimumWidth(),
				img_right.getMinimumHeight());
		this.setCompoundDrawables(null, null, img_right, null);

		showTv.setCompoundDrawables(null, null, img_right, null);
		mRightDrawable = this.getResources()
				.getDrawable(R.drawable.loc_icon);

		this.setOnFocusChangeListener(new FocusChangeListenerImpl());
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			boolean isClick = (event.getX() > (getWidth() - getTotalPaddingRight()))
					&& (event.getX() < (getWidth() - getPaddingRight()));
			if (isClick) {
				showMap();
			}

			break;

		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	private class FocusChangeListenerImpl implements OnFocusChangeListener {
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			// if (hasFocus)
			// isHasFocus=hasFocus;
			// setClickDrawableVisible(true);
			/*
			 * if (isHasFocus) { boolean
			 * isVisible=getText().toString().length()>=1;
			 * setClearDrawableVisible(isVisible); } else {
			 * setClearDrawableVisible(false); }
			 */
		}

	}

	private class TextWatcherImpl implements TextWatcher {
		@Override
		public void afterTextChanged(Editable s) {
			boolean isVisible = getText().toString().length() >= 1;
			setClickDrawableVisible(isVisible);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

	}

	protected void setClickDrawableVisible(boolean isVisible) {
		Drawable rightDrawable;
		if (isVisible) {
			rightDrawable = mRightDrawable;
		} else {
			rightDrawable = null;
		}

		setCompoundDrawables(null, null, img_right, null);
	}

	public void setShakeAnimation() {
		this.setAnimation(shakeAnimation(5));
	}

	public Animation shakeAnimation(int CycleTimes) {
		Animation translateAnimation = new TranslateAnimation(0, 10, 0, 10);
		translateAnimation.setInterpolator(new CycleInterpolator(CycleTimes));
		translateAnimation.setDuration(1000);
		return translateAnimation;
	}

	private void showMap() {

		if (defvalue == null || "".equals(defvalue)) {
			((Activity) this.context).startActivityForResult(new Intent(
					"mapBizContain"), REQUEST_CODE);
		} else if (defvalue.contains("mapcoord()")
				||defvalue.contains("mapcoord(N)")) {
			ButtonOnClick buttonOnClick = new ButtonOnClick(0);

			new AlertDialog.Builder(this.context).setTitle("方式选择")
					.setSingleChoiceItems(
					// .setMultiChoiceItems(
							mapCoordOpt, 0, buttonOnClick)
					.setPositiveButton("确定", buttonOnClick)
					.setNegativeButton("取消", buttonOnClick).show();
		}else if(defvalue.contains("mapcoordbound()")){
			// 进入地图画面
			/*AppContext.getInstance().setmHandle(mHandler);
			Intent intent = new Intent("mapBizContain");
			intent.putExtra("type", MapBizViewer.BIZ_MAP_OPERATE_BOUND);
			((Activity) MapEditText.this.context).startActivity(intent);*/
			ButtonOnClick2 buttonOnClick = new ButtonOnClick2(0);

			new AlertDialog.Builder(this.context).setTitle("方式选择")
					.setSingleChoiceItems(
					// .setMultiChoiceItems(
							mapCoordBoundPpt, 0, buttonOnClick)
					.setPositiveButton("确定", buttonOnClick)
					.setNegativeButton("取消", buttonOnClick).show();
		}else if (defvalue.contains("mapbound()")) {
			// 进入地图画面
			AppContext.getInstance().setmHandle(mHandler);
			Intent intent = new Intent("mapBizContain");
			intent.putExtra("type", MapBizViewer.BIZ_MAP_OPERATE_BOUND);
			((Activity) this.context).startActivity(intent);
		}else if (defvalue.contains("maparea()")){
			AppContext.getInstance().setmHandle(mHandler);
			Intent intent = new Intent("mapBizContain");
			intent.putExtra("type", MapBizViewer.BIZ_MAP_OPERATE_AREA);
			((Activity) this.context).startActivity(intent);
		}
			/* else if ("mapfac()".equalsIgnoreCase(defvalue)){
		}
			
			Button2OnClick buttonOnClick = new Button2OnClick(0); 
			
			new AlertDialog.Builder(this.context)
					.setTitle("方式选择")
					.setSingleChoiceItems(
					// .setMultiChoiceItems(
							mapCoordOpt1, 0, buttonOnClick)
					.setPositiveButton("确定",buttonOnClick).setNegativeButton("取消", buttonOnClick).show();
			

		} */else if (defvalue.contains("mapfac()")) {
			// 选择设施
			AppContext.getInstance().setmHandle(mHandler);
			Intent intent = new Intent("mapBizContain");
			intent.putExtra("type", MapBizViewer.BIZ_MAP_OPERATE_INS_ATTRIBUTE);
			((Activity) this.context).startActivity(intent);
		}
		/*
		 * Intent intent=new Intent(); intent.setClass(this.context,
		 * MapViewer.class); intent.putExtra("str", "Intent Demo"); ((Activity)
		 * this.context).startActivityForResult(intent, 1000000);
		 */
	}

	private class ButtonOnClick implements DialogInterface.OnClickListener {
		

		public ButtonOnClick(int index) {
			MapEditText.this.index = index;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
			if (which >= 0) {
				// 如果单击的是列表项，将当前列表项的索引保存在index中。
				// 如果想单击列表项后关闭对话框，可在此处调用dialog.cancel()
				// 或是用dialog.dismiss()方法。
				index = which;
			} else {
				// 用户单击的是【确定】按钮
				if (which == DialogInterface.BUTTON_POSITIVE) {
					if (index == 0) {
						if(AppContext.getInstance().getCurLocation()!=null){
							setText(AppContext.getInstance().getCurLocation().getCurMapPosition());
							showTv.setText("已获取坐标");
						}else{
							Toast.makeText(context, "获取不到GPS坐标", Toast.LENGTH_LONG).show();
						}
					} else {
						AppContext.getInstance().setmHandle(mHandler);
						Intent intent = new Intent("mapBizContain");
						intent.putExtra("type",MapBizViewer.BIZ_MAP_OPERATE_POSITION);
						if (!"".equals(MapEditText.this.getText())){
							intent.putExtra("position",MapEditText.this.getText().toString());
						}
						
						((Activity) MapEditText.this.context)
								.startActivityForResult(intent,
										MapBizViewer.BIZ_MAP_OPERATE_POSITION);
					}
				} else if (which == DialogInterface.BUTTON_NEGATIVE) {// 用户单击的是【取消】按钮
					
					return;

				}
			}
		}
	}
	private class ButtonOnClick2 implements DialogInterface.OnClickListener {
		//private int index; // 表示选项的索引

		public ButtonOnClick2(int index) {
			MapEditText.this.index = index;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
			if (which >= 0) {
				// 如果单击的是列表项，将当前列表项的索引保存在index中。
				// 如果想单击列表项后关闭对话框，可在此处调用dialog.cancel()
				// 或是用dialog.dismiss()方法。
				index = which;
			} else {
				// 用户单击的是【确定】按钮
				if (which == DialogInterface.BUTTON_POSITIVE) {
					if (index == 0) {
						if(AppContext.getInstance().getCurLocation()!=null){
							setText(AppContext.getInstance().getCurLocation().getCurMapPosition());
							showTv.setText("已获取坐标");
						}else{
							Toast.makeText(context, "获取不到GPS坐标", Toast.LENGTH_LONG).show();
						}
					} else if(index == 1){
						AppContext.getInstance().setmHandle(mHandler);
						Intent intent = new Intent("mapBizContain");
						intent.putExtra("type",MapBizViewer.BIZ_MAP_OPERATE_POSITION);
						if (!"".equals(MapEditText.this.getText())){
							intent.putExtra("position",MapEditText.this.getText().toString());
						}
						
						((Activity) MapEditText.this.context)
								.startActivityForResult(intent,
										MapBizViewer.BIZ_MAP_OPERATE_POSITION);
					}else if(index == 2){
						// 进入地图画面
						AppContext.getInstance().setmHandle(mHandler);
						Intent intent = new Intent("mapBizContain");
						intent.putExtra("type", MapBizViewer.BIZ_MAP_OPERATE_BOUND);
						((Activity) MapEditText.this.context).startActivity(intent);
					}
				} else if (which == DialogInterface.BUTTON_NEGATIVE) {// 用户单击的是【取消】按钮
					
					return;

				}
			}
		}
	}
	
	private class Button2OnClick implements DialogInterface.OnClickListener {
		//private int index; // 表示选项的索引

		public Button2OnClick(int index) {
			MapEditText.this.index = index;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			// which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。
			if (which >= 0) {
				// 如果单击的是列表项，将当前列表项的索引保存在index中。
				// 如果想单击列表项后关闭对话框，可在此处调用dialog.cancel()
				// 或是用dialog.dismiss()方法。
				index = which;
			} else {
				// 用户单击的是【确定】按钮
				if (which == DialogInterface.BUTTON_POSITIVE) {
					if (index == 0&&AppContext.getInstance().getCurLocation()!=null) {
						setText(AppContext.getInstance().getCurLocation().getCurMapPosition());
					} else if (index==1){
						
					}else if (index==2){
						//选择设施
						AppContext.getInstance().setmHandle(mHandler);
						Intent intent=new Intent("mapBizContain");
						intent.putExtra("type", MapBizViewer.BIZ_MAP_OPERATE_INS_ATTRIBUTE);
						((Activity) MapEditText.this.context).startActivity(intent);
					}
				} else if (which == DialogInterface.BUTTON_NEGATIVE) {// 用户单击的是【取消】按钮
					
					return;

				}
			}
		}
	}
}
