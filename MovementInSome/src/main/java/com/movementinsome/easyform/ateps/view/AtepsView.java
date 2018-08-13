package com.movementinsome.easyform.ateps.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.movementinsome.R;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.easyform.formengineer.XmlGuiForm;

import java.util.List;
import java.util.Map;

public class AtepsView {

	private List<Map<String, Object>> data;
	private Context context;
	private AtepsAdapter atepsAdapter;
	private Button ataps_commit;
	private Button ateps_back;
	
	public View getAtepsView(final Context context,final ViewFlipper llOut,final XmlGuiForm theForm,String text
			,final String taskCategory, final String tableName,final String taskNum){
		this.data=theForm.getAtapsData();
		this.context=context;
		View v=View.inflate(context, R.layout.ateps_activity, null);
		ateps_back=(Button) v.findViewById(R.id.ateps_back);// 返回
		TextView ateps_title=(TextView) v.findViewById(R.id.ateps_title);// 标题
		ataps_commit=(Button) v.findViewById(R.id.ataps_commit);// 提交
		
		ateps_title.setText(text);
		ListView ateps_list=(ListView) v.findViewById(R.id.ateps_list);
		
		atepsAdapter=new AtepsAdapter();
		ateps_list.setAdapter(atepsAdapter);
		ateps_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int location,
					long arg3) {
				// TODO Auto-generated method stub
				if(location==0||"2".equals((String) AtepsView.this.data.get(location-1).get("state"))||"5".equals((String) AtepsView.this.data.get(location-1).get("state"))){
					if("2".equals((String) AtepsView.this.data.get(location).get("state"))){
						Toast.makeText(AtepsView.this.context, "该步骤已提交不能填写", Toast.LENGTH_LONG).show();
					}else{
						for(int i=0;i<AtepsView.this.data.size();++i){
							
							LinearLayout llGroup=(LinearLayout) AtepsView.this.data.get(i).get("llGroup");
							if(i==location){
								theForm.setGroupidShow((String) AtepsView.this.data.get(i).get("groupid"));
								llGroup.setVisibility(View.VISIBLE);
							}else{
								llGroup.setVisibility(View.GONE);
							}
						}
						llOut.showNext();
						if(data.get(location).get("startState")!=null&&!data.get(location).get("startState").equals("")){
							TaskFeedBackAsyncTask taskFeedBackAsyncTask = new TaskFeedBackAsyncTask(
									context,
									false,
									false,
									taskNum,
									(String)data.get(location).get("startState"),
									null, tableName,
									taskCategory, null,
									null, null);
							taskFeedBackAsyncTask.execute();
						}
					}
				}else{
					Toast.makeText(AtepsView.this.context, "请按步骤填写", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		
		return v;
	}
	public class AtepsAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if(convertView==null){
				convertView=View.inflate(context, R.layout.ateps_activity_item, null);
			}
			TextView ateps_item_content=(TextView) convertView.findViewById(R.id.ateps_item_content);
			ImageView ateps_item_commit_mark=(ImageView) convertView.findViewById(R.id.ateps_item_commit_mark);
			if("2".equals((String)(data.get(position).get("state")))){
				ateps_item_commit_mark.setImageResource(R.drawable.jiantoulg);
				//ateps_item_commit_mark.setVisibility(View.VISIBLE);
			}else{
				//ateps_item_commit_mark.setVisibility(View.GONE);
				ateps_item_commit_mark.setImageResource(R.drawable.jiantouhs);
			}
			ateps_item_content.setText((String)(data.get(position).get("name")));
			return convertView;
		}
		
	}
	public  void updateList(XmlGuiForm theForm){
		this.data=theForm.getAtapsData();
		atepsAdapter.notifyDataSetChanged();
	}
	public Button getAtapsCommit(){
		return ataps_commit;
	}
	public Button getAteps_back() {
		return ateps_back;
	}
	public void setAteps_back(Button ateps_back) {
		this.ateps_back = ateps_back;
	}
	
}
