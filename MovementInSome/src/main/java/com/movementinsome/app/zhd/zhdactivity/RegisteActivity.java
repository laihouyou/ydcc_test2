package com.movementinsome.app.zhd.zhdactivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.movementinsome.R;
import com.movementinsome.app.zhd.zhdutil.Util;
import com.movementinsome.caice.base.TitleBaseActivity;
import com.zhd.communication.DeviceManager;
import com.zhd.communication.object.EnumCommResult;

/**
 * 注册
 */
public class RegisteActivity extends TitleBaseActivity {

    /**
     * 注册等待进度框
     */
    private ProgressDialog progressDialog;

    @Override
    protected void initTitle() {
        setTitle(getString(R.string.app_activity_regist_device));
    }

    @Override
    protected void initViews() {
        final EditText etRegister = (EditText) findViewById(R.id.et_regist_code);
        Button btnRegister = (Button) findViewById(R.id.btn_regist);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = Util.showProgressDialog(RegisteActivity.this,
                        String.format(getString(R.string.app_registing),
                                DeviceManager.getInstance().getDeviceId()));

                // 注册
                new MyTask().execute(etRegister.getText().toString());
            }
        });
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_device_register;
    }

    /**
     * 异步注册设备
     */
    private class MyTask extends AsyncTask<String, Void, EnumCommResult> {

        @Override
        protected EnumCommResult doInBackground(String... params) {
            return DeviceManager.getInstance().regist(params[0]);
        }

        @Override
        protected void onPostExecute(EnumCommResult result) {
            super.onPostExecute(result);

            if (result == EnumCommResult.OK) {     //注册成功
                Util.showToast(RegisteActivity.this, R.string.app_regist_success);
                finish();
            } else {       //注册失败
                Util.showToast(RegisteActivity.this,
                        getString(R.string.app_regist_failed)
                        + " : " + result.name());
            }
            progressDialog.dismiss();
        }
    }
}
