package com.movementinsome.app.mytask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;

import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.mytask.adapter.CoordinateAdapter;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.database.vo.CoordinateVO;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.initial.model.Module;

import java.sql.SQLException;
import java.util.List;

public class CoordinateActivity extends ContainActivity {

	private ListView coordinate_list;
	private List<Module> lstModule;// 所有表单配置
	private Module module;
	private List<CoordinateVO> coordinateVOList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coordinate_activity);
		coordinate_list=(ListView) findViewById(R.id.coordinate_list);
		lstModule = AppContext.getInstance().getModuleData();
		for(Module m:lstModule){
			if(Constant.COORDINATE_ID.equals(m.getId())){
				module=m;
				break;
			}
		}
		update();
		MyReceiver myReceiver=new MyReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.COORDINATE_ACTION);
		registerReceiver(myReceiver, filter);
	}
	/*public boolean onKeyDown(int keyCode, KeyEvent event) {
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
	private void update(){
		try {
			Dao<CoordinateVO, Long> coordinateVODao = AppContext.getInstance().getAppDbHelper().getDao(CoordinateVO.class);
			coordinateVOList=coordinateVODao.queryForAll();
			if(coordinateVOList!=null){
				coordinate_list.setAdapter(new CoordinateAdapter(this, coordinateVOList,module));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		String taskNum=data.getStringExtra("taskNum");// 任务编号
		String taskCategory=data.getStringExtra("taskCategory");// 表单类型
		String tableName=data.getStringExtra("tableName");// 表单在数据库中表名
		if(Constant.FROM_REQUEST_CODE==requestCode&&data!=null&&Constant.FROM_RESULT_CODE==resultCode){
			TaskFeedBackAsyncTask taskFeedBackAsyncTask
			=new TaskFeedBackAsyncTask(this, false,false, taskNum, Constant.UPLOAD_STATUS_PAUSE, null, tableName, taskCategory,null,null,null);
		taskFeedBackAsyncTask.execute();
		}
	}

}
