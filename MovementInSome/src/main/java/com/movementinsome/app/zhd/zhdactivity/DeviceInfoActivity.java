package com.movementinsome.app.zhd.zhdactivity;

import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.zhd.zhdutil.Util;
import com.movementinsome.caice.base.TitleBaseActivity;
import com.zhd.communication.DeviceManager;

public class DeviceInfoActivity extends TitleBaseActivity {

    private TextView tvDeviceType;
    private TextView tvDeviceID;
    private TextView tvDeviceWorkPattern;
    private TextView tvDataVersion;
    private TextView tvFirmwareVersion;
    private TextView tvDeviceExpirationDate;

    @Override
    protected void initTitle() {
        setTitle(getString(R.string.app_activity_device_info));
    }

    @Override
    protected void initViews() {
        tvDeviceType = (TextView) findViewById(R.id.tv_device_type);
        tvDeviceID = (TextView) findViewById(R.id.tv_device_id);
        tvDeviceWorkPattern = (TextView) findViewById(R.id.tv_work_mode);
        tvDataVersion = (TextView) findViewById(R.id.tv_data_version);
        tvFirmwareVersion = (TextView) findViewById(R.id.tv_firmware_version);
        tvDeviceExpirationDate = (TextView) findViewById(R.id.tv_expire_date);

        // 设备类型
        tvDeviceType.setText(DeviceManager.getInstance().getDeviceType().name());
        // 设备ID
        tvDeviceID.setText(DeviceManager.getInstance().getDeviceId());
        // 工作模式
        tvDeviceWorkPattern.setText(DeviceManager.getInstance().getWorkMode().name());
        // 数据版本
        tvDataVersion.setText(DeviceManager.getInstance().getOemDataVersion());
        // 固件版本
        tvFirmwareVersion.setText(DeviceManager.getInstance().getFirmwareVersion() + "");
        // 过期时间
        tvDeviceExpirationDate.setText(Util.getDateString(DeviceManager.getInstance().getExpiredDate()));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_get_device_info;
    }
}
