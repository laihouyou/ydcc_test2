package com.movementinsome.app.zhd.zhdactivity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;

import com.movementinsome.R;
import com.movementinsome.app.zhd.zhdutil.Util;
import com.movementinsome.caice.base.TitleBaseActivity;
import com.zhd.communication.DeviceManager;


public class ZhdMainActivity extends TitleBaseActivity {

	private Button btnConnectDevice;
	private Button btnDisconnectDevice;
	private Button btnGetDeviceInfo;
	private Button btnRegistDevice;
	private Button btnGetGpsPoint;
	private Button btnSetRover;

	@Override
	protected void initTitle() {
		setTitle(getString(R.string.app_activity_bluetooth_connect));
	}

	@Override
	protected void initViews() {
		setTitle(R.string.app_activity_main);

		btnConnectDevice = (Button) findViewById(R.id.btn_connect_device);
		btnDisconnectDevice = (Button) findViewById(R.id.btn_disconnect_device);
		btnGetDeviceInfo = (Button) findViewById(R.id.btn_get_device_info);
		btnRegistDevice = (Button) findViewById(R.id.btn_regist_device);
		btnGetGpsPoint = (Button) findViewById(R.id.btn_get_gps_point);
		btnSetRover = (Button) findViewById(R.id.btn_set_rover);


		// 设备连接
		btnConnectDevice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ZhdMainActivity.this, BluetoothConnectActivity.class));
			}
		});

		// 断开连接
		btnDisconnectDevice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final ProgressDialog progressDialog = Util.showProgressDialog(ZhdMainActivity.this,
						getString(R.string.app_disconnect_device));

				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						DeviceManager.getInstance().disconnect();
						return null;
					}

					@Override
					protected void onPostExecute(Void aVoid) {
						super.onPostExecute(aVoid);

						btnConnectDevice.setText(getString(R.string.app_connect_device));
						btnConnectDevice.setEnabled(true);
						btnDisconnectDevice.setEnabled(false);
						progressDialog.dismiss();
					}
				}.execute();
			}
		});

		/**
		 * 查看设备信息
		 */
		btnGetDeviceInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ZhdMainActivity.this, DeviceInfoActivity.class));
			}
		});

		/**
		 * 注册设备
		 */
		btnRegistDevice.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ZhdMainActivity.this, RegisteActivity.class));
			}
		});

		/**
		 * 获取GPS
		 */
		btnGetGpsPoint.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ZhdMainActivity.this, GPSInfoActivity.class));
			}
		});

		/**
		 * 移动站设置
		 */
		btnSetRover.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ZhdMainActivity.this, SetRoverActivity.class));
			}
		});
	}

	@Override
	protected int getContentView() {
		return R.layout.zhd_context_activity;
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (DeviceManager.getInstance().isConnected()) {
			btnConnectDevice.setText(getString(R.string.app_connected_device,
					DeviceManager.getInstance().getDeviceId()));
			btnConnectDevice.setEnabled(false);
			btnDisconnectDevice.setEnabled(true);
		} else {
			btnConnectDevice.setText(getString(R.string.app_connect_device));
			btnConnectDevice.setEnabled(true);
			btnDisconnectDevice.setEnabled(false);
		}
	}

//	@Override
//	protected void onDestroy() {
//		// 退出时，保证连接断开
//		if (DeviceManager.getInstance().isConnected()) {
//			DeviceManager.getInstance().disconnect();
//		}
//
//		super.onDestroy();
//	}
}
