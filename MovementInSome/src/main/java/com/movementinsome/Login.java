package com.movementinsome;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.movementinsome.app.server.SpringUtil;
import com.movementinsome.caice.activity.ChangPasswordActivity;
import com.movementinsome.caice.activity.LoginRegisterActivity;
import com.movementinsome.caice.okhttp.OkHttpURL;
import com.movementinsome.database.vo.UserBeanVO;
import com.movementinsome.kernel.activity.FullActivity;
import com.movementinsome.kernel.initial.model.Sub;
import com.movementinsome.kernel.util.DeviceCtrlTools;
import com.movementinsome.kernel.util.MD5;
import com.movementinsome.map.nearby.ToastUtils;
import com.movementinsome.sysmanager.init.SystemInitActivity;
import com.movementinsome.sysmanager.upwd.UpwdActivity;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

/**
 * 登入界面
 */
public class Login extends FullActivity implements View.OnClickListener{
	private EditText mUser; // 帐号编辑框
	private EditText mPassword; // 密码编辑框
	private TextView userRegedit;// 注册
	private TextView loginBtn;// 登录
	private TextView setSystem;// 重新初始化
	private TextView forget_password;// 忘记密码
	private CheckBox remUser;
	private Spinner login_map_spinner;

	private ProgressDialog progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity2);

		mUser = (EditText) findViewById(R.id.login_user_edit);
		mPassword = (EditText) findViewById(R.id.login_passwd_edit);

		userRegedit = (TextView) findViewById(R.id.login_register);
		userRegedit.setOnClickListener(this);
		forget_password = (TextView) findViewById(R.id.forget_password);
		forget_password.setOnClickListener(this);
		loginBtn = (TextView) findViewById(R.id.login_login_btn);
		setSystem = (TextView) findViewById(R.id.user_set_system);

		login_map_spinner = (Spinner) findViewById(R.id.login_map_spinner);
		login_map_spinner.setVisibility(View.GONE);

		remUser = (CheckBox) findViewById(R.id.rem_user);

		// 默认显示登录用户名
		mUser.setText(AppContext.getInstance().getCurUserName());

		// 判断记住密码多选框的状态
		if (AppContext.getInstance().isRember()) {
			// 设置默认是记录密码状态
			remUser.setChecked(true);
			mPassword.setText(AppContext.getInstance().getUserPawword());
//			// 判断自动登陆多选框状态
//			if (AppContext.getInstance().isAutoLogin()) {
//				// 设置默认是自动登录状态
//				autoLogin.setChecked(true);
//				// 跳转界面
//				login_main(loginBtn);
//			}
		}

		// 监听记住密码多选框按钮事件
		remUser.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (remUser.isChecked()) {

					AppContext.getInstance().setRember(true);

				} else {
					AppContext.getInstance().setRember(false);
				}

			}
		});

