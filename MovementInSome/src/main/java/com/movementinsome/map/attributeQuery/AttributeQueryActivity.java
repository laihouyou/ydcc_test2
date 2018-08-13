package com.movementinsome.map.attributeQuery;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.esri.core.map.FeatureSet;
import com.esri.core.tasks.ags.query.Query;
import com.esri.core.tasks.ags.query.QueryTask;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.initial.model.Field;
import com.movementinsome.kernel.initial.model.Ftlayer;
import com.movementinsome.kernel.initial.model.Mapservice;
import com.movementinsome.kernel.util.WebAccessTools.NET_STATE;

import java.util.List;

public class AttributeQueryActivity extends Activity implements OnClickListener{
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.attribute_query_activity);
		
		ftlayers = AppContext.getInstance().getMapParam().getFtlayers();
		layerTypeList = new String[ftlayers.size()];
		
		for(int i=0;i<ftlayers.size();++i){
			layerTypeList[i] = ftlayers.get(i).getName();
		}
		attribute_query_value_clear = (Button)findViewById(R.id.attribute_query_value_clear);
		attribute_query_value_clear.setOnClickListener(this);
		attribute_query_search = (Button)findViewById(R.id.attribute_query_search);
		attribute_query_search.setOnClickListener(this);
		attribute_query_value=(EditText)findViewById(R.id.attribute_query_value);
		attribute_query_listview=(ListView)findViewById(R.id.attribute_query_listview);
		attribute_query_finish = (Button) findViewById(R.id.attribute_query_finish);
		attribute_query_finish.setOnClickListener(this);
		// 查询类型
		attribute_query_type = (Spinner) findViewById(R.id.attribute_query_type);
		attribute_query_type.setAdapter(new ArrayAdapter<String>(this, 
				R.anim.myspinner,layerTypeList));
		// 查询字段
		attribute_query_field = (Spinner) findViewById(R.id.attribute_query_field);
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
				attribute_query_field.setAdapter(new ArrayAdapter<String>(AttributeQueryActivity.this, 
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
				Toast.makeText(AttributeQueryActivity.this, "请填写查询内容", Toast.LENGTH_LONG).show();
			}
			break;
		case R.id.attribute_query_finish:
			finish();
			break;

		default:
			break;
		}
	}
	public class MyQueryTask extends AsyncTask<String, Void, FeatureSet>{

		private ProgressDialog progressDialog;// 进度条
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(AttributeQueryActivity.this);
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
				if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.NULL){
					urlFeature = mapservice.getForeign()+"/"+ftlayer.getFeatureServerId();
				}else if (AppContext.getInstance().ACCESS_NET_STATE == NET_STATE.FOEIGN ){
					urlFeature = mapservice.getForeign()+"/"+ftlayer.getFeatureServerId();
				}else{
					urlFeature = mapservice.getLocal()+"/"+ftlayer.getFeatureServerId();
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
				progressDialog.dismiss();
				
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println(e);
			}
		}
	}
}
