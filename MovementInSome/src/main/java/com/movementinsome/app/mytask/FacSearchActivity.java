package com.movementinsome.app.mytask;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.mytask.adapter.DeviceSearchAdapter;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.database.vo.BsFacInfo;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.Module;
import com.movementinsome.kernel.util.JsonAnalysisUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FacSearchActivity extends FullActivity implements OnClickListener ,OnCheckedChangeListener{
	
	private EditText device_edit_search;
	private Button device_btn_search;
	private Button device_btn_search_code;// 二维码
	private ListView device_search_lv;
	private List<BsFacInfo> listMsg;
	private DeviceSearchAdapter adapter;
	private TextView search_result;
    private RadioGroup deviceSearch_rg;
    private RadioButton deviceSearch_bsIpsNum;
    private RadioButton deviceSearch_bsFacNum;
	private boolean isIpsNum = true; //判断选中的是哪个按钮
	// 所有表单配置
	private List<Module> lstModule;
	private List<Module> taskModule;
    
	private String inputNum;//在输入框中输入的内容
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fac_search_activity);
		initViews();
	}

	private void initViews() {
		lstModule = AppContext.getInstance().getModuleData();
		taskModule=new ArrayList<Module>();
		for(Module m:lstModule){
			if(Constant.BIZ_FAC_MSG_FORM.equals(m.getId())){
				taskModule.add(m);
			}
		}
		deviceSearch_bsIpsNum=(RadioButton) findViewById(R.id.deviceSearch_bsIpsNum);
	    deviceSearch_bsFacNum=(RadioButton) findViewById(R.id.deviceSearch_bsFacNum);
		device_edit_search = (EditText) findViewById(R.id.device_edit_search);
		device_btn_search = (Button) findViewById(R.id.device_btn_search);
		device_search_lv = (ListView) findViewById(R.id.device_search_lv);
		search_result = (TextView) findViewById(R.id.search_result);
		deviceSearch_rg = (RadioGroup) findViewById(R.id.deviceSearch_rg);
		device_btn_search_code=(Button) findViewById(R.id.device_btn_search_code);// 二维码
		deviceSearch_rg.setOnCheckedChangeListener(this);
		device_btn_search.setOnClickListener(this);
		device_btn_search_code.setOnClickListener(this);
		
		listMsg = new ArrayList<BsFacInfo>();
	}
	
	public String getData(){
		JSONObject ob=new JSONObject();
		try {
			if(isIpsNum){
				ob.put("bsIpsNum", inputNum);
			}else{
				ob.put("bsFacNum", inputNum);
			}
			ob.put("tableName", "BS_FAC_INFO");
			ob.put("imei", AppContext.getInstance().getPhoneIMEI());
			ob.put("usUsercode", AppContext.getInstance().getCurUser().getUserName());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		return ob.toString();

	}
	//解析从后台获取的JSON数组
	public void parseJSON(String jsonData){
		try{
			JSONArray jsonArray = new JSONArray(jsonData);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String bsFacNum = jsonObject.getString("bsFacNum");
				String bsFacName = jsonObject.getString("bsFacName");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//点击查询按钮后触发的事件
	@Override
	public void onClick(View v) {
		
		if(v.getId()== R.id.device_btn_search){
			inputNum = device_edit_search.getText().toString();
			new SearchTask().execute();
		}else if(v.getId()== R.id.device_btn_search_code){
			Intent intent =new Intent();
			intent.setClass(this, CaptureActivity.class);
			startActivityForResult(intent, 1);
		}
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (group.getCheckedRadioButtonId()) {
		//巡检点编号
		case R.id.deviceSearch_bsIpsNum:
			device_edit_search.setHint("请输入"+deviceSearch_bsIpsNum.getText());
			isIpsNum=true;
			break;
		//设施编号	
		case R.id.deviceSearch_bsFacNum:
			device_edit_search.setHint("请输入"+deviceSearch_bsFacNum.getText());
			isIpsNum = false;
			break;

		default:
			break;
		}
	}
	private class SearchTask  extends AsyncTask<String, Void, String>{

		private ProgressDialog progre;		//进度条显示
		public SearchTask(){
			listMsg.removeAll(listMsg);
			progre = new ProgressDialog(FacSearchActivity.this);
			progre.setCancelable(false);
			progre.setCanceledOnTouchOutside(false);
			progre.setMessage("正在搜索数据,耐心等候……");
			progre.show();
		}
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String url = AppContext.getInstance().getServerUrl();
			url += SpringUtil._REST_TASKDOWN;
			return SpringUtil.postData(url,getData());
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(progre != null)
				progre.dismiss();
			if(result==null){
				Toast.makeText(FacSearchActivity.this, "服务器繁忙，请稍后再搜！", Toast.LENGTH_LONG).show();
			}else{
				JSONObject downloadObject;
					try {
						downloadObject = new JSONObject(result);
						String content0 = (String) downloadObject.get("content");
						String tableName = downloadObject.getString("tableName");
						
						String code = downloadObject.getString("code");
						if("1".equals(code)){
							if(!"".equals(content0)){
								JSONArray content = new JSONArray(content0);
								if (content != null && content.length() > 0) {
									for (int i = 0; i < content.length(); ++i) {
										BsFacInfo bsFacInfo=new BsFacInfo();
										JsonAnalysisUtil
										.setJsonObjectData(
												content.getJSONObject(i),
												bsFacInfo);
										listMsg.add(bsFacInfo);
									}
									search_result.setVisibility(View.VISIBLE);
									device_search_lv.setVisibility(View.VISIBLE);
								}else{
									Toast.makeText(FacSearchActivity.this, "查询数据为空，请重新输入", Toast.LENGTH_LONG).show();
								}
							}else{
								Toast.makeText(FacSearchActivity.this, "查询数据为空，请重新输入", Toast.LENGTH_LONG).show();
							}
						}else{
							Toast.makeText(FacSearchActivity.this, "查询失败", Toast.LENGTH_LONG).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
			}
			adapter = new DeviceSearchAdapter(FacSearchActivity.this,listMsg,taskModule);
			device_search_lv.setAdapter(adapter);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			Bundle bundle =data.getBundleExtra("bundle");
			if(bundle!=null){
				device_edit_search.setText(bundle.getString("result"));
				inputNum = device_edit_search.getText().toString();
				new SearchTask().execute();
			}
		}
	}
}
