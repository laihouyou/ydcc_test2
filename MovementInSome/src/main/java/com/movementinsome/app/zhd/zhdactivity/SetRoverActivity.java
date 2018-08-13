package com.movementinsome.app.zhd.zhdactivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;

import com.movementinsome.R;
import com.movementinsome.app.pub.Constant;
import com.movementinsome.app.zhd.zhddapter.MountPointListAdapter;
import com.movementinsome.app.zhd.zhdutil.Util;
import com.movementinsome.caice.base.TitleBaseActivity;
import com.zhd.communication.CommConstant;
import com.zhd.communication.HpcDiffManager;
import com.zhd.communication.object.CorsNode;
import com.zhd.communication.object.DiffServerInfo;
import com.zhd.communication.object.EnumCommResult;
import com.zhd.communication.object.EnumDiffServerType;

import java.util.List;

import static com.movementinsome.R.id.et_zhd_address;

/**
 * 移动站设置页面
 */
public class SetRoverActivity extends TitleBaseActivity {

    /**
     * 服务器类型
     */
    private Spinner spinnerServerType;

    /**
     * VRS网络
     */
    private LinearLayout layoutVRS;
    private EditText etVRSAddress;
    private EditText etVRSPort;
    private EditText etVRSUserName;
    private EditText etVRSPassword;
    private EditText etVRSMountPoint;
    private Button btnGetVRSMountPoint;

    /**
     * 中海达网络
     */
    private LinearLayout layoutZHD;
    private EditText etZHDAddress;
    private EditText etZHDPort;
    private EditText etZHDUserGroup;
    private EditText etZHDWorkGroup;

    /**
     * 设置移动站
     */
    private Button btnSetRover;

    /**
     * 连接等待进度框
     */
    private ProgressDialog progressDialog;

    private SharedPreferences pPrefere; // 定义数据存储
    private SharedPreferences.Editor pEditor; // sharedpreferred数据提交

    @Override
    protected void initTitle() {
        setTitle(getString(R.string.app_activity_set_rover));
        setTopRightVible(false);//不显示
        setTopRightButton("   ",null);
        setTopLeftButton(R.drawable.black_while, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }

    @Override
    protected void initViews() {
        spinnerServerType = (Spinner) findViewById(R.id.spinner_network_type);
        layoutVRS = (LinearLayout) findViewById(R.id.layout_server_vrs);
        etVRSAddress = (EditText) findViewById(R.id.et_vrs_address);
        etVRSPort = (EditText) findViewById(R.id.et_vrs_port);
        etVRSUserName = (EditText) findViewById(R.id.et_vrs_username);
        etVRSPassword = (EditText) findViewById(R.id.et_vrs_password);
        etVRSMountPoint = (EditText) findViewById(R.id.et_vrs_mountpoint);
        btnGetVRSMountPoint = (Button) findViewById(R.id.btn_vrs_get_mountpoint);
        layoutZHD = (LinearLayout) findViewById(R.id.layout_server_zhd);
        etZHDAddress = (EditText) findViewById(et_zhd_address);
        etZHDPort = (EditText) findViewById(R.id.et_zhd_port);
        etZHDUserGroup = (EditText) findViewById(R.id.et_zhd_user_group);
        etZHDWorkGroup = (EditText) findViewById(R.id.et_zhd_work_group);
        btnSetRover = (Button) findViewById(R.id.btn_set_rover);

        // 初始化服务器类型
        String[] networkType = {
                getString(R.string.app_set_rover_net_type_vrs),
                getString(R.string.app_set_rover_net_type_zhd)
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, networkType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerServerType.setAdapter(adapter);
        spinnerServerType.setOnItemSelectedListener(new SpinnerSelectedListener());

        // 获取源节点
        btnGetVRSMountPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMountPoint();
            }
        });

