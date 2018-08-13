/*
 * XmlGui application.
 * Written by Frank Ableson for IBM Developerworks
 * June 2010
 * Use the code as you wish -- no warranty of fitness, etc, etc.
 */
package com.movementinsome.easyform.widgets;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.okhttp.OkHttpParam;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ExpGuiEditBox extends LinearLayout implements IXmlGuiFormFieldObject {
	TextView label;
	TextView txtBox;
	Context context;
	String value;
	String rule;
	// 访问后台获取唯一编号(上传参数)
	String tableName;
	String id;

	private String newText;		//输入后的值
	private String labelText;
	private String associatedControlId;

	public ExpGuiEditBox(final Context context,String labelText,String initialText
			,String value,String rule,boolean readOnly,String tableName,String id,String associatedControlId) {
		super(context);
		this.context = context;
		this.value = value;
		this.rule = rule;
		this.tableName = tableName;
		this.id = id;
		this.labelText=labelText;
		this.associatedControlId=associatedControlId;

		if (associatedControlId!=null&&!associatedControlId.equals("")){
			EventBus.getDefault().register(this);
		}
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
		txtBox = new EditText(context);
		txtBox.setTextSize(14);
		txtBox.setGravity(Gravity.CENTER_VERTICAL);
		if(id!=null&&!"".equals(id)){
			txtBox.setId(Integer.parseInt(id));
		}
		if (value != null){
			txtBox.setText(value);
		}else{
			txtBox.setText(initialText);
		}
		if(tableName!=null&&!"".equals(tableName)){
			txtBox = new TextView(context);
			txtBox.setHint("点击获取编号");
			txtBox.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					if("".equals(txtBox.getText()+""))
						new MyAsyncTask(context).execute("");
				}
			});
			txtBox.setBackgroundResource(R.drawable.input_icon);
			txtBox.setTextColor(Color.BLACK);
			txtBox.setGravity(Gravity.CENTER_VERTICAL);
			txtBox.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,DensityUtil.dip2px(context, 45)));
		}else{
			txtBox.setBackgroundResource(R.drawable.input_icon);
			txtBox.setTextColor(Color.BLACK);
			txtBox.setGravity(Gravity.CENTER_VERTICAL);
			txtBox.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,DensityUtil.dip2px(context, 45)));
		}
		if(readOnly){
			txtBox.setEnabled(false);
		}
		this.addView(label);
		this.addView(txtBox);
		
		txtBox.addTextChangedListener(new TextWatcher() {
		    
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		        // TODO Auto-generated method stub
		    }
		    
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
		            int after) {
		        // TODO Auto-generated method stub
		    }
		    
		    @Override
		    public void afterTextChanged(Editable s) {
		        // TODO Auto-generated method stub
				newText=s.toString();
		    	onChanged();
		    }
		});
	}

	public ExpGuiEditBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void makeNumeric()
	{
		DigitsKeyListener dkl = new DigitsKeyListener(true,true);
		txtBox.setKeyListener(dkl);
	}
	public void makeNumber(){
		txtBox.setInputType(InputType.TYPE_CLASS_NUMBER);
	}
	public String getValue()
	{
		return txtBox.getText().toString();
	}
	
	public void setValue(String v)
	{
		txtBox.setText(v);
	}

	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		txtBox.setText(value);
	}

	@Override
	public void onChanged() {
		// TODO Auto-generated method stub
		Intent intent = new Intent("RunForm");
		intent.putExtra("req", "onChanged");  
		intent.putExtra("value","");
		if (labelText.equals("*物探点号")||labelText.equals("*管线编号")){
			intent.putExtra(OkHttpParam.FAC_NAME,newText);
		}
		context.sendBroadcast(intent);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void getProjId(String pamcerStr) {
		String [] pamcerData =pamcerStr.split(",");
		if (pamcerData.length>1){
			String associatedControl=pamcerStr.split(",")[0];
			if (associatedControlId.equals(associatedControl)){
				String pvaluenum=pamcerStr.split(",")[1];
				txtBox.setText(pvaluenum);
			}
		}
	}

	private class MyAsyncTask extends AsyncTask<String, Void, String> {
		Context context;
		private ProgressDialog progressDialog;// 进度条
		
		public MyAsyncTask(Context context){
			this.context =context;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage("正在获取编号，请稍后");
			progressDialog.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String contextUrl = AppContext.getInstance().getServerUrl();
			contextUrl += SpringUtil._HEARTBEATRESOURCE_SEQUENCE;
			
			return SpringUtil.postData(contextUrl, tableName);
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			if(result==null||"".equals(result)){
				Toast.makeText(context, "获取编号失败,请点击获取", Toast.LENGTH_LONG).show();
			}
			txtBox.setText(result);
		}
	}

}
