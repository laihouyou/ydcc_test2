package com.movementinsome.app.zhd.zhdactivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ToggleButton;

import com.movementinsome.R;
import com.movementinsome.app.zhd.zhddapter.DeviceListAdapter;
import com.movementinsome.app.zhd.zhdutil.Util;
import com.movementinsome.caice.base.TitleBaseActivity;
import com.zhd.communication.CommConstant;
import com.zhd.communication.DeviceManager;
import com.zhd.communication.object.EnumDeviceType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class BluetoothConnectActivity extends TitleBaseActivity {

    /**
     * 未打开
     */
    private static final int STATUS_UNOPENED = 0;

    /**
     * 正在打开
     */
    private static final int STATUS_OPENING = 1;

    /**
     * 已打开
     */
    private static final int STATUS_OPENED = 2;

    /**
     * 正在搜索
     */
    private static final int STATUS_SEARCHING = 3;

    /**
     * 正在关闭
     */
    private static final int STATUS_CLOSING = 4;

    /**
     * 正在配对
     */
    private static final int STATUS_BANDING = 5;

    private ToggleButton tbBluetoothStatus;
    private LinearLayout layoutPaired;
    private LinearLayout layoutUnpaired;
    private ListView lvPaired;
    private ListView lvUnpaired;
    private ProgressBar progressBarUnPaired;
    private Button btnFind;

    /**
     * 蓝牙适配器
     */
    private BluetoothAdapter bluetoothAdapter = null;

    /**
     * 蓝牙广播
     */
    private BTBroadcastReceiver receiver = new BTBroadcastReceiver();

    /**
     * 已配对的设备
     */
    private List<BluetoothDevice> pairedDeviceList = null;
    private DeviceListAdapter pairedAdapter = null;

    /**
     * 未配对的设备
     */
    private List<BluetoothDevice> unpairedDeviceList = null;
    private DeviceListAdapter unpairedAdapter = null;

    /**
     * 蓝牙状态
     */
    private int status = STATUS_UNOPENED;

    /**
     * 蓝牙进度框
     */
    private ProgressDialog bluetoothProgressDialog;

    /**
     * 连接进度框
     */
    private ProgressDialog connectProgressDialog;

    @Override
    protected void initTitle() {
        setTitle(getString(R.string.app_activity_bluetooth_unpaired));
    }

    @Override
    protected void initViews() {
        tbBluetoothStatus = (ToggleButton) findViewById(R.id.tb_bluetooth_status);
        lvPaired = (ListView) findViewById(R.id.lv_paired);
        layoutPaired = (LinearLayout) findViewById(R.id.layout_paired);
        lvUnpaired = (ListView) findViewById(R.id.lv_unpaired);
        layoutUnpaired = (LinearLayout) findViewById(R.id.layout_unpaired);
        progressBarUnPaired = (ProgressBar) findViewById(R.id.progressBar_unpaired);
        btnFind = (Button) findViewById(R.id.btn_find);

        pairedDeviceList = new ArrayList<>();
        pairedAdapter = new DeviceListAdapter(this, pairedDeviceList);
        lvPaired.setAdapter(pairedAdapter);

        unpairedDeviceList = new ArrayList<>();
        unpairedAdapter = new DeviceListAdapter(this, unpairedDeviceList);
        lvUnpaired.setAdapter(unpairedAdapter);

        init();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_bluetooth_connect;
    }

    /**
     * 初始化
     */
    private void init() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // 不支持蓝牙
        if (bluetoothAdapter == null) {
            Util.showToast(this, R.string.app_activity_bluetooth_no_support);
            finish();
        }

        // 已连接
        if (DeviceManager.getInstance().isConnected()) {
            finish();
        }

        // 初始化蓝牙连接状态
        boolean isOpened = bluetoothAdapter.isEnabled();
        tbBluetoothStatus.setChecked(isOpened);
        updateUi(isOpened);

        if (isOpened) {
            // 如果已开启，则显示已配对的设备
            getPairedDevices();
            status = STATUS_OPENED;
        } else {
            bluetoothAdapter.enable();
        }

        // 注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED); // 接收“蓝牙开关状态”的广播
        filter.addAction(BluetoothDevice.ACTION_FOUND); // 接收“搜寻设备”的广播
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //接收“搜寻完毕”的广播
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED); // 配对情况发生改变广播
        filter.addAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        registerReceiver(receiver, filter);

        addListener();
    }

    //region 蓝牙管理

    /**
     * 更新ui
     *
     * @param bluetoothOpened 蓝牙开启状态
     */
    private void updateUi(boolean bluetoothOpened) {
        if (bluetoothOpened) {
            layoutPaired.setVisibility(View.VISIBLE);
            btnFind.setVisibility(View.VISIBLE);
        } else {
            layoutPaired.setVisibility(View.GONE);
            layoutUnpaired.setVisibility(View.GONE);
            btnFind.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 添加事件监听
     */
    private void addListener() {
        /**
         * 开关蓝牙
         */
        tbBluetoothStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == STATUS_OPENING || status == STATUS_CLOSING) {
                    return;
                }
                if (bluetoothAdapter.isEnabled()) {
                    // 关闭蓝牙
                    bluetoothAdapter.disable();
                } else {
                    // 打开蓝牙
                    bluetoothAdapter.enable();
                }
            }
        });

        /**
         * 搜索设备
         */
        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == STATUS_SEARCHING) {
                    return;
                }

                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.startDiscovery();
                unpairedDeviceList.clear();
                unpairedAdapter.notifyDataSetChanged();
                status = STATUS_SEARCHING;
                layoutUnpaired.setVisibility(View.VISIBLE);
                btnFind.setVisibility(View.INVISIBLE);
                progressBarUnPaired.setVisibility(View.VISIBLE);
            }
        });

        /**
         * 点击已配对设备的列表项，连接设备
         */
        pairedAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BluetoothDevice device) {
                if (!lvPaired.isEnabled()) {
                    return;
                }

                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }

                if (DeviceManager.getInstance().isConnected() == false) {
                    performConnect(EnumDeviceType.V90, device);
                }
            }
        });

        /**
         * 点击未配对设备进行配对
         */
        unpairedAdapter.setOnItemClickListener(new DeviceListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, BluetoothDevice device) {
                if (!lvUnpaired.isEnabled()) {
                    return;
                }
                if (device.getName().startsWith("Qbox5") || device.getName().startsWith("Qbox6")) {
                    Util.showProgressDialog(
                            BluetoothConnectActivity.this,
                            getString(R.string.app_activity_bluetooth_pairing, device.getName()));
                    performConnect(EnumDeviceType.QBOX5, device);
                } else if (device.getName().compareTo("13150000") >= 0 && device.getName().compareTo("13174999") <= 0) {
                    Util.showProgressDialog(
                            BluetoothConnectActivity.this,
                            getString(R.string.app_activity_bluetooth_pairing, device.getName()));
                    performConnect(EnumDeviceType.QBOX5, device);
                } else {
                    // 取消搜索
                    if (bluetoothAdapter.isDiscovering()) {
                        bluetoothAdapter.cancelDiscovery();
                        hideBluetoothProgressDialog();
                    }
                    // 尝试配对
                    if (device != null) {
                        lvPaired.setEnabled(false);
                        lvUnpaired.setEnabled(false);
                        createBond(device);
                        enableUi(false);
                        status = STATUS_BANDING;
                        Util.showProgressDialog(
                                BluetoothConnectActivity.this,
                                getString(R.string.app_activity_bluetooth_pairing, device.getName()));
                    }
                }

            }
        });
    }

    /**
     * 控制UI是否可用
     *
     * @param enabled
     */
    private void enableUi(boolean enabled) {
        tbBluetoothStatus.setEnabled(enabled);
        lvPaired.setEnabled(enabled);
        lvUnpaired.setEnabled(enabled);
        btnFind.setEnabled(enabled);
    }

    /**
     * 获取并显示已配对设备
     */
    private void getPairedDevices() {
        final Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        pairedDeviceList.clear();
        for (BluetoothDevice device : pairedDevices) {
            if (filterDevice(device)) {
                pairedDeviceList.add(device);
            }
        }
        pairedAdapter.notifyDataSetChanged();
        if (pairedDeviceList.size() < 1) {
            layoutPaired.setVisibility(View.GONE);
        } else {
            layoutPaired.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据期望显示的设备类型筛选设备
     *
     * @param device 蓝牙设备
     * @return 蓝牙设备名称
     */
    private boolean filterDevice(BluetoothDevice device) {
        String deviceName = device.getName();
        // 筛选Qbox 8设备（设备号13200000~13249999）
        if (deviceName == null) {
            return false;
        } else if (deviceName.compareTo("10000000") >= 0
                && deviceName.compareTo("99999999") <= 0) {
            return true;
        } else if (deviceName.startsWith("Qbox5")
                || deviceName.startsWith("Qbox6")) {
            return true;
        }
        return false;
    }

    /**
     * 接收系统发送的蓝牙设备搜索广播。
     */
    private class BTBroadcastReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 搜索到一个设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (!unpairedDeviceList.contains(device)) {
                        if (filterDevice(device)) {
                            unpairedDeviceList.add(device);
                            unpairedAdapter.notifyDataSetChanged();
                        }
                    }
                }

                // 搜索结束
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                progressBarUnPaired.setVisibility(View.GONE);
                btnFind.setVisibility(View.VISIBLE);
                if (status == STATUS_SEARCHING && unpairedDeviceList.isEmpty()) {
                    Util.showToast(BluetoothConnectActivity.this, R.string.app_activity_bluetooth_discovery_none);
                }
                status = STATUS_OPENED;

                // 蓝牙打开关闭状态
            } else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        tbBluetoothStatus.setChecked(false);
                        status = STATUS_UNOPENED;
                        updateUi(false);
                        hideBluetoothProgressDialog();
                        enableUi(true);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        status = STATUS_CLOSING;
                        bluetoothProgressDialog = Util.showProgressDialog(
                                BluetoothConnectActivity.this, getString(R.string.app_activity_bluetooth_closing));
                        enableUi(false);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        tbBluetoothStatus.setChecked(true);
                        status = STATUS_OPENED;
                        updateUi(true);
                        getPairedDevices();
                        hideBluetoothProgressDialog();
                        enableUi(true);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        status = STATUS_OPENING;
                        bluetoothProgressDialog = Util.showProgressDialog(
                                BluetoothConnectActivity.this, getString(R.string.app_activity_bluetooth_opening));
                        enableUi(false);
                        break;
                }
            }

            // 配对信息的监听
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (state) {
                    case BluetoothDevice.BOND_BONDED:// 与一台设备配对成功
                        hideBluetoothProgressDialog();
                        performConnect(EnumDeviceType.QBOX8, device);
                        break;

                    case BluetoothDevice.BOND_NONE:// 移除与一台设备的配对关系
                        if (status == STATUS_BANDING) { // 正在配对，配对失败
                            Util.showToast(BluetoothConnectActivity.this, getString(
                                    R.string.app_activity_bluetooth_bond_failed, device.getName()));
                        }
                        status = STATUS_OPENED;
                        hideBluetoothProgressDialog();
                        enableUi(true);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 创建配对
     *
     * @param device 蓝牙设备
     * @return 配对结果
     */
    public static boolean createBond(BluetoothDevice device) {
        Boolean result = false;
        if (device != null) {
            try {
                Method method = device.getClass().getMethod("createBond");
                result = (Boolean) method.invoke(device);
            } catch (SecurityException | NoSuchMethodException | IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 隐藏进度框
     */
    private void hideBluetoothProgressDialog() {
        progressBarUnPaired.setVisibility(View.GONE);
        if (bluetoothProgressDialog != null) {
            bluetoothProgressDialog.dismiss();
        }
    }

    //endregion

    //region 设备连接

    /**
     * 执行连接
     *
     * @param zt     设备类型
     * @param device 蓝牙设备
     */
    private void performConnect(EnumDeviceType zt, @NonNull BluetoothDevice device) {
        String msg = String.format(getString(R.string.app_connectting_to_decice), device.getName());

        connectProgressDialog = Util.showHorizontalProgressDialog(this, msg,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        DeviceManager.getInstance().cancelConnect();
                    }
                }, 10);

        // 执行连接进程
        new MyConnectTask().execute(zt, device);
    }

    /**
     * 异步连接设备
     */
    private class MyConnectTask extends AsyncTask<Object, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Object... params) {
            boolean result = DeviceManager.getInstance().connect(BluetoothConnectActivity.this,
                    (EnumDeviceType) params[0], (BluetoothDevice) params[1], commHandler);
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (result) {
                //region 注册设备自动重连的监听事件
//                    // 当设备关机或距离过远时会导致蓝牙断开，断开后通讯库会尝试自动重连，多次尝试失败后会自动断开
//                    ListenerManager.addDeviceReconnectListener(new IReconnectListener() {
//                        @Override
//                        public void onStart() {
//                            // 开始重连
//                        }
//
//                        @Override
//                        public void onResult(boolean b) {
//                            // 重连完成（成功或失败），失败将自动断开设备连接
//                        }
//                    });
                // endregion

                Util.showToast(BluetoothConnectActivity.this, R.string.app_connect_success);
                finish();
            } else {
                Util.showToast(BluetoothConnectActivity.this, R.string.app_connect_failed);
            }
            connectProgressDialog.dismiss();
        }
    }

    /**
     * 连接消息处理以及UI更新
     */
    public Handler commHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CommConstant.HANDLER_WHAT_COMM:
                    switch (msg.arg1) {
                        case CommConstant.HANDLER_ARG1_DEVICE_MANAGER:
                            switch (msg.arg2) {
                                case CommConstant.HANDLER_ARG2_BLUETOOTH_CONNECT_STATUS:    // 蓝牙连接结果
                                    boolean bluetoothConnectResult = (boolean) msg.obj;
                                    if (bluetoothConnectResult) {   // 蓝牙连接成功，开始检测设备
                                        connectProgressDialog.setProgress(1);
                                        connectProgressDialog.setMessage(
                                                BluetoothConnectActivity.this.getString(R.string.app_getting_version));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_READ_FIRM_VERSION_STATUS:    // 读取固件版本结果
                                    boolean readFirmVersionResult = (boolean) msg.obj;
                                    if (readFirmVersionResult) {
                                        connectProgressDialog.setProgress(2);
                                        connectProgressDialog.setMessage(BluetoothConnectActivity.this.getString(R.string.app_getting_setting));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_READ_CONFIGURATION_STATUS: // 读取配置信息的结果
                                    boolean readConfigResult = (boolean) msg.obj;
                                    if (readConfigResult) { // 读取成功
                                        if (DeviceManager.getInstance().getDeviceType() != EnumDeviceType.QBOX5) {
                                            connectProgressDialog.setProgress(3);
                                            connectProgressDialog.setMessage(BluetoothConnectActivity.this.getString(R.string.app_connectting_oem));
                                        }
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_OPEN_OEM_STATUS: // 开启主板输出的结果
                                    boolean sendMhResult = (boolean) msg.obj;
                                    if (sendMhResult) { // 读取成功
                                        connectProgressDialog.setProgress(4);
                                        connectProgressDialog.setMessage(BluetoothConnectActivity.this.getString(R.string.app_getting_oeminfo));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_READ_OEM_STATUS: // 读取主板信息的结果
                                    boolean readOemInfoResult = (boolean) msg.obj;
                                    if (readOemInfoResult) { // 读取成功
                                        connectProgressDialog.setProgress(5);
                                        connectProgressDialog.setMessage(BluetoothConnectActivity.this.getString(R.string.app_getting_support_zhd_command));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_READ_COMMAND_LIST_STATUS: // 读取命令列表的结果
                                    boolean readCommandListResult = (boolean) msg.obj;
                                    if (readCommandListResult) { // 读取成功
                                        connectProgressDialog.setProgress(6);
                                        connectProgressDialog.setMessage(BluetoothConnectActivity.this.getString(R.string.app_getting_datalink));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_READ_DATA_LINK_STATUS: // 读取数据链信息的结果
                                    boolean readDataLinkResult = (boolean) msg.obj;
                                    if (readDataLinkResult) { // 读取成功
                                        connectProgressDialog.setProgress(7);
                                        connectProgressDialog.setMessage(BluetoothConnectActivity.this.getString(R.string.app_getting_update));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_GET_UPDATE_STATUS: // 获取更新信息的结果
                                    boolean getUpdateResult = (boolean) msg.obj;
                                    if (getUpdateResult) { // 读取成功
                                        connectProgressDialog.setProgress(8);
                                        connectProgressDialog.setMessage(BluetoothConnectActivity.this.getString(R.string.app_init_device));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_INIT_DEVICE_STATUS: // 初始化设备的结果
                                    boolean initDeviceResult = (boolean) msg.obj;
                                    if (initDeviceResult) { // 读取成功
                                        connectProgressDialog.setProgress(9);
                                        connectProgressDialog.setMessage(BluetoothConnectActivity.this.getString(R.string.app_getting_endday));
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_READ_REGIST_DATE_STATUS: // 读取注册日期的结果
                                    boolean readRegistDateResult = (boolean) msg.obj;
                                    if (readRegistDateResult) { // 读取成功
                                        connectProgressDialog.setProgress(10);
                                    }
                                    break;
                                case CommConstant.HANDLER_ARG2_TEST_RESULT: // 检测设备的结果
                                    boolean testResult = (boolean) msg.obj;
                                    if (testResult) { // 检测通过
                                        connectProgressDialog.setProgress(10);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothAdapter != null) {
            if (receiver != null) {
                unregisterReceiver(receiver);
            }
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
        }
    }
}
