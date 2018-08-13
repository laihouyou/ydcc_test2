package com.movementinsome.map.nearby;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esri.core.map.FeatureSet;
import com.esri.core.map.Field;
import com.esri.core.map.Graphic;
import com.movementinsome.R;
import com.movementinsome.map.view.MyMapView;

import java.util.List;

public class FacListAdapter extends BaseAdapter {

	private List<Graphic> graphicList;
	private Context context;
	private List<FeatureSet> result;
	private List<Field> fields;
	private MyMapView mapView;
	private NearByTools nearByTools;

	public FacListAdapter(Context context, List<Graphic> graphicList,
			List<FeatureSet> result,NearByTools nearByTools) {
		this.context = context;
		this.graphicList = graphicList;
		this.result = result;
		fields = result.get(0).getFields();
		this.nearByTools=nearByTools;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return graphicList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return graphicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.nearby_fac_item, null);
		}
		TextView name = (TextView) convertView
				.findViewById(R.id.nearby_fac_name);
		TextView msg = (TextView) convertView.findViewById(R.id.nearby_fac_msg);
		TextView nearby_fac_loc = (TextView) convertView
				.findViewById(R.id.nearby_fac_loc);
		TextView nearby_fac_details = (TextView) convertView
				.findViewById(R.id.nearby_fac_details);
		TextView nearby_fac_arrive = (TextView) convertView
				.findViewById(R.id.nearby_fac_arrive);

		nearby_fac_loc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nearByTools.locFac(graphicList.get(position));
			}
		});
		nearby_fac_details.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String names="";
				String values="";
				for(int i=0;i<fields.size();++i){
					if(names.equals("")){
						names+=fields.get(i).getAlias();
					}else{
						names+=(","+fields.get(i).getAlias());
					}
					if(values.equals("")){
						values+=graphicList.get(position).getAttributes().get(fields.get(i).getName());
					}else{
						values+=(","+graphicList.get(position).getAttributes().get(fields.get(i).getName()));
					}
				}
				nearByTools.showFacDetails(names, values);
			}
		});
		nearby_fac_arrive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nearByTools.facArrive(graphicList.get(position).getGeometry());
			}
		});

		Graphic g = graphicList.get(position);
		name.setText("" + position);
		msg.setText(fields.get(0).getAlias() + ":"
				+ g.getAttributes().get(fields.get(0).getName() + ""));
		return convertView;
	}

}
