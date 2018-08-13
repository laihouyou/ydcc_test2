package com.movementinsome.caice.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.movementinsome.AppContext;
import com.movementinsome.R;
import com.movementinsome.caice.base.TitleBaseActivity;
import com.movementinsome.caice.util.ApkUpdateUtil;
import com.movementinsome.kernel.updateApk.UpdateApk;
import com.qx.wz.external.eventbus.EventBus;
import com.qx.wz.external.eventbus.Subscribe;
import com.qx.wz.external.eventbus.ThreadMode;


/**
 * 关于界面
 * Created by zzc on 2017/8/14.
 */

public class RegardsActivity extends TitleBaseActivity implements View.OnClickListener{
    ImageView imageView6;
    TextView tvAppName;
    TextView tvVersionNumber;
    TextView tvTermsService;
    TextView tvUpdateData;
    TextView tvCheckver;

    private UpdateApk updateApk;

    @Override
    protected void initTitle() {
        setTitle("关于移动采测");
        setTopLeftButton(R.drawable.black_while, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        setTopRightButton("      ", new OnClickListener() {
            @Override
            public void onClick() {

            }
        });
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);

        imageView6= (ImageView) findViewById(R.id.imageView6);
        tvAppName= (TextView) findViewById(R.id.tvAppName);
        tvVersionNumber= (TextView) findViewById(R.id.tvVersionNumber);
        tvTermsService= (TextView) findViewById(R.id.tvTermsService);
        tvUpdateData= (TextView) findViewById(R.id.tvUpdateData);
        tvCheckver= (TextView) findViewById(R.id.tvCheckver);

        imageView6.setOnClickListener(this);
        tvAppName.setOnClickListener(this);
        tvVersionNumber.setOnClickListener(this);
        tvTermsService.setOnClickListener(this);
        tvUpdateData.setOnClickListener(this);
        tvCheckver.setOnClickListener(this);

        tvVersionNumber.setText("版本号:"+ ApkUpdateUtil.getVersion(this));
        tvAppName.setText("移动采测");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_regards;
    }



    private void updateApk(){
        int extState = updateApk.existsUpdate();
        if (extState > 0) {
            EventBus.getDefault().post(String.valueOf(extState));
        } else {
            EventBus.getDefault().post("0");
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void getToast(String toast){
        if (toast.equals("0")){
            toast("当前已是最新版本");
        }else {
            doNewVersionUpdate(Integer.parseInt(toast));
        }
    }

    public void doNewVersionUpdate(int state) {
        if (state == 1) {
            if (AppContext.getInstance().getAPP_MIN_CODE() != 0) {

                int minCode = AppContext.getInstance().getAPP_MIN_CODE();
                if (AppContext.getInstance().getVersionCode() < minCode) {
                    if (updateApk != null) {
                        updateApk.install();
                        return;
                    }
                }

            }
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("检查到本地有下载完成的最新版应用包，是否立刻安装升级？")
                    .setNegativeButton("确定安装",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    // update();
                                    if (updateApk != null) {
                                        updateApk.install();
                                    }
                                }
                            })
                    .setPositiveButton("删除应用包",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub
                                    if (updateApk != null) {
                                        updateApk.delLocalApk();
                                    }
//                                    mHandler.sendEmptyMessage(12);
                                }
                            }).create().show();

        } else {
            if (AppContext.getInstance().getAPP_MIN_CODE() != 0) {

                int minCode = AppContext.getInstance().getAPP_MIN_CODE();
                if (AppContext.getInstance().getVersionCode() < minCode) {
                    if (updateApk != null) {
                        updateApk.download("");
                        return;
                    }
                }
            }
            String str = updateApk.getUpdateMessage();
            final String title = "下载主程序";
            new AlertDialog.Builder(this)
                    .setTitle("软件更新")
                    .setMessage(str.replace(";", "\n"))
                    // 设置内容
                    .setNegativeButton("立即更新",// 设置确定按钮
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (updateApk != null) {
                                        updateApk.download("");
                                    }
                                }
                            })
                    .setPositiveButton("暂不更新",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
//                                    run();
                                }
                            }).show();// 创建
        }
    }

    @Override
    public void onDetachedFromWindow() {
        EventBus.getDefault().unregister(this);
        super.onDetachedFromWindow();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvTermsService:
                toast("服务条款");
                break;
            case R.id.tvUpdateData:
                toast("更新内容");
                break;
            case R.id.tvCheckver:
                updateApk = new UpdateApk(this, false, AppContext
                        .getInstance().getServerUrl());
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        updateApk();
                    }

                }).start();
                break;
        }

    }
}
