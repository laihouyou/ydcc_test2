package com.movementinsome.app.pub.view;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;

import java.util.List;


public class CreateDynamicView {

	private Context context;
	private Resources res;

	public CreateDynamicView(Context context) {
		super();
		this.context = context;
		res = this.context.getResources();
	}


	/**
	 * 用于动态创建并显示消息推送下来新任务的字段
	 * @param context Activity
	 * @param itemKey 消息推送：Key
	 * @param itemValue 消息推送：Value
	 * @return
	 */
	public View dynamicAddView(List<String> listValue,int id){
		//拼接任务详细内容
		String[] itemKey = res.getStringArray(id);

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
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 

		LinearLayout pushPLayout = new LinearLayout(context);
		pushPLayout.setOrientation(LinearLayout.VERTICAL);
		pushPLayout.setLayoutParams(paramsPLayout);
		pushPLayout.setGravity(Gravity.CENTER_VERTICAL);

		for(int i = 0 ;i < itemKey.length; i++){
			String value = "";
			if(i<listValue.size()){
				value = listValue.get(i);
			}
	
			TextView tvKey = new TextView(context);
			tvKey.setTextColor(Color.BLACK);
			tvKey.setGravity(Gravity.CENTER_VERTICAL);
			tvKey.setText(itemKey[i]);
			tvKey.setTextSize(20);
			tvKey.setEms(4);
			tvKey.setPadding(0, 5, 0, 5);

			TextView tvValue = new TextView(context);
			tvValue.setTextColor(Color.BLACK);
			tvValue.setGravity(Gravity.CENTER_VERTICAL);
			tvValue.setText("null".equals(value)?"":value);
			tvValue.setTextSize(20);
			tvValue.setPadding(5, 5, 0, 5);
			

			
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
	/**
	 * 用于动态创建并显示消息推送下来新任务的字段
	 * @param context Activity
	 * @param itemKey 消息推送：Key
	 * @param itemValue 消息推送：Value
	 * @return
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
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT); 

		LinearLayout pushPLayout = new LinearLayout(context);
		pushPLayout.setOrientation(LinearLayout.VERTICAL);
		pushPLayout.setLayoutParams(paramsPLayout);
		pushPLayout.setGravity(Gravity.CENTER_VERTICAL);

		for(int i = 0 ;i < nameList.length; i++){
			String value = "";
			if(i<valueList.length){
				value = valueList[i];
			}
			TextView tvKey = new TextView(context);
			tvKey.setTextColor(Color.BLACK);
			tvKey.setGravity(Gravity.CENTER_VERTICAL);
			tvKey.setText(nameList[i]);
			tvKey.setTextSize(20);
			tvKey.setEms(4);
			tvKey.setPadding(0, 5, 0, 5);

			TextView tvValue = new TextView(context);
			tvValue.setTextColor(Color.BLACK);
			tvValue.setGravity(Gravity.CENTER_VERTICAL);
			tvValue.setTextSize(20);
			tvValue.setText(value);
			tvValue.setPadding(5, 5, 0, 5);

			
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
