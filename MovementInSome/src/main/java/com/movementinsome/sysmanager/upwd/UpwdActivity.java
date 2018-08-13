package com.movementinsome.sysmanager.upwd;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.util.MD5;

import org.apache.http.ParseException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class UpwdActivity extends FullActivity implements OnClickListener{
	private Button upwd_back;// 返回
	private Button upwd_commit;// 提交
	private TextView et_name;
	private EditText et_pass;
	private EditText et_xpass;
	private EditText et_qdpass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.upwd_activity);
		upwd_back=(Button) findViewById(R.id.upwd_back);// 返回
		upwd_back.setOnClickListener(this);
		upwd_commit=(Button) findViewById(R.id.upwd_commit);// 提交
		upwd_commit.setOnClickListener(this);
		
		et_name=(TextView) findViewById(R.id.et_name);
		et_pass=(EditText) findViewById(R.id.et_pass);
		et_xpass=(EditText) findViewById(R.id.et_xpass);
		et_qdpass=(EditText) findViewById(R.id.et_qdpass);
		
		et_name.setText(AppContext.getInstance().getCurUserName());
	}
	
	public class UpwdAsyncTask  extends AsyncTask<String, Void, String> {

		ProgressDialog progress;
		Context context;
		private String IMEI;
		private String username;
		private String pad;
		private String xpad;
		private String qdpad;
		private String pss;
		public UpwdAsyncTask(Context context,String IMEI,String username,String pad,String xpad,String qdpad,String pss){
			this.context=context;
			this.IMEI=IMEI;
			this.username=username;
			this.pad=pad;
			this.xpad=xpad;
			this.qdpad=qdpad;
			this.pss=pss;
			progress = new ProgressDialog(context);
			progress.setMessage("正在修改,请等待……");
			progress.setCancelable(false);
			progress.setCanceledOnTouchOutside(false);
			progress.show();
		}
		
		
		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			/*String url = AppContext.getInstance().getServerUrl()+MyPublicData.PASSWORD;
			url+="new_password="+pss+"&usernum="+username+"&old_password="+pad+"&imei="+IMEI+"";
			try {
				return HttpUtil.postRequest(url, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;*/
			try {
				return SpringUtil.userPwd(AppContext.getInstance().getServerUrl(), pss, username, pad, IMEI);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		@Override
		protected void onPostExecute(String result) {
			
			if(result==null){
				Toast.makeText(context, "修改失败", Toast.LENGTH_LONG).show();
			}else{
				JSONObject resultOb;
				try {
					resultOb = new JSONObject(result);
					String code=resultOb.getString("code");
					if("1".equals(code)){
						Toast.makeText(context, resultOb.getString("msg"), Toast.LENGTH_LONG).show();
						finish();
					}else {
						Toast.makeText(context, resultOb.getString("msg"), Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			if (progress != null)
				progress.dismiss();
		}
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.upwd_back:
			finish();
			break;
		case R.id.upwd_commit:
			String username=et_name.getText().toString();
	    	String pad=MD5.getInstance().getMD5ofStr(et_pass.getText().toString()).toLowerCase();
	    	String xpad=et_xpass.getText().toString();
	    	String qdpad=et_qdpass.getText().toString();
	    	if("".equals(xpad)){
	    		Toast.makeText(this, "新密码不能为空", Toast.LENGTH_LONG).show();
	    		break;
	    	}
			if(!xpad.equals(qdpad)){
				Toast.makeText(this, "新密码与确定密码不同", Toast.LENGTH_LONG).show();
				break;
			}
			String pss = MD5.getInstance().getMD5ofStr(xpad).toLowerCase();
				  
			UpwdAsyncTask asyncTask = new UpwdAsyncTask(this,AppContext.getInstance().getPhoneIMEI(), username, pad, xpad, qdpad, pss);
			asyncTask.execute();
			break;

		default:
			break;
		}
	}

}
