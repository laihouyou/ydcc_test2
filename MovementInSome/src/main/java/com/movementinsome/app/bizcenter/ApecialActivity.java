package com.movementinsome.app.bizcenter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.easyform.formengineer.RunForm;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.initial.model.Module;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ApecialActivity extends ContainActivity {

	private GridView _gridViewApp;
	private List<Module> lstModule;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.table_activity);

		lstModule = AppContext.getInstance().getModuleData(this.getMenu().getId());
		_gridViewApp = (GridView) findViewById(R.id.gridViewApp);

		ArrayList<HashMap<String, Object>> lst = new ArrayList<HashMap<String, Object>>();
		for (Module module : lstModule) {
			if (module.isCanwrite()){
				HashMap<String, Object> map = new HashMap<String, Object>();
				if (module.getIcon()!=null&&!module.getIcon().equals("")){
					int resID = this.getResources().getIdentifier(module.getIcon(), "drawable",
							this.getPackageName());
					map.put("itemImage", resID);//this.getResources().getDrawable(resID)
				}else{
					map.put("itemImage", R.drawable.function);
				}
				map.put("itemText", module.getName());
				map.put("intentId", module.getId());
				lst.add(map);

			}
		}
		int x =3-lst.size()%3;
		for(int i=0;i<x;++i){
			lst.add(new HashMap<String, Object>());
		}
		SimpleAdapter adpter = new SimpleAdapter(this, lst,
				R.layout.layout_gridview_item, new String[] { "itemImage",
						"itemText" }, new int[] { R.id.imageView_ItemImage,
						R.id.textView_ItemText });

		_gridViewApp.setAdapter(adpter);

		_gridViewApp.setOnItemClickListener(new gridViewClickListener());

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// pToastConcel();
	}

	class gridViewClickListener implements OnItemClickListener {

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			Object obj = _gridViewApp.getAdapter().getItem(arg2);
			HashMap<String, Object> map = (HashMap<String, Object>) obj;
			String str = (String) map.get("itemText");

			String id = (String) map.get("intentId");
			Module module = isXMLForm(id);
			if (module!=null){
				//
				Intent newFormInfo = new Intent(ApecialActivity.this,RunForm.class);
        		newFormInfo.putExtra("template", module.getTemplate());
        		if("false".equals(module.getIsShowDeleteBnt())){
        			newFormInfo.putExtra("isShowDeleteBnt", false);
        		}
        		/*if (params != null)
        			newFormInfo.putExtra("iParams", params);    	*/		
        		startActivity(newFormInfo);
			}else{
				try {
					startActivity(new Intent(id));
				} catch (ActivityNotFoundException ex) {
					Toast.makeText(getApplicationContext(), "该功能未授权使用，请联系软件提供商！", 0).show();
				}
			}

		}
		
		public Module isXMLForm(String id){
			for(Module module:lstModule){
				if (id.equals(module.getId())){
					if ("XML".equalsIgnoreCase(module.getFormtype())){
						return module;
					}else{
						return null;
					}
				}
			}
			return null;
		}

	}
}
