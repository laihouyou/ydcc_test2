package com.movementinsome.sysmanager.init;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.map.nearby.ToastUtils;
import com.movementinsome.sysmanager.init.task.InitDataAsyncTask;

/**
 * 系统数据初始化
 * http://gddst2013.no-ip.org:9016/fisds/rest/
 */
public class SystemInitActivity extends FullActivity implements OnClickListener{

	public TextView sysInitTvMsg;				//更新进度显示
	public TextView sysInitTvUrl;
	public LinearLayout sysInitLinearHint;		//提示说明容器
	public Button sysInitBtnBack;				//返回取消
	public Button sysInitBtnClear;				//清除url
	public Button sysInitBtnInit;				//开始初始化
	
	public EditText sysInitEtUrl;				// url数据
	public EditText sysInitEtUrl_P;				// 端口
	
	public EditText sysInitEtUrl2;				// url数据（备用）
	public EditText sysInitEtUrl_P2;				// 端口 （备用）
	public LinearLayout layout_url2;
	
	private String HTTP="http://";
	private String F=":";
	private String FISDS="/fisds";
	
	public EditText sysInitEtName;				//终端名称
	public InitDataAsyncTask systemInititlize;
	//public InitDataAsyncTaskXJYS initDataAsyncTaskXJYS;
	private String[] strUrl;

	private  Handler handler=new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what==1){
				setInitiali();
			}else if(msg.what==2){
				sysInitBtnInit.setEnabled(false);
				sysInitBtnClear.setEnabled(false);
				sysInitEtUrl.setEnabled(false);
				sysInitEtUrl_P.setEnabled(false);
				sysInitBtnInit.setTextColor(Color.rgb(200, 200, 200));
				sysInitBtnClear.setTextColor(Color.rgb(200, 200, 200));
				sysInitTvMsg.setGravity(Gravity.LEFT);
				sysInitLinearHint.setVisibility(View.VISIBLE);
				setInitiali();
			}else if (msg.what==3){
				ToastUtils.show("请确认备用服务地址是否正确");
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_initialize_activity);

		sysInitEtUrl = (EditText)findViewById(R.id.sysInitEtUrl);
		sysInitEtUrl_P=(EditText)findViewById(R.id.sysInitEtUrl_P);
		
		layout_url2 = (LinearLayout)findViewById(R.id.layout_url2);
		sysInitEtUrl2 = (EditText)findViewById(R.id.sysInitEtUrl2);				// url数据（备用）
		sysInitEtUrl_P2 = (EditText)findViewById(R.id.sysInitEtUrl_P2);				// 端口 （备用）
		
		sysInitEtName = (EditText)findViewById(R.id.sysInitEtName);
		sysInitBtnBack = (Button)findViewById(R.id.sysInitBtnBack);
		sysInitBtnInit = (Button)findViewById(R.id.sysInitBtnInit);
		sysInitBtnClear = (Button)findViewById(R.id.sysInitBtnClear);
		sysInitTvUrl = (TextView)findViewById(R.id.sysInitTvUrl);
		sysInitTvMsg = (TextView)findViewById(R.id.sysInitTvMsg);
		sysInitLinearHint = (LinearLayout)findViewById(R.id.sysInitLinearHint);

		sysInitTvUrl.setOnClickListener(this);
		sysInitBtnBack.setOnClickListener(this);
		sysInitBtnInit.setOnClickListener(this);
		sysInitBtnClear.setOnClickListener(this);
		//flgInit = true;
		
		//MyPublicData.initialiHandler=handler;
		
		strUrl = new String[]{"","",""};
		if (!"".equals(AppContext.getInstance().getIniConnSvr())){
			String []url=AppContext.getInstance().getIniConnSvr().split(F);
			if(url.length==2){
				sysInitEtUrl.setText(url[0]);
				sysInitEtUrl_P.setText(url[1]);
			}
		}
		
		if(sysInitEtUrl.getText().toString().equals(sysInitEtUrl2.getText().toString())
				&&sysInitEtUrl_P.getText().toString().equals(sysInitEtUrl_P2.getText().toString())){
			layout_url2.setVisibility(View.GONE);
		}
		
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.sysInitBtnClear:
			sysInitTvMsg.setGravity(Gravity.CENTER);
			sysInitEtUrl.setText("");
			sysInitEtUrl_P.setText("");
			sysInitTvMsg.setText(null);
			break;
		case R.id.sysInitBtnBack:
			if(systemInititlize != null){
				systemInititlize.cancel(true);
			}
			//flgInit = false;
			//AppContext.getInstance().setSystemInit(false);
			finish();
			break;
		case R.id.sysInitBtnInit:
			strUrl[0]= sysInitEtUrl.getText().toString();
			if(strUrl[0].equals("")){
				pToastShow("服务地址不能为空");
				return;
			}
			strUrl[1] = AppContext.getInstance().getPhoneIMEI();
			strUrl[2] = sysInitEtName.getText().toString();
			AppContext.getInstance().setIniConnSvr(strUrl[0]+F+sysInitEtUrl_P.getText().toString());
			strUrl[0]=HTTP+strUrl[0]+F+sysInitEtUrl_P.getText().toString()+FISDS;

			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (null == AppContext.getInstance().checkUrl(strUrl[0]+"/rest")){
						if(!sysInitEtUrl2.getText().toString().equals("")){
							strUrl[0]= sysInitEtUrl2.getText().toString();
							strUrl[0]=HTTP+strUrl[0]+F+sysInitEtUrl_P2.getText().toString()+FISDS;
							if(null == AppContext.getInstance().checkUrl(strUrl[0]+"/rest")){
//								ToastUtils.show("请确认备用服务地址是否正确");
								handler.sendEmptyMessage(3);
							}else{
								handler.sendEmptyMessage(2);
							}
						}else{
//							ToastUtils.show("请确认服务地址是否正确");
							handler.sendEmptyMessage(3);
						}
					}else{
						handler.sendEmptyMessage(2);
					}
				}
			}).start();
			
			break;
		case R.id.sysInitTvUrl:
			sysInitEtUrl.setText("gddst2013.no-ip.org");
			sysInitEtUrl_P.setText("9018");
			break;

		default:
			break;
		}
	}


	
	private  void setInitiali(){
		try{
			sysInitEtName.getText().toString();
			systemInititlize = new InitDataAsyncTask(this,sysInitBtnInit,sysInitBtnClear);
			systemInititlize.execute(strUrl);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}


	private void dialogShow(){
		new AlertDialog.Builder(this)
		.setTitle("提示")
		.setIcon(android.R.drawable.ic_menu_help)
		.setMessage("您还没有给你的终端创建名称,是否继续初始化?")
		.setNegativeButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			//	setInitiali();
				sysInitBtnInit.setEnabled(false);
				sysInitBtnClear.setEnabled(false);
				sysInitEtUrl.setEnabled(false);
				sysInitEtUrl_P.setEnabled(false);
				sysInitBtnInit.setTextColor(Color.rgb(200, 200, 200));
				sysInitBtnClear.setTextColor(Color.rgb(200, 200, 200));
				sysInitTvMsg.setGravity(Gravity.LEFT);
				sysInitLinearHint.setVisibility(View.VISIBLE);
				//String url=strUrl[0];
				//检查是否有最新版本
				//new UpdateApk(SystemInitActivity.this, false,url,true);
			}
		})
		.setPositiveButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		}).show();
	}

}
