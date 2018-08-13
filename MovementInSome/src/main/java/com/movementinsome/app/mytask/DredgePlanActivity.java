package com.movementinsome.app.mytask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.mytask.handle.PublicHandle;
import com.movementinsome.app.server.TaskFeedBackAsyncTask;
import com.movementinsome.app.server.UploadDataTask;
import com.movementinsome.database.vo.InsDredgePTask;
import com.movementinsome.database.vo.InsDredgeWTask;
import com.movementinsome.database.vo.InsTablePushTaskVo;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.util.MyDateTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.Date;

public class DredgePlanActivity extends FullActivity implements OnClickListener,PublicHandle {

	private ImageView dredge_plan_back;// 返回
	private Button dredge_plan_commit;// 提交
	private TextView dredge_plan_ysjcj_msg;// 雨水检查井(口):
	private TextView dredge_plan_wsjcj_msg;// 污水检查井(口):
	private TextView dredge_plan_ysk_msg;// 雨水口(口):
	private TextView dredge_plan_yskgqct_msg;// 雨水管渠尺寸
	private TextView dredge_plan_ysgqc_msg;// 雨水管渠长(m):
	private TextView dredge_plan_wsgqct_msg;// 污水管渠尺寸:
	private TextView dredge_plan_wsgqc_msg;//污水管渠长(m):
	
	private EditText dredge_plan_ysjcj;// 雨水检查井(口):
	private EditText dredge_plan_wsjcj;// 污水检查井(口):
	private EditText dredge_plan_ysk;// 雨水口(口):
	private EditText dredge_plan_yskgqct;// 雨水管渠尺寸
	private EditText dredge_plan_ysgqc;// 雨水管渠长(m):
	private EditText dredge_plan_wsgqct;// 污水管渠尺寸:
	private EditText dredge_plan_wsgqc;// 污水管渠长(m):
	private EditText dredge_plan_ynl;// 淤泥量(m3):
	private EditText dredge_plan_cyry;// 参加人员:
	
	private InsDredgePTask insDredgePTask;// 下载数据
	private InsTablePushTaskVo insTablePushTaskVo;// 推送消息
	private InsDredgeWTask insDredgeWTask=new InsDredgeWTask();// 上传数据
	
