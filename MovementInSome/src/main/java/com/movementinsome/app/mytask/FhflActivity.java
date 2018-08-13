package com.movementinsome.app.mytask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.mytask.adapter.TaskListFhplAdapter;
import com.movementinsome.database.vo.DrainageVO;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.initial.model.Module;

import java.sql.SQLException;
import java.util.List;

public class FhflActivity extends ContainActivity implements OnClickListener{

	private ListView fhpl_list;
	private List<DrainageVO> drainageVOList;
	private List<Module> lstModule;// 所有表单配置
	private Module module;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fhpl_activity);
		fhpl_list=(ListView) findViewById(R.id.fhpl_list);
		lstModule = AppContext.getInstance().getModuleData();
		for(Module m:lstModule){
			if(Constant.FLOODFB_ID.equals(m.getId())){
				module=m;
				break;
			}
		}
		update();
		MyReceiver myReceiver=new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.FHPL_ACTION);
		registerReceiver(myReceiver, filter);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	private void update(){
		try {
			Dao<DrainageVO, Long> drainageVODao = AppContext.getInstance().getAppDbHelper().getDao(DrainageVO.class);
			drainageVOList=drainageVODao.queryForAll();
			fhpl_list.setAdapter(new TaskListFhplAdapter(this, drainageVOList,module));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

/*	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}*/
	private final class MyReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			update();
		}
		
	}
	
}