//		// 监听自动登录多选框事件
//		autoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//			public void onCheckedChanged(CompoundButton buttonView,
//					boolean isChecked) {
//				if (autoLogin.isChecked()) {
//					AppContext.getInstance().setAutoLogin(true);
//				} else {
//					AppContext.getInstance().setAutoLogin(false);
//				}
//			}
//		});

	}

	public void login_main(View v) {
		if (null == AppContext.getInstance().getServerUrl()) {
			new AlertDialog.Builder(Login.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("登录错误").setMessage("无法连接服务器，请检查服务器连接设置或重新初始化！")
					.create().show();
			return;
		}

		if ("?imei".equals(mUser.getText().toString())) {
			mUser.setText(AppContext.getInstance().getPhoneIMEI());
			return;
		}
		if ("".equals(mUser.getText().toString())
				|| "".equals(mPassword.getText().toString())) {
			new AlertDialog.Builder(Login.this)
					.setIcon(
							getResources().getDrawable(
									R.drawable.login_error_icon))
					.setTitle("登录错误").setMessage("帐号或者密码不能为空，\n请输入后再登录！")
					.create().show();
			return;
		}
		String userName = mUser.getText().toString();
		String userPwd = mPassword.getText().toString();

		Map<String, String> map = new HashMap<String, String>();
		map.put("userName", userName);
		map.put("userPwd", userPwd);
		map.put("phoneIMEI", AppContext.getInstance().getPhoneIMEI());
		map.put(getString(R.string.appName), getString(R.string.APP_NAME));

		String url= OkHttpURL.serverUrl+ SpringUtil._REST_USERLOGIN
				+ "uuid=" + map.get("phoneIMEI")
				+"&"
				+getString(R.string.appName)+"=" + map.get(getString(R.string.appName))
				+ "&userName=" + map.get("userName") + "&password=" + MD5.getInstance().getMD5ofStr(map.get("userPwd")).toLowerCase();


		OkHttpUtils.post()
				.url(url)
				.build()
				.execute(new StringCallback() {
					@Override
					public void onBefore(Request request, int id) {
						super.onBefore(request, id);
						if (progress==null){
							progress = new ProgressDialog(Login.this);
							progress.setMessage("正在登录,请等待……");
							progress.setCancelable(false);
							progress.setCanceledOnTouchOutside(false);
							progress.show();
						}else {
							progress.show();
						}
					}
					@Override
					public void onAfter(int id) {
						super.onAfter(id);
						if (progress.isShowing()){
							progress.dismiss();
						}
					}
					@Override
					public void onError(Call call, Exception e, int id) {
						e.printStackTrace();
						ToastUtils.show("登入超时");
					}

					@Override
					public void onResponse(String response, int id) {
						if (response != null) {
							Gson gson = new Gson();
							// 用户属性对象
							UserBeanVO userBean = gson.fromJson(response, UserBeanVO.class);
							if (userBean != null) {
								if ("1".equals(userBean.getState())) {        //登入成功
									// 保留当前登录用户信息
									AppContext.getInstance().setCurUser(userBean);
									if (!login_map_spinner.isShown()) {
										for (Sub sub : AppContext.getInstance().getSolution()) {
											if (sub.getGroupNum().equals(userBean.getConfigFile())) {
												AppContext.getInstance().setDefSolution(
														sub.getId());
												break;
											}
										}
									}
									// 登录成功和记住密码框为选中状态才保存用户信息
									if (remUser.isChecked()) {
										// 记住用户名、密码、
										// Editor editor = sp.edit();
										AppContext.getInstance().setCurUserName(
												mUser.getText().toString());
										AppContext.getInstance().setUserPassword(
												mPassword.getText().toString());
									}

									Intent intent = new Intent();
									intent.setClass(Login.this, Main.class);
									startActivity(intent);
									Login.this.finish();
								} else {
									ToastUtils.show(userBean.getStateStr());
								}

//								else if ("2".equals(userBean.getState())) {
//									AlertDialog.Builder bd = new AlertDialog.Builder(Login.this);
//									AlertDialog ad = bd.create();
//									ad.setTitle("错误");
//									ad.setMessage("手机已绑定用户,请联系管理员是否绑定新用户");
//									ad.show();
//								} else if ("-2".equals(userBean.getState())) {
//									// -2手机挂失 -1手机失效
//									//uploadData();
//									AlertDialog.Builder bd = new AlertDialog.Builder(Login.this);
//									AlertDialog ad = bd.create();
//									ad.setTitle("错误");
//									ad.setMessage("该手机已挂失，请联系失主");
//									ad.show();
//								} else if ("-1".equals(userBean.getState())) {
//									AlertDialog.Builder bd = new AlertDialog.Builder(Login.this);
//									AlertDialog ad = bd.create();
//									ad.setTitle("错误");
//									ad.setMessage("该手机已失效，请联系管理员");
//									ad.show();
//								} else {
////								if (response.code() == 204) {
////									Toast.makeText(Login.this, "登录失败，机器未注册授权,请联系管理员!",
////											Toast.LENGTH_LONG).show();
////								} else {
//									Toast.makeText(Login.this, "登录失败，机器未注册授权或用户名密码错误",
//											Toast.LENGTH_LONG).show();
////								}
//								}
							}
						} else {
							Toast.makeText(Login.this, "连接服务器失败，请检查网络是否串通",
									Toast.LENGTH_LONG).show();
						}
					}
				});

	}

	public void login_back(View v) { // 标题栏 返回按钮
		this.finish();
		AppContext.getInstance().onTerminate();
		// System.exit(0);
	}

	public void login_regedit(View v) { // 用户注册按钮
	/*
	 * Intent intent = new Intent(); intent.setClass(Login.this,
	 * UsRgtActivity.class); startActivity(intent);
	 */
	}

	public void login_setSystem(View v) { // 设置
		Intent intent = new Intent();
		intent.setClass(Login.this, SystemInitActivity.class);
		startActivity(intent);
	}

	public void login_set_Pw(View v) {// 修改密码
		Intent intent = new Intent();
		intent.setClass(Login.this, UpwdActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		loginBtn.setEnabled(DeviceCtrlTools.isGPSEnable(this));
		if (!DeviceCtrlTools.isGPSEnable(this))
			openGPSConfig();
	}

	@Override
	protected void onPause(){
		super.onPause();
	}



	public void openGPSConfig() {
		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			this.startActivity(intent);

		} catch (ActivityNotFoundException ex) {

			// The Android SDK doc says that the location settings activity
			// may not be found. In that case show the general settings.

			// General settings activity
			intent.setAction(Settings.ACTION_SETTINGS);
			try {
				this.startActivity(intent);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.login_register:	//注册
				startActivity(new Intent(AppContext.getInstance(), LoginRegisterActivity.class));
				break;
			case R.id.forget_password:	//忘记密码
				startActivity(new Intent(AppContext.getInstance(), ChangPasswordActivity.class));
				break;
		}
	}
}