	private Button dredge_plan_ysjcj_zy;// 雨水检查井(口):增一
	private Button dredge_plan_ysjcj_jy;// 雨水检查井(口):减一
	private Button dredge_plan_parent_zy;// 污水检查井(口):增一
	private Button dredge_plan_parent_jy;// 污水检查井(口):减一
	private Button dredge_plan_ysk_zy;// 雨水口(口):增一
	private Button dredge_plan_ysk_jy;// 雨水口(口):减一
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dredge_plan_activity);
		init();
		setData();
	}
	
	private void init(){
		insDredgePTask = (InsDredgePTask) getIntent().getSerializableExtra("insDredgePTask");
		insTablePushTaskVo = (InsTablePushTaskVo) getIntent().getSerializableExtra("insTablePushTaskVo");
		dredge_plan_back=(ImageView) findViewById(R.id.dredge_plan_back);// 返回
		dredge_plan_back.setOnClickListener(this);
		dredge_plan_commit=(Button) findViewById(R.id.dredge_plan_commit);// 提交
		dredge_plan_commit.setOnClickListener(this);
		dredge_plan_ysjcj_msg=(TextView) findViewById(R.id.dredge_plan_ysjcj_msg);// 雨水检查井(口):
		dredge_plan_wsjcj_msg=(TextView) findViewById(R.id.dredge_plan_wsjcj_msg);// 污水检查井(口):
		dredge_plan_ysk_msg=(TextView) findViewById(R.id.dredge_plan_ysk_msg);// 雨水口(口):
		dredge_plan_yskgqct_msg=(TextView) findViewById(R.id.dredge_plan_yskgqct_msg);// 雨水管渠尺寸
		dredge_plan_ysgqc_msg=(TextView) findViewById(R.id.dredge_plan_ysgqc_msg);// 雨水管渠长(m):
		dredge_plan_wsgqct_msg=(TextView) findViewById(R.id.dredge_plan_wsgqct_msg);// 污水管渠尺寸:
		dredge_plan_wsgqc_msg=(TextView) findViewById(R.id.dredge_plan_wsgqc_msg);//污水管渠长(m):
		
		dredge_plan_ysjcj=(EditText) findViewById(R.id.dredge_plan_ysjcj);// 雨水检查井(口):
		dredge_plan_wsjcj=(EditText) findViewById(R.id.dredge_plan_wsjcj);// 污水检查井(口):
		dredge_plan_ysk=(EditText) findViewById(R.id.dredge_plan_ysk);// 雨水口(口):
		dredge_plan_yskgqct=(EditText) findViewById(R.id.dredge_plan_yskgqct);// 雨水管渠尺寸
		dredge_plan_ysgqc=(EditText) findViewById(R.id.dredge_plan_ysgqc);// 雨水管渠长(m):
		dredge_plan_wsgqct=(EditText) findViewById(R.id.dredge_plan_wsgqct);// 污水管渠尺寸:
		dredge_plan_wsgqc=(EditText) findViewById(R.id.dredge_plan_wsgqc);// 污水管渠长(m):
		
		dredge_plan_ynl=(EditText) findViewById(R.id.dredge_plan_ynl);// 淤泥量(m3):
		dredge_plan_cyry=(EditText) findViewById(R.id.dredge_plan_cyry);// 参加人员:
		
		dredge_plan_ysjcj_zy=(Button) findViewById(R.id.dredge_plan_ysjcj_zy);// 雨水检查井(口):增一
		dredge_plan_ysjcj_zy.setOnClickListener(this);
		dredge_plan_ysjcj_jy=(Button) findViewById(R.id.dredge_plan_ysjcj_jy);// 雨水检查井(口):减一
		dredge_plan_ysjcj_jy.setOnClickListener(this);
		dredge_plan_parent_zy=(Button) findViewById(R.id.dredge_plan_parent_zy);// 污水检查井(口):增一
		dredge_plan_parent_zy.setOnClickListener(this);
		dredge_plan_parent_jy=(Button) findViewById(R.id.dredge_plan_parent_jy);// 污水检查井(口):减一
		dredge_plan_parent_jy.setOnClickListener(this);
		dredge_plan_ysk_zy=(Button) findViewById(R.id.dredge_plan_ysk_zy);// 雨水口(口):增一
		dredge_plan_ysk_zy.setOnClickListener(this);
		dredge_plan_ysk_jy=(Button) findViewById(R.id.dredge_plan_ysk_jy);// 雨水口(口):减一
		dredge_plan_ysk_jy.setOnClickListener(this);
	}
	private void setData(){
		if(insDredgePTask!=null){
			
			dredge_plan_ysjcj_msg.setText("雨水检查井(口): 总数"+getNullLong(insDredgePTask.getRainWellSum())+"口，已检"+getNullLong(insDredgePTask.getRainWellSumRl())+"口");// 雨水检查井(口):
			dredge_plan_wsjcj_msg.setText("污水检查井(口): 总数"+getNullLong(insDredgePTask.getSewageWellSum())+"口，已检"+getNullLong(insDredgePTask.getSewageWellSumRl())+"口");// 污水检查井(口):
			dredge_plan_ysk_msg.setText("雨水口(口): 总数"+getNullLong(insDredgePTask.getGulliesSum())+"口，已检"+getNullLong(insDredgePTask.getGulliesSumRl())+"口");// 雨水口(口):
			
			dredge_plan_ysgqc_msg.setText("雨水管渠长(m): 总数"+getNullLong(insDredgePTask.getGulliesLen())+"m，已检"+getNullLong(insDredgePTask.getGulliesLenRl())+"m");// 雨水管渠长(m):
			
			dredge_plan_wsgqc_msg.setText("污水管渠长(m): 总数"+getNullLong(insDredgePTask.getSewerLen())+"m，已检"+getNullLong(insDredgePTask.getSewerLenRl())+"m");//污水管渠长(m):
			dredge_plan_wsgqct.setText(insDredgePTask.getSewerSize());// 污水管渠尺寸:
			dredge_plan_yskgqct.setText(insDredgePTask.getGulliesSize());// 雨水管渠尺寸
			
			dredge_plan_ysjcj.setText(getNullLong(insDredgePTask.getRainWellSumNow())+"");// 雨水检查井(口):
			
			dredge_plan_wsjcj.setText(getNullLong(insDredgePTask.getSewageWellSumNow())+"");// 污水检查井(口):
		
			dredge_plan_ysk.setText(getNullLong(insDredgePTask.getGulliesSumNow())+"");// 雨水口(口):
			
			dredge_plan_ysgqc.setText(getNullLong(insDredgePTask.getGulliesLenNow())+"");// 雨水管渠长(m):
			
			dredge_plan_wsgqc.setText(getNullLong(insDredgePTask.getSewerLenNow())+"");// 污水管渠长(m):
			
			dredge_plan_ynl.setText(getNullLong(insDredgePTask.getSludgeSum())+"");//淤泥量
			if(insDredgePTask.getWorkers()!=null){
				dredge_plan_cyry.setText(insDredgePTask.getWorkers());
			}
		}
	}
	private void saveDate(){
		if(insDredgePTask!=null){
			if(!"".equals(dredge_plan_ysjcj.getText()+"")){
				insDredgePTask.setRainWellSumNow(Long.parseLong(dredge_plan_ysjcj.getText()+""));// 雨水检查井(口):
			}
			if(!"".equals(dredge_plan_wsjcj.getText()+"")){
				insDredgePTask.setSewageWellSumNow(Long.parseLong(dredge_plan_wsjcj.getText()+""));// 污水检查井(口):
			}
			if(!"".equals(dredge_plan_ysk.getText()+"")){
				insDredgePTask.setGulliesSumNow(Long.parseLong(dredge_plan_ysk.getText()+""));// 雨水口(口):
			}
			
			if(!"".equals(dredge_plan_ysgqc.getText()+"")){
				insDredgePTask.setGulliesLenNow(Long.parseLong(dredge_plan_ysgqc.getText()+""));// 雨水管渠长(m):
			}
			
			if(!"".equals(dredge_plan_wsgqc.getText()+"")){
				insDredgePTask.setSewerLenNow(Long.parseLong(dredge_plan_wsgqc.getText()+""));// 污水管渠长(m):
			}
			if(!"".equals(dredge_plan_ynl.getText()+"")){
				insDredgePTask.setSludgeSum(Long.parseLong(dredge_plan_ynl.getText()+""));//淤泥量
			}
			
			insDredgePTask.setWorkers(dredge_plan_cyry.getText()+"");//参加人员
			try {
				Dao<InsDredgePTask, Long> insDredgePTaskDao = AppContext
				.getInstance().getAppDbHelper()
				.getDao(InsDredgePTask.class);
				insDredgePTaskDao.update(insDredgePTask);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TaskFeedBackAsyncTask taskFeedBackAsyncTask=new TaskFeedBackAsyncTask(this
				, false,false, insTablePushTaskVo.getTaskNum(), Constant.UPLOAD_STATUS_PAUSE,
				null, insTablePushTaskVo.getTitle(), insTablePushTaskVo.getTaskCategory(),null,null,null);
		taskFeedBackAsyncTask.execute();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dredge_plan_back:
			saveDate();
			finish();
			break;
		case R.id.dredge_plan_commit:
			commitDialog();
			break;
		case R.id.dredge_plan_ysjcj_zy:// 雨水检查井(口):增一
			dredge_plan_ysjcj.setText((getNullLong(dredge_plan_ysjcj.getText()+"")+1)+"");
			break;
		case R.id.dredge_plan_ysjcj_jy:// 雨水检查井(口):减一
			dredge_plan_ysjcj.setText(((getNullLong(dredge_plan_ysjcj.getText()+"")-1)<0?0:(getNullLong(dredge_plan_ysjcj.getText()+"")-1))+"");
			break;
		case R.id.dredge_plan_parent_zy:// 污水检查井(口):增一
			dredge_plan_wsjcj.setText((getNullLong(dredge_plan_wsjcj.getText()+"")+1)+"");
			break;		
		case R.id.dredge_plan_parent_jy:// 污水检查井(口):减一
			dredge_plan_wsjcj.setText(((getNullLong(dredge_plan_wsjcj.getText()+"")-1)<0?0:(getNullLong(dredge_plan_wsjcj.getText()+"")-1))+"");
			break;
		case R.id.dredge_plan_ysk_zy:// 雨水口(口):增一
			dredge_plan_ysk.setText((getNullLong(dredge_plan_ysk.getText()+"")+1)+"");
			break;
		case R.id.dredge_plan_ysk_jy:// 雨水口(口):减一
			dredge_plan_ysk.setText(((getNullLong(dredge_plan_ysk.getText()+"")-1)<0?0:(getNullLong(dredge_plan_ysk.getText()+"")-1))+"");
			break;
		default:
			break;
		}
		
	}
	private void commitDialog(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setIcon(android.R.drawable.ic_menu_help)
		.setMessage("确定要提交数据")
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		})
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				commit();
			}
		}).show();
	}
	private void commit(){
		if(!"".equals(dredge_plan_ysjcj.getText()+"")){
			
			insDredgeWTask.setRainWellSum(Long.parseLong(dredge_plan_ysjcj.getText()+""));// 雨水检查井数量
		}
		if(!"".equals(dredge_plan_wsjcj.getText()+"")){// 污水检查井(口):
			
			insDredgeWTask.setSewageWellSum(Long.parseLong(dredge_plan_wsjcj.getText()+""));//污水检查井数量
		}
		if(!"".equals(dredge_plan_ysk.getText()+"")){// 雨水口(口):
			
			insDredgeWTask.setGulliesSum(Long.parseLong(dredge_plan_ysk.getText()+""));//雨水口数量
		}	
		if(!"".equals(dredge_plan_ysgqc.getText()+"")){// 雨水管渠长(m):
			
			insDredgeWTask.setGulliesLen(Long.parseLong(dredge_plan_ysgqc.getText()+""));//雨水管渠长
		}
		if(!"".equals(dredge_plan_wsgqc.getText()+"")){
			
			insDredgeWTask.setSewerLen(Long.parseLong(dredge_plan_wsgqc.getText()+""));//污水管渠长
		}
		insDredgeWTask.setGulliesSize(dredge_plan_yskgqct.getText()+"");//雨水管渠尺寸
		insDredgeWTask.setSewerSize(dredge_plan_wsgqct.getText()+"");//污水管渠尺寸
		insDredgeWTask.setWorkers(dredge_plan_cyry.getText()+"");//参加人员
		if(!"".equals(dredge_plan_ynl.getText()+"")){
			insDredgeWTask.setSludgeSum(Long.parseLong(dredge_plan_ynl.getText()+""));//淤泥量
		}
		if(insDredgePTask!=null){
			insDredgeWTask.setDpNum(insDredgePTask.getSerialNumber());//计划清疏编号
			insDredgeWTask.setRainWellSumRl(getNullLong(insDredgePTask.getRainWellSumRl())+getNullLong(insDredgeWTask.getRainWellSum()));//雨水检查井数量
			insDredgeWTask.setSewageWellSumRl(getNullLong(insDredgePTask.getSewageWellSumRl())+getNullLong(insDredgeWTask.getSewageWellSum()));//污水检查井数量
			insDredgeWTask.setGulliesSumRl(getNullLong(insDredgePTask.getGulliesSumRl())+getNullLong(insDredgeWTask.getGulliesSum()));//雨水口数量
			//insDredgeWTask.setGulliesSizeRl(gulliesSizeRl);//雨水管渠尺寸
			insDredgeWTask.setGulliesLenRl(getNullLong(insDredgePTask.getGulliesLenRl())+getNullLong(insDredgeWTask.getGulliesLen()));//雨水管渠长
			insDredgeWTask.setSewerLenRl(getNullLong(insDredgePTask.getSewerLenRl())+getNullLong(insDredgeWTask.getSewerLen()));//污水管渠长
			//insDredgeWTask.setSewerSizeRl(sewerSizeRl);//污水管渠尺寸
			if((getNullLong(insDredgePTask.getSewerLen())<=getNullLong(insDredgeWTask.getSewerLenRl()))// 污水管渠长
					&&(getNullLong(insDredgePTask.getGulliesLen())<=getNullLong(insDredgeWTask.getGulliesLenRl()))//雨水管渠长
					&&(getNullLong(insDredgePTask.getGulliesSum())<=getNullLong(insDredgeWTask.getGulliesSumRl()))//雨水口数量
					&&(getNullLong(insDredgePTask.getSewageWellSum())<=getNullLong(insDredgeWTask.getSewageWellSumRl()))//污水检查井数量
					&&(getNullLong(insDredgePTask.getRainWellSum())<=getNullLong(insDredgeWTask.getRainWellSumRl()))//雨水检查井数量
					){
						insDredgeWTask.setState((long) 2);
					}else{
						insDredgeWTask.setState((long) 1);
					}
		}
		
		String content=new Gson().toJson(insDredgeWTask);
		JSONObject ob=new JSONObject();
		try {
			//ob.put("moiNum", this.getId());
			ob.put("imei", AppContext.getInstance().getPhoneIMEI());
			//ob.put("guid", this.getGuid());
			
			ob.put("workTaskNum", insTablePushTaskVo.getTaskNum());
			ob.put("taskCategory", insTablePushTaskVo.getTaskCategory());
			ob.put("tableName","Ins_Dredge_W_Task");
			ob.put("parentTable", insTablePushTaskVo.getTitle());
			
			ob.put("usId", AppContext.getInstance().getCurUser().getUserId());
			ob.put("usUsercode", AppContext.getInstance().getCurUser().getUserName());
			ob.put("usNameZh", AppContext.getInstance().getCurUser().getUserAlias());
			
			ob.put("createDate", MyDateTools.date2String(new Date()));
			
			
			String gpsCoord=null;
			String mapCoord=null;
			if (AppContext.getInstance().getCurLocation()!=null){
				gpsCoord=AppContext.getInstance().getCurLocation().getCurGpsPosition();
				mapCoord=AppContext.getInstance().getCurLocation().getCurMapPosition();
			}
			ob.put("gpsCoord",gpsCoord);
			ob.put("mapCoord",mapCoord);
			ob.put("content",content);
			new UploadDataTask(this,this, ob.toString()).execute();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		// 拦截MENU按钮点击事件，让他无任何操作
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	public Long getNullLong(Long v){
		return v==null?0:v;
	}
	public Long getNullLong(String v){
		return v.equals("")?0:Long.parseLong(v);
	}
	@Override
	public void updateData() {
		// TODO Auto-generated method stub
		Dao<InsDredgePTask, Long> insDredgePTaskDao;
		try {
			
			insDredgePTask.setSewerLenNow((long) 0);//实时污水管渠长
			insDredgePTask.setGulliesLenNow((long) 0);//实时雨水管渠长
			insDredgePTask.setGulliesSumNow((long) 0);//实时雨水口数量
			insDredgePTask.setSewageWellSumNow((long) 0);//实时污水检查井数量
			insDredgePTask.setRainWellSumNow((long) 0);//实时雨水检查井数量
			insDredgePTask.setSludgeSum((long) 0);//淤泥量
			insDredgePTask.setWorkers(null);// 参与人员

			insDredgePTask.setSewerLenRl(insDredgeWTask.getSewerLenRl());//实际污水管渠长
			insDredgePTask.setGulliesLenRl(insDredgeWTask.getGulliesLenRl());//实际雨水管渠长
			insDredgePTask.setGulliesSumRl(insDredgeWTask.getGulliesSumRl());//实际雨水口数量
			insDredgePTask.setSewageWellSumRl(insDredgeWTask.getSewageWellSumRl());//实际污水检查井数量
			insDredgePTask.setRainWellSumRl(insDredgeWTask.getRainWellSumRl());//实际雨水检查井数量
			insDredgePTask.setState(insDredgeWTask.getState());// 状态
			insDredgePTaskDao = AppContext
			.getInstance().getAppDbHelper()
			.getDao(InsDredgePTask.class);
			insDredgePTaskDao.update(insDredgePTask);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish();
	}
}
