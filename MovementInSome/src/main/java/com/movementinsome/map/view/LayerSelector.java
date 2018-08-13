package com.movementinsome.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.esri.android.map.MapView;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.util.WebAccessTools.NET_STATE;
import com.movementinsome.map.MapLoad;

import java.io.File;

public class LayerSelector {
	private View layerView = null;
	private PopupWindow mPopupWindow;
	
	//private Context context;
	private ImageButton layerButton;

	public LayerSelector(Context context,ImageButton layerButton,final MapView mMapView){
		//this.context = context;
		this.layerButton = layerButton;
		layerView = View.inflate(context,
				R.layout.map_layer, null);
		RadioGroup layer_selector = (RadioGroup)layerView.findViewById(R.id.layer_selector);
		LinearLayout layer_texts = (LinearLayout)layerView.findViewById(R.id.layer_texts);
		for(Mapservice mapService:MapLoad.mapParam.getBaseMap().getMapservices()){
			RadioButton rb = new RadioButton(context);
			rb.setId(mapService.getId());
			rb.setBackgroundResource(R.drawable.map_layer_2d);   // 设置RadioButton的背景图片  
			rb.setButtonDrawable(R.drawable.transparent);           // 设置按钮的样式  
			rb.setText(mapService.getLabel());  
			rb.setTextColor(Color.BLUE);
			rb.setGravity(Gravity.CENTER);
			if (MapLoad.mapParam.getBaseMap().getDefault()==mapService.getId())
				rb.setChecked(true);
			else
				rb.setChecked(false);
			layer_selector.addView(rb, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT); 
		}
		layer_selector
		.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group,
					int checkedId) {
				int id = group.getCheckedRadioButtonId();
				for (Mapservice service:MapLoad.mapParam.getBaseMap().getMapservices()){
					MapLoad.mapParam.getBaseMap().setDefault(id);
					String url = "";
					if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.FOEIGN){
						url = service.getForeign();
					}
					else{
						url = service.getLocal();
					}
					if (!url.equals("")){
						boolean vsb = false;
						if (service.getId()==id){
							vsb = true;
						}else{
							vsb = false;
						}
						
						if (url.contains("file:///")){ //本地地图文件
							File file = new File(url.substring(7));
							if (file.exists()){
								mMapView.getLayerByURL(url).setVisible(vsb);
							}
						}else{
							try{
								mMapView.getLayerByURL(url).setVisible(vsb);
							}catch(NullPointerException ex){
								;
							}
						}
					}					

					mMapView.postInvalidate();
				}
			}
		});
		mPopupWindow = new PopupWindow(layerView,
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(
				context.getResources(), (Bitmap) null));
		// mPopupWindow.showAsDropDown(layerView);
		
	}
	public void popu(){
		int[] location = new int[2];
		layerButton.getLocationOnScreen(location);
		mPopupWindow.showAtLocation(layerView, Gravity.NO_GRAVITY,
				location[0] - layerView.getWidth(), location[1]
						+ layerButton.getHeight());
	}
}
