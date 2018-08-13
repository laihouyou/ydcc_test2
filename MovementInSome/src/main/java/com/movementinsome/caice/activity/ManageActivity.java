package com.movementinsome.caice.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.app.bizcenter.HistoryReportActivity;
import com.movementinsome.app.zhd.zhdactivity.ZhdMainActivity;
import com.movementinsome.kernel.activity.ContainActivity;
import com.movementinsome.kernel.gpswidget.GpsViewActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;


/**
 * Created by LJQ on 2017/5/23.
 */

public class ManageActivity extends ContainActivity implements View.OnClickListener{
    LinearLayout offlineMap;
    LinearLayout manageStatistical;
    LinearLayout manageExport;
    LinearLayout submit_record;
    LinearLayout manageDevice;
    LinearLayout setting;
    LinearLayout mobileStation;
    LinearLayout regards;
    LinearLayout offlineMap2;
    LinearLayout manageBrokenNet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_activity);
        initViews();
    }

    private void initViews() {
        offlineMap= (LinearLayout) findViewById(R.id.offline_map);
        manageStatistical= (LinearLayout) findViewById(R.id.manage_statistical);
        manageExport= (LinearLayout) findViewById(R.id.manage_export);
        submit_record= (LinearLayout) findViewById(R.id.submit_record);
        manageDevice= (LinearLayout) findViewById(R.id.manage_device);
        setting= (LinearLayout) findViewById(R.id.setting);
        mobileStation= (LinearLayout) findViewById(R.id.mobile_station);
        regards= (LinearLayout) findViewById(R.id.regards);
        offlineMap2= (LinearLayout) findViewById(R.id.offline_map2);
        manageBrokenNet= (LinearLayout) findViewById(R.id.manage_broken_net);

        if(AppContext.getInstance().getReportHistory()==null){
            submit_record.setVisibility(View.GONE);
        }else{
            submit_record.setVisibility(View.VISIBLE);
        }

        offlineMap.setOnClickListener(this);
        manageStatistical.setOnClickListener(this);
        manageExport.setOnClickListener(this);
        submit_record.setOnClickListener(this);
        manageDevice.setOnClickListener(this);
        setting.setOnClickListener(this);
        mobileStation.setOnClickListener(this);
        regards.setOnClickListener(this);
        offlineMap2.setOnClickListener(this);
        manageBrokenNet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.offline_map:
                Intent intent = new Intent(this, OfflineActivity.class);
                startActivity(intent);
                break;
            case R.id.manage_statistical:
                Intent intentstatistical = new Intent(ManageActivity.this, StatisticalActivity.class);
                startActivity(intentstatistical);
                break;

            case R.id.manage_export:
                Intent intentexport = new Intent(ManageActivity.this, ProjectExportActivity.class);
                startActivity(intentexport);
                break;

            case R.id.submit_record:    //历史上报查询
                startActivity(new Intent(this,HistoryReportActivity.class));
                break;

            case R.id.manage_device:
                Intent intentdevice = new Intent(ManageActivity.this, ZhdMainActivity.class);
                startActivity(intentdevice);
                break;

            case R.id.setting:  //设置
                Intent intent1 = new Intent(this, SettingActivity.class);
                startActivity(intent1);
                break;

//            case R.id.mobile_station:  //移动站设置
//                Intent intent2 = new Intent(this, SetRoverActivity.class);
//                startActivity(intent2);
//                break;

            case R.id.manage_broken_net:    //个人中心
                Intent intentbroken = new Intent(ManageActivity.this, PersonalCenterActivity.class);
                startActivity(intentbroken);
                break;

            case R.id.regards:  //关于
                Intent intent3 = new Intent(this, RegardsActivity.class);
//                Intent intent3 = new Intent(this, MapViewer.class);
                startActivity(intent3);
                break;

            case R.id.offline_map2:  //卫星图
                Intent intent4 = new Intent(this, GpsViewActivity.class);
                startActivity(intent4);

                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == 2) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
