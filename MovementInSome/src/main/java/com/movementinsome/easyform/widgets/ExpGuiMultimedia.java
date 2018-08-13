package com.movementinsome.easyform.widgets;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.easyform.widgets.multimedia.view.VoicPlayPopupWindow;
import com.movementinsome.easyform.widgets.multimedia.view.VoicRcdPopupWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExpGuiMultimedia  extends LinearLayout implements IXmlGuiFormFieldObject{

	private ImageView voicPlay;
	private TextView voicRcd;
	private View exp_gui_multimedia;
	private VoicRcdPopupWindow voicRcdPopupWindow;
	private boolean isMove = true;
	private float startY;
	private boolean isMoveUp = false;
	long startVoiceT;
	private String path;
	String value;
	private List<String> voicList;
	Context context;
	
	public ExpGuiMultimedia(final Context context,String value) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.value = value;
		
		path = AppContext.getInstance().getAppStoreMedioVoicPath();
		exp_gui_multimedia = View.inflate(context, R.layout.exp_gui_multimedia, null);
		voicPlay = (ImageView) exp_gui_multimedia.findViewById(R.id.multimedia_play_voic);
		voicRcd = (TextView) exp_gui_multimedia.findViewById(R.id.multimedia_rcd_voic);
		voicList = new ArrayList<String>();
		if(value!=null){
			String[] values = value.split("\\,");
			if (values!=null&&!"".equalsIgnoreCase(value)){
				for(String v:values){
					voicList.add(v);
				}
			}
		}
		voicPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				VoicPlayPopupWindow voicPlayPopupWindow = new VoicPlayPopupWindow(context,voicPlay, voicList);
			}
		});
		voicRcd.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent mEve) {
				// TODO Auto-generated method stub
				int action = mEve.getAction();
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					voicRcd.setBackgroundResource(R.drawable.voice_rcd_btn_pressed);
					voicRcdPopupWindow = new VoicRcdPopupWindow(context);
					voicRcdPopupWindow.showAtLocation(voicRcd, Gravity.CENTER, 0, 0);
					startVoiceT = SystemClock.currentThreadTimeMillis();
					voicRcdPopupWindow.start(path,startVoiceT+".mp3");
					break;
				case MotionEvent.ACTION_MOVE:
					float y = mEve.getY();
					if(isMove){
						startY =y;
						isMove = false;
					}
					if(startY-y>10){
						isMoveUp = true;
					}
					break;
				case MotionEvent.ACTION_UP:
					isMove = true;
					
					voicRcd.setBackgroundResource(R.drawable.voice_rcd_btn_nor);
					voicRcdPopupWindow.stop();
					if(isMoveUp){
						File f = new File(path+startVoiceT+".mp3");
						f.delete();
						isMoveUp = false;
					}else{
						voicList.add(path+startVoiceT+".mp3");
					}
					voicRcdPopupWindow.dismiss();
					break;

				default:
					break;
				}
				return true;
			}
		});
		this.setLayoutParams(new LinearLayout.LayoutParams( 
					ViewGroup.LayoutParams.MATCH_PARENT, 
					ViewGroup.LayoutParams.WRAP_CONTENT));
		this.addView(exp_gui_multimedia);
	}

	public String getValue() {
		String value ="";
		for(String v:voicList){
			if ("".equals(value)){
				value = v;
			}else{
				value+=","+v;
			}
		}
		return value;
	}
	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		this.value = value;
	}

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("RunForm");
		intent.putExtra("req", "onChanged");  
		intent.putExtra("value","");
		context.sendBroadcast(intent);
	}

}