        // 设置移动站
        btnSetRover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRover();
            }
        });

        // 若已连接，则显示连接参数
        if (HpcDiffManager.getInstance().isConnected()) {
            DiffServerInfo diffServerInfo = HpcDiffManager.getInstance().getConnectParams();
            if (diffServerInfo.getServerType() == EnumDiffServerType.CORS) {
                spinnerServerType.setSelection(0);
                layoutVRS.setVisibility(View.VISIBLE);
                layoutZHD.setVisibility(View.GONE);
                etZHDAddress.setText("59.42.52.141");
                etZHDPort.setText("10040");
                etZHDUserGroup.setText("GIS001");
                etZHDWorkGroup.setText("123456");
                etVRSMountPoint.setText(diffServerInfo.getMountPoint());
            } else {
                spinnerServerType.setSelection(1);
                layoutVRS.setVisibility(View.GONE);
                layoutZHD.setVisibility(View.VISIBLE);
                etZHDAddress.setText("59.42.52.141");
                etZHDPort.setText("10040");
                etZHDUserGroup.setText("GIS001");
                etZHDWorkGroup.setText("123456");
            }
            updateUI(true);
        } else {
            updateUI(false);
        }

        // 系统初始参数读取
        if (pPrefere == null) {
            pPrefere = getSharedPreferences(Constant.SPF_NAME, 3);
        }
        pEditor = pPrefere.edit();

        //VRS参考站
        if (!pPrefere.getString(Constant.ETVRSADDRESS,"").equals("")){
            etVRSAddress.setText(pPrefere.getString(Constant.ETVRSADDRESS,""));
        }
        if (!pPrefere.getString(Constant.ETVRSPORT,"").equals("")){
            etVRSPort.setText(pPrefere.getString(Constant.ETVRSPORT,""));
        }
        if (!pPrefere.getString(Constant.ETVRSUSERNAME,"").equals("")){
            etVRSUserName.setText(pPrefere.getString(Constant.ETVRSUSERNAME,""));
        }
        if (!pPrefere.getString(Constant.ETVRSPASSWORD,"").equals("")){
            etVRSPassword.setText(pPrefere.getString(Constant.ETVRSPASSWORD,""));
        }
        if (!pPrefere.getString(Constant.ETVRSMOUNTPOINT,"").equals("")){
            etVRSMountPoint.setText(pPrefere.getString(Constant.ETVRSMOUNTPOINT,""));
        }

        //中海达
        if (!pPrefere.getString(Constant.ETZHDADDRESS,"").equals("")){
            etZHDAddress.setText(pPrefere.getString(Constant.ETZHDADDRESS,""));
        }
        if (!pPrefere.getString(Constant.ETZHDPORT,"").equals("")){
            etZHDPort.setText(pPrefere.getString(Constant.ETZHDPORT,""));
        }
        if (!pPrefere.getString(Constant.ETZHDUSERGROUP,"").equals("")){
            etZHDUserGroup.setText(pPrefere.getString(Constant.ETZHDUSERGROUP,""));
        }
        if (!pPrefere.getString(Constant.ETZHDWORKGROUP,"").equals("")){
            etZHDWorkGroup.setText(pPrefere.getString(Constant.ETZHDWORKGROUP,""));
        }

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set_rover;
    }

    @Override
    protected void  onDestroy() {
        //VRS参考站
        if (!etVRSAddress.getText().equals("")){
            pEditor.putString(Constant.ETVRSADDRESS,etVRSAddress.getText().toString());
        }
        if (!etVRSPort.getText().equals("")){
            pEditor.putString(Constant.ETVRSPORT,etVRSPort.getText().toString());
        }
        if (!etVRSUserName.getText().equals("")){
            pEditor.putString(Constant.ETVRSUSERNAME,etVRSUserName.getText().toString());
        }
        if (!etVRSPassword.getText().equals("")){
            pEditor.putString(Constant.ETVRSPASSWORD,etVRSPassword.getText().toString());
        }
        if (!etVRSMountPoint.getText().equals("")){
            pEditor.putString(Constant.ETVRSMOUNTPOINT,etVRSMountPoint.getText().toString());
        }

        //中海达
        if (!etZHDAddress.getText().equals("")){
            pEditor.putString(Constant.ETZHDADDRESS,etZHDAddress.getText().toString());
        }
        if (!etZHDPort.getText().equals("")){
            pEditor.putString(Constant.ETZHDPORT,etZHDPort.getText().toString());
        }
        if (!etZHDUserGroup.getText().equals("")){
            pEditor.putString(Constant.ETZHDUSERGROUP,etZHDUserGroup.getText().toString());
        }
        if (!etZHDWorkGroup.getText().equals("")){
            pEditor.putString(Constant.ETZHDWORKGROUP,etZHDWorkGroup.getText().toString());
        }

        pEditor.commit();
        super.onDestroy();
    }

    /**
     * 下拉菜单选择事件
     */
    private class SpinnerSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                layoutVRS.setVisibility(View.VISIBLE);
                layoutZHD.setVisibility(View.GONE);
            } else {
                layoutVRS.setVisibility(View.GONE);
                layoutZHD.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private void updateUI(boolean connected) {
        spinnerServerType.setEnabled(!connected);

        etVRSAddress.setEnabled(!connected);
        etVRSPort.setEnabled(!connected);
        etVRSUserName.setEnabled(!connected);
        etVRSPassword.setEnabled(!connected);
        etVRSMountPoint.setEnabled(!connected);
        btnGetVRSMountPoint.setEnabled(!connected);

        etZHDAddress.setEnabled(!connected);
        etZHDPort.setEnabled(!connected);
        etZHDUserGroup.setEnabled(!connected);
        etZHDWorkGroup.setEnabled(!connected);

        if (connected) {
            btnSetRover.setText(getString(R.string.app_set_rover_disconnect));
        } else {
            btnSetRover.setText(getString(R.string.app_set_rover_connect));
        }
    }

    //region 获取源节点

    /**
     * 获取源节点
     */
    private void getMountPoint() {
        progressDialog = Util.showProgressDialog(SetRoverActivity.this,
                getString(R.string.app_set_rover_getting_mountpoint),
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        HpcDiffManager.getInstance().cancelGetSourceNode();
                    }
                });
        new MyGetSourceNodeTask().execute(
                etVRSAddress.getText().toString(), etVRSPort.getText().toString());
    }

    /**
     * 异步获取源节点
     */
    private class MyGetSourceNodeTask extends AsyncTask<String, Integer, List<CorsNode>> {
        @Override
        protected List<CorsNode> doInBackground(String... params) {
            return HpcDiffManager.getInstance().getSourceNode(
                    params[0], Integer.parseInt(params[1]));
        }

        @Override
        protected void onPostExecute(List<CorsNode> result) {
            super.onPostExecute(result);

            if (result == null) {
                Util.showToast(SetRoverActivity.this, R.string.app_set_rover_get_mountpoint_failed);
            } else {
                showMountPointDialog(result);
            }
            progressDialog.dismiss();
        }
    }

    /**
     * 显示源节点信息
     */
    private void showMountPointDialog(List<CorsNode> mountPoints) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.app_set_rover_choose_mountpoint);

        View view = getLayoutInflater().inflate(R.layout.layout_mountpoint_dialog, null);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        if (view != null) {
            ListView lvMountPoint = (ListView) view.findViewById(R.id.lv_mountpoint);
            if (lvMountPoint != null) {
                MountPointListAdapter adapter = new MountPointListAdapter(this, mountPoints);
                adapter.setOnItemClickListener(new MountPointListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, CorsNode corsNode) {
                        etVRSMountPoint.setText(corsNode.MountPoint);
                        dialog.dismiss();
                    }
                });
                lvMountPoint.setAdapter(adapter);
                dialog.show();
            }
        }

    }

    //endregion

    //region 设置移动站

    /**
     * 设置移动站
     */
    private void setRover() {
        if (HpcDiffManager.getInstance().isConnected()) {
            // 断开差分服务器连接
            HpcDiffManager.getInstance().stop();
            updateUI(false);
            Util.showToast(this, R.string.app_set_rover_disconnect_success);
        } else {
            // 连接差分服务器
            progressDialog = Util.showProgressDialog(this,
                    getString(R.string.app_set_rover_checking_network),
                    new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            HpcDiffManager.getInstance().cancelSetRover();
                        }
                    });

            DiffServerInfo diffServerInfo = null;
            if (spinnerServerType.getSelectedItemPosition() == 0) {
                diffServerInfo = new DiffServerInfo(
                        etVRSAddress.getText().toString(),
                        Util.parseInt(etVRSPort.getText().toString()),
                        etVRSUserName.getText().toString(),
                        etVRSPassword.getText().toString(),
                        etVRSMountPoint.getText().toString());
            } else {
                diffServerInfo = new DiffServerInfo(
                        etZHDAddress.getText().toString(),
                        Util.parseInt(etZHDPort.getText().toString()),
                        etZHDUserGroup.getText().toString(),
                        Util.parseInt(etZHDWorkGroup.getText().toString()));
            }

            new SetRoverTask().execute(diffServerInfo);
        }
    }

    /**
     * 异步设置移动站
     */
    private class SetRoverTask extends AsyncTask<Object, Void, EnumCommResult> {
        @Override
        protected EnumCommResult doInBackground(Object... params) {
            return HpcDiffManager.getInstance().setRover(
                    SetRoverActivity.this, (DiffServerInfo) params[0],5, roverHandler);
        }

        @Override
        protected void onPostExecute(EnumCommResult result) {
            super.onPostExecute(result);

            if (result.equals(EnumCommResult.OK)) {       //设置成功
                updateUI(true);
                Util.showToast(SetRoverActivity.this, R.string.app_set_rover_success);
            } else {     //设置失败
                Util.showToast(SetRoverActivity.this,
                        getString(R.string.app_set_rover_failed) + result.name());
            }

            progressDialog.dismiss();
        }
    }

    /**
     * 移动站设置消息处理以及UI更新
     */
    public Handler roverHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommConstant.HANDLER_WHAT_COMM:
                    switch (msg.arg1) {
                        case CommConstant.HANDLER_ARG1_DIFF_MANAGER:
                            switch (msg.arg2) {
                                case CommConstant.HANDLER_ARG2_NETWORK_STATUS:    // 网络是否可用
                                    boolean netWorkStatus = (boolean) msg.obj;
                                    if (netWorkStatus) {
                                        progressDialog.setMessage(getString(R.string.app_set_rover_connecting_server));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_CONNECT_SERVER_STATUS:    // 连接服务器是否成功
                                    boolean connectServerResult = (boolean) msg.obj;
                                    if (connectServerResult) {
                                        progressDialog.setMessage(getString(R.string.app_set_rover_logining));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_LOGIN_STATUS: // 登陆服务器是否成功
                                    boolean loginResult = (boolean) msg.obj;
                                    if (loginResult) {
                                        progressDialog.setMessage(getString(R.string.app_set_rover_setting));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_SET_ROVER_STATUS: // 设置移动站是否成功
                                    boolean setRoverResult = (boolean) msg.obj;
                                    if (setRoverResult) {
                                        progressDialog.setMessage(getString(R.string.app_set_rover_success));
                                    }
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    //endregion
}
