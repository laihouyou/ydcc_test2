package com.movementinsome.easyform.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.map.MapBizViewer;

public class ExpGuiPipeBroAnalysisBtn extends LinearLayout implements IXmlGuiFormFieldObject {

	Context context;
	String value;
	Button pipeBroAnalysisBtn;
	public ExpGuiPipeBroAnalysisBtn(Context context,String title,String value) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.value = value;
		pipeBroAnalysisBtn = new Button(context);
		pipeBroAnalysisBtn.setText(title);
		pipeBroAnalysisBtn.setTextColor(Color.WHITE);
		pipeBroAnalysisBtn.setBackgroundResource(R.drawable.btn_next_bg);
		pipeBroAnalysisBtn.setLayoutParams(new LinearLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT));
		pipeBroAnalysisBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AppContext.getInstance().setmHandle(mHandler);
				Intent intent = new Intent("mapBizContain");
				intent.putExtra("type", MapBizViewer.BIZ_MAP_OPERATE_PIPEBRO_ANALYSIS);
				intent.putExtra("pipeBroAnalysisValue",ExpGuiPipeBroAnalysisBtn.this.value);
				((Activity) ExpGuiPipeBroAnalysisBtn.this.context).startActivity(intent);
			}
		});
		if(value!=null&&!"".equals(value)){
			pipeBroAnalysisBtn.setText("已分析");
			pipeBroAnalysisBtn.setTextColor(Color.RED);
		}
		this.addView(pipeBroAnalysisBtn);
	}
	private Handler mHandler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1){
				String result = AppContext.getInstance().getPipeBroAnalysisResult();
				if(result!=null&&!"".equals(result)){
					value = result.replace("\"", "\\\"");
					pipeBroAnalysisBtn.setText("已分析");
					pipeBroAnalysisBtn.setTextColor(Color.RED);
				}
			}
		};
	};

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		
	}
	public void setValue(String v)
	{
		this.value = v;
	}
	public String getValue()
	{
		return value;
	}
	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		this.value = value;
	}

}
