package com.movementinsome.easyform.widgets;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.util.DensityUtil;
import com.movementinsome.database.vo.TpconfigVO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpGuiMultiselect extends LinearLayout implements IXmlGuiFormFieldObject{

	Context context;
	String value;
	String rule;
	
	TextView label;
	Button multiselectBtn;
	Button fSpinner;
	boolean[] selected;
	String cName;
	String []opts;
	String id;
	String mName;
	public ExpGuiMultiselect(final View fGroup,Context context,String labelText,String options,String value,String rule,boolean readOnly
			,String id,String fId,String fName,final String cName,String mName) {
		super(context);
		
		this.context=context;
		this.value = value;
		this.rule = rule;
		this.cName =cName;
		this.id =id;
		this.mName = mName;
		
		label = new TextView(context);
		label.setText(labelText);
		label.setEms(context.getResources().getInteger(R.integer.label_ems));
		label.setGravity(Gravity.CENTER_VERTICAL);
		label.setTextColor(Color.BLACK);
		multiselectBtn=new Button(context);
		if(id!=null){
			multiselectBtn.setId(Integer.parseInt(id));
		}
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
					name = name.replace("*", "");
					Dao<TpconfigVO, Long> tpconfigDao;
					try {
						tpconfigDao = AppContext.getInstance().getAppDbHelper()
								.getDao(TpconfigVO.class);
						Map<String, Object> m = new HashMap<String, Object>();
						m.put("frontName", name);
						m.put("frontOption", fValue);
						if(ExpGuiMultiselect.this.mName!=null){
							m.put("name", ExpGuiMultiselect.this.mName);
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
		String[] values = (value == null?null:value.split("\\,"));

		//当opts值的顺序不一样时，会有BUG
		selected=new boolean[opts.length];
		if (values!=null){
			int i =0;
			for(String o:opts){
				
				for(String v:values){
					if (o.equals(v)){
						selected[i] = true;
						break;
					}
				}
				i++;
			}
		}
		multiselectBtn.setLayoutParams(new LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				DensityUtil.dip2px(context, 45)));
		multiselectBtn.setBackgroundResource(R.drawable.radio_icon);
		multiselectBtn.setText(value);
		multiselectBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				wxT4btnDialog(opts, selected, multiselectBtn);
			}
		});
		if(readOnly){
			multiselectBtn.setEnabled(false);
		}
		this.addView(label);
		this.addView(multiselectBtn);
		
		multiselectBtn.addTextChangedListener(new TextWatcher() {
		    
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
	public ExpGuiMultiselect(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 自定义多选控件
	 * @param items ：字符数组
	 * @param selected: boolean 数组
	 * @param wxT4btn : 显示控件ID
	 */
	public void wxT4btnDialog(final String items[],final boolean selected[],final Button btn){
		final boolean[] selected1=new boolean[selected.length];
		for(int i=0;i<selected.length;++i){
			selected1[i]=selected[i];
		}
		AlertDialog.Builder builer = new AlertDialog.Builder(context);
		AlertDialog dialog;
		builer.setTitle("多选");
		builer.setMultiChoiceItems(items, selected1, new DialogInterface.OnMultiChoiceClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				// TODO Auto-generated method stub
				selected1[which ] = isChecked ;
			}
		});
		builer.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String strValveFault = "";
				for (int i = 0; i < selected1.length; i++) {
					if(selected1[i] == true){
						if(strValveFault.length() > 0){
							strValveFault += ",";
						}
						strValveFault = strValveFault + items[i];
					}
				}
				for(int i=0;i<selected1.length;++i){
					selected[i]=selected1[i];
				}
				btn.setText(strValveFault);
				
			}
		});
		builer.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog = builer.create();
		dialog.show();
	}
	public String getValue(){
		return multiselectBtn.getText()+"";
	}
	@Override
	public void autoChangValue(String value) {
		// TODO Auto-generated method stub
		multiselectBtn.setText(value);
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
