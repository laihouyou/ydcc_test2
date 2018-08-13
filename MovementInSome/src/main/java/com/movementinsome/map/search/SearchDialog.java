package com.movementinsome.map.search;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable.Factory;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.esri.core.geometry.Point;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.map.search.adapter.SearchInfoAdapter;
import com.movementinsome.map.search.asynctask.SearchAddressAsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchDialog extends FullActivity implements OnClickListener{
	
	private ImageView searchImageBack;		//返回
	private Button searchBtnSearch;			//搜索
	private ExpandableListView searchExListView;
	private AutoCompleteTextView searchEtAdders;		//搜索地址
	private String[] items;
	private List<String> group;           //组列表
	private List<List<JSONObject>> child;     //子列表
	private SearchInfoAdapter adapterListView = null;
	private JSONObject jsonObject ;	//转换消息值为 Jsonobject格式

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity);
		searchBtnSearch = (Button)findViewById(R.id.searchBtnSearch);
		searchImageBack = (ImageView)findViewById(R.id.searchImageBack);
		searchExListView = (ExpandableListView)findViewById(R.id.searchExListView);
		searchEtAdders = (AutoCompleteTextView)findViewById(R.id.searchEtAdders);
		searchBtnSearch.setOnClickListener(this);
		searchImageBack.setOnClickListener(this);
		items = new String[]{""};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
				android.R.layout.simple_dropdown_item_1line,items == null?new String[]{""}:items);
		searchEtAdders.setAdapter(adapter);
		searchEtAdders.setThreshold(1);
		searchEtAdders.setFocusable(true);
		searchEtAdders.setEditableFactory(new Factory());
		searchEtAdders.setEnabled(true);

		group = new ArrayList<String>();
		child = new ArrayList<List<JSONObject>>();
		adapterListView = new SearchInfoAdapter(this, group, child);
		searchExListView.setCacheColorHint(0);  //设置拖动列表的时候防止出现黑色背景
		searchExListView.setAdapter(adapterListView);
		searchExListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				try {
					JSONObject json = new JSONObject(child.get(groupPosition).get(childPosition).getString("center"));
					double longitude = Double.parseDouble(json.getString("x").toString());
					double latitude = Double.parseDouble(json.getString("y").toString());
					Point point = new Point(longitude,latitude);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return false;
			}
		});
          getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE); 
               InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.showSoftInput(searchEtAdders, 0); //显示软键盘
                                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //显示软键盘

	}



	/**
	 * 第一：输入道路名搜索
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.searchBtnSearch://搜索
			searchAddress(searchEtAdders.getText().toString().trim());
			getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			break;
		case R.id.searchImageBack://返回
			if(group.size() == 0){
				finish();
			}else{
				backDialog();
			}
			break;
		default:
			break;
		}
	}


	/**
	 * 第二：判断格式正确性，开始异步线程查询
	 * @param address
	 */
	private void searchAddress(String address){
//		if(isValidTagAndAlias(address)){
			new SearchAddressAsyncTask(this,this, address);
//		}else{
//			pToastShow("格式错误,只能为数字,或中英文");
//		}
	}


	/**
	 * 第三：得到返回结果，分析显示
	 * @param json
	 * @throws JSONException
	 */
	public void analysisJson(String json){
		if(TextUtils.isEmpty(json)){
			Toast.makeText(this, "没有找到相关数据", Toast.LENGTH_LONG).show();
			return ;
		}
		group.clear();
		child.clear();
		try {
			jsonObject = new JSONObject(json);
			Iterator<?> it = jsonObject.keys();
			String key = "";
			while(it.hasNext()){
				key = it.next().toString();
				JSONArray array = null;
				try {
					array = jsonObject.getJSONArray(key);
				} catch (Exception e) {
					// TODO: handle exception
				}
				if(array != null && array.length() > 0){
					List<JSONObject> childitem = new ArrayList<JSONObject>();
					for(int i=0 ; i < array.length() ; i++){
						childitem.add(array.getJSONObject(i));
						if(childitem.size() > 100)
							break;
					}
					group.add(key);
					child.add(childitem);

				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, "解析异常", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
		adapterListView.notifyDataSetChanged();
	}

	private void backDialog(){
		new AlertDialog.Builder(this).setTitle("退出提示")
		.setMessage("是否放弃当前查询数据，退出到地图显示？")
		.setPositiveButton("取消", null)
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		}).create().show();
	}
}
