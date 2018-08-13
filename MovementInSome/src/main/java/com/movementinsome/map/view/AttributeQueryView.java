package com.movementinsome.map.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.initial.model.Field;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.util.WebAccessTools.NET_STATE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 设施属性查询
 * @author zzc
 *
 */
public class AttributeQueryView {

	private Context context;
	private MyMapView view;
	// 清除按钮
	private Button attribute_query_value_clear;
	// 搜索按钮
	private Button attribute_query_search;
	// 查询内容
	private EditText attribute_query_value;
	// 数据列表
	private ListView attribute_query_listview;
	// 关闭
	private Button attribute_query_finish;
	// 查询类型
	private Spinner attribute_query_type;
	// 查询字段
	private Spinner attribute_query_field;
	private String []layerTypeList;
	private List<Ftlayer> ftlayers;
	private Ftlayer ftlayer;
	private List<Field> fields;
	private Field field;
	private PopupWindow mPopupWindow;
	Graphic[] graphicList;
	private ArrayList<Map<String, Object>> identifyDataGs;

	
	public AttributeQueryView(Context context,MyMapView view) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.view = view;

		
		View v=View.inflate(context, R.layout.attribute_query_activity, null);
//		addView(v);
		identifyDataGs = new ArrayList<Map<String,Object>>();
		
		mPopupWindow = new PopupWindow(v, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		
		mPopupWindow.setTouchable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(),
				(Bitmap) null));
		mPopupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
		
		ftlayers = AppContext.getInstance().getMapParam().getFtlayers();
		layerTypeList = new String[ftlayers.size()];
		
		for(int i=0;i<ftlayers.size();++i){
			layerTypeList[i] = ftlayers.get(i).getName();
		}
		attribute_query_value_clear = (Button)v.findViewById(R.id.attribute_query_value_clear);
		attribute_query_value_clear.setOnClickListener(myMainBtnClickListener);
		attribute_query_search = (Button)v.findViewById(R.id.attribute_query_search);
		attribute_query_search.setOnClickListener(myMainBtnClickListener);
		attribute_query_value=(EditText)v.findViewById(R.id.attribute_query_value);
		attribute_query_listview=(ListView)view.findViewById(R.id.attribute_query_listview);
		attribute_query_finish = (Button) v.findViewById(R.id.attribute_query_finish);
		attribute_query_finish.setOnClickListener(myMainBtnClickListener);
		// 查询类型
		attribute_query_type = (Spinner) v.findViewById(R.id.attribute_query_type);
		attribute_query_type.setAdapter(new ArrayAdapter<String>(context, 
				R.anim.myspinner,layerTypeList));
		// 查询字段
		attribute_query_field = (Spinner) v.findViewById(R.id.attribute_query_field);
		attribute_query_type.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				ftlayer = ftlayers.get(position);
				fields = ftlayer.getFields();
				String []fieldList = new String[fields.size()];
				for(int i=0;i<fields.size();++i){
					fieldList[i] = fields.get(i).getAlias();
				}
				attribute_query_field.setAdapter(new ArrayAdapter<String>(AttributeQueryView.this.context, 
						R.anim.myspinner,fieldList));
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		attribute_query_field.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				field = fields.get(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		attribute_query_value.addTextChangedListener(new TextWatcher() {
			
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
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if(!"".equals(attribute_query_value.getText()+"")){
					attribute_query_value_clear.setVisibility(View.VISIBLE);
					attribute_query_search.setVisibility(View.VISIBLE);
				}else{
					attribute_query_value_clear.setVisibility(View.GONE);
					attribute_query_search.setVisibility(View.GONE);
				}
			}
		});
	}
		

	
	


	
	



	android.view.View.OnClickListener myMainBtnClickListener = new android.view.View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.attribute_query_value_clear:
				attribute_query_value.setText("");
				break;
				
			case R.id.attribute_query_search:
				if(!"".equals(attribute_query_value.getText()+"")){
					new MyQueryTask().execute(new String[]{field.getName()});
				}else{
					Toast.makeText(context, "请填写查询内容", Toast.LENGTH_LONG).show();
				}
				break;
			case R.id.attribute_query_finish:
				mPopupWindow.dismiss();
				break;
					
				
				default:
					break;
			}
		}
		
	};
	
	public class MyQueryTask extends AsyncTask<String, Void, FeatureSet>{

		private ProgressDialog progressDialog;// 进度条
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(context);
			progressDialog.setCancelable(false);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setMessage("正在查询，请稍后");
			progressDialog.show();
		}
		@Override
		protected FeatureSet doInBackground(String... params) {
			// TODO Auto-generated method stub
			String urlFeature="";
			if(ftlayer!=null){
				Mapservice mapservice = ftlayer.getMapservice();
				String layerId = "";
				if(ftlayer.getLayerIds()!=null){//&&!ftlayer.getLayerIds().contains(",")
					layerId = ftlayer.getLayerIds().split(",")[0];
				}else{
					layerId = ftlayer.getFeatureServerId();
				}
				if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.NULL){
					urlFeature = mapservice.getForeign()+"/"+layerId;
				}else if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.FOEIGN ){
					urlFeature = mapservice.getForeign()+"/"+layerId;
				}else{
					urlFeature = mapservice.getLocal()+"/"+layerId;
				}
			}
			Query query = new Query();
			query.setReturnGeometry(true);
			query.setOutFields(new String[]{"*"});
			query.setWhere(params[0]+" like'%"+attribute_query_value.getText()+"%'");
			QueryTask qTask = new QueryTask(urlFeature);
			FeatureSet fs = null;

			try {
				fs = qTask.execute(query);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return fs;
			}
			return fs;
		}
		@Override
		protected void onPostExecute(FeatureSet result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if(result!=null){
					progressDialog.dismiss();
					mPopupWindow.dismiss();
					graphicList = result.getGraphics();
					if(graphicList!=null&&graphicList.length>0){
						for (int i = 0; i < graphicList.length; i++) {
							Map<String,Object> m = new HashMap<String, Object>();
							m.put("Graphic", graphicList[i]);
							m.put("LayerName", ftlayer.getName());
							m.put("FtLayer", ftlayer);
							identifyDataGs.add(m);
						}
						com.movementinsome.map.view.IdentifyView2 identifyView = new IdentifyView2(context,view,identifyDataGs);
						view.addView(identifyView);
					}else{
						Toast.makeText(context, "查询不到设施", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(context, "查询失败", Toast.LENGTH_LONG).show();
				}
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
		}
	}

}

