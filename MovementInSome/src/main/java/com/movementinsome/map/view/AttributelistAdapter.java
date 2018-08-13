package com.movementinsome.map.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.esri.core.map.Graphic;
import com.movementinsome.R;
import com.movementinsome.kernel.initial.model.Field;
import com.movementinsome.kernel.initial.model.Ftlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AttributelistAdapter extends BaseAdapter {

	private Activity context;
	private int num;
	private List<Map<String, Object>> identifyDataGs;
	private Handler myHandler;
	MoveViewOnTouchListener moveViewOnTouchListener;
	private Button identifyDaoLayoutButton;
	private int hight;

	public AttributelistAdapter(Activity context, int num,ArrayList<Map<String, Object>> identifyDataGs, Handler myHandler,MoveViewOnTouchListener moveViewOnTouchListener,Button identifyDaoLayoutButton,int hight) {
		super();
		this.context = context;
		this.num = num;
		this.identifyDaoLayoutButton = identifyDaoLayoutButton;
		//this.activity = activity;
		this.identifyDataGs = identifyDataGs;
		this.myHandler = myHandler;
		this.moveViewOnTouchListener= moveViewOnTouchListener;
		this.hight = hight;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return identifyDataGs.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return identifyDataGs.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (arg1 == null) {
            holder = new ViewHolder();
            arg1 = View.inflate(context, num, null);
            holder.tv = (TextView)arg1.findViewById(R.id.iden_list_tv);
            holder.tv2 = (TextView)arg1.findViewById(R.id.iden_list_tvNum);
           // if(position == 0)  holder.radioButton.setChecked(true);
            arg1.setTag(holder);
        } else {
            holder = (ViewHolder) arg1.getTag();
        }
		 final Graphic graphic = (Graphic) identifyDataGs.get(arg0).get("Graphic");
         if(graphic==null){
        	 LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams( 
     				ViewGroup.LayoutParams.MATCH_PARENT,hight);
        	 holder.tv.setLayoutParams(paramsLayout);
        	 holder.tv.setText("");
        	 holder.tv2.setText("");
        	 return arg1;
         }else{
        	 LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams( 
      				ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
         	 holder.tv.setLayoutParams(paramsLayout);
        	 final String layerName = (String) identifyDataGs.get(arg0).get("LayerName");
        	 final Ftlayer ftLayer= (Ftlayer) identifyDataGs.get(arg0).get("FtLayer");
             List<Field> fields=ftLayer.getFields();
             String text="设施类型:"+layerName+"\n";
             for(int i=0;i<fields.size();++i){
             	String synopsis = fields.get(i).getSynopsis();
             	if("isSynopsis".equals(synopsis)){
             		String alias = fields.get(i).getAlias();
             		String v = graphic.getAttributes().get(fields.get(i).getName())+"";
                 	if("null".equals(v)){
                 		v = graphic.getAttributes().get(fields.get(i).getAlias())+"";
                 	}
                 	if(i==fields.size()-1){
                 		text+=alias+":"+v;
                 	}else{
                 		text+=alias+":"+v+"\n";
                 	}
             	}
             }
             holder.tv.setText(text);
             int nums = arg0+1;
             holder.tv2.setText("("+nums+")");
             holder.tv.setOnClickListener(new OnClickListener() {
    			
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				if(!moveViewOnTouchListener.isMove()){
    					String names="";
        				String values="";
        				String value2="";
        				String keys="";
        				 List<Field> fields=ftLayer.getFields();
        				 for(int i=0;i<fields.size();++i){
        					 keys+=fields.get(i).getName()+",";
        					 Graphic graphic= (Graphic) identifyDataGs.get(arg0).get("Graphic");
        					 String valuec=graphic.getAttributes().get(fields.get(i).getName())+"";
	 							if ("null".equals(valuec)){
	 								valuec=graphic.getAttributes().get(fields.get(i).getAlias())+"";
	 							}
	 							if(fields.get(i).getName().equals("FINISH_DATE")&&valuec.length()==13){
	 								valuec=TimeStamp2Date(valuec);
	 							}
	 						 value2+=valuec+",";
        					 if(fields.get(i).isVisible()){
        						 names+=fields.get(i).getAlias()+",";
            					 String value=graphic.getAttributes().get(fields.get(i).getName())+"";
        							if ("null".equals(value)){
        								value=graphic.getAttributes().get(fields.get(i).getAlias())+"";
        							}
        							if(fields.get(i).getName().equals("FINISH_DATE")&&value.length()==13){
        								value=TimeStamp2Date(value);
    	 							}
        						 values+=value+",";
        					 }
        				 }
        				 Message msg = new Message();
        				 Bundle data = new Bundle();
        				 Graphic graphic= (Graphic) identifyDataGs.get(arg0).get("Graphic");
        				 data.putSerializable("graphic", graphic);
        				 data.putString("names", names);
        				 data.putString("values", values);
        				 data.putString("keys", keys);
        				 data.putString("value2", value2);
        				 data.putString("layerName", layerName);
        				 identifyDaoLayoutButton.setVisibility(View.VISIBLE);
        				 msg.setData(data);
        				 msg.what = 4;
        				 myHandler.sendMessage(msg);
        				 myHandler.sendEmptyMessage(0);
    				}
    			}
    		});
             
    		return arg1;
         }
         
	}
	
	//Convert Unix timestamp to normal date style  
	public String TimeStamp2Date(String timestampString){  
		Long timestamp = Long.parseLong(timestampString)*1;  
		String date = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(timestamp));  
		 return date;  
	}  
	
	 private class ViewHolder {
		 TextView tv;
		 TextView tv2;
	 }
}
