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

import com.esri.core.tasks.identify.IdentifyResult;
import com.movementinsome.R;
import com.movementinsome.kernel.initial.model.Field;
import com.movementinsome.kernel.initial.model.Ftlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DenlistAdapter extends BaseAdapter {

	private Activity context;
	private int num;
	private List<Map<String, Object>> identifyDatass;
	private Handler myHandler;
	MoveViewOnTouchListener moveViewOnTouchListener;
	private Button identifyDaoLayoutButton;

	public DenlistAdapter(Activity context, int num,ArrayList<Map<String, Object>> identifyDatass, Handler myHandler,MoveViewOnTouchListener moveViewOnTouchListener,Button identifyDaoLayoutButton) {
		super();
		this.context = context;
		this.num = num;
		this.identifyDaoLayoutButton = identifyDaoLayoutButton;
		//this.activity = activity;
		this.identifyDatass = identifyDatass;
		this.myHandler = myHandler;
		this.moveViewOnTouchListener= moveViewOnTouchListener;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return identifyDatass.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return identifyDatass.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
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
         final IdentifyResult identifyResult = (IdentifyResult) identifyDatass.get(arg0).get("IdentifyResult");
         if(identifyResult==null){
        	 int screenHeight = (Integer) identifyDatass.get(arg0).get("itemH");
        	 LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams( 
     				ViewGroup.LayoutParams.MATCH_PARENT,screenHeight);
        	 holder.tv.setLayoutParams(paramsLayout);
        	 holder.tv.setText("");
        	 holder.tv2.setText("");
        	 return arg1;
         }else{
        	 LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams( 
      				ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        	 LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.0f);
         	 holder.tv.setLayoutParams(paramsLayout);
        	 final String layerName = (String) identifyDatass.get(arg0).get("LayerName");
        	 final Ftlayer ftLayer= (Ftlayer) identifyDatass.get(arg0).get("FtLayer");
             List<Field> fields=ftLayer.getFields();
             String text="设施类型:"+layerName+"\n";
             for(int i=0;i<fields.size();++i){
             	String synopsis = fields.get(i).getSynopsis();
             	if("isSynopsis".equals(synopsis)){
             		String alias = fields.get(i).getAlias();
                 	String v = identifyResult.getAttributes().get(fields.get(i).getName())+"";
                 	if("null".equals(v)){
                 		v = identifyResult.getAttributes().get(fields.get(i).getAlias())+"";
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
    			
    			public void onClick(View arg0) {
    				// TODO Auto-generated method stub
    				if(!moveViewOnTouchListener.isMove()){
    					String names="";
        				String values="";
        				String value2="";
        				String keys="";
        				 List<Field> fields=ftLayer.getFields();
        				 for(int i=0;i<fields.size();++i){
        					  keys+=fields.get(i).getName()+",";
        					 String valuec=identifyResult.getAttributes().get(fields.get(i).getName())+"";
	 							if ("null".equals(valuec)){
	 								valuec=identifyResult.getAttributes().get(fields.get(i).getAlias())+"";
	 							}
	 						 value2+=valuec+",";
        					 if(fields.get(i).isVisible()){
        						 names+=fields.get(i).getAlias()+",";
        						 String value=identifyResult.getAttributes().get(fields.get(i).getName())+"";
        							if ("null".equals(value)){
        								value=identifyResult.getAttributes().get(fields.get(i).getAlias())+"";
        							}
        						 values+=value+",";
        					 }
        				 }
        				 keys+="LayerName";
        				 value2+=layerName;
        				 /*Map<String, Object> m = identifyResult.getAttributes();
        				 Set<String> set = m.keySet();
        				 Iterator<String>iterator = set.iterator();
        				 while(iterator.hasNext()){
        					 String key = iterator.next();
        					 keys += key+",";
        					 value2 += m.get(key)+",";
        				 }*/
        				 Message msg = new Message();
        				 Bundle data = new Bundle();
        				 data.putSerializable("identifyResult", identifyResult);
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
	
	 private class ViewHolder {
		 TextView tv;
		 TextView tv2;
	 }
}
