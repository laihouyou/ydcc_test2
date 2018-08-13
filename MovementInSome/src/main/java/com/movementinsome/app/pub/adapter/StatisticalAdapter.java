package com.movementinsome.app.pub.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.caice.vo.ProjectVo;

import java.sql.SQLException;
import java.util.List;

public class StatisticalAdapter extends BaseAdapter{

	private List<ProjectVo> miningSurveyList;
	private Context context;
	private List<Double> projcetLineLenghts;	//工程管线长度集合
	private List<Integer> pointVos;	//	每个工程设施点数量

	public StatisticalAdapter(Context context, List<ProjectVo> miningSurveyList,
							  List<Double> projcetLineLenghts,List<Integer> pointVos) throws SQLException {
		// TODO Auto-generated constructor stub
		this.miningSurveyList = miningSurveyList;
		this.context = context;
		this.projcetLineLenghts=projcetLineLenghts;
		this.pointVos=pointVos;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return miningSurveyList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return miningSurveyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stubfinal ViewHolder holder;
		final ViewHolder holder;
		if(convertView==null) {

			/**
			 * 因为listview设置了一个头布局，所以全部 position都从1开始
			 */

			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.statictical_item, null);
			holder.stl_item_proName = (TextView)convertView.findViewById(R.id.stl_item_proName);
			holder.stl_item_proName.setText(miningSurveyList.get(position).getProjectName());
			holder.stl_item_proType = (TextView)convertView.findViewById(R.id.stl_item_proType);
			holder.stl_item_proType.setText(miningSurveyList.get(position).getProjectType());
			holder.stl_item_time = (TextView)convertView.findViewById(R.id.stl_item_time);
//			holder.stl_item_time.setText(miningSurveyList.get(position).getProjectEDateUpd());
			holder.stl_item_facNum = (TextView)convertView.findViewById(R.id.stl_item_facNum);
			if (pointVos!=null&&pointVos.size()>0){
				holder.stl_item_facNum.setText(pointVos.get(position)+"");
			}else {
				holder.stl_item_facNum.setText("0");
			}
			holder.stl_item_faclength = (TextView)convertView.findViewById(R.id.stl_item_faclength);
			if (projcetLineLenghts!=null&&projcetLineLenghts.size()>0){
				holder.stl_item_faclength.setText(projcetLineLenghts.get(position).toString()+"米");
			}else {
				holder.stl_item_faclength.setText("0米");
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	static class ViewHolder {
		TextView stl_item_proName;
		TextView stl_item_proType;
		TextView stl_item_time;
		TextView stl_item_facNum;
		TextView stl_item_faclength;
	}

	public List<ProjectVo> getMiningSurveyList() {
		return miningSurveyList;
	}

	public void setMiningSurveyList(List<ProjectVo> miningSurveyList) {
		this.miningSurveyList = miningSurveyList;
	}

	public List<Double> getProjcetLineLenghts() {
		return projcetLineLenghts;
	}

	public void setProjcetLineLenghts(List<Double> projcetLineLenghts) {
		this.projcetLineLenghts = projcetLineLenghts;
	}

	public List<Integer> getPointVos() {
		return pointVos;
	}

	public void setPointVos(List<Integer> pointVos) {
		this.pointVos = pointVos;
	}
}
