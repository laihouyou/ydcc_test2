package com.movementinsome.app.zhd.zhdactivity;

import android.widget.TextView;

import com.movementinsome.R;
import com.movementinsome.app.zhd.zhdutil.Util;
import com.movementinsome.caice.base.TitleBaseActivity;
import com.zhd.communication.DeviceManager;
import com.zhd.communication.listener.IPositionListener;
import com.zhd.communication.object.EnumGpsQuality;
import com.zhd.communication.object.GpsPoint;

import java.text.DecimalFormat;

public class GPSInfoActivity extends TitleBaseActivity {

    private TextView tvLongitude;
    private TextView tvLatitude;
    private TextView tvAltitude;
    private TextView tvUsedSatellites;
    private TextView tvQuality;
    private TextView tvHdop;
    private TextView tvDiffStationId;
    private TextView tvDiffAge;
    private TextView tvRmsX;
    private TextView tvRmsY;
    private TextView tvRmsH;
    private TextView tvTime;

    private Thread gpsPointThread = null;

    @Override
    protected void initTitle() {
        setTitle(getString(R.string.app_activity_gps_piont));
    }

    @Override
    protected void initViews() {
        tvLongitude = (TextView) findViewById(R.id.tv_longitude);
        tvLatitude = (TextView) findViewById(R.id.tv_latitude);
        tvAltitude = (TextView) findViewById(R.id.tv_altitude);
        tvUsedSatellites = (TextView) findViewById(R.id.tv_used_satellites);
        tvQuality = (TextView) findViewById(R.id.tv_gps_quality);
        tvHdop = (TextView) findViewById(R.id.tv_hdop);
        tvDiffStationId = (TextView) findViewById(R.id.tv_station_id);
        tvDiffAge = (TextView) findViewById(R.id.tv_diff_age);
        tvRmsX = (TextView) findViewById(R.id.tv_rms_x);
        tvRmsY = (TextView) findViewById(R.id.tv_rms_y);
        tvRmsH = (TextView) findViewById(R.id.tv_rms_h);
        tvTime = (TextView) findViewById(R.id.tv_utc_time);

        // 自定义线程、时间间隔通过getPosition获取位置信息
        gpsPointThread = new GPSInfoThread();
        gpsPointThread.start();

        // 通过通讯库注册监听事件实时接收位置信息，使用removePositionListener移除
//        ListenerManager.addPositionListener(positionListener);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_get_gps_info;
    }

    @Override
    protected void onDestroy() {
//        ListenerManager.removePositionListener(positionListener);
        super.onDestroy();
    }

    /**
     * 开启线程获取GPS信息
     */
    private class GPSInfoThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);

                    final GpsPoint gpsPoint = DeviceManager.getInstance().getPosition();
                    if (gpsPoint != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI(gpsPoint);
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }

    IPositionListener positionListener = new IPositionListener() {
        @Override
        public void onReceived(GpsPoint gpsPoint) {
            updateUI(gpsPoint);
        }
    };

    private void updateUI(GpsPoint gpsPoint) {
        if (gpsPoint == null) {
            return;
        }

        // 经度
        tvLongitude.setText(Util.getDegreeString(gpsPoint.L));
        // 纬度
        tvLatitude.setText(Util.getDegreeString(gpsPoint.B));
        // 高程
        tvAltitude.setText(new DecimalFormat("0.00").format(gpsPoint.H) + " m");
        // 卫星数
        tvUsedSatellites.setText(gpsPoint.UsedSats + "");
        // 卫星质量（解类型）
        tvQuality.setText(Util.getGpsQualityString(GPSInfoActivity.this, gpsPoint.Quality));
        // HDOP
        tvHdop.setText(gpsPoint.HDop + "");
        // 基站ID
        tvDiffStationId.setText(gpsPoint.DiffStationID);
        // 差分龄期
        tvDiffAge.setText(gpsPoint.DiffAge + " s");
        // 中误差x
        tvRmsX.setText(new DecimalFormat("0.0000").format(gpsPoint.XRms) + " m");
        // 中误差y
        tvRmsY.setText(new DecimalFormat("0.0000").format(gpsPoint.YRms) + " m");
        // 中误差h
        tvRmsH.setText(new DecimalFormat("0.0000").format(gpsPoint.HRms) + " m");
        // UTC时间
        tvTime.setText(Util.getTimeString(gpsPoint.Utc));

        // 单点定位时，无基站ID和差分龄期
        if (gpsPoint.Quality <= EnumGpsQuality.SINGLE) {
            tvDiffStationId.setText("");
            tvDiffAge.setText("");
        }
    }
}
