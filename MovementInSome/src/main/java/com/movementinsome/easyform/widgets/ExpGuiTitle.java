package com.movementinsome.easyform.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;

public class ExpGuiTitle extends RelativeLayout{
	private TextView titleV;
	private Button backBtn;
	private TextView commitBtn;
	private TextView saveBtn;
	private LinearLayout btnLayout;
	public ExpGuiTitle(final Context context,String title){
		super(context);
		btnLayout= new LinearLayout(context);
		RelativeLayout.LayoutParams paramsTv = new RelativeLayout.LayoutParams( 
				ViewGroup.LayoutParams.MATCH_PARENT, 
				ViewGroup.LayoutParams.WRAP_CONTENT);
		paramsTv.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		titleV = new TextView(context);
		titleV.setText(title);
		titleV.setTextColor(Color.WHITE);
		titleV.setGravity(Gravity.CENTER);
		titleV.setTextSize(18);
		titleV.setLayoutParams(paramsTv);
		backBtn=new Button(context);
		backBtn.setBackgroundResource(R.drawable.back_icon);
		RelativeLayout.LayoutParams params1=new RelativeLayout.LayoutParams(
				DensityUtil.dip2px(context, 20),
				DensityUtil.dip2px(context, 20));
		params1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		backBtn.setLayoutParams(params1);
		
		RelativeLayout.LayoutParams params2=new RelativeLayout.LayoutParams(
				DensityUtil.dip2px(context, 55),
				DensityUtil.dip2px(context, 32));
		RelativeLayout.LayoutParams params3=new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		params3.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
		
		btnLayout.setLayoutParams(params3);
		
		saveBtn=new TextView(context);
		saveBtn.setGravity(Gravity.CENTER);
		saveBtn.setBackgroundResource(R.drawable.btn_icon);
		saveBtn.setLayoutParams(params2);
		saveBtn.setText("保存");
		saveBtn.setTextColor(Color.WHITE);

		commitBtn=new TextView(context);
		commitBtn.setBackgroundResource(R.drawable.btn_icon);
		commitBtn.setLayoutParams(params2);
		commitBtn.setGravity(Gravity.CENTER);
		commitBtn.setText("提交");
		commitBtn.setTextColor(Color.WHITE);
		btnLayout.addView(saveBtn);
		btnLayout.addView(commitBtn);
		
		this.setPadding(DensityUtil.dip2px(context, 10), 0, DensityUtil.dip2px(context, 5), 0);
		this.setLayoutParams(new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				DensityUtil.dip2px(context, 45)));
		this.setBackgroundResource(R.color.title_color);
		this.addView(titleV);
		this.addView(backBtn);
		this.addView(btnLayout);
	}

	public ExpGuiTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public Button getBackBtn() {
		return backBtn;
	}

	public void setBackBtn(Button backBtn) {
		this.backBtn = backBtn;
	}

	public TextView getCommitBtn() {
		return commitBtn;
	}

	public void setCommitBtn(TextView commitBtn) {
		this.commitBtn = commitBtn;
	}

	public TextView getSaveBtn() {
		return saveBtn;
	}

	public void setSaveBtn(Button saveBtn) {
		this.saveBtn = saveBtn;
	}
	
	
}
