package com.movementinsome.map.nearby;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.R;

import java.util.List;


public class NearByFacTypeListAdater extends BaseAdapter{

	private List<List<FacTypeObj>>fac_type_data;
	private Context context;
	
	
	public NearByFacTypeListAdater(List<List<FacTypeObj>>fac_type_data,Context context){
		this.fac_type_data=fac_type_data;
		this.context=context;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fac_type_data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return fac_type_data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView==null){
			convertView=View.inflate(context, R.layout.fac_type_list, null);
		}
		TextView fac_type_title=(TextView) convertView.findViewById(R.id.fac_type_title);
		LinearLayout tableLayout=(LinearLayout) convertView.findViewById(R.id.fac_type);
		tableLayout.removeAllViews();
		
		List<FacTypeObj> d=fac_type_data.get(position);
		int n=0;
		if(d!=null){
			LinearLayout tableRow=new LinearLayout(context);
			for(int i=0;i<d.size();++i){
				if(i==0){// 默认第一个是总类型
					fac_type_title.setText(fac_type_data.get(position).get(i).getLabel());
				}else{
					final String fac_name=fac_type_data.get(position).get(i).getLabel();
					Button b=new Button(context);
				//	b.setOnClickListener(new FacOnClickListener(context,mapView,grapLayer,fac_type_data.get(position).get(i)));
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( 
							ViewGroup.LayoutParams.WRAP_CONTENT, 
							ViewGroup.LayoutParams.WRAP_CONTENT,1.0f); 
					b.setLayoutParams(params);
					b.setText(fac_type_data.get(position).get(i).getLabel());
					
					b.setBackgroundResource(R.drawable.map_layer_background);
					if(n<3){
						tableRow.addView(b);
						++n;
					}else{
						n=0;
						tableLayout.addView(tableRow);
						tableRow=new LinearLayout(context);
						tableRow.addView(b);
						++n;
					}
				}
			}
			if(n!=0){
				tableLayout.addView(tableRow);
			}
		}
		
		return convertView;
	}

}
