package com.movementinsome.easyform.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.database.vo.TpconfigVO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpGuiMutiLineSelect extends LinearLayout implements IXmlGuiFormFieldObject {

	TextView label;
	EditText txtBox;
	Button selectBtn;
	Context context;
	String value;
	String rule;
	String id;
	Button fSpinner;
	String cName;
	String[] opts;
	
	public ExpGuiMutiLineSelect(final View fGroup,Context context,String labelText,String initialText,String value,String options,String rule,boolean readOnly
			,String id,String fId,String fName,final String cName,String mName,String textId) {
		super(context);
		this.context = context;
		this.value = value;
		this.rule = rule;
		this.id =id;
		this.cName =cName;
		
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		label.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT));
		txtBox = new EditText(context);
		txtBox.setTextSize(14);
		if(textId!=null){
			txtBox.setId(Integer.parseInt(textId));
		}
		//txtBox.setBackgroundResource(R.drawable.input_sm_bg);
		txtBox.setHeight(200);
		selectBtn=new Button(context);
		if(id!=null){
			selectBtn.setId(Integer.parseInt(id));
		}
		selectBtn.setBackgroundResource(R.drawable.pull_down_big);
		opts = options.split("\\|");
		if(fId!=null){// 当前控件有父控件
			fSpinner = (Button) fGroup.findViewById(Integer.parseInt(fId));
			if(fSpinner!=null){
				String fValue = fSpinner.getText()+"";
				if(fValue.equals("")){
					fValue=" ";
				}
				String name = fName;
				if(fValue!=null&&name!=null){
					Dao<TpconfigVO, Long> tpconfigDao;
					try {
						tpconfigDao = AppContext.getInstance().getAppDbHelper()
								.getDao(TpconfigVO.class);
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("frontName", name);
						m.put("frontOption", fValue);
						if(mName!=null){
							m.put("name", mName);
						}
						List<TpconfigVO> configVos = tpconfigDao.queryForFieldValuesArgs(m);
						if(configVos!=null){
							opts = new String[configVos.size()];
							for (int i=0;i<opts.length;++i) {
								opts[i]=configVos.get(i).getPvalue();
							}
						}
						
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(opts==null||opts.length==0){
				opts = new String[]{""};
			}
		}
		
		RelativeLayout ll=new RelativeLayout(context);
		ll.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		
		if(readOnly){
			txtBox.setEnabled(false);
			selectBtn.setEnabled(false);
		}
		ll.addView(txtBox);
		ll.addView(selectBtn);
		if (value != null){
			txtBox.setText(value);
		}else{
			txtBox.setText(initialText);
		}
		txtBox.setBackgroundResource(R.drawable.input_big_icon);
		txtBox.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//与父容器的右侧对齐
		lp.addRule(RelativeLayout.CENTER_VERTICAL);
		lp.rightMargin=15;
		selectBtn.setLayoutParams(lp);
		this.addView(label);
		this.addView(ll);
		
		selectBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectDialog(opts);
			}
		});
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
		    	onChanged();
		    }
		});
	}

	public ExpGuiMutiLineSelect(Context context, AttributeSet attrs) {
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
		context.sendBroadcast(intent);
	}
	public void selectDialog(final String [] items){
		new AlertDialog.Builder(context)
		.setTitle("列表框")
		.setItems(items, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				setValue(items[which]);
			}
		})
		.show();
	}
}
